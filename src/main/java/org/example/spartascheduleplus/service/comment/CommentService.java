package org.example.spartascheduleplus.service.comment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.comment.CommentRequestDto;
import org.example.spartascheduleplus.dto.comment.CommentResponseDto;
import org.example.spartascheduleplus.entity.comment.Comment;
import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.example.spartascheduleplus.entity.user.User;
import org.example.spartascheduleplus.exception.ResponseExceptionProvider;
import org.example.spartascheduleplus.repository.comment.CommentRepository;
import org.example.spartascheduleplus.service.schedule.ScheduleService;
import org.example.spartascheduleplus.service.user.UserService;
import org.springframework.stereotype.Service;

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

    /**
     * [Service] 댓글 조회하는 메서드
     * @param commentId 댓글 id
     * @return 댓글 객체
     */
    public Comment findCommentById(Long commentId){
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> ResponseExceptionProvider.notFound("존재하지 않는 댓글입니다."));
    }

    /**
     * [Service] 일정의 모든 댓글을 조회하는 메서드
     * @param scheduleId 일정 id
     * @return 댓글 응답객체 리스트를 반환
     */
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

    /**
     * [Service] 댓글 수정하는 메서드
     * @param dto 사용자가 입력한 댓글요청 객체
     * @param commentId 댓글 id
     * @param userId 유저 id
     * @return 댓글 응답객체 반환
     */
    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto dto, Long commentId,Long userId) {
        Comment existComment = this.findCommentById(commentId);

        // 사용자가 작성한 일정이 아닐 경우
        if(!userId.equals(existComment.getUser().getId())){
            throw ResponseExceptionProvider.forbidden("해당 댓글의 수정권한이 없습니다.");
        }

        existComment.updateComment(dto.getComment());
        return new CommentResponseDto(existComment);
    }

    /**
     * [Service] 댓글 삭제하는 메서드
     * @param id 댓글 id
     */
    public void deleteComment(Long id) {
        this.findCommentById(id);

        commentRepository.deleteById(id);
    }
}
