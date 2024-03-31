package com.AmericanBoutique.service;

import com.AmericanBoutique.Utils.HibernateUtil;
import com.AmericanBoutique.model.Orders;
import com.AmericanBoutique.model.Product;
import com.AmericanBoutique.model.ShoppingCart;
import com.AmericanBoutique.repo.ProductRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import com.AmericanBoutique.repo.OrderRepository;


import java.util.List;
import java.util.Scanner;

@Service
public class OrderServiceImpl implements OrderService{

    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository){
        super();
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // Implement methods for managing orders

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Orders saveOrder(Orders orders) {
        return orderRepository.save(orders);
    }

    public void deleteOrderById(Long id){
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteOrderByEntity(Orders orders){
        orderRepository.delete(orders);
    }

    @Override
    public Orders getOrderById(Long id) {
        return orderRepository.findById(id).get();
    }


    @Override
    public void deleteOrderHibernate1ById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Orders orders = session.get(Orders.class, id);
            if (orders != null) {
                session.delete(orders);
            }
            transaction.commit(); // Commit the transaction
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback the transaction if an exception occurs
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @Override
    public void deleteOrderHibernate2ById(Long id){
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Teacher ID To delete: ");
        int tid = scanner.nextInt();

        Orders o = session.get(Orders.class,tid);

        String name = o.getStatus();
        System.out.println("-----------> ["+name+"] deleted");

        session.remove(o);
        transaction.commit();

        session.close();
    }


//    @Override
//    public Orders getOrderByUserId(Long user_id) {
//        return orderRepository.findOrdersByUser(user_id);
//    }

    public List<Product> findJoinProductsUserAddToCart(@Param("userId") Long userId){
        return orderRepository.findProductsByUserId(userId);
    }

    public void deleteOrderFromCart(@Param("oId") Long orderId){
        orderRepository.deleteOrderFromCart(orderId);
    }

//    public void deleteJoinOrderFromCart(Long id){
//        orderRepository.deleteOrderFromCart(id);
//    }


    public int getTotalInBag() {
        // Retrieve cart items from repository or service
        List<Orders> orders = orderRepository.findAll(); // Adjust this according to your actual implementation

        // Check if cartItems is null or empty
        if (orders == null || orders.isEmpty()) {
            return 0; // Return 0 if there are no cart items
        }

        int totalCount = orders.size();

        return totalCount;
    }


}