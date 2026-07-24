package com.teakter.auth.security;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;

public class JwtFilterTest {

@AfterEach
void  cleanup(){
    //limpear el contexto de seguridad despues de cada test
    SecurityContextHolder.clearContext();
}
    
@Test
void  conBearerValido_seteaAutenticacion() throws  Exception{
    //dependencias mockeadas
    JwtUtil jwtUtil=mock(JwtUtil.class);
    UserDetailsService uds=mock(UserDetailsService.class);
    FilterChain chain=mock(FilterChain.class);

    //instanciamos el filtro real
JwtFilter filter=new JwtFilter(jwtUtil, uds);

//request con authorization: bearer token
MockHttpServletRequest req=new MockHttpServletRequest();
req.addHeader("Authorization", "Bearer ABC");
MockHttpServletResponse res=new MockHttpServletResponse();

//stubs: extract y validacion
when(jwtUtil.extractUsername("ABC")).thenReturn("fernando");
when(uds.loadUserByUsername("fernando"))
.thenReturn(org.springframework.security.core.userdetails.User
.withUsername("fernando").password("x").authorities("ROLE_USER").build());
when(jwtUtil.isTokenValid("ABC", "fernando")).thenReturn(true);

//ejecutamos la cadena
filter.doFilter(req, res, chain);

//debe existir autenticacion en el contexto
assertNotNull(SecurityContextHolder.getContext().getAuthentication());
assertEquals("fernando", SecurityContextHolder.getContext().getAuthentication().getName());

//la cadena continuó
verify(chain, times(1)).doFilter(req, res);
}

@Test
void  sinHeader_noAutentica_YContinua() throws  Exception{
    JwtUtil jwtUtil=mock(JwtUtil.class);
    UserDetailsService uds=mock(UserDetailsService.class);
    FilterChain chain=mock(FilterChain.class);
    JwtFilter filter=new JwtFilter(jwtUtil, uds);
    MockHttpServletRequest req=new MockHttpServletRequest();
    MockHttpServletResponse res=new MockHttpServletResponse();
filter.doFilter(req, res, chain);
assertNull(SecurityContextHolder.getContext().getAuthentication());
verify(chain, times(1)).doFilter(req, res);
}

}
