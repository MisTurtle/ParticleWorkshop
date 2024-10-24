package particleworkshop.editor.item;

import java.util.ArrayList;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Region;
import particleworkshop.common.structures.ItemBase;
import particleworkshop.common.structures.ItemIdentifier;
import particleworkshop.editor.widgets.inspector.InspectorWidgetFactory;

public abstract class EditorItemBase<T extends ItemBase> implements IEditorItem<T> {
	
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
	
	public ArrayList<Region> generateControls()
	{
		ArrayList<Region> controls = new ArrayList<>();
		
		TextField tf = InspectorWidgetFactory.newTextFieldWithRegex(name, "^[A-Za-z0-9 ]*$");
		tf.getStyleClass().add("inspector-ghost-text-field");
		controls.add(tf);
		controls.add(InspectorWidgetFactory.newSeparator());

		return controls;
	}
}
