package online.hbat.recommend.DO;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("movies")
public class MovieDO {

    @Id
    private String id;

    private Long movieId;

    private String title;

    private String genres;
}
