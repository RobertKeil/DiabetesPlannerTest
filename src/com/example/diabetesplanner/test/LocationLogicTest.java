package com.example.diabetesplanner.test;

import com.example.diabetesplanner.DataExchange;

import recognition.model.DataCollector;
import recognition.model.LocationLogic;
import recognition.model.MySQLiteHelper;
import android.content.Context;
import android.content.Intent;
import android.test.ServiceTestCase;
import android.util.Log;

/**
 * This test case tests all location functionalities. It has to start the DataCollector service in order to access the GPSTracker
 * @author Robert
 *
 */
public class LocationLogicTest extends ServiceTestCase<DataCollector> {
	double lat1, lon1, lat2, lon2;
	
	public LocationLogicTest() {
		super(DataCollector.class);
	}

	@Override
	protected void setUp()throws Exception{
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception{
		super.tearDown();
	}

	
	public void testDistance(){
		lat1 = lat2 = 49.885535;
		lon1 = lon2 = 8.4624061;
		assertTrue(LocationLogic.distance(lat1,lon1,lat2,lon2)==0);		
	}
	
	public void testLocationAccuracy() throws InterruptedException{
		
		double lon, lat;
		double accuracy;
		int counterCorrectTagging=0;
		int numberIterations=20;
		String predictedLocation;
		final String testLocation="Test";
		Context context = getSystemContext();
		DataCollector collector = new DataCollector();

		//start Data Collector service in order to initialize GPSTracker
		Intent service = new Intent(context, DataCollector.class);
		startService(service);
		
//		MySQLiteHelper helper = DataCollector.testHelper;
//		DataExchange.setHelper(helper);
//		
		
		MySQLiteHelper helper1 = new MySQLiteHelper(context);
		Log.d("LocationLogicTest",helper1.getDatabaseName());
		DataExchange.setHelper(helper1); 		
		MySQLiteHelper helper2 = new MySQLiteHelper(context);
		
		lon = DataCollector.track.getLongitude();
		lat = DataCollector.track.getLatitude();
		
		//Add a Location Record with the current longitude and latitude
		helper2.addLocationRecord(lat, lon, testLocation);
		
		//Test the method getCurrentLocation several times
		for(int i=0; i<numberIterations; i++){
			
			predictedLocation=collector.getCurrentLocation(helper2);
			
			if (predictedLocation.equals(testLocation)){
				counterCorrectTagging++;
			}
			Thread.sleep(500);
		}
		
		accuracy = counterCorrectTagging/numberIterations;
		Log.d("LocationLogicTest","Accuracy "+accuracy);
		
		assertTrue(accuracy > 0.6);
		}
	}