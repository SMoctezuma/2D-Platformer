package finalGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

/**
 * Some notes: I fixed the fact that it wasn't moving. See the comments in
 * land.run(). For now it only jumps up and goes down with gravity, no X-axis
 * action. I also added collision with the bottom of the screen, which until we
 * have terrain and such is at Y=535. We should probably replace this with
 * general collision against terrain instead of just stopping the player at the
 * bottom of the screen, which I'll leave to Miguel to figure out.
 * 
 * @author Kit
 *
 */

public class FinalGame {

	public static void main(String[] args) throws InterruptedException {

		Window main = new Window();
		main.setup();
		main.world.setup();
		main.battle.scene.setup(Window.p1);

		// System.out.println("It is supposed to be moving. Velocity is "
		// +main.world.p1OW.velocity +" position is "
		// +main.world.p1OW.position);
		main.world.run(Window.p1);

	}
	

}

class Window extends JFrame {
	static land world = new land();
	static Player p1 = new Player("Player"); // TODO: make a player creation screen
	boolean battling = false;

	static fighter battle = new fighter();

	Window() {
		setVisible(true);
		setSize(500, 325);
		world.setSize(500, 325);
		world.map();
		this.add(world);// we didn't have these added in
		this.add(battle);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void setup() {
		//this.addKeyListener(p1);
		battle.b1.addActionListener(battle);
		battle.b2.addActionListener(battle);
		battle.b3.addActionListener(battle);

	}

	static void battler(int typeID) {
		world.setVisible(false);
		battle.setVisible(true);
		fighter.run(p1, typeID);
		lander();
	}

	static void lander() {
		battle.setVisible(false);
		world.setVisible(true);
		System.out.println("adding keylistener");
		//world.addKeyListener(Window.p1);
		System.out.println("running setup");
		world.setup();
		Window.p1.LEFT = false; //prevents you from getting stuck moving if you were in motion when you hit a monster
		Window.p1.RIGHT = false;
		Window.p1.UP = false; 
	}

}

class MoveAction extends AbstractAction
{
	String direction;
	
	MoveAction(String direction) {

        this.direction = direction;
    }
	
