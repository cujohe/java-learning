/**
 * =====================================================
 * Ch09: String 全家桶
 * =====================================================
 *
 * String 是 Java 中最常用的类，没有之一。但它有很多反直觉的设计。
 * 这部分不搞清楚，你会写出无数"看似正确其实有 bug"的代码。
 *
 * 【String 的三大特点】
 *
 * 1. 不可变性 (Immutable)
 *    String 对象一旦创建，内容就不能改变。
 *    所有看起来"修改"的操作（replace, substring, toUpperCase...）
 *    实际上是创建了一个新的 String 对象。
 *
 *    为什么这样设计？
 *    → 安全性（字符串常量池共享）、线程安全（不用加锁）、
 *       Hash 缓存（HashMap 的 key 性能更好）
 *
 * 2. 字符串常量池 (String Pool)
 *    JVM 在堆内存中划了一块区域叫"字符串常量池"。
 *    直接用双引号创建的字符串会存在这里，内容相同的字符串只存一份。
 *    new String("xxx") 则不会用常量池（在堆的普通区域新建）。
 *
 * 3. 用 equals() 比较内容，用 == 比较地址
 *    这是 Java 新手最容易犯的错。
 */

public class StringTutorial {

    public static void main(String[] args) {

        // ============================================
        // 【1. String 的创建——三种方式】
        // ============================================
        System.out.println("=== 创建 String 的三种方式 ===");

        // 方式1：字面量 —— 放入字符串常量池（推荐）
        String s1 = "hello";

        // 方式2：new —— 在堆中新建对象，不走常量池（除非主动 intern）
        String s2 = new String("hello");

        // 方式3：字符数组
        char[] chars = {'h', 'e', 'l', 'l', 'o'};
        String s3 = new String(chars);

        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
        System.out.println("s3: " + s3);


        // ============================================
        // 【2. == vs equals() —— 一定要搞懂！】
        // ============================================
        System.out.println("\n=== == vs equals() ===");

        String a = "hello";
        String b = "hello";              // 字面量相同 → 指向常量池中同一个对象
        String c = new String("hello");  // new → 强制新建对象

        System.out.println("a == b      : " + (a == b));        // true  (常量池共享)
        System.out.println("a == c      : " + (a == c));        // false (不同对象)
        System.out.println("a.equals(c) : " + a.equals(c));     // true  (内容相同)

        // 结论：比较字符串内容 → 永远用 equals()
        //      比较是不是同一个对象 → 才用 ==（极少需要）


        // ============================================
        // 【3. 不可变性演示】
        // ============================================
        System.out.println("\n=== 不可变性 ===");

        String original = "Hello";
        String modified = original.toUpperCase();  // 返回新对象！
        System.out.println("original  : " + original);  // "Hello"   ← 原对象没变
        System.out.println("modified  : " + modified);  // "HELLO"   ← 这是新对象

        // 如果确实需要频繁修改字符串 → 用 StringBuilder
        // （后面会讲，这是性能优化的关键）


        // ============================================
        // 【4. String 常用方法速查】
        // ============================================
        System.out.println("\n=== 常用方法 ===");

        String str = "  Hello Java World  ";

        // 长度
        System.out.println("长度: " + str.length());

        // 去头尾空格
        System.out.println("trim(): '" + str.trim() + "'");

        // 提取子串
        System.out.println("substring(2, 7): " + str.substring(2, 7));  // [2, 7) → "Hell"

        // 查找
        System.out.println("indexOf('J'): " + str.indexOf('J'));        // 下标8
        System.out.println("contains(\"ava\"): " + str.contains("ava")); // true

        // 替换
        System.out.println("replace('l', 'L'): " + str.replace('l', 'L'));

        // 分割（非常常用！）
        String csv = "苹果,香蕉,橙子,葡萄";
        String[] fruits = csv.split(",");   // 返回数组
        System.out.print("split 结果: ");
        for (String fruit : fruits) {
            System.out.print("[" + fruit + "] ");
        }
        System.out.println();

        // 大小写
        System.out.println("全大写: " + str.toUpperCase());
        System.out.println("全小写: " + str.toLowerCase());

        // 判空（最安全的方式）
        String empty = "";
        String nullStr = null;
        System.out.println("isEmpty(): " + empty.isEmpty());        // true
        // nullStr.isEmpty() → 空指针异常！所以判空要这样写：
        System.out.println("安全判空: " + (nullStr == null || nullStr.isEmpty()));


        // ============================================
        // 【5. StringBuilder 和 StringBuffer】
        // ============================================
        System.out.println("\n=== StringBuilder ===");

        // 如果在一个循环里不停用 + 拼接字符串 → 灾难！
        // 因为每次 + 都会创建新的 String 对象（不可变性），大量垃圾对象
        //
        // 反面教材：
        // String result = "";
        // for (int i = 0; i < 100000; i++) {
        //     result += i;  // 每次循环都 new 一个新 String 对象！极慢！
        // }

        // 正确做法：用 StringBuilder（线程不安全，但快）或 StringBuffer（线程安全，但慢）
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        sb.append("!");
        sb.insert(5, ", Beautiful");   // 在位置5插入
        String finalResult = sb.toString();

        System.out.println("StringBuilder 结果: " + finalResult);

        // 链式调用（append 返回 this，所以可以连着写）
        StringBuilder chain = new StringBuilder()
            .append("红")
            .append("豆")
            .append("生")
            .append("南")
            .append("国");
        System.out.println("链式调用: " + chain.toString());

        // 日常开发规则：
        // 少量拼接（<5次）：用 + 就行，编译器会优化
        // 循环内拼接：必须用 StringBuilder
        // 多线程环境：用 StringBuffer


        // ============================================
        // 【6. intern() 方法】
        // ============================================
        System.out.println("\n=== intern() 方法 ===");

        String heapStr   = new String("Java");
        String pooledStr = heapStr.intern();  // 把 heapStr 的内容放入常量池，返回常量池中的引用
        String literal   = "Java";            // 从常量池获取

        System.out.println("heapStr == literal  : " + (heapStr == literal));   // false
        System.out.println("pooledStr == literal: " + (pooledStr == literal)); // true
        // intern() 偶尔用于节省内存（大量相同字符串时），但一般不需要手动调用
    }
}
