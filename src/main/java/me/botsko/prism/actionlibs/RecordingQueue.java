package me.botsko.prism.actionlibs;

import java.util.concurrent.LinkedBlockingQueue;

import me.botsko.prism.actions.Handler;

public class RecordingQueue {

	/**
	 * 
	 */
	private static final LinkedBlockingQueue<Handler> queue = new LinkedBlockingQueue<>();

	/**
	 * 
	 */
	public static int getQueueSize() {
		return queue.size();
	}

	/**
	 * 
	 * @param a
	 */
	public static void addToQueue(final Handler a) {

		if (a == null)
			return;

		// some basic sanity checks
		if (a.getSourceName().trim().isEmpty())
			return;

		queue.add(a);

	}

	/**
	 * 
	 * @return
	 */
	public static LinkedBlockingQueue<Handler> getQueue() {
		return queue;
	}
}