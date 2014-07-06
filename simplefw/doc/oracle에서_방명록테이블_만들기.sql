create table guestbook (
	id number primary key,
	name varchar2(20) not null,
	password varchar2(8),
	content varchar2(4000) not null,
	ip varchar2(20),
	regdate date not null
);

create sequence guestbook_seq;

guestbook에서 사용할 sql문장들.

select id, name, password, content, ip, regdate from
( select rownum r, id, name, password, content, ip, regdate  from
  (
  	select id, name, password, content, ip, regdate from guestbook regdate desc
  )
) where r >= ? and r <= ?

select count(*) from guestbook;

insert into guestbook(id, name, password, content, ip, regdate) values (guestbook_seq.NEXTVAL, ?, ?, ?, ?, sysdate);

delete from guestbook where id = ?

select id, name, password, content, ip, regdate from guestbook where id = ?

select count(*) from guestbook where id = ? and password = ?

update guestbook where name = ?, password = ?, content = ? where id = ?