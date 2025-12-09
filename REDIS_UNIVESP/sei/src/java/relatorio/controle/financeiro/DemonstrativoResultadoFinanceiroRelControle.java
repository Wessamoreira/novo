package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.financeiro.DemonstrativoResultadoFinanceiroTurmaRelVO;

@Controller("DemonstrativoResultadoFinanceiroRelControle")
@Lazy
@Scope("viewScope")
public class DemonstrativoResultadoFinanceiroRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1699799093025744583L;
	private List<UnidadeEnsinoVO> unidadeEnsinoVOs;	
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private List<SelectItem> listaSelectItemMes;
	private List<SelectItem> listaSelectItemNivelEducacional;
	private Integer mesInicio;
	private Integer anoInicio;
	private Integer mesTermino;
	private Integer anoTermino;
	private String nivelEducacional;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private String layout;
	private List<CursoVO> listaConsultaCurso;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private Boolean filtrarDataFatoGerador;
	private Boolean filtrarUnidadeEnsino;
	
	private Boolean marcarTodasUnidadeEnsino;
	
	private DemonstrativoResultadoFinanceiroTurmaRelVO demonstrativoResultadoFinanceiroTurmaRelVO;
	
	public DemonstrativoResultadoFinanceiroRelControle() {
		setDemonstrativoResultadoFinanceiroTurmaRelVO(null);
	}
	
	public void marcarTodasUnidadesEnsinoAction(){
    	for(UnidadeEnsinoVO unidade :getUnidadeEnsinoVOs()){
    		if(marcarTodasUnidadeEnsino){
    			unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
    		}else {
    			unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
    	}
    }

	public void realizarGeracaoRelatorio() {
		try{
			if (getAnoTermino().intValue() < getAnoInicio().intValue()) {
				throw new Exception("O Período deve possuir o ano de início inferior ou igual ao ano de término!");
			} else if (getAnoTermino().intValue() == getAnoInicio().intValue()) {
				if (getMesTermino().intValue() < getMesInicio().intValue()) {
					throw new Exception("O Período deve possuir o mês de início inferior ou igual ao mês de término!");
				}
			}
			this.setCaminhoRelatorio(getFacadeFactory().getDemonstrativoResultadoFinanceiroRelFacade().realizarGeracaoRelatorio(getUnidadeEnsinoVOs(), getCursoVO(), getTurmaVO(), 
					getMesInicio(), getAnoInicio(), getMesTermino(), getAnoTermino(), getNivelEducacional(), getUsuarioLogado(), getFiltrarDataFatoGerador()));
			setFazerDownload(true);
			limparMensagem();
		}catch(Exception e){
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCurso() {
		try {

			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCursoVO(obj);
			setTurmaVO(null);
		} catch (Exception e) {
		}
	}

	List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), 0, false, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaVO(obj);
		setCursoVO(getTurmaVO().getCurso());
		obj = null;
		setValorConsultaTurma(null);
		setCampoConsultaTurma(null);
		Uteis.liberarListaMemoria(getListaConsultaTurma());
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	public void limparTurma() {
		setTurmaVO(null);
	}

	public void limparCurso() {
		setCursoVO(null);
	}

	public void limparUnidadeEnsino() {
		limparCurso();
		limparTurma();
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public Integer getColumn() {
        if (getUnidadeEnsinoVOs().size() > 4) {
            return 4;
        }
        return getUnidadeEnsinoVOs().size();
    }

    public Integer getElement() {
        return getUnidadeEnsinoVOs().size();
    }

	

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public List<SelectItem> getListaSelectItemMes() {
		if (listaSelectItemMes == null) {
			listaSelectItemMes = new ArrayList<SelectItem>(0);
			for (MesAnoEnum mesAnoEnum : MesAnoEnum.values()) {
				listaSelectItemMes.add(new SelectItem(Integer.valueOf(mesAnoEnum.getKey()), mesAnoEnum.getMes()));
			}
		}
		return listaSelectItemMes;
	}

	public void setListaSelectItemMes(List<SelectItem> listaSelectItemMes) {
		this.listaSelectItemMes = listaSelectItemMes;
	}

	public List<SelectItem> getListaSelectItemLayout() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("layout1", "Layout 1"));
		objs.add(new SelectItem("layout2", "Layout 2"));
		return objs;
	}
	
	public List<SelectItem> getListaSelectItemNivelEducacional() {
		if(listaSelectItemNivelEducacional == null){
			listaSelectItemNivelEducacional = new ArrayList<SelectItem>();
			listaSelectItemNivelEducacional.add(new SelectItem("", ""));
			listaSelectItemNivelEducacional.add(new SelectItem("POS_GRADUACAO", "Pós-Gradução"));
			listaSelectItemNivelEducacional.add(new SelectItem("GRADUACAO", "Graduação"));
			listaSelectItemNivelEducacional.add(new SelectItem("EXTENSAO", "Extensão Pós-Graduação"));
		}
		return listaSelectItemNivelEducacional;
	}

	public void setListaSelectItemNivelEducacional(List<SelectItem> listaSelectItemNivelEducacional) {
		this.listaSelectItemNivelEducacional = listaSelectItemNivelEducacional;
	}

	public Integer getMesInicio() {
		if (mesInicio == null) {
			mesInicio = Uteis.getMesDataAtual();
		}
		return mesInicio;
	}

	public void setMesInicio(Integer mesInicio) {
		this.mesInicio = mesInicio;
	}

	public Integer getAnoInicio() {
		if (anoInicio == null) {
			anoInicio = Integer.valueOf(Uteis.getAnoDataAtual4Digitos());
		}
		return anoInicio;
	}

	public void setAnoInicio(Integer anoInicio) {
		this.anoInicio = anoInicio;
	}

	public Integer getMesTermino() {
		if (mesTermino == null) {
			mesTermino = Uteis.getMesDataAtual();
		}
		return mesTermino;
	}

	public void setMesTermino(Integer mesTermino) {
		this.mesTermino = mesTermino;
	}

	public Integer getAnoTermino() {
		if (anoTermino == null) {
			anoTermino = Integer.valueOf(Uteis.getAnoDataAtual4Digitos());
		}
		return anoTermino;
	}

	public void setAnoTermino(Integer anoTermino) {
		this.anoTermino = anoTermino;
	}
	
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getNivelEducacional() {
		if(nivelEducacional == null){
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

    /**
     * @return the filtrarDataFatoGerador
     */
    public Boolean getFiltrarDataFatoGerador() {
        if (filtrarDataFatoGerador == null) {
            filtrarDataFatoGerador = Boolean.TRUE;
        }
        return filtrarDataFatoGerador;
    }

    /**
     * @param filtrarDataFatoGerador the filtrarDataFatoGerador to set
     */
    public void setFiltrarDataFatoGerador(Boolean filtrarDataFatoGerador) {
        this.filtrarDataFatoGerador = filtrarDataFatoGerador;
    }

	public List<UnidadeEnsinoVO> getUnidadeEnsinoVOs() {
		if(unidadeEnsinoVOs == null){
			try {
				unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());				
			} catch (Exception e) {

			}
		}
		return unidadeEnsinoVOs;
	}

	public void setUnidadeEnsinoVOs(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		this.unidadeEnsinoVOs = unidadeEnsinoVOs;
	}

	public Boolean getFiltrarUnidadeEnsino() {
		if(filtrarUnidadeEnsino == null){
			filtrarUnidadeEnsino = Boolean.FALSE;
		}
		return filtrarUnidadeEnsino;
	}

	public void setFiltrarUnidadeEnsino(Boolean filtrarUnidadeEnsino) {
		this.filtrarUnidadeEnsino = filtrarUnidadeEnsino;
	}

	public Boolean getMarcarTodasUnidadeEnsino() {
		if(marcarTodasUnidadeEnsino == null){
			marcarTodasUnidadeEnsino = Boolean.FALSE;
		}
		return marcarTodasUnidadeEnsino;
	}

	public void setMarcarTodasUnidadeEnsino(Boolean marcarTodasUnidadeEnsino) {
		this.marcarTodasUnidadeEnsino = marcarTodasUnidadeEnsino;
	}
	


	public DemonstrativoResultadoFinanceiroTurmaRelVO getDemonstrativoResultadoFinanceiroTurmaRelVO() {
		if (demonstrativoResultadoFinanceiroTurmaRelVO == null) {
			demonstrativoResultadoFinanceiroTurmaRelVO = new DemonstrativoResultadoFinanceiroTurmaRelVO();
		}
		return demonstrativoResultadoFinanceiroTurmaRelVO;
	}

	public void setDemonstrativoResultadoFinanceiroTurmaRelVO(DemonstrativoResultadoFinanceiroTurmaRelVO demonstrativoResultadoFinanceiroTurmaRelVO) {
		this.demonstrativoResultadoFinanceiroTurmaRelVO = demonstrativoResultadoFinanceiroTurmaRelVO;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "layout1";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String criarDemonstrativoResultado() {
		try {
			getFacadeFactory().getDemonstrativoResultadoFinanceiroRelFacade().criarDemonstrativoResultado(getUnidadeEnsinoVOs(), getCursoVO(), getTurmaVO(), getNivelEducacional(), getDataInicio(), getDataFinal(), getFiltrarDataFatoGerador(), getDemonstrativoResultadoFinanceiroTurmaRelVO());
			return "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

	public Date getDataInicio() {
		Calendar calInicio = Calendar.getInstance();
		calInicio.set(Calendar.DAY_OF_MONTH, 1);
		calInicio.set(Calendar.MONTH, getMesInicio());
		calInicio.set(Calendar.YEAR, getAnoInicio());
		return calInicio.getTime();
	}
	
	public Date getDataFinal() {
		Calendar calInicio = Calendar.getInstance();
		calInicio.set(Calendar.DAY_OF_MONTH, 1);
		calInicio.set(Calendar.MONTH, getMesTermino());
		calInicio.set(Calendar.YEAR, getAnoTermino());
		return UteisData.getUltimaDataMes(calInicio.getTime());
	}
}
