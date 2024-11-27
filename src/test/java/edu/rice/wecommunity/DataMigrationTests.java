//package edu.rice.wecommunity;
//
//import com.google.cloud.translate.Translate;
//import com.google.cloud.translate.TranslateOptions;
//import com.google.cloud.translate.Translation;
//import edu.rice.wecommunity.dao.*;
//import edu.rice.wecommunity.dao.elasticsearch.DiscussPostDAO;
//import edu.rice.wecommunity.entity.*;
//import edu.rice.wecommunity.service.ElasticsearchService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.util.HashMap;
//import java.util.List;
//
//import static edu.rice.wecommunity.util.CommunityConstant.ENTITY_TYPE_POST;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@ContextConfiguration(classes = CommunityApplication.class)
//public class DataMigrationTests {
//
//    @Autowired
//    private NoticeMapper noticeMapper;
//
//    @Autowired
//    private DiscussPostMapper discussPostMapper;
//
//    @Autowired
//    private MessageMapper messageMapper;
//
//    @Autowired
//    private CommentMapper commentMapper;
//
//    @Autowired
//    private DiscussPostDAO discussPostDAO;
//
//
//    @Test
//    public void tesPostSearchMigration() {
//        List<DiscussPost> posts = discussPostMapper.selectDiscussPosts(0, 0, 10000);
//        for (DiscussPost post : posts)
//            discussPostDAO.save(post);
//    }
//
//    @Test
//    public void tesNoticeMigration()  {
//        List<Message> mList = messageMapper.findNotices();
//        List<Notice> nList = new ArrayList<>();
//        for (Message msg : mList) {
//            ObjectMapper mapper = new ObjectMapper();
//            // 将JSON字符串解析为HashMap
//
//            HashMap<String, Object> map = null;
//            try {
//                map = mapper.readValue(msg.getContent(), HashMap.class);
//            } catch (IOException e) {
//                continue;
//            }
//
//
//            Notice notice = new Notice();
//            notice.setFromId((Integer) map.get("userId"));
//            notice.setToId(msg.getToId());
//            notice.setTopic(msg.getConversationId());
//            notice.setStatus(msg.getStatus());
//            notice.setEntityType((Integer) map.get("entityType"));
//            notice.setEntityId((Integer) map.get("entityId"));
//            notice.setCreateTime(msg.getCreateTime());
//
//            System.out.println(notice);
//            nList.add(notice);
//        }
//        noticeMapper.batchInsertNoticess(nList);
//    }
//
//    @Test
//    public void testSimpleTranslator() {
//        Translate translate = TranslateOptions.newBuilder()
//                .setApiKey("AIzaSyDWyLq8IYCkIUycW43vfiCsKCRv23_4yaw") // 替换为您的实际API密钥
//                .build()
//                .getService();
//
//        Translation translation = translate.translate("你好世界!");
//        System.out.printf("Translated Text:\n\t%s\n", translation.getTranslatedText());
//    }
//
//    public static boolean containsChinese(String str) {
//        if (str == null || str.isEmpty()) {
//            return false;
//        }
//        // 正则表达式匹配中文字符
//        String regex = "[\u4e00-\u9fa5]";
//        return str.matches(".*" + regex + ".*");
//    }
//
//    @Test
//    public void tesCommentMigration()  {
//        Translate translate = TranslateOptions.newBuilder()
//                .setApiKey("AIzaSyDWyLq8IYCkIUycW43vfiCsKCRv23_4yaw") // 替换为您的实际API密钥
//                .build()
//                .getService();
//
//
//        List<Comment> cList = commentMapper.findAll();
//        for (Comment comment : cList) {
//            String content = comment.getContent();
//            if (!containsChinese(content)) continue;
//
//            comment.setContent(translate.translate(comment.getContent()).getTranslatedText());
//
//        }
//
//        commentMapper.deleteAll();
//        commentMapper.batchInsertComments(cList);
//    }
//
//    @Test
//    public void tesPostMigration()  {
//        Translate translate = TranslateOptions.newBuilder()
//                .setApiKey("AIzaSyDWyLq8IYCkIUycW43vfiCsKCRv23_4yaw") // 替换为您的实际API密钥
//                .build()
//                .getService();
//
//
//        List<DiscussPost> pList = discussPostMapper.findAll();
//        for (DiscussPost post : pList) {
//            if (containsChinese(post.getContent()))
//                post.setContent(translate.translate(post.getContent()).getTranslatedText());
//
//            if (containsChinese(post.getTitle()))
//                post.setTitle(translate.translate(post.getTitle()).getTranslatedText());
//
//        }
//
//        discussPostMapper.deleteAll();
//        discussPostMapper.batchInsertPosts(pList);
//    }
//
//    @Test
//    public void tesMessageMigration()  {
//        Translate translate = TranslateOptions.newBuilder()
//                .setApiKey("AIzaSyDWyLq8IYCkIUycW43vfiCsKCRv23_4yaw") // 替换为您的实际API密钥
//                .build()
//                .getService();
//
//
//        List<Message> cList = messageMapper.findAll();
//        for (Message message : cList) {
//            String content = message.getContent();
//            if (!containsChinese(content)) continue;
//
//            message.setContent(translate.translate(message.getContent()).getTranslatedText());
//
//        }
//
//        messageMapper.deleteAll();
//        messageMapper.batchInsertMessages(cList);
//    }
//}
