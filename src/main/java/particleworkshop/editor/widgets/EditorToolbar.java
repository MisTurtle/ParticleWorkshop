package particleworkshop.editor.widgets;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import particleworkshop.editor.EditorContext;
import particleworkshop.editor.item.EditorItemBase;
import particleworkshop.editor.item.EditorItemCategory;
import particleworkshop.editor.item.EditorItemFactory;

public class EditorToolbar extends ToolBar implements IEditorWidget
{
	private EditorContext _context;
	private static final double height = 20.;  // px
	
	public EditorToolbar(EditorContext context)
	{
		super();
		setPrefHeight(height);
		_context = context;
		getItems().addAll(buildToolbarItems());
	}
	
	@Override
	public EditorContext getContext() {
		return _context;
	}
	
	private Control[] buildToolbarItems()
	{
		List<Control> items = new ArrayList<>();
		
		for(Entry<EditorItemCategory, List<Class<? extends EditorItemBase<?>>>> entry: EditorItemFactory.categoryContentMap.entrySet())
		{
			ImageView view = new ImageView(new Image(entry.getKey().iconName));
			view.setFitHeight(height); view.setFitWidth(height);
			
			Button button = new Button();
			button.setGraphic(view);
			button.setPrefSize(height, height);
			
			if(entry.getValue().size() == 1)
			{
				button.setOnAction(evt -> {
					try {
						EditorItemBase<?> newItem = entry.getValue().get(0).getDeclaredConstructor().newInstance();
						getContext().addItem(newItem);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
				});
			}
			
			items.add(button);
		}
		
		return items.toArray(new Control[0]);
	}

}
