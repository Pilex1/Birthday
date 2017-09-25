package util;

import static main.Main.*;

import java.io.*;

public class Color implements Serializable {

	public static Color Transparent;

	public static Color White;
	public static Color LightGrey;
	public static Color Grey;
	public static Color DarkGrey;
	public static Color Black;

	public static Color LightBlue;
	public static Color MediumBlue;
	public static Color DeepBlue;
	public static Color DarkBlue;
	public static Color LightGreen;
	public static Color MediumGreen;
	public static Color DeepGreen;

	public static Color LightPurple;
	public static Color Purple;
	public static Color Red;
	public static Color LightOrange;

	private static int Alpha = 128;

	public static void Init() {

		LightOrange = new Color(225, 235, 178);
		Purple = new Color(110, 56, 255);
		LightPurple = new Color(206, 142, 255);
		Red = new Color(255, 58, 104);

		Transparent = new Color(255, 255, 255, 0);

		White = new Color(255);
		LightGrey = new Color(192);
		Grey = new Color(128);
		DarkGrey = new Color(64);
		Black = new Color(0);

		LightBlue = new Color(22, 79, 184);
		MediumBlue = new Color(37, 101, 218);
		DeepBlue = new Color(136, 190, 244);
		DarkBlue = new Color(38, 66, 112);
		LightGreen = new Color(22, 184, 79);
		MediumGreen = new Color(37, 218, 101);
		DeepGreen = new Color(136, 244, 190);
	}

	public int val;

	public Color(java.awt.Color color) {
		this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public Color(int r, int g, int b, int a) {
		val = Processing.color(r, g, b, a);
	}

	public Color(int r, int g, int b) {
		this(r, g, b, 255);
	}

	public Color(int grey) {
		this(grey, grey, grey);
	}

	public int red() {
		return (int) Processing.red(val);
	}

	public int green() {
		return (int) Processing.green(val);
	}

	public int blue() {
		return (int) Processing.blue(val);
	}

	public int alpha() {
		return (int) Processing.alpha(val);
	}

	public Color Transparent() {
		int r = (int) Processing.red(val);
		int g = (int) Processing.green(val);
		int b = (int) Processing.blue(val);
		return new Color(r, g, b, Alpha);
	}

	public Color copy() {
		return new Color((int) Processing.red(val), (int) Processing.green(val), (int) Processing.blue(val),
				(int) Processing.alpha(val));
	}

}
