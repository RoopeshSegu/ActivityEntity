package com.tacx.activity.service;

import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.tacx.activity.abstractTests.AbstractTests;
import com.tacx.activity.domain.ActivityEntity;
import com.tacx.activity.domain.ActivityRecord;
import com.tacx.activity.domain.ActivitySummary;
import com.tacx.activity.repository.ActivityEntityRepository;
import com.tacx.activity.util.ActivityEntityUtil;

@ExtendWith(MockitoExtension.class)
class ActivityEntityServiceTest extends AbstractTests {

	private static final String DOCUMENT_ID = "0123456789";

	@InjectMocks
	ActivityEntityService service;

	@Mock
	ActivityEntityUtil utilMock;

	@Mock
	ActivityEntityRepository repoMock;

	private ActivitySummary summary;
	private ActivityRecord record;

	@BeforeEach
	void setUp() {
		this.summary = new ActivitySummary(80.0, 60.0, 600.5, "time_taken");
		this.record = new ActivityRecord("record", "time", 30, 40, 50);
	}

	@Test
	void createActivityEntityTest() throws IOException {
		MockMultipartFile file = new MockMultipartFile("data", "filename.csv", "multipart/form-data",
				"test".getBytes());

		Mockito.lenient().when(this.utilMock.readCsvFile(any(MultipartFile.class))).thenReturn(new ArrayList<String>());
		Mockito.lenient().when(this.utilMock.convertCsvToEntity(any(List.class))).thenReturn(createActivityEntity());
		Mockito.lenient().when(this.repoMock.save(any(ActivityEntity.class))).thenReturn(createActivityEntity());

		Assertions.assertEquals(DOCUMENT_ID, this.service.createActivityEntity(file));
	}

	// retrieve Summary service successfull call
	@Test
	void retrieveActivityEntityTest() throws IOException {
		Mockito.lenient().when(this.repoMock.findById(any(String.class)))
				.thenReturn(Optional.of(createActivityEntity()));
		Mockito.lenient().when(this.utilMock.fetchSummary(any(ActivityEntity.class))).thenReturn(this.summary);

		Optional<ActivitySummary> result = this.service.retrieveActivityEntity(DOCUMENT_ID);
		Assertions.assertEquals(60.0, result.get().getAverageCadence());
	}

	// retrieve Summary service, requested Document not present in MongoDB
	@Test
	void retrieveActivityEntityTestDocNotPresent() throws IOException {
		Mockito.lenient().when(this.repoMock.findById(any(String.class))).thenReturn(Optional.empty());

		Optional<ActivitySummary> result = this.service.retrieveActivityEntity(DOCUMENT_ID);
		Assertions.assertTrue(result.isEmpty());
	}

	// retrieve Summary service, requested Document not present in MongoDB
	@Test
	void updateActivityEntityTestDocNotPresent() throws IOException {
		Mockito.lenient().when(this.repoMock.findById(any(String.class))).thenReturn(Optional.empty());

		Optional<ActivitySummary> result = this.service.updateActivityEntity(DOCUMENT_ID, this.record);
		Assertions.assertTrue(result.isEmpty());
	}

	// retrieve Summary service, requested Document not present in MongoDB
	@Test
	void deleteActivityEntityTestDocNotPresent() throws IOException {
		Mockito.lenient().when(this.repoMock.findById(any(String.class))).thenReturn(Optional.empty());

		Optional<ActivitySummary> result = this.service.deleteRecordActivityEntity(DOCUMENT_ID, this.record);
		Assertions.assertTrue(result.isEmpty());
	}

	// retrieve Summary service successfull call
	@Test
	void updateActivityEntityTest() throws IOException {
		Mockito.lenient().when(this.repoMock.findById(any(String.class)))
				.thenReturn(Optional.of(createActivityEntity()));
		Mockito.lenient().when(this.utilMock.fetchSummary(any(ActivityEntity.class))).thenReturn(this.summary);

		Optional<ActivitySummary> result = this.service.updateActivityEntity(DOCUMENT_ID, this.record);
		Assertions.assertEquals(60.0, result.get().getAverageCadence());
	}

	// retrieve Summary service successfull call
	@Test
	void deleteActivityEntityTest() throws IOException {
		Mockito.lenient().when(this.repoMock.findById(any(String.class)))
				.thenReturn(Optional.of(createActivityEntity()));
		Mockito.lenient().when(this.utilMock.fetchSummary(any(ActivityEntity.class))).thenReturn(this.summary);

		Optional<ActivitySummary> result = this.service.deleteRecordActivityEntity(DOCUMENT_ID, this.record);
		Assertions.assertEquals(60.0, result.get().getAverageCadence());
	}

}
