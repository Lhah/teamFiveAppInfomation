package cn.smbms.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smbms.dao.user.UserMapper;
import cn.smbms.pojo.User;
import cn.smbms.service.user.UserBiz;

@Service("userBiz")
public class UserBizImpl implements UserBiz {
	
	@Autowired
	private UserMapper usermapper;
	
	//登陆方法
	//实现类中可以不用注解
	@Override
	public User getLoginUser(String userCode, String userPassword)
			throws Exception {
		return usermapper.getLoginUser(userCode, userPassword);
	}

	@Override
	public List<User> getUserList(String userName, int userRole,
			int currentPageNo, int pageSize) throws Exception {
		return usermapper.getUserList(userName, userRole, currentPageNo, pageSize);
	}

	@Override
	public int getUserCount(String userName, int userRole) throws Exception {
		return usermapper.getUserCount(userName, userRole);
	}

	@Override
	public int add(User user) throws Exception {
		return usermapper.add(user);
	}

	@Override
	public User getUserById(String id) throws Exception {
		return usermapper.getUserById(id);
	}

	@Override
	public int modify(User user) throws Exception {
		return usermapper.modify(user);
	}

	@Override
	public int deleteUserById(Integer delId) throws Exception {
		return usermapper.deleteUserById(delId);
	}

	@Override
	public int updatePwd(int id, String pwd) throws Exception {
		return usermapper.updatePwd(id, pwd);
	}

}
