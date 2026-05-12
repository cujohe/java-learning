/**
 * =====================================================
 * Ch11: 集合框架（Collections Framework）
 * =====================================================
 *
 * 【为什么需要集合？】
 *   数组太死板：长度固定、操作不便（删除/插入要自己写）、没有现成的算法。
 *   集合 = 更强大的"动态数组"，Java 已经把最常用的数据结构和算法都实现好了。
 *
 * 【集合框架全景图】
 *
 *   Collection（接口）
 *   ├── List（接口）—— 有序、可重复、有下标
 *   │     ├── ArrayList    → 底层数组，查快增删慢（最常用）
 *   │     └── LinkedList   → 底层双向链表，增删快查慢
 *   │     └── Vector       → 古老，线程安全但慢（别用）
 *   │
 *   ├── Set（接口）—— 无序、不可重复
 *   │     ├── HashSet      → 基于 HashMap，最快，不保证顺序
 *   │     └── LinkedHashSet → 维护插入顺序
 *   │     └── TreeSet      → 自动排序（红黑树）
 *   │
 *   └── Queue/Deque（接口）—— 队列/双端队列
 *         └── LinkedList, ArrayDeque, PriorityQueue
 *
 *   Map（接口）—— 键值对，不属于 Collection 家族
 *   ├── HashMap        → 最常用，基于哈希表，Key 无序（最常用）
 *   ├── LinkedHashMap  → Key 维护插入顺序
 *   └── TreeMap        → Key 自动排序
 *   └── Hashtable      → 古老线程安全版（别用，用 ConcurrentHashMap）
 */

import java.util.*;

public class CollectionsTutorial {

    public static void main(String[] args) {

        // ============================================
        // 【1. ArrayList —— 最常用的集合】
        // ============================================
        System.out.println("=== ArrayList ===");

        // 泛型：尖括号里指定元素类型
        // List<Integer> 而不是 List<int> → 集合只能装引用类型不能装基本类型
        // 基本类型会自动装箱为包装类（int → Integer）
        List<Integer> list = new ArrayList<>();

        // 增
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(1, 15);        // 在下标 1 处插入 15 → [10, 15, 20, 30]

        // 删
        list.remove(2);         // 删除下标 2 的元素（20）→ [10, 15, 30]
        // ⚠️ list.remove(20);  // 这是删下标 20！会越界！
        list.remove(Integer.valueOf(15)); // 正确删除值为 15 的元素

        // 改
        list.set(1, 25);        // 把下标 1 改成 25

        // 查
        int first = list.get(0);
        boolean exists = list.contains(25);
        int index = list.indexOf(25);

        System.out.println("列表内容: " + list);
        System.out.println("第一个元素: " + first);
        System.out.println("包含 25 吗: " + exists);
        System.out.println("25 的下标: " + index);

        // 遍历方式（三种）
        System.out.print("for 循环遍历: ");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();

        System.out.print("增强 for:     ");
        for (Integer num : list) {
            System.out.print(num + " ");
        }
        System.out.println();

        // ⚠️ 遍历中删除元素的正确方式：用迭代器
        // 用增强 for 或 for-i 在遍历时删除会抛 ConcurrentModificationException
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            if (value > 20) {
                iterator.remove();  // 用迭代器的 remove，不要用 list.remove()
            }
        }
        System.out.println("删除 >20 的元素后: " + list);

        System.out.println();

        // ============================================
        // 【2. HashSet —— 去重神器】
        // ============================================
        System.out.println("=== HashSet ===");

        Set<String> names = new HashSet<>();
        names.add("张三");
        names.add("李四");
        names.add("张三");   // 重复 → 不会添加，也不会报错
        names.add("王五");
        names.add("赵六");

        System.out.println("Set 内容: " + names);
        System.out.println("Set 大小: " + names.size());    // 4（张三只算一次）
        System.out.println("包含 '张三': " + names.contains("张三"));

        // HashSet 不保证顺序，输出可能是 [李四, 张三, 王五, 赵六] 或其他顺序
        // 想要顺序 → LinkedHashSet
        // 想要排序 → TreeSet

        System.out.println();

        // ============================================
        // 【3. HashMap —— 键值对仓库】
        // ============================================
        System.out.println("=== HashMap ===");

        // Map<K, V>：K=键的类型, V=值的类型
        Map<String, Integer> scores = new HashMap<>();

        // 增/改（key 相同就是改）
        scores.put("张三", 90);
        scores.put("李四", 85);
        scores.put("王五", 78);
        scores.put("张三", 95);  // 覆盖原来的 90

        // 查
        int zhangScore = scores.get("张三");      // 95
        int liuScore   = scores.getOrDefault("赵六", 0);  // key 不存在时返回默认值 0

        System.out.println("张三的分数: " + zhangScore);
        System.out.println("赵六的分数: " + liuScore + " (默认值)");
        System.out.println("包含王五吗: " + scores.containsKey("王五"));
        System.out.println("Map 大小: " + scores.size());

        // 遍历 Map（三种方式）
        System.out.println("\n--- 遍历方式1: 遍历 keySet ---");
        for (String key : scores.keySet()) {
            System.out.println(key + " → " + scores.get(key));
        }

        System.out.println("--- 遍历方式2: 遍历 values（只取值）---");
        for (int score : scores.values()) {
            System.out.print(score + " ");
        }
        System.out.println();

        System.out.println("--- 遍历方式3: 遍历 entrySet（最推荐，一次拿 key+value）---");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }

        // 删
        scores.remove("李四");
        System.out.println("删除李四后: " + scores);

        System.out.println();

        // ============================================
        // 【4. 集合工具类 Collections】
        // ============================================
        System.out.println("=== Collections 工具类 ===");

        List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6));

        Collections.sort(numbers);        // 排序
        System.out.println("排序后: " + numbers);

        Collections.reverse(numbers);     // 反转
        System.out.println("反转后: " + numbers);

        Collections.shuffle(numbers);     // 随机打乱
        System.out.println("打乱后: " + numbers);

        int maxVal = Collections.max(numbers);
        int minVal = Collections.min(numbers);
        System.out.println("最大值: " + maxVal + ", 最小值: " + minVal);
    }
}
