package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.NotaNaoLancadaProfessorRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.NotaNaoLancadaProfessorRel;

@SuppressWarnings("unchecked")
@Controller("NotaNaoLancadaProfessorRelControle")
@Scope("viewScope")
@Lazy
public class NotaNaoLancadaProfessorRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;

	private String campoFiltroPor;
	
    private TurmaVO turma;
    private List<TurmaVO> listaConsultaTurma;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    
    private String periodicidade;
    private String ano;
    private String semestre;
    private Date periodoMatriculaInicial;
    private Date periodoMatriculaFinal;
    
    private String unidadeEnsinoApresentar;
	private String cursoApresentar;
	private String turnoApresentar;
    
    private DisciplinaVO disciplina;
    private List<DisciplinaVO> listaConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	
	private FuncionarioVO professor;
    private List<FuncionarioVO> listaConsultaProfessor;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	    
    private String filtrarNotas;
    private String layout;
    private String ordenacao;
    
    private Date periodoAulaInicial;
    private Date periodoAulaFinal;

    public NotaNaoLancadaProfessorRelControle() throws Exception {
		consultarUnidadeEnsinoFiltroRelatorio("");
		verificarTodasUnidadesSelecionadas();
		getFiltroRelatorioAcademicoVO().setAtivo(true);
		getFiltroRelatorioAcademicoVO().setPreMatricula(true);
		getFiltroRelatorioAcademicoVO().setConcluido(true);
		getFiltroRelatorioAcademicoVO().setFormado(true);
		getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "NotaNaoLancadaProfessorRel", getUsuarioLogado());
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List<NotaNaoLancadaProfessorRelVO> listaObjetos = new ArrayList<NotaNaoLancadaProfessorRelVO>(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "NotaNaoLancadaProfessorRelControle", "Inicializando Geração de Relatório Nota Não Lançada Professor", "Emitindo Relatório");
            getFacadeFactory().getNotaNaoLancadaProfessorRelFacade().validarDados(getUnidadeEnsinoVOs(), getConfiguracaoAcademicaVO(), getCursoVOs(), getTurma(),
            		getPeriodicidade(), getAno(), getSemestre(), getPeriodoAulaInicial(), getPeriodoAulaFinal(), getCampoFiltroPor());

            listaObjetos = getFacadeFactory().getNotaNaoLancadaProfessorRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getPeriodicidade(), getAno(), getSemestre(),
            		getPeriodoMatriculaInicial(), getPeriodoMatriculaFinal(), getCampoFiltroPor(), getTurma(), getCursoVOs(), getTurnoVOs(), getDisciplina(),
            		getProfessor(), getConfiguracaoAcademicaVO(), getFiltrarNotas(), getFiltroRelatorioAcademicoVO(), getLayout(), getOrdenacao(), getPeriodoAulaInicial(), getPeriodoAulaFinal());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(NotaNaoLancadaProfessorRel.getDesignIReportRelatorio(getLayout()));
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(NotaNaoLancadaProfessorRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Nota Não Lançada Professor");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(NotaNaoLancadaProfessorRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa(super.getUnidadeEnsinoLogado().getNome());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().adicionarParametro("ano", getAno());
                getSuperParametroRelVO().adicionarParametro("semestre", getSemestre());
                String periodoMatricula= "";
                if (Uteis.isAtributoPreenchido(getPeriodoMatriculaInicial())) {
                	periodoMatricula += Uteis.getData(getPeriodoMatriculaInicial());
                } else {
                	periodoMatricula += "-";
                }
                if (Uteis.isAtributoPreenchido(getPeriodoMatriculaFinal())) {
                	periodoMatricula += " a ";
                	periodoMatricula += Uteis.getData(getPeriodoMatriculaFinal());
                } else {
                	periodoMatricula += "-";
                }
                	
                getSuperParametroRelVO().adicionarParametro("periodoMatricula", periodoMatricula);
                getSuperParametroRelVO().adicionarParametro("periodoAulaInicial", Uteis.getData(getPeriodoAulaInicial()));
                getSuperParametroRelVO().adicionarParametro("periodoAulaFinal", Uteis.getData(getPeriodoAulaFinal()));
                StringBuilder unidades = new StringBuilder();
            	String aux = "";
            	boolean possuiAlgumaUnidadeDesmarcada = false;
            	for (UnidadeEnsinoVO ue : getUnidadeEnsinoVOs()) {
            		if (ue.getFiltrarUnidadeEnsino()) {
            			unidades.append(aux).append(ue.getNome());
            			aux = ",";
            		} else {
            			possuiAlgumaUnidadeDesmarcada = true;
            		}
            	}
            	if (!possuiAlgumaUnidadeDesmarcada) {
            		getSuperParametroRelVO().adicionarParametro("unidadesEnsino", "Todas");
            	} else {
	            	if (!aux.isEmpty()) {
	            		if (unidades.toString().length() > 110) {
	            			getSuperParametroRelVO().adicionarParametro("unidadesEnsino", unidades.toString().substring(0, 108) + "...");
	            		} else {
	            			getSuperParametroRelVO().adicionarParametro("unidadesEnsino", unidades.toString());
	            		}
	            	}
            	}
                if (getCampoFiltroPor().equals("curso")) {
                	getSuperParametroRelVO().adicionarParametro("campoFiltroPor", "Curso");
                	StringBuilder cursos = new StringBuilder();
                	String aux1 = "";
                	boolean possuiAgumCursoDesmarcado = false;
                	for (CursoVO c : getCursoVOs()) {
                		if (c.getFiltrarCursoVO()) {
                			cursos.append(aux1).append(c.getNome());
                			aux1 = ",";
                		} else {
                			possuiAgumCursoDesmarcado = true;
                		}
                	}
                	if (!possuiAgumCursoDesmarcado) {
                		getSuperParametroRelVO().adicionarParametro("cursos", "Todos");
                	} else {
	                	if (!aux1.isEmpty()) {
	                		if (cursos.toString().length() > 124) {
	                			getSuperParametroRelVO().adicionarParametro("cursos", cursos.toString().substring(0, 122) + "...");
	                		} else {
	                			getSuperParametroRelVO().adicionarParametro("cursos", cursos.toString());
	                		}
	                	}
                	}
                	StringBuilder turnos = new StringBuilder();
                	String aux2 = "";
                	boolean possuiAlgumTurnoDesmarcado = false;
                	for (TurnoVO t : getTurnoVOs()) {
                		if (t.getFiltrarTurnoVO()) {
                			turnos.append(aux2).append(t.getNome());
                			aux2 = ",";
                		} else {
                			possuiAlgumTurnoDesmarcado = true;
                		}
                	}
                	if (!possuiAlgumTurnoDesmarcado || aux2.isEmpty()) {
                		getSuperParametroRelVO().adicionarParametro("turnos", "Todos");
                	} else {
	                	if (!aux2.isEmpty()) {
	                		getSuperParametroRelVO().adicionarParametro("turnos", turnos.toString());
	                	}
                	}
                } else if (getCampoFiltroPor().equals("turma")) {
                	getSuperParametroRelVO().adicionarParametro("campoFiltroPor", "Turma");
                	if (getTurma().getIdentificadorTurma().isEmpty()) {
                		getSuperParametroRelVO().adicionarParametro("turma", "Todas");
                	} else {
                		getSuperParametroRelVO().adicionarParametro("turma", getTurma().getIdentificadorTurma());
                	}
                }
                if (getPeriodicidade().equals("AN")) {
                	getSuperParametroRelVO().adicionarParametro("periodicidade", "Anual");
                } else if (getPeriodicidade().equals("SE")) {
                	getSuperParametroRelVO().adicionarParametro("periodicidade", "Semestral");
                } else if (getPeriodicidade().equals("IN")) {
                	getSuperParametroRelVO().adicionarParametro("periodicidade", "Integral");
                }
                if (getDisciplina().getNome().isEmpty()) {
                	getSuperParametroRelVO().adicionarParametro("disciplina", "Todas");
                } else {
                	getSuperParametroRelVO().adicionarParametro("disciplina", getDisciplina().getNome());
                }
                if (getProfessor().getPessoa().getNome().isEmpty()) {
                	getSuperParametroRelVO().adicionarParametro("professor", "Todos");
                } else {
                	getSuperParametroRelVO().adicionarParametro("professor", getProfessor().getPessoa().getNome());
                }
                getSuperParametroRelVO().adicionarParametro("configuracaoAcademica", getConfiguracaoAcademicaVO().getNome());
                
        		StringBuilder filtroNota = new StringBuilder();
        		String auxNota = "";
        		if (getConfiguracaoAcademicaVO().getUtilizarNota1() && getConfiguracaoAcademicaVO().isFiltrarNota1()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota1());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota2() && getConfiguracaoAcademicaVO().isFiltrarNota2()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota2());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota3() && getConfiguracaoAcademicaVO().isFiltrarNota3()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota3());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota4() && getConfiguracaoAcademicaVO().isFiltrarNota4()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota4());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota5() && getConfiguracaoAcademicaVO().isFiltrarNota5()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota5());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota6() && getConfiguracaoAcademicaVO().isFiltrarNota6()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota6());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota7() && getConfiguracaoAcademicaVO().isFiltrarNota7()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota7());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota8() && getConfiguracaoAcademicaVO().isFiltrarNota8()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota8());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota9() && getConfiguracaoAcademicaVO().isFiltrarNota9()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota9());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota10() && getConfiguracaoAcademicaVO().isFiltrarNota10()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota10());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota11() && getConfiguracaoAcademicaVO().isFiltrarNota11()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota11());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota12() && getConfiguracaoAcademicaVO().isFiltrarNota12()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota12());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota13() && getConfiguracaoAcademicaVO().isFiltrarNota13()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota13());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota14() && getConfiguracaoAcademicaVO().isFiltrarNota14()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota14());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota15() && getConfiguracaoAcademicaVO().isFiltrarNota15()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota15());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota16() && getConfiguracaoAcademicaVO().isFiltrarNota16()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota16());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota17() && getConfiguracaoAcademicaVO().isFiltrarNota17()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota17());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota18() && getConfiguracaoAcademicaVO().isFiltrarNota18()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota18());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota19() && getConfiguracaoAcademicaVO().isFiltrarNota19()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota19());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota20() && getConfiguracaoAcademicaVO().isFiltrarNota20()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota20());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota21() && getConfiguracaoAcademicaVO().isFiltrarNota21()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota21());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota22() && getConfiguracaoAcademicaVO().isFiltrarNota22()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota22());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota23() && getConfiguracaoAcademicaVO().isFiltrarNota23()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota23());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota24() && getConfiguracaoAcademicaVO().isFiltrarNota24()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota24());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota25() && getConfiguracaoAcademicaVO().isFiltrarNota25()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota25());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota26() && getConfiguracaoAcademicaVO().isFiltrarNota26()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota26());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota27() && getConfiguracaoAcademicaVO().isFiltrarNota27()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota27());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota28() && getConfiguracaoAcademicaVO().isFiltrarNota28()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota28());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota29() && getConfiguracaoAcademicaVO().isFiltrarNota29()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota29());
        			auxNota = ", ";
        		}
        		if (getConfiguracaoAcademicaVO().getUtilizarNota30() && getConfiguracaoAcademicaVO().isFiltrarNota30()) {
        			filtroNota.append(auxNota).append(getConfiguracaoAcademicaVO().getTituloNota30());
        			auxNota = ", ";
        		}
        		if (!auxNota.isEmpty()) {
        			if (filtroNota.toString().length() > 64) {
        				getSuperParametroRelVO().adicionarParametro("notasSelecionadas", filtroNota.toString().substring(0, 62) + "...");
        			} else {
        				getSuperParametroRelVO().adicionarParametro("notasSelecionadas", filtroNota.toString());
        			}
        		}
        		if (getFiltrarNotas().equals("and")) {
        			getSuperParametroRelVO().adicionarParametro("filtrarNotas", "Combinadas");
        		} else if (getFiltrarNotas().equals("or")) {
        			getSuperParametroRelVO().adicionarParametro("filtrarNotas", "Individuais");
        		}
        		StringBuilder situacaoAcademica = new StringBuilder();
        		String auxSit = "";
        		if (getFiltroRelatorioAcademicoVO().getAtivo()) {
        			situacaoAcademica.append(auxSit).append("Ativo");
        			auxSit = ",";
        		}
        		if (getFiltroRelatorioAcademicoVO().getPreMatricula()) {
        			situacaoAcademica.append(auxSit).append("Pré Matrícula");
        			auxSit = ",";
        		}
        		if (getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada()) {
        			situacaoAcademica.append(auxSit).append("Pré Matrícula Cancelada");
        			auxSit = ",";
        		}
        		if (getFiltroRelatorioAcademicoVO().getTrancado()) {
        			situacaoAcademica.append(auxSit).append("Trancado");
        			auxSit = ",";
        		}
        		if (getFiltroRelatorioAcademicoVO().getCancelado()) {
        			situacaoAcademica.append(auxSit).append("Cancelado");
        			auxSit = ",";
        		}
        		if (getFiltroRelatorioAcademicoVO().getConcluido()) {
        			situacaoAcademica.append(auxSit).append("Concluido");
        			auxSit = ",";
        		}
        		if (getFiltroRelatorioAcademicoVO().getTransferenciaInterna()) {
        			situacaoAcademica.append(auxSit).append("Transferência Interna");
        			auxSit = ",";
        		}
        		if (getFiltroRelatorioAcademicoVO().getTransferenciaExterna()) {
        			situacaoAcademica.append(auxSit).append("Transferência Saida");
        			auxSit = ",";
        		}
        		if (getFiltroRelatorioAcademicoVO().getFormado()) {
        			situacaoAcademica.append(auxSit).append("Formado");
        			auxSit = ",";
        		}
        		if (getFiltroRelatorioAcademicoVO().getAbandonado()) {
        			situacaoAcademica.append(auxSit).append("Abandono de Curso");
        			auxSit = ",";
        		}
                getSuperParametroRelVO().adicionarParametro("situacaoAcademica", situacaoAcademica.toString());
        		StringBuilder situacaoFinanceira = new StringBuilder();
        		if (getFiltroRelatorioAcademicoVO().getPendenteFinanceiro()) {
        			situacaoFinanceira.append("Pendente Financeiro");
        		}
        		if (getFiltroRelatorioAcademicoVO().getConfirmado()) {
        			if (!situacaoFinanceira.toString().isEmpty()) {
        				situacaoFinanceira.append(", ");
        			}
        			situacaoFinanceira.append("Confirmado");
        		}
                getSuperParametroRelVO().adicionarParametro("situacaoFinanceira", situacaoFinanceira.toString());
                if (getLayout().equals("curso")) {
	                if (getOrdenacao().equals("professor")) {
	                	getSuperParametroRelVO().adicionarParametro("ordenarPor", "Professor");
	                } else if (getOrdenacao().equals("turma")) {
	                	getSuperParametroRelVO().adicionarParametro("ordenarPor", "Turma");
	                } else if (getOrdenacao().equals("disciplina")) {
	                	getSuperParametroRelVO().adicionarParametro("ordenarPor", "Disciplina");
	                }
                } else if (getLayout().equals("professor")) {
                	if (getOrdenacao().equals("curso")) {
	                	getSuperParametroRelVO().adicionarParametro("ordenarPor", "Curso");
	                } else if (getOrdenacao().equals("turma")) {
	                	getSuperParametroRelVO().adicionarParametro("ordenarPor", "Turma");
	                } else if (getOrdenacao().equals("disciplina")) {
	                	getSuperParametroRelVO().adicionarParametro("ordenarPor", "Disciplina");
	                }
                }
                realizarImpressaoRelatorio();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "NotaNaoLancadaProfessorRel", getUsuarioLogado());
            registrarAtividadeUsuario(getUsuarioLogado(), "NotaNaoLancadaProfessorRelControle", "Finalizando Geração de Relatório Nota Não Lançada Professor", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }
    
	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = "SE";
		}
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	public String getFiltrarNotas() {
		if (filtrarNotas == null) {
			filtrarNotas = "individuais";
		}
		return filtrarNotas;
	}

	public void setFiltrarNotas(String filtrarNotas) {
		this.filtrarNotas = filtrarNotas;
	}
	
	public List<SelectItem> getTipoPeriodicidadeCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AN", "Anual"));
		itens.add(new SelectItem("SE", "Semestral"));
		itens.add(new SelectItem("IN", "Integral"));
		return itens;
	}
	
	public List<SelectItem> getTipoLayoutCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("curso", "Layout 1 - Nota Não Lançada Por Curso"));
		itens.add(new SelectItem("professor", "Layout 2 - Nota Não Lançada Por Professor"));
		return itens;
	}
    
	public List<SelectItem> getTipoOrdenacaoCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		if (getLayout().equals("curso")) {
			itens.add(new SelectItem("professor", "Professor"));
			itens.add(new SelectItem("turma", "Turma"));
			itens.add(new SelectItem("disciplina", "Disciplina"));
		} else if (getLayout().equals("professor")) {
			itens.add(new SelectItem("curso", "Curso"));
			itens.add(new SelectItem("turma", "Turma"));
			itens.add(new SelectItem("disciplina", "Disciplina"));
		}
		return itens;
	}
	
	public List<SelectItem> getTipoNotasCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("and", "Combinadas"));
		itens.add(new SelectItem("or", "Individuais"));
		return itens;
	}
	
	public String getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = "";
		}
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

    public void limparTurma() {
        setTurma(new TurmaVO());
        getListaConsultaTurma().clear();
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
    
	public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }
	
	public List<SelectItem> getTipoConsultaComboConfiguracaoAcademica() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
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

    public void selecionarTurma() {
        try {
        	TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
	        setTurma(obj);
	        setValorConsultaTurma("");
	        setCampoConsultaTurma("");
	        getListaConsultaTurma().clear();
	        setMensagemDetalhada("");
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public List<SelectItem> getTipoConsultaComboSemestre() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("1", "1º"));
        itens.add(new SelectItem("2", "2º"));
        return itens;
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

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
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

    public Boolean getIsFiltrarPorTurma() {
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

    public Boolean getIsExibirFiltroAno() {
        return getPeriodicidade().equals("AN");
    }

    public Boolean getIsExibirFiltroAnoSemestre() {
        return getPeriodicidade().equals("SE");
    }
    
    public Boolean getIsExibirFiltroPeriodoMatricula() {
        return getPeriodicidade().equals("IN");
    }

	public Date getPeriodoMatriculaInicial() {
		return periodoMatriculaInicial;
	}

	public void setPeriodoMatriculaInicial(Date periodoMatriculaInicial) {
		this.periodoMatriculaInicial = periodoMatriculaInicial;
	}
	
	public Date getPeriodoMatriculaFinal() {
		return periodoMatriculaFinal;
	}

	public void setPeriodoMatriculaFinal(Date periodoMatriculaFinal) {
		this.periodoMatriculaFinal = periodoMatriculaFinal;
	}

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(
			List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
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

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}
	
	public List<SelectItem> getTipoConsultaComboDisciplina() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
	
    public void limparDisciplina() {
        setDisciplina(new DisciplinaVO());
        getListaConsultaDisciplina().clear();
    }
    
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			setDisciplina(obj);
		} catch (Exception e) {
		}
	}
	
	public List<SelectItem> getTipoConsultaComboProfessor() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}
	
    public void limparProfessor() {
        setProfessor(new FuncionarioVO());
        getListaConsultaProfessor().clear();
    }
    
    public void consultarProfessor() {
        try {
            List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
            if (getCampoConsultaProfessor().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaProfessor(), "PR", null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProfessor().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaProfessor(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaProfessor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaProfessor(new ArrayList<FuncionarioVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public void selecionarProfessor() throws Exception {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");
			setProfessor(obj);
		} catch (Exception e) {
		}
	}
	
	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}
	
	public void marcarTodosTurnosAction() throws Exception {
		for (TurnoVO turnoVO : getTurnoVOs()) {
			turnoVO.setFiltrarTurnoVO(getMarcarTodosTurnos());
		}
		verificarTodosTurnosSelecionados();
	}
	
	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome().trim()).append("; ");
				}
			}
			setUnidadeEnsinoApresentar(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					setUnidadeEnsinoApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				}
			}
		}
		consultarCursoFiltroRelatorio(getPeriodicidade());
		consultarTurnoFiltroRelatorio();
	}
	
	public void verificarTodosCursosSelecionados() {
		StringBuilder curso = new StringBuilder();
		if (getCursoVOs().size() > 1) {
			for (CursoVO obj : getCursoVOs()) {
				if (obj.getFiltrarCursoVO()) {
					curso.append(obj.getCodigo()).append(" - ");
					curso.append(obj.getNome()).append("; ");
				}
			}
			setCursoApresentar(curso.toString());
		} else {
			if (!getCursoVOs().isEmpty()) {
				if (getCursoVOs().get(0).getFiltrarCursoVO()) {
					setCursoApresentar(getCursoVOs().get(0).getNome());
				}
			}
		}
	}
	
	public void verificarTodosTurnosSelecionados() {
		StringBuilder turno = new StringBuilder();
		if (getTurnoVOs().size() > 1) {
			for (TurnoVO obj : getTurnoVOs()) {
				if (obj.getFiltrarTurnoVO()) {
					turno.append(obj.getNome()).append("; ");
				}
			}
			setTurnoApresentar(turno.toString());
		} else {
			if (!getTurnoVOs().isEmpty()) {
				if (getTurnoVOs().get(0).getFiltrarTurnoVO()) {
					setTurnoApresentar(getTurnoVOs().get(0).getNome());
				}
			} else {
				setTurnoApresentar(turno.toString());
			}
		}
	}
	
	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}
	
	public String getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = "";
		}
		return cursoApresentar;
	}

	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	public String getTurnoApresentar() {
		if (turnoApresentar == null) {
			turnoApresentar = "";
		}
		return turnoApresentar;
	}

	public void setTurnoApresentar(String turnoApresentar) {
		this.turnoApresentar = turnoApresentar;
	}
	
	public void limparUnidadeEnsino() {
		try {
			setUnidadeEnsinoApresentar(null);
			setMarcarTodasUnidadeEnsino(false);
			marcarTodasUnidadesEnsinoAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCurso() {
		try {
			setCursoApresentar(null);
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurno() {
		try {
			setTurnoApresentar(null);
			setMarcarTodosTurnos(false);
			marcarTodosTurnosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void atualizarCurso() {
		try {
			consultarCursoFiltroRelatorio(getPeriodicidade());
		} catch (Exception e) {
		}
	}
	
	public String getLayout() {
		if (layout == null) {
			layout = "curso";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public FuncionarioVO getProfessor() {
		if (professor == null) {
			professor = new FuncionarioVO();
		}
		return professor;
	}

	public void setProfessor(FuncionarioVO professor) {
		this.professor = professor;
	}

	public List<FuncionarioVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public Date getPeriodoAulaInicial() {
		return periodoAulaInicial;
	}

	public void setPeriodoAulaInicial(Date periodoAulaInicial) {
		this.periodoAulaInicial = periodoAulaInicial;
	}

	public Date getPeriodoAulaFinal() {
		return periodoAulaFinal;
	}

	public void setPeriodoAulaFinal(Date periodoAulaFinal) {
		this.periodoAulaFinal = periodoAulaFinal;
	}
	
	

}