package cn.smbms.service.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smbms.dao.role.RoleMapper;
import cn.smbms.pojo.Role;

@Service("roleBiz")
public class RoleBizImpl implements RoleBiz {

	@Autowired
	private RoleMapper rolemapper;
	
	@Override
	public List<Role> getRoleList() throws Exception {
		return rolemapper.getRoleList();
	}

}
