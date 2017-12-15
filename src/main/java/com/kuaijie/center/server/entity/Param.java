package com.kuaijie.center.server.entity;

public class Param {
	private final Class[] clazzs;

	public Param(Class[] clazzs) {
		this.clazzs = clazzs;
	}
	public Class getClazz(int i) {
		if (i < clazzs.length)
			return clazzs[i];
		return null;
	}
	
	public int size() {
		return clazzs.length;
	}
	
	
}
