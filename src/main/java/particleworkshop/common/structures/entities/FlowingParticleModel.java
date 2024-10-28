package particleworkshop.common.structures.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FlowingParticleModel extends EntityModel 
{
	public FlowingParticleModel() { super(null); }
	public FlowingParticleModel(EntityModel o) { super(o); }
	
	@Override
	public EntityType getType() {
		return EntityType.FlowingParticle;
	}
}
