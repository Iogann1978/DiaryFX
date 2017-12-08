package ru.home.diaryfx.dao;

import java.util.List;

public interface DAOService {
	public <T> void insert(T entity);
	public <T> T update(T entity);
	public <T> void delete(T entity);
	public <T> T findById(Class<T> entityClass, long id);
	public <T> List<T> findAll(Class<T> entityClass);
	public <T> List<T> find(Class<T> entityClass, String param);
	public void close();
}
