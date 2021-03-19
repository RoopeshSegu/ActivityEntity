package com.tacx.activity.abstractTests;

import java.util.ArrayList;
import java.util.List;

import com.tacx.activity.domain.ActivityEntity;
import com.tacx.activity.domain.ActivityRecord;

public class AbstractTests {

	public ActivityEntity createActivityEntity() {

		ActivityEntity entity = new ActivityEntity();
		entity.setActivityDef("activity_def");
		entity.setName("John");
		entity.setType("Cycling");
		entity.setId("0123456789");
		List<ActivityRecord> records = new ArrayList<>();
		records.add(new ActivityRecord("record1", "2011-12-03T10:15:00", 0, 30, 40));
		records.add(new ActivityRecord("record2", "2011-12-03T10:30:00", 20, 40, 60));
		entity.setRecords(records);

		return entity;
	}

}
