package com.tacx.activity.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tacx.activity.domain.ActivityEntity;
import com.tacx.activity.domain.ActivityRecord;
import com.tacx.activity.domain.ActivitySummary;
import com.tacx.activity.repository.ActivityEntityRepository;
import com.tacx.activity.util.ActivityEntityUtil;

@Service
public class ActivityEntityService {

	@Autowired
	private ActivityEntityRepository repo;

	@Autowired
	private ActivityEntityUtil util;

	/**
	 * This service adds the csv file into database and save the file to path
	 * mentioned in application.properties
	 * 
	 * @param file
	 * @return id Document id from MongoDb of ActivityEntity
	 * @throws IOException
	 */
	public String createActivityEntity(MultipartFile file) throws IOException {

		// read the data from file
		List<String> csvData = this.util.readCsvFile(file);

		// convert it into ActivityEntityRepository
		ActivityEntity activityEntity = this.util.convertCsvToEntity(csvData);

		// save it to Database
		activityEntity = this.repo.save(activityEntity);

		// Store the file to specified local path
		this.util.saveToFileSystem(file);

		return activityEntity.getId();
	}

	/**
	 * This service finds ActivityEntity of requested id and calculate the summary
	 * of activity and sends back
	 * 
	 * @param id Document id of ActivityEntity
	 * @return summary of ActivityEntity
	 */
	public Optional<ActivitySummary> retrieveActivityEntity(String id) {
		// retrieve the Activity from database
		Optional<ActivityEntity> activityEntity = this.repo.findById(id);

		// if ActivityEntity is present, retrieve the summary and send back
		Optional<ActivitySummary> summary = Optional.empty();
		if (activityEntity.isPresent()) {
			summary = Optional.of(this.util.fetchSummary(activityEntity.get()));
		}

		return summary;
	}

	/**
	 * This service finds ActivityEntity of requested id, adds the incoming record,
	 * saves back to database and calculates the summary of activity and sends back
	 * 
	 * @param id Document id of ActivityEntity
	 * @return summary of ActivityEntity
	 */
	public Optional<ActivitySummary> updateActivityEntity(String id, ActivityRecord record) {
		// fetch the ActivityEntity from database
		Optional<ActivityEntity> activityEntity = this.repo.findById(id);

		// if ActivityEntity is present, add the record ,retrieve the summary and send
		// back
		Optional<ActivitySummary> summary = Optional.empty();
		if (activityEntity.isPresent()) {
			activityEntity.get().getRecords().add(record);
			summary = Optional.of(this.util.fetchSummary(activityEntity.get()));
			
			// save back to database since activity is modified
			this.repo.save(activityEntity.get());
		}

		return summary;
	}

	/**
	 * This service finds ActivityEntity of requested id, removes the incoming
	 * record, saves back to database and calculates the summary of activity and
	 * sends back
	 * 
	 * @param id Document id of ActivityEntity
	 * @return summary of ActivityEntity
	 */
	public Optional<ActivitySummary> deleteRecordActivityEntity(String id, ActivityRecord record) {
		// fetch the ActivityEntity from database
		Optional<ActivityEntity> activityEntity = this.repo.findById(id);

		// if ActivityEntity is present, add the record ,retrieve the summary and send
		// back
		Optional<ActivitySummary> summary = Optional.empty();
		if (activityEntity.isPresent()) {
			activityEntity.get().getRecords().remove(record);
			summary = Optional.of(this.util.fetchSummary(activityEntity.get()));
			
			// save back to database since activity is modified
			this.repo.save(activityEntity.get());
		}

		return summary;
	}
}
