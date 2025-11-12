package com.r_blog;

import com.r_blog.dto.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RBlogApplicationTests {

    @Test
    void contextLoads() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");  // 测试 setter
        System.out.println(request.getUsername());
    }

}
