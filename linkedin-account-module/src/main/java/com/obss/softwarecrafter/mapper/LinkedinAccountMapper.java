package com.obss.softwarecrafter.mapper;

import com.obss.softwarecrafter.model.contract.UpdateLinkedinAccountRequest;
import com.obss.softwarecrafter.model.dto.CreateUpdateLinkedinAccountDto;
import com.obss.softwarecrafter.model.dto.ReadLinkedinAccountDto;
import com.obss.softwarecrafter.model.jpa.entity.LinkedinAccountEntity;
import org.mapstruct.Mapper;

@Mapper
public interface LinkedinAccountMapper {
    ReadLinkedinAccountDto entityToDto(LinkedinAccountEntity linkedinAccountEntity);

    LinkedinAccountEntity createUpdateDtoToEntity(CreateUpdateLinkedinAccountDto createUpdateLinkedinAccountDto);

    LinkedinAccountEntity updateDtoToEntity(UpdateLinkedinAccountRequest updateLinkedinAccountRequest);

}

