package particleworkshop.common.scene;

import particleworkshop.App;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class PWScene extends Scene {
	
	private App _app;
	private boolean _componentsSetup;

	public PWScene(App owner, Pane layout) {
		super(layout);
		
		layout.getStylesheets().add("styles.css");
		
		_app = owner;
		_componentsSetup = false;
	}
	
	public Stage getStage() { return _app.getStage(); }
	public App getApp() { return _app; }
	
	public void onSetActive()
	{
		if(!_componentsSetup) { setupComponents(); _componentsSetup = true; }
		updateTitle();
	}
	public void onSetInactive()
	{
		
	}
	
	public void updateTitle()
	{
		getStage().setTitle(getTitle());
	}
	
	public abstract String getTitle();
	
	public abstract void setupComponents();

}
