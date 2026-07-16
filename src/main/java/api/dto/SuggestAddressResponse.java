package api.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SuggestAddressResponse {

    private List<Suggestion> suggestions;

    public List<Suggestion> getSuggestions(){
        return suggestions;
    }
    public void setSuggestions(List<Suggestion> suggestions){
        this.suggestions = suggestions;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Suggestion{
        private String value;
        private Data data;

        public String getValue() {
            return value;
        }

        public Data getData() {
            return data;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setData(Data data) {
            this.data = data;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Data{
            private String city;
            private String country;

            public String getCity() {
                return city;
            }

            public String getCountry() {
                return country;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public void setCountry(String country) {
                this.country = country;
            }
        }
    }

}
