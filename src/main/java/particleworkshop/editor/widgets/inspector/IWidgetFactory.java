package particleworkshop.editor.widgets.inspector;

import java.util.List;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import particleworkshop.common.exception.ObjectSerializationException;

public interface IWidgetFactory 
{
	HBox labelHorizontally(String label, Region target);
	HBox labelHorizontally(Label label, Region target);
	VBox labelVertically(String label, Region target);
	VBox labelVertically(Label label, Region target);
	
	HBox packHorizontally(Region ...targets);
	HBox packHorizontally(double spacing, Region ...targets);
	VBox packVertically(Region ...targets);
	VBox packVertically(double spacing, Region ...targets);
	
	TextField textInput(SimpleStringProperty subject);
	TextField textInput(SimpleStringProperty subject, String regex);
	
	TextField numberInput(SimpleFloatProperty subject);
	TextField numberInput(SimpleFloatProperty subject, float min, float max);
	Slider numberSlider(SimpleFloatProperty subject, float min, float max, float majorStep, boolean snap);
	Spinner<Float> numberSpinner(SimpleFloatProperty subject, float min, float max, float step);

	TextField numberInput(SimpleIntegerProperty subject);
	TextField numberInput(SimpleIntegerProperty subject, int min, int max);
	Slider numberSlider(SimpleIntegerProperty subject, int min, int max, int majorStep, boolean snap);
	Spinner<Integer> numberSpinner(SimpleIntegerProperty subject, int min, int max, int step);
	
	<T extends Enum<T>> ChoiceBox<T> enumInput(SimpleObjectProperty<T> subject, Class<T> enumType);

	Separator separator();
	
	List<Region> createWidgetsFor(Object obj) throws ObjectSerializationException;
}
