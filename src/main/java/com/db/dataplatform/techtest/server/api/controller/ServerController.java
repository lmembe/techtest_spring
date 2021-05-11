package com.db.dataplatform.techtest.server.api.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/dataserver")
@RequiredArgsConstructor
@Validated
public class ServerController {


    private final Server server;
    
    @PostMapping(value = "/pushdata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> pushData(@Valid @RequestBody DataEnvelope dataEnvelope) throws IOException, NoSuchAlgorithmException {

        log.info("Data envelope received: {}", dataEnvelope.getDataHeader().getName());
        boolean checksumPass = server.saveDataEnvelope(dataEnvelope);

        log.info("Data envelope persisted. Attribute name: {}", dataEnvelope.getDataHeader().getName());
        return ResponseEntity.ok(checksumPass);
    }
    
    @RequestMapping(value = "/update/{name}/{newBlockType}", 
    produces = "application/json", 
    method=RequestMethod.PATCH)
    public ResponseEntity<Boolean> updateData(@RequestParam("name") String name, @RequestParam("newBlockType") BlockTypeEnum newBlockType) throws IOException, NoSuchAlgorithmException {

        log.info("BlockName and BlockType received: {} and {}", name, newBlockType);
        
        boolean checksumPass = server.updateDataEnvelope(name, newBlockType);

        log.info("BlockName and BlockType persisted. Attribute names: {} and {}", name, newBlockType);
        return ResponseEntity.ok(checksumPass);
    } 
    
    @GetMapping(value = "/data/{blockType}")
    public List<DataEnvelope> getDataEnvelopeList(@RequestParam("blockType") BlockTypeEnum blockType) {
        log.info("Data List by blockType: {}", blockType);
        return server.getDataByBlockType(blockType);
    }

}
