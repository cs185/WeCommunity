package edu.rice.wecommunity.dao.elasticsearch;

import edu.rice.wecommunity.aspect.ServiceLogAspect;
import edu.rice.wecommunity.entity.DiscussPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscussPostDAO {
    @Autowired
    private DiscussPostRepository discussRepository;

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    public void save(DiscussPost post) {
        try {
            discussRepository.save(post);
        }
        catch (RuntimeException e) {
            logger.info(e.getMessage());
        }
    }

    public void saveAll(List<DiscussPost> posts) {
        try {
            discussRepository.saveAll(posts);
        }
        catch (RuntimeException e) {
            logger.info(e.getMessage());
        }
    }

    public void deleteById(int id) {
        try {
            discussRepository.deleteById(id);
        }
        catch (RuntimeException e) {
            logger.info(e.getMessage());
        }
    }

    public void deleteAll() {
        try {
            discussRepository.deleteAll();
        }
        catch (RuntimeException e) {
            logger.info(e.getMessage());
        }
    }
}
