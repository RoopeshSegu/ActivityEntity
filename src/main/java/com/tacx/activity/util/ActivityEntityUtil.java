package com.tacx.activity.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.tacx.activity.domain.ActivityEntity;
import com.tacx.activity.domain.ActivityRecord;
import com.tacx.activity.domain.ActivitySummary;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ActivityEntityUtil {

	@Value("${save.file.path}")
	private String pathToSave;

	/**
	 * This method reads CSV file and converts list of String
	 * 
	 * @param file
	 * @return resultList list of String
	 * @throws IOException
	 */
	public List<String> readCsvFile(MultipartFile file) throws IOException {
		List<String> resultList = new LinkedList<>();
		try (InputStream is = file.getInputStream()) {
			new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()
					.forEach(s -> resultList.add(s));
		} catch (IOException e) {
			throw new IOException(e);
		}
		return resultList;
	}

	/**
	 * This method saves the incoming csv file to local disk at path specified in
	 * application.properties
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void saveToFileSystem(MultipartFile file) throws IOException {
		try {
			file.transferTo(new File(pathToSave + file.getOriginalFilename()));
		} catch (IllegalStateException | IOException e) {
			log.error("Failed to save the file : {}", pathToSave + file.getOriginalFilename());
			throw new IOException(e);
		}
		log.info("File saved to the path : {}", pathToSave + file.getOriginalFilename());
	}

	/**
	 * This method is to fetch ActivitySummary from ActivityEntity present in
	 * database
	 * 
	 * @param activityEntity
	 * @return activitySummary
	 */
	public ActivitySummary fetchSummary(ActivityEntity activityEntity) {
		ActivitySummary summary = new ActivitySummary();
		summary.setAverageCadence(activityEntity.getAverageCadence());
		summary.setAveragePower(activityEntity.getAveragePower());
		summary.setTotalDistance(activityEntity.getTotalDistance());

		// fetch time of all records, find the total time taken by subtracting max and
		// min time
		List<LocalDateTime> dateList = activityEntity.getRecords().stream().map(r -> LocalDateTime.parse(r.getTime()))
				.collect(Collectors.toList());
		Collections.sort(dateList);
		Duration timeTaken = Duration.between(Collections.min(dateList), Collections.max(dateList));
		String totalTime = String.format("%d:%02d:%02d", timeTaken.toHours(), timeTaken.toMinutesPart(),
				timeTaken.toSecondsPart());

		summary.setTotalDuration(totalTime);
		return summary;
	}

	/**
	 * This method converts List of string into ActivityEntity object
	 * 
	 * @param csvData List of String
	 * @return entity
	 */
	public ActivityEntity convertCsvToEntity(List<String> csvData) {
		ActivityEntity entity = new ActivityEntity();

		// Add first line of Activity information.
		String[] generalInfo = csvData.get(1).split(",");
		if (generalInfo.length == 4) {
			entity.setActivityDef(generalInfo[0]);
			entity.setName(generalInfo[1]);
			entity.setType(generalInfo[2]);
			entity.setStartTime(LocalDateTime.parse(generalInfo[3]));
		}

		List<ActivityRecord> recordsList = new ArrayList<>();
		// Add all the records from line 4
		csvData.subList(3, csvData.size()).stream().forEach(data -> {
			String[] record = data.split(",");
			ActivityRecord activityRecord = new ActivityRecord(record[0], record[1], Integer.valueOf(record[2]),
					Integer.valueOf(record[3]), Integer.valueOf(record[4]));
			recordsList.add(activityRecord);
		});

		entity.setRecords(recordsList);
		return entity;
	}

}
