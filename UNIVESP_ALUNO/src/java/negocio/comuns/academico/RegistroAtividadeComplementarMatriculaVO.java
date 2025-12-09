package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.faces. model.SelectItem;

import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;


public class RegistroAtividadeComplementarMatriculaVO extends SuperVO {

	private Integer codigo;
	private RegistroAtividadeComplementarVO registroAtividadeComplementarVO;
	private MatriculaVO matriculaVO;
	private TipoAtividadeComplementarVO tipoAtividadecomplementarVO;
	private ArquivoVO arquivoVO;
	private Integer cargaHorariaEvento;
	private Integer cargaHorariaConsiderada;
	private Integer cargaHorariaExigida;
	private Integer cargaHorariaRealizada;
	private String situacao;
	private Integer cargaHorariaPendente;
	private HistoricoVO historicoVO;
	private String observacao;
	/**
	 * Transiente - utilizado apenas para desvincular o historico da disciplina da atividade complementar
	 */
	private Integer historicoAnt;
	/**
	 * Transiente
	 */
	private String caminhoArquivoWeb;
	private Integer cargaHorariaAguardandoDeferimento;
	private Integer cargaHorariaIndeferido;
	private Integer cargaHorariaMinimaExigida;
	
	private static final long serialVersionUID = 1L;
	/**
	 * Atributo Temporário utilizado na tela de Acompanhamento Atividade Complementar
	 */
	private List<RegistroAtividadeComplementarMatriculaPeriodoVO> registroAtividadeComplementarMatriculaPeriodoVOs;
	private Map<String, RegistroAtividadeComplementarMatriculaPeriodoVO> mapRegistroAtividadeComplementarMatriculaPeriodoVOs;
	/**
	 * Atributo Temporário utilizado na tela registroAtividadeComplementarForm para individualizar combobox de cada matrícula.
	 */
	private List<SelectItem> listaSelectItemTipoAtividadeComplementar;
	private OperacaoFuncionalidadeVO operacaoFuncionalidadeVO;
	private SituacaoAtividadeComplementarMatriculaEnum situacaoAtividadeComplementarMatricula;
	private UsuarioVO responsavelDeferimentoIndeferimento;
	private Date dataDeferimentoIndeferimento;
	private String motivoIndeferimento;
	private boolean chRealizadaMaiorQueHorasPermitidaPeriodoLetivo = false;
	private String tipoAtividadeComplementarApresentar;
	private String cargaHorariaExcedida;
	private Boolean atividadeComplementarIntegraliazada;
	private String justificativaAlteracaoCHConsiderada;
	private Date dataCriacao;
	private UsuarioVO responsavelEditarCHConsiderada;
	private Date dataEditarCHConsiderada;

	
	public RegistroAtividadeComplementarMatriculaVO() {
		super();
	}

