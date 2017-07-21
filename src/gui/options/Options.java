package gui.options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

// Die Klasse Options enthält die Werte der in den Options eingestellten Werte.
// Die Werte werden in der Datei options.ini gespeichtert.

public class Options {

	private File file = new File("options.ini");
	
	public int undoDepth;
	public int compositeSize;
	public int entrySize;
	public int exitSize;
	public int choiceSize;
	public int finalSize;
	public boolean coL, chL, enL, exL, fiL, tL;
	
	// Konstruktor
	public Options() {
		load();
	}
	
	// laden der ini-Datei
	public void load() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			undoDepth = Integer.parseInt(reader.readLine());
			compositeSize = Integer.parseInt(reader.readLine());
			entrySize = Integer.parseInt(reader.readLine());
			exitSize = Integer.parseInt(reader.readLine());
			choiceSize = Integer.parseInt(reader.readLine());
			finalSize = Integer.parseInt(reader.readLine());
			coL = Boolean.parseBoolean(reader.readLine());
			chL = Boolean.parseBoolean(reader.readLine());
			enL = Boolean.parseBoolean(reader.readLine());
			exL = Boolean.parseBoolean(reader.readLine());
			fiL = Boolean.parseBoolean(reader.readLine());
			tL = Boolean.parseBoolean(reader.readLine());
		} catch (FileNotFoundException e) {
			System.out.println("ups");
			setDefault();
			save();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Unable to load Options.\n" + e.getMessage(),
					"Loading Failure", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	// speichern der Werte in der ini-Datei
	public void save() {
		try {
			PrintWriter printer = new PrintWriter(new FileWriter(file), true);
			printer.println(undoDepth);
			printer.println(compositeSize);
			printer.println(entrySize);
			printer.println(exitSize);
			printer.println(choiceSize);
			printer.println(finalSize);
			printer.println(coL);
			printer.println(chL);
			printer.println(enL);
			printer.println(exL);
			printer.println(fiL);
			printer.println(tL);
			printer.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Unable to save Options for some Reason.",
					"Saving Failure", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	// Zurücksetzen der Werte auf Standardwerte
	protected void setDefault() {
		undoDepth = -1;
		compositeSize = 40;
		entrySize = 5;
		exitSize = 5;
		choiceSize = 10;
		finalSize = 10;
		coL = true;
		chL = true;
		enL = true;
		exL = true;
		fiL = true;
		tL = true;
	}
	
	// Öffnen eines Frames zum Einstellen der Options
	public void showOptionsDialog() {
		new OptionsDialog(this);
	}
}