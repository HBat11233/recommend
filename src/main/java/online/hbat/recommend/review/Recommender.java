package online.hbat.recommend.review;

import online.hbat.recommend.DO.MovieDO;
import online.hbat.recommend.DO.MoviesDO;
import online.hbat.recommend.DO.RatingDO;
import online.hbat.recommend.mongodb.MoviesDao;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Recommender {

    @Resource
    private MongoModel dataModel;

    @Resource
    private MoviesDao moviesDao;

    private MoviesDO qMoviesDO;

    org.apache.mahout.cf.taste.recommender.Recommender recommender;

    public void addRatingDOList(List<RatingDO> ratingDOs){
        dataModel.addRatingDOList(ratingDOs);
        init();
    }

    public void init() {
        try {
            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            //计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居
            UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);
            //构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于用户的协同过滤推荐
            recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostConstruct
    private MoviesDO qMovies() {
        if(qMoviesDO == null){
            qMoviesDO = new MoviesDO();
            List<MovieDO> data = moviesDao.findMoviesBySize(10);
            qMoviesDO.setData(data);
        }
        return qMoviesDO;
    }

    public MoviesDO getMovies(Integer userId,int size) {
        MoviesDO moviesDO = new MoviesDO();
        if(userId == null) { ;
            return qMovies();
        }
        try {
            List<RecommendedItem> recommendedItemList = recommender.recommend(userId, size);
            moviesDO.setData(moviesDao.findMoviesByMovieId(recommendedItemList.stream()
                    .map(recommendedItem -> recommendedItem.getItemID()).collect(Collectors.toList())));
            return moviesDO;
        }catch (Exception e){
            e.printStackTrace();
        }
        return moviesDO;
    }
}
