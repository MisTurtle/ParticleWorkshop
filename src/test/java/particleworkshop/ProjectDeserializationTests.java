package particleworkshop;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import particleworkshop.common.structures.ItemBase;
import particleworkshop.common.structures.entities.BoidsEntityModel;
import particleworkshop.common.structures.entities.EntityShape;
import particleworkshop.common.structures.items.EmptyItem;
import particleworkshop.common.structures.items.spawners.EntitySpawnerItem;
import particleworkshop.common.structures.items.spawners.SpawnerMode;
import particleworkshop.common.structures.project.SimulationStructure;

public class ProjectDeserializationTests {

	@Test
	public void test() throws JsonProcessingException, IllegalArgumentException, IllegalAccessException {
		ObjectMapper mp = new ObjectMapper();
		
		SimulationStructure testProject = new SimulationStructure();
		testProject.setName("Test Project ###");
		String emptyJson = mp.writerWithDefaultPrettyPrinter().writeValueAsString(testProject);
		System.out.println("> Empty project serialized as:\n" + emptyJson);
		SimulationStructure testProjectEmptyD = mp.readValue(emptyJson, SimulationStructure.class);
		assertEquals(testProject.getName(), testProjectEmptyD.getName());
		
		EmptyItem emptyItem1 = new EmptyItem(); emptyItem1.setUnused(1);
		EmptyItem emptyItem2 = new EmptyItem(); emptyItem2.setUnused(2);
		EntitySpawnerItem spawnerItem1 = new EntitySpawnerItem(); 
		spawnerItem1.setMode(SpawnerMode.RANDOM);
		spawnerItem1.setModel(new BoidsEntityModel());
		spawnerItem1.getModel().setShape(EntityShape.CIRCULAR);
		((BoidsEntityModel) spawnerItem1.getModel()).setGroupId(69);
		
		testProject.addItems(emptyItem1, spawnerItem1, emptyItem2);
		assertEquals(testProject.getItems().size(), 3);
		String filledJson = mp.writerWithDefaultPrettyPrinter().writeValueAsString(testProject);
		System.out.println("\n> Filled project serialized as:\n" + filledJson);
		SimulationStructure testProjectFilledD = mp.readValue(filledJson, SimulationStructure.class);
		assertEquals(testProjectFilledD.getItems().size(), 3);
		EntitySpawnerItem spawnerItem2 = null;
		
		for(ItemBase item : testProjectFilledD.getItems())
			if(item instanceof EntitySpawnerItem)
				spawnerItem2 = (EntitySpawnerItem) item;
		
		assertNotEquals(spawnerItem2, null);
		assertEquals(spawnerItem1.getMode(), spawnerItem2.getMode());
		assertEquals(spawnerItem1.getFrequency(), spawnerItem2.getFrequency(), 0.00001);
		
		System.out.println("Specific Properties for original model : ");
		for(Field f: spawnerItem1.getModel().getClass().getDeclaredFields())
		{
			f.setAccessible(true);
			System.out.println(" - " + f.getName() + " --> " + f.get(spawnerItem1.getModel()).toString());
		}
		System.out.println("Specific Properties for deserialized model : ");
		for(Field f: spawnerItem2.getModel().getClass().getDeclaredFields())
		{
			f.setAccessible(true);
			System.out.println(" - " + f.getName() + " --> " + f.get(spawnerItem2.getModel()).toString());
		}
	}

}
