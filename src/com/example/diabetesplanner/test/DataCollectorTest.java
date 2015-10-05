package com.example.diabetesplanner.test;

import properties.P;
import recognition.model.DataCollector;
import android.content.Intent;
import android.test.ServiceTestCase;
import android.util.Log;

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

	public void testSampleRate() throws InterruptedException{
		long startTime, endTime, duration, realSampleRate;

		//start service
		Intent service = new Intent(getSystemContext(), DataCollector.class);
		startService(service);

		startTime = System.currentTimeMillis();
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
}