package com.db.dataplatform.techtest.client.component.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

import com.db.dataplatform.techtest.client.api.model.DataEnvelope;
import com.db.dataplatform.techtest.client.component.Client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Client code does not require any test coverage
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientImpl implements Client {

    public static final String URI_PUSHDATA = "http://localhost:8090/dataserver/pushdata";
    public static final UriTemplate URI_GETDATA = new UriTemplate("http://localhost:8090/dataserver/data/{blockType}");
    public static final UriTemplate URI_PATCHDATA = new UriTemplate("http://localhost:8090/dataserver/update/{name}/{newBlockType}");    
    
    private final RestTemplate restTemplate;
    
    @Override
    public void pushData(DataEnvelope dataEnvelope) {
        log.info("Pushing data {} to {}", dataEnvelope.getDataHeader().getName(), URI_PUSHDATA);
        
        String uriComponents = UriComponentsBuilder.fromUriString(URI_PUSHDATA)
                .build().toUriString();

        restTemplate.postForObject(uriComponents,dataEnvelope,Boolean.class);
        log.info("Data pushed {} to {}", dataEnvelope.getDataHeader().getName(), URI_PUSHDATA);

    }

    @Override
    public List<DataEnvelope> getData(String blockType) {
    	    	
	        log.info("Query for data with header block type {}", blockType);
	        String uriComponents = UriComponentsBuilder.fromUri(URI_GETDATA.expand(blockType))
	                .build().toUriString();
	
	        ResponseEntity<DataEnvelope[]> response = restTemplate.getForEntity(uriComponents,DataEnvelope[].class);
	        DataEnvelope[]dataEnvolopes = response.getBody();
	        return Arrays.asList(dataEnvolopes);
	        
//	        return Arrays.asList(restTemplate.getForEntity(uriComponents,DataEnvelope[].class));
    }

    @Override
    public void updateData(DataEnvelope dataEnvelope, String blockName, String newBlockType) {
        log.info("Updating blocktype to {} for block with name {}", blockName, newBlockType);
        
        HttpClient client = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory httpRequestClient = new HttpComponentsClientHttpRequestFactory(client);
        httpRequestClient.setConnectionRequestTimeout(5000);
        httpRequestClient.setReadTimeout(5000);
        RestTemplate template= new RestTemplate();
        template.setRequestFactory(httpRequestClient); 
        
        String uriComponents = UriComponentsBuilder.fromUri(URI_PATCHDATA.expand(new Object[] {blockName, newBlockType}))
                .build().toUriString();

        template.patchForObject(uriComponents, dataEnvelope, Boolean.class);
        log.info("Updated blocktype to {} for block with name {}", blockName, newBlockType);
    }


}
