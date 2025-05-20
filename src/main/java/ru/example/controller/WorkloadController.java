package ru.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.example.model.Workload;
import ru.example.service.WorkloadService;
import java.util.List;

@RestController
@RequestMapping("/workloads")
public class WorkloadController {
    @Autowired
    private WorkloadService workloadService;

    @PostMapping
    public Workload createWorkload(@RequestBody Workload workload) {
        return workloadService.save(workload);
    }

    @GetMapping
    public List<Workload> getAllWorkloads() {
        return workloadService.findAll();
    }

    @GetMapping("/{id}")
    public Workload getWorkload(@PathVariable Long id) {
        return workloadService.findById(id).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void deleteWorkload(@PathVariable Long id) {
        workloadService.deleteById(id);
    }
}
