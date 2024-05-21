package com.advantal.adminRoleModuleService.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MonthObject {

	private String month;

	private Long count;

	private Long active;
}
