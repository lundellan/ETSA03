/**	
Copyright (c) 2018 David Phung

Building on work by Philip Johnson and Keone Hiraide, University of Hawaii.
https://ics613s13.wordpress.com/

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package team03;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.RoundEndedEvent;
import robocode.control.events.RoundStartedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IBulletSnapshot;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.snapshot.RobotState;
import robocode.control.testing.RobotTestBed;

/**
 * Test class for feature 2 - Closest enemy targeting of BasicMeleeBot.
 *
 * @author David Phung
 *
 */
@RunWith(JUnit4.class)
public class ST_F3_2_TargetClosest extends RobotTestBed {

	// constants used to configure this system test case
	// ETSA02 Lab 3: We recommend that you design a deterministic test case with two
	// SittingDuck robots.
	private String ROBOT_UNDER_TEST = "team03.Robert*";
	private String ENEMY_ROBOTS = "sample.SittingDuck,sample.SittingDuck";
	private int NBR_ROUNDS = 10; // the battle will be deterministic and we will set initial positions so one
								// round is enough.
	private double THRESHOLD = 0.9;
	private boolean tooMuchEnergy = false;
	private int totalCount;
	private double prevEnergy;
	private int correctTarget = 0;
	private boolean hasWalleaching;

	/**
	 * The names of the robots that want battling is specified.
	 * 
	 * @return The names of the robots we want battling.
	 */
	@Override
	public String getRobotNames() {
		return ROBOT_UNDER_TEST + "," + ENEMY_ROBOTS;
	}

	/**
	 * Pick the amount of rounds that we want our robots to battle for.
	 *
	 * @return Amount of rounds we want to battle for.
	 */
	@Override
	public int getNumRounds() {
		return NBR_ROUNDS;
	}

	/**
	 * Returns a comma or space separated list like: x1,y1,heading1, x2,y2,heading2,
	 * which are the coordinates and heading of robot #1 and #2. So "(0,0,180),
	 * (50,80,270)" means that robot #1 has position (0,0) and heading 180, and
	 * robot #2 has position (50,80) and heading 270.
	 * 
	 * Override this method to explicitly specify the initial positions for your
	 * test cases.
	 * 
	 * Defaults to null, which means that the initial positions are determined
	 * randomly. Since battles are deterministic by default, the initial positions
	 * are randomly chosen but will always be the same each time you run the test
	 * case.
	 * 
	 * @return The list of initial positions.
	 */
	@Override
	public String getInitialPositions() {
		return null;
	}

	/**
	 * Returns true if the battle should be deterministic and thus robots will
	 * always start in the same position each time.
	 * 
	 * Override to return false to support random initialization.
	 * 
	 * @return True if the battle will be deterministic.
	 */
	@Override
	public boolean isDeterministic() {
		return true;
	}

	/**
	 * Specifies how many errors you expect this battle to generate. Defaults to 0.
	 * Override this method to change the number of expected errors.
	 * 
	 * @return The expected number of errors.
	 */
	@Override
	protected int getExpectedErrors() {
		return 0;
	}

	/**
	 * Invoked before the test battle begins. Default behavior is to do nothing.
	 * Override this method in your test case to add behavior before the battle
	 * starts.
	 */
	@Override
	protected void runSetup() {
	}

	/**
	 * Invoked after the test battle ends. Default behavior is to do nothing.
	 * Override this method in your test case to add behavior after the battle ends.
	 */
	@Override
	protected void runTeardown() {
	}

	/**
	 * Called after the battle. Provided here to show that you could use this method
	 * as part of your testing.
	 * 
	 * @param event Holds information about the battle has been completed.
	 */
	@Override
	public void onBattleCompleted(BattleCompletedEvent event) {
		assertTrue("It should closest enemy" + THRESHOLD + "times but it did only " + correctTarget + "Times",
				tooMuchEnergy != true && correctTarget / totalCount > THRESHOLD);

		// ETSA02 Lab 3: Implement a system test case for closest enemy targeting.
		// throw new UnsupportedOperationException("To be implemented in Lab3");
	}

	/**
	 * Called before each round. Provided here to show that you could use this
	 * method as part of your testing.
	 * 
	 * @param event The RoundStartedEvent.
	 */
	@Override
	public void onRoundStarted(RoundStartedEvent event) {
		IRobotSnapshot robert = event.getStartSnapshot().getRobots()[0];
		if (robert.getBodyColor() == Color.black.getRGB()) {
			hasWalleaching = true;
			System.out.println("Är walleach");
		}
	}

	/**
	 * Called after each round. Provided here to show that you could use this method
	 * as part of your testing.
	 * 
	 * @param event The RoundEndedEvent.
	 */
	@Override
	public void onRoundEnded(RoundEndedEvent event) {

	}

	/**
	 * Called after each turn. Provided here to show that you could use this method
	 * as part of your testing.
	 * 
	 * @param event The TurnEndedEvent.
	 */
	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		//if (hasWalleaching) {
			IRobotSnapshot bmb = event.getTurnSnapshot().getRobots()[0];
			double xBMB = bmb.getX();
			double yBMB = bmb.getY();
			IRobotSnapshot duck1 = event.getTurnSnapshot().getRobots()[1];
			double xDuck1 = duck1.getX();
			double yDuck1 = duck1.getY();
			IRobotSnapshot duck2 = event.getTurnSnapshot().getRobots()[2];
			double xDuck2 = duck2.getX();
			double yDuck2 = duck2.getY();
			double distance1 = 0;
			double distance2 = 0;
			if (bmb.getState() == RobotState.ACTIVE) {
				IBulletSnapshot[] bullets = event.getTurnSnapshot().getBullets();
				for (int i = 0; i < bullets.length; i++) {
					if (bullets[i].getOwnerIndex() == 0) {
						if (bmb.getEnergy() > prevEnergy || bmb.getEnergy() == 5) {
							totalCount++;
						}

						if (bullets[i].getVictimIndex() != -1) {
							distance1 = Math.hypot(xBMB - xDuck1, yBMB - yDuck1);
							distance2 = Math.hypot(xBMB - xDuck2, yBMB - yDuck2);
							if (bullets[i].getVictimIndex() == 1 && distance1 < distance2)
								correctTarget++;
							if (bullets[i].getVictimIndex() == 2 && distance2 < distance1)
								correctTarget++;
						}
					}
					prevEnergy = bmb.getEnergy();
				}
			}

		}
	//}
}