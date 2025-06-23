### 第六章 仓库

#### 6.1 何为Maven仓库
坐标和依赖是任何一个构件在Maven世界中的逻辑表示方式；而<strong><span style="color:red;">构件的物理表示方式是文件，Maven通过仓库来统一管理这些文件。</span></strong>

在Maven世界中，任何一个依赖、插件或者项目构建的输出，都可以称为<strong><span style="color:red;">构件</span></strong>。任何一个构件都有一组坐标唯一标识。

Maven可以在某个位置统一存储所有Maven项目共享的构件，这个统一的位置就是<strong><span style="color:red;">仓库</span></strong>。实际的Maven项目将不再各自存储其依赖文件，它们只需要声明这些依赖的坐标，在需要的时候（例如，编译项目的时候需要将依赖加入到classpath中），Maven会自动根据坐标找到仓库中的构件，并使用它们。

#### 6.2 仓库的布局
任何一个构件都有其唯一的坐标，根据这个坐标可以定义其在仓库中的唯一存储路径，这便是Maven的仓库布局方式。

Maven仓库是基于简单文件系统存储的，因此，当遇到一些与仓库相关的问题时，可以很方便地查找相关文件，方便定位问题。

#### 6.3 仓库的分类

<strong><span style="color:red;">对于Maven来说，仓库只分为两类：本地仓库和远程仓库。</span></strong>当Maven根据坐标寻找构件的时候，它首先会查看本地仓库，如果本地仓库存在此构件，则直接使用；如果本地仓库不存在此构件，或者需要查看是否有更新的构件版本，Maven就会去远程仓库查找，发现需要的构件之后，下载到本地仓库再使用。<strong><span style="color:red;">如果本地仓库和远程仓库都没有需要的构件，Maven就会报错。</span></strong>

特殊的远程仓库：
1. <strong><span style="color:red;">中央仓库</span></strong>是Maven核心自带的远程仓库，它包含了绝大部分开源的构件。在默认配置下，当本地仓库没有Maven需要的构件的时候，它就会尝试从中央仓库下载。私服是另一种特殊的远程仓库，为了节省带宽和时间，应该在局域网内架设一个私有的仓库服务器，用其代理所有外部的远程仓库。内部的项目还能部署到私服上供其他项目使用。
2. <strong><span style="color:red;">私服</span></strong>是另一种特殊的远程仓库，为了节省带宽和时间，应该在局域网内架设一个私有的仓库服务器，用其代理所有外部的远程仓库。内部的项目还能部署到私服上供其他项目使用。
3. 其他公开的远程仓库。比如：阿里云Maven镜像仓库（ https://maven.aliyun.com/repository/public ）。

Maven仓库的分类见图。
<img src="https://res.weread.qq.com/wrepub/epub_602555_39">

##### 6.3.1 本地仓库

一般来说，在Maven项目目录下，没有诸如lib/这样用来存放依赖文件的目录。当Maven在执行编译或测试时，如果需要使用依赖文件，它总是基于坐标使用本地仓库的依赖文件。默认情况下，不管是在Windows还是Linux上，每个用户在自己的用户目录下都有一个路径名为.m2/repository/的仓库目录。

默认情况下，～/.m2/settings.xml文件是不存在的，用户需要从Maven安装目录复制$M2_HOME/conf/settings.xml文件再进行编辑。

<strong><span style="color:red;">一个构件只有在本地仓库中之后，才能由其他Maven项目使用</span></strong>，那么构件如何进入到本地仓库中呢？最常见的是依赖Maven从远程仓库下载到本地仓库中。<strong><span style="color:red;">还有一种常见的情况是，将本地项目的构件安装到Maven仓库中。例如，本地有两个项目A和B，两者都无法从远程仓库获得，而同时A又依赖于B，为了能构建A，B就必须首先得以构建并安装到本地仓库中。</span></strong>

install插件的install目标将项目的构建输出文件安装到本地仓库。

