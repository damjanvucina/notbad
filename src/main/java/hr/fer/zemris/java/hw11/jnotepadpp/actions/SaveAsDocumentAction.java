package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;
import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;

/**
 * The class responsible for saving the current document to a new location.
 * 
 * @author Damjan Vučina
 */
public class SaveAsDocumentAction extends AbstractAction {

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
	 * Instantiates a new save as document action.
	 *
	 * @param flp
	 *            The reference to the object responsible for acquiring the text for
	 *            the current locale.
	 * @param window
	 *            the window
	 * @param model
	 *            the model
	 */
	public SaveAsDocumentAction(FormLocalizationProvider flp, JNotepadPP window, MultipleDocumentModel model) {
		this.window = window;
		this.model = model;
		this.flp = flp;
	}

	/**
	 * Performs saving of the current file to a new location.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Save document");
		int dialogResult = jfc.showSaveDialog(window);

		if (dialogResult == JFileChooser.CANCEL_OPTION) {
			JOptionPane.showMessageDialog(window, flp.getString("savingCanceled"), "Info",
					JOptionPane.INFORMATION_MESSAGE);
			return;

		} else if (dialogResult == JFileChooser.APPROVE_OPTION) {

			int overwriteResult = 0;
			if (Files.exists(jfc.getSelectedFile().toPath())) {
				overwriteResult = JOptionPane.showConfirmDialog(jfc, flp.getString("fileExistsOverwrite"),
						"File exists", JOptionPane.YES_NO_OPTION);
				if (overwriteResult == JOptionPane.NO_OPTION) {
					return;
				}
			}
		}

		Path savePath = jfc.getSelectedFile().toPath();

		int indexOfSelectedTab = ((DefaultMultipleDocumentModel) model).getSelectedIndex();
		model.saveDocument(model.getDocument(indexOfSelectedTab), savePath);

		JTextArea tab = (JTextArea) ((JScrollPane) ((DefaultMultipleDocumentModel) model).getSelectedComponent())
				.getViewport().getView();
		tab.setToolTipText(String.valueOf(model.getCurrentDocument().getFilePath()));

		String title = String.valueOf(savePath.getFileName());
		((DefaultMultipleDocumentModel) model).setTitleAt(indexOfSelectedTab, title);

		window.getAvailableActionValidator().actionPerformed(e);
		window.setTitle(String.valueOf(savePath));

		DefaultMultipleDocumentModel defaultModel = ((DefaultMultipleDocumentModel) model);
		defaultModel.setIconAt(indexOfSelectedTab, window.acquireIcon("saved.png"));

	}

}
