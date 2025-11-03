package com.danielpm1982;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "MY_CLIENT")
public class MyClient extends PanacheEntity {
    public String name;
    public String email;
    public List<Long> favoriteRecipeIdList;
    public static List<MyClient> findClientByEmail(String email) {
        return find("email LIKE ?1","%"+email+"%").list();
    }
    public static List<MyClient> findClientByName(String name) {
        return find("name LIKE ?1","%"+name+"%").list();
    }
}
