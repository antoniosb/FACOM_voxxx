package br.com.facom.poo2.voxxx.avgFactory;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class ScoreDay extends ScoreEstNumber {
	
	private final String pattern = "yyyy-MM-dd";
	private final DateFormat df = new SimpleDateFormat(pattern, Locale.US);
	
	private String theDay = null;
	
	public String getTheDay() {
		return (theDay == null)? df.format(new Date()) : theDay;
	}

	public void setTheDay(String theDay) {
		this.theDay = theDay;
	}

	public ScoreDay(){
//		String today = df.format(score.getStartTimeAsDate());
		this.query = "{recordDay:\"" + getTheDay() + "\"}";
	}
	
	@Test
	public void testSetLimit(){
		setLimit(0);
		assertTrue(query.endsWith("&l=0"));
	}
	
	
	@Test
	public void testDefaultQuery(){
		assertEquals("{recordDay:\"2014-02-23\"}", query);
	}

}
