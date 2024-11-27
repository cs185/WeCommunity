package edu.rice.wecommunity;

import edu.rice.wecommunity.dao.DiscussPostMapper;
import edu.rice.wecommunity.dao.elasticsearch.DiscussPostDAO;
import edu.rice.wecommunity.dao.elasticsearch.DiscussPostRepository;
import edu.rice.wecommunity.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchTests {

    @Autowired
    private DiscussPostMapper discussMapper;

    @Autowired
    private DiscussPostDAO discussPostDAO;

    @Autowired
    private ElasticsearchRestTemplate elasticTemplate;

    @Test
    public void testInsert() {
        discussPostDAO.save(discussMapper.selectDiscussPostById(241));
        discussPostDAO.save(discussMapper.selectDiscussPostById(242));
        discussPostDAO.save(discussMapper.selectDiscussPostById(243));
    }

    @Test
    public void testInsertList() {
        discussPostDAO.saveAll(discussMapper.selectDiscussPosts(101, 0, 100));
        discussPostDAO.saveAll(discussMapper.selectDiscussPosts(102, 0, 100));
        discussPostDAO.saveAll(discussMapper.selectDiscussPosts(103, 0, 100));
        discussPostDAO.saveAll(discussMapper.selectDiscussPosts(111, 0, 100));
        discussPostDAO.saveAll(discussMapper.selectDiscussPosts(112, 0, 100));
        discussPostDAO.saveAll(discussMapper.selectDiscussPosts(131, 0, 100));
        discussPostDAO.saveAll(discussMapper.selectDiscussPosts(132, 0, 100));
        discussPostDAO.saveAll(discussMapper.selectDiscussPosts(133, 0, 100));
        discussPostDAO.saveAll(discussMapper.selectDiscussPosts(134, 0, 100));
    }

    @Test
    public void testUpdate() {
        DiscussPost post = discussMapper.selectDiscussPostById(231);
        post.setContent("I am new man!");
        discussPostDAO.save(post);
    }

    @Test
    public void testDelete() {
        // discussPostDAO.deleteById(231);
        discussPostDAO.deleteAll();
    }

    @Test
    public void testSearchByRepository() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("Job", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        // elasticTemplate.queryForPage(searchQuery, class, SearchResultMapper)

        SearchHits<DiscussPost> searchHits = elasticTemplate.search(searchQuery, DiscussPost.class);
        System.out.println(searchHits.getTotalHits());
        System.out.println(searchHits.getSearchHits());
        System.out.println(searchHits.getTotalHitsRelation());
        System.out.println(searchHits.getMaxScore());

        // Process search hits and handle highlighting
        for (var searchHit : searchHits) {
            DiscussPost post = searchHit.getContent();

            // Handling highlights for title and content
            if (searchHit.getHighlightFields().containsKey("title")) {
                post.setTitle(searchHit.getHighlightFields().get("title").get(0));
            }
            if (searchHit.getHighlightFields().containsKey("content")) {
                post.setContent(searchHit.getHighlightFields().get("content").get(0));
            }

            System.out.println(post);
        }

    }

//    @Test
//    public void testSearchByTemplate() {
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
//                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
//                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
//                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
//                .withPageable(PageRequest.of(0, 10))
//                .withHighlightFields(
//                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
//                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
//                ).build();
//
//        Page<DiscussPost> page = elasticTemplate.queryForPage(searchQuery, DiscussPost.class, new SearchResultMapper() {
//            @Override
//            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
//                SearchHits hits = response.getHits();
//                if (hits.getTotalHits() <= 0) {
//                    return null;
//                }
//
//                List<DiscussPost> list = new ArrayList<>();
//                for (SearchHit hit : hits) {
//                    DiscussPost post = new DiscussPost();
//
//                    String id = hit.getSourceAsMap().get("id").toString();
//                    post.setId(Integer.valueOf(id));
//
//                    String userId = hit.getSourceAsMap().get("userId").toString();
//                    post.setUserId(Integer.valueOf(userId));
//
//                    String title = hit.getSourceAsMap().get("title").toString();
//                    post.setTitle(title);
//
//                    String content = hit.getSourceAsMap().get("content").toString();
//                    post.setContent(content);
//
//                    String status = hit.getSourceAsMap().get("status").toString();
//                    post.setStatus(Integer.valueOf(status));
//
//                    String createTime = hit.getSourceAsMap().get("createTime").toString();
//                    post.setCreateTime(new Date(Long.valueOf(createTime)));
//
//                    String commentCount = hit.getSourceAsMap().get("commentCount").toString();
//                    post.setCommentCount(Integer.valueOf(commentCount));
//
//                    // 处理高亮显示的结果
//                    HighlightField titleField = hit.getHighlightFields().get("title");
//                    if (titleField != null) {
//                        post.setTitle(titleField.getFragments()[0].toString());
//                    }
//
//                    HighlightField contentField = hit.getHighlightFields().get("content");
//                    if (contentField != null) {
//                        post.setContent(contentField.getFragments()[0].toString());
//                    }
//
//                    list.add(post);
//                }
//
//                return new AggregatedPageImpl(list, pageable,
//                        hits.getTotalHits(), response.getAggregations(), response.getScrollId(), hits.getMaxScore());
//            }
//        });
//
//        System.out.println(page.getTotalElements());
//        System.out.println(page.getTotalPages());
//        System.out.println(page.getNumber());
//        System.out.println(page.getSize());
//        for (DiscussPost post : page) {
//            System.out.println(post);
//        }
//    }

}
