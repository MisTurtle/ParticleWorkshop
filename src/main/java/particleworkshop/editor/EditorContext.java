package particleworkshop.editor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import particleworkshop.common.structures.ItemBase;
import particleworkshop.common.structures.project.SimulationStructure;
import particleworkshop.editor.item.EditorItemBase;
import particleworkshop.editor.item.EditorItemFactory;

public class EditorContext implements IEventList
{	
	
	private Stage _stage;
	private Path _savePath;
	private boolean _changesSaved = true;
	
	private SimulationStructure _project;
	private SimulationSettingsAdapter _projectSettings;
	
	private List<EditorItemBase<? extends ItemBase>> _editorItems = new ArrayList<EditorItemBase<? extends ItemBase>>();
	private EditorItemBase<?> _selectedItem = null;
	
	private PropertyChangeSupport _support;
	
	public EditorContext(Stage stage)
	{
		this(null, null, stage);
	}
	
	public EditorContext(Path path, Stage stage)
	{
		this(path, null, stage);
	}
	
	public EditorContext(SimulationStructure project, Stage stage)
	{
		this(null, project, stage);
	}
	
	public EditorContext(Path path, SimulationStructure project, Stage stage)
	{
		_stage = stage;
		_savePath = path;
		_project = project;
		_projectSettings = null;
		_support = new PropertyChangeSupport(this);
	}
	
	public Stage getStage()
	{
		return _stage;
	}
	
	public SimulationStructure getProject()
	{
		return _project;
	}
	public SimulationSettingsAdapter getProjectSettings()
	{
		if(_project == null) return null;
		else if(_projectSettings == null) _projectSettings = new SimulationSettingsAdapter(_project.getSettings());
		
		return _projectSettings;
	}
	
	public Path getSavePath()
	{
		return _savePath;
	}
	
	public List<EditorItemBase<?>> getEditorItems()
	{
		return _editorItems;
	}
	
	public void setUnsaved()
	{
		if(_changesSaved)
		{
			_changesSaved = false;
			_support.firePropertyChange(EVT_PROJECT_SAVE, true, false);
		}
	}
	
	public void reloadProjectContents()
	{
		if(_project == null) _editorItems.clear();
		else
			for(ItemBase i: _project.getItems())
				_editorItems.add(EditorItemFactory.fromStructure(i));
	}
	
	public boolean sendSavePrompt()
	{ // Returns whether the current file should be saved or not
		if(_project == null) return false;
		if(_changesSaved) return true;
		
		Alert alert = new Alert(AlertType.CONFIRMATION, "Do you wish to save your changes before leaving?\nAny unsaved worked will be lost.", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();

		if(alert.getResult() == ButtonType.CANCEL) return false;
		if(alert.getResult() == ButtonType.YES && !actSaveProject()) return false;
		
		return true;
	}

	public boolean actNewProject()
	{
		if(_project != null && !sendSavePrompt()) return false;
		
		SimulationStructure old = _project;
		_project = new SimulationStructure();
		_projectSettings = null;
		
		//reloadProjectContents();
		_support.firePropertyChange(EVT_PROJECT_CHANGE, old, _project);
		return true;
	}
	
	public boolean actOpenProject()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open a project...");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("ParticleWorkshop Project", "*.pwp")); // TODO : Make this a constant
		File location = fileChooser.showOpenDialog(getStage());
		
		if(location == null) return false;
		
		Path path = location.toPath().toAbsolutePath();

		if(_project != null && !actCloseProject()) return false;
		// Either the project was successfully saved or changes are to be discarded
		
		try {
			ObjectMapper mp = new ObjectMapper();
			String projectContent = Files.readString(path);
			SimulationStructure simDesc = mp.readValue(projectContent, SimulationStructure.class);
			_project = simDesc;
			_projectSettings = null;
			reloadProjectContents();
		}catch(IOException e)
		{
			Alert alert = new Alert(AlertType.ERROR, "Your project could not be opened and might be corrupted:\n" + e.getMessage(), ButtonType.CANCEL);
			alert.showAndWait();
			return false;
		}
		
		_savePath = path;
		_support.firePropertyChange(EVT_PROJECT_CHANGE, null, _project);
		
		return true;
	}
	
	public boolean actSaveProject()
	{
		if(_project == null || (_changesSaved && _savePath != null)) return false;
		if(_savePath == null) { return actSaveProjectAs(); }

		try {
			ObjectMapper mp = new ObjectMapper();
			List<ItemBase> items = _editorItems.stream().map(editorItem -> editorItem.asStructure()).collect(Collectors.toList());
			_project.setItems((ArrayList<ItemBase>) items);
			_project.setSettings(getProjectSettings().asSettingsContainer());
			mp.writeValue(_savePath.toFile(), _project);
			this._changesSaved = true;
		} catch(IOException e) {
			String prefix = "An error occurred while saving your project:";
			Alert alert = new Alert(AlertType.ERROR, prefix + "\n" + e.getMessage(), ButtonType.CANCEL);
			alert.showAndWait();
			return false;
		}

		_support.firePropertyChange(EVT_PROJECT_SAVE, false, true);
		return true;
	}
	
	public boolean actSaveProjectAs()
	{
		if(_project == null) return false;
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save project as...");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("ParticleWorkshop Project", "*.pwp"));
		File location = fileChooser.showSaveDialog(getStage());
		
		if(location == null) return false;
		
		_savePath = location.toPath().toAbsolutePath();
		_changesSaved = false;
		
		return actSaveProject();
	}
	
	public boolean actCloseProject()
	{
		if(_project == null) return false;
		if(!sendSavePrompt()) return false;

		SimulationStructure projectSave = _project;
		_savePath = null;
		_project = null;
		_projectSettings = null;
		_changesSaved = true;
		_editorItems.clear();

		_support.firePropertyChange(EVT_PROJECT_CHANGE, projectSave, null);
		
		// reloadProjectContents();
		return true;
	}
	
	public void addItem(EditorItemBase<?> item)
	{
		if(_project == null) return;
		
		_editorItems.add(item);
		_changesSaved = false;
		
		_support.firePropertyChange(EVT_ITEM_CREATED, null, item);
		selectItem(item);
	}
	
	public void selectItem(EditorItemBase<?> item)
	{
		if(item != null && (_project == null || !_editorItems.contains(item))) return;
		_support.firePropertyChange(EVT_ITEM_SELECTED, _selectedItem, item);
		_selectedItem = item;
	}

	public void selectItemIndex(int selectedIndex) {
		if(_project == null || selectedIndex >= _editorItems.size() || selectedIndex < 0) selectItem(null); 
		else selectItem(_editorItems.get(selectedIndex));
	}
	
	public final String getTitle() {
		if(_project == null) return "ParticleWorkshop";
		
		String suffix = _project.getName();
		return "ParticleWorkshop - " + suffix + (_changesSaved ? "" : "*");
	}

	 public final void addPropertyChangeListener(PropertyChangeListener listener) {
		 _support.addPropertyChangeListener(listener); // TODO : Better way of doing that, either with enum as property identifiers or integers, but not strings anyways
	 }

	 public final void removePropertyChangeListener(PropertyChangeListener listener) {
		 _support.removePropertyChangeListener(listener);
	 }

	public void onItemChanged(EditorItemBase<?> item) {
		_support.firePropertyChange(EVT_ITEM_CHANGED, null, item);
	}
	
	public void onSimulationSettingsChanged() {
		_support.firePropertyChange(EVT_SIM_SETTINGS_CHANGED, null, getProjectSettings());
	}
}
