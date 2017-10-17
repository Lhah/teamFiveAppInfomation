package cn.smbms.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillBiz;
import cn.smbms.service.provider.ProviderBiz;
import cn.smbms.tools.Constants;

@Controller
@RequestMapping("/Bill")
public class BillController {
	
	@Autowired
	private BillBiz Bbiz;
	@Autowired
	private ProviderBiz pbiz;
	
	//查询全部信息顺带模糊查询
	@RequestMapping("/Billlist.html")
	public String list(HttpSession session,
			@RequestParam(value="queryProductName",required=false) String queryProductName,
			@RequestParam(value="queryProviderId",required=false) Integer queryProviderId,
			@RequestParam(value="queryIsPayment",required=false) Integer queryIsPayment){
		Bill bill = new Bill();
		try {
			if(queryProductName!=null && queryProductName!=""){
				bill.setProductName(queryProductName);
			}
			if(queryProviderId!=null){
				bill.setProviderId(queryProviderId);
			}
			if(queryIsPayment!=null){
				bill.setIsPayment(queryIsPayment);
			}
			//调用方法获取供应商列表
			List<Provider> list = pbiz.getProviderList(null, null);
			session.setAttribute("Prolist",list);
			//订单表
			List<Bill> BillAll = Bbiz.getBillAll(bill);
			session.setAttribute("BillAll", BillAll);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "user/billlist";
	}

	//增加商品清单信息
	@RequestMapping("/Billadd.html")
	public String add(){
		return "user/billadd";
	}
	
	//获取供应商的列表
	@RequestMapping("/providerShow.html")
	public void providerShow(HttpServletResponse response){
		try {
			 List<Provider> list = pbiz.getProviderList(null, null);
			//把list对象转成json对象输出
			response.setContentType("application/json");
			PrintWriter outPrintWriter = null;
			outPrintWriter = response.getWriter();
			outPrintWriter.write(JSONArray.toJSONString(list));
			outPrintWriter.flush();
			outPrintWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//增加商品信息的方法
	@RequestMapping("/BillAddInfo.html")
	public String BillAdd(HttpServletRequest request, HttpServletResponse response,
			HttpSession session){
		String billCode = request.getParameter("billCode");//订单编码
		String productName = request.getParameter("productName");//产品名称
		String productUnit = request.getParameter("productUnit");//产品单位
		String productCount = request.getParameter("productCount");//产品数量
		String totalPrice = request.getParameter("totalPrice");//总金额
		String providerId = request.getParameter("providerId");//供应商
		String isPayment = request.getParameter("isPayment");//是否付款
		Bill bill = new Bill();
		bill.setBillCode(billCode);
		bill.setProductName(productName);
		bill.setProductUnit(productUnit);
		bill.setProductCount(Double.valueOf(productCount));
		bill.setTotalPrice(Double.valueOf(totalPrice));
		bill.setProviderId(Integer.valueOf(providerId));
		bill.setIsPayment(Integer.valueOf(isPayment));
		bill.setModifyDate(new Date());
		try {
			if(Bbiz.addBill(bill)>0){
				return "redirect:/Bill/Billlist.html";
			}
			else{
				return "user/billadd";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//修改商品信息
	@RequestMapping("/Billmodify.html")
	public String modify(HttpServletRequest request){
		String billid = request.getParameter("billid");
		try {
			Bill bill = Bbiz.getBillById(billid);
			request.setAttribute("bill", bill);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "user/billmodify";
	}
	
	//修改商品信息的方法
	@RequestMapping("/BillmodifyInfo.html")
	public String modifyInfo(HttpServletRequest request){
		String id = request.getParameter("id");
		String productName = request.getParameter("productName");
		String productDesc = request.getParameter("productDesc");
		String productUnit = request.getParameter("productUnit");
		String productCount = request.getParameter("productCount");
		String totalPrice = request.getParameter("totalPrice");
		String providerId = request.getParameter("providerId");
		String isPayment = request.getParameter("isPayment");
		Bill bill = new Bill();
		bill.setId(Integer.valueOf(id));
		bill.setProductName(productName);
		bill.setProductDesc(productDesc);
		bill.setProductUnit(productUnit);
		bill.setProductCount(Double.valueOf(productCount));
		bill.setIsPayment(Integer.parseInt(isPayment));
		bill.setTotalPrice(Double.valueOf(totalPrice));
		bill.setProviderId(Integer.parseInt(providerId));
		bill.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		bill.setModifyDate(new Date());
		try {
			if (Bbiz.modify(bill)>0) {
				return "redirect:/Bill/Billlist.html";
			} else {
				return "user/billmodify";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//显示详细信息
	@RequestMapping("/Billview.html")
	public String view(HttpServletRequest request){
		String billid = request.getParameter("billid");
		try {
			Bill bill = Bbiz.getBillById(billid);
			request.setAttribute("bill", bill);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "user/billview";
	}
	
	//删除单条信息
	@RequestMapping("/Billdelete.html")
	public void BillDelete(HttpServletRequest request,HttpServletResponse response){
		String id=request.getParameter("billid");
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(!StringUtils.isNullOrEmpty(id)){
			int count=0;
			try {
				count = Bbiz.deleteBillById(id);
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
		response.setContentType("application/json");
		PrintWriter outPrintWriter;
		try {
			outPrintWriter = response.getWriter();
			outPrintWriter.write(JSONArray.toJSONString(resultMap));
			outPrintWriter.flush();
			outPrintWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
