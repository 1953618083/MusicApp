package com.music.controller;

import com.music.entity.dto.AddClassifyDTO;
import com.music.entity.dto.ModifyClassifyDTO;
import com.music.entity.dto.PageDTO;
import com.music.response.R;
import com.music.service.ClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/classify")
public class ClassifyController {

    @Autowired
    private ClassifyService classifyService;

    @PostMapping("/add")
    public R addClassify(@RequestBody AddClassifyDTO dto){
        return classifyService.addClassify(dto);
    }

    @PostMapping("/modify")
    public R modifyClassify(@RequestBody ModifyClassifyDTO dto){
        return classifyService.modifyClassify(dto);
    }

    @PostMapping("/delete")
    public R deleteClassify(@RequestParam("id") Long id){
        return classifyService.deleteClassify(id);
    }

    @PostMapping("/list")
    public R classifyList(){
        return classifyService.classifyList();
    }

    @PutMapping("/modify/{id}")
    public R modifyClassify(@PathVariable Long id, @RequestBody @Valid ModifyClassifyDTO dto) {
        dto.setId(id);
        return classifyService.modifyClassify(dto);
    }
    @PostMapping("/{id}/music")
    public R getMusicByClassify(@PathVariable Long id,@RequestParam(required = false) String userId) {
        return classifyService.getMusicByClassify(id, userId);
    }
}
