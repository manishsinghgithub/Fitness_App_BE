package com.fitness.app.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("user_attendence")
public class UserAttendance {

	
	@Id
	@Field
	private String email;
	@Field
	private String gym;
	@Field
	private String vendor;
	@Field
	private int booked;
	@Field
	private int attended;
	@Field
	private List<Integer> attendance;
	@Field
	private Double rating;
	public UserAttendance(String email, String gym, String vendor, int attended, Double rating) {
		
		this.email = email;
		this.gym = gym;
		this.vendor = vendor;
		this.attended = attended;
	}
	public UserAttendance(String email, String gym, String vendor) {
		this.email = email;
		this.gym = gym;
		this.vendor = vendor;
	}
	
	
	
	
	
	
}
