package ru.home.diaryfx.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.home.diaryfx.model.Diary;
import ru.home.diaryfx.model.Tags;


public class TestDiary
{
	private EntityManager em;
	private EntityManagerFactory emf;
	
	@Before
	public void init() {
		emf = Persistence.createEntityManagerFactory("DiaryEMFTest"); 
		em = emf.createEntityManager();
	}
	
	@After
	public void close() {
		em.close();
		emf.close();
	}
	
	@Test
	public void TestImport() {
		Map<Long, Tags> tagsMap = new HashMap<Long, Tags>();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("tags.csv"), "UTF8")))	{
			String line;
			em.getTransaction().begin();
			while((line = br.readLine()) != null) {
				String cols[] = line.split("\t");
				Tags tag = new Tags();
				tag.setTitle(cols[1]);
				em.persist(tag);
				tagsMap.put(Long.valueOf(cols[0]), tag);
			}
			em.getTransaction().commit();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<Long, Set<Tags>> linkMap = new HashMap<Long, Set<Tags>>();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("link.csv"), "UTF8"))) {
			String line;
			while((line = br.readLine()) != null) {
				String cols[] = line.split("\t");
				Long key_d = Long.valueOf(cols[1]);
				Long key_t = Long.valueOf(cols[2]);
				Tags tag = tagsMap.get(key_t);
				if(linkMap.containsKey(key_d)) {
					linkMap.get(key_d).add(tag);
				} else {
					Set<Tags> tlist = new HashSet<Tags>();
					tlist.add(tag);
					linkMap.put(key_d, tlist);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		List<Diary> diaryList = new ArrayList<Diary>();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("diary.csv"), "UTF8"))) {
			String line;
			em.getTransaction().begin();
			while((line = br.readLine()) != null) {
				String cols[] = line.split("\t");
				Diary diary = new Diary();
				diary.setDate(cols[1]);
				diary.setTitle(cols[2]);
				diary.setDescript(new String(decode(cols[3]), "UTF8"));
				diary.setListTags(linkMap.get(Long.valueOf(cols[0])));
				em.persist(diary);
				diaryList.add(diary);
			}
			em.getTransaction().commit();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
			
		List<Diary> dlist = em.createNamedQuery("Diary.findAll", Diary.class).getResultList();
		System.out.println(dlist.size() + "=" + diaryList.size());
		assertTrue(dlist.size() == diaryList.size());		

		List<Tags> tlist = em.createNamedQuery("Tags.findAll", Tags.class).getResultList();
		assertTrue(tlist.size() == tagsMap.size());		
	}
	
	 public static byte[] decode(String dataStr) {

	        if ((dataStr.length() & 0x01) == 0x01)
	            dataStr = new String(dataStr + "0");
	        BigInteger cI = new BigInteger(dataStr, 16);
	        byte[] data = cI.toByteArray();

	        return data;
	    }	
}
