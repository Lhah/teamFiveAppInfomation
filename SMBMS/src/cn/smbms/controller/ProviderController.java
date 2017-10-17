package cn.smbms.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.smbms.pojo.Provider;
import cn.smbms.service.provider.ProviderBiz;

@Controller
@RequestMapping("/Provider")
public class ProviderController {
	
	@Autowired
	private ProviderBiz Pbiz;
	
	//查询全部信息栓带着模糊查询
	@RequestMapping("/Providerlist.html")
	public String list(HttpSession session,
			@RequestParam(value="queryProCode",required=false) String proCode,
			@RequestParam(value="queryProName",required=false) String proName){
		try {
			List<Provider> providerList = Pbiz.getProviderList(proName,proCode);
			session.setAttribute("providerList", providerList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "user/providerlist";
	}
	
	//跳转到新增页面的路径
	@RequestMapping("/Provideradd.html")
	public String add(){
		return "user/provideradd";
	}
	
	@RequestMapping("/Providermodify.html")
	public String modify(){
		return "user/providermodify";
	}
	
	@RequestMapping("/Providerview.html")
	public String view(){
		return "user/providerview";
	}
	
}
