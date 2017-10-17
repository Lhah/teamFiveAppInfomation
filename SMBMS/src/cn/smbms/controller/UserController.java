package cn.smbms.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleBiz;
import cn.smbms.service.user.UserBiz;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/user")
public class UserController {
	
	private Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserBiz biz;
	@Autowired
	private RoleBiz Ubiz;
	
	//登录
	@RequestMapping("/login.html")
	public String login(Model model,
			@RequestParam(value="userCode",required=false) String userCode,
			@RequestParam(value="userPassword",required=false) String userPassword,
			HttpSession session){
		try {
			if(userCode==null){
				return "login";
			}
			User user = biz.getLoginUser(userCode, userPassword);
			if(null!=user){
				session.setAttribute("userSession", user);
				return "frame";
			}else{
				model.addAttribute("error", "用户名或者密码错误");
				return "login";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//退出
	@RequestMapping("/loginOut")
	public String loginOut(HttpSession session){
		session.removeAttribute(Constants.USER_SESSION);
		return "login";
	}
	
	//跳转到显示全部信息userlist页面
	@RequestMapping("/userList.html")
	public String list(HttpServletRequest request, HttpServletResponse response){
		String queryUserName = request.getParameter("queryname");//用户名
		String temp = request.getParameter("queryUserRole");//用户角色
		String pageIndex = request.getParameter("pageIndex");
		int queryUserRole = 0;
    	int pageSize = Constants.pageSize;//一页分的数量
    	int currentPageNo = 1;
    	//判断得到的值是否为空
    	if(queryUserName == null){
			queryUserName = "";
		}
		if(temp != null && !temp.equals("")){
			queryUserRole = Integer.parseInt(temp);
		}
    	if(pageIndex != null){
    		try{
    			currentPageNo = Integer.valueOf(pageIndex);
    		}catch(Exception e){
				e.printStackTrace();
    		}
    	}	
    	//总数量（表）	
    	int totalCount = 0;
		try {
			totalCount = biz.getUserCount(queryUserName, queryUserRole);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//总页数
    	PageSupport pages=new PageSupport();
    	pages.setCurrentPageNo(currentPageNo);
    	pages.setPageSize(pageSize);
    	pages.setTotalCount(totalCount);
    	int totalPageCount = pages.getTotalPageCount();
    	//控制首页和尾页
    	if(currentPageNo < 1){
    		currentPageNo = 1;
    	}else if(currentPageNo > totalPageCount){
    		currentPageNo = totalPageCount;
    	}
    	try {
			List<User> userList = biz.getUserList(queryUserName,queryUserRole,
					(currentPageNo-1)*pageSize, pageSize);//给分页查询传值
			//这几个都是跳转到userlist页面显示的值
			request.setAttribute("userList", userList);
			request.setAttribute("queryUserName", queryUserName);
			request.setAttribute("queryUserRole", queryUserRole);
			request.setAttribute("totalPageCount", totalPageCount);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("currentPageNo", currentPageNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "user/userlist";
	}

	//用户角色下拉框显示的信息
	@RequestMapping(value="/rolelist")
	@ResponseBody
	public Object rolelist(HttpServletResponse response){
		List<Role> roleList =null;
		try {
			roleList = Ubiz.getRoleList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try {
		 * 
			//把list对象转成json对象输出
			response.setContentType("application/json");
			PrintWriter outPrintWriter = null;
			outPrintWriter = response.getWriter();
			outPrintWriter.write(JSONArray.toJSONString(roleList));
			outPrintWriter.flush();
			outPrintWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return JSONArray.toJSONString(roleList);
	}
	
	//跳转到新增页面
	@RequestMapping("/userAdd.html")
	public String add(){
		return "user/useradd";
	}
	
	//实现新增的方法
	@RequestMapping("/userAddInfo.html")
	public String addInfo(HttpServletRequest request,User user,
			@RequestParam(value="a_idPicPath",required=true) MultipartFile attach){
		/*String userCode = request.getParameter("userCode");//用户编码
		String userName = request.getParameter("userName");//用户名称
		String userPassword = request.getParameter("userPassword");//用户密码
		String gender = request.getParameter("gender");//用户性别
		String birthday = request.getParameter("birthday");//用户出生日期
		String phone = request.getParameter("phone");//用户电话
		String address = request.getParameter("address");//用户居住地址
		String userRole = request.getParameter("userRole");//用户角色
		
		User user = new User();
		
		user.setUserCode(userCode);
		user.setUserName(userName);
		user.setUserPassword(userPassword);
		user.setGender(Integer.valueOf(gender));
		user.setBirthday(birthday);
		user.setPhone(phone);
		user.setAddress(address);
		user.setUserRole(Integer.valueOf(userRole));*/
		
		String idPicPath = null;
		//判断文件是否为空
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext()
					.getRealPath("statics"+File.separator+"uploadfiles");
			logger.debug("uploadFile path ========= >"+path);
			String oldFileName = attach.getOriginalFilename();//原文件名
			logger.debug("uploadFile oldFileName ========== >"+oldFileName);
			String prefix = FilenameUtils.getExtension(oldFileName);//源文件后缀
			logger.debug("uploadFile prefix =========== >"+prefix);
			int filesize = 500000;
			logger.debug("uploadFile size ======== >"+attach.getSize());
			if(attach.getSize() > filesize){//限制上传文件大小不能超过500kb
				request.setAttribute("uploadFileError", " * 上传大小超过500KB");
				return "user/useradd";
			} else if(prefix.equalsIgnoreCase("jpg")
					||prefix.equalsIgnoreCase("png")
					||prefix.equalsIgnoreCase("jpeg")
					||prefix.equalsIgnoreCase("pneg")) {//上传文件格式不对
				String fileName = System.currentTimeMillis()
						+RandomUtils.nextInt(1000000)+"_Person1.jpg";
				logger.debug("new FileName =========="+attach.getName());
				File targetFile = new File(path, fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				//保存
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("uploadFileError", " * 上传失败!");
					return "user/useradd";
				}
				idPicPath = path+File.separator+fileName;
			} else {
				request.setAttribute("uploadFileError", " * 上传图片格式不正确");
				return "user/useradd";
			}
			
		}
		user.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		user.setCreationDate(new Date());
		user.setIdPicPath(idPicPath);
		try {
			if(biz.add(user)>0){
				return "redirect:/user/userList.html";
			}
			else{
				return "user/useradd";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//跳转到显示单挑详细信息页面
	@RequestMapping("/userView.html")
	public String view(HttpServletRequest request){
		String id = request.getParameter("uid");
		try {
			User user = biz.getUserById(id);
			request.setAttribute("user", user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "user/userview";
	}
	
	//跳转到修改页面
	@RequestMapping("/usermodify.html")
	public String modify(HttpServletRequest request){
		String id = request.getParameter("uid");
		try {
			User user = biz.getUserById(id);
			request.setAttribute("user", user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "user/usermodify";
	}
	
	//实现修改的方法
	@RequestMapping("/usermodifyInfo.html")
	public String modifyInfo(HttpServletRequest request,User user){
		user.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		user.setModifyDate(new Date());
		user.setId(Integer.valueOf(request.getParameter("uid")));
		try {
			if(biz.modify(user)>0){
				return "redirect:/user/userList.html";
			} else {
				return "user/usermodify";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//实现删除的方法
	@RequestMapping("/userDelete.html")
	@ResponseBody
	public String delete(HttpServletRequest request,HttpServletResponse response){
		String id=request.getParameter("uid");
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(!StringUtils.isNullOrEmpty(id)){
			int count=0;
			try {
				count = biz.deleteUserById(Integer.valueOf(id));
				if(count>0){//删除成功
					resultMap.put("delResult", "true");
				}else{//删除失败
					resultMap.put("delResult", "false");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			resultMap.put("delResult", "notexit");
		}
		//把resultMap转换成json对象输出
		/*response.setContentType("application/json");
		PrintWriter outPrintWriter;
		try {
			outPrintWriter = response.getWriter();
			outPrintWriter.write(JSONArray.toJSONString(resultMap));
			outPrintWriter.flush();
			outPrintWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return JSONArray.toJSONString(resultMap);
	}

	//跳到修改密码的页面
	@RequestMapping("/pwaModify.html")
	public String updatePwd(){
		return "user/pwdmodify";
	}

	//修改密码的方法
	@RequestMapping("/updatePwdInfo.html")
	public void updatePwdInfo(HttpServletRequest request, HttpServletResponse response){
		Object o = request.getSession().getAttribute(Constants.USER_SESSION);
		String newpassword = request.getParameter("newpassword");
		//boolean flag = false;
		if(o != null && !StringUtils.isNullOrEmpty(newpassword)){
			//UserService userService = new UserServiceImpl();
			//flag = userService.updatePwd(((User)o).getId(),newpassword);
			//flag = biz.updatePwd(((User)o).getId(),newpassword);
			try {
				if(biz.updatePwd(((User)o).getId(),newpassword)>0){
					request.setAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
					request.getSession().removeAttribute(Constants.USER_SESSION);//session注销
				}else{
					request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
		}
		//request.getRequestDispatcher("pwdmodify.jsp").forward(request, response);
		
	}
	
}
