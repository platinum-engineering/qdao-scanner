package io.qdao.scanner.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class QueueTasksExecutor {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ExecutorService executor;

    private static final Long AWAIT_TERMINATION_SEC = 30L;

    public QueueTasksExecutor() {
        start();
    }

    private void start() {
        executor = Executors.newSingleThreadExecutor();
        new Thread(() -> {
            try {
                executor.awaitTermination(AWAIT_TERMINATION_SEC, TimeUnit.SECONDS);
                executor.shutdown();
            } catch (InterruptedException e) {
                logger.log(Level.INFO ,e.getLocalizedMessage());
            }
        }).start();
    }

    public void execute(Runnable runnable) {
        if (executor == null || executor.isShutdown()) {
            start();
        }
        executor.submit(runnable);
    }
}
