/**
 * =====================================================
 * Ch13: IO 流与文件操作
 * =====================================================
 *
 * 【什么是"流"？】
 *   流 = 一连串流动的数据，就像水管里的水。
 *   输入流（Input）：从外部"读"进程序（键盘、文件、网络...）
 *   输出流（Output）：从程序"写"到外部（屏幕、文件、网络...）
 *
 * 【Java IO 的分类（按操作单位）】
 *
 *   字节流（byte）：原始二进制数据
 *     InputStream  / OutputStream     ← 抽象父类
 *     FileInputStream / FileOutputStream  ← 文件读写
 *     BufferedInputStream / BufferedOutputStream ← 带缓冲（性能更好）
 *
 *   字符流（char）：文本数据（自动处理编码）
 *     Reader  / Writer               ← 抽象父类
 *     FileReader / FileWriter        ← 文件读写
 *     BufferedReader / BufferedWriter ← 带缓冲 + 一次性读一行
 *
 * 【装饰器模式】
 *   Java IO 用了经典的"装饰器模式"：
 *   基础流提供底层功能，包装流在基础流上增加能力。
 *   比如：new BufferedReader(new FileReader("file.txt"))
 *         FileReader 负责读取字符
 *         BufferedReader 包装它，增加缓冲和 readLine() 能力
 *
 * 【NIO 简介】
 *   JDK 1.4 引入了 NIO (New IO)，面向缓冲区(Buffer)和通道(Channel)，
 *   非阻塞，适合高并发场景。日常文件操作用传统 IO 就够了。
 */

import java.io.*;
import java.nio.file.*;   // JDK 7 后的 Files/Paths 工具类（更推荐）

public class IOTutorial {

    public static void main(String[] args) {
        // 为了演示，我们创建一个临时文件
        String testFile = "java-learning/ch13-io/test.txt";

        // ============================================
        // 【1. 写入文件——多种方式】
        // ============================================
        System.out.println("=== 写入文件 ===");

        // 方式1：FileWriter（字符流，最简单但不带缓冲）
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("第一行：你好，Java IO！\n");
            writer.write("第二行：FileWriter 简单但不带缓冲\n");
        } catch (IOException e) {
            System.out.println("写入失败: " + e.getMessage());
        }

        // 方式2：BufferedWriter（推荐，带缓冲 + newLine()）
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(testFile, true))) {
            // 第二个参数 true = 追加模式（不覆盖原文件）
            bw.write("第三行：用 BufferedWriter 写入");
            bw.newLine();         // 跨平台的换行（比 \n 更规范）
            bw.write("第四行：带缓冲，性能更好");
            bw.newLine();
        } catch (IOException e) {
            System.out.println("写入失败: " + e.getMessage());
        }

        // 方式3：Files.write（JDK 7+，一行搞定，最推荐）
        try {
            Files.writeString(Paths.get(testFile), "第五行：Files.writeString 一行搞定！\n",
                              StandardOpenOption.APPEND);  // 追加模式
        } catch (IOException e) {
            System.out.println("写入失败: " + e.getMessage());
        }

        System.out.println("写入完成！");


        // ============================================
        // 【2. 读取文件——多种方式】
        // ============================================
        System.out.println("\n=== 读取文件 ===");

        // 方式1：BufferedReader 逐行读（最经典）
        System.out.println("--- BufferedReader 逐行读取 ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(testFile))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {   // null 表示文件读完
                System.out.println("  " + lineNum + ": " + line);
                lineNum++;
            }
        } catch (IOException e) {
            System.out.println("读取失败: " + e.getMessage());
        }

        // 方式2：Files.readAllLines（JDK 7+，一次性读所有行到 List）
        System.out.println("--- Files.readAllLines ---");
        try {
            java.util.List<String> allLines = Files.readAllLines(Paths.get(testFile));
            for (String line : allLines) {
                System.out.println("  " + line);
            }
        } catch (IOException e) {
            System.out.println("读取失败: " + e.getMessage());
        }

        // 方式3：Files.readString（JDK 11+，一次性读整个文件为一个 String）
        System.out.println("--- Files.readString (文件总字符数) ---");
        try {
            String content = Files.readString(Paths.get(testFile));
            System.out.println("  文件共 " + content.length() + " 个字符");
        } catch (IOException e) {
            System.out.println("读取失败: " + e.getMessage());
        }


        // ============================================
        // 【3. 文件与目录操作（Files 工具类）】
        // ============================================
        System.out.println("\n=== 文件/目录操作 ===");

        Path path = Paths.get(testFile);

        try {
            System.out.println("文件存在: " + Files.exists(path));
            System.out.println("是否可读: " + Files.isReadable(path));
            System.out.println("是否可写: " + Files.isWritable(path));
            System.out.println("文件大小: " + Files.size(path) + " bytes");
            System.out.println("最后修改: " + Files.getLastModifiedTime(path));
        } catch (IOException e) {
            System.out.println("出错: " + e.getMessage());
        }

        // 创建目录
        Path dir = Paths.get("java-learning/ch13-io/subdir");
        try {
            Files.createDirectories(dir);  // 连父目录一起创建（mkdir -p 的效果）
            System.out.println("目录已创建: " + dir.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("创建目录失败: " + e.getMessage());
        }

        // 复制文件
        Path copied = Paths.get("java-learning/ch13-io/subdir/test_copy.txt");
        try {
            Files.copy(path, copied, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("文件已复制到: " + copied);
        } catch (IOException e) {
            System.out.println("复制失败: " + e.getMessage());
        }

        // 删除文件（演示完后清理）
        // 不需要清理，留着让 cujo 看到实际文件


        // ============================================
        // 【4. 序列化 —— 对象存到文件】
        // ============================================
        System.out.println("\n=== 序列化演示 ===");

        // 序列化：对象 → 字节流（存文件或网络传输）
        // 被序列化的类必须实现 Serializable 接口（标记接口，没有方法）
        Person person = new Person("张三", 25, "secret123");

        String objFile = "java-learning/ch13-io/person.obj";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(objFile))) {
            oos.writeObject(person);
            System.out.println("Person 对象已序列化");
        } catch (IOException e) {
            System.out.println("序列化失败: " + e.getMessage());
        }

        // 反序列化：字节流 → 对象
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objFile))) {
            Person loaded = (Person) ois.readObject();
            System.out.println("反序列化后: " + loaded);
            // 注意：password 字段上有 transient → 不会被序列化 → 反序列化后是 null
            System.out.println("密码字段(transient): " + loaded.password);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("反序列化失败: " + e.getMessage());
        }
    }
}


// ============================================
// 用于序列化的演示类
// ============================================
// Serializable 是一个"标记接口"——本身没有方法，只是告诉 JVM "这个类可以被序列化"
class Person implements Serializable {
    // serialVersionUID 用于版本控制，反序列化时 JVM 会检查这个值是否一致
    // 不显式定义的话 JVM 会自动生成，但类结构变了自动生成的值也会变→反序列化炸
    // 所以最佳实践：显式定义它
    private static final long serialVersionUID = 1L;

    private String name;
    private int    age;

    // transient：表示这个字段不要序列化
    // 密码这种敏感信息不应该被序列化
    private transient String password;

    public Person(String name, int age, String password) {
        this.name     = name;
        this.age      = age;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", password='" + password + "'}";
    }
}
