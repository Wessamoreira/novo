package negocio.comuns.utilitarias.dominios;

import negocio.comuns.utilitarias.Uteis;

public enum PastaBaseArquivoEnum {

	VIDEO("video", null), 
	VIDEO_TMP("videoTMP", null), 
	IMAGEM("imagem", null), 
	IMAGEM_TMP("imagemTMP", null),
	ARQUIVO("arquivo", null),
	ARQUIVO_TMP("arquivoTMP", null),
	SETRANSP_TMP("setranspTMP", null),
	SETRANSP("setransp", null),
	SERASA_TMP("serasaTMP", null),
	SERASA("serasa", null),
	CENSO("censo", null),
	CENSO_TMP("censoTMP", null),
	CONTABIL("contabil", null),
	CONTABIL_TMP("contabilTMP", null),
	REMESSA("remessa", null),
	REMESSA_TMP("remessaTMP", null),
	REMESSA_PG("remessaPG", null),
	REMESSA_PG_TMP("remessaPGTMP", null),
	COMUM("comum", null),
	COMUM_TMP("comumTMP", null),
	DOCUMENTOS("documentos", null),
	DOCUMENTOS_TMP("documentosTMP", null),
	CERTIFICADO_WEB_SERVICE("certificadoWebService", null),
	CERTIFICADO_WEB_SERVICE_TMP("certificadoWebServiceTMP", null),
	CERTIFICADO_DOCUMENTOS("certificadoDocumentos", null),
	CERTIFICADO_DOCUMENTOS_TMP("certificadoDocumentosTMP", null),
	DOCUMENTOS_ASSINADOS("documentosAssinados", null),
	DOCUMENTOS_ASSINADOS_TMP("documentosAssindadosTMP", null),
	IREPORT("ireport", null),
	IREPORT_TMP("ireportTMP", null),
	CURSO("curso", null),
	CURSO_TMP("cursoTMP", null),
	ASSINATURA("assinatura", null),
	ASSINATURA_TMP("assinaturaTMP", null),
	UNIDADEENSINO("unidadeEnsino", null),
	UNIDADEENSINO_TMP("unidadeEnsinoTMP", null),
	REQUERIMENTOS("requerimentos", null),
	REQUERIMENTOS_TMP("requerimentosTMP", null),
	TRAMITE_COTACAO_COMPRA_TMP("tramiteCotacaoCompraTMP", null),
	TRAMITE_COTACAO_COMPRA("tramiteCotacaoCompra", null),
	ANEXO_EMAIL("anexoEmail", null),
	EAD("EAD", null),
	EAD_TMP("EAD_TMP", null),
	EAD_CONTEUDO("CONTEUDO", PastaBaseArquivoEnum.EAD),
	EAD_QUESTOES("questoes", PastaBaseArquivoEnum.EAD),
	EAD_CONTEUDO_TMP("CONTEUDO_TMP", PastaBaseArquivoEnum.EAD_TMP),
	EAD_CONTEUDO_BACKGROUND("CONTEUDO_BACKGROUND", PastaBaseArquivoEnum.EAD_CONTEUDO),
	EAD_CONTEUDO_BACKGROUND_TMP("CONTEUDO_BACKGROUND_TMP", PastaBaseArquivoEnum.EAD_CONTEUDO_TMP),
	ICONE("ICONE", PastaBaseArquivoEnum.EAD_TMP),    
	OUVIDORIA_TMP("ouvidoriaTMP", null),
	OUVIDORIA("ouvidoria", null),
	OFX_TMP("oxfTMP", null),
	OFX("ofx", null),
	LOGO_UNIDADE_ENSINO_TMP("logoUnidadeEnsinoTMP", null),
	LOGO_UNIDADE_ENSINO("logoUnidadeEnsino", null),
	LAYOUT_ETIQUETA("LAYOUT_ETIQUETA", null),
	FOLLOW_ME("follow_me", null),
	CURRICULUM("curriculum", null),
	CURRICULUM_TMP("curriculum_TMP", null),
	SCANNER("scanner", null),
	TCC_TMP("tcc_TMP", null),
	TCC("tcc", null),
	ATIVIDADECOMPLEMENTAR_TMP("atividadeComplementarTMP", null),
	ATIVIDADECOMPLEMENTAR("atividadeComplementar", null),
	ESTAGIO("ESTAGIO", null),
	ESTAGIO_TMP("ESTAGIO_TMP", null),
	CERTIFICADO_TMP("certificadoTMP", null),
	CERTIFICADO("certificado", null),
	NFE("nfe", null),
	CERTIFICADO_NFE("certificado", null),
	NOTAS_ENVIADAS("notasEnviadas", PastaBaseArquivoEnum.NFE),
	NOTAS_CANCELADAS("notasCanceladas", PastaBaseArquivoEnum.NFE),
	NOTAS_INUTILIZADAS("notasInutilizadas", PastaBaseArquivoEnum.NFE),
	NOTAS_CARTA_CORRECAO("notasCartasCorrecao", PastaBaseArquivoEnum.NFE),
	BACKUP_NFE("notasBackup", PastaBaseArquivoEnum.NFE),
	INDICADOR_AVALIACAO("indicadorAvaliacao", null),
	INDICADOR_AVALIACAO_TMP("indicadorAvaliacaoTMP", null),
	IMAGEM_TEXTOPADRAO("imagemTextoPadrao", null),
	POLITICA_DIVULGACAO_MATRICULA_ONLINE_TMP("imagemPoliticaTMP", null),
	POLITICA_DIVULGACAO_MATRICULA_ONLINE("imagemPolitica", null),
	IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL("imagemFundoCarteiraEstudantil", null), 
	IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL_TMP("imagemFundoCarteiraEstudantilTMP", null),
	ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO("atividadeDiscursivaRespostaAluno", null),
	ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO_TMP("atividadeDiscursivaRespostaAlunoTMP", null),
	ATIVIDADE_DISCURSIVA_MATERIAL_APOIO("atividadeDiscursivaMaterialApoio", null),
	ATIVIDADE_DISCURSIVA_MATERIAL_APOIO_TMP("atividadeDiscursivaMaterialApoioTMP", null),
	PATRIMONIO_TMP("patrimonioTMP", null),
	PATRIMONIO("patrimonio", null),
	INTEGRACAO_FINANCEIRO("integracaoFinanceiro", null), 
	DEVOLUCAO_COMPRA("devolucaoCompra", null),
	DEVOLUCAO_COMPRA_TMP("devolucaoCompraTMP", null),
	AVALIACAO_INSTITUCIONAL_TMP("avaliacaoInstitucionalTMP", null),
	DIGITALIZACAO_GED("digitalizacaoGed", null),
	DIGITALIZACAO_GED_TMP("digitalizacaoGedTMP", null),
	CERTIFICADOSINSCRICOES("certificadosInscricoes", null),
	CERTIFICADOSINSCRICOES_TMP("certificadosInscricoesTMP", null),
	ATIVIDADE_EXTRA_CLASSE_PROFESSOR_TMP("ativiadeExtraClasseProfessorTMP", null),
	ARQUIVOSBILIOTECAEXTERNA("arquivosBibliotecaExterna", null),
	ARQUIVOSBILIOTECAEXTERNA_TMP("arquivosBibliotecaExternaTMP", null),
	PERGUNTA_RESPOSTA_ORIGEM("perguntaRespostaOrigem", null),
	PERGUNTA_RESPOSTA_ORIGEM_TMP("perguntaRespostaOrigemTMP", null),
	CONTA_PAGAR("contaPagar",null),
	CONTA_PAGAR_TMP("contaPagarTMP",null),
	PROCESSO_SELETIVO_QUESTOES("processoSeletivoQuestao", null), 
	BANNER_LOGIN_TMP("bannerLoginTMP",null),
	BANNER_LOGIN("bannerLogin",null),
	LAYOUT_HISTORICO("layoutHistorico", null),
	LAYOUT_HISTORICO_TMP("layoutHistoricoTMP", null),
	LAYOUT_ATA_RESULTADOS_FINAIS("layoutAtaResultadosFinais", null),
	LAYOUT_ATA_RESULTADOS_FINAIS_TMP("layoutAtaResultadosFinaisTMP", null),
	ENADE("enade", null),
	ARQUIVO_IMPORTACAO_CID ("ArquivoImportacaoCid", null),
	ARQUIVO_IMPORTACAO_CID_TMP ("ArquivoImportacaoCid", null);
	
	
	
	
	String valor;
	String descricao;
	PastaBaseArquivoEnum diretorioSuperior;

