package ru.inno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Component
public class SimpleWriter implements StrWriter {
    @Autowired
    private Repo repo;  //Подставляет мой репозиторий. Если писать JpaRepository, понимает, что пишем в User, но доп.методы не подхватывает

    //SimpleWriter(Repo repo){this.repo=repo;};
    @Override
    public void writeStr(List<String> inputStr) {

        inputStr.forEach(str -> {
                    String usrLogin, strFio = "", strAppl;
                    int spacePos;                    //Позиция пробела
                    Date dt;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

                    spacePos = str.indexOf(" ");
                    usrLogin = str.substring(0, spacePos);
                    strAppl = str.substring(str.lastIndexOf(" "));
                    try {
                        dt=format.parse(str.substring(str.lastIndexOf(" ") - 19, str.lastIndexOf(" ")));
                    } catch (ParseException e) {
                        dt = null;
                    }
                    //Если такой пользователь уже есть, дописываем его логины
                    if (repo.existsByUsername(usrLogin)) {
                        Users user = repo.findByUsername(usrLogin);
                        HashSet<Logins> logs = new HashSet<>();
                        logs.add(new Logins(strAppl, user, dt));
                        user.logs = logs;

                        repo.save(user);
                    } else {
                        //Фамилию выдираем из строки
                        strFio = str.substring(spacePos + 1);
                        spacePos = 0;
                        for (int i = 1; i <= 3; i++) {
                            spacePos = strFio.indexOf(" ", spacePos + 1);
                        }
                        strFio = strFio.substring(0, spacePos);

                        Users user = new Users(usrLogin, strFio);
                        HashSet<Logins> logs = new HashSet<>();
                        logs.add(new Logins(strAppl, user, dt));
                        user.logs = logs;

                        repo.save(user);
                    }
                }
        );
//        repo.save(new Users("Roma-heroma", "Roman Ivanov"));
//        repo.findAllByUsername("Roma-heroma").forEach(System.out::println);
//        if (repo.existsByUsername("Roma-heroma")) System.out.println("YESS!");

    }
}
