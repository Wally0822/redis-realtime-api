package com.redisrealtime.redis_realtime_api.controller;

import lombok.*;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final StringRedisTemplate redisTemplate;
    private static final String RANKING_KEY = "ranking";

    /**
     * 가데이터 삽입 (ex: user1 ~ user20, 랜덤 점수)
     */
    @PostMapping("/init")
    public ResponseEntity<String> insertDummyData(@RequestParam(defaultValue = "20") int count) {
        Random random = new Random();
        for (int i = 1; i <= count; i++) {
            String userId = "user" + i;
            double score = 50 + random.nextInt(101); // 50 ~ 150
            redisTemplate.opsForZSet().add(RANKING_KEY, userId, score);
        }
        return ResponseEntity.ok("✅ Dummy 데이터 " + count + "명 삽입 완료");
    }

    /**
     * 전체 랭킹 조회 (내림차순 TOP 100)
     */
    @GetMapping("/top")
    public ResponseEntity<List<RankingResponse>> getTopRankings() {
        Set<ZSetOperations.TypedTuple<String>> rankings = redisTemplate.opsForZSet()
                .reverseRangeWithScores(RANKING_KEY, 0, 99);

        if (rankings == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<RankingResponse> result = rankings.stream()
                .map(tuple -> new RankingResponse(tuple.getValue(), tuple.getScore()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * 사용자 개인 랭킹 조회
     */
    @GetMapping("/rank/{userId}")
    public ResponseEntity<?> getUserRank(@PathVariable String userId) {
        Double score = redisTemplate.opsForZSet().score(RANKING_KEY, userId);
        if (score == null) {
            return ResponseEntity.status(404).body(Map.of("error", "사용자를 찾을 수 없습니다."));
        }

        Long rank = redisTemplate.opsForZSet().reverseRank(RANKING_KEY, userId);
        if (rank == null) {
            return ResponseEntity.status(404).body(Map.of("error", "랭킹 정보를 찾을 수 없습니다."));
        }

        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "rank", rank + 1,
                "score", score
        ));
    }

    /**
     * 특정 사용자의 점수를 업데이트
     */
    @PatchMapping("/update")
    public ResponseEntity<String> updateUserScore(@RequestParam String userId, @RequestParam double newScore) {
        Boolean exists = redisTemplate.opsForZSet().rank(RANKING_KEY, userId) != null;
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ 해당 사용자가 존재하지 않습니다.");
        }

        redisTemplate.opsForZSet().add(RANKING_KEY, userId, newScore);
        return ResponseEntity.ok("✅ 사용자 " + userId + "의 점수를 " + newScore + "으로 업데이트했습니다.");
    }

    /**
     * 전체 랭킹 데이터 삭제
     */
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearRanking() {
        redisTemplate.delete(RANKING_KEY);
        return ResponseEntity.ok("🧹 전체 랭킹 데이터 삭제 완료");
    }

    /**
     * 랭킹 응답 DTO
     */
    @Getter
    @AllArgsConstructor
    static class RankingResponse {
        private String userId;
        private Double score;
    }
}
