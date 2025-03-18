package cn.leaf;

import java.nio.file.Paths;
import java.sql.*;

import static cn.leaf.UserDao.DATABASE_NAME;

public class ConfigDao {
    private static ConfigDao dao;
    private Connection conn;

    public boolean init() {
        String dbPath = Paths.get(System.getProperty("user.home"), DATABASE_NAME).toString();
        // 连接 SQLite 数据库（如果文件不存在会自动创建）
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("数据库已连接: " + dbPath);
            // 创建用户信息表
            try (Statement stmt = conn.createStatement()) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS config (" +
                        "id INTEGER PRIMARY KEY, " +
                        "ftp_port INTEGER NOT NULL, " +
                        "sftp_port INTEGER NOT NULL, " +
                        "webdav_port INTEGER NOT NULL)";
                stmt.execute(createTableSQL);
                // 插入初始数据（如果不存在）
                String insertSQL = "INSERT OR IGNORE INTO config (id, ftp_port, sftp_port, webdav_port) VALUES (1, 2121, 2222, 8080)";
                stmt.execute(insertSQL);
            }
        } catch (SQLException e) {
            System.err.println("数据库操作失败: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static ConfigDao getInstance() {
        synchronized (ConfigDao.class) {
            if (dao == null) {
                dao = new ConfigDao();
                dao.init();
            }
        }
        return dao;
    }

    // 读取 ftp_port
    public int getFtpPort() {
        return getPort("ftp_port");
    }

    // 读取 sftp_port
    public int getSftpPort() {
        return getPort("sftp_port");
    }

    // 读取 webdav_port
    public int getWebdavPort() {
        return getPort("webdav_port");
    }

    // 更新 ftp_port
    public boolean updateFtpPort(int ftpPort) {
        return updatePort("ftp_port", ftpPort);
    }

    // 更新 sftp_port
    public boolean updateSftpPort(int sftpPort) {
        return updatePort("sftp_port", sftpPort);
    }

    // 更新 webdav_port
    public boolean updateWebdavPort(int webdavPort) {
        return updatePort("webdav_port", webdavPort);
    }

    // 通用方法：读取指定端口字段的值
    private int getPort(String fieldName) {
        String sql = "SELECT " + fieldName + " FROM config WHERE id = 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(fieldName);
            }
            return 0; // 如果未找到记录
        } catch (SQLException e) {
            System.err.println("读取 " + fieldName + " 失败: " + e.getMessage());
            return 0;
        }
    }

    // 通用方法：更新指定端口字段的值
    private boolean updatePort(String fieldName, int port) {
        String sql = "UPDATE config SET " + fieldName + " = ? WHERE id = 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, port);
            int rows = pstmt.executeUpdate();
            return rows > 0; // 返回是否更新成功
        } catch (SQLException e) {
            System.err.println("更新 " + fieldName + " 失败: " + e.getMessage());
            return false;
        }
    }
}
