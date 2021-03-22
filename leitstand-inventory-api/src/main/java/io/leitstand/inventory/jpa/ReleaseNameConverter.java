package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.service.ReleaseName;

@Converter
public class ReleaseNameConverter implements AttributeConverter<ReleaseName, String>{

    @Override
    public String convertToDatabaseColumn(ReleaseName attribute) {
        return Scalar.toString(attribute);
    }

    @Override
    public ReleaseName convertToEntityAttribute(String dbData) {
        return ReleaseName.valueOf(dbData);
    }
}
