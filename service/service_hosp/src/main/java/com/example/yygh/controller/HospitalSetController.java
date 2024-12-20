package com.example.yygh.controller;

import com.example.yygh.model.hosp.*;
import com.example.yygh.service.HospitalSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hospital/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    @GetMapping("findAll")
    public List<HospitalSet> findAll() {
        return hospitalSetService.list();
    }

    @DeleteMapping("{id}")
    public boolean removeHospitalSet(@PathVariable String id) {
        return hospitalSetService.removeById(id);
    }
}
