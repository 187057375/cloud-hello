package org.zjt;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * DESC
 *
 * @author
 * @create 2017-04-18 下午2:28
 **/
public class Doc {
    private Integer id ;
    private String title ;
    private String content ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Doc() {
    }

    public IndexResponse getDocContent(TransportClient client){
        try {
            IndexResponse response = client.prepareIndex("database", "user", "1")
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("title", this.getTitle())
                            .field("id", this.getId())
                            .field("content", this.getContent())
                            .endObject()
                    )
                    .get();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

}
