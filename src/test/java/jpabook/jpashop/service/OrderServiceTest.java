package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;

@SpringBootTest
@Transactional
class OrderServiceTest {

	@PersistenceContext
	EntityManager em;
	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	// @DisplayName("상품을 주문한다")
	@Test
	void createOrder() throws Exception {
		//Given
		Member member = createMember();
		Item item = createBook("시골 JPA", 10000, 10);
		int orderCount = 2;

		//When
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

		//Then
		Order order = orderRepository.findOne(orderId);

		assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER);

		assertThat(order.getOrderItems().size()).isEqualTo(1);

		assertThat(order.getTotalPrice()).isEqualTo(10000 * 2);

		assertThat(item.getStockQuantity()).isEqualTo(8);

	}

	@Test
	void itemOverOrder() throws Exception{
	    //Given
		Member member = createMember();
		Item item = createBook("시골 JPA", 10000, 10);
		int orderCount =11;
		//When
		//Then

	assertThatThrownBy(() ->orderService.order(member.getId(), item.getId(), orderCount))
							.isInstanceOf(NotEnoughStockException.class)
							.hasMessage("need more stock");
	}


	@Test
	void cancelOrder() throws Exception{
	    //Given
		Member member = createMember();
		Item item = createBook("시골 JPA", 10000, 10);
		int orderCount =2;
		Long orderId = orderService.order(member.getId(), item.getId(),orderCount);
		//When
		orderService.cancelOrder(orderId);

		//Then
		Order order = orderRepository.findOne(orderId);

		assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
		assertThat(item.getStockQuantity()).isEqualTo(10);


	}


	private Member createMember() {
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강가", "123-123"));
		em.persist(member);
		return member;
	}

	private Book createBook(String name, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setStockQuantity(stockQuantity);
		book.setPrice(price);
		em.persist(book);
		return book;
	}

}