package test;

import com.github.paniclab.domain.Entity;

import java.util.Objects;

public class Customer  implements Entity<Long> {

    private Long id;
    private int age;
    private String name;
    private HaoticEnum haoticEnum;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Entity<Long> getThis() {
        return this;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, name);
    }

    public HaoticEnum getHaoticEnum() {
        return haoticEnum;
    }

    public void setHaoticEnum(HaoticEnum haoticEnum) {
        this.haoticEnum = haoticEnum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.hashCode() != obj.hashCode()) return false;

        if (!(this.getClass().equals(obj.getClass()))) return false;
        Customer other = Customer.class.cast(obj);

        if (!(Objects.equals(this.getId(), other.getId()))) return false;
        if (this.getAge() != other.getAge()) return false;
        if (!(Objects.equals(this.getName(), other.getName()))) return false;
        if (!(Objects.equals(this.getHaoticEnum(), other.getHaoticEnum()))) return false;

        return true;
    }
}
