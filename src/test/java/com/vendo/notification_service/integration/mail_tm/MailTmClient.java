package com.vendo.notification_service.integration.mail_tm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendo.notification_service.common.helper.OkHttpHelper;
import com.vendo.notification_service.integration.mail_tm.dto.AccountRequest;
import com.vendo.notification_service.integration.mail_tm.dto.GetDomainsResponse;
import com.vendo.notification_service.integration.mail_tm.dto.GetMessagesResponse;
import com.vendo.notification_service.integration.mail_tm.dto.GetTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.vendo.notification_service.common.helper.WaitHelper.waitSafely;
import static com.vendo.security.common.constants.AuthConstants.AUTHORIZATION_HEADER;
import static com.vendo.security.common.constants.AuthConstants.BEARER_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailTmClient {

    private final ObjectMapper objectMapper;

    private final OkHttpHelper okHttpHelper;

    private final OkHttpClient okHttpClient;

    private static final String MAIL_TM_BASE_URL= "https://api.mail.tm";

    public void createAccount(String address, String password) throws IOException {
        String createAccountUrl = MAIL_TM_BASE_URL + "/accounts";
        AccountRequest accountRequest = AccountRequest.builder()
                .address(address)
                .password(password)
                .build();

        RequestBody requestBody = RequestBody.create(objectMapper.writeValueAsString(accountRequest), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(createAccountUrl)
                .post(requestBody)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()){
            okHttpHelper.buildResponseBodyOrThrow(response, "MailTmApi error: " + response.code() + " - " + response.message());
        }
    }

    public void createAccountRetrying(String address, String password) {
        int attempts = 3;
        while (attempts != 0) {
            try {
                createAccount(address, password);
                return;
            } catch (IOException e) {
                attempts--;
                waitSafely(1000);
            }
        }
    }

    public GetTokenResponse getToken(String address, String password) {
        try {
            String createAccountUrl = MAIL_TM_BASE_URL + "/token";
            AccountRequest accountRequest = AccountRequest.builder()
                    .address(address)
                    .password(password)
                    .build();

            RequestBody requestBody = RequestBody.create(objectMapper.writeValueAsString(accountRequest), MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(createAccountUrl)
                    .post(requestBody)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            String responseBody = okHttpHelper.buildResponseBodyOrThrow(response, "MailTmApi error: " + response.code() + " - " + response.message());
            response.close();

            return objectMapper.readValue(responseBody, GetTokenResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception while getting token from MailTm");
        }
    }

    public GetDomainsResponse getDomains() {
        try {
            String createAccountUrl = MAIL_TM_BASE_URL + "/domains";
            Request request = new Request.Builder()
                    .url(createAccountUrl)
                    .get()
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            String responseBody = okHttpHelper.buildResponseBodyOrThrow(response, "MailTmApi error: " + response.code() + " - " + response.message());
            response.close();

            GetDomainsResponse getDomainsResponse = objectMapper.readValue(responseBody, GetDomainsResponse.class);
            if (getDomainsResponse.getTotalItems() == 0) {
                throw new RuntimeException("No domain found in MailTm");
            }

            return getDomainsResponse;
        } catch (IOException e) {
            throw new RuntimeException("Exception while getting domains from MailTm");
        }
    }

    public GetMessagesResponse getMessages(String token) {
        try {
            String createAccountUrl = MAIL_TM_BASE_URL + "/messages";
            Request request = new Request.Builder()
                    .url(createAccountUrl)
                    .header(AUTHORIZATION_HEADER,  BEARER_PREFIX + token)
                    .get()
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            String responseBody = okHttpHelper.buildResponseBodyOrThrow(response, "MailTmApi error: " + response.code() + " - " + response.message());
            response.close();

            return objectMapper.readValue(responseBody, GetMessagesResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception while getting messages from MailTm");
        }
    }
}
