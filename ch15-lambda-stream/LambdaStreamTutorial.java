/**
 * =====================================================
 * Ch15: Lambda 表达式与 Stream API
 * =====================================================
 *
 * 【Lambda 是什么？】
 *   一句话：可以当作参数传递的代码块。
 *
 *   在 JDK 8 之前，如果你想传"一段行为"给方法，只能写匿名内部类。
 *   匿名内部类啰嗦得要命 → Lambda 一行搞定。
 *
 *   对比：
 *     匿名内部类：
 *       new Thread(new Runnable() {
 *           public void run() { System.out.println("run"); }
 *       }).start();
 *
 *     Lambda：
 *       new Thread(() -> System.out.println("run")).start();
 *
 *
 * 【Lambda 的语法】
 *   (参数) -> { 方法体 }
 *
 *   如果只有一个参数，括号可以省：  x -> x * 2
 *   如果方法体只有一行，大括号可以省： (a, b) -> a + b
 *
 *
 * 【函数式接口 (Functional Interface)】
 *   Lambda 的"宿主"：只包含一个抽象方法的接口。
 *   比如 Runnable（只有 run()）、Comparator（只有 compare()）
 *   Java 提供了 @FunctionalInterface 注解，加上后如果接口多了一个抽象方法就编译报错。
 *
 *
 * 【Stream API —— Lambda 的主战场】
 *
 *   Stream 不是数据结构，不存数据。
 *   它是一条"流水线"，数据从源头流过一系列操作，最后汇总。
 *
 *   类比：
 *     原始数据 = 一堆未加工的苹果
 *     Stream   = 流水线
 *       .filter()  → 选红色的苹果
 *       .map()     → 削皮
 *       .sorted()  → 按大小排列
 *       .collect() → 装箱
 *
 *   Stream 操作分类：
 *     中间操作（Intermediate）：返回 Stream，可以链式调用
 *       filter, map, flatMap, sorted, distinct, limit, skip...
 *
 *     终端操作（Terminal）：返回结果，调用后 Stream 关闭
 *       collect, forEach, reduce, count, findFirst, anyMatch...
 */

import java.util.*;
import java.util.stream.*;

public class LambdaStreamTutorial {

