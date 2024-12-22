//package edu.rice.wecommunity.service;
//
//import edu.rice.wecommunity.dao.elasticsearch.DiscussPostDAO;
//import edu.rice.wecommunity.dao.elasticsearch.DiscussPostRepository;
//import edu.rice.wecommunity.entity.DiscussPost;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.SearchHitsIterator;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class ElasticsearchService {
//
//    @Autowired
//    private DiscussPostDAO discussPostDAO;
//
//    @Autowired
//    private ElasticsearchRestTemplate elasticTemplate;
//
//    public void saveDiscussPost(DiscussPost post) {
//        discussPostDAO.save(post);
//    }
//
//    public void deleteDiscussPost(int id) {
//        discussPostDAO.deleteById(id);
//    }
//
//    public Page<DiscussPost> searchDiscussPost(String keyword, int current, int limit) {
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
//                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
//                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
//                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
//                .withPageable(PageRequest.of(current, limit))
//                .withHighlightFields(
//                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
//                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
//                ).build();
//
//        SearchHits<DiscussPost> searchHits = elasticTemplate.search(searchQuery, DiscussPost.class);
//        List<DiscussPost> discussPosts = new ArrayList<>();
//
//        // Process search hits and handle highlighting
//        for (var searchHit : searchHits) {
//            DiscussPost post = searchHit.getContent();
//
//            // Handling highlights for title and content
//            if (searchHit.getHighlightFields().containsKey("title")) {
//                post.setTitle(searchHit.getHighlightFields().get("title").get(0));
//            }
//            if (searchHit.getHighlightFields().containsKey("content")) {
//                post.setContent(searchHit.getHighlightFields().get("content").get(0));
//            }
//
//            discussPosts.add(post);
//        }
//
//        Pageable pageable = PageRequest.of(current, limit);
//        return new PageImpl<>(discussPosts, pageable, searchHits.getTotalHits());
//    }
//
//}
