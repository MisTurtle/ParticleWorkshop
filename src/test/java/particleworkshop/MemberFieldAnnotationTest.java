package particleworkshop;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import particleworkshop.common.structures.entities.EntityShape;
import particleworkshop.editor.widgets.inspector.annotations.AsSlider;
import particleworkshop.editor.widgets.inspector.annotations.UseFloatRange;
import particleworkshop.editor.widgets.inspector.annotations.UseIntRange;

public class MemberFieldAnnotationTest {
	
	class TestClass {
		@AsSlider(majorStepSize = 0) @UseFloatRange(min=0, max=10)
		private SimpleFloatProperty testFloat = new SimpleFloatProperty(0);

		@AsSlider(majorStepSize = 0) @UseIntRange(min=0, max=20)
		private SimpleIntegerProperty testInt = new SimpleIntegerProperty(5);
		
		private SimpleObjectProperty<EntityShape> testObj = new SimpleObjectProperty<>(EntityShape.CIRCULAR);
	}
	
	class TestClass2 extends TestClass {
		@AsSlider(majorStepSize = 0)
		private SimpleFloatProperty testFloat2 = new SimpleFloatProperty(3.5f);
	}

	@Test
	public void test() throws IllegalArgumentException, IllegalAccessException {
		Object obj = new TestClass();
		Class<?> objClass = obj.getClass();
		
		assertEquals(objClass, TestClass.class);
		
		for(Field field: objClass.getDeclaredFields())
		{
			System.out.println("Annotations for field `" + field.getName() + "`");
			field.setAccessible(true);
			for(Annotation a: field.getAnnotations())
			{
				System.out.println(" - " + a.toString());
				if(a instanceof UseFloatRange) 
				{
					assertEquals(((UseFloatRange) a).max(), 10, 0.00001);
					assertTrue(field.get(obj) instanceof SimpleFloatProperty);
				}
				else if(a instanceof UseIntRange) assertEquals(((UseIntRange) a).max(), 20, 0.00001);
			}
		}
		
		Object obj2 = new TestClass2();
		Class<?> obj2Class = obj2.getClass();
		
		int annotationsCount = 0;
		while(obj2Class != null)
		{
			for(Field field: obj2Class.getDeclaredFields())
			{
				annotationsCount += field.getAnnotations().length;
			}
			obj2Class = obj2Class.getSuperclass();
		}
		assertEquals(annotationsCount, 5);
	}

}
