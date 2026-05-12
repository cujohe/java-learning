/**
 * =====================================================
 * Ch08: static / final / 内部类
 * =====================================================
 *
 * 这三个知识点虽然看似独立，但经常一起出现，而且面试必考。
 *
 * 【static —— "属于类，不属于对象"】
 *
 *   不加 static：每个对象各有一份（实例变量/方法）
 *   加上 static：整个类只有一份，所有对象共享（类变量/方法）
 *
 *   类比：
 *     实例变量 = 每辆车的油量（每辆车不一样）
 *     静态变量 = 汽车的生产厂家（所有车都一样）
 *
 *   规则：
 *     静态方法里不能直接访问实例变量/方法（因为没有 this）
 *     静态方法里可以访问静态变量/方法
 *     实例方法里可以访问一切
 *
 *
 * 【final —— "不可改变的"】
 *
 *   final 变量 → 常量，赋值后不可改
 *   final 方法 → 不能被子类重写
 *   final 类   → 不能被继承
 *
 *
 * 【内部类 —— "类里面定义的类"】
 *
 *   成员内部类：最普通，能访问外部类的所有成员
 *   静态内部类：加了 static，只能访问外部类的静态成员
 *   局部内部类：在方法里面定义的类（罕见）
 *   匿名内部类：没有名字的一次性子类（Lambda 前时代的常用写法）
 */


public class StaticFinalInner {

    public static void main(String[] args) {

        // ============================================
        // 【static 演示】
        // ============================================
        System.out.println("=== static 演示 ===");

        // 静态变量和静态方法可以直接用"类名."调用，不需要创建对象
        System.out.println("物种: " + Animal3.SPECIES);
        Animal3.printSpecies();

        Animal3 a1 = new Animal3("旺财");
        Animal3 a2 = new Animal3("咪咪");

        // 实例变量：每个对象各有一份
        System.out.println("a1 的名字: " + a1.getName());  // 旺财
        System.out.println("a2 的名字: " + a2.getName());  // 咪咪

        // 静态变量：所有对象共享，改一个全改
        Animal3.count = 999;  // 通过类名改（推荐）
        System.out.println("a1.count = " + a1.count);  // 999
        System.out.println("a2.count = " + a2.count);  // 999（同一个变量！）

        // ⚠️ 静态方法中没有 this
        // 因为 static 方法属于类本身，调用时可能没有任何对象存在
        // 所以 static 方法里不能写 this.xxx

        System.out.println();

        // ============================================
        // 【static 代码块】
        // ============================================
        // 静态代码块在类第一次被加载时执行，且只执行一次
        // 常用于初始化静态资源（如加载配置文件、建立数据库连接池）

        System.out.println("=== 静态代码块 ===");
        new StaticBlockDemo();  // 第一次创建 → 触发静态代码块
        new StaticBlockDemo();  // 第二次创建 → 静态代码块不再执行

        System.out.println();

        // ============================================
        // 【final 演示】
        // ============================================
        System.out.println("=== final 演示 ===");

        final double PI = 3.1415926;
        // PI = 3.14;  // ← 编译错误！final 变量不能修改

        final int[] arr = {1, 2, 3};
        arr[0] = 999;      // ← 这没问题！final 保证的是 arr 的引用不变
                            //    但 arr 指向的数组内容可以修改
        // arr = new int[]{4, 5, 6};  // ← 这才不行，因为改了引用

        System.out.println("final 数组: " + java.util.Arrays.toString(arr));

        // final 类：不能被继承 → String 就是 final 的
        // final 方法：不能被重写 → 保护核心逻辑不被篡改

        System.out.println();

        // ============================================
        // 【匿名内部类演示】
        // ============================================
        System.out.println("=== 匿名内部类 ===");

        // 不使用匿名内部类的方式：
        //   1. 定义一个 Greeting 接口的实现类
        //   2. new 这个实现类
        //   3. 调用

        // 使用匿名内部类：一步到位（但语法看着有点怪）
        Greeting english = new Greeting() {
            @Override
            public void greet(String name) {
                System.out.println("Hello, " + name + "!");
            }
        };

        Greeting chinese = new Greeting() {
            @Override
            public void greet(String name) {
                System.out.println("你好，" + name + "！");
            }
        };

        english.greet("Alice");
        chinese.greet("张三");

        // 匿名内部类本质：临时创建一个没有名字的类，继承某个类或实现某个接口
        // 在 Lambda 表达式（Ch15）出现后，大多数场景可以用 Lambda 替代
    }
}


// ============================================
// static 演示类
// ============================================
class Animal3 {
    // 静态变量（类变量）
    public static final String SPECIES = "动物界";
    public static int count = 0;

    // 实例变量
    private String name;

    public Animal3(String name) {
        this.name = name;
        count++;   // 每创建一个对象，count + 1
    }

    // 实例方法：可以访问所有东西
    public String getName() {
        return this.name;
    }

    // 静态方法：只能访问静态成员
    public static void printSpecies() {
        System.out.println("这个类的物种是: " + SPECIES);
        // System.out.println(this.name);  // ← 编译错误！静态方法里没有 this
        // System.out.println(name);       // ← 编译错误！不能直接访问实例变量
    }
}


// ============================================
// 静态代码块演示
// ============================================
class StaticBlockDemo {
    // 静态代码块
    static {
        System.out.println("  → 静态代码块执行了！（类加载时执行，仅一次）");
    }

    // 实例代码块（每次 new 对象时执行，在构造方法之前）
    {
        System.out.println("  → 实例代码块执行了");
    }

    public StaticBlockDemo() {
        System.out.println("  → 构造方法执行了");
    }
}


// ============================================
// final 演示类
// ============================================
// final class 不能被继承
final class CannotBeExtended {
    // 普通类的内容
}

// 一个演示 final 方法的类
class Parent {
    // 这个方法子类不能重写
    public final void criticalLogic() {
        System.out.println("核心逻辑，不能被子类篡改");
    }

    // 这个方法子类可以重写
    public void flexibleLogic() {
        System.out.println("灵活逻辑，欢迎重写");
    }
}


// ============================================
// 匿名内部类用到的接口
// ============================================
interface Greeting {
    void greet(String name);
}
