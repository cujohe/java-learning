/**
 * =====================================================
 * Ch17: 网络编程基础
 * =====================================================
 *
 * 【网络编程的核心概念】
 *
 *   IP 地址：计算机在网络上的"门牌号"
 *   端口号：一台计算机上不同程序的"房间号"（0-65535，0-1023 被系统占用）
 *   协议：通信的"语言规范"（TCP/UDP/HTTP...）
 *
 * 【TCP vs UDP —— 一句话区分】
 *
 *   TCP（传输控制协议）：
 *     - 像打电话：先建立连接（三次握手），确认对方在听，再说内容
 *     - 可靠、有序、不丢数据
 *     - 适用于：网页浏览、文件传输、邮件、聊天
 *
 *   UDP（用户数据报协议）：
 *     - 像寄明信片：直接扔出去，不管对方收没收到
 *     - 不可靠、可能丢包、可能乱序
 *     - 但快！适用于：视频直播、在线游戏、DNS 查询
 *
 * 【Socket —— 网络编程的"插座"】
 *   Socket 是 TCP/IP 协议的编程接口。
 *   一端插在客户端，一端插在服务端 → 数据从这头流到那头。
 *
 *   ServerSocket：服务端的"接线员"，负责监听并接受连接
 *   Socket：客户端的"电话"，拨号到服务端
 */


import java.io.*;
import java.net.*;

public class NetworkTutorial {

    public static void main(String[] args) {
        // 演示 TCP 通信：服务端和客户端在本机通信

        // ============================================
        // 【TCP 完整演示】
        // ============================================
        System.out.println("=== TCP Socket 编程 ===");

        int port = 8888;

        // 服务端线程
        Thread serverThread = new Thread(() -> {
            try {
                startEchoServer(port);
            } catch (IOException e) {
                System.out.println("服务端异常: " + e.getMessage());
            }
        }, "ServerThread");
        serverThread.setDaemon(true);  // 守护线程：主程序结束它也自动结束
        serverThread.start();

        // 等一会儿让服务端启动
        try { Thread.sleep(200); } catch (InterruptedException e) {}

        // 客户端
        try {
            runEchoClient(port);
        } catch (IOException e) {
            System.out.println("客户端异常: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== URL 和 HTTP ===");
        demonstrateURL();

        System.out.println();
        System.out.println("=== 网络编程安全原则 ===");
        System.out.println("1. 永远不要信任客户端发来的数据（输入验证）");
        System.out.println("2. 设置连接超时和读取超时");
        System.out.println("3. 服务端要限制最大连接数，防止资源耗尽");
        System.out.println("4. 传输敏感数据必须加密（TLS/SSL）");
        System.out.println("5. 实际开发用 Netty/OkHttp 等成熟框架，不要裸写 Socket");
    }


    /**
     * TCP Echo 服务端：接收客户端消息并原样返回
     */
    private static void startEchoServer(int port) throws IOException {
        // ServerSocket 绑定端口，等待客户端连接
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[服务端] 启动成功，监听端口 " + port + "...");

            // accept() 会阻塞，直到有客户端连上来
            // 每一次 accept 返回一个新的 Socket 代表和那个客户端的连接
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("[服务端] 客户端已连接: " +
                    clientSocket.getInetAddress().getHostAddress());

                // 获取输入/输出流
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
                PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
                // PrintWriter 的第二个参数 true = autoFlush（自动刷新缓冲区）

                // 读取客户端消息
                String message = reader.readLine();
                System.out.println("[服务端] 收到: " + message);

                // 原样返回（Echo）
                writer.println("[Echo] " + message);
                System.out.println("[服务端] 已回复");
            }
        }
        System.out.println("[服务端] 关闭");
    }

    /**
     * TCP Echo 客户端：发送消息给服务端并接收回复
     */
    private static void runEchoClient(int port) throws IOException {
        // Socket 连接到服务端
        try (Socket socket = new Socket("127.0.0.1", port)) {  // 127.0.0.1 = 本机地址
            System.out.println("[客户端] 已连接到服务端");

            // 设置超时（防止无限等待）
            socket.setSoTimeout(5000);  // 5 秒读超时

            // 获取输入/输出流
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
            PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            // 发送消息
            String sendMsg = "Hello, Server! 现在时间是 " + System.currentTimeMillis();
            writer.println(sendMsg);
            System.out.println("[客户端] 发送: " + sendMsg);

            // 接收回复
            String reply = reader.readLine();
            System.out.println("[客户端] 收到回复: " + reply);
        }
        System.out.println("[客户端] 关闭");
    }


    /**
     * URL 和 HTTP 请求演示
     */
    private static void demonstrateURL() {
        try {
            // URL: 统一资源定位符
            // 格式: protocol://host:port/path?query#fragment
            URL url = new URL("https://httpbin.org/get?name=java");

            System.out.println("协议: " + url.getProtocol());
            System.out.println("主机: " + url.getHost());
            System.out.println("路径: " + url.getPath());
            System.out.println("查询: " + url.getQuery());

            // 打开连接并读取
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            System.out.println("HTTP 状态码: " + status);

            if (status == 200) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // 只打印前 200 个字符
                String body = response.toString();
                System.out.println("响应体(前200字符): " +
                    body.substring(0, Math.min(200, body.length())) + "...");
            }

            conn.disconnect();
        } catch (IOException e) {
            System.out.println("HTTP 请求失败: " + e.getMessage());
            System.out.println("（网络不可用时这是正常的，不影响学习）");
        }
    }
}
