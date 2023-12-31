package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;

@SpringBootTest
@Transactional
class ItemServiceTest {

	@Autowired
	ItemRepository itemRepository;


	@DisplayName("상품을 추가한다")
	@Test
	void saveItem() throws Exception{
	    //Given
		Item item = new Item();
		item.setName("치킨");
		//When
		itemRepository.save(item);
		Item resultItem = itemRepository.findOne(item.getId());
		//Then
		Assertions.assertThat(item).isEqualTo(resultItem);
	}

	@DisplayName("상품을 추가하는데 있는 상품이면 변경된다")
	@Test
	void saveItem2() throws Exception{
	    //Given
		Item item = new Item();
		item.setName("치킨");
		itemRepository.save(item);
		Item mergeItem = itemRepository.findOne(item.getId());
		//When
		mergeItem.setName("피자");
		itemRepository.save(mergeItem);
		Item resultItem = itemRepository.findOne(mergeItem.getId());
		//Then
		Assertions.assertThat(resultItem.getName()).isEqualTo("피자");
	}


}