package in.AY.Movie.Backend.Download.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.AY.Movie.Backend.Download.Entity.DownloadRequest;
import in.AY.Movie.Backend.Download.Payload.DownloadProgressDto;
import in.AY.Movie.Backend.Download.Payload.DownloadRequestDto;
import in.AY.Movie.Backend.Download.Payload.DownloadResponseDto;
import in.AY.Movie.Backend.Download.Service.DownloadService;

@RestController
@RequestMapping("/api/download")
@CrossOrigin(origins = "http://localhost:5173")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @PostMapping("/movie")
    public ResponseEntity<?> downloadMovie(
            @RequestBody DownloadRequestDto request) {

        try {

            DownloadResponseDto response =
                    downloadService.downloadVideo(request.getUrl(), request.getQuality());

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/progress")
    public ResponseEntity<DownloadProgressDto> getProgress() {

        return ResponseEntity.ok(
            downloadService.getProgress()
        );
    }
    
    @PostMapping("/cancel")
    public ResponseEntity<DownloadProgressDto> cancelDownload() {

        return ResponseEntity.ok(downloadService.cancelDownload());
    }
    
    @PostMapping("/pause")
    public ResponseEntity<DownloadProgressDto> pauseDownload() {

        return ResponseEntity.ok(downloadService.pauseDownload());
    }
    
    @PostMapping("/resume")
    public ResponseEntity<DownloadResponseDto> resumeDownload() {

        return ResponseEntity.ok(downloadService.resumeDownload());
    }

    @GetMapping("/file/{name}")
    public ResponseEntity<Resource> getFile(
            @PathVariable String name)
            throws MalformedURLException {

        Path path = Paths.get("C:/Users/ACER/Downloads")
                         .resolve(name);

        Resource resource =
                new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + name + "\""
                )
                .body(resource);
    }
}