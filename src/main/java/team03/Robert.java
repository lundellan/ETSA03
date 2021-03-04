/**	
Copyright (c) 2018 David Phung

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

import java.io.IOException;
import java.util.Random;

import etsa03.*;
import robocode.TeamRobot;

import java.awt.Color;
import robocode.HitByBulletEvent;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

/**
 * @author Parents of R.O.B.E.R.T (Team03)
 * 
 *         Beta of Robert
 */
public class Robert extends TeamRobot {
	private Movement movementSystem;
	private Targeting targetingSystem;
	private EnemyTracker enemyTracker;

	private int numberOfRoberts = 0;

	private int rank;
	private RobertObjectList roberts;
	private RobertObject thisRobert;

	/**
	 * The main loop controlling the robot behavior.
	 * 
	 */
	public void run() {

		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		enemyTracker = new EnemyTracker(this);
		targetingSystem = new TargetingSystemPredict(enemyTracker, this);
		numberOfRoberts = searchForRoberts();
		thisRobert = new RobertObject(getName());
		roberts = new RobertObjectList();

		/* Comment row 68 when running SystemTestBed */
		useRobertTalk();
		roberts.add(thisRobert);
		rankUpdate();
		
		/* Comment row 73, 74 and 75 when running SystemTestBed */
		if (searchForRoberts() == 1) {
			adaptToRank(5);
		}

		/* Uncomment row 78 when running SystemTestBed to get random rank every round */
//		 adaptToRank(getRandomRank());

		do {
			if (getTime() > 30) {
				if (getTime()%100 == 0) 
	    			forceUpdateTarget();
				targetingSystem.update();
				movementSystem.update();
				execute();
			} else {
				setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
				execute();
			}
//			setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		} while (true);

	}

	/**
	 * Switches movement algorithm based on parameter Currently three different
	 * options available: 0. Anti-gravity 1. Walleaching 2. Wavesurfing
	 * 
	 * @param n Which behaviour to switch to according to list above, in that order.
	 */
	void switchMovementBehaviour(int n) {
		switch (n) {
		case 0:
			movementSystem = new MovementSystem(this, enemyTracker);
			break;
		case 1:
			movementSystem = new MovementSystemWalleaching(this);
			break;
		case 2:
			movementSystem = new MovementSystemWaveSurfer(this);
			break;
		default:
			movementSystem = new MovementSystemWaveSurfer(this);
			break;
		}
	}

	/**
	 * Switches targeting behaviour. Currently: 0. Near targeting 1. Highest energy
	 * targeting
	 * 
	 * @param targetingBehaviour which behaviour to switch to.
	 */
	void switchTargetingBehaviour(int targetingBehaviour) {
		switch (targetingBehaviour) {
		/* predictive targeting against closest target */
		case 0:
			enemyTracker.setNear(true);
			targetingSystem.update();
			break;
		/*
		 * predictive targeting single target, the enemy with the most inital energy or
		 * highest energy at target switching moment
		 */
		case 1:
			enemyTracker.setNear(false);
			targetingSystem.update();
			break;
		/* predictive targeting against closest target */
		default:
			enemyTracker.setNear(true);
			break;
		}
	}

	/**
	 * Describes the action taken when a robot has been scanned.
	 * 
	 * @param e The ScannedRobotEvent provided by Robocode.
	 */
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		enemyTracker.addEnemy(e);

		if (getTime() > 30 && movementSystem != null) {
			movementSystem.onScannedRobot(e);
			targetingSystem.update();
		}
	}

	/**
	 * Describes the action taken when a robot has been destroyed.
	 * 
	 * @param e The RobotDeathEvent provided by Robocode.
	 */
	@Override
	public void onRobotDeath(RobotDeathEvent e) {
		if (roberts.contains(e.getName())) {
			roberts.remove(e.getName());
			if (roberts.size() == 1) {
				System.out.println("Only 1 ROBERT left");
				adaptToRank(5);
			} else {
				rankUpdate();
			}
		}
		enemyTracker.removeEnemy(e.getName());

	}

	/**
	 * Describes the action taken when a robot has been hit by a bullet.
	 * 
	 * @param e the HitByBulletEvent provided by Robocode.
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		if (movementSystem != null) {
			movementSystem.onHitByBullet(e);
		}
	}

	/**
	 * onMessageReceived: Describes how incoming messages are treated.
	 * 
	 * @param e messageEvent from RoboCode
	 */
	@Override
	public void onMessageReceived(MessageEvent e) {

		/* Det här meddelandet används för att kommunicera färger. */
		if (e.getMessage() instanceof RobotColors) {
			RobotColors c = (RobotColors) e.getMessage();
			setBodyColor(c.bodyColor);
			setGunColor(c.gunColor);
			setRadarColor(c.radarColor);
			setScanColor(c.scanColor);
			setBulletColor(c.bulletColor);
		}

		/*
		 * Ett meddelande med en annan Roberts randomNumber, vilket vi lägger på en
		 * lista för att sedan kunna ta reda på varje Roberts rank.
		 */
		if (e.getMessage() instanceof RobertRank) {
			//System.out.println("Recieved message from " + e.getSender());
			RobertRank r = (RobertRank) e.getMessage();
			roberts.add(new RobertObject(r.name));
			roberts.sortList();
			if (roberts.size() == numberOfRoberts) {
				rankUpdate();
			}
		}
	}

