package ru.inno;

import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(3)
@NoArgsConstructor
public class ApplTransform implements StrTransrorm {
    @Override
    public List<String> transformStr(List<String> inputStr) {
        List<String> res = new ArrayList<>();

        inputStr.forEach((String str) -> {
            int spacePos = 0;
            String appl;
            //Приложение идет последним
            spacePos = str.lastIndexOf(" ");
            appl = str.substring(spacePos+1);
            if (!(appl.equals("web") || appl.equals("mobile"))){
                str = str.substring(0, spacePos+1)+"other:"+appl;
            }
            res.add(str);
        });
        return res;
    }
}
