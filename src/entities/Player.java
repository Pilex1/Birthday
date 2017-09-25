package entities;

import static main.Main.*;

import main.*;
import processing.core.*;
import res.*;
import terrain.*;
import util.*;

public class Player extends Npc {

	private enum PlatformId {
		Regular, VBounce, HBounce, Invisible, Phantom, Checkpoint;

		PlatformId incr() {
			int id = ordinal();
			id++;
			if (id >= PlatformId.values().length) {
				id = 0;
			}
			return PlatformId.values()[id];
		}

		PlatformId decr() {
			int id = ordinal();
			id--;
			if (id < 0) {
				id = PlatformId.values().length - 1;
			}
			return PlatformId.values()[id];
		}
	};

	private enum Editing {
		Playing, EditPos, EditSize, Removing
	}

	private float npcRange = 300;
	private boolean flying = false;
	private Editing editing = Editing.Playing;
	private PlatformId platformId = PlatformId.Regular;

	private PVector defaultSpawn;

	private boolean tutorialMove = false;
	private boolean tutorialTalk = false;

	private int deaths = 0;

	public Player() {
		super(new PVector(150, Manager.Floor), "");
		// super(new PVector(5600, Manager.Floor), "");

		defaultSpawn = getHitbox().getPos().copy();
		color = Color.White;
	}

	public boolean isRemoving() {
		return editing == Editing.Removing;
	}

	public boolean isEditing() {
		return editing == Editing.EditPos || editing == Editing.EditSize;
	}

	public PVector getPosTopLeft() {
		return new PVector(getHitbox().getCenterX() - Main.Processing.Width / 2,
				getHitbox().getCenterY() - Main.Processing.Height / 2);
	}

	private void handleInputs() {
		useGravity = !flying;
		if (Processing.keys['w']) {
			tutorialMove = true;
			if (flying) {
				flyUp();
			} else {
				jump();
			}
		}
		if (Processing.keys['s']) {
			tutorialMove = true;
			if (flying) {
				flyDown();
			}
		}
		if (Processing.keys['a']) {
			tutorialMove = true;
			if (flying) {
				flyLeft();
			} else {
				strafeLeft();
			}
		}
		if (Processing.keys['d']) {
			tutorialMove = true;
			if (flying) {
				flyRight();
			} else {
				strafeRight();
			}
		}
	}

	private void checkRespawning() {
		// respawning
		if (hitbox.getCenterY() >= 12000) {
			Checkpoint c = Processing.manager.getActiveCheckpoint();
			if (c == null) {
				hitbox.setPos(defaultSpawn);
			} else {
				teleportToCheckpoint(c);
			}
			setVel(new PVector(0, 0));
			Processing.manager.resetBlocks();
			deaths++;
		}
	}

	public PVector getMousePos() {
		float mx = Processing.mouseX + hitbox.getCenterX() - Processing.Width / 2;
		float my = Processing.mouseY + hitbox.getCenterY() - Processing.Height / 2;
		return new PVector(mx, my);
	}

	private void renderDebug() {
		Processing.fill(Color.White.val);
		Processing.textFont(Fonts.Tw_Cen_MT, 32);
		Processing.textAlign(PConstants.LEFT, PConstants.TOP);

		String debug = "";
		debug += "P: " + StringUtil.beautifyString(hitbox.getCenter()) + "\n";
		debug += "MP: " + StringUtil.beautifyString(getMousePos()) + "\n";
		debug += "V: " + StringUtil.beautifyString(vel) + "\n";
		if (isEditing()) {
			debug += "EP: " + StringUtil.beautifyString(Processing.manager.getGuidePos()) + "\n";
			debug += "ES: " + StringUtil.beautifyString(Processing.manager.getGuideSize()) + "\n";
		} else if (isRemoving()) {
			debug += "RP: " + StringUtil.beautifyString(Processing.manager.getGuideRemovalPos()) + "\n";
			debug += "RS: " + StringUtil.beautifyString(Processing.manager.getGuideRemovalSize()) + "\n";
		}
		Processing.text(debug, 0, 0);
	}

	private PVector blockify(PVector v, int x) {
		return new PVector((int) (v.x / x) * x, (int) (v.y / x) * x);
	}

	private void handleEditing() {
		Processing.fill(Color.White.val);
		Processing.textFont(Fonts.Tw_Cen_MT, 32);
		Processing.textAlign(PConstants.CENTER, PConstants.BOTTOM);
		Processing.text(platformId.name(), Processing.Width / 2, Processing.Height);

		PVector mousePos = getMousePos();
		if (editing == Editing.EditPos) {
			Processing.manager.setGuidePos(blockify(mousePos, 10));
		} else if (editing == Editing.EditSize) {
			PVector offset = PVector.sub(mousePos, Processing.manager.getGuidePos());
			if (offset.x >= 0)
				offset.x = Math.max(offset.x, 50);
			else
				offset.x = Math.min(offset.x, -50);
			if (offset.y >= 0)
				offset.y = Math.max(offset.y, 50);
			else
				offset.y = Math.min(offset.y, -50);
			Processing.manager.setGuideSize(blockify(offset, 50));
		}
	}

	public Platform getSelectedPlatform() {
		Platform[] platforms = Processing.manager.getActivePlatforms();
		Platform selected = null;
		for (Platform p : platforms) {
			PVector mousePos = getMousePos();
			if (p.getHitbox().inside(mousePos)) {
				selected = p;
				break;
			}
		}
		return selected;
	}

