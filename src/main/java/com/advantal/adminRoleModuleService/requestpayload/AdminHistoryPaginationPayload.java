package com.advantal.adminRoleModuleService.requestpayload;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminHistoryPaginationPayload {

	@NotNull(message = "PageIndex can't be null")
	private int pageIndex;

	@NotNull(message = "PageSize can't be null")
	private int pageSize;

	@NotNull(message = "KeyWord can't be null")
	private String keyWord;
}
