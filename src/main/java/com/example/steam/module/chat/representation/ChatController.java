package com.example.steam.module.chat.representation;

import com.example.steam.module.chat.application.ChatRoomService;
import com.example.steam.module.chat.application.ChatService;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;


    //채팅 보내기
    @MessageMapping("/chat/{chatRoomId}/send")
    public void sendChat(@DestinationVariable Long chatRoomId,
                           @Payload String content,
                         Principal principal) {
        Member member = memberService.findMemberByEmail(principal.getName());
        chatService.sendMessage(chatRoomId, member.getId(), content);
    }

}
