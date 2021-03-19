package com.tacx.activity.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityRecord {

	private String recordDef;
	private String time;
	private int distance;
	private int power;
	private int cadence;
}
