package top.wuare.json.data;

import java.util.Arrays;

public class AllData {
    private String s;
    private int i0;
    private Integer i1;
    private long l0;
    private Long l1;
    private float f0;
    private Float f1;
    private double d0;
    private Double d1;
    private User user;
    private User[] users;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public int getI0() {
        return i0;
    }

    public void setI0(int i0) {
        this.i0 = i0;
    }

    public Integer getI1() {
        return i1;
    }

    public void setI1(Integer i1) {
        this.i1 = i1;
    }

    public long getL0() {
        return l0;
    }

    public void setL0(long l0) {
        this.l0 = l0;
    }

    public Long getL1() {
        return l1;
    }

    public void setL1(Long l1) {
        this.l1 = l1;
    }

    public float getF0() {
        return f0;
    }

    public void setF0(float f0) {
        this.f0 = f0;
    }

    public Float getF1() {
        return f1;
    }

    public void setF1(Float f1) {
        this.f1 = f1;
    }

    public double getD0() {
        return d0;
    }

    public void setD0(double d0) {
        this.d0 = d0;
    }

    public Double getD1() {
        return d1;
    }

    public void setD1(Double d1) {
        this.d1 = d1;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "AllData{" +
                "s='" + s + '\'' +
                ", i0=" + i0 +
                ", i1=" + i1 +
                ", l0=" + l0 +
                ", l1=" + l1 +
                ", f0=" + f0 +
                ", f1=" + f1 +
                ", d0=" + d0 +
                ", d1=" + d1 +
                ", user=" + user +
                ", users=" + Arrays.toString(users) +
                '}';
    }
}
