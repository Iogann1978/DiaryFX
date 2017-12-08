package ru.home.diaryfx.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "diary")
@NamedQueries({
@NamedQuery(name="Diary.findAll", query="SELECT d FROM Diary d ORDER BY d.date DESC"),
@NamedQuery(name="Diary.find", query="SELECT d FROM Diary d WHERE UPPER(d.descript) LIKE ?1 ORDER BY d.date DESC")
})
public class Diary implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	private String date;
	private String title;
	@Column(columnDefinition = "LONGVARCHAR")
	private String descript;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "LINK_DIARY_TAGS", joinColumns = {@JoinColumn(name = "DIARY_ID")}, inverseJoinColumns = {@JoinColumn(name = "TAG_ID")})
	private Set<Tags> listTags;

	public Diary() {
		super();
		listTags = new HashSet<Tags>();
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Set<Tags> getListTags() {
		if(listTags == null) {
			listTags = new HashSet<Tags>();
		}
		return listTags;
	}

	public void setListTags(Set<Tags> listTags) {
		this.listTags = listTags;
	}

	@Override
	public String toString() {
		return "Diary [date=" + date + ", title=" + title + "]";
	}
}
