package br.com.facom.poo2.voxxx.avgFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScoreAll extends ScoreEstNumber {

	public ScoreAll(){
		this.query = "{}";
	}
	
	@Test
	public void testSetLimit(){
		setLimit(0);
		assertTrue(query.endsWith("&l=0"));
	}
	
	@Test
	public void testDefaultQuery(){
		assertEquals("{}", query);
	}
}
