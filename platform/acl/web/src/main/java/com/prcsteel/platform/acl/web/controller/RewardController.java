package com.prcsteel.platform.acl.web.controller;

import com.prcsteel.platform.acl.model.enums.RewardType;
import com.prcsteel.platform.acl.model.model.Reward;
import com.prcsteel.platform.acl.service.RewardService;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.service.CategoryGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by chenchen on 15-7-31.
 */
@Controller
@RequestMapping("/reward/")
public class RewardController extends BaseController {

    @Autowired
    RewardService rewardService;
    @Autowired
    CategoryGroupService categoryGroupService;

    @RequestMapping("index.html")
    public void index(ModelMap out) {
        out.put("message", "用户管理");
    }

    @RequestMapping("showreward.html")
    public String setreward(ModelMap out) {
        out.put("reward", rewardService.getAllRewardDto());
        out.put("reward_rate", rewardService.getAllReward().stream().collect(Collectors.groupingBy(Reward::getCategoryUuid, Collectors.mapping(Reward::getRewardRole, Collectors.toList()))));
        out.put("reward_init", categoryGroupService.selectNoSelectForReward());
        List<Reward> rewards=rewardService.getAllRadio();
        for(int i=0;i<rewards.size();i++){
            if(rewards.get(i).getRewardRole()!=null){
                rewards.get(i).setRewardRole(new BigDecimal(Math.floor(rewards.get(i).getRewardRole().doubleValue())));
                rewards.set(i, rewards.get(i));}
        }
        out.put("reward_radio", rewards);
        List<Reward>rewardComm=rewardService.getCommissionStandard();
          Reward  rewardCommissionStandard=null;
            for(int i = 0 ; i< rewardComm.size();i++){
                rewardCommissionStandard=rewardComm.get(rewardComm.size()-1);
            }
            out.put("commissionStandard", rewardCommissionStandard);
        out.put("tab", "reward");
        return "sys/reward";
    }

    @RequestMapping("saveReward.html")
    @ResponseBody
    public Result saveReward(ModelMap out, HttpServletRequest request,
                             @RequestParam("cat_uuid") String[] cat_uuid,
                             @RequestParam("rewardRole") String[] rewardRole,
                             @RequestParam("sellerLimit") String[] sellerLimit,
                             @RequestParam("minLimit") String[] minLimit,
                             @RequestParam("maxLimit") String[] maxLimit,
                             @RequestParam("buyRadio") String[] buyRadio) {
        Result result = new Result();
        List<Reward> rewardList = new ArrayList<Reward>();
        Reward temp;
        /*
         * 循环封装reward对象
         * 买家累积采购量区间
		 */
        for (int i = 0; i < minLimit.length; i++) {
            temp = new Reward();
            temp.setCreatedBy(this.getLoginUser().getName());
            temp.setLastUpdatedBy(this.getLoginUser().getName());
            temp.setSellerLimit(new BigDecimal(minLimit[i]));
            if(maxLimit[i]==null||maxLimit[i].equals("")){
                temp.setRewardRole(null);
            }else {
                temp.setRewardRole(new BigDecimal(maxLimit[i]));
            }
            temp.setBuyRadio(new BigDecimal(buyRadio[i]));
            temp.setSeq(i+1);
            temp.setRewardType(RewardType.RADIO.getCode());
            rewardList.add(temp);
        }
        for (int i = 0; i < cat_uuid.length; i++) {
            temp = new Reward();
            temp.setCategoryUuid(cat_uuid[i]);
            temp.setCreatedBy(this.getLoginUser().getName());
            temp.setLastUpdatedBy(this.getLoginUser().getName());
            temp.setSellerLimit(new BigDecimal(sellerLimit[i]));
            temp.setRewardRole(new BigDecimal(rewardRole[i]));
            temp.setRewardType(RewardType.CATEGORY.getCode());
            rewardList.add(temp);
        }
        Map<String, String> reward_rate = new HashMap<>();
        reward_rate.put("buyer_temp_local_reward", request.getParameter("buyer_temp_local_reward"));
        reward_rate.put("buyer_consign_local_reward", request.getParameter("buyer_consign_local_reward"));
        reward_rate.put("buyer_temp_range_reward", request.getParameter("buyer_temp_range_reward"));
        reward_rate.put("buyer_consign_range_reward", request.getParameter("buyer_consign_range_reward"));
        reward_rate.put("seller_temp_local_reward", request.getParameter("seller_temp_local_reward"));
        reward_rate.put("seller_consign_local_reward", request.getParameter("seller_consign_local_reward"));
        reward_rate.put("seller_temp_range_reward", request.getParameter("seller_temp_range_reward"));
        reward_rate.put("seller_consign_range_reward", request.getParameter("seller_consign_range_reward"));
        reward_rate.put("new_buyer_reward", request.getParameter("new_buyer_reward"));
        reward_rate.put("new_seller_reward", request.getParameter("new_seller_reward"));
        Reward rewardComm= new Reward();
        rewardComm.setBuyRadio(new BigDecimal(request.getParameter("buy_radio"))); //交易员差价利润百分比设置
        rewardComm.setRewardRole(BigDecimal.valueOf(Double.valueOf(request.getParameter("commissionStandard"))));
        rewardComm.setCategoryUuid(request.getParameter("category_UUID"));
        try {
            rewardService.refleshReward(rewardList, reward_rate,rewardComm,getLoginUser().getName());
            result.setSuccess(true);
        } catch (BusinessException e){
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }

    @RequestMapping("savaReward1.html")
    public
    @ResponseBody
    Result savaReward1() {
        Result result = new Result();
        result.setData(rewardService.getAllRewardDto());
        return result;
    }

}
