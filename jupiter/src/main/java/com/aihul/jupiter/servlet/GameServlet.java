package com.aihul.jupiter.servlet;

import com.aihul.jupiter.external.TwitchClient;
import com.aihul.jupiter.external.TwitchException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "GameServlet", value = "/game")
public class GameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gameName = request.getParameter("game_name");
        response.setContentType("application/Json");
        ObjectMapper objectMapper = new ObjectMapper();

        TwitchClient twitchClient = new TwitchClient();

        try {
            if (gameName != null) {
                response.getWriter().print(objectMapper.writeValueAsString(twitchClient.searchGame(gameName)));
            } else {
                response.getWriter().print(objectMapper.writeValueAsString(twitchClient.topGames(0)));
            }
        } catch (TwitchException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
