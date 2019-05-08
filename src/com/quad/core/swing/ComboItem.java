package com.quad.core.swing;

public class ComboItem implements CanEnable {
	private final Object obj;
	private boolean isEnable;

	public ComboItem(Object obj, boolean isEnable) {
		this.obj = obj;
		this.isEnable = isEnable;
	}

	public ComboItem(Object obj) {
		this(obj, true);
	}

	public boolean isEnabled() {
		return isEnable;
	}

	public void setEnabled(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public Object getObject() {
		return obj;
	}

	public String toString() {
		return obj.toString();
	}
}