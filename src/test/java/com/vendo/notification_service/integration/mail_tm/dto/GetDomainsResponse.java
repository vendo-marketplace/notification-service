package com.vendo.notification_service.integration.mail_tm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDomainsResponse {

    @JsonProperty("hydra:totalItems")
    private int totalItems;

    @JsonProperty("hydra:member")
    private List<Domain> domains;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Domain {

        private String id;

        private String domain;

        @JsonProperty("isActive")
        private boolean isActive;
    }
}
