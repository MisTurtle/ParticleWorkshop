package particleworkshop.editor.item;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import particleworkshop.common.structures.ItemBase;
import particleworkshop.common.structures.ItemIdentifier;
import particleworkshop.common.structures.ItemPosition;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;
import particleworkshop.editor.widgets.inspector.annotations.HorizontalBlock;
import particleworkshop.editor.widgets.inspector.annotations.Separator;

public abstract class EditorItemBase<T extends ItemBase> implements IEditorItem<T> {
	
	@Controlled 
	protected SimpleStringProperty name;
	protected SimpleLongProperty uid;
	@HorizontalBlock(label = "Position", size = 2)
	@Controlled(label = "X")
	protected SimpleFloatProperty x;
	@Controlled(label = "Y")
	@Separator(before=false, after=true)
	protected SimpleFloatProperty y;
	
	public EditorItemBase(ItemIdentifier identifier, ItemPosition position)
	{
		name = new SimpleStringProperty(identifier.getName());
		uid = new SimpleLongProperty(identifier.getUID());
		x = new SimpleFloatProperty((float) position.getX());
		y = new SimpleFloatProperty((float) position.getY());
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
	public float getX()
	{
		return x.get();
	}
	public float getY()
	{
		return y.get();
	}
	public ItemPosition getPosition()
	{
		return new ItemPosition((double) getX(), (double) getY());
	}
	
	public TreeItem<String> asTreeItem()
	{
		TreeItem<String> treeItem = new TreeItem<>(getName());
		treeItem.valueProperty().bind(name);
		return treeItem;
	}
	
	protected void fillBaseStruct(ItemBase target)
	{
		target.setIdentifier(getIdentifier());
		target.setPosition(getPosition());
	}
}
