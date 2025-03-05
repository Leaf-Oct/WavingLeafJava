package cn.leaf;

public class User {
    public String user, password, path;
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

    public User(String user, String password, String path, boolean writable, boolean enable) {
        this.user = user;
        this.password = password;
        this.path = path;
        this.writable = writable;
        this.enable = enable;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public boolean isWritable() {
        return writable;
    }
}
