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
import java.util.ArrayList;
import java.util.Arrays;
import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

public class EnemyTrackerList{

	private ArrayList<enemyObject> enemies;
	private Robert robot;
	private boolean isNear, hasFoundLeader;
	private int index, amountOfCalls;

	/**
	 * Construct an object to help with the management of enemies.
	 * 
	 * @param robot the robot we are working on.
	 */
	public EnemyTrackerList(Robert robot) {
		enemies = new ArrayList<enemyObject>();
		this.robot = robot;
		isNear = true;
		index = -1;
		amountOfCalls = 0;
		hasFoundLeader = false;
	}
	public int getEnemyCount() {
		return enemies.size();
	}

	/**
	 * To be called when an enemy is scanned.
	 * 
	 * @param e The ScannedRobotEvent received from the onScannedRobot method.
	 */
	public void addEnemy(ScannedRobotEvent e) {
		if (!(robot.isTeammate(e.getName()))) {	
			enemyObject enemy = findEnemyByName(e.getName());
			if (enemy != null) {
				Point2D.Double enemyPosition = MathUtils.calcEnemyPosition(robot, e);
				enemy.updateEnemyData(enemyPosition, e.getEnergy(), e.getVelocity(), e.getHeading());
			} else {
				Point2D.Double enemyPosition = MathUtils.calcEnemyPosition(robot, e);
				enemyObject enemyNew = new enemyObject(e.getName(), enemyPosition, e.getEnergy(), e.getVelocity(), e.getHeading());
				enemies.add(enemyNew);
			}
		}
	}

	/**
	 * Stop tracking an enemy, i.e., when that enemy is dead.
	 * 
	 * @param name the name of enemy to stop tracking.
	 */
	public void removeEnemy(String name) {
		enemyObject remove = findEnemyByName(name);
		enemies.remove(remove);
		robot.turnRadarLeft(720);

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
		if (!enemies.isEmpty()) {
			if (isNear) { // index ska ändras hela tiden om det är en closestTarget-ROBERT
				getNextCloseTarget();
			} else {
				if (!hasFoundLeader) {
					getNextHighEnergyTarget();
				}
				if ((index > (enemies.size() - 1)) || (enemies.get(index).getEnergy() <= 0)) {
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
		amountOfCalls++;
		double energy = 0;
		int i = 0;
		for (enemyObject enemy : enemies) {
			if (enemy.getEnergy() > energy) {
				index = i;
				energy = enemy.getEnergy();
			}
			i++;
		}		
		if (amountOfCalls >= 1 || getTargetEnergy() > 150)
			hasFoundLeader = true;
	}
	


	/**
	 * Gets the closest target based on square distance. 
	 * Interpreted from Lab02 exercise.
	 */
	public void getNextCloseTarget() {
		if (!enemies.isEmpty()) {
			Point2D.Double robotPosition = new Point2D.Double(robot.getX(), robot.getY());
			Double smallestDistanceSq = Double.POSITIVE_INFINITY;
			for (enemyObject enemy : enemies) {
				Double d = enemy.getPosition().distanceSq(robotPosition);
				if (d < smallestDistanceSq) {
					smallestDistanceSq = d;
					index = enemies.indexOf(enemy);
				}
			}
		}
	}

	/**
	 * Gets the current targets energy.
	 * @return Current targets energy.
	 */
	public Double getTargetEnergy() {
		if (!enemies.isEmpty())
			getNextTarget();
		return enemies.get(index).getEnergy();
	}
	
	/**
	 * Gets the current targets velocity.
	 * @return Current targets velocity.
	 */
	public Double getTargetVelocity() {
		return enemies.get(index).getVelocity();
	}
	
	/**
	 * Gets the current targets heading.
	 * @return Current targets heading.
	 */
	public Double getTargetHeading() {
		return enemies.get(index).getHeading();
	}
	
	/**
	 * Gets the current targets position.
	 * @return Point2D.Double Current targets position.
	 */
	public Point2D.Double getTargetPosition() {
		return enemies.get(index).getPosition();
	}
	public Point2D.Double[] getEnemyPositions() {
		Point2D.Double[] retPos = new Point2D.Double[enemies.size()];
		int i = 0;
		for (enemyObject enemy : enemies) {
			retPos[i] = enemy.getPosition();
			i++;
		}
		return retPos;
	}
	
	
	/**
	 * Finds enemys index based on name
	 * @param name the robots name
	 * @return current index in EnemyTracker
	 */
	private enemyObject findEnemyByName(String name) {
		for (enemyObject enemy : enemies) {
			if(enemy.getName().equals(name))
				return enemy;
		}
		return null;
	}
}
