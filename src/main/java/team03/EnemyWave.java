package team03;

import java.awt.geom.Point2D;

/**
 * Enemywave containing relevant data to an enemywave, such as 
 * fireLocation
 * firetime
 * Bullet velocity, angle and distance traveled and direction
 * @author Parents of R.O.B.E.R.T
 *
 */
public class EnemyWave {
	    Point2D.Double fireLocation;
	    long fireTime;
	    double bulletVelocity, directAngle, distanceTraveled;
	    int direction;

	    public EnemyWave() { 
	    	/**
	    	 * Helper class for wavesurfing
	    	 */
	    }
}
