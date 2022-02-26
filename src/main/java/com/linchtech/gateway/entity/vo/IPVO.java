package com.linchtech.gateway.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: 107
 * @date: 2018/10/11 13:39
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IPVO {


    /**
     * code : 200000
     * data : {"ip":"136.25.36.33","ip_infos":[{"isp":"webpass.net","region":"美国,加利福尼亚州,旧金山","source":"IPIP"},{"isp
     * ":"","region":"美国,Webpass","source":"ChunZhen"},{"isp":"","region":"加利福尼亚州,旧金山","source":"NTES"},{"isp":"",
     * "region":"美国,加利福尼亚州,旧金山","source":"MaxMind"},{"isp":"","region":"美国,加利福尼亚,旧金山","source":"TaoBao"},{"isp":"",
     * "region":"美国加利福尼亚旧金山","source":"BaiDu"}],"rDNS":"33.36.25.136.in-addr.arpa"}
     * msg : success
     */

    private int code;
    private DataBean data;
    private String msg;

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class DataBean {
        /**
         * ip : 136.25.36.33
         * ip_infos : [{"isp":"webpass.net","region":"美国,加利福尼亚州,旧金山","source":"IPIP"},{"isp":"","region":"美国,
         * Webpass","source":"ChunZhen"},{"isp":"","region":"加利福尼亚州,旧金山","source":"NTES"},{"isp":"","region":"美国,
         * 加利福尼亚州,旧金山","source":"MaxMind"},{"isp":"","region":"美国,加利福尼亚,旧金山","source":"TaoBao"},{"isp":"",
         * "region":"美国加利福尼亚旧金山","source":"BaiDu"}]
         * rDNS : 33.36.25.136.in-addr.arpa
         */

        private String ip;
        private String rDNS;
        private List<IpInfosBean> ip_infos;

        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class IpInfosBean {
            /**
             * isp : webpass.net
             * region : 美国,加利福尼亚州,旧金山
             * source : IPIP
             */

            private String isp;
            private String region;
            private String source;
        }
    }
}
