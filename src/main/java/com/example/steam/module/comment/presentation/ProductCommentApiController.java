package com.example.steam.module.comment.presentation;

import com.example.steam.module.comment.application.ProductCommentService;
import com.example.steam.module.comment.domain.ProductComment;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.dto.SimpleMemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class ProductCommentApiController {
    private final ProductCommentService productCommentService;
    private final MemberService memberService;

    @PostMapping("/product/{productId}/comment")
    public ResponseEntity<Map<String, String>> addProductComment(
            @PathVariable("productId") Long productId,
            @RequestBody Map<String, Object> body,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto member = memberService.findMemberDtoByEmail(principal.getName());
        String content = (String) body.get("content");
        Number ratingNum = (Number) body.get("rating");
        Float rating = ratingNum != null ? ratingNum.floatValue() : 0f;

        productCommentService.makeProductComment(member.getId(), productId, content, rating);

        return ResponseEntity.ok(Map.of("message", "리뷰가 작성되었습니다."));
    }

    @DeleteMapping("/product/comment/{commentId}")
    public ResponseEntity<Map<String, String>> deleteProductComment(
            @PathVariable("commentId") Long commentId,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto member = memberService.findMemberDtoByEmail(principal.getName());
        ProductComment productComment = productCommentService.findById(commentId);
        productCommentService.deleteProductComment(productComment, member.getId());

        return ResponseEntity.ok(Map.of("message", "삭제되었습니다."));
    }
}
