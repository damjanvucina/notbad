package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;

/**
 * The class that represents a status panel providing user with the information
 * about the length of the current document, current line and column number,
 * current selection length and a clock.
 * 
 * @author Damjan Vučina
 */
public class JStatusPanel extends JPanel implements SingleDocumentListener {

	/** The document length label tag. */
	public String lengthLabelDefault = "length: ";

	/** The line number label tag. */
	public String lnLabelDefault = "Ln: ";

	/** The column number label tag. */
	public String colLabelDefault = "Col: ";

	/** The selection length label tag. */
	public String selLabelDefault = "Sel: ";

	/** The length label. */
	private JLabel lengthLabel;

	/** The line number label. */
	private JLabel lnLabel;

	/** The column number label. */
	private JLabel colLabel;

	/** The selection length label. */
	private JLabel selLabel;

	/** The editor info panel. */
	private JPanel editorInfoPanel;

	/** The clock. */
	private Clock clock;

	/**
	 * The reference to the object responsible for acquiring the text for the
	 * current locale.
	 */
	private FormLocalizationProvider flp;

	/** The selection length. */
	private int selectionLength;

	/**
	 * Instantiates a new j status panel.
	 *
	 * @param flp
	 *            The reference to the object responsible for acquiring the text for
	 *            the current locale.
	 *
	 * @param layout
	 *            the layout
	 */
	public JStatusPanel(FormLocalizationProvider flp, LayoutManager layout) {
		setLayout(layout);

		this.flp = flp;
		lnLabel = new JLabel();
		colLabel = new JLabel();
		selLabel = new JLabel();
		editorInfoPanel = new JPanel();
	}

	/**
	 * Gets the length label.
	 *
	 * @return the length label
	 */
	public JLabel getLengthLabel() {
		return lengthLabel;
	}

	/**
	 * Gets the line number label.
	 *
	 * @return the line number label
	 */
	public JLabel getLnLabel() {
		return lnLabel;
	}

	/**
	 * Gets the column number label.
	 *
	 * @return the column number label
	 */
	public JLabel getColLabel() {
		return colLabel;
	}

	/**
	 * Gets the selection length label.
	 *
	 * @return the selection length label
	 */
	public JLabel getSelLabel() {
		return selLabel;
	}

	/**
	 * Sets the length label default.
	 *
	 * @param lengthLabelDefault
	 *            the new length label default
	 */
	public void setLengthLabelDefault(String lengthLabelDefault) {
		this.lengthLabelDefault = lengthLabelDefault;
	}

	/**
	 * Sets the line number label.
	 *
	 * @param lnLabelDefault
	 *            the new line number label
	 */
	public void setLnLabelDefault(String lnLabelDefault) {
		this.lnLabelDefault = lnLabelDefault;
	}

	/**
	 * Sets the column number label.
	 *
	 * @param colLabelDefault
	 *            the new column number label
	 */
	public void setColLabelDefault(String colLabelDefault) {
		this.colLabelDefault = colLabelDefault;
	}

	/**
	 * Sets the selection length label.
	 *
	 * @param selLabelDefault
	 *            the new selection length label
	 */
	public void setSelLabelDefault(String selLabelDefault) {
		this.selLabelDefault = selLabelDefault;
	}

	/**
	 * Gets the selection length.
	 *
	 * @return the selection length
	 */
	public int getSelectionLength() {
		return selectionLength;
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Turns off clock.
	 */
	public void turnOffClock() {
		clock.stop();
	}

	/**
	 * Sets up this status panel.
	 */
	public void setUp() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));

		lengthLabel = new JLabel(lengthLabelDefault + "0");
		lengthLabel.setHorizontalAlignment(JLabel.LEFT);
		add(lengthLabel);

		editorInfoPanel = new JPanel();
		lnLabel = new JLabel(lnLabelDefault + "0");
		colLabel = new JLabel(colLabelDefault + "0");
		selLabel = new JLabel(selLabelDefault + "0");
		editorInfoPanel.add(lnLabel);
		editorInfoPanel.add(colLabel);
		editorInfoPanel.add(selLabel);
		add(editorInfoPanel);

		clock = new Clock();
		add(clock);
	}

	/**
	 * The Class Clock.
	 */
	static class Clock extends JComponent {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/** The current time. */
		volatile String currentTime;

		/** The stop requested flag. */
		volatile boolean stopRequested;

		/** The formatter. */
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		/**
		 * Instantiates a new clock.
		 */
		public Clock() {
			updateTime();

			Thread t = new Thread(() -> {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (Exception ex) {
					}
					if (stopRequested)
						break;
					SwingUtilities.invokeLater(() -> {
						updateTime();
					});
				}
			});
			t.setDaemon(true);
			t.start();
		}

		/**
		 * Stops the clock.
		 */
		public void stop() {
			stopRequested = true;
		}

		/**
		 * Updates time.
		 */
		private void updateTime() {
			currentTime = formatter.format(LocalDateTime.now());
			repaint();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		protected void paintComponent(Graphics g) {
			Insets ins = getInsets();
			Dimension dim = getSize();
			Rectangle r = new Rectangle(ins.left, ins.top, dim.width - ins.left - ins.right,
					dim.height - ins.top - ins.bottom);
			if (isOpaque()) {
				g.setColor(getBackground());
				g.fillRect(r.x, r.y, r.width, r.height);
			}
			g.setColor(getForeground());

			FontMetrics fm = g.getFontMetrics();
			int w = fm.stringWidth(currentTime);
			int h = fm.getAscent();

			g.drawString(currentTime, r.x + (r.width - w) / 2, r.y + r.height - (r.height - h) / 2);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.java.hw11.jnotepadpp.SingleDocumentListener#
	 * documentModifyStatusUpdated(hr.fer.zemris.java.hw11.jnotepadpp.
	 * SingleDocumentModel)
	 */
	@Override
	public void documentModifyStatusUpdated(SingleDocumentModel model) {
		DefaultSingleDocumentModel defaultModel = (DefaultSingleDocumentModel) model;

		updateCurrentLength(defaultModel);

		updateEditorInfo(defaultModel);

		repaint();
	}

	/**
	 * Updates editor info.
	 *
	 * @param defaultModel
	 *            the default model
	 */
	private void updateEditorInfo(DefaultSingleDocumentModel defaultModel) {
		if (defaultModel.getText() != null) {
			int lineNumber = defaultModel.getText().split("\n").length;
			lnLabel.setText(flp.getString("lnLabel") + String.valueOf(lineNumber));

			String caretLine;
			if (defaultModel.getText().length() > defaultModel.getDot()) {
				caretLine = defaultModel.getText().substring(0, defaultModel.getDot());
			} else {
				caretLine = defaultModel.getText().substring(0, defaultModel.getText().length());
			}
			int caretLineNewLine = caretLine.lastIndexOf("\n");
			colLabel.setText(flp.getString("colLabel") + String.valueOf(defaultModel.getDot() - caretLineNewLine));

			selectionLength = Math.abs(defaultModel.getDot() - defaultModel.getMark());
			selLabel.setText(flp.getString("selLabel") + String.valueOf(selectionLength));

		}
	}

	/**
	 * Updates current length.
	 *
	 * @param defaultModel
	 *            the default model
	 */
	private void updateCurrentLength(DefaultSingleDocumentModel defaultModel) {
		int currentLength = defaultModel.getCurrentLength();
		lengthLabel.setText(flp.getString("lengthLabel") + String.valueOf(currentLength));
	}

	/**
	 * Not implemented here.
	 */
	@Override
	public void documentFilePathUpdated(SingleDocumentModel model) {
	}
}
