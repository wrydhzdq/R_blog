package com.r_blog.service;

import com.github.pagehelper.PageInfo;
import com.r_blog.dto.request.BlogQueryDTO;
import com.r_blog.entity.Blog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogService {
//    //根据id查博客
//    public Blog getBlogById(Long id);
//
//    // 根据条件动态查询博客列表
//    public PageInfo<Blog> getBlogList(BlogQueryDTO queryDTO);
//
//    // 插入博客
//    public boolean saveBlog(Blog blog);
//
//    //更新博客
//    public boolean updateBlog(Blog blog);
//
//    //删除博客
//    public boolean deleteBlog(Long id);
//
//    // 查询所有博客列表
//    public PageInfo<Blog> selectAll();
//
//    // 更新博客状态
//    public boolean updateStatus(Long id, Integer status);
//
//    // 增加阅读量
//    public boolean updateBlogViews(Long id);
//
//    // 根据分类ID查询
//    public PageInfo<Blog> selectByCategoryId(Long categoryId);



        /**
         * 根据ID获取博客详情
         * @param id 博客ID
         * @return 博客实体对象，如果不存在返回null
         */
        Blog getBlogById(Long id);

        /**
         * 根据条件分页查询博客列表
         * @param queryDTO 查询条件（标题、分类、状态等）
         * @return 分页结果，包含博客列表和分页信息
         */
        PageInfo<Blog> getBlogList(BlogQueryDTO queryDTO);

        /**
         * 新增博客
         * @param blog 博客实体对象
         * @return 是否新增成功
         */
        boolean saveBlog(Blog blog);

        /**
         * 更新博客信息
         * @param blog 博客实体对象（必须包含ID）
         * @return 是否更新成功
         */
        boolean updateBlog(Blog blog);

        /**
         * 逻辑删除博客（将is_deleted设为1）
         * @param id 博客ID
         * @return 是否删除成功
         */
        boolean deleteBlog(Long id);

        /**
         * 获取所有博客列表（分页）
         * @param page 页码，默认第1页
         * @param pageSize 每页大小，默认10条
         * @return 分页结果，包含所有未删除的博客
         */
        PageInfo<Blog> getAllBlogs(Integer page, Integer pageSize);

        /**
         * 更新博客状态（草稿/发布）
         * @param id 博客ID
         * @param status 状态：0-草稿，1-发布
         * @return 是否更新成功
         */
        boolean updateBlogStatus(Long id, Integer status);

        /**
         * 增加博客阅读量（每次+1）
         * @param id 博客ID
         * @return 是否增加成功
         */
        boolean increaseViewCount(Long id);

        /**
        *批量同步阅读量到数据库
        */
        public void syncViewCountToDatabase();

        /**
         * 根据分类ID查询该分类下的博客
         * @param categoryId 分类ID
         * @param page 页码
         * @param pageSize 每页大小
         * @return 分页结果，包含该分类下的博客列表
         */
        PageInfo<Blog> getBlogsByCategoryId(Long categoryId, Integer page, Integer pageSize);

        /**
         * 根据分类ID添加图片
         * @param id ID
         * @param coverUrl 地址
         */
        public boolean updateCover(Long id,String coverUrl);
    }

