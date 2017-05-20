package spider.study.useredis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by huang on 17-5-20.
 */
public class DownloadUseRedis implements Runnable {

    private Jedis jedis = new Jedis("localhost", 6379);

    public DownloadUseRedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public DownloadUseRedis() {
        String url = "";
        if (!jedis.sismember("done-url" , url)) {
            jedis.rpush("will-url" , url);
        }
    }

    /**
     * 通过URL获取文件名.
     * @param url URL
     * @param contentType 页面contentType
     * @return 文件名
     */
    public String getFileNameByUrl(String url, String contentType) {
        String fileName = null;
        // 移除http 或 https
        url = url.substring(url.indexOf("://") + 3, url.length());

        if (contentType.indexOf("html") != -1) {
            // text/html类型
            fileName = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
        } else {
            // application/pdf类型
            fileName = url.replaceAll("[\\?/:.*|<>\"]", "_")/* + "."
					+ contentType.substring(contentType.lastIndexOf("/") + 1)*/;
        }

        return fileName;
    }


    /**
     * 将数据保存为本地文件.
     * @param url url路径
     * @param fileName 文件名
     */
    public void saveToLocal(String url, String fileName) {
        String filePath = "test/";
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            // 建立连接
            URL toUrl = new URL(url);
            URLConnection urlConnection = toUrl.openConnection();
            inputStream = urlConnection.getInputStream();

            // 保存文件到本地
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            fos = new FileOutputStream(filePath + "/" + fileName);
            int len = 0;
            byte[] data = new byte[1024];
            while ((len = inputStream.read(data)) != -1) {
                fos.write(data, 0, len);
            }
            System.out.println(Thread.currentThread().getName() +" : 下载成功.....");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 下载网页
     * @param url 网页URL
     * @return 文件路径
     */
public String downloadFile(String url) {
    System.out.println("开始一个页面");
    String filePath = null;
    String src = null;
    String fileName = null;
    try {
        // 获取页面文档
        Document document = Jsoup.connect(url).timeout(10000).get();
        Elements links = document.select(/*"img[src]"*/"a[href]");
        for (Element element : links) {
            // 将要访问的路径添加队列
            String link =element.attr("href");
                if (!jedis.sismember("done-url" , link)) {
                    jedis.rpush("will-url" , link);
                }
        }
        Elements imgs = document.select("img[src]");
        for (Element img : imgs) {
            src = img.attr("src");
            fileName = getFileNameByUrl(src, "jpg");
            // System.out.println(src);
            if (isRight(src)) {
                if (!jedis.sismember("done-img", src)) {
                    saveToLocal(src, fileName);
                    jedis.sadd("done-img", src);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return filePath;
}

    /**
     * 用正则表达式对比url格式
     * @param url url
     * @return 是否为完全的URL
     */
    public static boolean isRight(String url){
        boolean ok=false;
        Pattern pattern=Pattern.compile("^http://([\\a-zA-Z0-9])+[\\a-zA-Z0-9]+([\\a-zA-Z0-9])?$");
        Matcher matcher=pattern.matcher(url);
        ok=matcher.matches();
        return ok;
    }

    public void run() {
        while (true) {
            try {
                String url = null;
                    url = jedis.lpop("will-url");
                System.out.println("URL:::: " + url);
                if (!jedis.sismember("done-url" , url)) {
                    jedis.sadd("done-url", url);
                    downloadFile(url);
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e1) {
                    System.out.println("失败");
                    e1.printStackTrace();
                }
                System.out.println("失败");
                e.printStackTrace();
            }

        }
    }
}
