package com.company.order_processing_engine.model;


import java.util.List;

public class Order {
        private String orderId;
        private Customer customer;
        private List<OrderItem> items;
        private OrderStatus status;
        private double totalAmount;
        private double discount;
        private double tax;


        // No-args constructor
        public Order() {
        }

        // All-args constructor
        public Order(String orderId, Customer customer, List<OrderItem> items,
                     OrderStatus status, double totalAmount, double discount, double tax) {
                this.orderId = orderId;
                this.customer = customer;
                this.items = items;
                this.status = status;
                this.totalAmount = totalAmount;
                this.discount = discount;
                this.tax = tax;
        }

        // Getters and Setters
        public String getOrderId() {
                return orderId;
        }

        public void setOrderId(String orderId) {
                this.orderId = orderId;
        }

        public Customer getCustomer() {
                return customer;
        }

        public void setCustomer(Customer customer) {
                this.customer = customer;
        }

        public List<OrderItem> getItems() {
                return items;
        }

        public void setItems(List<OrderItem> items) {
                this.items = items;
        }

        public OrderStatus getStatus() {
                return status;
        }

        public void setStatus(OrderStatus status) {
                this.status = status;
        }

        public double getTotalAmount() {
                return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
                this.totalAmount = totalAmount;
        }

        public double getDiscount() {
                return discount;
        }

        public void setDiscount(double discount) {
                this.discount = discount;
        }

        public double getTax() {
                return tax;
        }

        public void setTax(double tax) {
                this.tax = tax;
        }

}
