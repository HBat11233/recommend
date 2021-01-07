package online.hbat.recommend.controller;

import online.hbat.recommend.DO.MoviesDO;
import online.hbat.recommend.DO.RatingsDO;
import online.hbat.recommend.mongodb.RatingsDao;
import online.hbat.recommend.review.Recommender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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
