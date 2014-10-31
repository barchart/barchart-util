package com.barchart.util.guice.component;

import java.util.HashSet;
import java.util.Iterator;

public class UniqueObjectPathSet implements Iterable<String> {

	private final HashSet<String> set;

	public UniqueObjectPathSet() {
		this.set = new HashSet<String>();
	}

	public void add(String key) {
		String parentObjectPath = getParentObjectPath(key);
		if (!parentObjectPath.isEmpty()) {
			set.add(parentObjectPath);
			add(parentObjectPath);
		}
	}

	private String getParentObjectPath(String key) {
		int lastDotIndex = key.lastIndexOf('.');
		if (lastDotIndex == -1) {
			return "";
		} else {
			return key.substring(0, lastDotIndex);
		}
	}

	@Override
	public Iterator<String> iterator() {
		return set.iterator();
	}

}
