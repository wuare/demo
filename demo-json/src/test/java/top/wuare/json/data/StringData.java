package top.wuare.json.data;

public class StringData {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StringData{" +
                "name='" + name + '\'' +
                '}';
    }
}
