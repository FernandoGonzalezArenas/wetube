package com.teakter.user.service;

import com.teakter.user.dto.UploadUrlResponse;

public interface StorageService {

UploadUrlResponse generateUploadUrl(String filename);
String getPublicUrl(String filename);

}
