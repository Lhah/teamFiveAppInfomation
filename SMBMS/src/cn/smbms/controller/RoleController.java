package cn.smbms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.smbms.service.role.RoleBiz;

@Controller
@RequestMapping("/Role")
public class RoleController {
	@Autowired
	private RoleBiz Rbiz;
	
}
