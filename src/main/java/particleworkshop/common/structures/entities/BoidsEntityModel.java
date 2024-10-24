package particleworkshop.common.structures.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BoidsEntityModel extends EntityModel
{
	private int groupId = 0; // Which group should this boids care about
}
