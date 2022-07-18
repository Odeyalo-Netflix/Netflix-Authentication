package com.odeyalo.analog.auth.service.sender.mail;

public class GenericMailMessage {
    private String subject;
    private String body;
    private String to;

    public GenericMailMessage(String subject, String body, String to) {
        this.subject = subject;
        this.body = body;
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public static GenericMailMessageBuilder builder() {
        return new GenericMailMessageBuilder();
    }

    public static final class GenericMailMessageBuilder {
        private String subject;
        private String body;
        private String to;

        private GenericMailMessageBuilder() {
        }

        public GenericMailMessageBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public GenericMailMessageBuilder body(String body) {
            this.body = body;
            return this;
        }

        public GenericMailMessageBuilder to(String to) {
            this.to = to;
            return this;
        }

        public GenericMailMessage build() {
            return new GenericMailMessage(subject, body, to);
        }
    }
}
