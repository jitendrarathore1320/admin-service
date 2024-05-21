package com.advantal.adminRoleModuleService.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {
    private String fromEmail;
    private String toEmail;
    private String subject;
    private String message;
}
