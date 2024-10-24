package particleworkshop.editor.widgets;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import particleworkshop.editor.EditorContext;
import particleworkshop.editor.item.EditorItemBase;

public class EditorItemInspector extends VBox implements IEditorWidget
{
	private EditorContext _context;

	public EditorItemInspector(EditorContext context)
	{
		super();
		_context = context;
		
		getStyleClass().add("item-inspector");
		getContext().addPropertyChangeListener(evt -> {
			if(evt.getPropertyName().equals(context.EVT_SELECTED_ITEM))
			{
				EditorItemBase<?> newItem = (EditorItemBase<?>) evt.getNewValue();
				if(newItem == null) getChildren().clear();
				else {
					ArrayList<Region> controls = newItem.generateControls();
					for(Region c: controls)
						bindValueChangeListener(c);
					getChildren().setAll(controls);
				}
			} else if(evt.getPropertyName().equals(context.EVT_PROJECT_CHANGE) && evt.getNewValue() == null)
			{
				getChildren().clear();
			}
		});
	}
	
	@Override
	public EditorContext getContext() {
		return _context;
	}
	
	private void bindValueChangeListener(Parent parent)
	{
		for(Node child: parent.getChildrenUnmodifiable())
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
                getContext().setUnsaved();  // Your action for TextField
            });
        } else if (control instanceof CheckBox) {
            ((CheckBox) control).selectedProperty().addListener((observable, _old, _new) -> {
                getContext().setUnsaved();  // Your action for CheckBox
            });
        } else if (control instanceof Slider) {
            ((Slider) control).valueProperty().addListener((observable, _old, _new) -> {
                getContext().setUnsaved();  // Your action for Slider
            });
        } else if (control instanceof ToggleButton) {
            ((ToggleButton) control).selectedProperty().addListener((observable, _old, _new) -> {
                getContext().setUnsaved();  // Your action for ToggleButton
            });
        } else if (control instanceof ComboBox<?>) {
            ((ComboBox<?>) control).valueProperty().addListener((observable, _old, _new) -> {
                getContext().setUnsaved();  // Your action for ComboBox
            });
        } else if (control instanceof Spinner) {
        	((Spinner<?>) control).valueProperty().addListener((observable, _old, _new) -> {
        		getContext().setUnsaved();
        	});
        }
	}
}
