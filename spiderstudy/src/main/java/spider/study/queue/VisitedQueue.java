package spider.study.queue;

import java.util.HashSet;
import java.util.Set;

/**
 * 已访问过得URL的集合.
 * <p>
 * User : Dragon_hht
 * Date : 17-5-17
 * Time : 下午8:40
 */
public class VisitedQueue {

	/** 已经访问过得URL的集合. */
	private Set<String> visitedQueue = new HashSet<String>();

	/**
	 * 将URL加入队列.
	 * @param url url
	 */
	public void add(String url) {
		visitedQueue.add(url);
	}


	/**
	 * 判断队列是否为空.
	 * @return 判断结果,true为空
	 */
	public boolean isEmpty() {
		return visitedQueue.isEmpty();
	}

	/**
	 * 判断队列中是否含有指定的URL
	 * @param url 指定的URL
	 * @return 判断结果, true为包含
	 */
	public boolean isContians(String url) {
		return visitedQueue.contains(url);
	}
}
