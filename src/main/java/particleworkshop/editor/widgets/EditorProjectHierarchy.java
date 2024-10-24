package particleworkshop.editor.widgets;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import particleworkshop.editor.EditorContext;

public class EditorProjectHierarchy extends TreeView<String> implements IEditorWidget
{
	private EditorContext _context;
	
	public EditorProjectHierarchy(EditorContext context)
	{
		_context = context;
		getStyleClass().add("project-hierarchy");
		
		// Setup listeners
		context.addPropertyChangeListener(evt -> {
			if(evt.getPropertyName().equals(context.EVT_PROJECT_CHANGE))
				updateAll();
			else if(evt.getPropertyName().equals(context.EVT_PROJECT_RENAME))
				updateRoot();
		});

		this.getSelectionModel().selectedIndexProperty().addListener((observer, oldVal, newVal) -> context.selectItemIndex(newVal.intValue() - 1));
		
		updateRoot();
	}

	@Override
	public EditorContext getContext() {
		return _context;
	}
	
	@SuppressWarnings("unchecked")
	public void updateAll()
	{
		updateRoot();
		if(getRoot() == null) return;
 			
		TreeItem<String>[] treeItems = _context.getEditorItems().stream().map(item -> item.asTreeItem()).toArray(TreeItem[]::new);
		getRoot().getChildren().setAll(treeItems);
	}
	
	public void updateRoot()
	{
		if(_context.getProject() == null) { setRoot(null); return; }
		else if(getRoot() == null) setRoot(new TreeItem<String>(_context.getProject().getName()));
		else getRoot().setValue(_context.getProject().getName());
		
		getRoot().setExpanded(true);
	}

}
