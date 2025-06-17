package org.example.nbcompany.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meetings")
public class BizMeeingController {
    @PostMapping
    public ResponseEntity<?> createMeeting() {
        return ResponseEntity.ok("【占位符】用于创建新会议的接口");
    }

    // 这个方法将用于处理 GET /api/v1/meetings 请求 (获取会议列表)
    // 我们稍后会填充真正的逻辑
    @GetMapping
    public ResponseEntity<?> getMeetingList() {
        return ResponseEntity.ok("【占位符】用于获取会议列表的接口");
    }

    // 这个方法将用于处理 GET /api/v1/meetings/{meetingId} 请求 (获取单个会议详情)
    // 我们稍后会填充真正的逻辑
    @GetMapping("/{meetingId}")
    public ResponseEntity<?> getMeetingDetails(@PathVariable Long meetingId) {
        return ResponseEntity.ok("【占位符】用于获取ID为 " + meetingId + " 的会议详情");
    }

    // 这个方法将用于处理 PUT /api/v1/meetings/{meetingId} 请求 (更新会议)
    // 我们稍后会填充真正的逻辑
    @PutMapping("/{meetingId}")
    public ResponseEntity<?> updateMeeting(@PathVariable Long meetingId) {
        return ResponseEntity.ok("【占位符】用于更新ID为 " + meetingId + " 的会议");
    }

    // 这个方法将用于处理 DELETE /api/v1/meetings/{meetingId} 请求 (删除会议)
    // 我们稍后会填充真正的逻辑
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long meetingId) {
        return ResponseEntity.ok("【占位符】用于删除ID为 " + meetingId + " 的会议");
    }
}
