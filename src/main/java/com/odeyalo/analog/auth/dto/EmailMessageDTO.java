package com.odeyalo.analog.auth.dto;

import com.odeyalo.analog.auth.service.sender.mail.GenericMailMessage;

public class EmailMessageDTO {
    private String subject;
    private String body;
    private String to;

    public EmailMessageDTO(String subject, String body, String to) {
        this.subject = subject;
        this.body = body;
        this.to = to;
    }
    public static EmailMessageDTO from(GenericMailMessage message) {
        return new EmailMessageDTO(message.getSubject(), message.getBody(), message.getTo());
    }

    public static GenericMailMessage to(EmailMessageDTO dto) {
        return GenericMailMessage.builder()
                .body(dto.getBody())
                .to(dto.getTo())
                .subject(dto.getSubject())
                .build();
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

    @Override
    public String toString() {
        return "EmailMessageDTO{" +
                "subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
