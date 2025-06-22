### 第五章 坐标和依赖

#### 5.1 何为Maven坐标
为了能自动化地解析任何一个Java构件，Maven就必须将它们唯一标识，这就依赖管理的底层基础——坐标。

关于坐标（Coordinate），大家最熟悉的定义应该来自于平面几何。在一个平面坐标系中，坐标（x,y）表示该平面上与x轴距离为y，与y轴距离为x的一点，任何一个坐标都能够唯一标识该平面中的一点。

Maven定义了这样一组规则：世界上任何一个构件都可以使用Maven坐标唯一标识，<strong><span style="color:red;">Maven坐标的元素包括groupId、artifactId、version、packaging、classifier。</span></strong>

#### 5.2 坐标详解
先看一组坐标定义，如下：
```
<groupId>org.sonatype.nexus</groupId>
<artifactId>nexus-indexer</artifactId>
<version>2.0.0</version>
<packaging>jar</packaging>
```

<strong><span style="color:red;">Maven内置了一个中央仓库（ http://repo1.maven.org/maven2 ），该中央仓库包含了世界上大部分流行的开源项目构件，Maven会在需要的时候去那里下载。</span></strong>

groupId：定义当前Maven项目隶属的实际项目。groupId的表示方式与Java包名的表示方式类似，通常与域名反向一一对应。上例中，groupId为org.sonatype.nexus，org.sonatype表示Sonatype公司建立的一个非盈利性组织，nexus表示Nexus这一实际项目，该groupId与域名nexus.sonatype.org对应。

artifactId：该元素定义实际项目中的一个Maven项目（模块），推荐的做法是使用实际项目名称作为artifactId的前缀。比如上例中的artifactId是nexus-indexer，使用了实际项目名nexus作为前缀，这样做的好处是方便寻找实际构件。

version：该元素定义Maven项目当前所处的版本。

packaging：该元素定义Maven项目的打包方式。当不定义packaging的时候，Maven会使用默认值jar。

classifier：该元素用来帮助定义构建输出的一些附属构件。附属构件与主构件对应，如上例中的主构件是nexus-indexer-2.0.0.jar，该项目可能还会通过使用一些插件生成如nexus-indexer-2.0.0-javadoc.jar、nexus-indexer-2.0.0-sources.jar这样一些附属构件，其包含了Java文档和源代码。这时候，javadoc和sources就是这两个附属构件的classifier。这样，附属构件也就拥有了自己唯一的坐标。注意，不能直接定义项目的classifier，因为附属构件不是项目直接默认生成的，而是由附加的插件帮助生成。

<strong><span style="color:red;">上述5个元素中，groupId、artifactId、version是必须定义的，packaging是可选的（默认为jar），而classifier是不能直接定义的。</span></strong>

<strong><span style="color:red;">项目构件的文件名是与坐标相对应的，一般的规则为artifactId-version[-classifier].packaging，[-classifier]表示可选。</span></strong>这里还要强调的一点是，packaging并非一定与构件扩展名对应，比如packaging为maven-plugin的构件扩展名为jar。

#### 5.4 依赖的配置

一个依赖声明可以包含如下的一些元素：
```
<project>
    ……
    <dependencies>
        <dependency>
            <groupId>……</groupId>
            <artifactId>……</artifactId>
            <version>……</version>
            <type>……</type>
            <scope>……</scope>
            <optional>……</optional>
            <exclusions>
                <exclusion>
                    ……
                </exclusion>
                ……
            </exclusions>
        </dependency>
        ……
    </dependencies>
    ……
</project>
```
根元素project下的dependencies可以包含一个或者多个dependency元素，以声明一个或者多个项目依赖。

每个依赖可以包含的元素有：
1. <strong><span style="color:red;">groupId、artifactId和version：依赖的基本坐标，对于任何一个依赖来说，基本坐标是最重要的，Maven根据坐标才能找到需要的依赖。</span></strong>
2. type：依赖的类型，对应于项目坐标定义的packaging。大部分情况下，该元素不必声明，其默认值为jar。
3. scope：依赖的范围。
4. optional：标记依赖是否可选。
5. exclusions：用来排除传递性依赖。

大部分依赖声明只包含基本坐标，然而在一些特殊情况下，其他元素至关重要。

#### 5.5 依赖范围
依赖范围就是用来控制依赖与三种classpath（编译classpath、测试classpath、运行classpath）的关系。

