package com.selfvault.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.security.auth.Destroyable;
import java.util.Arrays;

public class SecretDto implements Destroyable {
    private final String title, login, note;
    private final char[] secret;

    @JsonCreator
    public SecretDto(
            @JsonProperty("title") String title,
            @JsonProperty("login") String login,
            @JsonProperty("note") String note,
            @JsonProperty("secret") char[] secret
    ) {
        this.title = title;
        this.note = note;
        this.login = login;
        this.secret = secret;
    }
    private SecretDto(Builder builder) {
        this.title = builder.title;
        this.note = builder.note;
        this.login = builder.login;
        this.secret = builder.secret;
    }

    @Override
    public void destroy() {
        if (this.secret != null) {
            Arrays.fill(this.secret, '\0');
        }
    }

    public static Builder builder(String title, char[] secret) {
        return new Builder(title, secret);
    }

    public static class Builder {
        private final String title;
        private final char[] secret;
        private String login;
        private String note;

        public Builder(String title, char[] secret) {
            this.title = title;
            this.secret = secret;
        }

        public Builder addLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder addNote(String note) {
            this.note = note;
            return this;
        }

        public SecretDto build() {
            return new SecretDto(this);
        }
    }

    public String getLogin() {
        return login;
    }

    public char[] getSecret() {
        return secret;
    }

    public String getNote() {
        return note;
    }

    public String getTitle() {
        return title;
    }
}
