package ru.inno;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
@NoArgsConstructor
public class DateTransform implements  StrTransrorm {
    @Value("${spring.application.log}")
    String logfile;

    @Override
    public List<String> transformStr(List<String> inputStr) {
        List<String> res = new ArrayList<>();

        inputStr.forEach((String str) -> {
            //Делаем допущение, что дата в таком формате
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDt;
            try
            {
                strDt = str.substring(str.lastIndexOf(" ")-19, str.lastIndexOf(" "));
                format.parse(strDt);
                res.add(str);}
            catch (ParseException e){
                //Дата не спарсилась, выводим в лог
                FileWriter fw;
                try {
                    fw = new FileWriter(logfile);
                    fw.write(str);
                    fw.close();
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
            }
        });
        return res;
    }
}
