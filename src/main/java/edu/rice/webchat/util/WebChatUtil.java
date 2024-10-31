package edu.rice.webchat.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WebChatUtil {
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证的超时时间
     */
    public static int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态的登录凭证超时时间
     */
    public static int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * 实体类型: 帖子
     */
    public static int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型: 评论
     */
    public static int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型: 用户
     */
    public static int ENTITY_TYPE_USER = 3;

    /**
     * 实体类型: 私信
     */
    public static int ENTITY_TYPE_CHAT = 4;

    /**
     * 实体类型: 群组消息
     */
    public static int ENTITY_TYPE_GROUP = 5;

    /**
     * 主题: 评论
     */
    public static final String TOPIC_COMMENT = "comment";

    /**
     * 主题: 点赞
     */
    public static final String TOPIC_LIKE = "like";

    /**
     * 主题: 关注
     */
    public static final String TOPIC_FOLLOW = "follow";

    /**
     * 主题: 发帖
     */
    public static final String TOPIC_PUBLISH = "publish";

    /**
     * 主题: 删帖
     */
    public static final String TOPIC_DELETE = "delete";

    /**
     * 主题: 删帖
     */
    public static final String TOPIC_CHAT = "chat";

    /**
     * 系统用户ID
     */
    public static int SYSTEM_USER_ID = 1;

    /**
     * 权限: 普通用户
     */
    public static String AUTHORITY_USER = "user";

    /**
     * 权限: 管理员
     */
    public static String AUTHORITY_ADMIN = "admin";

    /**
     * 权限: 版主
     */
    public static String AUTHORITY_MODERATOR = "moderator";

    // generate random string
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5 encoding
    // hello -> abc123def456
    // hello + 3e4a8 -> abc123def456abc
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

}
