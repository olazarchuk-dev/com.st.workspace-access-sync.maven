package com.programming.techie.mongo.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.programming.techie.mongo.entity.Stage;
import com.programming.techie.mongo.utils.GzipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.programming.techie.mongo.utils.GzipUtil.UTF_8;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@ChangeLog
@Slf4j
public class StageSyncChangeLog {
    private ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();

    private final AtomicInteger allCounter = new AtomicInteger(0);
    private final AtomicInteger successfulUpdatesCounter = new AtomicInteger(0);
    private final AtomicInteger warningUpdatesCounter = new AtomicInteger(0);

    public final int PER_QUERY_LIMIT = 5;

    @Value("${app.sync.update-expiration-seconds}")
    public int updateExpirationSeconds = 30;

    @ChangeSet(order = "004", id = "Sync script Payload decompress on workspace.Stage", author = "olazarchuk-dev", runAlways = true)
    public void syncPayloadDecodeOnStage(MongockTemplate mongockTemplate) {
        log.info("Start sync script do PayloadDecode to Database");

        var query = getStageQuery();
        List<Stage> stages = mongockTemplate.find(query, Stage.class);

        while (!stages.isEmpty()) {
            stages.stream()
                    .map(this::setStageUpdate)
                    .forEach(stage -> {
                        var criteriaUpdate = where("_id").is(stage.getId());
                        Update stageUpdate = new Update()
                                .set("payloadDecode", stage.getPayloadDecode())
                                .set("updatedAt", stage.getUpdatedAt());
                        mongockTemplate.findAndModify(new Query(criteriaUpdate), stageUpdate, Stage.class);
                    });

            stages = mongockTemplate.find(query, Stage.class);
        }

        log.info("Found by criteria of sync = {} stage(s)", allCounter);
        if (successfulUpdatesCounter.get() != 0)
            log.info("Successful sync updated = {} stage(s)", successfulUpdatesCounter);
        if (warningUpdatesCounter.get() != 0)
            log.warn("Error sync update = {} stage(s)", warningUpdatesCounter);
        log.info("Finish sync script do PayloadDecode to Database");
    }

    private Query getStageQuery() {
        var ltExpirationDate = Instant.now().minusSeconds(updateExpirationSeconds).toEpochMilli();
        var criteriaExpirationUpdate = Criteria.where("updatedAt").lt(ltExpirationDate);
        var query = Query.query(new Criteria().andOperator(criteriaExpirationUpdate));
        query.fields().include("_id", "payload");
        query.limit(PER_QUERY_LIMIT);

        return query;
    }

    private Stage setStageUpdate(Stage entity) {
        allCounter.getAndIncrement();
        try {
            var decompressPayload = GzipUtil.toDecompress(entity.getPayload(), UTF_8);
            var payloadDecode = getPayloadDecode(decompressPayload);
            entity.setPayloadDecode(payloadDecode);
            entity.setUpdatedAt(Instant.now().toEpochMilli());
            successfulUpdatesCounter.getAndIncrement();
        } catch (Exception ex) {
            ex.printStackTrace(); // FIXME
            warningUpdatesCounter.getAndIncrement();
        }

        return entity;
    }

    private Stage.PayloadDecodeObject getPayloadDecode(String payload) throws JsonProcessingException {
        var payloadDecodeSubstrStart = 40;
        var payloadDecodeSubstrEnd = payload.length() - 1;
        var payloadDecode = payload.substring(payloadDecodeSubstrStart, payloadDecodeSubstrEnd);

        return objectMapper.readValue(payloadDecode, Stage.PayloadDecodeObject.class);
    }
}
