package com.music.controller;

import com.music.entity.dto.PlayHistoryDTO;
import com.music.response.R;
import com.music.response.PageVO;
import com.music.service.PlayHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
public class PlayHistoryController {

    @Autowired
    private PlayHistoryService playHistoryService;

    /**
     * 添加播放记录
     * @param dto 播放记录DTO
     * @return 操作结果
     */
    @PostMapping("/add")
    public R addHistory(@RequestBody PlayHistoryDTO dto) {
        return playHistoryService.addPlayHistory(dto);
    }

    /**
     * 分页查询播放历史
     * @param userId 用户ID
     * @param pageNum 页码（默认1）
     * @param pageSize 每页数量（默认20）
     * @return 分页数据
     */
    /*@GetMapping("/list")
    public R getHistoryPage(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return playHistoryService.getPlayHistoryPage(userId, pageNum, pageSize);
    }*/
}