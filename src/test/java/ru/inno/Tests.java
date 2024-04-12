package ru.inno;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
public class Tests {
    @Value("${spring.application.log}")
    String log;
    @Value("${spring.application.path}")
    String path;

    @Autowired
    private Repo repo;

    @Autowired
    private SimpleWriter writer;

    @Autowired
    SimpleReader reader;

    @Autowired
    FiIOTransform ft;

    @Autowired
    DateTransform dt;

    @Autowired
    ApplTransform at;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(repo);
        Assertions.assertNotNull(writer);
        Assertions.assertNotNull(reader);
        Assertions.assertNotNull(ft);
        Assertions.assertNotNull(dt);
        Assertions.assertNotNull(at);
    }

    //Проверка промежуточных компонент
    @Test
    @DisplayName("Проверка промежуточных компонент")
    void TranformTest() {
        List<String> lst = new ArrayList<>();
        List<String> lstEtalon = new ArrayList<>();

        lst.add("r567 ivanov Ivan petrovich 2024-04-23 00:23:34 web");
        lst.add("st456 Sergeeva tanya V 2024-04-23 00:23:34 ozon");
        lst.add("st456 Sergeeva tanya V  ozon");

        //FIO
        lstEtalon.add("r567 Ivanov Ivan Petrovich 2024-04-23 00:23:34 web");
        lstEtalon.add("st456 Sergeeva Tanya V 2024-04-23 00:23:34 ozon");
        lstEtalon.add("st456 Sergeeva Tanya V  ozon");

        List<String> lstres = ft.transformStr(lst);
        Assertions.assertIterableEquals(lstEtalon, lstres);
        //Date. Проверяем независимо
        lstEtalon.clear();
        lstEtalon.add("r567 ivanov Ivan petrovich 2024-04-23 00:23:34 web");
        lstEtalon.add("st456 Sergeeva tanya V 2024-04-23 00:23:34 ozon");

        lstres = dt.transformStr(lst);
        Assertions.assertIterableEquals(lstEtalon, lstres);
        //application
        lstEtalon.clear();
        lstEtalon.add("r567 ivanov Ivan petrovich 2024-04-23 00:23:34 web");
        lstEtalon.add("st456 Sergeeva tanya V 2024-04-23 00:23:34 other:ozon");
        lstEtalon.add("st456 Sergeeva tanya V  other:ozon");

        lstres = at.transformStr(lst);
        Assertions.assertIterableEquals(lstEtalon, lstres);

    }

    //Проверка чтения
    @Test
    @DisplayName("Проверка чтения")
    void ReaderTest() {
        List<String> lstEtalon = new ArrayList<>();

        lstEtalon.add("vasya ivanov Ivan ivanovich 2024-03-23 20:00:03 shop");
        lstEtalon.add("xxx34 Lee Marina Vl ... mobile");
        lstEtalon.add("r55 Petrov Vladimir fedorovich 2024-02-22 00:01:05 web");

        Assertions.assertIterableEquals(lstEtalon, reader.readStr());

    }

    //Проверка записи
    @Test
    @DisplayName("Проверка записи")
    void WriterTest() throws ParseException {
        List<String> lstEtalon = new ArrayList<>();
        Users usr = new Users("vasya", "ivanov Ivan ivanovich");
        HashSet<Logins> logs = new HashSet<>();

        lstEtalon.add("vasya ivanov Ivan ivanovich 2024-03-23 20:00:03 shop");
        logs.add(new Logins("shop", usr, new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse("2024-03-23 20:00:03")));
        usr.logs = logs;

        repo.deleteAll();
        writer.writeStr(lstEtalon);

        Assertions.assertTrue(repo.existsByUsername("vasya"));
        Users actualUsr = repo.findByUsername("vasya");
        Assertions.assertEquals(usr.fio, actualUsr.fio);
    }
    //Всё сразу
    @Test
    @DisplayName("Проверка всего")
    void AllTest(){
        List<String> lst;

        // В файле 3 строки
//        vasya ivanov Ivan ivanovich 2024-03-23 20:00:03 shop
//        xxx34 Lee Marina Vl ... mobile
//        r55 Petrov Vladimir fedorovich 2024-02-22 00:01:05 web

        repo.deleteAll();
        lst = reader.readStr();
        lst = at.transformStr(dt.transformStr(ft.transformStr(lst)));
        writer.writeStr(lst);

        //Одну запись выкинули, т.к. нет даты
        Assertions.assertTrue(repo.existsByUsername("vasya"));
        Assertions.assertTrue(repo.existsByUsername("r55"));

    }
}
