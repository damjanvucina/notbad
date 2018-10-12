package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.function.Function;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.DefaultSingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.SingleDocumentModel;

/**
 * The action that is used for the purpose of transforming the text selection
 * into uppercase, lowercase or inverting the caps.
 * 
 * @author Damjan Vučina
 */
public class ChangeCaseAction extends AbstractAction implements SingleDocumentListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private MultipleDocumentModel model;

	/** The window. */
	private JNotepadPP window;

	/** The function defining the transformation. */
	private Function<Character, Character> format;

	/**
	 * Instantiates a new change case action.
	 *
	 * @param window
	 *            the window
	 * @param model
	 *            the model
	 * @param format
	 *            the format
	 */
	// toUpperCase, toLowerCase, invertCase
	public ChangeCaseAction(JNotepadPP window, MultipleDocumentModel model, Function<Character, Character> format) {
		this.window = window;
		this.model = model;
		this.format = format;
	}

	/**
	 * Performs the transformation of the caps.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int indexOfSelectedTab = ((DefaultMultipleDocumentModel) model).getSelectedIndex();
		JTextArea editor = model.getDocument(indexOfSelectedTab).getTextComponent();
		Document document = editor.getDocument();

		int selectionLength = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
		int offset = 0;
		if (selectionLength == 0) {
			JOptionPane.showMessageDialog(window, "Text selection not found.", "Change case error",
					JOptionPane.ERROR_MESSAGE);
			return;

		} else {
			offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		}

		char[] text = null;
		try {
			text = document.getText(offset, selectionLength).toCharArray();

		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < selectionLength; i++) {
			text[i] = format.apply(text[i]);
		}

		try {
			document.remove(offset, selectionLength);
			document.insertString(editor.getCaretPosition(), String.valueOf(text), null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Enables or disables itself, depending whether the user made a text selection.
	 */
	@Override
	public void documentModifyStatusUpdated(SingleDocumentModel model) {
		if (((DefaultSingleDocumentModel) model).getSelectionLength() > 0) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}

	/**
	 * Not implemented here.
	 */
	@Override
	public void documentFilePathUpdated(SingleDocumentModel model) {
	}

}
