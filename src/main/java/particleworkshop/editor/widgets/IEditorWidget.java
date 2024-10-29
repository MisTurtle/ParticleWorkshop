package particleworkshop.editor.widgets;

import particleworkshop.editor.EditorContext;
import particleworkshop.editor.IEventList;

public interface IEditorWidget extends IEventList 
{	
	EditorContext getContext();
}
