package com.vendo.notification_service.integration.mail_tm.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRequest {

    private String address;

    private String password;

}
