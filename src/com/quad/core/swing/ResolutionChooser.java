package com.quad.core.swing;

public final class ResolutionChooser {
	private int width;
	private int height;

	public ResolutionChooser(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder("");
		buf = buf.append(width).append(" * ").append(height);

		float ratio = width / height;
		if (Float.compare(ratio, 1.3333334F) == 0) {
			buf.append("  (4:3)");
		} else if (Float.compare(ratio, 1.25F) == 0) {
			buf.append("  (5:4)");
		} else if (Float.compare(ratio, 1.7777778F) == 0) {
			buf.append("  (16:9)");
		} else if (Float.compare(ratio, 1.6F) == 0) {
			buf.append("  (16:10)");
		}

		return buf.toString();
	}
}