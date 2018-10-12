package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * The implementation of the bridge connecting the object responsible for
 * translation and the objects that require translations. The goal of this class
 * is to avoid possible code duplication.
 * 
 * @author Damjan Vučina
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Instantiates a new form localization provider.
	 *
	 * @param iLocalizationProvider
	 *            the i localization provider
	 * @param frame
	 *            the frame
	 */
	public FormLocalizationProvider(ILocalizationProvider iLocalizationProvider, JFrame frame) {
		super(iLocalizationProvider);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent arg0) {
				connect();
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				disconnect();
			}
		});
	}

}