	private void handleRemoving() {
		Platform selected = getSelectedPlatform();
		if (selected == null) {
			Processing.manager.setGuideRemovalSize(new PVector());
			Processing.manager.setGuideRemovalPos(new PVector());
			return;
		}
		Processing.manager.setGuideRemovalPos(selected.getHitbox().getPos());
		Processing.manager.setGuideRemovalSize(selected.getHitbox().getSize());
	}

	public void leaveAllTalking() {
		for (Npc npc : Processing.manager.getAllNpcs()) {
			npc.leaveTalking();
		}
	}

	@Override
	protected void onUpdate() {
		handleInputs();
		checkRespawning();
		Npc npc = Processing.manager.getClosestNpc(this);
		if (npc != null && npc.getDistanceTo(this) > npcRange) {
			leaveAllTalking();
		}

		if (Processing.debug) {
			if (isEditing()) {
				handleEditing();
			} else if (isRemoving()) {
				handleRemoving();
			}
			renderDebug();
		}

		if (tutorialMove == false) {
			Processing.fill(Color.White.val);
			Processing.textFont(Fonts.Tw_Cen_MT, 32);
			Processing.textAlign(PConstants.CENTER, PConstants.TOP);

			Processing.text("Press W, A, S, D to move around.", Processing.Width / 2, 20);
		} else if (tutorialTalk == false) {
			Processing.fill(Color.White.val);
			Processing.textFont(Fonts.Tw_Cen_MT, 32);
			Processing.textAlign(PConstants.CENTER, PConstants.TOP);

			Processing.text("Press ENTER when near an NPC to interact.", Processing.Width / 2, 20);
		}

		Processing.fill(Color.White.val);
		Processing.textFont(Fonts.Tw_Cen_MT, 24);
		Processing.textAlign(PConstants.RIGHT, PConstants.BOTTOM);
		Processing.text("Deaths: " + deaths, Processing.Width - 10, Processing.Height - 10);
	}

	private void setEditing(Editing editing) {
		if (isEditing()) {
			Processing.manager.setGuideSize(new PVector(50, 50));
		}
		this.editing = editing;
	}

	public void updateOnMousePressed(int mouseButton) {
		if (editing == Editing.EditPos) {
			editing = Editing.EditSize;
			switch (platformId) {
			case Checkpoint:
				PVector pos = Processing.manager.getGuidePos();
				float y = pos.y;
				y += 50;
				y -= Checkpoint.stickHeight + Checkpoint.flagHeight - Checkpoint.offset;
				float x = pos.x;
				Processing.manager.addPlatform(new Checkpoint(new PVector(x, y)));
				setEditing(Editing.EditPos);
			default:
				break;
			}
		} else if (editing == Editing.EditSize) {
			PVector guidePos = Processing.manager.getGuidePos();
			PVector guideSize = Processing.manager.getGuideSize();
			Rectangle rect = new Rectangle(guidePos, guideSize).regularise();
			for (int i = 0; i < rect.getWidth() / 50; i++) {
				for (int j = 0; j < rect.getHeight() / 50; j++) {
					PVector pos = rect.getPos().copy();
					pos.x += i * 50;
					pos.y += j * 50;
					switch (platformId) {
					case Regular:
						Processing.manager.addPlatform(new Platform(pos));
						break;
					case VBounce:
						Processing.manager.addPlatform(new VBouncePlatform(pos));
						break;
					case Checkpoint:
						break;
					case Invisible:
						Processing.manager.addPlatform(new InvisiblePlatform(pos));
						break;
					case Phantom:
						Processing.manager.addPlatform(new PhantomPlatform(pos));
						break;
					case HBounce:
						Processing.manager.addPlatform(new HBouncePlatform(pos));
						break;
					default:
						break;
					}
				}
			}
			editing = Editing.EditPos;
			Processing.manager.setGuideSize(new PVector(50, 50));
		} else if (editing == Editing.Removing) {
			Platform selected = getSelectedPlatform();
			if (selected != null) {
				Processing.manager.removePlatform(selected);
			}
		}
	}

	public void teleportToCheckpoint(Checkpoint c) {
		setVel(new PVector(0, 0));
		hitbox.setX1(c.getHitbox().getX1());
		hitbox.setY2(c.getHitbox().getY2());
	}

	public void updateOnKeyTyped(int key) {
		if (Processing.keyEnter) {
			Npc npc = Processing.manager.getClosestNpc(this);
			if (npc != null) {
				if (npc.getDistanceTo(this) <= npcRange) {
					npc.talk();
					tutorialTalk = true;
				}
			}
		}
		if (Processing.debug) {
			if (Processing.keyEscape) {
				setEditing(Editing.Playing);
			}
			if (key == 'f') {
				flying = !flying;
			}
			if (key == 'e') {
				setEditing(Editing.EditPos);
			}
			if (key == 'r') {
				setEditing(Editing.Removing);
			}
			if (key == 'j') {
				platformId = platformId.decr();
			}
			if (key == 'l') {
				platformId = platformId.incr();
			}
			if (key == 'u') {
				Checkpoint c = Processing.manager.getPreviousCheckpoint(this);
				if (c != null) {
					teleportToCheckpoint(c);
					;
				}
			}
			if (key == 'o') {
				Checkpoint c = Processing.manager.getNextCheckpoint(this);
				if (c != null) {
					teleportToCheckpoint(c);
				}
			}
		}
	}
}
