/**
 * =====================================================
 * Ch10: 异常处理（Exception Handling）
 * =====================================================
 *
 * 【异常是什么？】
 *   程序运行中发生的"意外情况"。如果不处理，程序直接崩溃。
 *   异常处理 = 给程序装安全气囊，撞了不会死。
 *
 * 【异常的分级体系（继承树）】
 *
 *   Throwable（所有异常和错误的祖宗）
 *   ├── Error（严重问题，程序基本无法处理）
 *   │     └── OutOfMemoryError, StackOverflowError...
 *   └── Exception（可以处理的异常）
 *         ├── RuntimeException（运行时异常——可以不显式处理）
 *         │     └── NullPointerException, ArrayIndexOutOfBoundsException...
 *         └── 非 RuntimeException（受检异常——必须显式处理）
 *               └── IOException, SQLException...
 *
 *   【受检异常 vs 非受检异常】——面试高频考点！
 *
 *   受检异常 (Checked Exception)：
 *     编译时强制你处理。不处理代码编译不过。
 *     典型：文件找不到、数据库连不上——这些是你无法控制的
 *     处理方式：try-catch 或 throws
 *
 *   非受检异常 (Unchecked Exception) = RuntimeException：
 *     编译时不强制处理。但可能运行时炸。
 *     典型：空指针、数组越界——这些是程序员的 bug，应该修复代码而不是 try-catch
 *
 *
 * 【try-catch-finally 执行顺序（面试经典题）】
 *
 *   1. try 块正常执行
 *   2. 如果有异常 → 跳入匹配的 catch 块
 *   3. finally 块无论如何都会执行（哪怕 try 或 catch 里有 return！）
 *
 *   ⚠️ 唯一不执行 finally 的情况：System.exit(0) 或在 finally 之前 JVM 崩了
 */

import java.io.*;

public class ExceptionTutorial {

    public static void main(String[] args) {

        // ============================================
        // 【1. 基本 try-catch】
        // ============================================
        System.out.println("=== 基本 try-catch ===");

        try {
            // 可能出问题的代码
            int result = 10 / 0;     // ArithmeticException: / by zero
            System.out.println("这行不会执行");  // 上面抛异常后直接跳 catch
        } catch (ArithmeticException e) {
            // 捕获到了，程序不会崩
            System.out.println("捕获到异常: " + e.getMessage());
            // e.printStackTrace();  // 打印完整调用栈（调试时用）
        }

        System.out.println("程序继续运行 ← 如果没有 try-catch 就到不了这里");
        System.out.println();

        // ============================================
        // 【2. try-catch-finally】
        // ============================================
        System.out.println("=== try-catch-finally ===");

        System.out.println("finally 执行顺序演示: " + finallyOrderDemo());
        // 输出: 2 ← 虽然 try 里 return 了，但 finally 先执行！
        System.out.println();

        // ============================================
        // 【3. 自定义异常】
        // ============================================
        System.out.println("=== 自定义异常 ===");

        try {
            register("张三", 16);   // 年龄不够
        } catch (AgeTooYoungException e) {
            System.out.println("注册失败: " + e.getMessage());
        }

        try {
            register("", 20);       // 用户名为空
        } catch (IllegalArgumentException e) {
            System.out.println("注册失败: " + e.getMessage());
        }

        System.out.println();

        // ============================================
        // 【4. try-with-resources（JDK 7+，强烈推荐）】
        // ============================================
        System.out.println("=== try-with-resources ===");

        // 传统写法：finally 里手动 close → 又臭又长还容易忘
        // try-with-resources：自动关闭资源，只要实现了 AutoCloseable 接口就行

        // 演示：读取当前文件自己（只是为了演示语法）
        String fileName = "./java-learning/ch10-exception/ExceptionTutorial.java";

        // try 后面的括号里声明资源 → 无论是否异常，都会自动调用 close()
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String firstLine = reader.readLine();
            System.out.println("文件第一行: " + firstLine);
        } catch (FileNotFoundException e) {
            System.out.println("文件没找到: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("读取文件出错: " + e.getMessage());
        }
        // 不需要写 finally { reader.close(); } ← 自动帮你做了！

        System.out.println();

        // ============================================
        // 【5. 异常处理的最佳实践】
        // ============================================
        System.out.println("=== 异常处理黄金法则 ===");
        System.out.println("1. 不要空 catch（吞掉异常不处理，bug 排查火葬场）");
        System.out.println("2. 能处理才 catch，否则抛给上层（throws）");
        System.out.println("3. 不要 catch Exception 一锅端（太宽泛）");
        System.out.println("4. 优先用 try-with-resources 管理资源");
        System.out.println("5. 用自定义异常表达业务含义（而不是一律 RuntimeException）");
    }

    /**
     * 演示 finally 的执行时机：即使 try 里有 return，finally 也会执行
     */
    private static int finallyOrderDemo() {
        // 实际开发中不要这样写！这只是为了演示。
        // try-catch-finally 中如果有 return，可读性极差。
        try {
            return 1;
        } finally {
            // 这个 return 会覆盖 try 的 return
            return 2;
        }
    }

    /**
     * 模拟用户注册
     */
    private static void register(String username, int age) {
        if (username == null || username.trim().isEmpty()) {
            // IllegalArgumentException 是非受检异常，可以不声明 throws
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (age < 18) {
            // AgeTooYoungException 是我们自定义的受检异常，必须在方法签名声明 throws
            // 但这里主函数演示时我们直接 try-catch 了
            throw new AgeTooYoungException("年龄必须满18岁，当前: " + age);
        }
        System.out.println("注册成功！欢迎 " + username);
    }
}


// ============================================
// 自定义受检异常：继承 Exception
// ============================================
class AgeTooYoungException extends RuntimeException {
    // 如果继承 Exception → 受检异常（调用方必须处理）
    // 如果继承 RuntimeException → 非受检异常（调用方可选处理）
    // 这里用 RuntimeException 是为了演示简洁，实际看业务需求

    public AgeTooYoungException(String message) {
        super(message);   // 调用父类 Exception 的构造方法
    }
}
