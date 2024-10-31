package particleworkshop.editor.widgets;

import java.util.List;
import java.util.function.Consumer;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import particleworkshop.common.exception.ObjectSerializationException;
import particleworkshop.editor.EditorContext;
import particleworkshop.editor.item.EditorItemBase;
import particleworkshop.editor.widgets.inspector.DefaultWidgetFactory;
import particleworkshop.editor.widgets.inspector.IWidgetFactory;

public class EditorItemInspector extends VBox implements IEditorWidget
{
	private static final double DEFAULT_INSPECTOR_WIDTH = 175; // px
	
	private EditorContext _context;
	private EditorItemBase<?> _selected;

	public EditorItemInspector(EditorContext context)
	{
		super();
		_context = context;
		
		getStyleClass().add("item-inspector");
		// setPrefWidth(DEFAULT_INSPECTOR_WIDTH);
		getContext().addPropertyChangeListener(evt -> {
			if(evt.getPropertyName().equals(EVT_ITEM_SELECTED))
				_selected = (EditorItemBase<?>) evt.getNewValue();
			else if(evt.getPropertyName().equals(EVT_PROJECT_CHANGE) && evt.getNewValue() == null)
				_selected = null;
			else if(!evt.getPropertyName().equals(EVT_ITEM_MOVED)) return;
			reload();
		});
	}
	
	@Override
	public EditorContext getContext() {
		return _context;
	}
	
	public void reload()
	{
		try {
			if(_selected == null && _context.getProject() == null) getChildren().clear();
			else {
				List<Region> widgets = DefaultWidgetFactory.getInstance(this).createWidgetsFor(_selected == null ? _context.getProjectSettings() : _selected);
				widgets.forEach(this::bindValueChangeListener);
				getChildren().setAll(new VBox(IWidgetFactory.DEFAULT_VERTICAL_SPACING, widgets.toArray(Region[]::new)));
			}
		} catch (ObjectSerializationException e) {
			e.printStackTrace();
		}
	}
	
	private Consumer<Control> defaultValueChangeHandler()
	{
		return (c) -> {
			getContext().setUnsaved();
			
			if(_selected == null) getContext().onSimulationSettingsChanged(); // Root node selected 
			else getContext().onItemChanged(_selected);
		};
	}
	private Consumer<Control> defaultValueChangeHandler(final Consumer<Control> then)
	{
		return (c) -> {
			defaultValueChangeHandler().accept(c);
			then.accept(c);
		};
	}
	
	private void bindValueChangeListener(Node node)
	{
		if(node instanceof TitledPane) node = ((TitledPane) node).getContent();
		if(node instanceof Control) { bindValueChangeListener((Control) node); return; }
		if(!(node instanceof Parent)) return;
		
		for(Node child: ((Parent) node).getChildrenUnmodifiable())
		{
			if(child instanceof Control) bindValueChangeListener((Control) child);
			else if(child instanceof Parent) bindValueChangeListener((Parent) child);
		}
	}
	
	private void bindValueChangeListener(Control control)
	{
		// TODO : Create a UNDO and REDO list
		if (control instanceof TextField) {
            ((TextField) control).textProperty().addListener((observable, _old, _new) -> {
                defaultValueChangeHandler().accept(control);
            });
        } else if (control instanceof CheckBox) {
            ((CheckBox) control).selectedProperty().addListener((observable, _old, _new) -> {
                defaultValueChangeHandler().accept(control);
            });
        } else if (control instanceof Slider) {
            ((Slider) control).valueProperty().addListener((observable, _old, _new) -> {
                defaultValueChangeHandler().accept(control);
            });
        } else if (control instanceof ToggleButton) {
            ((ToggleButton) control).selectedProperty().addListener((observable, _old, _new) -> {
                defaultValueChangeHandler().accept(control);
            });
        } else if (control instanceof ComboBox<?>) {
            ((ComboBox<?>) control).valueProperty().addListener((observable, _old, _new) -> {
                defaultValueChangeHandler().accept(control);
            });
        } else if (control instanceof Spinner) {
        	((Spinner<?>) control).valueProperty().addListener((observable, _old, _new) -> {
                defaultValueChangeHandler().accept(control);
        	});
        }
	}
}
