package team03;

import robocode.TeamRobot;

/**
 * Walleaching movement behaviour
 * @author Parents of R.O.B.E.R.T (Team03)
 *
 */
public class MovementSystemWalleaching extends Movement {

	private TeamRobot robot;
	private double moveAmount;

	
	
	/**
	 * Super simple algorithm in order to manage walleaching.
	 * @param robot the robot which will be using the module
	 */
	public MovementSystemWalleaching(TeamRobot robot) {
		this.robot = robot;

		moveAmount = Math.max(robot.getBattleFieldWidth(), robot.getBattleFieldHeight());

		robot.turnLeft(robot.getHeading() % 90);
		robot.ahead(moveAmount);
		robot.turnRight(90);
	}
	/**
	 * Runs every round, moves to the next corner, turns right, scans the battlefield.
	 */
	public void update() {
		robot.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		robot.ahead(moveAmount);
		robot.turnRight(90);
	}



}
