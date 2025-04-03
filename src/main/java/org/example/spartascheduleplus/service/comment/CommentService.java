package org.example.spartascheduleplus.service.comment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.comment.CommentRequestDto;
import org.example.spartascheduleplus.dto.comment.CommentResponseDto;
import org.example.spartascheduleplus.entity.comment.Comment;
import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.example.spartascheduleplus.entity.user.User;
import org.example.spartascheduleplus.repository.comment.CommentRepository;
import org.example.spartascheduleplus.service.schedule.ScheduleService;
import org.example.spartascheduleplus.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final UserService userService;
    private final ScheduleService scheduleService;

    /**
     * [Service] 댓글 생성하는 메서드
     * @param dto 사용자가 입력한 댓글요청 객체
     * @param scheduleId 일정 id
     * @param userId 유저 id
     * @return 생성한 댓글응답 객체 반환
     */
    public CommentResponseDto createComment(CommentRequestDto dto, Long scheduleId, Long userId){
        Schedule schedule = scheduleService.findScheduleById(scheduleId);
        User user = userService.findUser(userId);

        Comment comment = new Comment(dto.getComment(), schedule, user);
        return new CommentResponseDto(commentRepository.save(comment));
    }

    public Comment findCommentById(Long commentId){
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."));
    }

    public List<CommentResponseDto> findAllComments(Long scheduleId) {
        // 존재하는 일정인지 체크
        scheduleService.findScheduleById(scheduleId);

        // 해당 일정의 모든 댓글을 리스트로 반환
        List<Comment> allComments = commentRepository.findAllByScheduleId(scheduleId);
        return allComments
                .stream()
                .map(CommentResponseDto::new)
                .toList();
    }

    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto dto, Long commentId,Long userId) {
        Comment existComment = this.findCommentById(commentId);

        // 사용자가 작성한 일정이 아닐 경우
        if(!userId.equals(existComment.getUser().getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 댓글의 수정권한이 없습니다.");
        }

        // 댓글 업데이트 및 댓글응답 객체 반환
        existComment.updateComment(dto.getComment());
        return new CommentResponseDto(existComment);
    }

    public void deleteComment(Long id) {
        // 존재하는 댓글인지 체크
        this.findCommentById(id);

        commentRepository.deleteById(id);
    }
}
