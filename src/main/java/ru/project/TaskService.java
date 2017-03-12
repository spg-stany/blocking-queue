package ru.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@Service
public class TaskService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private volatile Callable thread;
    private volatile boolean isStopping;

    private final BlockingQueue<Task> queue = new DelayQueue<Task>();
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final ExecutorService taskExecutor = Executors.newSingleThreadExecutor();

    public void add(LocalDateTime localDateTime, Callable callable) {
        Task task = new Task(localDateTime, callable);
        log.info("add new " + task);
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            log.error("exception", e);
        }
    }

    public synchronized void process() throws Exception {
        thread = new Callable() {
            public Object call() throws Exception {
                while (!isStopping) try {
                    Task task = queue.take();
                    log.info("received " + task);

                    log.info("execute");
                    taskExecutor.submit(task.getCallable());
                } catch (InterruptedException e) {
                    log.error("exception", e);
                }
                return null;
            }
        };
        executor.submit(thread);
    }

    public void stop() {
        isStopping = true;
    }

    @PreDestroy
    public void preDestroy() {
        taskExecutor.shutdownNow();
        executor.shutdownNow();
    }
}
