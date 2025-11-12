# R_Blog - 全栈博客系统

> **一个热爱Java的Young People的分享**  
> *—— R（我的名字缩写）*

## 📸 项目截图(专属于00后的童年风格,希望你能够喜欢)

![博客界面1](https://i.ibb.co/0jL1W2C/1.png)

![博客界面2](https://i.ibb.co/6Yy0V3k/2.png)



\## 🛠 技术栈



\### 后端技术栈

\- \*\*核心框架\*\*: Spring Boot 2.x + Spring MVC + Spring IOC

\- \*\*数据持久层\*\*: MyBatis + MyBatis PageHelper + MySQL + Redis

\- \*\*安全认证\*\*: JWT + Spring Interceptor (当前版本暂未启用)

\- \*\*文件存储\*\*: 阿里云OSS + MultipartFile



\### 前端技术栈  

\- \*\*核心框架\*\*: Vue 3 + Composition API + Vue Router 4

\- \*\*状态管理\*\*: Pinia

\- \*\*构建工具\*\*: Vite + npm

\- \*\*UI框架\*\*: Element Plus + CSS3 + Flexbox/Grid

\- \*\*HTTP客户端\*\*: Axios + 拦截器

\- \*\*开发语言\*\*: JavaScript ES6+



\## 🚀 完整部署指南



\### 环境要求

\- \*\*Java 17+\*\* (`java -version`)

\- \*\*MySQL 8.0+\*\* (`mysql --version`)

\- \*\*Node.js 16+\*\* (`node -v`)

\- \*\*Maven 3.6+\*\* (`mvn -version`)



\### 1. 克隆项目

```bash

git clone https://github.com/wrydhzdq/R\_blog.git

cd R\_blog

2\. 数据库初始化

sql

-- 登录MySQL

mysql -u root -p



-- 创建数据库

CREATE DATABASE r\_blog;



-- 执行初始化脚本

USE r\_blog;

SOURCE database/init.sql;



-- 验证数据

SELECT \* FROM admin\_user;  -- 应该看到admin用户

3\. 后端配置与启动

bash

cd backend

编辑 src/main/resources/application.yml：



yaml

spring:

&nbsp; datasource:

&nbsp;   password: "你的MySQL密码"  # ⚠️ 必须修改这里！



\# JWT配置（当前版本暂未启用，可忽略）

jwt:

&nbsp; secret: "任意字符串"  # 可随意填写，暂不影响使用



\# Redis配置（如不需要可删除整个redis配置节）

redis:

&nbsp; host: localhost

&nbsp; port: 6379

启动后端：



bash

mvn spring-boot:run

成功标志：看到 Started RBlogApplication in X.XXX seconds



4\. 前端配置与启动

bash

cd frontend

npm install

npm run dev

成功标志：看到 Local: http://localhost:5173



5\. 访问系统

前端地址: http://localhost:5173



测试账号: admin / 123456



🔐 认证说明

当前版本暂时关闭了JWT认证，所有接口都可以直接访问，无需登录验证。



🔧 常见问题解决

数据库连接失败

text

java.sql.SQLException: Access denied for user 'root'@'localhost'

解决：检查 application.yml 中的数据库密码是否正确



端口被占用

后端端口8080被占用：



修改 application.yml 中的 server.port



或停止占用8080端口的程序



前端端口5173被占用：



会自动使用其他端口，查看控制台输出



前端依赖安装失败

bash

cd frontend

rm -rf node\_modules package-lock.json

npm install

Redis连接失败

如不需要Redis，直接删除 application.yml 中的整个 redis: 配置节



✅ 部署成功验证

看到以下界面说明部署成功：



✅ 博客首页正常显示



✅ 能点击登录按钮（即使JWT未启用）



✅ 能看到测试博客文章



✅ 各功能按钮正常点击



💡 项目说明

这是一个学生作品，记录了从零开始搭建全栈项目的过程。技术栈选型贴近企业实际需求，适合学习参考。



如果这个项目对你有帮助，请给个⭐鼓励一下！

