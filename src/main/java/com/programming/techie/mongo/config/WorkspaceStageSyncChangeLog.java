package com.programming.techie.mongo.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.programming.techie.mongo.dto.ChartObj;
import com.programming.techie.mongo.dto.WorkspaceStageDTO;
import com.programming.techie.mongo.entity.WorkspaceStage;
import com.programming.techie.mongo.service.mapper.WorkspaceStageMapper;
import com.programming.techie.mongo.service.mapper.WorkspaceStageMapperImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@ChangeLog
@Slf4j
public class WorkspaceStageSyncChangeLog {
    private WorkspaceStageMapper workspaceStageMapper = new WorkspaceStageMapperImpl();
    private ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();

    private final AtomicInteger successfulUpdatesCounter = new AtomicInteger();
    private final AtomicInteger errorUpdatesCounter = new AtomicInteger();

    public final int PER_QUERY_LIMIT = 5;

    @Value("${app.sync.update-expiration-seconds}")
    public int updateExpirationSeconds = 30;

    @ChangeSet(order = "004", id = "Sync Decode-Payload on workspace.Stage", author = "olazarchuk-dev", runAlways = true)
    public void syncPayloadDecodeOnStage(MongockTemplate mongockTemplate) {
        log.info("Order-ChangeSet | Start Sync-PayloadDecode to Database");

        var ltExpirationDate = Instant.now().minusSeconds(updateExpirationSeconds).toEpochMilli();
        var criteriaExpirationUpdate = Criteria.where("updatedAt").lt(ltExpirationDate);
        var query = Query.query(new Criteria().andOperator(criteriaExpirationUpdate));
        query.fields().include("_id", "payload");
        query.limit(PER_QUERY_LIMIT);

        List<WorkspaceStage> stages = mongockTemplate.find(query, WorkspaceStage.class);

        while (!stages.isEmpty()) {
            stages.forEach(entity -> {
                try {
                    var dto = workspaceStageMapper.toDto(entity);
                    setPayloadDecode(dto);
                    dto.setUpdatedAt(Instant.now());
                    var saveEntity = workspaceStageMapper.toEntity(dto);

                    var criteriaUpdate = where("_id").is(entity.getId());
                    Update update = new Update()
                            .set("payloadDecode", saveEntity.getPayloadDecode())
                            .set("updatedAt", saveEntity.getUpdatedAt());
                    mongockTemplate.findAndModify(new Query(criteriaUpdate), update, WorkspaceStage.class);

                    successfulUpdatesCounter.getAndIncrement();
                } catch (Exception ex) {
                    errorUpdatesCounter.getAndIncrement();
                }
            });

            stages = mongockTemplate.find(query, WorkspaceStage.class);
        }

        log.info("Order-ChangeSet | Successful count update = {} stage(s)", successfulUpdatesCounter);
        log.info("Order-ChangeSet | Error count update = {} stage(s)", errorUpdatesCounter);
        log.info("Order-ChangeSet | Finish Sync-PayloadDecode to Database");
    }

    private void setPayloadDecode(WorkspaceStageDTO dto) {
        var payloadDecode = dto.getPayloadDecode();
        if (StringUtils.isNotBlank(payloadDecode)) {
            var payloadDecodeSubstr = payloadDecode.substring(40, payloadDecode.length() - 1);
            try {
                var chart = objectMapper.readValue(payloadDecodeSubstr, ChartObj.Chart.class);
                var payloadChart = objectMapper.writeValueAsString(chart);
                dto.setPayloadDecode(payloadChart);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
