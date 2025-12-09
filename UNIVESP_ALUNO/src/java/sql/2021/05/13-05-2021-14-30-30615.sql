ALTER TABLE contacorrente ALTER COLUMN tokenclienteregistroremessaonline TYPE varchar(300);
ALTER TABLE contacorrente ALTER COLUMN tokenidregistroremessaonline TYPE varchar(300);
ALTER TABLE contacorrente ALTER COLUMN tokenkeyregistroremessaonline TYPE varchar(300);
ALTER TABLE contacorrente add COLUMN if not exists permiterecebimentoBoletoVencidoRemessaOnline  boolean default false;
ALTER TABLE contacorrente add COLUMN if not exists numeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline int;
ALTER TABLE contacorrente add COLUMN if not exists variacaoCarteira  varchar(30);
