package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;

/**
 * The class responsible for closing all tabs and eventually the program itself.
 * 
 * @author Damjan Vučina
 */
public class ExitApplicationAction extends AbstractAction {

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
	 * Instantiates a new exit application action.
	 *
	 * @param flp
	 *            The reference to the object responsible for acquiring the text for
	 *            the current locale.
	 * @param window
	 *            the window
	 * @param model
	 *            the model
	 */
	public ExitApplicationAction(FormLocalizationProvider flp, JNotepadPP window, MultipleDocumentModel model) {
		this.window = window;
		this.model = model;
		this.flp = flp;
	}

	/**
	 * Closes all open tabs and then closes notepad program.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int saveResult;
		DefaultMultipleDocumentModel defaultModel = (DefaultMultipleDocumentModel) model;
		for (int i = 0, length = model.getNumberOfDocuments(); i < length; i++) {

			if (model.getDocument(i).isModified()) {
				defaultModel.setSelectedIndex(i);
				saveResult = JOptionPane.showConfirmDialog(window, flp.getString("doYouWantToSaveFile"),
						flp.getString("doYouWantToSaveFileTitle"), JOptionPane.YES_NO_CANCEL_OPTION);

				if (saveResult == JOptionPane.CANCEL_OPTION) {
					return;

				} else if (saveResult == JOptionPane.YES_OPTION) {
					window.getSaveAsDocumentAction().actionPerformed(null);
				}

			}
		}
		window.getStatusPanel().turnOffClock();
		window.dispose();
	}

}
