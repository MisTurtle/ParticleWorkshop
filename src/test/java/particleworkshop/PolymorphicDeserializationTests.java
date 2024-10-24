package particleworkshop;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import particleworkshop.common.structures.ItemBase;
import particleworkshop.common.structures.items.EmptyItem;
import particleworkshop.common.structures.items.spawners.EntitySpawnerItem;
import particleworkshop.common.structures.items.spawners.SpawnerMode;
import particleworkshop.common.utils.SimulationItemsList;

public class PolymorphicDeserializationTests {
	
	@Test
	public void test() throws JsonMappingException, JsonProcessingException
	{
		ObjectMapper mp = new ObjectMapper();
	
		EmptyItem item1 = new EmptyItem();
		String item1_json = mp.writeValueAsString(item1);
		System.out.println("Empty item serialized as : " + item1_json);
		
		// Deserialize while knowing the target class
		EmptyItem item1_unserialized = mp.readValue(item1_json, EmptyItem.class);
		assertEquals(item1.getUID(), item1_unserialized.getUID());
		
		// Deserialize without knowing the target class
		ItemBase item1_base = mp.readValue(item1_json, ItemBase.class);
		assertEquals(item1.getUID(), item1_base.getUID());
		assertTrue(item1_base instanceof EmptyItem);
		
		// Serialize and deserialize list of items
		EntitySpawnerItem spawner1 = new EntitySpawnerItem();
		spawner1.setMode(SpawnerMode.ALTERNATE);
		SimulationItemsList items = new SimulationItemsList();
		items.add(item1); items.add(spawner1);
		
		String list_items_json = mp.writeValueAsString(items);
		System.out.println("List of items serialized as : " + list_items_json);
		
		SimulationItemsList items_unserialized = mp.readValue(list_items_json, SimulationItemsList.class);
		assertEquals(items_unserialized.size(), 2);
		assertTrue(items_unserialized.get(0) instanceof EmptyItem);
		assertTrue(items_unserialized.get(1) instanceof EntitySpawnerItem);
		assertEquals(items_unserialized.get(0).getUID(), item1.getUID());

		EntitySpawnerItem spawner2 = (EntitySpawnerItem) items_unserialized.get(1);
		assertEquals(spawner2.getMode(), SpawnerMode.ALTERNATE);
	}
	
}
