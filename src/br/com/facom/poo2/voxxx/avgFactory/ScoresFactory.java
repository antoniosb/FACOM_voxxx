package br.com.facom.poo2.voxxx.avgFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ScoresFactory {
	public static ScoreEstNumber getScoreEst(ScoreSpecies tipoScore) {
		if (tipoScore == null)
			return null;
		else if (tipoScore.equals(ScoreSpecies.TODAY))
			return new ScoreDay();
		else if (tipoScore.equals(ScoreSpecies.PEEK))
			return new ScorePeek();
		else if (tipoScore.equals(ScoreSpecies.DURATION))
			return new ScoreDuration();
		else if (tipoScore.equals(ScoreSpecies.ALL))
			return new ScoreAll();
		else
			return null;
	}

	@Test
	public void testDayInstance() {
		ScoreEstNumber test = getScoreEst(ScoreSpecies.TODAY);
		assertEquals(ScoreDay.class, test.getClass());
	}
	
	@Test
	public void testPeekInstance(){
		ScoreEstNumber test = getScoreEst(ScoreSpecies.PEEK);
		assertEquals(ScorePeek.class, test.getClass());	
	}
	
	@Test
	public void testDurationInstance(){
		ScoreEstNumber test = getScoreEst(ScoreSpecies.DURATION);
		assertEquals(ScoreDuration.class, test.getClass());
	}
	
	@Test
	public void testAllInstance(){
		ScoreEstNumber test = getScoreEst(ScoreSpecies.ALL);
		assertEquals(ScoreAll.class, test.getClass());
	}
	
	@Test
	public void testNullReturnsNull(){
		ScoreEstNumber test = getScoreEst(null);
		assertNull(test);
	}

}