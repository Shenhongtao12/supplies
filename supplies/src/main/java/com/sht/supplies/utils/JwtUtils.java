package com.sht.supplies.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author Administrator
 */
@Slf4j
public class JwtUtils {
	private static final String SUBJECT = "Aaron-supplies";
	private static final long EXPIRE = 1000 * 60 * 60 * 24 * 3;
	private static final String APPSECRET = "e61451278486b545f860defcae13c19f";

	public static String geneJsonWebToken(Integer id) {
		String token;
		token = Jwts.builder().setSubject(SUBJECT)
				.claim("id", id)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
				.signWith(SignatureAlgorithm.HS256, APPSECRET)
				.compact();

		return token;
	}

	public static Claims checkJWT(String token) {
		try {
			return Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			log.info("身份信息已过期");
			return null;
		} catch (Exception e) {
			log.info("无效的身份信息");
			return null;
		}
	}
}
