### 第七章 生命周期和插件

#### 7.1 何为生命周期

Maven的生命周期是抽象的，其实际行为都由插件来完成，如package阶段的任务可能就会由maven-jar-plugin完成。生命周期和插件两者协同工作，密不可分。

Maven的生命周期就是为了对所有的构建过程进行抽象和统一。这个生命周期包含了项目的清理、初始化、编译、测试、打包、集成测试、验证、部署和站点生成等几乎所有构建步骤。也就是说，几乎所有项目的构建，都能映射到这样一个生命周期上。

<strong><span style="color:red;">Maven的生命周期是抽象的，这意味着生命周期本身不做任何实际的工作，在Maven的设计中，实际的任务（如编译源代码）都交由插件来完成。</span></strong>

<strong><span style="color:red;">生命周期抽象了构建的各个步骤，定义了它们的次序，但没有提供具体实现。每个构建步骤都可以绑定一个或者多个插件行为，而且Maven为大多数构建步骤编写并绑定了默认插件。</span></strong>例如，针对编译的插件有maven-compiler-plugin，针对测试的插件有maven-surefire-plugin，实际上编译是由maven-compiler-plugin完成的，而测试是由maven-surefire-plugin完成的。

#### 7.2 生命周期详解

##### 7.2.1 三套生命周期
<strong><span style="color:red;">Maven拥有三套相互独立的生命周期，它们分别为clean、default和site。</span></strong>clean生命周期的目的是清理项目，default生命周期的目的是构建项目，而site生命周期的目的是建立项目站点。

<strong><span style="color:red;">每个生命周期包含一些阶段（phase），这些阶段是有顺序的，并且后面的阶段依赖于前面的阶段，用户和Maven最直接的交互方式就是调用这些生命周期阶段。</span></strong>以clean生命周期为例，它包含的阶段有pre-clean、clean和post-clean。当用户调用pre-clean的时候，只有pre-clean阶段得以执行；当用户调用clean的时候，pre-clean和clean阶段会得以顺序执行；当用户调用post-clean的时候，pre-clean、clean和post-clean会得以顺序执行。

这些阶段的详细信息，可以参阅官方的解释：https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference 。

<strong><span style="color:red;">较之于生命周期阶段的前后依赖关系，三套生命周期本身是相互独立的，用户可以仅仅调用clean生命周期的某个阶段，或者仅仅调用default生命周期的某个阶段，而不会对其他生命周期产生任何影响。</span></strong>

##### 7.2.2 clean生命周期
clean生命周期的目的是清理项目，它包含三个阶段：
1. pre-clean 执行一些清理前需要完成的工作。
2. clean 清理上一次构建生成的文件。
3. post-clean 执行一些清理后需要完成的工作。

##### 7.2.3 default生命周期
default生命周期定义了真正构建时所需要执行的所有步骤，它是所有生命周期中最核心的部分，

这里笔者只对重要的阶段进行解释：
1. process-sources 处理项目主资源文件。一般来说，是对src/main/resources目录的内容进行变量替换等工作后，复制到项目输出的主classpath目录中。
2. compile 编译项目的主源码。一般来说，是编译src/main/java目录下的Java文件至项目输出的主classpath目录中。
3. process-test-sources 处理项目测试资源文件。一般来说，是对src/test/resources目录的内容进行变量替换等工作后，复制到项目输出的测试classpath目录中。
4. test-compile 编译项目的测试代码。一般来说，是编译src/test/java目录下的Java文件至项目输出的测试classpath目录中。
5. test 使用单元测试框架运行测试，测试代码不会被打包或部署。
6. package 接受编译好的代码，打包成可发布的格式，如JAR。
7. <strong><span style="color:red;">install 将包安装到Maven本地仓库，供本地其他Maven项目使用。</span></strong>
8. <strong><span style="color:red;">deploy 将最终的包复制到远程仓库，供其他开发人员和Maven项目使用。</span></strong>

