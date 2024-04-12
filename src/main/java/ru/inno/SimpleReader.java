package ru.inno;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component

public class SimpleReader implements StrReader {

    //@Value("${spring.application.path}")
    String path;

    SimpleReader(@Value("${spring.application.path}") String path){this.path = path;};

    @Override
    public List<String> readStr() {
        List<String> res = new ArrayList<>();
        File dir = new File(path);

        for (File f : dir.listFiles()) {
            try {
                Scanner sc = new Scanner(f);
               while (sc.hasNextLine()) {
                   res.add(sc.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }
}
