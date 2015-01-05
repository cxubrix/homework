package fm.ask.kplavins.service.impl;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import fm.ask.kplavins.data.GeoIpInfo;
import fm.ask.kplavins.service.GeoIpService;

@Service
public class GeoIpServiceImpl implements GeoIpService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InetAddressValidator ipValidator;

    /**
     * @see GeoIpService#getGeoIpInfo(String)
     */
    @Override
    public GeoIpInfo getGeoIpInfo(String ip) {
        if (!ipValidator.isValid(ip)) {
            return null;
        }
        GeoIpInfo geoIpInfo = null;
        try {
            geoIpInfo = restTemplate.getForObject(SERVICE_URL + ip, GeoIpInfo.class);
        } catch (RestClientException e) {
            geoIpInfo = new GeoIpInfo();
            geoIpInfo.setIp(ip);
            geoIpInfo.setCountry_code(FALLBACK_COUNTRY_CODE);
        }

        return geoIpInfo;
    }

}