##### 7.2.4 site生命周期
site生命周期的目的是建立和发布项目站点，Maven能够基于POM所包含的信息，自动生成一个友好的站点，方便团队交流和发布项目信息。该生命周期包含如下阶段：
1. pre-site 执行一些在生成项目站点之前需要完成的工作。
2. site 生成项目站点文档。
3. post-site 执行一些在生成项目站点之后需要完成的工作。
4. site-deploy 将生成的项目站点发布到服务器上。

##### 7.2.5 命令行与生命周期

<strong><span style="color:red;">从命令行执行Maven任务的最主要方式就是调用Maven的生命周期阶段。需要注意的是，各个生命周期是相互独立的，而一个生命周期的阶段是有前后依赖关系的。</span></strong>

$mvn clean install：该命令调用clean生命周期的clean阶段和default生命周期的install阶段。实际执行的阶段为clean生命周期的pre-clean、clean阶段，以及default生命周期的从validate至install的所有阶段。该命令结合了两个生命周期，在执行真正的项目构建之前清理项目是一个很好的实践。

<strong><span style="color:red;">Maven中主要的生命周期阶段并不多，而常用的Maven命令实际都是基于这些阶段简单组合而成的。</span></strong>

#### 7.3 插件目标

Maven的核心仅仅定义了抽象的生命周期，具体的任务是交由插件完成的，插件以独立的构件形式存在，因此，Maven核心的分发包很小，Maven会在需要的时候下载并使用插件。

对于插件本身，为了能够复用代码，它往往能够完成多个任务。例如maven-dependency-plugin，它能够基于项目依赖做很多事情。它能够分析项目依赖，帮助找出潜在的无用依赖；它能够列出项目的依赖树，帮助分析依赖来源；它能够列出项目所有已解析的依赖，等等。<strong><span style="color:red;">这些功能聚集在一个插件里，每个功能就是一个插件目标。</span></strong>

maven-dependency-plugin有十多个目标，每个目标对应了一个功能，上述提到的几个功能分别对应的插件目标为dependency:analyze、dependency:tree和dependency:list。这是一种通用的写法，<strong><span style="color:red;">冒号前面是插件前缀，冒号后面是该插件的目标。</span></strong>

#### 7.4 插件绑定

Maven的生命周期与插件相互绑定，用以完成实际的构建任务。具体而言，是<strong><span style="color:red;">生命周期（Lifecycle）的阶段（Phase）与插件（Plugin）的目标（Goal）相互绑定，以完成某个具体的构建任务。</span></strong>

##### 7.4.1 内置绑定

为了能让用户几乎不用任何配置就能构建Maven项目，<strong><span style="color:red;">Maven在核心为一些主要的生命周期阶段绑定了很多插件的目标，当用户通过命令行调用生命周期阶段的时候，对应的插件目标就会执行相应的任务。</span></strong>

生命周期阶段与插件目标的绑定关系可参阅Maven官方文档：https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Built-in_Lifecycle_Bindings 。

<img src="https://res.weread.qq.com/wrepub/epub_602555_47"></img><br/>

<img src="https://res.weread.qq.com/wrepub/epub_602555_48"></img><br/>

<img src="https://res.weread.qq.com/wrepub/epub_602555_49"></img>

##### 7.4.2 自定义绑定

除了内置绑定以外，用户还能够自己选择将某个插件目标绑定到生命周期的某个阶段上，这种自定义绑定方式能让Maven项目在构建过程中执行更多更富特色的任务。

