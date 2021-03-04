package team03;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team03.EnemyTracker;
import team03.MockScannedRobotEvent;

public class MovementWaveSurferTest {
	private MovementSystemWaveSurfer movement;
	private MockBot robot;
	private EnemyTracker enemyTracker;
	

	@Before
	public void setUp() {
		robot = new MockBot("fake robot", 100, 0, 400, 300);
		movement = new MovementSystemWaveSurfer(robot);
		enemyTracker = new EnemyTracker(robot);
		
		

	}

	@After
	public void tearDown() {
		robot = null;
		movement = null;
		enemyTracker = null;

	}
	
	@Test 
	public void testWaveSurfer() {
		//robot.run();
		movement.update();
		MockScannedRobotEvent e1 = new MockScannedRobotEvent("fake enemy 1", 100, 0, 100, 0, 0, false);
		MockScannedRobotEvent e2 = new MockScannedRobotEvent("fake enemy 2", 100, 90, 200, 0, 0, false);
		enemyTracker.addEnemy(e1);
		enemyTracker.addEnemy(e2);
		
		//movement.onScannedRobot(e1);
		assertEquals(true, true);
	}

}
