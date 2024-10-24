package particleworkshop.editor.item;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import particleworkshop.common.structures.ItemBase;
import particleworkshop.common.structures.items.EmptyItem;
import particleworkshop.common.structures.items.spawners.EntitySpawnerItem;
import particleworkshop.editor.item.list.EditorEmptyItem;
import particleworkshop.editor.item.list.EditorSpawnerItem;

public class EditorItemFactory 
{
	private static boolean initialized = false;
	public static Map<Class<? extends ItemBase>, Class<? extends EditorItemBase<?>>> structToEditorMap = new HashMap<>();
	public static Map<EditorItemCategory, List<Class<? extends EditorItemBase<?>>>> categoryContentMap = new HashMap<>();
	
	public static void init()
	{
		if(initialized) return;
		
		registerItem(EmptyItem.class, EditorEmptyItem.class, EditorItemCategory.EXTRA);
		registerItem(EntitySpawnerItem.class, EditorSpawnerItem.class, EditorItemCategory.SPAWNER);
		
		initialized = true;
	}
	
	private static void registerItem(
			Class<? extends ItemBase> structureType,
			Class<? extends EditorItemBase<?>> editorType,
			EditorItemCategory itemCategory
			)
	{
		structToEditorMap.put(structureType, editorType);
		if(!categoryContentMap.containsKey(itemCategory))
			categoryContentMap.put(itemCategory, new ArrayList<>());
		categoryContentMap.get(itemCategory).add(editorType);
	}
	
	public static EditorItemBase<?> fromStructure(ItemBase s)
	{
		if(!initialized) init();

		for(Entry<Class<? extends ItemBase>, Class<? extends EditorItemBase<?>>> entry: structToEditorMap.entrySet())
		{	
			if(entry.getKey().isInstance(s))
				try {
					return entry.getValue().getConstructor(entry.getKey()).newInstance(s);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
					return null;
				}
		}
		return null;
	}
	
}
