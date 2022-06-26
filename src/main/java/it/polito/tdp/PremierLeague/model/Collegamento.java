package it.polito.tdp.PremierLeague.model;

public class Collegamento {

	private Player p1;
	private Player p2;
	private double peso;
	public Collegamento(Player p1, Player p2, double peso) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.peso = peso;
	}
	public Player getP1() {
		return p1;
	}
	public Player getP2() {
		return p2;
	}
	public double getPeso() {
		return peso;
	}
	
	
}
