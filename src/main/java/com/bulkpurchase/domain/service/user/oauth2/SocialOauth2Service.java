package com.bulkpurchase.domain.service.user.oauth2;

import java.util.Map;

public interface SocialOauth2Service {

    void login(Map<String, Object> attributes);
}
