package com.vendo.notification_service.integration.mail_tm;

import com.vendo.notification_service.integration.mail_tm.common.dto.GetDomainsResponse;
import com.vendo.notification_service.integration.mail_tm.common.dto.GetMessagesResponse;
import com.vendo.notification_service.integration.mail_tm.common.dto.GetTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailTmService {

    private final MailTmClient mailTmClient;

    public String createAddressWithDomain(String emailPrefix, String password) {
        GetDomainsResponse domains = mailTmClient.getDomains();
        GetDomainsResponse.Domain domain = domains.getDomains().get(0);

        String mailTmAddress = "%s@%s".formatted(emailPrefix, domain.getDomain());

        mailTmClient.createAccountRetrying(mailTmAddress, password);

        return mailTmAddress;
    }

    public GetMessagesResponse retrieveTextFromMessage(String address, String password) {
        GetTokenResponse tokenResponse = mailTmClient.getToken(address, password);
        return mailTmClient.getMessages(tokenResponse.getToken());
    }
}
