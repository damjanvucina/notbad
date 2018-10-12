package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * The listener interface for receiving singleDocument events. This class
 * defines the communication with a single document.
 *
 * @author Damjan Vučina
 */
public interface SingleDocumentListener {

	/**
	 * Invoked when document modify status update occurs.
	 *
	 * @param model
	 *            the model
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);

	/**
	 * Invoked when document file path update occurs.
	 *
	 * @param model
	 *            the model
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
