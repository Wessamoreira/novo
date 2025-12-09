package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoTCCArtefatoVO;
import negocio.comuns.academico.ConfiguracaoTCCMembroBancaVO;
import negocio.comuns.administrativo.enumeradores.TipoTCCEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.QuestaoTCCVO;
import negocio.comuns.utilitarias.Ordenacao;

public class ConfiguracaoTCCVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4787311955270543020L;

	/**
	 * Atributos do Plano de TCC (1ª Etapa)
	 */
	private Boolean controlaPlanoTCC;
	private Integer prazoExecucaoPlanoTCC;
	private Integer prazoRespostaOrientadorPlanoTCC;
	private Boolean permiteInteracaoOrientadorPlanoTCC;
	private Boolean permiteSolicitarOrientador;
	private Double valorPagamentoOrientadorPlanoTCC;
	private Double valorPagamentoCoordenadorPlanoTCC;
	/**
	 * Atributos da Elaboração do TCC (2ª Etapa)
	 */
	private Boolean controlaElaboracaoTCC;
	private Integer prazoExecucaoElaboracaoTCC;
	private Integer prazoRespostaOrientadorElaboracaoTCC;
	private Boolean permiteInteracaoOrientadorElaboracaoTCC;
	private Boolean permiteArquivoApoioElaboracaoTCC;
	private Boolean controlarHistoricoPostagemArquivoElaboracaoTCC;
	private Double valorPagamentoOrientadorElaboracaoTCC;
	private Double valorPagamentoCoordenadorElaboracaoTCC;
	/**
	 * Atributos da Apresentação do TCC (3ª Etapa)
	 */
	private Integer prazoExecucaoApresentacaoTCC;
	private Boolean permiteInteracaoOrientadorApresentacaoTCC;
	private Boolean exigePostagemArquivoFinalAvaliacaoTCC;
	private Boolean permiteArquivoApresentacaoTCC;
	private Boolean permiteArquivoApoioApresentacaoTCC;
	private Double valorPagamentoOrientadorApresentacaoTCC;
	private Double valorPagamentoCoordenadorApresentacaoTCC;
	private Boolean controlarHistoricoPostagemArquivoApresentacaoTCC;
	
	/**
	 * Atributos Gerais
	 */
	private String descricao;
	private TipoTCCEnum tipoTCC;
	private String orientacaoGeral;
	private String orientacaoExtensaoPrazo;
	
	private String politicaApresentarTCCAluno;
	private Boolean ocultarOrientadoVisaoAluno;
	private Boolean validarPendenciaFinanceira;
	private Boolean validarPendenciaDocumento;
	private String extensoesPermitidasParaArquivos;
	private PessoaVO orientadorPadrao;
	private Boolean utilizarProfMinistCoordeConfTurma;
	private List<ConfiguracaoTCCMembroBancaVO> membroBancaPadraoVOs;
	private List<ConfiguracaoTCCMembroBancaVO> membroBancaPadraoCoordVOs;
	private String nomenclaturaUtilizarParaAvaliador;
	private String nomenclaturaUtilizarParaComissao;
	
	private Integer numeroDiaAntesPrimeiraAulaLiberarAcessoTCC;
	private Boolean apagarHistoricoPostagemArquivoAposFinalizacaoTCC;
	private Integer dataBaseVencimentoPagamentoOrientacao;
	private Integer dataBaseProcessamentoPagamentoOrientacao;
	private Integer codigo;
	private List<ConfiguracaoTCCArtefatoVO> configuracaoTCCArtefatoVOs;
	private Boolean controlaFrequencia;
	private Integer numeroDiaAntesEncerramentoEtapaPrimeiroAviso;
	private Integer numeroDiaAntesEncerramentoEtapaSegundoAviso;
	private Integer numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica;
	private String tamanhoWidth;
	private String textoOrientacaoPendenciaDoc;
	private String textoOrientacaoPendenciaFin;
	private String textoOrientacaoAlunoAprov;
	private String textoOrientacaoAlunoReprov;
	private Boolean apresentarContasVencidas;
	private Boolean composicaoNotaQuest;
	private List<QuestaoTCCVO> questaoFormatacaoVOs;
	private List<QuestaoTCCVO> questaoConteudoVOs;
	private Double mediaAprovacao;
	private Double notaMaximaMediaFormatacao;
	private Double notaMaximaMediaConteudo;

	public Boolean getControlaPlanoTCC() {
		if(controlaPlanoTCC == null){
			controlaPlanoTCC = false;
		}
		return controlaPlanoTCC;
	}

	public void setControlaPlanoTCC(Boolean controlaPlanoTCC) {
		this.controlaPlanoTCC = controlaPlanoTCC;
	}

	public Integer getPrazoExecucaoPlanoTCC() {
		if(prazoExecucaoPlanoTCC == null){
			prazoExecucaoPlanoTCC = 0;
		}
		return prazoExecucaoPlanoTCC;
	}

	public void setPrazoExecucaoPlanoTCC(Integer prazoExecucaoPlanoTCC) {
		this.prazoExecucaoPlanoTCC = prazoExecucaoPlanoTCC;
	}

	public Integer getPrazoRespostaOrientadorPlanoTCC() {
		if(prazoRespostaOrientadorPlanoTCC == null){
			prazoRespostaOrientadorPlanoTCC = 0;
		}
		return prazoRespostaOrientadorPlanoTCC;
	}

	public void setPrazoRespostaOrientadorPlanoTCC(Integer prazoRespostaOrientadorPlanoTCC) {
		this.prazoRespostaOrientadorPlanoTCC = prazoRespostaOrientadorPlanoTCC;
	}

	public Boolean getPermiteInteracaoOrientadorPlanoTCC() {
		if(permiteInteracaoOrientadorPlanoTCC == null){
			permiteInteracaoOrientadorPlanoTCC = false;
		}
		return permiteInteracaoOrientadorPlanoTCC;
	}

	public void setPermiteInteracaoOrientadorPlanoTCC(Boolean permiteInteracaoOrientadorPlanoTCC) {
		this.permiteInteracaoOrientadorPlanoTCC = permiteInteracaoOrientadorPlanoTCC;
	}

	public Boolean getPermiteSolicitarOrientador() {
		if(permiteSolicitarOrientador == null){
			permiteSolicitarOrientador = false;
		}
		return permiteSolicitarOrientador;
	}

	public void setPermiteSolicitarOrientador(Boolean permiteSolicitarOrientador) {
		this.permiteSolicitarOrientador = permiteSolicitarOrientador;
	}

	public Double getValorPagamentoOrientadorPlanoTCC() {
		if(valorPagamentoOrientadorPlanoTCC == null){
			valorPagamentoOrientadorPlanoTCC = 0.0;
		}
		return valorPagamentoOrientadorPlanoTCC;
	}

	public void setValorPagamentoOrientadorPlanoTCC(Double valorPagamentoOrientadorPlanoTCC) {
		this.valorPagamentoOrientadorPlanoTCC = valorPagamentoOrientadorPlanoTCC;
	}

	public Double getValorPagamentoCoordenadorPlanoTCC() {
		if(valorPagamentoCoordenadorPlanoTCC == null){
			valorPagamentoCoordenadorPlanoTCC = 0.0;
		}
		return valorPagamentoCoordenadorPlanoTCC;
	}

	public void setValorPagamentoCoordenadorPlanoTCC(Double valorPagamentoCoordenadorPlanoTCC) {
		this.valorPagamentoCoordenadorPlanoTCC = valorPagamentoCoordenadorPlanoTCC;
	}

	public Boolean getControlaElaboracaoTCC() {
		if(controlaElaboracaoTCC == null){
			controlaElaboracaoTCC = false;
		}
		return controlaElaboracaoTCC;
	}

	public void setControlaElaboracaoTCC(Boolean controlaElaboracaoTCC) {
		this.controlaElaboracaoTCC = controlaElaboracaoTCC;
	}

	public Integer getPrazoExecucaoElaboracaoTCC() {
		if(prazoExecucaoElaboracaoTCC == null){
			prazoExecucaoElaboracaoTCC = 0;
		}
		return prazoExecucaoElaboracaoTCC;
	}

	public void setPrazoExecucaoElaboracaoTCC(Integer prazoExecucaoElaboracaoTCC) {
		this.prazoExecucaoElaboracaoTCC = prazoExecucaoElaboracaoTCC;
	}

	public Integer getPrazoRespostaOrientadorElaboracaoTCC() {
		if(prazoRespostaOrientadorElaboracaoTCC == null){
			prazoRespostaOrientadorElaboracaoTCC = 0;
		}
		return prazoRespostaOrientadorElaboracaoTCC;
	}

	public void setPrazoRespostaOrientadorElaboracaoTCC(Integer prazoRespostaOrientadorElaboracaoTCC) {
		this.prazoRespostaOrientadorElaboracaoTCC = prazoRespostaOrientadorElaboracaoTCC;
	}

	public Boolean getPermiteInteracaoOrientadorElaboracaoTCC() {
		if(permiteInteracaoOrientadorElaboracaoTCC == null){
			permiteInteracaoOrientadorElaboracaoTCC = false;
		}
		return permiteInteracaoOrientadorElaboracaoTCC;
	}

	public void setPermiteInteracaoOrientadorElaboracaoTCC(Boolean permiteInteracaoOrientadorElaboracaoTCC) {
		this.permiteInteracaoOrientadorElaboracaoTCC = permiteInteracaoOrientadorElaboracaoTCC;
	}

	public Boolean getPermiteArquivoApoioElaboracaoTCC() {
		if(permiteArquivoApoioElaboracaoTCC == null){
			permiteArquivoApoioElaboracaoTCC = false;
		}
		return permiteArquivoApoioElaboracaoTCC;
	}

	public void setPermiteArquivoApoioElaboracaoTCC(Boolean permiteArquivoApoioElaboracaoTCC) {
		this.permiteArquivoApoioElaboracaoTCC = permiteArquivoApoioElaboracaoTCC;
	}

	public Boolean getControlarHistoricoPostagemArquivoElaboracaoTCC() {
		if(controlarHistoricoPostagemArquivoElaboracaoTCC == null){
			controlarHistoricoPostagemArquivoElaboracaoTCC = false;
		}
		return controlarHistoricoPostagemArquivoElaboracaoTCC;
	}

	public void setControlarHistoricoPostagemArquivoElaboracaoTCC(Boolean controlarHistoricoPostagemArquivoElaboracaoTCC) {
		this.controlarHistoricoPostagemArquivoElaboracaoTCC = controlarHistoricoPostagemArquivoElaboracaoTCC;
	}

	public Double getValorPagamentoOrientadorElaboracaoTCC() {
		if(valorPagamentoOrientadorElaboracaoTCC == null){
			valorPagamentoOrientadorElaboracaoTCC = 0.0;
		}
		return valorPagamentoOrientadorElaboracaoTCC;
	}

	public void setValorPagamentoOrientadorElaboracaoTCC(Double valorPagamentoOrientadorElaboracaoTCC) {
		this.valorPagamentoOrientadorElaboracaoTCC = valorPagamentoOrientadorElaboracaoTCC;
	}

	public Double getValorPagamentoCoordenadorElaboracaoTCC() {
		if(valorPagamentoCoordenadorElaboracaoTCC == null){
			valorPagamentoCoordenadorElaboracaoTCC = 0.0;
		}
		return valorPagamentoCoordenadorElaboracaoTCC;
	}

	public void setValorPagamentoCoordenadorElaboracaoTCC(Double valorPagamentoCoordenadorElaboracaoTCC) {
		this.valorPagamentoCoordenadorElaboracaoTCC = valorPagamentoCoordenadorElaboracaoTCC;
	}

	public Boolean getExigePostagemArquivoFinalAvaliacaoTCC() {
		if(exigePostagemArquivoFinalAvaliacaoTCC == null){
			exigePostagemArquivoFinalAvaliacaoTCC = false;
		}
		return exigePostagemArquivoFinalAvaliacaoTCC;
	}

	public void setExigePostagemArquivoFinalAvaliacaoTCC(Boolean exigePostagemArquivoFinalAvaliacaoTCC) {
		this.exigePostagemArquivoFinalAvaliacaoTCC = exigePostagemArquivoFinalAvaliacaoTCC;
	}

	public Boolean getPermiteArquivoApresentacaoTCC() {
		if(permiteArquivoApresentacaoTCC == null){
			permiteArquivoApresentacaoTCC = false;
		}
		return permiteArquivoApresentacaoTCC;
	}

	public void setPermiteArquivoApresentacaoTCC(Boolean permiteArquivoApresentacaoTCC) {
		this.permiteArquivoApresentacaoTCC = permiteArquivoApresentacaoTCC;
	}

	public Boolean getPermiteArquivoApoioApresentacaoTCC() {
		if(permiteArquivoApoioApresentacaoTCC == null){
			permiteArquivoApoioApresentacaoTCC = false;
		}
		return permiteArquivoApoioApresentacaoTCC;
	}

	public void setPermiteArquivoApoioApresentacaoTCC(Boolean permiteArquivoApoioApresentacaoTCC) {
		this.permiteArquivoApoioApresentacaoTCC = permiteArquivoApoioApresentacaoTCC;
	}

	public Boolean getControlarHistoricoPostagemArquivoApresentacaoTCC() {
		if(controlarHistoricoPostagemArquivoApresentacaoTCC == null){
			controlarHistoricoPostagemArquivoApresentacaoTCC = false;
		}
		return controlarHistoricoPostagemArquivoApresentacaoTCC;
	}

	public void setControlarHistoricoPostagemArquivoApresentacaoTCC(Boolean controlarHistoricoPostagemArquivoApresentacaoTCC) {
		this.controlarHistoricoPostagemArquivoApresentacaoTCC = controlarHistoricoPostagemArquivoApresentacaoTCC;
	}

	public TipoTCCEnum getTipoTCC() {
		if(tipoTCC == null){
			tipoTCC = TipoTCCEnum.AMBOS;
		}
		return tipoTCC;
	}

	public void setTipoTCC(TipoTCCEnum tipoTCC) {
		this.tipoTCC = tipoTCC;
	}

	public String getOrientacaoGeral() {
		if(orientacaoGeral == null){
			orientacaoGeral = "";
		}
		return orientacaoGeral;
	}

	public void setOrientacaoGeral(String orientacaoGeral) {
		this.orientacaoGeral = orientacaoGeral;
	}

	public String getOrientacaoExtensaoPrazo() {
		if(orientacaoExtensaoPrazo == null){
			orientacaoExtensaoPrazo = "";
		}
		return orientacaoExtensaoPrazo;
	}

	public void setOrientacaoExtensaoPrazo(String orientacaoExtensaoPrazo) {
		this.orientacaoExtensaoPrazo = orientacaoExtensaoPrazo;
	}

	public Integer getNumeroDiaAntesPrimeiraAulaLiberarAcessoTCC() {
		if(numeroDiaAntesPrimeiraAulaLiberarAcessoTCC == null){
			numeroDiaAntesPrimeiraAulaLiberarAcessoTCC = 0;
		}
		return numeroDiaAntesPrimeiraAulaLiberarAcessoTCC;
	}

	public void setNumeroDiaAntesPrimeiraAulaLiberarAcessoTCC(Integer numeroDiaAntesPrimeiraAulaLiberarAcessoTCC) {
		this.numeroDiaAntesPrimeiraAulaLiberarAcessoTCC = numeroDiaAntesPrimeiraAulaLiberarAcessoTCC;
	}

	public Boolean getApagarHistoricoPostagemArquivoAposFinalizacaoTCC() {
		if(apagarHistoricoPostagemArquivoAposFinalizacaoTCC == null){
			apagarHistoricoPostagemArquivoAposFinalizacaoTCC = false;
		}
		return apagarHistoricoPostagemArquivoAposFinalizacaoTCC;
	}

	public void setApagarHistoricoPostagemArquivoAposFinalizacaoTCC(Boolean apagarHistoricoPostagemArquivoAposFinalizacaoTCC) {
		this.apagarHistoricoPostagemArquivoAposFinalizacaoTCC = apagarHistoricoPostagemArquivoAposFinalizacaoTCC;
	}

	public Integer getDataBaseVencimentoPagamentoOrientacao() {
		if(dataBaseVencimentoPagamentoOrientacao == null){
			dataBaseVencimentoPagamentoOrientacao = 0;
		}
		return dataBaseVencimentoPagamentoOrientacao;
	}

	public void setDataBaseVencimentoPagamentoOrientacao(Integer dataBaseVencimentoPagamentoOrientacao) {
		this.dataBaseVencimentoPagamentoOrientacao = dataBaseVencimentoPagamentoOrientacao;
	}

	public Integer getDataBaseProcessamentoPagamentoOrientacao() {
		if(dataBaseProcessamentoPagamentoOrientacao == null){
			dataBaseProcessamentoPagamentoOrientacao = 0;
		}
		return dataBaseProcessamentoPagamentoOrientacao;
	}

	public void setDataBaseProcessamentoPagamentoOrientacao(Integer dataBaseProcessamentoPagamentoOrientacao) {
		this.dataBaseProcessamentoPagamentoOrientacao = dataBaseProcessamentoPagamentoOrientacao;
	}

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public List<ConfiguracaoTCCArtefatoVO> getConfiguracaoTCCArtefatoVOs() {
		if (configuracaoTCCArtefatoVOs == null) {
			configuracaoTCCArtefatoVOs = new ArrayList<ConfiguracaoTCCArtefatoVO>();
		}
		return configuracaoTCCArtefatoVOs;
	}

	public void setConfiguracaoTCCArtefatoVOs(List<ConfiguracaoTCCArtefatoVO> configuracaoTCCArtefatoVOs) {
		this.configuracaoTCCArtefatoVOs = configuracaoTCCArtefatoVOs;
	}

	public Integer getPrazoExecucaoApresentacaoTCC() {
		if(prazoExecucaoApresentacaoTCC == null){
			prazoExecucaoApresentacaoTCC = 0;
		}
		return prazoExecucaoApresentacaoTCC;
	}

	public void setPrazoExecucaoApresentacaoTCC(Integer prazoExecucaoApresentacaoTCC) {
		this.prazoExecucaoApresentacaoTCC = prazoExecucaoApresentacaoTCC;
	}

	public String getDescricao() {
		if(descricao == null){
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getPermiteInteracaoOrientadorApresentacaoTCC() {
		if(permiteInteracaoOrientadorApresentacaoTCC == null){
			permiteInteracaoOrientadorApresentacaoTCC = false;
		}
		return permiteInteracaoOrientadorApresentacaoTCC;
	}

	public void setPermiteInteracaoOrientadorApresentacaoTCC(Boolean permiteInteracaoOrientadorApresentacaoTCC) {
		this.permiteInteracaoOrientadorApresentacaoTCC = permiteInteracaoOrientadorApresentacaoTCC;
	}

	public Double getValorPagamentoOrientadorApresentacaoTCC() {
		if(valorPagamentoOrientadorApresentacaoTCC == null){
			valorPagamentoOrientadorApresentacaoTCC = 0.0;
		}
		return valorPagamentoOrientadorApresentacaoTCC;
	}

	public void setValorPagamentoOrientadorApresentacaoTCC(Double valorPagamentoOrientadorApresentacaoTCC) {
		this.valorPagamentoOrientadorApresentacaoTCC = valorPagamentoOrientadorApresentacaoTCC;
	}

	public Double getValorPagamentoCoordenadorApresentacaoTCC() {
		if(valorPagamentoCoordenadorApresentacaoTCC == null){
			valorPagamentoCoordenadorApresentacaoTCC = 0.0;
		}
		return valorPagamentoCoordenadorApresentacaoTCC;
	}

	public void setValorPagamentoCoordenadorApresentacaoTCC(Double valorPagamentoCoordenadorApresentacaoTCC) {
		this.valorPagamentoCoordenadorApresentacaoTCC = valorPagamentoCoordenadorApresentacaoTCC;
	}

	public Boolean getControlaFrequencia() {
		if (controlaFrequencia == null) {
			controlaFrequencia = false;
		}
		return controlaFrequencia;
	}

	public void setControlaFrequencia(Boolean controlaFrequencia) {
		this.controlaFrequencia = controlaFrequencia;
	}

	public Integer getNumeroDiaAntesEncerramentoEtapaPrimeiroAviso() {
		if (numeroDiaAntesEncerramentoEtapaPrimeiroAviso == null) {
			numeroDiaAntesEncerramentoEtapaPrimeiroAviso = 0;
		}
		return numeroDiaAntesEncerramentoEtapaPrimeiroAviso;
	}

	public void setNumeroDiaAntesEncerramentoEtapaPrimeiroAviso(Integer numeroDiaAntesEncerramentoEtapaPrimeiroAviso) {
		this.numeroDiaAntesEncerramentoEtapaPrimeiroAviso = numeroDiaAntesEncerramentoEtapaPrimeiroAviso;
	}

	public Integer getNumeroDiaAntesEncerramentoEtapaSegundoAviso() {
		if (numeroDiaAntesEncerramentoEtapaSegundoAviso == null) {
			numeroDiaAntesEncerramentoEtapaSegundoAviso = 0;
		}
		return numeroDiaAntesEncerramentoEtapaSegundoAviso;
	}

	public void setNumeroDiaAntesEncerramentoEtapaSegundoAviso(Integer numeroDiaAntesEncerramentoEtapaSegundoAviso) {
		this.numeroDiaAntesEncerramentoEtapaSegundoAviso = numeroDiaAntesEncerramentoEtapaSegundoAviso;
	}

	public Integer getNumeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica() {
		if (numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica == null) {
			numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica = 0;
		}
		return numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica;
	}

	public void setNumeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica(Integer numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica) {
		this.numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica = numeroDiaDepoisEncerramentoEtapaReprovacaoAutomatica;
	}

	public String getPoliticaApresentarTCCAluno() {
		if (politicaApresentarTCCAluno == null) {
			politicaApresentarTCCAluno = "DI";
		}
		return politicaApresentarTCCAluno;
	}

	public void setPoliticaApresentarTCCAluno(String politicaApresentarTCCAluno) {
		this.politicaApresentarTCCAluno = politicaApresentarTCCAluno;
	}

	public Boolean getOcultarOrientadoVisaoAluno() {
		if (ocultarOrientadoVisaoAluno == null) {
			ocultarOrientadoVisaoAluno = Boolean.FALSE;
		}
		return ocultarOrientadoVisaoAluno;
	}

	public void setOcultarOrientadoVisaoAluno(Boolean ocultarOrientadoVisaoAluno) {
		this.ocultarOrientadoVisaoAluno = ocultarOrientadoVisaoAluno;
	}

	public String getNomenclaturaUtilizarParaAvaliador() {
		if (nomenclaturaUtilizarParaAvaliador == null) {
			nomenclaturaUtilizarParaAvaliador = "Orientador";
		}
		return nomenclaturaUtilizarParaAvaliador;
	}

	public void setNomenclaturaUtilizarParaAvaliador(String nomenclaturaUtilizarParaAvaliador) {
		this.nomenclaturaUtilizarParaAvaliador = nomenclaturaUtilizarParaAvaliador;
	}

	public String getNomenclaturaUtilizarParaComissao() {
		if (nomenclaturaUtilizarParaComissao == null) {
			nomenclaturaUtilizarParaComissao = "Banca";
		}
		return nomenclaturaUtilizarParaComissao;
	}

	public void setNomenclaturaUtilizarParaComissao(String nomenclaturaUtilizarParaComissao) {
		this.nomenclaturaUtilizarParaComissao = nomenclaturaUtilizarParaComissao;
	}

	public String getExtensoesPermitidasParaArquivos() {
		if (extensoesPermitidasParaArquivos == null) {
			extensoesPermitidasParaArquivos = "";
		}
		return extensoesPermitidasParaArquivos;
	}

	public String getExtensoesPermitidasParaArquivosApresentar() {
		if (extensoesPermitidasParaArquivos == null) {
			extensoesPermitidasParaArquivos = "";
		}
		if (extensoesPermitidasParaArquivos != null && !extensoesPermitidasParaArquivos.isEmpty()) {
			extensoesPermitidasParaArquivos = extensoesPermitidasParaArquivos.replaceAll(";", ",");
			int posicao = extensoesPermitidasParaArquivos.lastIndexOf(",");
			if (posicao > 0) {
				extensoesPermitidasParaArquivos = extensoesPermitidasParaArquivos.substring(0, posicao);
			}
		}
		return extensoesPermitidasParaArquivos;
	}
	
	public void setExtensoesPermitidasParaArquivos(String extensoesPermitidasParaArquivos) {
		this.extensoesPermitidasParaArquivos = extensoesPermitidasParaArquivos;
	}

	public PessoaVO getOrientadorPadrao() {
		if (orientadorPadrao == null) {
			orientadorPadrao = new PessoaVO();
		}
		return orientadorPadrao;
	}

	public void setOrientadorPadrao(PessoaVO orientadorPadrao) {
		this.orientadorPadrao = orientadorPadrao;
	}

	public List<ConfiguracaoTCCMembroBancaVO> getMembroBancaPadraoVOs() {
		if (membroBancaPadraoVOs == null) {
			membroBancaPadraoVOs = new ArrayList<ConfiguracaoTCCMembroBancaVO>(0);
		}
		return membroBancaPadraoVOs;
	}

	public void setMembroBancaPadraoVOs(List<ConfiguracaoTCCMembroBancaVO> membroBancaPadraoVOs) {
		this.membroBancaPadraoVOs = membroBancaPadraoVOs;
	}

	public List<ConfiguracaoTCCMembroBancaVO> getMembroBancaPadraoCoordVOs() {
		if (membroBancaPadraoCoordVOs == null) {
			membroBancaPadraoCoordVOs = new ArrayList<ConfiguracaoTCCMembroBancaVO>(0);
		}
		return membroBancaPadraoCoordVOs;
	}
	
	public void setMembroBancaPadraoCoordVOs(List<ConfiguracaoTCCMembroBancaVO> membroBancaPadraoCoordVOs) {
		this.membroBancaPadraoCoordVOs = membroBancaPadraoCoordVOs;
	}
	

    public void adicionarObjMembroBancaVOs(ConfiguracaoTCCMembroBancaVO obj) throws Exception {
        ConfiguracaoTCCMembroBancaVO.validarDados(obj);
        getMembroBancaPadraoVOs().add(obj);
    }

    public void excluirObjMembroBancaVOs(ConfiguracaoTCCMembroBancaVO obj) throws Exception {
        int index = 0;
        Iterator i = getMembroBancaPadraoVOs().iterator();
        while (i.hasNext()) {
        	ConfiguracaoTCCMembroBancaVO objExistente = (ConfiguracaoTCCMembroBancaVO) i.next();
            if (objExistente.getNome().equals(obj.getNome())) {
            	getMembroBancaPadraoVOs().remove(index);
                return;
            }
            index++;
        }
    }
    
    public void adicionarExtensao(String obj) throws Exception {
    	if (!this.getExtensoesPermitidasParaArquivos().contains(obj)) {
    		if (this.getExtensoesPermitidasParaArquivos().equals("")) {
    			this.setExtensoesPermitidasParaArquivos(obj + ";");
    		} else {
    			this.setExtensoesPermitidasParaArquivos(this.getExtensoesPermitidasParaArquivos() + obj + ";");
    		}
    	}
    }
    
    public void removerExtensao(String obj) throws Exception {
    	if (this.getExtensoesPermitidasParaArquivos().contains(obj)) {
    		this.setExtensoesPermitidasParaArquivos(this.getExtensoesPermitidasParaArquivos().replace(obj + ";", ""));
    	}
    }

	public String getTamanhoWidth() {
		if (tamanhoWidth == null) {
			tamanhoWidth = "640px";
		}
		if (!this.getOcultarOrientadoVisaoAluno()) {
			tamanhoWidth = "100%";
		} else {			
			tamanhoWidth = "100%";
		}
		return tamanhoWidth;
	}

	public void setTamanhoWidth(String tamanhoWidth) {
		this.tamanhoWidth = tamanhoWidth;
	}

	public Boolean getApresentarContasVencidas() {
		if (apresentarContasVencidas == null) {
			apresentarContasVencidas = Boolean.FALSE;
		}
		return apresentarContasVencidas;
	}

	public void setApresentarContasVencidas(Boolean apresentarContasVencidas) {
		this.apresentarContasVencidas = apresentarContasVencidas;
	}
	
	public void alterarOrdemMembroBanca(List<ConfiguracaoTCCMembroBancaVO> configuracaoTCCMembroBancaVOs, ConfiguracaoTCCMembroBancaVO configuracaoTCCMembroBancaVO, boolean subir) throws Exception {
		if ((subir && configuracaoTCCMembroBancaVO.getOrdem() == 0) || (!subir && configuracaoTCCMembroBancaVO.getOrdem() == configuracaoTCCMembroBancaVOs.size())) {
			return;
		}
		int ordem1 = configuracaoTCCMembroBancaVO.getOrdem();
		int ordem2 = configuracaoTCCMembroBancaVO.getOrdem();
		if (subir) {
			ordem2 -= 1;
		} else {
			ordem2 += 1;
		}

		configuracaoTCCMembroBancaVOs.get(ordem1).setOrdem(ordem2);
		configuracaoTCCMembroBancaVOs.get(ordem2).setOrdem(ordem1);
		Ordenacao.ordenarLista(configuracaoTCCMembroBancaVOs, "ordem");

	}

	public Boolean getValidarPendenciaFinanceira() {
		if (validarPendenciaFinanceira == null) {
			validarPendenciaFinanceira = Boolean.FALSE;
		}
		return validarPendenciaFinanceira;
	}

	public void setValidarPendenciaFinanceira(Boolean validarPendenciaFinanceira) {
		this.validarPendenciaFinanceira = validarPendenciaFinanceira;
	}

	public Boolean getValidarPendenciaDocumento() {
		if (validarPendenciaDocumento == null) {
			validarPendenciaDocumento = Boolean.FALSE;
		}
		return validarPendenciaDocumento;
	}

	public void setValidarPendenciaDocumento(Boolean validarPendenciaDocumento) {
		this.validarPendenciaDocumento = validarPendenciaDocumento;
	}

	public Boolean getUtilizarProfMinistCoordeConfTurma() {
		if (utilizarProfMinistCoordeConfTurma == null) {
			utilizarProfMinistCoordeConfTurma = Boolean.TRUE;
		}
		return utilizarProfMinistCoordeConfTurma;
	}

	public void setUtilizarProfMinistCoordeConfTurma(Boolean utilizarProfMinistCoordeConfTurma) {
		this.utilizarProfMinistCoordeConfTurma = utilizarProfMinistCoordeConfTurma;
	}

	public Boolean getComposicaoNotaQuest() {
		if (composicaoNotaQuest == null) {
			composicaoNotaQuest = Boolean.FALSE;
		}
		return composicaoNotaQuest;
	}

	public void setComposicaoNotaQuest(Boolean composicaoNotaQuest) {
		this.composicaoNotaQuest = composicaoNotaQuest;
	}

	public List<QuestaoTCCVO> getQuestaoFormatacaoVOs() {
		if (questaoFormatacaoVOs == null) {
			questaoFormatacaoVOs = new ArrayList<QuestaoTCCVO>();
		}
		return questaoFormatacaoVOs;
	}

	public void setQuestaoFormatacaoVOs(List<QuestaoTCCVO> questaoFormatacaoVOs) {
		this.questaoFormatacaoVOs = questaoFormatacaoVOs;
	}

	public List<QuestaoTCCVO> getQuestaoConteudoVOs() {
		if (questaoConteudoVOs == null) {
			questaoConteudoVOs = new ArrayList<QuestaoTCCVO>();
		}
		return questaoConteudoVOs;
	}

	public void setQuestaoConteudoVOs(List<QuestaoTCCVO> questaoConteudoVOs) {
		this.questaoConteudoVOs = questaoConteudoVOs;
	}

	public Double getMediaAprovacao() {
		if (mediaAprovacao == null) {
			mediaAprovacao = new Double(7.0);
		}
		return mediaAprovacao;
	}

	public void setMediaAprovacao(Double mediaAprovacao) {
		this.mediaAprovacao = mediaAprovacao;
	}

	public Double getNotaMaximaMediaFormatacao() {
		if (notaMaximaMediaFormatacao == null) {
			notaMaximaMediaFormatacao = new Double(0);
		}
		return notaMaximaMediaFormatacao;
	}

	public void setNotaMaximaMediaFormatacao(Double notaMaximaMediaFormatacao) {
		this.notaMaximaMediaFormatacao = notaMaximaMediaFormatacao;
	}

	public Double getNotaMaximaMediaConteudo() {
		if (notaMaximaMediaConteudo == null) {
			notaMaximaMediaConteudo = new Double(0);
		}
		return notaMaximaMediaConteudo;
	}

	public void setNotaMaximaMediaConteudo(Double notaMaximaMediaConteudo) {
		this.notaMaximaMediaConteudo = notaMaximaMediaConteudo;
	}

	public String getTextoOrientacaoAlunoAprov() {
		if (textoOrientacaoAlunoAprov == null) {
			textoOrientacaoAlunoAprov = "";
		}
		return textoOrientacaoAlunoAprov;
	}

	public void setTextoOrientacaoAlunoAprov(String textoOrientacaoAlunoAprov) {
		this.textoOrientacaoAlunoAprov = textoOrientacaoAlunoAprov;
	}

	public String getTextoOrientacaoAlunoReprov() {
		if (textoOrientacaoAlunoReprov == null) {
			textoOrientacaoAlunoReprov = "";
		}
		return textoOrientacaoAlunoReprov;
	}

	public void setTextoOrientacaoAlunoReprov(String textoOrientacaoAlunoReprov) {
		this.textoOrientacaoAlunoReprov = textoOrientacaoAlunoReprov;
	}

	public String getTextoOrientacaoPendenciaDoc() {
		if (textoOrientacaoPendenciaDoc == null) {
			textoOrientacaoPendenciaDoc = "";
		}
		return textoOrientacaoPendenciaDoc;
	}

	public void setTextoOrientacaoPendenciaDoc(String textoOrientacaoPendenciaDoc) {
		this.textoOrientacaoPendenciaDoc = textoOrientacaoPendenciaDoc;
	}

	public String getTextoOrientacaoPendenciaFin() {
		if (textoOrientacaoPendenciaFin == null) {
			textoOrientacaoPendenciaFin = "";
		}
		return textoOrientacaoPendenciaFin;
	}

	public void setTextoOrientacaoPendenciaFin(String textoOrientacaoPendenciaFin) {
		this.textoOrientacaoPendenciaFin = textoOrientacaoPendenciaFin;
	}

	
}