    public static void main(String[] args) {

        // ============================================
        // 【1. Lambda 基础语法】
        // ============================================
        System.out.println("=== Lambda 基础语法 ===");

        // 无参数
        Runnable r1 = () -> System.out.println("Lambda: 无参数");

        // 一个参数（括号可省）
        java.util.function.Consumer<String> printer = s -> System.out.println(s);

        // 多个参数
        java.util.function.BinaryOperator<Integer> add = (a, b) -> a + b;

        // 多行方法体（需要大括号 + return）
        java.util.function.BiFunction<Integer, Integer, Integer> max =
            (a, b) -> {
                if (a > b) return a;
                else       return b;
            };

        r1.run();
        printer.accept("Lambda: 一个参数");
        System.out.println("10 + 20 = " + add.apply(10, 20));
        System.out.println("max(100, 200) = " + max.apply(100, 200));

        System.out.println();

        // ============================================
        // 【2. 常用的函数式接口】
        // ============================================
        System.out.println("=== 常用函数式接口 ===");
        // JDK 在 java.util.function 包里提供了大量现成的接口
        // 不需要自己定义，直接用：

        // Predicate<T>    : T → boolean           用于判断/过滤
        // Function<T,R>   : T → R                 用于转换
        // Consumer<T>     : T → void              用于消费（打印、存入...）
        // Supplier<T>     : () → T                用于提供（工厂方法）
        // BinaryOperator<T> : (T,T) → T           二元运算
        System.out.println("Predicate: 判断偶数 → " +
            ((java.util.function.Predicate<Integer>) n -> n % 2 == 0).test(10));
        System.out.println("Function: 平方 → " +
            ((java.util.function.Function<Integer, Integer>) n -> n * n).apply(5));
        System.out.println("Supplier: 随机数 → " +
            ((java.util.function.Supplier<Double>) Math::random).get());

        System.out.println();

        // ============================================
        // 【3. 方法引用 —— Lambda 的简写】
        // ============================================
        System.out.println("=== 方法引用 ===");

        // 4 种方法引用：
        // 1. 静态方法引用  → 类名::静态方法
        // 2. 实例方法引用  → 对象::实例方法
        // 3. 特定类实例方法 → 类名::实例方法  （稍特殊）
        // 4. 构造方法引用  → 类名::new

        // Lambda:  s -> System.out.println(s)
        // 方法引用: System.out::println
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        names.forEach(System.out::println);   // 用方法引用简化

        // Lambda:  s -> s.toUpperCase()
        // 方法引用: String::toUpperCase
        List<String> upper = names.stream()
                                  .map(String::toUpperCase)
                                  .collect(Collectors.toList());
        System.out.println("转大写: " + upper);

        System.out.println();

        // ============================================
        // 【4. Stream API 核心操作】
        // ============================================
        System.out.println("=== Stream API ===");

        // 准备数据：一个学生列表
        List<Student2> students = Arrays.asList(
            new Student2("张三", 20, 85),
            new Student2("李四", 22, 92),
            new Student2("王五", 19, 58),
            new Student2("赵六", 21, 76),
            new Student2("孙七", 23, 88),
            new Student2("周八", 20, 95)
        );

        // 需求：找出所有及格的学生的名字，按分数从高到低排序，只取前 3 名

        List<String> topStudents = students.stream()          // 创建流
            .filter(s -> s.score >= 60)                        // 过滤：只留及格的
            .sorted((s1, s2) -> s2.score - s1.score)           // 排序：分数从高到低
            .limit(3)                                           // 截取：只取前3个
            .map(s -> s.name + "(" + s.score + "分)")          // 转换：变成字符串
            .collect(Collectors.toList());                      // 收集：变成 List

        System.out.println("TOP 3 及格学生: " + topStudents);

        // ----- stream 的常用操作演示 -----

        // forEach：遍历（终端操作）
        System.out.print("\n所有学生: ");
        students.stream().forEach(s -> System.out.print(s.name + " "));
        System.out.println();

        // count：计数
        long passCount = students.stream().filter(s -> s.score >= 60).count();
        System.out.println("及格人数: " + passCount);

        // map + collect：提取并收集
        List<String> allNames = students.stream()
                                        .map(s -> s.name)       // 只要名字
                                        .collect(Collectors.toList());
        System.out.println("所有名字: " + allNames);

        // collect 分组：按是否及格分组
        Map<Boolean, List<Student2>> grouped = students.stream()
            .collect(Collectors.partitioningBy(s -> s.score >= 60));
        System.out.println("及格: " + grouped.get(true).size() + " 人");
        System.out.println("不及格: " + grouped.get(false).size() + " 人");

        // reduce：归约——把多个值合并成一个
        int totalScore = students.stream()
                                 .map(s -> s.score)
                                 .reduce(0, (sum, score) -> sum + score);
        System.out.println("总分: " + totalScore + ", 平均: " + (totalScore / students.size()));

        // 统计：IntStream 提供便捷统计方法
        double average = students.stream()
                                 .mapToInt(s -> s.score)     // Stream → IntStream
                                 .average()                   // 求平均值
                                 .orElse(0.0);                // 空时默认值
        System.out.println("平均分: " + average);

        // anyMatch / allMatch / noneMatch：匹配检查
        boolean allPassed = students.stream().allMatch(s -> s.score >= 60);
        boolean anyoneFail  = students.stream().anyMatch(s -> s.score < 60);
        System.out.println("全部及格? " + allPassed);
        System.out.println("有人不及格? " + anyoneFail);
    }
}


// ============================================
// 演示用的学生类
// ============================================
class Student2 {
    String name;
    int    age;
    int    score;

    public Student2(String name, int age, int score) {
        this.name  = name;
        this.age   = age;
        this.score = score;
    }

    @Override
    public String toString() {
        return name + "(" + score + ")";
    }
}
