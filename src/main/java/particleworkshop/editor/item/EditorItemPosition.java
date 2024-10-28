package particleworkshop.editor.item;

import javafx.beans.property.SimpleFloatProperty;
import lombok.Data;
import particleworkshop.common.structures.ItemPosition;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;

@Data
public class EditorItemPosition 
{
	@Controlled(label = "X")
	private SimpleFloatProperty x = new SimpleFloatProperty();
	@Controlled(label = "Y")
	private SimpleFloatProperty y = new SimpleFloatProperty();
	
	public EditorItemPosition() { this(new ItemPosition()); }
	
	public EditorItemPosition(ItemPosition pos)
	{
		x.set((float) pos.getX());
		y.set((float) pos.getY());
	}
}
