package test;

import com.github.paniclab.domain.Entity;

import java.util.*;

public class Customer  implements Entity<Long> {

    private Long id;
    private int age;
    private String name;
    private HaoticEnum haoticEnum;
    private Long[] array;
    private List<String> list = new ArrayList<>();
    private Set<Integer> set;


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

    public HaoticEnum getHaoticEnum() {
        return haoticEnum;
    }

    public void setHaoticEnum(HaoticEnum haoticEnum) {
        this.haoticEnum = haoticEnum;
    }

    public Long[] getArray() {
        return array;
    }

    public void setArray(Long[] array) {
        this.array = array;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<Integer> getSet() {
        return set;
    }

    public void setSet(Set<Integer> set) {
        this.set = set;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, name, haoticEnum, Arrays.deepHashCode(array), list, set);
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
        if (!(Arrays.deepEquals(this.getArray(), other.getArray()))) return false;
        if (!(Objects.equals(this.getList(), other.getList()))) return false;
        if (!Objects.equals(this.getSet(), other.getSet())) return false;

        return true;
    }
}
