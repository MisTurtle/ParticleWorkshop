package particleworkshop.editor.item.list;

import java.util.ArrayList;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.layout.Region;
import particleworkshop.common.structures.entities.EntityModel;
import particleworkshop.common.structures.items.spawners.EntitySpawnerItem;
import particleworkshop.common.structures.items.spawners.SpawnerMode;
import particleworkshop.editor.item.EditorItemBase;
import particleworkshop.editor.widgets.inspector.InspectorWidgetFactory;

public class EditorSpawnerItem extends EditorItemBase<EntitySpawnerItem> 
{
	private SimpleObjectProperty<SpawnerMode> mode;
	private SimpleFloatProperty frequency;
	private SimpleListProperty<SimpleFloatProperty> directions;
	private SimpleObjectProperty<EntityModel> model;
	
	public EditorSpawnerItem()
	{
		this(new EntitySpawnerItem());
	}

	public EditorSpawnerItem(EntitySpawnerItem item) {
		super(item.getIdentifier());
		mode = new SimpleObjectProperty<>(item.getMode());
		frequency = new SimpleFloatProperty(item.getFrequency());
		directions = new SimpleListProperty<SimpleFloatProperty>(FXCollections.observableArrayList());
		for(Float dir: item.getDirections())
			directions.getValue().add(new SimpleFloatProperty(dir));
		model = new SimpleObjectProperty<>(item.getModel());
	}

	@Override
	public ArrayList<Region> generateControls() {
		ArrayList<Region> controls = super.generateControls();
		
		controls.add(InspectorWidgetFactory.newChoiceBox("Mode", mode, SpawnerMode.class));
		controls.add(InspectorWidgetFactory.newFloatInput("Frequency", frequency));
		controls.add(InspectorWidgetFactory.newList("Directions", directions, InspectorWidgetFactory::newFloatInput, () -> {
			return new SimpleFloatProperty(0.f);
		}));
		// TODO : List Control => Pass in the property, a function to call when click the "+" button (Which returns a new default instance), and a handler when clicking the "-" button
		
		return controls;
	}
	
	@Override
	public EntitySpawnerItem asStructure() {
		EntitySpawnerItem struct = new EntitySpawnerItem();
		
		struct.setIdentifier(getIdentifier());
		struct.setMode(mode.get());
		struct.setFrequency(frequency.get());
		struct.setDirections(directions.stream().map(floatProperty -> floatProperty.get()).toList());
		struct.setModel(model.get());
		
		return struct;
	}


}
