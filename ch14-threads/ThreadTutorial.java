/**
 * =====================================================
 * Ch14: 多线程与并发
 * =====================================================
 *
 * 【什么是线程？什么是进程？】
 *
 *   进程 (Process)：操作系统分配资源的最小单位。
 *       一个正在运行的程序 = 一个进程。进程之间内存独立。
 *
 *   线程 (Thread)：CPU 调度的最小单位。
 *       一个进程内可以有多个线程，它们共享进程的内存空间。
 *
 *   类比：
 *     一栋办公楼 = 一个进程
 *     楼里的员工 = 线程
 *     每层楼的办公桌 = 共享内存（所有员工都能用）
 *
 * 【为什么需要多线程？】
 *   1. 充分利用多核 CPU
 *   2. 不让慢操作（如网络请求）阻塞主程序
 *       类比：你去餐厅点菜（主线程），厨房在做饭（后台线程），
 *       你不用干等着，可以玩手机。这叫"异步"。
 *
 * 【线程安全问题】
 *   多个线程同时修改同一个数据 → 数据错乱！
 *   类比：两个人同时往同一个银行账户取钱，余额计算就乱了。
 *   解决：加锁（synchronized），保证同一时刻只有一个线程操作。
 */

public class ThreadTutorial {

    // 共享变量——演示线程安全问题用
    private static int counter = 0;

    // 为什么加 volatile？后面解释
    private static volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {

        // ============================================
        // 【1. 创建线程的三种方式】
        // ============================================
        System.out.println("=== 创建线程 ===");

        // 主线程的名字
        System.out.println("主线程: " + Thread.currentThread().getName());

        // 方式1：继承 Thread 类（不推荐——Java 是单继承，继承了 Thread 就不能继承别的了）
        Thread t1 = new MyThread();
        t1.setName("方式1-Thread");
        t1.start();   // start() 才是启动线程！不要调用 run()！

        // 方式2：实现 Runnable 接口（推荐——更灵活）
        Runnable task = new MyRunnable();
        Thread t2 = new Thread(task, "方式2-Runnable");
        t2.start();

        // 方式3：Lambda 表达式（最简洁——Ch15 详细讲）
        Thread t3 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": Lambda 方式创建");
        }, "方式3-Lambda");
        t3.start();

        // 等待子线程结束（否则 main 结束子线程可能还没执行完）
        t1.join();
        t2.join();
        t3.join();

        System.out.println();

        // ============================================
        // 【2. 线程安全问题——为什么要同步？】
        // ============================================
        System.out.println("=== 线程安全问题 ===");

        counter = 0;

        // 两个线程各加 100000 次，期望结果 200000
        // 但实际结果可能小于 200000！这就是"竞态条件"(Race Condition)
        Thread tA = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter++;  // counter++ 不是原子操作！读→加→写 三步，可能被中断
            }
        }, "加法线程A");

        Thread tB = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter++;
            }
        }, "加法线程B");

        tA.start();
        tB.start();
        tA.join();
        tB.join();

        System.out.println("不加锁的结果: " + counter + "（期望 200000，但实际可能少！）");

        // 修复：加 synchronized
        counter = 0;
        Object lock = new Object();   // 锁对象

        Thread tC = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                synchronized (lock) {      // 拿到锁才能执行
                    counter++;
                }                          // 执行完释放锁
            }
        }, "同步线程C");

        Thread tD = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                synchronized (lock) {
                    counter++;
                }
            }
        }, "同步线程D");

        tC.start();
        tD.start();
        tC.join();
        tD.join();

        System.out.println("加锁后的结果: " + counter + "（和期望一致！）");

        System.out.println();

        // ============================================
        // 【3. volatile —— 轻量级同步】
        // ============================================
        System.out.println("=== volatile 演示 ===");

        // volatile 保证变量的"可见性"：一个线程修改后，其他线程立即可见
        // 但不保证原子性（counter++ 这种复合操作还是要用 synchronized）
        //
        // 常见使用场景：标志位
        running = true;

        Thread worker = new Thread(() -> {
            int count = 0;
            while (running) {  // 不加 volatile 的话，这个线程可能永远看不到 running 变成 false
                count++;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println("  worker 线程结束，循环了 " + count + " 次");
        });
        worker.start();

        Thread.sleep(10);    // 让 worker 跑一会儿
        running = false;     // 修改 volatile 变量，worker 立即可见
        worker.join();

        System.out.println("volatile 标志位有效！");

        System.out.println();

        // ============================================
        // 【4. 线程池（生产环境标配）】
        // ============================================
        System.out.println("=== 线程池 ===");

        // 不要每次 new Thread！创建和销毁线程开销大，而且线程数量不可控。
        // 用线程池管理线程——复用、限流、管理生命周期。

        java.util.concurrent.ExecutorService pool =
            java.util.concurrent.Executors.newFixedThreadPool(3);

        // 提交 5 个任务，但只有 3 个线程池工作
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("  任务" + taskId + " 由 " +
                                   Thread.currentThread().getName() + " 执行");
                try {
                    Thread.sleep(500);  // 模拟耗时操作
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // 关闭线程池（不再接受新任务，等已有任务执行完）
        pool.shutdown();
        // pool.shutdownNow();  // 强制关闭（尝试中断所有任务）

        // 等待所有任务结束
        boolean finished = pool.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
        System.out.println("线程池所有任务" + (finished ? "已" : "未") + "完成");


        // ============================================
        // 【5. 死锁演示——面试经典题】
        // ============================================
        System.out.println("\n=== 死锁演示（理论） ===");
        System.out.println("死锁条件（四个同时满足才会死锁）:");
        System.out.println("  1. 互斥：资源一次只能被一个线程占用");
        System.out.println("  2. 持有并等待：线程拿着一个锁，还想要另一个");
        System.out.println("  3. 不可剥夺：不能强制抢别人的锁");
        System.out.println("  4. 循环等待：A等B，B等A → 死循环");
        System.out.println("\n打破任何一个就能避免死锁。最常见：按固定顺序加锁。");
    }
}


// ============================================
// 方式1：继承 Thread
// ============================================
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println(getName() + ": 继承 Thread 方式");
    }
}

// ============================================
// 方式2：实现 Runnable（推荐）
// ============================================
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": 实现 Runnable 方式");
    }
}
