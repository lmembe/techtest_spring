package com.db.dataplatform.techtest.server.component.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.DataBodyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerImpl implements Server {

    private final DataBodyService dataBodyServiceImpl;
    private final ModelMapper modelMapper;

    /**
     * @param envelope
     * @return true if there is a match with the client provided checksum.
     */
    @Override
    public boolean saveDataEnvelope(DataEnvelope envelope) {

        // Save to persistence.
        persist(envelope);

        log.info("Data persisted successfully, data name: {}", envelope.getDataHeader().getName());
        return true;
    }

    private void persist(DataEnvelope envelope) {
        log.info("Persisting data with attribute name: {}", envelope.getDataHeader().getName());
        DataHeaderEntity dataHeaderEntity = modelMapper.map(envelope.getDataHeader(), DataHeaderEntity.class);

        DataBodyEntity dataBodyEntity = modelMapper.map(envelope.getDataBody(), DataBodyEntity.class);
        dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);

        saveData(dataBodyEntity);
    }

    private void saveData(DataBodyEntity dataBodyEntity) {
        dataBodyServiceImpl.saveDataBody(dataBodyEntity);
    }

	@Override
	public boolean updateDataEnvelope(String blockName, BlockTypeEnum newBlockType) throws IOException, NoSuchAlgorithmException {
        log.info("Persisting data with attribute names: {} and {}", blockName, newBlockType);
        
        DataHeaderEntity dataHeaderEntity = modelMapper.map(newBlockType, DataHeaderEntity.class);
        DataBodyEntity dataBodyEntity = modelMapper.map(new DataEnvelope().getDataBody(), DataBodyEntity.class);
        dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);

        saveData(dataBodyEntity);
		return true;
	}

	@Override
	public List<DataEnvelope> getDataByBlockType(BlockTypeEnum blockType) {
		return dataBodyServiceImpl.getDataByBlockType(blockType);
	}
    
    
    

}
