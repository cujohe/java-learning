/**
 * =====================================================
 * Ch04: 类与对象、封装——面向对象编程（上）
 * =====================================================
 *
 * 【面向对象最核心的三个问题】
 *
 * Q1: 什么是类(Class)？什么是对象(Object)？
 *   类 = 蓝图/模具/模板
 *   对象 = 根据蓝图造出来的具体实例
 *
 *   类比：
 *     "汽车设计图纸"是类
 *     "你停在楼下的那辆特斯拉"是对象
 *     一辆图纸可以造无数辆车（一个类可以 new 无数个对象）
 *
 * Q2: 为什么要有面向对象？
 *   把数据和对数据的操作打包在一起，代码更好组织、更好维护。
 *   把现实世界的事物直接映射到代码里——人类大脑天生就擅长这种思维。
 *
 * Q3: 封装(Encapsulation)是什么？
 *   把数据藏起来(private)，只暴露必要的方法(public)让外界使用。
 *   好处：保护数据不被乱改，出 bug 时只需检查暴露的方法，不用翻全部代码。
 *
 *   类比：汽车引擎盖下面的复杂线路都被盖住了，你只需要方向盘、刹车、油门就能开车。
 */

public class OOPTutorial {

    public static void main(String[] args) {
        // ============================================
        // 【创建和使用对象】
        // ============================================
        // new 关键字：在堆内存中分配空间，调用构造方法，返回对象的引用

        // "学生"类的蓝图在下面的 Student 类中
        Student stu1 = new Student("张三", 20, 85.5);
        Student stu2 = new Student("李四", 21, 92.0);

        // 通过公开的方法访问和修改数据（封装!）
        stu1.introduce();
        stu2.introduce();

        // 修改数据
        stu1.setScore(90.0);
        System.out.println(stu1.getName() + " 更新后的分数: " + stu1.getScore());

        // 尝试设置非法值 → 被 setter 拦截
        stu1.setAge(-5);    // 输出: 年龄不能为负数！
        stu1.setScore(150); // 输出: 分数必须在0-100之间！
        System.out.println(stu1.getName() + " 的实际年龄仍是: " + stu1.getAge());


        // ============================================
        // 【构造方法详解】
        // ============================================
        // 演示不同构造方法
        Student stu3 = new Student();                    // 无参构造
        Student stu4 = new Student("王五");               // 只给名字
        Student stu5 = new Student("赵六", 22, 76.0);     // 全参构造

        System.out.println("\n--- 不同构造方法创建的对象 ---");
        stu3.introduce();  // 姓名: 未知, 年龄: 0, 分数: 0.0
        stu4.introduce();  // 姓名: 王五, 年龄: 0, 分数: 0.0
        stu5.introduce();  // 姓名: 赵六, 年龄: 22, 分数: 76.0


        // ============================================
        // 【this 关键字】
        // ============================================
        // this 指向"当前对象"
        // 两个主要用途：
        //   1. 区分成员变量和局部变量同名的情况 (this.name = name)
        //   2. 在一个构造方法中调用另一个构造方法 this(...)

        ThisDemo demo = new ThisDemo("Alice");
        demo.print();
    }
}


// ============================================
// 示例类：一个被充分封装的 Student 类
// ============================================
class Student {
    /*
     * 【字段(field)/成员变量——用 private 藏起来】
     *
     * private 的意思是"只有这个类自己的代码能直接访问"
     * 外部代码（包括 main 方法）无法通过 stu.name 直接读写
     * 必须通过 public 的 getter/setter 方法来操作
     */
    private String name;   // 姓名
    private int    age;    // 年龄
    private double score;  // 分数

    /*
     * 【构造方法(Constructor)】
     *
     * 特点：
     *   1. 方法名必须和类名完全一致
     *   2. 没有返回值类型（连 void 都不写）
     *   3. 用 new 创建对象时自动调用
     *   4. 如果没写任何构造方法，Java 会自动提供一个无参的默认构造方法
     *      但如果写了任何一个构造方法，默认构造方法就消失了
     *
     * 重载(Overload)：多个构造方法，参数不同，各有用处
     */

    // 无参构造：给默认值
    public Student() {
        // this(...) 调用另一个构造方法，必须写在第一行
        this("未知", 0, 0.0);
    }

    // 只给名字的构造方法
    public Student(String name) {
        this(name, 0, 0.0);   // 委托给三参构造方法
    }

    // 全参构造方法
    public Student(String name, int age, double score) {
        // this.name 是成员变量（字段），name 是参数
        // 当它们同名时，用 this 区分。不加 this 就是参数（就近原则）
        this.name  = name;
        this.age   = age;
        this.score = score;
    }


    // ========== Getter 和 Setter（封装的核心实现）==========

    // Getter：读取数据，命名规范 getXxx
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getScore() {
        return score;
    }

    // Setter：修改数据，命名规范 setXxx
    // 在这里可以做数据校验——这就是封装的好处！
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("名字不能为空！");
            return;  // 方法直接结束，不设置
        }
        this.name = name;
    }

    public void setAge(int age) {
        if (age < 0 || age > 150) {
            System.out.println("年龄不能为负数，也不能超过150！");
            return;
        }
        this.age = age;
    }

    public void setScore(double score) {
        if (score < 0 || score > 100) {
            System.out.println("分数必须在0-100之间！");
            return;
        }
        this.score = score;
    }


    // ========== 业务方法 ==========

    /**
     * 自我介绍
     */
    public void introduce() {
        System.out.println("姓名: " + name + ", 年龄: " + age + ", 分数: " + score);
    }
}


// ============================================
// this 关键字演示类
// ============================================
class ThisDemo {
    private String name;

    public ThisDemo(String name) {
        this.name = name;   // this.name = 字段, name = 参数
    }

    public void print() {
        System.out.println("\n=== this 演示 ===");
        System.out.println("this.name = " + this.name);
        System.out.println("直接打印 this: " + this);  // this 就是当前对象的引用
        // 在 print() 的上下文中，this 指向调用 print() 的那个对象
    }
}
