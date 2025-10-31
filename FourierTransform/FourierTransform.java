package FourierTransform;

import javax.sound.sampled.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FourierTransform {
	public static int screen_width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int screen_height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public static JFrame frame = null;
	public static JPanel pane = null;
	public static Graphics2D g = null;
	
	//trumpet
	static String piano_file = "/Users/gregoryparamonau/Music/Music/Media.localized/Music/Unknown Artist/Unknown Album/piano.wav";
	
	//clarinet
	static String sax1_file = "/Users/gregoryparamonau/Downloads/saxophone.wav";
	static String sax2_file = "/Users/gregoryparamonau/Downloads/saxophone2.wav";
	static String sax3_file = "/Users/gregoryparamonau/Downloads/saxophone3.wav";
	
	static int total_bytes, samples_per_second;
	
	
	//fft
	static double freq_min = 0, freq_max = 7500, freq_sample = 0.1, human_freq_hear = 22000;
	static boolean filter_non_multiples = true;
	
	//visual
	static double h_div = 500, v_div = 0.01;
	static double ft_min = -0.02, ft_max = 0.02;
	static boolean show_ft = true, show_angle = true, show_xy = true;
	
	public static void main(String[] args) {
		
		
		init();
		
		
		
		double[] bytes_piano = new double[0];
		double[] bytes_sax1 = new double[0];
		double[] bytes_sax2 = new double[0];
		double[] bytes_sax3 = new double[0];
		try {
			bytes_piano = readWavFile(piano_file);
			//bytes_sax1 = readWavFile(sax1_file);
			//bytes_sax2 = readWavFile(sax2_file);
			//bytes_sax3 = readWavFile(sax3_file);
			//implement
			//x_scale: 400 in draw_point
			//y_scale: 
			//x_translation: 1600 in for loop
			//y trasnaltion: 
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		/*
		Vector[] ft = ft_array(bytes_piano, 50000, 55000, freq_min, freq_max, freq_sample);
		
		double max_ampl = 0;
		
		for (int x = 0; x<ft.length; x++) {
			max_ampl += ft[x].length_2D();
		}
		
		for (int x = 0; x<ft.length; x++) {
			ft[x].scale_2D(1.0 / max_ampl);
		}
		
		for (int x = 0; x<ft.length; x++) {
			System.out.println("FREQUENCY: " + ft[x].z + " AMPLITUDE: " + ft[x].length_2D());
		}
		
		System.out.print("{");
		
		for (int x = 0; x<ft.length; x++) {
			System.out.print(ft[x].length_2D() + ", ");
		}
		
		System.out.println("}");
		
		System.exit(0);*/
		
		//Vector[] ft = ft_array(bytes_sax1, 50000, 55000, freq_min, freq_max, freq_sample);
		//Vector[] ft = ft_array(bytes_trumpet, 50000, 55000, freq_min, freq_max, freq_sample);

		
		//horizontal lines across screen
		for (double x = ft_min; x<= ft_max; x+= v_div) {
			g.setColor(Color.red);
			Vector pnt = convert_point_pos(new Vector(freq_min, x), freq_min, freq_max, ft_min, ft_max);
			
			g.drawLine((int)pnt.x, (int)pnt.y, (int)pnt.x + screen_width, (int)pnt.y);
			
			g.setColor(Color.black);
			
			g.drawString((double)Math.round(x * 1000) / 1000 + "", 20, (int)pnt.y);
			
		}
		
		//vertical lines along screen
		for (double x = freq_min; x<freq_max; x+=h_div) {
			g.setColor(Color.red);
			
			Vector pnt = convert_point_pos(new Vector(x, 0), freq_min, freq_max, ft_min, ft_max);
			
			g.drawLine((int)pnt.x, (int)pnt.y + screen_height, (int)pnt.x, (int)pnt.y - screen_height);
			
			g.drawString((int)x + "", (int)(pnt.x), (int)pnt.y + 20);
		}
		
		//g.drawLine(convert_pos, target, target, target);
		
		for (double x = freq_min; x<freq_max; x += freq_sample) {
			
			
			printft(g, x, 1, 0, bytes_piano, 50000, 55000, Color.black);
			//printft(g, x, 1, 0, bytes_sax2, 50000, 55000, Color.red);
			//printft(g, x, 1, 0, bytes_sax3, 50000, 55000, Color.green);

			//printft(g, x, 0, bytes_clarinet, 50000, 55000);
			//printft(g, x, -0.5, bytes_piano, 50000, 55000);
			

		}
		
		/*for (int x = 0; x<ft.length; x++) {
			g.setColor(Color.MAGENTA);
			
			Vector pnt = convert_point_pos(new Vector(ft[x].z, ft[x].length_2D()), freq_min, freq_max, ft_min, ft_max);
			
			g.fillOval((int)pnt.x - 5, (int)pnt.y - 5, 10, 10);
			
			g.setColor(Color.black);
			
			g.drawString("frequency: " + (double)Math.round(ft[x].z * 100) / 100, (int)pnt.x + 10, (int)pnt.y - 30);
			g.drawString("length: " + (double)Math.round(ft[x].length_2D() * 100000) / 100000, (int)pnt.x + 10, (int)pnt.y - 10);
			g.drawString("m: " + (double)Math.round(ft[x].z / ft[0].z * 10000) / 10000, (int)pnt.x + 10, (int)pnt.y + 10);
			g.drawString("θ: " + (double)Math.round(find_angle(ft[x]) * 100) / 100, (int)pnt.x + 10, (int)pnt.y + 30);


			
			//System.out.println("frequency: " + ft[x].z + " length: " + ft[x].length_2D() + " m: " + ft[x].z / ft[0].z);
		}*/
	}
	
	
	public static void printft(Graphics2D g, double x, double x_mult, double y_offset, double[] bytes, int start, int end, Color col) {
		g.setColor(col);
		draw_point(g, new Vector(x * x_mult, y_offset + ft(bytes, 50000, 55000, x).length_2D()), freq_min, freq_max, ft_min, ft_max);
		
		g.setColor(Color.magenta);
		//draw_point(g, new Vector(x, find_angle(ft(bytes, 50000, 55000, x))), freq_min, freq_max, -2 * Math.PI, 2 * Math.PI);
		//draw_point(g, new Vector(x, find_angle(ft(bytes, 50000, 55000, x)) - 2 * Math.PI), freq_min, freq_max, -2 * Math.PI, 2 * Math.PI);
		//draw_point(g, new Vector(x, find_angle(ft(bytes, 50000, 55000, x)) + 2 * Math.PI), freq_min, freq_max, -2 * Math.PI, 2 * Math.PI);
		
		g.setColor(Color.blue);
		//draw_point(g, new Vector(x, ft(bytes, 50000, 55000, x).x), freq_min, freq_max, ft_min, ft_max);

		g.setColor(Color.green);
		//draw_point(g, new Vector(x, ft(bytes, 50000, 55000, x).y), freq_min, freq_max, ft_min, ft_max);

	}
	public static void draw_point(Graphics2D g, Vector in, double x_min, double x_max, double y_min, double y_max) {
		Vector pnt = convert_point_pos(in, x_min, x_max, y_min, y_max);
		g.drawRect((int)pnt.x, (int)pnt.y, 1, 1);
	}
	
	public static Vector convert_point_pos(Vector in, double x_min, double x_max, double y_min, double y_max) {
		Vector out = new Vector(0, 0);
		out.x = (int)((in.x - x_min) / (x_max - x_min) * screen_width);
		out.y = (int)( -(in.y - y_min) / (y_max - y_min) * screen_height + screen_height);
		return out;
	}
	
	public static Vector ft(double[] bytes, int start, int end, double freq) {
		Vector temp = new Vector(0, 0, freq);
		
		for (int x = start; x<Math.min(end, bytes.length); x++) {
			temp.add(new Vector(bytes[x] * Math.cos(-4 * Math.PI * freq * (x-start) / samples_per_second), bytes[x] * Math.sin(-4 * Math.PI * freq * (x-start) / samples_per_second)));
		}
		temp.scale_2D(1.0 / (Math.min(end, bytes.length) - start));
		
		return temp;
	}
	public static Vector[] ft_array(double[] bytes, int start, int end, double freq_min, double freq_max, double step_size) {
		Vector[] out = new Vector[(int)((freq_max - freq_min) / step_size)];
		int index = 0;
		for (double x = freq_min; x<freq_max; x+=step_size) {
			out[index] = ft(bytes, start, end, x);
			index++;
			
			if (index >= out.length) break;
		}
		out = isolate_maxima(out);
		out = isolate_maxima(out);
		
		if (filter_non_multiples) out = filter_non_multiples(out);
		
		sort_vec_arr(out);
		
		return out;
	}
	public static Vector[] filter_non_multiples(Vector[] in) {
		Vector[] out = new Vector[0];
		
		double min_freq = 1;
		
		for (int x = 0; x<in.length; x++) {
			if (in[x].z > 200) {
				min_freq = in[x].z;
				break;
			}
		}
		
		for (int x = 0; x<in.length; x++) {
			if (Math.abs(in[x].z / min_freq - Math.round(in[x].z / min_freq)) > 0.12 || in[x].z < min_freq) continue;
			out = Vector.add_to_vec_arr(out, in[x]);
		}
		
		for (int x = 1; x<out.length; x++) {
			if (out[x].z - out[x - 1].z < min_freq / 2) {
				out = Vector.del_from_vec_arr(out, out[x].length() < out[x - 1].length() ? x : x - 1);
			}
		}
		
		return out;
	}
	public static Vector[] isolate_maxima(Vector[] in) {
		Vector[] out = new Vector[0];
		
		for (int x = 1; x< in.length - 1; x++) {
			if (in[x].length_2D() >= in[x - 1].length_2D() && in[x].length_2D() >= in[x + 1].length_2D()) {
				out = Vector.add_to_vec_arr(out, in[x]);
			}
		}		
		return out;
	}
	public static void sort_vec_arr(Vector[] in) {
		boolean sorted = true;
		do {
			sorted = true;
			for (int x = 1; x<in.length; x++) {
				
				if (in[x].z < in[x - 1].z) {
					Vector temp = in[x];
					in[x] = in[x - 1];
					in[x - 1] = temp;
					
					sorted = false;
				}
			}
		}while(!sorted);
	}
	
	
	//clarinet combo
	
	public static Vector[] combo(Vector[] in, Vector[] clars, int index) {
		Vector[] out = in;
		
		sort_vec_arr(out);
		
		//frequency, amplitude, phase
		Vector.add_to_vec_arr(clars, new Vector(in[index].z, in[index].length(), find_angle(in[index]) - Math.PI));
		
		for (int x = index; x<out.length; x++) {
			
		}
		
		
		
		
		
		
		return out;
	}
	
	
	public static double find_angle(Vector in) {
		double a = Math.atan(Math.abs(in.y / in.x));
		
		//quad 2
		if (in.y > 0 && in.x < 0) {
			return Math.PI - a;
		}
		
		//quad 3
		if (in.y < 0 && in.x < 0) {
			return - Math.PI + a;
		}
		
		//quad 4
		if (in.y < 0 && in.x > 0) {
			return - a;
		}
		
		//quad 1
		return a;
	}
	
	public static double[] readWavFile(String filePath) throws IOException, UnsupportedAudioFileException {
        File file = new File(filePath);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioInputStream.getFormat();
        int bytesPerFrame = format.getFrameSize();
        
        int totalFrames = (int) audioInputStream.getFrameLength();
        
        total_bytes = totalFrames * bytesPerFrame;
        
        byte[] audioBytes = new byte[totalFrames * bytesPerFrame];
        audioInputStream.read(audioBytes);
        
        samples_per_second = (int)(format.getFrameRate() * bytesPerFrame);
        
        System.out.println("SAMPLES PER SECOND: " + samples_per_second);
        
        //System.exit(0);
        

        // Convert bytes to audio samples
        double[] samples = new double[totalFrames];
        ByteBuffer buffer = ByteBuffer.wrap(audioBytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // PCM uses little-endian

        for (int i = 0; i < totalFrames; i++) {
            short sample = buffer.getShort(); // 16-bit PCM
            samples[i] = sample / 32768.0; // Normalize between -1 and 1
        }

        return samples;
    }
	public static void init() {
		frame = new JFrame();
		frame.setSize(screen_width, screen_height);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		pane = new JPanel();
		pane.setSize(frame.getWidth(), frame.getHeight() - frame.getInsets().top - frame.getInsets().bottom);
		frame.add(pane);
		
		System.out.println("f: " + frame.getWidth() + " " + frame.getHeight());
		System.out.println("p: " + pane.getWidth() + " " + pane.getHeight());
		
		try {Thread.sleep(2000);}catch(Exception e) {e.printStackTrace();}
		g = (Graphics2D) pane.getGraphics();
		
	}
}