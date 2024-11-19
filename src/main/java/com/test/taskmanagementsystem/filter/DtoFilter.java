package com.test.taskmanagementsystem.filter;

import org.springframework.data.jpa.domain.Specification;

public interface DtoFilter<FilterDto, Entity> {

    boolean isApplicable(FilterDto filters);

    Specification<Entity> toSpecification(FilterDto filters);
}
