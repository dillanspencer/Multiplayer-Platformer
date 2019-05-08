package com.quad.test;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import com.quad.core.GameContainer;
import com.quad.core.Settings;
import com.quad.core.swing.ActionCombo;
import com.quad.core.swing.ComboItem;
import com.quad.core.swing.RateChooser;
import com.quad.core.swing.ResolutionChooser;
import com.quad.core.swing.Swing;

public class OptionManager {

	public final int[][] RESOLUTIONS = { { 1080 }, { 640 } };
	public final int[] FRAME_RATES = { 10, 25, 30, 50, 60, 75, 120 };
	public final String[] availableLanguages = { "ENGLISH", "FRENCH" };
	private final ComboItem[] resolutions;
	private final ComboItem[] rate;
	private final JComboBox<?> resolutionMenu;
	private final JComboBox<?> rateMenu;
	private final GameContainer gc;

	public OptionManager(GameContainer gc) {
		setTheme();

		this.gc = gc;

		JFrame frame = new JFrame("Options");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setPreferredSize(new Dimension(320, 200));
		frame.add(centerPanel, "Center");

		JPanel screenPanel = Swing.createBorderedPanel("Screen", 1);
		screenPanel.setLayout(new GridLayout(0, 2));
		centerPanel.add(screenPanel, "Center");

		resolutions = new ComboItem[RESOLUTIONS.length - 1];
		for (int i = 0; i < RESOLUTIONS.length - 1; i++) {
			resolutions[i] = new ComboItem(new ResolutionChooser(RESOLUTIONS[0][i], RESOLUTIONS[1][i]));
		}

		resolutionMenu = Swing.addMenuCombo("Resolution", screenPanel, resolutions, new ActionCombo() {

			@Override
			public void action(Object paramObject) {

			}
		});

		rate = new ComboItem[FRAME_RATES.length];
		for (int i = 0; i < FRAME_RATES.length - 1; i++) {
			rate[i] = new ComboItem(new RateChooser(FRAME_RATES[i]));
		}

		rateMenu = Swing.addMenuCombo("Desired Frame Rate", screenPanel, rate, new ActionCombo() {

			@Override
			public void action(Object item) {
				RateChooser p = (RateChooser) item;
				if (p != null) {
					Settings.changeFps(gc, p.getRate());
				}
			}

		});
		
		for (int i = 0; i < FRAME_RATES.length; i++) {
		      if (FRAME_RATES[i] == gc.getFrameCap()) {
		        rateMenu.setSelectedIndex(i);
		        break;
		      }
		    }

		JPanel otherPanel = new JPanel(new GridLayout(0, 3));
		centerPanel.add(otherPanel, "South");

		JPanel screenDepthPanel = Swing.createBorderedPanel("Screen properties", 1);
		screenDepthPanel.setLayout(new BoxLayout(screenDepthPanel, 3));
		otherPanel.add(screenDepthPanel);

		JRadioButton[] depth = new JRadioButton[2];
		ButtonGroup bg = new ButtonGroup();
		depth[0] = Swing.addRadioButton("16 bits", screenDepthPanel, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		bg.add(depth[0]);
		depth[1] = Swing.addRadioButton("32 bits", screenDepthPanel, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		bg.add(depth[1]);

		JCheckBox checkBox = Swing.addCheckBox("Fullscreen", screenDepthPanel, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean selection = ((JCheckBox)e.getSource()).isSelected();
				System.out.println(selection);
				if(selection) {
					gc.setFullscreen(1);
				}else {
					gc.setFullscreen(0);
				}
			}
		});

		JPanel serverPanel = Swing.createBorderedPanel("Start Server?", 1);
		serverPanel.setLayout(new BoxLayout(serverPanel, 3));
		otherPanel.add(serverPanel);

		JCheckBox serverCheckBox = Swing.addCheckBox("Server", serverPanel, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean selection = ((JCheckBox)e.getSource()).isSelected();
				System.out.println(selection);
				if(selection) {
					gc.setServer(true);
				}else {
					gc.setServer(false);
				}
			}
		});
		
		JPanel userNamePanel = Swing.createBorderedPanel("Username", 1);
		userNamePanel.setLayout(new BoxLayout(userNamePanel, 3));
		otherPanel.add(userNamePanel);
		
		JTextField userArea = new JTextField();
		userArea.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = ((JTextField)e.getSource()).getText();
				Settings.setUsername(userName);
			}
		});
		userNamePanel.add(userArea);
		
		JPanel southPanel = new JPanel(new GridLayout(0, 2));
		frame.add(southPanel, "South");
		Swing.addButton("Accept", southPanel, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				gc.setWidth(Settings.WIDTH);
				gc.setHeight(Settings.HEIGHT);
				gc.setScale(Settings.SCALE);
				gc.setTitle("New Game - Dillan Spencer");
				gc.setClearScreen(true);
				gc.start();
			}
		});
		Swing.addButton("Cancel", southPanel, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		////////////
		frame.pack();
	}

	private static void setTheme() {
		// set theme
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

}
