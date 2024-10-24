package particleworkshop.editor.item;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Region;
import particleworkshop.common.structures.ItemBase;

public interface IEditorItem<T extends ItemBase>
{ // Adapter interface of a Project Item Descriptor to implement editor-related functions and attributes
	
	// TODO : Add groups support later to organise a project
	Node getIcon();
	TreeItem<String> asTreeItem();
	ArrayList<Region> generateControls();
	T asStructure();
}