	public Integer getCodigo() {
		if (this.codigo == null) {
			this.codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public RegistroAtividadeComplementarVO getRegistroAtividadeComplementar() {
		if (this.registroAtividadeComplementarVO == null) {
			this.registroAtividadeComplementarVO = new RegistroAtividadeComplementarVO();
		}
		return this.registroAtividadeComplementarVO;
	}

	public void setRegistroAtividadeComplementarVO(RegistroAtividadeComplementarVO registroAtividadeComplementarVO) {
		this.registroAtividadeComplementarVO = registroAtividadeComplementarVO;
	}

	public MatriculaVO getMatriculaVO() {
		if (this.matriculaVO == null) {
			this.matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public Integer getCargaHorariaEvento() {
		if (this.cargaHorariaEvento == null) {
			this.cargaHorariaEvento = 0;
		}
		return cargaHorariaEvento;
	}

	public void setCargaHorariaEvento(Integer cargaHorariaEvento) {
		this.cargaHorariaEvento = cargaHorariaEvento;
	}

	public Integer getCargaHorariaConsiderada() {
		if (this.cargaHorariaConsiderada == null) {
			this.cargaHorariaConsiderada = 0;
		}
		return cargaHorariaConsiderada;
	}

	public void setCargaHorariaConsiderada(Integer cargaHorariaConsiderada) {
		this.cargaHorariaConsiderada = cargaHorariaConsiderada;
	}

	public TipoAtividadeComplementarVO getTipoAtividadeComplementarVO() {
		if (this.tipoAtividadecomplementarVO == null) {
			this.tipoAtividadecomplementarVO = new TipoAtividadeComplementarVO();
		}
		return this.tipoAtividadecomplementarVO;
	}

	public void setTipoAtividadeComplementarVO(TipoAtividadeComplementarVO tipoAtividadeComplementarVO) {
		this.tipoAtividadecomplementarVO = tipoAtividadeComplementarVO;
	}

	public ArquivoVO getArquivoVO() {
		if (this.arquivoVO == null) {
			this.arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

	public Integer getCargaHorariaExigida() {
		if (this.cargaHorariaExigida == null) {
			this.cargaHorariaExigida = 0;
		}
		return cargaHorariaExigida;
	}

	public void setCargaHorariaExigida(Integer cargaHorariaExigida) {
		this.cargaHorariaExigida = cargaHorariaExigida;
	}

	public Integer getCargaHorariaRealizada() {
		if (this.cargaHorariaRealizada == null) {
			this.cargaHorariaRealizada = 0;
		}
		return cargaHorariaRealizada;
	}

	public void setCargaHorariaRealizada(Integer cargaHorariaRealizada) {
		this.cargaHorariaRealizada = cargaHorariaRealizada;
	}

	public String getSituacao() {
		if (atividadeComplementarIntegraliazada) {
			this.situacao = "Concluído";
		} else {
			this.situacao = "Pendente";
		}
		return this.situacao;
	}

	public Integer getCargaHorariaPendente() {
		if (this.cargaHorariaPendente == null) {
			this.cargaHorariaPendente = 0;
		} else if (this.cargaHorariaPendente < 0) {
			this.cargaHorariaPendente = 0;
		}
		return cargaHorariaPendente;
	}

	public void setCargaHorariaPendente(Integer cargaHorariaPendente) {
		this.cargaHorariaPendente = cargaHorariaPendente;
	}

	public Boolean getApresentarDownload() {
		return !getArquivoVO().getCodigo().equals(0) || !getArquivoVO().getNome().trim().isEmpty();
	}

	public HistoricoVO getHistoricoVO() {
		if (historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	public Integer getHistoricoAnt() {
		if (historicoAnt == null && getHistoricoVO().getCodigo() > 0 && getNovoObj()) {
			historicoAnt = getHistoricoVO().getCodigo();
		}
		return historicoAnt;
	}

	public void setHistoricoAnt(Integer historicoAnt) {
		this.historicoAnt = historicoAnt;
	}

	public String getCaminhoArquivoWeb() {		
		return caminhoArquivoWeb;
	}

	public void setCaminhoArquivoWeb(String caminhoArquivoWeb) {
		this.caminhoArquivoWeb = caminhoArquivoWeb;
	}
	
	public List<SelectItem> getListaSelectItemTipoAtividadeComplementar() {
		if (listaSelectItemTipoAtividadeComplementar == null) {
			listaSelectItemTipoAtividadeComplementar = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoAtividadeComplementar;
	}

	public void setListaSelectItemTipoAtividadeComplementar(List<SelectItem> listaSelectItemTipoAtividadeComplementar) {
		this.listaSelectItemTipoAtividadeComplementar = listaSelectItemTipoAtividadeComplementar;
	}
	
	public OperacaoFuncionalidadeVO getOperacaoFuncionalidadeVO() {
		if(operacaoFuncionalidadeVO == null){
			operacaoFuncionalidadeVO = new OperacaoFuncionalidadeVO();
		}
		return operacaoFuncionalidadeVO;
	}

	public void setOperacaoFuncionalidadeVO(OperacaoFuncionalidadeVO operacaoFuncionalidadeVO) {
		this.operacaoFuncionalidadeVO = operacaoFuncionalidadeVO;
	}

	public boolean isChRealizadaMaiorQueHorasPermitidaPeriodoLetivo() {
		if ((Uteis.isAtributoPreenchido(getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().getCargaHorasPermitidasPeriodoLetivo()) 
				&& getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().getCargaHorasPermitidasPeriodoLetivo() 
				< (getTipoAtividadeComplementarVO().getCargaHorasJaRealizadaPeriodoLetivo() + getCargaHorariaConsiderada())
				&& getCargaHorariaConsiderada() > 0)
			||
			(!Uteis.isAtributoPreenchido(getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().getCargaHorasPermitidasPeriodoLetivo()) 
			&& Uteis.isAtributoPreenchido(getTipoAtividadeComplementarVO().getCargaHorasPermitidasPeriodoLetivo()) 
			&& getTipoAtividadeComplementarVO().getCargaHorasPermitidasPeriodoLetivo() < (getTipoAtividadeComplementarVO().getCargaHorasJaRealizadaPeriodoLetivo() + getCargaHorariaConsiderada())
			&& getCargaHorariaConsiderada() > 0)
			||
			(Uteis.isAtributoPreenchido(getTipoAtividadeComplementarVO().getCargaHorasPermitidasPeriodoLetivo()) 
			&& getTipoAtividadeComplementarVO().getCargaHorasPermitidasPeriodoLetivo() < getCargaHorariaConsiderada())
			&& getCargaHorariaConsiderada() > 0){
			return true;
			
		}
		return false;
	}

	public boolean isChLiberadaPorOperacaoFuncionalidade() {
		return Uteis.isAtributoPreenchido(getOperacaoFuncionalidadeVO().getOrigem());
	}
	
	public String getObservacao() {
		if(observacao == null){
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public SituacaoAtividadeComplementarMatriculaEnum getSituacaoAtividadeComplementarMatricula() {
		if (situacaoAtividadeComplementarMatricula == null) {
			situacaoAtividadeComplementarMatricula = SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO;
		}
		return situacaoAtividadeComplementarMatricula;
	}

	public void setSituacaoAtividadeComplementarMatricula(SituacaoAtividadeComplementarMatriculaEnum situacaoAtividadeComplementarMatricula) {
		this.situacaoAtividadeComplementarMatricula = situacaoAtividadeComplementarMatricula;
	}

	public UsuarioVO getResponsavelDeferimentoIndeferimento() {
		if (responsavelDeferimentoIndeferimento == null) {
			responsavelDeferimentoIndeferimento = new UsuarioVO();
		}
		return responsavelDeferimentoIndeferimento;
	}

	public void setResponsavelDeferimentoIndeferimento(UsuarioVO responsavelDeferimentoIndeferimento) {
		this.responsavelDeferimentoIndeferimento = responsavelDeferimentoIndeferimento;
	}

	public Date getDataDeferimentoIndeferimento() {
		if (dataDeferimentoIndeferimento == null) {
			dataDeferimentoIndeferimento = new Date();
		}
		return dataDeferimentoIndeferimento;
	}

	public void setDataDeferimentoIndeferimento(Date dataDeferimentoIndeferimento) {
		this.dataDeferimentoIndeferimento = dataDeferimentoIndeferimento;
	}

	public String getMotivoIndeferimento() {
		if (motivoIndeferimento == null) {
			motivoIndeferimento = "";
		}
		return motivoIndeferimento;
	}

	public void setMotivoIndeferimento(String motivoIndeferimento) {
		this.motivoIndeferimento = motivoIndeferimento;
	}

	public Integer getCargaHorariaAguardandoDeferimento() {
		if (cargaHorariaAguardandoDeferimento == null) {
			cargaHorariaAguardandoDeferimento = 0;
		}
		return cargaHorariaAguardandoDeferimento;
	}

	public void setCargaHorariaAguardandoDeferimento(Integer cargaHorariaAguardandoDeferimento) {
		this.cargaHorariaAguardandoDeferimento = cargaHorariaAguardandoDeferimento;
	}

	public Integer getCargaHorariaIndeferido() {
		if (cargaHorariaIndeferido == null) {
			cargaHorariaIndeferido = 0;
		}
		return cargaHorariaIndeferido;
	}

	public void setCargaHorariaIndeferido(Integer cargaHorariaIndeferido) {
		this.cargaHorariaIndeferido = cargaHorariaIndeferido;
	}
	
	
	public List<RegistroAtividadeComplementarMatriculaPeriodoVO> getRegistroAtividadeComplementarMatriculaPeriodoVOs() {
		if (registroAtividadeComplementarMatriculaPeriodoVOs == null) {
			registroAtividadeComplementarMatriculaPeriodoVOs = new ArrayList<RegistroAtividadeComplementarMatriculaPeriodoVO>(0);
		}
		return registroAtividadeComplementarMatriculaPeriodoVOs;
	}

	public void setRegistroAtividadeComplementarMatriculaPeriodoVOs(List<RegistroAtividadeComplementarMatriculaPeriodoVO> registroAtividadeComplementarMatriculaPeriodoVOs) {
		this.registroAtividadeComplementarMatriculaPeriodoVOs = registroAtividadeComplementarMatriculaPeriodoVOs;
	}

	public Map<String, RegistroAtividadeComplementarMatriculaPeriodoVO> getMapRegistroAtividadeComplementarMatriculaPeriodoVOs() {
		if (mapRegistroAtividadeComplementarMatriculaPeriodoVOs == null) {
			mapRegistroAtividadeComplementarMatriculaPeriodoVOs = new HashMap<String, RegistroAtividadeComplementarMatriculaPeriodoVO>(0);
		}
		return mapRegistroAtividadeComplementarMatriculaPeriodoVOs;
	}

	public void setMapRegistroAtividadeComplementarMatriculaPeriodoVOs(Map<String, RegistroAtividadeComplementarMatriculaPeriodoVO> mapRegistroAtividadeComplementarMatriculaPeriodoVOs) {
		this.mapRegistroAtividadeComplementarMatriculaPeriodoVOs = mapRegistroAtividadeComplementarMatriculaPeriodoVOs;
	}

	public Integer getCargaHorariaMinimaExigida() {
		if (cargaHorariaMinimaExigida == null) {
			cargaHorariaMinimaExigida = 0;
		}
		return cargaHorariaMinimaExigida;
	}

	public void setCargaHorariaMinimaExigida(Integer cargaHorariaMinimaExigida) {
		this.cargaHorariaMinimaExigida = cargaHorariaMinimaExigida;
	}

	public String getTipoAtividadeComplementarApresentar() {
		if (tipoAtividadeComplementarApresentar == null) {
			tipoAtividadeComplementarApresentar = "";
		}
		return tipoAtividadeComplementarApresentar;
	}

	public void setTipoAtividadeComplementarApresentar(String tipoAtividadeComplementarApresentar) {
		this.tipoAtividadeComplementarApresentar = tipoAtividadeComplementarApresentar;
	}

	public String getCargaHorariaExcedida() {
		if (cargaHorariaExcedida == null) {
			cargaHorariaExcedida = "";
		}
		return cargaHorariaExcedida;
	}

	public void setCargaHorariaExcedida(String cargaHorariaExcedida) {
		this.cargaHorariaExcedida = cargaHorariaExcedida;
	}

	public Boolean getAtividadeComplementarIntegraliazada() {
		if (atividadeComplementarIntegraliazada == null) {
			atividadeComplementarIntegraliazada = false;
		}
		return atividadeComplementarIntegraliazada;
	}

	public void setAtividadeComplementarIntegraliazada(Boolean atividadeComplementarIntegraliazada) {
		this.atividadeComplementarIntegraliazada = atividadeComplementarIntegraliazada;
	}

	public String getJustificativaAlteracaoCHConsiderada() {
		if (justificativaAlteracaoCHConsiderada == null) {
			justificativaAlteracaoCHConsiderada = "";
		}
		return justificativaAlteracaoCHConsiderada;
	}

	public void setJustificativaAlteracaoCHConsiderada(String justificativaAlteracaoCHConsiderada) {
		this.justificativaAlteracaoCHConsiderada = justificativaAlteracaoCHConsiderada;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public String getData_Apresentar() {
		return (Uteis.getDataComHora(dataCriacao));
	}

	public UsuarioVO getResponsavelEditarCHConsiderada() {
		if (responsavelEditarCHConsiderada == null) {
			responsavelEditarCHConsiderada = new UsuarioVO();
		}
		return responsavelEditarCHConsiderada;
	}

	public void setResponsavelEditarCHConsiderada(UsuarioVO responsavelEditarCHConsiderada) {
		this.responsavelEditarCHConsiderada = responsavelEditarCHConsiderada;
	}

	public Date getDataEditarCHConsiderada() {
		return dataEditarCHConsiderada;
	}

	public void setDataEditarCHConsiderada(Date dataEditarCHConsiderada) {
		this.dataEditarCHConsiderada = dataEditarCHConsiderada;
	}
	public String getDataEditarCHConsiderada_Apresentar() {
		return (Uteis.getData(dataEditarCHConsiderada));
	}
}