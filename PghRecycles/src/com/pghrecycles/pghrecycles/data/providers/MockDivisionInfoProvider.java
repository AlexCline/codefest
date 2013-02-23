package com.pghrecycles.pghrecycles.data.providers;

import android.text.format.Time;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.DivisionInfo.Division;
import com.pghrecycles.pghrecycles.data.PickupDate;
import com.pghrecycles.pghrecycles.data.RecyclingSchedule;
import com.pghrecycles.pghrecycles.data.YardDebrisSchedule;

/**
 * hard-coded set of values based on 2013 maps
 * @author Adam
 *
 */
public class MockDivisionInfoProvider implements DivisionInfoProvider {

	@Override
	public DivisionInfo getDivisionInfo(Division division, Time year) {
		DivisionInfo divisionInfo = new DivisionInfo();
		divisionInfo.setName(division.name());
		divisionInfo.setDivision(division);
		
		YardDebrisSchedule yardDebrisSchedule = new YardDebrisSchedule();
		RecyclingSchedule recyclingSchedule = new RecyclingSchedule();
		Time pickupDateTime;
		
		// construct values
		switch (division) {
		case CENTRAL:
		case EASTERN:
			// YardDebrisSchedule:
			pickupDateTime = new Time();
			pickupDateTime.set(18, 5-1, 2013);
			pickupDateTime.normalize(true);
			yardDebrisSchedule.addPickupDate(new PickupDate(pickupDateTime));
			
			pickupDateTime = new Time();
			pickupDateTime.set(9, 11-1, 2013);
			pickupDateTime.normalize(true);
			yardDebrisSchedule.addPickupDate(new PickupDate(pickupDateTime));
			
			// RecyclingSchedule
			recyclingSchedule.setStartWeek(1);			
			break;
		case NORTHERN:
		case SOUTHERN:
			// YardDebrisSchedule:
			pickupDateTime = new Time();
			pickupDateTime.set(18, 5-1, 2013);
			pickupDateTime.normalize(true);
			yardDebrisSchedule.addPickupDate(new PickupDate(pickupDateTime));
			
			pickupDateTime = new Time();
			pickupDateTime.set(9, 11-1, 2013);
			pickupDateTime.normalize(true);
			yardDebrisSchedule.addPickupDate(new PickupDate(pickupDateTime));
			
			// RecyclingSchedule
			recyclingSchedule.setStartWeek(2);			
			break;
		}
		
		divisionInfo.setYardDebrisSchedule(yardDebrisSchedule);
		divisionInfo.setYardDebrisSchedule(yardDebrisSchedule);
		
		return divisionInfo;
	}
}