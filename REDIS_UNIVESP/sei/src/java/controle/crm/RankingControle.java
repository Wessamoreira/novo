package controle.crm;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.crm.ComissionamentoTurmaFaixaValorVO;
import negocio.comuns.crm.RankingTurmaConsultorAlunoVO;
import negocio.comuns.crm.RankingTurmaVO;
import negocio.comuns.crm.RankingVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Carlos
 */
@Controller("RankingControle")
@Scope("viewScope")
@Lazy
public class RankingControle extends SuperControle {

    private UnidadeEnsinoVO unidadeEnsinoVO;
    private TurmaVO turmaVO;
    private CursoVO cursoVO;
    private RankingTurmaVO rankingTurmaVO;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private String valorConsultaMes;
    private List listaConsultaCurso;
    private List listaSelectItemUnidadeEnsino;
    private List<RankingTurmaVO> listaRankingTurmaVO;
    private List<RankingVO> listaRankingVO;
    private RankingVO rankingVO;
    private Boolean apresentarAlunoConsiderado;

    public RankingControle() {
        inicializarListasSelectItemTodosComboBox();
    }

    public String novo() {

        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("rankingForm.xhtml");
    }

    public String editar() throws Exception {
        RankingTurmaVO obj = (RankingTurmaVO) context().getExternalContext().getRequestMap().get("rankingTurmaItens");
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("rankingForm.xhtml");
    }

    public void selecionarRankingTurma() {
        try {
        	setRankingTurmaVO(new RankingTurmaVO());
            RankingTurmaVO obj = (RankingTurmaVO) context().getExternalContext().getRequestMap().get("rankingTurmaItens");
            setRankingTurmaVO(obj);
//            setListaRankingVO(getFacadeFactory().getRankingFacade().consultarRanking(obj, getValorConsultaMes(), getUsuarioLogado()));
            setMensagemID("msg_dados_editar");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            getListaRankingVO().clear();
        }
        //return "editar";
    }
    
    public void selecionarRankingAlunoNaoConsiderado(){
    	setRankingVO(new RankingVO());
    	setRankingVO((RankingVO) context().getExternalContext().getRequestMap().get("rankingItens"));
        setApresentarAlunoConsiderado(false);
    }
    
    public List<RankingTurmaConsultorAlunoVO> getListaAlunoApresentar(){
    	return getApresentarAlunoConsiderado()? getRankingVO().getRankingTurmaConsultorAlunoAptoVOs(): getRankingVO().getRankingTurmaConsultorAlunoInaptoVOs();
    }
    
