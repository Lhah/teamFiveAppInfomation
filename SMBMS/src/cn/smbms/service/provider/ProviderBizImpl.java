package cn.smbms.service.provider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smbms.dao.provider.ProviderMapper;
import cn.smbms.pojo.Provider;

@Service("providerBiz")
public class ProviderBizImpl implements ProviderBiz {

	@Autowired
	private ProviderMapper providermapper;
	
	@Override
	public List<Provider> getProviderList(String proName, String proCode)
			throws Exception {
		return providermapper.getProviderList(proName, proCode);
	}

}
