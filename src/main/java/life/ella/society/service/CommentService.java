package life.ella.society.service;

import life.ella.society.enums.CommentTypeEnum;
import life.ella.society.exception.CustomizeErrorCode;
import life.ella.society.exception.CustomizeException;
import life.ella.society.mapper.CommentMapper;
import life.ella.society.mapper.QuestionExtMapper;
import life.ella.society.mapper.QuestionMapper;
import life.ella.society.model.Comment;
import life.ella.society.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Transactional
    public void insert(Comment comment) {
        if(comment.getParentId()==null||comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if(comment.getType()==null|| ! CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else{
           Question question=questionMapper.selectByPrimaryKey(comment.getParentId());
           if(question==null){
               throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
           }
           commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }
}