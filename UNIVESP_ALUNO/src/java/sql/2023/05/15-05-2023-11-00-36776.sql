alter table if exists tiporequerimento add column if not exists validaranosemestreingresso boolean default false,
add column if not exists anoingresso varchar(4) default '',
add column if not exists semestreingresso varchar(2) default '';