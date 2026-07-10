package api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {
    private String postal_code;
    private String country;
    private String city_with_type;
    private String city;

    public UserData(){

    }

    public UserData(String postal_code, String country,String city_with_type, String city) {
        this.postal_code = postal_code;
        this.country = country;
        this.city_with_type = city_with_type;
        this.city = city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getCountry() {
        return country;
    }

    public String getCity_with_type() {
        return city_with_type;
    }

    public String getCity() {
        return city;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity_with_type(String city_with_type) {
        this.city_with_type = city_with_type;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
