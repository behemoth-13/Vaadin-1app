package com.example.myapp1.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtility{
	private static final EntityManagerFactory emFactory;
	private static final EntityManager em;
	
	static {
		System.out.println("static");
		   emFactory = Persistence.createEntityManagerFactory("demo_hotels");
		   em = emFactory.createEntityManager();
	}
	public static EntityManager getEntityManager(){
		return em;
	}
	public static void close(){
		emFactory.close();
	}
}
