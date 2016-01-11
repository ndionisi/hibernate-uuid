package com.github.ndionisi.hibernateuuid;

import com.github.ndionisi.hibernateuuid.entity.Customer;
import com.github.ndionisi.hibernateuuid.entity.Order;
import com.github.ndionisi.hibernateuuid.entity.OrderItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HibernateUuidApplication.class)
public class MappingITests {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	private EntityManager entityManager;

	private UUID orderId;

	@Before
	public void setUp() throws Exception {
		entityManager = entityManagerFactory.createEntityManager();
	}

	@Test
	public void save() {
		buildAndPersistData();
		// Clear to make sure the data will be read from the database again (to check the user-type)
		entityManager.clear();

		Order order = entityManager.find(Order.class, orderId);
		Customer customer = order.getCustomer();
		Set<OrderItem> orderItems = order.getOrderItems();

		assertThat(order, notNullValue());
		assertThat(order.getId(), notNullValue());
		assertThat(customer, notNullValue());
		assertThat(customer.getId(), notNullValue());
		assertThat(orderItems, hasSize(2));
		for (OrderItem orderItem : orderItems) {
			assertThat(orderItem, notNullValue());
			assertThat(orderItem.getId(), notNullValue());
		}
	}

	private void buildAndPersistData() {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		Customer customer = new Customer();
		customer.setName("John Smith");
		entityManager.persist(customer);

		Order order = new Order();
		order.setCustomer(customer);

		OrderItem orderItem1 = new OrderItem();
		orderItem1.setOrder(order);
		orderItem1.setProductName("Apple iPhone 6");

		OrderItem orderItem2 = new OrderItem();
		orderItem2.setOrder(order);
		orderItem2.setProductName("Google Nexus 5");

		Set<OrderItem> orderItems = new HashSet<>(Arrays.asList(orderItem1, orderItem2));
		order.setOrderItems(orderItems);

		entityManager.persist(order);
		transaction.commit();

		orderId = order.getId();
	}

}
