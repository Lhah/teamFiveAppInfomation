package cn.smbms.dao.role;

import java.util.List;

import cn.smbms.pojo.Role;

public interface RoleMapper {
	/**
	 * 查询用户角色信息表信息
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public List<Role> getRoleList()throws Exception;
}
