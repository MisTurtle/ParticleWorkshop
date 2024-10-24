package particleworkshop.common.scene;

public class PWSceneManager  {
	
	private static volatile PWSceneManager _instance;
	
	private PWSceneManager() { }
	
	public static PWSceneManager getInstance()
	{
		if(_instance != null) return _instance;
		
		synchronized(PWSceneManager.class)
		{
			if(_instance == null) _instance = new PWSceneManager();
			return _instance;
		}
	}
	
	// I'm writing this as I'm still not sure how Java FX works
	// Ideally this should either be the editor scene (with ProjectSelection / Settings being modal windows) or ParticleSim (being its own stand alone process)
	private PWScene _activeScene;
	
	public void setActiveScene(PWScene scene)
	{
		if(_activeScene != null) _activeScene.onSetInactive();
		_activeScene = scene;
		_activeScene.onSetActive();
		_activeScene.getStage().setScene(_activeScene);
	}
	
	public PWScene getActiveScene()
	{
		return _activeScene;
	}
}
