package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DisponibilidadeHorarioAlunoVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioAlunoDiaItemVO;
import negocio.comuns.academico.HorarioAlunoDiaVO;
import negocio.comuns.academico.HorarioAlunoTurnoVO;
import negocio.comuns.academico.HorarioAlunoVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrdemHistoricoDisciplina;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.academico.Turma;
import negocio.facade.jdbc.arquitetura.Usuario;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("ProcessoExclusaoDisciplinaMatriculaControle")
@Scope("request")
@Lazy
public class ProcessoExclusaoDisciplinaMatriculaControle extends SuperControle implements Serializable {

    private MatriculaVO matriculaVO;
    private HistoricoVO historico;
    private MatriculaPeriodoVO matriculaPeriodoVO;
    private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTumaDisciplinaVO;
    private HorarioAlunoVO horarioAlunoVO;
    private Integer peridoLetivo;
    private Integer gradeDisciplina;
    private TurmaVO turma;
    private List listaSelectItemPeriodoLetivo;
    private List listaSelectItemTurma;
    private List listaSelectItemDisciplina;
    private List listaDisciplinasSemVagas;
    private List listaDisciplinasComVagasUltrapassadas;
    private String userName;
    private String senha;
    private String matricula;
    private HorarioAlunoTurnoVO horarioAlunoTurnoVO;
    protected List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinaAdicionadas;
    private List<HistoricoVO> listaHistorico;
    protected Boolean imprimir;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List listaConsultaAluno;

    public ProcessoExclusaoDisciplinaMatriculaControle() {
        super();
        setMatricula("");
        novo();
    }

