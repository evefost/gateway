package com.xhg.gateway.bo;

public class VRequestBody {

    /**
     * requestBody : {"data":{"latitude":"22.554386","longitude":"113.950383"}}
     * requestHead : {"appId":"com.ps.recycling2c","appVersion":"1.1.10","channel":"ps-recycle","configVersion":"1.1.7","deviceId":"00000000-0550-3ea8-0000-00000033c587","ostype":"ANDROID","phoneModel":"LDN-AL00","phoneResolution":"720*1360","systemVersion":"8.0.0","token":"cd77fcd39178380ae4f63ae8da3f2077425e14f3dcbe20250fe4ac63710594eb136fbff742d24301f7be7bdc44b95282","validdateTime":"1527673968830"}
     */

    private RequestBodyBean requestBody;


    private RequestHeadBean requestHead;

    public RequestBodyBean getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBodyBean requestBody) {
        this.requestBody = requestBody;
    }

    public RequestHeadBean getRequestHead() {
        return requestHead;
    }

    public void setRequestHead(RequestHeadBean requestHead) {
        this.requestHead = requestHead;
    }

    public static class RequestBodyBean {

        /**
         * data : {"latitude":"22.554386","longitude":"113.950383"}
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * latitude : 22.554386
             * longitude : 113.950383
             */

            private String latitude;
            private String longitude;

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }
        }
    }

    public static class RequestHeadBean {
        /**
         * appId : com.ps.recycling2c
         * appVersion : 1.1.10
         * channel : ps-recycle
         * configVersion : 1.1.7
         * deviceId : 00000000-0550-3ea8-0000-00000033c587
         * ostype : ANDROID
         * phoneModel : LDN-AL00
         * phoneResolution : 720*1360
         * systemVersion : 8.0.0
         * token : cd77fcd39178380ae4f63ae8da3f2077425e14f3dcbe20250fe4ac63710594eb136fbff742d24301f7be7bdc44b95282
         * validdateTime : 1527673968830
         */

        private String appId;
        private String appVersion;
        private String channel;
        private String configVersion;
        private String deviceId;
        private String ostype;
        private String phoneModel;
        private String phoneResolution;
        private String systemVersion;
        private String token;
        private String validdateTime;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getConfigVersion() {
            return configVersion;
        }

        public void setConfigVersion(String configVersion) {
            this.configVersion = configVersion;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getOstype() {
            return ostype;
        }

        public void setOstype(String ostype) {
            this.ostype = ostype;
        }

        public String getPhoneModel() {
            return phoneModel;
        }

        public void setPhoneModel(String phoneModel) {
            this.phoneModel = phoneModel;
        }

        public String getPhoneResolution() {
            return phoneResolution;
        }

        public void setPhoneResolution(String phoneResolution) {
            this.phoneResolution = phoneResolution;
        }

        public String getSystemVersion() {
            return systemVersion;
        }

        public void setSystemVersion(String systemVersion) {
            this.systemVersion = systemVersion;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getValiddateTime() {
            return validdateTime;
        }

        public void setValiddateTime(String validdateTime) {
            this.validdateTime = validdateTime;
        }
    }
}
