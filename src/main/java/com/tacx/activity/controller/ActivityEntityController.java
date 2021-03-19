package com.tacx.activity.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tacx.activity.domain.ActivityRecord;
import com.tacx.activity.domain.ActivitySummary;
import com.tacx.activity.exception.ActivityEntityException;
import com.tacx.activity.service.ActivityEntityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/tacx/activity/entity")
public class ActivityEntityController {

	private static String EMPTY_FILE_ERROR = "Uploaded File is empty. Please upload the file and try again!. -> ";
	private static String DOCUMENT_NOT_FOUND_ERROR = "Document in database for id {} not found!. ";

	@Autowired
	private ActivityEntityService service;

	/**
	 * This Post mapping is to accept a csv file, save it into database and save the
	 * file to path mentioned in application.properties
	 * 
	 * @param file
	 * @return id Document id from MongoDb of ActivityEntity
	 */
	@PostMapping(path = "/create", consumes = "multipart/form-data")
	public ResponseEntity<String> createActivityEntity(@RequestPart("file") MultipartFile file) {
		log.info("Creating the ActivityEntity, en");

		if (file.isEmpty()) {
			log.error(EMPTY_FILE_ERROR + file.getOriginalFilename());
			throw new ActivityEntityException(EMPTY_FILE_ERROR + file.getOriginalFilename());
		}

		String id = null;
		try {
			id = this.service.createActivityEntity(file);
		} catch (IOException e) {
			throw new ActivityEntityException(
					"IO Exception while saving the file to the local disk -->" + e.getMessage());
		}
		return ResponseEntity.ok(id);
	}

	/**
	 * This Get Mapping is to take Document id as input and calculate the summary of
	 * activity and send back a json
	 * 
	 * @param id Document id of ActivityEntity
	 * @return summary of ActivityEntity
	 */
	@GetMapping(path = "/retrieve")
	public ResponseEntity<ActivitySummary> retrieveActivityEntity(@RequestParam("id") String id) {
		log.info("Retrieving the ActivityEntity for id : {}", id);
		Optional<ActivitySummary> summary = this.service.retrieveActivityEntity(id);
		if (summary.isEmpty()) {
			log.error(DOCUMENT_NOT_FOUND_ERROR, id);
			throw new ActivityEntityException(DOCUMENT_NOT_FOUND_ERROR + id);
		}

		log.info("Summary for ActivityEntity for id {} is -> {} ", id, summary.get());
		return ResponseEntity.ok(summary.get());
	}

	/**
	 * This Put Mapping is to accept a Record and document id as input. Add the
	 * record in Activity Entity that is stored in database, calculate the summary
	 * details after adding the record and return back the same
	 * 
	 * @param id     of ActivityEntity in database
	 * @param record to be added to ActivityEntity
	 * @return summary of ActivityEntity
	 */
	@PutMapping(path = "/update")
	public ResponseEntity<ActivitySummary> updateActivityEntity(@RequestParam("id") String id,
			@RequestBody ActivityRecord record) {
		log.info("Retrieving the ActivityEntity for id : {}", id);
		Optional<ActivitySummary> summary = this.service.updateActivityEntity(id, record);
		if (summary.isEmpty()) {
			log.error(DOCUMENT_NOT_FOUND_ERROR, id);
			throw new ActivityEntityException(DOCUMENT_NOT_FOUND_ERROR + id);
		}

		log.info("Summary after adding the record -> {} ", summary.get());
		return ResponseEntity.ok(summary.get());
	}

	/**
	 * This Put Mapping is to accept a Record and document id as input. Remove the
	 * record in Activity Entity that is stored in database, calculate the summary
	 * details after adding the record and return back the same
	 * 
	 * @param id     of ActivityEntity in database
	 * @param record to be added to ActivityEntity
	 * @return summary of ActivityEntity
	 */
	@PutMapping(path = "/delete")
	public ResponseEntity<ActivitySummary> deleteRecordActivityEntity(@RequestParam("id") String id,
			@RequestBody ActivityRecord record) {
		log.info("Deleting the activity for id : {}", id);
		Optional<ActivitySummary> summary = this.service.deleteRecordActivityEntity(id, record);

		if (summary.isEmpty()) {
			log.error(DOCUMENT_NOT_FOUND_ERROR, id);
			throw new ActivityEntityException(DOCUMENT_NOT_FOUND_ERROR + id);
		}
		log.info("Summary after deleting the record -> {} ", summary.get());
		return ResponseEntity.ok(summary.get());
	}

}