	String value;

	private PastaBaseArquivoEnum(String value, PastaBaseArquivoEnum diretorioSuperior) {
		this.value = value;
		this.diretorioSuperior = diretorioSuperior;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
    public PastaBaseArquivoEnum getDiretorioSuperior() {
		return diretorioSuperior;
	}

	public void setDiretorioSuperior(PastaBaseArquivoEnum diretorioSuperior) {
		this.diretorioSuperior = diretorioSuperior;
	}

	public static PastaBaseArquivoEnum toString(String value){
		if(Uteis.isAtributoPreenchido(value)){
	        for(PastaBaseArquivoEnum pastaBaseArquivoEnum:values()){
	        	if((value.startsWith("/") || value.contains(":")) &&   
	        			((value.contains("TMP") && pastaBaseArquivoEnum.getValue().contains("TMP") && value.contains(pastaBaseArquivoEnum.getValue()))
	        			  || (!value.contains("TMP") && !pastaBaseArquivoEnum.getValue().contains("TMP") && value.contains(pastaBaseArquivoEnum.getValue())))){
	        		return pastaBaseArquivoEnum;
	        	}else if(pastaBaseArquivoEnum.getValue().equals(value)){
	                return pastaBaseArquivoEnum;
	            }
	        }
		}
        return null;
    }
	
	public static PastaBaseArquivoEnum obterPastaBaseEnum(String urlBase, String pastaBase) {		
		for(PastaBaseArquivoEnum pastaBaseArquivoEnum: PastaBaseArquivoEnum.values()) {
			if(!pastaBaseArquivoEnum.getValue().endsWith("TMP")
				&& !pastaBase.contains(pastaBaseArquivoEnum.getValue()+"TMP")
				&& pastaBase.contains(pastaBaseArquivoEnum.getValue())) {				
				return pastaBaseArquivoEnum;
			}
		}
		return null;
	}
}
