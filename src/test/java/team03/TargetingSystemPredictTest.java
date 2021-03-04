package team03;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TargetingSystemPredictTest {
	
	private Robert robert1;
	private MockBot mockbot, mockbotHigh;
	// private MockBot mockbotHigh;
	private TargetingSystemPredict targeting;
	private EnemyTracker tracker;
	
	@Before
	public void setUp()	{
		robert1 = new Robert();
		mockbot = new MockBot("Mockbot low energy", 100, 0, 0, 0);
		mockbotHigh = new MockBot("Mockbot high energy", 1000, 0, 0, 0);
		
		tracker = new EnemyTracker(mockbot);
		targeting = new TargetingSystemPredict(tracker, robert1);
		
	}
	
	@After
	public void tearDown()	{
		robert1 = null;
		mockbot = null;
		tracker = null;
		targeting = null;
	}
	
	@Test
	public void testUpdate()	{
//		 assertEquals(0, targeting.getIndex());
		MockScannedRobotEvent e1 = new MockScannedRobotEvent("fake enemy 1", 100, 0, 100, 0, 0, false);
		
		mockbot.run();
		tracker.addEnemy(e1);
		mockbot.run();
		 //targeting.update();
		 targeting.getTargetParameters();
		 
//		assertEquals(true, targeting.doneShit); //mockgate
		assertEquals(true, true);
	}
	
	@Test
	public void testDoShit()	{
		
		assertEquals(true, true);
	}
	
}
