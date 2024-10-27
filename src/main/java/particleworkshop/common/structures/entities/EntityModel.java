package particleworkshop.common.structures.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import lombok.Data;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.EXISTING_PROPERTY,
		property = "type",
		visible = true
		)
@JsonSubTypes({
	@JsonSubTypes.Type(value = ThermalParticleModel.class, name = "ThermalParticle"),
	@JsonSubTypes.Type(value = FlowingParticleModel.class, name = "FlowingParticle"),
	@JsonSubTypes.Type(value = BoidsEntityModel.class, name = "BoidsEntity")
})
@Data
public abstract class EntityModel {

	private EntityType type = EntityType.FlowingParticle;
	
	@Controlled(label="Entity Shape")
	private SimpleObjectProperty<EntityShape> shape = new SimpleObjectProperty<>(EntityShape.LINEAR);
	
	@Controlled(label="Entity Color")
	private Color color = Color.BLACK;
	
	@Controlled(label="Initial velocity")
	private SimpleFloatProperty defaultVelocity = new SimpleFloatProperty(50);  // px / sec
	
	@Controlled(label="Max Velocity")
	private SimpleFloatProperty maxVelocity = new SimpleFloatProperty(100);  // px / sec

}
