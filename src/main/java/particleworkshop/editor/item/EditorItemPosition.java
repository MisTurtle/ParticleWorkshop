package particleworkshop.editor.item;

import javafx.beans.property.SimpleFloatProperty;
import lombok.Data;
import particleworkshop.common.structures.Vector2;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;

@Data
public class EditorItemPosition 
{
	@Controlled(label = "X")
	private SimpleFloatProperty x = new SimpleFloatProperty();
	@Controlled(label = "Y")
	private SimpleFloatProperty y = new SimpleFloatProperty();
	
	public EditorItemPosition() { this(new Vector2()); }
	
	public EditorItemPosition(Vector2 pos)
	{
		x.set((float) pos.getX());
		y.set((float) pos.getY());
	}
}