	@Override
	public void actionPerformed(ActionEvent ma) {
		System.out.println(direction);
		if (direction.equals("UP"))
		{
			Window.p1.UP = true;
		}
		if (direction.equals("SUP"))
		{
			Window.p1.UP = false;
		}
		if (direction.equals("RIGHT"))
		{
			Window.p1.RIGHT = true;
			Window.p1.FACINGL = false;
		}
		if (direction.equals("SRIGHT"))
		{
			Window.p1.RIGHT = false;
		}
		if (direction.equals("LEFT"))
		{
			Window.p1.LEFT = true;
			Window.p1.FACINGL = true;
		}
		if (direction.equals("SLEFT"))
		{
			Window.p1.LEFT = false;
		}
	}
}

class land extends JPanel {
	JPanel land = new JPanel();
	static ArrayList<Block> Units = new ArrayList<Block>();
	// Player p1 = new Player(); //stole this from your heli game code
	public Image raster;
	public Graphics rasterGraphics;
	java.awt.Image Background;
	static boolean GameOver;
	int i = 0;

	
	land() {
		setVisible(true);
		//learned all of this from http://stackoverflow.com/questions/22741215/how-to-use-key-bindings-instead-of-key-listeners and http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TextComponentDemoProject/src/components/TextComponentDemo.java
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( KeyStroke.getKeyStroke( "UP" ), "MOVE_UP"); //also here really helped http://stackoverflow.com/questions/30447944/java-key-bindings-not-working-actionperformed-not-being-called 
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "MOVE_RIGHT");//oh and here http://stackoverflow.com/questions/8524874/java-swing-key-bindings-missing-action-for-released-key
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "MOVE_LEFT");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released UP"), "STOP_UP");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released RIGHT"), "STOP_RIGHT");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released LEFT"), "STOP_LEFT");
		
		this.getActionMap().put("MOVE_UP", new MoveAction("UP"));
		this.getActionMap().put("MOVE_RIGHT", new MoveAction("RIGHT"));
		this.getActionMap().put("MOVE_LEFT", new MoveAction("LEFT"));
		this.getActionMap().put("STOP_UP", new MoveAction("SUP"));
		this.getActionMap().put("STOP_RIGHT", new MoveAction("SRIGHT"));
		this.getActionMap().put("STOP_LEFT", new MoveAction("SLEFT"));
	}

	public void map() {
		int iter = 1;
		File file = new File("Map.txt");
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String k = sc.nextLine();
				int i;
				for (i = 0; i < k.length(); i++) {
					if (k.charAt(i) == '1') {
						new Dirt(i * 25 - 250, 25 * iter);
					}
					if (k.charAt(i) == '2') {
						new Grass(i * 25 - 250, 25 * iter);
					}
					if (k.charAt(i) == '4') {
						new Cloud(i*25 - 250, 25* iter);
					}
					if (k.charAt(i) == '9') {
						new Castle(i * 25 - 250, 25 * iter);
					}
					if (k.charAt(i) == '8') {
						new MonBlock(i*25 - 250, 25* iter);
					}
					
				}

				iter++;
			}
			sc.close();
		} catch (FileNotFoundException e) {
		}
	}

	public void run(Player p1) {
		boolean running = true;
		while (running) {
//			i = 0;
//			while (i < 1000) {
				DrawEverything(rasterGraphics, p1);
				DrawObjects(rasterGraphics,Window.p1);
				Graphics frame = this.getGraphics(); // we needed to add this to
														// create the image
				frame.drawImage(raster, 0, 0, 800, 600, null); // and then this
																// to draw it in
																// the end
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				running = Move(); // moves everything
//				i++;
			}
		
		DrawEverything(rasterGraphics, p1);
		DrawObjects(rasterGraphics,Window.p1);
		DrawWin(rasterGraphics);
		Graphics frame = this.getGraphics(); 
		frame.drawImage(raster, 0, 0, 800, 600, null);
//			Window.battler();
//			Window.battle.run(Window.p1);
//			System.out.println("Switching back to the overworld");
//		}
	}

	public void paintComponent(Graphics g) {
		setup();
		rasterGraphics.setColor(Color.MAGENTA);
		rasterGraphics.fillRect(100, 100, 100, 100);
		DrawEverything(g, Window.p1);
		DrawWin(g);
	}

	public void setup() {
		//this.addKeyListener(Window.p1);
		raster = this.createImage(800, 622);
		rasterGraphics = raster.getGraphics();
		Background = this.createImage(800, 600);
		draw(Background.getGraphics());

	}

	public void draw(Graphics g) {
		DrawEverything(g, Window.p1);
		g.drawImage(Background, 0, 0, null);
	}

	public void DrawEverything(Graphics g, Player p1) {
		g.setColor(new Color(175,175,255));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		DrawObjects(g, p1);
		DrawWin(g);

	}
	
	public void DrawWin(Graphics g) {
		if (GameOver)
		{
		g.setColor(new Color(190,190,190,200));
		g.fillRect(0,0,1000,1000);
		g.setColor(Color.BLACK);
		g.setFont(new Font("sans", Font.BOLD, 22));
		g.drawString("You Win!", 245, 100);
		System.out.println("changing to GO screen");
		}
	}


	public boolean Move() {
		
		return Window.p1.move();
	}

	public void DrawObjects(Graphics g, Player p1) {
		Window.p1.draw(g, p1);
		for (int b = 0; b < Units.size(); b++) {
			Units.get(b).draw(g, p1);
		}

	}

}



class fighter extends JPanel implements ActionListener {
	static JPanel battleTop = new JPanel();
	JPanel battleBottom = new JPanel();
	JPanel battleButtons = new JPanel();
	static fightscene scene = new fightscene(battleTop);
	JPanel battle = new JPanel();
	Dimension d = new Dimension(600, 200);
	JButton b1 = new JButton("FIREBALL");
	JButton b2 = new JButton("THUNDERSHOCK");
	JButton b3 = new JButton("HEAL");
	static JTextArea battleText = new JTextArea(1, 20);

	// JButton b3 = new JButton("HEADBUTT");
	// JButton b4 = new JButton("KICK");

	static String attack = "none";

	fighter() {
		setVisible(false);
		setBackground(Color.RED);
		setSize(800, 600);
		TitledBorder battleTitle;
		battleTitle = BorderFactory.createTitledBorder("BATTLE");
		setBorder(battleTitle);
		setLayout(new GridLayout(2, 1));
		add(battleTop);
		battleTop.setSize(this.getWidth(), this.getHeight() - 200);
		battleTop.setLayout(new BorderLayout());
		battleTop.add(scene);

		add(battleBottom);
		battleBottom.setBackground(Color.LIGHT_GRAY);
		battleBottom.setSize(this.getWidth(), 200);
		battleBottom.setLayout(new BorderLayout());
		battleBottom.add(battleButtons);
		battleButtons.setLayout(new GridLayout(1,3));
		battleButtons.add(b1);
		battleButtons.add(b2);
		battleButtons.add(b3);
		battleText.setEditable(false);
		battleBottom.add(battleText, BorderLayout.NORTH);
	}

