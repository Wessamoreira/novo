alter table if exists gradecurricularEstagio
add column if not exists informarSupervisorEstagio bool default false;

alter table if exists tipoConcedente
add column if not exists codigoMECObrigatorio bool default false,
add column if not exists permitirCadastroConcedente bool default true;

alter table if exists concedente
add column if not exists codigoescolamec varchar(255) default '';

alter table if exists estagio
add column if not exists nomeSupervisor varchar(255) default '',
add column if not exists cpfSupervisor varchar(255) default '';