package particleworkshop.editor;

import java.nio.file.Path;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import particleworkshop.App;
import particleworkshop.common.scene.PWScene;
import particleworkshop.editor.item.EditorItemFactory;
import particleworkshop.editor.widgets.EditorItemInspector;
import particleworkshop.editor.widgets.EditorMenuBar;
import particleworkshop.editor.widgets.EditorProjectHierarchy;
import particleworkshop.editor.widgets.EditorToolbar;

public class EditorScene extends PWScene {

	private Label _projectNameLabel;
	private EditorMenuBar _menuBar;
	private EditorProjectHierarchy _hierarchy;
	private EditorToolbar _toolbar;
	private EditorItemInspector _inspector;
	
	private EditorContext _context;

	public EditorScene(App owner) {
		super(owner, new BorderPane());
		
		EditorItemFactory.init();
		_context = new EditorContext(getStage());
		
		setupEventHandlers();
	}

	public EditorScene(App owner, String path) {
		super(owner, new BorderPane());

		EditorItemFactory.init();
		_context = new EditorContext(Path.of(path), getStage());
		
		setupEventHandlers();
	}
	
	private void setupEventHandlers()
	{
		_context.addPropertyChangeListener(evt -> {
			updateTitle();
		});
		setOnMousePressed(event -> {
			if(event.getSource() != getFocusOwner() && getFocusOwner() instanceof Node)
				getFocusOwner().getParent().requestFocus();
		});
	}
	
	@Override
	public void onSetActive()
	{
		super.onSetActive();
		if(_context.getProject() == null)
			_context.actNewProject(); // This could be changed to display a welcome window later 
	}
	
	public EditorContext getContext()
	{
		return _context;
	}
	
	@Override
	public final void updateTitle()
	{
		super.updateTitle();
		_projectNameLabel.setText(_context.getTitle()); // Editor title and window title are different
	}

	@Override
	public final String getTitle() {
		String prefix = "ParticleWorkshop - ";
		if(getContext().getProject() == null)
			return prefix + "Welcome Page";
		String projectName = getContext().getProject().getName();
		if(getContext().getSavePath() == null)
			return prefix + projectName + " [Not saved on disk]";
		return prefix + projectName + " [ " + getContext().getSavePath().toAbsolutePath() + "]";
	}

	@Override
	public final void setupComponents() {
		BorderPane root = (BorderPane) getRoot();
		
		// TODO : Update buttons states according to the project state (saved, close, ....)
		// Navigation bar
		_menuBar = new EditorMenuBar(getContext()); _menuBar.getStyleClass().add("darklight");
		_projectNameLabel = new Label(getTitle()); _projectNameLabel.setStyle("-fx-font-weight: bold;");
		_hierarchy = new EditorProjectHierarchy(getContext());
		_toolbar = new EditorToolbar(getContext());
		_inspector = new EditorItemInspector(getContext());

		StackPane menuBarWithLabel = new StackPane(_menuBar, _projectNameLabel);
		StackPane.setAlignment(_projectNameLabel, javafx.geometry.Pos.CENTER);
		VBox topBox = new VBox(); topBox.getChildren().addAll(menuBarWithLabel, _toolbar); topBox.getStyleClass().add("darklight");
		StackPane inspectorPane = new StackPane(_inspector);
		
		root.setTop(topBox);
		root.setLeft(_hierarchy);
		root.setRight(inspectorPane);
		
		updateTitle();
	}
}
