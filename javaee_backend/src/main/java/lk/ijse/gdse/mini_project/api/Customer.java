package lk.ijse.gdse.mini_project.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.mini_project.db.DBProcess;
import lk.ijse.gdse.mini_project.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "customer", urlPatterns = "/customer",
        initParams = {
                @WebInitParam(name = "db-user", value = "root"),
                @WebInitParam(name = "db-pw", value = "1234"),
                @WebInitParam(name = "db-url", value = "jdbc:mysql://localhost:3306/miniProject"),
                @WebInitParam(name="db-class", value = "com.mysql.cj.jdbc.Driver")
        }
        ,loadOnStartup = 7
)
public class Customer extends HttpServlet {

    private Connection connection;
    final static Logger logger = LoggerFactory.getLogger(Customer.class);

    @Override
    public void init() throws ServletException {
        logger.info("Init customer servlet");
        try {
            String user = getServletConfig().getInitParameter("db-user");
            String password = getServletConfig().getInitParameter("db-pw");
            String url = getServletConfig().getInitParameter("db-url");
            String driverClass = getServletConfig().getInitParameter("db-class");

            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection initialization failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");

        if (id != null){
            PrintWriter printWriter = resp.getWriter();
            resp.setContentType("text/html");

            CustomerDTO customer = DBProcess.getCustomer(Integer.parseInt(id), connection);
            if (customer != null){
                printWriter.println(
                        customer.getId()+" : "+customer.getName()+" : "+customer.getEmail()+" : "+customer.getCity()
                );
            }else {
                printWriter.println("Couldn't find the Customer");
            }
        }else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }




    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String city = req.getParameter("city");
        String email = req.getParameter("email");

        if (id != null || name != null || city != null || email != null){
            PrintWriter printWriter = resp.getWriter();
            resp.setContentType("text/html");
            printWriter.println(
                    DBProcess.saveCustomer(new CustomerDTO(Integer.parseInt(id), name, email, city), connection) ? "Customer Saved" : "Customer not Saved"
            );

        }else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String city = req.getParameter("city");
        String email = req.getParameter("email");

        if (id != null || name != null || city != null || email != null){
            PrintWriter printWriter = resp.getWriter();
            resp.setContentType("text/html");
            printWriter.println(
                    DBProcess.updateCustomer(new CustomerDTO(Integer.parseInt(id), name, email, city), connection) ? "Customer Updated" : "Customer not Updated"
            );

        }else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");

        if (id != null){
            PrintWriter printWriter = resp.getWriter();
            resp.setContentType("text/html");
            printWriter.println(
                    DBProcess.deleteCustomer(id, connection) ? "Customer Deleted" : "Customer not Deleted"
            );

        }else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
