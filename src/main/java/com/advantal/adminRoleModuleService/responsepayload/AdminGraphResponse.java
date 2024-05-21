package com.advantal.adminRoleModuleService.responsepayload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AdminGraphResponse {

	private String label;

	private int year;

	private List<MonthObject> monthList;
}
