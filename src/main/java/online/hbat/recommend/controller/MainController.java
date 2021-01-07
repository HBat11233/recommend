package online.hbat.recommend.controller;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import online.hbat.recommend.DO.MoviesDO;
import online.hbat.recommend.DO.RatingDO;
import online.hbat.recommend.DO.RatingsDO;
import online.hbat.recommend.mongodb.RatingsDao;
import online.hbat.recommend.review.MongoModel;
import online.hbat.recommend.review.Recommender;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
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

    @Resource
    private Recommender recommender;

    @Resource
    private RatingsDao ratingsDao;

    @RequestMapping("/")
    @ResponseBody
    public MoviesDO index(){
        MoviesDO moviesDO = recommender.getMovies(null,10);
        moviesDO.setUserId(ratingsDao.getNewUserId());
        return moviesDO;
    }

    @RequestMapping("/recommend")
    @ResponseBody
    public MoviesDO recommend(@RequestBody RatingsDO ratingsDO){
        recommender.addRatingDOList(ratingsDO.getData());
        return recommender.getMovies(ratingsDO.getData().get(0).getUserId().intValue(),10);
    }
}
