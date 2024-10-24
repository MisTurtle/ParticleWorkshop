package particleworkshop.common.structures.project;

import lombok.Data;

@Data
public class SimulationSettingsContainer {

	private float targetFPS = 60.0f;
	private boolean fullscreen = false;

}
