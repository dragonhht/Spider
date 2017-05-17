package spider.study.handler;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import java.io.*;

/**
 * 下载器.
 * <p>
 * User : Dragon_hht
 * Date : 17-5-17
 * Time : 下午8:45
 */
public class Downloader {

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
			fileName = url.replaceAll("[\\?/:*|<>\"]", "_") + "."
					+ contentType.substring(contentType.lastIndexOf("/") + 1);
		}

		return fileName;
	}


	/**
	 * 将数据保存为本地文件.
	 * @param data 数据
	 * @param filePath 文件路径
	 */
	public void saveToLocal(byte[] data, String filePath) {
		FileOutputStream fos = null;
		DataOutputStream out = null;
		File file = new File(filePath);
		try {
			fos = new FileOutputStream(file);
			out = new DataOutputStream(fos);
			for (int i = 0; i < data.length; i++) {
				out.write(data[i]);
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (out != null) {
				try {
					out.close();
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
		String filePath = null;
		// 创建HttpClient对象
		HttpClient httpClient = new HttpClient();
		// 设置HTTP连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 生成GetMethod方法
		GetMethod getMethod = new GetMethod(url);
		// 设置get请求超时5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 执行HTTP GET请求
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			// 判断访问的状态码
			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("请求失败!");
				filePath = null;
			}
			// 处理HTTP相应内容
			byte[] responseBody = getMethod.getResponseBody();
			// 生成URL
			String contentType = getMethod.getResponseHeader("Content-Type").getValue();
			filePath = "test/" + getFileNameByUrl(url, contentType);
			saveToLocal(responseBody, filePath);
		} catch (IOException e) {
			System.out.println("失败!!!");
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return filePath;
	}

}
