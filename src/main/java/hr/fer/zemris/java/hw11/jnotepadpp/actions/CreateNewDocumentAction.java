package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;

/**
 * The class responsible for creating a new document on user's request.
 * 
 * @author Damjan Vučina
 */
public class CreateNewDocumentAction extends AbstractAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	MultipleDocumentModel model;

	/** The window. */
	JNotepadPP window;

	/**
	 * Instantiates a new creates the new document action.
	 *
	 * @param window
	 *            the window
	 * @param model
	 *            the model
	 */
	public CreateNewDocumentAction(JNotepadPP window, MultipleDocumentModel model) {
		this.window = window;
		this.model = model;
	}

	/**
	 * Creates a new document.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		model.createNewDocument();

		window.getAvailableActionValidator().actionPerformed(e);

		DefaultMultipleDocumentModel defaultModel = ((DefaultMultipleDocumentModel) model);
		defaultModel.setIconAt(defaultModel.getDocuments().indexOf(defaultModel.getCurrentDocument()),
				window.acquireIcon("notsaved.png"));
	}

}
