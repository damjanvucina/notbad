package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

/**
 * The interface defining the communication with this notepad's documents.
 * 
 * @author Damjan Vučina
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	
	/**
	 * Creates the new document.
	 *
	 * @return the single document model
	 */
	SingleDocumentModel createNewDocument();

	/**
	 * Gets the current document.
	 *
	 * @return the current document
	 */
	SingleDocumentModel getCurrentDocument();

	/**
	 * Loads document.
	 *
	 * @param path the path
	 * @return the single document model
	 */
	SingleDocumentModel loadDocument(Path path);

	/**
	 * Saves document.
	 *
	 * @param model the model
	 * @param newPath the new path
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);

	/**
	 * Closes document.
	 *
	 * @param model the model
	 */
	void closeDocument(SingleDocumentModel model);

	/**
	 * Adds the multiple document listener.
	 *
	 * @param l the l
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Removes the multiple document listener.
	 *
	 * @param l the l
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Gets the number of documents.
	 *
	 * @return the number of documents
	 */
	int getNumberOfDocuments();

	/**
	 * Gets the document.
	 *
	 * @param index the index
	 * @return the document
	 */
	SingleDocumentModel getDocument(int index);

}
