create table if not exists registrowebservice (
 codigo serial,
 jsonRecebido text,
 jsonRetorno text,
 servico varchar(250),
 ip varchar(100),
 usuario varchar(300),
 created timestamp default now(),
 codigocreated int,
 nomecreated varchar(300),
 
 constraint pk_registrowebservice_codigo primary key (codigo)
);