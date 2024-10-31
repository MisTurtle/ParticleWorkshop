package particleworkshop.common.structures;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import particleworkshop.common.structures.items.EmptyItem;
import particleworkshop.common.structures.items.spawners.EntitySpawnerItem;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "@type"
		)
@JsonSubTypes({ // This might require Reflections in the long run
	@JsonSubTypes.Type(value = EmptyItem.class, name = "empty"),
	@JsonSubTypes.Type(value = EntitySpawnerItem.class, name = "spawner:entity")
})
public class ItemBase
{
	private ItemIdentifier identifier;
	private Vector2 position;
	
	public ItemBase()
	{
		identifier = new ItemIdentifier();
		identifier.setName(getDefaultName());
		position = new Vector2();
	}
	
	public ItemBase(ItemIdentifier identifier)
	{
		this.identifier = identifier;
		position = new Vector2();
	}
	
	public void setIdentifier(ItemIdentifier identifier)
	{
		this.identifier = identifier;
	}
	public ItemIdentifier getIdentifier()
	{
		return identifier;
	}
	public void setPosition(Vector2 position)
	{
		this.position = position;
	}
	public Vector2 getPosition()
	{
		return position;
	}
	
	@JsonIgnore
	public long getUID()
	{
		return getIdentifier().getUID();
	}
	
	@JsonIgnore
	public String getName()
	{
		return getIdentifier().getName();
	}
	
	@JsonIgnore
	public String getDefaultName()
	{
		return "Unnamed Item";
	}
	
	@JsonIgnore
	public double getX()
	{
		return position.getX();
	}
	@JsonIgnore
	public double getY()
	{
		return position.getY();
	}
}
