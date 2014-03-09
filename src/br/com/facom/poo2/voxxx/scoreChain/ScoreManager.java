package br.com.facom.poo2.voxxx.scoreChain;

import br.com.facom.poo2.voxxx.VoxxxScore;

public class ScoreManager implements ScoreHandler {
	
	ScoreHandler handler = null;
	

	public ScoreHandler getHandler() {
		return handler;
	}

	public void setHandler(ScoreHandler handler) {
		this.handler = handler;
	}

	@Override
	public void handleScore(VoxxxScore request) {
		handler.handleScore(request);
	}
	
	public static ScoreManager createChain(){
		ScoreManager mgr = new ScoreManager();
		ScoreLow low = new ScoreLow();
		ScoreMedium med = new ScoreMedium();
		ScoreHigh high = new ScoreHigh();
		
		mgr.setHandler(low);
		low.setNext(med);
		med.setNext(high);
		
		return mgr;
	}
		

}
