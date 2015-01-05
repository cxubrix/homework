package fm.ask.kplavins.service;

import fm.ask.kplavins.data.GeoIpInfo;

/**
 * 
 * Service to lookup {@link GeoIpInfo} by users IP address.
 */
public interface GeoIpService {

    static final String SERVICE_URL = "http://www.telize.com/geoip/";
    static final String FALLBACK_COUNTRY_CODE = "LV";

    /**
     * get {@link GeoIpInfo} by ip address
     * 
     * @param ip
     *            to get the code for
     * @return ip info. Can be null in case of invalid IP, or fallback to
     *         {@link GeoIpInfo#getCountry_code()} default countr code
     *         FALLBACK_COUNTRY_CODE="LV"
     */
    GeoIpInfo getGeoIpInfo(String ip);

}
