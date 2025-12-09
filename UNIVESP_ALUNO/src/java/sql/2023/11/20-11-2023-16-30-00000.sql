alter table layoutrelatorioseidecidir add column if not exists layoutPersonalizado boolean default false;
alter table layoutrelatorioseidecidir add column if not exists queryLayoutPersonalizado text;

alter table layoutrelatorioseidecidir add column if not exists apresentarFiltroFixoUnidadeEnsino boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoNivelEducacional boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoCotaIngresso boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoCurso boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoTurno boolean default true; 
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoTurma boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoMatricula boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoTurmaEstudouDisciplina boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoDisciplina boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoFiltroPeriodo boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoPeriodoLetivo boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoAceiteEletronicoContrato boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoFiltroSituacaoAcademica boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoFinanceiraMatricula boolean default true;


alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoCentroReceita boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoFormaPagamento boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoContaCorrente boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoTipoPessoa boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoConsiderarUnidadeFinanceira boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoContaCorrenteRecebimento boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoContaReceber boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoTipoOrigem boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoPeriodoLetivoCentroResultado boolean default true;

alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoCentroResultado  boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoNivelCentroResultado  boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoCategoriaDespesa  boolean default true;

alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoFavorecido boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoContaPagar boolean default true;

alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoTipoRequerimento boolean default true; 
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoResponsavel boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoTurmaReposicao boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoRequerente boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoCoordenador boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoDepartamentoResponsavel boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoTramite boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoRequerimento boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoFinanceiraRequerimento boolean default true;


alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoBiblioteca boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoTipoCatalogo boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoClassificacaoBibliografica boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoTitulo boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSecao boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoAreaConhecimento boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoFormaEntrada boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoDataInicioAquisicao boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoDataFimAquisicao boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoTipo boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoCatalogoPeriodico boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoEmprestimo boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoDataInicioEmprestimo boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoDataFimEmprestimo boolean default true;


alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoProcessoSeletivo boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoDataProvaInicio boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoDataProvaFim boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoResultadoProcessoSeletivo boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoInscricao boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoFinanceiraProcessoSeletivo boolean default true;

alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoCampanha boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoConsultor boolean default true;

alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoComponenteEstagio boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoSituacaoEstagio boolean default true;
alter table layoutrelatorioseidecidir add column if not exists  apresentarFiltroFixoPeriodoEstagio boolean default true;

alter table filtropersonalizado add column obrigatorio Boolean default false;


