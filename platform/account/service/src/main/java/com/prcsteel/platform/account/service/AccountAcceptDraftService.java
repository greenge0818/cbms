package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.model.AccountAcceptDraft;
import com.prcsteel.platform.acl.model.model.User;

public interface AccountAcceptDraftService {

    void save(AccountAcceptDraft accountAcceptDraft, User user);

}
