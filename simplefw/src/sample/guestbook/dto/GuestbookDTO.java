package sample.guestbook.dto;

import java.sql.Date;

public class GuestbookDTO {
	private int id;
	private String name;
	private String password;
	private String content;
	private Date regdate;
	private String ip;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Override
	public String toString() {
		return "GuestbookDTO [id=" + id + ", name=" + name + ", password="
				+ password + ", content=" + content + ", regdate=" + regdate
				+ ", ip=" + ip + "]";
	}
	
	
}
