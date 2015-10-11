/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ars.source;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sharan
 */
@WebServlet(name = "DisplayFlightDetails", urlPatterns = {"/DisplayFlightDetails"})
public class DisplayFlightDetails extends HttpServlet {

    private String getFlightDetails(String flightNo) {
        try {
            Connection cn = MySQL.connect();
            EntityFactory ef = new EntityFactory(cn, "select flight_leg.flight_number, flight_leg.leg_number, airport.name as departure_airport,\n" +
"flight_leg.scheduled_departure_time,aairport.name as arrival_airport, flight_leg.scheduled_arrival_time\n" +
"from flight_leg join airport on flight_leg.departure_airport_code = airport.airport_code\n" +
"join airport aairport on flight_leg.arrival_airport_code = aairport.airport_code\n" +
"where flight_leg.flight_number = ?\n" +
"order by leg_number ");
            List<Map<String, Object>> resSet = ef.findMultiple(new String[]{flightNo});            
            String res = new Gson().toJson(resSet);
            MySQL.close(cn);
            return res;
        } catch (Exception e) {
            return null;
        }
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {            
            String res = getFlightDetails(request.getParameter("flight_no"));            
            out.write(res);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
