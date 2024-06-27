package edu.rice.webchat.cache;

import edu.rice.webchat.entity.group.AChatGroup;
import org.springframework.stereotype.Component;

@Component
public class GroupCache extends Cache<Integer> {
    @Override
    public AChatGroup delete(Integer key) {
        return (AChatGroup) cache.remove(key);
    }

    @Override
    public AChatGroup get(Integer key) {
        return (AChatGroup) cache.get(key);
    }
}
