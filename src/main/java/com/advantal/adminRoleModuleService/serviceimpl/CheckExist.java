package com.advantal.adminRoleModuleService.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advantal.adminRoleModuleService.models.Admin;
import com.advantal.adminRoleModuleService.models.Role;
import com.advantal.adminRoleModuleService.repository.AdminRepository;
import com.advantal.adminRoleModuleService.repository.RoleRepository;
import com.advantal.adminRoleModuleService.utils.Constant;

@Service
public class CheckExist {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AdminRepository adminRepository;


	public boolean isExistRoleName(String roleName) {
		Role role = roleRepository.findByRoleNameAndStatus(roleName, Constant.ONE);
		if (role != null)
			return true;
		else
			return false;
	}

	public boolean isExistRoleNameAndId(String roleName, Long id) {
		Role role = roleRepository.findByRoleNameAndIdAndStatus(roleName, id, Constant.ONE);
		if (role != null)
			return true;
		else
			return false;
	}

	public boolean isExistEmailAndStatus(String email) {
	Admin admin=adminRepository.findByEmailAndStatus(email, Constant.ONE);
		if (admin !=null)
			return true;
		else
			return false;
	}
	
	public boolean isExistMobile(String mobile) {
		Admin admin=adminRepository.findByMobileAndStatus(mobile, Constant.ONE);
		if (admin !=null)
			return true;
		else
			return false;
	}

	public boolean isExistMobileAndId(String mobile, Long id) {
		Admin admin = adminRepository.findByMobileAndIdAndStatus(mobile, id, Constant.ONE);
		if (admin != null)
			return true;
		else
			return false;
	}

	
}
