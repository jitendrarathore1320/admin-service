package com.advantal.adminRoleModuleService.models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin")
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String mobile;

	private String email;

	private String password;

	private Date entryDate;

	private Date updationDate;
	
	private Date passwordUpdationDate;

	private Short status;
	
	private Long roleId;
	// new parameter
    private String imageUrl;
	
	private String country;
	
	private String city;
	
	private String pinCode;
	
	private String address;

	@OneToOne(targetEntity = Role.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "role_id_fk", referencedColumnName = "id")
	private Role role;
	
}
