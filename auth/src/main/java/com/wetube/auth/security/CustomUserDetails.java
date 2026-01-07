package com.wetube.auth.security;

import com.wetube.auth.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {

private final String userId;
private final String email;
private final UserEntity userEntity;

public CustomUserDetails(UserEntity userEntity, Collection<? extends GrantedAuthority> authorities){
    super(userEntity.getUsername(), userEntity.getPassword(), authorities);
    this.userId= userEntity.getId().toString();
    this.email=userEntity.getEmail();
    this.userEntity=userEntity;
}

}
