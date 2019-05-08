package com.quad.core.swing;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public final class Swing {
	
	public static JPanel createBorderedPanel(String title, int margin)
	  {
	    JPanel panel = new JPanel();
	    panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title), BorderFactory.createEmptyBorder(margin, margin, margin, margin)));

	    return panel;
	  }
	  
	  public static JButton addButton(String name, JPanel panel, ActionListener a) {
	    JButton button = new JButton(name);
	    button.addActionListener(a);
	    panel.add(button);
	    return button;
	  }
	  
	  public static JComboBox<?> addMenuCombo(String name, JPanel panel, Object[] tab, ActionCombo a) {
	    JComboBox<?> combo = new JComboBox<Object>(tab);
	   // combo.setRenderer(new ComboRenderer());
	    combo.addActionListener(new ComboListener(combo, a));
	    if (name != null) {
	      JLabel label = new JLabel(name);
	      label.setLabelFor(combo);
	      JPanel container = new JPanel();
	      container.add(label);
	      container.add(combo);
	      panel.add(container);
	    } else {
	      panel.add(combo);
	    }
	    return combo;
	  }
	  
	  public static JMenu addMenu(JMenuBar bar, String name, String ico) {
	    JMenuBar menuBar = bar;
	    if (menuBar == null) {
	      menuBar = new JMenuBar();
	    }
	    JMenu menu = new JMenu(name);
	    if (ico != null) {
	      menu.setIcon(new ImageIcon(ico));
	    }
	    menuBar.add(menu);
	    return menu;
	  }
	  
	  public static JMenuItem addMenuItem(JMenu menu, String name, String ico, ActionListener a) {
	    JMenuItem item = new JMenuItem(name);
	    item.addActionListener(a);
	    menu.add(item);
	    if (ico != null) {
	      item.setIcon(new ImageIcon(ico));
	    }
	    return item;
	  }
	  
	  public static JCheckBox addCheckBox(String name, JPanel panel, ActionListener a) {
	    JCheckBox checkBox = new JCheckBox(name);
	    checkBox.addActionListener(a);
	    panel.add(checkBox);
	    return checkBox;
	  }
	  
	  public static JRadioButton addRadioButton(String name, JPanel panel, ActionListener a) {
	    return addRadioButton(name, panel, null, a);
	  }
	  
	  public static JRadioButton addRadioButton(String name, JPanel panel, String tip, ActionListener a) {
	    JRadioButton radio = new JRadioButton(name);
	    radio.setToolTipText(tip);
	    radio.addActionListener(a);
	    panel.add(radio);
	    return radio;
	  }
	  
	  public static JTextField createField(String name, JPanel panel, int labelWidth, int fieldWidth, int height) {
	    JPanel container = new JPanel();
	    JTextField field = new JTextField(fieldWidth);
	    field.setMinimumSize(new Dimension(fieldWidth, height));
	    field.setMaximumSize(new Dimension(fieldWidth, height));
	    field.setEditable(false);
	    if (name != null) {
	      JLabel label = new JLabel(name + ":");
	      label.setLabelFor(field);
	      label.setPreferredSize(new Dimension(labelWidth, height));
	      container.add(label);
	    }
	    container.add(field);
	    panel.add(container);
	    return field;
	  }
	  
	  public static JDialog createDialog(JFrame owner, String title, int width, int height) {
	    JDialog dialog = new JDialog(owner, title);
	    dialog.setDefaultCloseOperation(0);
	    dialog.setPreferredSize(new Dimension(width, height));
	    dialog.setResizable(false);
	    dialog.addWindowListener(new WindowAdapter()
	    {
	      public void windowClosing(WindowEvent e)
	      {
	        Swing.terminateDialog(dialog);
	      }
	    });
	    return dialog;
	  }
	  
	  public static void startDialog(JDialog dialog) {
	    dialog.validate();
	    dialog.pack();
	    dialog.getParent().setEnabled(false);
	    dialog.setLocationRelativeTo(null);
	    dialog.setVisible(true);
	  }
	  
	  public static void terminateDialog(JDialog dialog) {
	    dialog.getParent().setEnabled(true);
	    dialog.dispose();
	  }

}
