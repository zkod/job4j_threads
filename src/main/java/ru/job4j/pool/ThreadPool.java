package ru.job4j.pool;

import ru.job4j.wait.producerconsumer.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks;

    public ThreadPool() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public ThreadPool(int limitQueue) {
        tasks = new SimpleBlockingQueue<>(limitQueue);
        int size = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < size; i++) {
            threads.add(new Thread(() -> {
                while (!Thread.interrupted()) {
                    try {
                        tasks.poll().run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }));
        }
        threads.forEach(Thread::start);
    }

    /**
     * Добавляем задачи на выполнение.
     *
     * @param run Задача.
     */
    public void work(Runnable run) throws InterruptedException {
        tasks.offer(run);
    }

    /**
     * Завершение всех задач.
     */
    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }
}