Maven有以下几种依赖范围：
1. <strong><span style="color:red;">compile：编译依赖范围。如果没有指定，就会默认使用该依赖范围。</span></strong>使用此依赖范围的Maven依赖，对于编译、测试、运行三种classpath都有效。
2. test：测试依赖范围。使用此依赖范围的Maven依赖，只对于测试classpath有效，在编译主代码或者运行项目的使用时将无法使用此类依赖。
3. provided：已提供依赖范围。使用此依赖范围的Maven依赖，对于编译和测试class-path有效，但在运行时无效。典型的例子是servlet-api，编译和测试项目的时候需要该依赖，但在运行项目的时候，由于容器已经提供，就不需要Maven重复地引入一遍。
4. runtime：运行时依赖范围。使用此依赖范围的Maven依赖，对于测试和运行class-path有效，但在编译主代码时无效。
5. system：系统依赖范围。该依赖与三种classpath的关系，和provided依赖范围完全一致。但是，使用system范围的依赖时必须通过systemPath元素显式地指定依赖文件的路径。由于此类依赖不是通过Maven仓库解析的，而且往往与本机系统绑定，可能造成构建的不可移植，因此应该谨慎使用。
6. import（Maven 2.0.9及以上）：导入依赖范围。该依赖范围不会对三种classpath产生实际的影响。该范围的依赖只在 dependencyManagement 元素下才有效果，使用该范围的依赖通常指向一个 POM，作用是将目标 POM 中的 dependencyManagement 配置导入并合并到当前 POM 的 dependencyManagement 元素中。例如想要在另外一个模块中使用与某个pom.xml中完全一样的 dependencyManagement 配置，除了复制配置或者继承这两种方式之外，还可以使用 import 范围依赖将这一配置导入。

上述除import以外的各种依赖范围与三种classpath的关系如表5-1所示。

<img src="https://res.weread.qq.com/wrepub/epub_602555_31">

#### 5.6 传递性依赖
假设account-mail有一个compile范围的spring-core依赖，spring-core有一个compile范围的commons-logging依赖，那么commons-logging就会成为account-email的compile范围依赖，commons-logging是account-email的一个传递性依赖。

<img src="https://res.weread.qq.com/wrepub/epub_602555_32">

<strong><span style="color:red;">Maven会解析各个直接依赖的POM，将那些必要的间接依赖，以传递性依赖的形式引入到当前的项目中。</span></strong>

<strong><span style="color:red;">第一直接依赖的范围和第二直接依赖的范围决定了传递性依赖的范围</span></strong>，如表5-2所示，最左边一列表示第一直接依赖范围，最上面一行表示第二直接依赖范围，中间的交叉单元格则表示传递性依赖范围。

<img src="https://res.weread.qq.com/wrepub/epub_602555_33">

#### 5.7 依赖调解
大部分情况下我们只需要关心项目的直接依赖是什么，而不用考虑这些直接依赖会引入什么传递性依赖。但有时候，当传递性依赖造成问题的时候，我们就需要清楚地知道该传递性依赖是从哪条依赖路径引入的。

Maven依赖调解（Dependency Mediation）的原则：
1. <strong><span style="color:red;">第一原则是：路径最近者优先。</span></strong>例如，项目A有这样的依赖关系：A->B->C->X（1.0）、A->D->X（2.0），X是A的传递性依赖，但是两条依赖路径上有两个版本的X，那么哪个X会被Maven解析使用呢？两个版本都被解析显然是不对的，因为那会造成依赖重复，因此必须选择一个。该例中X（1.0）的路径长度为3，而X（2.0）的路径长度为2，因此X（2.0）会被解析使用。
2. <strong><span style="color:red;">第二原则是：第一声明者优先。</span></strong>在依赖路径长度相等的前提下，在POM中依赖声明的顺序决定了谁会被解析使用，顺序最靠前的那个依赖优胜。比如这样的依赖关系：A->B->Y（1.0）、A->C->Y（2.0），Y（1.0）和Y（2.0）的依赖路径长度是一样的，都为2。那么到底谁会被解析使用呢？该例中，如果B的依赖声明在C之前，那么Y（1.0）就会被解析使用。

#### 5.8 可选依赖
假设有这样一个依赖关系，项目A依赖于项目B，项目B依赖于项目X和Y,B对于X和Y的依赖都是可选依赖：A->B、B->X（可选）、B->Y（可选）。根据传递性依赖的定义，如果所有这三个依赖的范围都是compile，那么X、Y就是A的compile范围传递性依赖。然而，由于这里X、Y是可选依赖，依赖将不会得以传递。换句话说，X、Y将不会对A有任何影响，如图所示。

<img src="https://res.weread.qq.com/wrepub/epub_602555_34">

