package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.amazonaws.services.dynamodbv2.model.Stream;

import controle.academico.ExpedicaoDiplomaControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ConfiguracaoAtaResultadosFinaisVO;
import negocio.comuns.academico.ConfiguracaoLayoutAtaResultadosFinaisVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AtaResultadosFinaisDisciplinasRelVO;
import relatorio.negocio.comuns.academico.AtaResultadosFinaisRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.AtaResultadosFinaisRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.HistoricoAlunoRel;

@Controller("AtaResultadosFinaisRelControle")
@Scope("viewScope")
public class AtaResultadosFinaisRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	protected List<TurmaVO> listaConsultaTurma;
	protected String valorConsultaTurma;
	protected String campoConsultaTurma;
	protected TurmaVO turmaVO;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	protected UnidadeEnsinoVO unidadeEnsinoVO;
	protected Boolean existeUnidadeEnsino;
	private String ano;
	private String semestre;
	private String layout;
	private Boolean apresentarDisciplinaComposta;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private String tipoAluno;
	private Boolean apresentarDataTransferencia;
	private String alinhamentoAssinaturaDigital;

	private FuncionarioVO funcionarioPrincipalVO;
	private FuncionarioVO funcionarioSecundarioVO;

	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	
	private List<SelectItem> listaSelectAlinhamentoAssinaturaDigitalEnum;
	
	private Date dataApuracao;
	
	private List<ConfiguracaoAtaResultadosFinaisVO> configuracaoAtaResultadosFinaisVOs;
	private ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO;
	private ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisSelecionadoVO;
	private TipoRelatorioEnum tipoRelatorioEnum;
	private ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO;
	private String tipoLayout;
	private Integer reconhecimentoCurso;
	private List<SelectItem> listaSelectItemReconhecimentoCurso;

	public AtaResultadosFinaisRelControle() { }

	@PostConstruct
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
	}

	@PostConstruct
	private void executarPreechimentoFiltroPadrao() {
		try {
			setAssinarDigitalmente(false);
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), AtaResultadosFinaisRelControle.class.getSimpleName(), getUsuarioLogado());
			Map<String, String> mapResultado = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] {"ano", "semestre", "layout", "tipoAluno", "apresentarDisciplinaComposta", "apresentarDataTransferencia", "assinaturafunc1", "assinaturafunc2", "dataApuracao"}, AtaResultadosFinaisRelControle.class.getSimpleName());
			if(mapResultado.containsKey("ano")) {
				setAno(mapResultado.get("ano"));
			}
			if(mapResultado.containsKey("semestre")) {
				setSemestre(mapResultado.get("semestre"));
			}
			if(mapResultado.containsKey("layout")) {
				setTipoLayout(mapResultado.get("layout"));
			}
			if(mapResultado.containsKey("dataApuracao")) {
				setDataApuracao(UteisData.getData(mapResultado.get("dataApuracao")));
			}
			if(mapResultado.containsKey("tipoAluno")) {
				setTipoAluno(mapResultado.get("tipoAluno"));
			}
			if(mapResultado.containsKey("apresentarDisciplinaComposta")) {
				setApresentarDisciplinaComposta(Boolean.valueOf(mapResultado.get("apresentarDisciplinaComposta")));
			}
			if(mapResultado.containsKey("apresentarDataTransferencia")) {
				setApresentarDataTransferencia(Boolean.valueOf(mapResultado.get("apresentarDataTransferencia")));
			}
			
			if (mapResultado.containsKey("assinaturafunc1") && Uteis.isAtributoPreenchido(Integer.parseInt(mapResultado.get("assinaturafunc1")))) {
				setFuncionarioPrincipalVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(Integer.parseInt(mapResultado.get("assinaturafunc1")), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				setAssinarDigitalmente(false);
			}

			if (mapResultado.containsKey("assinaturafunc2") && Uteis.isAtributoPreenchido(Integer.parseInt(mapResultado.get("assinaturafunc2")))) {
				setFuncionarioSecundarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(Integer.parseInt(mapResultado.get("assinaturafunc2")), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				setAssinarDigitalmente(false);
			}
		}catch (Exception e) {

		}		
	}

	public void persistirDadosPadroes() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), AtaResultadosFinaisRelControle.class.getSimpleName(), getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAno(),AtaResultadosFinaisRelControle.class.getSimpleName(), "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestre(),AtaResultadosFinaisRelControle.class.getSimpleName(), "semestre", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getLayout(),AtaResultadosFinaisRelControle.class.getSimpleName(), "layout", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoAluno(),AtaResultadosFinaisRelControle.class.getSimpleName(), "tipoAluno", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarDisciplinaComposta().toString(),AtaResultadosFinaisRelControle.class.getSimpleName(), "apresentarDisciplinaComposta", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarDataTransferencia().toString(),AtaResultadosFinaisRelControle.class.getSimpleName(), "apresentarDataTransferencia", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(UteisData.getDataAno4Digitos(getDataApuracao()),AtaResultadosFinaisRelControle.class.getSimpleName(), "dataApuracao", getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO())) {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFuncionarioPrincipalVO().getCodigo().toString(),AtaResultadosFinaisRelControle.class.getSimpleName(), "assinaturafunc1", getUsuarioLogado());
			}

			if (Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO())) {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFuncionarioSecundarioVO().getCodigo().toString(),AtaResultadosFinaisRelControle.class.getSimpleName(), "assinaturafunc2", getUsuarioLogado());
			}
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getComboboxProvedorAssinaturaPadrao(){
    	Integer codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getUnidadeEnsinoVO().getCodigo()) ? getUnidadeEnsinoVO().getCodigo() : 0;
		if(!Uteis.isAtributoPreenchido(codigoUnidadeEnsino)){
			setProvedorDeAssinaturaEnum(null);
			return new ArrayList<SelectItem>();
		}
		return this.getComboboxProvedorAssinaturaPadrao(codigoUnidadeEnsino, TipoOrigemDocumentoAssinadoEnum.ATA_RESULTADO_FINAL);
	}

	public void imprimirPDF() {
		try {
			
			String design = "";
			String tipoLayoutComparar = !getTipoLayoutPersonalizado() ? getTipoLayout() : getConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO().getChave().trim().isEmpty() || getConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO().getChave().equals("0") ?  getConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO().getCodigo()+"" : getConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO().getChave();
			registrarAtividadeUsuario(getUsuarioLogado(), "AtaResultadosFinaisRelControle", "Iniciando Impressao Relatorio PDF", "Emitindo Relatorio");
			getFacadeFactory().getAtaResultadosFinaisRelFacade().validarDados(getTurmaVO().getCodigo(), getIsApresentarAno(), getAno(), getIsApresentarSemestre(), getSemestre());
			if(isAssinarDigitalmente()){
				getFacadeFactory().getAtaResultadosFinaisRelFacade().validarFuncionarios(tipoLayoutComparar, getFuncionarioPrincipalVO(), getFuncionarioSecundarioVO());
			}
			
			if (Uteis.getIsValorNumerico(getTipoLayout())) {
				

				if (getConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO().getPastaBaseArquivoPdfPrincipal() == null) {
					throw new Exception("Não foi possível encontrar o modelo do layout principal selecionado ("
							+ getTipoLayout() + ").");
				}
				design = getConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO().getPastaBaseArquivoPdfPrincipal() + File.separator
						+ getConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO().getNomeArquivoPdfPrincipal();
				
			} else {
				design = AtaResultadosFinaisRel.getDesignIReportRelatorio(getTipoLayout());
			}
			
			AtaResultadosFinaisRelVO ataResultadosFinaisRelVO = getFacadeFactory().getAtaResultadosFinaisRelFacade().criarObjeto(getTurmaVO(), getAno(), getSemestre(), tipoLayoutComparar,
					getApresentarDisciplinaComposta(), getUsuarioLogado(), getFiltroRelatorioAcademicoVO(), getTipoAluno(), getApresentarDataTransferencia(), getFuncionarioPrincipalVO(), getFuncionarioSecundarioVO(), getDataApuracao());
			if (tipoLayoutComparar.equals("Layout2") || tipoLayoutComparar.equals("Layout4")) {
				getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(unidadeEnsinoVO, NivelMontarDados.BASICO, getUsuarioLogado());
				montarDadosAtaResultadoFinal(ataResultadosFinaisRelVO);
				getSuperParametroRelVO().adicionarParametro("dataTerminoPeriodoLetivo", Uteis.getDataCidadeDiaMesPorExtensoEAno("", getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarDataFimPeriodoLetivoPorTurmaAnoSemestre(getTurmaVO().getCodigo(), getAno(), getSemestre()), true));
				getSuperParametroRelVO().adicionarParametro("dataAtual", Uteis.getDataCidadeDiaMesPorExtensoEAno(unidadeEnsinoVO.getCidade().getNome() + "/" + unidadeEnsinoVO.getCidade().getEstado().getSigla(), new Date(), false));
			}
			List<AtaResultadosFinaisRelVO> listaObjetos = new ArrayList<AtaResultadosFinaisRelVO>(0);

			if (tipoLayoutComparar.equals("Layout3")) {
				int totalAgrupadoPorNomeDisciplina = ataResultadosFinaisRelVO.getAtaResultadosFinaisDisciplinasRelVOs().stream().collect(Collectors.groupingBy(AtaResultadosFinaisDisciplinasRelVO::getAgrupadorDisciplinaNota)).size();
				int resto = totalAgrupadoPorNomeDisciplina % 16;
				/*if (totalAgrupadoPorNomeDisciplina < 20) {
					resto = (totalAgrupadoPorNomeDisciplina % 20);
				} else {
					resto = totalAgrupadoPorNomeDisciplina;
				}*/
				
				AtaResultadosFinaisDisciplinasRelVO obj = null;
				if (resto != 0) {
					while(resto < 16) {
						obj = new AtaResultadosFinaisDisciplinasRelVO();
						obj.setOrdem(9000000+resto);
						obj.setOrdemLinha(99999999);
						obj.setNomeDisciplina("--");
						obj.setCodigoDisciplina(99999999);
						obj.setMediaFinal("--");
						obj.setNomeAluno("--");

						ataResultadosFinaisRelVO.getAtaResultadosFinaisDisciplinasRelVOs().add(obj);
						resto++;
					}
				}
			}
//			if (tipoLayoutComparar.equals("Layout4")) {
//				ataResultadosFinaisRelVO.setDataApuracaoString(UteisData.getDataPorExtenso(getDataApuracao()).toLowerCase()); 
//				int totalAgrupadoDisciplina = ataResultadosFinaisRelVO.getAtaResultadosFinaisDisciplinasRelVOs().stream().collect(Collectors.groupingBy(AtaResultadosFinaisDisciplinasRelVO::getAgrupadorDisciplinaNota)).size();
//				if(getTurmaVO().getTurmaAgrupada()) {
//					List<CursoVO> cursoVOs = getFacadeFactory().getCursoFacade().consultarCursoTurmasAgrupadasPorTurmaOrigem(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
//					if(Uteis.isAtributoPreenchido(cursoVOs)) {
//						ataResultadosFinaisRelVO.setTotalDiasLetivos(""+getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPorUnidadeEnsinoTurnoCursoAnoSemestre(getAno(), getSemestre(), getTurmaVO().getUnidadeEnsino().getCodigo(), getTurmaVO().getTurno().getCodigo(), cursoVOs.get(0).getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()).getTotalDiaLetivoAno());
//					}
//				}else {
//					ataResultadosFinaisRelVO.setTotalDiasLetivos(""+getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPorUnidadeEnsinoTurnoCursoAnoSemestre(getAno(), getSemestre(), getTurmaVO().getUnidadeEnsino().getCodigo(), getTurmaVO().getTurno().getCodigo(), getTurmaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()).getTotalDiaLetivoAno());
//				}
//				getSuperParametroRelVO().adicionarParametro("totalDiasLetivos", ataResultadosFinaisRelVO.getTotalDiasLetivos());
//				int disciplina = totalAgrupadoDisciplina % 16;
//				AtaResultadosFinaisDisciplinasRelVO objs = null;
//				if (disciplina != 0) {
//					while(disciplina < 7) {
//						objs = new AtaResultadosFinaisDisciplinasRelVO();
//						objs.setOrdem(90000000+disciplina);
//						objs.setOrdemLinha(999999999);
//						objs.setNomeDisciplina("--");
//						objs.setCodigoDisciplina(999999999);
//						objs.setMediaFinal("--");
//						objs.setQtdeFaltas("--");
//						objs.setNomeAluno("--");
//				
//						
//						ataResultadosFinaisRelVO.getAtaResultadosFinaisDisciplinasRelVOs().add(objs);
//						disciplina++;
//					}
//				}
//			}

			listaObjetos.add(ataResultadosFinaisRelVO);
			if (!ataResultadosFinaisRelVO.getAtaResultadosFinaisDisciplinasRelVOs().isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(design);
				if (Uteis.getIsValorNumerico(getTipoLayout())) {					

					getSuperParametroRelVO().setSubReport_Dir(
							getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator
									+ getConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO().getPastaBaseArquivoPdfPrincipal() + File.separator);
					getSuperParametroRelVO().setCaminhoBaseRelatorio(
							getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator
									+ getConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO().getPastaBaseArquivoPdfPrincipal() + File.separator);
				

				getSuperParametroRelVO().adicionarParametro("configuracaoLayoutAtaResultadosFinaisVO",
						getConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO());
				} else {
					getSuperParametroRelVO().setSubReport_Dir(AtaResultadosFinaisRel.getCaminhoBaseRelatorio());
					getSuperParametroRelVO().setCaminhoBaseRelatorio(AtaResultadosFinaisRel.getCaminhoBaseRelatorio());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());

				if (tipoLayoutComparar.equals("Layout3")) {
					getSuperParametroRelVO().setTituloRelatorio(" ATA DE RESULTADOS FINAIS DO ANO LETIVO " + getAno());					
				} else {
					getSuperParametroRelVO().setTituloRelatorio("ATA DE RESULTADOS FINAIS");
				}
				getSuperParametroRelVO().setListaObjetos(listaObjetos);

				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
//				if (tipoLayoutComparar.equals("Layout4")) {
////					setListaAutorizacao(getFacadeFactory().getAutorizacaoCursoFacade().consultarPorListaCurso(getTurmaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//					GradeCurricularVO gradeAtual = getFacadeFactory().getGradeCurricularFacade().consultarGradePorCodigoCurso(getTurmaVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//					try {
//						AreaConhecimentoVO areaConhecimentoCurso = getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(getTurmaVO().getCurso().getAreaConhecimento().getCodigo(), getUsuarioLogado());
//						getSuperParametroRelVO().adicionarParametro("areaConhecimentoCurso", areaConhecimentoCurso.getNome());
//					} catch (Exception e) {
//						AreaConhecimentoVO areaConhecimentoCurso = new AreaConhecimentoVO();
//						areaConhecimentoCurso.setNome("");
//						getSuperParametroRelVO().adicionarParametro("areaConhecimentoCurso", areaConhecimentoCurso.getNome());
//					}
//					String tituloRelatorio = getTituloRelatorio();
//					String tituloEstagio = gradeAtual.getTotalCargaHorariaEstagio() > 0 ? "Estágio Curricular Supervisionado" : "Estágio Curricular Supervisionado Não Obrigatório";
////					if (getListaAutorizacao().isEmpty()) {
////						String primeiroReconhecimento = "";
////						String segundoReconhecimento = "";
////						primeiroReconhecimento = "";
////						segundoReconhecimento = "";
////						getSuperParametroRelVO().adicionarParametro("primeiroReconhecimento", primeiroReconhecimento);
////						getSuperParametroRelVO().adicionarParametro("segundoReconhecimento", segundoReconhecimento);
////					} else if (!getListaAutorizacao().isEmpty()) {
////						String primeiroReconhecimento = getListaAutorizacao().get(0).getNome();
////						String segundoReconhecimento = getListaAutorizacao().get(getListaAutorizacao().size() -1).getNome();
////						getSuperParametroRelVO().adicionarParametro("primeiroReconhecimento", primeiroReconhecimento);
////						getSuperParametroRelVO().adicionarParametro("segundoReconhecimento", segundoReconhecimento);
////					}
////					else if (getListaAutorizacao().size() == 1) {
////						String segundoReconhecimento = "";
////						segundoReconhecimento = "";
////						getSuperParametroRelVO().adicionarParametro("segundoReconhecimento", segundoReconhecimento);
////					}
//					if (getTurmaVO().getSemestral()) {
//						tituloRelatorio += " - SEMESTRE LETIVO 0" + getSemestre() + "-" + getAno();
//					} else if (getTurmaVO().getAnual()) {
//						tituloRelatorio += " - ANO LETIVO " + getAno();
//					}
//					getSuperParametroRelVO().adicionarParametro("resolucaoGrade", gradeAtual.getResolucao());
//					getSuperParametroRelVO().adicionarParametro("tituloEstagio", tituloEstagio);
//					getSuperParametroRelVO().adicionarParametro("nomeDiretor", unidadeEnsinoVO.getDiretorGeral().getPessoa().getNome());
//					getSuperParametroRelVO().adicionarParametro("titulo", tituloRelatorio);
//					getSuperParametroRelVO().adicionarParametro("email", unidadeEnsinoVO.getEmail());
//					getSuperParametroRelVO().adicionarParametro("observacao", getObservacao());
//					getSuperParametroRelVO().adicionarParametro("unidadeCertificadora", unidadeEnsinoVO.getUnidadeCertificadora());
//					getSuperParametroRelVO().adicionarParametro("funcionarioNome", getFuncionarioPrincipalVO().getPessoa().getNome());
//				}
				UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsinoVO.getNome());
				getSuperParametroRelVO().adicionarParametro("razaoSocial", unidadeEnsinoVO.getRazaoSocial());
				getSuperParametroRelVO().setCurso(getTurmaVO().getCurso().getNome());
				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
				}
				getSuperParametroRelVO().adicionarParametro("razaoSocial", unidadeEnsinoVO.getRazaoSocial());	
				
				if(Uteis.isAtributoPreenchido(getReconhecimentoCurso())) {
					AutorizacaoCursoVO reconhecimentoCurso = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(getReconhecimentoCurso(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
					getSuperParametroRelVO().adicionarParametro("reconhecimentoCurso", reconhecimentoCurso.getNome());
				}

				limparMensagem();
				montarDadosAssinaturaDigitalFuncionario();
				realizarImpressaoRelatorio();

				if(isAssinarDigitalmente()){
					setCaminhoRelatorio(getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorAtaResultadosFinais(
							getCaminhoRelatorio(), getTurmaVO(), null, getAno(), getSemestre(), 
							TipoOrigemDocumentoAssinadoEnum.ATA_RESULTADO_FINAL,getProvedorDeAssinaturaEnum(), "#000000", 80f, 200f,
							getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(), getAlinhamentoAssinaturaDigital(),unidadeEnsinoVO, getFuncionarioPrincipalVO(), "", "", getFuncionarioSecundarioVO(),"", ""));
					getListaDocumentoAsssinados().clear();
				}
				
				persistirDadosPadroes();
				//removerObjetoMemoria(this);
				//inicializarListasSelectItemTodosComboBox();
				executarPreechimentoFiltroPadrao();
			} else {
				setMensagemDetalhada("Não foram encontrados nenhum registro com base nos filtros aplicados.");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "AtaResultadosFinaisRelControle", "Finalizando Impressao Relatorio PDF", "Emitindo Relatorio");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	private void montarDadosAtaResultadoFinal(AtaResultadosFinaisRelVO ataResultadosFinaisRelVO) {
		ataResultadosFinaisRelVO.setEndereco(unidadeEnsinoVO.getEndereco());
		ataResultadosFinaisRelVO.setNumero(unidadeEnsinoVO.getNumero());
		ataResultadosFinaisRelVO.setBairro(unidadeEnsinoVO.getSetor());
		ataResultadosFinaisRelVO.setComplemento(unidadeEnsinoVO.getComplemento());
		ataResultadosFinaisRelVO.setCep(unidadeEnsinoVO.getCEP());
		ataResultadosFinaisRelVO.setEstado(unidadeEnsinoVO.getCidade().getEstado().getSigla());
		ataResultadosFinaisRelVO.setFone(unidadeEnsinoVO.getTelComercial1());
		ataResultadosFinaisRelVO.setSite(unidadeEnsinoVO.getSite());
		ataResultadosFinaisRelVO.setEmail(unidadeEnsinoVO.getEmail());
		ataResultadosFinaisRelVO.setCidade(unidadeEnsinoVO.getCidade().getNome());
	}

	private void montarDadosAssinaturaDigitalFuncionario() throws Exception {
		if(isAssinarDigitalmente() && Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(unidadeEnsinoVO.getCodigo(), getUsuarioLogado()))
				&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(unidadeEnsinoVO.getCodigo(), getUsuarioLogado()).getConfiguracaoGedAtaResultadosFinaisVO().getApresentarAssinaturaDigitalizadoFuncionario()){
		if (Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO().getCodigo())) {
			getFuncionarioPrincipalVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(getFuncionarioPrincipalVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getCodigo())) {
				getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioPrimario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + getFuncionarioPrincipalVO().getArquivoAssinaturaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", true);
			} else {
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
			}
		}
//		FUNCIONÁRIO SECUNDÁRIO
		if (Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO().getCodigo())) {
			getFuncionarioSecundarioVO().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(getFuncionarioSecundarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo())) {
				getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioSecundario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", true);
			} else {
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
			}
		} 
		}else {
			getSuperParametroRelVO().adicionarParametro("assinaturaFuncionarioPrincipal", "");
			getSuperParametroRelVO().adicionarParametro("assinaturaFuncionarioSecundario", "");
			getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
			getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
		}
	}

