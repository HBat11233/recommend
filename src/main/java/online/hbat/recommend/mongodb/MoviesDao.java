package online.hbat.recommend.mongodb;

import online.hbat.recommend.DO.MovieDO;

import java.util.List;

public interface MoviesDao {
    MovieDO findMovieByMovieId(Long movieId);
    List<MovieDO> findMoviesByMovieId(List<Long> movieIdList);
    List<MovieDO> findMoviesBySize(int size);
}