可选依赖的配置：
```
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.chenanguo.mvnbook</groupId>
    <artifactId>project-b</artifactId>
    <version>1.0.0</version>
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.10</version>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>8.4-701.jdbc3</version>
            <optional>true</optional>
        </dependency>
    </dependencies>
</project>
```
使用<optional>元素表示mysql-connector-java和postgresql这两个依赖为可选依赖，它们只会对当前项目B产生影响，当其他项目依赖于B的时候，这两个依赖不会被传递。<strong><span style="color:red;">因此，当项目A依赖于项目B的时候，如果其实际使用基于MySQL数据库，那么在项目A中就需要显式地声明mysql-connector-java这一依赖。</span></strong>

在理想的情况下，是不应该使用可选依赖的。

#### 5.9 最佳实践

##### 5.9.1 排除依赖

传递性依赖会给项目隐式地引入很多依赖，这极大地简化了项目依赖的管理，但是有些时候这种特性也会带来问题。例如，当前项目有一个第三方依赖，而这个第三方依赖由于某些原因依赖了另外一个类库的SNAPSHOT版本，那么这个SNAPSHOT就会成为当前项目的传递性依赖，而SNAPSHOT的不稳定性会直接影响到当前的项目。<strong><span style="color:red;">这时就需要排除掉该SNAPSHOT，并且在当前项目中声明该类库的某个正式发布的版本。</span></strong>

```
<dependency>
    <groupId>com.chenanguo.mvnbook</groupId>
    <artifactId>project-b</artifactId>
    <version>1.0.0</version>
    <exclusions>
        <exclusion>
            <groupId>com.chenanguo.mvnbook</groupId>
            <artifactId>project-c</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>com.chenanguo.mvnbook</groupId>
    <artifactId>project-c</artifactId>
    <version>1.1.0</version>
</dependency>
```
上述代码中，项目A依赖于项目B，但是由于一些原因，不想引入传递性依赖C，而是自己显式地声明对于项目C 1.1.0版本的依赖。

代码中使用exclusions元素声明排除依赖，exclusions可以包含一个或者多个exclusion子元素，因此可以排除一个或者多个传递性依赖。

需要注意的是，<strong><span style="color:red;">声明exclusion的时候只需要groupId和artifactId，而不需要version元素</span></strong>，这是因为只需要groupId和artifactId就能唯一定位依赖图中的某个依赖。

该例的依赖解析逻辑如图所示。
<img src="https://res.weread.qq.com/wrepub/epub_602555_35">

##### 5.9.2 Maven属性
首先使用properties元素定义Maven属性，然后可以使用美元符号和大括弧环绕的方式引用Maven属性。举例：${springframework.version}。

##### 5.9.3 优化依赖

<strong><span style="color:red;">Maven会自动解析所有项目的直接依赖和传递性依赖，并且根据规则正确判断每个依赖的范围，对于一些依赖冲突，也能进行调节，以确保任何一个构件只有唯一的版本在依赖中存在。</span></strong>在这些工作之后，最后得到的那些依赖被称为已解析依赖（Resolved Dependency）。

可以运行如下的命令查看当前项目的已解析依赖：`mvn dependency:list`。

将直接在当前项目POM声明的依赖定义为顶层依赖，而这些顶层依赖的依赖则定义为第二层依赖，以此类推，有第三、第四层依赖。当这些依赖经Maven解析后，就会构成一个依赖树，通过这棵依赖树就能很清楚地看到某个依赖是通过哪条传递路径引入的。

可以运行如下命令查看当前项目的依赖树：`mvn dependency:tree`。更新有漏洞的jar包时，可以使用此命令查看jar在哪些路径中。

命令：`mvn dependency:analyze`工具可以帮助分析当前项目的依赖。
该命令结果中重要的是两个部分。
1. 首先是Used undeclared dependencies，意指项目中使用到的，但是没有显式声明的依赖。<strong><span style="color:red;">推荐：显式声明任何项目中直接用到的依赖。</span></strong>
2. 还有一个重要的部分是Unused declared dependencies，意指项目中未使用的，但显式声明的依赖。需要注意的是，对于这样一类依赖，我们不应该简单地直接删除其声明，而是应该仔细分析。由于dependency:analyze只会分析<strong><span style="color:red;">编译主代码和测试代码</span></strong>需要用到的依赖，一些<strong><span style="color:red;">执行测试和运行时</span></strong>需要的依赖它就发现不了。当然，有时候确实能通过该信息找到一些没用的依赖，但一定要小心测试。