package particleworkshop.editor.widgets;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.*;
import particleworkshop.common.exception.MenuDoesNotExistException;
import particleworkshop.common.exception.MenuExistsException;
import particleworkshop.editor.EditorContext;

public final class EditorMenuBar extends MenuBar implements IEditorWidget
{
	private EditorContext _context;
	private Map<String, MenuItem> _items = new HashMap<>();
	
	public EditorMenuBar(EditorContext context)
	{
		super();
		_context = context;
		getMenus().add(buildFileMenu());
	}
	
	public EditorContext getContext()
	{
		return _context;
	}
	
	public MenuItem getItemById(String identifier) throws MenuDoesNotExistException
	{
		if(!_items.containsKey(identifier)) throw new MenuDoesNotExistException("No menu identified with " + identifier + " could be found");
		return _items.get(identifier);
	}
	
	public void applyMethodSafely(String identifier, Consumer<MenuItem> c)
	{
		try {
			c.accept(getItemById(identifier));
		}catch(MenuDoesNotExistException e) {
			e.printStackTrace();
		}
	}
	
	private void assertUnknown(String identifier) throws MenuExistsException
	{
		if(_items.containsKey(identifier)) throw new MenuExistsException("A menu identified with " + identifier + " already exists.");
	}
	
	private MenuItem newMenuItem(String identifier, String label, EventHandler<ActionEvent> h) throws MenuExistsException
	{
		assertUnknown(identifier);
		
		MenuItem i = new MenuItem(label);
		i.setOnAction(h);
		i.getStyleClass().addAll("darklight", "lighthover");
		
		_items.put(identifier, i);
		return i;
	}
	
	private Menu newMenu(String identifier, String label) throws MenuExistsException
	{
		assertUnknown(identifier);
		
		Menu i = new Menu(label);
		i.getStyleClass().addAll("darklight", "lighthover");
		
		_items.put(identifier, i);
		return i;
	}

	private SeparatorMenuItem newSeparator()
	{
		SeparatorMenuItem separator = new SeparatorMenuItem();
		return separator;
	}
	
	private Menu buildFileMenu()
	{
		try {
			Menu fileMenu = newMenu("file", "File");
			
			// New project subitem
			MenuItem newFile = newMenuItem("file:new", "New...", ev -> getContext().actNewProject());
			newFile.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
			// Open project subitem
			MenuItem openFile = newMenuItem("file:open", "Open...", ev -> getContext().actOpenProject());
			openFile.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
			// Open recent subitem
			Menu openRecent = newMenu("file:open_recent", "Recent Projects");
			// TODO : Update this menu's subitems when hovered

			
			MenuItem saveFile = newMenuItem("file:save", "Save", ev -> getContext().actSaveProject());
			saveFile.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
			
			MenuItem saveFileAs = newMenuItem("file:save_as", "Save As...", ev -> getContext().actSaveProjectAs());
			saveFileAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
			
			MenuItem closeFile = newMenuItem("file:close", "Close Project", ev -> getContext().actCloseProject());
			
			fileMenu.getItems().addAll(
					newFile, openFile, openRecent, newSeparator(),
					saveFile, saveFileAs, newSeparator(),
					closeFile
				);
			fileMenu.getStyleClass().add("darklight");
			return fileMenu;
		}catch(MenuExistsException e) {
			e.printStackTrace();
		}
		return null;
	}
}
