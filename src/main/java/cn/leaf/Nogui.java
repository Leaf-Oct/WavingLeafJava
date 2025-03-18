package cn.leaf;

import java.io.File;
import java.util.Scanner;

public class Nogui {

    private static final Scanner IN=new Scanner(System.in);
    private static final String I="1", II="2", III="3", IV="4", V="5", TRUE="true", FALSE="false", YES="yes", NO="no";
    private static FTPServer ftp_server=new FTPServer();
    private static UserDao dao=UserDao.getInstance();

    public static void main(){
//        1) 编辑用户
//            1) 列出用户
//            2) 新增用户
//            3) 编辑用户
//            4) 删除用户
//            5) 返回
//        2) 操作程序
//            1) FTP
//              运行中/停止
//                1) 启动/停止
//                2) 修改端口
//                3) 返回
//            2) SFTP
//              运行中/停止
//                1) 启动/停止
//                2) 修改端口
//                3) 返回
//            3) WEBDAV
//              运行中/停止
//                1) 启动/停止
//                2) 修改端口
//                3) 返回
//            4) 返回
//        3) 结束程序
        var nogui_status=true;
        while (nogui_status){
            System.out.println("请选择操作 (输入序号数字): ");
            System.out.println("1) 操作用户");
            System.out.println("2) 操作功能");
            System.out.println("3) 结束程序");
            var operation=IN.nextLine();
            switch (operation){
                case I:
                    editUsers();
                    break;
                case II:
                    editPrograms();
                    break;
                case III:
                    nogui_status=false;
                    break;
                default:
                    System.out.println("未知输入: " + operation+" 请重新输入操作");
            }
        }
        exit();
    }
    private static void editUsers(){
        var edit_user=true;
        while (edit_user){
            System.out.println("请选择要对用户进行的操作: ");
            System.out.println("1) 列出用户");
            System.out.println("2) 新增用户");
            System.out.println("3) 编辑用户");
            System.out.println("4) 删除用户");
            System.out.println("5) 返回");
            var operation=IN.nextLine();
            switch (operation){
                case I:
                    for (var u:dao.getAllUsers()){
                        System.out.println(u);
                    }
                    break;
                case II:
                    addUser();
                    break;
                case III:
                    editUser();
                    break;
                case IV:
                    deleteUser();
                    break;
                case V:
                    edit_user=false;
                    break;
                default:
                    System.out.println("未知输入: " + operation+" 请重新输入操作");
            }
        }
    }

    private static void addUser(){
        System.out.println("请输入用户名：");
        var user_name=IN.nextLine();
        System.out.println("请输入用户主目录：");
        var home=IN.nextLine();
        var home_dir=new File(home);
        if (!home_dir.isDirectory()){
            System.err.println("目录 "+ home_dir+" 不存在");
            return;
        }
        System.out.println("请授权用户是否可写：(输入1或true或yes表可写)");
        var w=IN.nextLine().toLowerCase();
        var writable=false;
        if (w.equals(I)||w.equals(TRUE)||w.equals(YES)){
            writable=true;
        }
        System.out.println("最后请输入用户的密码");
        var pwd=IN.nextLine();
        var user=new User(user_name, pwd, home, writable, true);
        if (dao.addUser(user)){
            System.out.println("添加用户"+user_name+"成功");
        }
    }

    private static void editUser(){

    }

    private static void deleteUser(){

    }

    private static void editPrograms(){
        System.out.println("请选择要对程序进行的操作: ");
        var operation=IN.nextLine();

    }

    private static void exit(){
        IN.close();
        if (ftp_server.isRunning()){
            ftp_server.stop();
        }

    }
}
