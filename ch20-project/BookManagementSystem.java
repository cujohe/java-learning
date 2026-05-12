/**
 * =====================================================
 * Ch20: 综合项目——图书管理系统
 * =====================================================
 *
 * 【项目定位】
 *   一个命令行图书管理系统，整合前面学过的所有核心知识：
 *   - 面向对象（封装、继承、多态、接口）
 *   - 集合框架（List, Map）
 *   - 异常处理
 *   - IO 流（文件持久化）
 *   - Lambda 与 Stream
 *
 * 【功能需求】
 *   1. 添加图书
 *   2. 查看所有图书
 *   3. 搜索图书（按书名或作者）
 *   4. 借出/归还图书
 *   5. 删除图书
 *   6. 数据保存到文件（程序重启不丢数据）
 *
 * 【架构设计】
 *
 *   Main (入口 + 菜单循环)
 *     │
 *     ├── model/
 *     │     └── Book.java           → 图书实体类（数据模型）
 *     │
 *     ├── service/
 *     │     ├── BookService.java    → 接口：定义业务规范
 *     │     └── BookServiceImpl.java → 实现：具体业务逻辑
 *     │
 *     └── util/
 *           └── FileUtil.java       → 文件读写工具
 *
 * 【设计思考（cujo 要理解）】
 *
 *   为什么分三层？
 *     model：纯数据，不依赖任何其他层 → 最稳定
 *     service：业务逻辑，依赖 model → 中间层
 *     util：工具类，被 service 调用 → 最底层
 *
 *   为什么 BookService 用接口？
 *     如果将来换存储方式（从文件 → 数据库），只需新写一个实现类，
 *     Main 里的代码一行不用改——这就是"面向接口编程"。
 */


import java.io.*;
import java.util.*;
import java.util.stream.*;


// ============================================
// 主程序入口
// ============================================
public class BookManagementSystem {

    private static BookService bookService = new BookServiceImpl();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════╗");
        System.out.println("║    📚 图书管理系统 v1.0   ║");
        System.out.println("╚════════════════════════════╝");

        // 程序启动时从文件加载数据
        bookService.loadFromFile("books.dat");

        // 主菜单循环
        while (true) {
            printMenu();
            System.out.print("请选择操作 [0-6]: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ 请输入有效的数字！\n");
                continue;
            }

            switch (choice) {
                case 1 -> addBook();
                case 2 -> listBooks();
                case 3 -> searchBooks();
                case 4 -> borrowBook();
                case 5 -> returnBook();
                case 6 -> deleteBook();
                case 0 -> {
                    // 退出前保存数据
                    bookService.saveToFile("books.dat");
                    System.out.println("👋 再见！数据已保存。");
                    System.exit(0);
                }
                default -> System.out.println("❌ 无效选项，请重新输入！\n");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n══════ 功能菜单 ══════");
        System.out.println("  1. 添加图书");
        System.out.println("  2. 查看全部图书");
        System.out.println("  3. 搜索图书");
        System.out.println("  4. 借出图书");
        System.out.println("  5. 归还图书");
        System.out.println("  6. 删除图书");
        System.out.println("  0. 退出系统");
        System.out.println("══════════════════════");
    }

    private static void addBook() {
        System.out.println("\n--- 添加图书 ---");
        System.out.print("书名: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("❌ 书名不能为空！");
            return;
        }

        System.out.print("作者: ");
        String author = scanner.nextLine().trim();
        if (author.isEmpty()) {
            System.out.println("❌ 作者不能为空！");
            return;
        }

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();

        try {
            bookService.addBook(new Book(title, author, isbn));
            System.out.println("✅ 《" + title + "》添加成功！");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private static void listBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("\n📭 图书馆还没有书，先添加几本吧！");
            return;
        }

        System.out.println("\n══════ 图书列表（共 " + books.size() + " 本）══════");
        System.out.printf("%-5s %-20s %-15s %-15s %-6s%n",
                          "ID", "书名", "作者", "ISBN", "状态");
        System.out.println("─".repeat(65));

        for (Book book : books) {
            System.out.printf("%-5d %-20s %-15s %-15s %-6s%n",
                book.getId(),
                truncate(book.getTitle(), 20),
                truncate(book.getAuthor(), 15),
                book.getIsbn(),
                book.isBorrowed() ? "已借出" : "可借阅");
        }
    }

    private static void searchBooks() {
        System.out.println("\n--- 搜索图书 ---");
        System.out.print("输入书名或作者关键词: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("❌ 请输入搜索关键词！");
            return;
        }

        // 用 Stream API 进行过滤
        List<Book> results = bookService.searchBooks(keyword);

        if (results.isEmpty()) {
            System.out.println("🔍 未找到匹配的图书。");
        } else {
            System.out.println("🔍 找到 " + results.size() + " 本相关图书:");
            for (Book book : results) {
                System.out.println("  · 《" + book.getTitle() + "》— " + book.getAuthor()
                    + (book.isBorrowed() ? " [已借出]" : " [可借阅]"));
            }
        }
    }

