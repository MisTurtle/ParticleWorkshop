package particleworkshop.editor.item.list;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import particleworkshop.common.structures.items.EmptyItem;
import particleworkshop.editor.item.EditorItemBase;
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
		super(item.getIdentifier(), item.getPosition());
		unused = new SimpleIntegerProperty(item.getUnused());
	}

	@Override
	public Node getIcon() {
		return null;
	}

	@Override
	public EmptyItem asStructure() {
		EmptyItem struct = new EmptyItem();

		fillBaseStruct(struct);
		struct.setUnused(unused.get());
		
		return struct;
	}

}
