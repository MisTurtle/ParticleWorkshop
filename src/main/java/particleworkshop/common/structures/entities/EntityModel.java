package particleworkshop.common.structures.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "@type",
		visible = true
		)
@JsonSubTypes({
	@JsonSubTypes.Type(value = ThermalParticleModel.class, name = "ThermalParticle"),
	@JsonSubTypes.Type(value = FlowingParticleModel.class, name = "FlowingParticle"),
	@JsonSubTypes.Type(value = BoidsEntityModel.class, name = "BoidsEntity")
})
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class EntityModel {

	@Controlled(label="Entity Shape")
	private SimpleObjectProperty<EntityShape> shape = new SimpleObjectProperty<>(EntityShape.LINEAR);
	
	@Controlled(label="Entity Color")
	private Color color = Color.BLACK;
	
	@Controlled(label="Initial velocity")
	private SimpleFloatProperty defaultVelocity = new SimpleFloatProperty(50);  // px / sec
	
	@Controlled(label="Max Velocity")
	private SimpleFloatProperty maxVelocity = new SimpleFloatProperty(100);  // px / sec
	
	public abstract EntityType getType();
	
	public EntityShape getShape() { return shape.get(); }
	public void setShape(EntityShape s) { shape.set(s); }
	
	//public Color getColor() { return color; }
	//public void setColor(Color c) { color = c; }
	
	public float getDefaultVelocity() { return defaultVelocity.get(); }
	public void setDefaultVelocity(float v) { defaultVelocity.set(v); }

	public float getMaxVelocity() { return maxVelocity.get(); }
	public void setMaxVelocity(float v) { maxVelocity.set(v); }
}
