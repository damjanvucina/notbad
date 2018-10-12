package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * The interface that defines the communication for registration and
 * deregistration of listener classes as well as a method for acquiring the
 * translation for the given key depending on the the current locale.
 * 
 * @author Damjan Vučina
 */
public interface ILocalizationProvider {

	/**
	 * Gets the translation for the given key depending on the the current locale..
	 *
	 * @param key
	 *            the key
	 * @return the translation for the given key
	 */
	String getString(String key);

	/**
	 * Adds the localization listener.
	 *
	 * @param l
	 *            the localization listener.
	 */
	void addLocalizationListener(ILocalizationListener l);

	/**
	 * Removes the localization listener.
	 *
	 * @param l
	 *            the localization listener.
	 */
	void removeLocalizationListener(ILocalizationListener l);

	/**
	 * Gets the current language.
	 *
	 * @return the current language
	 */
	String getCurrentLanguage();
}
