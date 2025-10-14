package com.nordic.backend.company.features.admin.common.mapper;

import com.nordic.backend.company.features.admin.common.dto.AdminFetchDto;
import com.nordic.backend.company.features.admin.common.dto.AdminUpdateCreateDto;
import com.nordic.backend.company.features.admin.model.AdminModel;

public class EntityDtoMapper {

    public static AdminFetchDto toDTOResponse(AdminModel adminModel){
        return new AdminFetchDto(adminModel.getId(), adminModel.getFirstname(), adminModel.getLastname(), adminModel.getEmail(), adminModel.getImageurl());
    }

    public static AdminUpdateCreateDto toDTORequest(AdminModel adminModel){
        return new AdminUpdateCreateDto(adminModel.getId(), adminModel.getFirstname(), adminModel.getLastname(), adminModel.getPassword(), adminModel.getEmail(), adminModel.getRole(), adminModel.getImageurl());
    }

    public static AdminModel toModel(AdminUpdateCreateDto adminUpdateCreateDto){
        return  new AdminModel(adminUpdateCreateDto.getId(), adminUpdateCreateDto.getFirstname(), adminUpdateCreateDto.getLastname(), adminUpdateCreateDto.getPassword(), adminUpdateCreateDto.getEmail(),adminUpdateCreateDto.getRole(), adminUpdateCreateDto.getImageurl());
    }
}
