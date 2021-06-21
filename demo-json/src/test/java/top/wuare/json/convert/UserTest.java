package top.wuare.json.convert;

/**
 * java bean for test convert
 *
 * @author wuare
 * @date 2021/6/18
 */
public class UserTest {
    private int id;
    private String name;

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

    @Override
    public String toString() {
        return "UserTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
