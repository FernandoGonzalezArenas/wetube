package com.teakter.video.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
@RequiredArgsConstructor
public class HlsTranscoderService {

private final S3Client s3Client;

@Value("${aws.s3.bucket-videos}")
    private String bucketName;

public void processVideoToHls(String rawFilename) {
    String baseName=rawFilename.contains(".") ? rawFilename.substring(0, rawFilename.lastIndexOf('.')) : rawFilename;
    Path tempDir=null;

    try {
        tempDir=            Files.createTempDirectory("hls_proc_");
        File localMp4=new File(tempDir.toFile(), rawFilename);


//descargar el archivo .mp4 desde s3
        log.info("descargando video desde S3: {}", rawFilename);
        s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key("videos/" + rawFilename)
                        .build(),
                localMp4.toPath()
        );

        File outputDir = new File(tempDir.toFile(), baseName);
//ejecutar ffmpeg
        executeFFmpegHls(localMp4, outputDir);

        //subir los fracmentos HLS a S3
uploadHlsFilesToS3(outputDir, baseName);

log.info("Proceso HLS terminado y cargado en S3 para: {}", rawFilename);

    } catch (Exception e) {
        log.error("Error durante el procesamiento a HLS de el video con ID: {}", rawFilename, e);
        throw new RuntimeException("Error al procesar el video a HLS", e);
    } finally {
if (tempDir != null){
    //eliminar los archivos temporales locales recursivamente
    deleteDirectoryRecursively(tempDir.toFile());
}
    }
 }

 private void executeFFmpegHls(File inputVideoFile, File outputDir){
if (!outputDir.exists()){
    outputDir.mkdirs();
}

     //ejecutar FFmpeg localmente (fracmentacion HLS a 5 segundos)

     ProcessBuilder pb = new ProcessBuilder(
             "ffmpeg",
             "-i", inputVideoFile.getAbsolutePath(),
             "-c:v", "libx264",
             "-preset", "ultrafast",
             "-crf", "28",
             "-g", "150", //fuersa Keyframes cada 150 frames (para fracmentos de exactamente 5 segundos a 30fps)
             "-sc_threshold", "0", //evita que cambie de segmento en cambios de esena
             "-c:a", "aac",
             "-b:a", "128k",
             "-start_number", "0",
             "-hls_time", "5",
             "-hls_list_size", "0",
             "-f", "hls",
             new File(outputDir, "master.m3u8").getAbsolutePath()
     );

pb.redirectErrorStream(true);

try {
    Process process = pb.start();

try(BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()))){
    String line;

    while ((line = reader.readLine()) != null){
        log.trace("[FFmpeg] {}", line);
    }
    }

    int exitCode = process.waitFor();

    if (exitCode != 0) {
        throw new RuntimeException("error en FFmpeg al transcodificar el video. Exit code: " + exitCode);
    }
} catch (Exception e){
    throw new RuntimeException("Error al ejecutar el comando FFmpeg", e);
}
}

private void uploadHlsFilesToS3(File outputDir, String baseName){
    //subir los archivos divididos de el video a la carpeta hls de AWS S3
    File[] generatedFiles = outputDir.listFiles();
    if (generatedFiles == null) return;

        for (File file : generatedFiles) {
            String s3Key = "videos/hls/" + baseName + "/" + file.getName();
            String contentType= file.getName().endsWith(".m3u8")
                    ? "application/x-mpegURL"
                    : "video/MP2T";

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Key)
                            .contentType(contentType)
                            .build(),
                    RequestBody.fromFile(file)
            );
        }
}

private void deleteDirectoryRecursively(File file){
    File[] contents=file.listFiles();
    if (contents != null){
        for (File f : contents){
            deleteDirectoryRecursively(f);
        }
    }
    file.delete();
}

}
