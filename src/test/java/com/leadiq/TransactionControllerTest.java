package com.leadiq;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import java.net.URL;
import java.util.Arrays;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadiq.dao.TransactionDao;
import com.leadiq.dto.TResponse;
import com.leadiq.dto.TransactionDto;
import com.leadiq.dto.TransactionSumDto;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TransactionControllerTest {

	private JacksonTester<TResponse> trxResponseDTOTester;
	private JacksonTester<TransactionSumDto> transactionSumDTOTester;
	private JacksonTester<TransactionDto> transactionDtoTester;
	
	private TransactionDto trxDto1,trxDto2;
	private TResponse tResponseOK;
	private TResponse tResponseError;
	
    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Autowired 
    private ObjectMapper objectMapper;
    
    
    @Autowired
	private TransactionDao transactionDao;
    
    
    
    @Autowired
    private MockMvc mvc;
    
    @Before
    public void setUp() throws Exception {
    	JacksonTester.initFields(this, objectMapper);
        this.base = new URL("http://localhost:" + port + "/");
        tResponseOK = new TResponse("ok");
        tResponseError = new TResponse("error");
        trxDto1 = new TransactionDto(1, "car", 20, 15);
        trxDto2 = new TransactionDto(2, "car", 10, 15);
                
        transactionDao.reset();
        
    }

    
    @Test
    public void transactionsByNonExistingTypeIsEmptyArray() throws Exception {
    	transactionDao.reset();
    	
        mvc.perform(MockMvcRequestBuilders.get("/transactionservice/types/cars1")
        		.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));
    	
    	
    }
    
    @Test
    public void transactionsByTypeIsArrayOfIds() throws Exception {
    	transactionDao.reset();
    	
    	mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/"+trxDto1.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
												                        new BasicNameValuePair("amount", ""+trxDto1.getAmount()),
												                        new BasicNameValuePair("parent_id", ""+trxDto1.getParent_id()),
												                        new BasicNameValuePair("type", trxDto1.getType())
                																	)
                													  )
                							 )
                
                		)
                )
    			.andExpect(status().isOk());
    	
    	mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/"+trxDto2.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
												                        new BasicNameValuePair("amount", ""+trxDto2.getAmount()),
												                        new BasicNameValuePair("parent_id", ""+trxDto2.getParent_id()),
												                        new BasicNameValuePair("type", trxDto2.getType())
                																	)
                													  )
                							 )
                
                		)
                )
    			.andExpect(status().isOk());
    	
        mvc.perform(MockMvcRequestBuilders.get("/transactionservice/types/"+trxDto1.getType())
        		.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("["+trxDto1.getId()+","+trxDto2.getId()+"]")));
    	
    	
    }
    
    
    @Test
    public void getTransactionByIdIsJSONResponse() throws Exception {
    	transactionDao.reset();
    	
    	mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/"+trxDto1.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
												                        new BasicNameValuePair("amount", ""+trxDto1.getAmount()),
												                        new BasicNameValuePair("parent_id", ""+trxDto1.getParent_id()),
												                        new BasicNameValuePair("type", trxDto1.getType())
                																	)
                													  )
                							 )
                
                		)
                )
    			.andExpect(status().isOk());
    
    	mvc.perform(MockMvcRequestBuilders.get("/transactionservice/transaction/"+trxDto1.getId())
            	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
              )
			.andExpect(status().isOk())
			.andExpect(content().json(transactionDtoTester.write(trxDto1).getJson()));
    	
    }
    
    @Test
    public void defaultSumForNonExistingParentidIsZero() throws Exception {
    	//tResponseSumZero
    	transactionDao.reset();
    	mvc.perform(MockMvcRequestBuilders.get("/transactionservice/transaction/sum/9999")
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                  )
    			.andExpect(status().isOk())
    			.andExpect(content().json(transactionSumDTOTester.write(new TransactionSumDto(0)).getJson()));
    }
    
   
    @Test
    public void sumAfterAddingOneTransactionIsSameAsAmount() throws Exception {
    	transactionDao.reset();
    	
    	mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/"+trxDto1.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
												                        new BasicNameValuePair("amount", ""+trxDto1.getAmount()),
												                        new BasicNameValuePair("parent_id", ""+trxDto1.getParent_id()),
												                        new BasicNameValuePair("type", trxDto1.getType())
                																	)
                													  )
                							 )
                
                		)
                )
    			.andExpect(status().isOk());
    	
    	  mvc.perform(MockMvcRequestBuilders.get("/transactionservice/transaction/sum/"+trxDto1.getParent_id())
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                  )
    			.andExpect(status().isOk())
    			.andExpect(content().json(transactionSumDTOTester.write(new TransactionSumDto(trxDto1.getAmount())).getJson()));
       
    }
    
    
    @Test
    public void sumAfterAddingMultipleTransactionIsTotalOfAmountInEachRequest() throws Exception {
    	transactionDao.reset();
    	mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/"+trxDto1.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
												                        new BasicNameValuePair("amount", ""+trxDto1.getAmount()),
												                        new BasicNameValuePair("parent_id", ""+trxDto1.getParent_id()),
												                        new BasicNameValuePair("type", trxDto1.getType())
                																	)
                													  )
                							 )
                
                		)
                )
    			.andExpect(status().isOk());
    	
    	mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/"+trxDto2.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
												                        new BasicNameValuePair("amount", ""+trxDto2.getAmount()),
												                        new BasicNameValuePair("parent_id", ""+trxDto2.getParent_id()),
												                        new BasicNameValuePair("type", trxDto2.getType())
                																	)
                													  )
                							 )
                
                		)
                )
    			.andExpect(status().isOk());
    	
    	
    	
    	
    	  mvc.perform(MockMvcRequestBuilders.get("/transactionservice/transaction/sum/"+trxDto1.getParent_id())
                	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                  )
    			.andExpect(status().isOk())
    			.andExpect(content().json(transactionSumDTOTester.write(new TransactionSumDto(trxDto1.getAmount()+trxDto2.getAmount())).getJson()));
       
    }
    
    
    
    
    
	
    
    @Test
    public void testInvalidTransaction() throws Exception {
    	final String tResponseJson = trxResponseDTOTester.write(tResponseError).getJson();

    	// transaction "id" cannot be 0
    	mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/0")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
												                        new BasicNameValuePair("amount", "5"),
												                        new BasicNameValuePair("type", "type1")
                																	)
                													  )
                							 )
                
                		)
                )
    			.andExpect(status().isOk())
    			.andExpect(content().json(tResponseJson));
    	
    	//"type" is mandatory field,no "type" provided in form params
    	mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
												                        new BasicNameValuePair("amount", "5")
                																	)
                													  )
                							 )
                
                		)
                )
    			.andExpect(status().isOk())
    			.andExpect(content().json(tResponseJson));
      
    	//transaction "type" cannot be empty string 
    	mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
												                        new BasicNameValuePair("amount", trxDto1.getAmount()+""),
												                        new BasicNameValuePair("type", " ")
                																	)
                													  )
                							 )
                
                		)
                )
    			.andExpect(status().isOk())
    			.andExpect(content().json(tResponseJson));
    	
    	//transaction amount cannot be zero
    	mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
												                        new BasicNameValuePair("amount", "0"),
												                        new BasicNameValuePair("type", "type1")
                																	)
                													  )
                							 )
                
                		)
                )
    			.andExpect(status().isOk())
    			.andExpect(content().json(trxResponseDTOTester.write(tResponseError).getJson()));
    }
    
    @Test
    public void inserTransactionReturnsStatusOkJSON() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/"+trxDto1.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
												                        new BasicNameValuePair("id", trxDto1.getId()+""),
												                        new BasicNameValuePair("amount", trxDto1.getAmount()+""),
												                        new BasicNameValuePair("type", trxDto1.getType())
                																	)
                													  )
                							 )
                
                		)
                )
    			.andExpect(status().isOk())
    			.andExpect(content().json(trxResponseDTOTester.write(tResponseOK).getJson()));
                
    }
    
    
    @Test
    public void getHomeBody() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        assertThat(response.getBody(), equalTo("Hello Spring Boot!"));
    }
	
	
    @Test
    public void getHomeJSON() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/")
        		.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello Spring Boot!")));
    }
    
     
}
