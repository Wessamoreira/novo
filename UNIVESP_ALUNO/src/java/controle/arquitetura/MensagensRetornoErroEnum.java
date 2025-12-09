package controle.arquitetura;

import negocio.comuns.utilitarias.Constantes;

public enum MensagensRetornoErroEnum {

	CK_VALIDARUNICIDADEREGISTROAULA("ck_validarunicidaderegistroaula" , "Já existe um registro de aula complementar para esta turma, disciplina e data, neste caso a mesma deve ser alterada"),
	MATRICULA_PKEY("matricula_pkey" , "Já existe uma matrícula cadastrada no sistema com essa número de matrícula."),
	FN_ALTERAR_MATRICULA_ALUNO("fn_alterarmatriculaaluno" , "A nova matrícula informada já existe. Por favor verifique a matricula informada."),
	CK_UNICIDADECONVENIOTIPOPADRAO ("check_unicidade_conveniotipopadrado" , "Já existe um registro de Convênio Marcado como Padrão"),
	UNI_CONTARECEBER_CODORIGEM_TIPOOROGEM_PARCELA_PARCEIRO_MATRICU("unq_contareceber_codorigem_tipoorigem_parcela_parceiro_matricu" , "Já existe um registro de Conta Receber para essa Matrícula com o mesmo Código Origem, Tipo Origem, Parcela e Parceiro."),
	ERRO_INESPERADO("erro_inesperado","Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado."),
	CONTAPAGAR_NRDOCUMENTO("contapagar_nrdocumento","Já existe um registro com este valor para o campo Número Documento."),
	PAIZ_NOME_KEY("paiz_nome_key","Já existe um País cadastrado com este nome."),
	REGISTRO_REFERENCIADO("violates foreign key constraint","Este registro é referenciado por outro cadastro, por isto não pode ser excluído e/ou modificado."),
	CK_MATRICULAPERIODOANOSEMESTRE("check_matriculaperiodo_ano_semestre_matricula" , "já existe uma matrícula ativa , pré-matrícula ou finalizada para esse aluno, nesse curso e nesse ano/semestre!"),
	CK_CENTRORECEITAPRINCIPAL("check_centroreceita_centroreceitaprincipal" , "Não e possivel incluir/alterar o centro de receita, tendo ele mesmo como centro de receita principal."),
	CK_CENTRORESULTADOPRINCIPAL("check_centroresultado_centroresultadoprincipal" , "Não e possivel incluir/alterar o centro de resultado, tendo ele mesmo como centro de resultado principal."),
	CK_CATEGORIADESPESAPRINCIPAL("check_categoriadespesa_categoriadespesaprincipal" , "Não e possivel incluir/alterar a categoria despesa, tendo ele mesmo como categoria despesa principal."),
	UNIQUE_CATEGORIA_DESPESA_IDENTIFICADOR("unique_categoriadespesa_identificadorcategoriadespesa" , "O identificador da Categoria de Despesa gerado já existe."),
	WEB_SERVICE_CONNECTION_REFUSED("connection refused" , "Conexão recusada com Web Service."),
	GOOGLE_PRIMARY_USER_EMAIL("primary_user_email" , "O usuário de e-mail ou o domínio de e-mail fornecido  não é valido."),
	GOOGLE_SOCKET_CLOSE("Socket closed" , "A conexão com a Google foi encerrada de forma inesperada. Por favor tentar novamente daqui alguns instantes."),
	UNABLE_TO_FIND_VALID_CERTIFICATION_PATH_TO_REQUESTED_TARGET("unable to find valid certification path to requested target" , "A url informada de destino do web service não foi localizada."),
	THE_REQUEST_WAS_REJECTED_BECAUSE_THE_URL_WAS_NOT_NORMALIZED("The request was rejected because the URL was not normalized" , "A url informada de destino do web service não foi localizada."),
	CK_CONVENIO("ck_convenio", "Não é possível realizar a alteração, pois existe um convênio com a mesma descrição."),
	CK_NEGOCIACAOCONTARECEBER_CONTARECEBER("check_negociacaocontareceber_contareceber", "Não é possível criar a negociação pois já existe negociação para a(s) conta(s) negociada(s)."),
	UNQ_NEGOCIACAOCONTARECEBER_CONTARECEBER("unq_contarecebernegociado_contareceber", "Não é possível criar a negociação pois já existe negociação para a(s) conta(s) negociada(s)."),
	UNQ_HISTORICONOTAPARCIAL("unq_historiconotaparcial", "Não é possível criar a nota parcial, pois já existe um registro com para esses dados."),
	CK_TIPOREQUERIMENTO_TIPOREQUERIMENTOABRIRDEFERIMENTO("check_tiporequerimento_tiporequerimentoabrirdeferimento", "O Tipo Requerimento a ser aberto ao deferir possui vínculo direto ou indireto com o Tipo Requerimento editado."),
	UNQ_CALENDARIOABERTURAREQUERIMENTO_UNIDADEENSINO("calendarioaberturarequerimento_unidadeensino_key", "Já existe um Calendário de Abertura de Requerimento para a Unidade de Ensino selecionada."),
	CK_DEVOLUCAOCHEQUE_SITUACAOCHEQUEDEVOLUCAO("check_devolucaocheque_situacaochequedevolucao", "Já existe uma devolução para este cheque e a atual situação dele não permite uma nova devolução."),
	UNQ_CONTARECEBERNEGOCIACAORECEBIMENTO_CONTARECEBER("unq_contarecebernegociacaorecebimento_contareceber", "já existe um registro de recebimento para esta conta receber."),
	TG_SALAAULABLACKBOARDPESSOA_EXISTE_ENSALAMENTO("fn_validarsituacaocontareceber", "Já existe para esse email um ensalamento."),
	TG_CONTARECEBER_SITUACAOCONTARECEBER("fn_validarsituacaocontareceber", "A Situação da Conta a Receber está desatualizada. Atualize os dados."),
	TG_MATRICULA_ALUNOCURSOGRADECURRICULAR("fn_matricula_validaralunocursogradecurricular", "Já existe uma matrícula para o Aluno considerando as regras de curso, tipo de matrícula, nível educacional e grade curricular."),
	TG_PESSOA_UNICIDADE_EMAIL("fn_pessoa_validarunicidadeemail", "Não é permitido/possível cadastrar um email que já é utilizado por outra pessoa."),
	FN_REQUERIMENTO_PERMISSAOABERTURAANOSEMESTRE2021_2("fn_validarpermissaoaberturarequerimento_tipo_42_48", "Não foi permitida a abertura do Requerimento! Permissão concedida apenas para os alunos ingressantes em 2021/2."),
	CK_PAISIGUALALUNO("validaralunoigualpai", "O CPF do responsável não pode ser igual ao do aluno."),
	CHECK_VALIDAR_UNICIDADE_CPF("pessoa_check_validarunicidadecpf", "Já existe uma pessoa cadastrado com esse CPF."),
	CHECK_VALIDAR_UNICIDADE_MATRICULA_PROGRAMACAO_FORMATURA_ALUNO("fn_programacaoformaturaaluno_validarunicidadematricula", "Não é possível ter mais de uma programação formatura do aluno com a situação \"Colou Grau\" ou \"Não Informado\""),
	FN_VALIDAR_MATRICULA_PERIODO_DUPLICADA("fn_validarmatriculaperiododuplicada", "");
	
	
	private final String constraint;
	private final String mensagem;
	
