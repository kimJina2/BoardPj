package com.boardproject.File;

import com.boardproject._core.errors.exception.Exception500;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    public static final char SEPARATOR_CHAR = File.separatorChar;   // 운영체제에 맞추기 위함
    public static final String PROJECT_ROOT_PATH = System.getProperty("user.dir");
    public static final String DEV_FILES_PATH = "src/main/resources/static/files/";
    public static final String OS_FILES_PATH = DEV_FILES_PATH.replace('/', SEPARATOR_CHAR);
    public static final String TEMP_SERVER_PATH = PROJECT_ROOT_PATH + SEPARATOR_CHAR + OS_FILES_PATH + "temp";
    public static final String SAVED_SERVER_PATH = PROJECT_ROOT_PATH + SEPARATOR_CHAR + OS_FILES_PATH + "saved";

    @Transactional
    public JsonObject save(MultipartFile file) {
        JsonObject jsonObject = new JsonObject();

        // 기존 파일명
        String originName = file.getOriginalFilename();

        // 랜덤 식별자
        UUID uuid = UUID.randomUUID();

        // 식별자_파일명 -> 저장될 파일 이름
        String randomFileName = uuid + "_" + originName;

        // 새 파일명과 경로를 지정하여 파일 생성
        File saveFile = new File(TEMP_SERVER_PATH, randomFileName);

        try {
            // 파일 저장
            InputStream fileStream = file.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, saveFile);

            // 응답
            jsonObject.addProperty("url", "/file/load?path=temp&name=" + randomFileName);
            jsonObject.addProperty("originName", originName);
            jsonObject.addProperty("savedName", randomFileName);
            jsonObject.addProperty("reponseCode", "success");

            return jsonObject;
        } catch (IOException | IllegalStateException e) {
            FileUtils.deleteQuietly(saveFile);    // 실패시 저장된 파일 삭제
            throw new Exception500("서버: 파일 저장 오류!");
        }
    }

    @Transactional
    public FileResponse.loadDTO load(String path, String fileName) {
        try {
            String reqPath = path.equals("temp") ? TEMP_SERVER_PATH : SAVED_SERVER_PATH;

            Path filePath = Paths.get(reqPath + SEPARATOR_CHAR + fileName);

            String contentType = Files.probeContentType(filePath);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath));

            return new FileResponse.loadDTO(resource, contentType);
        } catch (IOException e) {
            throw new Exception500("서버: 파일 로드 오류!");
        }
    }

    @Transactional
    public void moveFileToSave() {
        copy(new File(TEMP_SERVER_PATH), new File(SAVED_SERVER_PATH));
    }

    @Transactional
    public void delete(String fileName) {
        try {
            Path path = Paths.get(SAVED_SERVER_PATH + SEPARATOR_CHAR + fileName);

            Files.delete(path);
        } catch (Exception e) {
            throw new Exception500("서버: 파일 삭제 오류!");
        }
    }

    private void cleanDirectory(String path) {
        File folder = new File(path);
        try {
            if (folder.exists()) {
                File[] folder_list = folder.listFiles();

                for (int i = 0; i < folder_list.length; i++) {
                    if (folder_list[i].isFile()) {
                        folder_list[i].delete();
                    } else {
                        cleanDirectory(folder_list[i].getPath());
                    }
                    folder_list[i].delete();
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void copy(File sourceF, File targetF) {
        File[] target_file = sourceF.listFiles();

        for (File file : target_file) {
            File temp = new File(targetF.getAbsolutePath() + SEPARATOR_CHAR + file.getName());
            if (file.isDirectory()) {
                temp.mkdir();
                copy(file, temp);
            } else {
                FileInputStream fis = null;
                FileOutputStream fos = null;

                try {
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(temp);
                    byte[] b = new byte[4096];
                    int cnt = 0;
                    while ((cnt = fis.read(b)) != -1) {
                        fos.write(b, 0, cnt);
                    }
                } catch (Exception e) {
                    throw new Exception500("서버: 파일 저장 오류!");
                } finally {
                    try {
                        fis.close();
                        fos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        throw new Exception500("서버: 파일 저장 오류!");
                    }
                }
            }
        }

        cleanDirectory(sourceF.toString());
    }
}
