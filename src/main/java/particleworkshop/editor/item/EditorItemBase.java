package particleworkshop.editor.item;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import particleworkshop.common.structures.ItemBase;
import particleworkshop.common.structures.ItemIdentifier;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;
import particleworkshop.editor.widgets.inspector.annotations.Separator;

public abstract class EditorItemBase<T extends ItemBase> implements IEditorItem<T> {
	
	@Controlled @Separator(before=false, after=true)
	protected SimpleStringProperty name;
	protected SimpleLongProperty uid;
	
	public EditorItemBase(ItemIdentifier identifier)
	{
		name = new SimpleStringProperty(identifier.getName());
		uid = new SimpleLongProperty(identifier.getUID());
	}

	public String getName()
	{
		return name.get();
	}
	public long getUID()
	{
		return uid.get();
	}
	public Node getIcon()
	{
		return null;
	}
	public ItemIdentifier getIdentifier()
	{
		return new ItemIdentifier(getName(), getUID());
	}
	
	public TreeItem<String> asTreeItem()
	{
		TreeItem<String> treeItem = new TreeItem<>(getName());
		treeItem.valueProperty().bind(name);
		return treeItem;
	}
}
