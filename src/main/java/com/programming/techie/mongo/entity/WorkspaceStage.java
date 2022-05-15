package com.programming.techie.mongo.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "stage")
@ToString
@Builder
public class WorkspaceStage {

    @Id
    private String id;
    private Boolean active;
    private String backgroundColor;
    private Long date;
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
    private Long createdAt;
    private Long updatedAt;
}
