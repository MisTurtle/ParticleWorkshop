package particleworkshop.common.structures.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import particleworkshop.common.structures.ItemBase;

@Data
@EqualsAndHashCode(callSuper=false)
public class SimulationStructure {
	
	private String name;
	private SimulationSettingsContainer settings;
	@JsonIgnore private HashMap<Long, ItemBase> itemMap;
	
	public SimulationStructure(
			String name,
			SimulationSettingsContainer settings,
			ArrayList<ItemBase> items
	) {
		this.name = name;
		this.settings = settings;
		this.itemMap = new HashMap<>();
		if(items.size() > 0) setItems(items);
	}

	public SimulationStructure()
	{
		this("Untitled Simulation", new SimulationSettingsContainer(), new ArrayList<>());
	}

	public void setItems(ArrayList<ItemBase> itemList)
	{
		itemMap.clear();
		for(ItemBase i: itemList)
			addItem(i);
	}
	
	public Collection<ItemBase> getItems()
	{
		return itemMap.values();
	}
	
	public void addItem(ItemBase i)
	{
		itemMap.put(i.getIdentifier().getUID(), i);
	}

	public void addItems(ItemBase ...items)
	{
		for(ItemBase i: items)
			addItem(i);
	}
}
