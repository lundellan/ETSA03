package team03;

import java.awt.geom.Point2D;

import robocode.TeamRobot;
import robocode.util.Utils;

/**
 * Predicts where the targeted robot will be when the bullet reaches. So far:
 * implemented for linear movement in Intercept.
 * 
 * Targeting behavior inspired from <a
 * href="https://www.ibm.com/developerworks/library/j-pred-targeting/j-pred-targeting-pdf.pdf</a>
 * <br/>
 * 
 * @author Parents of R.O.B.E.R.T (Team03)
 *
 */

public class TargetingSystemPredict extends Targeting {
	Intercept intercept = new CircularIntercept();
	private EnemyTracker enemyTracker;
	private Robert robot;
	private double bulletPower = 1;
	private double currentTargetPositionX, currentTargetPositionY, currentTargetHeading_deg, currentTargetVelocity;
	boolean doneShit;

	/**
	 * Constructs a predictive targeting system for the active robot that tracks the
	 * target.
	 * 
	 * @param enemyTracker Handles all the enemydata
	 * @param robot        the active robot.
	 */
	public TargetingSystemPredict(EnemyTracker enemyTracker, Robert robot) {
		this.enemyTracker = enemyTracker;
		this.robot = robot;
		doneShit = false;
	}

	/**
	 * Updates the current coordinates of the target(leader) and calculates at which
	 * angle the gun will be pointed for a hit on the target (leader). The bullet's
	 * trajectory and power is based on the targets(leaders) position, heading and
	 * velocity.
	 * 
	 * Linear movement
	 */
	@Override
	public void update() {
		getTargetParameters();
			double distX = Math.abs(robot.getX() - currentTargetPositionX);
			double distY = Math.abs(robot.getY() - currentTargetPositionY);
			bulletPower = 10-Math.hypot(distX, distY)*0.03;
			if (bulletPower < 1)
				bulletPower = 1;
			intercept.calculate(robot.getX(), robot.getY(), currentTargetPositionX, currentTargetPositionY,
					currentTargetHeading_deg, currentTargetVelocity, bulletPower, 0);
			// Helper function that converts any angle into
			// an angle between +180 and -180 degrees.
			double turnAngle = Utils.normalRelativeAngleDegrees(intercept.bulletHeading_deg - robot.getGunHeading());
			// Move gun to target angle
			robot.setTurnGunRight(turnAngle);
			
			if (Math.abs(turnAngle) <= intercept.angleThreshold) {
				// Ensure that the gun is pointing at the correct angle
				if ((intercept.impactPoint.x > 0) && (intercept.impactPoint.x < robot.getBattleFieldWidth())
						&& (intercept.impactPoint.y > 0) && (intercept.impactPoint.y < robot.getBattleFieldHeight())) {
					// Ensure that the predicted impact point is within
					// the battlefield				
					robot.setFire(bulletPower);
					doneShit = true;
				}
			}
			
	}
	
	/**
	 * For testing purposes. 
	 * @return boolean if hasDoneShit == true
	 */
	public boolean hasDoneShit() {
		return doneShit;
	}

	/**
	 * Sets the target attributes from enemyTracker
	 */
	public void getTargetParameters() {
		if (enemyTracker.getEnemyCount() != 0){
			enemyTracker.getNextTarget();
			currentTargetPositionX = enemyTracker.getTargetPosition().x;
			currentTargetPositionY = enemyTracker.getTargetPosition().y;
			currentTargetHeading_deg = enemyTracker.getTargetHeading();
			currentTargetVelocity = enemyTracker.getTargetVelocity();				
		}
		}
}