	/**
	 * Sends a random number in a broadcast message
	 */
	private void useRobertTalk() {
		RobertRank r = new RobertRank();
		r.name = getName();
		try {
			broadcastMessage(r);
			//System.out.println(getName() + " broadcasted the message " + "'" + r.name + "'");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Adapts the rank to different movement and targeting depending on ROBERTs rank
	 * 
	 * Anpassar beteenden och movement beroende på ROBERTs rank
	 * 
	 * @param rank Which rank to adapt to. Rank 1 - usual targeting and wavesurfing
	 *             Rank 2 - Walleaching Rank 3 - BMB Rank 4 - BMB + Predictive
	 *             targeting Rank 5 - Predictive and wavesurfing Rank 6 - ONLY
	 *             Predictive targeting, no movement
	 */
	private void adaptToRank(int rank) {
//		clearAllEvents();
//		System.out.println("Switched to rank: " + rank);
		setColor(rank);
		switch (rank) {
		case 1:
			// Wavesurfing and leader
			switchTargetingBehaviour(1);
			switchMovementBehaviour(2);
			break;
		case 2:
			// Walleaching and near
			switchTargetingBehaviour(0);
			switchMovementBehaviour(1);
			break;
		case 3:
			// Anti gravity and near
			switchTargetingBehaviour(0);
			switchMovementBehaviour(0);
			break;
		case 4:
			// Wavesurfing and leader
			switchTargetingBehaviour(1);
			switchMovementBehaviour(2);
			break;
		case 5:
			// Wavesurfing and near
			switchTargetingBehaviour(0);
			switchMovementBehaviour(2);
			break;
		default:
			switchTargetingBehaviour(1);
			switchMovementBehaviour(2);
			break;
		}
	}

	/**
	 * Only used for testing if the aquired rank is acheived. The robot with pink
	 * colors has rank 1, the robot with black colors rank 2 and the robot with
	 * white colors has rank 3.
	 */
	private void setColor(int rank) {
		/*switch (rank) {
		case 1:
			setBodyColor(Color.pink);
			setGunColor(Color.pink);
			setRadarColor(Color.pink);
			setBulletColor(Color.pink);
			setScanColor(Color.pink);
			break;
		case 2:
			setBodyColor(Color.black);
			setGunColor(Color.black);
			setRadarColor(Color.black);
			setBulletColor(Color.black);
			setScanColor(Color.black);
			break;
		case 3:
			setBodyColor(Color.white);
			setGunColor(Color.white);
			setRadarColor(Color.white);
			setBulletColor(Color.white);
			setScanColor(Color.white);
			break;
		case 5:
			setBodyColor(Color.pink);
			setGunColor(Color.white);
			setRadarColor(Color.white);
			setBulletColor(Color.white);
			setScanColor(Color.white);
			break;
		default:
			setBodyColor(Color.black);
			setGunColor(Color.white);
			setRadarColor(Color.white);
			setBulletColor(Color.black);
			setScanColor(Color.black);
		}*/
	}

	/**
	 * Used to determine how many R.O.B.E.R.Ts are in the same team
	 */
	public int searchForRoberts() {
		int n = 0;
		if (getTeammates() == null) {
			return 1;
		}
		String[] roberts = getTeammates();
		for (int i = 0; i < roberts.length; i++) {
			if (roberts[i].toLowerCase().contains("robert")) {
				n++;
			}
		}
		return n + 1;
	}

	/**
	 * 
	 * Gets random rank for RobertTalk.
	 * 
	 * @return A random number between 1-3 is returned determining the rank of the
	 *         R.O.B.E.R.Ts
	 */
	private int getRandomRank() {
		Random rand = new Random();
		return rand.nextInt(3) + 1;
	}

	/**
	 * Returner for rank.
	 * 
	 * @return the current rank
	 */
	private int getRank() {
		return rank;
	}

	/**
	 * Updates roberts rank according to RobertTalk.
	 */
	private void rankUpdate() {
		rank = roberts.getRank(getName());
		adaptToRank(rank);
	}

	/**
	 * Force updates current target based on doing a blocking call to spin radar And
	 * to update next target in EnemyTracker.
	 */
	public void forceUpdateTarget() {
			setTurnRadarLeft(720000);
			enemyTracker.getNextTarget();
	}

}
