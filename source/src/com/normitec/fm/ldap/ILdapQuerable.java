package com.normitec.fm.ldap;

import com.normitec.exception.NormitecException;

public interface ILdapQuerable {

	public abstract String search(String searchBase,
			String firstName, String lastName) throws NormitecException;

	public abstract String search(String searchBase,
			String compoundSearchFilter) throws NormitecException;
	
	public abstract void closeConnection();

}