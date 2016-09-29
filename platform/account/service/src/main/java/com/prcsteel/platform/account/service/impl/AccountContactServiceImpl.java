package com.prcsteel.platform.account.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prcsteel.platform.account.model.dto.AccountContactDto;
import com.prcsteel.platform.account.model.dto.AccountContactOrgUserDto;
import com.prcsteel.platform.account.model.dto.UpdateAssignPotentialcustomer;
import com.prcsteel.platform.account.model.enums.AccountStatus;
import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.persist.dao.AccountContactDao;
import com.prcsteel.platform.account.service.AccountAssignService;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.StringUtil;


/**
 * Created by kongbinheng on 2015/7/14.
 */
@Transactional
@Service("accountContactService")
public class AccountContactServiceImpl implements AccountContactService {
	
    private static final String FAILCODE = "201";
    @Autowired
    private AccountContactDao accountContactDao;
    @Resource
    private AccountService accountService;
    
    @Resource
    private AccountAssignService accountAssignService;
    
    @Value("${wechat.server.domain}")
    private String wechatServerDomain;  // 接口服务地址
    @Override
    public List<AccountContactDto> queryByAccountId(Map<String, Object> paramMap) {
        return accountContactDao.queryByAccountId(paramMap);
    }

    @Override
    public int totalContact(Map<String, Object> paramMap) {
        return accountContactDao.totalContact(paramMap);
    }

    @Override
    public boolean add(AccountContact accountContact) {
        //添加成功
        if (accountContactDao.insert(accountContact) > 0) {
            //请求参数封装
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("accountId", accountContact.getAccountId());
            paramMap.put("type", accountContact.getType());
            paramMap.put("isMain", 1);  //主联系人

            //买家主联系人数
            int totalIsMain = accountContactDao.totalIsMain(paramMap);
            paramMap.put("id", accountContact.getId());
            paramMap.put("lastUpdated", new Date());
            paramMap.put("lastUpdatedBy", String.valueOf(SecurityUtils.getSubject().getPrincipal()));
            if (totalIsMain > 1) {
                if (accountContactDao.updateIsMainByIdArr(paramMap) > 0)
                    return true;
                else
                    return false;
            }
            return true;
        } else {  //添加失败
            return false;
        }
    }

    @Override
    public boolean edit(AccountContact accountContact, String type) {
        if ("buyer".equals(type)) {

            //请求参数封装
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("accountId", accountContact.getAccountId());
            paramMap.put("type", type);
            paramMap.put("isMain", 1);  //主联系人

            //编辑成功
            if (accountContactDao.updateAccountContactById(accountContact) > 0) {
                //买家主联系人数
                int totalIsMain = accountContactDao.totalIsMain(paramMap);
                paramMap.put("id", accountContact.getId());
                paramMap.put("lastUpdated", new Date());
                paramMap.put("lastUpdatedBy", String.valueOf(SecurityUtils.getSubject().getPrincipal()));
                if (totalIsMain > 1) {
                    if (accountContactDao.updateIsMainByIdArr(paramMap) > 0)
                        return true;
                    else
                        return false;
                }
                return true;
            } else {  //编辑失败
                return false;
            }
        } else {
            //编辑成功
            if (accountContactDao.updateAccountContactById(accountContact) > 0)
                return true;
            else  //编辑失败
                return false;
        }
    }

    @Override
    public boolean disabledById(AccountContact accountContact) {
        //锁定成功
        if (accountContactDao.updateStatusById(accountContact) > 0)
            return true;
        else  //锁定失败
            return false;
    }

    /**
     * 根据部门Id，联系人Id，交易员Id锁定/解锁联系人信息
     * @param accountContact
     * @return
     */
    @Override
    public int updateStatusByIds(AccountContact accountContact){
        return accountContactDao.updateStatusByIds(accountContact);
    }

    @Override
    public AccountContact queryById(String id) {
        return accountContactDao.queryById(id);
    }

    @Override
    public int totalIsMain(Map<String, Object> paramMap) {
        return accountContactDao.totalIsMain(paramMap);
    }

