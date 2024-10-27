package particleworkshop.common.structures.entities;

import javafx.beans.property.SimpleIntegerProperty;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;

public class BoidsEntityModel extends EntityModel
{
	@Controlled(label="Group ID")
	private SimpleIntegerProperty groupId = new SimpleIntegerProperty(0);
	
	public int getGroupId() { return groupId.get(); }
	public void setGroupId(int id) { groupId.set(id); }

	@Override
	public EntityType getType() {
		return EntityType.BoidsEntity;
	}
}
