package org.example.spartascheduleplus.controller.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.api.SuccessResponseDto;
import org.example.spartascheduleplus.dto.comment.CommentRequestDto;
import org.example.spartascheduleplus.dto.comment.CommentResponseDto;
import org.example.spartascheduleplus.dto.user.UserResponseDto;
import org.example.spartascheduleplus.service.comment.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/schedule/{scheduleId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDto<CommentResponseDto>> createComment(
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentRequestDto requestDto,
            @SessionAttribute(
                    name = "loginUser")
            UserResponseDto loginUser,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                new SuccessResponseDto<>(
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.getReasonPhrase(),
                        httpRequest.getRequestURI(),
                        "댓글 생성에 성공하였습니다.",
                        commentService.createComment(
                                requestDto,
                                scheduleId,
                                loginUser.getId())
                ),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<SuccessResponseDto<List<CommentResponseDto>>> findAllComments(
            @PathVariable Long scheduleId,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                new SuccessResponseDto<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        httpRequest.getRequestURI(),
                        "일정의 전체 댓글을 조회합니다.",
                        commentService.findAllComments(scheduleId)
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponseDto<CommentResponseDto>> findCommentById(
            @PathVariable String scheduleId,
            @PathVariable Long id,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                new SuccessResponseDto<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        httpRequest.getRequestURI(),
                        "단일 댓글을 조회합니다.",
                        new CommentResponseDto(commentService.findCommentById(id))
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<SuccessResponseDto<CommentResponseDto>> updateComment(
            @Valid @RequestBody CommentRequestDto dto,
            @PathVariable Long scheduleId,
            @PathVariable Long id,
            @SessionAttribute(
                    name = "loginUser")
            UserResponseDto loginUser,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                new SuccessResponseDto<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        httpRequest.getRequestURI(),
                        "일정 수정을 완료했습니다.",
                        commentService.updateComment(dto, id, loginUser.getId())
                ),
                HttpStatus.OK
        );

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponseDto> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long id,
            HttpServletRequest httpRequest
    ){
        // 특정 일정 삭제
        commentService.deleteComment(id);

        return new ResponseEntity<>(
                new SuccessResponseDto<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        httpRequest.getRequestURI(),
                        "요청한 댓글을 성공적으로 삭제했습니다.",
                        null
                ),
                HttpStatus.OK
        );
    }
}
