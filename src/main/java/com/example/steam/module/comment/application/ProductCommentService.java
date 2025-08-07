package com.example.steam.module.comment.application;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.comment.domain.ProductComment;
import com.example.steam.module.comment.repository.ProductCommentRepository;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.product.domain.Product;
import com.example.steam.module.product.repository.ProductRepository;
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
public class ProductCommentService {
    private final ProductRepository productRepository;
    private final ProductCommentRepository productCommentRepository;

    //댓글 달기
    public ProductComment makeProductComment(Member member, Long productId, String content, Float rate){
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        ProductComment productComment = ProductComment.of(product, member, content, rate);
        return productCommentRepository.save(productComment);
    }

    //게임id로 게임 후기 찾기
    public Page<ProductComment> findProductCommentByProductId(Long productId, int pageNum){
        PageRequest pageRequest = PageRequest.of(pageNum, PageConst.PRODUCT_COMMENT_PAGE_SIZE);
        return productCommentRepository.findAllByProductId(productId, pageRequest);
    }

    //댓글 삭제
    public boolean deleteProductComment(Long productCommentId, Member member){
        ProductComment productComment = productCommentRepository.findById(productCommentId).orElseThrow(NoSuchElementException::new);
        if(productComment.getMember().getId() != member.getId()){ //댓글 작성자와 삭제 요청자가 다른 경우
            log.info("잘못된 ProductComment 삭제:: 회원 불일치");
            return false;
        }
        Product product = productComment.getProduct();
        product.getProductComments().remove(productComment);
        productCommentRepository.delete(productComment);
        return true;
    }
}
