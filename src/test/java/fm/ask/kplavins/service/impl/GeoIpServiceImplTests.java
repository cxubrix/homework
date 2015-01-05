package fm.ask.kplavins.service.impl;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import fm.ask.kplavins.data.GeoIpInfo;
import fm.ask.kplavins.service.GeoIpService;
import fm.ask.kplavins.service.impl.GeoIpServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class GeoIpServiceImplTests {

    private static final String NL_IP = "46.19.37.108";
    private static final String TIMEOUT_IP = "46.19.37.109";
    private static final String LOCAL_IP = "10.10.1.1";

    private static final GeoIpInfo NL_GEO_IP_INFO = new GeoIpInfo();
    static {
        NL_GEO_IP_INFO.setCountry_code("NL");
        NL_GEO_IP_INFO.setIp(NL_IP);
    }

    private static final GeoIpInfo LOCAL_GEO_IP_INFO = new GeoIpInfo();
    static {
        LOCAL_GEO_IP_INFO.setCountry_code(null);
        LOCAL_GEO_IP_INFO.setIp(LOCAL_IP);
    }

    @Autowired
    private GeoIpService geoIpService;

    @Autowired
    private RestTemplate restTemplate;

    @Configuration
    static class ContextConfiguration {

        @Bean
        public GeoIpService geoIpService() {
            GeoIpService geoIpService = new GeoIpServiceImpl();
            return geoIpService;
        }

        @Bean
        public RestTemplate restTemplate() {
            RestTemplate restTemplate = mock(RestTemplate.class);
            when(restTemplate.getForObject(eq(GeoIpService.SERVICE_URL + NL_IP), eq(GeoIpInfo.class))).thenReturn(NL_GEO_IP_INFO);
            when(restTemplate.getForObject(eq(GeoIpService.SERVICE_URL + LOCAL_IP), eq(GeoIpInfo.class))).thenReturn(LOCAL_GEO_IP_INFO);
            when(restTemplate.getForObject(eq(GeoIpService.SERVICE_URL + TIMEOUT_IP), eq(GeoIpInfo.class))).thenThrow(
                    new RestClientException("Timeout"));
            return restTemplate;
        }

        @Bean
        public InetAddressValidator inetAddressValidator() {
            return InetAddressValidator.getInstance();
        }

    }

    @Test
    public void testPredefinedIp() {
        GeoIpInfo info = geoIpService.getGeoIpInfo(NL_IP);
        Assert.assertNotNull(info);
        Assert.assertEquals("NL", info.getCountry_code());
        Assert.assertEquals(NL_IP, info.getIp());
    }

    @Test
    public void testLocalIp() {
        GeoIpInfo info = geoIpService.getGeoIpInfo(LOCAL_IP);
        Assert.assertNotNull(info);
        Assert.assertNull(info.getCountry_code());
        Assert.assertEquals(LOCAL_IP, info.getIp());
    }

    @Test
    public void testInvalidIp() {
        GeoIpInfo info = geoIpService.getGeoIpInfo("NotAValidIp");
        Assert.assertNull(info);
    }

    @Test
    public void testTimeout() {
        GeoIpInfo info = geoIpService.getGeoIpInfo(TIMEOUT_IP);
        Assert.assertNotNull(info);
        Assert.assertEquals("LV", info.getCountry_code());
        Assert.assertEquals(TIMEOUT_IP, info.getIp());
    }

    @Test
    public void testNull() {
        GeoIpInfo info = geoIpService.getGeoIpInfo(null);
        Assert.assertNull(info);
    }

}
