package lu.isd.isd_api.config;

// EDIT: Added debug logging and exception handling around JWT validation (2026-01-19)

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import lu.isd.isd_api.security.CustomUserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lu.isd.isd_api.repository.UserRepository;
import lu.isd.isd_api.service.JwtService;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                if (jwtService.isTokenValid(token)) {
                    String username = jwtService.extractUsername(token);
                    String role = jwtService.extractRole(token);
                    System.out.println("[JwtAuthFilter] Extracted username=" + username + " role=" + role);

                    CustomUserDetails principal = new CustomUserDetails(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            principal.getAuthorities());

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("[JwtAuthFilter] SecurityContext auth set: " +
                            SecurityContextHolder.getContext().getAuthentication());
                } else {
                    System.out.println("[JwtAuthFilter] Token invalid or expired");
                }
            } catch (Exception e) {
                System.out.println("[JwtAuthFilter] Error validating token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
