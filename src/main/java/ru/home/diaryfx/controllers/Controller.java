package ru.home.diaryfx.controllers;

import ru.home.diaryfx.dao.DAOService;

public interface Controller {
	public void dispose();
	public void refresh();
	public DAOService getService();
	public void setService(DAOService service);
	public void initialize();
}
