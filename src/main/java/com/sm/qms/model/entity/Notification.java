package com.sm.qms.model.entity;

import com.sm.qms.model.constants.AppConstants;

public class Notification {

    public final String title;
    public final String message;
    public final String email;
    public final String phone;

    private Notification(String title, String message, String email, String phone) {
        this.title = title;
        this.message = message;
        this.email = email;
        this.phone = phone;
    }

    public static Notification create(String title, String message, String email) {
        return new Notification(title, message, email, null);
    }

    public static Notification create(String title, String message) {
        return new Notification(title, message, AppConstants.EMAIL_TO, null);
    }

    public static class Builder {
        public String title;
        public String message;
        public String email;
        public String phone;

        public Builder setTitle(String title) {
            this.title = title;
            return Builder.this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return Builder.this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return Builder.this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return Builder.this;
        }

        public Notification build() {
            return new Notification(this.title, this.message, this.email, this.phone);
        }
    }
}
