package com.negotium.reminder.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.CollectionMappingStrategy.ADDER_PREFERRED;
import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

@MapperConfig(
    componentModel = "spring",
    collectionMappingStrategy = ADDER_PREFERRED,
    nullValueCheckStrategy = ALWAYS,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MappingConfig {
}
