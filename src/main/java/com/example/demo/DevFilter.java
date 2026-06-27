/*package com.example.demo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class DevFilter extends OncePerRequestFilter {
    @Value("${telemetry.token}")
    private String password;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,FilterChain filterChain) throws ServletException,IOException
    {
        if(!httpServletRequest.getMethod().equals("POST"))
        {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

       String iToken =  httpServletRequest.getHeader("X-Device-Token");
       if(iToken==null||!iToken.equals(password)){
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.getWriter().write("Unauthorized User go away nigga ");
        return;
       }
       filterChain.doFilter(httpServletRequest, httpServletResponse);

    }


}*/
