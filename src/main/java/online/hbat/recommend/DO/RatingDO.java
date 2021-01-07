package online.hbat.recommend.DO;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Document("ratings")
public class RatingDO implements Serializable {

    @Id
    private String id;

    private Long userId;

    private Long movieId;

    private Float rating;

    private Long create_at;

    private Long delete_at;
}
