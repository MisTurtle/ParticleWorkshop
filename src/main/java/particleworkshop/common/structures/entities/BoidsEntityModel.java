package particleworkshop.common.structures.entities;

import javafx.beans.property.SimpleIntegerProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;

@Data
@EqualsAndHashCode(callSuper=false)
public class BoidsEntityModel extends EntityModel
{
	@Controlled(label="Group ID")
	private SimpleIntegerProperty groupId = new SimpleIntegerProperty(0);
}
