package com.example.steam.module.comment.application;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.comment.domain.ProfileComment;
import com.example.steam.module.comment.repository.ProfileCommentRepository;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProfileCommentService {
    private final ProfileCommentRepository profileCommentRepository;
    private final MemberRepository memberRepository;
    //댓글 달기
    public ProfileComment makeProfileComment(Long memberId, Long profileMemberId, String content){
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        Member profileMember = memberRepository.findById(profileMemberId).orElseThrow(NoSuchElementException::new);
        ProfileComment profileComment = ProfileComment.of(member, content, profileMember);
        return profileCommentRepository.save(profileComment);
    }

    //profile member의 id로 댓글 찾기
    public Page<ProfileComment> findProfileCommentByProfileMemberId(Long profileMemberId, int pageNum){
        PageRequest pageRequest = PageRequest.of(pageNum, PageConst.PROFILE_COMMENT_PAGE_SIZE);
        return profileCommentRepository.findAllByProfileMemberId(profileMemberId, pageRequest);
    }

    //댓글 삭제
    public boolean deleteProfileComment(Long profileCommentId, Long memberId){
        ProfileComment profileComment = profileCommentRepository.findById(profileCommentId).orElseThrow(NoSuchElementException::new);
        if(profileComment.getMember().getId() != memberId || profileComment.getProfileMember().getId() != memberId){
            log.info("잘못된 ProfileComment 삭제:: 회원 불일치");
            return false;
        }
        profileCommentRepository.delete(profileComment);
        return true;
    }
}
