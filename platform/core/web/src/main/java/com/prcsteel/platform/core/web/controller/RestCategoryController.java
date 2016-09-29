package com.prcsteel.platform.core.web.controller;

import com.prcsteel.platform.core.model.model.CategoryAlias;
import com.prcsteel.platform.core.service.CategoryService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lichaowei
 * @version v2.0_account
 * @Description: 大类品名api
 * @date 2016/4/25
 */
@RestController
@RequestMapping("/api/category/")
public class RestCategoryController {

    @Resource
    CategoryService categoryService;

    /**
     * 进项票品名映射设置：根据别名模糊查找所有的品类名
     * @return
     */
    @RequestMapping(value = "querycategory/{aliasName}.html", method = RequestMethod.GET)
    public List<CategoryAlias> queryCategoryByAliasName(@PathVariable String aliasName) {
        return categoryService.queryCategoryAliasLikeAlias(aliasName);
    }
}
