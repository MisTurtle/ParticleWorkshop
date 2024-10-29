package particleworkshop.common.structures.project;

import lombok.Data;

@Data
public class SimulationSettingsContainer {

	private float targetFPS = 60.0f;
	private boolean fullscreen = false;
	private float canvasWidth = 600;
	private float canvasHeight = 450;

}
