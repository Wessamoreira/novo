/**
 * 
 */
package jobs.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/**
 * @author Rodrigo Wind
 *
 */
public enum JobsEnum {
	
	JOB_ALTERAR_COMISSIONAMENTO_TURMA("Alterar Comissionamento Turma", "jobAlterarComissionamentoTurma", "run"),
	JOB_PROCESSAR_ARQUIVO_RETORNO("Processar Arquivo Retorno", "JobProcessarArquivoRetornoLocalizarContas", "run"),
	JOB_CANCELAR_PIX("Processar Cancelamento Pix", "JobCancelamentoPix", "run"),
	JOB_REMOVER_PRE_MATRICULA("Excluir Pré-Matrícula não Confirmada", "jobRemoverPreMatriculaAluno", "run"),
	JOB_NOTIFICAR_SUSPENSAO_MATRICULA("Notificar e Registrar Supensão Matricula Por Pendência Documentação", "jobNotificacaoSuspensaoMatricula", "realizarNotificacaoMatriculaSuspensa"),
	JOB_ABANDONO_CURSO("Registrar Abandono Curso", "jobAbandonoCurso", "run"),
	JOB_RESERVAS_VENCIDAS("Registrar Reserva Biblioteca Vencida", "jobAlterarReservasVencidas", "run"),
	JOB_ALTERAR_SITUACAO_TURMA_ABERTURA_INAUGURACAO("Registrar Inauguração Turma", "jobAlterarSituacaoTurmaAberturaInaugurada", "run"),
	JOB_ATUALIZAR_ALUNO_CUMPRIU_DISCIPLINAS_REGULARES("Atualizar Aluno Cumpriu Disciplinas Regulares", "jobAtualizacaoAlunoConcluiuDisciplinasRegulares", "run"),
	JOB_ENVIO_BOLETO_ALUNO("Enviar Boleto Aluno", "jobEnvioBoletoAluno", "run"),
	JOB_SERASA_API_GEO_REGISTRAR("Serasa Api Geo Registrar", "jobSerasaApiGeo", "executeRegistarSerasaApiGeo"),
	JOB_SERASA_API_GEO_REMOVER("Serasa Api Geo Remover", "jobSerasaApiGeo", "executeRemocaoSerasaApiGeo"),
	JOB_ESTAGIO_ENSALAMENTO("Job Estágio Ensalamento", "jobEstagioEnsalamento", "run"),
	JOB_TCC_ENSALAMENTO("Job Tcc Ensalamento", "jobTccEnsalamento", "run"),
	JOB_ESTAGIO_DESENSALAMENTO("Job Estágio Desensalamento", "jobEstagioDesensalamento", "run"),
	JOB_TCC_DESENSALAMENTO("Job Tcc Desensalamento", "jobTccDesensalamento", "run"),
	JOB_ESTAGIO_OBRIGATORIO("Job Estágio Obrigatorio", "jobEstagioObrigatorio", "run"),
	JOB_EXCLUIR_REQUERIMENTO_DAFASADO("Indeferir Requerimento Defasado", "jobRemoverRequerimentoAntigosBaseDados", "run"),		
	JOB_BAIXA_CARTAO_CREDITO_DCC("Baixar Recebimento Cartão Crédito DCC", "jobBaixaCartaoCreditoDCC", "run"),
	JOB_NOTIFICAR_CAND_VENC_INSCRICAO("Notificar Candidato Vencimento Inscrição", "jobNotificarCandidatoVencimentoInscricao", "run"),
	JOB_NOTIFICAR_VENC_BOLETO("Notificar Antecedência Vencimento Conta Receber", "jobNotificarAntecedenciaVencimentoContaReceber", "run"),
	JOB_ENVIAR_EMAIL("Enviar Email", "jobEnviarEmail", "realizarEnvioEmail"),
	JOB_NOTIFICAR_ANIVERSARIANTE("Enviar Notificação Aniversariante", "jobNotificacaoAniversariante", "run"),
	JOB_NOTIFICAR_RESPONDENTE_AVALIACAO_INSTITUCIONAL("Notificar Respondente Avaliação Institucional", "jobNotificarRespondenteAvaliacaoInstitucional", "run"),
	JOB_NOTIFICAR_ALUNO_DOWNLOAD_MATERIAL("Notificar Aluno Download Material", "jobNotificarAlunoDownloadMaterial", "run"),
	JOB_ALTERAR_RESERVAS_VENCIDAS("Alterar Reservas Vencidas", "jobAlterarReservasVencidas", "run"),
	JOB_CALCULAR_INDICE_REAJUSTE_PRECO("Calcular Índice Reajuste de Preço", "jobCalculoValorIndiceReajustePreco", "run"),
	JOB_NOTIFICAR_ALUNO_AVISO_DESCONTO("Notificar Aluno Aviso Desconto", "jobNotificarAlunoAvisoDesconto", "run"),
	JOB_NOTIFICAR_NAO_LANCAMENTO_AULA("Notificar Não Lançamento de Aula/Frequencia", "jobNotificarNaoLancamentoAula", "run"),
	JOB_SITUACAO_FUNCIONARIO_CARGO_AFASTADO("Atualizar Situação Funcionário Cargo Afastado", "jobSituacaoFuncionarioCargoAfastado", "run"),
	JOB_SITUACAO_FUNCIONARIO_CARGO_FERIAS("Atualizar Situação Funcionário Cargo Ferias", "jobSituacaoFuncionarioCargoFerias", "run"),
	JOB_BLACKBOARD_APURAR_NOTAS("Blackboard Apuração Notas", "", ""),
	JOB_BLACKBOARD_OPERACAO("Blackboard Operacao", "JobSalaAulaBlackboardOperacao", "executarJobSalaAulaBlackboardOperacao"),
	JOB_BLACKBOARD_CONTAS_SALA("Criar Blackboard Contas Sala", "ThreadClassroomContasLote", "run"),
	JOB_CRIAR_CONTAS_GSUITE("Criar Contas Gsuite", "JobCriarContaGsuite", "run"),
	JOB_CRIAR_GOOGLE_MEET_GSUITE("Criar Google Meet Gsuite", "JobCriarEventoCalendar", "run"),
	JOB_CLASSROOM_OPERACAO("Classromm Operacao", "JobClassroomOperacao", "executarJobClassroomOperacao"),
	JOB_CLASSROOM_CONTAS_GSUITE("Criar Classromm Contas Gsuite", "ThreadClassroomContasLote", "run"),
	JOB_CLASSROOM_DRIVE_GSUITE("Drive Classromm Gsuite", "JobAlterarPermissaoDriver", "executarJobAlterarPermissaoDriver"),
	JOB_EXCLUIR_NEGOCIACAO_CONTA_RECEBER_VENCIDA("Excluir Negociação Conta Receber Vencida", "jobExcluirNegociacaoRecebimentoVencida", "run"),
	JOB_NOTIFICAR_ALUNO_DOCUMENTACAO_PENDENTE("Notificar Aluno Documentação Pendente", "jobNotificacaoAlunoDocumentacaoPendente", "run"),
	JOB_NOTIFICAR_ALUNO_SOLICITOU_AVISO_AULA_FUTURA("Notificar Aluno Solicitou Aula Futura", "jobNotificacaoAlunoSolicitouAvisoAulaFuturo", "run"),
	JOB_REMOVER_USUARIO_INTEGRACAO_MINHABIBLIOTECA("Excluir Alunos Integração Minha Biblioteca", "jobExcluirAlunosIntegracaoMinhaBiblioteca", "run"),
	JOB_CRIAR_CAMPANHA_RECORRENTE("Criar Campanha Recorrente", "jobCriarCampanhaRecorrente", "run"),
    JOB_ALTERAR_SITUACAO_USUARIO_LDAP("Alterar Situação Usuário LDAP", "jobAlterarSituacaoUsuarioLDAP", "run"),
    JOB_REMOVER_CONTA_RECEBER_INSCRICAO_CANDIDATO_INADIMPLENTE("Remover Conta Receber Inscricao Candidato Inadimplente","jobRemoverContaReceberInscricaoCandidatoInadimplente" , "run"),
    JOB_BAIXAR_CARTAO_CREDITO("Job Baixar Cartão de Crédito", "jobBaixaCartaoCreditoDCC", "run"),
	JOB_REGISTRAR_AULA_AUTOMATICAMENTE("Job Registrar Aula Automaticamente", "jobRegistrarAulaAutomaticamente", "executarJobRegistrarAulaAutomaticamente"),
	JOB_VALIDARASSINATURASDOCUMENTOASSINADOATE30DIAS("Job Validar Assinatura Documento Assinado Ate 30 Dias", "jobValidarAssinaturasDocumentoAssinadoPorProvedorCertiSign", "executarJobValidacaoDocumentoAssinadoEnviadosAte30DiasPorProvedorCertiSign"),
	JOB_VALIDARASSINATURASDOCUMENTOASSINADOSUPERIOR30DIAS("Job Validar Assinatura Documento Assinado Superior a 30 Dias TechCert", "jobValidarAssinaturasDocumentoAssinadoPorProvedorCertiSign", "executarJobValidacaoDocumentoAssinadoEnviadosSuperiorA30DiasPorProvedorCertiSign"),
	JOB_VALIDARASSINATURASDOCUMENTOASSINADOATE30DIASTECHCERT("Job Validar Assinatura Documento Assinado Ate 30 Dias TechCert", "jobValidarAssinaturasDocumentoAssinadoPorProvedorTechCert", "executarJobValidacaoDocumentoAssinadoEnviadosAte30DiasPorProvedorTechCert"),
	JOB_VALIDARASSINATURASDOCUMENTOASSINADOSUPERIOR30DIASTECHCERT("Job Validar Assinatura Documento Assinado Superior a 30 Dias TechCert", "jobValidarAssinaturasDocumentoAssinadoPorProvedorTechCert", "executarJobValidacaoDocumentoAssinadoEnviadosSuperiorA30DiasPorProvedorTechCert"),
	JOB_ALTERARSITUACAOMATRICULAINTEGRALIZADA("Job Alterar Situação Matricula Integralizada Para Finalizada", "jobAlterarSituacaoMatriculaIntegralizada", "executarJobAlteracaoSituacaoMatriculaIntegralizadaParaFinalizada"),
	JOB_ATUALIZAR_AJUDA("Job Atualizar Ajuda", "jobAtualizarAjuda", "run"),
	JOB_FECHAMENTO_NOTAS("Job Blackboard Fechamento de Notas", "jobBlackboardFechamentoNota", "run"),
	JOB_NOTIFICAR_ALUNO_MENSAGEM_ATIVACAO_PREMATRICULA("Notificar Aluno Mensagem Ativação Pre-Matrícula", "jobNotificacaoAlunoMensagemAtivacaoPreMatricula", "run"),
	JOB_NOTIFICAR_PERIODO_ENTREGA_RELATORIO_FACILITADOR("Notificar Período Entrega Relatório Final Facilitador", "JobNotificacaoPeriodoEntregaRelatorioFacilitador", "run"),
	JOB_OPERACAO_MENSAGEM_MOODLE("Job Operações Mensagens Moodle", "jobOperacaoMensagemMoodle", "run"),
	JOB_OPERACAO_NOTA_MOODLE("Job Operações Notas Moodle", "jobOperacaoNotaMoodle", "run");
	
	private String name;	
	private String classe;
	private String metodo;

	/**
	 * @param name
	 * @param classe
	 * @param metodo
	 */
	private JobsEnum(String name, String classe, String metodo) {
		this.name = name;
		this.classe = classe;
		this.metodo = metodo;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		if (name == null) {
			name = "";
		}
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the classe
	 */
	public String getClasse() {		
		return classe;
	}
	/**
	 * @param classe the classe to set
	 */
	public void setClasse(String classe) {
		this.classe = classe;
	}
	/**
	 * @return the metodo
	 */
	public String getMetodo() {
		if (metodo == null) {
			metodo = "";
		}
		return metodo;
	}
	/**
	 * @param metodo the metodo to set
	 */
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	
	public static JobsEnum getEnum (String value){
		if(Uteis.isAtributoPreenchido(value)){
	        for(JobsEnum pastaBaseArquivoEnum: JobsEnum.values()){
	        	if(value.equals(pastaBaseArquivoEnum.getName())){
	        		return pastaBaseArquivoEnum;
	        	}
	        }
		}
        return null;
    }
	
}
