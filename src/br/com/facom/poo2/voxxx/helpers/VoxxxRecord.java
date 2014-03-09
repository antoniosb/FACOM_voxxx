package br.com.facom.poo2.voxxx.helpers;

import java.io.Serializable;
import java.util.Date;

public class VoxxxRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fileName;
	private String pathToStream;
	private long startTime = 0;
	private double duration = 0;
	private int peeks = 0;
	private String deviceId = "records";
	
	public String getDeviceId() {
		return deviceId;
	}

	private void setDeviceId(String deviceId) {
		this.deviceId = (deviceId == null || deviceId == "")? "records" : deviceId; 
	}

	public VoxxxRecord (String filename, String deviceId){
		this.fileName = filename;
		setDeviceId(deviceId);
	}
	public VoxxxRecord(String filename){
		this.fileName = filename;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPathToStream() {
		return pathToStream;
	}

	public void setPathToStream(String pathToStream) {
		this.pathToStream = pathToStream;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public int getPeeks() {
		return peeks;
	}

	public void setPeeks(int peeks) {
		this.peeks = peeks;
	}
	
	public Date getStartTimeAsDate(){
		return new Date(this.getStartTime());
	}
	
	public String getRecordDay(){
		return getFileName().substring(0, 10);
	}
	
	public double getScore(){
		double durationInSeconds = getDuration() / 5000.0;
		return getPeeks() / durationInSeconds;
	}
}
