package ru.job4j.thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Загрузка файла.
 */
public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream out = new FileOutputStream("files/tmp/" + fileName)) {
            byte[] dataBuffer = new byte[speed];
            int byteRead;
            while ((byteRead = in.read(dataBuffer, 0, speed)) != -1) {
                out.write(dataBuffer, 0, byteRead);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        verification(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }

    private static void verification(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Not set args. Set args: url speed");
        }

    }
}
