package relatorio.controle.biblioteca;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.biblioteca.AcervoVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ClassificacaoBibliograficaVO;
import negocio.comuns.biblioteca.EditoraVO;
import negocio.comuns.biblioteca.SecaoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.biblioteca.AcervoRelVO;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas AcervoForm.jsp
 * AcervoCons.jsp) com as funcionalidades da classe <code>Acervo</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Acervo
 * @see AcervoVO
 */
@Controller("AcervoRelControle")
@Scope("request")
@Lazy
public class AcervoRelControle extends SuperControleRelatorio {

    private AcervoRelVO acervoRelVO;
    private List listaSelectItemUnidadeEnsino;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private List listaSelectItemBiblioteca;
    private BibliotecaVO bibliotecaVO;
    private String campoConsultaSecao;
    private String valorConsultaSecao;
    private List listaConsultaSecao;
    private SecaoVO secaoVO;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private CursoVO cursoVO;
    private TurnoVO turnoVO;
    private List listaSelectItemMatriz;
    private GradeCurricularVO matrizVO;
    private String campoConsultaDisciplina;
    private String valorConsultaDisciplina;
    private List listaConsultaDisciplina;
    private DisciplinaVO disciplinaVO;
    private String campoConsultaEditora;
    private String valorConsultaEditora;
    private List listaConsultaEditora;
    private EditoraVO editoraVO;
    private List listaSelectItemClassificacao;
    private ClassificacaoBibliograficaVO classificacaoBibliograficaVO;
    private String campoConsultaCatalogo;
    private String valorConsultaCatalogo;
    private List listaConsultaCatalogo;
    private CatalogoVO catalogoVO;
    private String tipoRelatorio;

    public AcervoRelControle() throws Exception {
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_prmconsulta");
    }

