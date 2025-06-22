### 第四章 背景案例

从本章开始，引入一个较为真实的背景案例：简单的账户注册服务，以演示Maven使用的真实场景。

这里<strong><span style="color:red;">基于包名划分模块</span></strong>，这也是在Java中比较常见的做法。

账户注册服务的模块划分，如下：
1. com.chenanguo.mvnbook.account.service：系统的核心，它封装了所有下层细节，对外暴露简单的接口。
2. com.chenanguo.mvnbook.account.web：顾名思义，该模块包含所有与web相关的内容，包括可能的JSP、Servlet、web.xml等，它直接依赖于com.chenanguo.mvnbook.account.service模块，使用其提供的服务。
3. com.chenanguo.mvnbook.account.persist：处理账户信息的持久化，包括增、删、改、查等，根据实现，可以基于数据库或者文件。
4. com.chenanguo.mvnbook.account.captcha：处理验证码的key生成、图片生成以及验证等，这里需要第三方的类库来帮助实现这些功能。
5. com.chenanguo.mvnbook.account.email：处理邮件服务的配置、激活邮件的编写和发送等工作。