package negocio.comuns.academico.enumeradores;

/**
 * 
 * @author Pedro Otimize
 *
 */
public enum TipoOrigemDocumentoAssinadoEnum {
	NENHUM(""), 
	DECLARACAO("Declaração"), 
	CONTRATO("Contrato"), 
	HISTORICO("Histórico"), 
	DIARIO("Diário"), 
	REQUERIMENTO("Requerimento"), 
	IMPOSTO_RENDA("Imposto de Renda"), 
	ATA_RESULTADO_FINAL("Ata Resultados Finais"), 
	EXPEDICAO_DIPLOMA("Expedição de Diploma"), 
	UPLOAD_INSTITUCIONAL("Upload Institucional"),
	ESTAGIO("Estágio"),
	TERMO_ESTAGIO_OBRIGATORIO("Termo Estágio Obrigatório"),
	TERMO_ESTAGIO_NAO_OBRIGATORIO("Termo Estágio Não Obrigatório"),
	TERMO_ADITIVO_ESTAGIO_NAO_OBRIGATORIO("Termo Aditivo Estágio Não Obrigatório"),
	TERMO_RESCISAO_ESTAGIO_NAO_OBRIGATORIO("Termo Rescisão Estágio Não Obrigatório"),
	BOLETIM_ACADEMICO("Boletim Acadêmico"), 
	EMISSAO_CERTIFICADO("Emissão Certificado"), 
	DOCUMENTO_GED("Documento Ged"), 
	DOCUMENTO_ALUNO("Documento Aluno"), 
	DOCUMENTO_PROFESSOR("Documento Funcionário"),
	ATA_COLACAO_GRAU("Ata Colação Grau"),
	PLANO_DE_ENSINO("Plano de Ensino"),
	DIPLOMA_DIGITAL("Diploma Digital"),
	DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL("Documentação Acadêmica para Emissão e Registro"),
	HISTORICO_DIGITAL("Histórico Digital"),
	CURRICULO_ESCOLAR_DIGITAL("Curriculo Escolar Digital");
	
	private final String descricao;
	
	private TipoOrigemDocumentoAssinadoEnum(final String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao(){
		return this.descricao;
	}

	public boolean isDeclaracao() {
		return equals(TipoOrigemDocumentoAssinadoEnum.DECLARACAO);
	}
	
	public boolean isDocumentoAluno() {
		return equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO);
	}
	
	public boolean isDocumentoProfessor() {
		return equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR);
	}
	
	public boolean isDocumentoGED() {
		return equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_GED) 
				|| equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO) 
				|| equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR);
	}

	public boolean isContrato() {
		return equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO);
	}

	public boolean isHistorico() {
		return equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO);
	}

	public boolean isDiario() {
		return equals(TipoOrigemDocumentoAssinadoEnum.DIARIO);
	}

	public boolean isRequerimento() {
		return equals(TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO);
	}

	public boolean isImpostoRenda() {
		return equals(TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA);
	}

	public boolean isAtaResultadoFinal() {
		return equals(TipoOrigemDocumentoAssinadoEnum.ATA_RESULTADO_FINAL);
	}

	public boolean isApresentarVisaoProfessor() {
		return equals(TipoOrigemDocumentoAssinadoEnum.DIARIO);
	}

	public boolean isApresentarVisaoCoordenador() {
		return equals(TipoOrigemDocumentoAssinadoEnum.DECLARACAO) 
				|| equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO)
				|| equals(TipoOrigemDocumentoAssinadoEnum.DIARIO);
	}
	
	public boolean isExpedicaoDiploma() {
		return equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA);
	}
	
	public boolean isUploadIntitucional() {
		return equals(TipoOrigemDocumentoAssinadoEnum.UPLOAD_INSTITUCIONAL);
	}
	
	public boolean isBoletimAcademico() {
		return equals(TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO);
	}
	public boolean isDigitacaoGED() {
		return equals(TipoOrigemDocumentoAssinadoEnum.DECLARACAO)
				|| equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO)
				|| equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO)
				|| equals(TipoOrigemDocumentoAssinadoEnum.IMPOSTO_RENDA)
				|| equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA)
				|| equals(TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO);
	}	
	
	public boolean isEmissaoCertificado() {
		return equals(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO);
	}
	
	public boolean isTermoEstagioObrigatorio() {
		return equals(TipoOrigemDocumentoAssinadoEnum.TERMO_ESTAGIO_OBRIGATORIO);
	}
	
	public boolean isTermoEstagioNaoObrigatorio() {
		return equals(TipoOrigemDocumentoAssinadoEnum.TERMO_ESTAGIO_NAO_OBRIGATORIO);
	}
	
	public boolean isTermoAditivoEstagioNaoObrigatorio() {
		return equals(TipoOrigemDocumentoAssinadoEnum.TERMO_ADITIVO_ESTAGIO_NAO_OBRIGATORIO);
	}
	public boolean isTermoRescisaoEstagioNaoObrigatorio() {
		return equals(TipoOrigemDocumentoAssinadoEnum.TERMO_RESCISAO_ESTAGIO_NAO_OBRIGATORIO);
	}
	
	public boolean isAtaColacaoGrau() {
		return equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU);
	}
	
	public boolean isPlanoDeEnsino(){
		return equals(TipoOrigemDocumentoAssinadoEnum.PLANO_DE_ENSINO);
	}
	
	public boolean isDiplomaDigital() {
		return equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL);
	}
	
	public boolean isHistoricoDigital() {
		return equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL);
	}
	
	public boolean isDocumentacaoAcademicaRegistroDiplomaDigital() {
		return equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL);
	}
	
	public boolean isCurriculoEscolarDigital() {
		return equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL);
	}
	
	public boolean isXmlMec() {
		return isDiplomaDigital() || isHistoricoDigital() || isDocumentacaoAcademicaRegistroDiplomaDigital() || isCurriculoEscolarDigital();
	}
	
	public boolean isGerarArquivoPdfA() {
		return equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO) 
				|| equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_GED) 
				|| equals(TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU);
	}
}