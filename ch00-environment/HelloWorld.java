/**
 * =====================================================
 * Ch00: 环境搭建与"Hello World"的解剖
 * =====================================================
 *
 * 【Java 程序是怎么跑起来的？——一张图讲清楚】
 *
 *   你写的 .java 源码
 *        │
 *        ▼  javac 编译器
 *   字节码 .class 文件（与平台无关的中间代码）
 *        │
 *        ▼  java 命令启动 JVM
 *   JVM 把字节码翻译成当前操作系统能懂的机器码
 *        │
 *        ▼
 *   程序运行
 *
 *   关键概念：
 *   - JVM (Java Virtual Machine)：Java 虚拟机，运行 .class 字节码的"假想计算机"
 *   - JRE (Java Runtime Environment)：JVM + 核心类库，用来"跑"Java 程序
 *   - JDK (Java Development Kit)：JRE + 开发工具（javac编译器等），用来"写"Java 程序
 *   - 我们安装的是 JDK，它包含了 JRE
 *
 * 【环境搭建】
 *   1. 下载 JDK：推荐 JDK 17 (LTS 长期支持版) 或 JDK 21 (最新 LTS)
 *      - Oracle JDK: https://www.oracle.com/java/technologies/downloads/
 *      - 或 OpenJDK: https://adoptium.net/
 *   2. 安装后验证：终端输入 java -version 和 javac -version
 *   3. IDE 选择：IntelliJ IDEA Community Edition（免费，强烈推荐）
 *      - VS Code + Extension Pack for Java 也可
 *
 * 【编译与运行】
 *   终端操作：
 *   $ javac HelloWorld.java    # 编译，生成 HelloWorld.class
 *   $ java HelloWorld          # 运行（注意不要写 .class 后缀）
 */


// ============== 以下是逐行解剖 ==============


/**
 * public class HelloWorld —— 这一行在说什么？
 *
 * public    : 访问修饰符，意思是"公开的"，任何地方都能访问这个类
 * class     : 关键字，告诉 Java："我要定义一个类"
 * HelloWorld: 类名，必须和文件名完全一致（HelloWorld.java）
 *
 * 规则：一个 .java 文件最多只能有一个 public 类，且类名必须等于文件名。
 */
public class HelloWorld {

    /**
     * public static void main(String[] args) —— 每个单词都什么意思？
     *
     * public     : 公开的，JVM 需要从外部调用这个方法
     * static     : 静态的，意思是"属于类本身，不需要先 new 一个对象就能调用"
     *              JVM 启动时还没有任何对象，所以入口方法必须是 static
     * void       : 返回值类型为"空"，main 方法不需要返回任何东西
     * main       : 方法名，JVM 规定入口方法必须叫 main（约定，不可改）
     * String[] args : 参数是字符串数组，命令行传参用的
     *              e.g. $ java HelloWorld arg1 arg2  → args = ["arg1", "arg2"]
     */
    public static void main(String[] args) {
        // System.out.println —— 这又是什么？
        // System  : java.lang 包下的一个类，代表"系统"
        // out     : System 类里的一个静态属性，类型是 PrintStream，代表"标准输出"
        // println : PrintStream 的方法，打印一行并换行
        //           print("不换行") / printf("格式化 %s", "字符串")
        System.out.println("Hello, World!");

        // ---- 演示命令行参数 ----
        // 运行: java HelloWorld Java 从零开始
        // 输出: Hello, World!
        //        接收到 2 个参数
        //        参数0: Java
        //        参数1: 从零开始
        System.out.println("接收到 " + args.length + " 个参数");
        for (int i = 0; i < args.length; i++) {
            System.out.println("参数" + i + ": " + args[i]);
        }
    }
}
