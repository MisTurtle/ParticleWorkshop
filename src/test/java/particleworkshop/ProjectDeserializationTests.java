package particleworkshop;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import particleworkshop.common.structures.items.EmptyItem;
import particleworkshop.common.structures.items.spawners.EntitySpawnerItem;
import particleworkshop.common.structures.items.spawners.SpawnerMode;
import particleworkshop.common.structures.project.SimulationStructure;

public class ProjectDeserializationTests {

	@Test
	public void test() throws JsonProcessingException {
		ObjectMapper mp = new ObjectMapper();
		
		SimulationStructure testProject = new SimulationStructure();
		testProject.setName("Test Project ###");
		String emptyJson = mp.writerWithDefaultPrettyPrinter().writeValueAsString(testProject);
		System.out.println("> Empty project serialized as:\n" + emptyJson);
		SimulationStructure testProjectEmptyD = mp.readValue(emptyJson, SimulationStructure.class);
		assertEquals(testProject.getName(), testProjectEmptyD.getName());
		
		EmptyItem emptyItem1 = new EmptyItem(); emptyItem1.setUnused(1);
		EmptyItem emptyItem2 = new EmptyItem(); emptyItem2.setUnused(2);
		EntitySpawnerItem spawnerItem1 = new EntitySpawnerItem(); spawnerItem1.setMode(SpawnerMode.RANDOM);
		
		testProject.addItems(emptyItem1, spawnerItem1, emptyItem2);
		assertEquals(testProject.getItems().size(), 3);
		String filledJson = mp.writerWithDefaultPrettyPrinter().writeValueAsString(testProject);
		System.out.println("\n> Filled project serialized as:\n" + filledJson);
		SimulationStructure testProjectFilledD = mp.readValue(filledJson, SimulationStructure.class);
		assertEquals(testProjectFilledD.getItems().size(), 3);
	}

}
