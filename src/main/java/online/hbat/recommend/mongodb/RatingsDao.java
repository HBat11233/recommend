package online.hbat.recommend.mongodb;

import online.hbat.recommend.DO.RatingDO;

import java.util.List;

public interface RatingsDao {
    void saveRatingDO(RatingDO ratingDO);

    void removeRatingDO(String id);

    void updateRatingDO(RatingDO ratingDO);

    RatingDO findRatingDOById(String id);

    List<RatingDO> findAllUserID();

    RatingDO findRatingDONotInIdList(List<String> ids);
}
