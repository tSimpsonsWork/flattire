package com.stampede.flattire.controller.scans;

import com.stampede.flattire.dto.response.ScanStatusResponse;
import com.stampede.flattire.service.nvd.scans.ScanReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flattire/api/scan")
@RequiredArgsConstructor
public class ScanReportController {

    private final ScanReportService scanReportService;

    //GET http://localhost:8080/flattire/api/scan/status
    @GetMapping("/status")
    public ScanStatusResponse getStatus() {
        return scanReportService.getStatus();
    }
}