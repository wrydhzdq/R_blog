package com.r_blog.controller;


import com.r_blog.dto.request.BlogQueryDTO;
import com.r_blog.entity.Blog;
import com.r_blog.entity.Result;
import com.r_blog.service.BlogService;
import com.r_blog.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;


/*
* 使用 @RequestParam（显式） 一般少于3个url参数用这个 多余就默认
*
* 路径参数 → 必须用 @PathVariable
*
* @RequestBody将请求体中的JSON转化为对象
*
* 请求体数据必须用 @RequestBody，URL参数可以自动绑定！
* */


/**
 * 博客管理控制器
 * 处理博客的增删改查相关请求
 */
@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private FileService fileService;

    /**
     * 根据ID获取博客详情
     * @param id 博客ID
     * @return 博客详情
     */
    @GetMapping("/{id}")
    public Result<Blog> getBlogById(@PathVariable Long id) {
        Blog blog = blogService.getBlogById(id);
        if (blog != null) {
            return Result.success(blog);
        }
        return Result.error("博客不存在");
    }

    /**
     * 根据条件分页查询博客列表
     * @param queryDTO 查询条件
     * @return 分页的博客列表
     */
    @GetMapping("/list")
    public Result<PageInfo<Blog>> getBlogList(BlogQueryDTO queryDTO) {
        PageInfo<Blog> pageInfo = blogService.getBlogList(queryDTO);
        return Result.success(pageInfo);
    }

    /**
     * 新增博客
     * @param blog 博客实体
     * @return 操作结果
     */
    @PostMapping
    public Result<String> saveBlog(@RequestBody Blog blog) {
        boolean result = blogService.saveBlog(blog);
        return result ? Result.success("新增成功") : Result.error("新增失败");
    }

    /**
     * 更新博客信息
     * @param blog 博客实体
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateBlog(@RequestBody Blog blog) {
        boolean result = blogService.updateBlog(blog);
        return result ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 逻辑删除博客
     * @param id 博客ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteBlog(@PathVariable Long id) {
        boolean result = blogService.deleteBlog(id);
        return result ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 获取所有博客列表（分页）
     * @param page 页码
     * @param pageSize 每页大小
     * @return 分页的博客列表
     */
    @GetMapping("/all")
    public Result<PageInfo<Blog>> getAllBlogs(
            //分页操作建议加上这个,有个默认值可以用
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Blog> pageInfo = blogService.getAllBlogs(page, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 更新博客状态
     * @param id 博客ID
     * @param status 状态：0-草稿，1-发布
     * @return 操作结果
     * /status 是资源操作端点     /status = "我要操作这个博客的状态"
     */
    @PutMapping("/{id}/status")
    public Result<String> updateBlogStatus(
            //路径参数 → 必须用 @PathVariable
            @PathVariable Long id,
            @RequestParam Integer status) {
        boolean result = blogService.updateBlogStatus(id, status);
        return result ? Result.success("状态更新成功") : Result.error("状态更新失败");
    }

    /**
     * 增加博客阅读量
     * @param id 博客ID
     * @return 操作结果
     */
    @PutMapping("/{id}/view")
    public Result<String> increaseViewCount(@PathVariable Long id) {
        boolean result = blogService.increaseViewCount(id);
        return result ? Result.success("阅读量增加成功") : Result.error("阅读量增加失败");
    }

    /**
     * 根据分类ID查询博客
     * @param categoryId 分类ID
     * @param page 页码
     * @param pageSize 每页大小
     * @return 分页的博客列表
     */
    @GetMapping("/category/{categoryId}")
    public Result<PageInfo<Blog>> getBlogsByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Blog> pageInfo = blogService.getBlogsByCategoryId(categoryId, page, pageSize);
        return Result.success(pageInfo);
    }


    /**
     * 根据分类ID添加图片
     * @param id ID
     * @param file 图片
     * @return 图片地址
     */
    @PostMapping("/{id}/cover")
    public Result<String> uploadBlogCover(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        String coverUrl = fileService.uploadImage(file);
        if (coverUrl != null && blogService.updateCover(id, coverUrl)) {
            return Result.success("封面图上传成功", coverUrl);
        }
        return Result.error("封面图上传失败");
    }

    @DeleteMapping("/{id}/cover")  // 建议改为cover，更符合RESTful
    public Result<String> deleteCover(
            @PathVariable("id") Long id,
            @RequestParam("fileUrl") String fileUrl) {
        if (fileService.deleteFile(fileUrl)) {
            blogService.updateCover(id, null);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}




//@RestController
//@RequestMapping("/api/blog")
//public class BlogController {
//    @Autowired
//    private BlogService blogService;
//
//    /**
//     * 根据ID获取博客详情
//     * @param id 博客ID
//     * @return 博客详情
//     */
//    @GetMapping("/{id}")
//    public Result<Blog> getBlogById(@PathVariable Long id) {
//        Blog blog = blogService.getBlogById(id);
//        if(blog != null) {
//            return Result.success(blog);
//        }
//        return Result.error("博客不存在!");
//    }
//
//    /**
//     * 根据条件分页查询博客列表
//     * @param queryDTO 查询条件
//     * @return 分页的博客列表
//     */
//    @GetMapping("/list")
//    public Result<PageInfo<Blog>> getBlogByPage(BlogQueryDTO queryDTO) {
//
//        PageInfo<Blog> pageInfo = blogService.getBlogList(queryDTO);
//        return Result.success(pageInfo);
//    }
//
//    /**
//     * 新增博客
//     * @param blog 博客实体
//     * @return 操作结果
//     */
//    @PostMapping()
//    public Result<String> saveBlog(Blog blog) {
//        boolean result = blogService.saveBlog(blog);
//        return result ? Result.success("新增成功") : Result.error("新增失败");
//    }
//
//    /**
//     * 更新博客信息
//     * @param blog 博客实体
//     * @return 操作结果
//     */
//    @PutMapping()
//    public Result<String> updateBlog(Blog blog) {
//        boolean result = blogService.updateBlog(blog);
//        return result ? Result.success("新增成功") : Result.error("新增失败");
//    }
//
//    /**
//     * 逻辑删除博客
//     * @param id 博客ID
//     * @return 操作结果
//     */
//    @DeleteMapping("/{id}")
//    public Result<String> deleteBlog(@PathVariable Long id) {
//        boolean result = blogService.deleteBlog(id);
//        return result ? Result.success("删除成功") : Result.error("删除失败");
//    }
//
//    /**
//     * 获取所有博客列表（分页）
//     * @param page 页码
//     * @param pageSize 每页大小
//     * @return 分页的博客列表
//     */
//    @GetMapping("/all")
//    public Result<PageInfo<Blog>> getBlogListByPage(Integer page, Integer pageSize) {
//        PageInfo<Blog> pageInfo = blogService.getAllBlogs(page, pageSize);
//        return Result.success(pageInfo);
//    }
//
//    ///status 是 资源操作端点     /status = "我要操作这个博客的状态"
//    @PutMapping("/{id}")
//    public Result<String>




