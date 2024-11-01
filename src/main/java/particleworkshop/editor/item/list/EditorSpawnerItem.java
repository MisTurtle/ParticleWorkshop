package particleworkshop.editor.item.list;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import particleworkshop.common.structures.entities.BoidsEntityModel;
import particleworkshop.common.structures.entities.EntityModel;
import particleworkshop.common.structures.entities.EntityType;
import particleworkshop.common.structures.entities.FlowingParticleModel;
import particleworkshop.common.structures.entities.ThermalParticleModel;
import particleworkshop.common.structures.items.spawners.EntitySpawnerItem;
import particleworkshop.common.structures.items.spawners.SpawnerMode;
import particleworkshop.editor.item.EditorItemBase;
import particleworkshop.editor.widgets.inspector.annotations.AsSlider;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;
import particleworkshop.editor.widgets.inspector.annotations.ListData;
import particleworkshop.editor.widgets.inspector.annotations.Separator;
import particleworkshop.editor.widgets.inspector.annotations.TypeEnum;
import particleworkshop.editor.widgets.inspector.annotations.UseFloatRange;

public class EditorSpawnerItem extends EditorItemBase<EntitySpawnerItem> {
	
	@Controlled(label = "Spawning Mode")
	private SimpleObjectProperty<SpawnerMode> mode;

	@Controlled(label = "Frequency")
	@AsSlider(majorStepSize = 0.1f, displayPrecision = 1)
	@UseFloatRange(min = 0.1f, max = 10.f)
	private SimpleFloatProperty frequency;
	
	@Controlled(label = "Spawning Radius")
	@AsSlider(majorStepSize = 0.1f, displayPrecision = 1)
	@UseFloatRange(min = 0f, max = 50.f)
	private SimpleFloatProperty radius;

	@Controlled(label = "Directions", stackVertically = true)
	@ListData(itemType = SimpleFloatProperty.class)
	private SimpleListProperty<SimpleFloatProperty> directions;

	@Separator(before = true, after = false)
	@Controlled(label = "Entity Model", stackVertically = true)
	private SimpleObjectProperty<EntityModel> model;

	@Controlled(label="Entity Type") @TypeEnum
	private SimpleObjectProperty<EntityType> type;

	public EditorSpawnerItem() {
		this(new EntitySpawnerItem());
	}

	public EditorSpawnerItem(EntitySpawnerItem item) {
		super(item.getIdentifier(), item.getPosition());
		mode = new SimpleObjectProperty<>(item.getMode());
		frequency = new SimpleFloatProperty(item.getFrequency());
		radius = new SimpleFloatProperty(item.getRadius());
		directions = new SimpleListProperty<SimpleFloatProperty>(FXCollections.observableArrayList());
		for (Float dir : item.getDirections())
			directions.getValue().add(new SimpleFloatProperty(dir));
		model = new SimpleObjectProperty<>(item.getModel() == null ? new FlowingParticleModel() : item.getModel());
		type = new SimpleObjectProperty<>(model.get().getType());
		type.addListener((observer, oldV, newV) -> {
			switch(newV)
			{
				case BoidsEntity: model.set(new BoidsEntityModel(model.get())); break;
				case FlowingParticle: model.set(new ThermalParticleModel(model.get())); break;
				case ThermalParticle: model.set(new FlowingParticleModel(model.get())); break;
			}
		});
	}

	@Override
	public EntitySpawnerItem asStructure() {
		EntitySpawnerItem struct = new EntitySpawnerItem();

		fillBaseStruct(struct);
		struct.setMode(mode.get());
		struct.setFrequency(frequency.get());
		struct.setRadius(radius.get());
		struct.setDirections(directions.stream().map(floatProperty -> floatProperty.get()).toList());
		struct.setModel(model.get());

		return struct;
	}

	@Override
	public Group render() {
		Group g = new Group();
		
		Circle core = new Circle(radius.get());
		core.setFill(Color.TRANSPARENT);
		core.setStroke(Color.RED);
		core.setStrokeWidth(3.f);
		core.getStrokeDashArray().addAll(10.0, 5.0);
		g.getChildren().addAll(core); // TODO : Direction lines
		
		return g;
	}

}
