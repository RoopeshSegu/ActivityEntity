package com.tacx.activity.controller;

import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.tacx.activity.abstractTests.AbstractTests;
import com.tacx.activity.domain.ActivityRecord;
import com.tacx.activity.domain.ActivitySummary;
import com.tacx.activity.exception.ActivityEntityException;
import com.tacx.activity.service.ActivityEntityService;

@ExtendWith(MockitoExtension.class)
class ActivityEntityControllerTest extends AbstractTests {

	@InjectMocks
	private ActivityEntityController controller;

	@Mock
	private ActivityEntityService serviceMock;

	private String DOCUMENT_ID = "mongoDBDocumentId";
	private MockMultipartFile file = null;
	private ActivitySummary summary;
	private ActivityRecord record;

	@BeforeEach
	void setUp() {
		this.summary = new ActivitySummary(80.0, 60.0, 600.5, "time_taken");
		this.record = new ActivityRecord("record", "time", 30, 40, 50);
	}

	// Create Activity Entity successfully
	@Test
	void createActivityEntityTest() throws IOException {
		this.file = new MockMultipartFile("data", "filename.csv", "multipart/form-data", "test".getBytes());
		Mockito.lenient().when(this.serviceMock.createActivityEntity(file)).thenReturn(DOCUMENT_ID);
		ResponseEntity<String> result = this.controller.createActivityEntity(file);

		Assertions.assertEquals(result.getStatusCodeValue(), 200);
		Assertions.assertEquals(DOCUMENT_ID, result.getBody());
	}

	// Create Activity Entity with Empty file
	@Test
	void createActivityEntityTestEmptyFile() throws IOException {
		this.file = new MockMultipartFile("filename", "".getBytes());
		Assertions.assertThrows(ActivityEntityException.class, () -> this.controller.createActivityEntity(file));
	}

	// retrieve ActivityEntity for given document id
	@Test
	void retrieveActivityEntityTest() {
		Mockito.lenient().when(this.serviceMock.retrieveActivityEntity(any(String.class)))
				.thenReturn(Optional.of(this.summary));
		ResponseEntity<ActivitySummary> result = this.controller.retrieveActivityEntity(DOCUMENT_ID);

		Assertions.assertEquals(result.getStatusCodeValue(), 200);
		Assertions.assertEquals(60.0, result.getBody().getAverageCadence());
	}

	// retrieve ActivityEntity for given document id, but not present in MongoDB
	@Test
	void retrieveActivityEntityTestDocNotPresent() throws IOException {
		Mockito.lenient().when(this.serviceMock.retrieveActivityEntity(any(String.class))).thenReturn(Optional.empty());
		Assertions.assertThrows(ActivityEntityException.class,
				() -> this.controller.retrieveActivityEntity(DOCUMENT_ID));
	}

	// update ActivityEntity for given document id and record. Return back the
	// updated summary
	@Test
	void updateActivityEntityTest() {
		Mockito.lenient().when(this.serviceMock.updateActivityEntity(any(String.class), any(ActivityRecord.class)))
				.thenReturn(Optional.of(this.summary));
		ResponseEntity<ActivitySummary> result = this.controller.updateActivityEntity(DOCUMENT_ID, this.record);

		Assertions.assertEquals(result.getStatusCodeValue(), 200);
		Assertions.assertEquals(80.0, result.getBody().getAveragePower());
	}

	// update ActivityEntity for given document id and record, but not present in
	// MongoDB
	@Test
	void updateActivityEntityTestDocNotPresent() throws IOException {
		Mockito.lenient().when(this.serviceMock.updateActivityEntity(any(String.class), any(ActivityRecord.class)))
				.thenReturn(Optional.empty());
		Assertions.assertThrows(ActivityEntityException.class,
				() -> this.controller.updateActivityEntity(DOCUMENT_ID, this.record));
	}

	// delete record in ActivityEntity for given document id, Return back the
	// updated summary
	@Test
	void deleteActivityEntityTest() {
		Mockito.lenient()
				.when(this.serviceMock.deleteRecordActivityEntity(any(String.class), any(ActivityRecord.class)))
				.thenReturn(Optional.of(this.summary));
		ResponseEntity<ActivitySummary> result = this.controller.deleteRecordActivityEntity(DOCUMENT_ID, this.record);

		Assertions.assertEquals(result.getStatusCodeValue(), 200);
		Assertions.assertEquals(600.5, result.getBody().getTotalDistance());
	}

	// delete the record for given document id, but not present in MongoDB
	@Test
	void deleteActivityEntityTestDocNotPresent() throws IOException {
		Mockito.lenient().when(this.serviceMock.retrieveActivityEntity(any(String.class))).thenReturn(Optional.empty());
		Assertions.assertThrows(ActivityEntityException.class,
				() -> this.controller.deleteRecordActivityEntity(DOCUMENT_ID, this.record));
	}

}
