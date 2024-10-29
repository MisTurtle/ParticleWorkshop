package particleworkshop.editor.widgets;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import particleworkshop.editor.EditorContext;
import particleworkshop.editor.IEventList;
import particleworkshop.editor.item.EditorItemBase;

public class EditorProjectDisplay extends StackPane implements IEditorWidget {

	private EditorContext _context;
	
	public EditorProjectDisplay(EditorContext context)
	{
		_context = context;
		context.addPropertyChangeListener(evt -> {
			if(
					evt.getPropertyName().equals(EVT_PROJECT_CHANGE) ||
					evt.getPropertyName().equals(EVT_ITEM_CREATED) ||
					evt.getPropertyName().equals(EVT_ITEM_CHANGED)
				)
				redrawProject();
		});
	}
	
	private void redrawProject()
	{
		System.out.println("Redrawing project");
		getChildren().clear();
		for(EditorItemBase<?> item: _context.getEditorItems())
		{
			createItem(item);
		}
	}
	
	private void createItem(EditorItemBase<?> item)
	{
		Group g = item.render();
		g.setOnMouseClicked(evt -> _context.selectItem(item));
		getChildren().add(g);
	}
	
	@Override
	public EditorContext getContext() {
		return _context;
	}
	
}
