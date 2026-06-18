package by.psu.db;

import by.psu.model.Excursion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcHelper {
    private final Connection conn;

    public JdbcHelper(Connection conn) {
        this.conn = conn;
    }

    public Excursion findExcursionById(Integer id) throws SQLException {
        var statement = conn.prepareStatement("SELECT * FROM public.excursion WHERE id = ?");
        statement.setInt(1, id);
        var result = statement.executeQuery();
        result.next();
        return new Excursion(
                result.getInt("id"),
                result.getString("name"),
                result.getBigDecimal("price"),
                result.getObject("from", LocalDate.class),
                result.getObject("to", LocalDate.class),
                result.getString("guide_name"),
                result.getString("excursion_type"),
                result.getBoolean("lunch_included"));
    }

    public List<Excursion> findAllExcursions() throws SQLException {
        var statement = conn.prepareStatement("SELECT * FROM public.excursion");
        var result = statement.executeQuery();
        var list = new ArrayList<Excursion>();
        while (result.next()) {
            list.add(new Excursion(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getBigDecimal("price"),
                    result.getObject("from", LocalDate.class),
                    result.getObject("to", LocalDate.class),
                    result.getString("guide_name"),
                    result.getString("excursion_type"),
                    result.getBoolean("lunch_included")));
        }
        return list;
    }

    public void saveExcursion(Excursion excursion) throws SQLException {
        if (excursion.getId() == null) {
            Integer id = insertExcursion(excursion);
            excursion.setId(id);
        } else {
            updateExcursion(excursion);
        }
    }

    private Integer insertExcursion(Excursion excursion) throws SQLException {
        var statement = conn.prepareStatement("""
                insert into public.excursion (name, price, "from", "to", guide_name, excursion_type, lunch_included)
                values (?, ?, ?, ?, ?, ?, ?)""", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, excursion.getName());
        statement.setBigDecimal(2, excursion.getPrice());
        statement.setObject(3, excursion.getFrom());
        statement.setObject(4, excursion.getTo());
        statement.setString(5, excursion.getGuideName());
        statement.setString(6, excursion.getExcursionType());
        statement.setBoolean(7, excursion.isLunchIncluded());
        statement.execute();
        var keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getInt(1);
    }

    private void updateExcursion(Excursion excursion) throws SQLException {
        var statement = conn.prepareStatement("""
                update public.excursion set 
                                            name = ?,
                                            price = ?,
                                            "from" = ?,
                                            "to" = ?,
                                            guide_name = ?,
                                            excursion_type = ?,
                                            lunch_included = ?
                where id = ?""");
        statement.setString(1, excursion.getName());
        statement.setBigDecimal(2, excursion.getPrice());
        statement.setObject(3, excursion.getFrom());
        statement.setObject(4, excursion.getTo());
        statement.setString(5, excursion.getGuideName());
        statement.setString(6, excursion.getExcursionType());
        statement.setBoolean(7, excursion.isLunchIncluded());
        statement.setInt(8, excursion.getId());
        statement.executeUpdate();
    }
}