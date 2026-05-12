/**
 * =====================================================
 * Ch06: 多态（Polymorphism）
 * =====================================================
 *
 * 【多态——面向对象最强大也最难理解的概念】
 *
 * 多态的字面意思："多种形态"
 * 同一个方法调用，根据对象的实际类型不同，表现出不同的行为。
 *
 * 先记住一句话：
 *   "编译看左边，运行看右边"
 *
 *   左边 = 引用变量的类型（编译时类型）
 *   右边 = 实际对象的类型（运行时类型）
 *
 * 【一个经典例子说明一切】
 *
 *   Animal a = new Dog();
 *   a.eat();  // 调用的是 Dog 的 eat()，不是 Animal 的 eat()
 *
 *   编译时：编译器检查 Animal 有没有 eat() 方法 → 有，通过编译
 *   运行时：JVM 发现 a 实际指向 Dog 对象 → 调用 Dog 的 eat()
 *
 *   这就是"动态绑定"(Dynamic Binding) / "运行时多态"
 *
 * 【多态的三个必要条件】
 *   1. 有继承关系（或接口实现）
 *   2. 子类重写父类方法
 *   3. 父类引用指向子类对象
 *
 * 【多态的价值】
 *   不写重复代码。比如你有一个动物园，里面有狗、猫、鸟...
 *   不用为每种动物写一份喂食逻辑，而是统一写：
 *     for (Animal a : animals) { a.eat(); }
 *   新增一种动物时，只要继承 Animal 并重写 eat()，喂食代码一行不用改。
 *
 *   → 这就是"开闭原则"：对扩展开放，对修改关闭
 */

public class PolymorphismTutorial {

    public static void main(String[] args) {
        // ============================================
        // 【1. 多态的基本演示】
        // ============================================

        System.out.println("=== 多态基本演示 ===");

        // 创建不同子类对象，但都用父类引用接收
        Animal2 a1 = new Dog2("旺财");
        Animal2 a2 = new Cat2("咪咪");
        Animal2 a3 = new Bird2("小翠");

        // 同样的方法调用 a.makeSound()，不同的实际行为
        a1.makeSound();  // 旺财: 汪汪！
        a2.makeSound();  // 咪咪: 喵喵！
        a3.makeSound();  // 小翠: 啾啾！

        System.out.println();

        // ============================================
        // 【2. 多态的实际价值：统一处理】
        // ============================================

        System.out.println("=== 多态价值：批量处理 ===");

        // 不同类型的动物放到同一个数组里
        Animal2[] zoo = {
            new Dog2("阿黄"),
            new Cat2("小花"),
            new Bird2("啾啾"),
            new Dog2("小黑"),
            new Cat2("小白")
        };

        // 不管具体是什么动物，让它们都叫一遍
        // 新增第10种动物时，这段循环代码完全不用改！
        for (Animal2 animal : zoo) {
            animal.makeSound();
        }

        System.out.println();

        // ============================================
        // 【3. 多态的局限与向下转型】
        // ============================================

        System.out.println("=== 转型(Casting) ===");

        Animal2 ref = new Dog2("大黄");

        // ref.fetch();  // ← 编译错误！Animal2 类型没有 fetch() 方法
                         // 虽然 ref 实际指向 Dog2，但编译器只看引用类型

        // 向下转型 (Downcasting)：父类引用 → 子类引用
        // 前提：你确定这个对象真的是那个子类
        if (ref instanceof Dog2) {        // instanceof: 检查对象是否是某个类的实例
            Dog2 dogRef = (Dog2) ref;     // 强制转换
            dogRef.fetch();               // 现在可以调用 Dog2 特有的方法了
        }

        // ⚠️ 转型的风险：
        // Animal2 a = new Cat2("猫");
        // Dog2 d = (Dog2) a;  // 运行时 ClassCastException！猫不是狗！
        // 所以转型前一定用 instanceof 检查

        // instanceof 的现代写法（JDK 16+ 模式匹配）：
        if (ref instanceof Dog2 dogRef) {  // 检查+转型+声明 一步到位
            dogRef.fetch();
        }

        System.out.println();

        // ============================================
        // 【4. 多态中的方法调用链】
        // ============================================
        // 如果子类没有重写，就调用父类的
        // 如果子类重写了，就调用子类的

        System.out.println("=== 方法调用链 ===");

        Animal2 d = new Dog2("多态犬");
        d.eat();          // Dog2 重写了 eat() → 调用 Dog2 的版本
        d.commonBehavior();// Animal2 的方法，Dog2 没重写 → 调用 Animal2 的版本
    }
}


// ============================================
// 父类
// ============================================
class Animal2 {
    protected String name;

    public Animal2(String name) {
        this.name = name;
    }

    // 这个方法默认实现：所有动物都能叫，但每种叫声不同
    public void makeSound() {
        System.out.println(name + ": ...（不知道哪种动物，发不出声）");
    }

    // 通用行为：所有动物都会吃
    public void eat() {
        System.out.println(name + " 在吃东西");
    }

    // 通用行为：不被重写
    public void commonBehavior() {
        System.out.println(name + ": 所有动物都需要呼吸");
    }
}


// ============================================
// 子类：狗
// ============================================
class Dog2 extends Animal2 {
    public Dog2(String name) {
        super(name);
    }

    @Override
    public void makeSound() {
        System.out.println(name + ": 汪汪！🐶");
    }

    @Override
    public void eat() {
        System.out.println(name + ": 狼吞虎咽地吃狗粮");
    }

    // Dog 独有的方法
    public void fetch() {
        System.out.println(name + " 去捡飞盘了！");
    }
}


// ============================================
// 子类：猫
// ============================================
class Cat2 extends Animal2 {
    public Cat2(String name) {
        super(name);
    }

    @Override
    public void makeSound() {
        System.out.println(name + ": 喵喵！🐱");
    }

    @Override
    public void eat() {
        System.out.println(name + ": 优雅地吃猫粮");
    }
}


// ============================================
// 子类：鸟
// ============================================
class Bird2 extends Animal2 {
    public Bird2(String name) {
        super(name);
    }

    @Override
    public void makeSound() {
        System.out.println(name + ": 啾啾！🐦");
    }

    // 鸟没有重写 eat()，所以用父类的默认吃法
    // 也没有重写 commonBehavior()，用父类的
}
