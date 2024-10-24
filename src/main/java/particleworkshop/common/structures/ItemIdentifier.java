package particleworkshop.common.structures;

import java.util.Random;

import lombok.Data;

@Data
public class ItemIdentifier {
	
	private String name;
	private long UID;
	
	public ItemIdentifier() {
		this("Unnamed Object");
	}
	public ItemIdentifier(String name) { 
		this(name, generateUID());
	}
	public ItemIdentifier(String name, long UID)
	{
		this.name = name;
		this.UID = UID;
	}
	public ItemIdentifier(ItemIdentifier o)
	{
		this(o.name);
	}
	
	public static long generateUID() { return (new Random()).nextLong(); }
}
