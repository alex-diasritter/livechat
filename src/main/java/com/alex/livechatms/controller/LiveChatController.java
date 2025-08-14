package com.alex.livechatms.controller;
import com.alex.livechatms.domain.ChatInput;
import com.alex.livechatms.domain.ChatOutput;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class LiveChatController {

    @MessageMapping("/new-message")
    @SendTo("/topics/livechat")
    public ChatOutput newMessage(ChatInput input) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new ChatOutput(HtmlUtils.htmlEscape("[" + time + "] "+ input.user() + ": " + input.message()));
    }
}
