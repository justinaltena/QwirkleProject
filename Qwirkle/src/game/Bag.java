package game;

import java.util.List;
import java.util.Random;

import game.Stone.Color;
import game.Stone.Shape;

import java.util.ArrayList;
import java.util.Arrays;

public class Bag {
	private static int NUMBEROFDUPLICATES = 3;
	protected List<Stone> bag = new ArrayList<Stone>();
	
	public Bag() {
		createBag();
	}
	
	
	public void createBag() {
		List<Shape> shapes = Arrays.asList(Shape.values());
		List<Color> colors = Arrays.asList(Color.values());
		for(int i = 0; i < NUMBEROFDUPLICATES; i++) {
			for(Shape e: shapes) {
				for(Color f: colors) {
					if(!e.equals(Shape.EMPTY) && !f.equals(Color.EMPTY)) {
					bag.add(new Stone(e, f));
					}
				}
			}
		}
	}
	
	public void resetBag() {
		bag.clear();
		createBag();
	}
	
	

	public Stone takeStone() {
		Stone resStone;
		Random rand = new Random();
		int value = rand.nextInt(bag.size()-1);
		resStone = bag.get(value);
		bag.remove(value);
		return resStone;

	}
	
	public void addStone(Stone stone) {
		bag.add(stone);
	}
	
	
	public Stone tradeStone(Stone stone) {
			Stone tradedStone = takeStone();
			bag.add(stone);
			return tradedStone;
	}
	
	public int getSize() {
		return bag.size();
	}
	
	public static void main(String[] args) {
		Bag bag = new Bag();
		StringBuilder sb = new StringBuilder();
		System.out.println(bag.takeStone().toString());
		
	}
}
