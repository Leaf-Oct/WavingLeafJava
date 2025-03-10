package cn.leaf;

import java.util.Scanner;

public class Nogui {

    private static final Scanner IN=new Scanner(System.in);
    private static final String I="1", II="2", III="3", IV="4", V="5";

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
        while (true){
            System.out.println("请选择操作 (输入序号数字): ");
            var operation=IN.nextLine();
            if (operation.equals(I)){

            } else if (operation.equals(II)) {

            } else if (operation.equals(III)){
                exit();
                break;
            } else {
                System.out.println("未知输入: " + operation+" 请重新输入操作");
            }
        }
    }
    private static void editUser(){
        System.out.println("请选择要对用户进行的操作: ");
        var operation=IN.nextLine();

    }

    private static void editProgram(){
        System.out.println("请选择要对程序进行的操作: ");
        var operation=IN.nextLine();

    }

    private static void exit(){
        IN.close();
    }
}
