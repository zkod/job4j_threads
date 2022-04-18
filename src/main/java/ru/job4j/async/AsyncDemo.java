package ru.job4j.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Класс для демонтрации асинхронизации.
 */
public class AsyncDemo {
    private static void iWork() throws InterruptedException {
        int count = 0;
        while (count < 10) {
            System.out.println("Вы: Я работаю.");
            TimeUnit.SECONDS.sleep(1);
            count++;
        }
    }

    public static CompletableFuture<Void> goToTrash() {
        return CompletableFuture.runAsync(() -> {
            System.out.println("Сын: Мам/Пап, я пошел выносить мусор.");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Сын: Мам/Пап, я вернулся!");
        });
    }

    public static void runAsyncExample() throws InterruptedException {
        CompletableFuture<Void> gtt = goToTrash();
        iWork();
    }

    public static CompletableFuture<String> buyProduct(String product) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Сын: Мам/Пап, я пошел в магазин.");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Сын: Мам/Пап, я купил " + product + ".");
            return product;
        });
    }

    public static void supplyAsyncExample() throws ExecutionException, InterruptedException {
        CompletableFuture<String> bp = buyProduct("Молоко");
        iWork();
        System.out.println("Куплено: " + bp.get());
    }

    public static void thenRunExample() throws InterruptedException {
        CompletableFuture<Void> gtt = goToTrash();
        gtt.thenRun(() -> {
            int count = 0;
            while (count < 3) {
                System.out.println("Сын: Я мою руки.");
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                count++;
            }
            System.out.println("Сын: Я помыл руки.");
        });
        iWork();
    }

    public static void thenAcceptExample() throws ExecutionException, InterruptedException {
        CompletableFuture<String> bp = buyProduct("Молоко");
        bp.thenAccept(product -> System.out.println("Сын: Я убрал " + product + " в холодильник."));
        iWork();
        System.out.println("Куплено: " + bp.get());
    }

    public static void thenApplyExample() throws ExecutionException, InterruptedException {
        CompletableFuture<String> bp = buyProduct("Молоко")
                .thenApply(product -> "Сын: Я налил тебе в кружку " + product + ". Держи.");
        iWork();
        System.out.println(bp.get());
    }

    public static void thenComposeExample() throws InterruptedException {
        CompletableFuture<Void> result = buyProduct("Молоко").thenCompose(a -> goToTrash());
        iWork();
    }

    public static void thenCombineExample() throws InterruptedException, ExecutionException {
        CompletableFuture<String> result = buyProduct("Молоко")
                .thenCombine(buyProduct("Хлеб"), (r1, r2) -> "Куплены " + r1 + " и " + r2);
        iWork();
        System.out.println(result.get());
    }

    public static CompletableFuture<Void> washHands(String name) {
        return CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(name + ", моет руки.");
        });
    }

    public static void allOfExample() throws InterruptedException {
        CompletableFuture<Void> all = CompletableFuture.allOf(washHands("Папа"), washHands("Мама"),
                washHands("Ваня"), washHands("Боря"));
        TimeUnit.SECONDS.sleep(3);
    }

    public static CompletableFuture<String> whoWashHands(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return name + ", моет руки.";
        });
    }

    public static void anyOfExample() throws ExecutionException, InterruptedException {
        CompletableFuture<Object> first = CompletableFuture.anyOf(whoWashHands("Папа"), whoWashHands("Мама"),
                whoWashHands("Ваня"), whoWashHands("Боря"));
        System.out.println("Кто сейчас моет руки?");
        TimeUnit.SECONDS.sleep(1);
        System.out.println(first.get());
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] actions = new String[]{"runAsyncExample", "supplyAsyncExample",
                "thenRunExample", "thenAcceptExample", "thenApplyExample",
                "thenComposeExample", "thenCombineExample", "allOfExample", "anyOfExample"};
        String action = actions[8];
        if (action.equals(actions[0])) {
            runAsyncExample();
        }
        if (action.equals(actions[1])) {
            supplyAsyncExample();
        }
        if (action.equals(actions[2])) {
            thenRunExample();
        }
        if (action.equals(actions[3])) {
            thenAcceptExample();
        }
        if (action.equals(actions[4])) {
            thenApplyExample();
        }
        if (action.equals(actions[5])) {
            thenComposeExample();
        }
        if (action.equals(actions[6])) {
            thenCombineExample();
        }
        if (action.equals(actions[7])) {
            allOfExample();
        }
        if (action.equals(actions[8])) {
            anyOfExample();
        }
    }
}
