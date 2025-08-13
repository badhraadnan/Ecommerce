package com.ecom.orderService.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RabbitMQListner {

    @Autowired
    private emailSend emailSend;

    @Autowired
    private ObjectMapper objectMapper;


    //Place Order --Conformation
    @RabbitListener(queues = "ecommerce.place.order")
    public void placeOrder(String message) {
        try {
            System.out.println("📩 Received RabbitMQ message: " + message);

            if (message == null || message.trim().isEmpty()) {
                System.err.println("❌ Empty message received");
                return;
            }

            // Convert JSON message to Map
            Map<String, Object> cart = objectMapper.readValue(message, Map.class);

            String userName = (String) cart.get("UserName");
            String userEmail = (String) cart.get("Email");
            String subject = "✅ Order Placed Successfully";


            List<Map<String, String>> products = (List<Map<String, String>>) cart.get("Products");

            StringBuilder productHtml = new StringBuilder();

            for (Map<String, String> product : products) {
                String image = product.getOrDefault("Image", "https://via.placeholder.com/100");
                String name = product.getOrDefault("ProductName", "N/A");
                String qty = product.getOrDefault("Qty", product.getOrDefault("Quantity", "1"));
                String price = product.getOrDefault("Price", "0");

                productHtml.append(String.format("""
                <div style="margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 10px; display: flex; align-items: center;">
                                      <!-- Image on the left -->
                                      <div style="flex: 0 0 100px;">
                                          <img src="%s" alt="Product Image" width="100" height="100"
                                               style="object-fit: contain; border-radius: 5px;" />
                                      </div>
                                      <!-- Product details on the right -->
                                      <div style="margin-left: 20px; flex: 1;">
                                          <p><strong>Product:</strong> %s</p>
                                          <p><strong>Quantity:</strong> %s</p>
                                          <p><strong>Price:</strong> ₹%s</p>
                                      </div>
                                  </div>

            """, image, name, qty, price));
            }

            String body = String.format("""
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
                    .container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.05); }
                    .header { color: #4CAF50; font-size: 20px; font-weight: bold; margin-bottom: 20px; }
                    .details { font-size: 16px; line-height: 1.5; }
                    .footer { margin-top: 30px; font-size: 14px; color: #555; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">Hello %s,</div>
                    <div class="details">
                        <p>Thank you for your order! Below are the details of your purchase:</p>
                        %s
                    </div>
                    <div class="footer">
                        We appreciate your business.<br/>
                        <strong>— E-Commerce Team</strong>
                    </div>
                </div>
            </body>
            </html>
        """, userName, productHtml.toString());

            // ✅ Send email as HTML
            emailSend.SendEmail(userEmail, subject, body, true);

        } catch (JsonProcessingException e) {
            System.err.println("❌ JSON parsing error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Error in placeOrder listener: " + e.getMessage());
            e.printStackTrace();
        }
    }



    //Cart Reminder
    @RabbitListener(queues = "ecommerce.cart.reminder")
    public void carReminder(String message) {
        try {

            System.out.println("Received RabbitMQ message: " + message);

            if (message == null || message.trim().isEmpty()) {
                System.err.println("Empty message received");
                return;
            }

            Map<String, Object> cart = objectMapper.readValue(message, Map.class);

            String userName = (String) cart.get("UserName");
            String userEmail = (String) cart.get("Email");
            String Subject = "Reminder to Checkout your Cart";

            List<Map<String, String>> products = (List<Map<String, String>>) cart.get("Product");

            StringBuilder productHtml = new StringBuilder();

            for (Map<String, String> product : products) {
                productHtml.append("""
                            <div style="margin-bottom: 20px;">
                                <img src="%s" alt="Product Image" class="product-image" />
                                <p><strong>Product:</strong> %s</p>
                                <p><strong>Quantity:</strong> %s</p>
                                <p><strong>Price:</strong> ₹%s</p>
                            </div>
                        """.formatted(
                        product.get("Image"),
                        product.get("ProductName"),
                        product.get("Qty"),
                        product.get("Price")
                ));
            }

            String Body = """
                        <html>
                        <head>
                            <style>
                                body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
                                .container { background-color: #fff; padding: 20px; border-radius: 8px; }
                                .header { color: #4CAF50; font-size: 20px; font-weight: bold; margin-bottom: 20px; }
                                .details { font-size: 16px; line-height: 1.5; }
                                .footer { margin-top: 30px; font-size: 14px; color: #555; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">Hello %s,</div>
                                <div class="details">
                                    <p style='color:blue;'>Looks like you left some delicious items in your cart!</p>
                                    %s
                                </div>
                                <div class="footer">
                                    Thank you for shopping with us!<br/>
                                    <strong>E-Commerce Team</strong>
                                </div>
                            </div>
                        </body>
                        </html>
                    """.formatted(
                    userName,
                    productHtml.toString()
            );
            emailSend.SendEmail(userEmail, Subject, Body, true);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println("❌ Error handling cart reminder: " + e.getMessage());
            e.printStackTrace();
        }

    }


    //Cancel order
    @RabbitListener(queues = "ecommerce.cancel.order")
    public void CancelOrder(String message) {
        try {
            Map<String, String> map = objectMapper.readValue(message, Map.class);

            String toEmail = map.get("email");
            String Subject = map.get("ProductName") + "... from your order has been cancelled";
            String body = """
                    <html>
                    <head>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f5f5f5;
                                margin: 0;
                                padding: 20px;
                            }
                    
                            .container {
                                background-color: #ffffff;
                                padding: 20px;
                                border-radius: 6px;
                                max-width: 700px;
                                margin: auto;
                                border: 1px solid #ddd;
                            }
                    
                            .status-box {
                                border: 1px solid #28a745;
                                background-color: #e6f9ec;
                                padding: 15px;
                                border-radius: 4px;
                                margin-bottom: 20px;
                            }
                    
                            .status-box p {
                                margin: 0;
                                font-size: 15px;
                                color: #333;
                            }
                    
                            .highlight {
                                font-weight: bold;
                            }
                    
                            .order-link {
                                color: #007bff;
                                text-decoration: none;
                            }
                            .product {
                                display: flex;
                                gap: 20px;
                                align-items: center;
                                margin-top: 20px;
                                border-top: 1px solid #ddd;
                                padding-top: 20px;
                            }
                    
                            .product img {
                                width: 100px;
                                height: auto;
                            }
                    
                            .product-details {
                                flex: 1;
                            }
                    
                            .product-details p {
                                margin: 5px 0;
                                font-size: 15px;
                            }
                    
                            .footer {
                                margin-top: 30px;
                                font-size: 14px;
                                color: #555;
                            }
                    
                            .bold {
                                font-weight: bold;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="status-box">
                                <p>Hi <span class="highlight">%s</span>,</p>
                                <p>Based on your request, your order <a href="#" class="order-link"></a> for the below listed item has been cancelled by the seller.</p><br>
                                <p>The amount you paid will be refunded within 24 hours. We will notify you via email and SMS when the refund is initiated.</p>
                            </div>
                    
                            <div class="product">
                                <img src="%s" alt="Product Image" />
                                <div class="product-details">
                                    <p class="bold">%s</p>
                                    <p>Seller: %s</p>
                                    <p>Qty: %s</p>
                                    <p class="bold">Rs. %s</p>
                                </div>
                            </div>
                    
                            <div class="footer">
                                Hope to see you again soon.
                            </div>
                        </div>
                    </body>
                    </html>
                    
                    """.formatted(
                    map.get("userName"),
                    map.get("Image"),
                    map.get("ProductName"),
                    map.get("Price"),
                    map.get("Qty"),
                    map.get("TotalAmount")
            );
            emailSend.SendEmail(toEmail,Subject,body,true);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

