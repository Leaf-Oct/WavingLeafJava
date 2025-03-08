package cn.leaf;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    private final ListView<String> user_list = new ListView<>();
    private final Map<String, User> users = new HashMap<>();
    private final TextField username_field = new TextField();
    private final PasswordField password_field = new PasswordField();
    private final TextField directory_field = new TextField();
    private final CheckBox enable_checkbox = new CheckBox("启用");
    private final CheckBox writable_checkbox=new CheckBox("可写");

    @Override
    public void start(Stage primaryStage) {

        // 左侧用户列表（30%宽度）
        VBox.setVgrow(user_list, Priority.ALWAYS);
        VBox leftPane = new VBox(10, user_list);

        // 右侧表单区域
        GridPane formPane = new GridPane();
        formPane.setVgap(10);
        formPane.setHgap(10);
        formPane.setPadding(new Insets(10));

        // 目录选择组件
        HBox dirBox = new HBox(5);
        Button browseButton = new Button("浏览...");
        dirBox.getChildren().addAll(directory_field, browseButton);
        directory_field.setPrefWidth(200);

        // 表单内容
        formPane.addRow(0, new Label("用户名:"), username_field);
        formPane.addRow(1, new Label("密码:"), password_field);
        formPane.addRow(2, new Label("主目录:"), dirBox);
        formPane.addRow(3, enable_checkbox, writable_checkbox);

        // 操作按钮
        HBox buttonBox = new HBox(10);
        Button saveButton = new Button("保存");
        Button deleteButton = new Button("删除");
        buttonBox.getChildren().addAll(saveButton, deleteButton);

        VBox rightPane = new VBox(20, formPane, buttonBox);
        rightPane.setPadding(new Insets(10));


        // 事件处理
        setupEventHandlers(browseButton, saveButton, deleteButton);
        setupListSelection();

        // 窗口设置
        // 主布局
        SplitPane root = new SplitPane(leftPane, rightPane);
        root.setPadding(new Insets(10));
        root.setDividerPositions(0.3);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("用户管理工具");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupEventHandlers(Button browseButton, Button saveButton, Button deleteButton) {
        // 目录选择
        browseButton.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            File selected = chooser.showDialog(null);
            if (selected != null) {
                directory_field.setText(selected.getAbsolutePath());
            }
        });

        // 保存用户
        saveButton.setOnAction(e -> {
            var user = new User(username_field.getText(),
                    password_field.getText(),
                    directory_field.getText(),
                    writable_checkbox.isSelected(),
                    enable_checkbox.isSelected());
            users.put(user.user, user);
            refreshUserList();
        });

        // 删除用户
        deleteButton.setOnAction(e -> {
            String selected = user_list.getSelectionModel().getSelectedItem();
            if (selected != null) {
                users.remove(selected);
                refreshUserList();
            }
        });
    }

    private void setupListSelection() {
        user_list.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                User user = users.get(newVal);
                username_field.setText(user.user);
                password_field.setText(user.password);
                directory_field.setText(user.home);
                enable_checkbox.setSelected(user.enable);
                writable_checkbox.setSelected(user.writable);
            }
        });
    }

    private void refreshUserList() {
        user_list.getItems().setAll(users.keySet());
    }


    public static void main(String[] args) {
        if (args.length > 0) {
            var firstArg = args[0];
            if ("--help".equals(firstArg)) {
                System.out.println("命令行选项：");
                System.out.println("  --help   显示本帮助信息");
                System.out.println("  --nogui  以控制台模式运行程序");
                System.out.println("\n示例：");
                System.out.println("  java Main --nogui");
            } else if ("--nogui".equals(firstArg)) {
//                TODO 以命令行运行
                Nogui.main();
            } else {
                System.out.println("未知参数 '" + firstArg + "'，请输入 --help 查看帮助");
            }
        }
        else {
            launch(args);
        }
    }
}