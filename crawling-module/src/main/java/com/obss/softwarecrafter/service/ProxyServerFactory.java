package com.obss.softwarecrafter.service;

import com.obss.softwarecrafter.model.contract.linkedin.ChangeStatusLinkedinAccountRequest;
import com.obss.softwarecrafter.model.contract.proxy.ChangeStatusProxyServerRequest;
import com.obss.softwarecrafter.model.contract.proxy.ChangeStatusProxyServerResponse;
import com.obss.softwarecrafter.model.dto.LinkedinAccountDto;
import com.obss.softwarecrafter.model.dto.proxy.ProxyServerDto;
import com.obss.softwarecrafter.model.dto.proxy.ReadProxyServerDto;
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
public class ProxyServerFactory {
    @Value("${gateway.auth.token}")
    private String gatewayToken;
    public ProxyServerDto getProxyServer() {
        List proxyServerDtoList = RestClient.builder().build()
                .get()
                .uri("http://localhost:8888/api/proxy-server/?active=true")
                .header("X-Gateway-Auth",gatewayToken)
                .retrieve()
                .body(List.class);
        if(proxyServerDtoList != null && !proxyServerDtoList.isEmpty()){
            return new ProxyServerDto(
                    (String) ((LinkedHashMap<?, ?>) proxyServerDtoList.get(0)).get("id"),
                    (String) ((LinkedHashMap<?, ?>) proxyServerDtoList.get(0)).get("ip"),
                    (Integer) ((LinkedHashMap<?, ?>) proxyServerDtoList.get(0)).get("port"),
                    (String) ((LinkedHashMap<?, ?>) proxyServerDtoList.get(0)).get("username"),
                    (String) ((LinkedHashMap<?, ?>) proxyServerDtoList.get(0)).get("password"),
                    (String) ((LinkedHashMap<?, ?>) proxyServerDtoList.get(0)).get("type"));
        } else {
            throw  new NoSuchElementException("aktif proxy server bulunamadı");
        }
    }

    public void disable(String id){
        try {
        ChangeStatusProxyServerRequest changeStatusProxyServerRequest = new ChangeStatusProxyServerRequest();
        changeStatusProxyServerRequest.setStatus(AccountStatusEnum.PASSIVE.ordinal());
        changeStatusProxyServerRequest.setIdList(List.of(UUID.fromString(id)));
            ChangeStatusProxyServerResponse response = RestClient.builder().build()
                .post()
                .uri("http://localhost:8888/api/proxy-server/change-status")
                .body(changeStatusProxyServerRequest)
                .header("X-Gateway-Auth", gatewayToken)
                .retrieve()
                .body(ChangeStatusProxyServerResponse.class);
            if (response != null && !response.getProxyServerDtoList().isEmpty()) {
                log.info("proxy server disabled, id: " + id);
            } else {
                log.error("proxy server cant disabled, id: " + id);
            }
        }catch (Exception e) {
            log.error("proxy server cant disabled, id: " + id, e);
        }
    }

}
