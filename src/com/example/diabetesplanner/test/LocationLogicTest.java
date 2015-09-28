package com.example.diabetesplanner.test;

import junit.framework.TestCase;
import recognition.model.LocationLogic;

public class LocationLogicTest extends TestCase {
	double lat1, lon1, lat2, lon2;
	
	@Override
	protected void setUp()throws Exception{
		super.setUp();
		lat1 = lat2 = 49.885535;
		lon1 = lon2 = 8.4624061;
	}
	
	public void testDistance(){
		assertTrue(LocationLogic.distance(lat1,lon1,lat2,lon2)==0);		
	}	
}
