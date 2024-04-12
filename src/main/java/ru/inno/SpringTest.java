package ru.inno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringTest {

    @Autowired
    private StrReader dataReader;
    @Autowired
    private List<StrTransrorm> dataTransform;
    @Autowired
    private StrWriter dataWriter;

    public void doSomething() {
        List<String> res;

        res = dataReader.readStr();
        System.out.println(res);
        for (StrTransrorm st: dataTransform) {
            res = st.transformStr(res);
        }
        System.out.println(res);
        dataWriter.writeStr(res);

    }
}
