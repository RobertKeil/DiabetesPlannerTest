package com.example.diabetesplanner.test;

import teamproject.diabetesplanner.model.DataCollector;
import teamproject.diabetesplanner.model.LocationLogic;
import teamproject.diabetesplanner.model.MySQLiteHelper;
import teamproject.diabetesplanner.properties.P;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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
		Location location;
		int counterCorrectTagging=0;
		int secondsForTest=20;
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
		
		P.helper = new MySQLiteHelper(context);
		
		location = DataCollector.track.getLocation();
		lon = location.getLongitude();
		lat = location.getLatitude();
		
		//Add a Location Record with the current longitude and latitude
		P.helper.addLocationRecord(lat, lon, testLocation);
		
		//Test the method getCurrentLocation several times
		for(int i=0; i<secondsForTest; i++){
			
			predictedLocation=collector.getCurrentLocation();
			
			if (predictedLocation.equals(testLocation)){
				counterCorrectTagging++;
			}
			Thread.sleep(1000);
		}
		
		P.helper.deleteDatabase();
		
		accuracy = counterCorrectTagging/secondsForTest;
		Log.d("LocationLogicTest","Accuracy "+accuracy);
		
		assertTrue(accuracy > 0.6);
		}
	}