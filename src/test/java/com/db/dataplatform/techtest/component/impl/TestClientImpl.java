package com.db.dataplatform.techtest.component.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.db.dataplatform.techtest.client.api.model.DataBody;
import com.db.dataplatform.techtest.client.api.model.DataEnvelope;
import com.db.dataplatform.techtest.client.api.model.DataHeader;
import com.db.dataplatform.techtest.client.component.Client;
import com.db.dataplatform.techtest.client.component.impl.ClientImpl;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;

public class TestClientImpl {

    private Client client;

    @Mock
    public RestTemplate restTemplate;
    @Mock
    public DataEnvelope dataEnvelope;
    @Mock
    public DataHeader dataHeader;
    
    @Mock
    public DataBody dataBody;
    
    @Before
    public void Setup()
    {
        initMocks(this);
        client = new ClientImpl(restTemplate);
        when(dataEnvelope.getDataHeader()).thenReturn(dataHeader);
        when(dataEnvelope.getDataBody()).thenReturn(dataBody);
        when(dataHeader.getName()).thenReturn("name");
        when(dataHeader.getBlockType()).thenReturn(BlockTypeEnum.BLOCKTYPEA);
        when(dataBody.getDataBody()).thenReturn("This is Lester testing");
        
    }

    @Test
    public void pushData() throws Exception {
        client.pushData(dataEnvelope);
        verify(restTemplate).postForObject(anyString(),eq(dataEnvelope),eq(Boolean.class),anyMap());
    }

    @Test
    public void getData() throws Exception
    {
        when(restTemplate.getForObject(any(),any())).thenReturn(new DataEnvelope[] {});
        client.getData("blockType");
        verify(restTemplate).getForObject(any(),any());
    }
}