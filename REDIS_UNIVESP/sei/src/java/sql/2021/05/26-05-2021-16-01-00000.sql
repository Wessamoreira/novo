alter table configuracaogeralsistema
    drop column if exists possuiintegracaoldap;
alter table configuracaogeralsistema
    drop column if exists hostnameldap;
alter table configuracaogeralsistema
    drop column if exists portaldap;
alter table configuracaogeralsistema
    drop column if exists dnldap;
alter table configuracaogeralsistema
    drop column if exists dcldap;
alter table configuracaogeralsistema
    drop column if exists grupoprincipalldap;
alter table configuracaogeralsistema
    drop column if exists grupoalunoldap;
alter table configuracaogeralsistema
    drop column if exists grupocandidatoldap;
alter table configuracaogeralsistema
    drop column if exists grupoprofessorldap;
alter table configuracaogeralsistema
    drop column if exists grupocoordenadorldap;
alter table configuracaogeralsistema
    drop column if exists grupofuncionarioldap;
alter table configuracaogeralsistema
    drop column if exists senhaldap;
alter table configuracaogeralsistema
    drop column if exists urlldap;
alter table configuracaogeralsistema
    drop column if exists usuarioldap;
alter table configuracaogeralsistema
    drop column if exists formatosenhaldap;

create table if not exists configuracaoldap
(
    codigo                   serial       not null
        constraint pk_configuracaoldap_codigo primary key,
    configuracaogeralsistema integer      not null
        constraint fk_configuracaoldap_configuracaogeralsistema references configuracaogeralsistema,
    hostnameldap             varchar(255) not null,
    portaldap                varchar(255) not null,
    dnldap                   varchar(255) not null,
    senhaldap                varchar(255) not null,
    dcldap                   varchar(255) not null,
    formatosenhaldap         varchar(255) not null,
    dominio                  varchar(255) not null,
    diretorio                varchar(255) not null
);

alter table curso
    add column if not exists configuracaoldap integer
        constraint fk_curso_configuracaoldap references configuracaoldap;
alter table registroldap
    add if not exists configuracaoldap integer
        constraint fk_registroldap_configuracaoldap references configuracaoldap;
alter table registroldap
    add if not exists matricula varchar(20)
        constraint fk_registroldap_matricula references matricula;