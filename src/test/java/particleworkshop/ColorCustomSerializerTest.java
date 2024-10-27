package particleworkshop;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import particleworkshop.common.structures.entities.EntityModel;
import particleworkshop.common.structures.entities.ThermalParticleModel;

public class ColorCustomSerializerTest {

	@Test
	public void test() throws JsonProcessingException {
		ThermalParticleModel model = new ThermalParticleModel();
		model.setColor("#DEADBEEF");
		ObjectMapper mp = new ObjectMapper();
		
		String modelJson = mp.writeValueAsString(model);
		System.out.println("Serialized as : " + modelJson);
		
		EntityModel deserialized = mp.readValue(modelJson, EntityModel.class);
		assertEquals(model.getColor(), deserialized.getColor());
	}

}
