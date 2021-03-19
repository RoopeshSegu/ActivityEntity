package com.tacx.activity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySummary {

	private double averagePower;
	private double averageCadence;
	private double totalDistance;
	private String totalDuration;

}
