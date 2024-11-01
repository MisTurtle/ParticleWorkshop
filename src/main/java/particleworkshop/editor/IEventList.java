package particleworkshop.editor;

public interface IEventList {
	public final String EVT_PROJECT_CHANGE = "project:change";
	public final String EVT_PROJECT_RENAME = "project:rename";
	public final String EVT_PROJECT_SAVE = "project:save";
	
	public final String EVT_ITEM_SELECTED = "item:select";
	public final String EVT_ITEM_CREATED = "item:create";
	public final String EVT_ITEM_DELETED = "item:delete";
	public final String EVT_ITEM_CHANGED = "item:changed";
	public final String EVT_ITEM_MOVED = "item:moved"; // Called when an item is moved from the scene editor and not the inspector
	
	public final String EVT_SIM_SETTINGS_CHANGED = "settings:changed";
}
