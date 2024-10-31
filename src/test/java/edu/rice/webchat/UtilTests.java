package edu.rice.webchat;

//import edu.rice.webchat.dao.DiscussPostMapper;

import edu.rice.webchat.service.GroupService;
import edu.rice.webchat.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebchatApplication.class)
public class UtilTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testTrie() {
        String[] senWords = {"ak47", "heheda", "my man"};
        for (String str : senWords)
            sensitiveFilter.addWord(str);

        String[] testSenWords = {"ak47", "heheda", "my man", "ak48", "hehed", "myman"};
        for (String str : testSenWords)
            System.out.println(sensitiveFilter.checkWord(str));
    }

    @Test
    public void testSensitiveFilter() {
        String[] senWords = {"ak47", "man"};
        for (String str : senWords)
            sensitiveFilter.addWord(str);
        String text = "man! I just got my ak47. M3?";
        System.out.println(sensitiveFilter.filter(text));
    }
}
