package com.obss.softwarecrafter.service;

import com.obss.softwarecrafter.exception.LinkedInAccountNotFoundException;
import com.obss.softwarecrafter.model.contract.linkedin.ChangeStatusLinkedinAccountRequest;
import com.obss.softwarecrafter.model.contract.linkedin.ChangeStatusLinkedinAccountResponse;
import com.obss.softwarecrafter.model.contract.proxy.ChangeStatusProxyServerRequest;
import com.obss.softwarecrafter.model.dto.LinkedinAccountDto;
import com.obss.softwarecrafter.model.enums.AccountStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class LinkedinAccountFactory {
    @Value("${gateway.auth.token}")
    private String gatewayToken;
    public LinkedinAccountDto getLinkedinAccount() {
        try {
            List linkedinAccountDtoList = RestClient.builder().build()
                    .get()
                    .uri("http://localhost:8888/api/linkedin-account/?active=true")
                    .header("X-Gateway-Auth", gatewayToken)
                    .retrieve()
                    .body(List.class);
            if (linkedinAccountDtoList != null && !linkedinAccountDtoList.isEmpty()) {
                return new LinkedinAccountDto(
                        (String) ((LinkedHashMap<?, ?>) linkedinAccountDtoList.get(0)).get("id"),
                        (String) ((LinkedHashMap<?, ?>) linkedinAccountDtoList.get(0)).get("username"),
                        (String) ((LinkedHashMap<?, ?>) linkedinAccountDtoList.get(0)).get("password"));
            } else {
                throw new NoSuchElementException("aktif linkedin hesabı bulunamadı");
            }
        } catch (Exception e) {
            log.error("linkedin hesabı çekilirken hata olustu",e);
            throw new LinkedInAccountNotFoundException();
        }
    }

    public void disable(String id){
        try {
            ChangeStatusLinkedinAccountRequest changeStatusLinkedinAccountRequest = new ChangeStatusLinkedinAccountRequest();
            changeStatusLinkedinAccountRequest.setStatus(AccountStatusEnum.PASSIVE.ordinal());
            changeStatusLinkedinAccountRequest.setIdList(List.of(UUID.fromString(id)));
            ChangeStatusLinkedinAccountResponse response = RestClient.builder().build()
                    .post()
                    .uri("http://localhost:8888/api/linkedin-account/change-status")
                    .body(changeStatusLinkedinAccountRequest)
                    .header("X-Gateway-Auth", gatewayToken)
                    .retrieve()
                    .body(ChangeStatusLinkedinAccountResponse.class);
            if (response != null && !response.getLinkedinAccountList().isEmpty()) {
                log.info("linkedin account disabled, id: " + id);
            } else {
                log.error("linkedin account cant disabled, id: " + id);
            }
        }catch (Exception e) {
            log.error("linkedin account cant disabled, id: " + id, e);
        }
    }

}
