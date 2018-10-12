package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * The listener interface for receiving multipleDocument events.
 *
 * @author Damjan Vučina
 */
public interface MultipleDocumentListener {

	/**
	 * Signifies that current document has been changed.
	 *
	 * @param previousModel
	 *            the previous model
	 * @param currentModel
	 *            the current model
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

	/**
	 * Signifies that document has been added.
	 *
	 * @param model
	 *            the model
	 */
	void documentAdded(SingleDocumentModel model);

	/**
	 * Signifies that document has been removed.
	 *
	 * @param model
	 *            the model
	 */
	void documentRemoved(SingleDocumentModel model);
}
