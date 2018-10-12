package hr.fer.zemris.java.hw11.jnotepadpp;

import javax.swing.ImageIcon;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Class that is responsible for editing documents modifed icon (red/green
 * floppy disk) signifying whether the document has been edited after last
 * saving.
 *
 * @author Damjan Vučina
 */
public class IconListener implements DocumentListener {

	/** The default model. */
	private DefaultMultipleDocumentModel defaultModel;

	/** The tab index. */
	private int tabIndex;

	/** The not saved icon. */
	private ImageIcon notSavedIcon;

	/** The document. */
	private DefaultSingleDocumentModel document;

	/**
	 * Instantiates a new icon listener.
	 *
	 * @param defaultModel
	 *            the default model
	 * @param tabIndex
	 *            the tab index
	 * @param notSavedIcon
	 *            the not saved icon
	 * @param newDocument
	 *            the new document
	 */
	public IconListener(DefaultMultipleDocumentModel defaultModel, int tabIndex, ImageIcon notSavedIcon,
			SingleDocumentModel newDocument) {
		this.defaultModel = defaultModel;
		this.tabIndex = tabIndex;
		this.notSavedIcon = notSavedIcon;
		this.document = (DefaultSingleDocumentModel) newDocument;
	}

/**
 * Sets not saved icon after a change has occured.
 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		setNotSavedIcon();
	}

	/**
	 * Sets not saved icon after an insert has occured.
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		setNotSavedIcon();
	}

	/**
	 * Sets not saved icon after a removal has occured.
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		setNotSavedIcon();
	}

	/**
	 * Sets the not saved icon.
	 */
	private void setNotSavedIcon() {
		defaultModel.setIconAt(tabIndex, notSavedIcon);
		document.setModified(true);
	}

}
