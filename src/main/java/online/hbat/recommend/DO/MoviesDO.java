package online.hbat.recommend.DO;

import lombok.Data;

import java.util.List;

@Data
public class MoviesDO {
    List<MovieDO> data;
    Long userId;
}