    public void selecionarRankingAlunoConsiderado() {
    	setRankingVO(new RankingVO());
    	setRankingVO((RankingVO) context().getExternalContext().getRequestMap().get("rankingItens"));
        setApresentarAlunoConsiderado(true);    	
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            setListaSelectItemUnidadeEnsino(getFacadeFactory().getRankingFacade().montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    @Override
    public String consultar() {
        try {
        	if (getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
        		throw new Exception("O campo Unidade Ensino deve ser informado");
        	}
        	if (getTurmaVO().getCodigo().intValue() == 0) {
        		throw new Exception("O campo Turma deve ser informado");
        	}
            setListaRankingTurmaVO(getFacadeFactory().getRankingFacade().consultarRankingTurma(getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getValorConsultaMes(), getUsuarioLogado()));
//            setListaRankingTurmaVO(getFacadeFactory().getRankingFacade().consultar(getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getValorConsultaMes(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("rankingForm.xhtml");
        } catch (Exception e) {
            getControleConsultaOtimizado().getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("rankingForm.xhtml");
        }
    }

    public void consultarCurso() {
        try {
            setListaConsultaCurso(getFacadeFactory().getRankingFacade().consultarCurso(getValorConsultaCurso(), getCampoConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarTurma() {
        try {
            super.consultar();
            setListaConsultaTurma(getFacadeFactory().getRankingFacade().consultarTurma(getCampoConsultaTurma(), getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparRankingTurma() {
        getListaRankingTurmaVO().clear();
    }

    public void selecionarTurma() {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        setTurmaVO(obj);
        setCursoVO(obj.getCurso());
    }

    public void selecionarCurso() {
        CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
        setCursoVO(obj);
    }

    public void limparConsultaTurma() {
        getListaConsultaTurma().clear();
    }

    public void limparDadosRankingTurma() {
        setTurmaVO(new TurmaVO());
        setRankingTurmaVO(new RankingTurmaVO());
    }

    public void limparDadosTurma() {
        setTurmaVO(new TurmaVO());
    }

    public void limparConsultaCurso() {
        getListaConsultaCurso().clear();
    }

    public void limparDadosCurso() {
        setCursoVO(new CursoVO());
        setTurmaVO(new TurmaVO());
    }

    public List<SelectItem> getTipoConsultaComboCurso() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nome", "Nome Curso"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void adicionarComissaoFaixaValor() {
        try {
            //getFacadeFactory().getComissionamentoTurmaFacade().adicionarObjComissionamentoFaixaValorVOs(getComissionamentoTurmaVO(), getComissionamentoTurmaFaixaValorVO());
            //setComissionamentoTurmaFaixaValorVO(new ComissionamentoTurmaFaixaValorVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void removerComissionamentoFaixaValor() throws Exception {
        ComissionamentoTurmaFaixaValorVO obj = (ComissionamentoTurmaFaixaValorVO) context().getExternalContext().getRequestMap().get("comissionamentoFaixaValor");
        //getFacadeFactory().getComissionamentoTurmaFacade().excluirObjComissionamentoFaixaValorVOs(comissionamentoTurmaVO, obj);
        setMensagemID("msg_dados_excluidos");
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("rankingForm.xhtml");
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ContaReceberCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

    public void irPaginaInicial() throws Exception {
        controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Turma"));
        itens.add(new SelectItem("curso", "Curso"));
        itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
        itens.add(new SelectItem("dataPrimeiroPgto", "Data Primeiro Pgto"));
        return itens;
    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        return itens;
    }

    public boolean getIsApresentarCampoDataPrimeiroPagamento() {
        return getControleConsulta().getCampoConsulta().equals("dataPrimeiroPgto");
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

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public List getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
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

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
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

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

    public List getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    public String getValorConsultaMes() {
        if (valorConsultaMes == null) {
            valorConsultaMes = "";
        }
        return valorConsultaMes;
    }

    public void setValorConsultaMes(String valorConsultaMes) {
        this.valorConsultaMes = valorConsultaMes;
    }

    public List<RankingTurmaVO> getListaRankingTurmaVO() {
        if (listaRankingTurmaVO == null) {
            listaRankingTurmaVO = new ArrayList(0);
        }
        return listaRankingTurmaVO;
    }

    public void setListaRankingTurmaVO(List<RankingTurmaVO> listaRankingTurmaVO) {
        this.listaRankingTurmaVO = listaRankingTurmaVO;
    }

    public boolean getApresentarListaRankingTurmaVOs() {
        return !getListaRankingTurmaVO().isEmpty();
    }

    public boolean getApresentarListaRankingComissaoVOs() {
        return !getListaRankingVO().isEmpty();
    }

    public List<RankingVO> getListaRankingVO() {
        if (listaRankingVO == null) {
            listaRankingVO = new ArrayList();
        }
        return listaRankingVO;
    }

    public void setListaRankingVO(List<RankingVO> listaRankingVO) {
        this.listaRankingVO = listaRankingVO;
    }

    public RankingTurmaVO getRankingTurmaVO() {
        if (rankingTurmaVO == null) {
            rankingTurmaVO = new RankingTurmaVO();
        }
        return rankingTurmaVO;
    }

    public void setRankingTurmaVO(RankingTurmaVO rankingTurmaVO) {
        this.rankingTurmaVO = rankingTurmaVO;
    }

	public RankingVO getRankingVO() {
		if(rankingVO ==null){
			rankingVO = new RankingVO();
		}
		return rankingVO;
	}

	public void setRankingVO(RankingVO rankingVO) {
		this.rankingVO = rankingVO;
	}

	public Boolean getApresentarAlunoConsiderado() {
		if(apresentarAlunoConsiderado == null){
			apresentarAlunoConsiderado = true;
		}
		return apresentarAlunoConsiderado;
	}

	public void setApresentarAlunoConsiderado(Boolean apresentarAlunoConsiderado) {
		this.apresentarAlunoConsiderado = apresentarAlunoConsiderado;
	}
    
    
}
