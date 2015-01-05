package fm.ask.kplavins.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoIpInfo {

    private String country_code;
    private String ip;

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
