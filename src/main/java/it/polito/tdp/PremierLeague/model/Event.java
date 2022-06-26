package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event> {
	
	public enum EventType{
		GOAL,
		ESPULSIONE,
		INFORTUNIO
	}
	private EventType type;
	private int ordineAzione;
	
	public Event(EventType type, int ordineAzione) {
		super();
		this.type = type;
		this.ordineAzione = ordineAzione;
	}
	public EventType getType() {
		return type;
	}
	public int getOrdineAzione() {
		return ordineAzione;
	}
	@Override
	public int compareTo(Event o) {
		return this.getOrdineAzione()-o.getOrdineAzione();
	}
	
	
	
	

}
