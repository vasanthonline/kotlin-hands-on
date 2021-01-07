package com.example.authentication.security

import com.example.authentication.constants.AppConstants
import com.example.authentication.model.User
import com.example.authentication.service.UserService
import com.example.authentication.service.UserSessionService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CookieAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtUtils: JwtUtils,
    private val userService: UserService,
    private val userSessionService: UserSessionService
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val cookies = request.cookies
        if(cookies == null || cookies.isEmpty()) {
            filterChain.doFilter(request, response)
            return
        }
        val authToken = cookies.find { it.name == AppConstants.COOKIE_AUTHORIZATION }?.value ?: ""
        val usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken? = getAuthentication(authToken)
        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken

        filterChain.doFilter(request, response)
    }

    private fun getAuthentication(bearerToken: String): UsernamePasswordAuthenticationToken? {
        if(!userSessionService.isActiveSession(jwtUtils.parseBearerToken(bearerToken) ?: ""))
            return null
        val email:String = jwtUtils.parseToken(bearerToken) ?: return null
        val user: User? = userService.getUserByEmail(email)
        return UsernamePasswordAuthenticationToken(user, null, ArrayList())
    }

}