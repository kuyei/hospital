package com.example.yygh.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.util.MD5;
import com.example.yygh.common.result.Result;
import com.example.yygh.model.hosp.*;
import com.example.yygh.service.HospitalSetService;
import com.example.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/admin/hospital/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        return Result.ok(hospitalSetService.list());
    }

    @ApiOperation(value = "根据id删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospitalSet(@PathVariable String id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "分页查询医院设置")
    @PostMapping("getPage{current}/{limit}")
    public Result getHospitalSetPage(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo vo) {
        Page<HospitalSet> hospPage = new Page<>(current,limit);
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hospitalCode = vo.getHoscode();
        String hospitalName = vo.getHosname();
        if (!StringUtils.isEmpty(hospitalName)) {
            wrapper.like("hosname", hospitalName);
        }
        if (!StringUtils.isEmpty(hospitalCode)) {
            wrapper.eq("hoscode", hospitalCode);
        }

        Page<HospitalSet> page = hospitalSetService.page(hospPage, wrapper);
        return Result.ok(page);
    }

    @ApiOperation(value = "添加医院设置")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 设置医院集合的状态，1为启用，0为禁用
        hospitalSet.setStatus(1);
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        boolean save = hospitalSetService.save(hospitalSet);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
    @ApiOperation(value = "根据id获取医院设置")
    @GetMapping("getHospitalSet/{id}")
    public Result getHospSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    @ApiOperation(value = "修改医院设置")
    @PostMapping("updateHospitalSet")
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "批量删除医院设置")
    @DeleteMapping("batchDeletion")
    public Result batchDeletion(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }
    @ApiOperation(value = "医院设置锁定")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    @ApiOperation(value = "发送签名密钥")
    @PutMapping("sendKey/{id}")
    public Result sendKey(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String code = hospitalSet.getHoscode();
        String signKey = hospitalSet.getSignKey();
        // todo: 发送短信
        return Result.ok();
    }
}
