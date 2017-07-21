package nua;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;


public class NuView extends JScrollPane

{

	/**
	 * Das Kommando, mit dem das Programm dot aufgerufen wird.
	 * Idealerweise sollte das Programm im PATH vorzufinden sein.
	 */
	static final public String dotCommand = "dot";

	Point viewPosition;
	Point clickPosition;
	Point picSize;

	public NuView(NuAutomaton nua) throws IOException, InterruptedException {
		final String dotname = NuView.saveTempDotfile(nua);
		System.err.println(".dot saved to: " + dotname);
		final String picname = NuView.dot2Png(dotname);
		System.err.println(".png saved to: " + picname);
		// Erstellt ein ImageIcon mit dem angegebenen Dateinamen. Das
		// Bild wird mit Hilfe eines MediaTracker vorgeladen.
		final ImageIcon pic = new ImageIcon(picname);
		// Breite und Höhe des Icons werden gespeichert
		this.picSize = new Point(pic.getIconWidth(), pic.getIconHeight());

		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel(pic), BorderLayout.CENTER);
		getViewport().add(panel);
		// Vertikale Scrollbar
		final JScrollBar vsb = getVerticalScrollBar();
		// Horizontale Scrollbar
		final JScrollBar hsb = getHorizontalScrollBar();

		vsb.setValue(pic.getIconHeight());
		hsb.setValue(pic.getIconWidth());

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				NuView.this.clickPosition = e.getPoint();
				NuView.this.viewPosition = getViewport().getViewPosition();
			}
		}

		);
		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				int xnew = NuView.this.viewPosition.x;
				int ynew = NuView.this.viewPosition.y;

				final Point dragPosition = e.getPoint();

				final Rectangle r = getViewport().getViewRect();

				if (NuView.this.picSize.x > r.width) {
					xnew = Math.max(0, xnew + NuView.this.clickPosition.x
						- dragPosition.x);
					xnew = Math.min(xnew, NuView.this.picSize.x - r.width);
				}

				if (NuView.this.picSize.y > r.height) {
					ynew = Math.max(0, ynew + NuView.this.clickPosition.y
						- dragPosition.y);
					ynew = Math.min(ynew, NuView.this.picSize.y - r.height);
				}

				getViewport().setViewPosition(new Point(xnew, ynew));
			}
		});
	}

	/**
	 * nummeriert alle Bestandteile eines Nu-Automaten
	 */
	static int enumerateComponents(NuAutomaton nua) {
		int i = 0;
		for (final NuState s : nua.states) {
			s.uniqueId = i++;
			for (final NuTransition t : s.transitions)
				t.uniqueId = i++;
		}
		return i;
	}

	/**
	 * schreibt ein .dot-File in den PrintWriter, das den Nu-Automaten
	 * graphisch darstellt
	 * 
	 * @throws IOException
	 */
	static public void makeDotfile(Writer writer, NuAutomaton nua)
			throws IOException {
		NuView.enumerateComponents(nua);
		writer.append("digraph {\n");
		for (final NuState source : nua.states) {
			NuView.writeState(writer, source);
			for (final NuTransition trans : source.transitions) {
				NuView.writeTransition(writer, trans);
			}
		}
		writer.append("}\n");
	}

	private static void writeDot(Writer writer, int dotId)
			throws IOException {
		writer.append((dotId) + "[label=\"\",shape=point];\n");
	}

	private static void writeTransition(Writer writer, NuTransition trans)
			throws IOException {
		String action = trans.action;
		if (trans.debugInfo != null)
			action += "\\n" + trans.debugInfo;
		// Eine Transition, die mehr als einen Target besitzt, wird rot
		// gefärbt.
		if ((trans.targets.size() != 1)) {
			NuView.writeDot(writer, trans.uniqueId);
			NuView.solidArrow(writer, trans.source.uniqueId,
					trans.uniqueId, action);
			for (final NuState s2 : trans.targets) {
				NuView.dashedArrow(writer, trans.uniqueId, s2.uniqueId);
			}
		}
		// Transitionen mit nur einem Target werden schwarz gefärbt.
		else {
			for (final NuState s2 : trans.targets) {
				NuView.simpleArrow(writer, trans.source.uniqueId,
						s2.uniqueId, action);
			}
		}
	}

	private static void dashedArrow(Writer writer, int sourceId,
			int targetId) throws IOException {
		writer.append(sourceId + " -> " + targetId
			+ "[label=\"\",style=dotted,color=red];\n");
	}

	private static void solidArrow(Writer writer, int sourceId,
			int targetId, String label) throws IOException {
		writer.append(sourceId + " -> " + targetId
			+ "[label=\"\",headlabel=\"" + label
			+ "\",dir=none,color=red];\n");
	}

	private static void simpleArrow(Writer writer, int sourceId,
			int targetId, String label) throws IOException {
		writer.append(sourceId + " -> " + targetId + "[label=\"" + label
			+ "\"];\n");
	}

	private static void writeState(Writer writer, NuState state)
			throws IOException {
		String label;
		if (state instanceof IllegalNuState)
			label = ((IllegalNuState) state).description;
		else
			label = "";

		writer.append(state.uniqueId + "[label=\"" + label);
		if (state.debugInfo != null)
			writer.append(state.debugInfo);
		writer.append("\"");
		// illegaler Rootstate wird als rotes Fünfeck dargestellt
		if (state instanceof IllegalNuState && state.isRootState)
			writer
					.append(",shape=polygon,sides=5,peripheries=2,color=red,style=filled");
		else if (state instanceof IllegalNuState)
			// illegaler State wird als roter Kreis dargestellt
			writer.append(",peripheries=2,color=red,style=filled");
		else if (state.isRootState)
			// Rootstate wird als hellblauer Fünfeck dargestellt
			writer
					.append(",shape=polygon,sides=5,peripheries=2,color=lightblue,style=filled");
		writer.append("];\n");
	}

	/**
	 * erzeugt eine temporäre Datei, die den Nu-Automaten als .dot-File
	 * enthält
	 */
	static public String saveTempDotfile(NuAutomaton nua)
			throws IOException {
		final File dotfile = File.createTempFile("nua", ".dot");
		dotfile.deleteOnExit();

		final BufferedWriter out = new BufferedWriter(new FileWriter(
				dotfile));
		NuView.makeDotfile(out, nua);
		out.close();

		return dotfile.getAbsolutePath();
	}

	/**
	 * ruft das Programm "dot" auf, um aus einem Dotfile eine
	 * .png-Grafik zu generieren.
	 * <p>
	 * siehe http://www.graphviz.org/ oder die Datei dotguide.pdf in der
	 * Dokumentation
	 */
	static public String dot2Png(String dotfilename) throws IOException,
			InterruptedException {
		final File pngfile = File.createTempFile("nua", ".png");
		pngfile.deleteOnExit();

		final Runtime rt = Runtime.getRuntime();
		final String cmd = NuView.dotCommand + " -Tpng " + dotfilename
			+ " -o" + pngfile.getAbsolutePath();
		final Process p = rt.exec(cmd);
		p.waitFor();

		return pngfile.getAbsolutePath();
	}
}
