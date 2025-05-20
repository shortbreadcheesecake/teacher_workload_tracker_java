package ru.example.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.WorkloadDto;
import ru.example.mapper.WorkloadMapper;
import ru.example.model.Workload;
import ru.example.service.WorkloadService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workloads")
public class WorkloadApiController {
    
    @Autowired
    private WorkloadService workloadService;
    
    @Autowired
    private WorkloadMapper workloadMapper;
    
    @GetMapping
    public ResponseEntity<List<WorkloadDto>> getAllWorkloads() {
        List<Workload> workloads = workloadService.findAll();
        List<WorkloadDto> workloadDtos = workloads.stream()
                .map(workloadMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(workloadDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WorkloadDto> getWorkloadById(@PathVariable Long id) {
        return workloadService.findById(id)
                .map(workloadMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<WorkloadDto> createWorkload(@Valid @RequestBody WorkloadDto workloadDto) {
        Workload workload = workloadMapper.toEntity(workloadDto);
        Workload savedWorkload = workloadService.save(workload);
        return new ResponseEntity<>(workloadMapper.toDto(savedWorkload), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<WorkloadDto> updateWorkload(@PathVariable Long id, @Valid @RequestBody WorkloadDto workloadDto) {
        if (!workloadService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Workload workload = workloadMapper.toEntity(workloadDto);
        workload.setId(id);
        Workload updatedWorkload = workloadService.save(workload);
        
        return ResponseEntity.ok(workloadMapper.toDto(updatedWorkload));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkload(@PathVariable Long id) {
        if (!workloadService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        workloadService.delete(workloadService.findById(id).get());
        return ResponseEntity.noContent().build();
    }
}
