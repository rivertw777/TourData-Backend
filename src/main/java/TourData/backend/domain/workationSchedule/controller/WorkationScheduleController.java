package TourData.backend.domain.workationSchedule.controller;

import TourData.backend.domain.workationSchedule.dto.WorkationScheduleDto.WorkationScheduleCreateRequest;
import TourData.backend.domain.workationSchedule.dto.WorkationScheduleDto.WorkationScheduleResponse;
import TourData.backend.domain.workationSchedule.service.WorkationScheduleService;
import TourData.backend.global.security.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules/workation")
public class WorkationScheduleController {

    private final WorkationScheduleService workationScheduleService;

    @Operation(summary = "사용자 워케이션 일정 등록")
    @PostMapping
    public ResponseEntity<Void> createWorkationSchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                               @Valid @RequestBody WorkationScheduleCreateRequest requestParam) {
        Long userId = customUserDetails.getUser().getId();
        workationScheduleService.createWorkationSchedule(userId, requestParam);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자 워케이션 일정 조회")
    @GetMapping
    public ResponseEntity<List<WorkationScheduleResponse>> getAllWorkationSchedules(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUser().getId();
        List<WorkationScheduleResponse> responses = workationScheduleService.getAllWorkationSchedules(userId);
        return ResponseEntity.ok(responses);
    }

}
