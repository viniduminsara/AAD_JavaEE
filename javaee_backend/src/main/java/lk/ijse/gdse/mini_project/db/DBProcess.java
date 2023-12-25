package lk.ijse.gdse.mini_project.db;

import lk.ijse.gdse.mini_project.dto.CustomerDTO;
import lk.ijse.gdse.mini_project.dto.ItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBProcess{

    //customer
    private static final String GET_QUERY = "SELECT * FROM customer WHERE id = ?";
    private static final String SAVE_QUERY = "INSERT INTO customer(id,name,email,city) VALUES (?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE customer SET name = ?, email = ?, city = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM customer WHERE id = ?";

    //item
    private static final String SAVE_DATA = "INSERT INTO item (id, name, price, qty) VALUES (?,?,?,?)";
    private static final String UPDATE_DATA = "UPDATE item SET name = ?, price = ?, qty = ? WHERE id = ?";
    private static final String DELETE_DATA = "DELETE FROM item WHERE id = ?";

    public static CustomerDTO getCustomer(int id, Connection connection){

        try {
            PreparedStatement ps = connection.prepareStatement(GET_QUERY);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                return new CustomerDTO(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean saveCustomer(CustomerDTO customer, Connection connection){

        try {
            PreparedStatement ps = connection.prepareStatement(SAVE_QUERY);
            ps.setInt(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getCity());

            return ps.executeUpdate() != 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean updateCustomer(CustomerDTO customer, Connection connection){

        try {
            PreparedStatement ps = connection.prepareStatement(UPDATE_QUERY);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getCity());
            ps.setInt(4, customer.getId());

            return ps.executeUpdate() != 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteCustomer(String id, Connection connection){

        try {
            PreparedStatement ps = connection.prepareStatement(DELETE_QUERY);
            ps.setInt(1, Integer.parseInt(id));

            return ps.executeUpdate() != 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean saveItems(ItemDTO itemDTO, Connection connection){
        try {
            PreparedStatement ps = connection.prepareStatement(SAVE_DATA);
                ps.setInt(1, itemDTO.getId());
                ps.setString(2, itemDTO.getName());
                ps.setDouble(3, itemDTO.getPrice());
                ps.setInt(4, itemDTO.getQty());

            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean updateItems(ItemDTO itemDTO, Connection connection)    {
        try {
            PreparedStatement ps = connection.prepareStatement(UPDATE_DATA);
            ps.setString(1, itemDTO.getName());
            ps.setDouble(2, itemDTO.getPrice());
            ps.setInt(3, itemDTO.getQty());
            ps.setInt(4, itemDTO.getId());

            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteItems(ItemDTO itemDTO, Connection connection) {
        try {
            PreparedStatement ps = connection.prepareStatement(DELETE_DATA);
            ps.setInt(1, itemDTO.getId());

            return ps.executeUpdate() != 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
