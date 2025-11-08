package co.edu.umanizales.bookverse.service;

import co.edu.umanizales.bookverse.model.Order;
import co.edu.umanizales.bookverse.repository.IOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private final IOrderRepository orderRepository;

    @Autowired
    public OrderService(IOrderRepository orderRepository) {
        log.info("Initializing OrderService with repository: {}", orderRepository.getClass().getName());
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        order.calculateTotals();
        return orderRepository.save(order);
    }

    public boolean deleteOrder(String id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            log.info("Order deleted successfully: {}", id);
            return true;
        }
        log.warn("Order not found for deletion: {}", id);
        return false;
    }

    public boolean existsById(String id) {
        return orderRepository.existsById(id);
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getOrdersBySalesperson(String salespersonId) {
        return orderRepository.findBySalespersonId(salespersonId);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}
