# Java 从零开始 — 学习笔记

一份系统化的 Java 学习代码笔记，从环境搭建到综合项目实战，覆盖 Java 核心知识体系。

## 项目特点

- **逐行注解**：每个 Java 文件都是完整的教程，关键概念以中文注释详细解释
- **循序渐进**：按知识依赖关系编排章节，建议按序阅读
- **即拿即用**：每个文件均可独立编译运行，动手实验零门槛

## 目录

| 章节 | 内容 | 文件 |
|------|------|------|
| Ch00 | 环境搭建与 HelloWorld 解剖 | [HelloWorld.java](ch00-environment/HelloWorld.java) |
| Ch01 | 变量、基本数据类型、运算符 | [VariablesAndTypes.java](ch01-basics/VariablesAndTypes.java) / [Operators.java](ch01-basics/Operators.java) |
| Ch02 | 控制流程（if/switch/for/while） | [ControlFlow.java](ch02-controlflow/ControlFlow.java) |
| Ch03 | 数组 | [ArraysTutorial.java](ch03-arrays/ArraysTutorial.java) |
| Ch04 | 面向对象基础（封装） | [OOPTutorial.java](ch04-oop-part1/OOPTutorial.java) |
| Ch05 | 继承 | [InheritanceTutorial.java](ch05-inheritance/InheritanceTutorial.java) |
| Ch06 | 多态 | [PolymorphismTutorial.java](ch06-polymorphism/PolymorphismTutorial.java) |
| Ch07 | 抽象类与接口 | [AbstractAndInterface.java](ch07-abstract-interface/AbstractAndInterface.java) |
| Ch08 | static / final / 内部类 | [StaticFinalInner.java](ch08-static-final-inner/StaticFinalInner.java) |
| Ch09 | String 与字符串操作 | [StringTutorial.java](ch09-string/StringTutorial.java) |
| Ch10 | 异常处理 | [ExceptionTutorial.java](ch10-exception/ExceptionTutorial.java) |
| Ch11 | 集合框架（List/Set/Map） | [CollectionsTutorial.java](ch11-collections/CollectionsTutorial.java) |
| Ch12 | 泛型 | [GenericsTutorial.java](ch12-generics/GenericsTutorial.java) |
| Ch13 | IO 流与文件操作 | [IOTutorial.java](ch13-io/IOTutorial.java) |
| Ch14 | 多线程 | [ThreadTutorial.java](ch14-threads/ThreadTutorial.java) |
| Ch15 | Lambda 表达式与 Stream API | [LambdaStreamTutorial.java](ch15-lambda-stream/LambdaStreamTutorial.java) |
| Ch16 | JDBC 数据库操作 | [JDBCTutorial.java](ch16-jdbc/JDBCTutorial.java) |
| Ch17 | 网络编程 | [NetworkTutorial.java](ch17-network/NetworkTutorial.java) |
| Ch20 | 综合实战：图书管理系统 | [BookManagementSystem.java](ch20-project/BookManagementSystem.java) |

## 环境要求

- **JDK 17** 或以上（推荐 JDK 21 LTS）
  - [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
  - [OpenJDK / Adoptium](https://adoptium.net/)
- **IDE**（可选）：IntelliJ IDEA Community Edition / VS Code

## 使用方法

```bash
# 克隆仓库
git clone git@github.com:cujohe/java-learning.git
cd java-learning

# 编译并运行任意章节
javac ch00-environment/HelloWorld.java -d out
java -cp out HelloWorld

# 或直接运行（需 JDK 11+，支持单文件运行）
java ch01-basics/VariablesAndTypes.java
```

## 学习路线建议

```
Ch00 环境搭建
  ↓
Ch01 变量与运算符 → Ch02 控制流程 → Ch03 数组
  ↓
Ch04 面向对象基础 → Ch05 继承 → Ch06 多态 → Ch07 抽象类与接口
  ↓
Ch08 static/final/内部类 → Ch09 String
  ↓
Ch10 异常 → Ch11 集合 → Ch12 泛型
  ↓
Ch13 IO → Ch14 多线程 → Ch15 Lambda/Stream
  ↓
Ch16 JDBC / Ch17 网络编程（可并行）
  ↓
Ch20 综合项目实战
```

---

持续更新中，欢迎 Star ⭐
