package main;

import processing.core.*;
import res.*;
import util.*;

public class Main extends PApplet {

	public static Main Processing;

	public final int Width = 1280;
	public final int Height = 720;

	public boolean[] keys;
	public boolean keyEnter;
	public boolean keyEscape;
	public boolean keyBackspace;
	public boolean keyLeft;
	public boolean keyRight;
	public boolean keyUp;
	public boolean keyDown;

	public Manager manager;

	public boolean debug = false;

	public static void main(String[] args) {

		PApplet.main("main.Main");
	}

	@Override
	public void settings() {
		size(Width, Height);
		Processing = this;
	}

	@Override
	public void setup() {
		keys = new boolean[255];

		Color.Init();
		Fonts.Init();

		manager = new Manager();
		surface.setTitle("Happy Birthday!");
	}

	@Override
	public void draw() {
		background(120, 150, 210);

		manager.update();
		manager.render();
	}

	@Override
	public void keyTyped() {
		manager.getPlayer().updateOnKeyTyped(key);
	}

	@Override
	public void mousePressed() {
		manager.getPlayer().updateOnMousePressed(mouseButton);
	}

	@Override
	public void keyPressed() {
		key = Character.toLowerCase(key);
		if (key >= 0 && key <= 255)
			keys[key] = true;
		if (keyCode == ESC) {
			keyEscape = true;
			key = 0;
		}
		if (keyCode == BACKSPACE)
			keyBackspace = true;
		if (keyCode == UP)
			keyUp = true;
		if (keyCode == DOWN)
			keyDown = true;
		if (keyCode == LEFT)
			keyLeft = true;
		if (keyCode == RIGHT)
			keyRight = true;
		if (keyCode == ENTER)
			keyEnter = true;
	}

	@Override
	public void keyReleased() {
		if (key >= 0 && key <= 255) {
			keys[key] = false;
		}
		if (keyCode == ESC) {
			keyEscape = false;
		}
		if (keyCode == BACKSPACE) {
			keyBackspace = false;
		}
		if (keyCode == UP)
			keyUp = false;
		if (keyCode == DOWN)
			keyDown = false;
		if (keyCode == LEFT)
			keyLeft = false;
		if (keyCode == RIGHT)
			keyRight = false;
		if (keyCode == ENTER)
			keyEnter = false;
	}

	@Override
	public void dispose() {
		manager.savePlatforms();
	}

}
