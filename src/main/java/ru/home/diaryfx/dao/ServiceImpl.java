package ru.home.diaryfx.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ServiceImpl implements DAOService {
	private EntityManager em;
	private EntityManagerFactory emf;

	public ServiceImpl() {
		emf = Persistence.createEntityManagerFactory("DiaryEMF"); 
		em = emf.createEntityManager();
	}
	
	public void close() {
		em.close();
		emf.close();
	}
	
	public <T> void insert(T entity) {
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
	}

	public <T> T update(T entity) {
		em.getTransaction().begin();
		T ne = em.merge(entity);
		em.getTransaction().commit();
		return ne;
	}

	public void delete(Object entity) {
		em.getTransaction().begin();
		em.remove(em.merge(entity));
		em.getTransaction().commit();
	}

	public <T> T findById(Class<T> entityClass, long id) {
		return em.find(entityClass, id);
	}

	public <T> List<T> findAll(Class<T> entityClass) {
		String queryName = entityClass.getSimpleName() + ".findAll";
		List<T> list = em.createNamedQuery(queryName, entityClass).getResultList();
		return list;
	}
	
	public <T> List<T> find(Class<T> entityClass, String param)
	{
		String queryName = entityClass.getSimpleName() + ".find";
		List<T> list = em.createNamedQuery(queryName, entityClass).setParameter(1, param).getResultList();
		return list;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
}
