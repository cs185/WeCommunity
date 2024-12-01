package edu.rice.wecommunity;

import edu.rice.wecommunity.util.SensitiveFilter;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "We can do gambling prostitution drug and fuck here!";
        text = sensitiveFilter.filter(text);
        System.out.println(text);

        text = "We can do☆gambling☆prostitution☆drug☆and☆fuck☆here!☆";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

}
