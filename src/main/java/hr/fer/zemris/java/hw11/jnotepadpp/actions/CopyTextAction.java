package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;

/**
 * The class that is assigned with the task of copying user's text selection.
 * 
 * @author Damjan Vučina
 */
public class CopyTextAction extends AbstractAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private MultipleDocumentModel model;

	/** The window. */
	private JNotepadPP window;

	/**
	 * The reference to the object responsible for acquiring the text for the
	 * current locale.
	 */
	private FormLocalizationProvider flp;

	/**
	 * Instantiates a new copy text action.
	 *
	 * @param flp
	 *            The reference to the object responsible for acquiring the text for
	 *            the current locale.
	 * @param window
	 *            the window
	 * @param model
	 *            the model
	 */
	public CopyTextAction(FormLocalizationProvider flp, JNotepadPP window, MultipleDocumentModel model) {
		this.window = window;
		this.model = model;
		this.flp = flp;
	}

	/**
	 * Performs copying of user's text selection.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (model.getNumberOfDocuments() == 0) {
			JOptionPane.showMessageDialog(window, flp.getString("docNotFound"), flp.getString("copyError"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int indexOfSelectedTab = ((DefaultMultipleDocumentModel) model).getSelectedIndex();
		JTextArea editor = model.getDocument(indexOfSelectedTab).getTextComponent();
		Document document = editor.getDocument();

		int selectionLength = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
		int offset = 0;
		if (selectionLength == 0) {
			JOptionPane.showMessageDialog(window, flp.getString("textNotFound"), flp.getString("copyError"),
					JOptionPane.ERROR_MESSAGE);
			return;

		} else {
			offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		}

		String text = null;
		try {
			text = document.getText(offset, selectionLength);
			((DefaultMultipleDocumentModel) model).setClipboard(text);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

}
