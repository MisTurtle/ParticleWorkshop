package particleworkshop.editor.widgets;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import particleworkshop.common.structures.Vector2;
import particleworkshop.editor.EditorContext;
import particleworkshop.editor.SimulationSettingsAdapter;
import particleworkshop.editor.item.EditorItemBase;

public class EditorProjectDisplay extends StackPane implements IEditorWidget {
	
	private final Color DISPLAY_BACKGROUND_COLOR = Color.rgb(0x35, 0x35, 0x35);
	private final float MIN_ZOOM_FACTOR = .05f;
	private final float MAX_ZOOM_FACTOR = 5.f;
	private final float ZOOM_FACTOR = .8f;

	private EditorContext _context;
	private Canvas _simScreen = new Canvas();
	private GraphicsContext _ctx = _simScreen.getGraphicsContext2D();
	
	// Keep track of which group belongs to which object (maps an editor object's UID to a group object)
	private Map<Long, Group> uidToGroupMap = new HashMap<>();
	
	// Mouse position tracking relative to the editor
	private double _cursorX = .0;
	private double _cursorY = .0;
	// Mouse dragging operation origin and previous step for delta computation
	private double _dragOriginX = .0;
	private double _dragOriginY = .0;
	
	// Size and coordinates of the simulation canvas
	private SimpleFloatProperty _canvasWidth = new SimpleFloatProperty(.0f);
	private SimpleFloatProperty _canvasHeight = new SimpleFloatProperty(.0f);
	// Top left corner of the actual canvas ( <=> (0, 0) for simulation items)
	private SimpleFloatProperty _originX = new SimpleFloatProperty(.0f);
	private SimpleFloatProperty _originY = new SimpleFloatProperty(.0f);
	private SimpleFloatProperty _zoomFactor = new SimpleFloatProperty(1.f);
	
	public EditorProjectDisplay(EditorContext context)
	{
		_context = context;
		context.addPropertyChangeListener(evt -> {
			if(evt.getPropertyName().equals(EVT_PROJECT_CHANGE)) {
				setupProject();
			}else if(evt.getPropertyName().equals(EVT_ITEM_CREATED)) {
				addItem((EditorItemBase<?>) evt.getNewValue());
			}else if(evt.getPropertyName().equals(EVT_ITEM_DELETED)) {
				deleteItem((EditorItemBase<?>) evt.getNewValue());
			}else if(evt.getPropertyName().equals(EVT_ITEM_CHANGED)) {
				updateItem((EditorItemBase<?>) evt.getNewValue());
			}else if(evt.getPropertyName().equals(EVT_SIM_SETTINGS_CHANGED)) {
				drawCanvas();
			}
		});
		initialSetup();
	}
	
	private Vector2 toEditorCoordinates(double x, double y) {
		return toEditorCoordinates(new Vector2(x, y));
	}
	private Vector2 toEditorCoordinates(Vector2 canvasCoordinates) {
		toEditorSize(canvasCoordinates);
		canvasCoordinates.add(_originX.get(), _originY.get());
		return canvasCoordinates;
	}
	private Vector2 toEditorSize(double x, double y) {
		return toEditorSize(new Vector2(x, y));
	}
	private Vector2 toEditorSize(Vector2 canvasSize) {
		canvasSize.add(canvasSize.copy().multiply(_zoomFactor.get() - 1));
		return canvasSize;
	}
	
	private void updateDragOrigin(double x, double y)
	{
		_dragOriginX = x;
		_dragOriginY = y;
	}
	
	private void updateCursorPosition(double x, double y)
	{
		_cursorX = x;
		_cursorY = y;
	}
	
	private void initialSetup()
	{
		// This is only performed once when the instance is created
		this.setOnScroll(evt -> {
			float target = evt.getDeltaY() > 0 ? (_zoomFactor.get() / ZOOM_FACTOR) : (_zoomFactor.get() * ZOOM_FACTOR);
			target = Math.round(target * 1000.f) / 1000.f;
			
			_zoomFactor.set(Math.clamp(target, MIN_ZOOM_FACTOR, MAX_ZOOM_FACTOR));
		}); // Scrolling => Zooming in or out of the canvas
		this.setOnMouseMoved(evt -> updateCursorPosition(evt.getX(), evt.getY()));
		this.setOnMousePressed(evt -> updateDragOrigin(evt.getX(), evt.getY()));
		this.setOnMouseDragged(evt -> {
			if(evt.getButton() != MouseButton.PRIMARY) return; 
			// Dragging the mouse changes the canvas origin of position
			_originX.set(_originX.get() + (float) (evt.getX() - _dragOriginX));
			_originY.set(_originY.get() + (float) (evt.getY() - _dragOriginY));
			updateDragOrigin(evt.getX(), evt.getY());
			updateAll();
		});
		
		// Redraw the scene when something about the canvas changes (zoom, position, ...)
		_zoomFactor.addListener((observer, oldV, newV) -> {
			// Move the canvas by some amount to zoom towards where the cursor is
			double zf = newV.doubleValue() / oldV.doubleValue();
			Vector2 canvasSize = toEditorSize(_canvasWidth.get(), _canvasHeight.get());
			Vector2 ratio = new Vector2((_cursorX - _originX.get()) / canvasSize.getX(), (_cursorY - _originY.get()) / canvasSize.getY());
			_originX.set((float) (_originX.get() - ratio.getX() * canvasSize.getX() * (zf - 1)));
			_originY.set((float) (_originY.get() - ratio.getY() * canvasSize.getY() * (zf - 1)));
			updateAll();
		});
		
		// Setup canvas real size and height (Screen width and height).
		_simScreen.setManaged(false); // This is required so that the user can resize the panes however they want, and to be able not to show the whole canvas at once
		_simScreen.widthProperty().bind(widthProperty());
		_simScreen.heightProperty().bind(heightProperty());
		_simScreen.widthProperty().addListener((observer, oldV, newV) -> {
			drawCanvas();
			System.out.printf("New width : %f\n", newV);
		});
		_simScreen.heightProperty().addListener((observer, oldV, newV) -> {
			drawCanvas();
			System.out.printf("New height : %f\n", newV);
		});
	}
	
