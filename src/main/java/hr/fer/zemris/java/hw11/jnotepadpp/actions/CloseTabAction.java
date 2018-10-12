package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;

/**
 * The class that is used for the purpose of closing tabs of this notepad.
 */
public class CloseTabAction extends AbstractAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private MultipleDocumentModel model;

	/** The window. */
	private JNotepadPP window;

	/**
	 * The reference to the object responsible for acquiring the text for the
	 * current locale.
	 */
	private FormLocalizationProvider flp;

	/**
	 * Instantiates a new close tab action.
	 *
	 * @param flp
	 *            The reference to the object responsible for acquiring the text for
	 *            the current locale.
	 * @param window
	 *            the window
	 * @param model
	 *            the model
	 */
	public CloseTabAction(FormLocalizationProvider flp, JNotepadPP window, MultipleDocumentModel model) {
		this.window = window;
		this.model = model;
		this.flp = flp;
	}

	/**
	 * Closes the currently opened tab.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int indexOfSelectedTab = ((DefaultMultipleDocumentModel) model).getSelectedIndex();

		int saveResult;
		if (model.getDocument(indexOfSelectedTab).isModified()) {
			saveResult = JOptionPane.showConfirmDialog(window, flp.getString("doYouWantToSaveFile"),
					flp.getString("doYouWantToSaveFileTitle"), JOptionPane.YES_NO_OPTION);

			if (saveResult == JOptionPane.YES_OPTION) {
				window.getSaveAsDocumentAction().actionPerformed(null);
			}
		}

		model.getDocument(indexOfSelectedTab);
		model.closeDocument(model.getDocument(indexOfSelectedTab));

		window.getAvailableActionValidator().actionPerformed(null);

	}
}
