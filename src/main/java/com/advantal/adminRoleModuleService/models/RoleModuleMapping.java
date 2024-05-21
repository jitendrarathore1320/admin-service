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
@Table(name = "role_module_mapping")
public class RoleModuleMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="role_id_fk")
//	private Long role_id_fk;
	private Long roleIdFk;
	
//	private Long moduleId;
	
	private Short moduleAction;

	private String moduleName;

	private String parentModuleName;

	private String moduleCode;

	private Short addAction;

	private Short updateAction;

	private Short deleteAction;

	private Short viewAction;

	private Short downloadAction;

	private Date entryDate;

	private Date updationDate;

	private Short status;

}
