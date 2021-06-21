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
    private UserIdCardTest idCard;

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

    public UserIdCardTest getIdCard() {
        return idCard;
    }

    public void setIdCard(UserIdCardTest idCard) {
        this.idCard = idCard;
    }

    @Override
    public String toString() {
        return "UserTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idCard=" + idCard +
                '}';
    }
}
