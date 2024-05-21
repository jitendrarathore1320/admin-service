package com.advantal.adminRoleModuleService.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "module")
public class Module{
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "uuid",unique = true)
	private String uuId;
	
	private Short moduleAction;
	
	private String parentModuleName;//
	
	private String moduleName;
	
	private String moduleCode;//

	private Short addAction;

	private Short updateAction;

	private Short deleteAction;

	private Short viewAction;
	
	private Short downloadAction;
	
	private Date entryDate;
		
	private Short status;
	

}
