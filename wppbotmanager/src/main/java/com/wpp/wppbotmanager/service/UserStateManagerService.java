package com.wpp.wppbotmanager.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class UserStateManagerService {
    public static final String MENU_PRINCIPAL = "MENU_PRINCIPAL";
    private final Cache userStateCache;

    public UserStateManagerService(CacheManager cacheManager) {
        this.userStateCache = cacheManager.getCache("userStateCache");
    }

    public void setState(String numUser, String state) {
        userStateCache.put(numUser, state);
    }

    public String getState(String numUser) {
        String state = userStateCache.get(numUser, String.class);
        if(state == null) {
            return MENU_PRINCIPAL;
        }
        return state;
    }
}
