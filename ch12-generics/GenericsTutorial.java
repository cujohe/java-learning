/**
 * =====================================================
 * Ch12: 泛型（Generics）
 * =====================================================
 *
 * 【泛型是什么？】
 *   让一个类或方法可以处理"多种类型"的数据，同时保证类型安全。
 *   核心目的：把运行时可能出现的 ClassCastException 提前到编译时发现。
 *
 *   类比：
 *     没有泛型的集合 = 一个什么都装的麻袋
 *       → 你往里面扔了苹果、铅笔、袜子，拿出来时不知道是什么，可能搞错
 *
 *     有泛型的集合 = 一个只装苹果的篮子
 *       → 往篮子里塞铅笔？编译器直接报错！拿出来的肯定是苹果！
 *
 *
 * 【泛型擦除——Java 泛型的核心机制（面试高频）】
 *
 *   Java 的泛型是"编译时"的，编译后泛型信息会被擦除。
 *   List<String> 和 List<Integer> 在运行时是同一个类型：List
 *   ——这就是为什么不能写 new T()、不能用 instanceof 检查泛型类型
 *
 *   这个设计的目的是向后兼容（Java 5 才引入泛型，之前的代码全是原始类型）
 */


public class GenericsTutorial {

    public static void main(String[] args) {

        // ============================================
        // 【1. 为什么需要泛型？——一个血淋淋的例子】
        // ============================================
        System.out.println("=== 为什么需要泛型？ ===");

        // 没有泛型的时代（Java 5 之前）
        List oldList = new ArrayList();     // 原始类型 (Raw Type)
        oldList.add("Hello");
        oldList.add(123);                   // 能存任何东西——但这是灾难！
        oldList.add(new Date());

        // 取出来时不知道类型，强转可能炸
        String item = (String) oldList.get(0);  // OK
        System.out.println("第一个元素: " + item);

        // String crash = (String) oldList.get(1);  // ← 运行时 ClassCastException！
        // 编译器不报错，但运行就炸 —— 这就是没有泛型的代价

        // 有了泛型之后：
        List<String> safeList = new ArrayList<>();
        safeList.add("Hello");
        // safeList.add(123);  // ← 编译错误！在炸之前就被拦截了

        System.out.println();

        // ============================================
        // 【2. 泛型类】
        // ============================================
        System.out.println("=== 泛型类 ===");

        // 同一个 Box 类，可以装不同类型
        Box<String> stringBox = new Box<>("一本书");
        Box<Integer> intBox    = new Box<>(42);
        Box<Double> doubleBox  = new Box<>(3.14);

        System.out.println("stringBox: " + stringBox.get());
        System.out.println("intBox:    " + intBox.get());
        System.out.println("doubleBox: " + doubleBox.get());

        System.out.println();

        // ============================================
        // 【3. 泛型接口】
        // ============================================
        System.out.println("=== 泛型接口 ===");

        Comparator<String> lengthComparator = new LengthComparator();
        int cmpResult = lengthComparator.compare("apple", "banana");
        System.out.println("比较 'apple' 和 'banana' 的长度: " + cmpResult + " (负数=前者短)");

        System.out.println();

        // ============================================
        // 【4. 泛型方法】
        // ============================================
        System.out.println("=== 泛型方法 ===");

        // 同一个方法，不同类型的参数，不需要重载写三遍！
        String[] strArr = {"Java", "Python", "Go"};
        Integer[] intArr = {1, 2, 3, 4, 5};
        Double[] dblArr = {1.1, 2.2, 3.3};

        System.out.println("字符串数组中间元素: " + getMiddle(strArr));  // Python
        System.out.println("整数数组中间元素:   " + getMiddle(intArr));  // 3
        System.out.println("浮点数组中间元素:   " + getMiddle(dblArr));  // 2.2

        System.out.println();

        // ============================================
        // 【5. 通配符 ? —— 泛型的灵活运用】
        // ============================================
        System.out.println("=== 通配符 ===");

        List<Integer> intList    = Arrays.asList(1, 2, 3);
        List<Double>  doubleList = Arrays.asList(1.1, 2.2, 3.3);
        List<String>  strList   = Arrays.asList("a", "b", "c");

        // ? extends Number → 可以接受 Number 及其子类（Integer, Double...）
        System.out.println("整数列表求和: " + sumOfNumbers(intList));     // 6
        System.out.println("浮点列表求和: " + sumOfNumbers(doubleList));  // 6.6
        // sumOfNumbers(strList);  // ← 编译错误！String 不是 Number 的子类

        // ? → 任意类型
        printList(intList);   // [1, 2, 3]
        printList(strList);   // [a, b, c]

        System.out.println();

        // ============================================
        // 【6. PECS 原则（面试必考）】
        // ============================================
        System.out.println("=== PECS 原则 ===");
        System.out.println("Producer Extends, Consumer Super");
        System.out.println("  - 只读用 extends：List<? extends Number> → 能拿 Number，不能往里加");
        System.out.println("  - 只写用 super：List<? super Integer> → 能往里加 Integer，取出是 Object");
        System.out.println("  - 又要读又要写 → 就不要用通配符，用具体的类型参数");
    }


    // ==================== 泛型方法 ====================

    /**
     * 泛型方法：取数组中间元素
     * <T> 声明这是一个泛型方法，T 是类型参数
     * T[] 参数类型，T 返回值类型
     */
    public static <T> T getMiddle(T[] array) {
        return array[array.length / 2];
    }

    /**
     * 上限通配符：接受 Number 及其子类
     */
    public static double sumOfNumbers(List<? extends Number> list) {
        double sum = 0.0;
        for (Number n : list) {
            sum += n.doubleValue();   // Number 有 doubleValue() 方法
        }
        return sum;
    }

    /**
     * 无界通配符：接受任意类型（只读）
     */
    public static void printList(List<?> list) {
        System.out.print("列表内容: ");
        for (Object obj : list) {
            System.out.print(obj + " ");
        }
        System.out.println();
    }
}


// ============================================
// 泛型类：一个可以装任意类型东西的盒子
// ============================================
class Box<T> {
    private T content;  // T 是"类型占位符"，使用时决定具体是什么类型

    public Box(T content) {
        this.content = content;
    }

    public T get() {
        return content;
    }

    public void set(T content) {
        this.content = content;
    }

    // T 可以在方法参数和返回值中任意使用
    public Box<T> cloneBox() {
        return new Box<>(this.content);
    }
}


// ============================================
// 泛型接口
// ============================================
interface Comparator<T> {
    int compare(T o1, T o2);  // 返回负数=o1小, 0=相等, 正数=o1大
}

class LengthComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length();
    }
}
