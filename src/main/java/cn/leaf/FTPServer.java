package cn.leaf;

import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FTPServer {

    public static void main() {
//        初始化ftp server
        var server_factory = new FtpServerFactory();
        var user_manager=new PropertiesUserManagerFactory().createUserManager();
        server_factory.setUserManager(user_manager);
        var listener_factory = new ListenerFactory();
        Config config= null;
        try {
            config = yaml.load(new InputStreamReader(new FileInputStream("config.yaml")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//        加载用户数据
        listener_factory.setPort(config.port);
        var write_permission=new ArrayList<Authority>();
        write_permission.add(new WritePermission());
        for(var u: config.users){
            var user=new BaseUser();
            user.setName(u.user);
            user.setPassword(u.password);
            user.setHomeDirectory(u.path);
            user.setAuthorities(u.writable?write_permission: new ArrayList<>());
            try {
                user_manager.save(user);
            } catch (FtpException e) {
                throw new RuntimeException(e);
            }
        }
//        开始监听
        server_factory.addListener("default", listener_factory.createListener());
        var server = server_factory.createServer();
        try {
            server.start();
            System.out.println("FTP服务器已启动，监听端口..."+config.port);
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }
}