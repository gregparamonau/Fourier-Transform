package FourierTransform;

public class Vector {
	double x, y, z;
	
	//vec 2
	public Vector(double a, double b) {
		this.x = a;
		this.y = b;
		this.z = 0;
	}
	
	//vec 3
	public Vector(double a, double b, double c) {
		this.x = a;
		this.y = b;
		this.z = c;
	}
	
	public void add(Vector in){
		this.x += in.x;
		this.y += in.y;
		this.z += in.z;
	}
	
	public void scale(double in) {
		this.x *= in;
		this.y *= in;
		this.z *= in;
	}
	
	public void scale_2D(double in) {
		this.x *= in;
		this.y *= in;
	}
	
	public double length_2D() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public double length() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}
	
	public String toString() {
		return "x: " + this.x + " y: " + this.y + " z: " + this.z;
	}
	
	public static Vector[] add_to_vec_arr(Vector[] in, Vector add) {		
		Vector[] out = new Vector[in.length + 1];
		
		for (int x = 0; x<in.length; x++) {
			out[x] = in[x];
		}
		
		out[out.length - 1] = add;
		
		return out;
		
	}
	
	public static Vector[] del_from_vec_arr(Vector[] in, int index) {
		Vector[] out = new Vector[in.length - 1];
		
		for (int x = 0; x<out.length; x++) {
			out[x] = in[(x < index ? x : x + 1)];
		}
		
		return out;
	}
}
