package in.AY.Movie.Backend.User.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "user_name", nullable = false, length = 100)
	private String name;
	
	@Column
	private String email;
	
	@Column
	private String password;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
	joinColumns=@JoinColumn(name="user", referencedColumnName="id"),
	inverseJoinColumns = @JoinColumn(name="role", referencedColumnName="id")
	)
	private Set<Role> roles = new HashSet<>();
	
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/*@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return new ArrayList();
	}*/
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		System.out.println("Roles = " + roles);
	    return this.roles.stream()
	    		.map(role -> {
	                System.out.println(role.getName());
	                return new SimpleGrantedAuthority(role.getName());
	            })
	            .collect(Collectors.toList());
	}
	
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	 }
	
}
