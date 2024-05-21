package com.advantal.adminRoleModuleService.responsepayload;

import java.util.List;

import com.advantal.adminRoleModuleService.models.Download;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadResponse {

	private List<Download> downloadList;
	private Long totalCount;

}
