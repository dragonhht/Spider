package spider.study.handler;

import spider.study.handler.Downloader;
import spider.study.queue.Queue;
import spider.study.queue.VisitedQueue;

import javax.swing.text.Document;

/**
 * Created by huang on 17-5-20.
 */
public class Main {
    public static void main(String[] args) {
        // 线程数量
        final int THREAD_POOL_SIZE = 4;
        String url = "http://pic.yesky.com/";
        Queue queue = new Queue();
        VisitedQueue visitedQueue = new VisitedQueue();
        queue.add(url);

        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            Thread thread = new Thread(new Downloader(queue, visitedQueue), "线程 " + i + " ");
            thread.start();
        }
    }
}
