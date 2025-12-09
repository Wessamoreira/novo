alter table matricula add column if not exists bolsasAuxilios boolean;
alter table matricula add column if not exists autodeclaracaoPretoPardoIndigena boolean;
alter table matricula add column if not exists normasMatricula boolean;
alter table configuracaoGeralSistema add column if not exists linkNormasMatricula text;
alter table configuracaoGeralSistema add column if not exists textofinalizacaomatriculaonline text;