    public void realizarImpressaoPDF() {
        try {

            if (getUnidadeEnsinoVO().getCodigo() == null || getUnidadeEnsinoVO().getCodigo() == 0) {
                throw new Exception("Para Emissão do Relatório é necessário informar a Unidade de Ensino.");
            }
            if (getBibliotecaVO().getCodigo() == null || getBibliotecaVO().getCodigo() == 0) {
                throw new Exception("Para Emissão do Relatório é necessário informar a Biblioteca.");
            }
            List objetos = getFacadeFactory().getAcervoRelFacade().realizarBuscaDeObjetosParaMontarRelatorioAcervo(getUnidadeEnsinoVO(), getBibliotecaVO(),
                    getSecaoVO(), getCursoVO(), getTurnoVO(), getMatrizVO(), getDisciplinaVO(), getEditoraVO(), getClassificacaoBibliograficaVO(),
                    getCatalogoVO(), getTipoRelatorio());

            if (!objetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAcervoRelFacade().designIReportRelatorio(getTipoRelatorio()));
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAcervoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Acervo");
                getSuperParametroRelVO().setListaObjetos(objetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAcervoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

                getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());

                getSuperParametroRelVO().setBiblioteca(getBibliotecaVO().getNome());


                realizarImpressaoRelatorio();
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void inicializarListasSelectItemTodosComboBox() throws Exception {
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemBiblioteca();
        montarListaSelectItemMatriz();
        montarListaSelectItemClassificacao();
    }

    /* Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>unidadeEnsino</code>
     */
    public void montarListaSelectItemUnidadeEnsino() throws Exception {
        setListaSelectItemBiblioteca(new ArrayList(0));
        limparDadosCurso();
        limparDadosDisciplina();
        setListaSelectItemUnidadeEnsino(getFacadeFactory().getAcervoRelFacade().consultarUnidadeEnsino("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
    }

    /* Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>biblioteca</code>
     */
    public void montarListaSelectItemBiblioteca() throws Exception {
        limparDadosCurso();
        if (getUnidadeEnsinoVO() != null && !getUnidadeEnsinoVO().getCodigo().equals(0)) {
            setListaSelectItemBiblioteca(getFacadeFactory().getAcervoRelFacade().consultarBiblioteca(getUnidadeEnsinoVO().getCodigo(), Obrigatorio.NAO, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
        } else {
            setListaSelectItemBiblioteca(new ArrayList(0));
        }
    }
    /* Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>matriz</code>
     */

    public void montarListaSelectItemMatriz() throws Exception {
        if (getCursoVO() != null && !getCursoVO().getCodigo().equals(0)) {
            setListaSelectItemMatriz(getFacadeFactory().getAcervoRelFacade().consultarMatrizCurricular(getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
        } else {
            setListaSelectItemMatriz(new ArrayList(0));
        }
    }

    public void montarListaSelectItemClassificacao() throws Exception {
        setListaSelectItemClassificacao(getFacadeFactory().getAcervoRelFacade().consultarClassificacao("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
    }

    public String consultarSecao() {
        try {
            setListaConsultaSecao(getFacadeFactory().getAcervoRelFacade().consultarSecao(getCampoConsultaSecao(), getValorConsultaSecao(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
            return "consultar";
        } catch (Exception e) {
            getListaConsultaSecao().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public void limparDadosSecao() {
        setValorConsultaSecao("");
        setSecaoVO(new SecaoVO());
        setMensagemID("msg_entre_dados", Uteis.ALERTA);

    }

    public void selecionarSecao() {
        try {
            SecaoVO secao = ((SecaoVO) context().getExternalContext().getRequestMap().get("secao"));
            setSecaoVO(secao);
            getListaConsultaSecao().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboTipoRelatorio() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("porClassificacao", "Por Classificação"));
        itens.add(new SelectItem("porLocal", "Por Local"));
        return itens;
    }

    public List getTipoConsultaComboSecao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt, getUnidadeEnsinoVO().getCodigo(),"", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false,  false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    public void limparDadosCurso() {
        setValorConsultaCurso("");
        setCursoVO(new CursoVO());
        setTurnoVO(new TurnoVO());
        setListaSelectItemMatriz(new ArrayList(0));
        setMensagemID("msg_entre_dados", Uteis.ALERTA);

    }

    public void selecionarCurso() {
        try {
            UnidadeEnsinoCursoVO unidadeEnsinoCurso = ((UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocurso"));
            setCursoVO(unidadeEnsinoCurso.getCurso());
            setTurnoVO(unidadeEnsinoCurso.getTurno());
            montarListaSelectItemMatriz();
            getListaConsultaCurso().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public String consultarDisciplina() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaDisciplina().equals("codigo")) {
                if (getValorConsultaDisciplina().equals("")) {
                    setValorConsultaDisciplina("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaDisciplina());
                if (getMatrizVO() != null && !getMatrizVO().getCodigo().equals(0)) {
                    objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo_GradeCurricular(new Integer(valorInt), getMatrizVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                } else {
                    objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo_UnidadeEnsino(new Integer(valorInt), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                }
            }
            if (getCampoConsultaDisciplina().equals("nome")) {
                if (getMatrizVO() != null && !getMatrizVO().getCodigo().equals(0)) {
                    objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome_GradeCurricular(getValorConsultaDisciplina(), getMatrizVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                } else {
                    objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome_UnidadeEnsino(getValorConsultaDisciplina(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                }
            }
            setListaConsultaDisciplina(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsultaDisciplina(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public void limparDadosDisciplina() {
        setValorConsultaDisciplina("");
        setDisciplinaVO(new DisciplinaVO());
        setMensagemID("msg_entre_dados", Uteis.ALERTA);

    }

    public void selecionarDisciplina() {
        try {
            DisciplinaVO disciplina = ((DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplina"));
            setDisciplinaVO(disciplina);
            getListaConsultaDisciplina().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboDisciplina() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public String consultarEditora() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaEditora().equals("codigo")) {
                if (getValorConsultaEditora().equals("")) {
                    setValorConsultaEditora("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaEditora());
                objs = getFacadeFactory().getEditoraFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaEditora().equals("nome")) {
                objs = getFacadeFactory().getEditoraFacade().consultarPorNome(getValorConsultaEditora(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaEditora(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsultaEditora(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public void limparDadosEditora() {
        setValorConsultaEditora("");
        setEditoraVO(new EditoraVO());
        setMensagemID("msg_entre_dados", Uteis.ALERTA);

    }

    public void selecionarEditora() {
        try {
            EditoraVO editora = ((EditoraVO) context().getExternalContext().getRequestMap().get("editora"));
            setEditoraVO(editora);
            getListaConsultaEditora().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboEditora() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public String consultarCatalogo() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaCatalogo().equals("codigo")) {
                if (getValorConsultaCatalogo().equals("")) {
                    setValorConsultaCatalogo("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCatalogo());
                objs = getFacadeFactory().getCatalogoFacade().consultarPorCodigo(new Integer(valorInt), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getBibliotecaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
            }
            if (getCampoConsultaCatalogo().equals("tituloTitulo")) {
                objs = getFacadeFactory().getCatalogoFacade().consultarPorTitulo(getValorConsultaCatalogo(), true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getBibliotecaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), 0, getUsuarioLogado());
            }
            if (getCampoConsultaCatalogo().equals("nomeEditora")) {
                objs = getFacadeFactory().getCatalogoFacade().consultarPorNomeEditora(getValorConsultaCatalogo(), true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getBibliotecaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
            }
            setListaConsultaCatalogo(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsultaCatalogo(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public void limparDadosCatalogo() {
        setValorConsultaCatalogo("");
        setCatalogoVO(new CatalogoVO());
        setMensagemID("msg_entre_dados", Uteis.ALERTA);

    }

    public void selecionarCatalogo() {
        try {
            CatalogoVO catalogo = ((CatalogoVO) context().getExternalContext().getRequestMap().get("catalogo"));
            setCatalogoVO(catalogo);
            getListaConsultaCatalogo().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboCatalogo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("tituloTitulo", "Título"));
        itens.add(new SelectItem("nomeEditora", "Editora"));
        return itens;
    }

    @Override
    public void limparMensagem() {
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
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

    public List getListaSelectItemBiblioteca() {
        if (listaSelectItemBiblioteca == null) {
            listaSelectItemBiblioteca = new ArrayList(0);
        }
        return listaSelectItemBiblioteca;
    }

    public void setListaSelectItemBiblioteca(List listaSelectItemBiblioteca) {
        this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
    }

    public String getCampoConsultaSecao() {
        if (campoConsultaSecao == null) {
            campoConsultaSecao = "";
        }
        return campoConsultaSecao;
    }

    public void setCampoConsultaSecao(String campoConsultaSecao) {
        this.campoConsultaSecao = campoConsultaSecao;
    }

    public String getValorConsultaSecao() {
        if (valorConsultaSecao == null) {
            valorConsultaSecao = "";
        }
        return valorConsultaSecao;
    }

    public void setValorConsultaSecao(String valorConsultaSecao) {
        this.valorConsultaSecao = valorConsultaSecao;
    }

    public List getListaConsultaSecao() {
        if (listaConsultaSecao == null) {
            listaConsultaSecao = new ArrayList(0);
        }
        return listaConsultaSecao;
    }

    public void setListaConsultaSecao(List listaConsultaSecao) {
        this.listaConsultaSecao = listaConsultaSecao;
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

    public String getTipoRelatorio() {
        if (tipoRelatorio == null) {
            tipoRelatorio = "porClassificacao";
        }
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public AcervoRelVO getAcervoRelVO() {
        if (acervoRelVO == null) {
            acervoRelVO = new AcervoRelVO();
        }
        return acervoRelVO;
    }

    public void setAcervoRelVO(AcervoRelVO acervoRelVO) {
        this.acervoRelVO = acervoRelVO;
    }

    public BibliotecaVO getBibliotecaVO() {
        if (bibliotecaVO == null) {
            bibliotecaVO = new BibliotecaVO();
        }
        return bibliotecaVO;
    }

    public void setBibliotecaVO(BibliotecaVO bibliotecaVO) {
        this.bibliotecaVO = bibliotecaVO;
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

    public SecaoVO getSecaoVO() {
        if (secaoVO == null) {
            secaoVO = new SecaoVO();
        }
        return secaoVO;
    }

    public void setSecaoVO(SecaoVO secaoVO) {
        this.secaoVO = secaoVO;
    }

    public TurnoVO getTurnoVO() {
        if (turnoVO == null) {
            turnoVO = new TurnoVO();
        }
        return turnoVO;
    }

    public void setTurnoVO(TurnoVO turnoVO) {
        this.turnoVO = turnoVO;
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

    public String getCampoConsultaCatalogo() {
        if (campoConsultaCatalogo == null) {
            campoConsultaCatalogo = "";
        }
        return campoConsultaCatalogo;
    }

    public void setCampoConsultaCatalogo(String campoConsultaCatalogo) {
        this.campoConsultaCatalogo = campoConsultaCatalogo;
    }

    public String getCampoConsultaDisciplina() {
        if (campoConsultaDisciplina == null) {
            campoConsultaDisciplina = "";
        }
        return campoConsultaDisciplina;
    }

    public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
        this.campoConsultaDisciplina = campoConsultaDisciplina;
    }

    public String getCampoConsultaEditora() {
        if (campoConsultaEditora == null) {
            campoConsultaEditora = "";
        }
        return campoConsultaEditora;
    }

    public void setCampoConsultaEditora(String campoConsultaEditora) {
        this.campoConsultaEditora = campoConsultaEditora;
    }

    public CatalogoVO getCatalogoVO() {
        if (catalogoVO == null) {
            catalogoVO = new CatalogoVO();
        }
        return catalogoVO;
    }

    public void setCatalogoVO(CatalogoVO catalogoVO) {
        this.catalogoVO = catalogoVO;
    }

    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    public EditoraVO getEditoraVO() {
        if (editoraVO == null) {
            editoraVO = new EditoraVO();
        }
        return editoraVO;
    }

    public void setEditoraVO(EditoraVO editoraVO) {
        this.editoraVO = editoraVO;
    }

    public List getListaConsultaCatalogo() {
        if (listaConsultaCatalogo == null) {
            listaConsultaCatalogo = new ArrayList(0);
        }
        return listaConsultaCatalogo;
    }

    public void setListaConsultaCatalogo(List listaConsultaCatalogo) {
        this.listaConsultaCatalogo = listaConsultaCatalogo;
    }

    public List getListaConsultaDisciplina() {
        if (listaConsultaDisciplina == null) {
            listaConsultaDisciplina = new ArrayList(0);
        }
        return listaConsultaDisciplina;
    }

    public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
    }

    public List getListaConsultaEditora() {
        if (listaConsultaEditora == null) {
            listaConsultaEditora = new ArrayList(0);
        }
        return listaConsultaEditora;
    }

    public void setListaConsultaEditora(List listaConsultaEditora) {
        this.listaConsultaEditora = listaConsultaEditora;
    }

    public List getListaSelectItemClassificacao() {
        if (listaSelectItemClassificacao == null) {
            listaSelectItemClassificacao = new ArrayList(0);
        }
        return listaSelectItemClassificacao;
    }

    public void setListaSelectItemClassificacao(List listaSelectItemClassificacao) {
        this.listaSelectItemClassificacao = listaSelectItemClassificacao;
    }

    public List getListaSelectItemMatriz() {
        if (listaSelectItemMatriz == null) {
            listaSelectItemMatriz = new ArrayList(0);
        }
        return listaSelectItemMatriz;
    }

    public void setListaSelectItemMatriz(List listaSelectItemMatriz) {
        this.listaSelectItemMatriz = listaSelectItemMatriz;
    }

    public GradeCurricularVO getMatrizVO() {
        if (matrizVO == null) {
            matrizVO = new GradeCurricularVO();
        }
        return matrizVO;
    }

    public void setMatrizVO(GradeCurricularVO matrizVO) {
        this.matrizVO = matrizVO;
    }

    public String getValorConsultaCatalogo() {
        if (valorConsultaCatalogo == null) {
            valorConsultaCatalogo = "";
        }
        return valorConsultaCatalogo;
    }

    public void setValorConsultaCatalogo(String valorConsultaCatalogo) {
        this.valorConsultaCatalogo = valorConsultaCatalogo;
    }

    public String getValorConsultaDisciplina() {
        if (valorConsultaDisciplina == null) {
            valorConsultaDisciplina = "";
        }
        return valorConsultaDisciplina;
    }

    public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
        this.valorConsultaDisciplina = valorConsultaDisciplina;
    }

    public String getValorConsultaEditora() {
        if (valorConsultaEditora == null) {
            valorConsultaEditora = "";
        }
        return valorConsultaEditora;
    }

    public void setValorConsultaEditora(String valorConsultaEditora) {
        this.valorConsultaEditora = valorConsultaEditora;
    }

    public ClassificacaoBibliograficaVO getClassificacaoBibliograficaVO() {
        if (classificacaoBibliograficaVO == null) {
            classificacaoBibliograficaVO = new ClassificacaoBibliograficaVO();
        }
        return classificacaoBibliograficaVO;
    }

    public void setClassificacaoBibliograficaVO(ClassificacaoBibliograficaVO classificacaoBibliograficaVO) {
        this.classificacaoBibliograficaVO = classificacaoBibliograficaVO;
    }
}
