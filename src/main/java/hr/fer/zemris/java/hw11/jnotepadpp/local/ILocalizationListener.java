package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * The interface defining objects that require to be notify whenever the current
 * language is changed.
 * 
 * @author Damjan Vučina
 *
 */
public interface ILocalizationListener {

	/**
	 * Invoked when a change in current language occurs.
	 */
	void localizationChanged();
}
