package particleworkshop.editor.widgets;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import particleworkshop.editor.EditorContext;
import particleworkshop.editor.item.EditorItemBase;

public class EditorProjectHierarchy extends TreeView<String> implements IEditorWidget
{
	private EditorContext _context;
	
	public EditorProjectHierarchy(EditorContext context)
	{
		_context = context;
		getStyleClass().add("project-hierarchy");
		
		// Setup listeners
		context.addPropertyChangeListener(evt -> {
			if(evt.getPropertyName().equals(EVT_PROJECT_CHANGE)) updateAll();
			else if(evt.getPropertyName().equals(EVT_PROJECT_RENAME)) updateRoot();
			else if(evt.getPropertyName().equals(EVT_ITEM_CREATED))
			{
				if(evt.getNewValue() == null || !context.getEditorItems().contains(evt.getNewValue())) return;
				else {
					addItem((EditorItemBase<?>) evt.getNewValue());
					getSelectionModel().select(context.getEditorItems().indexOf(evt.getNewValue()) + 1);
				}
			}else if(evt.getPropertyName().equals(EVT_ITEM_SELECTED))
			{
				int index;
				if(evt.getNewValue() == null || !context.getEditorItems().contains(evt.getNewValue())) index = 0;
				else index = context.getEditorItems().indexOf(evt.getNewValue()) + 1;
				
				if(getRoot() == null || index == getSelectionModel().getSelectedIndex() || index > getRoot().getChildren().size()) return;
				getSelectionModel().select(index);
			}
		});

		getSelectionModel().selectedIndexProperty().addListener((observer, oldVal, newVal) -> context.selectItemIndex(newVal.intValue() - 1));
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
	
	public void addItem(EditorItemBase<?> item)
	{
		getRoot().getChildren().add(item.asTreeItem());
	}
	
	public void updateRoot()
	{
		if(_context.getProject() == null) { setRoot(null); return; }
		else if(getRoot() == null) setRoot(new TreeItem<String>(_context.getProject().getName()));
		else getRoot().setValue(_context.getProject().getName());
		
		getRoot().setExpanded(true);
	}

}
