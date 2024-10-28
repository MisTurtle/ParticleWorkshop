package particleworkshop.common.structures.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ThermalParticleModel extends EntityModel
{
	public ThermalParticleModel() { super(null); }
	public ThermalParticleModel(EntityModel o) { super(o); }
	
	@Override
	public EntityType getType() {
		return EntityType.ThermalParticle;
	}
}
