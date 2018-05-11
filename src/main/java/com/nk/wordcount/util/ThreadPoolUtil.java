package com.nk.wordcount.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具方法
 *
 * @author Created by niuyang on 2018/5/11.
 */
public class ThreadPoolUtil {
	
	private ExecutorService exec;
	
	private static volatile ThreadPoolUtil instance;

	/**
	 * 线程池最少线程数
	 */
    public static final int THREAD_POOL_CORE_SIZE = 5;// 线程池最少线程数

	/**
	 * 最大线程数
	 */
    public static final int THREAD_POOL_MAX_SIZE = 20;// 最大线程数

	/**
	 * 最大线程等待数
	 */
    public static final int THREAD_MAX_THREAD_WAIT = 1000;

	/**
	 * 最长等待时间
	 */
	public static final int THREAD_POOL_WAIT_SECONDS = 5 * 60;
	
	private ThreadPoolUtil() {
		exec = new ThreadPoolExecutor(THREAD_POOL_CORE_SIZE, THREAD_POOL_MAX_SIZE,THREAD_POOL_WAIT_SECONDS, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(THREAD_MAX_THREAD_WAIT), new ThreadPoolExecutor.AbortPolicy());
	}
	
	public static ThreadPoolUtil getInstance() {
		if (instance == null) {
			synchronized (ThreadPoolUtil.class) {
				if (instance == null) {
					instance = new ThreadPoolUtil();
				}
			}
		}
		return instance;
	}
	
	public <T> Future<T> submit(Callable<T> command) {
		return exec.submit(command);
	}

	public void shutdown() {
		exec.shutdown();
	}

}
