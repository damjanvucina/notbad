package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * A proxy class connecting the object responsible for acquiring the
 * translations and the objects that require translations. The goal of this
 * class is to prevent memory leakage.
 * 
 * @author Damjan Vučina
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/** The i localization provider. */
	private ILocalizationProvider iLocalizationProvider;

	/** The connected flag. */
	private boolean connected;

	/**
	 * Instantiates a new localization provider bridge.
	 *
	 * @param iLocalizationProvider
	 *            the i localization provider
	 */
	public LocalizationProviderBridge(ILocalizationProvider iLocalizationProvider) {
		this.iLocalizationProvider = iLocalizationProvider;
	}

	/**
	 * Connects the listener.
	 */
	public void connect() {
		if (!connected) {
			iLocalizationProvider.addLocalizationListener(() -> fire());
			connected = true;
		}
	}

	/**
	 * Disconnects the listener.
	 */
	public void disconnect() {
		if (connected) {
			iLocalizationProvider.removeLocalizationListener(() -> fire());
			connected = false;
		}
	}

	/**
	 * Gets the translation for the given key.
	 */
	public String getString(String key) {
		return iLocalizationProvider.getString(key);
	}

	/**
	 * Gets current language.
	 */
	@Override
	public String getCurrentLanguage() {
		return iLocalizationProvider.getCurrentLanguage();
	}
}
