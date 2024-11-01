package particleworkshop.editor.widgets.inspector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import particleworkshop.common.exception.MissingAnnotationException;
import particleworkshop.common.exception.ObjectSerializationException;
import particleworkshop.common.exception.UnknownDefaultInputException;
import particleworkshop.common.utils.color.ColorPropertyWrapper;
import particleworkshop.editor.widgets.EditorItemInspector;
import particleworkshop.editor.widgets.inspector.annotations.AsSlider;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;
import particleworkshop.editor.widgets.inspector.annotations.HorizontalBlock;
import particleworkshop.editor.widgets.inspector.annotations.ListData;
import particleworkshop.editor.widgets.inspector.annotations.TypeEnum;
import particleworkshop.editor.widgets.inspector.annotations.UseFloatRange;
import particleworkshop.editor.widgets.inspector.annotations.UseIntRange;
import particleworkshop.editor.widgets.inspector.annotations.UseRegex;

public class DefaultWidgetFactory implements IWidgetFactory
{
	
	private static DefaultWidgetFactory _instance = null;
	
	public static DefaultWidgetFactory getInstance(EditorItemInspector inspector)
	{
		if(_instance == null) _instance = new DefaultWidgetFactory(inspector);
		return _instance;
	}
	
	private EditorItemInspector _inspector;
	
	public DefaultWidgetFactory(EditorItemInspector inspector)
	{
		_inspector = inspector;
	}

	@Override
	public HBox labelHorizontally(String label, Region target) {
		return labelHorizontally(new Label(label), target);
	}

	@Override
	public HBox labelHorizontally(Label label, Region target) {
		// Unify the width of controls by forcing the label to grow over the extra space
		HBox.setHgrow(label, Priority.ALWAYS); label.setMaxWidth(Double.MAX_VALUE);
		target.setPrefWidth(DEFAULT_CONTROL_WIDTH);
		
		// Align items to the left
		HBox box = new HBox(DEFAULT_HORIZONTAL_SPACING, label, target);
		box.setAlignment(Pos.CENTER_LEFT);
		
		return box;
	}

	@Override
	public VBox labelVertically(String label, Region target) {
		return labelVertically(new Label(label), target);
	}

	@Override
	public VBox labelVertically(Label label, Region target) {
		// For visual consistency, also for the controls to fit into a smaller space than available
		target.setPrefWidth(DEFAULT_V_CONTROL_WIDTH);
		return new VBox(DEFAULT_VERTICAL_SPACING, label, target);
	}

	@Override
	public HBox packHorizontally(Region... targets) {
		return new HBox(DEFAULT_HORIZONTAL_SPACING, targets);
	}

	@Override
	public HBox packHorizontally(double spacing, Region... targets) {
		return new HBox(spacing, targets);
	}

	@Override
	public VBox packVertically(Region... targets) {
		return new VBox(DEFAULT_VERTICAL_SPACING, targets);
	}

	@Override
	public VBox packVertically(double spacing, Region... targets) {
		return new VBox(spacing, targets);
	}
	
	@Override
	public CheckBox booleanInput(SimpleBooleanProperty subject) {
		CheckBox cb = new CheckBox();
		cb.setSelected(subject.get());
		subject.bind(cb.selectedProperty());
		return cb;
	}
	
	@Override
	public TextField textInput(SimpleStringProperty subject) {
		TextField tf = new TextField(subject.get());
		subject.bind(tf.textProperty());
		return tf;
	}

	@Override
	public TextField textInput(SimpleStringProperty subject, String regex) {
		TextField tf = textInput(subject);
		tf.textProperty().addListener((observer, oldV, newV) -> {
			if(!newV.isBlank() && !newV.matches(regex)) tf.setText(oldV);
		});
		return tf;
	}

	@Override
	public TextField numberInput(SimpleFloatProperty subject) {
		return numberInput(subject, -Float.MAX_VALUE, Float.MAX_VALUE);
	}

	@Override
	public TextField numberInput(SimpleFloatProperty subject, float min, float max) {
		String regex = "^[+-]?(\\d+(\\.\\d*)?|\\.\\d+|\\d*\\.\\d+|-|.)([eE][+-]?\\d*)?$";
		TextField tf = new TextField(String.valueOf(subject.get()));
		tf.textProperty().addListener((observer, oldV, newV) -> {
			newV = newV.strip();
			if(!newV.isEmpty() && !newV.matches(regex)) { tf.setText(oldV); return; }
			if(newV.isEmpty() || newV.equals("-") || newV.equals(".") || newV.endsWith("e") || newV.endsWith("E") || newV.endsWith("e-") || newV.endsWith("E-")) newV += "0";
			float fVal = Float.valueOf(newV);
			
			if(fVal < min) { fVal = min; tf.setText(String.valueOf(min)); }
			else if(fVal > max) { fVal = max; tf.setText(String.valueOf(max)); }
			
			subject.set(fVal);
		});
		return tf;
	}

