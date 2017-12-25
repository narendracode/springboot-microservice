package com.leadiq.dto;

public class TransactionDto implements ResponseDto{
	private long id;
	private long parent_id;
	private String type;
	private double amount;
	
	public TransactionDto() {
		super();
	}
	public TransactionDto(long id, String type, double amount) {
		this();
		this.id = id;
		this.type = type;
		this.amount = amount;
	}
	public TransactionDto(long id, String type, double amount, long parent_id) {
		this(id,type,amount);
		this.parent_id = parent_id;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParent_id() {
		return parent_id;
	}
	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
