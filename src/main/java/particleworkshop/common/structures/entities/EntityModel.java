package particleworkshop.common.structures.entities;

import javafx.scene.paint.Color;
import lombok.Data;

@Data
public abstract class EntityModel {
	
	private EntityShape shape = EntityShape.LINEAR;
	private Color color = Color.BLACK;
	private float defaultVelocity = 50;  // px / sec
	private float maxVelocity = 100;  // px / sec
	
}