    @Override
    public boolean updateIsMainById(Map<String, Object> paramMap) {
        if (accountContactDao.updateIsMainById(paramMap) > 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean updateIsMainByIdArr(Map<String, Object> paramMap) {
        if (accountContactDao.updateIsMainByIdArr(paramMap) > 0)
            return true;
        else
            return false;
    }

    @Override
    public AccountContactDto queryMainByAccountId(Long accountId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("accountId", accountId);
        paramMap.put("start", 0);
        paramMap.put("length", 50);
        paramMap.put("status", 1);
        List<AccountContactDto> list = accountContactDao.queryByAccountId(paramMap);
        if (list.size() > 0) {
            for (AccountContactDto item : list) {
                if (item.getIsMain() == 1) {
                    return item;
                }
            }
        }
        return null;
    }

    @Override
    public List<AccountContactDto> queryByManagerIdAndAccountId(Long managerId, String type, Long accountId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("accountId", accountId);
        paramMap.put("start", 0);
        paramMap.put("length", 50);
        paramMap.put("status", 1);
        paramMap.put("type", type);
        return accountContactDao.queryByAccountId(paramMap);
    }

    @Override
    public int totalTel(Map<String, Object> paramMap) {
        return accountContactDao.totalTel(paramMap);
    }

    /**
     * 根据tel获取联系人信息
     *
     * @param tel 手机号码
     * @return 联系人对象
     */
    @Override
    public AccountContact queryByTel(String tel) {
        return accountContactDao.queryByTel(tel);
    }

    @Override
    public Long updateAccount(Account account, User user) {
        Account accountModel = accountService.selectAccountByName(account.getName().trim());
        if (accountModel != null) {
            if (accountService.updateType(accountModel.getId(),
                    AccountType.both.toString()) != 1) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新用户信息失败");
            }
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("accountId", accountModel.getId());
        paramMap.put("isMain", Constant.YES);
        paramMap.put("type", AccountType.seller.toString());
        List<AccountContactDto> list = queryByAccountId(paramMap);
        if (list != null && list.size() > 0) {
            AccountContact contact = list.get(0);
            contact.setType(AccountType.both.toString());
            contact.setLastUpdated(new Date());
            contact.setLastUpdatedBy(user.getLoginId());
            contact.setModificationNumber(contact.getModificationNumber() + 1);
            if (accountContactDao.updateAccountContactById(contact) != 1) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新联系人信息失败");
            }
        }
        return accountModel.getId();
    }
    /**
     * 获取指定客户的所以联系人信息
     * @param accountId 客户ID
     * @return
     */
    @Override
    public List<AccountContact> queryContactListByAccountId(Long accountId){
        return accountContactDao.queryContactListByAccountId(accountId);
    }
    /**
     * 获取微信端相关的联系人信息
     * @param openId 微信端ID
     * @return
     */
    @Override
    public AccountContactOrgUserDto queryAccountContactByOpenId(String openId){
        try{
           return accountContactDao.queryAccountContactByOpenId(openId);
        }catch(Exception e){
            throw new BusinessException(FAILCODE, e.getMessage());
        }
    }

    /**
     * 判断联系人关系是否被锁定
     * @param departmentId 部门ID
     * @param contact      联系人
     * @param managerId    交易员Id
     */
    @Override
    public void isLockForAccountContact(Contact contact,Long departmentId, Long managerId, String accountName, String departmentName) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("accountId", departmentId);
        param.put("contactId", contact.getId());
        param.put("manager", managerId);
        List<AccountContact> list= accountContactDao.queryByIds(param);
        list.forEach(item -> {
            if (item != null && AccountStatus.LOCKED.getCode().equals(item.getStatus())) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, accountName + " " + departmentName + " " + contact.getName() + " 已被锁定！");
            }
        });
    }

    /**
     * 根据accountId，contactId，managerId获取客户的联系人信息
     * @return
     */
    @Override
    public AccountContact queryByIds(Long accountId,Integer contactId,Long manager){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("accountId", accountId);
        param.put("contactId", contactId);
        param.put("manager", manager);
        List<AccountContact> list= accountContactDao.queryByIds(param);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
	
	 /**
	 * @Description: CBMS新增客户联系人时，通知微信端
	 * @author wangxianjun
	 * @param phone  手机号
	 * @param user  当前用户
	 * @date 2016年5月26日
	 *
	 */
	public void noticeWechat(String phone,User user){
		try {
			HttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());

			HttpGet get = new HttpGet(wechatServerDomain);
			// 获取_xsrf
			HttpResponse httpResponse = httpClient.execute(get);
			String csrf = getCSRFtoken(httpResponse);
	
			Map<String,String> params  = new HashMap<String,String>();
			params.put("phone", phone);
			Long timestamp =  new Date().getTime();
			params.put("timestamp", timestamp.toString());

			String url = wechatServerDomain+"/service/cbms/failed_rebind";
			HttpPost httpPost = new HttpPost(url);
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			//phone  手机号 timestamp  时间戳 signature 签名
			nvps.add(new BasicNameValuePair("phone", phone));
			nvps.add(new BasicNameValuePair("timestamp", timestamp.toString()));
			String token = StringUtil.getSignature(params, Constant.SECRET);
			nvps.add(new BasicNameValuePair("signature", token));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			httpPost.setHeader("X-CSRF-TOKEN",csrf);
			
			httpResponse = httpClient.execute(httpPost);
			System.out.println("call result:"+EntityUtils.toString(httpResponse.getEntity()));
			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			System.out.println("调用用户信息同步接口出错：：手机号码"+phone);
		}
	} 
	
	public static String getCSRFtoken(HttpResponse httpResponse){
		Header headers[] = httpResponse.getHeaders("Set-Cookie");
		if (headers == null || headers.length == 0) {
			return null;
		}
		String cookie = "";
		String CSRF_TOKEN = "CSRF-TOKEN=";
		for (int i = 0; i < headers.length; i++) {
			cookie = headers[i].getValue();
			 if(cookie.contains(CSRF_TOKEN)){
				 int begin = cookie.indexOf(CSRF_TOKEN);
				 int end = cookie.lastIndexOf("; Path=/");
				
				 String csrf_token = cookie.substring(begin+CSRF_TOKEN.length(),end);
				 return csrf_token;
			 }	
		}
		return "";
	}

    public List<Long> getAccountIdsByContactId(Integer contactId){
        return accountContactDao.getAccountIdsByContactId(contactId);
    }


    /**
     * 如果交易员与公司部门的联系人没有关系，创建关系
     * @param departmentId 部门Id
     * @param contactId 联系人Id
     * @param managerId 交易员Id
     * @param user
     */
    @Override
    public void addAccountContact(Long departmentId, Integer contactId, Long managerId, User user) {
        AccountContact accountContact = queryByIds(departmentId, contactId, managerId);
        if (accountContact == null) {
            accountContact = new AccountContact();
            accountContact.setAccountId(departmentId);
            accountContact.setManager(managerId);
            accountContact.setContactId(contactId);
            accountContact.setCreated(new Date());
            accountContact.setCreatedBy(user.getName());
            accountContact.setLastUpdated(new Date());
            accountContact.setLastUpdatedBy(user.getName());
            accountContact.setModificationNumber(0);
            Integer flag = accountContactDao.insertSelective(accountContact);
            if (flag == 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "添加联系人关系失败");
            }
        }
    }

	@Override
	public List<AccountContact> getContactByAccountId(Long accountId) {
		return accountContactDao.getContactByAccountId(accountId);
	}

	@Override
	public List<AccountContactDto> queryByContactId(Long contactId) {
		return accountContactDao.queryByContactId(contactId);
	}

	@Override
	public void assignPotentialcustomer(UpdateAssignPotentialcustomer dto, User user) {
		
		if(dto.getUpdates()!=null && !dto.getUpdates().isEmpty()){
			for (AccountContact d : dto.getUpdates()) {
				accountContactDao.insertSelective(new AccountContact()
					.setAccountId(d.getAccountId())
					.setContactId(d.getContactId())
					.setModificationNumber(1)
					.setCreated(new Date())
					.setCreatedBy(user.getName())
					.setLastUpdated(new Date())
					.setLastUpdatedBy(user.getName())
					.setIsDeleted(false)
					.setManager(d.getManager()));
			}
		}
		
		if(dto.getRemoves()!=null && !dto.getRemoves().isEmpty()){
			List<Long> ids = dto.getRemoves();
			accountContactDao.deleteByIds(ids);
		}
		
	}

    /**
     * 根据公司ID获取交易员
     * @param companyId
     * @return
     */
    @Override
    public List<User> queryUserByCompanyId(Long companyId){
        return accountContactDao.queryUserByCompanyId(companyId);
    }
}
