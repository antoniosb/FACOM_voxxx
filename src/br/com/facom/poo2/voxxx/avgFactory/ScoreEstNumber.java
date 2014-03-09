package br.com.facom.poo2.voxxx.avgFactory;

public abstract class ScoreEstNumber {
	protected String query;
	
	public String getQuery(){
		return query;
	}
	
	public void setLimit(int limit){
		this.query += "&l="+limit;
	}
}
