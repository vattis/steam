package com.example.steam.core.init;

import com.example.steam.module.company.domain.Company;
import com.example.steam.module.company.repository.CompanyRepository;
import com.example.steam.module.discount.domain.Discount;
import com.example.steam.module.discount.repository.DiscountRepository;
import com.example.steam.module.friendship.application.FriendshipService;
import com.example.steam.module.friendship.domain.Friendship;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.domain.MemberGame;
import com.example.steam.module.member.repository.MemberGameRepository;
import com.example.steam.module.member.repository.MemberRepository;
import com.example.steam.module.product.domain.Product;
import com.example.steam.module.product.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostInit {
    private final MemberRepository memberRepository;
    private final MemberGameRepository memberGameRepository;
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final FriendshipService friendshipService;
    private final DiscountRepository discountRepository;
    @PostConstruct
    public void init(){
        List<Company> companies = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        List<Member> members = new ArrayList<>();
        //List<Product> discountProducts = new ArrayList<>();
        List<Discount> discounts = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            companies.add(Company.makeSample(i));
        }
        companies = companyRepository.saveAll(companies);
        for(int i = 1; i <= 21; i++){
            products.add(Product.makeSample(i, companies.get(i%5)));
            Product product = Product.makeSample(i+22, companies.get(i%5));
            productRepository.save(product);
            Discount discount = Discount.makeSample(i, product);
            discount.activeDiscount();
            //discountProducts.add(product);
            discounts.add(discount);
        }
        products = productRepository.saveAll(products);
        discounts = discountRepository.saveAll(discounts);
        for(int i = 1; i <= 10; i++){
            Member member = memberRepository.save(Member.makeSample(i));
            members.add(member);
            for(int j = 1;  j <= 4; j++){
                memberGameRepository.save(MemberGame.of(products.get(i*j/2), member));
            }
        }
        for(int i = 0; i < 9; i++){
            Friendship friendship = friendshipService.inviteFriend(members.get(i).getId(), members.get(i+1).getId());
            friendshipService.acceptFriend(friendship.getFromMember().getId(), friendship.getToMember().getId());
        }
        for(int i = 0; i < 8; i++){
            Friendship friendship = friendshipService.inviteFriend(members.get(i).getId(), members.get(i+2).getId());
            friendshipService.acceptFriend(friendship.getFromMember().getId(), friendship.getToMember().getId());
        }

    }

}
