/**
 * =====================================================
 * Ch07: abstract 与 interface
 * =====================================================
 *
 * 这两个东西经常被混淆，但其实区别非常明确。
 * 一句话区分：
 *   abstract class：is-a 关系，"是什么"（模板继承）
 *   interface：     can-do 能力，"能干什么"（行为契约）
 *
 * 【抽象类 (abstract class)】
 *   场景：父类本身不应该被实例化，因为太"抽象"了
 *
 *   例如：
 *     Shape（形状）—— 你不能 new 一个"形状"，因为不知道它长什么样
 *     但 Circle、Rectangle 可以 new，因为它们有具体的样子
 *
 *   规则：
 *   1. 有 abstract 关键字的类是抽象类，不能被 new
 *   2. 有 abstract 关键字的方法是抽象方法，没有方法体（强逼子类实现）
 *   3. 抽象类可以有普通方法（子类直接继承使用）
 *   4. 抽象类可以有构造方法（给子类用的）
 *   5. 一个类只能继承一个抽象类（单继承）
 *
 *
 * 【接口 (interface)】
 *   场景：定义一组行为规范，不关心是谁实现的
 *
 *   例如：
 *     Flyable（能飞的）—— 鸟能飞，飞机能飞，超人能飞
 *     它们之间没有任何"is-a"关系，但有共同的"can-do"能力
 *
 *   规则：
 *   1. JDK 8+ 接口可以有 default 方法（带方法体）和 static 方法
 *   2. 接口里的变量默认是 public static final（常量）
 *   3. 接口里的方法默认是 public abstract（JDK 8 前）
 *   4. 一个类可以实现多个接口（弥补单继承的局限）
 */


public class AbstractAndInterface {

    public static void main(String[] args) {

        // ============================================
        // 【抽象类演示】—— 模板模式
        // ============================================
        System.out.println("=== 抽象类演示 ===");

        // Shape s = new Shape("形状");  // ← 编译错误！不能实例化抽象类

        Shape circle = new Circle("红色圆形", 5.0);
        Shape rect   = new Rectangle("蓝色矩形", 4.0, 6.0);

        // 多态：同样的 calculateArea() 调用，不同的实现
        circle.display();  // 抽象类的普通方法（子类直接用的）
        rect.display();

        System.out.println();

        // ============================================
        // 【接口演示】—— 能力组合
        // ============================================
        System.out.println("=== 接口演示 ===");

        // 一个类实现多个接口
        Superman clark = new Superman("克拉克");
        clark.fly();       // Flyable 接口
        clark.swim();      // Swimmable 接口
        clark.introduce(); // Superman 自己的方法

        System.out.println();

        // ============================================
        // 【接口的多态——和抽象类一样的用法】
        // ============================================
        System.out.println("=== 接口多态 ===");

        // 只要是 Flyable 的，都能让它飞
        Flyable[] flyers = {
            new Bird("麻雀"),
            new Airplane("波音747"),
            new Superman("超人")
        };

        for (Flyable f : flyers) {
            f.fly();  // 不管实际是什么，都能飞
        }

        System.out.println();

        // ============================================
        // 【default 方法演示（JDK 8+）】
        // ============================================
        // 为什么要 default 方法？
        // 如果接口需要增加新方法，所有实现类都必须修改，太痛苦了
        // default 方法 = 接口里的普通方法，子类可以直接用，也可以选择重写

        System.out.println("=== default 方法演示 ===");
        Bird sparrow = new Bird("麻雀");
        sparrow.takeOff();          // Flyable 的 default 方法
        sparrow.emergencyLanding(); // 也是 default 方法

        Airplane plane = new Airplane("空客A380");
        plane.takeOff();            // Airplane 重写了 takeOff()
    }
}


// ============================================
// 抽象类：Shape
// ============================================
abstract class Shape {
    protected String description;

    public Shape(String description) {
        this.description = description;
    }

    // 抽象方法：没有方法体，子类必须实现
    // 这就像父类说："我知道形状能算面积，但怎么算我不知道，子类你们自己看着办"
    public abstract double calculateArea();

    // 普通方法：子类直接继承，不需要重写
    public void display() {
        System.out.println(description + " → 面积: " + calculateArea());
    }
}

class Circle extends Shape {
    private double radius;

    public Circle(String description, double radius) {
        super(description);   // 调用抽象父类的构造方法（抽象类可以有构造方法！）
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;  // πr²
    }
}

class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(String description, double width, double height) {
        super(description);
        this.width  = width;
        this.height = height;
    }

    @Override
    public double calculateArea() {
        return width * height;  // 长 × 宽
    }
}


// ============================================
// 接口：Flyable
// ============================================
interface Flyable {
    // 抽象方法（默认 public abstract，可以不写）
    void fly();

    // default 方法：有方法体，实现类可以直接用，也可以重写
    default void takeOff() {
        System.out.println("准备起飞...");
        fly();          // default 方法可以调用抽象方法（运行时由实际对象提供实现）
        System.out.println("已升空！");
    }

    default void emergencyLanding() {
        System.out.println("紧急降落！");
    }

    // static 方法：属于接口本身，不用实现类就能调用：Flyable.maxAltitude()
    static int maxAltitude() {
        return 12000;  // 一般飞行高度上限（米）
    }
}

// 接口：Swimmable
interface Swimmable {
    void swim();
}


// ============================================
// 实现多个接口的类
// ============================================
class Bird implements Flyable {
    private String name;

    public Bird(String name) {
        this.name = name;
    }

    @Override
    public void fly() {
        System.out.println(name + " 扑打着翅膀飞行");
    }
}

class Airplane implements Flyable {
    private String model;

    public Airplane(String model) {
        this.model = model;
    }

    @Override
    public void fly() {
        System.out.println(model + " 引擎轰鸣，冲上云霄！");
    }

    @Override
    public void takeOff() {
        System.out.println(model + ": 塔台，请求起飞！");
        System.out.println(model + ": 跑道加速中...V1, V2, Rotate!");
    }
}

// 一个类实现两个接口（这是接口相对抽象类的巨大优势）
class Superman implements Flyable, Swimmable {
    private String name;

    public Superman(String name) {
        this.name = name;
    }

    @Override
    public void fly() {
        System.out.println(name + " 双手向前，直接飞起来了！");
    }

    @Override
    public void swim() {
        System.out.println(name + " 以超音速潜泳");
    }

    public void introduce() {
        System.out.println("我是 " + name + "，我能飞也能游");
    }
}
