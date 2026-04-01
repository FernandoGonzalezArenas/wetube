package com.wetube.user.service;

import com.wetube.user.dto.UploadUrlResponse;

public interface StorageService {

UploadUrlResponse generateUploadUrl(String filename);
String getPublicUrl(String filename);

}
