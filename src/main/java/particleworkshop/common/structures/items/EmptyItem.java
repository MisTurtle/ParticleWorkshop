package particleworkshop.common.structures.items;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import particleworkshop.common.structures.ItemBase;

/**
 * Empty item used for tests
 * Might be useful later if support for custom scripts is added, but that's probably not going to happen any time soon
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class EmptyItem extends ItemBase {
	
	private int unused = 0;

	@Override
	@JsonIgnore
	public String getDefaultName()
	{
		return "EmptyItem";
	}
	
}
