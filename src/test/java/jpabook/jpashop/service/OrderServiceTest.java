package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NoEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

  @Autowired EntityManager em;
  @Autowired OrderService orderService;
  @Autowired OrderRepository orderRepository;

  @Test
  public void 상품주문() {
    // given
    Member member = createMember();
    Book book = createBook("시골 JPA", 10_000, 10);

    int orderCount = 2;

    // when
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    // then
    Order getOrder = orderRepository.findOne(orderId);

    assertEquals(OrderStatus.ORDER, getOrder.getStatus());
    assertEquals(1, getOrder.getOrderItems().size());
    assertEquals(10_000 * orderCount, getOrder.getTotalPrice());
    assertEquals(8, book.getStockQuantity());
  }

  private Book createBook(String name, int price, int stockQuantity) {
    Book book = new Book();
    book.setName(name);
    book.setPrice(price);
    book.setStockQuantity(stockQuantity);
    em.persist(book);
    return book;
  }

  private Member createMember() {
    Member member = new Member();
    member.setName("회원1");
    member.setAddress(new Address("서울", "강가", "123-123"));
    em.persist(member);
    return member;
  }

  @Test
  public void 상품주문_재고수량초과() {
    // given
    Member member = createMember();
    Book book = createBook("시골 JPA", 10_000, 10);

    int orderCount = 11;

    // when & then
    assertThrows(NoEnoughStockException.class, () -> {
      orderService.order(member.getId(), book.getId(), orderCount);
    });
  }

  @Test
  public void 주문취소() {
    // given
    Member member = createMember();
    Book book = createBook("시골 JPA", 10_000, 10);

    int orderCount = 2;
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    // when
    orderService.cancelOrder(orderId);

    // then
    Order getOrder = orderRepository.findOne(orderId);

    assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
    assertEquals(10, book.getStockQuantity());
  }


}