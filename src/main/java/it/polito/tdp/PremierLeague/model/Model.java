package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private List<Player> giocatori;
	
	public List<Match> getAllMatches(){
		this.dao = new PremierLeagueDAO();
		return this.dao.listAllMatches();
	}
	
	public int getTeamByPlayer(int playerId, int matchId) {
		this.dao = new PremierLeagueDAO();
		return this.dao.getTeamByPlayer(playerId, matchId);
	}
	
	public List<Player> getPlayersByMatch(Match m){
		this.dao = new PremierLeagueDAO();
		giocatori = this.dao.getPlayersByMatch(m.getMatchID());
		return giocatori;
	}
	
	public List<Collegamento> getCollegamenti(Match m){
		this.dao = new PremierLeagueDAO();
		return this.dao.getCollegamentiByMatch(m.getMatchID());
	}
	
	public String creaGrafo(Match m){
		
		this.grafo = new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//AGGIUNGO I VERTICI
		
		Graphs.addAllVertices(grafo, this.getPlayersByMatch(m));
		
		//AGGIUNGO GLI ARCHI
		
		List<Collegamento> archi = this.getCollegamenti(m);
		for(Collegamento c : archi)
			Graphs.addEdgeWithVertices(grafo, c.getP1(), c.getP2(), c.getPeso());
		
		
		String result = "Grafo creato\n#VERTICI: " + grafo.vertexSet().size() + "\n#ARCHI: " + grafo.edgeSet().size();
		return result;
		
	}
	
	public String getGiocatoreMigliore() {
		if(this.grafo==null)
			return "Grafo non ancora creato";
		Player bestPlayer = null;
		double maxDelta=0;
		for(Player p : this.giocatori) {
			double delta=0;
			Set<DefaultWeightedEdge> uscenti = grafo.outgoingEdgesOf(p);	//DA SOMMARE
			Set<DefaultWeightedEdge> entranti = grafo.incomingEdgesOf(p);	//DA SOTTRARRE
			for(DefaultWeightedEdge e : uscenti)
				delta += grafo.getEdgeWeight(e);
			for(DefaultWeightedEdge e : entranti)
				delta -= grafo.getEdgeWeight(e);
			if(delta>maxDelta) {
				maxDelta = delta;
				bestPlayer = p;
			}
		}
		String result = "Giocatore migliore:\n" + bestPlayer + " con delta efficienza: " + maxDelta;
		return result;
		
	}
	
	public Player getBestPlayer() {
		if(this.grafo==null)
			return null;
		Player bestPlayer = null;
		double maxDelta=0;
		for(Player p : this.giocatori) {
			double delta=0;
			Set<DefaultWeightedEdge> uscenti = grafo.outgoingEdgesOf(p);	//DA SOMMARE
			Set<DefaultWeightedEdge> entranti = grafo.incomingEdgesOf(p);	//DA SOTTRARRE
			for(DefaultWeightedEdge e : uscenti)
				delta += grafo.getEdgeWeight(e);
			for(DefaultWeightedEdge e : entranti)
				delta -= grafo.getEdgeWeight(e);
			if(delta>maxDelta) {
				maxDelta = delta;
				bestPlayer = p;
			}
		}
		return bestPlayer;
		
	}
	
	public String Simulazione(int nAzioni, Match m) {
		Player p = this.getBestPlayer();
		int teamBestPlayer=0;
		int bestTeamID = this.getTeamByPlayer(p.getPlayerID(), m.getMatchID());
		if(bestTeamID == m.getTeamHomeID())
			teamBestPlayer=1;
		else teamBestPlayer=2;
		
		Simulatore s = new Simulatore();
		s.init(nAzioni, teamBestPlayer);
		Risultato r = s.run();
		String result = "#ESPULSI " + m.getTeamHomeNAME() + " : " + r.getEspulsiT1() +
				"\n#ESPULSI " + m.getTeamAwayNAME() + " : " + r.getEspulsiT2() +
				"\n#GOAL " + m.getTeamHomeNAME() + " : " + r.getGoalT1() +
				"\n#GOAL " + m.getTeamAwayNAME() + " : " + r.getGoalT2();
		return result;
	}
	
	
	
}
