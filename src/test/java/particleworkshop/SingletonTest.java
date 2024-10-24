package particleworkshop;

import static org.junit.Assert.*;

import org.junit.Test;

import particleworkshop.common.scene.PWSceneManager;

public class SingletonTest {

	@Test
	public void test() {
		PWSceneManager sceneManager = PWSceneManager.getInstance();
		assertTrue(PWSceneManager.getInstance() == sceneManager);
	}

}
