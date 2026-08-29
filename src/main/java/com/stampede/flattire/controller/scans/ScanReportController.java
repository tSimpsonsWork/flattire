package com.stampede.flattire.controller.scans;

import com.stampede.flattire.dto.response.ScanStatusResponse;
import com.stampede.flattire.dto.response.ScanSummaryResponse;
import com.stampede.flattire.dto.response.VulnerableDependencyResponse;
import com.stampede.flattire.service.nvd.scans.ScanReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flattire/api/scan")
@RequiredArgsConstructor
public class ScanReportController {

    private final ScanReportService scanReportService;

    //http://localhost:8080/flattire/api/scan/status
    @GetMapping("/status")
    public ScanStatusResponse getStatus() {
        return scanReportService.getStatus();
    }

    //http://localhost:8080/flattire/api/scan/summary
    @GetMapping("/summary")
    public ScanSummaryResponse getSummary() {
        return scanReportService.getSummary();
    }

    //http://localhost:8080/flattire/api/scan/vulnerabilities
    @GetMapping("/vulnerabilities")
    public List<VulnerableDependencyResponse> getVulnerabilities() {
        return scanReportService.getVulnerableDependencies();
    }
}