package me.codpoe.gank.data.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Codpoe on 2016/9/15.
 */
public class MeizhiBean {

    @SerializedName("error")
    private boolean error;
    /**
     * _id : 57d8982f421aa95bd05015af
     * createdAt : 2016-09-14T08:22:07.587Z
     * desc : 9-14
     * publishedAt : 2016-09-14T11:35:01.991Z
     * source : chrome
     * type : 福利
     * url : http://ww1.sinaimg.cn/large/610dc034jw1f7sszr81ewj20u011hgog.jpg
     * used : true
     * who : daimajia
     */

    @SerializedName("results")
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        @SerializedName("_id")
        private String id;
        @SerializedName("desc")
        private String desc;
        @SerializedName("type")
        private String type;
        @SerializedName("url")
        private String url;
        @SerializedName("who")
        private String who;
        private int height;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }
    }
}
