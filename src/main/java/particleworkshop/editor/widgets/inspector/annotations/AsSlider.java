package particleworkshop.editor.widgets.inspector.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AsSlider {
	float majorStepSize();
	int displayPrecision() default 3;
	boolean snap() default true;
}