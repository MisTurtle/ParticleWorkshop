package particleworkshop;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonLibraryTest {

	@Test
	public void test() throws JsonProcessingException {
		Map<String, Integer> testMap = new HashMap<>();
		for(int i = 0; i < 100; ++i)
			testMap.put("Test #" + String.valueOf(i), 2 * i + 5);
		
		ObjectMapper mp = new ObjectMapper();
		String mapAsStr = mp.writeValueAsString(testMap);
		@SuppressWarnings("unchecked")
		Map<String, Integer> readMap = mp.readValue(mapAsStr, HashMap.class);
		
		for(int i = 0; i < 100; ++i)
		{
			assertTrue(readMap.containsKey("Test #" + String.valueOf(i)));
			assertEquals((int) readMap.get("Test #" + String.valueOf(i)), i * 2 + 5);
		}
	}

}