一个常见的例子是创建项目的源码jar包，内置的插件绑定关系中并没有涉及这一任务，因此需要用户自行配置。maven-source-plugin可以帮助我们完成该任务，它的jar-no-fork目标能够将项目的主代码打包成jar文件，可以将其绑定到default生命周期的verify阶段上，在执行完集成测试后和安装构件之前创建源码jar包。
```
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-source-plugin</artifactId>
            <version>2.1.1</version>
            <executions>
                <execution>
                    <id>attach-sources</id>
                    <phase>verify</phase>
                    <goals>
                        <goal>jar-no-fork</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
插件执行配置executions下每个execution子元素可以用来配置执行一个任务。该例中配置了一个id为attach-sources的任务，<strong><span style="color:red;">通过phase配置，将其绑定到verify生命周期阶段上，再通过goals配置指定要执行的插件目标。</span></strong>

有很多插件的目标在编写时已经定义了默认绑定阶段。可以使用maven-help-plugin查看插件详细信息，了解插件目标的默认绑定阶段。运行命令如下：$`mvn help:describe -Dplugin=org.apache.maven.plugins:maven-source-plugin:2.1.1 -Ddetail`
```
source:jar-no-fork
Description: This goal bundles all the sources into a jar archive. This
goal functions the same as the jar goal but does not fork the build and is
suitable for attaching to the build lifecycle.
Implementation: org.apache.maven.plugin.source.SourceJarNoForkMojo
Language: java
Bound to phase: package
```
该输出包含了一段关于jar-no-fork目标的描述，这里关心的是Bound to phase这一项，它表示该目标默认绑定的生命周期阶段（这里是package）。也就是说，当用户配置使用maven-source-plugin的jar-no-fork目标的时候，如果不指定phase参数，该目标就会被绑定到package阶段。

<strong><span style="color:red;">当插件目标被绑定到不同的生命周期阶段的时候，其执行顺序会由生命周期阶段的先后顺序决定。当多个插件目标绑定到同一个阶段的时候，这些插件声明的先后顺序决定了目标的执行顺序。</span></strong>

#### 7.5 插件配置
几乎所有Maven插件的目标都有一些可配置的参数，用户可以通过命令行和POM配置等方式来配置这些参数。

##### 7.5.1 命令行插件配置
<strong><span style="color:red;">很多插件目标的参数都支持从命令行配置，用户可以在Maven命令中使用-D参数，并伴随一个参数键=参数值的形式，来配置插件目标的参数。</span></strong>

例如，maven-surefire-plugin提供了一个maven.test.skip参数，当其值为true的时候，就会跳过执行测试。于是，在运行命令的时候，加上如下-D参数就能跳过测试：$`mvn install -Dmaven.test.skip=true`

参数-D是Java自带的，其功能是通过命令行设置一个Java系统属性，Maven简单地重用了该参数，在准备插件的时候检查系统属性，便实现了插件参数的配置。

##### 7.5.2 POM中插件全局配置
用户可以在声明插件的时候，对此插件进行一个全局的配置。也就是说，所有该基于该插件目标的任务，都会使用这些配置。

例如，我们通常会需要配置maven-compiler-plugin告诉它编译Java 1.7版本的源文件，生成与JVM 1.7兼容的字节码文件：
```
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.7.0</version>
            <configuration>
                <source>1.7</source>
                <target>1.7</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```
这样，不管绑定到compile阶段的maven-compiler-plugin:compile任务，还是绑定到test-compiler阶段的maven-compiler-plugin:testCompile任务，就都能够使用该配置，基于Java 1.7版本进行编译。

#### 7.6 获取插件信息

##### 7.6.1 访问在线的插件文档

以插件maven-surefire-plugin为例，查看目标test的skipTests参数说明，步骤：
1. 详细的插件列表：https://maven.apache.org/plugins/index.html 。
2. 点击上一步页面中“Supported By The Maven Project”部分的表格里列“Plugin”里的行“surefire”，进入页面：https://maven.apache.org/surefire/maven-surefire-plugin/ 。
3. 点击上一步页面中“Goals Overview”部分的文字连接“surefire:test”，进入页面：https://maven.apache.org/surefire/maven-surefire-plugin/test-mojo.html 。
4. 在上一步页面中，搜索：skipTests，找到skipTests的参数说明：
```
<skipTests>
Set this to "true" to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite convenient on occasion.
Failsafe plugin deprecated the parameter skipTests and the parameter will be removed in Failsafe 3.0.0 as it is a source of conflicts between Failsafe and Surefire plugin.
Type: boolean
Since: 2.4
Required: No
User Property: skipTests
Default: false
```
<strong><span style="color:red;">可以在POM中配置maven-surefire-plugin的skipTests参数为true来跳过测试；从命令行传入的参数，是由该插件参数的User Property（User Property: skipTests，此处也是skipTests）决定的。</span></strong>

##### 7.6.2 使用maven-help-plugin描述插件
除了访问在线的插件文档之外，还可以借助maven-help-plugin来获取插件的详细信息。可以运行如下命令来获取maven-compiler-plugin 2.1版本的信息：$`mvn help:describe -Dplugin=org.apache.maven.plugins:maven-compiler-plugin:2.1`

这里执行的是maven-help-plugin的describe目标，在参数plugin中输入需要描述插件的groupId、artifactId和version。
```
Name: Maven Compiler Plugin
Description: The Compiler Plugin is used to compile the sources of your
  project.
