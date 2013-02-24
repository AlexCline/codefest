package com.pghrecycles.pghrecycles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclingDataPopulator {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<RecyclingItem> ITEMS = new ArrayList<RecyclingItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, RecyclingItem> ITEM_MAP = new HashMap<String, RecyclingItem>();

	static {
		addItem(new RecyclingItem("1", ""));
		addItem(new RecyclingItem("2", ""));
		addItem(new RecyclingItem("3", ""));
	}

	private static void addItem(RecyclingItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class RecyclingItem {
		public String id;
		public String content;

		public RecyclingItem(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
