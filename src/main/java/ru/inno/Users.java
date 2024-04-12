package ru.inno;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    public Users(String username, String fio){
        this.username = username;
        this.fio = fio;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Getter
    @Setter
    @Column(name = "username")
    String username;
    @Getter
    @Setter
    @Column(name = "fio")
    String fio;

    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    Set<Logins> logs;
    @Override
    public String toString()
    {
        return username+ " "+fio;
    }

}