Group Id: org.apache.maven.plugins
Artifact Id: maven-compiler-plugin
Version: 2.1
Goal Prefix: compiler
```
这里值得一提的是<strong><span style="color:red;">目标前缀（Goal Prefix），其作用是方便在命令行直接运行插件。</span></strong>maven-compiler-plugin的目标前缀是compiler（上面输出中的：Goal Prefix: compiler）。

在描述插件的时候，还可以省去版本信息，让Maven自动获取最新版本来进行表述。例如：$`mvn help:describe -Dplugin=org.apache.maven.plugins:maven-compiler-plugin`

进一步简化，可以使用插件目标前缀替换坐标。例如：$`mvn help:describe -Dplugin=compiler`

如果想仅仅描述某个插件目标的信息，可以加上goal参数：$`mvn help:describe -Dplugin=compiler -Dgoal=compile`

如果想让maven-help-plugin输出更详细的信息，可以加上detail参数：$`mvn help:describe -Dplugin=compiler -Ddetail`

#### 7.7 从命令行调用插件
如果在命令行运行`mvn -h`来显示mvn命令帮助，就可以看到如下的信息：
```
usage: mvn [options] [<goal(s)>] [<phase(s)>]
```
<strong><span style="color:red;">可以通过mvn命令激活生命周期阶段，从而执行那些绑定在生命周期阶段上的插件目标。</span></strong>

但<strong><span style="color:red;">Maven还支持直接从命令行调用插件目标。</span></strong>Maven支持这种方式是因为有些任务不适合绑定在生命周期上，例如maven-help-plugin:describe，我们不需要在构建项目的时候去描述插件信息，又如maven-dependency-plugin:tree，我们也不需要在构建项目的时候去显示依赖树。

#### 7.8 插件解析机制

为了方便用户使用和配置插件，Maven不需要用户提供完整的插件坐标信息，就可以解析得到正确的插件。

`mvn help:system`这样一条命令，它到底执行了什么插件？该插件的groupId、artifactId和version分别是什么？这个构件是从哪里来的？

##### 7.8.1 插件仓库
与依赖构件一样，插件构件同样基于坐标存储在Maven仓库中。在需要的时候，Maven会从本地仓库寻找插件，如果不存在，则从远程插件仓库查找。找到插件之后，再下载到本地仓库使用。

<strong><span style="color:red;">Maven会区别对待依赖的远程仓库与插件的远程仓库。不同于依赖的远程仓库配置repositories及其repository子元素，插件的远程仓库使用pluginRepositories和pluginRepository配置。</span></strong>

一般来说，中央仓库所包含的插件完全能够满足我们的需要，因此也不需要配置其他的插件仓库。只有在很少的情况下，项目使用的插件无法在中央仓库找到，或者自己编写了插件，这个时候可以参考上述的配置，在POM或者settings.xml中加入其他的插件仓库配置。

##### 7.8.2 插件的默认groupId
在POM中配置插件的时候，如果该插件是Maven的官方插件（即如果其groupId为org.apache.maven.plugins），就可以省略groupId配置。

不推荐使用Maven的这一机制，虽然这么做可以省略一些配置，但这样的配置会让团队中不熟悉Maven的成员感到费解，况且能省略的配置也就仅仅一行而已。

##### 7.8.3 解析插件版本
同样是为了简化插件的配置和使用，在用户没有提供插件版本的情况下，Maven会自动解析插件版本。

Maven在超级POM中为所有核心插件设定了版本，超级POM是所有Maven项目的父POM，所有项目都继承这个超级POM的配置，因此，<strong><span style="color:red;">即使用户不加任何配置，Maven使用核心插件的时候，它们的版本就已经确定了。</span></strong>这些插件包括maven-clean-plugin、maven-compiler-plugin、maven-surefire-plugin等。

如果用户使用某个插件时没有设定版本，而这个插件又不属于核心插件的范畴，Maven就会去检查所有仓库中可用的版本，然后做出选择。<strong><span style="color:red;">Maven 3当不属于核心的插件没有声明版本的时候，选择使用最新release版本。</span></strong>

依赖Maven解析插件版本其实是不推荐的做法，即使Maven 3将版本解析到最新的release版本，也还是会有潜在的不稳定性。例如，可能某个插件发布了一个新的版本，而这个版本的行为与之前的版本发生了变化，这种变化就可能导致项目构建失败。因此，使用插件的时候，应该一直显式地设定版本。

##### 7.8.4　解析插件前缀
前面讲到mvn命令行支持使用插件前缀来简化插件的调用，现在解释Maven如何根据插件前缀解析得到插件的坐标。

插件前缀与groupId:artifactId是一一对应的，这种匹配关系存储在仓库元数据中。这里的仓库元数据为groupId/maven-metadata.xml。主要的插件都位于 http://repo1.maven.org/maven2/org/apache/maven/plugins/ 和 http://repository.codehaus.org/org/codehaus/mojo/ ，相应地，Maven在解析插件仓库元数据的时候，会默认使用org.apache.maven.plugins和org.codehaus.mojo两个groupId。也可以通过配置settings.xml让Maven检查其他groupId上的插件仓库元数据：
```
<settings>
    <pluginGroups>
        <pluginGroup>com.your.plugins</pluginGroup>
    </pluginGroups>
