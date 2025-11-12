package com.r_blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.r_blog.dto.request.BlogQueryDTO;
import com.r_blog.entity.Blog;
import com.r_blog.mapper.BlogMapper;
import com.r_blog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//@Slf4j
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 缓存键前缀
    private static final String BLOG_CACHE_KEY = "blog:";
    // 缓存过期时间（1小时）
    private static final long CACHE_EXPIRE_HOURS = 1;

    // 阅读量缓存键
    private static final String BLOG_VIEWS_KEY = "blog_views:";

    //增加阅读量
    @Override
    public boolean increaseViewCount(Long id) {
        try {
            // 使用Redis的原子操作增加阅读量
            String viewKey = BLOG_VIEWS_KEY + id;
            redisTemplate.opsForValue().increment(viewKey);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    ;

    //批量同步阅读量到数据库
    @Override
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    public void syncViewCountToDatabase() {
        try {
            Set<String> keys = redisTemplate.keys(BLOG_VIEWS_KEY + "*");
            if (keys != null && !keys.isEmpty()) {
                for (String key : keys) {
                    //Long.parseLong转为Long类型 key.substring(BLOG_VIEWS_KEY.length())从这个长度开始截取
                    Long blogId = Long.parseLong(key.substring(BLOG_VIEWS_KEY.length()));
                    //Long viewCount = (Long) redisTemplate.opsForValue().get(key);
                    Object value = redisTemplate.opsForValue().get(key);
                    Long viewCount = value != null ? ((Number) value).longValue() : 0L;

                    if (viewCount != null && viewCount > 0) {
                        //更新数据库
                        blogMapper.updateViewCount(blogId, viewCount);
                        //删除缓存
                        redisTemplate.delete(BLOG_VIEWS_KEY + blogId);
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    ;


    /**
     * 根据ID获取博客详情
     * 包含参数验证和异常处理
     */
    @Override
    public Blog getBlogById(Long id) {
        String cacheKey = BLOG_CACHE_KEY + id;

        try {
            // 1. 先查缓存 这行代码可能抛出异常,这就使得外层try能查出是不是redis不可用
            Blog cacheBlog = (Blog) redisTemplate.opsForValue().get(cacheKey);
            if (cacheBlog != null) {
                //log.info(" 缓存命中 - 博客ID: {}", id);
                return cacheBlog;
            }

            // 2. 缓存未命中，查数据库
            //log.info(" 缓存未命中，查询数据库 - 博客ID: {}", id);
            Blog blog = blogMapper.selectById(id);

            if (blog != null) {
                // 3. 异步存入缓存（不阻塞主流程） 能到这说明redis可用,内存啥的问题
                try {
                    redisTemplate.opsForValue().set(
                            cacheKey,
                            blog,
                            CACHE_EXPIRE_HOURS,
                            TimeUnit.HOURS
                    );
                    //log.info(" 博客数据已缓存 - 博客ID: {}", id);
                } catch (Exception e) {
                    //log.warn(" 缓存写入失败，但不影响主流程: {}", e.getMessage());
                }
            } else {
                //log.warn(" 博客不存在 - 博客ID: {}", id);
            }

            return blog;

        } catch (Exception e) {
            //log.error(" 获取博客异常，降级到直接查数据库 - 博客ID: {}, 错误: {}", id, e.getMessage());
            // 降级策略：直接查数据库
            return blogMapper.selectById(id);
        }
    }


    /**
     * //     * 根据条件分页查询博客列表
     * //     * 使用PageHelper进行分页，支持动态查询条件
     * //
     */
    // 博客列表缓存键
    private static final String BLOG_LIST_KEY = "blog_list:";

    @Override
    public PageInfo<Blog> getBlogList(BlogQueryDTO queryDTO) {
        // 生成缓存键（基于查询条件）
        String cacheKey = generateListCacheKey(queryDTO);

        try {
            // 先查缓存
            PageInfo<Blog> cachedPage = (PageInfo<Blog>) redisTemplate.opsForValue().get(cacheKey);
            if (cachedPage != null) {
                //log.info("博客列表缓存命中 - 条件: {}", cacheKey);
                return cachedPage;
            }

            // 缓存未命中，查数据库
            //log.info(" 博客列表缓存未命中，查询数据库 - 条件: {}", cacheKey);

            // 使用PageHelper分页
            PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
            List<Blog> blogList = blogMapper.selectByCondition(queryDTO);
            PageInfo<Blog> pageInfo = new PageInfo<>(blogList);

            // 缓存结果（5分钟过期）
            redisTemplate.opsForValue().set(cacheKey, pageInfo, 5, TimeUnit.MINUTES);

            return pageInfo;

        } catch (Exception e) {
            //log.error("博客列表缓存异常，降级到数据库: {}", e.getMessage());
            PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
            List<Blog> blogList = blogMapper.selectByCondition(queryDTO);
            return new PageInfo<>(blogList);
        }
    }

    // 生成列表缓存键
    private String generateListCacheKey(BlogQueryDTO queryDTO) {
        return BLOG_LIST_KEY +
                "page_" + queryDTO.getPage() +
                "_size_" + queryDTO.getPageSize() +
                (queryDTO.getTitle() != null ? "_title_" + queryDTO.getTitle() : "") +
                (queryDTO.getCategoryId() != null ? "_cat_" + queryDTO.getCategoryId() : "") +
                (queryDTO.getStatus() != null ? "_status_" + queryDTO.getStatus() : "");
    }


//    /**
//     * 根据条件分页查询博客列表
//     * 使用PageHelper进行分页，支持动态查询条件
//     */
//    @Override
//    public PageInfo<Blog> getBlogList(BlogQueryDTO queryDTO) {
//        if (queryDTO == null) {
//            queryDTO = new BlogQueryDTO(); // 提供默认查询条件
//        }
//        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
//        List<Blog> blogList = blogMapper.selectByCondition(queryDTO);
//        return new PageInfo<>(blogList);
//    }

    /**
     * 新增博客
     * 设置默认值：阅读量0、未删除、发布状态
     * 使用数据库自动生成创建时间和更新时间
     */
    @Override
    public boolean saveBlog(Blog blog) {
        if (blog == null) {
            //log.warn("新增博客参数为空");
            return false;
        }
        try {
            // 设置默认值
            blog.setViewCount(0L);
            blog.setIsDeleted(0);
            blog.setStatus(1); // 默认发布状态

            int result = blogMapper.insert(blog);
            return result > 0;
        } catch (Exception e) {
            //log.error("新增博客失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 更新博客信息
     * 只能更新未删除的博客，自动更新update_time字段
     */
    @Override
    public boolean updateBlog(Blog blog) {
        if (blog == null || blog.getId() == null) {
            //log.warn("更新博客参数错误");
            return false;
        }
        try {
            int result = blogMapper.update(blog);
            return result > 0;
        } catch (Exception e) {
            //log.error("更新博客失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 逻辑删除博客
     * 将is_deleted字段设为1，并非物理删除
     */
    @Override
    public boolean deleteBlog(Long id) {
        if (id == null || id <= 0) {
            //log.warn("删除博客ID参数错误: {}", id);
            return false;
        }
        try {
            int result = blogMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            //log.error("删除博客失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取所有博客列表（分页）
     * 查询所有未删除的博客，按创建时间倒序排列
     */
    @Override
    public PageInfo<Blog> getAllBlogs(Integer page, Integer pageSize) {
        // 提供默认分页参数
        int currentPage = page != null ? page : 1;
        int size = pageSize != null ? pageSize : 10;

        PageHelper.startPage(currentPage, size);
        List<Blog> blogList = blogMapper.selectAll();
        return new PageInfo<>(blogList);
    }

    /**
     * 更新博客状态
     * 用于在草稿和发布状态之间切换
     */
    @Override
    public boolean updateBlogStatus(Long id, Integer status) {
        if (id == null || status == null) {
            //log.warn("更新状态参数错误: id={}, status={}", id, status);
            return false;
        }
        try {
            int result = blogMapper.updateStatus(id, status);
            return result > 0;
        } catch (Exception e) {
            //log.error("更新博客状态失败: {}", e.getMessage());
            return false;
        }
    }


    /**
     * 根据分类ID查询博客
     * 查询指定分类下的所有已发布且未删除的博客
     */
    @Override
    public PageInfo<Blog> getBlogsByCategoryId(Long categoryId, Integer page, Integer pageSize) {
        if (categoryId == null || categoryId <= 0) {
            //log.warn("分类ID参数错误: {}", categoryId);
            return new PageInfo<>(Collections.emptyList());
        }

        int currentPage = page != null ? page : 1;
        int size = pageSize != null ? pageSize : 10;

        PageHelper.startPage(currentPage, size);
        List<Blog> blogList = blogMapper.selectByCategoryId(categoryId);
        return new PageInfo<>(blogList);
    }

    /**
     * 根据分类ID添加图片
     * @param id ID
     * @param coverUrl 地址
     */
    @Override
    public boolean updateCover(Long id, String coverUrl) {
        try {
            return blogMapper.updateCover(id, coverUrl) > 0;
        } catch (Exception e) {
            System.out.println("更新封面图失败: " + e.getMessage());
            return false;
        }
    }
}