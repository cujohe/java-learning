/**
 * =====================================================
 * Ch01-2: 运算符（Operators）
 * =====================================================
 *
 * 【运算符优先级速查（高→低）】
 *   () 括号 → 最高，想先算什么就加括号
 *   ! ~ ++ --  一元运算符
 *   * / %
 *   + -
 *   < <= > >= 比较
 *   == !=     相等
 *   &&        逻辑与
 *   ||        逻辑或
 *   =         赋值 → 最低
 *
 * 记不住？加括号！括号永远最优先，而且让代码可读性暴增。
 */

public class Operators {

    public static void main(String[] args) {
        // ============================================
        // 1. 算术运算符: + - * / %
        // ============================================
        int a = 10;
        int b = 3;

        System.out.println("=== 算术运算符 ===");
        System.out.println("a + b = " + (a + b));   // 13
        System.out.println("a - b = " + (a - b));   // 7
        System.out.println("a * b = " + (a * b));   // 30
        System.out.println("a / b = " + (a / b));   // 3  ← 注意！整数除法截断，不是 3.33
        System.out.println("a % b = " + (a % b));   // 1  ← 取余（模运算）

        // 要想得到小数结果，必须先转成浮点：
        double preciseResult = (double) a / b;       // 3.3333...
        System.out.println("(double)a / b = " + preciseResult);

        // % 取余的妙用：
        System.out.println("判断奇偶: 10%2=" + (10 % 2) + " (0=偶), 11%2=" + (11 % 2) + " (1=奇)");


        // ============================================
        // 2. 自增/自减运算符: ++ --
        // ============================================
        // 这是初学者最容易踩的坑，要彻底理解"前置"和"后置"的区别
        //
        // ++i (前置): 先把 i 加 1，再加 1 后的值参与当前表达式
        // i++ (后置): 先让 i 的旧值参与当前表达式，然后把 i 加 1
        //
        // 用记忆口诀：符号在前面就先加，符号在后面就后加

        int i = 5;

        // 后置 ++
        int x = i++;   // 等价于: int x = i; i = i + 1; → x=5, i=6

        // 前置 ++
        int j = 5;
        int y = ++j;   // 等价于: j = j + 1; int y = j; → y=6, j=6

        System.out.println("\n=== 自增/自减 ===");
        System.out.println("i++: x=" + x + ", i=" + i);   // x=5, i=6
        System.out.println("++j: y=" + y + ", j=" + j);   // y=6, j=6

        // ⚠️ 经典陷阱（面试常考）：
        int n = 3;
        int m = n++ + ++n;  // 拆解:
                            // n++ → 返回3, n变成4
                            // ++n → n变成5, 返回5
                            // 所以 m = 3 + 5 = 8
        System.out.println("n++ + ++n (n初始为3): m=" + m + ", n=" + n);  // m=8, n=5


        // ============================================
        // 3. 比较/关系运算符: == != < > <= >=
        // ============================================
        // 比较运算的结果是 boolean (true/false)

        System.out.println("\n=== 比较运算符 ===");
        System.out.println("5 == 5 : " + (5 == 5));  // true
        System.out.println("5 != 3 : " + (5 != 3));  // true
        System.out.println("3 > 5  : " + (3 > 5));   // false

        // ⚠️ == 对引用类型比较的是"地址是否相同"，不是"内容是否相同"
        // 这个问题在 Ch09 String 会详细展开
        String s1 = new String("hello");   // 在堆里新创建一个对象
        String s2 = new String("hello");   // 又创建一个新对象，地址不同
        System.out.println("两个 new String(\"hello\") 用 == 比较: " + (s1 == s2));
        System.out.println("正确做法用 .equals(): " + s1.equals(s2));


        // ============================================
        // 4. 逻辑运算符: && || !
        // ============================================
        // && (与): 两边都为 true 才为 true  —— "并且"
        // || (或): 只要一边为 true 就为 true —— "或者"
        // !  (非): 取反                       —— "不是"
        //
        // 【短路求值 (Short-circuit)】—— 重要！
        // &&: 左边为 false 时，右边不会执行（因为已经确定结果是 false 了）
        // ||: 左边为 true  时，右边不会执行（因为已经确定结果是 true 了）

        int age = 20;
        boolean hasTicket = true;
        System.out.println("\n=== 逻辑运算符 ===");
        System.out.println("年龄>=18 且 有票: " + (age >= 18 && hasTicket));  // true

        // 短路求值验证：
        int left = 10;
        // 左边 false→短路→rightTest() 没有被调用
        boolean shortCircuit = (left < 5) && rightTest();
        System.out.println("短路验证 (left<5 && rightTest()): " + shortCircuit);

        // 非短路版本: & 和 | （单个符号）不短路，两边都会执行
        // 日常开发用 && || 就够了


        // ============================================
        // 5. 赋值运算符: = += -= *= /= %=
        // ============================================
        // a += b  等价于  a = a + b
        // 这些叫"复合赋值运算符"，写起来更简洁

        int num = 10;
        num += 5;   // num = num + 5 = 15
        num *= 2;   // num = num * 2 = 30
        num %= 7;   // num = num % 7 = 2

        System.out.println("\n=== 赋值运算符 ===");
        System.out.println("num 经过 +=5 *=2 %=7 后: " + num);  // 2

        // 复合赋值运算符自带强制类型转换！
        short sh = 10;
        // sh = sh + 1;   ← 这行会编译报错！因为 sh+1 结果是 int，不能直接赋给 short
        sh += 1;          // ← 这行没问题！+= 内部帮你做了 (short)
        System.out.println("short += 1: " + sh);


        // ============================================
        // 6. 三元（条件）运算符: 条件 ? 值1 : 值2
        // ============================================
        // 条件为 true 返回值1，否则返回值2
        // 它是 if-else 的简写，但更适合用在表达式中

        int score = 85;
        String grade = (score >= 60) ? "及格" : "不及格";
        int max = (a > b) ? a : b;  // 取两者中较大的值

        System.out.println("\n=== 三元运算符 ===");
        System.out.println("分数" + score + ": " + grade);
        System.out.println("a=10, b=3, 较大者: " + max);
    }

    // 辅助方法：验证短路求值用
    private static boolean rightTest() {
        System.out.println("  → rightTest() 被调用了！说明没有短路");
        return true;
    }
}