    private static void borrowBook() {
        System.out.println("\n--- 借出图书 ---");
        System.out.print("请输入图书 ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());

            if (bookService.borrowBook(id)) {
                System.out.println("✅ 借阅成功！请按时归还。");
            } else {
                System.out.println("❌ 借阅失败：图书不存在或已被借出。");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ 请输入有效的数字 ID！");
        }
    }

    private static void returnBook() {
        System.out.println("\n--- 归还图书 ---");
        System.out.print("请输入图书 ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());

            if (bookService.returnBook(id)) {
                System.out.println("✅ 归还成功！感谢您的使用。");
            } else {
                System.out.println("❌ 归还失败：图书不存在或未被借出。");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ 请输入有效的数字 ID！");
        }
    }

    private static void deleteBook() {
        System.out.println("\n--- 删除图书 ---");
        System.out.print("请输入要删除的图书 ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());

            if (bookService.deleteBook(id)) {
                System.out.println("✅ 图书已删除。");
            } else {
                System.out.println("❌ 删除失败：图书不存在。");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ 请输入有效的数字 ID！");
        }
    }

    /** 字符串截断工具 */
    private static String truncate(String s, int maxLen) {
        return s.length() > maxLen ? s.substring(0, maxLen) : s;
    }
}


// ============================================
// model: Book 实体类（封装）
// ============================================
class Book implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private int     id;
    private String  title;
    private String  author;
    private String  isbn;
    private boolean borrowed;

    public Book(String title, String author, String isbn) {
        this.title  = title;
        this.author = author;
        this.isbn   = isbn;
        this.borrowed = false;
        // id 由 BookService 分配，这里先不管
    }

    // ====== Getter/Setter ======
    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }

    public String getTitle()            { return title; }
    public String getAuthor()           { return author; }
    public String getIsbn()             { return isbn; }

    public boolean isBorrowed()         { return borrowed; }
    public void setBorrowed(boolean b)  { this.borrowed = b; }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author='" + author + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


// ============================================
// service: 业务接口——定义规范
// ============================================
interface BookService {

    void addBook(Book book);

    List<Book> getAllBooks();

    List<Book> searchBooks(String keyword);

    boolean borrowBook(int id);

    boolean returnBook(int id);

    boolean deleteBook(int id);

    /** 数据持久化 */
    void saveToFile(String filePath);

    void loadFromFile(String filePath);
}


// ============================================
// service: 业务实现
// ============================================
class BookServiceImpl implements BookService {

    // 用 Map 存书：ID → Book（根据 ID 查找是 O(1)）
    private Map<Integer, Book> books = new LinkedHashMap<>();  // LinkedHashMap 保持插入顺序
    private int nextId = 1;

    @Override
    public void addBook(Book book) {
        // ISBN 唯一性检查
        boolean isbnExists = books.values().stream()
            .anyMatch(b -> b.getIsbn().equals(book.getIsbn()));
        if (isbnExists) {
            throw new IllegalArgumentException("ISBN 已存在: " + book.getIsbn());
        }

        book.setId(nextId++);
        books.put(book.getId(), book);
    }

    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());  // 返回副本，保护内部数据
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return books.values().stream()
            .filter(b -> b.getTitle().toLowerCase().contains(lowerKeyword)
                      || b.getAuthor().toLowerCase().contains(lowerKeyword))
            .collect(Collectors.toList());
    }

    @Override
    public boolean borrowBook(int id) {
        Book book = books.get(id);
        if (book == null || book.isBorrowed()) {
            return false;
        }
        book.setBorrowed(true);
        return true;
    }

    @Override
    public boolean returnBook(int id) {
        Book book = books.get(id);
        if (book == null || !book.isBorrowed()) {
            return false;
        }
        book.setBorrowed(false);
        return true;
    }

    @Override
    public boolean deleteBook(int id) {
        return books.remove(id) != null;  // remove 返回被删除的值，null 表示不存在
    }

    // ==================== 文件持久化 ====================

    @Override
    public void saveToFile(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            // 把整个 books Map 序列化到文件
            oos.writeObject(new SaveData(books, nextId));
            System.out.println("💾 数据已保存到 " + filePath);
        } catch (IOException e) {
            System.err.println("❌ 保存数据失败: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("📄 数据文件不存在，使用空数据开始。");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            SaveData data = (SaveData) ois.readObject();
            this.books  = (Map<Integer, Book>) data.books;
            this.nextId = data.nextId;
            System.out.println("📂 已加载 " + books.size() + " 本图书。");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ 加载数据失败: " + e.getMessage());
        }
    }

    // 序列化包装类
    private static class SaveData implements Serializable {
        private static final long serialVersionUID = 1L;
        Map<Integer, Book> books;
        int nextId;

        SaveData(Map<Integer, Book> books, int nextId) {
            this.books  = books;
            this.nextId = nextId;
        }
    }
}
