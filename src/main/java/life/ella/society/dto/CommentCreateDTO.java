package life.ella.society.dto;

import life.ella.society.model.User;
import lombok.Data;

@Data
public class CommentCreateDTO {
    private Long parentId;
    private Integer type;
    private String content;
}