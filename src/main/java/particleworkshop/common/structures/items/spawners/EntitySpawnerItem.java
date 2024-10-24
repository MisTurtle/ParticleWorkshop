package particleworkshop.common.structures.items.spawners;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;

import lombok.Data;
import lombok.EqualsAndHashCode;
import particleworkshop.common.structures.ItemBase;
import particleworkshop.common.structures.entities.EntityModel;

@Data
@EqualsAndHashCode(callSuper=false)
public class EntitySpawnerItem extends ItemBase {
	
	private SpawnerMode mode = SpawnerMode.LINEAR;
	private float frequency = 5.0f;
	private List<Float> directions = Arrays.asList(0.f); // Array of directions in which particles can be spawned
	private EntityModel model = null;
	
	@Override
	@JsonIgnore
	public String getDefaultName()
	{
		return "EntitySpawner";
	}
	
}

