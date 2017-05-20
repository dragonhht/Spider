package spider.study.handler;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import spider.study.queue.Queue;
import spider.study.queue.VisitedQueue;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * 下载器.
 * <p>
 * User : Dragon_hht
 * Date : 17-5-17
 * Time : 下午8:45
 */
public class Downloader implements Runnable {

    /** 将访问的URL队列. */
    private Queue queue;
    /** 已访问过的URL集合. */
    private VisitedQueue visitedQueue;

    public Downloader(Queue queue, VisitedQueue visitedQueue) {
        this.queue = queue;
        this.visitedQueue = visitedQueue;
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
           // System.out.println(Thread.currentThread().getName() +" : 下载成功.....");
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
	public String downloadFile(String url, Queue queue, VisitedQueue visitedQueue) {
	    System.out.println("开始一个页面");
		String filePath = null;
		String src = null;
		String fileName = null;
		try {
			// 获取页面文档
			Document document = Jsoup.connect(url).timeout(5000).get();
            Elements links = document.select(/*"img[src]"*/"dt a");
            for (Element element : links) {

                // 将为访问的路径添加队列
                String link = element.attr("href");
                if (!visitedQueue.isContians(link)) {
                    if (!queue.isContians(link)) {
                        queue.add(link);
                    }
                }
            }

            Elements imgs = document.select("img[src]");

            for (Element img : imgs) {
                //System.out.println(element.attr("alt") + " " + element.attr("src"));
                src = img.attr("src");
                fileName = getFileNameByUrl(src, "jpg");
                saveToLocal(src, fileName);
            }

		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

    public void run() {
        while (true) {
           if (!queue.isEmpty()) {
               downloadFile(queue.getUrl(), queue, visitedQueue);
           }
        }
    }
}
