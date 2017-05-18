package spider.study.queue;

import java.util.LinkedList;

/**
 * 用于保存将要访问的URL队列.
 * <p>
 * User : Dragon_hht
 * Date : 17-5-17
 * Time : 下午8:27
 */
public class Queue {
	/** 保存URL的队列. */
	private LinkedList<String> queue = new LinkedList<String>();

	/**
	 * 将URL加入队列.
	 * @param url url
	 */
	public void add(String url) {
		queue.add(url);
	}

	/**
	 * 获取一个URL,并且将该URL从队列中移除.
	 * @return URL
	 */
	public String getUrl() {
		return queue.removeFirst();
	}

	/**
	 * 判断队列是否为空.
	 * @return 判断结果,true为空
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	/**
	 * 判断队列中是否含有指定的URL
	 * @param url 指定的URL
	 * @return 判断结果, true为包含
	 */
	public boolean isContians(String url) {
		return queue.contains(url);
	}

	public LinkedList<String> getQueue() {
		return queue;
	}
}
