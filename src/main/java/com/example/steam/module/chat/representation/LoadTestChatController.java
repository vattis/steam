package com.example.steam.module.chat.representation;

import com.example.steam.module.chat.application.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Profile("load")
@Controller
@RequiredArgsConstructor
public class LoadTestChatController {
    private final ChatService chatService;
    @MessageMapping("/chat/{chatRoomId}/send")
    public void sendLoadTest(@DestinationVariable("chatRoomId") Long chatRoomId, @Payload String content){
        chatService.sendMessage(chatRoomId, 100001L, content);
    }
}
