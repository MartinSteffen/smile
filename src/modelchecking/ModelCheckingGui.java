package modelchecking;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.*;

import nua.NuAutomaton;

public class ModelCheckingGui extends JFrame {
	/**
	 * 
	 */
	static void addComponent(Container cont, GridBagLayout gbl, Component c,
			int x, int y, int width, int height, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}

	private static final long serialVersionUID = 1L;

	static int style = Font.PLAIN, size = 12;

	static String name = "SAN_SERIF";

	private Parser parser;

	final JTextArea t;

	final JTextArea taus;

	private NuAutomaton nua;

	public ModelCheckingGui(final NuAutomaton nua) {

		super("Model Checking");

		this.nua = nua;

		t = new JTextArea();
		taus = new JTextArea();
		t.setBackground(Color.white);
		taus.setBackground(Color.lightGray);
		taus.setEditable(false);

		parser = new Parser(this);
		setDefaultLookAndFeelDecorated(true);
		pack();

		Container c = super.getContentPane();

		GridBagLayout gbl = new GridBagLayout();
		c.setLayout(gbl);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		setSize(d.width * 1 / 2, d.height * 1 / 2);
		setLocation((d.width - getSize().width) / 2,
				(d.height - getSize().height) / 2);

		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("exit".equals(e.getActionCommand()))
					dispose();

				if ("save".equals(e.getActionCommand())) {
					JFileChooser fc = new JFileChooser();
					int returnVal = fc.showDialog(null, "Save");
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						try {
							FileOutputStream writer = new FileOutputStream(file);
							writer.write(t.getText().getBytes());
							writer.close();
						} catch (FileNotFoundException e1) {

							e1.printStackTrace();
						} catch (IOException e2) {

							e2.printStackTrace();
						}

					}
				}

				if ("open".equals(e.getActionCommand())) {

					JFileChooser fc = new JFileChooser();
					int returnVal = fc.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						try {
							FileInputStream reader = new FileInputStream(file);
							byte[] buffer = new byte[(int) file.length()];
							reader.read(buffer);
							reader.close();
							String s = new String(buffer);
							t.setText(s);
						} catch (FileNotFoundException e1) {

							e1.printStackTrace();
						} catch (IOException e2) {

							e2.printStackTrace();
						}

					}
				}

				if ("help".equals(e.getActionCommand())) {
					ModelCheckHelp h = new ModelCheckHelp();
					h.show();

				}

				if ("check".equals(e.getActionCommand())) {
					taus.setText("");
					String tmp = parser.preparsing(t.getText());
					if (tmp != null) {
						Expression expr = parser.parse(tmp);
						if (expr != null) {
							String antw = parser.variablenChecker(expr);
							if (!antw.equals("")) {
								writeError(antw);
							} else {
								expr = parser.completeTree(expr);
								parser.calculatingTetta(expr);
								parser.postparsing(expr);
								parser.print(expr);
								SatisfactionCheck scheck = new SatisfactionCheck(
										nua, expr);
								int res = scheck.Check();
								if (res == 1)
									write("Der nu-Automat erfŸllt die eingegebene Formel");
								if (res == 0)
									write("Der nu-Automat erfŸllt die duale Formel");
								if (res == -1)
									write("ErfŸllbarkeitstest nicht erfolgreich");
							}
						}
					} else {
						writeError("Ziffern ausserhalb von Anfuerungszeichen nicht erlaubt");
					}
				}
			}
		};

		AbstractButton b;
		addComponent(c, gbl, b = new JToggleButton("check"), 0, 0, 1, 1, 0, 0);
		b.addActionListener(al);
		b.setFont(new Font(name, Font.BOLD, 12));

		addComponent(c, gbl, b = new JToggleButton("help"), 1, 0, 1, 1, 0, 0);
		b.addActionListener(al);
		b.setFont(new Font(name, Font.BOLD, 12));

		addComponent(c, gbl, b = new JToggleButton("save"), 2, 0, 1, 1, 0, 0);
		b.addActionListener(al);
		b.setFont(new Font(name, Font.BOLD, 12));

		addComponent(c, gbl, b = new JToggleButton("open"), 3, 0, 1, 1, 0, 0);
		b.addActionListener(al);
		b.setFont(new Font(name, Font.BOLD, 12));

		addComponent(c, gbl, b = new JToggleButton("exit"), 4, 0, 1, 1, 0, 0);
		b.addActionListener(al);

		addComponent(c, gbl, new JScrollPane(t), 0, 1, 8, 2, 1.0, 1.0);
		t.setFont(new Font(name, style, size));
		t.setBorder(BorderFactory.createLineBorder(Color.gray));

		addComponent(c, gbl, new JScrollPane(taus), 0, 4, 8, 1, 1.0, 1.0);
		// taus = new JTextArea();
		taus.setFont(new Font(name, style, size));
		taus.setBorder(BorderFactory.createLineBorder(Color.gray));

	}

	public void write(String s) {
		taus.setForeground(Color.blue);
		taus.append("---> " + s);
	}

	public void writeError(String s) {
		taus.setForeground(Color.red);
		taus.append("Error > " + s);
	}

}
