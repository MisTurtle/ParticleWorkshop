package particleworkshop;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;

import particleworkshop.common.scene.PWSceneManager;
import particleworkshop.editor.EditorScene;


public class App extends Application {
	
	private Stage _stage = null;
	private PWSceneManager _manager = null;
	
	public static void main(String[] args)
	{
		launch();
	}

	@Override
	public void start(Stage stage)
	{
		this._stage = stage;

		Rectangle2D screenSize = Screen.getPrimary().getBounds();
		
		stage.setFullScreenExitHint("");
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.setWidth(screenSize.getWidth() / 2); stage.setHeight(screenSize.getHeight() / 2);
		
		stage.setMaximized(true);
		
		_manager = PWSceneManager.getInstance(); 
		_manager.setActiveScene(new EditorScene(this));
		
		stage.show();
	}
	
	public PWSceneManager getSceneManager()
	{
		return _manager;
	}
	
	public Stage getStage()
	{
		return _stage;
	}
}
