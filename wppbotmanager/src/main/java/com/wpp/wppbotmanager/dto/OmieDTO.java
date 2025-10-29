package com.wpp.wppbotmanager.dto;

public class OmieDTO {

    public static class OmieApiRequest {
        private String appKey;
        private String appSecret;


        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }

        public OmieApiRequest(String appKey, String appSecret) {
            this.appKey = appKey;
            this.appSecret = appSecret;
        }
    }
}
