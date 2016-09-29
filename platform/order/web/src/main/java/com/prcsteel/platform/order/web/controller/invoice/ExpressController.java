package com.prcsteel.platform.order.web.controller.invoice;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.service.OrganizationService;
import com.prcsteel.platform.common.enums.OpType;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.model.Express;
import com.prcsteel.platform.order.service.invoice.ExpressService;
import com.prcsteel.platform.order.service.invoice.InvoiceOutService;
import com.prcsteel.platform.order.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by rolyer on 15-8-5.
 */
@Controller
@RequestMapping("/invoice/express/")
public class ExpressController extends BaseController {
    private static final Logger logger = Logger.getLogger(ExpressController.class);

    @Autowired
    private ExpressService expressService;

    @Autowired
    private InvoiceOutService invoiceOutService;

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping("index.html")
    public void index(ModelMap out, String orgId, String buyerName, String from, String to){
        Long orgid = null;
        if (StringUtils.isNumeric(orgId)) {
            orgid = Long.parseLong(orgId);
        }

        if (StringUtils.isBlank(buyerName)) {
            buyerName = null;
        } else {
            buyerName = Tools.encodeStr(buyerName);
        }

        if (StringUtils.isBlank(from)) {
            from = null;
        }

        if (StringUtils.isBlank(to)) {
            to = null;
        }

        List<Express> list = invoiceOutService.query(orgid, buyerName, from, to);

        out.put("list", list);
        out.put("orgId", orgid);
        out.put("buyerName", buyerName);
    }

    @RequestMapping(value="add.html", method= RequestMethod.POST)
    @OpLog(OpType.AddCourier)
    @OpParam("ids")
    @OpParam("company")
    @OpParam("expressName")
    public @ResponseBody
    Result add(@RequestParam("p[]") List<Long> ids, String company, String expressName) {
        Result result = new Result();

        Express express = new Express();

        express.setType("OUT");
        express.setCompany(company);
        express.setExpressName(expressName);

        User u = getLoginUser();
        express.setCreatedBy(u.getLoginId());

        int effect = expressService.insertSelective(express);
        if(effect >0 && express.getId() != null) {
            result.setSuccess(true);
            for (Long id : ids) {
                int i = invoiceOutService.updateExpressIdById(id, express.getId());
                if(i==0) {
                    result.setSuccess(false);
                }
            }
        }

        return result;
    }

    /**
     * 初始化页面上服务中心的下拉框
     *
     * @return
     */
    @RequestMapping(value = "/initOrganization.html", method = RequestMethod.POST)
    @ResponseBody
    public Result initOrganization() {
        Result result = new Result();
        List<Organization> organizationList = organizationService.getAllOrganization();
        if (organizationList.size() > 0) {
            result.setData(organizationList);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

}
