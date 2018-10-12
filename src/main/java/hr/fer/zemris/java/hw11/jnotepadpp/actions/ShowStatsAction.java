package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;

/**
 * The class responsible for analyzing the current document and providing user
 * with some information about it such as number of lines, characters, blanks
 * etc.
 * 
 * @author Damjan Vučina
 */
public class ShowStatsAction extends AbstractAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	MultipleDocumentModel model;

	/** The window. */
	JNotepadPP window;

	/**
	 * The reference to the object responsible for acquiring the text for the
	 * current locale.
	 */
	FormLocalizationProvider flp;

	/**
	 * Instantiates a new show stats action.
	 *
	 * @param window
	 *            the window
	 * @param model
	 *            the model
	 * @param flp
	 *            The reference to the object responsible for acquiring the text for
	 *            the current locale.
	 */
	public ShowStatsAction(JNotepadPP window, MultipleDocumentModel model, FormLocalizationProvider flp) {
		this.window = window;
		this.model = model;
		this.flp = flp;
	}

	/**
	 * Inspects the current document and provides user with some information about
	 * it such as number of lines, characters, blanks etc.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int indexOfSelectedTab = ((DefaultMultipleDocumentModel) model).getSelectedIndex();
		//JTextArea editor = model.getCurrentDocument().getTextComponent();
		JTextArea editor = model.getDocument(indexOfSelectedTab).getTextComponent();

		int numOfAllChars = editor.getText().getBytes().length;
		int numOfNonBlankChars = editor.getText().replace(" ", "").getBytes().length;
		int numOfLines = editor.getText().split("\n").length;

		JOptionPane.showMessageDialog(window,
				flp.getString("numOfAllChars") + numOfAllChars + "\n" + flp.getString("numOfNonBlankChars")
						+ numOfNonBlankChars + "\n" + flp.getString("numOfLines") + numOfLines,
				flp.getString("documentStats"), JOptionPane.INFORMATION_MESSAGE);
	}

}
