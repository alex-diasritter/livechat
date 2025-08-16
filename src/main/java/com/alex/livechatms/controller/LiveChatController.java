package com.alex.livechatms.controller;
import com.alex.livechatms.domain.ChatInput;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class LiveChatController {

    @MessageMapping("/new-message")
    @SendTo("/topics/livechat")
    public Map<String, String> newMessage(ChatInput input, Principal principal) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        String sender = (principal != null) ? principal.getName() : "Anônimo";

        return Map.of(
                "type", input.user().equals("System") ? "system" : "user",
                "user", HtmlUtils.htmlEscape(input.user()),
                "message", HtmlUtils.htmlEscape(input.message()),
                "timestamp", time
        );
    }
}
