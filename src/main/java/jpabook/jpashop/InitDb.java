package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

  private final InitService initService;

  @PostConstruct
  public void init() {
    initService.dbInit1();
    initService.dbInit2();
  }

  @Component
  @Transactional
  @RequiredArgsConstructor
  static class InitService {

    private final EntityManager em;
    public void dbInit1() {
      Member member = new Member();
      member.setName("userA");
      member.setAddress(new Address("서울", "1", "1111"));
      em.persist(member);

      Book book1 = new Book();
      book1.setName("JPA1 BOOK");
      book1.setPrice(10_000);
      book1.setStockQuantity(100);
      em.persist(book1);

      Book book2 = new Book();
      book2.setName("JPA2 BOOK");
      book2.setPrice(20_000);
      book2.setStockQuantity(100);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10_000, 1);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20_000, 2);

      Delivery delivery = new Delivery();
      delivery.setAddress(member.getAddress());
      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);
    }

    public void dbInit2() {
      Member member = new Member();
      member.setName("userB");
      member.setAddress(new Address("진주", "2", "2222"));
      em.persist(member);

      Book book1 = new Book();
      book1.setName("SPRING1 BOOK");
      book1.setPrice(10_000);
      book1.setStockQuantity(100);
      em.persist(book1);

      Book book2 = new Book();
      book2.setName("SPRING2 BOOK");
      book2.setPrice(20_000);
      book2.setStockQuantity(100);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10_000, 1);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20_000, 2);

      Delivery delivery = new Delivery();
      delivery.setAddress(member.getAddress());
      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);
    }
  }
}
