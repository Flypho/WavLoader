package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mateusz Gołosz
 */
public class DAOImpl {

    String url = "jdbc:mysql://127.0.0.1:3306/music_data";
    String user = "root";
    String password = "root";
    
    
    private void closeConnections(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public DAOImpl(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(DAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int insertPerformer(String performer){
        long id = 0;
        final String insertSQL = "INSERT INTO performers (name) VALUES (?) ON DUPLICATE KEY UPDATE name=?";
        final String selectSQL = "SELECT performer_id from performers where name=?";
        PreparedStatement insertPs = null;
        PreparedStatement selectPs = null;
        Connection conn = null;
        ResultSet generatedKeys = null;
        ResultSet select = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            insertPs = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            insertPs.setString(1, performer);
            insertPs.setString(2, performer);
            insertPs.executeUpdate();
            generatedKeys = insertPs.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            } else {
                selectPs = conn.prepareStatement(selectSQL);
                selectPs.setString(1, performer);
                select = selectPs.executeQuery();
                while (select.next()) {
                    id = select.getInt("performer_id");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("insert performer sqlexception, data: " + performer);
        } finally {
            closeConnections(generatedKeys, insertPs, conn);
            closeConnections(select, insertPs, null);
        }
        return (int)id;
    }
    
    public int insertAlbum(String album, String performer){
        
        long id = 0;
        Connection conn = null;
        PreparedStatement insertPs = null;
        PreparedStatement selectPs = null;
        ResultSet selectPerf = null;
        ResultSet selectAlb = null;
        ResultSet generatedKeys = null;
        try {
            final String selectSQL = "SELECT performer_id from performers WHERE name=?";
            final String insertSQL = "INSERT INTO albums (name, fk_album_performer_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE name=?";
            final String selectSQLalb = "SELECT album_id from albums where name=?";
            conn = DriverManager.getConnection(url, user, password);
            insertPs = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            selectPs = conn.prepareStatement(selectSQL);

            selectPs.setString(1, performer);
            selectPerf = selectPs.executeQuery();
            int album_id = 0;
            while (selectPerf.next()) {
                album_id = selectPerf.getInt("performer_id");
            }
            insertPs.setString(1, album);
            insertPs.setString(3, album);
            if (album_id == 0) {
                insertPs.setNull(2, Types.INTEGER);
            } else {
                insertPs.setInt(2, album_id);
            }
            insertPs.executeUpdate();
            generatedKeys = insertPs.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            } else {
                selectPs = conn.prepareStatement(selectSQLalb);
                selectPs.setString(1, album);
                selectAlb = selectPs.executeQuery();
                while (selectAlb.next()) {
                    id = selectAlb.getInt("album_id");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("insert album sqlexception, data: " + performer + " " + album);
        } finally {
            closeConnections(selectAlb, insertPs, null);
            closeConnections(selectPerf, selectPs, conn);
            closeConnections(generatedKeys, null, null);
        }
        return (int)id;
    }
    
    public int insertGenre(String tag) {
        long id = 0;
        final String insertSQL = "INSERT into genres (tag) values (?) ON DUPLICATE KEY UPDATE tag=?;";
        final String selectSQL = "SELECT tag_id from genres where tag=?";
        PreparedStatement insertPs = null;
        PreparedStatement selectPs = null;
        Connection conn = null;
        ResultSet generatedKeys = null;
        ResultSet select = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            insertPs = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            insertPs.setString(1, tag);
            insertPs.setString(2, tag);
            insertPs.executeUpdate();
            generatedKeys = insertPs.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            } else {
                selectPs = conn.prepareStatement(selectSQL);
                selectPs.setString(1, tag);
                select = selectPs.executeQuery();
                while (select.next()) {
                    id = select.getInt("tag_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("insert tag sqlexception, data: " + tag);
        } finally {
            closeConnections(generatedKeys, insertPs, conn);
            closeConnections(select, selectPs, null);
        }
        return (int)id;
    }
    
    public int insertTrack(String name, String path, int length, String album, String performer, String... tags){
        long id = 0;
        final String insertSQL = "INSERT INTO tracks (name, path, length, fk_album_id, fk_performer_id, fk_tag1, fk_tag2, fk_tag3) values (?,?,?,?,?,?,?,?)ON DUPLICATE KEY UPDATE path=?;";
        final String selectSQL = "SELECT track_id from tracks where path=?";
        Connection conn = null;
        PreparedStatement insertPs = null;
        PreparedStatement selectPs = null;
        ResultSet generatedKeys = null;
        ResultSet select = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            insertPs = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            insertPs.setString(1, name);
            insertPs.setString(2, path);
            insertPs.setInt(3, length);
            int performerDB = insertPerformer(performer);
            insertPs.setInt(4, insertAlbum(album, performer));
            insertPs.setInt(5, performerDB);
            int i;
            /*for (i = 0; i < 3; i++) {
                if (tags.length >= i){
                    insertPs.setInt(i+6, insertGenre(tags[i]));
                } else {
                    insertPs.setNull(i+6, Types.VARCHAR);
                }
            }*/
            insertPs.setInt(6, insertGenre(tags[0]));
            insertPs.setInt(7, insertGenre(tags[1]));
            insertPs.setInt(8, insertGenre(tags[2]));
            insertPs.setString(9, path);
            insertPs.executeUpdate();
            generatedKeys = insertPs.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            } else {
                selectPs = conn.prepareStatement(selectSQL);
                selectPs.setString(1, path);
                select = selectPs.executeQuery();
                while (select.next()) {
                    id = select.getInt("track_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("insert track sqlexception, data: " + name + " " + path + " " + length + " " + album + " " + performer + " " + tags.toString());
        } finally {
            closeConnections(generatedKeys, insertPs, conn);
            closeConnections(select, selectPs, null);
        }
       
        return (int)id;
    }
}
