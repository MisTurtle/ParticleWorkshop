package particleworkshop.editor.widgets.inspector;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import particleworkshop.common.structures.items.spawners.SpawnerMode;

public class InspectorWidgetFactory {
	
	private static final double DEFAULT_CONTROL_WIDTH = 125; // px
	private static final double DEFAULT_V_CONTROL_WIDTH = 150; // px	
	private static final double DEFAULT_HORIZONTAL_SPACING = 10; // px
	
	private static Label createLabel(String label)
	{
		return new Label(label); // This might require css classes later so that's why this function exists
	}
	
	private static HBox labelControlHorizontally(String label, Region control)
	{
		Label labelItem = createLabel(label);
		
		HBox.setHgrow(labelItem, Priority.ALWAYS);
		labelItem.setMaxWidth(Double.MAX_VALUE);
		control.setPrefWidth(DEFAULT_CONTROL_WIDTH);
		
		HBox box = new HBox(DEFAULT_HORIZONTAL_SPACING, labelItem, control);
		box.setAlignment(Pos.CENTER_LEFT);
		
		return box;
	}
	
	private static VBox labelControlVertically(String label, Region control)
	{
		control.setPrefWidth(DEFAULT_CONTROL_WIDTH);
		VBox box = new VBox(createLabel(label), control);
		return box;
	}
	
	private static HBox packHorizontally(Region ...regions) {
		return new HBox(regions);
	}
	
	private static HBox packHorizontally(double spacing, Region ...regions) {
		return new HBox(spacing, regions);
	}
	
	private static VBox packVertically(Region ...regions) {
		return new VBox(regions);
	}
	
	private static VBox packVertically(double spacing, Region ...regions) {
		return new VBox(spacing, regions);
	}
	
	public static Separator newSeparator()
	{
		return new Separator();
	}
	
	public static TextField newTextField(SimpleStringProperty subject)
	{
		TextField tf = new TextField(subject.get());
		subject.bind(tf.textProperty());
		return tf;
	}

	public static HBox newTextField(String label, SimpleStringProperty subject)
	{
		return labelControlHorizontally(label, newTextField(subject));
	}
	
	public static TextField newTextFieldWithRegex(SimpleStringProperty subject, String regex)
	{
		TextField tf = newTextField(subject);
		tf.textProperty().addListener((observer, oldV, newV) -> {
			if(!newV.matches(regex)) tf.setText(oldV);
		});
		return tf;
	}
	
	public static HBox newTextFieldWithRegex(String label, SimpleStringProperty subject, String regex)
	{
		return labelControlHorizontally(label, newTextFieldWithRegex(subject, regex));
	}
	
	public static TextField newFloatInput(SimpleFloatProperty subject)
	{
		String regex = "^[+-]?(\\d+(\\.\\d*)?|\\.\\d+)$";
		TextField tf = new TextField(String.valueOf(subject.get()));
		tf.textProperty().addListener((observer, oldV, newV) -> {
			if(newV.isEmpty()) { newV = "0"; tf.setText(newV); }
			if(!newV.matches(regex)) tf.setText(oldV);
			else subject.set(Float.parseFloat(newV));
		});
		return tf;
	}

	public static HBox newFloatInput(String label, SimpleFloatProperty subject)
	{
		return labelControlHorizontally(label, newFloatInput(subject));
	}
	
	public static HBox newSpinner(String label, SimpleIntegerProperty subject, int min, int max)
	{
		Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, subject.get()));
        spinner.valueProperty().addListener((observer, oldV, newV) -> subject.set(newV));
		return labelControlHorizontally(label, spinner);
	}
	
	public static <T extends Enum<T>> HBox newChoiceBox(String label, SimpleObjectProperty<T> subject, Class<T> enumType)
	{
		ChoiceBox<T> cb = new ChoiceBox<T>();
		cb.getItems().setAll(enumType.getEnumConstants());
		cb.setValue(subject.get());
		subject.bind(cb.valueProperty());
		return labelControlHorizontally(label, cb);
	}
	
	public static <T> VBox newList(String label, SimpleListProperty<T> subject, Function<T, Control> itemControlGenerator, Supplier<T> defaultItemSupplier)
	{
		VBox items = new VBox(3);
		items.getStyleClass().add("item-inspector-list");
		
		Consumer<T> addItemToList = item -> {
			Button btn = new Button("-"); btn.getStyleClass().add("list-del-button");
			HBox itemHbox = packHorizontally(5, itemControlGenerator.apply(item), btn);
			itemHbox.setPrefWidth(DEFAULT_V_CONTROL_WIDTH); itemHbox.setMinWidth(DEFAULT_V_CONTROL_WIDTH); itemHbox.setMaxWidth(DEFAULT_V_CONTROL_WIDTH);
			btn.setOnAction(evt -> { subject.remove(item); items.getChildren().remove(itemHbox); }); // Assign a button to delete that same entry
			items.getChildren().add(itemHbox);	
		};
		
		// Create a new item handle for each entry in subject
		for(T item: new ArrayList<>(subject)) { addItemToList.accept(item); }

		// Adding a new item means adding a new entry to subject and creating a new control
		Button addButton = new Button("+"); HBox.setHgrow(addButton, Priority.ALWAYS); addButton.setPrefWidth(DEFAULT_V_CONTROL_WIDTH);
		addButton.getStyleClass().add("list-add-button");
		addButton.setOnAction(evt -> {
			T newItem = defaultItemSupplier.get();
			subject.add(newItem);
			addItemToList.accept(newItem);
		});
		
		HBox addButtonHbox = new HBox(addButton); addButtonHbox.setPrefWidth(DEFAULT_V_CONTROL_WIDTH); addButtonHbox.setAlignment(Pos.CENTER_RIGHT);
		return labelControlVertically(label, packVertically(5, items, addButtonHbox));
	}
}
