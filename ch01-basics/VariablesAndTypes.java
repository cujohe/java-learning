/**
 * =====================================================
 * Ch01-1: 变量、基本数据类型
 * =====================================================
 *
 * 【什么是变量？】
 *   变量 = 一块命名的内存空间，用来存数据。
 *   类比：变量像一个盒子，盒子上贴了标签（变量名），盒子里装东西（值）。
 *
 *   Java 是强类型语言——每个变量必须先声明类型，类型一旦确定就不能改变。
 *
 *   int age = 25;
 *   │   │    │
 *   │   │    └── 值：往盒子里放的数字 25
 *   │   └────── 变量名：盒子上的标签
 *   └────────── 数据类型：规定盒子只能装整数
 */

public class VariablesAndTypes {

    public static void main(String[] args) {
        // ============================================
        // 【Java 的 8 种基本类型 (Primitive Types)】
        // 它们不是对象，直接存值，存在栈内存中。
        // ============================================

        // ----- 整型（4种）-----
        // byte:  1字节 = 8位, 范围 -128 ~ 127
        // short: 2字节, 范围 -32,768 ~ 32,767
        // int:   4字节, 范围 ±21亿（最常用的整数类型）
        // long:  8字节, 非常大的整数。字面量后面要加 L
        byte  b = 100;
        short s = 30000;
        int   i = 2000000000;
        long  l = 9999999999999L;  // 不加 L 会被当作 int，而 999... 超出 int 范围→编译报错

        System.out.println("=== 整型 ===");
        System.out.println("byte:  " + b);
        System.out.println("short: " + s);
        System.out.println("int:   " + i);
        System.out.println("long:  " + l);

        // ----- 浮点型（2种）-----
        // float:  4字节，单精度，字面量后面必须加 f
        // double: 8字节，双精度，默认的小数字面量类型（最常用）
        // ⚠️ 浮点数有精度问题：0.1 + 0.2 != 0.3（这是 IEEE 754 标准的问题，不是 Java 的锅）
        float  f = 3.14f;      // 不加 f 会被当作 double→编译报错
        double d = 3.1415926;
        double precisionDemo = 0.1 + 0.2;  // 结果: 0.30000000000000004

        System.out.println("\n=== 浮点型 ===");
        System.out.println("float:  " + f);
        System.out.println("double: " + d);
        System.out.println("0.1 + 0.2 = " + precisionDemo + " ← 精度陷阱！");

        // ----- 字符型（1种）-----
        // char: 2字节，存一个 Unicode 字符，用单引号
        // Java 的 char 是 16 位无符号整数，本质存的是字符的 Unicode 编码值
        char c1 = 'A';
        char c2 = '中';
        char c3 = 65;          // 65 是 'A' 的 ASCII/Unicode 码值，所以 c3 也是 'A'

        System.out.println("\n=== 字符型 ===");
        System.out.println("c1 = " + c1 + " (直接赋值)");
        System.out.println("c2 = " + c2 + " (中文也没问题)");
        System.out.println("c3 = " + c3 + " (用整数65赋值，实际存 'A')");

        // ----- 布尔型（1种）-----
        // boolean: 只有 true 和 false 两个值
        // ⚠️ Java 中 boolean 和 int 不能互转（不像 C 语言 0=假,非0=真）
        boolean isJavaFun = true;
        boolean isTired  = false;

        System.out.println("\n=== 布尔型 ===");
        System.out.println("Java 有趣吗？" + isJavaFun);
        System.out.println("累吗？" + isTired);


        // ============================================
        // 【类型转换：自动 vs 强制】
        // ============================================

        // 自动类型转换（隐式）：小类型→大类型，自动完成，不会丢数据
        // 规则: byte → short → int → long → float → double
        //             ↗ char ↗
        int    autoInt   = 100;
        long   autoLong  = autoInt;     // int → long，OK
        double autoDouble = autoInt;     // int → double，OK

        // 强制类型转换（显式）：大类型→小类型，需要手动写，可能丢数据
        // 写法: (目标类型) 值
        double big    = 3.99;
        int    result = (int) big;       // 小数部分直接截断，不是四舍五入！结果 = 3

        // 溢出（Overflow）——要警惕
        int maxInt = Integer.MAX_VALUE;  // 2147483647
        int overflowed = maxInt + 1;     // 回绕成 -2147483648！

        System.out.println("\n=== 类型转换 ===");
        System.out.println("自动转换 int→double: " + autoDouble);
        System.out.println("强制转换 3.99→int: " + result + " ← 注意是截断，不是四舍五入！");
        System.out.println("Integer.MAX_VALUE = " + maxInt);
        System.out.println("MAX_VALUE + 1   = " + overflowed + " ← 溢出！");


        // ============================================
        // 【变量的命名规则与规范】
        // ============================================
        // 规则（违反会编译报错）：
        //   1. 只能由 字母/数字/_/$ 组成
        //   2. 不能以数字开头
        //   3. 不能是 Java 关键字（如 class, public, int...）
        //   4. 区分大小写（age 和 Age 是两个变量）
        //
        // 规范（不违反也能编译，但大家都这么写）：
        //   变量/方法: camelCase（小驼峰）→ userName, studentAge
        //   类名:      PascalCase（大驼峰）→ HelloWorld, StudentInfo
        //   常量:      UPPER_SNAKE_CASE      → MAX_SIZE, PI
        //   包名:      全小写，用.分层        → com.company.project

        // 好的命名（自解释，不需要注释就知道是什么意思）
        String customerName  = "张三";
        int    customerAge   = 28;
        double accountBalance = 1000.50;

        System.out.println("\n=== 变量命名 ===");
        System.out.println("客户: " + customerName + ", 年龄: " + customerAge
                           + ", 余额: " + accountBalance);


        // ============================================
        // 【引用类型简介】
        // ============================================
        // 除了 8 种基本类型，其他全是"引用类型"(Reference Type)
        // 引用类型变量存的是"对象在内存中的地址"，不是对象本身
        //
        // 类比：
        //   基本类型 int x = 5     → 盒子里直接装着一个 5
        //   引用类型 String s = "hi" → 盒子里装着一张纸条，写着"hi 在 0x1234"
        //
        // String 是最常用的引用类型，下面会专门讲

        String greeting = "Hello";   // greeting 存的是字符串对象的地址
        String another  = greeting;  // another 也指向同一个对象（浅拷贝）
        // 这时 greeting 和 another 指向同一个 "Hello" 对象

        System.out.println("\n=== 引用类型演示 ===");
        System.out.println("greeting: " + greeting);
        System.out.println("another:  " + another);
        System.out.println("它们指向同一个对象吗？" + (greeting == another)); // true
    }
}
