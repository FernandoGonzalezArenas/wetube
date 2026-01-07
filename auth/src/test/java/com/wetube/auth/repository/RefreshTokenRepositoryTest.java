package com.wetube.auth.repository;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.wetube.auth.entity.RefreshTokenEntity;
import com.wetube.auth.entity.UserEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.ANY)
public class RefreshTokenRepositoryTest {

@Autowired RefreshTokenRepository refreshRepo;
@Autowired UserRepository userRepo;

@Test
void  findByToken_ok(){
    //creamos un usuario y registro en la entidad de refresh
    UserEntity u=userRepo.save(UserEntity.builder()
.username("fernando")
.password("x")
.email("fer@mail.com")
.build());
    RefreshTokenEntity r=RefreshTokenEntity.builder()
    .token("REF")
    .expiryDate(Instant.now().plusSeconds(60))
    .user(u)
    .build();
    refreshRepo.save(r);

    //se busca el token
    var  out=refreshRepo.findByToken("REF");

    //verificaciones
    assertThat(out).isPresent();
    assertThat(out.get().getUser().getUsername()).isEqualTo("fernando");
}

@Test
void  deleteAllByExpiryDateBefore_eliminaExpirados(){
    UserEntity u=userRepo.save(UserEntity.builder()
.username("bob")
.password("b")
.email("bob@mail.com")
.build());

    //token expirado y vijente
    refreshRepo.save(RefreshTokenEntity.builder()
.token("OLD")
.expiryDate(Instant.now().minusSeconds(5))
.user(u)
.build());
    refreshRepo.save(RefreshTokenEntity.builder()
.token("NEW")
.expiryDate(Instant.now().plusSeconds(60))
.user(u)
.build());

    //borramos los expirados
    refreshRepo.deleteAllByExpiryDateBefore(Instant.now());

    //comprobamos que OLD fue borrado y NEW aun existe
    assertThat(refreshRepo.findByToken("OLD")).isEmpty();
    assertThat(refreshRepo.findByToken("NEW")).isPresent();
}

}
