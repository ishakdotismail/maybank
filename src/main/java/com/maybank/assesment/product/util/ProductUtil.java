package com.maybank.assesment.product.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.maybank.assesment.product.exception.ProductException;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class ProductUtil {
	
	private static final Logger logger = LogManager.getLogger(ProductUtil.class);
	
	@Autowired
	private static EurekaClient eurekaClient;
	
			
	public static long generateProductId() {
		return ThreadLocalRandom.current().nextLong(0, 100000);
	}
	
	public static Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
	
	
	public static String sendEmailNotification(String email){
    	RestTemplate restTemplate = new RestTemplate();
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("AUTHENTICATION-SERVICE", false);
        //logger.info("calling EurekaService serviceName instance=>{}",instance);
        var port = instance.getPort();
        //logger.info("calling EurekaService port=>{} , uri={}",port,uri);
        String serviceUrl = "http://localhost:8095/api/notification/sendEmail"; 
        //logger.info("calling EurekaService serviceUrl=>{}",serviceUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  
        
        
        
        String jsonBody = "{\r\n"
		 		+ "  \"email\":\""+email+"\",\r\n"
		 		+ "  \"message\":\"New product has been registered\"\r\n"
		 		+ "}";
        
        logger.info("request body ==>{}",jsonBody);
        HttpEntity<Object> requestEntity = new HttpEntity<>(jsonBody, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    serviceUrl,
                    HttpMethod.POST, 
                    requestEntity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
            	HttpHeaders respHeader = response.getHeaders();
            	logger.info("response headers ==>{}",respHeader.getFirst("Authorization"));
            	return respHeader.getFirst("Authorization");
            } else {
            	logger.error("Error callAuthenticationService {}",response.getStatusCode());
            	throw new ProductException(
    					"EMAIL_NOT_SENT",
                        "Error calling eureka service. Please contact system admin",
                        "Ralat semasa memproses data. Please contact system admin",
                        HttpStatus.BAD_REQUEST
    		    );
            }
        } catch (RestClientResponseException e) {
        	logger.error("Error calling Eureka service sendEmailNotification {}", e);
            throw e;
        }
  }

}