    public String novo() {
        removerObjetoMemoria(this);
        setMatriculaVO(new MatriculaVO());
        setMatriculaPeriodoVO(new MatriculaPeriodoVO());
        setMatriculaPeriodoTumaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
        setHorarioAlunoVO(new HorarioAlunoVO());
        setListaDisciplinasComVagasUltrapassadas(new ArrayList(0));
        setListaDisciplinasSemVagas(new ArrayList(0));
        setListaHistorico(new ArrayList(0));
        setListaSelectItemPeriodoLetivo(new ArrayList(0));
        setListaDisciplinaAdicionadas(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
        setSenha("");
        setUserName("");
        setGradeDisciplina(0);
        setPeridoLetivo(0);
        setImprimir(Boolean.FALSE);
        montarListaSelectItens();
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    public void autorizar() {
        try {
            if (!getListaDisciplinasComVagasUltrapassadas().isEmpty()) {
                verificaPermisaoMatriculaNrMaximo();
            }
            removerDisciplinasSemVagas();
            getFacadeFactory().getMatriculaPeriodoFacade().alterar(getMatriculaPeriodoVO(), getMatriculaVO(), null, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void removerDisciplinasSemVagas() throws Exception {
        Iterator i = getListaDisciplinasSemVagas().iterator();
        while (i.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
            getMatriculaPeriodoVO().excluirObjMatriculaPeriodoVOs(obj.getDisciplina().getCodigo());
            getHorarioAlunoVO().removerDisponibilidadeHorarioAlunoVOs(obj.getDisciplina().getCodigo());
        }
        setListaDisciplinasSemVagas(new ArrayList(0));
    }

    public void verificaPermisaoMatriculaNrMaximo() throws Exception {
        UsuarioVO usuario = Usuario.verificarLoginUsuario(getUserName(), getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS);
        Matricula.verificarPermissaoUsuarioFuncionalidade(usuario, "MatriculaNrMaximoVagas");
        getMatriculaPeriodoVO().setResponsavelMatriculaForaPrazo(usuario);
    }

    public void naoAutorizar() {
        setListaDisciplinasComVagasUltrapassadas(new ArrayList(0));
        setListaDisciplinasSemVagas(new ArrayList(0));
        setMensagemDetalhada("", "");
    }

    public void gravar() {
        try {
            getFacadeFactory().getMatriculaPeriodoFacade().gerarMatriculaInclusaoForaPrazo(getMatriculaVO(), getMatriculaPeriodoVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            setImprimir(Boolean.TRUE);
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getExigirPermissao() {
        if (getListaDisciplinasComVagasUltrapassadas().isEmpty() && getListaDisciplinasSemVagas().isEmpty()) {
            return "";
        }
        return "RichFaces.$('panelDisciplina').show()";
    }

    public Boolean getDisciplinasSemVaga() {
        if (getListaDisciplinasSemVagas().isEmpty()) {
            return false;
        }
        return true;
    }

    public Boolean getDisciplinasComVagaUltrapassadas() {
        if (getListaDisciplinasComVagasUltrapassadas().isEmpty()) {
            return false;
        }
        return true;
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);

            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
//                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, "AT");
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
//                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, "AT");
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
//                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, "AT");
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunoPorMatricula() {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
//            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaSituacaoDadosCompletos(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, "AT");
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado ou a situação da matrícula não possibilita a alteração da mesma.");
            }
            this.setMatriculaVO(objAluno);
            montarListaPeriodoLetivo();
            getMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(0);
            setListaHistorico(new ArrayList(0));
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            this.setMatriculaVO(new MatriculaVO());
        }
    }

    public void limparDadosAluno() throws Exception {
        setMatriculaVO(new MatriculaVO());
        setListaHistorico(new ArrayList(0));
        setMatriculaPeriodoVO(new MatriculaPeriodoVO());
        setListaSelectItemPeriodoLetivo(new ArrayList(0));
    }

    public void fechar() {
        setHistorico(new HistoricoVO());
        setMensagemDetalhada("msg_erro", getMensagemInternalizacao("msg_excluirDisciplinaFechar"));
    }

	public void removerHistorico() throws Exception {
		try {
			getHistorico().setMatricula(getMatriculaVO());
			getFacadeFactory().getHistoricoFacade().excluirHistoricoForaPrazo(getListaHistorico(), getHistorico(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_excluirDisciplinaHistorico");
		} catch (Exception e) {
			setMensagemID("msg_excluirDisciplinaHistoricoErro");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    public void selecionarHistorico() throws Exception {
        HistoricoVO obj = (HistoricoVO) context().getExternalContext().getRequestMap().get("historico");
        obj.setMatriculaPeriodo(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodo().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
        setHistorico(obj);
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
        MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
        setMatriculaVO(objCompleto);
        setListaSelectItemTurma(new ArrayList(0));
        montarListaPeriodoLetivo();
        getMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(0);
        setListaHistorico(new ArrayList(0));
        obj = null;
        objCompleto = null;
        setValorConsultaAluno("");
        setCampoConsultaAluno("");
        getListaConsultaAluno().clear();
    }

    public String consultarMatriculaPorMatricula() {
        try {
            novo();
            setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula(), getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_DADOSBASICOS), getUsuarioLogado()));
            Matricula.montarDadosAluno(getMatriculaVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            Matricula.montarDadosCurso(getMatriculaVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            Matricula.montarDadosTurno(getMatriculaVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            List lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (lista.isEmpty()) {
                throw new Exception("Não foi encontrado nehuma Matricula Periodo para esta Matricula.");
            }
            setMatriculaPeriodoVO((MatriculaPeriodoVO) lista.get(0));
            List<MatriculaPeriodoTurmaDisciplinaVO> listas = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaAtiva(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado());
            List<HorarioTurmaVO> horarioTurmaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaPelaMatriculaAluno(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            atualizarDadosHorarioAluno(horarioTurmaVOs, listas);
            montarListaSelectItens();
            setMatricula("");
            setMensagemID("msg_dados_consultados");
            return "editar";
        } catch (Exception e) {
            setMatricula("");
            setMatriculaVO(new MatriculaVO());
            setMatriculaPeriodoVO(new MatriculaPeriodoVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public void montarListaPeriodoLetivo() {
        try {
            List periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            List<SelectItem> objs = new ArrayList<SelectItem>();
            int index = 0;
            boolean repetido = false;
            Iterator i = periodoLetivoVOs.iterator();
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
                index = 0;
                while (index < objs.size()) {
                    if (objs.get(index).getValue().equals(obj.getPeriodoLetivo())) {
                        repetido = true;
                    }
                    index++;
                }
                if (!repetido) {
                    objs.add(new SelectItem(obj.getPeriodoLetivo(), obj.getPeriodoLetivo() + "°"));
                }
                repetido = false;
            }
            setListaSelectItemPeriodoLetivo(objs);
        } catch (Exception e) {
            setListaSelectItemPeriodoLetivo(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarLista() {
        try {
            getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, NivelMontarDados.TODOS, getUsuarioLogado());
            List<MatriculaPeriodoVO> listaMatriculaPeriodoGradeCurricular = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaMatriculaNrPeriodoLetivo(matriculaVO.getMatricula(), matriculaPeriodoVO.getPeridoLetivo().getPeriodoLetivo(), false, getUsuarioLogado());
            List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatriculaGradeCurricularesEMatriculaPeriodosSomenteDisciplinasDaGradeParaExclusaoForaPrazo(matriculaVO.getMatricula(), listaMatriculaPeriodoGradeCurricular, 2, false, NivelMontarDados.BASICO, getUsuarioLogado());

//            List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatriculaEPeriodoLeticoParaExclusaoForaPrazo(matriculaVO.getMatricula(), matriculaPeriodoVO.getPeridoLetivo().getPeriodoLetivo(), 2, false, NivelMontarDados.BASICO);
//            PeriodoLetivoVO periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricularDisciplina(historicoVO.getDisciplina().getCodigo(), gradeCurricularVO.getCodigo(),
//                Uteis.NIVELMONTARDADOS_DADOSBASICOS);
            setListaHistorico(historicoVOs);
            setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void atualizarDadosHorarioAluno(List<HorarioTurmaVO> horarioTurmaVOs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) throws Exception {
        setHorarioAlunoVO(new HorarioAlunoVO());
        getFacadeFactory().getHorarioAlunoFacade().montarHorarioAluno(getHorarioAlunoVO(), horarioTurmaVOs, matriculaPeriodoTurmaDisciplinaVOs, getUsuarioLogado());
        montarDadosProfessor();
    }

    public void montarDadosProfessor() throws Exception {
        for (HorarioAlunoTurnoVO horarioAlunoTurnoVO : getHorarioAlunoVO().getHorarioAlunoTurnoVOs()) {
            montarDadosProfessorHorarioSemanal(horarioAlunoTurnoVO);
            montarDadosProfessorHorarioDiario(horarioAlunoTurnoVO);
        }
    }

    private void montarDadosProfessorHorarioDiario(HorarioAlunoTurnoVO horarioAlunoTurnoVO) throws Exception {
        for (HorarioAlunoDiaVO horarioAlunoDiaVO : horarioAlunoTurnoVO.getHorarioAlunoDiaVOs()) {
            for (HorarioAlunoDiaItemVO horarioAlunoDiaItemVO : horarioAlunoDiaVO.getHorarioAlunoDiaItemVOs()) {
                horarioAlunoDiaItemVO.setProfessor(consultarProfessor(horarioAlunoDiaItemVO.getProfessor().getCodigo()));
            }
        }
    }

    private void montarDadosProfessorHorarioSemanal(HorarioAlunoTurnoVO horarioAlunoTurnoVO) throws Exception {
        for (DisponibilidadeHorarioAlunoVO disponibilidadeHorarioAlunoVO : horarioAlunoTurnoVO.getDisponibilidadeHorarioAlunoVOs()) {
            disponibilidadeHorarioAlunoVO.setProfessorDomingo(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorDomingo().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorSegunda(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorSegunda().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorTerca(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorTerca().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorQuarta(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorQuarta().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorQuinta(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorQuinta().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorSexta(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorSexta().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorSabado(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorSabado().getCodigo()));
        }
    }

    public PessoaVO consultarProfessor(Integer codigo) throws Exception {
        if (codigo.intValue() > 0) {
            PessoaVO prof = null;
            prof = getHorarioAlunoVO().getProfessor(codigo);
            if (prof != null) {
                return prof;
            }
            return getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(codigo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        }
        return new PessoaVO();
    }


    public void adicionarMatriculaPeriodoTurmaDisciplina() {
        try {
            setMatriculaPeriodoTumaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
            getMatriculaPeriodoTumaDisciplinaVO().setTurma(getTurma());
            if (getMatriculaPeriodoVO().getCodigo().intValue() != 0) {
                getMatriculaPeriodoTumaDisciplinaVO().setMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo());
            }
            if (getMatriculaPeriodoTumaDisciplinaVO().getTurma().getCodigo().intValue() != 0) {
                Integer turma = getMatriculaPeriodoTumaDisciplinaVO().getTurma().getCodigo().intValue();
                TurmaVO obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turma, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                Turma.montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                getMatriculaPeriodoTumaDisciplinaVO().setTurma(obj);
            }
            if (getGradeDisciplina().intValue() != 0) {
                GradeDisciplinaVO obj = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(getGradeDisciplina(), getUsuarioLogado());
                getMatriculaPeriodoTumaDisciplinaVO().setDisciplina(obj.getDisciplina());
            }
            getMatriculaPeriodoTumaDisciplinaVO().setAno(getMatriculaPeriodoVO().getAno());
            getMatriculaPeriodoTumaDisciplinaVO().setSemestre(getMatriculaPeriodoVO().getSemestre());
            getMatriculaPeriodoVO().adicionarObjMatriculaPeriodoVOsInclusaoForaPrazo(getMatriculaPeriodoTumaDisciplinaVO());
            getListaDisciplinaAdicionadas().add(getMatriculaPeriodoTumaDisciplinaVO());
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMatriculaPeriodoTumaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void adicionarDisciplinaNaDisponibilidadeHorarioAluno() throws Exception {

        List<HorarioTurmaVO> horarioTurmaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaPeloCodigoTurmaTrazendoTurmaAgrupada(
                getMatriculaPeriodoTumaDisciplinaVO().getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        List<MatriculaPeriodoTurmaDisciplinaVO> objs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
        objs.add(getMatriculaPeriodoTumaDisciplinaVO());
        if (!getFacadeFactory().getHorarioAlunoFacade().montarHorarioAluno(getHorarioAlunoVO(), horarioTurmaVOs, objs, getUsuarioLogado())) {
            throw new Exception("Não existe no Horário da Turma (" + getMatriculaPeriodoTumaDisciplinaVO().getTurma().getIdentificadorTurma() + ") a Disciplina ("
                    + getMatriculaPeriodoTumaDisciplinaVO().getDisciplina().getNome() + ") cadastrado para montar o Horário do Aluno");
        }
        montarDadosProfessor();
        objs = null;
        horarioTurmaVOs = null;
    }

    public void verificarAlunoJaAprovadoNaDisciplina(MatriculaPeriodoVO mat, GradeDisciplinaVO grade) throws Exception {
        List<HistoricoVO> lista = getFacadeFactory().getHistoricoFacade().consultarPorMatricula(mat.getMatricula(), OrdemHistoricoDisciplina.ANO_SEMESTRE.getValor(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
        for (HistoricoVO obj : lista) {
            if (obj.getDisciplina().getCodigo().intValue() == grade.getDisciplina().getCodigo().intValue()) {
                if (obj.getSituacao().equals("AA") || obj.getSituacao().equals("AP")) {
                    throw new Exception("Aluno já aprovado nessa disciplina.");
                }
            }
        }
    }

    public void verificarDisciplinaNaGradeAluno(MatriculaPeriodoVO mat, GradeDisciplinaVO obj) throws Exception {
        List<MatriculaPeriodoTurmaDisciplinaVO> lista = mat.getMatriculaPeriodoTumaDisciplinaVOs();
        for (MatriculaPeriodoTurmaDisciplinaVO objeto : lista) {
            if (objeto.getDisciplina().getCodigo().intValue() == obj.getDisciplina().getCodigo().intValue()) {
                throw new Exception("Disciplina já incluída, caso queira incluir a disciplina em outro turno, primeiro exclua a disciplina e depois inclua em um novo turno.");
            }
        }
    }

    public void verHorarioTurnoAluno() {
        HorarioAlunoTurnoVO obj = (HorarioAlunoTurnoVO) context().getExternalContext().getRequestMap().get("turno");
        setHorarioAlunoTurnoVO(obj);
    }

    public HorarioTurmaVO consultarHorarioTurma() throws Exception {
        HorarioTurmaVO obj = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurmaUnico(getMatriculaPeriodoTumaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoVO().getSemestre(), getMatriculaPeriodoVO().getAno(), false,
                Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
        return obj;
    }

    public void removerMatriculaPeriodoTurmaDisciplina() throws Exception {
        MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("turmaDisciplina");
        getMatriculaPeriodoVO().excluirObjMatriculaPeriodoVOs(obj.getDisciplina().getCodigo());
        getListaDisciplinaAdicionadas().remove(obj);
        getHorarioAlunoVO().removerDisponibilidadeHorarioAlunoVOs(obj.getDisciplina().getCodigo());
        setMensagemID("msg_dados_excluidos");
    }

    public void removerMatriculaPeriodoTurmaDisciplinaVagas() throws Exception {
        MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("disciplina");
        getMatriculaPeriodoVO().excluirObjMatriculaPeriodoVOs(obj.getDisciplina().getCodigo());
        getHorarioAlunoVO().removerDisponibilidadeHorarioAlunoVOs(obj.getDisciplina().getCodigo());
        removerItemListaDisciplinasComVagasUltapassada(obj.getDisciplina().getCodigo());
        setMensagemID("msg_dados_excluidos");
    }

    public void removerItemListaDisciplinasComVagasUltapassada(Integer disciplina) {
        Iterator i = getListaDisciplinasComVagasUltrapassadas().iterator();
        int index = 0;
        while (i.hasNext()) {
            MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
            if (obj.getDisciplina().getCodigo().equals(disciplina)) {
                getListaDisciplinasComVagasUltrapassadas().remove(index);
                return;
            }
            index++;
        }
    }

//    public void emitirBoleto() {
//        try {
//            imprimirBoleto();
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        }
//    }
//
//    public String getBoleto() {
//        if (getImprimir()) {
//            return "abrirPopup('BoletoBancarioSV?codigoContaReceber=" + matriculaPeriodoVO.getContaReceber() + "&titulo=matricula', 'boletoMatricula', 780, 585)";
//        }
//        return "";
//    }
//
//    public void imprimirBoleto() throws Exception {
//        BoletoBancarioSV ser = new BoletoBancarioSV();
//        ser.setCodigoContaReceber(getMatriculaPeriodoVO().getContaReceber());
//    }
    public void montarListaSelectItens() {
        //montarListaSelectItemPeridoLetivo();
        montarListaSelectItemTurma();
        montarListaSelectItemDisciplina();
    }

    public void montarListaTurmaDisciplina() {
        montarListaSelectItemTurma();
        montarListaSelectItemDisciplina();
    }

    public void montarListaSelectItemPeridoLetivo() {
        try {
            if (getMatriculaPeriodoVO().getCodigo().intValue() == 0) {
                setListaSelectItemPeriodoLetivo(new ArrayList(0));
                return;
            }
            if (getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs().isEmpty()) {
                getMatriculaPeriodoVO().getGradeCurricular().setPeriodoLetivosVOs(consultarPeriodoLetivoPorMatricula());
            }
            List objs = new ArrayList(0);
            Iterator i = getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs().iterator();
            while (i.hasNext()) {
                PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
            }
            setPeridoLetivo(getMatriculaPeriodoVO().getPeridoLetivo().getCodigo());
            setListaSelectItemPeriodoLetivo(objs);

        } catch (Exception e) {
            setListaSelectItemPeriodoLetivo(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaSelectItemTurma() {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getPeridoLetivo().intValue() == 0) {
                setListaSelectItemTurma(new ArrayList(0));
                return;
            }
            resultadoConsulta = consultarTurmaPorPeriodoLetivo();
            List objs = new ArrayList(0);
            i = resultadoConsulta.iterator();
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                TurmaVO obj = (TurmaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma()));
            }
            if (objs.isEmpty()) {
                throw new Exception("Não existe turma cadastrada para o curso: " + getMatriculaVO().getCurso().getNome().toUpperCase() + " no período: "
                        + getMatriculaVO().getTurno().getNome().toUpperCase());
            }
            setListaSelectItemTurma(objs);
        } catch (Exception e) {
            setListaSelectItemTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemDisciplina() {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getPeridoLetivo().intValue() == 0) {
                setListaSelectItemDisciplina(new ArrayList(0));
                return;
            }
            resultadoConsulta = consultarDisciplinaPorPeriodoLetivo();
            List objs = new ArrayList(0);
            i = resultadoConsulta.iterator();
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                GradeDisciplinaVO obj = (GradeDisciplinaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDisciplina().getNome()));
            }
            setListaSelectItemDisciplina(objs);
        } catch (Exception e) {
            setListaSelectItemDisciplina(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarTurmaPorPeriodoLetivo() throws Exception {
        List listaResultado = getFacadeFactory().getTurmaFacade().consultarPorPeriodoLetivoUnidadeEnsinoCurso(getPeridoLetivo(), getMatriculaVO().getUnidadeEnsino().getCodigo(),
                getMatriculaVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return listaResultado;
    }

    public List consultarDisciplinaPorPeriodoLetivo() throws Exception {
        List listaResultado = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getPeridoLetivo(), false, getUsuarioLogado(), null);
        return listaResultado;
    }

    public List consultarPeriodoLetivoPorMatricula() throws Exception {
        List listaRetorno = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return listaRetorno;

    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public List getListaSelectSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
    }

    public List getListaSelectItemDisciplina() {
        return listaSelectItemDisciplina;
    }

    public void setListaSelectItemDisciplina(List listaSelectItemDisciplina) {
        this.listaSelectItemDisciplina = listaSelectItemDisciplina;
    }

    public List getListaSelectItemPeriodoLetivo() {
        return listaSelectItemPeriodoLetivo;
    }

    public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
        this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
    }

    public List getListaSelectItemTurma() {
        return listaSelectItemTurma;
    }

    public void setListaSelectItemTurma(List listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    public MatriculaPeriodoVO getMatriculaPeriodoVO() {
        return matriculaPeriodoVO;
    }

    public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
        this.matriculaPeriodoVO = matriculaPeriodoVO;
    }

    public MatriculaVO getMatriculaVO() {
        return matriculaVO;
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTumaDisciplinaVO() {
        return matriculaPeriodoTumaDisciplinaVO;
    }

    public void setMatriculaPeriodoTumaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTumaDisciplinaVO) {
        this.matriculaPeriodoTumaDisciplinaVO = matriculaPeriodoTumaDisciplinaVO;
    }

    public Integer getGradeDisciplina() {
        return gradeDisciplina;
    }

    public void setGradeDisciplina(Integer gradeDisciplina) {
        this.gradeDisciplina = gradeDisciplina;
    }

    public Integer getPeridoLetivo() {
        return peridoLetivo;
    }

    public void setPeridoLetivo(Integer peridoLetivo) {
        this.peridoLetivo = peridoLetivo;
    }

    public List getListaDisciplinasComVagasUltrapassadas() {
        return listaDisciplinasComVagasUltrapassadas;
    }

    public void setListaDisciplinasComVagasUltrapassadas(List listaDisciplinasComVagasUltrapassadas) {
        this.listaDisciplinasComVagasUltrapassadas = listaDisciplinasComVagasUltrapassadas;
    }

    public List getListaDisciplinasSemVagas() {
        return listaDisciplinasSemVagas;
    }

    public void setListaDisciplinasSemVagas(List listaDisciplinasSemVagas) {
        this.listaDisciplinasSemVagas = listaDisciplinasSemVagas;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public HorarioAlunoVO getHorarioAlunoVO() {
        return horarioAlunoVO;
    }

    public void setHorarioAlunoVO(HorarioAlunoVO horarioAlunoVO) {
        this.horarioAlunoVO = horarioAlunoVO;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void pegarMatricula() {
        //System.out.print("teste");
    }

    public HorarioAlunoTurnoVO getHorarioAlunoTurnoVO() {
        return horarioAlunoTurnoVO;
    }

    public void setHorarioAlunoTurnoVO(HorarioAlunoTurnoVO horarioAlunoTurnoVO) {
        this.horarioAlunoTurnoVO = horarioAlunoTurnoVO;
    }

    public List<MatriculaPeriodoTurmaDisciplinaVO> getListaDisciplinaAdicionadas() {
        if (listaDisciplinaAdicionadas == null) {
            listaDisciplinaAdicionadas = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
        }
        return listaDisciplinaAdicionadas;
    }

    public void setListaDisciplinaAdicionadas(List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinaAdicionadas) {
        this.listaDisciplinaAdicionadas = listaDisciplinaAdicionadas;
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public Boolean getImprimir() {
        return imprimir;
    }

    public void setImprimir(Boolean imprimir) {
        this.imprimir = imprimir;
    }

    /**
     * @return the valorConsultaAluno
     */
    public String getValorConsultaAluno() {
        return valorConsultaAluno;
    }

    /**
     * @param valorConsultaAluno the valorConsultaAluno to set
     */
    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    /**
     * @return the campoConsultaAluno
     */
    public String getCampoConsultaAluno() {
        return campoConsultaAluno;
    }

    /**
     * @param campoConsultaAluno the campoConsultaAluno to set
     */
    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    /**
     * @return the listaConsultaAluno
     */
    public List getListaConsultaAluno() {
        return listaConsultaAluno;
    }

    /**
     * @param listaConsultaAluno the listaConsultaAluno to set
     */
    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    /**
     * @return the listaHistorico
     */
    public List<HistoricoVO> getListaHistorico() {
        return listaHistorico;
    }

    /**
     * @param listaHistorico the listaHistorico to set
     */
    public void setListaHistorico(List<HistoricoVO> listaHistorico) {
        this.listaHistorico = listaHistorico;
    }

    /**
     * @return the historico
     */
    public HistoricoVO getHistorico() {
        return historico;
    }

    /**
     * @param historico the historico to set
     */
    public void setHistorico(HistoricoVO historico) {
        this.historico = historico;
    }
}
