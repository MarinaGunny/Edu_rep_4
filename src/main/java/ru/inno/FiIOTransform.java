package ru.inno;

import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
@Loggable
@NoArgsConstructor
public class FiIOTransform implements  StrTransrorm {

    @Override
    public List<String> transformStr(List<String> inputStr) {
        List<String> res = new ArrayList<>();

        inputStr.forEach((String str) -> {
            int spacePos = 0;

            for (int i=1;i<=3;i++) {
                spacePos = str.indexOf(" ", spacePos + 1);
                str = str.substring(0, spacePos + 1) + str.substring(spacePos + 1, spacePos + 2).toUpperCase() + str.substring(spacePos + 2);
            }
            res.add(str);
        });
        return res;
    }
}
