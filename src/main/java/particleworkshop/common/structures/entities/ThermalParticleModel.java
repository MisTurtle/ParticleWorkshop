package particleworkshop.common.structures.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ThermalParticleModel extends EntityModel
{@Override
	public EntityType getType() {
		return EntityType.ThermalParticle;
	}
}
