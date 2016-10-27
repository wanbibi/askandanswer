package com.wanzhengchao.service;

import com.wanzhengchao.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 16.10.26.
 */
@Service
public class SearchService {
    private static final String SOLR_URL = "http://127.0.0.1:8983/solr/wenda";
    private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";


    public List<Question> searchQuestion(String keyword, int offfset, int count,String hlPre, String hlPos) throws IOException, SolrServerException {
        ArrayList<Question> questionList = new ArrayList<>();
        SolrQuery solrQuery = new SolrQuery(keyword);
        solrQuery.setRows(count);
        solrQuery.setStart(offfset);
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre(hlPre);
            solrQuery.setHighlightSimplePost(hlPos);
            solrQuery.set("hl.fl",QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);
            QueryResponse response = client.query(solrQuery);
            for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
                Question q = new Question();
                q.setId(Integer.parseInt(entry.getKey()));
                if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
                    List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                    if (contentList.size() > 0) {
                        q.setContent(contentList.get(0));
                    }
                }
                if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
                    List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                    if (titleList.size() > 0) {
                        q.setTitle(titleList.get(0));
                    }
                }
            questionList.add(q);
        }
        return questionList;
    }

    public boolean indexQuestion(int qid, String title, String content) throws Exception {
        SolrInputDocument doc =  new SolrInputDocument();
        doc.setField("id", qid);
        doc.setField(QUESTION_TITLE_FIELD, title);
        doc.setField("QUESTION_CONTENT_FIELD", content);
        UpdateResponse response = client.add(doc, 1000);
        return response != null && response.getStatus() == 0;
    }
}
