### 第2章 Maven的安装和配置

<strong><span style="color:red;">在安装Maven之前，首先要确认你已经正确安装了JDK。</span></strong>Maven 3.9+需要JDK 8及以上版本。

#### 2.1　在Windows上安装Maven

##### 2.1.1　检查JDK安装
```
C:\Users\chenanguo＞ echo %JAVA_HOME%
C:\Users\chenanguo＞ java -version
```
上述命令首先检查环境变量JAVA_HOME是否指向了正确的JDK目录，接着尝试运行java命令。如果Windows无法执行java命令，或者无法找到JAVA_HOME环境变量，就需要检查Java是否安装了，或者环境变量是否设置正确。

##### 2.1.2　下载Maven

请访问Maven的下载页面：http://maven.apache.org/download.cgi
，其中包含针对不同平台的各种版本的Maven下载文件。对于首次接触Maven的读者来说，推荐使用最新版Maven 3.9.10，因此需要下载apache-maven-3.9.10-bin.zip。

##### 2.1.3　本地安装

将安装文件解压到指定的目录：D:\bin\apache-maven-3.9.10，接着需要设置环境变量，将Maven安装配置到操作系统环境中。

打开系统属性面板（在桌面上右击“我的电脑”→“属性”），单击高级系统设置，再单击环境变量，在系统变量中新建一个变量，变量名为M2_HOME，变量值为Maven的安装目录`D:\bin\apache-maven-3.9.10`。单击“确定”按钮，接着在系统变量中找到一个名为Path的变量，在变量值的末尾加上`%M2_HOME%\bin;`。注意：多个值之间需要有分号隔开，然后单击“确定”按钮。至此，环境变量设置完成。

值得注意的是<strong><span style="color:red;">Path环境变量</span></strong>。当我们在cmd中输入命令时，Windows首先会在当前目录中寻找可执行文件或脚本，如果没有找到，Windows会接着遍历环境变量Path中定义的路径。由于将%M2_HOME%\bin添加到了Path中，而这里%M2_HOME%实际上是引用了前面定义的另一个变量，其值是Maven的安装目录。因此，Windows会在执行命令时搜索目录D:\bin\apache-maven-3.9.10\bin，而mvn执行脚本的位置就是这里。

现在打开一个新的cmd窗口（这里强调新的窗口是因为新的环境变量配置需要新的cmd窗口才能生效），运行如下命令检查Maven的安装情况：
```
C:\Users\chenanguo＞ echo %M2_HOME%
C:\Users\chenanguo＞ mvn -v
```

#### 2.2　在基于UNIX的系统（包括Linux、Mac OS以及FreeBSD等）上安装Maven

首先，与在Windows上安装Maven一样，需要检查JAVA_HOME环境变量以及Java命令。命令如下：
```
chenanguo@root:～$ echo $JAVA_HOME
chenanguo@root:～$ java -version
```

接着到 http://maven.apache.org/download.cgi 下载Maven安装文件，如apache-maven-3.9.10-bin.zip，把Maven安装文件下载到~/bin目录下，然后解压到本地目录：
```
chenanguo@root:～$ mkdir bin
chenanguo@root:～$ cd bin
chenanguo@root:bin$ unzip apache-maven-3.9.10-bin.zip
```
现在已经创建好了一个Maven安装目录apache-maven-3.9.10。

接下来，需要设置M2_HOME环境变量指向apache-maven-3.9.10，并且把Maven安装目录下的bin文件夹添加到系统环境变量PATH中：
```
chenanguo@root:bin$ export M2_HOME=/home/chenanguo/bin/apache-maven-3.9.10
chenanguo@root:bin$ export PATH=$PATH:$M2_HOME/bin
```
一般来说，需要将这两行命令加入到系统的登录shell脚本中去，以Ubuntu 8.10为例，编辑～/.bashrc文件，添加这两行命令。这样，每次启动一个终端，这些配置就能自动执行。

至此，安装完成。可以运行以下命令检查Maven安装：
```
chenanguo@root:bin$ echo $M2_HOME
chenanguo@root:bin$ mvn -v
```

#### 2.3　安装目录分析

安装目录下的conf：该目录包含了一个非常重要的文件settings.xml。直接修改该文件，就能在机器上全局地定制Maven的行为。一般情况下，<strong><span style="color:red;">我们更偏向于复制settings.xml文件至～/.m2/目录下（～表示用户目录），然后修改该文件，在用户范围定制Maven的行为。</span></strong>后面将会多次提到settings.xml，并逐步分析其中的各个元素。

我们先运行一条简单的命令：mvn help:system。该命令会打印出所有的Java系统属性和环境变量。

在用户目录下可以发现.m2文件夹。默认情况下，该文件夹下放置了<strong><span style="color:red;">Maven本地仓库.m2/repository。所有的Maven构件都被存储到该仓库中，以方便重用。</span></strong>可以到～/.m2/repository/org/apache/maven/plugins/maven-help-plugins/目录下找到刚才下载的maven-help-plugin的pom文件和jar文件。

由于Maven仓库是通过简单文件系统透明地展示给Maven用户的，有些时候可以绕过Maven直接查看或修改仓库文件，在遇到疑难问题时，这往往十分有用。