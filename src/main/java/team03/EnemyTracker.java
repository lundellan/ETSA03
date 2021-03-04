/**	
Copyright (c) 2018 David Phung

Building on work by Mathew A. Nelson and Robocode contributors.

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

import java.awt.geom.Point2D;
import java.util.Arrays;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

/**
 * A class to help with the management of enemies. Modified from David Phungs
 * original
 */
public class EnemyTracker {

	private Point2D.Double[] enemyPositions;
	String[] enemyNames;
	private int enemyCount;
	TeamRobot robot;
	private Double[] enemyEnergy, enemyVelocity, enemyHeading;
	private boolean isNear, foundLeader;
	private int index, indexLeader;
	private Double lastUpdate;

	/**
	 * Construct an object to help with the management of enemies.
	 * 
	 * @param robot the robot we are working on.
	 */
	public EnemyTracker(TeamRobot robot) {
		this.robot = robot;
		enemyNames = new String[10];
		enemyPositions = new Point2D.Double[10];
		enemyCount = 0;
		enemyEnergy = new Double[10];
		enemyVelocity = new Double[10];
		enemyHeading = new Double[10];
		isNear = true;
		lastUpdate = 0.0;
		foundLeader = false;
		indexLeader = -1;
		index = 0;
	}

	/**
	 * To be called when an enemy is scanned.
	 * 
	 * @param e The ScannedRobotEvent received from the onScannedRobot method.
	 */
	public void addEnemy(ScannedRobotEvent e) {
		if (!(robot.isTeammate(e.getName()))) {	
			Point2D.Double enemyPosition = MathUtils.calcEnemyPosition(robot, e);
			int index2 = findEnemyByName(e.getName());
			if (index2 >= 0) {
				enemyPositions[index2] = enemyPosition;
				enemyEnergy[index2] = e.getEnergy();
				enemyVelocity[index2] = e.getVelocity();
				enemyHeading[index2] = e.getHeading();
			} else {
				enemyNames[enemyCount] = e.getName();
				enemyPositions[enemyCount] = enemyPosition;
				enemyEnergy[enemyCount] = e.getEnergy();
				enemyVelocity[enemyCount] = e.getVelocity();
				enemyHeading[enemyCount] = e.getHeading();
				if (e.getEnergy() > 150 && !foundLeader) {
					indexLeader = enemyCount;
					index = enemyCount;
					foundLeader = true;
				}
				enemyCount++;
			}
		}
	}

	/**
	 * Stop tracking an enemy, i.e., when that enemy is dead.
	 * 
	 * @param name the name of enemy to stop tracking.
	 */
	public void removeEnemy(String name) {
		
		int index2 = findEnemyByName(name);
		if (index2 >= 0) {
			for (int i = index2 + 1; i < enemyCount; i++) {
				enemyNames[i - 1] = enemyNames[i];
				enemyPositions[i - 1] = enemyPositions[i];
				enemyEnergy[i - 1] = enemyEnergy[i];
				enemyHeading[i - 1] = enemyHeading[i];
				enemyVelocity[i - 1] = enemyVelocity[i];
				enemyEnergy[index2] = 0.0; 
			} 
			enemyCount--;
			if (index == index2) {
				index--;
				if (index == -1)
					index = 0;
			}
			getNextTarget();
			if (index2 == indexLeader) {
				indexLeader = -1;
				index = 0;				
			}
		}
	}

	/**
	 * Returns the last known positions of the enemies.
	 * 
	 * @return An array of the last known enemy positions.
	 */
	public Point2D.Double[] getEnemyPositions() {
		Point2D.Double[] positions = new Point2D.Double[enemyCount];
		for (int i = 0; i < enemyCount; i++) {
			positions[i] = enemyPositions[i];
		}

		return positions;

	}

	/**
	 * Setter for targeting behaviour. If true then shoots at nearest targets.
	 * If false highest energy.
	 * @param bool Boolean to set whether to shoot near or high energy.
	 */
	public void setNear(boolean bool) {
		this.isNear = bool;
	}

	/**
	 * Updates next target based on which targeting behaviour is chosen.
	 */
	public void getNextTarget() {
		if (enemyCount != 0) {
			if (robot.getTime()%50 == 0)
				lastUpdate = enemyEnergy[index];
			if (isNear) { // index ska ändras hela tiden om det är en closestTarget-ROBERT
				getNextCloseTarget();
			} else {
				if (!foundLeader) {
					getNextHighEnergyTarget();
				} else if (enemyEnergy[index] <= 1) {
					getNextHighEnergyTarget();
				}
			}
		}
	}

	/**
	 * Gets current targeting index, used for unit testing(?)
	 * 
	 * @return current index
	 */
	public int getIndex() { 
		return index;
	}


	/**
	 * Gets next highest energy target based on looping through all the current energies, 
	 * and setting the variable index to the appropriate index. 
	 */
	public void getNextHighEnergyTarget() {
		double energy = 0;
		if (enemyEnergy != null && enemyCount > 0) {
			for (int i = 0; i < enemyCount; i++) {
				if (enemyEnergy[i] != null && enemyEnergy[i] > energy) {
					index = i;
					energy = enemyEnergy[i];
				}
			}
		}
	}
	


	/**
	 * Gets the closest target based on square distance. 
	 * Interpreted from Lab02 exercise.
	 */
	public void getNextCloseTarget() {
		if (enemyCount != 0) {
			Point2D.Double robotPosition = new Point2D.Double(robot.getX(), robot.getY());
			double smallestDistanceSq = Double.POSITIVE_INFINITY;
			for (int i = 0; i < enemyCount; i++) {
				Point2D.Double enemyPosition = enemyPositions[i];
				double d = enemyPosition.distanceSq(robotPosition);
				if (d < smallestDistanceSq) {
					smallestDistanceSq = d;
					index = i;
				}
			}
		}
	}
	

	/**
	 * Returns the amount of enemies currently in EnemyTracker
	 * @return the number of enemies currently being tracked.
	 */
	public int getEnemyCount() {
		return enemyCount;
	}


	/**
	 * Gets the current targets energy.
	 * @return Current targets energy.
	 */
	public Double getTargetEnergy() {
		return enemyEnergy[index];
	}
	
	/**
	 * Gets the current targets velocity.
	 * @return Current targets velocity.
	 */
	public Double getTargetVelocity() {
		return enemyVelocity[index];
	}
	
	/**
	 * Gets the current targets heading.
	 * @return Current targets heading.
	 */
	public Double getTargetHeading() {
		return enemyHeading[index];
	}
	
	/**
	 * Gets the current targets position.
	 * @return Point2D.Double Current targets position.
	 */
	public Point2D.Double getTargetPosition() {
		Point2D.Double position = enemyPositions[index];
		return position;
	}
	
	/**
	 * Gets all the enemies energy.
	 * @return all enemies energy
	 */
	public Double[] getEnemyEnergy() {
		return enemyEnergy;
	}
	
	/**
	 * Gets all the enemies velocity.
	 * @return all enemies velocity
	 */
	public Double[] getEnemyVelocity() {
		return enemyVelocity;
	}
	
	/**
	 * Gets all the enemies heading.
	 * @return all enemies heading
	 */
	public Double[] getEnemyHeading() {
		return enemyHeading;
	}

	
	/**
	 * Finds enemys index based on name
	 * @param name the robots name
	 * @return current index in EnemyTracker
	 */
	private int findEnemyByName(String name) {
		for (int i = 0; i < enemyCount; i++) {
			if (enemyNames[i].equals(name))
				return i;
		}
		return -1;
	}
}
