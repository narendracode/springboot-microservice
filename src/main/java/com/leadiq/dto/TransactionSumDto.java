package com.leadiq.dto;

public class TransactionSumDto implements ResponseDto{
	private double sum;

	public TransactionSumDto(double sum) {
		super();
		this.sum = sum;
	}

	public double getSum() {
		return sum;
	}

	public void setsum(double sum) {
		this.sum = sum;
	}
	
}