	public static void run(Player p1, int typeID) {
		boolean battling = true;
		p1.PsetMaxHP(); //player set HP to whatever it should be for the level
		scene.monsters.remove(0);
		scene.setMonster(p1, typeID);
		while (battling) 
		{
			scene.run(p1);
			battling = scene.Fight(attack, p1); // reset attack
		}
		scene.didLevel(p1.setLevel(scene.aftermath(p1)), p1.getLevel()); 
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			attack = "fireball";
		} else if (e.getSource() == b2) {
			attack = "thundershock";
		}else if (e.getSource() == b3) {
			attack = "heal";
			System.out.println("healing");
		}

	}

} // end of fighter

class fightscene extends JPanel {
	JPanel scene = new JPanel();
	public Image raster;
	public Graphics rasterGraphics;
	java.awt.Image Background;
	boolean playerTurn = true;
	ArrayList<Monster> monsters = new ArrayList<Monster>();

	public fightscene(JPanel battlescene) {
		setVisible(true);
		setSize(600, 200);
		monsters.add(new Monster(scene, 1, 1));
	}

	public void setMonster(Player p1, int typeID) // must be run before every fight
	{
		if (typeID != 8)
		{
			System.out.println("typeID is " +typeID);
			int monNum = (int) (Math.random() * 5 + 1); // number from 1 - 5
			int monLevel = (int) (Math.random() * (p1.getLevel() - 1) + 1);
			monsters.add(new Monster(scene, monNum, monLevel));
		}
		else
			
		{
			System.out.println("should be setting to the boss");
			monsters.add(new Boss(scene, 0, 5));
		}
	}

	public void paintComponent(Graphics g, Player p1) {
		super.paintComponent(g);
		setup(p1);
	}

	public void setup(Player p1) {
		// this.addKeyListener(p1OW); //going to become an action listener
		raster = this.createImage(800, 622);
		rasterGraphics = raster.getGraphics();
		Background = this.createImage(800, 600);
		DrawAll(Background.getGraphics(), p1);
		DrawBKG(rasterGraphics);
		DrawFighters(rasterGraphics, p1);
	}

