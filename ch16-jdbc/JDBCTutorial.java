/**
 * =====================================================
 * Ch16: JDBC 数据库编程
 * =====================================================
 *
 * 【JDBC 是什么？】
 *   Java Database Connectivity——Java 连接数据库的标准接口。
 *   它定义了一组接口（java.sql 包），各个数据库厂商提供具体实现（驱动）。
 *
 *   你写的代码只依赖 JDBC 接口，换个数据库只需换驱动 jar 包，代码不用改。
 *   这就是"面向接口编程"的力量。
 *
 * 【JDBC 编程六步走（背下来）】
 *
 *   1. 加载驱动（JDK 6+ 可省略，SPI 机制自动加载）
 *   2. 获取连接 (Connection)      → DriverManager.getConnection(url, user, password)
 *   3. 创建 Statement/PreparedStatement → conn.createStatement() 或 conn.prepareStatement()
 *   4. 执行 SQL                   → executeQuery() 查询 / executeUpdate() 增删改
 *   5. 处理结果集 (ResultSet)      → 查询时用
 *   6. 释放资源                    → try-with-resources 自动关
 *
 * 【为什么强烈推荐 PreparedStatement？】
 *   1. 防止 SQL 注入（面试必问！）
 *   2. 预编译，执行效率更高（重复执行时）
 *   3. 代码更清晰（参数和 SQL 分离）
 *
 * 【SQL 注入演示】
 *   用户输入: ' OR '1'='1
 *   Statement 拼接: SELECT * FROM users WHERE username='' OR '1'='1' AND password='xxx'
 *   → 永远返回 true！登录绕过！
 *   PreparedStatement 用 ? 占位符 → 用户输入被当作普通字符串，不可能逃逸。
 *
 *
 * 【前置准备】
 *   本教程使用 H2 嵌入式数据库（纯 Java 实现，不需要安装）
 *   在项目中加入 h2 的 jar 包即可运行。
 *   实际项目中换成 MySQL/PostgreSQL，只改连接 URL 和驱动名即可。
 */

import java.sql.*;

public class JDBCTutorial {

    // H2 内存数据库：数据存在内存中，程序结束就消失（演示专用）
    // 实际 MySQL 连接: jdbc:mysql://localhost:3306/数据库名?useSSL=false&serverTimezone=UTC
    private static final String URL      = "jdbc:h2:mem:testdb";
    private static final String USER     = "sa";     // H2 默认用户名
    private static final String PASSWORD = "";       // H2 默认密码为空