	MensagensRetornoErroEnum(String constraint, String mensagem) {
        this.constraint = constraint;
        this.mensagem = mensagem;
    }
	
	public static String getMensagensRetornoErroEnum(String mensagemDetalhada) {
		MensagensRetornoErroEnum[] valores = values();
		
		if (mensagemDetalhada == null) {
			return ERRO_INESPERADO.mensagem;
		}
		
		for (MensagensRetornoErroEnum objEnum : valores) {
            if (mensagemDetalhada.toLowerCase().contains(objEnum.getConstraint())) {
            	if (mensagemDetalhada.contains("table")) {
            		String table = "";
            		table = mensagemDetalhada.substring(mensagemDetalhada.lastIndexOf("table")+6, mensagemDetalhada.length());
					table = table.substring(table.indexOf("\"")+1, table.length());
					table = " ("+table.substring(0, table.indexOf("\""))+")";
					return objEnum.mensagem.concat(table);
				} else if (mensagemDetalhada.contains(Constantes.INICIO_MSG_ERRO) && mensagemDetalhada.contains(Constantes.FIM_MSG_ERRO)) {
					return objEnum.mensagem + mensagemDetalhada.substring(mensagemDetalhada.indexOf(Constantes.INICIO_MSG_ERRO) + 15, mensagemDetalhada.indexOf(Constantes.FIM_MSG_ERRO));
				} else {
					return objEnum.mensagem;
				}
            }
        }
		return mensagemDetalhada;
	}
	
	
	public String getConstraint() {
		return constraint;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
}
