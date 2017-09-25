package terrain;

import static main.Main.*;

import processing.core.*;
import util.*;

public class Checkpoint extends Platform {

	public static final String Id = "C";

	public static float stickHeight = 120;
	public static float stickWidth = 20;
	public static float flagHeight = 60;
	public static float flagWidth = 60;
	public static float offset = 20;

	private boolean checked = false;

	public Checkpoint(PVector pos) {
		super(pos);
		hitbox.setWidth(flagWidth);
		hitbox.setHeight(flagHeight + stickHeight - offset);
		solid = false;
	}

	@Override
	public String getId() {
		return Id;
	}

	public boolean isChecked() {
		return checked;
	}

	@Override
	public void onRender(PVector camera) {
		Processing.stroke(Color.Black.val);
		Processing.fill(Processing.color(20, 20, 30));
		Processing.rect(hitbox.getX1() - camera.x, hitbox.getY2() - stickHeight - camera.y, stickWidth, stickHeight);

		if (checked) {
			Processing.fill(Color.MediumGreen.val);
		} else {
			Processing.fill(Color.Red.val);
		}
		Processing.triangle(hitbox.getX1() - camera.x, hitbox.getY1() - camera.y, hitbox.getX1() + flagWidth - camera.x,
				hitbox.getY1() + flagHeight / 2 - camera.y, hitbox.getX1() - camera.x,
				hitbox.getY1() + flagHeight - camera.y);
	}

	@Override
	public void onUpdate() {
		if (isIntersecting(Processing.manager.getPlayer())) {
			for (Checkpoint c : Processing.manager.getAllCheckpoints()) {
				c.checked = false;
			}
			checked = true;
		}
	}
}
