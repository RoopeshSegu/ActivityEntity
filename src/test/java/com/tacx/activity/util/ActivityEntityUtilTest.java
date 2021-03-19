package com.tacx.activity.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.tacx.activity.abstractTests.AbstractTests;
import com.tacx.activity.domain.ActivityEntity;
import com.tacx.activity.domain.ActivitySummary;

class ActivityEntityUtilTest extends AbstractTests {

	private ActivityEntityUtil util;
	private MockMultipartFile file = null;

	@BeforeEach
	void setUp() {
		this.util = new ActivityEntityUtil();
		ReflectionTestUtils.setField(util, "pathToSave", "src/test/resources");
	}

	@Test
	void readCsvFileTest() throws IOException {
		this.file = new MockMultipartFile("data", "filename.csv", "multipart/form-data", "test".getBytes());
		List<String> result = this.util.readCsvFile(file);

		Assertions.assertEquals("test", result.get(0));
	}

	@Test
	void fetchSummaryTest() {
		ActivitySummary result = this.util.fetchSummary(createActivityEntity());

		Assertions.assertEquals(50.0, result.getAverageCadence());
		Assertions.assertEquals(35.0, result.getAveragePower());
		Assertions.assertEquals(20.0, result.getTotalDistance());
		Assertions.assertEquals("0:15:00", result.getTotalDuration());
	}
	
	@Test
	void convertCsvToEntityTest() {
		List<String> csvData = new ArrayList<>();
		csvData.add("activty_def,name,type,start_time");
		csvData.add("activity,My first ride,cycling,2011-12-03T10:15:00");
		csvData.add("record_def,time,distance,power,cadence");
		csvData.add("record,2011-12-03T10:15:00,0,150,92");
		csvData.add("record ,2011-12-03T10:16:00,300,150,75");
		csvData.add("record,2011-12-03T10:17:00,600,150,85");
		ActivityEntity entity = this.util.convertCsvToEntity(csvData);
		
		Assertions.assertEquals("My first ride", entity.getName());
		Assertions.assertEquals("cycling", entity.getType());
		Assertions.assertEquals(3, entity.getRecords().size());
		Assertions.assertEquals("2011-12-03T10:15:00", entity.getRecords().get(0).getTime());
	}
	
	@Test
	void saveToFileSystemTest() throws IOException {
		this.file = new MockMultipartFile("data", "filename.csv", "multipart/form-data", "test".getBytes());
		this.util.saveToFileSystem(file);
	}

}
