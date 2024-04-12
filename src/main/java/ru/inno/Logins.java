package ru.inno;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.Date;

@Entity
@Table(name = "Logins")
@AllArgsConstructor
@NoArgsConstructor
public class Logins {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Getter
    @Setter
    @Column(name = "access_date")
    Date access_date;
    @Getter
    @Setter
    @Column(name = "application")
    String application;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "user_id")
    @Getter
    @Setter
    private Users user_id;

//    public Logins (String name)
//    {
//        this.name = name;
//    }
    public Logins (String application, Users user, Date access_date)
    {
        this.application = application;
        this.user_id = user;
        this.access_date = access_date;
    }
}
