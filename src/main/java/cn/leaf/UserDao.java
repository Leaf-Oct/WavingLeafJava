package cn.leaf;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static UserDao dao=null;
    private Connection conn;
    public static final String DATABASE_NAME="wavingleaf.db";
    public boolean init(){
        String userHome = System.getProperty("user.home");
        String dbPath = Paths.get(userHome, DATABASE_NAME).toString();

        // 连接 SQLite 数据库（如果文件不存在会自动创建）
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("数据库已连接: " + dbPath);
            // 创建用户信息表
            try (Statement stmt = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS users (" +
                        "user TEXT PRIMARY KEY, " + // 用户名（主键）
                        "password TEXT NOT NULL, " + // 密码
                        "home TEXT NOT NULL, " +   // 主目录
                        "writable BOOLEAN NOT NULL, " + // 是否可写
                        "enable BOOLEAN NOT NULL)"; // 是否启用
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.err.println("数据库操作失败: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static UserDao getInstance(){
        if (dao==null){
            synchronized (UserDao.class){
                dao=new UserDao();
            }
        }
        return dao;
    }

    // 新增用户
    public boolean addUser(User user) {
        if (user == null || user.user == null || user.user.isEmpty()) {
            return false;
        }
        String sql = "INSERT INTO users (user, password, home, writable, enable) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.user);
            pstmt.setString(2, user.password);
            pstmt.setString(3, user.home);
            pstmt.setBoolean(4, user.writable);
            pstmt.setBoolean(5, user.enable);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("新增用户失败: " + e.getMessage());
            return false;
        }
    }

    // 更新用户信息
    public boolean updateUser(User user) {
        if (user == null || user.user == null || user.user.isEmpty()) {
            return false;
        }
        String sql = "UPDATE users SET password = ?, home = ?, writable = ?, enable = ? WHERE user = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.password);
            pstmt.setString(2, user.home);
            pstmt.setBoolean(3, user.writable);
            pstmt.setBoolean(4, user.enable);
            pstmt.setString(5, user.user);
            int rows = pstmt.executeUpdate();
            return rows > 0; // 返回是否更新成功
        } catch (SQLException e) {
            System.err.println("更新用户失败: " + e.getMessage());
            return false;
        }
    }

    // 删除用户
    public boolean deleteUser(String user) {
        if (user == null || user.isEmpty()) {
            return false;
        }

        String sql = "DELETE FROM users WHERE user = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            int rows = pstmt.executeUpdate();
            return rows > 0; // 返回是否删除成功
        } catch (SQLException e) {
            System.err.println("删除用户失败: " + e.getMessage());
            return false;
        }
    }

//    获取密码用于判断
    public String getPasswordByUser(String user) {
        if (user == null || user.isEmpty()) {
            return null;
        }

        String sql = "SELECT password FROM users WHERE user = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
            return null;
        } catch (SQLException e) {
            System.err.println("获取密码失败: " + e.getMessage());
            return null;
        }
    }

    // 通过用户名获取 home
    public String getHomeByUser(String user) {
        if (user == null || user.isEmpty()) {
            return null;
        }

        String sql = "SELECT home FROM users WHERE user = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("home");
            }
            return null;
        } catch (SQLException e) {
            System.err.println("获取主目录失败: " + e.getMessage());
            return null;
        }
    }

    // 通过用户名获取 writable
    public Boolean getWritableByUser(String user) {
        if (user == null || user.isEmpty()) {
            return null;
        }

        String sql = "SELECT writable FROM users WHERE user = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("writable");
            }
            return null;
        } catch (SQLException e) {
            System.err.println("获取 writable 失败: " + e.getMessage());
            return null;
        }
    }

    // 通过用户名获取 enable
    public Boolean getEnableByUser(String user) {
        if (user == null || user.isEmpty()) {
            return null;
        }

        String sql = "SELECT enable FROM users WHERE user = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("enable");
            }
            return null;
        } catch (SQLException e) {
            System.err.println("获取 enable 失败: " + e.getMessage());
            return null;
        }
    }

    // 获取所有用户
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("user"),
                        rs.getString("password"),
                        rs.getString("home"),
                        rs.getBoolean("writable"),
                        rs.getBoolean("enable")
                ));
            }
        } catch (SQLException e) {
            System.err.println("获取所有用户失败: " + e.getMessage());
        }
        return users;
    }
}