	@Override
	public Slider numberSlider(SimpleFloatProperty subject, float min, float max, float majorStep, boolean snap) {
		Slider s = new Slider(min, max, subject.get());
		s.setMajorTickUnit(majorStep); s.setMinorTickCount(0);
		s.setSnapToTicks(snap);
		subject.bind(s.valueProperty());
		return s;
	}

	@Override
	public Spinner<Float> numberSpinner(SimpleFloatProperty subject, float min, float max, float step) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextField numberInput(SimpleIntegerProperty subject) {
		return numberInput(subject, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public TextField numberInput(SimpleIntegerProperty subject, int min, int max) {
		String regex = "^[+-]?(\\d+|-)?$";
		TextField tf = new TextField(String.valueOf(subject.get()));
		tf.textProperty().addListener((observer, oldV, newV) -> {
			newV = newV.strip();
			if(!newV.isEmpty() && !newV.matches(regex)) { tf.setText(oldV); return; }
			if(newV.isEmpty() || newV.equals("-")) newV += "0";
			
			BigInteger val = new BigInteger(newV);
			int clampedResult;
			if(val.compareTo(BigInteger.valueOf(min)) < 0) { tf.setText(String.valueOf(min)); clampedResult = min; }
			else if(val.compareTo(BigInteger.valueOf(max)) > 0) { tf.setText(String.valueOf(max)); clampedResult = max; }
			else clampedResult = val.intValue();
			
			subject.set(clampedResult);
		});
		return tf;
	}

	@Override
	public Slider numberSlider(SimpleIntegerProperty subject, int min, int max, int majorStep, boolean snap) {
		Slider s = new Slider(min, max, subject.get());
		s.setMajorTickUnit(majorStep); s.setMinorTickCount(0);
		s.setSnapToTicks(snap);
		subject.bind(s.valueProperty());
		return s;
	}

	@Override
	public Spinner<Integer> numberSpinner(SimpleIntegerProperty subject, int min, int max, int step) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public <T extends Enum<T>> ChoiceBox<T> enumInput(SimpleObjectProperty<T> subject, Class<T> enumType) {
		ChoiceBox<T> cb = new ChoiceBox<T>();
		cb.getItems().setAll(enumType.getEnumConstants());
		cb.setValue(subject.get());
		subject.bind(cb.valueProperty());
		return cb;
	}
	
	@Override
	public ColorPicker colorInput(SimpleObjectProperty<ColorPropertyWrapper> subject) {
		ColorPicker cp = new ColorPicker(subject.get().getColor());
		cp.valueProperty().addListener((observer, oldV, newV) -> subject.set(new ColorPropertyWrapper(newV)));
		return cp;
	}
	
	@Override
	public <T> VBox listInput(SimpleListProperty<T> subject, Class<T> itemType) {
		VBox output = new VBox(DEFAULT_VERTICAL_SPACING);
		Button addButton = new Button("+"); addButton.getStyleClass().add("list-add-button");
		Consumer<T> createRow = itemVal -> {
			Button delButton = new Button("-"); delButton.getStyleClass().add("list-del-button");
			Region input;
			try {
				input = this.createDefaultInputFor(itemVal);
			} catch (UnknownDefaultInputException e) {
				e.printStackTrace();
				return;
			}
			HBox row = packHorizontally(input, delButton);
			row.setAlignment(Pos.CENTER_RIGHT);
			delButton.setOnAction(delEvt -> output.getChildren().remove(row));
			output.getChildren().add(output.getChildren().size() - 1, row);
		};
		
		addButton.setOnAction(addEvt -> {
			try {
				T newItem = itemType.getConstructor().newInstance();
				createRow.accept(newItem);
				subject.add(newItem);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		});
		HBox addButtonHBox = new HBox(addButton);
		HBox.setHgrow(addButton, Priority.ALWAYS);
		addButton.setPrefWidth(DEFAULT_V_CONTROL_WIDTH);
		addButtonHBox.setPrefWidth(DEFAULT_V_CONTROL_WIDTH);
		addButtonHBox.setAlignment(Pos.CENTER_RIGHT);
		
		output.getChildren().add(addButtonHBox);
		for(T item: subject)
			createRow.accept(item);
		
		return output;
	}

	@Override
	public Separator separator() {
		return new Separator();
	}
	
	@Override
	public List<Region> createWidgetsFor(Object obj) throws ObjectSerializationException
	{
		/**
		 * Loop through every field declared by the object's class and subclasses
		 * Generate a widget for each of those fields, taking annotations into account as control parameters
		 */
		List<Region> regions = new ArrayList<Region>();
		Class<?> objClass = obj.getClass();


		HorizontalBlock hBlock = null;
		int separatorCount = 0;
		List<Region> horizontalStackingRegions = new ArrayList<Region>();
		
		while(objClass != null)
		{
			List<Region> classRegions = new ArrayList<Region>();
			for(Field field: objClass.getDeclaredFields())
			{
				field.setAccessible(true);
				// Check whether the field wants to be modified from the editor
				try {
					if(field.getAnnotation(Controlled.class) == null) continue;
					
					if(hBlock == null) hBlock = field.getAnnotation(HorizontalBlock.class);
					particleworkshop.editor.widgets.inspector.annotations.Separator separatorProp = field.getAnnotation(particleworkshop.editor.widgets.inspector.annotations.Separator.class);
					
					// Add a separator before the element
					if(separatorProp != null && separatorProp.before()) { horizontalStackingRegions.add(separator()); ++separatorCount; }	
					
					// Create the widget
					Region output = createWidgetFor(obj, field);
					if(output == null) continue;
					
					// Add this widget to the horizontal stack
					horizontalStackingRegions.add(output);
					
					// Check if the horizontal block should be flushed
					int blockCount = horizontalStackingRegions.size() - separatorCount;
					
					// Verify if we've reached the end of the horizontal block
					if(hBlock == null || blockCount >= hBlock.size())
					{
						if(hBlock == null) classRegions.addAll(horizontalStackingRegions);
						else if(blockCount >= hBlock.size()) 
						{
							if(hBlock.label().isBlank()) classRegions.add(packHorizontally(horizontalStackingRegions.toArray(Region[]::new)));
							else classRegions.add(labelHorizontally(hBlock.label(), packHorizontally(horizontalStackingRegions.toArray(Region[]::new))));
						}
						horizontalStackingRegions.clear(); separatorCount = 0; hBlock = null;
					}
					
					// Add a separator after this widget if requested
					if(separatorProp != null && separatorProp.after() && (!separatorProp.before() || output != null)) { classRegions.add(separator()); }
					
				}catch(Exception e) {
					throw new ObjectSerializationException(e);
				}
			}
			objClass = objClass.getSuperclass();
			classRegions.addAll(regions);
			regions = classRegions;
		}
		
		return regions;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Region createWidgetFor(Object obj, Field field) throws IllegalArgumentException, IllegalAccessException, MissingAnnotationException, ObjectSerializationException
	{
		/**
		 * Create a widget to be added to the inspector for a given field
		 * The field is assumed to be accessible
		 */
		Class<?> fieldType = field.getType();
		Region output = null;
		
		// Find which type of control to create
		if(fieldType == SimpleBooleanProperty.class) output = booleanInput((SimpleBooleanProperty) field.get(obj));
		else if(fieldType == SimpleStringProperty.class) output = createTextField(obj, field); 
		else if(fieldType == SimpleFloatProperty.class) output = createFloatControl(obj, field);
		else if(fieldType == SimpleIntegerProperty.class) output = createIntegerControl(obj, field);
		else if(fieldType == SimpleListProperty.class) {
			ListData listData = field.getAnnotation(ListData.class);
			if(listData == null) throw new MissingAnnotationException("A serialized list property requires an annotation of type " + ListData.class.toString());
			output = listInput((SimpleListProperty) field.get(obj), listData.itemType());
		} else if(fieldType == SimpleObjectProperty.class) {
			SimpleObjectProperty<?> property = (SimpleObjectProperty<?>) field.get(obj);
			if(property.get() == null) return null;
			
			Class<?> pClass = property.get().getClass();
			if(pClass.isEnum()) output = createEnumChoice(obj, field, (Class) pClass);
			else if(pClass == ColorPropertyWrapper.class) output = colorInput((SimpleObjectProperty<ColorPropertyWrapper>) property);
			else{ // Create a retractable titled pane object that holds the object's data
				output = new TitledPane();
				((TitledPane) output).setContent(new VBox(DEFAULT_VERTICAL_SPACING, createWidgetsFor(property.get()).toArray(new Region[0])));
			}
		} else return null;
		
		
		// Label this generated control
		Controlled controlInfo = field.getAnnotation(Controlled.class);
		if(!controlInfo.label().isBlank()) 
		{
			if(output instanceof TitledPane) ((TitledPane) output).setText(controlInfo.label());
			else if(controlInfo.stackVertically()) return labelVertically(controlInfo.label(), output);
			else return labelHorizontally(controlInfo.label(), output);
		}
		
		return output;
	}
	
	public <T> Region createDefaultInputFor(T property) throws UnknownDefaultInputException
	{
		if(property.getClass() == SimpleBooleanProperty.class) return booleanInput((SimpleBooleanProperty) property);
		else if(property.getClass() == SimpleStringProperty.class) return textInput((SimpleStringProperty) property); 
		else if(property.getClass() == SimpleFloatProperty.class) return numberInput((SimpleFloatProperty) property);
		else if(property.getClass() == SimpleIntegerProperty.class) return numberInput((SimpleIntegerProperty) property);
		else throw new UnknownDefaultInputException("No default input set for property of type " + property.getClass().toString());
	}

	private Region createFloatControl(Object obj, Field field) throws MissingAnnotationException, IllegalArgumentException, IllegalAccessException
	{
		UseFloatRange fieldRange = field.getAnnotation(UseFloatRange.class);
		AsSlider asSlider = field.getAnnotation(AsSlider.class);
		
		if(fieldRange == null && asSlider != null) throw new MissingAnnotationException("A slider float control requires a " + UseFloatRange.class.toString() + " annotation.");
		if(asSlider != null)
		{
			Slider s = numberSlider((SimpleFloatProperty) field.get(obj), fieldRange.min(), fieldRange.max(), asSlider.majorStepSize(), asSlider.snap());
			Label l = new Label(String.valueOf(s.getValue()));
			s.valueProperty().addListener((observer, oldV, newV) -> {
				double vDouble = newV.doubleValue();
				
				int tickId = (int) ((vDouble - s.getMin()) / s.getMajorTickUnit());
				double next = (tickId + 1) * s.getMajorTickUnit() + s.getMin();
				double prev = tickId * s.getMajorTickUnit() + s.getMin();
				double snapped = vDouble - prev > next - vDouble ? next : prev;
				
				l.setText(String.format("%s%." + String.valueOf(asSlider.displayPrecision()) + "f", snapped < 0 ? "-" : " ", Math.abs(snapped)));
			});
			return packVertically(0, l, s);
		}
		else if(fieldRange != null) return numberInput((SimpleFloatProperty) field.get(obj), fieldRange.min(), fieldRange.max());
		else return numberInput((SimpleFloatProperty) field.get(obj));
	}

	
	private Region createIntegerControl(Object obj, Field field) throws MissingAnnotationException, IllegalArgumentException, IllegalAccessException
	{
		UseIntRange fieldRange = field.getAnnotation(UseIntRange.class);
		AsSlider asSlider = field.getAnnotation(AsSlider.class);
		
		if(fieldRange == null && asSlider != null) throw new MissingAnnotationException("A slider integer control requires a " + UseIntRange.class.toString() + " annotation.");
		if(asSlider != null)
		{
			Slider s = numberSlider((SimpleIntegerProperty) field.get(obj), fieldRange.min(), fieldRange.max(), (int) asSlider.majorStepSize(), asSlider.snap());
			Label l = new Label(String.valueOf(s.getValue()));
			s.valueProperty().addListener((observer, oldV, newV) -> {
				double vDouble = newV.doubleValue();
				
				int tickId = (int) ((vDouble - s.getMin()) / s.getMajorTickUnit());
				int next = (int) ((tickId + 1) * s.getMajorTickUnit() + s.getMin());
				int prev = (int) (tickId * s.getMajorTickUnit() + s.getMin());
				int snapped = vDouble - prev > next - vDouble ? next : prev;
				
				l.setText(String.valueOf(snapped));
			});
			return packVertically(0, l, s);
		}
		else if(fieldRange != null) return numberInput((SimpleIntegerProperty) field.get(obj), fieldRange.min(), fieldRange.max());
		else return numberInput((SimpleIntegerProperty) field.get(obj));
	}

	private Region createTextField(Object obj, Field field) throws MissingAnnotationException, IllegalArgumentException, IllegalAccessException
	{
		UseRegex regex = field.getAnnotation(UseRegex.class);
		if(regex == null) return textInput((SimpleStringProperty) field.get(obj));
		else return textInput((SimpleStringProperty) field.get(obj), regex.regex());
	}
	
	private <T extends Enum<T>> Region createEnumChoice(Object obj, Field field, Class<T> enumType) throws IllegalArgumentException, IllegalAccessException
	{
		@SuppressWarnings("unchecked")
		SimpleObjectProperty<T> property = (SimpleObjectProperty<T>) field.get(obj);
		TypeEnum isTypeEnum = field.getAnnotation(TypeEnum.class);
		ChoiceBox<T> control = enumInput(property, enumType);
		if(isTypeEnum != null) 
		{
			control.valueProperty().addListener((observer, oldV, newV) -> {
				_inspector.reload(); // TODO : I feel like it's weird to have that check performed here, a better place should be found
			});
		}
		return control;
	}

}
