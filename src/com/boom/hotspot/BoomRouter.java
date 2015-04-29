package com.boom.hotspot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.boom.db.DBConnection;

import android.content.Context;
//import com.boomnoticeboard.*;
import android.os.Environment;

public class BoomRouter extends Thread {
	public static boolean isrunning = true;
	public static Context context;
	static int c = 0;
	ArrayList<Socket> clsoc = new ArrayList<Socket>();
	Hashtable<String, Socket> clientHT = new Hashtable<String, Socket>();
	// Hashtable<String, String> usrHT = new Hashtable<String, String>();
	int jobId = 0;
	File f;
	File f1;

	public void run() {
		try {

			f = new File(Environment.getExternalStorageDirectory() + "/boofile");
			if (!f.exists()) {
				f.mkdir();
			}

			ServerSocket srSoc = new ServerSocket(1234);
			while (isrunning) {
				Socket soc = srSoc.accept();
				new Child(soc).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class Child extends Thread {
		Socket csoc = null;

		public Child(Socket s) {
			csoc = s;
		}

		public void run() {
			try {
				InputStream in = csoc.getInputStream();
				OutputStream out = csoc.getOutputStream();
				PrintStream ps1 = new PrintStream(out);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String type = br.readLine();
				if (type.equals("client")) {
					clsoc.add(csoc);
					clientHT.put(csoc.getInetAddress().getHostAddress(), csoc);
				}
				while (isrunning) {
					String str = br.readLine();
					System.out.println("Client Messages>>" + str);
					if (str == null)
						break;
					if (str.equals("exit"))
						break;
					else if (str.equals("Keyword")) {
						String uname = br.readLine();
						DBConnection dbConnection = new DBConnection(context);
						String cip = br.readLine();
						String fileName = br.readLine();
						String latlon = br.readLine();

						String details = cip + "_" + fileName + "_" + latlon
								+ ":";
						System.out.println("Details >>> "+details);

						String sql = "insert into names values ('" + cip
								+ "','" + fileName + "','" + latlon + "')";
						dbConnection.insertData(sql);
						dbConnection.registration(uname, cip);

						f1 = new File(f + "/names.txt");
						if (!f1.exists()) {
							f1.createNewFile();
						}
						FileOutputStream fout = new FileOutputStream(f1, true);
						byte[] bt = details.getBytes();
						fout.write(bt);
						fout.flush();
						c = 1;
						fout.close();
					}

					else if (str.equals("search")) {
						String cip = br.readLine();
						String sword = br.readLine();

						String reply = "no";

						DBConnection dbConnection = new DBConnection(context);
						String ownerIp = dbConnection.search(sword);
						if (ownerIp != null) {
								
						}

						FileInputStream fin = new FileInputStream(f	+ "/names.txt");
						byte[] bt = new byte[fin.available()];
						int ch = 0;
						
						while ((ch = fin.read(bt)) != -1) {
							reply = new String(bt);
						}
						fin.close();
						
						StringTokenizer st = new StringTokenizer(reply, ":");
						while (st.hasMoreElements()) {
							String[] ar = st.nextElement().toString()
									.split("_");
							System.out.println("ar = " + ar[1]);
							int i = ar[1].lastIndexOf("/");

							String s2 = ar[1].substring(i + 1, ar[1].length());
							System.out.println("File = " + s2);

							int j = s2.lastIndexOf(".");
							String s3 = s2.substring(0, j);
							System.out.println("s3 = " + s3);

							if (s3.equals(sword)) {
								reply = "ok";
								System.out.println("Owner Ip == "+ar[0]);
								Socket soc = clientHT.get(ar[0]);
								PrintStream ps = new PrintStream(soc.getOutputStream());
								ps.println("fetchreply");
								ps.println(s3);
								// String
								// ip=csoc.getInetAddress().getHostAddress();
								ps.println(cip);
								ps.println("exit");
							}
						}
						ps1.println(reply);

					}else if("bylocation".equals(str)){
						String cip = br.readLine();
						String latlong = br.readLine();
						System.out.println("***** lat long **** "+latlong);
						String reply = "no";
						
						String sword = latlong.substring(0, latlong.indexOf("."));
						DBConnection dbConnection = new DBConnection(context);
						String ownerDet[] = dbConnection.searchBylocation(sword);
						if(ownerDet!=null){
							reply = "ok";
							
							String ip = ownerDet[0].trim();
							System.out.println("reply ok "+ip);
							Socket soc1 = clientHT.get(ip);
							System.out.println("Got owner det *** ");
							if(soc1==null){
								System.out.println("socket not found ");
							}
							Socket sc2 = new Socket(ip,4445);
							PrintStream ps2 = new PrintStream(sc2.getOutputStream());
							//ps2.println("fetchreply");
							ps2.println("fetchreply");
							String path = ownerDet[1];
							String fname = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
							System.out.println("file name "+fname);
							ps2.println(fname);
							ps2.println(cip);
							ps2.println("exit");
							
							
							
//							PrintStream ps = new PrintStream(soc1.getOutputStream());
//							ps.println("fetchreply");
//							String path = ownerDet[1];
//							String fname = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
//							ps.println(fname);
//							ps.println(cip);
//							ps.println("exit");
						}
						ps1.println(reply);
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}