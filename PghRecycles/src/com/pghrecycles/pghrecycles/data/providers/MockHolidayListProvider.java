package com.pghrecycles.pghrecycles.data.providers;

import android.text.format.Time;

import com.pghrecycles.pghrecycles.data.Holiday;
import com.pghrecycles.pghrecycles.data.HolidayList;

public class MockHolidayListProvider implements HolidayListProvider {

	@Override
	public HolidayList getHolidayList(Time year) {		
		HolidayList holidayList = new HolidayList();
		Time tempDateTime;
		
//		switch (year.year) {
//		case 2013:
			tempDateTime = new Time();
			tempDateTime.set(1, 1, 2013);
			holidayList.add(new Holiday("New Year's Day", tempDateTime));

			tempDateTime = new Time();
			tempDateTime.set(21, 1, 2013);
			holidayList.add(new Holiday("Rev. Dr. Martin Luther King, Jr. Day", tempDateTime));

			tempDateTime = new Time();
			tempDateTime.set(27, 5, 2013);
			holidayList.add(new Holiday("Memorial Day", tempDateTime));
			
			tempDateTime = new Time();
			tempDateTime.set(4, 7, 2013);
			holidayList.add(new Holiday("Independence Day", tempDateTime));

			tempDateTime = new Time();
			tempDateTime.set(2, 9, 2013);
			holidayList.add(new Holiday("Labor Day", tempDateTime));
			
			tempDateTime = new Time();
			tempDateTime.set(28, 11, 2013);
			holidayList.add(new Holiday("Thanksgiving", tempDateTime));
			
			tempDateTime = new Time();
			tempDateTime.set(25, 12, 2013);
			holidayList.add(new Holiday("Christmas", tempDateTime));

			//break;
//		case 2014:
//			
//			break;
//		}
		
		return holidayList;
	}

}
