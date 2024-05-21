package com.advantal.adminRoleModuleService.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String roleName;
	
	private String roleDescription;
	
	@OneToMany(targetEntity = RoleModuleMapping.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "role_id_fk", referencedColumnName = "id" )
	private List<RoleModuleMapping> roleModuleMappingList;
	
	private Date entryDate;
	
	private Date updationDate;
	
	private Short status;
				
}
