package client;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Vector;
import javax.swing.JPanel;

public class GraphingData extends JPanel {

	private static final int MAX = 4000;
	private int showLast = 100;
	Vector[] data;
	final int PAD = 20;

	public GraphingData(int sets) {
		data = new Vector[sets];
		for (int i = 0; i < sets; i++)
			data[i] = new Vector<Double>();
	}

	public void addData(Double value, int set) {
		data[set].add(value);
		if (data[set].size() > MAX) {
			data[set].remove(0);
		}
		repaint();
	}

	public int getShowLast() {
		return showLast;
	}

	public void setShowLast(int showLast) {
		this.showLast = showLast;
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth();
		int h = getHeight();
		// Draw ordinate.
		g2.draw(new Line2D.Double(PAD, PAD, PAD, h - PAD));
		// Draw abcissa.
		g2.draw(new Line2D.Double(PAD, h - PAD, w - PAD, h - PAD));
		// Draw labels.
		Font font = g2.getFont();
		FontRenderContext frc = g2.getFontRenderContext();
		LineMetrics lm = font.getLineMetrics("0", frc);
		float sh = lm.getAscent() + lm.getDescent();
		// Ordinate label.
		String s = "data";
		float sy = PAD + ((h - 2 * PAD) - s.length() * sh) / 2 + lm.getAscent();
		// for(int i = 0; i < s.length(); i++) {
		// String letter = String.valueOf(s.charAt(i));
		// float sw = (float)font.getStringBounds(letter, frc).getWidth();
		// float sx = (PAD - sw)/2;
		// g2.drawString(letter, sx, sy);
		// sy += sh;
		// }
		// Abcissa label.
		s = "time";
		sy = h - PAD + (PAD - sh) / 2 + lm.getAscent();
		float sw = (float) font.getStringBounds(s, frc).getWidth();
		float sx = (w - sw) / 2;
		g2.drawString(s, sx, sy);
		// Draw lines.
		double max = getMax();
		double xInc = (double) (w - 2 * PAD) / (showLast - 1);
		double scale = (double) (h - 2 * PAD) / max;

		int[] init = new int[data.length];

		for (int i = 0; i < data.length; i++) {

			init[i] = Math.max(0, data[i].size() - showLast);
			g2.setPaint(getColor(i));
			for (int j = init[i]; j < data[i].size() - 1; j++) {
				double x1 = PAD + (j - init[i]) * xInc;
				double y1 = h - PAD - scale * (Double) (data[i].get(j));
				double x2 = PAD + (j - init[i] + 1) * xInc;
				double y2 = h - PAD - scale * (Double) (data[i].get(j + 1));
				g2.draw(new Line2D.Double(x1, y1, x2, y2));
			}

		}

		g2.setPaint(Color.black);
		g2.drawString(String.valueOf(max), 0, (int) (h - PAD - scale * max));

		// Mark data points.
		for (int i = 0; i < data.length; i++) {
		
			g2.setPaint(getColor(i));

			for (int j = Math.max(0, data[i].size() - showLast); j < data[i]
					.size(); j++) {
				double x = PAD + (j - init[i]) * xInc;
				double y = h - PAD - scale * (Double)data[i].get(j);
				g2.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
			}
		}
	}
	
	private Color getColor(int set) {
		
		switch(set) {
			case 0: return Color.red;
			case 1: return Color.green;
			case 2: return Color.blue;
			case 3: return Color.orange;
			case 4: return Color.MAGENTA;
			case 5: return Color.yellow;
			case 6: return Color.BLACK;
			default: return Color.PINK;
		}
	}

	private double getMax() {
		// double max = -Integer.MAX_VALUE;
		// for (int i = Math.max(0, data.size() - showLast); i < data.size();
		// i++) {
		// if (data.get(i) > max)
		// max = data.get(i);
		// }
		// return max;
		return 1;
	}

}