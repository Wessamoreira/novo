package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.ControleVagaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.ControleVagaRel;

@SuppressWarnings("unchecked")
@Controller("ControleVagaRelControle")
@Scope("viewScope")
@Lazy
public class ControleVagaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private ControleVagaRelVO ControleVagaRelVO;
    private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
    private String valorConsultaCurso;
    private String campoConsultaCurso;
    private TurmaVO turmaVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private List<TurmaVO> listaConsultaTurma;
    private List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private String campoFiltroPor;
    private String ano;
    private String semestre;
    private Boolean isUnificarTurmas;
    private Boolean isDetalharCalouroVeterano;
    private String tipoRelatorio;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private String periodicidade;
    private List<SelectItem> listaPeriodicidade;

    public ControleVagaRelControle() throws Exception {
        montarListaSelectItemUnidadeEnsino();
        setTipoRelatorio("SI");
        verificarLayoutPadrao();
        verificarParametroRelatorio();
        consultarCursoFiltroRelatorio();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List<ControleVagaRelVO> listaObjetos = new ArrayList<ControleVagaRelVO>(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ControleVagaRelControle", "Inicializando Geração de Relatório Controle de Vaga", "Emitindo Relatório");
            List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs = getUnidadeEnsinoCursoVOs().stream().filter(u -> u.getFiltrarUnidadeEnsinoCurso()).collect(Collectors.toList());
            getFacadeFactory().getControleVagaRelFacade().validarDados(getTurmaVO(), unidadeEnsinoCursoVOs, getControleVagaRelVO(), getCampoFiltroPor(), getPeriodicidade(), getAno(), getSemestre());
            listaObjetos = getFacadeFactory().getControleVagaRelFacade().criarObjeto(getControleVagaRelVO(), getTurmaVO(), unidadeEnsinoCursoVOs,getIsUnificarTurmas(), getIsDetalharCalouroVeterano(), getCampoFiltroPor(), getTipoRelatorio(), getAno(), getSemestre());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(ControleVagaRel.getDesignIReportRelatorio(getTipoRelatorio()));
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(ControleVagaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Controle de Vaga");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(ControleVagaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa(super.getUnidadeEnsinoLogado().getNome());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().adicionarParametro("ano", getAno());
                getSuperParametroRelVO().adicionarParametro("semestre", getSemestre());
                getSuperParametroRelVO().adicionarParametro("detalharVeteranoCalouro", getIsDetalharCalouroVeterano());
                getSuperParametroRelVO().adicionarParametro("unificarTurmas", getIsUnificarTurmas());
                getSuperParametroRelVO().adicionarParametro("campoFiltroPor", getCampoFiltroPor());
                if (getCampoFiltroPor().equals("curso")) {
                	StringBuilder cursos = new StringBuilder();
                	String aux = "";
                	for (UnidadeEnsinoCursoVO uec : unidadeEnsinoCursoVOs) {
                		cursos.append(aux).append(uec.getCurso().getNome());
                		aux = ", ";
                	}
                	if (!aux.isEmpty()) {
                		getSuperParametroRelVO().adicionarParametro("cursos", cursos.toString().length() > 160 ? cursos.toString().substring(0, 159).concat("...") : cursos.toString());
                	}
                } else if (getCampoFiltroPor().equals("turma")) {
                	getSuperParametroRelVO().adicionarParametro("turma", getTurmaVO().getIdentificadorTurma());
                }
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
		}
                realizarImpressaoRelatorio();
                persistirLayoutPadrao(getTipoRelatorio());
    			persistirParametroRelatorio();
//                removerObjetoMemoria(this);
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "ControleVagaRelControle", "Finalizando Geração de Relatório Controle de Vaga", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }
    
	private void persistirLayoutPadrao(String valor) throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "ControleVagaRel", "tipoRelatorio", getUsuarioLogado());
	}
	
	private void persistirParametroRelatorio() throws Exception {
		getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("ControleVagaRel", "unificarTurmas", getIsUnificarTurmas(), getUsuarioLogado());
		getFacadeFactory().getParametroRelatorioFacade().persistirParametroRelatorio("ControleVagaRel", "detalharCalouroVeterano", getIsDetalharCalouroVeterano(), getUsuarioLogado());
	}
	
	private void verificarLayoutPadrao() throws Exception {
		LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("ControleVagaRel", "tipoRelatorio", false, getUsuarioLogado());
		setTipoRelatorio(layoutPadraoVO.getValor());
	}
	
	private void verificarParametroRelatorio() throws Exception {
		setIsUnificarTurmas(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("ControleVagaRel", "unificarTurmas", false, getUsuarioLogado()).getApresentarCampo());
		setIsDetalharCalouroVeterano(getFacadeFactory().getParametroRelatorioFacade().consultarPorEntidadeCampo("ControleVagaRel", "detalharCalouroVeterano", false, getUsuarioLogado()).getApresentarCampo());
	}

    public void montarDadosFiltro() throws Exception {
//        getTurmaVO().setTurno(getUnidadeEnsinoCursoVO().getTurno());
    }

    public void limparDadosCursoTurma() throws Exception {
        setTurmaVO(new TurmaVO());
        setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
        setAno("");
        setSemestre("");
        getListaConsultaCurso().clear();
        getListaConsultaTurma().clear();
        getUnidadeEnsinoCursoVOs().clear();
    }

    public void limparIdentificador() {
        setTurmaVO(new TurmaVO());
        getListaConsultaTurma().clear();
    }

    public void limparDadosCurso() {
        setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
        getListaConsultaCurso().clear();
    }

    public List<SelectItem> getTipoConsultaComboFiltroPor() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("curso", "Curso"));
        itens.add(new SelectItem("turma", "Turma"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaComboCurso() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void consultarCurso() {
        try {
            List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
            if (getCampoConsultaCurso().equals("codigo")) {
                if (getValorConsultaCurso().equals("")) {
                    setValorConsultaCurso("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCurso());
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt, getUnidadeEnsinoVO().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCurso() throws Exception {
        try {
            setUnidadeEnsinoCursoVO((UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("cursoItens"));
            if (!getUnidadeEnsinoCursoVO().getCurso().getNivelEducacionalPosGraduacao() || !getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("IN")) {
                setMensagemDetalhada("");
                adicionarCurso(getUnidadeEnsinoCursoVO());
            } else {
                throw new Exception("Para Controle de Vaga de Pós-Graduação, por favor usar a consulta por turma.");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    @SuppressWarnings("rawtypes")
    public void adicionarCurso(UnidadeEnsinoCursoVO obj) {
    	try {
    		int index = 0;
			Iterator j = getUnidadeEnsinoCursoVOs().iterator();
			while (j.hasNext()) {
				UnidadeEnsinoCursoVO objExistente = (UnidadeEnsinoCursoVO) j.next();
				if ((objExistente.getCurso().getCodigo().equals(obj.getCurso().getCodigo())) && (obj.getTurno().getCodigo().equals(obj.getTurno().getCodigo())) && (objExistente.getUnidadeEnsino().equals(obj.getUnidadeEnsino()))) {
					getUnidadeEnsinoCursoVOs().set(index, obj);
					return;
				}
				index++;
			}
    		getUnidadeEnsinoCursoVOs().add(obj);    		
    	} catch (Exception e) {
    		
    	}
    }
    
    @SuppressWarnings("rawtypes")
	public void removerCurso() {
    	try {
    		setUnidadeEnsinoCursoVO((UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("cursoItens"));
    		Iterator i = getUnidadeEnsinoCursoVOs().iterator();
    		while (i.hasNext()) {
    			UnidadeEnsinoCursoVO uni = (UnidadeEnsinoCursoVO)i.next();
    			if (uni.getCodigo().intValue() == getUnidadeEnsinoCursoVO().getCodigo().intValue()) {
    				getUnidadeEnsinoCursoVOs().remove(uni);
    				break;
    			}
    		}
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
	public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void consultarTurma() {
        try {
            List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoIdentificadorTurma(getUnidadeEnsinoVO().getCodigo().intValue(), getValorConsultaTurma(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeCurso")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
        obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
        setTurmaVO(obj);
        setAno("");
        setSemestre("");
        valorConsultaTurma = "";
        campoConsultaTurma = "";
        listaConsultaTurma.clear();
    }

    public List<SelectItem> getTipoConsultaComboSemestre() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("1", "1º"));
        itens.add(new SelectItem("2", "2º"));
        return itens;
    }
    
    public void montarListaSelectItemUnidadeEnsino() {
        try {
        	setIsUnificarTurmas(false);
        	List<UnidadeEnsinoVO> resultadoConsulta = new ArrayList<UnidadeEnsinoVO>(0);
        	if (Uteis.isAtributoPreenchido(super.getUnidadeEnsinoLogado().getCodigo())) {
        		resultadoConsulta.add(super.getUnidadeEnsinoLogado());
        	} else {
        		resultadoConsulta = consultarUnidadeEnsinoPorNome("");
        	}
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false));
            if (getCampoFiltroPor().equals("turma")) {
            	setUnidadeEnsinoCursoVOs(new ArrayList<>(0));
            } else {
            	limparListasConsultas();
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }
    
    public void limparListasConsultas() {
        setTurmaVO(null);
        getUnidadeEnsinoCursoVOs().clear();
        getListaConsultaTurma().clear();
        setUnidadeEnsinoCursoApresenta("");
        consultarCursoFiltroRelatorio();
    }
    
    /**
     * @return the valorConsultaCurso
     */
    public String getValorConsultaCurso() {
        return valorConsultaCurso;
    }

    /**
     * @param valorConsultaCurso
     *            the valorConsultaCurso to set
     */
    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    /**
     * @return the campoConsultaCurso
     */
    public String getCampoConsultaCurso() {
        return campoConsultaCurso;
    }

    /**
     * @param campoConsultaCurso
     *            the campoConsultaCurso to set
     */
    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    /**
     * @return the listaConsultaCurso
     */
    public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
        }
        return listaConsultaCurso;
    }

    /**
     * @param listaConsultaCurso
     *            the listaConsultaCurso to set
     */
    public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    /**
     * @return the ControleVagaRelVO
     */
    public ControleVagaRelVO getControleVagaRelVO() {
        if (ControleVagaRelVO == null) {
            ControleVagaRelVO = new ControleVagaRelVO();
        }
        return ControleVagaRelVO;
    }

    /**
     * @param ControleVagaRelVO
     *            the ControleVagaRelVO to set
     */
    public void setControleVagaRelVO(ControleVagaRelVO ControleVagaRelVO) {
        this.ControleVagaRelVO = ControleVagaRelVO;
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

    public String getCampoFiltroPor() {
        if (campoFiltroPor == null) {
            campoFiltroPor = "curso";
        }
        return campoFiltroPor;
    }

    public void setCampoFiltroPor(String campoFiltroPor) {
        this.campoFiltroPor = campoFiltroPor;
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

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
        if (unidadeEnsinoCursoVO == null) {
            unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
        }
        return unidadeEnsinoCursoVO;
    }

    public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
        this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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

    public String getAno() {
        if (ano == null) {
            return "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public Boolean getIsFiltrarPorturma() {
        if (getCampoFiltroPor().equals("turma")) {
            return true;
        }
        return false;
    }

    public Boolean getIsFiltrarPorCurso() {
        if (getCampoFiltroPor().equals("curso")) {
            return true;
        }
        return false;
    }

    public Boolean getIsFiltrarPorAno() {
        if ((getTurmaVO().getCodigo() != 0 && !getTurmaVO().getCurso().getNivelEducacionalPosGraduacao() && !getTurmaVO().getTurmaAgrupada()
                && (getTurmaVO().getCurso().getPeriodicidade().equals("AN") || getTurmaVO().getCurso().getPeriodicidade().equals("SE")))
                || (getCampoFiltroPor().equals("curso") && !getUnidadeEnsinoCursoVOs().isEmpty())) {
            return true;
        } else {
            if (getTurmaVO().getTurmaAgrupada()) {
                for (TurmaAgrupadaVO turmaAgrupada : getTurmaVO().getTurmaAgrupadaVOs()) {
                    if (!turmaAgrupada.getTurma().getAnual() && !turmaAgrupada.getTurma().getSemestral()) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public Boolean getIsFiltrarPorSemestre() {
        if ((getTurmaVO().getCodigo() != 0 && !getTurmaVO().getCurso().getNivelEducacionalPosGraduacao() && !getTurmaVO().getTurmaAgrupada()
                && getTurmaVO().getCurso().getPeriodicidade().equals("SE"))
                || (getCampoFiltroPor().equals("curso") && !getUnidadeEnsinoCursoVOs().isEmpty())) {
            return true;
        } else {
            if (getTurmaVO().getTurmaAgrupada()) {
                for (TurmaAgrupadaVO turmaAgrupada : getTurmaVO().getTurmaAgrupadaVOs()) {
                    if (!turmaAgrupada.getTurma().getSemestral()) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

	public List<UnidadeEnsinoCursoVO> getUnidadeEnsinoCursoVOs() {
		if (unidadeEnsinoCursoVOs == null) {
			unidadeEnsinoCursoVOs = new ArrayList<UnidadeEnsinoCursoVO>();
		}
		return unidadeEnsinoCursoVOs;
	}

	public void setUnidadeEnsinoCursoVOs(List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs) {
		this.unidadeEnsinoCursoVOs = unidadeEnsinoCursoVOs;
	}
	
	public Boolean getIsUnificarTurmas() {
		if (isUnificarTurmas == null) {
			isUnificarTurmas = false;
		}
		return isUnificarTurmas;
	}

	public void setIsUnificarTurmas(Boolean isUnificarTurmas) {
		this.isUnificarTurmas = isUnificarTurmas;
	}

	public Boolean getIsDetalharCalouroVeterano() {
		if (isDetalharCalouroVeterano == null) {
			isDetalharCalouroVeterano = false;
		}
		return isDetalharCalouroVeterano;
	}

	public void setIsDetalharCalouroVeterano(Boolean isDetalharCalouroVeterano) {
		this.isDetalharCalouroVeterano = isDetalharCalouroVeterano;
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	
    public List<SelectItem> getTipoRelatorioCombo() {
    	List<SelectItem> itens = new ArrayList<SelectItem>(0);
    	itens.add(new SelectItem("SI", "Sintético"));
    	itens.add(new SelectItem("AN", "Analítico"));
    	return itens;
    }
    
    private Boolean marcarTodosCursos;
    private String unidadeEnsinoCursoApresenta;
    
    public Boolean getMarcarTodosCursos() {
		if (marcarTodosCursos == null) {
			marcarTodosCursos = false;
		}
    	return marcarTodosCursos;
	}
    
    public void setMarcarTodosCursos(Boolean marcarTodosCursos) {
		this.marcarTodosCursos = marcarTodosCursos;
	}
    
    public String getUnidadeEnsinoCursoApresenta() {
		if (unidadeEnsinoCursoApresenta == null) {
			unidadeEnsinoCursoApresenta = "";
		}
    	return unidadeEnsinoCursoApresenta;
	}
    
    public void setUnidadeEnsinoCursoApresenta(String unidadeEnsinoCursoApresenta) {
		this.unidadeEnsinoCursoApresenta = unidadeEnsinoCursoApresenta;
	}
    
    public void realizarMarcacaoCursos() {
		for (UnidadeEnsinoCursoVO unidadeEnsinoCursoVO : getUnidadeEnsinoCursoVOs()) {
			unidadeEnsinoCursoVO.setFiltrarUnidadeEnsinoCurso(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}
    
    public void verificarTodosCursosSelecionados() {
    	setUnidadeEnsinoCursoApresenta("");
		StringBuilder curso = new StringBuilder();
		if (getUnidadeEnsinoCursoVOs().size() > 1) {
			for (UnidadeEnsinoCursoVO obj : getUnidadeEnsinoCursoVOs()) {
				if (obj.getFiltrarUnidadeEnsinoCurso()) {
					curso.append(obj.getCurso().getNome()).append(" - ");
					curso.append(obj.getTurno().getNome()).append("; ");
				}
			}
			setUnidadeEnsinoCursoApresenta(curso.toString());
		} else {
			if (!getUnidadeEnsinoCursoVOs().isEmpty()) {
				if (getUnidadeEnsinoCursoVOs().get(0).getFiltrarUnidadeEnsinoCurso()) {
					setUnidadeEnsinoCursoApresenta(getUnidadeEnsinoCursoVOs().get(0).getCurso().getNome());
				}
			} else {
				setUnidadeEnsinoCursoApresenta(curso.toString());
			}
		}
	}
    
    public void consultarCursoFiltroRelatorio() {
		try {
			setUnidadeEnsinoCursoApresenta("");
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO().getCodigo())) {
				setUnidadeEnsinoCursoVOs(null);
				return;
			}
			setUnidadeEnsinoCursoVOs(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsinoNivelEducacional("", getUnidadeEnsinoVO().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getPeriodicidade()));
			verificarTodosCursosSelecionados();
		} catch (Exception e) {
			setUnidadeEnsinoCursoVOs(null);
		}
	}
    
    public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = "AN";
		}
    	return periodicidade;
	}
    
    public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}
    
    public List<SelectItem> getListaPeriodicidade() {
		if (listaPeriodicidade == null) {
			listaPeriodicidade = new ArrayList<SelectItem>(0);
			for (PeriodicidadeEnum obj : PeriodicidadeEnum.values()) {
				listaPeriodicidade.add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		}
    	return listaPeriodicidade;
	}
    
    public void setListaPeriodicidade(List<SelectItem> listaPeriodicidade) {
		this.listaPeriodicidade = listaPeriodicidade;
	}
    
    public Boolean getApresentarFiltroAno() {
    	if (getIsFiltrarPorCurso()) {
    		return getPeriodicidade().equals("IN") ? false : true;
    	} if (getTurmaVO().getAnual()) {
			return true;
		} else {
    		return getIsFiltrarPorSemestre();
    	}
    }
    
    public Boolean getApresentarFiltroSemestre() {
    	if (getIsFiltrarPorCurso()) {
    		return getPeriodicidade().equals("SE") ? true : false;
    	} else {
    		return getIsFiltrarPorSemestre();
    	}
    }
    
}