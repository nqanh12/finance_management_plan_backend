package com.quocanh.finance_manager_plan_backend.mapper;

import com.quocanh.finance_manager_plan_backend.dto.request.CategoryDTO;
import com.quocanh.finance_manager_plan_backend.entity.Categories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "userId", source = "user.id")
    CategoryDTO toDTO(Categories category);

    @Mapping(target = "user.id", source = "userId")
    Categories toEntity(CategoryDTO categoryDTO);
}
