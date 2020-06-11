package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ReleaseName;

@Converter
public class ReleaseNameConverter implements AttributeConverter<ReleaseName, String>{

    @Override
    public String convertToDatabaseColumn(ReleaseName attribute) {
        return ReleaseName.toString(attribute);
    }

    @Override
    public ReleaseName convertToEntityAttribute(String dbData) {
        return ReleaseName.valueOf(dbData);
    }
}
