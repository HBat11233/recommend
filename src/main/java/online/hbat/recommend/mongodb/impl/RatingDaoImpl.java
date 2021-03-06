package online.hbat.recommend.mongodb.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import online.hbat.recommend.DO.RatingDO;
import online.hbat.recommend.mongodb.RatingsDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class RatingDaoImpl implements RatingsDao {

    @Resource
    MongoTemplate mongoTemplate;

    Long size = null;
    Long newUserId = null;

    @Override
    public void insertRatingDOList(List<RatingDO> ratingDOList) {
        mongoTemplate.insert(ratingDOList, RatingDO.class);
    }

    @Override
    public void removeRatingDO(String id) {
        mongoTemplate.remove(id);
    }

    @Override
    public void updateRatingDO(RatingDO ratingDO) {
        Query query = new Query(Criteria.where("_id").is(ratingDO.getId()));
        Update update = new Update();
        update.set("userId", ratingDO.getUserId());
        update.set("movieID", ratingDO.getMovieId());
        update.set("rating", ratingDO.getRating());
        update.set("create_at", ratingDO.getCreate_at());
        update.set("delete_at", ratingDO.getDelete_at());
        mongoTemplate.updateFirst(query, update, RatingDO.class);
    }

    @Override
    public RatingDO findRatingDOById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, RatingDO.class);
    }

    @Override
    public List<RatingDO> findAllUserID() {
        Query query = new BasicQuery("{}");
        List<RatingDO> user = mongoTemplate.find(query, RatingDO.class);
        return user;
    }

    @Override
    public List<RatingDO> findSizeUserID(int size, int skip) {
        Query query = new BasicQuery("{}").skip(skip).limit(size);
        List<RatingDO> user = mongoTemplate.find(query, RatingDO.class);
        return user;
    }

    @Override
    public Long getAllUserSize(boolean toDo) {
        if (size == null || toDo) {
            Query query = new BasicQuery("{}");
            size = mongoTemplate.count(query, RatingDO.class);
        }
        return size;
    }

    @Override
    public Long getFindSize() {
        return getAllUserSize(false) / 700;
    }

    @Override
    public RatingDO findRatingDONotInIdList(List<String> ids) {
        if (ids == null)
            ids = new ArrayList<>();
        String json = "{_id:{$nin:" + ids + "}}";
        Query query = new BasicQuery(json);
        RatingDO ratingDO = mongoTemplate.findOne(query, RatingDO.class);
        return ratingDO;
    }

    @Override
    @PostConstruct
    public Long getNewUserId() {
        if (newUserId == null) {
            Query query = new Query();
            query.with(Sort.by(Sort.Direction.DESC, "userId"));
            newUserId = mongoTemplate.findOne(query, RatingDO.class).getUserId() + 1;
        }
        Long temp = newUserId;
        newUserId += 1;
        return temp;
    }
}
