package cn.smbms.service.bill;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smbms.dao.bill.BillMapper;
import cn.smbms.pojo.Bill;

@Service("billBiz")
public class BillBizImpl implements BillBiz {

	@Autowired
	private BillMapper billmapper;
	
	@Override
	public List<Bill> getBillAll(Bill bill) throws Exception {
		return billmapper.getBillList(bill);
	}

	@Override
	public int addBill(Bill bill) throws Exception {
		return billmapper.addBill(bill);
	}

	@Override
	public Bill getBillById(String id) throws Exception {
		return billmapper.getBillById(id);
	}

	@Override
	public int modify(Bill bill) throws Exception {
		return billmapper.modify(bill);
	}

	@Override
	public int deleteBillById(String delId) throws Exception {
		return billmapper.deleteBillById(delId);
	}

}
