package tw.com.chainsea.androidasynctask;

import java.util.List;

/**
 * Story
 * Created by Fleming on 2016/6/9.
 */
public class Story {
    /**
     * date : 20170210
     * stories : [{"images":["http://pic2.zhimg.com/6ecda2cd82710ca9a65be31f1b490f61.jpg"],"type":0,"id":9034429,"ga_prefix":"021015","title":"用这个完美爱情的公式，来看看你的爱情是哪一种模式"},{"images":["http://pic2.zhimg.com/178c8a0f1759c3b45ff70b92baeb1321.jpg"],"type":0,"id":9208718,"ga_prefix":"021014","title":"未来十年，金融行业会被人工智能取代吗？"}]
     */

    private String date;
    private List<StoriesBean> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public static class StoriesBean {
        /**
         * images : ["http://pic2.zhimg.com/6ecda2cd82710ca9a65be31f1b490f61.jpg"]
         * type : 0
         * id : 9034429
         * ga_prefix : 021015
         * title : 用这个完美爱情的公式，来看看你的爱情是哪一种模式
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
