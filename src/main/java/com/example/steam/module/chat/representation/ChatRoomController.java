package com.example.steam.module.chat.representation;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.chat.application.ChatRoomService;
import com.example.steam.module.chat.application.ChatService;
import com.example.steam.module.chat.domain.ChatRoom;
import com.example.steam.module.chat.dto.ChatDtoScroll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    //채팅방 입장
    @GetMapping("/chatRoom/{chatRoomId}")
    private String gotoChatRoom(@PathVariable Long chatRoomId, Long memberId, Model model){
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        if(!chatRoom.checkMember(memberId)){
            log.info("권한이 없는 채팅방 입장 시도: chatRoom = {} , member = {}", chatRoomId, memberId);
            return null;
        }
        ChatDtoScroll chatDtoScroll = chatService.loadChats(chatRoomId, PageConst.CHAT_SCROLL_SIZE, null, null);
        model.addAttribute("chats", chatDtoScroll.getChats());
        model.addAttribute("paging", chatDtoScroll.getChatPaging());
        return "chatroom";
    }
}
