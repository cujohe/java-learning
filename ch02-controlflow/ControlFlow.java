/**
 * =====================================================
 * Ch02: 控制流（Control Flow）
 * =====================================================
 *
 * 程序默认是"从上到下逐行执行"，控制流就是改变这个默认顺序。
 * 三种基本结构：
 *   1. 顺序结构：一行接一行（默认）
 *   2. 分支结构：根据条件选择执行哪些代码（if/else, switch）
 *   3. 循环结构：重复执行某段代码（for, while, do-while）
 *
 * 理论上任何程序只需要这三种结构就能写完。（结构化编程定理）
 */

public class ControlFlow {

    public static void main(String[] args) {

        // ============================================
        // 【1. if / else if / else】
        // ============================================
        //
        // 语法:
        //   if (条件表达式) {
        //       条件为 true 时执行
        //   } else if (另一个条件) {
        //       上面为 false 且 这个条件为 true 时执行
        //   } else {
        //       所有条件都不满足时执行
        //   }
        //
        // ⚠️ 条件表达式必须是 boolean 类型（Java 不接受 0/1 当 true/false）

        int score = 82;

        System.out.println("=== if/else 演示 ===");

        // 单 if：最简形式
        if (score >= 90) {
            System.out.println("优秀");
        }

        // if-else：二选一
        if (score >= 60) {
            System.out.println("通过");
        } else {
            System.out.println("不及格");
        }

        // if-else if-else：多选一（按顺序匹配，匹配到就停止）
        if (score >= 90) {
            System.out.println("等级: A");
        } else if (score >= 80) {
            System.out.println("等级: B");       // 82 命中这里
        } else if (score >= 70) {
            System.out.println("等级: C");
        } else if (score >= 60) {
            System.out.println("等级: D");
        } else {
            System.out.println("等级: F");
        }

        // ⚠️ 常见错误：把 = (赋值) 当成 == (比较)
        // if (score = 100) { ... }  ← 编译报错，因为 score=100 的类型是 int 不是 boolean
        // 在 Java 里这种错误能被编译器拦截，不像 C/C++ 直接给你一个 bug


        // ============================================
        // 【2. switch】
        // ============================================
        //
        // 本质是"多路跳转"，比一长串 if-else 更清晰
        // JDK 14+ 支持箭头语法(->) 和 表达式形式，更推荐
        //
        // switch 括号内支持的类型: byte, short, int, char, String, enum
        // ⚠️ 不支持 long, float, double, boolean

        System.out.println("\n=== switch 演示 ===");

        int dayOfWeek = 3;

        // 传统写法（需要 break，否则会"穿透"到下一个 case）
        // "穿透"有时有用（多个 case 共享同一逻辑），但更多时候是 bug
        switch (dayOfWeek) {
            case 1:
                System.out.println("星期一：困");
                break;    // 没有 break 会继续执行 case 2！
            case 2:
                System.out.println("星期二：还是困");
                break;
            case 3:
                System.out.println("星期三：小周末");
                break;
            case 4:
                System.out.println("星期四：快周五了");
                break;
            case 5:
                System.out.println("星期五：不困了");
                break;
            case 6:
            case 7:       // 故意不写 break，6 和 7 共享"周末"输出
                System.out.println("周末：睡到自然醒");
                break;
            default:      // 相当于 else，以上都不匹配时执行
                System.out.println("无效的星期数");
        }

        // 新式写法（JDK 14+，推荐）:
        // 用 -> 代替冒号+break，代码更干净
        String dayType = switch (dayOfWeek) {
            case 1, 2, 3, 4, 5 -> "工作日";
            case 6, 7           -> "周末";
            default             -> "未知";
        };
        System.out.println("类型: " + dayType);


        // ============================================
        // 【3. for 循环】
        // ============================================
        //
        // 最适合"已知循环次数"的场景
        //
        // 语法:
        //   for (初始化; 条件; 更新) { 循环体 }
        //
        // 执行顺序:
        //   初始化 → (检查条件 → 执行循环体 → 更新) 重复 → 条件为 false 退出

        System.out.println("\n=== for 循环演示 ===");

        // 经典用法：打印 1 到 5
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();  // 换行

        // for 的三个部分都可以为空（但分号不能省）
        // for (;;) { ... }  ← 这是无限循环，等价于 while(true)
        // 变量 i 的作用域只在 for 循环体内部，循环结束 i 就销毁了

        // 嵌套 for：打印九九乘法表
        System.out.println("\n九九乘法表:");
        for (int row = 1; row <= 9; row++) {
            for (int col = 1; col <= row; col++) {
                System.out.print(col + "×" + row + "=" + (row * col) + "\t");
            }
            System.out.println();  // 每行结束换行
        }


        // ============================================
        // 【4. while 循环】
        // ============================================
        //
        // 最适合"不知道循环多少次，只知道退出条件"的场景
        // 语法: while (条件) { ... }
        // 特点: 如果一开始条件就不满足，循环体一次都不执行

        System.out.println("\n=== while 循环演示 ===");

        // 示例：统计一个数字有几位（除到 0 为止）
        int number = 2026;
        int digits  = 0;
        int temp    = number;

        while (temp > 0) {
            temp = temp / 10;   // 每次砍掉最后一位
            digits++;
        }
        System.out.println(number + " 有 " + digits + " 位数字");


        // ============================================
        // 【5. do-while 循环】
        // ============================================
        //
        // 和 while 的唯一区别：先执行一次循环体，再检查条件
        // 所以 do-while 的循环体至少执行一次
        //
        // 使用场景极少，但要知道它的存在（主要是用户输入验证等场景）

        System.out.println("\n=== do-while 演示 ===");

        int count = 5;
        do {
            System.out.println("count = " + count);
            count--;
        } while (count > 0);
        // 输出: 5, 4, 3, 2, 1


        // ============================================
        // 【6. break 和 continue】
        // ============================================
        //
        // break    : 直接跳出当前循环（循环提前结束）
        // continue : 跳过本次循环的剩余代码，进入下一次迭代
        //
        // 两者都可以配合标签(Label)跳出多层循环，但不推荐滥用

        System.out.println("\n=== break 和 continue 演示 ===");

        // break 示例：找到第一个能被 7 整除的数就停
        System.out.print("break 演示: ");
        for (int i = 1; i <= 50; i++) {
            if (i % 7 == 0) {
                System.out.println("第一个7的倍数是 " + i);
                break;  // 跳出循环
            }
        }

        // continue 示例：只打印奇数（跳过偶数）
        System.out.print("continue 演示 (1-10的奇数): ");
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                continue;  // 偶数 → 跳过本次，不打印
            }
            System.out.print(i + " ");
        }
        System.out.println();

        // 标签示例（了解即可，现实中很少用）
        System.out.print("带标签的 break 演示: ");
        outerLoop:           // 给外层循环贴个标签
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (i == 2 && j == 2) {
                    break outerLoop;  // 跳出外层循环，而不只是内层
                }
                System.out.print("(" + i + "," + j + ") ");
            }
        }
        System.out.println("← 在 (2,2) 处跳出外层");
    }
}
