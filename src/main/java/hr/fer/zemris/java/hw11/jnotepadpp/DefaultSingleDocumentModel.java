package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * The class that represents an implementation of SingleDocumentModel interface
 * providing user i.e. Notepad program with the ability of managing and editing
 * a document.
 * 
 * @author Damjan Vučina
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {

	/** The file path. */
	private Path filePath;

	/** The text. */
	private String text;

	/** The text component. */
	private JTextArea textComponent;

	/** The currently registered listeners. */
	private List<SingleDocumentListener> listeners;

	/** The modified flag. */
	private boolean modified;

	/** The current length of the document. */
	private int currentLength;

	/** The position of the dot. */
	private int dot;

	/** The position of the mark. */
	private int mark;

	/** The selection length. */
	private int selectionLength;

	/** The offset. */
	private int offset;

	/**
	 * Instantiates a new default single document model.
	 *
	 * @param path
	 *            the path
	 * @param text
	 *            the text
	 */
	public DefaultSingleDocumentModel(Path path, String text) {
		this.filePath = path;
		this.text = text;
		this.currentLength = (text == null) ? 0 : text.length();

		listeners = new ArrayList<>();

		textComponent = new JTextArea(text);
		addDocumentAndCaretListeners(textComponent);

	}

	/**
	 * Adds the document and caret listeners.
	 *
	 * @param textComponent
	 *            the text component
	 */
	private void addDocumentAndCaretListeners(JTextArea textComponent) {

		textComponent.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateEditorLength(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateEditorLength(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateEditorLength(e);
			}

			private void updateEditorLength(DocumentEvent e) {
				currentLength = e.getDocument().getLength();
				try {
					text = e.getDocument().getText(0, currentLength);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				notifyListeners(listener -> listener.documentModifyStatusUpdated(DefaultSingleDocumentModel.this));
			}
		});

		textComponent.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				dot = e.getDot();
				mark = e.getMark();
				selectionLength = Math.abs(e.getDot() - e.getMark());
				offset = Math.min(e.getDot(), e.getMark());

				notifyListeners(listener -> listener.documentModifyStatusUpdated(DefaultSingleDocumentModel.this));
			}
		});

	}

	/**
	 * Gets the offset.
	 *
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Sets the offset.
	 *
	 * @param offset
	 *            the new offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Gets the dot.
	 *
	 * @return the dot
	 */
	public int getDot() {
		return dot;
	}

	/**
	 * Gets the mark.
	 *
	 * @return the mark
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * Gets the selection length.
	 *
	 * @return the selection length
	 */
	public int getSelectionLength() {
		return selectionLength;
	}

	/**
	 * Sets the current length.
	 *
	 * @param currentLength
	 *            the new current length
	 */
	public void setCurrentLength(int currentLength) {
		this.currentLength = currentLength;
	}

	/**
	 * Sets the dot.
	 *
	 * @param dot
	 *            the new dot
	 */
	public void setDot(int dot) {
		this.dot = dot;
	}

	/**
	 * Sets the selection length.
	 *
	 * @param selectionLength
	 *            the new selection length
	 */
	public void setSelectionLength(int selectionLength) {
		this.selectionLength = selectionLength;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gets the file path.
	 */
	@Override
	public Path getFilePath() {
		return filePath;
	}

	/**
	 * Sets the file path.
	 */
	@Override
	public void setFilePath(Path filePath) {
		Objects.requireNonNull(filePath, "Path cannot be set to null");
		this.filePath = filePath;

		notifyListeners(listener -> listener.documentFilePathUpdated(this));
	}

	/**
	 * Gets th text component of the document
	 */
	@Override
	public JTextArea getTextComponent() {
		return textComponent;
	}

	/**
	 * Checks if doucument has been modified.
	 */
	@Override
	public boolean isModified() {
		return modified == true;
	}

	/**
	 * Gets the current length.
	 *
	 * @return the current length
	 */
	public int getCurrentLength() {
		return currentLength;
	}

	/**
	 * Sets the modification flag.
	 */
	@Override
	public void setModified(boolean modified) {
		this.modified = modified;

		notifyListeners(listener -> listener.documentModifyStatusUpdated(this));
	}

	/**
	 * Notifies listeners.
	 *
	 * @param action
	 *            the action
	 */
	private void notifyListeners(Consumer<SingleDocumentListener> action) {
		for (SingleDocumentListener listener : listeners) {
			action.accept(listener);
		}
	}

	/**
	 * Registers a new document listener.
	 */
	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		Objects.requireNonNull(l, "Cannot add null listener to the collection.");

		listeners.add(l);
	}

	/**
	 * Deregisters a previously registered document listener.
	 */
	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		Objects.requireNonNull(l, "Cannot remove null listener to the collection.");

		listeners.remove(l);
	}

}
