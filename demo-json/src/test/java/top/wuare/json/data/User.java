package top.wuare.json.data;

public class User {
    private int id;
    private String name;
    private UserAttach attach;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserAttach getAttach() {
        return attach;
    }

    public void setAttach(UserAttach attach) {
        this.attach = attach;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", attach=" + attach +
                '}';
    }
}
