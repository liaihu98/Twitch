//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.aihul.jupiter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "helloServlet",
        value = {"/hello-servlet"}
)
public class HelloServlet extends HttpServlet {
    private String message;

    public HelloServlet() {
    }

    public void init() {
        this.message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + this.message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}
