package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.academico.enumeradores.SituacaoTCCEnum;
import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.administrativo.enumeradores.TipoTCCEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
//import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class TrabalhoConclusaoCursoVO extends SuperVO {

	private static final long serialVersionUID = -1416856270023713290L;
	private Integer codigo;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina;
	private PessoaVO aluno;
	private ConfiguracaoTCCVO configuracaoTCC;
	private PessoaVO orientador;
	private PessoaVO orientadorSugerido;
	private String titulo;
	private EtapaTCCEnum etapaTCC;
	private SituacaoTCCEnum situacaoTCC;
	private DataModelo historicoSituacaoTCCVOs;
	private List<TrabalhoConclusaoCursoArtefatoVO> trabalhoConclusaoCursoArtefatoVOs;
	private Date dataAlteracaoSituacao;
	private Date dataUltimoAcessoAluno;
	private PessoaVO coordenador;
	private Date dataDefinicaoBanca;
	private String observacaoBanca;
	private List<TrabalhoConclusaoCursoMembroBancaVO> trabalhoConclusaoCursoMembroBancaVOs;

	private Boolean possuiPendFin;
	private Boolean possuiPendDoc;
	private List<DocumetacaoMatriculaVO> documentacaoMatriculaPendVOs;
//	private List<ContaReceberVO> contaReceberPendVOs;

	/*
	 * Informações da Etapa Plano TCC
	 */
	private TipoTCCEnum tipoTCC;
	private Date dataInicioPlanoTCC;
	private Date dataTerminoPlanoTCC;
	private String tema;
	private String problema;
	private String objetivoGeral;
	private String objetivoEspecifico;
	private String metodologia;
	private String referenciaBibliografica;
	private DataModelo trabalhoConclusaoCursoInteracaoPlanoTCCVOs;
	/*
	 * Informações da Etapa Elaboração TCC
	 */
	private DataModelo trabalhoConclusaoCursoArquivoEtapaElaboracaoVOs;
	private DataModelo trabalhoConclusaoCursoInteracaoElaboracaoTCCVOs;
	private Date dataInicioElaboracaoTCC;
	private Date dataTerminoElaboracaoTCC;
	/*
	 * Informações da Etapa Avaliação TCC
	 */
	private DataModelo trabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs;
	private DataModelo trabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs;
	private Date dataInicioAvaliacaoTCC;
	private Date dataTerminoAvaliacaoTCC;
	private List<QuestaoTrabalhoConclusaoCursoVO> questaoFormatacaoVOs;
	private List<QuestaoTrabalhoConclusaoCursoVO> questaoConteudoVOs;
	private Double mediaFormatacao;
	private Double mediaConteudo;
	private Double media;

	/**
	 * Atributos Transientes
	 * 
	 * @return
	 */
	private HistoricoVO historico;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplina() {
		if (matriculaPeriodoTurmaDisciplina == null) {
			matriculaPeriodoTurmaDisciplina = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplina;
	}

	public void setMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina) {
		this.matriculaPeriodoTurmaDisciplina = matriculaPeriodoTurmaDisciplina;
	}

	public PessoaVO getAluno() {
		if (aluno == null) {
			aluno = new PessoaVO();
		}
		return aluno;
	}

	public void setAluno(PessoaVO aluno) {
		this.aluno = aluno;
	}

	public ConfiguracaoTCCVO getConfiguracaoTCC() {
		if (configuracaoTCC == null) {
			configuracaoTCC = new ConfiguracaoTCCVO();
		}
		return configuracaoTCC;
	}

	public void setConfiguracaoTCC(ConfiguracaoTCCVO configuracaoTCC) {
		this.configuracaoTCC = configuracaoTCC;
	}

	public PessoaVO getOrientador() {
		if (orientador == null) {
			orientador = new PessoaVO();
		}
		return orientador;
	}

	public void setOrientador(PessoaVO orientador) {
		this.orientador = orientador;
	}

	public PessoaVO getOrientadorSugerido() {
		if (orientadorSugerido == null) {
			orientadorSugerido = new PessoaVO();
		}
		return orientadorSugerido;
	}

	public void setOrientadorSugerido(PessoaVO orientadorSugerido) {
		this.orientadorSugerido = orientadorSugerido;
	}

	public EtapaTCCEnum getEtapaTCC() {
		if (etapaTCC == null) {
			etapaTCC = EtapaTCCEnum.PLANO_TCC;
		}
		return etapaTCC;
	}

	public void setEtapaTCC(EtapaTCCEnum etapaTCC) {
		this.etapaTCC = etapaTCC;
	}

	public SituacaoTCCEnum getSituacaoTCC() {
		if (situacaoTCC == null) {
			situacaoTCC = SituacaoTCCEnum.NOVO;
		}
		return situacaoTCC;
	}

	public void setSituacaoTCC(SituacaoTCCEnum situacaoTCC) {
		this.situacaoTCC = situacaoTCC;
	}

	public Date getDataAlteracaoSituacao() {
		if (dataAlteracaoSituacao == null) {
			dataAlteracaoSituacao = new Date();
		}
		return dataAlteracaoSituacao;
	}

	public void setDataAlteracaoSituacao(Date dataAlteracaoSituacao) {
		this.dataAlteracaoSituacao = dataAlteracaoSituacao;
	}

	public TipoTCCEnum getTipoTCC() {
		if (tipoTCC == null) {
			tipoTCC = TipoTCCEnum.MONOGRAFIA;
		}
		return tipoTCC;
	}

	public void setTipoTCC(TipoTCCEnum tipoTCC) {
		this.tipoTCC = tipoTCC;
	}

	public Date getDataInicioPlanoTCC() {

		return dataInicioPlanoTCC;
	}

	public void setDataInicioPlanoTCC(Date dataInicioPlanoTCC) {
		this.dataInicioPlanoTCC = dataInicioPlanoTCC;
	}

	public Date getDataTerminoPlanoTCC() {
		return dataTerminoPlanoTCC;
	}

	public void setDataTerminoPlanoTCC(Date dataTerminoPlanoTCC) {
		this.dataTerminoPlanoTCC = dataTerminoPlanoTCC;
	}

	public String getDataTerminoPlanoTCCApresentar() {
		if (dataTerminoPlanoTCC == null) {
			return "";
		}
		return Uteis.getData(getDataTerminoPlanoTCC());
	}

	public String getDataInicioPlanoTCCApresentar() {
		if (dataInicioPlanoTCC == null) {
			return "";
		}
		return Uteis.getData(getDataInicioPlanoTCC());
	}

	public String getDataTerminoElaboracaoTCCApresentar() {
		if (dataTerminoElaboracaoTCC == null) {
			return "";
		}
		return Uteis.getData(getDataTerminoElaboracaoTCC());
	}

	public String getDataInicioElaboracaoTCCApresentar() {
		if (dataInicioElaboracaoTCC == null) {
			return "";
		}
		return Uteis.getData(getDataInicioElaboracaoTCC());
	}

	public String getDataTerminoAvaliacaoTCCApresentar() {
		if (dataTerminoAvaliacaoTCC == null) {
			return "";
		}
		return Uteis.getData(getDataTerminoAvaliacaoTCC());
	}

	public String getDataInicioAvaliacaoTCCApresentar() {
		if (dataInicioAvaliacaoTCC == null) {
			return "";
		}
		return Uteis.getData(getDataInicioAvaliacaoTCC());
	}

	public String getTema() {
		if (tema == null) {
			tema = "";
		}
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getProblema() {
		if (problema == null) {
			problema = "";
		}
		return problema;
	}

	public void setProblema(String problema) {
		this.problema = problema;
	}

	public String getObjetivoGeral() {
		if (objetivoGeral == null) {
			objetivoGeral = "";
		}
		return objetivoGeral;
	}

	public void setObjetivoGeral(String objetivoGeral) {
		this.objetivoGeral = objetivoGeral;
	}

	public String getObjetivoEspecifico() {
		if (objetivoEspecifico == null) {
			objetivoEspecifico = "";
		}
		return objetivoEspecifico;
	}

	public void setObjetivoEspecifico(String objetivoEspecifico) {
		this.objetivoEspecifico = objetivoEspecifico;
	}

	public String getMetodologia() {
		if (metodologia == null) {
			metodologia = "";
		}
		return metodologia;
	}

	public void setMetodologia(String metodologia) {
		this.metodologia = metodologia;
	}

	public String getReferenciaBibliografica() {
		if (referenciaBibliografica == null) {
			referenciaBibliografica = "";
		}
		return referenciaBibliografica;
	}

	public void setReferenciaBibliografica(String referenciaBibliografica) {
		this.referenciaBibliografica = referenciaBibliografica;
	}

	public Date getDataInicioElaboracaoTCC() {
		return dataInicioElaboracaoTCC;
	}

	public void setDataInicioElaboracaoTCC(Date dataInicioElaboracaoTCC) {
		this.dataInicioElaboracaoTCC = dataInicioElaboracaoTCC;
	}

	public Date getDataTerminoElaboracaoTCC() {
		return dataTerminoElaboracaoTCC;
	}

	public void setDataTerminoElaboracaoTCC(Date dataTerminoElaboracaoTCC) {
		this.dataTerminoElaboracaoTCC = dataTerminoElaboracaoTCC;
	}

	public Date getDataInicioAvaliacaoTCC() {
		return dataInicioAvaliacaoTCC;
	}

	public void setDataInicioAvaliacaoTCC(Date dataInicioAvaliacaoTCC) {
		this.dataInicioAvaliacaoTCC = dataInicioAvaliacaoTCC;
	}

	public Date getDataTerminoAvaliacaoTCC() {
		return dataTerminoAvaliacaoTCC;
	}

	public void setDataTerminoAvaliacaoTCC(Date dataTerminoAvaliacaoTCC) {
		this.dataTerminoAvaliacaoTCC = dataTerminoAvaliacaoTCC;
	}

	public List<TrabalhoConclusaoCursoArtefatoVO> getTrabalhoConclusaoCursoArtefatoPendenteVOs() {
		List<TrabalhoConclusaoCursoArtefatoVO> lista = new ArrayList<TrabalhoConclusaoCursoArtefatoVO>(0);
		for (TrabalhoConclusaoCursoArtefatoVO obj : getTrabalhoConclusaoCursoArtefatoVOs()) {
			if (!obj.getEntregue()) {
				lista.add(obj);
			}
		}
		return lista;
	}

	public List<TrabalhoConclusaoCursoArtefatoVO> getTrabalhoConclusaoCursoArtefatoVOs() {
		if (trabalhoConclusaoCursoArtefatoVOs == null) {
			trabalhoConclusaoCursoArtefatoVOs = new ArrayList<TrabalhoConclusaoCursoArtefatoVO>(0);
		}
		return trabalhoConclusaoCursoArtefatoVOs;
	}

	public void setTrabalhoConclusaoCursoArtefatoVOs(List<TrabalhoConclusaoCursoArtefatoVO> trabalhoConclusaoCursoArtefatoVOs) {
		this.trabalhoConclusaoCursoArtefatoVOs = trabalhoConclusaoCursoArtefatoVOs;
	}

	public DataModelo getHistoricoSituacaoTCCVOs() {
		if (historicoSituacaoTCCVOs == null) {
			historicoSituacaoTCCVOs = new DataModelo();
		}
		return historicoSituacaoTCCVOs;
	}

	public void setHistoricoSituacaoTCCVOs(DataModelo historicoSituacaoTCCVOs) {
		this.historicoSituacaoTCCVOs = historicoSituacaoTCCVOs;
	}

	public DataModelo getTrabalhoConclusaoCursoInteracaoPlanoTCCVOs() {
		if (trabalhoConclusaoCursoInteracaoPlanoTCCVOs == null) {
			trabalhoConclusaoCursoInteracaoPlanoTCCVOs = new DataModelo();
		}
		return trabalhoConclusaoCursoInteracaoPlanoTCCVOs;
	}

	public void setTrabalhoConclusaoCursoInteracaoPlanoTCCVOs(DataModelo trabalhoConclusaoCursoInteracaoPlanoTCCVOs) {
		this.trabalhoConclusaoCursoInteracaoPlanoTCCVOs = trabalhoConclusaoCursoInteracaoPlanoTCCVOs;
	}

	public DataModelo getTrabalhoConclusaoCursoArquivoEtapaElaboracaoVOs() {
		if (trabalhoConclusaoCursoArquivoEtapaElaboracaoVOs == null) {
			trabalhoConclusaoCursoArquivoEtapaElaboracaoVOs = new DataModelo();
		}
		return trabalhoConclusaoCursoArquivoEtapaElaboracaoVOs;
	}

	public void setTrabalhoConclusaoCursoArquivoEtapaElaboracaoVOs(DataModelo trabalhoConclusaoCursoArquivoEtapaElaboracaoVOs) {
		this.trabalhoConclusaoCursoArquivoEtapaElaboracaoVOs = trabalhoConclusaoCursoArquivoEtapaElaboracaoVOs;
	}

	public DataModelo getTrabalhoConclusaoCursoInteracaoElaboracaoTCCVOs() {
		if (trabalhoConclusaoCursoInteracaoElaboracaoTCCVOs == null) {
			trabalhoConclusaoCursoInteracaoElaboracaoTCCVOs = new DataModelo();
		}
		return trabalhoConclusaoCursoInteracaoElaboracaoTCCVOs;
	}

	public void setTrabalhoConclusaoCursoInteracaoElaboracaoTCCVOs(DataModelo trabalhoConclusaoCursoInteracaoElaboracaoTCCVOs) {
		this.trabalhoConclusaoCursoInteracaoElaboracaoTCCVOs = trabalhoConclusaoCursoInteracaoElaboracaoTCCVOs;
	}

	public DataModelo getTrabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs() {
		if (trabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs == null) {
			trabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs = new DataModelo();
		}
		return trabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs;
	}

	public void setTrabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs(DataModelo trabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs) {
		this.trabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs = trabalhoConclusaoCursoArquivoEtapaAvaliacaoVOs;
	}

	public DataModelo getTrabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs() {
		if (trabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs == null) {
			trabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs = new DataModelo();
		}
		return trabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs;
	}

	public void setTrabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs(DataModelo trabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs) {
		this.trabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs = trabalhoConclusaoCursoInteracaoAvaliacaoTCCVOs;
	}

	public HistoricoVO getHistorico() {
		if (historico == null) {
			historico = new HistoricoVO();
		}
		return historico;
	}

	public void setHistorico(HistoricoVO historico) {
		this.historico = historico;
	}

	public boolean getIsApresentarEtapaPlanoTCC() {
		return getConfiguracaoTCC().getControlaPlanoTCC();
	}

	public boolean getIsPermiteAlunoAlterarPlanoTCC() {
		return getEtapaTCC().equals(EtapaTCCEnum.PLANO_TCC) && (getSituacaoTCC().equals(SituacaoTCCEnum.NOVO) || getSituacaoTCC().equals(SituacaoTCCEnum.EM_ELABORACAO));
	}

	public boolean getIsPermiteAlunoAlterarElaboracaoTCC() {
		return getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC) && (getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_ARQUIVO));
	}

	public boolean getIsPermiteAlunoAlterarAvaliacaoTCC() {
		return getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC) && (getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_ARQUIVO));
	}

	public boolean getIsApresentarEtapaElaboracaoTCC() {
		return getConfiguracaoTCC().getControlaElaboracaoTCC() && !getEtapaTCC().equals(EtapaTCCEnum.PLANO_TCC);
	}

	public boolean getIsApresentarEtapaAvaliacaoTCC() {
		try {
			return getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC);
		} catch (Exception e) {
			return false;
		}
		
	}

	public String getDataInicioTCCApresentar() {

		if (getConfiguracaoTCC().getControlaPlanoTCC() && getDataInicioPlanoTCC() != null) {
			return Uteis.getData(getDataInicioPlanoTCC());
		} else if (!getConfiguracaoTCC().getControlaPlanoTCC() && getConfiguracaoTCC().getControlaElaboracaoTCC() && getDataInicioElaboracaoTCC() != null) {
			return Uteis.getData(getDataInicioElaboracaoTCC());
		} else if (!getConfiguracaoTCC().getControlaPlanoTCC() && !getConfiguracaoTCC().getControlaElaboracaoTCC() && getDataInicioAvaliacaoTCC() != null) {
			return Uteis.getData(getDataInicioAvaliacaoTCC());
		}
		return "";

	}

	public String getDataTerminoTCCApresentar() {
		if (getDataTerminoAvaliacaoTCC() != null) {
			return Uteis.getData(getDataTerminoAvaliacaoTCC());
		}
		return "";
	}

	public boolean getIsPermiteSolicitarOrientador() {
		return getConfiguracaoTCC().getPermiteSolicitarOrientador() && getOrientador().getCodigo() == 0;
	}

	public boolean getIsExisteOrientadorSolicitado() {
		return getConfiguracaoTCC().getPermiteSolicitarOrientador() && getOrientadorSugerido().getCodigo() == 0;
	}

	public String getOrientadorApresentar() {
		if (getOrientador().getCodigo() > 0) {
			return getOrientador().getNome();
		}
		// if(getIsExisteOrientadorSolicitado()){
		// return getOrientadorSugerido().getNome();
		// }
		// if(getIsPermiteSolicitarOrientador()){
		// return "Solicite um(a) Orientador(a)";
		// }
		return this.getConfiguracaoTCC().getNomenclaturaUtilizarParaAvaliador() + "(a) não definido";
	}

	public String getOrientadorSugeridoApresentar() {
		if (getOrientadorSugerido().getCodigo() > 0) {
			return getOrientadorSugerido().getNome();
		}
		if (getIsPermiteSolicitarOrientador()) {
			return "Solicite um(a) " + this.getConfiguracaoTCC().getNomenclaturaUtilizarParaAvaliador() + "(a)";
		}
		return this.getConfiguracaoTCC().getNomenclaturaUtilizarParaAvaliador() + "(a) não definido";
	}

	public boolean getIsExisteFotoOrientador() {
		if (getOrientador().getCodigo() > 0) {
			return getOrientador().getExisteImagem();
		}
		if (getIsExisteOrientadorSolicitado()) {
			return getOrientadorSugerido().getExisteImagem();
		}
		return false;
	}

	public String getFotoOrientador() {
		if (getOrientador().getCodigo() > 0) {
			return getOrientador().getArquivoImagem().getNome();
		}
		if (getIsExisteOrientadorSolicitado()) {
			return getOrientadorSugerido().getArquivoImagem().getNome();
		}
		return "";
	}

	public Boolean getIsPermiteOrientadorAlterarPlano() {
		return getEtapaTCC().equals(EtapaTCCEnum.PLANO_TCC) && getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_APROVACAO_ORIENTADOR);
	}

	public Boolean getIsPermiteOrientadorAlterarElaboracao() {
		return getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC) && getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_APROVACAO_ORIENTADOR);
	}

	public Boolean getIsPermiteAlterarAvaliacao() {
		return getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC) && getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA);
	}

	public Boolean getIsPermiteReprovarPlanoTCC() {
		return getEtapaTCC().equals(EtapaTCCEnum.PLANO_TCC) && !getIsAprovadoReprovado();
	}

	public Boolean getIsPermiteReprovarElaboracaoTCC() {
		return getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC) && !getIsAprovadoReprovado();
	}

	public Boolean getIsPermiteVoltarEtapaPlano() {
		return getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC) && !getIsAprovadoReprovado();
	}

	public Boolean getIsAprovadoReprovado() {
		return getSituacaoTCC().equals(SituacaoTCCEnum.APROVADO) || getSituacaoTCC().equals(SituacaoTCCEnum.REPROVADO);
	}

	public Boolean getIsPermiteVoltarEtapaElaboracao() {
		return getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC) && !(getSituacaoTCC().equals(SituacaoTCCEnum.APROVADO) || getSituacaoTCC().equals(SituacaoTCCEnum.REPROVADO));
	}

	public Boolean getIsPermiteDefinirOrientador() {
		return getOrientador().getCodigo() == 0;
	}

	public Boolean getIsPermiteConfirmarOrientadorSugerido() {
		return getOrientadorSugerido().getCodigo() > 0 && getOrientador().getCodigo() == 0 && !getIsAprovadoReprovado();
	}

	public boolean getApresentarOrientacaoGeral() {
		return (getConfiguracaoTCC().getOrientacaoGeral() != null && !Uteis.retiraTags(getConfiguracaoTCC().getOrientacaoGeral()).trim().isEmpty());
	}

	public boolean getApresentarOrientacaoExtensaoPrazo() {
		return (getConfiguracaoTCC().getOrientacaoExtensaoPrazo() != null && !Uteis.retiraTags(getConfiguracaoTCC().getOrientacaoExtensaoPrazo()).trim().isEmpty() && (getSituacaoTCC().equals(SituacaoTCCEnum.NOVO) || getSituacaoTCC().equals(SituacaoTCCEnum.EM_ELABORACAO) || getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_ARQUIVO)));
	}

	public Date getDataUltimoAcessoAluno() {
		return dataUltimoAcessoAluno;
	}

	public void setDataUltimoAcessoAluno(Date dataUltimoAcessoAluno) {
		this.dataUltimoAcessoAluno = dataUltimoAcessoAluno;
	}

	public boolean getIsPrimeiroAcessoAluno() {
		return (getDataUltimoAcessoAluno() == null);
	}

	public String getNomeResponsavelSituacaoAtual() {
		if (getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_ARQUIVO) || getSituacaoTCC().equals(SituacaoTCCEnum.EM_ELABORACAO) || (getSituacaoTCC().equals(SituacaoTCCEnum.NOVO) && getOrientador().getCodigo().intValue() != 0)) {
			return getAluno().getNome();
		} else if (getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_APROVACAO_ORIENTADOR)) {
			return getOrientador().getNome();
		} else if (getSituacaoTCC().equals(SituacaoTCCEnum.AGUARDANDO_AVALIACAO_BANCA) || (getSituacaoTCC().equals(SituacaoTCCEnum.NOVO) && getOrientador().getCodigo().intValue() == 0)) {
			return getCoordenador().getNome();
		} else {
			return "";
		}
	}

	public Date getDataInicioEtapaAtual() {
		if (getEtapaTCC().equals(EtapaTCCEnum.PLANO_TCC)) {
			return getDataInicioPlanoTCC();
		} else if (getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC)) {
			return getDataInicioElaboracaoTCC();
		} else if (getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC)) {
			return getDataInicioAvaliacaoTCC();
		} else {
			return null;
		}
	}

	public Date getDataTerminoEtapaAtual() {
		if (getEtapaTCC().equals(EtapaTCCEnum.PLANO_TCC)) {
			return getDataTerminoPlanoTCC();
		} else if (getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC)) {
			return getDataTerminoElaboracaoTCC();
		} else if (getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC)) {
			return getDataTerminoAvaliacaoTCC();
		} else {
			return null;
		}
	}

	public void setDataTerminoEtapaAtual(Date dataTerminoEtapaAtual) {
		if (getEtapaTCC().equals(EtapaTCCEnum.PLANO_TCC)) {
			setDataTerminoPlanoTCC(dataTerminoEtapaAtual);
		} else if (getEtapaTCC().equals(EtapaTCCEnum.ELABORACAO_TCC)) {
			setDataTerminoElaboracaoTCC(dataTerminoEtapaAtual);
		} else if (getEtapaTCC().equals(EtapaTCCEnum.AVALIACAO_TCC)) {
			setDataTerminoAvaliacaoTCC(dataTerminoEtapaAtual);
		}
	}

	public String getDataInicioEtapaAtualApresentar() {
		if (getDataInicioEtapaAtual() == null) {
			return "";
		}
		return Uteis.getData(getDataInicioEtapaAtual());
	}

	public String getDataTerminoEtapaAtualApresentar() {
		if (getDataTerminoEtapaAtual() == null) {
			return "";
		}
		return Uteis.getData(getDataTerminoEtapaAtual());
	}

	public Integer getDiasAtrasoEtapaAtual() {
		Date dataAtual = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dataAtual);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Integer diasAtrasoEtapaAtual = Uteis.getObterDiferencaDiasEntreDuasData(c.getTime(), getDataTerminoEtapaAtual());
		if (diasAtrasoEtapaAtual < 0) {
			diasAtrasoEtapaAtual = 0;
		}
		return diasAtrasoEtapaAtual;
	}

	public PessoaVO getCoordenador() {
		if (coordenador == null) {
			coordenador = new PessoaVO();
		}
		return coordenador;
	}

	public void setCoordenador(PessoaVO coordenador) {
		this.coordenador = coordenador;
	}

	public String getDataDefinicaoBancaApresentar() {
		if (dataDefinicaoBanca == null) {
			return "";
		}
		return Uteis.getData(getDataDefinicaoBanca());
	}

	public Date getDataDefinicaoBanca() {
		if (dataDefinicaoBanca == null) {
			dataDefinicaoBanca = new Date();
		}
		return dataDefinicaoBanca;
	}

	public void setDataDefinicaoBanca(Date dataDefinicaoBanca) {
		this.dataDefinicaoBanca = dataDefinicaoBanca;
	}

	public String getObservacaoBanca() {
		if (observacaoBanca == null) {
			observacaoBanca = "";
		}
		return observacaoBanca;
	}

	public void setObservacaoBanca(String observacaoBanca) {
		this.observacaoBanca = observacaoBanca;
	}

	public List<TrabalhoConclusaoCursoMembroBancaVO> getTrabalhoConclusaoCursoMembroBancaVOs() {
		if (trabalhoConclusaoCursoMembroBancaVOs == null) {
			trabalhoConclusaoCursoMembroBancaVOs = new ArrayList<TrabalhoConclusaoCursoMembroBancaVO>();
		}
		return trabalhoConclusaoCursoMembroBancaVOs;
	}

	public void setTrabalhoConclusaoCursoMembroBancaVOs(List<TrabalhoConclusaoCursoMembroBancaVO> trabalhoConclusaoCursoMembroBancaVOs) {
		this.trabalhoConclusaoCursoMembroBancaVOs = trabalhoConclusaoCursoMembroBancaVOs;
	}

	public void adicionarObjMembroBancaVOs(TrabalhoConclusaoCursoMembroBancaVO obj) throws Exception {
		TrabalhoConclusaoCursoMembroBancaVO.validarDados(obj);
		getTrabalhoConclusaoCursoMembroBancaVOs().add(obj);
	}

	public void excluirObjMembroBancaVOs(TrabalhoConclusaoCursoMembroBancaVO obj) throws Exception {
		int index = 0;
		Iterator i = getTrabalhoConclusaoCursoMembroBancaVOs().iterator();
		while (i.hasNext()) {
			TrabalhoConclusaoCursoMembroBancaVO objExistente = (TrabalhoConclusaoCursoMembroBancaVO) i.next();
			if (objExistente.getNome().equals(obj.getNome())) {
				getTrabalhoConclusaoCursoMembroBancaVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public List<DocumetacaoMatriculaVO> getDocumentacaoMatriculaPendVOs() {
		if (documentacaoMatriculaPendVOs == null) {
			documentacaoMatriculaPendVOs = new ArrayList<DocumetacaoMatriculaVO>();
		}
		return documentacaoMatriculaPendVOs;
	}

	public void setDocumentacaoMatriculaPendVOs(List<DocumetacaoMatriculaVO> documentacaoMatriculaPendVOs) {
		this.documentacaoMatriculaPendVOs = documentacaoMatriculaPendVOs;
	}

//	public List<ContaReceberVO> getContaReceberPendVOs() {
//		if (contaReceberPendVOs == null) {
//			contaReceberPendVOs = new ArrayList<ContaReceberVO>();
//		}
//		return contaReceberPendVOs;
//	}
//
//	public void setContaReceberPendVOs(List<ContaReceberVO> contaReceberPendVOs) {
//		this.contaReceberPendVOs = contaReceberPendVOs;
//	}

	public Boolean getPossuiPendFin() {
		if (possuiPendFin == null) {
			possuiPendFin = Boolean.FALSE;
		}
		return possuiPendFin;
	}

	public void setPossuiPendFin(Boolean possuiPendFin) {
		this.possuiPendFin = possuiPendFin;
	}

	public Boolean getPossuiPendDoc() {
		if (possuiPendDoc == null) {
			possuiPendDoc = Boolean.FALSE;
		}
		return possuiPendDoc;
	}

	public void setPossuiPendDoc(Boolean possuiPendDoc) {
		this.possuiPendDoc = possuiPendDoc;
	}

	public String getTitulo() {
		if (titulo == null) {
			titulo = "Informe o título do seu TCC aqui!";
		}
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public JRDataSource getListaQuestaoFormatacaoVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getQuestaoFormatacaoVOs().toArray());
		return jr;
	}
	
	public List<QuestaoTrabalhoConclusaoCursoVO> getQuestaoFormatacaoVOs() {
		if (questaoFormatacaoVOs == null) {
			questaoFormatacaoVOs = new ArrayList<QuestaoTrabalhoConclusaoCursoVO>();
		}
		return questaoFormatacaoVOs;
	}

	public void setQuestaoFormatacaoVOs(List<QuestaoTrabalhoConclusaoCursoVO> questaoFormatacaoVOs) {
		this.questaoFormatacaoVOs = questaoFormatacaoVOs;
	}

	public JRDataSource getListaQuestaoConteudoVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getQuestaoConteudoVOs().toArray());
		return jr;
	}	

	public List<QuestaoTrabalhoConclusaoCursoVO> getQuestaoConteudoVOs() {
		if (questaoConteudoVOs == null) {
			questaoConteudoVOs = new ArrayList<QuestaoTrabalhoConclusaoCursoVO>();
		}
		return questaoConteudoVOs;
	}

	public void setQuestaoConteudoVOs(List<QuestaoTrabalhoConclusaoCursoVO> questaoConteudoVOs) {
		this.questaoConteudoVOs = questaoConteudoVOs;
	}

	public Double getMediaFormatacao() {
		if (mediaFormatacao == null) {
			mediaFormatacao = null;
		}
		return mediaFormatacao;
	}

	public void setMediaFormatacao(Double mediaFormatacao) {
		this.mediaFormatacao = mediaFormatacao;
	}

	public Double getMediaConteudo() {
		if (mediaConteudo == null) {
			mediaConteudo = null;
		}
		return mediaConteudo;
	}

	public void setMediaConteudo(Double mediaConteudo) {
		this.mediaConteudo = mediaConteudo;
	}

	public Double getMedia() {
		if (media == null) {
			media = null;
		}
		return media;
	}

	public void setMedia(Double media) {
		this.media = media;
	}

	public void calcularMediaConteudo() throws Exception {
		try {
			Double media = 0.0;
			if (!this.getQuestaoConteudoVOs().isEmpty()) {
				Iterator i = this.getQuestaoConteudoVOs().iterator();
				while (i.hasNext()) {
					QuestaoTrabalhoConclusaoCursoVO q = (QuestaoTrabalhoConclusaoCursoVO) i.next();
					if (q.getValor() != null) {
						media = media + q.getValor();
					} else {
						throw new Exception("A nota para a pergunta de conteúdo (" + q.getEnunciado() + ") deve ser informada!");
					}
				}
			}
			this.setMediaConteudo(media);
			if (this.getMediaConteudo().doubleValue() > this.getConfiguracaoTCC().getNotaMaximaMediaConteudo().doubleValue()) {
				throw new Exception("A média final conteúdo não pode ser superior a nota máxima conteúdo configurada dentro da configuração de TCC!");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void calcularMediaFormatacao() throws Exception {
		try {
			Double media = 0.0;
			if (!this.getQuestaoFormatacaoVOs().isEmpty()) {

				Iterator i = this.getQuestaoFormatacaoVOs().iterator();
				while (i.hasNext()) {
					QuestaoTrabalhoConclusaoCursoVO q = (QuestaoTrabalhoConclusaoCursoVO) i.next();
					if (q.getValor() != null) {
						media = media + q.getValor();
					} else {
						throw new Exception("A nota para a pergunta de formatação (" + q.getEnunciado() + ") deve ser informada!");
					}
				}
			}
			this.setMediaFormatacao(media);
			if (this.getMediaFormatacao().doubleValue() > this.getConfiguracaoTCC().getNotaMaximaMediaFormatacao().doubleValue()) {
				throw new Exception("A média final formatação não pode ser superior a nota máxima formatação configurada dentro da configuração de TCC (" +this.getConfiguracaoTCC().getNotaMaximaMediaFormatacao().doubleValue() + ")!");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void calcularMediaFinal() {
		Double media = 0.0;
		if (!this.getQuestaoFormatacaoVOs().isEmpty()) {
			Iterator i = this.getQuestaoFormatacaoVOs().iterator();
			while (i.hasNext()) {
				QuestaoTrabalhoConclusaoCursoVO q = (QuestaoTrabalhoConclusaoCursoVO) i.next();
				if (q.getValor() != null) {
					media = media + q.getValor();
				}
			}
		}
		if (!this.getQuestaoConteudoVOs().isEmpty()) {
			Iterator i = this.getQuestaoConteudoVOs().iterator();
			while (i.hasNext()) {
				QuestaoTrabalhoConclusaoCursoVO q = (QuestaoTrabalhoConclusaoCursoVO) i.next();
				if (q.getValor() != null) {
					media = media + q.getValor();
				}
			}
		}
		this.setMedia(media);
		if (this.getMedia().doubleValue() >= this.getConfiguracaoTCC().getMediaAprovacao().doubleValue()) {
			this.setSituacaoTCC(SituacaoTCCEnum.APROVADO);
		} else {
			this.setSituacaoTCC(SituacaoTCCEnum.REPROVADO);
		}
	}

}