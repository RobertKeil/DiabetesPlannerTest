package com.example.diabetesplanner.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import gui.items.BloodSugar;
import gui.items.Carb;
import gui.items.Insulin;
import gui.items.MeasuringActivity;
import recognition.model.MySQLiteHelper;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class SQLiteHelperTest extends AndroidTestCase {

	private MySQLiteHelper db;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new MySQLiteHelper(context);
	}
	
	@Override
    public void tearDown() throws Exception {
        db.close(); 
        super.tearDown();
    }
	
	public void testDataBaseCreation(){
		assertNotNull(db);
	}
	
	public void testAddTempRecord(){
		
		long time1 = 298127712;
		long time2 = 12178623;
		long time3 = 88;
		
		String activity1 = "walking";
		String activity2 = "standing";
		String activity3 = "sitting";
		
		db.addTempRecord(time1, activity1);
		db.addTempRecord(time2, activity2);
		db.addTempRecord(time3, activity3);
		
		assertTrue(db.addTempRecord(time1, activity1));
		assertTrue(db.addTempRecord(time2, activity2));
		assertTrue(db.addTempRecord(time3, activity3));
		assertTrue(db.addTempRecord(time3, activity2));
		assertTrue(db.addTempRecord(time2, activity1));
		assertTrue(db.addTempRecord(time1, activity3));
			
		
	}
	
	public void TestAddMeasuringRecord(){
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
		Calendar time1 = new GregorianCalendar(2015, 7, 1, 13, 55, 43);
		Calendar time2 = new GregorianCalendar(2015, 7, 1, 13, 55, 43);
		Calendar time3 = new GregorianCalendar(2015, 7, 1, 13, 55, 43);
		
		double amount1 = 12.0;
		double amount2 = 1.167;
		double amount3 = 45.17;
		
		MeasuringActivity insulin = new Insulin(time1, amount2);
		MeasuringActivity carbs = new Carb(time2, amount3);
		MeasuringActivity bloodSugar = new BloodSugar(time3, amount1);
		MeasuringActivity bloodSugar1 = new BloodSugar(time1, amount2);
		
		assertEquals(insulin.getValue(),amount2);
		assertEquals(carbs.getValue(),amount3);
		assertEquals(bloodSugar.getValue(),amount1);
		
		assertNotSame(insulin, bloodSugar);
		assertNotSame(carbs, insulin);
		assertSame(bloodSugar, bloodSugar1);
		
		assertTrue("the measuring activity was not added", db.addMeasuringRecord(insulin));
		assertTrue("the measuring activity was not added", db.addMeasuringRecord(carbs));
		assertTrue("the measuring activity was not added", db.addMeasuringRecord(bloodSugar));
		assertTrue("the measuring activity was not added", db.addMeasuringRecord(bloodSugar1));
		
	}

}
