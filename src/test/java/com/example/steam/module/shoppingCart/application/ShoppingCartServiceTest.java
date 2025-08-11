package com.example.steam.module.shoppingCart.application;

import com.example.steam.module.company.domain.Company;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import com.example.steam.module.order.domain.Orders;
import com.example.steam.module.order.repository.OrdersRepository;
import com.example.steam.module.product.domain.Product;
import com.example.steam.module.product.repository.ProductRepository;
import com.example.steam.module.shoppingCart.domain.ShoppingCart;
import com.example.steam.module.shoppingCart.domain.ShoppingCartProduct;
import com.example.steam.module.shoppingCart.repository.ShoppingCartProductRepository;
import com.example.steam.module.shoppingCart.repository.ShoppingCartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    ShoppingCartRepository shoppingCartRepository;
    @Mock
    ShoppingCartProductRepository shoppingCartProductRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    OrdersRepository ordersRepository;

    @InjectMocks
    ShoppingCartService shoppingCartService;


    @Test
    @DisplayName("member id를 받고 shoppingCartProductRepository.findAllByShoppingCartId를 제대로 호출 하는지 확인")
    void getShoppingCartProducts() {
        //given
        Member member = Member.makeSample(1);
        int pageNo = 0;
        ReflectionTestUtils.setField(member, "id", 1L);
        ShoppingCart shoppingCart = member.getShoppingCart();
        ReflectionTestUtils.setField(shoppingCart, "id", 1L);
        Optional<Member> optional = Optional.of(member);

        //when
        shoppingCartService.getShoppingCartProducts(member, pageNo);

        //then
        verify(shoppingCartProductRepository, times(1)).findAllByShoppingCartId(eq(shoppingCart.getId()), any(PageRequest.class));
    }

    @Test
    void removeShoppingCartProduct() {
        //given
        Long shoppingCartProductId = 1L;

        //when
        shoppingCartService.removeShoppingCartProduct(shoppingCartProductId);

        //then
        verify(shoppingCartProductRepository, times(1)).deleteById(shoppingCartProductId);
    }

    @Test
    void addShoppingCartProduct() {
        //given
        Member member = Member.makeSample(1);
        ReflectionTestUtils.setField(member, "id", 1L);
        Optional<Member> optionalMember = Optional.of(member);
        given(memberRepository.findById(member.getId())).willReturn(optionalMember);
        Product product = Product.makeSample(1, Company.makeSample(1));
        ReflectionTestUtils.setField(product, "id", 1L);
        Optional<Product> optionalProduct = Optional.of(product);
        given(productRepository.findById(product.getId())).willReturn(optionalProduct);

        //when
        shoppingCartService.addShoppingCartProduct(member.getId(), product.getId());

        //then
        assertThat(optionalMember.get().getShoppingCart().getShoppingCartProducts().get(0).getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("shoppingCart에 있는 게임을 지우고 Order로 변환되는지 확인")
    void makeShoppingCartToOrder() {
        //given
        Member member = Member.makeSample(1);
        ReflectionTestUtils.setField(member, "id", 1L);
        Optional<Member> optionalMember = Optional.of(member);
        given(memberRepository.findById(member.getId())).willReturn(optionalMember);
        ShoppingCart shoppingCart = member.getShoppingCart();
        for(int i = 0; i < 10; i++){
            given(productRepository.findById((long)i)).willReturn(Optional.of(Product.makeSample(i, Company.makeSample(i))));
            shoppingCartService.addShoppingCartProduct(member.getId(), (long)i);
        }
        Orders order = member.getShoppingCart().toOrders();
        ReflectionTestUtils.setField(order, "id", 1L);
        given(ordersRepository.save(any(Orders.class))).willReturn(order);

        //when
        Orders orderResult = shoppingCartService.makeShoppingCartToOrder(member.getId());

        //then
        //shoppingCart를 orders로 전환한 후 shoppingCart를 비웠는지 확인
        assertThat(shoppingCart.getShoppingCartProducts().isEmpty()).isTrue();
        //order에 아이템이 들어갔는지 확인
        assertThat(orderResult.getOrderProducts().size()).isEqualTo(10);


    }
}