package lk.ijse.gdse.mini_project.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.mini_project.db.DBProcess;
import lk.ijse.gdse.mini_project.dto.ItemDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "item", urlPatterns = "/item",
        initParams = {
                @WebInitParam(name = "db-user", value = "root"),
                @WebInitParam(name = "db-pw", value = "1234"),
                @WebInitParam(name = "db-url", value = "jdbc:mysql://localhost:3306/miniProject"),
                @WebInitParam(name="db-class", value = "com.mysql.cj.jdbc.Driver")
        }
        , loadOnStartup = 7)
public class Item extends HttpServlet {

    private Connection connection;

    @Override
    public void init() throws ServletException {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")) {
            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            if(DBProcess.saveItems(itemDTO, connection)){
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")) {
            Jsonb jsonb = JsonbBuilder.create();
            List<ItemDTO> itemDTOList = jsonb.fromJson(
                    req.getReader(),
                    new ArrayList<ItemDTO>() {}.getClass().getGenericSuperclass()
            );

            PrintWriter writer = resp.getWriter();
            resp.setContentType("text/html");

            writer.println(DBProcess.updateItems(itemDTOList, connection) ? "Item Updated" : "Item not Updated");
        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")) {
            Jsonb jsonb = JsonbBuilder.create();
            List<ItemDTO> itemDTOList = jsonb.fromJson(
                    req.getReader(),
                    new ArrayList<ItemDTO>() {}.getClass().getGenericSuperclass()
            );

            PrintWriter writer = resp.getWriter();
            resp.setContentType("text/html");

            writer.println(DBProcess.deleteItems(itemDTOList, connection) ? "Item Deleted" : "Item not Deleted");
        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
