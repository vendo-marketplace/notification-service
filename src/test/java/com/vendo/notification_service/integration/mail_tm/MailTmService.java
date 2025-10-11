package com.vendo.notification_service.integration.mail_tm;

import com.vendo.notification_service.integration.mail_tm.dto.GetDomainsResponse;
import com.vendo.notification_service.integration.mail_tm.dto.GetMessagesResponse;
import com.vendo.notification_service.integration.mail_tm.dto.GetTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.vendo.notification_service.common.helper.WaitHelper.waitSafely;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailTmService {

    private final MailTmClient mailTmClient;

    public String createAddressWithDomainOncePerSecond(String emailPrefix, String password) {
        GetDomainsResponse domains = mailTmClient.getDomains();
        GetDomainsResponse.Domain domain = domains.getDomains().get(0);

        String mailTmAddress = "%s@%s".formatted(emailPrefix, domain.getDomain());

        mailTmClient.createAccount(mailTmAddress, password);

        waitSafely(1100);
        return mailTmAddress;
    }

    public GetMessagesResponse retrieveTextFromMessage(String address, String password) {
        GetTokenResponse tokenResponse = mailTmClient.getToken(address, password);
        return mailTmClient.getMessages(tokenResponse.getToken());
    }
}
