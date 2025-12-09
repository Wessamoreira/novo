ALTER TABLE disciplina add column if not exists fontededadosblackboard varchar(20);
ALTER TABLE configuracaoestagioobrigatorio add column if not exists fontededadosblackboardestagio varchar(20);
ALTER TABLE configuracaoestagioobrigatorio add column if not exists fontededadosblackboardcomponenteestagio varchar(20);
ALTER TABLE salaaulablackboardoperacao add column if not exists datasourceid varchar(20);
ALTER TABLE salaaulablackboard add column if not exists datasourceid varchar(20);
