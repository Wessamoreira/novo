package negocio.comuns.ead;

import java.text.ParseException;
import java.util.Date;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeEnum;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoOrigemEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * @author Victor Hugo 02/10/2014
 */
@XmlRootElement(name = "calendarioEAD")
public class CalendarioAtividadeMatriculaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String descricao;
	private String historicoModificacao;
	private Date dataCadastro;
	private UsuarioVO responsavelCadastro;
	private Date dataInicio;
	private Date dataFim;
	private String codOrigem;
	private MatriculaVO matriculaVO;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private Date dateRealizado;
	private Integer nrVezesProrrogado;
	private TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividade;
	private TipoOrigemEnum tipoOrigem;
	private SituacaoAtividadeEnum situacaoAtividade;
	/*
	 * 
	 * Transient
	 */
	private Boolean selecionarAtividade;
	private GraficoAproveitamentoAvaliacaoVO graficoAproveitamentoAvaliacaoVO;
	private AtividadeDiscursivaVO atividadeDiscursivaVO;
	private ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO;
	private PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO;
	/*
	 * 
	 * Fim Transient
	 */
	private AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "descricao")
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getHistoricoModificacao() {
		if (historicoModificacao == null) {
			historicoModificacao = "";
		}
		return historicoModificacao;
	}

	public void setHistoricoModificacao(String historicoModificacao) {
		this.historicoModificacao = historicoModificacao;
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public UsuarioVO getResponsavelCadastro() {
		if (responsavelCadastro == null) {
			responsavelCadastro = new UsuarioVO();
		}
		return responsavelCadastro;
	}

	public void setResponsavelCadastro(UsuarioVO responsavelCadastro) {
		this.responsavelCadastro = responsavelCadastro;
	}

	@XmlElement(name = "dataInicio")
	public Date getDataInicio() {		
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@XmlElement(name = "dataFim")
	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getCodOrigem() {
		if (codOrigem == null) {
			codOrigem = "";
		}
		return codOrigem;
	}

	public void setCodOrigem(String codOrigem) {
		this.codOrigem = codOrigem;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}

	@XmlElement(name = "dataRealizacao")
	public Date getDateRealizado() {
		if (dateRealizado == null) {
			dateRealizado = new Date();
		}
		return dateRealizado;
	}

	public void setDateRealizado(Date dateRealizado) {
		this.dateRealizado = dateRealizado;
	}

	public Integer getNrVezesProrrogado() {
		if (nrVezesProrrogado == null) {
			nrVezesProrrogado = 0;
		}
		return nrVezesProrrogado;
	}

	public void setNrVezesProrrogado(Integer nrVezesProrrogado) {
		this.nrVezesProrrogado = nrVezesProrrogado;
	}

	public TipoCalendarioAtividadeMatriculaEnum getTipoCalendarioAtividade() {
		if (tipoCalendarioAtividade == null) {
			tipoCalendarioAtividade = TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_CONSULTA;
		}
		return tipoCalendarioAtividade;
	}

	public void setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividade) {
		this.tipoCalendarioAtividade = tipoCalendarioAtividade;
	}

	public TipoOrigemEnum getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = TipoOrigemEnum.NENHUM;
		}
		return tipoOrigem;
	}

	public void setTipoOrigem(TipoOrigemEnum tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}

	@XmlElement(name = "tipoCalendarioAtividade")
	public String getTipoCalendarioAtividade_Apresentar() {
		if(getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA)) {
			return getTipoCalendarioAtividade().getValorApresentar() + " - "+ getConteudoUnidadePaginaRecursoEducacionalVO().getTitulo();
		}
		return getTipoCalendarioAtividade().getValorApresentar();
	}

	public String getTipoOrigem_Apresentar() {
		return getTipoOrigem().getValorApresentar();
	}

	public SituacaoAtividadeEnum getSituacaoAtividade() {
		if (situacaoAtividade == null) {
			situacaoAtividade = SituacaoAtividadeEnum.NAO_CONCLUIDA;
		}
		return situacaoAtividade;
	}

	public void setSituacaoAtividade(SituacaoAtividadeEnum situacaoAtividade) {
		this.situacaoAtividade = situacaoAtividade;
	}

	@XmlElement(name = "situacaoAtividade")
	public String getSituacaoAtividade_Apresentar() {
		return getSituacaoAtividade().getValorApresentar();
	}

	public Boolean getSelecionarAtividade() {
		if (selecionarAtividade == null) {
			selecionarAtividade = false;
		}
		return selecionarAtividade;
	}

	public void setSelecionarAtividade(Boolean selecionarAtividade) {
		this.selecionarAtividade = selecionarAtividade;
	}

	public Boolean getIsApresentarCheckBoxAtividade() {
		return getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO) 
				|| getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_CURSO) 
				|| getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS)
				|| getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE)
				|| getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA)
				|| getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_ATIVIDADE_DISCURSIVA);
	}

	public AvaliacaoOnlineMatriculaVO getAvaliacaoOnlineMatriculaVO() {
		if (avaliacaoOnlineMatriculaVO == null) {
			avaliacaoOnlineMatriculaVO = new AvaliacaoOnlineMatriculaVO();
		}
		return avaliacaoOnlineMatriculaVO;
	}

	public void setAvaliacaoOnlineMatriculaVO(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO) {
		this.avaliacaoOnlineMatriculaVO = avaliacaoOnlineMatriculaVO;
	}

	public boolean getIsApresentarBotaoIniciarAvaliacaoOnline() throws ParseException {
		return Uteis.isAtributoPreenchido(getDataInicio()) 
				&& Uteis.isAtributoPreenchido(getDataFim()) 
				&& (getDataInicio().compareTo(new Date()) <= 0 || UteisData.validarDataInicialMaiorFinalComHora(new Date() ,getDataInicio())) 
				&& (getDataFim().compareTo(new Date()) >= 0  || UteisData.validarDataInicialMaiorFinalComHora(getDataFim() , new Date()) ) 
				&& (getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO));
	}
	
	public boolean isCalendarioAtividadeDentroDoPeriodoRealizacao() throws ParseException  {
		return Uteis.isAtributoPreenchido(getDataInicio()) 
				&& Uteis.isAtributoPreenchido(getDataFim()) 
				&& (getDataInicio().compareTo(new Date()) <= 0 || UteisData.validarDataInicialMaiorFinalComHora(new Date() ,getDataInicio())) 
				&& (getDataFim().compareTo(new Date()) >= 0  || UteisData.validarDataInicialMaiorFinalComHora(getDataFim() , new Date()) );
	}
	

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}
	
	/**
	 * @author Victor Hugo 18/12/2014
	 * 
	 * Variaveis transiente criadas para a tela de monitoramentoAlunosEADCons
	 */
	private String qtdeAvaliacaoRealizada;
	private String qtdeAvaliacaoAguardandoRealizacao;
	private String qtdeAtividadeDiscursivasAvaliadas;
	private String qtdeAtividadeDiscursivasAguardando;
	private String qtdeAtividadeDiscursivasAguardandoAluno;
	private Date dataInicioAcessoConteudoEstudo;
	private Date dataFimAcessoConteudoEstudo;
	private Integer codigoAcessoConteudoEstudo;
	private String qtdeDuvidasProfessor;
	private String qtdeDuvidasAguardandoRespostaProfessor;
	private String qtdeDuvidasAguardandoRespostaAluno;
	private String qtdeAvaliacaoOnline;

	public String getQtdeAvaliacaoRealizada() {
		if (qtdeAvaliacaoRealizada == null) {
			qtdeAvaliacaoRealizada = "";
		}
		return qtdeAvaliacaoRealizada;
	}

	public void setQtdeAvaliacaoRealizada(String qtdeAvaliacaoRealizada) {
		this.qtdeAvaliacaoRealizada = qtdeAvaliacaoRealizada;
	}

	public String getQtdeAvaliacaoAguardandoRealizacao() {
		if (qtdeAvaliacaoAguardandoRealizacao == null) {
			qtdeAvaliacaoAguardandoRealizacao = "";
		}
		return qtdeAvaliacaoAguardandoRealizacao;
	}

	public void setQtdeAvaliacaoAguardandoRealizacao(String qtdeAvaliacaoAguardandoRealizacao) {
		this.qtdeAvaliacaoAguardandoRealizacao = qtdeAvaliacaoAguardandoRealizacao;
	}

	public Date getDataInicioAcessoConteudoEstudo() {
		if (dataInicioAcessoConteudoEstudo == null) {
			dataInicioAcessoConteudoEstudo = new Date();
		}
		return dataInicioAcessoConteudoEstudo;
	}

	public void setDataInicioAcessoConteudoEstudo(Date dataInicioAcessoConteudoEstudo) {
		this.dataInicioAcessoConteudoEstudo = dataInicioAcessoConteudoEstudo;
	}

	public Date getDataFimAcessoConteudoEstudo() {
		if (dataFimAcessoConteudoEstudo == null) {
			dataFimAcessoConteudoEstudo = new Date();
		}
		return dataFimAcessoConteudoEstudo;
	}

	public void setDataFimAcessoConteudoEstudo(Date dataFimAcessoConteudoEstudo) {
		this.dataFimAcessoConteudoEstudo = dataFimAcessoConteudoEstudo;
	}

	public Integer getCodigoAcessoConteudoEstudo() {
		if (codigoAcessoConteudoEstudo == null) {
			codigoAcessoConteudoEstudo = 0;
		}
		return codigoAcessoConteudoEstudo;
	}

	public void setCodigoAcessoConteudoEstudo(Integer codigoAcessoConteudoEstudo) {
		this.codigoAcessoConteudoEstudo = codigoAcessoConteudoEstudo;
	}

	public String getQtdeAtividadeDiscursivasAvaliadas() {
		if (qtdeAtividadeDiscursivasAvaliadas == null) {
			qtdeAtividadeDiscursivasAvaliadas = "";
		}
		return qtdeAtividadeDiscursivasAvaliadas;
	}

	public void setQtdeAtividadeDiscursivasAvaliadas(String qtdeAtividadeDiscursivasAvaliadas) {
		this.qtdeAtividadeDiscursivasAvaliadas = qtdeAtividadeDiscursivasAvaliadas;
	}

	public String getQtdeAtividadeDiscursivasAguardando() {
		if (qtdeAtividadeDiscursivasAguardando == null) {
			qtdeAtividadeDiscursivasAguardando = "";
		}
		return qtdeAtividadeDiscursivasAguardando;
	}

	public void setQtdeAtividadeDiscursivasAguardando(String qtdeAtividadeDiscursivasAguardando) {
		this.qtdeAtividadeDiscursivasAguardando = qtdeAtividadeDiscursivasAguardando;
	}

	public String getQtdeDuvidasProfessor() {
		if (qtdeDuvidasProfessor == null) {
			qtdeDuvidasProfessor = "";
		}
		return qtdeDuvidasProfessor;
	}

	public void setQtdeDuvidasProfessor(String qtdeDuvidasProfessor) {
		this.qtdeDuvidasProfessor = qtdeDuvidasProfessor;
	}

	public String getQtdeDuvidasAguardandoRespostaProfessor() {
		if (qtdeDuvidasAguardandoRespostaProfessor == null) {
			qtdeDuvidasAguardandoRespostaProfessor = "";
		}
		return qtdeDuvidasAguardandoRespostaProfessor;
	}

	public void setQtdeDuvidasAguardandoRespostaProfessor(String qtdeDuvidasAguardandoRespostaProfessor) {
		this.qtdeDuvidasAguardandoRespostaProfessor = qtdeDuvidasAguardandoRespostaProfessor;
	}

	public String getQtdeDuvidasAguardandoRespostaAluno() {
		if (qtdeDuvidasAguardandoRespostaAluno == null) {
			qtdeDuvidasAguardandoRespostaAluno = "";
		}
		return qtdeDuvidasAguardandoRespostaAluno;
	}

	public void setQtdeDuvidasAguardandoRespostaAluno(String qtdeDuvidasAguardandoRespostaAluno) {
		this.qtdeDuvidasAguardandoRespostaAluno = qtdeDuvidasAguardandoRespostaAluno;
	}
	
	public String getQtdeAvaliacaoOnline() {
		if(qtdeAvaliacaoOnline == null) {
			qtdeAvaliacaoOnline = "";
		}
		return qtdeAvaliacaoOnline;
	}

	public void setQtdeAvaliacaoOnline(String qtdeAvaliacaoOnline) {
		this.qtdeAvaliacaoOnline = qtdeAvaliacaoOnline;
	}

	public GraficoAproveitamentoAvaliacaoVO getGraficoAproveitamentoAvaliacaoVO() {
		if(graficoAproveitamentoAvaliacaoVO == null) {
			graficoAproveitamentoAvaliacaoVO = new GraficoAproveitamentoAvaliacaoVO();
		}
		return graficoAproveitamentoAvaliacaoVO;
	}

	public void setGraficoAproveitamentoAvaliacaoVO(GraficoAproveitamentoAvaliacaoVO graficoAproveitamentoAvaliacaoVO) {
		this.graficoAproveitamentoAvaliacaoVO = graficoAproveitamentoAvaliacaoVO;
	}
	
	public String getQtdeAtividadeDiscursivasAguardandoAluno() {
		if(qtdeAtividadeDiscursivasAguardandoAluno == null) {
			qtdeAtividadeDiscursivasAguardandoAluno = "";
		}
		return qtdeAtividadeDiscursivasAguardandoAluno;
	}

	public void setQtdeAtividadeDiscursivasAguardandoAluno(String qtdeAtividadeDiscursivasAguardandoAluno) {
		this.qtdeAtividadeDiscursivasAguardandoAluno = qtdeAtividadeDiscursivasAguardandoAluno;
	}
	
	public AtividadeDiscursivaVO getAtividadeDiscursivaVO() {
		if(atividadeDiscursivaVO == null){
			atividadeDiscursivaVO = new AtividadeDiscursivaVO();
		}
		return atividadeDiscursivaVO;
	}

	public void setAtividadeDiscursivaVO(AtividadeDiscursivaVO atividadeDiscursivaVO) {
		this.atividadeDiscursivaVO = atividadeDiscursivaVO;
	}
	
	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO getPeriodoLetivoAtivoUnidadeEnsinoCursoVO() {
		if(periodoLetivoAtivoUnidadeEnsinoCursoVO == null){
			periodoLetivoAtivoUnidadeEnsinoCursoVO = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
		}
		return periodoLetivoAtivoUnidadeEnsinoCursoVO;
	}

	public void setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO) {
		this.periodoLetivoAtivoUnidadeEnsinoCursoVO = periodoLetivoAtivoUnidadeEnsinoCursoVO;
	}
	
	public ConteudoUnidadePaginaRecursoEducacionalVO getConteudoUnidadePaginaRecursoEducacionalVO() {
		if(conteudoUnidadePaginaRecursoEducacionalVO == null){
			conteudoUnidadePaginaRecursoEducacionalVO = new ConteudoUnidadePaginaRecursoEducacionalVO();
		}
		return conteudoUnidadePaginaRecursoEducacionalVO;
	}

	public void setConteudoUnidadePaginaRecursoEducacionalVO(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) {
		this.conteudoUnidadePaginaRecursoEducacionalVO = conteudoUnidadePaginaRecursoEducacionalVO;
	}
	
	public boolean isApresentarConteudoUnidadePaginaRecursoEducacional(){
		return getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo() > 0 ? true: false ;
	}
	
	public boolean isBloquearAntecipacaoOuProrrogacao() {
		if (getTipoCalendarioAtividade().isPeriodoRealizacaoAvaliacaoOnline()
				&& Uteis.isAtributoPreenchido(getAvaliacaoOnlineMatriculaVO())
				&& (getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().isAprovado() || getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().isReprovado())) {
			return true;
		}
		return false;
	}
	
	private Boolean maximizarLista;
	private Boolean minimizarLista;
	
	public Boolean getMaximizarLista() {
		if (maximizarLista == null) {
			maximizarLista = false;
		}
		return maximizarLista;
	}
	
	public void setMaximizarLista(Boolean maximizarLista) {
		this.maximizarLista = maximizarLista;
	}
	
	public Boolean getMinimizarLista() {
		if (minimizarLista == null) {
			minimizarLista = false;
		}
		return minimizarLista;
	}
	
	public void setMinimizarLista(Boolean minimizarLista) {
		this.minimizarLista = minimizarLista;
	}
}
