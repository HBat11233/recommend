package online.hbat.recommend.controller;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import online.hbat.recommend.DO.RatingDO;
import online.hbat.recommend.mongodb.RatingsDao;
import online.hbat.recommend.review.MongoModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

//    @Value("${spring.data.mongodb.host}")
//    private String host;
//    @Value("${spring.data.mongodb.port}")
//    private int port;
//    @Value("${spring.data.mongodb.database}")
//    private String database;
//    @Value("${spring.data.mongodb.username}")
//    private String username;
//    @Value("${spring.data.mongodb.password}")
//    private String password;

//    MongoClientURI mongoClientURI = new MongoClientURI("mongodb://dbrw:dbrw@www.seeknows.cn/recommend?authSource=admin");
//    MongoClient mongoClient = new MongoClient(mongoClientURI);
    @Resource
    private MongoModel dataModel;

    @RequestMapping("/test2021")
    @ResponseBody
    public List<String> test2021(@RequestBody String msg) {

        List<String> ans = new ArrayList<>();
        try {
            //准备数据 这里是电影评分数据
//            File file = new File("/home/hbat/github/recommend/src/main/resources/data/ml-latest/ratings.csv");
            //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
//            DataModel dataModel = new FileDataModel(file);
//            DataModel dataModel = new GroupLensDataModel(file);
            //计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            //计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居
            UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);
            //构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于用户的协同过滤推荐
            Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);
            //给用户ID等于5的用户推荐10部电影
            List<RecommendedItem> recommendedItemList = recommender.recommend(5, 10);
            //打印推荐的结果

            ans.add("使用基于用户的协同过滤算法");
            ans.add("为用户5推荐10个商品");

            System.out.println("使用基于用户的协同过滤算法");
            System.out.println("为用户5推荐10个商品");
            for (RecommendedItem recommendedItem : recommendedItemList) {
                ans.add(recommendedItem.toString());
                System.out.println(recommendedItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }
}
