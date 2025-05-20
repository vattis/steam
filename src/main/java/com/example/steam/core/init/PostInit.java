package com.example.steam.core.init;

import com.example.steam.module.company.domain.Company;
import com.example.steam.module.company.repository.CompanyRepository;
import com.example.steam.module.member.application.MemberService;
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

    @PostConstruct
    public void init(){
        List<Company> companies = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            companies.add(Company.makeSample(i));
        }
        companies = companyRepository.saveAll(companies);
        for(int i = 1; i <= 21; i++){
            products.add(Product.makeSample(i, companies.get(i%5)));
        }
        products = productRepository.saveAll(products);
        for(int i = 1; i <= 10; i++){
            Member member = memberRepository.save(Member.makeSample(i));
            for(int j = 1;  j <= 4; j++){
                memberGameRepository.save(MemberGame.of(products.get(i*j/2), member));
            }
        }

    }

}