	private void setupProject()
	{
		// Setup bindings for a new project
		uidToGroupMap.clear();
		getChildren().clear();
		SimulationSettingsAdapter s = getContext().getProjectSettings();
		
		if(s == null) return;
		
		_canvasWidth.bind(s.getCanvasWidth());
		_canvasHeight.bind(s.getCanvasHeight());
		_originX.set(0);
		_originY.set(0);
		_zoomFactor.set(1.f);
		
		getChildren().add(_simScreen);
		getContext().getEditorItems().forEach(i -> addItem(i));
	}
	
	private void bindGroupHandles(EditorItemBase<?> owner, Group g)
	{
		g.setOnMouseClicked(evt -> _context.selectItem(owner));
		g.setOnMouseEntered(evt -> _context.getStage().getScene().setCursor(Cursor.MOVE));
		g.setOnMouseExited(evt -> _context.getStage().getScene().setCursor(Cursor.DEFAULT));
	}
	
	private void applyGroupTransforms(EditorItemBase<?> owner, Group g)
	{
		g.setManaged(false);
		Vector2 editorPos = this.toEditorCoordinates(owner.getPosition().copy());
		g.setTranslateX(editorPos.getX());
		g.setTranslateY(editorPos.getY());
		g.setScaleX(_zoomFactor.get());
		g.setScaleY(_zoomFactor.get());
	}
	
	private void addItem(EditorItemBase<?> item)
	{
		if(uidToGroupMap.containsKey(item.getUID())) return; // Already added for some reason (shouldn't happen)
		
		Group g = item.render();
		bindGroupHandles(item, g);
		applyGroupTransforms(item, g);
		
		uidToGroupMap.put(item.getUID(), g);
		getChildren().add(g);
	}
	
	private void updateItem(EditorItemBase<?> item)
	{
		if(!uidToGroupMap.containsKey(item.getUID())) return;
		getChildren().remove(uidToGroupMap.get(item.getUID()));
		
		Group g = item.render();
		bindGroupHandles(item, g);
		applyGroupTransforms(item, g);
		
		uidToGroupMap.put(item.getUID(), g);
		getChildren().add(g);
	}
	
	private void deleteItem(EditorItemBase<?> item)
	{
		if(!uidToGroupMap.containsKey(item.getUID())) return;
		
		getChildren().remove(uidToGroupMap.get(item.getUID()));
	}
	
	private void drawCanvas()
	{	
		Vector2 cOrigin = toEditorCoordinates(0, 0);
		Vector2 cEnd = toEditorCoordinates(_canvasWidth.get(), _canvasHeight.get());
		Vector2 gridSpacing = toEditorSize(100, 100);
		
		if(gridSpacing.getX() <= 2) gridSpacing.setX(2);
		if(gridSpacing.getY() <= 2) gridSpacing.setY(2);
		
		// Draw background
		_ctx.setFill(DISPLAY_BACKGROUND_COLOR);
		_ctx.fillRect(0, 0, _simScreen.getWidth(), _simScreen.getHeight());
		
		// Draw simulation viewport
		_ctx.setFill(Color.GRAY);
		_ctx.fillRect(cOrigin.getX(), cOrigin.getY(), cEnd.getX() - cOrigin.getX(), cEnd.getY() - cOrigin.getY());
		
		// Draw grid
		_ctx.setFill(Color.WHITE);
		for(double x = cOrigin.getX(); x < cEnd.getX(); x += gridSpacing.getX())
			_ctx.fillRect(x, cOrigin.getY(), 1, cEnd.getY() - cOrigin.getY());
		for(double y = cOrigin.getY(); y < cEnd.getY(); y += gridSpacing.getY())
			_ctx.fillRect(cOrigin.getX(), y, cEnd.getX() - cOrigin.getX(), 1);
	}
	
	private void updateAll()
	{
		drawCanvas();
		for(EditorItemBase<?> item: getContext().getEditorItems())
		{
			Group g = uidToGroupMap.get(item.getUID());
			applyGroupTransforms(item, g);
		}
	}
	
	@Override
	public EditorContext getContext() {
		return _context;
	}
	
	public void resetZoom()
	{
		_zoomFactor.set(1.0f);
	}
	
}
