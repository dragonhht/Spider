package spider.study;

import spider.study.handler.Downloader;
import spider.study.queue.Queue;
import spider.study.queue.VisitedQueue;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                boolean ok = true;
                String url = "http://pic.yesky.com/";
                Downloader downloader = new Downloader();
                Queue queue = new Queue();
                VisitedQueue visitedQueue = new VisitedQueue();
                downloader.downloadFile(url, queue, visitedQueue);
                while (ok) {
                    if (!queue.isEmpty()) {
                        url = queue.getUrl();
                        downloader.downloadFile(url, queue, visitedQueue);
                    } else {
                        ok = false;
                    }
                }
            }
        });
        thread.start();
    }
}
