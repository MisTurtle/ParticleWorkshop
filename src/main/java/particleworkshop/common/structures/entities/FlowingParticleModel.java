package particleworkshop.common.structures.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FlowingParticleModel extends EntityModel 
{
	@Override
	public EntityType getType() {
		return EntityType.FlowingParticle;
	}
}
