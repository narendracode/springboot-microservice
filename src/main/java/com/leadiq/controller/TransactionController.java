package com.leadiq.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.leadiq.dto.TResponse;

@RestController
@RequestMapping("/transactionservice")
public class TransactionController {

	@RequestMapping(value="/transaction/{transaction_id}", method = RequestMethod.PUT)
	TResponse  putTransaction(@PathVariable long transaction_id){
		// BODY { "amount":double,"type":string,"parent_id":long }
		/* transaction id is a long specifying a new transaction
		   amount is a double specifying the amount
		   type is a string specifying a type of the transaction.
		   parent id is an optional long that may specify the parent transaction of this transaction.
		 */
		return new TResponse("ok");
	}
	
	@RequestMapping(value="/transaction/{transaction_id}",method = RequestMethod.GET)
	TResponse getTransaction(@PathVariable long transaction_id){
		// Returns: { "amount":double,"type":string,"parent_id":long }
		
		return new TResponse("ok");
	}
	
	@RequestMapping(value="/types/{type}", method = RequestMethod.GET)
	TResponse getTransactions(@PathVariable String type){
		// Returns: [long, long, ... ] A json list of all transaction ids that share the same type $type.
		return new TResponse("ok");
	}
	
	@RequestMapping(value="/transaction/sum/{transaction_id}", method=RequestMethod.GET)
	TResponse getTransactionSum(@PathVariable long transaction_id){
		// Returns: { "amount":double,"type":string,"parent_id":long }
		return new TResponse("ok");
	}
	
}
