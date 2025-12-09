package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.MediaDescontoTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.MediaDescontoTurmaRel;

@SuppressWarnings("unchecked")
@Controller("MediaDescontoTurmaRelControle")
@Scope("viewScope")
@Lazy
public class MediaDescontoTurmaRelControle extends SuperControleRelatorio {

    protected List listaSelectItemTurma;
    protected List listaConsultaTurma;
    protected String valorConsultaTurma;
    protected String campoConsultaTurma;
    protected MediaDescontoTurmaRelVO mediaDescontoTurmaRelVO;
    private List<String> situacoes;
    private Map<String, String> situacoesContaReceber;
    private Boolean marcarTodos;
    private String tipoOrdenacao;

    public MediaDescontoTurmaRelControle() throws Exception {

        inicializarListasSelectItemTodosComboBox();
        montarSituacoesContaReceber();
		if(getSituacoes() == null) {
			setSituacoes(new ArrayList<String>(0));
			getSituacoes().add("AR");
		}
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

//    public void imprimirPDF() {
//        String titulo = null;
//        String nomeEmpresa = null;
//        String design = null;
//        List<MediaDescontoTurmaRelVO> listaMediaDescontosTurmaRelVO = new ArrayList<MediaDescontoTurmaRelVO>(0);
//        try {
//            MediaDescontoTurmaRel.validarDados(getMediaDescontoTurmaRelVO());
//            getFacadeFactory().getMediaDescontoTurmaRelFacade().setDescricaoFiltros("");
//            titulo = " Relatório da média dos descontos por Turma";
//            nomeEmpresa = super.getUnidadeEnsinoLogado().getNome();
//            design = MediaDescontoTurmaRel.getDesignIReportRelatorio();
//
//            listaMediaDescontosTurmaRelVO = getFacadeFactory().getMediaDescontoTurmaRelFacade().criarObjeto(getMediaDescontoTurmaRelVO(), getUsuarioLogado());
//            if (listaMediaDescontosTurmaRelVO.isEmpty()) {
//                throw new ConsistirException(UteisJSF.internacionalizar("msg_relatorio_sem_dados"));
//            } else {
//                apresentarRelatorioObjetos(MediaDescontoTurmaRel.getIdEntidade(), titulo, nomeEmpresa, "", "PDF", "", design, getUsuarioLogado().getNome(), getFacadeFactory().getMediaDescontoTurmaRelFacade().getDescricaoFiltros(),
//                        listaMediaDescontosTurmaRelVO, MediaDescontoTurmaRel.getCaminhoBaseRelatorio());
//                removerObjetoMemoria(this);
////                inicializarListasSelectItemTodosComboBox();
//                setMensagemID("msg_relatorio_ok");
//            }
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        } finally {
//            titulo = null;
//            nomeEmpresa = null;
//            design = null;
//            Uteis.liberarListaMemoria(listaMediaDescontosTurmaRelVO);
//        }
//    }
    
    public void imprimirPDF() {
        String titulo = null;
        String nomeEmpresa = null;
        String design = null;
        List<MediaDescontoTurmaRelVO> listaMediaDescontosTurmaRelVO = new ArrayList<MediaDescontoTurmaRelVO>(0);
        try {
            MediaDescontoTurmaRel.validarDados(getMediaDescontoTurmaRelVO());
            getFacadeFactory().getMediaDescontoTurmaRelFacade().setDescricaoFiltros("");
            nomeEmpresa = super.getUnidadeEnsinoLogado().getNome();
 

            listaMediaDescontosTurmaRelVO = getFacadeFactory().getMediaDescontoTurmaRelFacade().criarObjeto(getMediaDescontoTurmaRelVO(), getUsuarioLogado(), getFiltroRelatorioFinanceiroVO(), getSituacoes(), getTipoOrdenacao());
            if (listaMediaDescontosTurmaRelVO.isEmpty()) {
                throw new ConsistirException(UteisJSF.internacionalizar("msg_relatorio_sem_dados"));
            } else {
				getSuperParametroRelVO().setNomeDesignIreport(MediaDescontoTurmaRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(MediaDescontoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório da média dos descontos por Turma");
				getSuperParametroRelVO().setListaObjetos(listaMediaDescontosTurmaRelVO);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(MediaDescontoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().adicionarParametro("tipoOrigem", getFiltroRelatorioFinanceiroVO().getItensFiltroTipoOrigem());
				getSuperParametroRelVO().adicionarParametro("situacaoContaReceber", getSituacoesApresentar());
				getSuperParametroRelVO().adicionarParametro("ano", getMediaDescontoTurmaRelVO().getAno());
				getSuperParametroRelVO().adicionarParametro("semestre", getMediaDescontoTurmaRelVO().getSemestre());
				realizarImpressaoRelatorio();
//                removerObjetoMemoria(this);
//                inicializarListasSelectItemTodosComboBox();
                setMensagemID("msg_relatorio_ok");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            nomeEmpresa = null;
            design = null;
            Uteis.liberarListaMemoria(listaMediaDescontosTurmaRelVO);
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TurmaCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultarTurma() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

//            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
//                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeUnidadeEnsino(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//            }

            if (getCampoConsultaTurma().equals("nomeCurso")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

//            if (getCampoConsultaTurma().equals("nomeTurno")) {
//                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurno(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }

    }

    public void consultarTurmaPorIdentificador() {
        try {
            if (getMediaDescontoTurmaRelVO().getTurmaVO().getIdentificadorTurma().equalsIgnoreCase("")) {
                return;
            } else {
                TurmaVO turmaVO = getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(
                        getMediaDescontoTurmaRelVO().getTurmaVO().getIdentificadorTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
                getMediaDescontoTurmaRelVO().setTurmaVO(turmaVO);
                setMensagemID("msg_dados_consultados");
            }
        } catch (Exception e) {
            limparDadosTurma();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        getMediaDescontoTurmaRelVO().setTurmaVO(obj);
        setCampoConsultaTurma("");
        setValorConsultaTurma("");
        setListaConsultaTurma(new ArrayList(0));

    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        //itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        //itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void limparDadosTurma() {
        getMediaDescontoTurmaRelVO().setTurmaVO(null);
    }

    private void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemOrdenacao();
    }

    public void montarListaSelectItemOrdenacao() {
        Vector opcoes = getFacadeFactory().getMediaDescontoTurmaRelFacade().getOrdenacoesRelatorio();
        Enumeration i = opcoes.elements();
        List objs = new ArrayList(0);
        int contador = 0;
        while (i.hasMoreElements()) {
            String opcao = (String) i.nextElement();
            objs.add(new SelectItem(new Integer(contador), opcao));
            contador++;
        }
        setListaSelectItemOrdenacoesRelatorio(objs);
    }

    /**
     * @return the listaSelectItemTurma
     */
    public List getListaSelectItemTurma() {
        return listaSelectItemTurma;
    }

    /**
     * @param listaSelectItemTurma
     *            the listaSelectItemTurma to set
     */
    public void setListaSelectItemTurma(List listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    /**
     * @return the listaConsultaTurma
     */
    public List getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    /**
     * @param listaConsultaTurma
     *            the listaConsultaTurma to set
     */
    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    /**
     * @return the valorConsultaTurma
     */
    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    /**
     * @param valorConsultaTurma
     *            the valorConsultaTurma to set
     */
    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    /**
     * @return the campoConsultaTurma
     */
    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";
        }
        return campoConsultaTurma;
    }

    /**
     * @param campoConsultaTurma
     *            the campoConsultaTurma to set
     */
    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    public MediaDescontoTurmaRelVO getMediaDescontoTurmaRelVO() {
        if (mediaDescontoTurmaRelVO == null) {
            mediaDescontoTurmaRelVO = new MediaDescontoTurmaRelVO();
        }
        return mediaDescontoTurmaRelVO;
    }

    public void setMediaDescontoTurmaRelVO(MediaDescontoTurmaRelVO mediaDescontoTurmaRelVO) {
        this.mediaDescontoTurmaRelVO = mediaDescontoTurmaRelVO;
    }
    
	public Boolean getApresentarAno() {
		return Uteis.isAtributoPreenchido(getMediaDescontoTurmaRelVO().getTurmaVO().getCurso().getCodigo()) && !getMediaDescontoTurmaRelVO().getTurmaVO().getCurso().getIntegral() 
				|| (Uteis.isAtributoPreenchido(getMediaDescontoTurmaRelVO().getTurmaVO().getCurso().getCodigo())) && !getMediaDescontoTurmaRelVO().getTurmaVO().getIntegral();
	}

	public Boolean getApresentarSemestre() {
		return Uteis.isAtributoPreenchido(getMediaDescontoTurmaRelVO().getTurmaVO().getCurso().getCodigo()) && getMediaDescontoTurmaRelVO().getTurmaVO().getCurso().getSemestral() 
				|| Uteis.isAtributoPreenchido(getMediaDescontoTurmaRelVO().getTurmaVO().getCodigo()) && getMediaDescontoTurmaRelVO().getTurmaVO().getSemestral();
	}
	
	public List<String> getSituacoes() {
		return situacoes;
	}

	public void setSituacoes(List<String> situacoes) {
		this.situacoes = situacoes;
	}
	
	public void montarSituacoesContaReceber() {
		situacoesContaReceber = new HashMap<>();
		situacoesContaReceber.put("A Receber", "AR");
		situacoesContaReceber.put("Recebido", "RE");
		situacoesContaReceber.put("Negociado", "NE");
		situacoesContaReceber.put("Cancelado", "CF");
	}
	
	public Map<String, String> getSituacoesContaReceber() {
		if (situacoesContaReceber == null) {
			montarSituacoesContaReceber();
		}
		return situacoesContaReceber;
	}

	public void setSituacoesContaReceber(Map<String, String> situacoesContaReceber) {
		this.situacoesContaReceber = situacoesContaReceber;
	}
	
	public Boolean getMarcarTodos() {
		if (marcarTodos == null) {
			marcarTodos = Boolean.TRUE;
		}
		return marcarTodos;
	}

	public void setMarcarTodos(Boolean marcarTodos) {
		this.marcarTodos = marcarTodos;
	}
	
	public void marcarDesmarcarTodosSituacao() {
		situacoes = getMarcarTodos() ? new ArrayList<>(situacoesContaReceber.values()) : null;
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodasSituacoesRequerimento() {
		if (getMarcarTodos()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
    public List getListaSelectItemTipoOrdenacaoRelatorio() throws Exception {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("dataVencimento", "Data Vencimento"));
        itens.add(new SelectItem("alunoDataVencimento", "Aluno/Data Vencimento"));
        return itens;
    }
    
    public String getTipoOrdenacao() {
        if (tipoOrdenacao == null) {
            tipoOrdenacao = "";
        }
        return tipoOrdenacao;
    }
    
    public void setTipoOrdenacao(String tipoOrdenacao) {
        this.tipoOrdenacao = tipoOrdenacao;
    }
    
    public String getSituacoesApresentar() {
		StringBuilder builder = new StringBuilder();
		if (getSituacoes().contains("RE")) {
			builder.append(builder.length()>0?", ":"").append(SituacaoContaReceber.RECEBIDO.getDescricao());
		}
		if (getSituacoes().contains("NE")) {
			builder.append(builder.length()>0?", ":"").append(SituacaoContaReceber.NEGOCIADO.getDescricao());
		}
		if (getSituacoes().contains("CF")) {
			builder.append(builder.length()>0?", ":"").append(SituacaoContaReceber.CANCELADO_FINANCEIRO.getDescricao());
		}
		if (getSituacoes().contains("AR")) {
			builder.append(builder.length()>0?", ":"").append(SituacaoContaReceber.A_RECEBER.getDescricao());
		}
		return builder.toString();
	}
}