    public static void main(String[] args) {
        // 由于可能没有 H2 驱动，用 try-catch 包裹
        // 注释代码演示所有操作，实际运行时需要 H2 的 jar

        System.out.println("=== JDBC 完整演示 ===\n");
        System.out.println("本演示需要 H2 数据库驱动。");
        System.out.println("下载: https://www.h2database.com/");
        System.out.println("或 Maven: <dependency><groupId>com.h2database</groupId><artifactId>h2</artifactId></dependency>");
        System.out.println();

        // 实际代码逻辑如下（放在 try-catch 里，没驱动也不会崩整个程序）
        try {
            runJDBCDemo();
        } catch (ClassNotFoundException e) {
            System.out.println("❌ 找不到 H2 驱动类。请添加 h2 jar 到 classpath。");
        } catch (SQLException e) {
            System.out.println("❌ 数据库操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * JDBC 完整操作演示
     */
    private static void runJDBCDemo() throws ClassNotFoundException, SQLException {

        // ============================================
        // 步骤 1: 加载驱动（JDK 6+ JDBC 4.0 之后可省略）
        // ============================================
        Class.forName("org.h2.Driver");  // 显式加载驱动（兼容老版本习惯）
        System.out.println("✓ 驱动加载成功");


        // ============================================
        // 步骤 2: 获取连接
        // ============================================
        // try-with-resources 保证 Connection 自动关闭
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("✓ 数据库连接成功: " + conn.getMetaData().getDatabaseProductName());

            // ============================================
            // 步骤 3&4: 创建表（DDL）
            // ============================================
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS students (
                        id   INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50)  NOT NULL,
                        age  INT          NOT NULL,
                        score DOUBLE      DEFAULT 0
                    )
                """);
                System.out.println("✓ 表创建成功");
            }


            // ============================================
            // 步骤 3&4: 插入数据（DML —— 用 PreparedStatement）
            // ============================================
            String insertSQL = "INSERT INTO students (name, age, score) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                // 批量插入
                addStudent(pstmt, "张三", 20, 85.5);
                addStudent(pstmt, "李四", 22, 92.0);
                addStudent(pstmt, "王五", 19, 58.0);
                addStudent(pstmt, "赵六", 21, 76.5);
                System.out.println("✓ 4 条数据插入成功");
            }


            // ============================================
            // 步骤 3&4: 查询数据（DQL）
            // ============================================
            System.out.println("\n--- 查询所有学生 ---");
            String querySQL = "SELECT id, name, age, score FROM students WHERE score >= ? ORDER BY score DESC";

            try (PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
                pstmt.setDouble(1, 60.0);   // 只查及格的

                try (ResultSet rs = pstmt.executeQuery()) {
                    System.out.printf("%-5s %-8s %-5s %-6s%n", "ID", "姓名", "年龄", "分数");
                    System.out.println("---------------------------");

                    while (rs.next()) {
                        int    id    = rs.getInt("id");
                        String name  = rs.getString("name");
                        int    age   = rs.getInt("age");
                        double score = rs.getDouble("score");

                        System.out.printf("%-5d %-8s %-5d %-6.1f%n", id, name, age, score);
                    }
                }
            }


            // ============================================
            // 步骤 3&4: 更新数据
            // ============================================
            String updateSQL = "UPDATE students SET score = ? WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                pstmt.setDouble(1, 95.0);
                pstmt.setString(2, "王五");
                int affected = pstmt.executeUpdate();
                System.out.println("\n✓ 更新了 " + affected + " 条记录 (王五 → 95分)");
            }


            // ============================================
            // 步骤 3&4: 删除数据
            // ============================================
            String deleteSQL = "DELETE FROM students WHERE score < ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                pstmt.setDouble(1, 60.0);
                int affected = pstmt.executeUpdate();
                System.out.println("✓ 删除了 " + affected + " 条不及格记录");
            }


            // ============================================
            // 事务演示
            // ============================================
            System.out.println("\n--- 事务演示 ---");

            // 默认自动提交：每条 SQL 都是一个独立事务
            // 设置手动提交后，多条 SQL 组成一个事务
            conn.setAutoCommit(false);   // 开始事务

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, "小明");
                pstmt.setInt(2, 18);
                pstmt.setDouble(3, 88.0);
                pstmt.executeUpdate();

                pstmt.setString(1, "小红");
                pstmt.setInt(2, 19);
                pstmt.setDouble(3, 91.0);
                pstmt.executeUpdate();

                // 如果这里一切正常 → 提交事务
                conn.commit();
                System.out.println("✓ 事务提交成功，两条学生记录已插入");
            } catch (SQLException e) {
                // 任何一步出错 → 回滚全部操作
                conn.rollback();
                System.out.println("✗ 事务回滚: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);  // 恢复自动提交
            }


            // ============================================
            // SQL 注入对比演示
            // ============================================
            System.out.println("\n--- SQL 注入演示 ---");

            // 模拟用户登录输入
            String userInput = "' OR '1'='1";

            // ❌ 危险写法：Statement + 字符串拼接
            System.out.println("【危险】拼接 SQL: SELECT * FROM students WHERE name='" + userInput + "'");
            System.out.println("  这句会把所有学生都查出来！这就是 SQL 注入！");

            // ✓ 安全写法：PreparedStatement
            String safeSQL = "SELECT * FROM students WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(safeSQL)) {
                pstmt.setString(1, userInput);  // 输入当普通字符串处理，不会执行
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("【安全】找到用户: " + rs.getString("name"));
                    } else {
                        System.out.println("【安全】没有这个用户（输入被安全转义了）");
                    }
                }
            }
        }
    }

    /**
     * 辅助方法：用 PreparedStatement 插入一条学生记录
     */
    private static void addStudent(PreparedStatement pstmt, String name, int age, double score)
            throws SQLException {
        pstmt.setString(1, name);   // 第1个 ? → name
        pstmt.setInt(2, age);       // 第2个 ? → age
        pstmt.setDouble(3, score);  // 第3个 ? → score
        pstmt.executeUpdate();
    }
}
