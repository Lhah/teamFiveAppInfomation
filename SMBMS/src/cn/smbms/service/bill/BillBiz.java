package cn.smbms.service.bill;

import java.util.List;

import cn.smbms.pojo.Bill;

public interface BillBiz {
	/**
	 * 无条件查询Billlist要的全部信息-getBillAll
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Bill> getBillAll(Bill bill) throws Exception;
	/**
	 * 增加订单
	 * @param connection
	 * @param bill
	 * @return
	 * @throws Exception
	 */
	public int addBill(Bill bill) throws Exception;
	/**
	 * 通过billId查询详细信息
	 * 
	 * @param connection
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Bill getBillById(String id) throws Exception;
	/**
	 * 修改订单信息
	 * 
	 * @param connection
	 * @param bill
	 * @return
	 * @throws Exception
	 */
	public int modify(Bill bill) throws Exception;
	/**
	 * 通过delId删除Bill
	 * 
	 * @param connection
	 * @param delId
	 * @return
	 * @throws Exception
	 */
	public int deleteBillById(String delId) throws Exception;
}
