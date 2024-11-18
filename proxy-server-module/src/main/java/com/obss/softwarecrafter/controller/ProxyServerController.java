package com.obss.softwarecrafter.controller;

import com.obss.softwarecrafter.mapper.ProxyServerMapper;
import com.obss.softwarecrafter.model.contract.*;
import com.obss.softwarecrafter.model.dto.ReadProxyServerDto;
import com.obss.softwarecrafter.model.enums.AccountStatusEnum;
import com.obss.softwarecrafter.model.jpa.entity.ProxyServerEntity;
import com.obss.softwarecrafter.service.ProxyServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/proxy-server")
public class ProxyServerController {
    private final ProxyServerService proxyServerService;
    private final ProxyServerMapper proxyServerMapper;

    @PostMapping("/")
    public ResponseEntity<CreateProxyServerResponse> create(@RequestBody CreateProxyServerRequest request) {
        List<ReadProxyServerDto> proxyServerDtoList = request.getProxyServerList().stream().map(proxyServerDto ->
                proxyServerMapper.entityToDto(proxyServerService.create(proxyServerMapper.createUpdateDtoToEntity(proxyServerDto)))
        ).toList();

        return ResponseEntity.ok(new CreateProxyServerResponse(proxyServerDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadProxyServerDto> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(proxyServerMapper.entityToDto(proxyServerService.getById(id)));
    }

    @GetMapping("/")
    public ResponseEntity<List<ReadProxyServerDto>> getAll(@RequestParam(required = false) String active) {
        if(Objects.nonNull(active)){
            List<ProxyServerEntity> byStatusList = proxyServerService.getByStatusList(Boolean.parseBoolean(active) ? AccountStatusEnum.ACTIVE : AccountStatusEnum.PASSIVE);
            return ResponseEntity.ok(byStatusList.stream().map(proxyServerMapper::entityToDto).toList());
        }
        return ResponseEntity.ok(proxyServerService.getAll().stream().map(proxyServerMapper::entityToDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable("id") UUID crawlingId) {
        proxyServerService.deleteById(crawlingId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch-delete")
    public ResponseEntity<?> deleteByList(@RequestBody List<UUID> idList) {
        proxyServerService.deleteByIdList(idList);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateProxyServerResponse> update(@PathVariable UUID id, @RequestBody UpdateProxyServerRequest request ) {
        ProxyServerEntity updated = proxyServerService.update(id, proxyServerMapper.updateDtoToEntity(request));
        return ResponseEntity.ok(new UpdateProxyServerResponse(proxyServerMapper.entityToDto(updated)));
    }

    @PostMapping("/change-status")
    public ResponseEntity<ChangeStatusProxyServerResponse> changeStatus(@RequestBody ChangeStatusProxyServerRequest request) {
        List<ReadProxyServerDto> proxyServerDtoList = request.getIdList().stream().map(uuid -> {
            ProxyServerEntity proxyServerEntity = proxyServerService.getById(uuid);
            proxyServerEntity.setStatus(AccountStatusEnum.values()[request.getStatus()]);
            return proxyServerMapper.entityToDto(proxyServerService.update(proxyServerEntity));
        }).toList();

        return ResponseEntity.ok(new ChangeStatusProxyServerResponse(proxyServerDtoList));
    }

    @PostMapping("/change-proxy")
    public ResponseEntity<ChangeStatusProxyServerResponse> changeProxy(@RequestBody ChangeStatusProxyServerRequest request) {
        List<ReadProxyServerDto> proxyServerDtoList = request.getIdList().stream().map(uuid -> {
            ProxyServerEntity proxyServerEntity = proxyServerService.getById(uuid);
            proxyServerEntity.setStatus(AccountStatusEnum.PASSIVE);
            proxyServerService.update(proxyServerEntity);

//            proxyServerService.getByStatusList()

            return proxyServerMapper.entityToDto(proxyServerService.update(proxyServerEntity));
        }).toList();

        return ResponseEntity.ok(new ChangeStatusProxyServerResponse(proxyServerDtoList));
    }
}