	public void run(Player p1) {
		Move(p1); // moves the fighters
		DrawAll(rasterGraphics, p1);
		Graphics frame = this.getGraphics(); // we needed to add this to create
												// the image
		frame.drawImage(raster, 0, 0, 800, 600, null); // and then this to draw
														// it in the end
		try {
			Thread.sleep(333);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int aftermath(Player p1) {
		Creature winner = p1;
		Creature loser = p1;
		DrawAll(rasterGraphics, p1);
		if (p1.getHP() > 0) {
			winner = p1;
			loser = monsters.get(0);
			if(monsters.get(0).typeID==8)
			{
				p1.bossAlive=false;
			}
		} else {
			winner = monsters.get(0);
			loser = p1;
		}
		fighter.battleText.setText(winner.name + " defeated " + loser.name
				+ "!");
		Graphics frame = this.getGraphics(); // we needed to add this to create
												// the image
		frame.drawImage(raster, 0, 0, 800, 600, null); // and then this to draw
														// it in the end
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (p1.getHP() > 0)
			return monsters.get(0).getMaxHP();
		else
		{
			p1.die();
			return 0;
		}
	}

	public void didLevel(boolean levelUp, int level) {
		if (levelUp) {
			fighter.battleText
					.setText("You leveled up to level " + level + "!");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void Move(Player p1) {
		monsters.get(0).move(scene); // calls the method that moves the monster and passes it the scene so it can get the size.
		p1.moveBattle();
	}

	public void DrawAll(Graphics g, Player p1) {
		DrawBKG(g);
		DrawFighters(g, p1);
		DrawStats(g, p1);
	}

	public void DrawBKG(Graphics g) {
		g.setColor(new Color(0, 125, 125));
		g.fillRect(0, 0, 800, 600);
	}

	public void DrawFighters(Graphics g, Player p1) {
		monsters.get(0).drawBattle(g);
		p1.drawBattle(g);
	}

	public boolean Fight(String attack, Player p1) {
		// first you move, then the monster moves
		int damage = 0;
		int healing = 0;
		System.out.println("attack is " + attack + " and player turn is "
				+ playerTurn);
		if (playerTurn) // if it's the player's turn
		{
			if (attack.equals("fireball")) // a hard attack
			{
				damage = attack(3 + p1.getLevel(), 6 * p1.getLevel()); // 66% chance of hitting, up to 6 damage (for level one)
				playerTurn = false; // only set turn to false for next round if player moved
			} else if (attack.equals("thundershock")) // an easy attack
			{
				damage = attack(10 + p1.getLevel(), 2 * p1.getLevel()); // 90% chance of hitting, up to 2 damage
				playerTurn = false;
			}
			if (attack.equals("thundershock") | attack.equals("fireball")) {
				if (damage > 0)
					fighter.battleText.setText("You " + attack
							+ " the monster for " + damage + " damage!");
				else
					fighter.battleText.setText("You try to " + attack
							+ " the monster, but miss!");
			}
			if (attack.equals("heal"))
			{
				healing = (int)(2*((Math.random()*((float)p1.getMaxHP())/2f) + 1));
				fighter.battleText.setText("You heal yourself for " +healing +" HP!");
				p1.setHPLower(-healing);
			}
			monsters.get(0).setHPLower(damage);
			fighter.attack = "none";
			if (monsters.get(0).getHP() <= 0)
				playerTurn = true;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else { // the monster's turn

			damage = 0;

			System.out.println("monster is attacking");
			int monAttack = (int) (Math.random() + .5); // 1 or 0
			if (monAttack == 0) // a hard attack
			{
				damage = attack(3 + monsters.get(0).getLevel(), 6 * monsters.get(0).getLevel()); // 66% chance of hitting, up to 6
												// damage
				playerTurn = true;
			} else // an easy attack
			{
				damage = attack(10 + monsters.get(0).getLevel(), 2 * monsters.get(0).getLevel()); // 90% chance of hitting, up to 2
												// damage
				playerTurn = true;

			}
			if (damage > 0)
				fighter.battleText.setText(monsters.get(0).name
						+ " attacks you for " + damage + " damage!");
			else
				fighter.battleText.setText(monsters.get(0).name
						+ " tried to attack you, but missed!");
			p1.setHPLower(damage);
			if (p1.getHP() <= 0)
				playerTurn = false;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		if (p1.getHP() > 0 && monsters.get(0).getHP() > 0) {
			fighter.battleText.setText("You have " + p1.getHP()
					+ " HP left. The opponent " + monsters.get(0).name
					+ " has " + monsters.get(0).getHP() + " HP left.");
			return true;
		} else {
			playerTurn = true;// set it so the next battle starts with the
								// player's turn again
			return false;
		}

	}

	public int attack(int hitdie, int dmgdie) {
		int damage = 0;
		int hit; // going to equal an int or 0
		hit = (int) (Math.random() * hitdie); // for example, if it's a 50/50
												// chance of hitting, hitdie is
												// 2 because it's a 1 in 2
												// chance of not hitting. If
												// there's a 90% chance of
												// hitting, it's 1 in 10 to not
												// hit, so hitdie is 10
		if (hit != 0) // if it does equal 0, then it continues on with damage =
						// 0
		{
			damage = (int) (Math.random() * dmgdie + 1); // always +1 so that
															// you never do 0
															// damage if you hit
		}
		return damage;
	}

	public void DrawStats(Graphics g, Player p1) {
		// TODO: Make it so that when the HP gets lower it changes color.
		// displays the background HP bar
		g.setColor(Color.GRAY);
		g.fillRect(0, (this.getHeight()), 200, 10);
		g.fillRect((this.getWidth()) - 200, (this.getHeight()), 200, 10);
		g.setColor(Color.BLACK);

		// num to display the percentage of HP graphically
		float p1PercentHP = (float) p1.getHP() / (float) p1.getMaxHP();
		float monPercentHP = (float) monsters.get(0).getHP()
				/ (float) monsters.get(0).getMaxHP();

		// displays the actual HP
		g.setColor(Color.GREEN);
		g.fillRect(0, (this.getHeight()), (int) (200 * p1PercentHP), 10);
		g.fillRect((this.getWidth()) - 200, (this.getHeight()),
				(int) (200 * monPercentHP), 10);
		g.setColor(Color.WHITE);
		g.drawString("Player HP: " + p1.getHP() + "/" + p1.getMaxHP() + " Lvl."
				+ p1.getLevel() + " (" + p1.getEXP() + " EXP)", 0,
				this.getHeight() - 10);
		g.drawString(
				"Foe HP: " + monsters.get(0).getHP() + "/"
						+ monsters.get(0).getMaxHP() + " Lvl."
						+ monsters.get(0).getLevel(), this.getWidth() - 200,
				this.getHeight() - 10);

	}
}

abstract class Block {

	Vector2D velocity;
	Vector2D position;
	Rectangle R;
	int Count;
	boolean gravity;
	int typeID;

	public Block() {
		typeID = 0; // 0 = nothing/sky 1 = player/monster 2 = dirt 3 = grass 4 = cloud 5 = miniboss image 6 = miniboss collision 7 = boss image 8 = boss collision 9 = castle

	}

	abstract public void draw(Graphics g, Player p1);

	public void resolveCollision(Block B) {
		
		int ty = 25;
		int tx = 14;
		// <-the tolerance
		if (this.position.getY() + this.R.getHeight() - ty <= B.position.getY()) {
			this.position
					.setY((float) ((B.position.getY() - this.R.getHeight())));
			this.velocity.setY(0);
			this.Count = 0;

			if (this.velocity.getX() > 0) {

				velocity = velocity.multiply(.9f);
			} else if (this.velocity.getX() < 0) {

				velocity = velocity.multiply(.9f);
			}

		}

		else if (this.position.getY() + 10 >= B.R.getY() + B.R.getHeight()) {

			this.position.setY((float) ((B.position.getY() + B.R.getHeight() + 1)));
			this.velocity.setY(0);

		} else if (this.position.getX() + this.R.getWidth() - tx <= B.R.getX()) {
			
			System.out.println(this.position);
			System.out.println(B.position);
			System.out.println(B.position +"" +this.position);
			this.position.setX((float) ((B.position.getX() - this.R.getWidth())));
			this.velocity.setX(0);

		} else if (this.position.getX() + tx > B.position.getX()
				+ B.R.getWidth()) {

			this.position
					.setX((float) ((B.position.getX() + B.R.getWidth())));
			this.velocity.setX(0);

		}

	}
}

abstract class Creature extends Block {
	private int HP;
	private int MaxHP;
	String name;

	Creature() {
		typeID = 1;
	}

	public void draw(Graphics g) {

	}

	public void Move() {

	}

	public int getMaxHP() {
		return MaxHP;
	}

	public void setMaxHP(int maxHP) {
		MaxHP = maxHP;
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

	public void setHPLower(int hit) {
		HP -= hit;
		if (HP < 0)
			HP = 0;
		if (HP > getMaxHP())
			HP = getMaxHP();
	}

	public void setHPZero(int hP) {
		HP = 0;
	}

}

class Player extends Creature{
	
	int ScrollX;
	int ScrollY;
	long UPDELAY = 0;

	boolean UP, RIGHT, LEFT, FACINGL; // FACINGL = facing left
	int battleSequence;
	int OWSequence;
	int X, Y; // (For position in battle)
	private int experience;
	private int level;
	private Image p1Basic;
	private Image p1Walk1;
	boolean bossAlive;
	private Image p1Walk2;
	private Image p1BasicL;
	private Image p1Walk1L;
	private Image p1Walk2L;
	private Image p1Battle1;
	private Image p1Battle2;
	private Image CurBattleImage; // Current battle image
	private Image p1Walker;
	long DELAY4 = 0;
	Player(String name) {
		// import all of the images
		p1Basic = new ImageIcon("art/player/p1Basic.png").getImage();
		p1Walk1 = new ImageIcon("art/player/p1Walk1.png").getImage();
		p1Walk2 = new ImageIcon("art/player/p1Walk2.png").getImage();
		p1BasicL = new ImageIcon("art/player/p1BasicL.png").getImage();
		p1Walk1L = new ImageIcon("art/player/p1Walk1L.png").getImage();
		p1Walk2L = new ImageIcon("art/player/p1Walk2L.png").getImage();
		p1Battle1 = new ImageIcon("art/player/battle/p1Battle1.png").getImage();
		p1Battle2 = new ImageIcon("art/player/battle/p1Battle2.png").getImage();
		p1Walker = p1Basic;
		bossAlive = true;
		
		CurBattleImage = p1Battle1;
		//
		// land.Units.add(this);
		R = new Rectangle(0, 0, 0, 0); //added defining the rectangles in the constructors, not draw
		gravity = true;
		position = new Vector2D(25,800);
		velocity = new Vector2D();
		battleSequence = 0;
		OWSequence = 0; // overworld sequence
		X = 100;
		Y = 10;
		setMaxHP(15);
		setHP(getMaxHP());
		this.name = name;
		experience = 0;
		Count = 0;
		level = (int) (experience / 50) + 1; //(int) (experience / 50) + 1;
	}

	public void die() {
		position.setX(0);
		position.setY(0);
		setLevelOne(); //sets the player's position back and the level/EXP back to 0
		setHP(getMaxHP());
	}

	public void draw(Graphics g, Player p1) 
	{
		ScrollX=(int)position.getX()-25-200; //added ScrollX and utilized it
		ScrollY=(int)position.getY()-40-125;
		g.drawImage(p1Walker, (int) position.getX()-ScrollX,(int) position.getY()-ScrollY, 25, 40, null);
	}

	public void PsetMaxHP() {
		setMaxHP((int) (30 * (float) (((float)level) / 2f)));
	}

	public void drawBattle(Graphics g) {
		if (getHP() > 0)
			g.drawImage(CurBattleImage, X, Y, 70, 100, null);
		else
			g.fillRect(X, Y + 30, 100, 70);
		g.setColor(Color.WHITE);
		g.drawString(name, X, Y);

	}

	public void setLevelOne() // also sets experience
	{
		experience = 0;
		level = 1;
	}
	
	public boolean setLevel(int newEXP) // also sets experience
	{
		int oldLevel = level; // holds current level to be compared later
											  //2, 3,  4,  5,  6,   7,   8,   9,   10
		int ExpCap = 5 * (oldLevel*oldLevel); //5, 20, 45, 80, 125, 180, 245, 320, 405
		experience += newEXP;
		if (experience >= ExpCap)
		{
			level++;
		}
//		level = (int) (experience / ExpCap) + 1;
		if (oldLevel < level) // if level increased
		{
			System.out.println("you leveled up to level " + level + "!");
			return true;
		} else
			return false;
	}

	public int getLevel() {
		return level;
	}

	public int getEXP() {
		return experience;
	}

	public void moveBattle() {
		if (battleSequence == 0) {
			CurBattleImage = p1Battle1;
			battleSequence++; // moves to the next sequence for the next time
								// move is called
		} else {
			CurBattleImage = p1Battle2;
			battleSequence = 0;
		}
	}

	public boolean move() {

		for (Block b : land.Units) {
			
			if (b != this && this.R.intersects(b.R)) {
			
				this.resolveCollision(b);
				if (b.typeID == 3) //grass
				{
					
					if(DELAY4+3000<System.currentTimeMillis())
					{
						
					int chance = (int) (Math.random() * 100);
					if (chance < 40)
						Window.battler(0);
						DELAY4 = System.currentTimeMillis();
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (b.typeID == 9)
				{
					System.out.println("You made it to the end!");
					land.GameOver = true;
					return false;
				}
				if (b.typeID == 8 && bossAlive)
				{
					Window.battler(b.typeID);
					
				}
			} 
		}
		
		if (FACINGL)
			p1Walker = p1Walk2L;
		else
			p1Walker = p1Walk2;
		
		if (UP) {
			System.out.println("should be moving");
			if (UPDELAY + 400 < System.currentTimeMillis() && Count < 2) {
				UPDELAY = System.currentTimeMillis();
				velocity = velocity.add(new Vector2D(0, -10f));
				if(Count==1)
					velocity = velocity.add(new Vector2D(0, -5f));
				Count++;
			}
			if (FACINGL)
				p1Walker = p1Walk2L;
			else
				p1Walker = p1Walk2;

		}

		if (RIGHT) {
			System.out.println("should be moving");
			velocity = velocity.add(new Vector2D(.3f, 0));
			if (OWSequence >= 0 && OWSequence < 5) {
				p1Walker = p1Basic;
				OWSequence++;
			} else if (OWSequence >= 5 && OWSequence < 10) {
				p1Walker = p1Walk1;
				OWSequence++;
			} else if (OWSequence >= 10 && OWSequence < 15) {
				p1Walker = p1Basic;
				OWSequence++;
			} else if (OWSequence >= 15 && OWSequence < 20) {
				p1Walker = p1Walk2;
				if (OWSequence < 19)
					OWSequence++;
				else
					OWSequence = 0;
			}
		}

		if (LEFT) {
			System.out.println("should be moving");
			velocity = velocity.add(new Vector2D(-.3f, 0));
			if (OWSequence >= 0 && OWSequence < 5) {
				p1Walker = p1BasicL;
				OWSequence++;
			} else if (OWSequence >= 5 && OWSequence < 10) {
				p1Walker = p1Walk1L;
				OWSequence++;
			} else if (OWSequence >= 10 && OWSequence < 15) {
				p1Walker = p1BasicL;
				OWSequence++;
			} else if (OWSequence >= 15 && OWSequence < 20) {
				p1Walker = p1Walk2L;
				if (OWSequence < 19)
					OWSequence++;
				else
					OWSequence = 0;
			}
		}

		if (velocity.getY() > 5) {
			velocity.setY(5);
		}
		if (velocity.getX() > 5) {
			velocity.setX(5);
		}
		if (velocity.getY() < -40) {
			velocity.setY(-40);
		}
		if (velocity.getX() < -5) {
			velocity.setX(-5);
		}

		if (gravity) {
			velocity = velocity.add(new Vector2D(0, .5f));
		}

		position = position.add(new Vector2D(velocity.getX(), (float) velocity.getY()));
		R = new Rectangle((int)position.getX(), (int)position.getY(), 25, 40);
		if(position.getY()>1400)
		{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			die();
		}
		 //true is for still moving, still running the game


//		if (move.equals("UP")) {
//			UP = true;
//		}
//		if (move.equals("RIGHT")) {
//			RIGHT = true;
//			FACINGL = false;
//		}
//		if (move.equals("LEFT")) {
//			LEFT = true;
//			FACINGL = true;
//		}
		return true;
	}

}

class Move extends AbstractAction {

    String direction;

    Move(String direction) {

        this.direction = direction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Same as the move method in the question code.
        // Player can be detected by e.getSource() instead and call its own move method.
    }
}

class Monster extends Creature {
	int X, Y;
	int sequence;
	Image Programmer1;
	Image Programmer2;
	Image icon;
	Image icon2;
	Image CurBatMon; // currentBattleMonster
	private int monNum;
	private int level;

	Monster(JPanel scene, int num, int level) {
		Programmer1 = new ImageIcon("art/monsters/programmer1.png").getImage();
		Programmer2 = new ImageIcon("art/monsters/programmer2.png").getImage();
		this.monNum = num;
		this.level = level;
		setMaxHP((int) (((int) ((monNum / 2) + 1)) * (10 * (float) (((float)level) / 2)))); //(int) (((int) ((monNum / 2) + 1)) * (10 * (float) (((float)level) / 2)))
		setHP(getMaxHP());
		X = 310;
		Y = 10;
		sequence = 0;
		if (monNum == 1) {
			name = "Cat";
			icon = Programmer1;
			icon2 = Programmer2;
			CurBatMon = icon;
		} else if (monNum == 2) {
			name = "Evil Turtle";
			icon = Programmer1;
			icon2 = Programmer2;
			CurBatMon = icon;
		} else if (monNum == 3) {
			name = "Sentient Blob of Ants";
			icon = Programmer1;
			icon2 = Programmer2;
			CurBatMon = icon;
		} else if (monNum == 4) {
			name = "Tired Programmer";
			icon = Programmer1;
			icon2 = Programmer2;
			CurBatMon = icon;
		} else if (monNum == 5) {
			name = "WereWolf";
			icon = Programmer1;
			icon2 = Programmer2;
			CurBatMon = icon;
		} else {
			name = "NoMonster";
			icon = Programmer1;
			icon2 = Programmer2;
			CurBatMon = icon;
		}
	}

	public int getLevel() {
		return level;
	}

	public void drawBattle(Graphics g) {
		if (getHP() > 0)
			g.drawImage(CurBatMon, X, Y, 70, 100, null);
		else
			g.fillRect(X, Y + 30, 100, 70);
		g.setColor(Color.WHITE);
		g.drawString(name, X, Y);
	}

	public void move(JPanel scene) {
		// the position in the sequence of three positions
		if (sequence == 0) {
			CurBatMon = icon;
			sequence++; // moves to the next sequence for the next time move is
						// called
		} else if (sequence == 1) {
			CurBatMon = icon2;
			sequence = 0;
		}
	}

	@Override
	public void draw(Graphics g, Player p1) {
		// TODO Auto-generated method stub
		
	}
}

class Boss extends Monster
{
	Boss(JPanel scene, int num, int level) {
		super(scene, 5, level);
		typeID = 8;
		name = "Pikachowser";
		level = 5;
	}
	
	public void draw(Graphics g, Player p1)
	{
		g.setColor(Color.MAGENTA);
		g.drawRect((int)( position.getX()  -p1.ScrollX), (int) position.getY() -p1.ScrollY - 2, 25, 25);
	}
}

class Terrain extends Block {
	public Terrain() {
		land.Units.add(this);
	}

	public void draw(Graphics g, Player p1) {

	}

}

class MonBlock extends Terrain //activates collision for monster
{
	public MonBlock(int X, int Y) {
		typeID = 8;
		R = new Rectangle(X-75, Y-175, 100, 200); //moved this up to the constructor
		position = new Vector2D(X-75, Y-175);
	}

	public void draw(Graphics g, Player p1) {
		// g.setColor(Color.YELLOW);
		g.setColor(Color.MAGENTA);
		g.drawRect((int)( position.getX()  -p1.ScrollX), (int) position.getY() -p1.ScrollY - 2, 100, 200);
		// g.fillRect((int)position.getX(),(int)position.getY(),100,50);
	}
}

class Cloud extends Terrain {

	public Cloud(int X, int Y) {
		
		typeID = 4;
		R = new Rectangle(X, Y, 25, 25); //moved this up to the constructor
		position = new Vector2D(X, Y);
	}

	public void draw(Graphics g, Player p1) {
		g.setColor(Color.WHITE);
		g.fillRect((int)( position.getX()  -p1.ScrollX), (int) position.getY() -p1.ScrollY - 2, 25, 25);
		// g.fillRect((int)position.getX(),(int)position.getY(),100,50);
	}

}

class Dirt extends Terrain {
	private Image dirt;

	public Dirt(int X, int Y) {
		
		typeID = 2;
		R = new Rectangle(X, Y, 25, 25); //moved this up to the constructor
		position = new Vector2D(X, Y);
		dirt = new ImageIcon("art/blocks/dirt.png").getImage();
	}

	public void draw(Graphics g, Player p1) {
		// g.setColor(Color.YELLOW);
		
		
		g.drawImage(dirt, (int)( position.getX()  -p1.ScrollX), (int) position.getY() -p1.ScrollY - 2, null);
		// g.fillRect((int)position.getX(),(int)position.getY(),100,50);
	}

}

class Grass extends Terrain {
	private Image grass;

	public Grass(int X, int Y) {
		typeID = 3;
		R = new Rectangle(X, Y, 25, 25);
		position = new Vector2D(X, Y);
		grass = new ImageIcon("art/blocks/grass.png").getImage();

	}

	public void draw(Graphics g, Player p1) {
		// TODO Auto-generated method stub
		g.drawImage(grass, (int)( position.getX() -p1.ScrollX), (int) position.getY() -p1.ScrollY - 21, null);
	}
	


}

class Castle extends Terrain {
	private Image grass;

	public Castle(int X, int Y) {
		typeID = 9;
		R = new Rectangle(X, Y, 25, 25);
		position = new Vector2D(X, Y);
		grass = new ImageIcon("art/blocks/grass.png").getImage();

	}

	public void draw(Graphics g, Player p1) {
		g.setColor(Color.MAGENTA);
		g.fillRect((int) position.getX() -p1.ScrollX, (int) position.getY()-p1.ScrollY, 25, 25);
	}
	
}

class Vector2D // copied from DrawWithoutPaintMultiballWithCollision.java,
				// original Author: Zaheer Ahmed
{
	private float x;
	private float y;

	public Vector2D() {
		this.setX(0);
		this.setY(0);
	}

	public Vector2D(float x, float y) {
		this.setX(x);
		this.setY(y);
	}

	public void set(float x, float y) {
		this.setX(x);
		this.setY(y);
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	// Specialty method used during calculations of ball to ball collisions.
	public float dot(Vector2D v2) {
		float result = 0.0f;
		result = this.getX() * v2.getX() + this.getY() * v2.getY();
		return result;
	}

	public float getLength() {
		return (float) Math.sqrt(getX() * getX() + getY() * getY());
	}

	public Vector2D add(Vector2D v2) {
		Vector2D result = new Vector2D();
		result.setX(getX() + v2.getX());
		result.setY(getY() + v2.getY());
		return result;
	}

	public Vector2D subtract(Vector2D v2) {
		Vector2D result = new Vector2D();
		result.setX(this.getX() - v2.getX());
		result.setY(this.getY() - v2.getY());
		return result;
	}

	public Vector2D multiply(float scaleFactor) {
		Vector2D result = new Vector2D();
		result.setX(this.getX() * scaleFactor);
		result.setY(this.getY() * scaleFactor);
		return result;
	}

	// Specialty method used during calculations of ball to ball collisions.
	public Vector2D normalize() {
		float length = getLength();
		if (length != 0.0f) {
			this.setX(this.getX() / length);
			this.setY(this.getY() / length);
		} else {
			this.setX(0.0f);
			this.setY(0.0f);
		}
		return this;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}