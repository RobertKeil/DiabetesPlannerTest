package com.example.diabetesplanner.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import gui.items.BloodSugar;
import gui.items.Carb;
import gui.items.HumanActivity;
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
	
	public void testAddMeasuringRecord(){
		
		Calendar time1 = new GregorianCalendar(2015, 7, 1, 13, 55, 43);
		Calendar time2 = new GregorianCalendar(2015, 7, 1, 14, 10, 43);
		Calendar time3 = new GregorianCalendar(2015, 7, 1, 18, 23, 43);
		
		double amount1 = 12.0;
		double amount2 = 1.167;
		double amount3 = 45.17;
		
		double carbVal = amount3*1.2;
//		double askeValue = (double)Math.round(carbVal * 10d) / 10d;
		
		MeasuringActivity insulin = new Insulin(time1, amount2);
		MeasuringActivity carbs = new Carb(time2, amount3);
		MeasuringActivity bloodSugar = new BloodSugar(time3, amount1);
		MeasuringActivity bloodSugar1 = new BloodSugar(time1, amount2);
		
		assertEquals(insulin.getValue(),amount2);
		assertEquals(carbs.getValue(),carbVal);
		assertEquals(bloodSugar.getValue(),amount1);
		
		assertNotSame(insulin, bloodSugar);
		assertNotSame(carbs, insulin);
		assertNotSame(bloodSugar, bloodSugar1);
		
		assertTrue("the measuring activity was not added", db.addMeasuringRecord(insulin));
		assertTrue("the measuring activity was not added", db.addMeasuringRecord(carbs));
		assertTrue("the measuring activity was not added", db.addMeasuringRecord(bloodSugar));
		assertTrue("the measuring activity was not added", db.addMeasuringRecord(bloodSugar1));
		
	}
	
	public void testAddManualRecord(){
		
		String activityName1 = "sitting";
		String activityName2 = "standing";
		String activityName3 = "walking";
		String activityName4 = "running";
		
		int activityDuration1 = 42;
		int activityDuration2 = 15;
		int activityDuration3 = 30;
		int activityDuration4 = 13;
		
		boolean autocreatedT = false;
		boolean autocreatedF = true;
		
		
		String location1 = "home";
		String location2 = "uni";
		String location3 = "street";
		String location4 = "club";
		
		
		Calendar time1 = new GregorianCalendar(2015, 7, 1, 13, 55, 43);
		Calendar time2 = new GregorianCalendar(2015, 7, 1, 14, 10, 43);
		Calendar time3 = new GregorianCalendar(2015, 7, 1, 18, 23, 43);

		HumanActivity human1 = new HumanActivity(activityName1, time1, activityDuration1, autocreatedT, location1);
		HumanActivity human2 = new HumanActivity(activityName2, time2, activityDuration2, autocreatedF, location2);
		HumanActivity human3 = new HumanActivity(activityName3, time3, activityDuration3, autocreatedT, location3);
		HumanActivity human4 = new HumanActivity(activityName4, time1, activityDuration4, autocreatedF, location4);
		
		assertEquals(activityName1, human1.getName());
		assertEquals(activityName2, human2.getName());
		
		human1.setName(activityName2);
		human2.setName(activityName4);
		
		assertEquals(activityName2, human1.getName());
		assertEquals(activityName4, human2.getName());
		
		assertEquals(activityDuration3, human3.getDurationMinutes());
		assertEquals(activityDuration4, human4.getDurationMinutes());
		
		human3.setDurationMinutes(activityDuration1);
		human4.setDurationMinutes(activityDuration2);
		
		assertEquals(activityDuration1, human3.getDurationMinutes());
		assertEquals(activityDuration2, human4.getDurationMinutes());
		
		assertEquals(location1, human1.getLocation());
		assertEquals(location3, human3.getLocation());
		
		assertTrue("the manual activity was not added", db.addManualRecord(human1));
		assertTrue("the manual activity was not added", db.addManualRecord(human2));
		assertTrue("the manual activity was not added", db.addManualRecord(human3));
		assertTrue("the manual activity was not added", db.addManualRecord(human4));


	}

}