##### 6.3.2 远程仓库
当Maven无法从本地仓库找到需要的构件的时候，就会从远程仓库下载构件至本地仓库。

对于Maven来说，每个用户只有一个本地仓库，但可以配置访问很多远程仓库。

##### 6.3.3 中央仓库
中央仓库是一个默认的远程仓库，Maven的安装文件自带了中央仓库的配置。

##### 6.3.4 私服
私服是一种特殊的远程仓库，它是架设在局域网内的仓库服务，私服代理广域网上的远程仓库，供局域网内的Maven用户使用。当Maven需要下载构件的时候，它从私服请求，如果私服上不存在该构件，则从外部的远程仓库下载，缓存在私服上之后，再为Maven的下载请求提供服务。

此外，一些无法从外部仓库下载到的构件也能从本地上传到私服上供大家使用。

#### 6.4 远程仓库的配置
配置POM使用aliyun Maven仓库：
```xml
<project>
    ……
    <repositories>
        <repository>
            <id>aliyun</id>
            <name>aliyun Repository</name>
            <url>https://maven.aliyun.com/repository/public</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <layout>default</layout>
        </repository>
    </repositories>
    ……
</project>
```
在repositories元素下，可以使用repository子元素声明一个或者多个远程仓库。

<strong><span style="color:red;">任何一个仓库声明的id必须是唯一的，尤其需要注意的是，Maven自带的中央仓库使用的id为central</span></strong>，如果其他的仓库声明也使用该id，就会覆盖中央仓库的配置。该配置中的url值指向了仓库的地址。

<strong><span style="color:red;">该例配置中的releases和snapshots元素比较重要，它们用来控制Maven对于发布版构件和快照版构件的下载。</span></strong>这里需要注意的是enabled子元素，该例中releases的enabled值为true，表示开启aliyun仓库的发布版本下载支持，而snapshots的enabled值为false，表示关闭aliyun仓库的快照版本的下载支持。因此，根据该配置，Maven只会从aliyun仓库下载发布版的构件，而不会下载快照版的构件。

该例中的layout元素值default表示仓库的布局是Maven 2及Maven 3的默认布局，而不是Maven 1的布局。

对于releases和snapshots来说，除了enabled，它们还包含另外两个子元素updatePolicy和checksumPolicy：
```xml
<snapshots>
    <enabled>true</enabled>
    <updatePolicy>daily</updatePolicy>
    <checksumPolicy>warn</checksumPolicy>
</snapshots>
```
<strong><span style="color:red;">元素updatePolicy用来配置Maven从远程仓库检查更新的频率</span></strong>，默认的值是daily，表示Maven每天检查一次。其他可用的值包括：never—从不检查更新；always—每次构建都检查更新；interval:X—每隔X分钟检查一次更新（X为任意整数）。

<strong><span style="color:red;">元素checksumPolicy用来配置Maven检查检验和文件的策略。</span></strong>当构件被部署到Maven仓库中时，会同时部署对应的校验和文件。在下载构件的时候，Maven会验证校验和文件，如果校验和验证失败，怎么办？当checksumPolicy的值为默认的warn时，Maven会在执行构建时输出警告信息，其他可用的值包括：fail—Maven遇到校验和错误就让构建失败；ignore—使Maven完全忽略校验和错误。

##### 6.4.1 远程仓库的认证
大部分远程仓库无须认证就可以访问，但有时候出于安全方面的考虑，我们需要提供认证信息才能访问一些远程仓库。

配置认证信息和配置仓库信息不同，仓库信息可以直接配置在POM文件中，但是<strong><span style="color:red;">认证信息必须配置在settings.xml文件中。</span></strong>这是因为POM往往是被提交到代码仓库中供所有成员访问的，而settings.xml一般只放在本机。因此，在settings.xml中配置认证信息更为安全。

