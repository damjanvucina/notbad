package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * The class responsible for changing the language of this notepad.
 * 
 * @author Damjan Vučina
 */
public class SetLanguageAction extends AbstractAction {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The language. */
	private String language;

	/**
	 * Instantiates a new sets the language action.
	 *
	 * @param language
	 *            the language
	 */
	public SetLanguageAction(String language) {
		this.language = language;
	}

	/**
	 * Changes language of this notepad.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		LocalizationProvider.getInstance().setLanguage(language);
	}

}
