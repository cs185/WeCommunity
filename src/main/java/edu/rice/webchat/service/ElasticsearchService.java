//package edu.rice.webchat.service;
//
//import edu.rice.webchat.entity.post.DiscussPost;
//import edu.rice.webchat.repository.DiscussPostRepository;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.springframework.data.domain.*;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.query.Criteria;
//import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
//import org.springframework.data.elasticsearch.core.query.HighlightQuery;
//import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
////import org.springframework.data.elasticsearch.core.;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ElasticsearchService {
//
//    @Autowired
//    private DiscussPostRepository discussRepository;
//
////    @Autowired
//    private ElasticsearchRestTemplate elasticsearchRestTemplate;
//
//    public void saveDiscussPost(DiscussPost post) {
//        discussRepository.save(post);
//    }
//
//    public void deleteDiscussPost(int id) {
//        discussRepository.deleteById(id);
//    }
//
//    public Page<DiscussPost> searchDiscussPost(String keyword, int current, int limit) {
//        Criteria criteria = new Criteria("title").matches(keyword)
//                .or(new Criteria("content").matches(keyword));
//        CriteriaQuery query = new CriteriaQuery(criteria);
//
//        query.addSort(Sort.by(Sort.Order.desc("type")))
//                .addSort(Sort.by(Sort.Order.desc("score")))
//                .addSort(Sort.by(Sort.Order.desc("createTime")))
//                .setPageable(PageRequest.of(current, limit))
//                .setHighlightQuery(new HighlightQuery(
//                        new Highlight(Arrays.asList(
//                                new HighlightField("title"),
//                                new HighlightField("content"))), DiscussPost.class));
//
//        SearchHits<DiscussPost> searchHits = elasticsearchRestTemplate.search(query, DiscussPost.class);
//        List<DiscussPost> discussPosts = searchHits.getSearchHits().stream()
//                .map(hit -> {
//                    DiscussPost post = hit.getContent();
//                    if (hit.getHighlightFields().containsKey("title")) {
//                        post.setTitle(hit.getHighlightFields().get("title").get(0));
//                    }
//                    if (hit.getHighlightFields().containsKey("content")) {
//                        post.setContent(hit.getHighlightFields().get("content").get(0));
//                    }
//                    return post;
//                })
//                .collect(Collectors.toList());
//
//        Pageable pageable = PageRequest.of(current, limit);
//        return new PageImpl<>(discussPosts, pageable, searchHits.getTotalHits());
//    }
//
//}
