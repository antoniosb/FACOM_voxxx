package br.com.facom.poo2.voxxx.avgFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScoreDuration extends ScoreEstNumber {
	
	private int howMany = 0;

	public ScoreDuration( ){
		this.query = "{duration:{$gte:"+getHowMany()+"}}";
	}

	public int getHowMany() {
		return howMany;
	}

	public void setHowMany(int howMany) {
		this.howMany = (howMany < 0)? 0 : howMany;
	}
	
	@Test
	public void testSetLimit(){
		setLimit(0);
		assertTrue(query.endsWith("&l=0"));
	}
	
	@Test
	public void testDefaultQuery(){
		assertEquals("{duration:{$gte:0}}", query);
	}
	
}