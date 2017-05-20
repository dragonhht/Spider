package spider.study.useredis;


import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huang on 17-5-20.
 */
public class Main {
    public static void main(String[] args) {
        // 线程数量
        final int THREAD_POOL_SIZE = 4;
        //String url = "http://pic.yesky.com/";

        // 创建线程池
        ExecutorService es = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        for (int i = 0; i < 8; i++) {
            DownloadUseRedis downloadUseRedis = new DownloadUseRedis();
            es.submit(downloadUseRedis);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