在settings.xml中配置仓库认证信息：
```
<settings>
    ……
    <servers>
        <server>
            <id>my-proj</id>
            <username>repo-user</username>
            <password>repo-pwd</password>
        </server>
    </servers>
    ……
</settings>
```
Maven使用settings.xml文件中的servers元素及其server子元素配置仓库认证信息。

<strong><span style="color:red;">这里的关键是id元素，settings.xml中server元素的id必须与POM中需要认证的repository元素的id完全一致。换句话说，正是这个id将认证信息与仓库配置联系在了一起。</span></strong>

##### 6.4.2 部署至远程仓库
<strong><span style="color:red;">私服的一大作用是部署第三方构件，包括组织内部生成的构件以及一些无法从外部仓库直接获取的构件。</span></strong>无论是日常开发中生成的构件，还是正式版本发布的构件，都需要部署到仓库中，供其他团队成员使用。

在POM中配置构件部署地址：
```xml
<project>
    ……
    <distributionManagement>
        <repository>
            <id>proj-releases</id>
            <name>Proj Release Repository</name>
            <url>http://192.168.1.100/content/repositories/proj-releases</url>
        </repository>
        <snapshotRepository>
            <id>proj-snapshots</id>
            <name>Proj Snapshot Repository</name>
            <url>http://192.168.1.100/content/repositories/proj-snapshots</url>
        </snapshotRepository>
    </distributionManagement>
    ……
</project>
```
distributionManagement包含repository和snapshotRepository子元素，前者表示发布版本构件的仓库，后者表示快照版本的仓库。

这两个元素下都需要配置id、name和url，id为该远程仓库的唯一标识，name是为了方便人阅读，关键的url表示该仓库的地址。

往远程仓库部署构件的时候，往往需要认证。配置认证的方式已在第6.4.1节中详细阐述，简而言之，就是需要在settings.xml中创建一个server元素，其id与仓库的id匹配，并配置正确的认证信息。不论从远程仓库下载构件，还是部署构件至远程仓库，当需要认证的时候，配置的方式是一样的。

<strong><span style="color:red;">配置正确后，在命令行运行`mvn clean deploy`，Maven就会将项目构建输出的构件部署到配置对应的远程仓库，如果项目当前的版本是快照版本，则部署到快照版本仓库地址，否则就部署到发布版本仓库地址。</span></strong>

#### 6.5 快照版本
在Maven的世界中，任何一个项目或者构件都必须有自己的版本。版本的值可能是1.0.0、1.3-alpha-4、2.0、2.1-SNAPSHOT或者2.1-20091214.221414-13。其中，1.0.0、1.3-alpha-4和2.0是稳定的发布版本，<strong><span style="color:red;">而2.1-SNAPSHOT和2.1-20091214.221414-13是不稳定的快照版本。</span></strong>

Maven为什么要区分发布版和快照版呢？

小张在开发模块A的2.1版本，该版本还未正式发布，与模块A一同开发的还有模块B，它由小张的同事小陈开发，B的功能依赖于A。

小张只需要将模块A的版本设定为2.1-SNAPSHOT，然后发布到私服中，在发布的过程中，Maven会自动为构件打上时间戳。比如2.1-20091214.221414-13就表示2009年12月14日22点14分14秒的第13次快照。有了该时间戳，Maven就能随时找到仓库中该构件2.1-SNAPSHOT版本最新的文件。这时，小陈配置对于模块A的2.1-SNAPSHOT版本的依赖，当小陈构建模块B的时候，Maven会自动从仓库中检查模块A的2.1-SNAPSHOT的最新构件，当发现有更新时便进行下载。<strong><span style="color:red;">默认情况下，Maven每天检查一次更新（由仓库配置的updatePolicy控制，见第6.4节），用户也可以使用命令行-U参数强制让Maven检查更新，如`mvn clean install -U`。</span></strong>

基于快照版本机制，小张在构建模块A成功之后将构件部署至仓库，而小陈可以完全不用考虑模块A的构建，并且他能确保随时得到模块A的最新可用的快照构件，而这一切都不需要额外的手工操作。

