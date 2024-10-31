package particleworkshop.common.structures;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vector2 {
	private double x = .0;
	private double y = .0;
	
	public Vector2 copy()
	{
		return new Vector2(x, y);
	}
	
	public Vector2 add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}
	public Vector2 add(Vector2 x) {
		this.x += x.x;
		this.y += x.y;
		return this;
	}
	
	public Vector2 subtract(double x, double y) {
		return this.add(-x, -y);
	}
	public Vector2 subtract(Vector2 x) {
		return this.add(-x.x, -x.y);
	}
	
	public Vector2 dot(double x, double y) {
		this.x *= x;
		this.y *= y;
		return this;
	}
	public Vector2 dot(Vector2 x) {
		this.x *= x.x;
		this.y *= x.y;
		return this;
	}
	
	public Vector2 multiply(double x) {
		this.x *= x;
		this.y *= x;
		return this;
	}
	
	public double squaredLength() {
		return Math.pow(x, 2) + Math.pow(y, 2);
	}
	
	public double length() {
		return Math.sqrt(squaredLength());
	}
	
	@Override
	public String toString()
	{
		return String.format("Vector2{x=%.5f, y=%.5f}", x, y);
	}
}
