package de.pruefprot.vote;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * Identifies a browser without login: on the first request a random voter id is stored in an
 * HttpOnly cookie. This is deliberately a simple lock, not real abuse protection.
 */
@Component
public class VoterCookieFilter extends OncePerRequestFilter {

    public static final String COOKIE_NAME = "pruefprot_voter";
    public static final String VOTER_ID_ATTRIBUTE = "pruefprot.voterId";

    private static final Duration COOKIE_MAX_AGE = Duration.ofDays(365);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        UUID voterId = readVoterId(request).orElseGet(() -> {
            UUID newId = UUID.randomUUID();
            ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, newId.toString())
                    .httpOnly(true)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(COOKIE_MAX_AGE)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return newId;
        });
        request.setAttribute(VOTER_ID_ATTRIBUTE, voterId);
        chain.doFilter(request, response);
    }

    private static Optional<UUID> readVoterId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .flatMap(value -> parseUuid(value).stream())
                .findFirst();
    }

    private static Optional<UUID> parseUuid(String value) {
        try {
            return Optional.of(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
