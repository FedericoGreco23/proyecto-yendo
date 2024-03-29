package com.vpi.springboot.security.util;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vpi.springboot.Modelo.Usuario;

public class MyDetails implements UserDetails {
	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private Usuario user;

	     public MyDetails(Usuario user) {
	    	super();
	        this.user = user;
	    }

		public Usuario getUser() {
			return user;
		}

		public void setUser(Usuario user) {
			this.user = user;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			// TODO Auto-generated method stub
			return new ArrayList<>();
		}

		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return user.getContrasenia();
		}

		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return user.getMail();
		}

		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return true;
		}
	    
	    


	    
	}