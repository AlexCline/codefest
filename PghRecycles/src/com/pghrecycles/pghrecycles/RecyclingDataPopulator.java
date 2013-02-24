package com.pghrecycles.pghrecycles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.Resources;

public class RecyclingDataPopulator {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<RecyclingItem> ITEMS = new ArrayList<RecyclingItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, RecyclingItem> ITEM_MAP = new HashMap<String, RecyclingItem>();

//	static {
////		addItem(new RecyclingItem("0", res.getString(R.string.recycling_category_name_0), res.getString(R.string.recycling_category_details_0)));
////		addItem(new RecyclingItem("1", res.getString(R.string.recycling_category_name_1), res.getString(R.string.recycling_category_details_1)));
//////		addItem(new RecyclingItem("1", "Plastics", "(plastics details)"));
////		addItem(new RecyclingItem("2", res.getString(R.string.recycling_category_name_2), res.getString(R.string.recycling_category_details_2)));
////		addItem(new RecyclingItem("3", res.getString(R.string.recycling_category_name_3), res.getString(R.string.recycling_category_details_3)));
////		addItem(new RecyclingItem("4", res.getString(R.string.recycling_category_name_4), res.getString(R.string.recycling_category_details_4)));
////		addItem(new RecyclingItem("5", res.getString(R.string.recycling_category_name_5), res.getString(R.string.recycling_category_details_5)));
////		addItem(new RecyclingItem("6", res.getString(R.string.recycling_category_name_6), res.getString(R.string.recycling_category_details_6)));
////		addItem(new RecyclingItem("7", res.getString(R.string.recycling_category_name_7), res.getString(R.string.recycling_category_details_7)));
////		addItem(new RecyclingItem("8", res.getString(R.string.recycling_category_name_8), res.getString(R.string.recycling_category_details_8)));
//	}
	
	private static volatile boolean initialized = false;
	public static void initialize(Resources res) {
		if (!initialized) {
			addItem(new RecyclingItem("0", res.getString(R.string.recycling_category_name_0), res.getString(R.string.recycling_category_details_0)));
			addItem(new RecyclingItem("1", res.getString(R.string.recycling_category_name_1), res.getString(R.string.recycling_category_details_1)));
	//		addItem(new RecyclingItem("1", "Plastics", "(plastics details)"));
			addItem(new RecyclingItem("2", res.getString(R.string.recycling_category_name_2), res.getString(R.string.recycling_category_details_2)));
			addItem(new RecyclingItem("3", res.getString(R.string.recycling_category_name_3), res.getString(R.string.recycling_category_details_3)));
			addItem(new RecyclingItem("4", res.getString(R.string.recycling_category_name_4), res.getString(R.string.recycling_category_details_4)));
			addItem(new RecyclingItem("5", res.getString(R.string.recycling_category_name_5), res.getString(R.string.recycling_category_details_5)));
			addItem(new RecyclingItem("6", res.getString(R.string.recycling_category_name_6), res.getString(R.string.recycling_category_details_6)));
			addItem(new RecyclingItem("7", res.getString(R.string.recycling_category_name_7), res.getString(R.string.recycling_category_details_7)));
			addItem(new RecyclingItem("8", res.getString(R.string.recycling_category_name_8), res.getString(R.string.recycling_category_details_8)));
			initialized = true;
		}

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
		public String details;

		public RecyclingItem(String id, String content, String details) {
			this.id = id;
			this.content = content;
			this.details = details;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
