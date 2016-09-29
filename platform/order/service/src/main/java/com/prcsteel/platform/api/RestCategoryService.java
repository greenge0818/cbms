package com.prcsteel.platform.api;

import com.prcsteel.platform.core.model.model.CategoryAlias;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by lichaowei on 2016/4/25.
 */
@RestApi(value="restCategoryService", restServer="coreRestServer")
public interface RestCategoryService {
    /**
     * 根据用户id和角色id查询用户权限列表
     */
    @RestMapping(value = "category/querycategory/{aliasName}.html", method = RequestMethod.GET)
    List<CategoryAlias> queryCategoryByAliasName(@UrlParam("aliasName") String aliasName);
}
