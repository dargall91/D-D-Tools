package tracker;

import monster.Monster;
import player.*;
import encounter.MonsterData;
import java.io.*;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Combatant implements Comparable<Combatant>, Serializable {
	private boolean monster;
	private boolean reinforcement;
	private int initiative, bonus, quantity;
	private int weight; //used to break absolute ties (both initiative and bonus are ==), higher weight == higher initiative
	private PlayerCharacter pc;
	//MonsterData monData;
	private boolean complete; //used to track if tiebreaker has finished executing
	private String[] ac, hp;
	private int breaker;
	private boolean alive[];
	private String name;

	Combatant(PlayerCharacter pc, int initiative) {
		this.pc = pc;
		monster = false;
		reinforcement = false;
		this.initiative = initiative;
		breaker = 0;
		complete = false;
		weight = 0;
		ac = new String[1];
		ac[0] = Integer.toString(pc.getAC());
	}
	
	Combatant(MonsterData monData, Monster mon) {
		//this.monData = monData;
		name = monData.getMonster();
		monster = true;
		reinforcement = monData.isReinforcement();
		bonus = mon.getInitiativeBonus();
		initiative = monData.getInitiative() + bonus;
		breaker = 0;
		complete = false;
		this.quantity = monData.getQuantity();
		ac = new String[quantity];
		hp = new String[quantity];
		alive = new boolean[quantity];
		
		for (int i = 0; i < quantity; i++)
			setAC(i, mon.getAC());
			
		for (int i = 0; i < quantity; i++)
			setHP(i, mon.getHP());

		for (int i = 0; i < quantity; i++)
			alive[i] = true;
			
		weight = 0;
	}

	/**
	 * Constructor used for adding in monsters from outsid the predefined encounter
	 * @param monster
	 */
	Combatant(Monster mon, int quantity, int initiative) {
		name = mon.getName();
		monster = true;
		reinforcement = false;
		bonus = mon.getInitiativeBonus();
		this.initiative = initiative + bonus;
		breaker = 0;
		complete = false;
		this.quantity = quantity;
		ac = new String[quantity];
		hp = new String[quantity];
		alive = new boolean[quantity];

		for (int i = 0; i < quantity; i++)
			setAC(i, mon.getAC());

		for (int i = 0; i < quantity; i++)
			setHP(i, mon.getHP());

		for (int i = 0; i < quantity; i++)
			alive[i] = true;

		weight = 0;
	}

	public void initFromJson(JSONObject json) {
		//TODO: is this even needed?
	}
	
	public String getName() {
		if (monster)
			return name;
		
		return pc.getName();
	}
	
	public int getInitiative() {
		return initiative;
	}
	
	public int getBonus() {
		if (monster)
			return bonus;
			
		return pc.getBonus();
	}
	
	public int getQuantity() {
		if (monster)
			return quantity;
			
		return 1;
	}
	
	public int getBreaker() {
		return breaker;
	}
	
	public boolean isMonster() {
		return monster;
	}
	
	public boolean isReinforcement() {
		return reinforcement;
	}
	
	public String getAC(int index) {
		if (monster)
			return ac[index];
			
		return ac[0];
	}

	/**
	 * Gets the Hit Points of the combatant
	 * @param index THe index of the monster, only used if there is more than 1 of this smonster
	 * @return The HP of the monster, or 999 if this is a PC
	 */
	public String getHP(int index) {
		if (monster)
			return hp[index];
		
		return "999";
	}
	
	private boolean complete() {
		return complete;
	}
	
	public void setPlayerCharacter(PlayerCharacter pc) {
		this.pc = pc;
	}
	
	public void setInitiative(int initiative) {
		this.initiative = initiative;
	}
	
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	
	public void setMonster(boolean monster) {
		this.monster = monster;
	}
	
	public void setReinforcement(boolean reinforcement) {
		this.reinforcement = reinforcement;
	}
	
	public void setAC(int index, String ac) {
		if (monster)
			this.ac[index] = ac;
		
		else
			this.ac[0] = ac;
	}
	
	public void setHP(int index, String hp) {
		this.hp[index] = hp;
	}
	
	public void increaseWeight() {
		weight++;
	}
	
	public int getWeight() {
		return weight;
	}
	
	private void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	public void setBreaker(int breaker) {
		this.breaker = breaker;
	}

	/**
	 * For now, should only be used for monsters, since PCs have a chance of revival
	 *
	 * @param index
	 */
	public void kill(int index) {
		alive[index] = false;
	}

	/**
	 * For now, should only be used for monsters, since PCs have a chance of revival
	 *
	 * @param index
	 */
	public void revive(int index) {
		alive[index] = true;
	}

	/**
	 * Checks if this combatant is alive or dead. A PC is considered always alive for now, may update in future
	 *
	 * @param index
	 * @return true if alive, false otherwise
	 */
	public boolean isAlive(int index) {
		if (monster)
			return alive[index];

		else
			return true;
	}
	
	private class TieBreaker {
		TieBreaker(Combatant c) {
			final Integer[] list = new Integer[21];
		
			for (int i = 0; i < 21; i++)
				list[i] = i;

			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setAlignmentX(Component.LEFT_ALIGNMENT);
			panel.setMinimumSize(new Dimension(300, 100));
			panel.setMaximumSize(new Dimension(300, 100));
			panel.setPreferredSize(new Dimension(300, 100));
			
			JPanel labels = new JPanel();
			labels.setLayout(new BoxLayout(labels, BoxLayout.X_AXIS));
			labels.setAlignmentX(Component.CENTER_ALIGNMENT);
			JLabel name = new JLabel(getName());
			name.setMinimumSize(new Dimension(150, 30));
			name.setMaximumSize(new Dimension(150, 30));
			name.setPreferredSize(new Dimension(150, 30));
			JLabel cName = new JLabel(c.getName());
			cName.setMinimumSize(new Dimension(150, 30));
			cName.setMaximumSize(new Dimension(150, 30));
			cName.setPreferredSize(new Dimension(150, 30));
			
			JPanel boxes = new JPanel();
			boxes.setLayout(new BoxLayout(boxes, BoxLayout.X_AXIS));
			boxes.setAlignmentX(Component.CENTER_ALIGNMENT);
			JComboBox weightBox = new JComboBox(list);
			weightBox.setMinimumSize(new Dimension(50, 20));
			weightBox.setMaximumSize(new Dimension(50, 20));
			weightBox.setPreferredSize(new Dimension(50, 20));
			JComboBox cWeightBox = new JComboBox(list);
			cWeightBox.setMinimumSize(new Dimension(50, 20));
			cWeightBox.setMaximumSize(new Dimension(50, 20));
			cWeightBox.setPreferredSize(new Dimension(50, 20));
			
			weightBox.setSelectedIndex(getBreaker());
			cWeightBox.setSelectedIndex(c.getBreaker());
			
			labels.add(name);
			labels.add(Box.createRigidArea(new Dimension(50, 0)));
			labels.add(cName);
			
			boxes.add(weightBox);
			boxes.add(Box.createRigidArea(new Dimension(125, 0)));
			boxes.add(cWeightBox);
			
			panel.add(labels);
			panel.add(Box.createRigidArea(new Dimension(0, 5)));
			panel.add(boxes);
			panel.add(Box.createRigidArea(new Dimension(0, 5)));
			
			Object[] options = {"OK"};
			int result = JOptionPane.showOptionDialog(null, panel, "Tie Breaker", JOptionPane.PLAIN_MESSAGE,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			if (result == JOptionPane.OK_OPTION) {
				setComplete(true);// = true;
				setBreaker((Integer) weightBox.getSelectedItem());
				c.setBreaker((Integer) cWeightBox.getSelectedItem());
			}
		}
	}
	
	//weighs this combatant against another to determine turn order
	public void weigh(Combatant c) {
		setComplete(false);
		
		//use total initiaitve to determine turn order
		if (getInitiative() > c.getInitiative()) {
			increaseWeight();
			return;
		}
			
		if (getInitiative() < c.getInitiative()) {
			c.increaseWeight();
			return;
		}
		
		//in the event of an initiative tie, priority goes to combatant with higher initiative bonus
		if (getBonus() > c.getBonus()) {
			increaseWeight();
			return;
		}
			
		if (getBonus() < c.getBonus()) {
			c.increaseWeight();
			return;
		}
		
		//handle further ties, then compare weights
		if (getBreaker() == 0 || c.getBreaker() == 0 || getBreaker() == c.getBreaker()) {
			TieBreaker tieBreaker = new TieBreaker(c);
		}
		
		else
			setComplete(true);
		
		while(!complete());
		
		//ensure 0 values weren't entered, also ensure not tied again
		if (getBreaker() == 0 || c.getBreaker() == 0 || getBreaker() == c.getBreaker()) {
			weigh(c);
			return;
		}

		if (getBreaker() > c.getBreaker()) {
			increaseWeight();
			return;
		}
			
		if (getBreaker() < c.getBreaker())
			c.increaseWeight();
	}

	public void reset() {
		weight = 0;
	}

	/**
	 * Used for sorting, combatants must be weighed first
	 *
	 * @param c The combatant to compare this one to
	 * @return 1 if this > c, -1 if this < c, 0 if tie
	 */
	public int compareTo(Combatant c) {
		if (weight > c.getWeight())
			return 1;
		
		if (weight < c.getWeight())
			return -1;
		
		return 0;
	}

	/**
	 * Stringifies the JSONObject created by toSimpleJson
	 * @return stringified JSONObject compatible with SimpleCombatant.java
	 */
	public String toSimpleJsonString() {
		String result = "{}";

		try {
			result = toSimpleJson().toString(4);
		} catch (Exception e) {
			System.out.println("Error in Combatant.toJsonString: " + e.getMessage());
		}

		return result;
	}

	/**
	 * Converts a combatant into a json string compatible with SimpleCombatant
	 * @return a JSONObject with only the information needed by SimpleCombatant.java
	 */
	public JSONObject toSimpleJson() {
		JSONObject obj = new JSONObject();

		obj.put("reinforcement", reinforcement);
		obj.put("name", getName());
		obj.put("initiative", getInitiative());
		obj.put("weight", getWeight());

		return obj;
	}
}
