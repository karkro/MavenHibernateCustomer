package com.karkro.MavenHibernateCustomer;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class TestSystem {

	private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
			.createEntityManagerFactory("MavenHibernateCustomer");
	
	public static void main(String[] args) {
		addCustomer(1, "John", "Black");
		addCustomer(2, "Clint", "Eastwood");
		addCustomer(3, "Andrzej", "Pilipiuk");
		addCustomer(4, "Arnold", "Boczek");
		addCustomer(5, "John", "Bravo");
		addCustomer(6, "Karol", "Krol");
		getCustomer(2);
		getCustomers();
		changeFName(5, "Johny");
		getCustomer(5);
		deleteCustomer(1);
		getCustomers();
		
		ENTITY_MANAGER_FACTORY.close();
	}
	
	public static void addCustomer(int id, String fName, String lName) {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction et = null;
		try {
			et = em.getTransaction();
			et.begin();
			Customer customer = new Customer();
			customer.setId(id);
			customer.setFName(fName);
			customer.setLName(lName);
			em.persist(customer);
			et.commit();
		} catch (Exception ex) {
			if(et != null) {
				et.rollback();
			}
			ex.printStackTrace();
		} finally {
			em.close();
		}
	}
	
	public static void getCustomer(int id) {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		String query = "SELECT c FROM Customer c WHERE c.id = :custID";
		
		TypedQuery<Customer> tq = em.createQuery(query, Customer.class);
		tq.setParameter("custID", id);
		Customer customer = null;
		try {
			customer = tq.getSingleResult();
			System.out.println(customer.getFName() + " " + customer.getLName());
		} catch (NoResultException ex) {
			ex.printStackTrace();
		} finally {
			em.close();
		}
	}
	
	public static void getCustomers() {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		String strQuery = "SELECT c FROM Customer c WHERE c.id IS NOT NULL";
		TypedQuery<Customer> tq = em.createQuery(strQuery, Customer.class);
		List<Customer> customers;
		try {
			customers = tq.getResultList();
			customers.forEach(customer->System.out.println(customer.getFName() + 
					" " + customer.getLName()));
		} catch(NoResultException ex) {
			ex.printStackTrace();
		} finally {
			em.close();
		}
	}
	
	public static void changeFName(int id, String fName) {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction et = null;
		Customer customer = null;
		try {
			et = em.getTransaction();
			et.begin();
			customer  = em.find(Customer.class, id);
			customer.setFName(fName);
			em.persist(customer);
			et.commit();
		} catch (Exception ex) {
			if(et != null) {
				et.rollback();
			}
			ex.printStackTrace();
		} finally {
			em.close();
		}
	}
	
	public static void deleteCustomer(int id) {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction et = null;
		Customer customer = null;
		try {
			et = em.getTransaction();
			et.begin();
			customer = em.find(Customer.class, id);
			em.remove(customer);
			et.commit();
		} catch (Exception ex) {
			if(et != null) {
				et.rollback();
			}
			ex.printStackTrace();
		} finally {
			em.close();
		}
	}
}
