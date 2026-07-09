package api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DADATAStats {

    @JsonProperty("date")
    private String date;
    @JsonProperty("services")
    private Services services;
    @JsonProperty("remaining")
    private Remaining remaining;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Services{
        @JsonProperty("brand")
        private Integer brand;
        @JsonProperty("clean")
        private Integer clean;
        @JsonProperty("company")
        private Integer company;
        @JsonProperty("docs")
        private Integer docs;
        @JsonProperty("merging")
        private Integer merging;
        @JsonProperty("suggestions")
        private Integer suggestions;

        public Services(){

        }

        public Integer getSuggestions() {
            return suggestions;
        }

        public Integer getMerging() {
            return merging;
        }

        public Integer getDocs() {
            return docs;
        }

        public Integer getCompany() {
            return company;
        }

        public Integer getClean() {
            return clean;
        }

        public Integer getBrand() {
            return brand;
        }

        public void setBrand(Integer brand) {
            this.brand = brand;
        }

        public void setClean(Integer clean) {
            this.clean = clean;
        }

        public void setCompany(Integer company) {
            this.company = company;
        }

        public void setDocs(Integer docs) {
            this.docs = docs;
        }

        public void setMerging(Integer merging) {
            this.merging = merging;
        }

        public void setSuggestions(Integer suggestions) {
            this.suggestions = suggestions;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Remaining{
        @JsonProperty("brand")
        private Integer brand;
        @JsonProperty("clean")
        private Integer clean;
        @JsonProperty("company")
        private Integer company;
        @JsonProperty("docs")
        private Integer docs;
        @JsonProperty("merging")
        private Integer merging;
        @JsonProperty("suggestions")
        private Integer suggestions;

        public Remaining(){

        }

        public Integer getBrand() {
            return brand;
        }

        public Integer getClean() {
            return clean;
        }

        public Integer getCompany() {
            return company;
        }

        public Integer getDocs() {
            return docs;
        }

        public Integer getMerging() {
            return merging;
        }

        public Integer getSuggestions() {
            return suggestions;
        }

        public void setBrand(Integer brand) {
            this.brand = brand;
        }

        public void setClean(Integer clean) {
            this.clean = clean;
        }

        public void setCompany(Integer company) {
            this.company = company;
        }

        public void setDocs(Integer docs) {
            this.docs = docs;
        }

        public void setMerging(Integer merging) {
            this.merging = merging;
        }

        public void setSuggestions(Integer suggestions) {
            this.suggestions = suggestions;
        }
    }

    public String getDate() {
        return date;
    }

    public Services getServices() {
        return services;
    }

    public Remaining getRemaining() {
        return remaining;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public void setRemaining(Remaining remaining) {
        this.remaining = remaining;
    }
}
