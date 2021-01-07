package online.hbat.recommend.review;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import online.hbat.recommend.DO.RatingDO;
import online.hbat.recommend.mongodb.RatingsDao;
import org.apache.mahout.cf.taste.common.NoSuchItemException;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class MongoModel implements DataModel {

    @Resource
    private RatingsDao ratingsDao;

    private Date mongoTimestamp;
    private DataModel delegate;
    List<RatingDO> ratingDOList;
    private int skip = 0;

    private int getSkip() {
        skip += ratingsDao.getFindSize();
        skip %= ratingsDao.getAllUserSize(false) - ratingsDao.getFindSize();
        return skip;
    }


    public void download() {
        ratingDOList = ratingsDao.findSizeUserID(ratingsDao.getFindSize().intValue(), getSkip());
    }

    public void addRatingDOList(List<RatingDO> ratingDOs) {
        ratingDOList.addAll(ratingDOs);
//        ratingDOs.stream().forEach(ratingDO -> ratingsDao.saveRatingDO(ratingDO));
        buildModel();
    }


    private void buildModel() {
        mongoTimestamp = new Date(0);
        FastByIDMap<Collection<Preference>> userIDPrefMap = new FastByIDMap<Collection<Preference>>();

        for (RatingDO ratingDO : ratingDOList) {
//            RatingDO ratingDOTemp = ratingsDao.findRatingDOById(ratingDO.getId());
            Collection<Preference> userPrefs = userIDPrefMap.get(ratingDO.getUserId());
            if (userPrefs == null) {
                userPrefs = Lists.newArrayListWithCapacity(2);
                userIDPrefMap.put(ratingDO.getUserId(), userPrefs);
            }
            userPrefs.add(new GenericPreference(ratingDO.getUserId(), ratingDO.getMovieId(), ratingDO.getRating()));
        }
        delegate = new GenericDataModel(GenericDataModel.toDataMap(userIDPrefMap, true));
    }

    @Override
    public LongPrimitiveIterator getUserIDs() throws TasteException {
        return delegate.getUserIDs();
    }

    @Override
    public PreferenceArray getPreferencesFromUser(long id) throws TasteException {
        return delegate.getPreferencesFromUser(id);
    }

    @Override
    public FastIDSet getItemIDsFromUser(long userID) throws TasteException {
        return delegate.getItemIDsFromUser(userID);
    }

    @Override
    public LongPrimitiveIterator getItemIDs() throws TasteException {
        return delegate.getItemIDs();
    }

    @Override
    public PreferenceArray getPreferencesForItem(long itemID) throws TasteException {
        return delegate.getPreferencesForItem(itemID);
    }

    @Override
    public Float getPreferenceValue(long userID, long itemID) throws TasteException {
        return delegate.getPreferenceValue(userID, itemID);
    }

    @Override
    public Long getPreferenceTime(long userID, long itemID) throws TasteException {
        return delegate.getPreferenceTime(userID, itemID);
    }

    @Override
    public int getNumItems() throws TasteException {
        return delegate.getNumItems();
    }

    @Override
    public int getNumUsers() throws TasteException {
        return delegate.getNumUsers();
    }

    @Override
    public int getNumUsersWithPreferenceFor(long itemID) throws TasteException {
        return delegate.getNumUsersWithPreferenceFor(itemID);
    }

    @Override
    public int getNumUsersWithPreferenceFor(long itemID1, long itemID2) throws TasteException {
        return delegate.getNumUsersWithPreferenceFor(itemID1, itemID2);
    }

    @Override
    public void setPreference(long userID, long itemID, float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePreference(long userID, long itemID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPreferenceValues() {
        return delegate.hasPreferenceValues();
    }

    @Override
    public float getMaxPreference() {
        return delegate.getMaxPreference();
    }

    @Override
    public float getMinPreference() {
        return delegate.getMinPreference();
    }

    @Override
    public void refresh(Collection<Refreshable> alreadyRefreshed) {

    }
}
