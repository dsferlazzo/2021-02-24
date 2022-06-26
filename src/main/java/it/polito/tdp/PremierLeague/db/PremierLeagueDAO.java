package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Collegamento;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> getPlayersByMatch(int matchId){
		String sql = "SELECT DISTINCT p.* "
				+ "FROM actions a, players p "
				+ "WHERE p.PlayerID=a.PlayerID AND a.MatchID=?";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, matchId);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Team> listAllTeams(){
		String sql = "SELECT * FROM Teams";
		List<Team> result = new ArrayList<Team>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				result.add(team);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * ritorna la lista di tutti i matches nel database
	 * @return
	 */
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID "
				+ "ORDER BY m.MatchID asc";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Collegamento> getCollegamentiByMatch(int matchId){
		String sql = "WITH player_score "
				+ "AS "
				+ "	( "
				+ "	SELECT DISTINCT p.*,a.TeamID, (a.TotalSuccessfulPassesAll+ a.Assists)/a.TimePlayed AS score "
				+ "	FROM actions a, players p "
				+ "	WHERE p.PlayerID=a.PlayerID AND a.MatchID=? "
				+ "	) "
				+ "SELECT ps1.PlayerID AS id1, ps1.Name AS n1, ps2.PlayerID AS id2, ps2.Name AS n2, ps1.score-ps2.score AS peso "
				+ "FROM player_score ps1, player_score ps2 "
				+ "WHERE (ps1.score>ps2.score AND ps1.TeamID<>ps2.TeamID) OR "
				+ "	(ps1.score=ps2.score AND ps1.TeamID<>ps2.TeamID AND ps1.PlayerID>ps2.PlayerID)";
		List<Collegamento> result = new ArrayList<Collegamento>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, matchId);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Player p1 = new Player(res.getInt("id1"), res.getString("n1"));
				Player p2 = new Player(res.getInt("id2"), res.getString("n2"));
				Collegamento c = new Collegamento(p1, p2, res.getDouble("peso"));
				result.add(c);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getTeamByPlayer(int playerId, int matchId) {
	String sql = "SELECT DISTINCT a.TeamID "
			+ "FROM actions a "
			+ "WHERE a.PlayerID=? AND a.MatchID=?";
	Connection conn = DBConnect.getConnection();
	int result=-1;
	try {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, playerId);
		st.setInt(2, matchId);
		ResultSet res = st.executeQuery();
		res.next();
		result = res.getInt("TeamId");
		conn.close();
		return result;
		
	} catch (SQLException e) {
		e.printStackTrace();
		return -1;
		}
	}
}
