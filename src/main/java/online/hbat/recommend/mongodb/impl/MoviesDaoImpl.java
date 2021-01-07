package online.hbat.recommend.mongodb.impl;

import online.hbat.recommend.DO.MovieDO;
import online.hbat.recommend.mongodb.MoviesDao;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MoviesDaoImpl implements MoviesDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public MovieDO findMovieByMovieId(Long movieId) {
        Query query = new Query(Criteria.where("movieId").is(movieId));
        return mongoTemplate.findOne(query, MovieDO.class);
    }

    @Override
    public List<MovieDO> findMoviesByMovieId(List<Long> movieIdList) {
        Query query = new Query(Criteria.where("movieId").in(movieIdList));
        return mongoTemplate.find(query, MovieDO.class);
    }

    @Override
    public List<MovieDO> findMoviesBySize(int size) {
        Query query = new BasicQuery("{}").limit(size);
        return mongoTemplate.find(query, MovieDO.class);
    }
}
