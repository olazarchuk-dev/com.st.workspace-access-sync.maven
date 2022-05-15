package com.programming.techie.mongo.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class WorkspaceStageDTO {
    private String id;
    private Boolean active;
    private String backgroundColor;
    private Instant date;
    private String name;
    private String oldid;
    private String owner;
    private String payload;
    private String payloadDecode;
    private Integer revision;
    private String screenshot;
    private Integer layout;
    private List<String> tags;
    private String title;
    private Instant createdAt;
    private Instant updatedAt;
}
