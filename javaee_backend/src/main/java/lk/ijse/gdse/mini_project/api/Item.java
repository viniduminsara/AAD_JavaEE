package lk.ijse.gdse.mini_project.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.mini_project.db.DBProcess;
import lk.ijse.gdse.mini_project.dto.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "item", urlPatterns = "/item", loadOnStartup = 7)
public class Item extends HttpServlet {

    private Connection connection;
    final static Logger logger = LoggerFactory.getLogger(Item.class);

    @Override
    public void init() throws ServletException {
        try {
            logger.info("Init Item servlet");

            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/miniproject");
            this.connection = pool.getConnection();

        } catch (NamingException | SQLException e) {
            throw new ServletException("Database connection initialization failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ItemDTO> items = DBProcess.getItems(connection);

        if (!items.isEmpty()) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            try (PrintWriter out = resp.getWriter()) {
                Jsonb jsonb = JsonbBuilder.create();
                String jsonItems = jsonb.toJson(items);
                out.write(jsonItems);
                logger.info("Sending items data");
            } catch (Exception e) {
                // Handle exceptions appropriately
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            logger.warn("Not Found Requested item");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")) {
            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            if(DBProcess.saveItem(itemDTO, connection)){
                resp.setStatus(HttpServletResponse.SC_CREATED);
                logger.info("Item Save Successful");
            }else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.info("Failed to Save");
            }

        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")) {
            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            if(DBProcess.updateItem(itemDTO, connection)){
                resp.setStatus(HttpServletResponse.SC_OK);
                logger.info("Item Update Successful");
            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                logger.warn("Not Found the Requested item");
            }
        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            logger.info("Failed to Update");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")) {
            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            if(DBProcess.deleteItem(itemDTO, connection)){
                resp.setStatus(HttpServletResponse.SC_OK);
                logger.info("Item Delete Successful");
            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                logger.warn("Not Found the Requested item");
            }
        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            logger.info("Failed to Delete");
        }
    }
}
