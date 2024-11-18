package com.obss.softwarecrafter.controller;

import com.obss.softwarecrafter.mapper.LinkedinAccountMapper;
import com.obss.softwarecrafter.model.contract.*;
import com.obss.softwarecrafter.model.dto.ReadLinkedinAccountDto;
import com.obss.softwarecrafter.model.enums.AccountStatusEnum;
import com.obss.softwarecrafter.model.jpa.entity.LinkedinAccountEntity;
import com.obss.softwarecrafter.service.LinkedinAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/linkedin-account")
public class LinkedinAccountController {
    private final LinkedinAccountService linkedinAccountService;
    private final LinkedinAccountMapper linkedinAccountMapper;

    @PostMapping("/")
    public ResponseEntity<CreateLinkedinAccountResponse> create(@RequestBody CreateLinkedinAccountRequest request) {
        List<ReadLinkedinAccountDto> linkedinAccountDtoList = request.getLinkedinAccountList().stream().map(linkedinAccountDto ->
                linkedinAccountMapper.entityToDto(linkedinAccountService.create(linkedinAccountMapper.createUpdateDtoToEntity(linkedinAccountDto)))
        ).toList();

        return ResponseEntity.ok(new CreateLinkedinAccountResponse(linkedinAccountDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadLinkedinAccountDto> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(linkedinAccountMapper.entityToDto(linkedinAccountService.getById(id)));
    }

    @GetMapping("/")
    public ResponseEntity<List<ReadLinkedinAccountDto>> getAll(@RequestParam(required = false) String active) {
        if(Objects.nonNull(active)){
            List<LinkedinAccountEntity> byStatusList = linkedinAccountService.getByStatusList(Boolean.parseBoolean(active) ? AccountStatusEnum.ACTIVE : AccountStatusEnum.PASSIVE);
            return ResponseEntity.ok(byStatusList.stream().map(linkedinAccountMapper::entityToDto).toList());
        }
        return ResponseEntity.ok(linkedinAccountService.getAll().stream().map(linkedinAccountMapper::entityToDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        linkedinAccountService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch-delete")
    public ResponseEntity<?> deleteByList(@RequestBody List<UUID> idList) {
        linkedinAccountService.deleteByIdList(idList);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateLinkedinAccountResponse> update(@PathVariable UUID id, @RequestBody UpdateLinkedinAccountRequest request ) {
        LinkedinAccountEntity updated = linkedinAccountService.update(id, linkedinAccountMapper.updateDtoToEntity(request));
        return ResponseEntity.ok(new UpdateLinkedinAccountResponse(linkedinAccountMapper.entityToDto(updated)));
    }

    @PostMapping("/change-status")
    public ResponseEntity<ChangeStatusLinkedinAccountResponse> changeStatus(@RequestBody ChangeStatusLinkedinAccountRequest request) {
        List<ReadLinkedinAccountDto> linkedinAccountDtoList = request.getIdList().stream().map(uuid -> {
            LinkedinAccountEntity linkedinAccountEntity = linkedinAccountService.getById(uuid);
            linkedinAccountEntity.setStatus(AccountStatusEnum.values()[request.getStatus()]);
            return linkedinAccountMapper.entityToDto(linkedinAccountService.update(linkedinAccountEntity));
        }).toList();

        return ResponseEntity.ok(new ChangeStatusLinkedinAccountResponse(linkedinAccountDtoList));
    }
}
