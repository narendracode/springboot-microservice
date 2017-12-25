package com.leadiq.dao;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.leadiq.dto.TransactionDto;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

@Service
public class TransactionDao {
	private static Map<String,List<Long>> trxByType = null;
	private static Map<Long,List<Long>> trxByParentId = null;
	private static Map<Long,Double> trxSumByParentId = null;
	private static Map<Long,TransactionDto> trxs = null;
	static{
		trxByType 		 = new HashMap<String,List<Long>>();
		trxByParentId    = new HashMap<Long,List<Long>>();
		trxSumByParentId = new HashMap<Long,Double>();
		trxs			 = new HashMap<Long,TransactionDto>();
	}
	
	
	
	public void reset(){
		trxByType 		 = new HashMap<String,List<Long>>();
		trxByParentId    = new HashMap<Long,List<Long>>();
		trxSumByParentId = new HashMap<Long,Double>();
		trxs			 = new HashMap<Long,TransactionDto>();
	}
	
	public TransactionDto getTransaction(Long trxid){
		if(trxs.containsKey(trxid))
			return trxs.get(trxid);
		else
			return null;
	}
	
	
	public  List<Long> getTransactionsByType(String type){
		if(trxByType.containsKey(type))
			return trxByType.get(type);
		else
			return null;
	}
	
	public  Double getTransactionSum(Long parentId){
		if(trxSumByParentId.containsKey(parentId))
			return trxSumByParentId.get(parentId);
		else
			return 0.0;
	}
	
	
	public  boolean addTransaction(TransactionDto trx){
		
		if(trx.getId() == 0 || trx.getAmount() == 0 || trx.getType() == null || trx.getType().trim().length()==0)
			return false;
		
		
		//update list of all transactions
		trxs.put(trx.getId(),trx);
		
		//update transactions by parentid cache
		List<Long> listpid = null;
		if(trxByParentId.containsKey(trx.getParent_id())){
			listpid = trxByParentId.get(trx.getParent_id());
		}else{
			listpid = new ArrayList<Long>();
		}
		listpid.add(trx.getParent_id());
		trxByParentId.put(trx.getParent_id(), listpid);
		
		
		//update transactions by type cache
		List<Long> list = null;
		if(trxByType.containsKey(trx.getType())){
			list = trxByType.get(trx.getType());
		}else{
			list = new ArrayList<Long>();
		}
		list.add(trx.getId());
		trxByType.put(trx.getType(), list);
		
		
		//update transactions sum by parentid cache
		double sum = 0;
		if(trx.getParent_id()!=0){
			if(trxSumByParentId.containsKey(trx.getParent_id())){
				sum = trxSumByParentId.get(trx.getParent_id()) + trx.getAmount();
			}else{
				sum = trx.getAmount();
			}
			trxSumByParentId.put(trx.getParent_id(),sum);
		}
		
		return true;
	}
	
	
}
