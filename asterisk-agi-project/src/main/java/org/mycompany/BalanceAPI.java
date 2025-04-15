package org.mycompany;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.io.IOException;

@Path("/BalanceAPI")
public class BalanceAPI {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalance(@QueryParam("msisdn") String msisdn) {
        try {
            if (msisdn == null || msisdn.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Phone number is missing").build();
            }

            // اتصل بقاعدة البيانات باستخدام كلاس psql
            PSQL db = new PSQL(); // تأكد أن هذا الكلاس فيه دالة getBalance
            double balance = db.getBalance(msisdn);

            return Response.ok(String.valueOf(balance)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving balance").build();
        }
    }
}
