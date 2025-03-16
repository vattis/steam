package com.example.steam.module.friendship.domain;

import com.example.steam.module.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

//친구 관계 엔티티
//서로 친구일 경우 fromMember = member1, toMember = member2
//fromMember = member2, toMember = member1
//이런식으로 양방향으로 엔티티가 생기도록 함
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql="UPDATE friendship SET deleted = true WHERE id=?")
@SQLRestriction("deleted is false")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //자신
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member fromMember;

    //상대
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member toMember;
    
    //친구 수락 여부
    @Column(nullable = false)
    private Boolean accepted;

    @Column(name="deleted", nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean deleted = false;
}
