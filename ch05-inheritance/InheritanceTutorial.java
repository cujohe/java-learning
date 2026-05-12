/**
 * =====================================================
 * Ch05: 继承（Inheritance）
 * =====================================================
 *
 * 【继承是什么？】
 *   子类自动拥有父类的所有属性和方法（除了 private 的不能直接访问）。
 *   用 extends 关键字。
 *
 *   类比：
 *     "动物"是父类（有名字、年龄、会吃东西）
 *     "狗"是子类（除了名字、年龄、吃东西，还会汪汪叫）
 *     狗 extends 动物 → 狗自动有了动物的所有能力，再加上自己特有的
 *
 * 【为什么需要继承？】
 *   核心目的：代码复用 + 建立"is-a"关系
 *   狗 is-a 动物 ✓    → 适合继承
 *   汽车 has-a 引擎 ✓  → 不适合继承（应该用组合）
 *
 * 【Java 继承的规则】
 *   1. 单继承：一个类只能有一个直接父类（像人类的单亲遗传，但可以通过接口多实现）
 *   2. 所有类默认继承 Object 类（Java 的终极祖先）
 *   3. 构造方法不会被继承，但子类必须调用父类的构造方法（super）
 *   4. private 的属性和方法不会被继承（但确实存在于子类对象的内存中）
 */

public class InheritanceTutorial {

    public static void main(String[] args) {
        // ============================================
        // 【基本继承演示】
        // ============================================
        System.out.println("=== 继承演示 ===");

        Dog myDog = new Dog("旺财", 3, "金毛");
        myDog.eat();          // 继承自 Animal 的方法
        myDog.sleep();        // 继承自 Animal 的方法
        myDog.bark();         // Dog 自己的方法
        System.out.println(myDog);  // 自动调用 toString()

        System.out.println();

        Cat myCat = new Cat("咪咪", 2, "英短");
        myCat.eat();          // Cat 重写了 eat() 方法
        myCat.meow();

        System.out.println();

        // ============================================
        // 【多态的前奏：父类引用指向子类对象】
        // ============================================
        // 这就是多态的基础——将在 Ch06 详细展开
        Animal animalRef = new Dog("阿黄", 1, "土狗");
        animalRef.eat();     // 调用的是 Dog 的 eat()（如果重写了）还是 Animal 的 eat()？
                             // 答：Dog 重写过的！这就是动态绑定/多态
        // animalRef.bark(); // ← 编译错误！因为 Animal 类型没有 bark() 方法
                             // 编译器只看引用类型，运行时才找实际对象的方法


        // ============================================
        // 【super 关键字】
        // ============================================
        System.out.println("=== super 演示 ===");
        Dog puppy = new Dog("小旺", 1, "柯基");
        puppy.callSuperEat();  // 调用父类的 eat() 版本
    }
}


// ============================================
// 父类：动物
// ============================================
class Animal {
    // protected: 自己+子类+同包可以访问，外部不行
    // 介于 private（仅自己）和 public（所有人）之间
    protected String name;
    protected int    age;

    // 父类构造方法
    public Animal(String name, int age) {
        this.name = name;
        this.age  = age;
        System.out.println("Animal 构造方法被调用: " + name);
    }

    // 父类的无参构造方法（如果父类只有有参构造，子类必须显式用 super 调用）
    public Animal() {
        this("无名", 0);
    }

    public void eat() {
        System.out.println(name + " 在吃东西");
    }

    public void sleep() {
        System.out.println(name + " 在睡觉");
    }

    // Object 类就有 toString()，这里重写它
    @Override
    public String toString() {
        return "Animal{name='" + name + "', age=" + age + "}";
    }
}


// ============================================
// 子类：狗
// ============================================
class Dog extends Animal {
    private String breed;  // 品种（狗特有的属性）

    // 子类构造方法
    public Dog(String name, int age, String breed) {
        // super() 必须写在构造方法的第一行
        // 它调用父类的构造方法，完成父类部分的初始化
        super(name, age);
        this.breed = breed;
        System.out.println("Dog 构造方法被调用: " + name + " (" + breed + ")");
    }

    // 子类自己的方法
    public void bark() {
        // 可以直接用 name 和 age，因为它们是 protected 的（不是 private）
        System.out.println(name + " 汪汪叫！ (品种: " + breed + ")");
    }

    // 重写(Override)父类的方法：加自己的逻辑
    @Override
    public void eat() {
        super.eat();  // 先调用父类的 eat()
        System.out.println("  → 但其实 " + name + " 吃得更快！");  // 再加自己的
    }

    // 演示如何调用父类被重写的方法
    public void callSuperEat() {
        System.out.print("调用父类的原始 eat(): ");
        super.eat();  // 跳过自己的重写，直接调用父类的
    }

    @Override
    public String toString() {
        return "Dog{name='" + name + "', age=" + age + ", breed='" + breed + "'}";
    }
}


// ============================================
// 子类：猫（演示另一个子类）
// ============================================
class Cat extends Animal {
    private String breed;

    public Cat(String name, int age, String breed) {
        super(name, age);
        this.breed = breed;
    }

    @Override
    public void eat() {
        System.out.println(name + " 优雅地吃着猫粮...");
    }

    public void meow() {
        System.out.println(name + " 喵喵叫！");
    }
}
