package team03;

import robocode.*;

/**
 * Superclass in order to enable modularity
 * @author Parents of R.O.B.E.R.T (Team03)
 *
 */
public class Movement {

	Movement() {

	}

	/**
	 * activates every turn.
	 */
	public void update() {
		/**
		 * activates every turn.
		 */
	}

	/**
	 * 
	 * @param e ScannedRobotEvent passed from robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		/**
		 * OnScannedRobot event passed from Robocode. Supermethod
		 */
	}

	/**
	 * When R.O.B.E.R.T is hit by a bullet, he will move to a different location.
	 * 
	 * @param e HitByBulletEvent passed from robot
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		/**
		 * onHitByBullet event passed from Robocode. Supermethod
		 */
	}

	/**
	 * When R.O.B.E.R.T collides with another robot he will try to get away from it
	 * (or/and , alternatively, switch target and try to kill it) <--
	 * 
	 * (det i parantes:Targeting ist?)
	 * 
	 * @param e HitRobotEvent passed from robot
	 */
	public void onHitRobot(HitRobotEvent e) {
		/**
		 * onHitRobot event passed from Robocode. Supermethod
		 */
	}

	/**
	 * When another robot dies this event occurs.
	 * 
	 * If the robot that dies are R.O.B.E.R.Ts target, R.O.B.E.R.T will scan for a
	 * new target robot.
	 * 
	 * vet inte om det jag kommenterat är helt rätt... iom detta är i movement...
	 * känner att denna metod borde vara i Targeting ist...
	 * 
	 * @param e RobotDeathEvent passed from robot
	 */
	public void onRobotDeath(RobotDeathEvent e) {
		/**
		 * onRobotDeath event passed from Robocode. Supermethod
		 */
	}

}
