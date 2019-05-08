package com.quad.core.swing;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class ComboRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = 1L;

	public ComboRenderer() {
		setOpaque(true);
		setBorder(new EmptyBorder(1, 1, 1, 1));
	}

	public java.awt.Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		if (!((CanEnable) value).isEnabled()) {
			setBackground(list.getBackground());
			setForeground(UIManager.getColor("Label.disabledForeground"));
		}
		setFont(list.getFont());
		setText(value == null ? "" : value.toString());
		return this;
	}
}