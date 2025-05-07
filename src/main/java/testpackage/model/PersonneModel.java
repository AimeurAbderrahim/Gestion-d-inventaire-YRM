package testpackage.model;

import testpackage.model.core.Personne;

import java.util.HashMap;

public class PersonneModel<K,V> {
	private HashMap<String,Personne> personneTab ;

	public PersonneModel() {
		this.personneTab = new HashMap<String,Personne>();
	}

	public HashMap<String, Personne> getPersonneTab() {
		return personneTab;
	}

	public void setPersonneTab(HashMap<String, Personne> personneTab) {
		this.personneTab = personneTab;
	}

	public void ajoutPersonne(Personne personne){
		personneTab.put(personne.getNom()+" "+personne.getPrenom(),personne) ;
	}
}
