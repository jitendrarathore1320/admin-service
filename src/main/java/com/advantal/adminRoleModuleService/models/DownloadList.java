package com.advantal.adminRoleModuleService.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadList {

	  private List<Download> downloadList;

	  
	    private int totalCount;

}
