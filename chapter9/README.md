
### 下载Nexus Repository Community Edition并启动：

> 安装详见：https://help.sonatype.com/en/install-nexus-repository.html

1. 打开页面：https://www.sonatype.com/products/nexus-community-edition-download ，填写“Get Started for Free”信息后，点击“Download”，进入选择相应操作系统的界面，点击进行下载。
2. 我选择操作系统：macOS x86 (Intel)后，下载的安装文件：nexus-3.81.1-01-mac-x86_64.tar.gz
3. tar xvzf nexus-3.81.1-01-mac-x86_64.tar.gz
4. cd nexus-3.81.1-01-mac-x86_64/nexus-3.81.1-01/bin
5. 启动Nexus Repository：`./nexus start`
6. 打开Nexus网址：http://127.0.0.1:8081/
7. 登录后，在页面：http://127.0.0.1:8081/#admin/repository/repositories ，查看仓库和新建仓库。