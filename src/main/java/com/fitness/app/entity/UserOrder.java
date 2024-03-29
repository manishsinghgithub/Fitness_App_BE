package com.fitness.app.entity;

import java.time.LocalDate;
import java.time.LocalTime;
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
@Document(collection = "user_order")
public class UserOrder {

	
	@Id
	@Field
	private String id;
	@Field
	private String email;
	@Field
	private String gym;
	@Field
	private List<String> services;
	@Field
	private String subscription;
	@Field
	private String slot;
	@Field
	private int amount;
	@Field
	private String booked;
    @Field
    private String status;
    @Field
    private String paymentId;
	@Field
	private String receipt;
	@Field
	private LocalDate date;
	@Field
	private LocalTime time;


	public UserOrder(String email, String gym, List<String> services,
					 String subscription, String slot, int amount, String booked,
					 String status, String paymentId, String receipt, LocalDate date, LocalTime time) {
		this.email = email;
		this.gym = gym;
		this.services = services;
		this.subscription = subscription;
		this.slot = slot;
		this.amount = amount;
		this.booked = booked;
		this.status = status;
		this.paymentId = paymentId;
		this.receipt = receipt;
		this.date = date;
		this.time = time;
	}
}
