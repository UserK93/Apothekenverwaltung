package com.example.demo;

import javax.persistence.*;

//Klasse als Entity für die Datenbank
@Entity
//Tabellenname der Datenbank
@Table(name="Apotheke")
public class Apotheke {

	public Apotheke() {
		super();
	}

	//Integer id als ID (Pharmazentralnummer) für die Datenbank festlegen
	//Autoincrement für ID


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer pharmazentralnummer;
	String produktname;
	String wirkstoff;
	String hersteller;
	int anzahl;


	//Konstruktor für Products

	/**
	 *
	 * @param pharmazentralnummer
	 * @param anzahl
	 * @param produktname
	 * @param wirkstoff
	 * @param hersteller
	 */
	public Apotheke(Integer pharmazentralnummer, int anzahl, String produktname, String wirkstoff, String hersteller) {
		super();
		this.pharmazentralnummer = pharmazentralnummer;
		this.anzahl = anzahl;
		this.produktname = produktname;
		this.hersteller = hersteller;
		this.wirkstoff = wirkstoff;

	}

	//Getter und Setter
	public int getPhNr() {
		return pharmazentralnummer;
	}
	public void setPhNr(Integer pharmazentralnummer) {
		this.pharmazentralnummer = pharmazentralnummer;
	}
	public int getAnzahl() {
		return anzahl;
	}
	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
	public String getProduktname() {return produktname; }
	public void setProduktname(String produktname) {
		this.produktname = produktname;
	}
	public String getWirkstoff() {return wirkstoff; }
	public void setWirkstoff(String wirkstoff) {
		this.wirkstoff = wirkstoff;
	}
	public String getHersteller() {return hersteller; }
	public void setHersteller(String hersteller) {
		this.hersteller = hersteller;
	}


	
	
	

}
