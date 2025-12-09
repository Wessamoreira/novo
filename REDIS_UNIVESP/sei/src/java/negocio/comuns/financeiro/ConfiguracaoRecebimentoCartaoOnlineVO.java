package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum;
import negocio.comuns.financeiro.enumerador.VisaoParcelarEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

/**
 * @author Victor Hugo 08/03/2016 5.0.4.0
 * 
 * Define as regras de funcionamento do recebimento em cartão de crédito
 * 
 * Unicidade entre os campos unidade de ensino, nivel educacional, curso e turma
 * 
 */
public class ConfiguracaoRecebimentoCartaoOnlineVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String descricao;
	private SituacaoEnum situacao;
	private TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum tipoNivelConfiguracaoRecebimentoCartaoOnline;
	/**
	 * Unidade Ensino - Deve ser apresentado para os níveis de configuração: Unidade Ensino (Obrigatorio), Nivel Educacional(Não Obrigatorio), 
	 * Curso (Não Obrigatorio) e Turma (readonly)
	 */
	private UnidadeEnsinoVO unidadeEnsinoVO;
	/**
	 * Deve ser apresentado apenas para o nível de configuração Nível Educacional
	 */
	private TipoNivelEducacional tipoNivelEducacional;
	/**
	 * Deve ser apresentado para os níveis de configuração: Curso (Obrigatorio) e Turma (Readonly)
	 */
	private CursoVO cursoVO;
	/**
	 * Deve ser apresentado apenas para o nível Turma
	 */
	private TurmaVO turmaVO;
	private Boolean usarConfiguracaoVisaoAlunoPais;
	private Boolean usarConfiguracaoVisaoCandidato;
	private Boolean usarConfiguracaoVisaoAdministrativa;

	/** INÍCIO CRÉDITO **/
	
	/**
	 * Caso o valor seja 0 será considerado qualquer valor, caso contrario o sistema deverá restringir a este valor mínimo.
	 */
	private Double valorMinimoRecebimentoCartaoCredito;
	private Boolean permiteReceberContaBiblioteca;
	private Boolean permiteReceberContaContratoReceita;
	private Boolean permiteReceberContaOutros;
	private Boolean permiteReceberContaDevolucaoCheque;
	private Boolean permiteReceberContaInclusaoReposicao;
	private Boolean permiteReceberContaInscricaoProcessoSeletivo;
	private Boolean permiteReceberContaRequerimento;
	private Boolean permitirReceberContaVencida;
	private Integer numeroMaximoDiasReceberContaVencida;
	/**
	 * Quando marcado apresentar campo Habilitar Na Tela de Renegociação On-line - se Marcado ao concluir a renegociação o sistema deverá apresentar a 
	 * opção para receber em cartão as percelas geradas a 
	 * partir da renegociação de forma já parcelada conforme a regra da renegociação.
	 */
	private Boolean permiteReceberContaNegociacao;
	private Boolean habilitarRenegociacaoOnline;
	/**
	 * Quando marcado apresentar campo Habilitar Recebimento Matrícula Na Tela Matrícula On-line - Se marcado ao final da matrícula on-line deve dar a opção de receber a conta.
     * Quando marcado apresentar campo Habilitar Recebimento Matrícula Na Tela Renovação On-line - Se marcado ao final da renovaçao on-line deve dar a opção de receber a conta.
	 */
	private Boolean permiteReceberContaMatricula;
	private Boolean habilitarRecebimentoMatriculaOnline;
	private Boolean habilitarRecebimentoMatriculaRenovacaoOnline;
	/**
	 * Quando marcado apresentar campo Habilitar Recebimento Mensalidade Na Tela Matrícula On-line - Se marcado ao final da matrícula on-line deve dar a opção de receber  
	 * as mesalidades de forma parcelada de acordo com o plano financeiro do aluno.
     * Quando marcado apresentar campo Habilitar Recebimento Mensalidade Na Tela Renovação On-line - Se marcado ao final da renovaçao on-line deve dar a opção de receber 
     * as mesalidades de forma parcelada de acordo com o plano financeiro do aluno.
	 */
	private Boolean permiteReceberContaMensalidade;
	private Boolean permiteReceberContaMaterialDidatico;
	private Boolean habilitarRecebimentoMensalidadeMatriculaOnline;
	private Boolean habilitarRecebimentoMensalidadeRenovacaoOnline;
	
	/** FIM CRÉDITO **/
	
	/** INÍCIO DÉBITO **/
	
	/**
	 * Caso o valor seja 0 será considerado qualquer valor, caso contrario o sistema deverá restringir a este valor mínimo.
	 */
	private Double valorMinimoRecebimentoCartaoDebito;
	private Boolean permiteReceberContaBibliotecaDebito; // não é usado no cartão online !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	private Boolean permiteReceberContaContratoReceitaDebito;
	private Boolean permiteReceberContaOutrosDebito;
	private Boolean permiteReceberContaDevolucaoChequeDebito;
	private Boolean permiteReceberContaInclusaoReposicaoDebito;
	private Boolean permiteReceberContaInscricaoProcessoSeletivoDebito;
	private Boolean permiteReceberContaRequerimentoDebito;
	private Boolean permitirReceberContaVencidaDebito;
	private Integer numeroMaximoDiasReceberContaVencidaDebito;
	/**
	 * Quando marcado apresentar campo Habilitar Na Tela de Renegociação On-line - se Marcado ao concluir a renegociação o sistema deverá apresentar a 
	 * opção para receber em cartão as percelas geradas a 
	 * partir da renegociação de forma já parcelada conforme a regra da renegociação.
	 */
	private Boolean permiteReceberContaNegociacaoDebito;
	private Boolean habilitarRenegociacaoOnlineDebito;
	/**
	 * Quando marcado apresentar campo Habilitar Recebimento Matrícula Na Tela Matrícula On-line - Se marcado ao final da matrícula on-line deve dar a opção de receber a conta.
     * Quando marcado apresentar campo Habilitar Recebimento Matrícula Na Tela Renovação On-line - Se marcado ao final da renovaçao on-line deve dar a opção de receber a conta.
	 */
	private Boolean permiteReceberContaMatriculaDebito;
	private Boolean habilitarRecebimentoMatriculaOnlineDebito;
	private Boolean habilitarRecebimentoMatriculaRenovacaoOnlineDebito;
	/**
	 * Quando marcado apresentar campo Habilitar Recebimento Mensalidade Na Tela Matrícula On-line - Se marcado ao final da matrícula on-line deve dar a opção de receber  
	 * as mesalidades de forma parcelada de acordo com o plano financeiro do aluno.
     * Quando marcado apresentar campo Habilitar Recebimento Mensalidade Na Tela Renovação On-line - Se marcado ao final da renovaçao on-line deve dar a opção de receber 
     * as mesalidades de forma parcelada de acordo com o plano financeiro do aluno.
	 */
	private Boolean permiteReceberContaMensalidadeDebito;
	private Boolean permiteReceberContaMaterialDidaticoDebito;
	private Boolean habilitarRecebimentoMensalidadeMatriculaOnlineDebito;
	private Boolean habilitarRecebimentoMensalidadeRenovacaoOnlineDebito;
	
	//** FIM DÉBITO **/
	
	private PermitirCartaoEnum permitirCartao;
	
	private List<ConfiguracaoFinanceiroCartaoRecebimentoVO> configuracaoFinanceiroCartaoRecebimentoVOs;
	private Date dataCriacao;
	private UsuarioVO reposnsavelCriacao;
	private Date dataAtivacao;
	private UsuarioVO reposnsavelAtivacao;
	private Date dataInativacao;
	private UsuarioVO reposnsavelInativacao;
	private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;
	
	private Boolean apresentarOpcaoRecorrenciaAluno;
	private Boolean utilizarOpcaoRecorrenciaDefaulMarcado;
	private FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoDataBaseCartaoRecorrente;
	private String orientacaoRecorrenciaAluno;
	private Boolean exigirConfirmacaoRecorrencia;
	private String mensagemConfirmacaoRecorrencia;
	

	//Transient
	private String usadoVisao;
	
	public ConfiguracaoRecebimentoCartaoOnlineVO() {

	}
	
	public ConfiguracaoRecebimentoCartaoOnlineVO clone() throws CloneNotSupportedException {
		ConfiguracaoRecebimentoCartaoOnlineVO clone = (ConfiguracaoRecebimentoCartaoOnlineVO) super.clone();
		clone.setCodigo(0);
		clone.setNovoObj(true);
		clone.setDescricao(this.getDescricao() + "- clone");
		clone.setSituacao(SituacaoEnum.EM_CONSTRUCAO);
		clone.setReposnsavelAtivacao(new UsuarioVO());
		clone.setDataAtivacao(new Date());
		clone.setReposnsavelCriacao(new UsuarioVO());
		clone.setDataCriacao(new Date());
		clone.setReposnsavelInativacao(new UsuarioVO());
		clone.setDataInativacao(new Date());
		clone.setConfiguracaoFinanceiroCartaoRecebimentoVOs(new ArrayList<ConfiguracaoFinanceiroCartaoRecebimentoVO>());
		for (ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO : this.getConfiguracaoFinanceiroCartaoRecebimentoVOs()) {
			ConfiguracaoFinanceiroCartaoRecebimentoVO cloneConfiguracaoFinanceiroCartaoRecebimentoVO = configuracaoFinanceiroCartaoRecebimentoVO.clone();
			cloneConfiguracaoFinanceiroCartaoRecebimentoVO.setConfiguracaoRecebimentoCartaoOnlineVO(clone);
			clone.getConfiguracaoFinanceiroCartaoRecebimentoVOs().add(cloneConfiguracaoFinanceiroCartaoRecebimentoVO);
		}
		return clone;
	}

	public Integer getCodigo() {
		if(codigo == null) 
			codigo = 0;
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		if(descricao == null)
			descricao = "";
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public SituacaoEnum getSituacao() {
		if(situacao == null)
			situacao = SituacaoEnum.EM_CONSTRUCAO;
		return situacao;
	}

	public void setSituacao(SituacaoEnum situacao) {
		this.situacao = situacao;
	}

	public TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum getTipoNivelConfiguracaoRecebimentoCartaoOnline() {
		if(tipoNivelConfiguracaoRecebimentoCartaoOnline == null)
			tipoNivelConfiguracaoRecebimentoCartaoOnline = TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum.UNIDADE_ENSINO;
		return tipoNivelConfiguracaoRecebimentoCartaoOnline;
	}

	public void setTipoNivelConfiguracaoRecebimentoCartaoOnline(TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum tipoNivelConfiguracaoRecebimentoCartaoOnline) {
		this.tipoNivelConfiguracaoRecebimentoCartaoOnline = tipoNivelConfiguracaoRecebimentoCartaoOnline;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null)
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public TipoNivelEducacional getTipoNivelEducacional() {
		if(tipoNivelEducacional == null)
			tipoNivelEducacional = TipoNivelEducacional.SUPERIOR;
		return tipoNivelEducacional;
	}

	public void setTipoNivelEducacional(TipoNivelEducacional tipoNivelEducacional) {
		this.tipoNivelEducacional = tipoNivelEducacional;
	}

	public CursoVO getCursoVO() {
		if(cursoVO == null)
			cursoVO = new CursoVO();
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public TurmaVO getTurmaVO() {
		if(turmaVO == null)
			turmaVO = new TurmaVO();
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public Boolean getUsarConfiguracaoVisaoAlunoPais() {
		if(usarConfiguracaoVisaoAlunoPais == null) 
			usarConfiguracaoVisaoAlunoPais = Boolean.FALSE;
		return usarConfiguracaoVisaoAlunoPais;
	}

	public void setUsarConfiguracaoVisaoAlunoPais(Boolean usarConfiguracaoVisaoAlunoPais) {
		this.usarConfiguracaoVisaoAlunoPais = usarConfiguracaoVisaoAlunoPais;
	}

	public Boolean getUsarConfiguracaoVisaoCandidato() {
		if(usarConfiguracaoVisaoCandidato == null)
			usarConfiguracaoVisaoCandidato = Boolean.FALSE;
		return usarConfiguracaoVisaoCandidato;
	}

	public void setUsarConfiguracaoVisaoCandidato(Boolean usarConfiguracaoVisaoCandidato) {
		this.usarConfiguracaoVisaoCandidato = usarConfiguracaoVisaoCandidato;
	}

	public Boolean getUsarConfiguracaoVisaoAdministrativa() {
		if(usarConfiguracaoVisaoAdministrativa == null)
			usarConfiguracaoVisaoAdministrativa = Boolean.FALSE;
		return usarConfiguracaoVisaoAdministrativa;
	}

	public void setUsarConfiguracaoVisaoAdministrativa(Boolean usarConfiguracaoVisaoAdministrativa) {
		this.usarConfiguracaoVisaoAdministrativa = usarConfiguracaoVisaoAdministrativa;
	}

	public Double getValorMinimoRecebimentoCartaoCredito() {
		if(valorMinimoRecebimentoCartaoCredito == null)
			valorMinimoRecebimentoCartaoCredito = 0.0;
		return valorMinimoRecebimentoCartaoCredito;
	}

	public void setValorMinimoRecebimentoCartaoCredito(Double valorMinimoRecebimentoCartaoCredito) {
		this.valorMinimoRecebimentoCartaoCredito = valorMinimoRecebimentoCartaoCredito;
	}

	public Boolean getPermiteReceberContaBiblioteca() {
		if(permiteReceberContaBiblioteca == null)
			permiteReceberContaBiblioteca = Boolean.TRUE;
		return permiteReceberContaBiblioteca;
	}

	public void setPermiteReceberContaBiblioteca(Boolean permiteReceberContaBiblioteca) {
		this.permiteReceberContaBiblioteca = permiteReceberContaBiblioteca;
	}

	public Boolean getPermiteReceberContaContratoReceita() {
		if(permiteReceberContaContratoReceita == null)
			permiteReceberContaContratoReceita = Boolean.TRUE;
		return permiteReceberContaContratoReceita;
	}

	public void setPermiteReceberContaContratoReceita(Boolean permiteReceberContaContratoReceita) {
		this.permiteReceberContaContratoReceita = permiteReceberContaContratoReceita;
	}

	public Boolean getPermiteReceberContaOutros() {
		if(permiteReceberContaOutros == null)
			permiteReceberContaOutros = Boolean.TRUE;
		return permiteReceberContaOutros;
	}

	public void setPermiteReceberContaOutros(Boolean permiteReceberContaOutros) {
		this.permiteReceberContaOutros = permiteReceberContaOutros;
	}

	public Boolean getPermiteReceberContaDevolucaoCheque() {
		if(permiteReceberContaDevolucaoCheque == null)
			permiteReceberContaDevolucaoCheque = Boolean.TRUE;
		return permiteReceberContaDevolucaoCheque;
	}

	public void setPermiteReceberContaDevolucaoCheque(Boolean permiteReceberContaDevolucaoCheque) {
		this.permiteReceberContaDevolucaoCheque = permiteReceberContaDevolucaoCheque;
	}

	public Boolean getPermiteReceberContaInclusaoReposicao() {
		if(permiteReceberContaInclusaoReposicao == null)
			permiteReceberContaInclusaoReposicao = Boolean.TRUE;
		return permiteReceberContaInclusaoReposicao;
	}

	public void setPermiteReceberContaInclusaoReposicao(Boolean permiteReceberContaInclusaoReposicao) {
		this.permiteReceberContaInclusaoReposicao = permiteReceberContaInclusaoReposicao;
	}

	public Boolean getPermiteReceberContaInscricaoProcessoSeletivo() {
		if(permiteReceberContaInscricaoProcessoSeletivo == null)
			permiteReceberContaInscricaoProcessoSeletivo = Boolean.TRUE;
		return permiteReceberContaInscricaoProcessoSeletivo;
	}

	public void setPermiteReceberContaInscricaoProcessoSeletivo(Boolean permiteReceberContaInscricaoProcessoSeletivo) {
		this.permiteReceberContaInscricaoProcessoSeletivo = permiteReceberContaInscricaoProcessoSeletivo;
	}

	public Boolean getPermiteReceberContaRequerimento() {
		if(permiteReceberContaRequerimento == null)
			permiteReceberContaRequerimento = Boolean.TRUE;
		return permiteReceberContaRequerimento;
	}

	public void setPermiteReceberContaRequerimento(Boolean permiteReceberContaRequerimento) {
		this.permiteReceberContaRequerimento = permiteReceberContaRequerimento;
	}

	public Boolean getPermiteReceberContaNegociacao() {
		if(permiteReceberContaNegociacao == null)
			permiteReceberContaNegociacao = Boolean.TRUE;
		return permiteReceberContaNegociacao;
	}

	public void setPermiteReceberContaNegociacao(Boolean permiteReceberContaNegociacao) {
		this.permiteReceberContaNegociacao = permiteReceberContaNegociacao;
	}

	public Boolean getPermiteReceberContaMatricula() {
		if(permiteReceberContaMatricula == null)
			permiteReceberContaMatricula = Boolean.TRUE;
		return permiteReceberContaMatricula;
	}

	public void setPermiteReceberContaMatricula(Boolean permiteReceberContaMatricula) {
		this.permiteReceberContaMatricula = permiteReceberContaMatricula;
	}

	public Boolean getPermiteReceberContaMensalidade() {
		if(permiteReceberContaMensalidade == null)
			permiteReceberContaMensalidade = Boolean.TRUE;
		return permiteReceberContaMensalidade;
	}

	public void setPermiteReceberContaMensalidade(Boolean permiteReceberContaMensalidade) {
		this.permiteReceberContaMensalidade = permiteReceberContaMensalidade;
	}

	public List<ConfiguracaoFinanceiroCartaoRecebimentoVO> getConfiguracaoFinanceiroCartaoRecebimentoVOs() {
		if(configuracaoFinanceiroCartaoRecebimentoVOs == null)
			configuracaoFinanceiroCartaoRecebimentoVOs = new ArrayList<ConfiguracaoFinanceiroCartaoRecebimentoVO>(0);
		return configuracaoFinanceiroCartaoRecebimentoVOs;
	}

	public void setConfiguracaoFinanceiroCartaoRecebimentoVOs(List<ConfiguracaoFinanceiroCartaoRecebimentoVO> configuracaoFinanceiroCartaoRecebimentoVOs) {
		this.configuracaoFinanceiroCartaoRecebimentoVOs = configuracaoFinanceiroCartaoRecebimentoVOs;
	}

	public Date getDataCriacao() {
		if(dataCriacao == null)
			dataCriacao = new Date();
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public UsuarioVO getReposnsavelCriacao() {
		if(reposnsavelCriacao == null)
			reposnsavelCriacao = new UsuarioVO();
		return reposnsavelCriacao;
	}

	public void setReposnsavelCriacao(UsuarioVO reposnsavelCriacao) {
		this.reposnsavelCriacao = reposnsavelCriacao;
	}

	public Date getDataAtivacao() {
		if(dataAtivacao == null)
			dataAtivacao = new Date();
		return dataAtivacao;
	}

	public void setDataAtivacao(Date dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}

	public UsuarioVO getReposnsavelAtivacao() {
		if(reposnsavelAtivacao == null)
			reposnsavelAtivacao = new UsuarioVO();
		return reposnsavelAtivacao;
	}

	public void setReposnsavelAtivacao(UsuarioVO reposnsavelAtivacao) {
		this.reposnsavelAtivacao = reposnsavelAtivacao;
	}

	public Date getDataInativacao() {
		if(dataInativacao == null)
			dataInativacao = new Date();
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	public UsuarioVO getReposnsavelInativacao() {
		if(reposnsavelInativacao == null)
			reposnsavelInativacao = new UsuarioVO();
		return reposnsavelInativacao;
	}

	public void setReposnsavelInativacao(UsuarioVO reposnsavelInativacao) {
		this.reposnsavelInativacao = reposnsavelInativacao;
	}

	public Boolean getHabilitarRenegociacaoOnline() {
		if(habilitarRenegociacaoOnline == null)
			habilitarRenegociacaoOnline = Boolean.FALSE;
		return habilitarRenegociacaoOnline;
	}

	public void setHabilitarRenegociacaoOnline(Boolean habilitarRenegociacaoOnline) {
		this.habilitarRenegociacaoOnline = habilitarRenegociacaoOnline;
	}

	public Boolean getHabilitarRecebimentoMatriculaOnline() {
		if(habilitarRecebimentoMatriculaOnline == null)
			habilitarRecebimentoMatriculaOnline = Boolean.FALSE;
		return habilitarRecebimentoMatriculaOnline;
	}

	public void setHabilitarRecebimentoMatriculaOnline(Boolean habilitarRecebimentoMatriculaOnline) {
		this.habilitarRecebimentoMatriculaOnline = habilitarRecebimentoMatriculaOnline;
	}

	public Boolean getHabilitarRecebimentoMatriculaRenovacaoOnline() {
		if(habilitarRecebimentoMatriculaRenovacaoOnline == null)
			habilitarRecebimentoMatriculaRenovacaoOnline = Boolean.FALSE;
		return habilitarRecebimentoMatriculaRenovacaoOnline;
	}

	public void setHabilitarRecebimentoMatriculaRenovacaoOnline(Boolean habilitarRecebimentoMatriculaRenovacaoOnline) {
		this.habilitarRecebimentoMatriculaRenovacaoOnline = habilitarRecebimentoMatriculaRenovacaoOnline;
	}

	public Boolean getHabilitarRecebimentoMensalidadeMatriculaOnline() {
		if(habilitarRecebimentoMensalidadeMatriculaOnline == null)
			habilitarRecebimentoMensalidadeMatriculaOnline = Boolean.FALSE;
		return habilitarRecebimentoMensalidadeMatriculaOnline;
	}

	public void setHabilitarRecebimentoMensalidadeMatriculaOnline(Boolean habilitarRecebimentoMensalidadeMatriculaOnline) {
		this.habilitarRecebimentoMensalidadeMatriculaOnline = habilitarRecebimentoMensalidadeMatriculaOnline;
	}

	public Boolean getHabilitarRecebimentoMensalidadeRenovacaoOnline() {
		if(habilitarRecebimentoMensalidadeRenovacaoOnline == null)
			habilitarRecebimentoMensalidadeRenovacaoOnline = Boolean.FALSE;
		return habilitarRecebimentoMensalidadeRenovacaoOnline;
	}

	public void setHabilitarRecebimentoMensalidadeRenovacaoOnline(Boolean habilitarRecebimentoMensalidadeRenovacaoOnline) {
		this.habilitarRecebimentoMensalidadeRenovacaoOnline = habilitarRecebimentoMensalidadeRenovacaoOnline;
	}

	public Boolean getPermitirReceberContaVencida() {
		if(permitirReceberContaVencida == null)
			permitirReceberContaVencida = Boolean.TRUE;
		return permitirReceberContaVencida;
	}

	public void setPermitirReceberContaVencida(Boolean permitirReceberContaVencida) {
		this.permitirReceberContaVencida = permitirReceberContaVencida;
	}

	public Integer getNumeroMaximoDiasReceberContaVencida() {
		if(numeroMaximoDiasReceberContaVencida == null)
			numeroMaximoDiasReceberContaVencida = 0;
		return numeroMaximoDiasReceberContaVencida;
	}

	public void setNumeroMaximoDiasReceberContaVencida(Integer numeroMaximoDiasReceberContaVencida) {
		this.numeroMaximoDiasReceberContaVencida = numeroMaximoDiasReceberContaVencida;
	}

	@Override
	public String toString() {
		return "ConfiguracaoRecebimentoCartaoOnlineVO [codigo=" + codigo + ", descricao=" + descricao + ", situacao=" + situacao + ", tipoNivelConfiguracaoRecebimentoCartaoOnline=" + tipoNivelConfiguracaoRecebimentoCartaoOnline + ", unidadeEnsinoVO=" + unidadeEnsinoVO + ", tipoNivelEducacional=" + tipoNivelEducacional + ", cursoVO=" + cursoVO + ", turmaVO=" + turmaVO + ", usarConfiguracaoVisaoAlunoPais=" + usarConfiguracaoVisaoAlunoPais + ", usarConfiguracaoVisaoCandidato=" + usarConfiguracaoVisaoCandidato + ", usarConfiguracaoVisaoAdministrativa=" + usarConfiguracaoVisaoAdministrativa + ", valorMinimoRecebimentoCartaoCredito=" + valorMinimoRecebimentoCartaoCredito + ", permiteReceberContaBiblioteca=" + permiteReceberContaBiblioteca + ", permiteReceberContaContratoReceita=" + permiteReceberContaContratoReceita
				+ ", permiteReceberContaOutros=" + permiteReceberContaOutros + ", permiteReceberContaDevolucaoCheque=" + permiteReceberContaDevolucaoCheque + ", permiteReceberContaInclusaoReposicao=" + permiteReceberContaInclusaoReposicao + ", permiteReceberContaInscricaoProcessoSeletivo=" + permiteReceberContaInscricaoProcessoSeletivo + ", permiteReceberContaRequerimento=" + permiteReceberContaRequerimento + ", permiteReceberContaNegociacao=" + permiteReceberContaNegociacao + ", habilitarRenegociacaoOnline=" + habilitarRenegociacaoOnline + ", permiteReceberContaMatricula=" + permiteReceberContaMatricula + ", habilitarRecebimentoMatriculaOnline=" + habilitarRecebimentoMatriculaOnline + ", habilitarRecebimentoMatriculaRenovacaoOnline=" + habilitarRecebimentoMatriculaRenovacaoOnline + ", permiteReceberContaMensalidade=" + permiteReceberContaMensalidade + ", habilitarRecebimentoMensalidadeMatriculaOnline=" + habilitarRecebimentoMensalidadeMatriculaOnline
				+ ", habilitarRecebimentoMensalidadeRenovacaoOnline=" + habilitarRecebimentoMensalidadeRenovacaoOnline + ", configuracaoFinanceiroCartaoRecebimentoVOs=" + configuracaoFinanceiroCartaoRecebimentoVOs + ", dataCriacao=" + dataCriacao + ", reposnsavelCriacao=" + reposnsavelCriacao + ", dataAtivacao=" + dataAtivacao + ", reposnsavelAtivacao=" + reposnsavelAtivacao + ", dataInativacao=" + dataInativacao + ", reposnsavelInativacao=" + reposnsavelInativacao + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cursoVO == null) ? 0 : cursoVO.hashCode());
		result = prime * result + ((tipoNivelEducacional == null) ? 0 : tipoNivelEducacional.hashCode());
		result = prime * result + ((turmaVO == null) ? 0 : turmaVO.hashCode());
		result = prime * result + ((unidadeEnsinoVO == null) ? 0 : unidadeEnsinoVO.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfiguracaoRecebimentoCartaoOnlineVO other = (ConfiguracaoRecebimentoCartaoOnlineVO) obj;
		if (cursoVO == null) {
			if (other.cursoVO != null)
				return false;
		} else if (!cursoVO.equals(other.cursoVO))
			return false;
		if (tipoNivelEducacional != other.tipoNivelEducacional)
			return false;
		if (turmaVO == null) {
			if (other.turmaVO != null)
				return false;
		} else if (!turmaVO.equals(other.turmaVO))
			return false;
		if (unidadeEnsinoVO == null) {
			if (other.unidadeEnsinoVO != null)
				return false;
		} else if (!unidadeEnsinoVO.equals(other.unidadeEnsinoVO))
			return false;
		return true;
	}

	public String getUsadoVisao() {
		if(usadoVisao == null) {
			usadoVisao = visoesUsado();
		}
		return usadoVisao;
	}

	public void setUsadoVisao(String usadoVisao) {
		this.usadoVisao = usadoVisao;
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
		if(configuracaoFinanceiroVO == null){
			configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();
		}
		return configuracaoFinanceiroVO;
	}

	public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
	}
	
	private String visoesUsado() {
		String visoesUsado = "";
		if(getUsarConfiguracaoVisaoAdministrativa()) {
			visoesUsado += "Visão Administrativa";			
		}
		if(getUsarConfiguracaoVisaoCandidato()) {
			if(visoesUsado.isEmpty()) {
				visoesUsado += "Visão Candidato";	
			} else {
				visoesUsado += " / Visão Candidato";					
			}
		}
		if(getUsarConfiguracaoVisaoAlunoPais()) {
			if(visoesUsado.isEmpty()) {
				visoesUsado += "Visão Aluno";	
			} else {
				visoesUsado += " / Visão Aluno";	
			}
		}
		return visoesUsado;
	}

	public Boolean getPermiteReceberContaMaterialDidatico() {
		if (permiteReceberContaMaterialDidatico == null) {
			permiteReceberContaMaterialDidatico = Boolean.TRUE;
		}
		return permiteReceberContaMaterialDidatico;
	}

	public void setPermiteReceberContaMaterialDidatico(Boolean permiteReceberContaMaterialDidatico) {
		this.permiteReceberContaMaterialDidatico = permiteReceberContaMaterialDidatico;
	}

	public Double getValorMinimoRecebimentoCartaoDebito() {
		if(valorMinimoRecebimentoCartaoDebito == null) {
			valorMinimoRecebimentoCartaoDebito = 0.0;
		}
		return valorMinimoRecebimentoCartaoDebito;
	}

	public void setValorMinimoRecebimentoCartaoDebito(Double valorMinimoRecebimentoCartaoDebito) {
		this.valorMinimoRecebimentoCartaoDebito = valorMinimoRecebimentoCartaoDebito;
	}

	public Boolean getPermiteReceberContaBibliotecaDebito() {
		if(permiteReceberContaBibliotecaDebito == null) {
			permiteReceberContaBibliotecaDebito = Boolean.TRUE;
		}
		return permiteReceberContaBibliotecaDebito;
	}

	public void setPermiteReceberContaBibliotecaDebito(Boolean permiteReceberContaBibliotecaDebito) {
		this.permiteReceberContaBibliotecaDebito = permiteReceberContaBibliotecaDebito;
	}

	public Boolean getPermiteReceberContaContratoReceitaDebito() {
		if(permiteReceberContaContratoReceitaDebito == null) {
			permiteReceberContaContratoReceitaDebito = Boolean.TRUE;
		}
		return permiteReceberContaContratoReceitaDebito;
	}

	public void setPermiteReceberContaContratoReceitaDebito(Boolean permiteReceberContaContratoReceitaDebito) {
		this.permiteReceberContaContratoReceitaDebito = permiteReceberContaContratoReceitaDebito;
	}

	public Boolean getPermiteReceberContaOutrosDebito() {
		if(permiteReceberContaOutrosDebito == null) {
			permiteReceberContaOutrosDebito = Boolean.TRUE;
		}
		return permiteReceberContaOutrosDebito;
	}

	public void setPermiteReceberContaOutrosDebito(Boolean permiteReceberContaOutrosDebito) {
		this.permiteReceberContaOutrosDebito = permiteReceberContaOutrosDebito;
	}

	public Boolean getPermiteReceberContaDevolucaoChequeDebito() {
		if(permiteReceberContaDevolucaoChequeDebito == null) {
			permiteReceberContaDevolucaoChequeDebito = Boolean.TRUE;
		}
		return permiteReceberContaDevolucaoChequeDebito;
	}

	public void setPermiteReceberContaDevolucaoChequeDebito(Boolean permiteReceberContaDevolucaoChequeDebito) {
		this.permiteReceberContaDevolucaoChequeDebito = permiteReceberContaDevolucaoChequeDebito;
	}

	public Boolean getPermiteReceberContaInclusaoReposicaoDebito() {
		if(permiteReceberContaInclusaoReposicaoDebito == null) {
			permiteReceberContaInclusaoReposicaoDebito = Boolean.TRUE;
		}
		return permiteReceberContaInclusaoReposicaoDebito;
	}

	public void setPermiteReceberContaInclusaoReposicaoDebito(Boolean permiteReceberContaInclusaoReposicaoDebito) {
		this.permiteReceberContaInclusaoReposicaoDebito = permiteReceberContaInclusaoReposicaoDebito;
	}

	public Boolean getPermiteReceberContaInscricaoProcessoSeletivoDebito() {
		if(permiteReceberContaInscricaoProcessoSeletivoDebito == null) {
			permiteReceberContaInscricaoProcessoSeletivoDebito = Boolean.TRUE;
		}
		return permiteReceberContaInscricaoProcessoSeletivoDebito;
	}

	public void setPermiteReceberContaInscricaoProcessoSeletivoDebito(
			Boolean permiteReceberContaInscricaoProcessoSeletivoDebito) {
		this.permiteReceberContaInscricaoProcessoSeletivoDebito = permiteReceberContaInscricaoProcessoSeletivoDebito;
	}

	public Boolean getPermiteReceberContaRequerimentoDebito() {
		if(permiteReceberContaRequerimentoDebito == null) {
			permiteReceberContaRequerimentoDebito = Boolean.TRUE;
		}
		return permiteReceberContaRequerimentoDebito;
	}

	public void setPermiteReceberContaRequerimentoDebito(Boolean permiteReceberContaRequerimentoDebito) {
		this.permiteReceberContaRequerimentoDebito = permiteReceberContaRequerimentoDebito;
	}

	public Boolean getPermitirReceberContaVencidaDebito() {
		if(permitirReceberContaVencidaDebito == null) {
			permitirReceberContaVencidaDebito = Boolean.TRUE;
		}
		return permitirReceberContaVencidaDebito;
	}

	public void setPermitirReceberContaVencidaDebito(Boolean permitirReceberContaVencidaDebito) {
		this.permitirReceberContaVencidaDebito = permitirReceberContaVencidaDebito;
	}

	public Integer getNumeroMaximoDiasReceberContaVencidaDebito() {
		if(numeroMaximoDiasReceberContaVencidaDebito == null) {
			numeroMaximoDiasReceberContaVencidaDebito = 0;
		}
		return numeroMaximoDiasReceberContaVencidaDebito;
	}

	public void setNumeroMaximoDiasReceberContaVencidaDebito(Integer numeroMaximoDiasReceberContaVencidaDebito) {
		this.numeroMaximoDiasReceberContaVencidaDebito = numeroMaximoDiasReceberContaVencidaDebito;
	}

	public Boolean getPermiteReceberContaNegociacaoDebito() {
		if(permiteReceberContaNegociacaoDebito == null) {
			permiteReceberContaNegociacaoDebito = Boolean.TRUE;
		}
		return permiteReceberContaNegociacaoDebito;
	}

	public void setPermiteReceberContaNegociacaoDebito(Boolean permiteReceberContaNegociacaoDebito) {
		this.permiteReceberContaNegociacaoDebito = permiteReceberContaNegociacaoDebito;
	}

	public Boolean getHabilitarRenegociacaoOnlineDebito() {
		if(habilitarRenegociacaoOnlineDebito == null) {
			habilitarRenegociacaoOnlineDebito = Boolean.FALSE;
		}
		return habilitarRenegociacaoOnlineDebito;
	}

	public void setHabilitarRenegociacaoOnlineDebito(Boolean habilitarRenegociacaoOnlineDebito) {
		this.habilitarRenegociacaoOnlineDebito = habilitarRenegociacaoOnlineDebito;
	}

	public Boolean getPermiteReceberContaMatriculaDebito() {
		if(permiteReceberContaMatriculaDebito == null) {
			permiteReceberContaMatriculaDebito = Boolean.TRUE;
		}
		return permiteReceberContaMatriculaDebito;
	}

	public void setPermiteReceberContaMatriculaDebito(Boolean permiteReceberContaMatriculaDebito) {
		this.permiteReceberContaMatriculaDebito = permiteReceberContaMatriculaDebito;
	}

	public Boolean getHabilitarRecebimentoMatriculaOnlineDebito() {
		if(habilitarRecebimentoMatriculaOnlineDebito == null) {
			habilitarRecebimentoMatriculaOnlineDebito = Boolean.FALSE;
		}
		return habilitarRecebimentoMatriculaOnlineDebito;
	}

	public void setHabilitarRecebimentoMatriculaOnlineDebito(Boolean habilitarRecebimentoMatriculaOnlineDebito) {
		this.habilitarRecebimentoMatriculaOnlineDebito = habilitarRecebimentoMatriculaOnlineDebito;
	}

	public Boolean getHabilitarRecebimentoMatriculaRenovacaoOnlineDebito() {
		if(habilitarRecebimentoMatriculaRenovacaoOnlineDebito == null) {
			habilitarRecebimentoMatriculaRenovacaoOnlineDebito = Boolean.FALSE;
		}
		return habilitarRecebimentoMatriculaRenovacaoOnlineDebito;
	}

	public void setHabilitarRecebimentoMatriculaRenovacaoOnlineDebito(
			Boolean habilitarRecebimentoMatriculaRenovacaoOnlineDebito) {
		this.habilitarRecebimentoMatriculaRenovacaoOnlineDebito = habilitarRecebimentoMatriculaRenovacaoOnlineDebito;
	}

	public Boolean getPermiteReceberContaMensalidadeDebito() {
		if(permiteReceberContaMensalidadeDebito == null) {
			permiteReceberContaMensalidadeDebito = Boolean.TRUE;
		}
		return permiteReceberContaMensalidadeDebito;
	}

	public void setPermiteReceberContaMensalidadeDebito(Boolean permiteReceberContaMensalidadeDebito) {
		this.permiteReceberContaMensalidadeDebito = permiteReceberContaMensalidadeDebito;
	}

	public Boolean getPermiteReceberContaMaterialDidaticoDebito() {
		if(permiteReceberContaMaterialDidaticoDebito == null) {
			permiteReceberContaMaterialDidaticoDebito = Boolean.TRUE;
		}
		return permiteReceberContaMaterialDidaticoDebito;
	}

	public void setPermiteReceberContaMaterialDidaticoDebito(Boolean permiteReceberContaMaterialDidaticoDebito) {
		this.permiteReceberContaMaterialDidaticoDebito = permiteReceberContaMaterialDidaticoDebito;
	}

	public Boolean getHabilitarRecebimentoMensalidadeMatriculaOnlineDebito() {
		if(habilitarRecebimentoMensalidadeMatriculaOnlineDebito == null) {
			habilitarRecebimentoMensalidadeMatriculaOnlineDebito = Boolean.FALSE;
		}
		return habilitarRecebimentoMensalidadeMatriculaOnlineDebito;
	}

	public void setHabilitarRecebimentoMensalidadeMatriculaOnlineDebito(
			Boolean habilitarRecebimentoMensalidadeMatriculaOnlineDebito) {
		this.habilitarRecebimentoMensalidadeMatriculaOnlineDebito = habilitarRecebimentoMensalidadeMatriculaOnlineDebito;
	}

	public Boolean getHabilitarRecebimentoMensalidadeRenovacaoOnlineDebito() {
		if(habilitarRecebimentoMensalidadeRenovacaoOnlineDebito == null) {
			habilitarRecebimentoMensalidadeRenovacaoOnlineDebito = Boolean.FALSE;
		}
		return habilitarRecebimentoMensalidadeRenovacaoOnlineDebito;
	}

	public void setHabilitarRecebimentoMensalidadeRenovacaoOnlineDebito(
			Boolean habilitarRecebimentoMensalidadeRenovacaoOnlineDebito) {
		this.habilitarRecebimentoMensalidadeRenovacaoOnlineDebito = habilitarRecebimentoMensalidadeRenovacaoOnlineDebito;
	}

	public PermitirCartaoEnum getPermitirCartao() {
		if (permitirCartao == null) {
			permitirCartao = PermitirCartaoEnum.CREDITO;
		}
		return permitirCartao;
	}

	public void setPermitirCartao(PermitirCartaoEnum permitirCartao) {
		this.permitirCartao = permitirCartao;
	}
	
	public boolean getIsPermitirCartaoDebito() {
		return getPermitirCartao().equals(PermitirCartaoEnum.AMBOS) || getPermitirCartao().equals(PermitirCartaoEnum.DEBITO);
	}
	
	public boolean getIsPermitirCartaoCredito() {
		return getPermitirCartao().equals(PermitirCartaoEnum.AMBOS) || getPermitirCartao().equals(PermitirCartaoEnum.CREDITO);
	}
	
	public Integer getQtdeParcelasPermitida(double valor, Date vencimento, UsuarioVO usuarioLogado, List<TipoOrigemContaReceber> listaTipoOrigemContaReceberEnum) {
		int parcelas = realizarEscolhaConfiguracaoFinanceiroCartaoRecebimentoAdequada(valor, vencimento, usuarioLogado, listaTipoOrigemContaReceberEnum).getParcelasAte();
		if (parcelas < 1) {
			parcelas = 1;
		}
		return parcelas;
	}
	
	public TipoFinanciamentoEnum getTipoFinanciamentoPermitido(double valor, Date vencimento, UsuarioVO usuarioLogado, List<TipoOrigemContaReceber> listaTipoOrigemContaReceberEnum) {
		return realizarEscolhaConfiguracaoFinanceiroCartaoRecebimentoAdequada(valor, vencimento, usuarioLogado, listaTipoOrigemContaReceberEnum).getTipoFinanciamentoEnum();
	}
	
	public ConfiguracaoFinanceiroCartaoRecebimentoVO realizarEscolhaConfiguracaoFinanceiroCartaoRecebimentoAdequada(double valor, Date vencimento, UsuarioVO usuarioLogado, List<TipoOrigemContaReceber> listaTipoOrigemContaReceberEnum) {
		ConfiguracaoFinanceiroCartaoRecebimentoVO conf = new ConfiguracaoFinanceiroCartaoRecebimentoVO();
		for (ConfiguracaoFinanceiroCartaoRecebimentoVO obj : getConfiguracaoFinanceiroCartaoRecebimentoVOs()) {
			if (valor >= obj.getValorMinimo() &&
					(VisaoParcelarEnum.TODAS.equals(obj.getVisao()) ||
					 (VisaoParcelarEnum.ADMINISTRATIVA.equals(obj.getVisao()) && usuarioLogado.getIsApresentarVisaoAdministrativa()) ||
					 (VisaoParcelarEnum.ALUNO_E_PAIS.equals(obj.getVisao()) && !usuarioLogado.getIsApresentarVisaoAdministrativa()))) {
				long diasAtraso = Uteis.nrDiasEntreDatas(new Date(), vencimento);
				if ( ((obj.getQtdeDiasInicialParcelarContaVencida() == 0 && diasAtraso <= 0) ||
					(diasAtraso >= obj.getQtdeDiasInicialParcelarContaVencida() && diasAtraso <= obj.getQtdeDiasFinalParcelarContaVencida())) &&
					validarTiposOrigensContaReceber(obj, listaTipoOrigemContaReceberEnum)) {
					if (obj.getValorMinimo() >= conf.getValorMinimo()) {
						conf = obj;
					}
				}
			}
		}
		return conf;
	}

	private boolean validarTiposOrigensContaReceber(ConfiguracaoFinanceiroCartaoRecebimentoVO obj, List<TipoOrigemContaReceber> listaTipoOrigemContaReceberEnum) {
		if (!Uteis.isAtributoPreenchido(listaTipoOrigemContaReceberEnum)) {
			return true;
		}
		for (TipoOrigemContaReceber tipoOrigemContaReceber : listaTipoOrigemContaReceberEnum) {
			if((tipoOrigemContaReceber.isMatricula() && !obj.getTipoOrigemContaReceberMatricula())
					|| (tipoOrigemContaReceber.isIncricaoProcessoSeletivo() && !obj.getTipoOrigemContaReceberInscricaoProcessoSeletivo())
					|| (tipoOrigemContaReceber.isRequerimento() && !obj.getTipoOrigemContaReceberRequerimento())
					|| (tipoOrigemContaReceber.isBiblioteca() && !obj.getTipoOrigemContaReceberBiblioteca())
					|| (tipoOrigemContaReceber.isMensalidade() && !obj.getTipoOrigemContaReceberMensalidade())
					|| (tipoOrigemContaReceber.isNegociacao() && !obj.getTipoOrigemContaReceberNegociacao())
					|| (tipoOrigemContaReceber.isOutros() && !obj.getTipoOrigemContaReceberOutros())
					|| (tipoOrigemContaReceber.isMaterialDidatico() && !obj.getTipoOrigemContaReceberMaterialDidatico())
					|| (tipoOrigemContaReceber.isDevolucaoCheque() && !obj.getTipoOrigemContaReceberDevolucaoCheque())
					|| (tipoOrigemContaReceber.isBolsaCusteadaConvenio() && !obj.getTipoOrigemContaReceberBolsaCusteada())
					|| (tipoOrigemContaReceber.isContratoReceita() && !obj.getTipoOrigemContaReceberContratoReceita())
					|| (tipoOrigemContaReceber.isInclusaoReposicao() && !obj.getTipoOrigemContaReceberInclusaoReposicao())) {
				return false;
			}
		}
		return true;
	}
	
	public Boolean getApresentarOpcaoRecorrenciaAluno() {
		if (apresentarOpcaoRecorrenciaAluno == null) {
			apresentarOpcaoRecorrenciaAluno = true;
		}
		return apresentarOpcaoRecorrenciaAluno;
	}

	public void setApresentarOpcaoRecorrenciaAluno(Boolean apresentarOpcaoRecorrenciaAluno) {
		this.apresentarOpcaoRecorrenciaAluno = apresentarOpcaoRecorrenciaAluno;
	}

	public Boolean getUtilizarOpcaoRecorrenciaDefaulMarcado() {
		if (utilizarOpcaoRecorrenciaDefaulMarcado == null) {
			utilizarOpcaoRecorrenciaDefaulMarcado = true;
		}
		return utilizarOpcaoRecorrenciaDefaulMarcado;
	}

	public void setUtilizarOpcaoRecorrenciaDefaulMarcado(Boolean utilizarOpcaoRecorrenciaDefaulMarcado) {
		this.utilizarOpcaoRecorrenciaDefaulMarcado = utilizarOpcaoRecorrenciaDefaulMarcado;
	}

	public FormaPadraoDataBaseCartaoRecorrenteEnum getFormaPadraoDataBaseCartaoRecorrente() {
		if (formaPadraoDataBaseCartaoRecorrente == null) {
			formaPadraoDataBaseCartaoRecorrente = FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO;
		}
		return formaPadraoDataBaseCartaoRecorrente;
	}

	public void setFormaPadraoDataBaseCartaoRecorrente(
			FormaPadraoDataBaseCartaoRecorrenteEnum formaPadraoDataBaseCartaoRecorrente) {
		this.formaPadraoDataBaseCartaoRecorrente = formaPadraoDataBaseCartaoRecorrente;
	}

	public String getOrientacaoRecorrenciaAluno() {
		if (orientacaoRecorrenciaAluno == null) {
			orientacaoRecorrenciaAluno = "";
		}
		return orientacaoRecorrenciaAluno;
	}

	public void setOrientacaoRecorrenciaAluno(String orientacaoRecorrenciaAluno) {
		this.orientacaoRecorrenciaAluno = orientacaoRecorrenciaAluno;
	}

	public Boolean getExigirConfirmacaoRecorrencia() {
		if (exigirConfirmacaoRecorrencia == null) {
			exigirConfirmacaoRecorrencia = false;
		}
		return exigirConfirmacaoRecorrencia;
	}

	public void setExigirConfirmacaoRecorrencia(Boolean exigirConfirmacaoRecorrencia) {
		this.exigirConfirmacaoRecorrencia = exigirConfirmacaoRecorrencia;
	}

	public String getMensagemConfirmacaoRecorrencia() {
		if (mensagemConfirmacaoRecorrencia == null) {
			mensagemConfirmacaoRecorrencia = "";
		}
		return mensagemConfirmacaoRecorrencia;
	}

	public void setMensagemConfirmacaoRecorrencia(String mensagemConfirmacaoRecorrencia) {
		this.mensagemConfirmacaoRecorrencia = mensagemConfirmacaoRecorrencia;
	}
}
