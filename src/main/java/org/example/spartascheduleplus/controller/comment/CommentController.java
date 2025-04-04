package org.example.spartascheduleplus.controller.comment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.api.SuccessResponseDto;
import org.example.spartascheduleplus.dto.comment.CommentRequestDto;
import org.example.spartascheduleplus.dto.comment.CommentDetailResponseDto;
import org.example.spartascheduleplus.dto.comment.PagedCommentResponseDto;
import org.example.spartascheduleplus.dto.user.UserResponseDto;
import org.example.spartascheduleplus.service.comment.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.example.spartascheduleplus.dto.api.SuccessResponseDto.createSuccessResponseDto;

@RestController
@RequestMapping("/schedule/{scheduleId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * [Controller] 댓글 생성하는 메서드
     * @param scheduleId 일정 id
     * @param requestDto 사용자가 작성한 댓글요청 객체
     * @param loginUser 로그인 유저 정보
     * @param httpRequest httpRequest 정보
     * @return 생성된 상세댓글 응답 객체를 반환
     */
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDto<CommentDetailResponseDto>> createComment(
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentRequestDto requestDto,
            @SessionAttribute(
                    name = "loginUser")
            UserResponseDto loginUser,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                createSuccessResponseDto(
                        HttpStatus.CREATED,
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

    /**
     * [Controller] 특정 일정의 모든 댓글을 조회하는 메서드
     * @param scheduleId 일정 id
     * @param httpRequest httpRequest
     * @param pageable 페이징 설정
     * @return 페이징댓글 응답 객체를 반환
     */
    @GetMapping
    public ResponseEntity<SuccessResponseDto<PagedCommentResponseDto>> findAllComments(
            @PathVariable Long scheduleId,
            HttpServletRequest httpRequest,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return new ResponseEntity<>(
                createSuccessResponseDto(
                        HttpStatus.OK,
                        httpRequest.getRequestURI(),
                        "일정의 전체 댓글을 조회합니다.",
                        commentService.findAllComments(scheduleId, pageable)
                ),
                HttpStatus.OK
        );
    }

    /**
     * [Controller] 특정 댓글을 조회하는 메서드
     * @param scheduleId 일정 id
     * @param id 댓글 id
     * @param httpRequest httpRequest
     * @return 특정 상세댓글 응답 객체를 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponseDto<CommentDetailResponseDto>> findCommentById(
            @PathVariable Long scheduleId,
            @PathVariable Long id,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                createSuccessResponseDto(
                        HttpStatus.OK,
                        httpRequest.getRequestURI(),
                        "단일 댓글을 조회합니다.",
                        commentService.createCommentResponseDto(id, scheduleId)
                ),
                HttpStatus.OK
        );
    }

    /**
     * [Controller] 특정 댓글을 수정하는 메서드
     * @param dto 사용자가 입력한 댓글 요청 객체
     * @param scheduleId 일정 id
     * @param id 댓글 id
     * @param loginUser 로그인 유저
     * @param httpRequest httpRequest
     * @return 수정된 상세댓글 응답 객체를 반환
     */
    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponseDto<CommentDetailResponseDto>> updateComment(
            @Valid @RequestBody CommentRequestDto dto,
            @PathVariable Long scheduleId,
            @PathVariable Long id,
            @SessionAttribute(
                    name = "loginUser")
            UserResponseDto loginUser,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                createSuccessResponseDto(
                        HttpStatus.OK,
                        httpRequest.getRequestURI(),
                        "일정 수정을 완료했습니다.",
                        commentService.updateComment(dto, id, scheduleId, loginUser.getId())
                ),
                HttpStatus.OK
        );

    }

    /**
     * [Controller] 특정 댓글을 삭제하는 메서드
     * @param scheduleId 일정 id
     * @param id 댓글 id
     * @param loginUser 로그인 유저 정보
     * @param httpRequest httpRequest
     * @return API 응답 객체 반환
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponseDto> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long id,
            @SessionAttribute(
                    name = "loginUser")
            UserResponseDto loginUser,
            HttpServletRequest httpRequest
    ){
        // 특정 일정 삭제
        commentService.deleteComment(id, scheduleId, loginUser.getId());

        return new ResponseEntity<>(
                createSuccessResponseDto(
                        HttpStatus.OK,
                        httpRequest.getRequestURI(),
                        "요청한 댓글을 성공적으로 삭제했습니다.",
                        null
                ),
                HttpStatus.OK
        );
    }
}
