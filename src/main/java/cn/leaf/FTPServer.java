package cn.leaf;

import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;

public class FTPServer {

    private org.apache.ftpserver.FtpServer server;

    public void init() {
        var user_dao = UserDao.getInstance();
        var config_dao = ConfigDao.getInstance();
//        初始化ftp server
        var server_factory = new FtpServerFactory();
        var user_manager = new PropertiesUserManagerFactory().createUserManager();
        server_factory.setUserManager(user_manager);
        var listener_factory = new ListenerFactory();
        listener_factory.setPort(config_dao.getFtpPort());
        var write_permission = new ArrayList<Authority>();
        write_permission.add(new WritePermission());
        for (var u : user_dao.getAllUsers()) {
            var user = new BaseUser();
            user.setName(u.user);
            user.setPassword(u.password);
            user.setHomeDirectory(u.home);
            user.setAuthorities(u.writable ? write_permission : new ArrayList<>());
            try {
                user_manager.save(user);
            } catch (FtpException e) {
                throw new RuntimeException(e);
            }
        }
//        开始监听
        server_factory.addListener("default", listener_factory.createListener());
        server = server_factory.createServer();

    }

    public void start() {
        try {
            server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (!server.isStopped()) {
            server.stop();
            server = null;
        }
    }

    public boolean isRunning() {
        return server == null ? false : !server.isStopped();
    }
}