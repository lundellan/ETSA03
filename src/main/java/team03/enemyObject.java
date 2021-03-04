package team03;

import java.awt.geom.Point2D;

public class enemyObject {
	private String name;
	private Point2D.Double position;
	private Double energy, velocity, heading;

	enemyObject(String name, Point2D.Double pos, Double energy, Double velocity, Double heading) {
		this.name = name;
		this.position = pos;
		this.energy = energy;
		this.velocity = velocity;
		this.heading = heading;
	}
	public String getName() {
		return name;
	}
	public void updateEnemyData(Point2D.Double pos, Double energy, Double velocity, Double heading) {
		this.position = pos;
		this.energy = energy;
		this.velocity = velocity;
		this.heading = heading;
	}
	public Double getEnergy() {
		return energy;
	}
	public Point2D.Double getPosition() {
		return position;
	}
	public Double getVelocity() {
		return velocity;
	}
	public Double getHeading() {
		return heading;
	}
}
