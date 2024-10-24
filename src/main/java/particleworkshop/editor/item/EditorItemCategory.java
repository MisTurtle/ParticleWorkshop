package particleworkshop.editor.item;

public enum EditorItemCategory {
	SPAWNER("Spawner", "icon-spawner.png"),
	EXTRA("Extra", "icon-extra.png");
	
	public final String displayName;
	public final String iconName;
	
	private EditorItemCategory(String displayName, String iconName)
	{
		this.displayName = displayName;
		this.iconName = iconName;
	}
}
