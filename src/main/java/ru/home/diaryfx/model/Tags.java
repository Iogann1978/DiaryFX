package ru.home.diaryfx.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "tags")
@NamedQuery(name="Tags.findAll", query="SELECT t FROM Tags t ORDER BY t.title")
public class Tags implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	private String title;
	@ManyToMany(mappedBy = "listTags")
	private Set<Diary> listDiaries;
	
	public Tags() {
		super();
		listDiaries = new HashSet<Diary>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Tags [title=" + title + "]";
	}

	public Set<Diary> getListDiaries() {
		if(listDiaries == null) {
			listDiaries = new HashSet<Diary>();
		}
		return listDiaries;
	}

	public void setListDiaries(Set<Diary> listDiaries) {
		this.listDiaries = listDiaries;
	}
}
