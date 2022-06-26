package it.polito.tdp.PremierLeague.model;

import java.util.PriorityQueue;

import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Simulatore {
	
	//PARAMETRI DI OUTPUT
	private int goalT1;
	private int goalT2;
	private int espulsiT1;
	private int espulsiT2;
	//PARAMETRI DELLA SIMULAZIONE
	private int nAzioni;
	private int azioniAggiunte;
	
	//PARAMETRI DELLO STATO DEL MONDO
	private int giocatoriT1;
	private int giocatoriT2;
	private int teamGiocatoreMigliore;
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	
	public void init(int nAzioni, int teamGiocatoreMigliore) {
		this.azioniAggiunte=0;
		this.goalT1=0;
		this.goalT2=0;
		this.giocatoriT1=11;
		this.giocatoriT2=11;
		this.teamGiocatoreMigliore = teamGiocatoreMigliore;
		this.nAzioni = nAzioni;
		this.queue = new PriorityQueue<Event>();
		for(int i = 0;i<nAzioni;i++) {
			Event e = this.generaEvento();
			queue.add(e);	
		}
		
		
	}
	
	public Risultato run() {
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			eventHandler(e);
		}
		this.espulsiT1=11-this.giocatoriT1;
		this.espulsiT2=11-this.giocatoriT2;
		Risultato result = new Risultato(this.goalT1, this.goalT2, this.espulsiT1, this.espulsiT2);
		return result;
	}
	
	public void eventHandler(Event e) {
		switch(e.getType()) {
		case GOAL:
			//GOAL++ A TEAM CON + GIOCATORI
			if(this.giocatoriT1>this.giocatoriT2)
				this.goalT1++;
			else if(this.giocatoriT2>this.giocatoriT1)
				this.goalT2++;
				else if (this.teamGiocatoreMigliore==1)
					this.goalT1++;
						else this.goalT2++;
			break;
		case ESPULSIONE:
			double random = Math.random();
			if(random<0.6) {
				if(this.teamGiocatoreMigliore==1)
					this.giocatoriT1--;
				else this.giocatoriT2--;
			}
			else {
				if(this.teamGiocatoreMigliore==1)
					this.giocatoriT2--;
				else this.giocatoriT1--;
			}
			break;
		case INFORTUNIO:
			double random1 = Math.random();
				Event e1 = this.generaEvento();
				Event e2 = this.generaEvento();
				queue.add(e1);
				queue.add(e2);
				if(random1>0.5) {
					Event e3 = this.generaEvento();
					queue.add(e3);
				}
			
			break;
		}
	}
	
	public Event generaEvento() {
		Event e = null;
		double random = Math.random();
		if(random<0.5) {	//GOAL
			e = new Event(EventType.GOAL, queue.size());
		}
		else if (random<0.8) {	//ESPULSIONE
			e = new Event(EventType.ESPULSIONE, queue.size());
		}
		else {	//INFORTUNIO
			e = new Event(EventType.INFORTUNIO, queue.size());
		}
		return e;
	}

}
