package com.example.diabetesplanner.test;

import java.io.File;

import teamproject.diabetesplanner.model.DataCollector;
import teamproject.diabetesplanner.properties.P;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.test.ServiceTestCase;
import android.util.Log;

/**
 * Tests the behaviour of the Data Collector Service
 * @author Robert
 *
 */
public class DataCollectorTest extends ServiceTestCase<DataCollector> {

	public DataCollectorTest() {
		super(DataCollector.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception{
		DataCollector.counterSampleRate=0;
		super.tearDown();
	}
	 /**Tests whether the sample rate is adhered in the app, i.e. whether really P.sampleRate records are sent per second to the calculation class
	  * @author Robert
	  * @throws InterruptedException
	  */
	public void testSampleRate() throws InterruptedException{
		long startTime, endTime, duration, realSampleRate;

		//start service
		Intent service = new Intent(getSystemContext(), DataCollector.class);
		startService(service);

		startTime = System.currentTimeMillis();
		//let the service run for a seccond
		Thread.sleep(1000);
		endTime = System.currentTimeMillis();
		duration = endTime-startTime;
		realSampleRate = DataCollector.counterSampleRate/(duration/1000);
		
		Log.d("JUnit Test", "testSampleRate: "+realSampleRate);
		
		//True if real sample rate is close to predefined sample rate
		assertTrue(P.sampleRate-4 < realSampleRate && realSampleRate < P.sampleRate+4);
	}

	/**This test tests, if final time window width is obeyed during sensor data collection. 
	 * @author Robert
	 * @throws InterruptedException 
	 */
	public void testFinalTimeWindow() throws InterruptedException{
		long startTime, endTime, duration;

		//start service
		Intent service = new Intent(getSystemContext(), DataCollector.class);
		startService(service);

		startTime = System.currentTimeMillis();

		//wait until the Collection has been sent. Ideally this takes exactly P.initTimeWindow seconds.
		while(!DataCollector.firstCollectionSent){
				Thread.sleep(500);
			}

		endTime = System.currentTimeMillis();
		
		//duration in seconds
		duration = (endTime - startTime)/1000; 
		Log.d("JUnit Test", "testFinalTimeWindow: "+duration);
		assertTrue(P.finTimeWindow-2<duration && duration<P.finTimeWindow+2);
	}
	
	
	/** Tests how many bytes are needed for each day
	 * @author Robert
	 */
	public void testDatabaseSize() throws InterruptedException{
		
		long startTime, endTime; 
		long startSize, endSize, sizeDifference;
		double consumptionDay, consumptionSecond;
		
		//duration of test
		int wait=60000;
		SQLiteDatabase db;
		
		//start service
		Intent service = new Intent(getSystemContext(), DataCollector.class);
		startService(service);

		startTime = System.currentTimeMillis();
		
		db = P.helper.getReadableDatabase();
		startSize=new File (db.getPath()).length();
		Log.d("Test", "testDatabaseSize startSize " + startSize + "byte");
		
		//The DataCollector continues collecting data and writing it to the database while waiting
		Thread.sleep(wait);
		
		endSize=new File (db.getPath()).length();
		Log.d("Test", "testDatabaseSize endSize " + startSize + "byte");
		
		sizeDifference = endSize - startSize; 
		consumptionSecond = sizeDifference/wait*1000;
		consumptionDay = consumptionSecond*60*60*24;
		Log.d("Test", "testDatabaseSize consumptionDay " + consumptionDay + "byte");
		
		//Not more than 1 MB a day
		assertTrue(consumptionDay<1000000);
	}
}