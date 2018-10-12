package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.DefaultSingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;

/**
 * The class responsible for the sorting of lines selected by the user.
 * 
 * @author Damjan Vučina
 */
public class LineSortAction extends AbstractAction implements SingleDocumentListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ascending sorting. */
	private final String ASC = "ascending";

	/** The descending sorting. */
	private final String DESC = "descending";

	/** The model. */
	private MultipleDocumentModel model;

	/** The direction. */
	private String direction;

	/**
	 * The reference to the object responsible for acquiring the text for the
	 * current locale.
	 */
	private FormLocalizationProvider flp;

	/**
	 * Instantiates a new line sort action.
	 *
	 * @param flp
	 *            The reference to the object responsible for acquiring the text for
	 *            the current locale.
	 * @param model
	 *            the model
	 * @param direction
	 *            the direction
	 */
	public LineSortAction(FormLocalizationProvider flp, MultipleDocumentModel model, String direction) {
		this.model = model;
		this.direction = direction;
		this.flp = flp;
	}

	/**
	 * Performs the sorting of user's text selection.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int indexOfSelectedTab = ((DefaultMultipleDocumentModel) model).getSelectedIndex();
		JTextArea editor = model.getDocument(indexOfSelectedTab).getTextComponent();
		Document document = editor.getDocument();

		int selectionStartOffset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		int selectionLength = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
		int selectionEndOffset = selectionStartOffset + selectionLength;

		int lineStartOffset = 0;
		int lineEndOffset = 0;
		try {
			lineStartOffset = editor.getLineStartOffset(editor.getLineOfOffset(selectionStartOffset));
			lineEndOffset = editor.getLineEndOffset(editor.getLineOfOffset(selectionEndOffset));
		} catch (BadLocationException e2) {
			e2.printStackTrace();
		}

		String view = null;
		try {
			view = document.getText(lineStartOffset, lineEndOffset - lineStartOffset);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		List<String> viewList = new LinkedList<>(Arrays.asList(view.split("\n")));
		Locale locale = new Locale(flp.getCurrentLanguage());
		Collator collator = Collator.getInstance(locale);

		if (ASC.equals(direction)) {
			Collections.sort(viewList, collator);

		} else if (DESC.equals(direction)) {
			Collections.sort(viewList, collator.reversed());

		} else {
			Set<String> set = new LinkedHashSet<>(viewList);
			viewList.clear();
			viewList.addAll(set);
		}

		StringBuilder sb = new StringBuilder(viewList.size());
		for (String s : viewList) {
			sb.append(s).append("\n");
		}

		try {
			document.remove(lineStartOffset, lineEndOffset - lineStartOffset);
			document.insertString(lineStartOffset, sb.toString(), null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		setEnabled(false);
	}

	/**
	 * Enables or disables this action depending on the existence of a text
	 * selection.
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
