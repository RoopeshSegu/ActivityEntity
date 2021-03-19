package com.tacx.activity.domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "ActivityEntityData")
@Data
public class ActivityEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String activityDef;
	private String name;
	private String type;
	private LocalDateTime startTime;
	private List<ActivityRecord> records;

	public double getTotalDistance() {
		return this.records.stream().mapToDouble(d -> d.getDistance()).sum();
	}

	public double getAverageCadence() {
		return this.records.stream().mapToDouble(d -> d.getCadence()).average().orElse(0);
	}

	public double getAveragePower() {
		return this.records.stream().mapToDouble(d -> d.getPower()).average().orElse(0);
	}

}
