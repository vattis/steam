package com.example.steam.module.chat.application;

import com.example.steam.module.chat.domain.ChatRoom;
import com.example.steam.module.chat.repository.ChatRepository;
import com.example.steam.module.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;

    public ChatRoom getChatRoomById(long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(NoSuchElementException::new);
    }
}
