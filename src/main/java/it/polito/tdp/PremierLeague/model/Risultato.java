package it.polito.tdp.PremierLeague.model;

public class Risultato {
	
	private int goalT1;
	private int goalT2;
	private int espulsiT1;
	private int espulsiT2;
	public Risultato(int goalT1, int goalT2, int espulsiT1, int espulsiT2) {
		super();
		this.goalT1 = goalT1;
		this.goalT2 = goalT2;
		this.espulsiT1 = espulsiT1;
		this.espulsiT2 = espulsiT2;
	}
	public int getGoalT1() {
		return goalT1;
	}
	public int getGoalT2() {
		return goalT2;
	}
	public int getEspulsiT1() {
		return espulsiT1;
	}
	public int getEspulsiT2() {
		return espulsiT2;
	}
	
	

}
