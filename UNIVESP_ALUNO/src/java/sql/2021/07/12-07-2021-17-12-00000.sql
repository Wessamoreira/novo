alter table estagio add column if not exists rgBeneficiario varchar(200);
alter table estagio add column if not exists cpfBeneficiario varchar(50);
alter table estagio add column if not exists nomeBeneficiario varchar(150);
alter table estagio add column if not exists emailBeneficiario varchar(150);
alter table estagio add column if not exists telefoneBeneficiario varchar(150);
alter table estagio add column if not exists cepBeneficiario varchar(50);
alter table estagio add column if not exists cidadeBeneficiario varchar(100);
alter table estagio add column if not exists estadoBeneficiario varchar(100);
alter table estagio add column if not exists numeroBeneficiario varchar(50);
alter table estagio add column if not exists enderecoBeneficiario varchar(150);
alter table estagio add column if not exists complementoBeneficiario varchar(100);
alter table estagio add column if not exists setorBeneficiario varchar(100);

alter table concedente alter column bairro drop not null;
alter table concedente alter column cep type varchar(11);
alter table concedente alter column numero type varchar(50);

alter table estagio alter column cep type varchar(11);
alter table estagio alter column numero type varchar(50);
