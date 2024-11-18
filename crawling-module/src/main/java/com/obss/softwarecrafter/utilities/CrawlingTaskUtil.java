package com.obss.softwarecrafter.utilities;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.ListUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@UtilityClass
public class CrawlingTaskUtil {
    private static final Map<UUID, TaskWrapper> taskMap = new ConcurrentHashMap<>();

    private static class TaskWrapper {
        private final List<CompletableFuture<?>> futures;
        private final AtomicBoolean isRunning;
        private final Map<CompletableFuture<?>, Thread> futureThreadMap;

        public TaskWrapper() {
            this.futures = new CopyOnWriteArrayList<>();
            this.isRunning = new AtomicBoolean(true);
            this.futureThreadMap = new ConcurrentHashMap<>();
        }
    }

    public static <T> List<List<T>> splitList(List<T> list, int count) {
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException("List cannot be empty");
        }

        int totalSize = list.size();
        int partitionSize = totalSize > count ? (int) Math.ceil((double) totalSize / count) : 1;
        return ListUtils.partition(list, partitionSize);
    }

    public static CompletableFuture<Void> runAsync(TaskExecutor taskExecutor, UUID id, Runnable userRunnable) {
        validateTaskId(id);
        TaskWrapper taskWrapper = taskMap.get(id);

        Runnable wrappedRunnable = () -> {
            Thread currentThread = Thread.currentThread();
            taskWrapper.futureThreadMap.put(CompletableFuture.completedFuture(null), currentThread);

            try {
                // İnterrupt kontrolü için periyodik kontrol döngüsü
                while (taskWrapper.isRunning.get() && !Thread.currentThread().isInterrupted()) {
                    userRunnable.run();
                    break;
                }
            } catch (Exception e) {
                // İnterrupt flag'ini yeniden set et
                Thread.currentThread().interrupt();
                throw new RuntimeException("Task interrupted: " + id, e);
            } finally {
                // Cleanup
                taskWrapper.futureThreadMap.remove(CompletableFuture.completedFuture(null));
            }
        };

        CompletableFuture<Void> future = CompletableFuture.runAsync(wrappedRunnable, taskExecutor);
        taskWrapper.futures.add(future);

        return future.whenComplete((result, ex) -> cleanup(taskWrapper, future));
    }

    public static <U> CompletableFuture<U> supplyAsync(TaskExecutor taskExecutor, UUID id, Supplier<U> userSupplier) {
        validateTaskId(id);
        TaskWrapper taskWrapper = taskMap.get(id);

        Supplier<U> wrappedSupplier = () -> {
            Thread currentThread = Thread.currentThread();
            taskWrapper.futureThreadMap.put(CompletableFuture.completedFuture(null), currentThread);

            try {
                if (!taskWrapper.isRunning.get() || Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("Task was interrupted");
                }

                return userSupplier.get();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Task interrupted: " + id, e);
            } finally {
                taskWrapper.futureThreadMap.remove(CompletableFuture.completedFuture(null));
            }
        };

        CompletableFuture<U> future = CompletableFuture.supplyAsync(wrappedSupplier, taskExecutor);
        taskWrapper.futures.add(future);

        return future.whenComplete((result, ex) -> cleanup(taskWrapper, future));
    }

    private static void cleanup(TaskWrapper taskWrapper, CompletableFuture<?> future) {
        taskWrapper.futureThreadMap.remove(future);
        taskWrapper.futures.remove(future);
    }

    public static void stopTask(UUID id) {
        validateTaskId(id);
        TaskWrapper taskWrapper = taskMap.get(id);

        // Running flag'ini false yap
        taskWrapper.isRunning.set(false);

        // Tüm thread'leri interrupt et
        taskWrapper.futureThreadMap.values().forEach(Thread::interrupt);

        // Tüm future'ları iptal et
        List<CompletableFuture<?>> futuresToCancel = new ArrayList<>(taskWrapper.futures);
        futuresToCancel.forEach(future -> {
            try {
                future.cancel(true);
                cleanup(taskWrapper, future);
            } catch (Exception e) {
                System.err.println("Error while stopping future: " + e.getMessage());
            }
        });
    }

    public static void allOfCompletedThenRun(UUID id, CompletableFuture<?>[] taskList, Runnable runnable) {
        validateTaskId(id);
        TaskWrapper taskWrapper = taskMap.get(id);

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(taskList)
                .thenRun(() -> {
                    if (taskWrapper.isRunning.get() && !Thread.currentThread().isInterrupted()) {
                        runnable.run();
                    }
                });

        taskWrapper.futures.add(combinedFuture);
    }

    public static void initTask(UUID id) {
        taskMap.put(id, new TaskWrapper());
    }

    public static void taskDone(UUID id) {
        validateTaskId(id);
        TaskWrapper taskWrapper = taskMap.get(id);
        if (taskWrapper != null) {
            stopTask(id);  // Önce tüm task'ları durdur
            taskMap.remove(id);  // Sonra map'ten kaldır
        }
    }

    private static void validateTaskId(UUID id) {
        if (!taskMap.containsKey(id)) {
            throw new IllegalStateException("Task not initialized for ID: " + id);
        }
    }

    public static boolean isTaskRunning(UUID id) {
        TaskWrapper taskWrapper = taskMap.get(id);
        return taskWrapper != null && taskWrapper.isRunning.get() && !taskWrapper.futures.isEmpty();
    }
}