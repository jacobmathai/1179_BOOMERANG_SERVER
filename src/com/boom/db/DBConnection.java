package com.boom.db;

import java.util.ArrayList;
import java.util.List;

import com.boom.hotspot.Users;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBConnection {
	public Context context;
	SQLiteDatabase sq;
	
	public DBConnection(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		//sq=this.context.openOrCreateDatabase("boom", SQLiteDatabase.OPEN_READWRITE, null);
		init();
//		insert();
		getAll();
	}
	
	public void init() {
		SQLiteDatabase sq = null;
		try {
			System.out.println("Okkkkkkk");
			sq = context.openOrCreateDatabase("boom",
					SQLiteDatabase.OPEN_READWRITE, null);
			sq.execSQL("create table if not exists names(cip varchar(40),filename varchar(80) ,latlong varchar(60))");
			sq.execSQL("create table if not exists users(username varchar(40),cip varchar(80))");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sq.close();
		}
	}
	
	public int registration(String name,String cip) {
		int i=0;
		Cursor cu1=null;
		try {
			sq=context.openOrCreateDatabase("boom", SQLiteDatabase.OPEN_READWRITE, null);
			System.out.println("select * from users where cip='"+cip+"' and username='"+name+"'");
			
			cu1=sq.rawQuery("select * from users where cip='"+cip+"' and username='"+name+"'", null);
			if(cu1.moveToNext()){
		     System.out.println(cu1.getString(0));
		     i=2;
		    }
			else{
				sq.execSQL("insert into users (username,cip) values('"+name+"','"+cip+"')");
				i=1;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			cu1.close();
			sq.close();
		}
		return i;
	}
	
	
	public List<Users> display() {
		List<Users>userList = new ArrayList<Users>();
		Users users2;
		SQLiteDatabase sq1=context.openOrCreateDatabase("boom", SQLiteDatabase.OPEN_READWRITE, null);
		Cursor cu = null;
		try {
			
			cu=sq1.rawQuery("select * from users",null);
			while(cu.moveToNext())
			{
				users2 = new Users();
				users2.setIp(cu.getString(1));
				users2.setName(cu.getString(0));
				userList.add(users2);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			cu.close();
			sq1.close();
		}
		return userList;
	}
	
	public int insertData(String sql) {
		int stat = 1;
		
		try {
			sq=context.openOrCreateDatabase("boom", SQLiteDatabase.OPEN_READWRITE, null);
			sq.execSQL(sql);
		} catch (Exception e) {
			// TODO: handle exception
			stat=0;
		}finally{
			sq.close();
		}
		return stat;
	}
	
	
	public String search(String keyword) {
		SQLiteDatabase sq1 =null;
		String sql = "select * from names where filename='"+keyword+"'";
		String ownerIp = null;
		Cursor cu1=null;
		try {
			sq1=context.openOrCreateDatabase("boom", SQLiteDatabase.OPEN_READWRITE, null);
			cu1 = sq1.rawQuery(sql, null);
			if(cu1.moveToNext()){
				ownerIp = cu1.getString(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			cu1.close();
			sq1.close();
		}		
		return ownerIp;
	}
	
	public String[] searchBylocation(String latlong) {
		SQLiteDatabase sq1 =null;
		String sql = "select * from names where latlong like '"+latlong+"%'";
		System.out.println("sql **** "+sql);
		String ownerDet[] = null;
		Cursor cu1=null;
		try {
			sq1=context.openOrCreateDatabase("boom", SQLiteDatabase.OPEN_READWRITE, null);
			cu1 = sq1.rawQuery(sql, null);
			if(cu1.moveToNext()){
				System.out.println("Result found *****************");
				ownerDet = new String[2];
				ownerDet[0] = cu1.getString(0);
				ownerDet[1] = cu1.getString(1);
				System.out.println("Result found *****************"+ownerDet[0]+" *** "+ownerDet[1]);
			}else{
				System.out.println(" No Result found *****************");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			cu1.close();
			sq1.close();
		}
		return ownerDet;
	}
	
	public String getAllFIles(String ip){
		String files = "";
		SQLiteDatabase sq1 =null;
		String sql = "select filename from names where cip='"+ip+"'";
		
		Cursor cu1=null;
		try {
			sq1=context.openOrCreateDatabase("boom", SQLiteDatabase.OPEN_READWRITE, null);
			cu1 = sq1.rawQuery(sql, null);
			while (cu1.moveToNext()) {
				files+=cu1.getString(0)+"\n";
			}
		}catch(Exception e){
			
		}finally{
			cu1.close();
			sq1.close();
		}
		return files;
	}
	
	public void insert() {
		String sql = "insert into users values ('192.168.1.154','cerin')";
		try {
			sq=context.openOrCreateDatabase("boom", SQLiteDatabase.OPEN_READWRITE, null);
			sq.execSQL(sql);
		} catch (Exception e) {
			// TODO: handle exception
			
		}finally{
			sq.close();
		}
	}
	
	
	public void getAll() {
		SQLiteDatabase sq1 =null;
		String sql = "select * from names ";
		
		Cursor cu1=null;
		try {
			sq1=context.openOrCreateDatabase("boom", SQLiteDatabase.OPEN_READWRITE, null);
			cu1 = sq1.rawQuery(sql, null);
			while (cu1.moveToNext()) {
				System.out.println("** "+cu1.getString(0)+" ** "+cu1.getString(1)+" ** "+cu1.getString(2));
			}
		}catch(Exception e){
			
		}finally{
			cu1.close();
			sq1.close();
		}
	}

}
