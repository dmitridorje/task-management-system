package com.test.taskmanagementsystem.model.dto.requestdtos;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChangeStatusDto {

    @Pattern(regexp = "^(PENDING|IN_PROGRESS|COMPLETED)$", message = "Status must be one of PENDING, IN_PROGRESS, COMPLETED")
    private String status;
}
