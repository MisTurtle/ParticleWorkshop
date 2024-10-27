package particleworkshop.editor.item.list;

import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import particleworkshop.common.structures.items.EmptyItem;
import particleworkshop.editor.item.EditorItemBase;
import particleworkshop.editor.widgets.inspector.InspectorWidgetFactory;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;
import particleworkshop.editor.widgets.inspector.annotations.UseIntRange;

public class EditorEmptyItem extends EditorItemBase<EmptyItem> {

	@Controlled(label="Unused Property")
	@UseIntRange(max = 65, min = -30)
	private SimpleIntegerProperty unused;  // TODO : Annotations simply to specify the label contents? Then how to make sure each label goes with the right control?
	
	public EditorEmptyItem()
	{
		this(new EmptyItem());
	}
	
	public EditorEmptyItem(EmptyItem item) {
		super(item.getIdentifier());
		unused = new SimpleIntegerProperty(item.getUnused());
	}

	@Override
	public ArrayList<Region> generateControls()
	{
		ArrayList<Region> controls = super.generateControls();
		
		controls.add(InspectorWidgetFactory.newSpinner("Unused", unused, 0, 100));
		controls.add(InspectorWidgetFactory.newTextField("Test", name));
		
		return controls;
	}

	@Override
	public Node getIcon() {
		return null;
	}

	@Override
	public EmptyItem asStructure() {
		EmptyItem struct = new EmptyItem();
		
		struct.setIdentifier(getIdentifier());
		struct.setUnused(unused.get());
		
		return struct;
	}

}
