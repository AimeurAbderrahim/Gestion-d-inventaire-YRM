package testpackage.model.core;

import java.time.LocalDateTime;

public class Bon {
	private String id_bon;
	private LocalDateTime dateBon;
	private boolean type;

	private int id_f;
	private String id_emplacement;

	public Bon(){
		this.type = false;
	}

	public Bon(String id_bon , LocalDateTime dateBon , boolean type , String id){
		this.id_bon = id_bon;
		this.dateBon = dateBon;
		if(type == false){
			this.id_emplacement = id;
			this.id_f = -1;
		}else{
			this.id_f = Integer.parseInt(id);
			this.id_emplacement = null;
		}
	}

	public String getIdBon(){
		return this.id_bon;
	}

	public void setDateBon(LocalDateTime date){
		this.dateBon = date;
	}

	public LocalDateTime getDateBon(){
		return this.dateBon;
	}

	public boolean isBonReception(){
		return this.type;
	}

	public String getReferenceId(){
		// NOTE: should parse string to integer
		if(this.type)
			return String.format("02%s" , this.id_f);
		return this.id_emplacement;
	}

}
