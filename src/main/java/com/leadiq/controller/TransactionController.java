package com.leadiq.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.leadiq.dao.TransactionDao;
import com.leadiq.dto.ResponseDto;
import com.leadiq.dto.TResponse;
import com.leadiq.dto.TransactionDto;
import com.leadiq.dto.TransactionSumDto;



@RestController
@RequestMapping("/transactionservice")
public class TransactionController {

	@Autowired
	private TransactionDao transactionDao;
	
	@RequestMapping(value="/transaction/{transaction_id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	ResponseDto  putTransaction(@PathVariable long transaction_id, TransactionDto trxDto){
		// BODY { "amount":double,"type":string,"parent_id":long }
		/* transaction id is a long specifying a new transaction
		   amount is a double specifying the amount
		   type is a string specifying a type of the transaction.
		   parent id is an optional long that may specify the parent transaction of this transaction.
		 */
		
		trxDto.setId(transaction_id);
		
		if(transactionDao.addTransaction(trxDto)){
			return new TResponse("ok");
		}else
			return new TResponse("error");
		
	}
	
	@RequestMapping(value="/transaction/{transaction_id}",method = RequestMethod.GET)
	ResponseDto getTransaction(@PathVariable long transaction_id){
		// Returns: { "amount":double,"type":string,"parent_id":long }
		if(transactionDao.getTransaction(transaction_id) == null)
			return new TResponse("error", "not found");
		else
			return transactionDao.getTransaction(transaction_id);
	}
	
	@RequestMapping(value="/types/{type}", method = RequestMethod.GET)
	List<Long> getTransactions(@PathVariable String type){
		// Returns: [long, long, ... ] A json list of all transaction ids that share the same type $type.
		if(transactionDao.getTransactionsByType(type) == null || transactionDao.getTransactionsByType(type).size() == 0)
			return new ArrayList<Long>();
		else
			return transactionDao.getTransactionsByType(type);
			
	}
	
	@RequestMapping(value="/transaction/sum/{transaction_id}", method=RequestMethod.GET)
	ResponseDto getTransactionSum(@PathVariable long transaction_id){
		// Returns: { "sum":double }
		return new TransactionSumDto(transactionDao.getTransactionSum(transaction_id));
	}
	
}
