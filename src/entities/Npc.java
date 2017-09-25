package entities;

import static main.Main.*;

import java.util.*;

import processing.core.*;
import res.*;
import terrain.*;
import util.*;

public class Npc extends Entity {

	protected String name;
	protected ArrayList<String> conversations = new ArrayList<>();
	private int conversationId = -1;

	public Npc(PVector pos, String name) {
		super(new Rectangle(pos, new PVector(20, 40)));
		this.name = name;
	}

	public boolean isTalking() {
		return conversationId != -1;
	}

	public void leaveTalking() {
		conversationId = -1;
	}

	public void talk() {
		conversationId++;
		if (conversationId >= conversations.size())
			conversationId = -1;
	}

	@Override
	public void onRender(PVector camera) {
		Processing.noStroke();
		Processing.fill(color.val);
		Processing.rect(hitbox.getX1() - camera.x, hitbox.getY1() - camera.y, hitbox.getWidth(), hitbox.getHeight());

		if (isTalking()) {

			float w = 350;
			float h = 120;
			float margin = 10;

			float x = 0;
			float y = 0;

			PVector playerpos = Processing.manager.getPlayer().getHitbox().getCenter();
			if (playerpos.x <= getHitbox().getX1()) {
				// render text on left
				x = hitbox.getCenterX() - camera.x - 20 - w;
			} else {
				// render text on right
				x = hitbox.getCenterX() - camera.x + 20;
			}
			y = hitbox.getY1() - camera.y - 20 - h;

			Processing.fill(Color.White.Transparent().val);
			Processing.stroke(Color.Black.Transparent().val);
			Processing.rect(x, y, w, h, 5);
			if (playerpos.x <= getHitbox().getX1()) {
				Processing.ellipse(x + w, y + h + 10, 20, 20);
			} else {
				Processing.ellipse(x, y + h + 10, 20, 20);
			}

			// conversation
			String text = conversations.get(conversationId);
			Processing.fill(Color.Black.Transparent().val);
			Processing.textSize(24);
			Processing.textAlign(PConstants.CENTER, PConstants.CENTER);
			Processing.text(text, x + margin, y + margin, w - 2 * margin, h - 2 * margin);

			// npc name
			Processing.textAlign(PConstants.LEFT, PConstants.BOTTOM);
			Processing.textSize(24);
			Processing.fill(Color.White.Transparent().val);
			Processing.text(name, x, y);
		} else {
			Processing.textAlign(PConstants.CENTER, PConstants.BOTTOM);
			Processing.fill(Color.White.Transparent().val);
			Processing.textFont(Fonts.Tw_Cen_MT, 24);
			Processing.text(name, hitbox.getCenterX() - camera.x, hitbox.getY1() - camera.y - 10);
		}
	}

}
