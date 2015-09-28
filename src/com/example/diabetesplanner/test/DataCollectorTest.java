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
	
	/**This test tests, if initial time window width is obeyed during sensor data collection. 
	 * @author Robert
	 */
	public void testInitialTimeWindow(){
		long startTime, endTime, duration;

		//start service
		Intent service = new Intent(getSystemContext(), DataCollector.class);
		startService(service);
		
		startTime = System.currentTimeMillis();
		
		//wait until the Collection has been sent. Ideally this takes exactly P.initTimeWindow seconds.
		while(!DataCollector.firstCollectionSent){
			try{
				Thread.sleep(500);
			} catch(Exception e){
				e.printStackTrace(); 
				fail();
			}
		}
		
		endTime = System.currentTimeMillis();
		duration = endTime - startTime; 
		Log.d("JUnit Test", "testInitialTimeWindow "+duration/1000);
		assertTrue(P.initTimeWindow-2<duration && duration<P.initTimeWindow+2);
	}
}
