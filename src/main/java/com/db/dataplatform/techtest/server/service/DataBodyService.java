package com.db.dataplatform.techtest.server.service;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;

import java.util.List;
import java.util.Optional;

public interface DataBodyService {
    void saveDataBody(DataBodyEntity dataBody);
    List<DataEnvelope> getDataByBlockType(BlockTypeEnum blockType);
    Optional<DataBodyEntity> getDataByBlockName(String blockName);
    void updateBlockType(DataBodyEntity dataBodyEntity);
}
