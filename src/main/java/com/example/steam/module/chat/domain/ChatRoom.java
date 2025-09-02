package com.example.steam.module.chat.domain;

import com.example.steam.module.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
@Check(constraints = "member1_id <> member2_id")
@SQLDelete(sql="UPDATE chat_room SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member1_id", nullable = false)
    private Member member1;

    @ManyToOne
    @JoinColumn(name = "member2_id", nullable = false)
    private Member member2;

    @Column
    private LocalDateTime created;

    @Builder.Default
    private boolean deleted = false;

    public static ChatRoom of(Member member1, Member member2){
        if(member1.getId().equals(member2.getId())){
            log.info("잘못된 채팅방 생성 요청: 두 Member 가 일치함");
            return null;
        }
        return ChatRoom.builder()
                .member1(member1)
                .member2(member2)
                .created(LocalDateTime.now())
                .build();
    }
}
