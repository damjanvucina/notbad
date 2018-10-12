package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;

/**
 * The class that is used for the purpose of enabling and disabling certain
 * actions depending on the number of documents opened, current text selection
 * etc.
 * 
 * @author Damjan Vučina
 */
public class AvailableActionValidator extends AbstractAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	MultipleDocumentModel model;

	/** The window. */
	JNotepadPP window;

	/**
	 * Instantiates a new available action validator.
	 *
	 * @param window
	 *            the window
	 * @param model
	 *            the model
	 */
	public AvailableActionValidator(JNotepadPP window, MultipleDocumentModel model) {
		this.window = window;
		this.model = model;
	}

	/**
	 * Signifies that this action has been triggered.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		validateActions();

	}

	/**
	 * Validates actions.
	 */
	private void validateActions() {
		if (model.getNumberOfDocuments() > 0) {

			window.getSaveAsDocumentAction().setEnabled(true);
			window.getCloseTabAction().setEnabled(true);
			window.getShowStatsAction().setEnabled(true);

		} else {
			window.getSaveDocumentAction().setEnabled(false);
			window.getSaveAsDocumentAction().setEnabled(false);
			window.getCloseTabAction().setEnabled(false);
			window.getShowStatsAction().setEnabled(false);
		}

		if (model.getCurrentDocument() != null && model.getCurrentDocument().getFilePath() != null) {
			window.getSaveDocumentAction().setEnabled(true);
		} else {
			window.getSaveDocumentAction().setEnabled(false);
		}
		
	}
}
