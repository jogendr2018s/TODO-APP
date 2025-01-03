package org.example;

import io.dropwizard.core.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.*;
import jakarta.validation.constraints.*;

public class trueConfiguration extends Configuration {

    @NotEmpty
    private String databaseUrl;

    @NotEmpty
    private String databaseUser;

    @NotEmpty
    private String databasePassword;

    @JsonProperty
    public String getDatabaseUrl() {
        return databaseUrl;
    }

    @JsonProperty
    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    @JsonProperty
    public String getDatabaseUser() {
        return databaseUser;
    }

    @JsonProperty
    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    @JsonProperty
    public String getDatabasePassword() {
        return databasePassword;
    }

    @JsonProperty
    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
}