//	public List<SelectItem> getTipoLayout() {
//		List<SelectItem> tipoLayoutLista = new ArrayList<SelectItem>(0);
//		tipoLayoutLista.add(new SelectItem("Layout1", "Layout 1"));
//		tipoLayoutLista.add(new SelectItem("Layout2", "Layout 2"));
//		tipoLayoutLista.add(new SelectItem("Layout3", "Layout 3"));
//		tipoLayoutLista.add(new SelectItem("Layout4", "Layout 4"));
//		return tipoLayoutLista;
//	}
	
	public List<SelectItem> listaTipoLayout;	

	public void setListaTipoLayout(List<SelectItem> listaTipoLayout) {
		this.listaTipoLayout = listaTipoLayout;
	}

	public List<SelectItem> getListaTipoLayout() {
		if (listaTipoLayout == null) {
			try {
							
				listaTipoLayout = getListaTipoLayoutAtaResultadosFinais(getTipoLayout());
				
				setTipoLayout(getTipoLayoutAtaResultadosFinais());
				if(getTipoLayoutPersonalizado()) {
					carregarDadosLayout();
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return listaTipoLayout;
	}


	public void limparTurma() {
		setTurmaVO(new TurmaVO());
		setListaSelectItemReconhecimentoCurso(null);
	}

	public void selecionarTurma() throws Exception {
		try {
			setTurmaVO((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
			//if (getTurmaVO().getTurmaAgrupada() && !getTurmaVO().getTurmaAgrupada()) {
			//	throw new Exception("Não é possível emitir o relátorio de uma turma agrupada! Selecione uma turma específica.");
			//}
			setUnidadeEnsinoVO(getTurmaVO().getUnidadeEnsino());
			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
			montarListaSelectItemReconhecimentoCurso();
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarFuncionarioPrincipal() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioPrincipal");
		setFuncionarioPrincipalVO(obj);
		consultarFuncionarioPrincipal();
	}

	public void selecionarFuncionarioSecundario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioSecundario");
		setFuncionarioSecundarioVO(obj);
		consultarFuncionarioSecundario();
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioPrincipal() throws Exception {
		try {
			setFuncionarioPrincipalVO(consultarFuncionarioPorMatricula(getFuncionarioPrincipalVO().getMatricula()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioSecundario() throws Exception {
		try {
			setFuncionarioSecundarioVO(consultarFuncionarioPorMatricula(getFuncionarioSecundarioVO().getMatricula()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public FuncionarioVO consultarFuncionarioPorMatricula(String matricula) throws Exception {
		FuncionarioVO funcionarioVO = null;
		try {
			funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(matricula, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(funcionarioVO)) {
				return funcionarioVO;
			} else {
				setMensagemDetalhada("msg_erro", "Funcionário de matrícula " + matricula + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new FuncionarioVO();
	}

	public void limparDadosFuncionarioPrincipal() {
		removerObjetoMemoria(getFuncionarioPrincipalVO());
	}

	public void limparDadosFuncionarioSecundario() {
		removerObjetoMemoria(getFuncionarioSecundarioVO());
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			} else {
				setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}
	
	public List<SelectItem> getListaSelectItemSituacaoRecuperacao() {
		return UtilSelectItem.getListaSelectItemEnum(SituacaoRecuperacaoNotaEnum.values(), Obrigatorio.SIM);
	}
	
	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, true, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(), 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			executarMetodoControle(ExpedicaoDiplomaControle.class.getSimpleName(), "setMensagemID", "msg_dados_consultados");
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
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

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

	public Boolean getExisteUnidadeEnsino() {
		if (existeUnidadeEnsino == null) {
			existeUnidadeEnsino = false;
		}
		return existeUnidadeEnsino;
	}

	public void setExisteUnidadeEnsino(Boolean existeUnidadeEnsino) {
		this.existeUnidadeEnsino = existeUnidadeEnsino;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
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

	public Boolean getIsApresentarAno() {
		return !getTurmaVO().getIntegral();
	}

	public Boolean getIsApresentarSemestre() {
		return getTurmaVO().getSemestral();
	}
	
	public boolean getIsApresentarCampoBimestre() {
		return (getTurmaVO().getCurso().getNivelEducacional().equals(TipoNivelEducacional.INFANTIL.getValor()) || getTurmaVO().getCurso().getNivelEducacional().equals(TipoNivelEducacional.BASICO.getValor()) || getTurmaVO().getCurso().getNivelEducacional().equals(TipoNivelEducacional.MEDIO.getValor()));
	}

	public String getLayout() {
		if (layout == null) {
			layout = "Layout1";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public Boolean getApresentarDisciplinaComposta() {
		if (apresentarDisciplinaComposta == null) {
			apresentarDisciplinaComposta = false;
		}
		return apresentarDisciplinaComposta;
	}

	public void setApresentarDisciplinaComposta(Boolean apresentarDisciplinaComposta) {
		this.apresentarDisciplinaComposta = apresentarDisciplinaComposta;
	}

	public List<SelectItem> getListaSelectItemTipoAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("normal", "Normal"));
		itens.add(new SelectItem("reposicao", "Reposição/Inclusão"));
		return itens;
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}

	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}

	public String getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = "";
		}
		return tipoAluno;
	}

	public void setTipoAluno(String tipoAluno) {
		this.tipoAluno = tipoAluno;
	}

	public Boolean getApresentarDataTransferencia() {
		if(apresentarDataTransferencia == null) {
			apresentarDataTransferencia =  false;
		}
		return apresentarDataTransferencia;
	}

	public void setApresentarDataTransferencia(Boolean apresentarDataTransferencia) {
		this.apresentarDataTransferencia = apresentarDataTransferencia;
	}

	public boolean getTipoLayout1() {
		return getTipoLayout().equals("Layout1");
	}

	public String getAlinhamentoAssinaturaDigital() {
		return alinhamentoAssinaturaDigital;
	}

	public void setAlinhamentoAssinaturaDigital(String alinhamentoAssinaturaDigital) {
		this.alinhamentoAssinaturaDigital = alinhamentoAssinaturaDigital;
	}

	public FuncionarioVO getFuncionarioPrincipalVO() {
		if (funcionarioPrincipalVO == null) {
			funcionarioPrincipalVO = new FuncionarioVO();
		}
		return funcionarioPrincipalVO;
	}

	public void setFuncionarioPrincipalVO(FuncionarioVO funcionarioPrincipalVO) {
		this.funcionarioPrincipalVO = funcionarioPrincipalVO;
	}

	public FuncionarioVO getFuncionarioSecundarioVO() {
		if (funcionarioSecundarioVO == null) {
			funcionarioSecundarioVO = new FuncionarioVO();
		}
		return funcionarioSecundarioVO;
	}

	public void setFuncionarioSecundarioVO(FuncionarioVO funcionarioSecundarioVO) {
		this.funcionarioSecundarioVO = funcionarioSecundarioVO;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<>();
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}
	
	public List<SelectItem> getListaSelectAlinhamentoAssinaturaDigitalEnum() {
		if (listaSelectAlinhamentoAssinaturaDigitalEnum == null) {
			listaSelectAlinhamentoAssinaturaDigitalEnum = new ArrayList<>();
			listaSelectAlinhamentoAssinaturaDigitalEnum.add(new SelectItem("", ""));
			for (Enum<AlinhamentoAssinaturaDigitalEnum> enumerador : AlinhamentoAssinaturaDigitalEnum.values()) {
				listaSelectAlinhamentoAssinaturaDigitalEnum.add(new SelectItem(enumerador, enumerador.toString()));
			}
		}
		return listaSelectAlinhamentoAssinaturaDigitalEnum;
	}
	
	public Date getDataApuracao() {
		if (dataApuracao == null) {
			dataApuracao = new Date();
		}
		return dataApuracao;
	}

	public void setDataApuracao(Date dataApuracao) {
		this.dataApuracao = dataApuracao;
	}
	
//	public String getObservacao() {
//		if (observacao == null) {
//			observacao = "";
//		}
//		return observacao;
//	}
//	
//	public void setObservacao(String observacao) {
//		this.observacao = observacao;
//	}
	
	public Boolean getApresentarObservacao() {
		if (getTipoLayout().equals("Layout4")) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	public Boolean getApresentarTituloRelatorio() {
		if (getTipoLayout().equals("Layout4")) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
//	public String getTituloRelatorio() {
//		if (tituloRelatorio == null) {
//			tituloRelatorio = "";
//		}
//		return tituloRelatorio;
//	}
//	
//	public void setTituloRelatorio(String tituloRelatorio) {
//		this.tituloRelatorio = tituloRelatorio;
//	}
//	
//	public String getTotalCargaHoraria() {
//		if (totalCargaHoraria == null) {
//			totalCargaHoraria = "0";
//		}
//		return totalCargaHoraria;
//	}
//	
//	public void setTotalCargaHoraria(String totalCargaHoraria) {
//		this.totalCargaHoraria = totalCargaHoraria;
//	}
	
private List<AutorizacaoCursoVO> listaAutorizacao;
	
	public List<AutorizacaoCursoVO> getListaAutorizacao() {
		if (listaAutorizacao == null) {
			listaAutorizacao = new ArrayList<>(0);
		}
		return listaAutorizacao;
	}
	
	public void setListaAutorizacao(List<AutorizacaoCursoVO> listaAutorizacao) {
		this.listaAutorizacao = listaAutorizacao;
	}
	
//	public void persistirLayout() {
//		try {
//			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTituloRelatorio().toString(), AtaResultadosFinaisRel.class.getName()+"_"+getUsuarioLogado().getCodigo(), "Titulo Padrão", getUsuarioLogado());
//		} catch (Exception e) {
//			e.getMessage();
//		}
//	}
	
	public ConfiguracaoAtaResultadosFinaisVO getConfiguracaoAtaResultadosFinaisVO() {
		if (configuracaoAtaResultadosFinaisVO == null) {
			configuracaoAtaResultadosFinaisVO = new ConfiguracaoAtaResultadosFinaisVO();
		}
		return configuracaoAtaResultadosFinaisVO;
	}

	public void setConfiguracaoAtaResultadosFinaisVO(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO) {
		this.configuracaoAtaResultadosFinaisVO = configuracaoAtaResultadosFinaisVO;
	}

	public void consultarConfiguracaoAtaResultadosFinais() {
		try {
			setConfiguracaoAtaResultadosFinaisVOs(getFacadeFactory().getConfiguracaoAtaResultadosFinaisFacade()
					.consultarConfiguracoesAtaResultadosFinais(getUsuarioLogado()));
			getConfiguracaoAtaResultadosFinaisVOs().forEach(t -> t.setEdicaoManual(true));
			setConfiguracaoAtaResultadosFinaisVO(getConfiguracaoAtaResultadosFinaisVOs().get(0));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<ConfiguracaoAtaResultadosFinaisVO> getConfiguracaoAtaResultadosFinaisVOs() {
		if (configuracaoAtaResultadosFinaisVOs == null) {
			configuracaoAtaResultadosFinaisVOs = new ArrayList<ConfiguracaoAtaResultadosFinaisVO>(0);
		}
		return configuracaoAtaResultadosFinaisVOs;
	}

	public void setConfiguracaoAtaResultadosFinaisVOs(List<ConfiguracaoAtaResultadosFinaisVO> configuracaoAtaResultadosFinaisVOs) {
		this.configuracaoAtaResultadosFinaisVOs = configuracaoAtaResultadosFinaisVOs;
	}

	public ConfiguracaoLayoutAtaResultadosFinaisVO getConfiguracaoLayoutAtaResultadosFinaisVO() {
		if (configuracaoLayoutAtaResultadosFinaisVO == null) {
			configuracaoLayoutAtaResultadosFinaisVO = new ConfiguracaoLayoutAtaResultadosFinaisVO();
		}
		return configuracaoLayoutAtaResultadosFinaisVO;
	}

	public void setConfiguracaoLayoutAtaResultadosFinaisVO(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO) {
		this.configuracaoLayoutAtaResultadosFinaisVO = configuracaoLayoutAtaResultadosFinaisVO;
	}

	public TipoRelatorioEnum getTipoRelatorioEnum() {
		if (tipoRelatorioEnum == null) {
			tipoRelatorioEnum = TipoRelatorioEnum.PDF;
		}
		return tipoRelatorioEnum;
	}

	public void setTipoRelatorioEnum(TipoRelatorioEnum tipoRelatorioEnum) {
		this.tipoRelatorioEnum = tipoRelatorioEnum;
	}

	public void novoConfiguracaoLayoutAtaResultadosFinaisVO() {
		setOncompleteModal("");
		setConfiguracaoLayoutAtaResultadosFinaisVO(new ConfiguracaoLayoutAtaResultadosFinaisVO());
		getConfiguracaoLayoutAtaResultadosFinaisVO().setConfiguracaoAtaResultadosFinaisVO(getConfiguracaoAtaResultadosFinaisVO());
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}

	public void editarConfiguracaoLayoutAtaResultadosFinaisVO(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO) {
		setOncompleteModal("");
		setConfiguracaoLayoutAtaResultadosFinaisVO(new ConfiguracaoLayoutAtaResultadosFinaisVO());
		setConfiguracaoLayoutAtaResultadosFinaisVO(configuracaoLayoutAtaResultadosFinaisVO);
		getConfiguracaoLayoutAtaResultadosFinaisVO().setConfiguracaoAtaResultadosFinaisVO(getConfiguracaoAtaResultadosFinaisVO());
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}

	public void persistirConfiguracaoLayoutAtaResultadosFinais(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO) {
		try {
			getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade().persistir(
					getConfiguracaoAtaResultadosFinaisVO(),
					configuracaoLayoutAtaResultadosFinaisVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setOncompleteModal("RichFaces.$('panelConfiguracaoLayout').hide()");
			consultarConfiguracaoAtaResultadosFinais();
			setListaTipoLayout(null);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarDefinicaoArquivoPrincipalConfiguracaoLayoutAtaResultadosFinais(
			ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO, ArquivoVO arquivoVO) {
		try {

			getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade()
					.realizarDefinicaoArquivoPrincipalConfiguracaoLayoutAtaResultadosFinais(configuracaoLayoutAtaResultadosFinaisVO,
							arquivoVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativarConfiguracaoLayoutAtaResultadosFinais(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO) {
		try {
			configuracaoLayoutAtaResultadosFinaisVO.setInativarLayout(true);
			getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade().persistir(getConfiguracaoAtaResultadosFinaisVO(),
					configuracaoLayoutAtaResultadosFinaisVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativarTodasConfiguracaoLayoutAtaResultadosFinais() {
		try {
			getConfiguracaoAtaResultadosFinaisVO().getConfiguracaoLayoutAtaResultadosFinaisVOs().forEach(t -> t.setInativarLayout(true));
			getFacadeFactory().getConfiguracaoAtaResultadosFinaisFacade().persistir(getConfiguracaoAtaResultadosFinaisVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void ativarTodasConfiguracaoLayoutAtaResultadosFinais() {
		try {
			getConfiguracaoAtaResultadosFinaisVO().getConfiguracaoLayoutAtaResultadosFinaisVOs().forEach(t -> t.setInativarLayout(false));
			getFacadeFactory().getConfiguracaoAtaResultadosFinaisFacade().persistir(getConfiguracaoAtaResultadosFinaisVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void ativarConfiguracaoLayoutAtaResultadosFinais(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO) {
		try {
			configuracaoLayoutAtaResultadosFinaisVO.setInativarLayout(false);
			getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade().persistir(getConfiguracaoAtaResultadosFinaisVO(),
					configuracaoLayoutAtaResultadosFinaisVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirConfiguracaoLayoutAtaResultadosFinais(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO) {
		try {
			getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade().excluir(configuracaoAtaResultadosFinaisVO,
					configuracaoLayoutAtaResultadosFinaisVO, getUsuarioLogado());
			consultarConfiguracaoAtaResultadosFinais();
			setListaTipoLayout(null);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void cancelarConfiguracaoLayoutAtaResultadosFinais() {
		if (Uteis.isAtributoPreenchido(getConfiguracaoLayoutAtaResultadosFinaisVO())) {
			try {
				ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO = getConfiguracaoAtaResultadosFinaisVOs().stream()
						.findFirst().get();
				setConfiguracaoLayoutAtaResultadosFinaisVO(getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade()
						.consultarPorChavePrimaria(getConfiguracaoLayoutAtaResultadosFinaisVO().getCodigo(), getUsuarioLogado()));
				configuracaoAtaResultadosFinaisVO.getConfiguracaoLayoutAtaResultadosFinaisVOs().set(configuracaoAtaResultadosFinaisVO
						.getConfiguracaoLayoutAtaResultadosFinaisVOs().indexOf(getConfiguracaoLayoutAtaResultadosFinaisVO()),
						getConfiguracaoLayoutAtaResultadosFinaisVO());
				limparMensagem();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void persistirConfiguracaoAtaResultadosFinais(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO) {
		try {
			getFacadeFactory().getConfiguracaoAtaResultadosFinaisFacade().persistir(configuracaoAtaResultadosFinaisVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void uploadLayout(FileUploadEvent fileUploadEvent) {
		try {

			getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade().adicionarLayout(fileUploadEvent,
					getTipoRelatorioEnum(),
					getConfiguracaoAtaResultadosFinaisVO(),
					getConfiguracaoLayoutAtaResultadosFinaisVO(), getUsuarioLogado());

			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerLayout(ArquivoVO arquivoVO, ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO) {
		try {
			getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade().removerLayout(arquivoVO,
					configuracaoLayoutAtaResultadosFinaisVO, getUsuarioLogado());
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarDownloadArquivo(ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO,
			ArquivoVO arquivoVO) throws Exception {
		if (configuracaoLayoutAtaResultadosFinaisVO.getLayoutFixoSistema()) {
			ArquivoVO cloneArquivo = (ArquivoVO) arquivoVO.clone();
			cloneArquivo.setPastaBaseArquivo(UteisJSF.getCaminhoWeb().replace("/", File.separator) + File.separator
					+ "WEB-INF" + File.separator + "classes" + File.separator + cloneArquivo.getPastaBaseArquivo());
			context().getExternalContext().getSessionMap().put("arquivoVO", cloneArquivo);
		} else if (!arquivoVO.getPastaBaseArquivo()
				.startsWith(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo())) {
			ArquivoVO cloneArquivo = (ArquivoVO) arquivoVO.clone();
			cloneArquivo.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()
					+ File.separator + cloneArquivo.getPastaBaseArquivo());
			context().getExternalContext().getSessionMap().put("arquivoVO", cloneArquivo);
		} else {
			context().getExternalContext().getSessionMap().put("arquivoVO", arquivoVO);
		}

	}
	
	public void realizarDefinicaoLayoutPadraoConfiguracaoLayoutAtaResultadosFinais(
			ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO,
			ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisVO) {
		try {
			getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade()
					.realizarDefinicaoLayoutPadraoConfiguracaoLayoutAtaResultadosFinais(configuracaoAtaResultadosFinaisVO,
							configuracaoLayoutAtaResultadosFinaisVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarDefinicaoConfiguracaoAtaResultadosFinaisUsar() {
		try {

			setConfiguracaoAtaResultadosFinaisVO(getFacadeFactory().getConfiguracaoAtaResultadosFinaisFacade()
						.consultarConfiguracaoAtaResultadosFinais(getUsuarioLogado()));

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public Boolean getTipoLayoutPersonalizado() {
		return Uteis.isAtributoPreenchido(getTipoLayout()) && Uteis.getIsValorNumerico(getTipoLayout());
	}


	public ConfiguracaoLayoutAtaResultadosFinaisVO getConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO() {
		if(configuracaoLayoutAtaResultadosFinaisSelecionadoVO == null) {
			configuracaoLayoutAtaResultadosFinaisSelecionadoVO = new ConfiguracaoLayoutAtaResultadosFinaisVO();
		}
		return configuracaoLayoutAtaResultadosFinaisSelecionadoVO;
	}

	public void setConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO(
			ConfiguracaoLayoutAtaResultadosFinaisVO configuracaoLayoutAtaResultadosFinaisSelecionadoVO) {
		this.configuracaoLayoutAtaResultadosFinaisSelecionadoVO = configuracaoLayoutAtaResultadosFinaisSelecionadoVO;
	} 
	
	public void carregarDadosLayout() {
		try {
			if (Uteis.getIsValorNumerico(getTipoLayout())) {
				if (Uteis.isAtributoPreenchido(getConfiguracaoAtaResultadosFinaisVO())
					&& getConfiguracaoAtaResultadosFinaisVO().getConfiguracaoLayoutAtaResultadosFinaisVOs().stream()
							.anyMatch(l -> l.getCodigo().toString().equals(getTipoLayout()))) {
					setConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO(null);
					setConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO(getConfiguracaoAtaResultadosFinaisVO().getConfiguracaoLayoutAtaResultadosFinaisVOs()
						.stream().filter(l -> l.getCodigo().toString().equals(getTipoLayout())).findFirst()
						.get());			
				} else {
					setConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO(getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade()
						.consultarPorChavePrimaria(Integer.parseInt(getTipoLayout()), getUsuario()));						
				}	
			}else {
				setConfiguracaoLayoutAtaResultadosFinaisSelecionadoVO(new ConfiguracaoLayoutAtaResultadosFinaisVO());
			}
		}catch (Exception e) {
			// TODO: handle exception
		}

	}

		public void setTipoLayout(String tipoLayout) {
			this.tipoLayout = tipoLayout;
		}

		public String getTipoLayout() {
			if (tipoLayout == null) {
				tipoLayout = "";
			}
			return tipoLayout;
		}

		public Integer getReconhecimentoCurso() {
			if(reconhecimentoCurso == null) {
					reconhecimentoCurso = 0;				
			}
			return reconhecimentoCurso;
		}

		public void setReconhecimentoCurso(Integer reconhecimentoCurso) {
			this.reconhecimentoCurso = reconhecimentoCurso;
		}
		
		public List<SelectItem> getListaSelectItemReconhecimentoCurso() {
			if (listaSelectItemReconhecimentoCurso == null) {
				listaSelectItemReconhecimentoCurso = new ArrayList<SelectItem>(0);
			}
			return listaSelectItemReconhecimentoCurso;
		}

		public void setListaSelectItemReconhecimentoCurso(List<SelectItem> listaSelectItemReconhecimentoCurso) {
			this.listaSelectItemReconhecimentoCurso = listaSelectItemReconhecimentoCurso;
		}
		
		public void montarListaSelectItemReconhecimentoCurso() throws Exception {
			try {
						
				if(Uteis.isAtributoPreenchido(getTurmaVO().getCurso().getAutorizacaoCursoVOs())) {
					
					getTurmaVO().getCurso().getAutorizacaoCursoVOs()
					.stream()
					.sorted(Comparator.comparingInt(AutorizacaoCursoVO::getCodigo))
					.findFirst()
					.ifPresent(a -> setReconhecimentoCurso(a.getCodigo()));	
					
					setListaSelectItemReconhecimentoCurso(UtilSelectItem.getListaSelectItem(getTurmaVO().getCurso().getAutorizacaoCursoVOs(), "codigo", "nome"));
					
				
				}

			} catch (Exception e) {
				throw e;
			}
		}
		
		
		
}
