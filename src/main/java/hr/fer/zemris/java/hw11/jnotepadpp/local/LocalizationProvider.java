package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The class that implements an object responsible for acquiring the
 * translations for the given keys depending on the current locale. There can
 * only exist a single instance of this class.
 * 
 * @author Damjan Vučina
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	/** The Constant BASE_NAME. */
	private static final String BASE_NAME = "hr.fer.zemris.java.hw11.jnotepadpp.local.translations";

	/** The current language. */
	private String language;

	/** The bundle. */
	private ResourceBundle bundle;

	/** The instance. */
	private static LocalizationProvider instance = new LocalizationProvider();

	/**
	 * Instantiates a new localization provider.
	 */
	private LocalizationProvider() {
		setLanguage("en");
	}

	/**
	 * Gets the single instance of LocalizationProvider.
	 *
	 * @return single instance of LocalizationProvider
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}

	/**
	 * Sets the language.
	 *
	 * @param language
	 *            the new language
	 */
	public void setLanguage(String language) {
		this.language = language;

		setUpBundle();
	}

	/**
	 * Sets the up bundle.
	 */
	private void setUpBundle() {
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle(BASE_NAME, locale);

		fire();
	}

	/**
	 * Acquires the translation for the given key depending on the current locale.
	 */
	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	/**
	 * Gets the current language.
	 */
	@Override
	public String getCurrentLanguage() {
		return language;
	}

}
