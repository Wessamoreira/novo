/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioProfessorTurnoVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.GraduacaoPosGraduacaoEnum;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.HorarioProfessorDia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * 
 * @author Carlos
 */
@Controller("AgendaProfessorRelControle")
@Scope("viewScope")
@Lazy
public class AgendaProfessorRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 583892548734241081L;

	private HorarioProfessorDiaVO horarioProfessorDiaVO;
    private PessoaVO pessoaVO;
    private FuncionarioVO funcionarioVO;
    private TurnoVO turnoVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private Date DataInicio;
    private Date DataFim;
    private String campoConsultaProfessor;
    private String valorConsultaProfessor;
    private List<FuncionarioVO> listaConsultaProfessor;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private String campoConsultaDisciplina;
    private String valorConsultaDisciplina;
    private List listaConsultaDisciplina;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private TurmaVO turmaVO;
    private DisciplinaVO disciplinaVO;
    private List<SelectItem> tipoConsultaComboProfessor;
    private List<SelectItem> tipoConsultaComboDisciplina;
    private List<SelectItem> tipoConsultaComboCurso;
    private List<SelectItem> tipoConsultaComboTurma;
    private String layout;
    private List<HorarioProfessorTurnoVO> horarioProfessorTurnoVOs;
    private Boolean calendarioMensal;
    private Date dataBaseHorarioAula;
    private Boolean ocultarHorarioLivre;
	private String ordenacao;
	private List<SelectItem> listaSelectItemTipoTextoPadrao;
	private List<TextoPadraoDeclaracaoVO> listaTextoDeclaracaoVOs;
	private Integer textoPadrao;
	private boolean permitirEmissaoDeclaracao;
    protected List<HorarioProfessorDiaItemVO> horarioProfessorGraduacao;
    protected List<HorarioProfessorDiaItemVO> horarioProfessorPosGraduacao;
	
	
	private FuncionarioCargoVO funcionarioCargoVO;
	private List<SelectItem> listaSelectItemFuncionarioCargo;

    public AgendaProfessorRelControle() {
    	try {
			verificarLayoutPadrao();
			inicializarTextoPadrao();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void imprimirPDF() {
    	if (getLayout().equals("LAYOUT_DIA_A_DIA") || getLayout().equals("LAYOUT_COM_ASSINATURA")) {
    		realizarImpressaoPDF();
    	}else if(getLayout().equals("LAYOUT_EMISSAO_DECLARACAO")){
    		realizarImpressaoPDFEmissaoDeclaracao();
    	} else {
    		realizarImpressaoPDFSemanal();
    	}
    }
    
    private void realizarImpressaoPDFEmissaoDeclaracao() {
    	Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> mapaHorario = null;
    	List<CursoVO> cursoVO = new ArrayList<CursoVO>();
		try {
			if (!Uteis.isAtributoPreenchido(getTextoPadrao())) {
				throw new Exception("O campo Texto Padrão deve ser informado.");
			}
			validarDados();
			setOrdenacao("curso");
			mapaHorario = getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorariosProfessorSeparadoPorNivelEducacional(getFuncionarioVO().getPessoa().getCodigo(),
	                getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo(), getDataInicio(), getDataFim(), false, "", getUsuarioLogado(), getOrdenacao(), verificarPermissaoUsuarioIncluirFuncionarioCargo(), getFuncionarioCargoVO());
			
			TextoPadraoDeclaracaoVO textoLayout = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(getTextoPadrao(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			
		       if (mapaHorario.containsKey(GraduacaoPosGraduacaoEnum.GRADUACAO)) {
		              getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(mapaHorario.get(GraduacaoPosGraduacaoEnum.GRADUACAO));
		        }
		        if (mapaHorario.containsKey(GraduacaoPosGraduacaoEnum.POS_GRADUACAO)) {
		              getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(mapaHorario.get(GraduacaoPosGraduacaoEnum.POS_GRADUACAO));
		        }
		        
		        if (!getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().isEmpty()) {
		        	
		        	cursoVO = getFacadeFactory().getHorarioProfessorDiaFacade().montarCursoVinculadoProfessor(getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs());
		        	
					if(textoLayout.getTexto().contains("Nome_Professor")) {
						getSuperParametroRelVO().adicionarParametro("Nome_Professor", getFuncionarioVO().getPessoa().getNome());
					}
					if(textoLayout.getTexto().contains("CPF_Professor")) {
						getSuperParametroRelVO().adicionarParametro("CPF_Professor", getFuncionarioVO().getPessoa().getCPF());
					}
					if(textoLayout.getTexto().contains("NivelEducacional_Curso")) {
						getSuperParametroRelVO().adicionarParametro("NivelEducacional_Curso", getUnidadeEnsinoCursoVO().getCurso().getNivelEducacional());
					}
					if(textoLayout.getTexto().contains("Lista_AulasMinistradasProfessor")) {
						getSuperParametroRelVO().adicionarParametro("Lista_AulasMinistradasProfessor", getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs());
					}
					if(textoLayout.getTexto().contains("Lista_AulasMinistradasProfessorComCargaHoraria")) {
						getSuperParametroRelVO().adicionarParametro("Lista_AulasMinistradasProfessorComCargaHoraria", getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs());
						
					}if(textoLayout.getTexto().contains("DataAtualExtenso_Outras")) {
						List<UnidadeEnsinoVO> unidadeEnsinos = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorProfessor(getFuncionarioVO().getPessoa().getCodigo(), false,  Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
						getFuncionarioVO().setUnidadeEnsino(unidadeEnsinos.iterator().next());
						textoLayout.substituirTag(getFuncionarioVO(), getUsuario(), getSuperParametroRelVO());
					}
					
					String caminhoArquivo = (!textoLayout.getArquivoIreport().getPastaBaseArquivo().startsWith(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()) ? getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator  : "") + textoLayout.getArquivoIreport().getPastaBaseArquivo() + File.separator + textoLayout.getArquivoIreport().getNome();
					
					getSuperParametroRelVO().setNomeDesignIreport(caminhoArquivo);
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
					getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator);
					getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator);
					String caminhoArquivoSubRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().criarCaminhoPastaAteDiretorioSubRelatorio(textoLayout, getConfiguracaoGeralSistemaVO());
					getSuperParametroRelVO().setSubRelatorio_Dir(caminhoArquivoSubRelatorio);
					getSuperParametroRelVO().setListaObjetos(cursoVO);
					
					realizarImpressaoRelatorio();
					if(textoLayout.getAssinarDigitalmenteTextoPadrao()) {
						setCaminhoRelatorio(getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaAgendaProfessor(getCaminhoRelatorio(),textoLayout.getAlinhamentoAssinaturaDigitalEnum(),textoLayout.getCorAssinaturaDigitalmente(), textoLayout.getAlturaAssinatura(), textoLayout.getLarguraAssinatura(), getConfiguracaoGeralPadraoSistema(), getFuncionarioVO(), getUsuarioLogado()));
					}
					getSuperParametroRelVO().limparParametros();
					persistirLayoutPadrao(getLayout());
					setMensagemID("msg_relatorio_ok");
			}else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		
		} catch (NumberFormatException e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
            Uteis.liberarListaMemoria(getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs());
            if (mapaHorario != null) {
                mapaHorario.clear();
            }
        }
		
	}

	public void realizarImpressaoPDFSemanal() {
    	List<CronogramaDeAulasRelVO> listaObjetos = new ArrayList<CronogramaDeAulasRelVO>(0);
		try {
			listaObjetos = getFacadeFactory().getHorarioProfessorDiaFacade().criarObjetoRelatorioSemanal(getHorarioProfessorTurnoVOs(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(HorarioProfessorDia.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(HorarioProfessorDia.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Horário do Professor");
				
				if(getOrdenacao().equals("unidadeEnsino")) {
					Ordenacao.ordenarLista(listaObjetos, "unidadeEnsino");	
				}
				else if(getOrdenacao().equals("data")) {
					Ordenacao.ordenarLista(listaObjetos, "dataInicio");	
				}
				else if(getOrdenacao().equals("disciplina")) {
					Ordenacao.ordenarLista(listaObjetos, "disciplina");	
				}
				/*if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) && getVisaoAlunoControle().getMatricula().getCurso().getNivelEducacional().equals("PO")) {
					Ordenacao.ordenarLista(listaObjetos, "dataInicio");	
				} else {
					Ordenacao.ordenarLista(listaObjetos, "modulo");
				}*/
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(HorarioProfessorDia.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().adicionarParametro("ocultarData", false);
				getSuperParametroRelVO().setProfessor(getFuncionarioVO().getPessoa().getNome());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().setCurso(getUnidadeEnsinoCursoVO().getCurso().getNome());
				getSuperParametroRelVO().setMatricula(getFuncionarioVO().getMatricula());

				getSuperParametroRelVO().setDataInicio(Uteis.getDataAno4Digitos(getDataInicio()));
				getSuperParametroRelVO().setDataFim(Uteis.getDataAno4Digitos(getDataFim()));
				realizarImpressaoRelatorio();
				persistirLayoutPadrao(getLayout());
				//removerObjetoMemoria(this);
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
    }
    
    public void consultarHorarioProfessorRel() {
		try {
			if(getFuncionarioVO().getPessoa().getCodigo().equals(0)){
				throw new ConsistirException("O campo PROFESSOR deve ser informado.");
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
		consultarHorarioAulaProfessor();
	}
    
    public void consultarHorarioAulaProfessor() {
		try {
			if (getCalendarioMensal()) {
//				setDataInicio(Uteis.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(Uteis.getDataPrimeiroDiaMes(getDataBaseHorarioAula()))), 1));
//				setDataTermino(Uteis.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getUltimoDiaSemana(Uteis.getDataUltimoDiaMes(getDataBaseHorarioAula()))), 1));
				setDataInicio(Uteis.obterDataPassada(new Date(), 500));
//				setDataTermino(Uteis.obterDataFutura(new Date(), 500));
			} else {
				setDataInicio(UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(getDataBaseHorarioAula())), 1));
				setDataFim(UteisData.obterDataFutura(UteisData.getUltimoDiaSemana(getDataBaseHorarioAula()), 1));
			}
			setHorarioProfessorTurnoVOs(getFacadeFactory().getHorarioProfessorDiaFacade().consultarMeusHorariosProfessor(getFuncionarioVO().getPessoa(), getDataBaseHorarioAula(), getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(),getDisciplinaVO(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
    
    public void visualizarProximaSemana() {
		if (getCalendarioMensal()) {
			setDataBaseHorarioAula(Uteis.getDataPrimeiroDiaMes(Uteis.obterDataAvancadaPorMes(getDataBaseHorarioAula(), 1)));
		} else {
			setDataBaseHorarioAula(UteisData.getPrimeiroDiaProximaSemana(getDataBaseHorarioAula()));
		}
		
		consultarHorarioAulaProfessor();
	}

	public void visualizarSemanaAnterior() {
		if (getCalendarioMensal()) {
			setDataBaseHorarioAula(Uteis.getDataPrimeiroDiaMes(Uteis.obterDataAvancadaPorMes(getDataBaseHorarioAula(), -1)));
		} else {
			setDataBaseHorarioAula(UteisData.getPrimeiroDiaSemanaPassada(getDataBaseHorarioAula()));
		}

		consultarHorarioAulaProfessor();
	}

    public void realizarImpressaoPDF() {
        Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> mapaHorario = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AgendaProfessorRelControle", "Inicializando Geração de Relatório Agenda Professor", "Emitindo Relatório");
            validarDados();
            mapaHorario = getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorariosProfessorSeparadoPorNivelEducacional(getFuncionarioVO().getPessoa().getCodigo(),
                    getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo(), getDataInicio(), getDataFim(), false, "", getUsuarioLogado(), getOrdenacao(), verificarPermissaoUsuarioIncluirFuncionarioCargo(), getFuncionarioCargoVO());
            if (mapaHorario.containsKey(GraduacaoPosGraduacaoEnum.GRADUACAO)) {
                getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(mapaHorario.get(GraduacaoPosGraduacaoEnum.GRADUACAO));
            }
            if (mapaHorario.containsKey(GraduacaoPosGraduacaoEnum.POS_GRADUACAO)) {
                getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(mapaHorario.get(GraduacaoPosGraduacaoEnum.POS_GRADUACAO));
            }

            if (!getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().isEmpty()) {
            	getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getHorarioProfessorDiaItemFacade().designIReportRelatorio(getLayout()));          	
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getHorarioProfessorDiaItemFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                if(getLayout().equals("LAYOUT_COM_ASSINATURA")) {
                	getSuperParametroRelVO().setTituloRelatorio("Folha de Ponto");
                }else {
                	getSuperParametroRelVO().setTituloRelatorio("Agenda do Professor");
                }
                
                getSuperParametroRelVO().setListaObjetos(getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getHorarioProfessorDiaItemFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setProfessor(getFuncionarioVO().getPessoa().getNome());
                getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
                	setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
                	getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
                }
                
                getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());
                getSuperParametroRelVO().adicionarParametro("dataFim", getDataFim());
                
                realizarImpressaoRelatorio();
                persistirLayoutPadrao(getLayout());
//                removerObjetoMemoria(this);
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "AgendaProfessorRelControle", "Finalizando Geração de Relatório Agenda Professor", "Emitindo Relatório");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        } finally {
            Uteis.liberarListaMemoria(getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs());
            if (mapaHorario != null) {
                mapaHorario.clear();
            }
//            return "editar";
        }
    }

    public void realizarImpressaoExcel() {
        Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> mapaHorario = null;
        try {
            validarDados();
            mapaHorario = getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorariosProfessorSeparadoPorNivelEducacional(getFuncionarioVO().getPessoa().getCodigo(),
                    getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo(), getDataInicio(), getDataFim(), false, "", getUsuarioLogado(), getOrdenacao(), verificarPermissaoUsuarioIncluirFuncionarioCargo(), getFuncionarioCargoVO());
            if (mapaHorario.containsKey(GraduacaoPosGraduacaoEnum.GRADUACAO)) {
                getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(mapaHorario.get(GraduacaoPosGraduacaoEnum.GRADUACAO));
            }
            if (mapaHorario.containsKey(GraduacaoPosGraduacaoEnum.POS_GRADUACAO)) {
                getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(mapaHorario.get(GraduacaoPosGraduacaoEnum.POS_GRADUACAO));
            }

            if (!getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getHorarioProfessorDiaItemFacade().designIReportRelatorioExcel(getLayout()));
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getHorarioProfessorDiaItemFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Agenda do Professor");
                getSuperParametroRelVO().setListaObjetos(getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getHorarioProfessorDiaItemFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setProfessor(getFuncionarioVO().getPessoa().getNome());
                getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
                realizarImpressaoRelatorio();
//                removerObjetoMemoria(this);
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        } finally {
            Uteis.liberarListaMemoria(getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs());
            if (mapaHorario != null) {
                mapaHorario.clear();
            }
//            return "editar";
        }
    }

    /**
     * Verifica se o usuário logado possui perfil de funcionalidade para filtrar por {@link FuncionarioCargoVO}.
     * do professor selecionado.
     * @return - 
     * @throws Exception
     */
    public Boolean verificarPermissaoUsuarioIncluirFuncionarioCargo() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteInformarFuncionarioCargoAgenda", getUsuarioLogado());
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;			
		}
	}

    /**
	 * Monta Lista de {@link SelectItem} do {@link FuncionarioCargoVO} pelo {@link FuncionarioVO} informado.
	 * 
	 * @param funcionario
	 * @throws Exception
	 */
	private void montarListaSelectItemFuncionarioCargo(FuncionarioVO funcionario) throws Exception {
		if (verificarPermissaoUsuarioIncluirFuncionarioCargo()) {
			listaSelectItemFuncionarioCargo.clear();
			@SuppressWarnings("unchecked")
			List<FuncionarioCargoVO> lista = getFacadeFactory().getFuncionarioCargoFacade().consultarPorFuncionario(funcionario.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			listaSelectItemFuncionarioCargo.add(new SelectItem("", ""));
			for (FuncionarioCargoVO funcionarioCargoVO : lista) {
				if (funcionarioCargoVO.getSituacaoFuncionario().equals(SituacaoFuncionarioEnum.ATIVO.name())) {
					listaSelectItemFuncionarioCargo.add(new SelectItem(funcionarioCargoVO.getCodigo(), funcionarioCargoVO.getMatriculaCargo() + " - " +funcionarioCargoVO.getFuncionarioVO().getPessoa().getNome()));
				}
			}
		}
	}

    
    public List<SelectItem> getTipoConsultaComboProfessor() {
        if (tipoConsultaComboProfessor == null) {
            tipoConsultaComboProfessor = new ArrayList<SelectItem>();
            tipoConsultaComboProfessor.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboProfessor.add(new SelectItem("matricula", "Matrícula"));
        }
        return tipoConsultaComboProfessor;
    }

    public void validarDados() throws Exception {
        if (getFuncionarioVO().getPessoa().getNome().equals("")) {
            throw new Exception("O campo PROFESSOR deve ser informado!");
        }
    }

    public void limparListasConsultas() {
        setTurmaVO(null);
        setDisciplinaVO(null);
        setUnidadeEnsinoCursoVO(null);
        getListaConsultaCurso().clear();
        getListaConsultaTurma().clear();
        getListaConsultaDisciplina().clear();
        if (getLayout().equals("LAYOUT_SEMANAL") && !getFuncionarioVO().getPessoa().getCodigo().equals(0)) {
        	consultarHorarioAulaProfessor();
        }
    }

    public void consultarProfessor() {
        try {
            List<FuncionarioVO> objs = new ArrayList<>(0);
            if (getCampoConsultaProfessor().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaProfessor(), "PR", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getUsuarioLogado());
            }
            if (getCampoConsultaProfessor().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaProfessor(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getUsuarioLogado());
            }
            setListaConsultaProfessor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaProfessor(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarProfessor() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");
        getFacadeFactory().getFuncionarioFacade().carregarDados(obj, getUsuarioLogado());
        setFuncionarioVO(obj);
        montarListaSelectItemUnidadeEnsino();

        montarListaSelectItemFuncionarioCargo(obj);
        obj = null;
        setValorConsultaProfessor("");
        setCampoConsultaProfessor("");
        getListaConsultaProfessor().clear();
        if (getLayout().equals("LAYOUT_SEMANAL") && !getFuncionarioVO().getPessoa().getCodigo().equals(0)) {
        	consultarHorarioAulaProfessor();
        }else if(getLayout().equals("LAYOUT_EMISSAO_DECLARACAO") ) {
			inicializarTextoPadrao();		
		}
    }

    @SuppressWarnings("unchecked")
	public void montarListaSelectItemUnidadeEnsino() {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorProfessor(getFuncionarioVO().getPessoa().getCodigo());
            Iterator<UnidadeEnsinoVO>i = resultadoConsulta.iterator();
            getListaSelectItemUnidadeEnsino().clear();
            getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
                removerObjetoMemoria(obj);
            }
        } catch (Exception e) {
           setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorProfessor(Integer codigoProfessor) throws Exception {
        return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorProfessor(codigoProfessor, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
    }

    public void limparConsulta() {
        setListaConsultaProfessor(null);
        setMensagemID("msg_entre_prmconsulta");
    }

    public void consultarTurma() {
        try {
            super.consultar();
            List objs = new ArrayList<>(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarTurmaPorChavePrimaria() {
        try {
            setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoVO().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            if (getTurmaVO().getCodigo() == 0) {
                setTurmaVO(null);
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setTurmaVO(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            setTurmaVO(obj);
            limparDisciplina();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDisciplina() {
        try {
            setDisciplinaVO(null);
            if (getLayout().equals("LAYOUT_SEMANAL") && !getFuncionarioVO().getPessoa().getCodigo().equals(0)) {
            	consultarHorarioAulaProfessor();
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparTurma() {
        try {
            setTurmaVO(null);
            limparDisciplina();
            if (getLayout().equals("LAYOUT_SEMANAL") && !getFuncionarioVO().getPessoa().getCodigo().equals(0)) {
            	consultarHorarioAulaProfessor();
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboTurma() {
        if (tipoConsultaComboTurma == null) {
            tipoConsultaComboTurma = new ArrayList<>(0);

            tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
//		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
//		itens.add(new SelectItem("nomeTurno", "Turno"));
//		itens.add(new SelectItem("nomeCurso", "Curso"));
        }

        return tipoConsultaComboTurma;
    }

    public void consultarCurso() {
        try {
            List objs = new ArrayList<>(0);
            if (getCampoConsultaCurso().equals("codigo")) {
                if (getValorConsultaCurso().equals("")) {
                    setValorConsultaCurso("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCurso());
//				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(valorInt, getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,
//						getUsuarioLogado());
            }
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorProfessor(getValorConsultaCurso(), getFuncionarioVO().getPessoa().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCurso() throws Exception {
        try {
            UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
            setUnidadeEnsinoCursoVO(obj);
            limparTurma();
        } catch (Exception e) {
        }
    }

    public void limparCurso() throws Exception {
        try {
            setUnidadeEnsinoCursoVO(null);
            limparTurma();
            if (getLayout().equals("LAYOUT_SEMANAL") && !getFuncionarioVO().getPessoa().getCodigo().equals(0)) {
            	consultarHorarioAulaProfessor();
            }
        } catch (Exception e) {
        }
    }

    public void limparProfessor() throws Exception {
    	setFuncionarioVO(null);

        setPessoaVO(null);
        limparCurso();
        setHorarioProfessorTurnoVOs(null);
        getHorarioProfessorPosGraduacao().clear();
        setLayout("LAYOUT_DIA_A_DIA");
        getListaSelectItemFuncionarioCargo().clear();
    }

    public List<SelectItem> getTipoConsultaComboCurso() {
        if (tipoConsultaComboCurso == null) {
            tipoConsultaComboCurso = new ArrayList<>(0);
            tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboCurso;
    }

    public void consultarDisciplina() {
        try {
            List objs = new ArrayList<>(0);
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
            setListaConsultaDisciplina(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarDisciplina() throws Exception {
        DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
        setDisciplinaVO(obj);
    }
    
    public void inicializarTextoPadrao() throws Exception {

    	setListaTextoDeclaracaoVOs(getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("AM", getUnidadeEnsinoVO().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
    	
   		setListaSelectItemTipoTextoPadrao(UtilSelectItem.getListaSelectItem(getListaTextoDeclaracaoVOs(), "codigo", "descricao"));
    }

    public List<SelectItem> getTipoConsultaComboDisciplina() {
        if (tipoConsultaComboDisciplina == null) {
            tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
            tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboDisciplina;
    }
    
    private boolean verificarPermissaoProfessor() {
    	try {
    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITIR_EMITIR_DECLARACAO_REL, getUsuarioLogado());
    		setPermitirEmissaoDeclaracao(true);
		} catch (Exception e) {
			setPermitirEmissaoDeclaracao(false);
		}
    	return isPermitirEmissaoDeclaracao();
    }

    public Boolean getApresentarFiltros() {
        if (getFuncionarioVO().getPessoa().getCodigo() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the horarioProfessorDiaVO
     */
    public HorarioProfessorDiaVO getHorarioProfessorDiaVO() {
        if (horarioProfessorDiaVO == null) {
            horarioProfessorDiaVO = new HorarioProfessorDiaVO();
        }
        return horarioProfessorDiaVO;
    }

    /**
     * @param horarioProfessorDiaVO
     *            the horarioProfessorDiaVO to set
     */
    public void setHorarioProfessorDiaVO(HorarioProfessorDiaVO horarioProfessorDiaVO) {
        this.horarioProfessorDiaVO = horarioProfessorDiaVO;
    }

    /**
     * @return the DataInicio
     */
    public Date getDataInicio() {
        if (DataInicio == null) {
            DataInicio = new Date();
        }
        return DataInicio;
    }

    /**
     * @param DataInicio
     *            the DataInicio to set
     */
    public void setDataInicio(Date DataInicio) {
        this.DataInicio = DataInicio;
    }

    /**
     * @return the DataFim
     */
    public Date getDataFim() {
        return DataFim;
    }

    /**
     * @param DataFim
     *            the DataFim to set
     */
    public void setDataFim(Date DataFim) {
        this.DataFim = DataFim;
    }

    /**
     * @return the turnoVO
     */
    public TurnoVO getTurnoVO() {
        if (turnoVO == null) {
            turnoVO = new TurnoVO();
        }
        return turnoVO;
    }

    /**
     * @param turnoVO
     *            the turnoVO to set
     */
    public void setTurnoVO(TurnoVO turnoVO) {
        this.turnoVO = turnoVO;
    }

    /**
     * @return the campoConsultaProfessor
     */
    public String getCampoConsultaProfessor() {
        if (campoConsultaProfessor == null) {
            campoConsultaProfessor = "";
        }
        return campoConsultaProfessor;
    }

    /**
     * @param campoConsultaProfessor
     *            the campoConsultaProfessor to set
     */
    public void setCampoConsultaProfessor(String campoConsultaProfessor) {
        this.campoConsultaProfessor = campoConsultaProfessor;
    }

    /**
     * @return the valorConsultaProfessor
     */
    public String getValorConsultaProfessor() {
        if (valorConsultaProfessor == null) {
            valorConsultaProfessor = "";
        }
        return valorConsultaProfessor;
    }

    /**
     * @param valorConsultaProfessor
     *            the valorConsultaProfessor to set
     */
    public void setValorConsultaProfessor(String valorConsultaProfessor) {
        this.valorConsultaProfessor = valorConsultaProfessor;
    }

    /**
     * @return the listaConsultaProfessor
     */
    public List<FuncionarioVO> getListaConsultaProfessor() {
        if (listaConsultaProfessor == null) {
            listaConsultaProfessor = new ArrayList<>(0);
        }
        return listaConsultaProfessor;
    }

    /**
     * @param listaConsultaProfessor
     *            the listaConsultaProfessor to set
     */
    public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
        this.listaConsultaProfessor = listaConsultaProfessor;
    }

    /**
     * @return the unidadeEnsinoVO
     */
    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    /**
     * @param unidadeEnsinoVO
     *            the unidadeEnsinoVO to set
     */
    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    /**
     * @return the pessoaVO
     */
    public PessoaVO getPessoaVO() {
        if (pessoaVO == null) {
            pessoaVO = new PessoaVO();
        }
        return pessoaVO;
    }

    /**
     * @param pessoaVO
     *            the pessoaVO to set
     */
    public void setPessoaVO(PessoaVO pessoaVO) {
        this.pessoaVO = pessoaVO;
    }

    /**
     * @return the funcionarioVO
     */
    public FuncionarioVO getFuncionarioVO() {
        if (funcionarioVO == null) {
            funcionarioVO = new FuncionarioVO();
        }
        return funcionarioVO;
    }

    /**
     * @param funcionarioVO
     *            the funcionarioVO to set
     */
    public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
        this.funcionarioVO = funcionarioVO;
    }

    /**
     * @return the listaSelectItemUnidadeEnsino
     */
    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    /**
     * @param listaSelectItemUnidadeEnsino
     *            the listaSelectItemUnidadeEnsino to set
     */
    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public Boolean getMostrarFiltrosAdicionais() {
        if (getFuncionarioVO().getCodigo() != 0) {
            return true;
        } else {
            return false;
        }
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

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
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
            listaConsultaTurma = new ArrayList<>(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
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
            listaConsultaCurso = new ArrayList<>(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
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

    public List getListaConsultaDisciplina() {
        if (listaConsultaDisciplina == null) {
            listaConsultaDisciplina = new ArrayList<>(0);
        }
        return listaConsultaDisciplina;
    }

    public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
    }

	public String getLayout() {
		if (layout == null) {
			layout = "LAYOUT_DIA_A_DIA";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
	
	public List<SelectItem> getListaSelectUtemLayoutVOs() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("LAYOUT_DIA_A_DIA", "Layout Dia a Dia"));
		itens.add(new SelectItem("LAYOUT_SEMANAL", "Layout Semanal"));
		itens.add(new SelectItem("LAYOUT_COM_ASSINATURA", "Layout Folha de Ponto (C/ Assinatura)"));
		if(verificarPermissaoProfessor()) {
		itens.add(new SelectItem("LAYOUT_EMISSAO_DECLARACAO", "Layout  Emissão de Declaração"));
		}
		return itens;
	}

	public List<HorarioProfessorTurnoVO> getHorarioProfessorTurnoVOs() {
		if (horarioProfessorTurnoVOs == null) {
			horarioProfessorTurnoVOs = new ArrayList<HorarioProfessorTurnoVO>(0);
		}
		return horarioProfessorTurnoVOs;
	}

	public void setHorarioProfessorTurnoVOs(List<HorarioProfessorTurnoVO> horarioProfessorTurnoVOs) {
		this.horarioProfessorTurnoVOs = horarioProfessorTurnoVOs;
	}
	
	public Boolean getCalendarioMensal() {
		if (calendarioMensal == null) {
			calendarioMensal = false;
		}
		return calendarioMensal;
	}

	public void setCalendarioMensal(Boolean calendarioMensal) {
		this.calendarioMensal = calendarioMensal;
	}

	public Date getDataBaseHorarioAula() {
		if (dataBaseHorarioAula == null) {
			dataBaseHorarioAula = new Date();
		}
		return dataBaseHorarioAula;
	}
	
	public void setDataBaseHorarioAula(Date dataBaseHorarioAula) {
		this.dataBaseHorarioAula = dataBaseHorarioAula;
	}
	
	public Boolean getOcultarHorarioLivre() {
		if (ocultarHorarioLivre == null) {
			ocultarHorarioLivre = false;
		}
		return ocultarHorarioLivre;
	}

	/**
	 * @param ocultarHorarioLivre the ocultarHorarioLivre to set
	 */
	public void setOcultarHorarioLivre(Boolean ocultarHorarioLivre) {
		this.ocultarHorarioLivre = ocultarHorarioLivre;
	}
	
	public void inicializarDadosHorarioProfessor() {
		if (getLayout().equals("LAYOUT_SEMANAL")) {
			consultarHorarioAulaProfessor();
		}
	}
	
	private void persistirLayoutPadrao(String valor) throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "agendaProfessor", "designAgendaProfessor", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getOrdenacao(), "agendaProfessor", "ordenacao", getUsuarioLogado());
	}
	
	private void verificarLayoutPadrao() throws Exception {
		LayoutPadraoVO layoutPadraoVO = new LayoutPadraoVO();
		layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("agendaProfessor", "designAgendaProfessor", false, getUsuarioLogado());
		if (!layoutPadraoVO.getValor().equals("")) {
			setLayout(layoutPadraoVO.getValor());
			if(getLayout().equals("LAYOUT_EMISSAO_DECLARACAO")  &&  !verificarPermissaoProfessor()) {
				setLayout("LAYOUT_DIA_A_DIA");
			}else if(getLayout().equals("LAYOUT_EMISSAO_DECLARACAO")  &&  verificarPermissaoProfessor()) {
				inicializarTextoPadrao();		
			}
			layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("agendaProfessor", "ordenacao", false, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(layoutPadraoVO.getValor())) {
				setOrdenacao(layoutPadraoVO.getValor());
			}
		}
	}

	public String getOrdenacao() {
		if(ordenacao ==  null) {
			ordenacao = "";
		}
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

	public List<SelectItem> getTipoOrdenacaoCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino e Data e Hora da Aula"));
		itens.add(new SelectItem("data", "Data e Hora da Aula"));
		itens.add(new SelectItem("disciplina", "Disciplina e Data e Hora da Aula"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemFuncionarioCargo() {
		if (listaSelectItemFuncionarioCargo == null) {
			listaSelectItemFuncionarioCargo = new ArrayList<>();
		}
		return listaSelectItemFuncionarioCargo;
	}

	public void setListaSelectItemFuncionarioCargo(List<SelectItem> listaSelectItemFuncionarioCargo) {
		this.listaSelectItemFuncionarioCargo = listaSelectItemFuncionarioCargo;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public List<SelectItem> getListaSelectItemTipoTextoPadrao() {
		return listaSelectItemTipoTextoPadrao;
	}

	public void setListaSelectItemTipoTextoPadrao(List<SelectItem> listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}
	
	public List<TextoPadraoDeclaracaoVO> getListaTextoDeclaracaoVOs() {
		if(listaTextoDeclaracaoVOs == null) {
			listaTextoDeclaracaoVOs = new ArrayList<TextoPadraoDeclaracaoVO>();
		}
		return listaTextoDeclaracaoVOs;
	}

	public void setListaTextoDeclaracaoVOs(List<TextoPadraoDeclaracaoVO> listaTextoDeclaracaoVOs) {
		this.listaTextoDeclaracaoVOs = listaTextoDeclaracaoVOs;
	}

	public Integer getTextoPadrao() {
		if (textoPadrao == null) {
			textoPadrao = 0;
		}
		return textoPadrao;
	}

	public void setTextoPadrao(Integer textoPadrao) {
		this.textoPadrao = textoPadrao;
	}

	public boolean isPermitirEmissaoDeclaracao() {
		return permitirEmissaoDeclaracao;
	}

	public void setPermitirEmissaoDeclaracao(boolean permitirEmissaoDeclaracao) {
		this.permitirEmissaoDeclaracao = permitirEmissaoDeclaracao;
	}
	
    public List<HorarioProfessorDiaItemVO> getHorarioProfessorGraduacao() {
        if (horarioProfessorGraduacao == null) {
            horarioProfessorGraduacao = new ArrayList<HorarioProfessorDiaItemVO>(0);
        }
        return horarioProfessorGraduacao;
    }

    public void setHorarioProfessorGraduacao(List<HorarioProfessorDiaItemVO> horarioProfessorGraduacao) {
        this.horarioProfessorGraduacao = horarioProfessorGraduacao;
    }

    public List<HorarioProfessorDiaItemVO> getHorarioProfessorPosGraduacao() {
        if (horarioProfessorPosGraduacao == null) {
            horarioProfessorPosGraduacao = new ArrayList<HorarioProfessorDiaItemVO>(0);
        }
        return horarioProfessorPosGraduacao;
    }
	
    public void consultarHorarioProfessorDia() {
        try {
        	validarDados();;
            Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> mapConsulta = null;

            mapConsulta = getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorariosProfessorSeparadoPorNivelEducacional(getFuncionarioVO().getPessoa().getCodigo(), 0, 0, 0, 0, getDataInicio(), getDataFim(), true, "", getUsuarioLogado(), getOrdenacao());

            getHorarioProfessorGraduacao().clear();
            getHorarioProfessorPosGraduacao().clear();
            if (mapConsulta.containsKey(GraduacaoPosGraduacaoEnum.GRADUACAO)) {
                getHorarioProfessorGraduacao().addAll(mapConsulta.get(GraduacaoPosGraduacaoEnum.GRADUACAO));
            }
            if (mapConsulta.containsKey(GraduacaoPosGraduacaoEnum.POS_GRADUACAO)) {
                getHorarioProfessorPosGraduacao().addAll(mapConsulta.get(GraduacaoPosGraduacaoEnum.POS_GRADUACAO));
            }
            mapConsulta.clear();
            //            getHorarioProfessorDiaVO().setHorarioProfessorDiaItemVOs(getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorariosProfessor(getUsuarioLogado().getPessoa().getCodigo(), 0, 0, 0, 0, getDataInicio(), getDataFim(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
	
}
