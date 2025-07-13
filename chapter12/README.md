### 第十二章　灵活的构建

Maven为了支持构建的灵活性，内置了两大特性，即属性和Profile。

#### 12.1 Maven属性
六类Maven属性分别为：
1. <strong><span style="color:red;">内置属性</span></strong>：主要有两个常用内置属性——${basedir}表示项目根目录，即包含pom.xml文件的目录；${version}表示项目版本。
2. <strong><span style="color:red;">POM属性：用户可以使用该类属性引用POM文件中对应元素的值。</span></strong>例如${project.artifactId}就对应了＜project＞＜artifactId＞元素的值，常用的POM属性包括：
* ${project.build.sourceDirectory}：项目的主源码目录，默认为src/main/java/。 
* ${project.build.testSourceDirectory}：项目的测试源码目录，默认为src/test/java/。
* ${project.build.directory}：项目构建输出目录，默认为target/。
* ${project.outputDirectory}：项目主代码编译输出目录，默认为target/classes/。
* ${project.testOutputDirectory}：项目测试代码编译输出目录，默认为target/test-classes/。
* ${project.groupId}：项目的groupId。
* ${project.artifactId}：项目的artifactId。
* ${project.version}：项目的version，与${version}等价。
* ${project.build.finalName}：项目打包输出文件的名称，默认为${project.artifactId}-${project.version}。
3. <strong><span style="color:red;">自定义属性：用户可以在POM的＜properties＞元素下自定义Maven属性。</span></strong>
4. <strong><span style="color:red;">Settings属性：与POM属性同理，用户使用以settings.开头的属性引用settings.xml文件中XML元素的值</span></strong>，如常用的${settings.localRepository}指向用户本地仓库的地址。
5. <strong><span style="color:red;">Java系统属性：所有Java系统属性都可以使用Maven属性引用</span></strong>，例如${user.home}指向了用户目录。用户可以使用mvn help:system查看所有的Java系统属性。
6. <strong><span style="color:red;">环境变量属性：所有环境变量都可以使用以env.开头的Maven属性引用。</span></strong>例如${env.JAVA_HOME}指代了JAVA_HOME环境变量的值。用户可以使用mvn help:system查看所有的环境变量。

Maven属性能让我们在POM中方便地引用项目环境和构建环境的各种十分有用的值，这是创建灵活构建的基础。

#### 12.2 Maven Profile
profile能够在构建的时候修改POM的一个子集，或者添加额外的配置元素。

如果用户希望某个profile默认一直处于激活状态，就可以配置settings.xml文件的activeProfiles元素，表示其配置的profile对于所有项目都处于激活状态。9.5节就曾经用到这种方式默认激活了一个关于仓库配置的profile。