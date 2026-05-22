package in.AY.Movie.Backend.User.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
			
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{
		String requestToken = request.getHeader("Authorization");
		
		System.out.println(requestToken);
		
		String userName = null;
		String token = null;
		
		if(requestToken != null && requestToken.startsWith("Bearer ")) {
			token = requestToken.substring(7);
			try 
			{
				userName = this.jwtTokenHelper.getEmailFromToken(token);
			} catch (Exception e) {
				System.out.println("jwt unable to get token");
			}
		}
		
		else {
			System.out.println("Jwt token is not begin with Bearer");
		}
		
		if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null) 
		{
			//This fetches: Username, Password, Roles/Authorities From your DB.
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
			if(this.jwtTokenHelper.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(
				        new WebAuthenticationDetailsSource().buildDetails(request)
				);
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			else 
			{
				System.out.println("Invalid jwt token");
			}
		}
		else 
		{
			System.out.println("username is null or context is null");
		}
		filterChain.doFilter(request, response);
		
	}

}
