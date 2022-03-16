package com.odeyalo.analog.auth.dto;

public class MailMessageDTO {
    private String subject;
    private String body;
    private String to;

    public MailMessageDTO() {
    }

    public MailMessageDTO(String subject, String body, String to) {
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
}
