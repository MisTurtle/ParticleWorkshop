package particleworkshop.common.utils.color;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ColorPropertySerializer extends StdSerializer<ColorPropertyWrapper>
{
	private static final long serialVersionUID = 2400168828215584614L;

	protected ColorPropertySerializer() {
		super(ColorPropertyWrapper.class);
	}

	@Override
	public void serialize(ColorPropertyWrapper value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		gen.writeString(value.getColor().toString());
	}
}
