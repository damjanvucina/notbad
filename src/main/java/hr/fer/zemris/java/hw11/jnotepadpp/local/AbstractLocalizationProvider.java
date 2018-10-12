package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The class that implements the registration and deregistration of objects that
 * require to be notified whenever a change to the current language occurs.
 * 
 * @author Damjan Vučina
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/** The listeners. */
	List<ILocalizationListener> listeners;

	/**
	 * Instantiates a new abstract localization provider.
	 */
	public AbstractLocalizationProvider() {
		listeners = new ArrayList<>();
	}

	/**
	 * Registers a new localization listener.
	 * 
	 * @throws NullPointerException
	 *             if listener to be registered is null
	 */
	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		Objects.requireNonNull(l, "Cannot add null listener");

		listeners.add(l);
	}

	/**
	 * Deregisters a previously registered listener.
	 * 
	 * @throws NullPointerException
	 *             if listener to be registered is null
	 */
	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		Objects.requireNonNull(l, "Cannot remove null listener");
		listeners.remove(l);
	}

	/**
	 * Notifies registered listeners of a change to the current language.
	 */
	public void fire() {
		for (ILocalizationListener l : listeners) {
			l.localizationChanged();
		}
	}

}
