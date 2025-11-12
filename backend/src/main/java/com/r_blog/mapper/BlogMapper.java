package com.r_blog.mapper;

import com.r_blog.dto.request.BlogQueryDTO;
import com.r_blog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper {

    //批量增加博客阅读量
    int updateViewCount(@Param("id") Long id,@Param("viewCount") Long viewCount);

    /**
     * 根据ID查询博客详情
     * @param id 博客ID
     * @return 博客实体
     */
    Blog selectById(Long id);

    /**
     * 查询所有未删除的博客（按创建时间倒序）
     * @return 博客列表
     */
    List<Blog> selectAll();

    /**
     * 根据条件动态查询博客列表
     * @param queryDTO 查询条件（标题、分类、状态等）
     * @return 博客列表
     */
    List<Blog> selectByCondition(BlogQueryDTO queryDTO);

    /**
     * 插入新博客
     * @param blog 博客实体
     * @return 影响的行数
     */
    int insert(Blog blog);

    /**
     * 更新博客信息
     * @param blog 博客实体
     * @return 影响的行数
     */
    int update(Blog blog);

    /**
     * 逻辑删除博客（设置is_deleted=1）
     * @param id 博客ID
     * @return 影响的行数
     */
    int deleteById(Long id);

    /**
     * 更新博客状态
     * @param id 博客ID
     * @param status 状态：0-草稿，1-发布
     * @return 影响的行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);


    /**
     * 根据分类ID查询该分类下的博客
     * @param categoryId 分类ID
     * @return 博客列表
     */
    List<Blog> selectByCategoryId(Long categoryId);


    /**
     * 根据分类ID添加图片
     * @param id ID
     * @param coverUrl 地址
     */
    int updateCover(@Param("id") Long id,@Param("coverUrl") String coverUrl);
}