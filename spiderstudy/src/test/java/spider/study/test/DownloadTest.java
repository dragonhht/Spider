package spider.study.test;

import org.junit.Test;
import spider.study.handler.Downloader;
import spider.study.queue.Queue;
import spider.study.queue.VisitedQueue;

/**
 * ClassDescription
 * <p>
 * User : Dragon_hht
 * Date : 17-5-17
 * Time : 下午8:53
 */
public class DownloadTest {
	@Test
	public void testDownload() {
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
