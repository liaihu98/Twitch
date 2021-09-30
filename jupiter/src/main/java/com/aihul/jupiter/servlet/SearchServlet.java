package com.aihul.jupiter.servlet;

import com.aihul.jupiter.external.TwitchClient;
import com.aihul.jupiter.external.TwitchException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "SearchServlet", value = "/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String gameId = request.getParameter("game_id");
        response.setContentType("application/json; charset = UTF-8");

        if (gameId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        TwitchClient client = new TwitchClient();
        try {
            response.getWriter().print(mapper.writeValueAsString(client.searchItems(gameId)));
        } catch (TwitchException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
