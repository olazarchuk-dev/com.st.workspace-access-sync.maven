package com.programming.techie.mongo.service.mapper;

import com.programming.techie.mongo.dto.WorkspaceStageDTO;
import com.programming.techie.mongo.entity.WorkspaceStage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkspaceStageMapper {

    @Mapping(target = "date", expression = "java( entity.getDate()!=null ? java.time.Instant.ofEpochMilli(entity.getDate()) : null )")
    @Mapping(target = "createdAt", expression = "java( entity.getCreatedAt()!=null ? java.time.Instant.ofEpochMilli(entity.getCreatedAt()) : null )")
    @Mapping(target = "updatedAt", expression = "java( entity.getUpdatedAt()!=null ? java.time.Instant.ofEpochMilli(entity.getUpdatedAt()) : null )")
    @Mapping(target = "payloadDecode", expression = "java( com.programming.techie.mongo.utils.GzipUtil.doHandleDecompressPayload(entity.getPayload()) )")
    WorkspaceStageDTO toDto(WorkspaceStage entity);

    @Mapping(target = "date", expression = "java( dto.getDate()!=null ? dto.getDate().toEpochMilli() : null )")
    @Mapping(target = "createdAt", expression = "java( dto.getCreatedAt()!=null ? dto.getCreatedAt().toEpochMilli() : null )")
    @Mapping(target = "updatedAt", expression = "java( dto.getUpdatedAt()!=null ? dto.getUpdatedAt().toEpochMilli() : null )")
    WorkspaceStage toEntity(WorkspaceStageDTO dto);

}
