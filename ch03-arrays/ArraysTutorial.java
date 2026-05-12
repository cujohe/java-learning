/**
 * =====================================================
 * Ch03: 数组（Array）
 * =====================================================
 *
 * 【数组是什么？】
 *   数组就是一个能装多个"同类型"数据的容器。
 *   类比：一排编号的储物柜，每个柜子大小一样，只能放同一种东西。
 *
 *   关键特性：
 *   1. 长度固定——创建时定死，之后不能改变（想变长？用 ArrayList，Ch11讲）
 *   2. 下标从 0 开始——第一个元素是 arr[0]，最后一个是 arr[length-1]
 *   3. 连续内存——所以访问 arr[i] 是 O(1)，非常快
 *   4. 数组本身是引用类型，但可以装基本类型
 */

import java.util.Arrays;  // Java 提供的数组工具类

public class ArraysTutorial {

    public static void main(String[] args) {

        // ============================================
        // 【1. 数组的声明与创建——三种方式】
        // ============================================

        // 方式一：先声明，再创建，再逐个赋值
        int[] scores;            // 声明一个 int 数组变量（此时还没有实际空间）
        scores = new int[5];     // 创建长度为 5 的 int 数组，默认值全是 0
        scores[0] = 90;
        scores[1] = 85;
        scores[2] = 78;
        scores[3] = 92;
        scores[4] = 88;
        // scores[5] = 100;   // ← 编译通过但运行报错！ArrayIndexOutOfBoundsException

        // 方式二：声明 + 创建 + 初始化 一步到位（最常用）
        int[] numbers = {10, 20, 30, 40, 50};    // 语法糖，长度自动推断为 5

        // 方式三：用 new + 初始化值（匿名数组方式）
        double[] prices = new double[]{9.99, 19.99, 29.99};

        // 两种声明写法：int[] arr  和  int arr[]，效果一模一样
        // 推荐 int[] arr——类型和变量名清楚分开
        int[]  recommended;     // √ 推荐
        int   notRecommended[]; // × 可读性差，不推荐


        // ============================================
        // 【2. 访问和修改数组元素】
        // ============================================

        System.out.println("=== 数组基本操作 ===");
        System.out.println("scores 长度: " + scores.length);        // length 是属性不是方法，不加 ()
        System.out.println("第一个元素: " + scores[0]);
        System.out.println("最后一个:   " + scores[scores.length - 1]);

        // 修改元素
        scores[2] = 80;
        System.out.println("修改后的 scores[2]: " + scores[2]);


        // ============================================
        // 【3. 遍历数组——三种方法】
        // ============================================

        System.out.println("\n=== 遍历数组 ===");

        // 方法1：传统 for（当你需要下标时用这个）
        System.out.print("传统 for: ");
        for (int i = 0; i < scores.length; i++) {
            System.out.print(scores[i] + " ");
        }
        System.out.println();

        // 方法2：增强 for（foreach）—— 最简洁，不需要下标时优先用
        System.out.print("增强 for: ");
        for (int s : scores) {       // 读作："对于 scores 中的每个 int s"
            System.out.print(s + " ");
        }
        System.out.println();

        // ⚠️ 增强 for 只能读，不能改元素值
        // 因为 s 只是每个元素的拷贝（值拷贝），改 s 不影响原数组
        for (int s : scores) {
            s = 999;   // 这只是改了局部变量 s，scores 数组完全不受影响
        }
        System.out.println("尝试用增强 for 修改后: " + scores[0] + " ← 没变，还是 90");

        // 方法3：Arrays.toString() 快速打印（调试神器）
        System.out.println("Arrays.toString: " + Arrays.toString(scores));


        // ============================================
        // 【4. 数组的默认值】
        // ============================================
        // 创建数组时，Java 会自动给每个位置填默认值：
        //   整型(byte/short/int/long) → 0
        //   浮点(float/double)         → 0.0
        //   char                       → '\u0000'（空字符）
        //   boolean                    → false
        //   引用类型(String等)          → null

        int[]     defaultInt    = new int[3];
        double[]  defaultDouble = new double[3];
        boolean[] defaultBool   = new boolean[3];
        String[]  defaultStr    = new String[3];

        System.out.println("\n=== 默认值 ===");
        System.out.println("int[3]:     " + Arrays.toString(defaultInt));     // [0, 0, 0]
        System.out.println("double[3]:  " + Arrays.toString(defaultDouble));  // [0.0, 0.0, 0.0]
        System.out.println("boolean[3]: " + Arrays.toString(defaultBool));    // [false, false, false]
        System.out.println("String[3]:  " + Arrays.toString(defaultStr));     // [null, null, null]


        // ============================================
        // 【5. 数组排序与查找】
        // ============================================

        System.out.println("\n=== 排序与查找 ===");

        int[] messy = {5, 2, 8, 1, 9, 3};
        System.out.println("排序前: " + Arrays.toString(messy));

        // 排序（原地排序，会修改原数组）
        Arrays.sort(messy);
        System.out.println("排序后: " + Arrays.toString(messy));  // [1, 2, 3, 5, 8, 9]

        // 二分查找（要求数组已排好序！）
        int index = Arrays.binarySearch(messy, 5);
        System.out.println("5 的下标: " + index);  // 3

        // binarySearch 找不到时返回负数: -(插入点) - 1
        int notFound = Arrays.binarySearch(messy, 4);
        System.out.println("4 的下标: " + notFound + " (负数=没找到，插入点是" + (-notFound - 1) + ")");


        // ============================================
        // 【6. 数组拷贝】
        // ============================================

        System.out.println("\n=== 数组拷贝 ===");

        int[] original = {1, 2, 3, 4, 5};

        // 方式1：System.arraycopy —— 最底层，最快
        int[] copy1 = new int[5];
        System.arraycopy(original, 0, copy1, 0, original.length);
        // 参数: 源数组, 源起始位置, 目标数组, 目标起始位置, 拷贝个数

        // 方式2：Arrays.copyOf —— 更便捷（底层也是调 arraycopy）
        int[] copy2 = Arrays.copyOf(original, original.length);

        // 方式3：Arrays.copyOfRange —— 拷贝指定范围
        int[] subArray = Arrays.copyOfRange(original, 1, 4);  // 下标 [1, 4) → {2, 3, 4}

        // ⚠️ 直接赋值不是拷贝！只是让两个变量指向同一个数组
        int[] alias = original;       // alias 和 original 指向同一个数组对象
        alias[0] = 999;              // 改 alias[0]
        System.out.println("original[0]=" + original[0] + " ← 也跟着变了！因为指向同一个对象");

        // 正确方式验证
        copy1[0] = 777;
        System.out.println("copy1[0]=" + copy1[0] + " 但 original[0]=" + original[0] + " ← 互不影响");

        System.out.println("subArray: " + Arrays.toString(subArray));  // [3, 4, 5]... wait, let me check

        // 修正：original 现在是 {999, 2, 3, 4, 5}，所以 copyOfRange(original, 1, 4) = {2, 3, 4}
        // 但上面 copy1 是从 original 拷贝的，original 已被改过，copy1 是独立的


        // ============================================
        // 【7. 二维数组】
        // ============================================
        // Java 没有真正的"二维数组"，只有"数组的数组"
        // 每个元素本身也是一个数组，所以每行长度可以不同（锯齿数组/Jagged Array）

        System.out.println("\n=== 二维数组 ===");

        // 规则的二维数组（矩阵）
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        System.out.println("矩阵:");
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                System.out.print(matrix[row][col] + " ");
            }
            System.out.println();
        }

        // 不规则数组（每行长度不同）
        int[][] jagged = new int[3][];
        jagged[0] = new int[]{1, 2};
        jagged[1] = new int[]{3, 4, 5, 6};
        jagged[2] = new int[]{7};

        System.out.println("锯齿数组:");
        for (int[] row : jagged) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
}
