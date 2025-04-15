/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.mycompany;

import org.asteriskjava.fastagi.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mycompany.BalanceAPI;

/**
 *
 * @author syousrei
 */

public class AGIasterisk extends BaseAgiScript {

    @Override
    public void service(AgiRequest request, AgiChannel channel) throws AgiException {
        Client client = ClientBuilder.newClient();
        String number = "";
        int triers = 1;

        try {
            answer();

            // ننتظر قليلًا
            channel.exec("WAIT", "2");
            channel.streamFile("custom/welcome-prompt");

            // ادخل الرقم
            while (true || triers > 2) {
                channel.streamFile("enter-number");
                number = channel.getData("beep", 5000, 11); // Timeout: 5s, Max 11 digits
                if ("".equals(number) || 11 > number.length()) {
                    channel.streamFile("invalid-number");
                    channel.streamFile("try-again");
                    if (triers == 2) {
                        // Error handling after 2 retries
                    }
                    triers++;
                } else {
                    channel.exec("WAIT", "2");
                    channel.streamFile("you-entered");
                    channel.sayDigits(number);
                    break;
                }
            }

            // ارسال طلب HTTP إلى الـ API لاسترجاع الرصيد
            WebTarget target = client.target("http://localhost:8080/api/BalanceAPI")
                                      .queryParam("msisdn", number);
            Response response = target.request(MediaType.APPLICATION_JSON).get();

            // التحقق من حالة الاستجابة
            if (response.getStatus() == 200) {
                // استرجاع الرصيد من الـ API
                String balance = response.readEntity(String.class);

                // إذا تم الحصول على الرصيد بنجاح
                channel.streamFile("your-balance-is");
                channel.sayDigits(balance); // قول الرقم
                channel.streamFile("egyptian-pound");
                channel.exec("WAIT", "1");
                channel.streamFile("thank-you");
            } else {
                // في حالة عدم النجاح في الحصول على الرصيد
                channel.streamFile("api-error");
            }

            channel.hangup();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            channel.streamFile("error");
        } finally {
            hangup();
        }
    }
}
