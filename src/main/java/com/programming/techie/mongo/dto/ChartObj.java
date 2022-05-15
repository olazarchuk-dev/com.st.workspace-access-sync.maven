package com.programming.techie.mongo.dto;

import lombok.Data;

@Data
public class ChartObj {

    private Chart obj;

    @Data
    public static class Chart {
        private String id;
        private Instrument instrument;
        private Long timeInterval;
        private Boolean isVisible;
        private Integer index;
        private Boolean isPlaceHolder;
        private String symbol;
        private Long timestamp;
        private String barType;
//        private String author; // TODO ?
//        private String state; // TODO ?
//        private String moveKind; // TODO ?
    }

    @Data
    public static class Instrument {
        private String symbol;
        private String company;
        private String exchange;
    }
}
