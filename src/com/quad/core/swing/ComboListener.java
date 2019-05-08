package com.quad.core.swing;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;


public class ComboListener implements java.awt.event.ActionListener {
	private final JComboBox combo;
	private Object currentItem;
	private final ActionCombo action;

	public ComboListener(JComboBox combo, ActionCombo action) {
		this.combo = combo;
		combo.setSelectedIndex(0);
		currentItem = combo.getSelectedItem();
		this.action = action;
	}

	public void actionPerformed(ActionEvent e) {
		Object tempItem = combo.getSelectedItem();
		if (!((CanEnable) tempItem).isEnabled()) {
			combo.setSelectedItem(currentItem);
		} else {
			currentItem = tempItem;
		}
		action.action(((ComboItem) tempItem).getObject());
	}
}
