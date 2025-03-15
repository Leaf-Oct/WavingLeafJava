package cn.leaf;

public class User {
    public String user, password, home;
    public boolean writable, enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public User(String user, String password, String home, boolean writable, boolean enable) {
        this.user = user;
        this.password = password;
        this.home = home;
        this.writable = writable;
        this.enable = enable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean isWritable() {
        return writable;
    }

    @Override
    public String toString() {
        return "User{" +
                "user='" + user + '\'' +
                ", home='" + home + '\'' +
                ", writable=" + writable +
                ", enable=" + enable +
                '}';
    }
}
