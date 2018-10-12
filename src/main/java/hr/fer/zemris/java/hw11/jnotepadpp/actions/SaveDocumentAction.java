package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;

/**
 * The class responsible for saving an existing file to a previously defined
 * location.
 * 
 * @author Damjan Vučina
 */
public class SaveDocumentAction extends AbstractAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	MultipleDocumentModel model;

	/** The window. */
	JNotepadPP window;

	/**
	 * Instantiates a new save document action.
	 *
	 * @param window
	 *            the window
	 * @param model
	 *            the model
	 */
	public SaveDocumentAction(JNotepadPP window, MultipleDocumentModel model) {
		this.window = window;
		this.model = model;
	}

	/**
	 * Performs saving of the file to the existing location.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int indexOfSelectedTab = ((DefaultMultipleDocumentModel) model).getSelectedIndex();

		model.saveDocument(model.getDocument(indexOfSelectedTab), null);

		window.getAvailableActionValidator().actionPerformed(e);

		DefaultMultipleDocumentModel defaultModel = ((DefaultMultipleDocumentModel) model);
		defaultModel.setIconAt(indexOfSelectedTab, window.acquireIcon("saved.png"));
	}
}
