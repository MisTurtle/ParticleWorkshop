package particleworkshop.common.utils.color;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javafx.scene.paint.Color;

@JsonSerialize(using = ColorPropertySerializer.class)
@JsonDeserialize(using = ColorPropertyDeserializer.class)
public class ColorPropertyWrapper
{	
	private Color _color;
	
	public ColorPropertyWrapper() { _color = Color.BLACK; }
	public ColorPropertyWrapper(Color c) { _color = c; }
	public ColorPropertyWrapper(String hex) { _color = Color.web(hex); }
	public ColorPropertyWrapper(int r, int g, int b) { _color = Color.rgb(r, g, b); }
	public ColorPropertyWrapper(int r, int g, int b, double a) { _color = Color.rgb(r, g, b, a); }
	
	public Color getColor() {
		return _color;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other == this) return true;
		if(other instanceof ColorPropertyWrapper) return ((ColorPropertyWrapper) other).getColor().equals(_color);
		return false;
	}
}

