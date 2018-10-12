package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.java.hw11.jnotepadpp.actions.AvailableActionValidator;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.ChangeCaseAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.CloseTabAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.CopyTextAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.CreateNewDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.CutTextAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.ExitApplicationAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.LineSortAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.OpenDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.PasteTextAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SaveAsDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SaveDocumentAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SetLanguageAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.ShowStatsAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationListener;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

import static java.awt.event.KeyEvent.getExtendedKeyCodeForChar;

/**
 * The class that represents the notepad program frame. JNotepadPP is a text
 * editor and source code editor. It supports tabbed editing, which allows
 * working with multiple open files in a single window.
 * 
 * @author Damjan Vučina
 */
public class JNotepadPP extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant DEFAULT_TITLE. */
	public static final String DEFAULT_TITLE = "JNotepad++";

	/** The Constant UNTITLED. */
	public static final String UNTITLED = "Untitled";

	/** The Constant TITLE_SEPARATOR. */
	public static final String TITLE_SEPARATOR = " - ";

	/** The open document action. */
	private OpenDocumentAction openDocumentAction;

	/** The create new document action. */
	private CreateNewDocumentAction createNewDocumentAction;

	/** The save document action. */
	private SaveDocumentAction saveDocumentAction;

	/** The save as document action. */
	private SaveAsDocumentAction saveAsDocumentAction;

	/** The close tab action. */
	private CloseTabAction closeTabAction;

	/** The exit application action. */
	private ExitApplicationAction exitApplicationAction;

	/** The copy text action. */
	private CopyTextAction copyTextAction;

	/** The paste text action. */
	private PasteTextAction pasteTextAction;

	/** The cut text action. */
	private CutTextAction cutTextAction;

	/** The show stats action. */
	private ShowStatsAction showStatsAction;

	/** The available action validator. */
	private AvailableActionValidator availableActionValidator;

	/** The set croatian action. */
	private SetLanguageAction setCroatian;

	/** The set english action. */
	private SetLanguageAction setEnglish;

	/** The set german action. */
	private SetLanguageAction setGerman;

	/** The to upper case action. */
	private ChangeCaseAction toUpperCaseAction;

	/** The to lower case action. */
	private ChangeCaseAction toLowerCaseAction;

	/** The invert case action. */
	private ChangeCaseAction invertCaseAction;

	/** The sort ascending action. */
	private LineSortAction sortAscendingAction;

	/** The sort descending action. */
	private LineSortAction sortDescendingAction;

	/** The unique action. */
	private LineSortAction uniqueAction;

	/**
	 * The reference to the object responsible for acquiring the text for the
	 * current locale.
	 */
	private FormLocalizationProvider flp;

	/** The model. */
	private DefaultMultipleDocumentModel model;

	/** The panel. */
	private JPanel panel;

	/** The status panel. */
	private JStatusPanel statusPanel;

	/** The file menu. */
	private JMenu fileMenu;

	/** The edit menu. */
	private JMenu editMenu;

	/** The help menu. */
	private JMenu helpMenu;

	/** The language menu. */
	private JMenu languageMenu;

	/** The tools menu. */
	private JMenu toolsMenu;

	/** The change case menu. */
	private JMenu changeCaseMenu;

	/** The sort menu. */
	private JMenu sortMenu;

	/** The editor length. */
	private int editorLength;

	/** The action mappings. */
	private Map<AbstractAction, String> actionMappings;

	/** The menu mappings. */
	private Map<JMenu, String> menuMappings;

	/** The label mappings. */
	private Map<JLabel, String> labelMappings;

	/**
	 * Instantiates a new jnotepad.
	 */
	public JNotepadPP() {
		setSize(600, 600);
		setLocation(50, 50);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

		actionMappings = new HashMap<>();
		menuMappings = new HashMap<>();
		labelMappings = new HashMap<>();
		initGui();

		addLocalizationListener();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitApplicationAction.actionPerformed(null);
			}
		});

		flp.fire();
	}

	/**
	 * Initializess the GUI.
	 */
	private void initGui() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		panel = new JPanel(new BorderLayout());
		statusPanel = new JStatusPanel(flp, new GridLayout(1, 3));

		cp.add(panel, BorderLayout.CENTER);
		cp.add(statusPanel, BorderLayout.SOUTH);

		model = new DefaultMultipleDocumentModel();
		panel.add(model, BorderLayout.CENTER);

		initializeActions();

		createMenus();
		setIcons();
		disableStartActions();
		createToolbars();
		statusPanel.setUp();

		model.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int indexOfSelectedTab = ((DefaultMultipleDocumentModel) e.getSource()).getSelectedIndex();
				model.getCurrentDocument().addSingleDocumentListener(statusPanel);
				model.getCurrentDocument().addSingleDocumentListener(toUpperCaseAction);
				model.getCurrentDocument().addSingleDocumentListener(toLowerCaseAction);
				model.getCurrentDocument().addSingleDocumentListener(invertCaseAction);
				model.getCurrentDocument().addSingleDocumentListener(sortAscendingAction);
				model.getCurrentDocument().addSingleDocumentListener(sortDescendingAction);
				model.getCurrentDocument().addSingleDocumentListener(uniqueAction);
				String title = null;
				if (((DefaultMultipleDocumentModel) model).getNumberOfDocuments() > 0) {
					Path filePath = model.getDocument(indexOfSelectedTab).getFilePath();
					title = (filePath == null) ? UNTITLED : String.valueOf(filePath);

				} else {
					title = UNTITLED;
					editorLength = 0;
				}

				setTitle(title + TITLE_SEPARATOR + DEFAULT_TITLE);

				if (model.getNumberOfDocuments() > 0) {
					statusPanel.documentModifyStatusUpdated(model.getDocument(indexOfSelectedTab));
				}

			}
		});

		initializeActionMappings();
		initializeMenuMappings();
		initializeLabelMappings();

		availableActionValidator.actionPerformed(null);
	}

	/**
	 * Disables start actions.
	 */
	private void disableStartActions() {
		toUpperCaseAction.setEnabled(false);
		toLowerCaseAction.setEnabled(false);
		invertCaseAction.setEnabled(false);
		sortAscendingAction.setEnabled(false);
		sortDescendingAction.setEnabled(false);
		uniqueAction.setEnabled(false);
	}

	/**
	 * Adds the localization listener.
	 */
	private void addLocalizationListener() {
		flp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {

				for (Map.Entry<AbstractAction, String> entry : actionMappings.entrySet()) {
					AbstractAction action = entry.getKey();
					String name = entry.getValue();

					action.putValue(Action.NAME, flp.getString(name));

					action.putValue(Action.ACCELERATOR_KEY,
							KeyStroke.getKeyStroke(flp.getString(name + "Accelerator")));

					char c = flp.getString(name + "Mnemonic").charAt(0);
					action.putValue(Action.MNEMONIC_KEY, getExtendedKeyCodeForChar(c));

					action.putValue(Action.SHORT_DESCRIPTION, flp.getString(name + "Desc"));
				}

				for (Map.Entry<JMenu, String> entry : menuMappings.entrySet()) {
					JMenu menu = entry.getKey();
					String name = entry.getValue();

					menu.setText(flp.getString(name));
				}

				for (Map.Entry<JLabel, String> entry : labelMappings.entrySet()) {
					JLabel label = entry.getKey();
					String name = entry.getValue();

					label.setText(flp.getString(name));
				}
			}
		});
	}

	/**
	 * Initializes action mappings.
	 */
	private void initializeActionMappings() {
		actionMappings.put(createNewDocumentAction, "menuItemNew");
		actionMappings.put(openDocumentAction, "menuItemOpen");
		actionMappings.put(saveDocumentAction, "menuItemSave");
		actionMappings.put(saveAsDocumentAction, "menuItemSaveAs");
		actionMappings.put(closeTabAction, "menuItemClose");
		actionMappings.put(exitApplicationAction, "menuItemExit");
		actionMappings.put(copyTextAction, "menuItemCopy");
		actionMappings.put(pasteTextAction, "menuItemPaste");
		actionMappings.put(cutTextAction, "menuItemCut");
		actionMappings.put(showStatsAction, "menuItemStats");
		actionMappings.put(setCroatian, "menuItemCroatian");
		actionMappings.put(setEnglish, "menuItemEnglish");
		actionMappings.put(setGerman, "menuItemGerman");
		actionMappings.put(toUpperCaseAction, "menuItemUppercase");
		actionMappings.put(toLowerCaseAction, "menuItemLowercase");
		actionMappings.put(invertCaseAction, "menuItemInvertCase");
		actionMappings.put(sortAscendingAction, "menuItemAscending");
		actionMappings.put(sortDescendingAction, "menuItemDescending");
		actionMappings.put(uniqueAction, "menuItemUnique");
	}

	/**
	 * Initializes menu mappings.
	 */
	private void initializeMenuMappings() {
		menuMappings.put(fileMenu, "menuFile");
		menuMappings.put(editMenu, "menuEdit");
		menuMappings.put(helpMenu, "menuHelp");
		menuMappings.put(fileMenu, "menuFile");
		menuMappings.put(languageMenu, "menuLanguage");
		menuMappings.put(toolsMenu, "menuTools");
		menuMappings.put(changeCaseMenu, "menuChangeCase");
		menuMappings.put(sortMenu, "menuSort");
	}

	/**
	 * Initializes label mappings.
	 */
	private void initializeLabelMappings() {
		labelMappings.put(statusPanel.getLengthLabel(), "lengthLabel");
		labelMappings.put(statusPanel.getLnLabel(), "lnLabel");
		labelMappings.put(statusPanel.getColLabel(), "colLabel");
		labelMappings.put(statusPanel.getSelLabel(), "selLabel");
	}

	/**
	 * Sets the icons.
	 */
	private void setIcons() {
		createNewDocumentAction.putValue(Action.SMALL_ICON, acquireIcon("new.png"));
		openDocumentAction.putValue(Action.SMALL_ICON, acquireIcon("open.png"));
		saveDocumentAction.putValue(Action.SMALL_ICON, acquireIcon("save.png"));
		saveAsDocumentAction.putValue(Action.SMALL_ICON, acquireIcon("saveas.png"));
		closeTabAction.putValue(Action.SMALL_ICON, acquireIcon("close.png"));
		exitApplicationAction.putValue(Action.SMALL_ICON, acquireIcon("exit.png"));
		copyTextAction.putValue(Action.SMALL_ICON, acquireIcon("copy.png"));
		pasteTextAction.putValue(Action.SMALL_ICON, acquireIcon("paste.png"));
		cutTextAction.putValue(Action.SMALL_ICON, acquireIcon("cut.png"));
		showStatsAction.putValue(Action.SMALL_ICON, acquireIcon("stats.png"));
		setCroatian.putValue(Action.SMALL_ICON, acquireIcon("croatian.png"));
		setEnglish.putValue(Action.SMALL_ICON, acquireIcon("english.png"));
		setGerman.putValue(Action.SMALL_ICON, acquireIcon("deutsch.png"));
		toUpperCaseAction.putValue(Action.SMALL_ICON, acquireIcon("uppercase.png"));
		toLowerCaseAction.putValue(Action.SMALL_ICON, acquireIcon("lowercase.png"));
		invertCaseAction.putValue(Action.SMALL_ICON, acquireIcon("invertcase.png"));
		sortAscendingAction.putValue(Action.SMALL_ICON, acquireIcon("ascending.png"));
		sortDescendingAction.putValue(Action.SMALL_ICON, acquireIcon("descending.png"));
		uniqueAction.putValue(Action.SMALL_ICON, acquireIcon("unique.png"));
	}

	/**
	 * Acquires icon specified by the given name.
	 *
	 * @param iconName
	 *            the icon name
	 * @return the image icon
	 */
	public ImageIcon acquireIcon(String iconName) {
		StringBuilder sb = new StringBuilder("icons");
		sb.append("/").append(iconName);

		InputStream is = this.getClass().getResourceAsStream(sb.toString());

		if (is == null) {
			throw new IllegalArgumentException("Cannot access icon " + sb.toString());
		}

		byte[] bytes = null;
		try {
			bytes = is.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ImageIcon(bytes);
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public DefaultMultipleDocumentModel getModel() {
		return model;
	}

	/**
	 * Gets the available action validator.
	 *
	 * @return the available action validator
	 */
	public AvailableActionValidator getAvailableActionValidator() {
		return availableActionValidator;
	}

	/**
	 * Gets the open document action.
	 *
	 * @return the open document action
	 */
	public OpenDocumentAction getOpenDocumentAction() {
		return openDocumentAction;
	}

	/**
	 * Gets the creates the new document action.
	 *
	 * @return the creates the new document action
	 */
	public CreateNewDocumentAction getCreateNewDocumentAction() {
		return createNewDocumentAction;
	}

	/**
	 * Gets the save document action.
	 *
	 * @return the save document action
	 */
	public SaveDocumentAction getSaveDocumentAction() {
		return saveDocumentAction;
	}

	/**
	 * Gets the save as document action.
	 *
	 * @return the save as document action
	 */
	public SaveAsDocumentAction getSaveAsDocumentAction() {
		return saveAsDocumentAction;
	}

	/**
	 * Gets the close tab action.
	 *
	 * @return the close tab action
	 */
	public CloseTabAction getCloseTabAction() {
		return closeTabAction;
	}

	/**
	 * Gets the exit application action.
	 *
	 * @return the exit application action
	 */
	public ExitApplicationAction getExitApplicationAction() {
		return exitApplicationAction;
	}

	/**
	 * Gets the paste text action.
	 *
	 * @return the paste text action
	 */
	public PasteTextAction getPasteTextAction() {
		return pasteTextAction;
	}

	/**
	 * Gets the cut text action.
	 *
	 * @return the cut text action
	 */
	public CutTextAction getCutTextAction() {
		return cutTextAction;
	}

	/**
	 * Gets the show stats action.
	 *
	 * @return the show stats action
	 */
	public ShowStatsAction getShowStatsAction() {
		return showStatsAction;
	}

	/**
	 * Gets the copy text action.
	 *
	 * @return the copy text action
	 */
	public CopyTextAction getCopyTextAction() {
		return copyTextAction;
	}

	/**
	 * Gets the editor length.
	 *
	 * @return the editor length
	 */
	public int getEditorLength() {
		return editorLength;
	}

	/**
	 * Sets the editor length.
	 *
	 * @param editorLength
	 *            the new editor length
	 */
	public void setEditorLength(int editorLength) {
		this.editorLength = editorLength;
	}

	/**
	 * Gets the status panel.
	 *
	 * @return the status panel
	 */
	public JStatusPanel getStatusPanel() {
		return statusPanel;
	}

	/**
	 * Gets the to upper case.
	 *
	 * @return the to upper case
	 */
	public ChangeCaseAction getToUpperCase() {
		return toUpperCaseAction;
	}

	/**
	 * Gets the to lower case.
	 *
	 * @return the to lower case
	 */
	public ChangeCaseAction getToLowerCase() {
		return toLowerCaseAction;
	}

	/**
	 * Gets the invert case.
	 *
	 * @return the invert case
	 */
	public ChangeCaseAction getInvertCase() {
		return invertCaseAction;
	}

	/**
	 * Initializes actions.
	 */
	private void initializeActions() {
		openDocumentAction = new OpenDocumentAction(flp, this, model);
		createNewDocumentAction = new CreateNewDocumentAction(this, model);
		saveDocumentAction = new SaveDocumentAction(this, model);
		saveAsDocumentAction = new SaveAsDocumentAction(flp, this, model);
		closeTabAction = new CloseTabAction(flp, this, model);
		exitApplicationAction = new ExitApplicationAction(flp, this, model);
		copyTextAction = new CopyTextAction(flp, this, model);
		pasteTextAction = new PasteTextAction(flp, this, model);
		cutTextAction = new CutTextAction(flp, this, model);
		showStatsAction = new ShowStatsAction(this, model, flp);
		availableActionValidator = new AvailableActionValidator(this, model);
		setCroatian = new SetLanguageAction("hr");
		setEnglish = new SetLanguageAction("en");
		setGerman = new SetLanguageAction("de");
		toUpperCaseAction = new ChangeCaseAction(this, model, c -> Character.toUpperCase(c));
		toLowerCaseAction = new ChangeCaseAction(this, model, c -> Character.toLowerCase(c));
		invertCaseAction = new ChangeCaseAction(this, model, c -> {
			if (Character.isUpperCase(c)) {
				return Character.toLowerCase(c);
			} else {
				return Character.toUpperCase(c);
			}
		});
		sortAscendingAction = new LineSortAction(flp, model, "ascending");
		sortDescendingAction = new LineSortAction(flp, model, "descending");
		uniqueAction = new LineSortAction(flp, model, "unique");
	}

	/**
	 * Creates the menus.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		fileMenu = new JMenu();
		menuBar.add(fileMenu);
		fileMenu.add(createNewDocumentAction);
		fileMenu.add(openDocumentAction);
		fileMenu.addSeparator();
		fileMenu.add(saveDocumentAction);
		fileMenu.add(saveAsDocumentAction);
		fileMenu.addSeparator();
		fileMenu.add(closeTabAction);
		fileMenu.add(exitApplicationAction);

		editMenu = new JMenu();
		menuBar.add(editMenu);
		editMenu.add(copyTextAction);
		editMenu.add(pasteTextAction);
		editMenu.add(cutTextAction);

		languageMenu = new JMenu();
		menuBar.add(languageMenu);
		languageMenu.add(setCroatian);
		languageMenu.add(setEnglish);
		languageMenu.add(setGerman);

		toolsMenu = new JMenu();
		menuBar.add(toolsMenu);
		changeCaseMenu = new JMenu();
		toolsMenu.add(changeCaseMenu);
		changeCaseMenu.add(toUpperCaseAction);
		changeCaseMenu.add(toLowerCaseAction);
		changeCaseMenu.add(invertCaseAction);

		sortMenu = new JMenu();
		toolsMenu.add(sortMenu);
		sortMenu.add(sortAscendingAction);
		sortMenu.add(sortDescendingAction);
		toolsMenu.add(uniqueAction);

		helpMenu = new JMenu();
		menuBar.add(helpMenu);
		helpMenu.add(showStatsAction);

		this.setJMenuBar(menuBar);
	}

	/**
	 * Creates the toolbar.
	 */
	private void createToolbars() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(true);

		toolBar.add(createNewDocumentAction);
		toolBar.add(openDocumentAction);
		toolBar.addSeparator();
		toolBar.add(saveDocumentAction);
		toolBar.add(saveAsDocumentAction);
		toolBar.addSeparator();
		toolBar.add(closeTabAction);
		toolBar.add(exitApplicationAction);
		toolBar.addSeparator();
		toolBar.add(copyTextAction);
		toolBar.add(pasteTextAction);
		toolBar.add(cutTextAction);
		toolBar.addSeparator();
		toolBar.add(toUpperCaseAction);
		toolBar.add(toLowerCaseAction);
		toolBar.add(invertCaseAction);
		toolBar.addSeparator();
		toolBar.add(showStatsAction);
		panel.add(toolBar, BorderLayout.NORTH);
	}

	/**
	 * The Class ActionLanguagePack used for the purpose of storing the data about a
	 * single action. Makes the translation of the actions name, accelerators,
	 * mnemonics and descriptions possible.
	 */
	public static class ActionLanguagePack {

		/** The name of the action. */
		String name;

		/** The key stroke of the action. */
		String keyStroke;

		/** The key event of the action. */
		String keyEvent;

		/** The description of the action. */
		String description;

		/**
		 * Instantiates a new action language pack.
		 *
		 * @param name
		 *            the name of the action
		 * @param keyStroke
		 *            the key stroke of the action
		 * @param keyEvent
		 *            the key event of the action
		 * @param description
		 *            the description of the action
		 */
		public ActionLanguagePack(String name, String keyStroke, String keyEvent, String description) {
			this.name = name;
			this.keyStroke = keyStroke;
			this.keyEvent = keyEvent;
			this.description = description;
		}
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
	}

}
