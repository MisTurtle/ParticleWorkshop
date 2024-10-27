package particleworkshop.common.utils.color;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ColorPropertyDeserializer extends StdDeserializer<ColorPropertyWrapper> {

	private static final long serialVersionUID = 9202856550643412395L;

	protected ColorPropertyDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public ColorPropertyWrapper deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JacksonException {
		return new ColorPropertyWrapper(p.getValueAsString());
	}

}
