//package edu.rice.wecommunity;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import javax.annotation.PostConstruct;
//
//@SpringBootApplication
//public class CommunityApplication {
//
//    @PostConstruct
//    public void init() {
//        // 解决netty启动冲突问题
//        // see Netty4Utils.setAvailableProcessors()
//        System.setProperty("es.set.netty.runtime.available.processors", "false");
//    }
//
//    public static void main(String[] args) {
////        String version = System.getProperty("java.version");
////        System.out.println("Java Runtime Version: " + version);
//        SpringApplication.run(CommunityApplication.class, args);
//    }
//
//}
