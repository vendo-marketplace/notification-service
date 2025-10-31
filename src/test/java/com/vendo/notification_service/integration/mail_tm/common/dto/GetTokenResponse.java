package com.vendo.notification_service.integration.mail_tm.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTokenResponse {

    private String token;

}
