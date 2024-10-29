package particleworkshop.editor.widgets.inspector;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import particleworkshop.common.structures.project.SimulationSettingsContainer;
import particleworkshop.editor.widgets.inspector.annotations.Controlled;
import particleworkshop.editor.widgets.inspector.annotations.HorizontalBlock;
import particleworkshop.editor.widgets.inspector.annotations.UseFloatRange;

public class SimulationSettingsAdapter 
{
	@Controlled(label = "Target FPS")
	@UseFloatRange(min=10, max=Float.MAX_VALUE)
	private SimpleFloatProperty targetFps;
	@Controlled(label = "Play Fullscreen")
	private SimpleBooleanProperty fullscreen;
	@HorizontalBlock(size = 2, label = "Canvas Size")
	@Controlled(label = "X")
	@UseFloatRange(min=1, max=15360)
	private SimpleFloatProperty canvasWidth;
	@Controlled(label = "Y")
	@UseFloatRange(min=1, max=15360)
	private SimpleFloatProperty canvasHeight;
	
	public SimulationSettingsAdapter(SimulationSettingsContainer simulation)
	{
		targetFps = new SimpleFloatProperty(simulation.getTargetFPS());
		fullscreen = new SimpleBooleanProperty(simulation.isFullscreen());
		canvasWidth = new SimpleFloatProperty(simulation.getCanvasWidth());
		canvasHeight = new SimpleFloatProperty(simulation.getCanvasHeight());
	}
	
	public SimulationSettingsContainer asSettingsContainer()
	{
		SimulationSettingsContainer s = new SimulationSettingsContainer();
		s.setTargetFPS(targetFps.get());
		s.setFullscreen(fullscreen.get());
		s.setCanvasWidth(canvasWidth.get());
		s.setCanvasHeight(canvasHeight.get());
		return s;
	}
}
