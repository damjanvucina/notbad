package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * The class that represents an implementation of MultipleDocumentModel
 * interface providing user i.e. Notepad program with the ability of managing
 * multiple documents at the same time using tabs.
 * 
 * @author Damjan Vučina
 */
//@formatter:off
public class DefaultMultipleDocumentModel extends JTabbedPane 
										  implements MultipleDocumentModel,
										  			 Iterable<SingleDocumentModel> {
//@formatter:on

	/** The Constant UNTITLED. */
	public static final String UNTITLED = "Untitled";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The documents currently opened. */
	private List<SingleDocumentModel> documents;

	/** The listeners currently registered. */
	private List<MultipleDocumentListener> listeners;

	/** The current document. */
	private SingleDocumentModel currentDocument;

	/** The clipboard. */
	private String clipboard;

	/**
	 * Instantiates a new default multiple document model.
	 */
	public DefaultMultipleDocumentModel() {
		documents = new ArrayList<>();
		listeners = new ArrayList<>();
	}

	/**
	 * Gets the clipboard.
	 *
	 * @return the clipboard
	 */
	public String getClipboard() {
		return clipboard;
	}

	/**
	 * Gets the documents.
	 *
	 * @return the documents
	 */
	public List<SingleDocumentModel> getDocuments() {
		return documents;
	}

	/**
	 * Sets the clipboard.
	 *
	 * @param clipboard
	 *            the new clipboard
	 */
	public void setClipboard(String clipboard) {
		this.clipboard = clipboard;
	}

	/**
	 * Creates a blank new document.
	 */
	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel newDocument = new DefaultSingleDocumentModel(null, null);

		documents.add(newDocument);
		notifyListeners(listener -> listener.documentAdded(newDocument));

		currentDocument = newDocument;
		notifyListeners(listener -> listener.currentDocumentChanged(documents.get(documents.size() - 2), newDocument));

		createNewTab(newDocument);

		newDocument.getTextComponent().getDocument().addDocumentListener(
				new IconListener(this, documents.indexOf(newDocument), acquireIcon("notsaved.png"), newDocument));

		return newDocument;
	}

	/**
	 * Gets the current document.
	 */
	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	/**
	 * Loads the document defined by the path argument from drive.
	 */
	@Override
	public SingleDocumentModel loadDocument(Path path) {
		Objects.requireNonNull(path, "Given path cannot be null");

		SingleDocumentModel oldDocument = currentDocument;
		SingleDocumentModel loadedDocument = acquireDocument(path);

		if (loadedDocument == null) {
			if (!Files.isReadable(path)) {
				JOptionPane.showMessageDialog(this, "Cannot read document" + path, "Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}

			byte[] bytes;
			try {
				bytes = Files.readAllBytes(path);
			} catch (Exception r) {
				JOptionPane.showMessageDialog(this, "Error reading file.", "Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}

			loadedDocument = new DefaultSingleDocumentModel(path, new String(bytes, StandardCharsets.UTF_8));
			currentDocument = loadedDocument;

			documents.add(loadedDocument);
			notifyListeners(listener -> listener.documentAdded(documents.get(documents.size() - 1)));

			loadedDocument.getTextComponent().getDocument().addDocumentListener(new IconListener(this,
					documents.indexOf(loadedDocument), acquireIcon("notsaved.png"), loadedDocument));

			createNewTab(loadedDocument);
		} else {
			setSelectedIndex(documents.indexOf(loadedDocument));
		}

		// currentDocument = loadedDocument;
		notifyListeners(listener -> listener.currentDocumentChanged(oldDocument, currentDocument));
		return loadedDocument;
	}

	/**
	 * Creates a new tab for specified document.
	 *
	 * @param document
	 *            the document
	 */
	private void createNewTab(SingleDocumentModel document) {
		JTextArea tab = document.getTextComponent();

		String title = (document.getFilePath() == null) ? UNTITLED
				: String.valueOf(document.getFilePath().getFileName());

		JScrollPane scrollPane = new JScrollPane(tab);
		addTab(title, scrollPane);
		if (document.getFilePath() != null) {
			setToolTipTextAt(documents.indexOf(document), String.valueOf(document.getFilePath()));
		}

		setSelectedComponent(scrollPane);

	}

	/**
	 * Acquires document.
	 *
	 * @param path
	 *            the path of the document
	 * @return the single document model
	 */
	private SingleDocumentModel acquireDocument(Path path) {
		for (SingleDocumentModel document : documents) {
			Path currentPath = document.getFilePath();
			if (currentPath != null && currentPath.equals(path)) {
				return document;
			}
		}

		return null;
	}

	/**
	 * Notifies listeners.
	 *
	 * @param action
	 *            the action
	 */
	private void notifyListeners(Consumer<MultipleDocumentListener> action) {
		for (MultipleDocumentListener listener : listeners) {
			action.accept(listener);
		}
	}

	/**
	 * Saves given document to the specified path.
	 */
	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		Objects.requireNonNull(model, "Given model cannot be null");

		if (acquireDocument(newPath) != null) {
			throw new IllegalArgumentException("Specified file is already opened.");
		}

		byte[] bytes = model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8);
		Path savePath = (newPath == null) ? model.getFilePath() : newPath;

		try {
			Files.write(savePath, bytes);
		} catch (Exception e) {
			//System.out.println("Error saving file.");
		}

		model.setModified(false);

		if (newPath != null) {
			model.setFilePath(newPath);
			setToolTipTextAt(documents.indexOf(model), String.valueOf(model.getFilePath()));
		}
	}

	/**
	 * Closes specified document.
	 */
	@Override
	public void closeDocument(SingleDocumentModel model) {
		Objects.requireNonNull(model, "Cannot close null document");

		int removedIndex = documents.indexOf(model);
		documents.remove(removedIndex);
		notifyListeners(listener -> listener.documentRemoved(model));

		removeTabAt(removedIndex);
	}

	/**
	 * Registers a new listener so it can be notified when a change occurs.
	 */
	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		Objects.requireNonNull(l, "Multiple document listener cannot be null.");

		listeners.add(l);
	}

	/**
	 * Deregisters previously registered listener.
	 */
	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		Objects.requireNonNull(l, "Multiple document listener cannot be null.");

		listeners.remove(l);
	}

	/**
	 * Gets number of currently opened documents.
	 */
	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	/**
	 * Gets the document under specified index.
	 */
	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(index);
	}

	/**
	 * Gets an instance of class responsible for iterating over currently opened
	 * documents.
	 */
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return new DocumentsIterator();
	}

	/**
	 * The class responsible for iterating over currently opened documents.
	 */
	private class DocumentsIterator implements Iterator<SingleDocumentModel> {

		/** The index of the to be processed document. */
		private int index = 0;

		/**
		 * Checks if there are more non-processed documents.
		 */
		@Override
		public boolean hasNext() {
			return index < documents.size();
		}

		/**
		 * Gets the next document.
		 */
		@Override
		public SingleDocumentModel next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No more documents are available.");
			}

			return documents.get(index++);
		}
	}

	/**
	 * Acquires icon defined by its name.
	 *
	 * @param iconName
	 *            the icon name
	 * @return the image icon
	 */
	private ImageIcon acquireIcon(String iconName) {
		StringBuilder sb = new StringBuilder("icons");
		sb.append("/").append(iconName);

		InputStream is = this.getClass().getResourceAsStream(sb.toString());

		if (is == null) {
			throw new IllegalArgumentException("Cannot access icon " + sb.toString());
		}

		byte[] bytes = null;
		try {
			bytes = is.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ImageIcon(bytes);
	}

}