</settings>
```

从中央仓库的org.apache.maven.plugins groupId下插件仓库元数据中截取的一些片段：
```
<metadata>
    <plugins>
        <plugin>
            <name>Maven Clean Plugin</name>
            <prefix>clean</prefix>
            <artifactId>maven-clean-plugin</artifactId>
        </plugin>
        <plugin>
            <name>Maven Compiler Plugin</name>
            <prefix>compiler</prefix>
            <artifactId>maven-compiler-plugin</artifactId>
        </plugin>
        <plugin>
            <name>Maven Dependency Plugin</name>
            <prefix>dependency</prefix>
            <artifactId>maven-dependency-plugin</artifactId>
        </plugin>
    </plugins>
</metadata>
```
查看prefix元素，可以看到maven-clean-plugin的前缀为clean、maven-compiler-plugin的前缀为compiler、maven-dependency-plugin的前缀为dependency。

<strong><span style="color:red;">当Maven解析到dependency:tree这样的命令后，它首先基于默认的groupId归并所有插件仓库的元数据org/apache/maven/plugins/maven-metadata.xml；其次检查归并后的元数据，找到对应的artifactId为maven-dependency-plugin；然后结合当前元数据的groupId org.apache.maven.plugins；最后使用第7.8.3节描述的方法解析得到version，这时就得到了完整的插件坐标。如果org/apache/maven/plugins/maven-metadata.xml没有记录该插件前缀，则接着检查其他groupId下的元数据，如org/codehaus/mojo/maven-metadata.xml，以及用户自定义的插件组。如果所有元数据中都不包含该前缀，则报错。</span></strong>