当项目经过完善的测试后需要发布的时候，就应该将快照版本更改为发布版本。例如，将2.1-SNAPSHOT更改为2.1，表示该版本已经稳定，且只对应了唯一的构件。相比之下，2.1-SNAPSHOT往往对应了大量的带有不同时间戳的构件，这也决定了其不稳定性。

快照版本只应该在组织内部的项目或模块间依赖使用，因为这时，组织对于这些快照版本的依赖具有完全的理解及控制权。项目不应该依赖于任何组织外部的快照版本依赖，由于快照版本的不稳定性，这样的依赖会造成潜在的危险。

#### 6.6 从仓库解析依赖的机制

当本地仓库没有依赖构件的时候，Maven会自动从远程仓库下载；当依赖版本为快照版本的时候，Maven会自动找到最新的快照。

<strong><span style="color:red;">这背后的依赖解析机制可以概括如下：</span></strong>
1. 当依赖的范围是system的时候，Maven直接从本地文件系统解析构件。
2. 根据依赖坐标计算仓库路径后，尝试直接从本地仓库寻找构件，如果发现相应构件，则解析成功。
3. 在本地仓库不存在相应构件的情况下，如果依赖的版本是显式的发布版本构件，如1.2、2.1-beta-1等，则遍历所有的远程仓库，发现后，下载并解析使用。
4. 如果依赖的版本是RELEASE或者LATEST，则基于更新策略读取所有远程仓库的元数据groupId/artifactId/maven-metadata.xml，将其与本地仓库的对应元数据合并后，计算出RELEASE或者LATEST真实的值，然后基于这个真实的值检查本地和远程仓库，如步骤2）和3）。
5. 如果依赖的版本是SNAPSHOT，则基于更新策略读取所有远程仓库的元数据groupId/artifactId/version/maven-metadata.xml，将其与本地仓库的对应元数据合并后，得到最新快照版本的值，然后基于该值检查本地仓库，或者从远程仓库下载。
6. 如果最后解析得到的构件版本是时间戳格式的快照，如1.4.1-20091104.121450-121，则复制其时间戳格式的文件至非时间戳格式，如SNAPSHOT，并使用该非时间戳格式的构件。

当依赖的版本不明晰的时候，如RELEASE、LATEST和SNAPSHOT，Maven就需要基于更新远程仓库的更新策略来检查更新。

在依赖声明中使用LATEST和RELEASE是不推荐的做法，因为Maven随时都可能解析到不同的构件。

#### 6.7 镜像
如果仓库X可以提供仓库Y存储的所有内容，那么就可以认为X是Y的一个镜像。换句话说，任何一个可以从仓库Y获得的构件，都能够从它的镜像中获取。

编辑settings.xml，配置中央仓库镜像：
```
<settings>
    ……
    <mirrors>
        <mirror>
            <id>maven.net.cn</id>
            <name>one of the central mirrors in China</name>
            <url>http://maven.net.cn/content/groups/public/</url>
            <mirrorOf>central</mirrorOf>
        </mirror>
    </mirrors>
    ……
</settings>
```
<mirrorOf>的值为central，表示该配置为中央仓库的镜像，任何对于中央仓库的请求都会转至该镜像，用户也可以使用同样的方法配置其他仓库的镜像。

关于镜像的一个更为常见的用法是结合私服。由于私服可以代理任何外部的公共仓库（包括中央仓库），因此，<strong><span style="color:red;">对于组织内部的Maven用户来说，使用一个私服地址就等于使用了所有需要的外部仓库</span></strong>，这可以将配置集中到私服，从而简化Maven本身的配置。在这种情况下，任何需要的构件都可以从私服获得，私服就是所有仓库的镜像。这样可以配置：`<mirrorOf>*</mirrorOf>`。

#### 6.8 仓库搜索服务

可以使用仓库搜索服务来根据关键字得到Maven坐标：https://mvnrepository.com/
