package particleworkshop;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import particleworkshop.common.structures.ItemBase;
import particleworkshop.common.structures.items.EmptyItem;
import particleworkshop.common.structures.items.spawners.EntitySpawnerItem;
import particleworkshop.editor.item.EditorItemBase;
import particleworkshop.editor.item.EditorItemFactory;
import particleworkshop.editor.item.list.EditorEmptyItem;
import particleworkshop.editor.item.list.EditorSpawnerItem;

public class EditorItemAdapterTest {

	@Test
	public void test() throws JsonProcessingException {
		// TODO : Automatically build an object of every class through reflection
		ObjectMapper mp = new ObjectMapper();
	
		EmptyItem item1 = new EmptyItem(); String item1_json = mp.writeValueAsString(item1);
		EntitySpawnerItem item2 = new EntitySpawnerItem(); String item2_json = mp.writeValueAsString(item2);
		
		// Deserialize Individually, not knowing the target type
		ItemBase i = mp.readValue(item1_json, ItemBase.class);
		assertTrue(i instanceof EmptyItem);
		
		EditorItemBase<?> editorItem1 = EditorItemFactory.fromStructure(i);
		assertTrue(editorItem1 instanceof EditorEmptyItem);
		assertFalse(editorItem1 instanceof EditorSpawnerItem);
		
		EditorItemBase<?> editorItem2 = EditorItemFactory.fromStructure(mp.readValue(item2_json, ItemBase.class));
		assertFalse(editorItem2 instanceof EditorEmptyItem);
		assertTrue(editorItem2 instanceof EditorSpawnerItem);
	}

}
