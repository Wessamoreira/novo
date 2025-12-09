package webservice.servicos;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.academico.RenovarMatriculaControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PoliticaDivulgacaoMatriculaOnlineVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.CampanhaColaboradorCursoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.EnderecoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.processosel.PreInscricaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import relatorio.controle.academico.DocumentacaoMatriculaRelControle;
import relatorio.controle.financeiro.ComprovanteRecebimentoRelControle;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;
import webservice.servicos.excepetion.ErrorInfoRSVO;

@Service
@Path("/matriculaOnlineExternaRS")
public class MatriculaOnlineExternaRS extends SuperControle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private RenovarMatriculaControle renovarMatriculaControle;
	private List<BannerObject> bannerObjects;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private ComprovanteRecebimentoRelControle comprovanteRecebimentoRelControle;
	@Context 
	private HttpServletRequest request;
	
	public MatriculaOnlineExternaRS() throws Exception {
		super();
		consultarConfiguracaoGeralSistemaUtilizar(null);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/cadastrarPreInscricao")
	public Response realizarPreInscricao(final PessoaObject pessoaObject) {
		PreInscricaoVO preInscricaoVO = new PreInscricaoVO();
		try {
			consultarConfiguracaoGeralSistemaUtilizar(null);
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getConfiguracaoGeralSistemaVO().getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
			preInscricaoVO.getProspect().setNome(pessoaObject.getNome());
			preInscricaoVO.getProspect().setEmailPrincipal(pessoaObject.getEmail());
			preInscricaoVO.getProspect().setTelefoneResidencial(pessoaObject.getTelefoneResidencial());
			preInscricaoVO.getProspect().setCelular(pessoaObject.getCelular());
			preInscricaoVO.getCurso().setCodigo(pessoaObject.getCodigoCurso());
			preInscricaoVO.setMatriculaOnlineExterna(true);
			if (pessoaObject.getCodigoCurso() != null) {
				CursoInteresseVO cursoInteresseVO = new CursoInteresseVO();
				cursoInteresseVO.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(pessoaObject.getCodigoCurso(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuarioVO));
				preInscricaoVO.getProspect().getCursoInteresseVOs().add(cursoInteresseVO);
			}			
		    getFacadeFactory().getPreInscricaoFacade().incluirPreInscricaoAPartirMatriculaExternaOnline(preInscricaoVO);		    
		    PessoaObject pessoaRetorno = pessoaObject;
		    if(!preInscricaoVO.getProspect().getPessoa().getCodigo().equals(0)) {
		    	pessoaRetorno = montarPessoaPreInscricaoPessoaExistente(preInscricaoVO.getProspect().getPessoa());
		    } else {
		    	pessoaRetorno.setCodigo(0);
		    	pessoaRetorno.setNome(preInscricaoVO.getNome());			
		    }
			return Response.status(Status.OK).entity(pessoaRetorno).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.BAD_REQUEST.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
		}
	}
	
	public PessoaObject montarPessoaPreInscricaoPessoaExistente(PessoaVO pessoaVO) throws Exception {
		PessoaObject pessoaObject = new PessoaObject();
		pessoaObject.setCodigo(pessoaVO.getCodigo());
		pessoaObject.setNome(pessoaVO.getNome());	
		pessoaObject.setEmail(pessoaVO.getEmail());
		pessoaObject.setTelefoneResidencial(pessoaVO.getTelefoneRes());
		pessoaObject.setCelular(pessoaVO.getCelular());
		pessoaObject.setEndereco(pessoaVO.getEndereco());
		pessoaObject.setSetor(pessoaVO.getSetor());
		pessoaObject.setNumero(pessoaVO.getNumero());
		pessoaObject.setRg(pessoaVO.getRG());
		pessoaObject.setCpf(pessoaVO.getCPF());
		pessoaObject.setCep(pessoaVO.getCEP());
		pessoaObject.setComplemento(pessoaVO.getComplemento());
		pessoaObject.getCidade().setCodigo(pessoaVO.getCidade().getCodigo());
		pessoaObject.getCidade().setNome(pessoaVO.getCidade().getNome());
		pessoaObject.getCidade().setCep(pessoaVO.getCidade().getCep());
		pessoaObject.getCidade().setEstado(pessoaVO.getCidade().getEstado());
		return pessoaObject;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/banners")
	public Response consultarBanners() {
		try {
			setBannerObjects(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarBannersAtivosDivulgacaoParaComunidadeMatriculaOnlineExterna(getConfiguracaoGeralSistemaVO()));
			return Response.ok().entity(new GenericEntity<List<BannerObject>>(getBannerObjects()) {}).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.BAD_REQUEST.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
		}
	}

	public List<BannerObject> getBannerObjects() {
		if (bannerObjects == null) {
			bannerObjects = new ArrayList<BannerObject>(0);
		}
		return bannerObjects;
	}

	public void setBannerObjects(List<BannerObject> bannerObjects) {
		this.bannerObjects = bannerObjects;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if (configuracaoGeralSistemaVO == null) {
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	public void consultarConfiguracaoGeralSistemaUtilizar(Integer unidadeEnsino) {
		try {
			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, null, unidadeEnsino));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GET
	@Produces({ "application/json" })
	@Path("/consultarCurso/{codigoCurso}")
	public Response consultarCurso(@PathParam("codigoCurso") Integer codigoCurso) {
		CursoObject cursoObject = new CursoObject();
		try {
			cursoObject = getFacadeFactory().getCursoFacade().consultarPorChavePrimariaMatriculaExterna(codigoCurso);
			return Response.status(Status.OK).entity(cursoObject).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.BAD_REQUEST.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
		}
	}

	@GET
	@Produces({ "application/json" })
	@Path("/consultarDadosParaRealizarMatriculaOnlineExterna/{codigoCurso}/{codigoBanner}/{codigoPessoa}")
	public MatriculaRSVO consultarDadosParaRealizarMatriculaOnlineExterna(@PathParam("codigoCurso") Integer codigoCurso, @PathParam("codigoBanner") Integer codigoBanner) {
		MatriculaRSVO matriculaRSVO = new MatriculaRSVO();
		try {
			renovarMatriculaControle = new RenovarMatriculaControle(true);
			renovarMatriculaControle.setMatriculaVO(new MatriculaVO());
			renovarMatriculaControle.setMatriculaPeriodoVO(new MatriculaPeriodoVO());
			renovarMatriculaControle.setRealizandoNovaMatriculaAluno(true);
			renovarMatriculaControle.setBanner(codigoBanner);
			renovarMatriculaControle.getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(codigoCurso, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, null));
			matriculaRSVO.setCursoObject(getFacadeFactory().getCursoFacade().consultarPorChavePrimariaMatriculaExterna(codigoCurso));
			matriculaRSVO.getUnidadeEnsinoRSVOs().addAll(consultarUnidadeEnsinoPorCondigoBanner(codigoBanner));
			if(matriculaRSVO.getUnidadeEnsinoRSVOs().isEmpty()){
				throw new Exception("Não foi encontrada nenhuma UNIDADE DE ENSINO habilitada para a realização da matrícula on-line.");
			}
			matriculaRSVO.getTurnoRSVOs().addAll(consultarTurnoPorCondigoBannerUnidadeEnsino(codigoBanner, matriculaRSVO.getUnidadeEnsinoRSVOs().get(0).getCodigo()));
			if(matriculaRSVO.getTurnoRSVOs().isEmpty()){
				throw new Exception("Não foi encontrado nenhum TURNO habilitada para a realização da matrícula on-line.");
			}
			renovarMatriculaControle.getMatriculaVO().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(matriculaRSVO.getTurnoRSVOs().get(0).getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			matriculaRSVO.getProcessoMatriculaRSVOs().addAll(consultarProcessoMatriculaPorCondigoBanner(codigoBanner, matriculaRSVO.getUnidadeEnsinoRSVOs().get(0).getCodigo()));
			if(matriculaRSVO.getProcessoMatriculaRSVOs().isEmpty()){
				throw new Exception("Não foi encontrado nenhum CALENDÁRIO DE MATRÍCULA habilitada para a realização da matrícula on-line.");
			}
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, matriculaRSVO.getUnidadeEnsinoRSVOs().get(0).getCodigo(), null);
			matriculaRSVO.getTurmaRSVOs().addAll(consultarTurmaPorPeriodoLetivoUnidadeEnsinoCursoTurno(matriculaRSVO.getUnidadeEnsinoRSVOs().get(0).getCodigo(), matriculaRSVO.getCursoObject().getCodigo(), matriculaRSVO.getTurnoRSVOs().get(0).getCodigo(), matriculaRSVO.getCursoObject().getGradeDisciplinaObject().getCodigo(), configuracaoFinanceiroVO));
			if(matriculaRSVO.getTurmaRSVOs().isEmpty()){
				throw new Exception("Não foi encontrada nenhuma TURMA habilitada para a realização da matrícula on-line.");
			}
			renovarMatriculaControle.getMatriculaPeriodoVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(matriculaRSVO.getTurmaRSVOs().get(0).getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			getFacadeFactory().getTurmaFacade().processarDadosPermitinentesTurmaSelecionada(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), configuracaoFinanceiroVO, null);
			matriculaRSVO.getCondicaoPagamentoRSVOs().addAll(consultarCondicaoPagamentoPlanoFinanceiroCurso(configuracaoFinanceiroVO));
			if(matriculaRSVO.getProcessoMatriculaRSVOs().isEmpty()){
				throw new Exception("Não foi encontrada nenhuma CONDIÇÃO FINANCEIRA habilitada para a realização da matrícula on-line.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			matriculaRSVO.setMensagem(e.getMessage());
		}
		return matriculaRSVO;
	}

	public List<UnidadeEnsinoRSVO> consultarUnidadeEnsinoPorCondigoBanner(Integer codigoBanner) {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs;
		UnidadeEnsinoRSVO unidadeEnsinoRSVO = null;
		List<UnidadeEnsinoRSVO> unidadeEnsinoRSVOs = new ArrayList<UnidadeEnsinoRSVO>();
		try {
			unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoDoProcessoMatriculaPorCodigoBanner(codigoBanner, null);
			renovarMatriculaControle.getMatriculaVO().setUnidadeEnsino(unidadeEnsinoVOs.get(0));
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				unidadeEnsinoRSVO = new UnidadeEnsinoRSVO();
				unidadeEnsinoRSVO.setCodigo(unidadeEnsinoVO.getCodigo());
				unidadeEnsinoRSVO.setNome(unidadeEnsinoVO.getNome());
				unidadeEnsinoRSVOs.add(unidadeEnsinoRSVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unidadeEnsinoRSVOs;
	}

	public List<TurnoRSVO> consultarTurnoPorCondigoBannerUnidadeEnsino(Integer codigoBanner, Integer codigoUnidadeEnsino) {
		List<TurnoVO> turnoVOs;
		TurnoRSVO turnoRSVO = null;
		List<TurnoRSVO> turnoRSVOs = new ArrayList<TurnoRSVO>();
		try {
			turnoVOs = getFacadeFactory().getTurnoFacade().consultarTurnoPorCodigoBanner(codigoBanner, codigoUnidadeEnsino, null);
			for (TurnoVO turnoVO : turnoVOs) {
				turnoRSVO = new TurnoRSVO();
				turnoRSVO.setCodigo(turnoVO.getCodigo());
				turnoRSVO.setNome(turnoVO.getNome());
				turnoRSVOs.add(turnoRSVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return turnoRSVOs;
	}

	public List<ProcessoMatriculaRSVO> consultarProcessoMatriculaPorCondigoBanner(Integer codigoBanner, Integer unidadeEnsino) {
		List<ProcessoMatriculaVO> processoMatriculaVOs;
		ProcessoMatriculaRSVO processoMatriculaRSVO = null;
		List<ProcessoMatriculaRSVO> processoMatriculaRSVOs = new ArrayList<ProcessoMatriculaRSVO>();
		try {
			processoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultarProcessosMatriculasPorCodigoBanner(codigoBanner, unidadeEnsino, null);
			for (ProcessoMatriculaVO processoMatriculaVO : processoMatriculaVOs) {
				processoMatriculaRSVO = new ProcessoMatriculaRSVO();
				processoMatriculaRSVO.setCodigo(processoMatriculaVO.getCodigo());
				processoMatriculaRSVO.setNome(processoMatriculaVO.getDescricao());
				processoMatriculaRSVOs.add(processoMatriculaRSVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processoMatriculaRSVOs;
	}

	public List<TurmaRSVO> consultarTurmaPorPeriodoLetivoUnidadeEnsinoCursoTurno(Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurno, Integer codigoGradeCurricular, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		TurmaRSVO turmaRSVO = null;
		List<TurmaRSVO> turmaRSVOs = new ArrayList<TurmaRSVO>();
		try {
			List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurno(1, codigoUnidadeEnsino, codigoCurso, codigoTurno, codigoGradeCurricular, true, false, false, renovarMatriculaControle.getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), renovarMatriculaControle.getMatriculaPeriodoVO().getAno(), renovarMatriculaControle.getMatriculaPeriodoVO().getSemestre(),  false, null, configuracaoFinanceiroVO, true);
			for (TurmaVO turmaVO : turmaVOs) {
				turmaRSVO = new TurmaRSVO();
				turmaRSVO.setCodigo(turmaVO.getCodigo());
				turmaRSVO.setNome(turmaVO.getIdentificadorTurma());
				turmaRSVOs.add(turmaRSVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return turmaRSVOs;
	}

	public List<CondicaoPagamentoRSVO> consultarCondicaoPagamentoPlanoFinanceiroCurso(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		CondicaoPagamentoRSVO condicaoPagamentoRSVO = null;
		List<CondicaoPagamentoRSVO> condicaoPagamentoRSVOs = new ArrayList<CondicaoPagamentoRSVO>();
		renovarMatriculaControle.getMatriculaVO().setMatriculaOnlineExterna(true);
		try {
			if (!configuracaoFinanceiroVO.getRealizarMatriculaComFinanceiroManualAtivo()) {
				getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), null);
				List<CondicaoPagamentoPlanoFinanceiroCursoVO> condicaoPagamentoPlanoFinanceiroCursoVOs = getFacadeFactory().getPlanoFinanceiroCursoFacade().inicializarListaSelectItemPlanoFinanceiroCursoParaTurma(renovarMatriculaControle.getRealizandoNovaMatriculaAluno(), renovarMatriculaControle.getBanner(), renovarMatriculaControle.getMatriculaPeriodoVO(), renovarMatriculaControle.getMatriculaVO(), null);
				for (CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO : condicaoPagamentoPlanoFinanceiroCursoVOs) {
					condicaoPagamentoRSVO = new CondicaoPagamentoRSVO();
					condicaoPagamentoRSVO.setCodigo(condicaoPagamentoPlanoFinanceiroCursoVO.getCodigo());
					condicaoPagamentoRSVO.setNome(condicaoPagamentoPlanoFinanceiroCursoVO.getDescricao());
					condicaoPagamentoRSVO.setValorMatricula(condicaoPagamentoPlanoFinanceiroCursoVO.getValorMatricula());
					condicaoPagamentoRSVO.setValorMensalidade(condicaoPagamentoPlanoFinanceiroCursoVO.getValorParcela());
					condicaoPagamentoRSVO.setParcelas(condicaoPagamentoPlanoFinanceiroCursoVO.getNrParcelasPeriodo());
					condicaoPagamentoRSVO.setCategoria(condicaoPagamentoPlanoFinanceiroCursoVO.getCategoria());
					condicaoPagamentoRSVOs.add(condicaoPagamentoRSVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return condicaoPagamentoRSVOs;
	}

	@GET
	@Produces({ "application/json" })
	@Path("/atualizarDadosQuandoTurnoAlterado/{codigoUnidadeEnsino}/{codigoCurso}/{codigoTurno}/{codigoGradeCurricular}/{codigoBanner}/{codigoPessoa}")
	public MatriculaRSVO atualizarDadosQuandoTurnoAlterado(@PathParam("codigoUnidadeEnsino") Integer codigoUnidadeEnsino, @PathParam("codigoCurso") Integer codigoCurso, @PathParam("codigoTurno") Integer codigoTurno, @PathParam("codigoGradeCurricular") Integer codigoGradeCurricular, @PathParam("codigoBanner") Integer codigoBanner) {
		MatriculaRSVO matriculaRSVO = new MatriculaRSVO();
		try {
			renovarMatriculaControle = new RenovarMatriculaControle(true);
			renovarMatriculaControle.setMatriculaVO(new MatriculaVO());
			renovarMatriculaControle.setMatriculaPeriodoVO(new MatriculaPeriodoVO());
			renovarMatriculaControle.setRealizandoNovaMatriculaAluno(true);
			renovarMatriculaControle.setBanner(codigoBanner);
			renovarMatriculaControle.getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(codigoCurso, Uteis.NIVELMONTARDADOS_TODOS, false, null));
			matriculaRSVO.setCursoObject(getFacadeFactory().getCursoFacade().consultarPorChavePrimariaMatriculaExterna(codigoCurso));
			matriculaRSVO.getUnidadeEnsinoRSVOs().addAll(consultarUnidadeEnsinoPorCondigoBanner(codigoBanner));
			matriculaRSVO.getTurnoRSVOs().addAll(consultarTurnoPorCondigoBannerUnidadeEnsino(codigoBanner, codigoUnidadeEnsino));
			renovarMatriculaControle.getMatriculaVO().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(codigoTurno, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			matriculaRSVO.getProcessoMatriculaRSVOs().addAll(consultarProcessoMatriculaPorCondigoBanner(codigoBanner, codigoUnidadeEnsino));
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, codigoUnidadeEnsino, null);
			matriculaRSVO.getTurmaRSVOs().addAll(consultarTurmaPorPeriodoLetivoUnidadeEnsinoCursoTurno(codigoUnidadeEnsino, codigoCurso, codigoTurno, codigoGradeCurricular, configuracaoFinanceiroVO));
			renovarMatriculaControle.getMatriculaPeriodoVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(matriculaRSVO.getTurmaRSVOs().get(0).getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			getFacadeFactory().getTurmaFacade().processarDadosPermitinentesTurmaSelecionada(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), configuracaoFinanceiroVO, null);
			matriculaRSVO.getCondicaoPagamentoRSVOs().addAll(consultarCondicaoPagamentoPlanoFinanceiroCurso(configuracaoFinanceiroVO));
			matriculaRSVO.setCodigoUnidadeEnsino(codigoUnidadeEnsino);
			matriculaRSVO.setCodigoTurno(codigoTurno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matriculaRSVO;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/matricularAluno")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MatriculaRSVO realizarMatriculaOnlineExterna(final MatriculaRSVO matriculaRSVO) {
		try {
			consultarConfiguracaoGeralSistemaUtilizar(matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo());
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getConfiguracaoGeralSistemaVO().getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
			renovarMatriculaControle = new RenovarMatriculaControle(true);
			renovarMatriculaControle.setMatriculaVO(new MatriculaVO());
			renovarMatriculaControle.setMatriculaPeriodoVO(new MatriculaPeriodoVO());
			renovarMatriculaControle.setRealizandoNovaMatriculaAluno(true);
			renovarMatriculaControle.setBanner(matriculaRSVO.getCodigoBanner());
			renovarMatriculaControle.getMatriculaVO().setMatriculaOnlineExterna(true);
			renovarMatriculaControle.getMatriculaVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			if(!Uteis.isAtributoPreenchido(matriculaRSVO.getCursoObject().getCodigo()) && Uteis.isAtributoPreenchido(matriculaRSVO.getCodigoBanner())){
				PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO = getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarPorChavePrimaria(matriculaRSVO.getCodigoBanner(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO, null);
				renovarMatriculaControle.getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(politicaDivulgacaoMatriculaOnlineVO.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO));
				matriculaRSVO.getCursoObject().setCodigo(renovarMatriculaControle.getMatriculaVO().getCurso().getCodigo());
			}else{
				renovarMatriculaControle.getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(matriculaRSVO.getCursoObject().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO));
			}
			renovarMatriculaControle.getMatriculaPeriodoVO().setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(matriculaRSVO.getCursoObject().getCodigo(), matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), matriculaRSVO.getTurnoRSVO().getCodigo(), usuarioVO));			
			PessoaVO pessoaVO = new PessoaVO();
			pessoaVO = incluirPessoaMatricula(matriculaRSVO.getPessoaObject(), pessoaVO, usuarioVO);
			
			renovarMatriculaControle.getMatriculaVO().setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(pessoaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			renovarMatriculaControle.getMatriculaVO().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(matriculaRSVO.getTurnoRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			renovarMatriculaControle.getMatriculaPeriodoVO().setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorCursoGradeCurricular(matriculaRSVO.getCursoObject().getCodigo(), matriculaRSVO.getCursoObject().getGradeDisciplinaObject().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO).get(0));
			renovarMatriculaControle.getMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(1);
			renovarMatriculaControle.getMatriculaPeriodoVO().setGradeCurricular(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(matriculaRSVO.getCursoObject().getGradeDisciplinaObject().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			renovarMatriculaControle.getMatriculaVO().setGradeCurricularAtual(renovarMatriculaControle.getMatriculaPeriodoVO().getGradeCurricular());
			renovarMatriculaControle.getMatriculaPeriodoVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(matriculaRSVO.getTurmaRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), usuarioVO);
			if (!renovarMatriculaControle.getMatriculaVO().getTipoMatricula().equalsIgnoreCase("EX")) {
				getFacadeFactory().getMatriculaFacade().verificaAlunoJaMatriculado(renovarMatriculaControle.getMatriculaVO(), false, configuracaoFinanceiroVO, usuarioVO,false, false);
			} 
			getFacadeFactory().getTurmaFacade().processarDadosPermitinentesTurmaSelecionada(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), configuracaoFinanceiroVO, usuarioVO);
			renovarMatriculaControle.getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(matriculaRSVO.getCondicaoPagamentoRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), usuarioVO);
			renovarMatriculaControle.getMatriculaPeriodoVO().setUnidadeEnsinoCurso(renovarMatriculaControle.getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getCodigo());
			renovarMatriculaControle.setProcessoCalendarioMatriculaVO(getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorChavePrimaria(renovarMatriculaControle.getMatriculaVO().getCurso().getCodigo(), renovarMatriculaControle.getMatriculaVO().getTurno().getCodigo(), matriculaRSVO.getProcessoMatriculaRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			renovarMatriculaControle.getMatriculaPeriodoVO().setProcessoMatriculaVO(getFacadeFactory().getProcessoMatriculaFacade().consultarPorChavePrimaria(matriculaRSVO.getProcessoMatriculaRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			renovarMatriculaControle.getMatriculaPeriodoVO().setProcessoMatricula(renovarMatriculaControle.getMatriculaPeriodoVO().getProcessoMatriculaVO().getCodigo());
			renovarMatriculaControle.getMatriculaPeriodoVO().setProcessoMatriculaCalendarioVO(renovarMatriculaControle.getProcessoCalendarioMatriculaVO());
			getFacadeFactory().getMatriculaFacade().adicionarObjMatriculaPeriodoVOs(renovarMatriculaControle.getMatriculaPeriodoVO(), renovarMatriculaControle.getMatriculaVO());
			renovarMatriculaControle.getMatriculaVO().getPlanoFinanceiroAluno().setResponsavel(usuarioVO);
			renovarMatriculaControle.getMatriculaVO().setUsuario(usuarioVO);
//			getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosAnoSemestreMatricula(renovarMatriculaControle.getMatriculaPeriodoVO(), renovarMatriculaControle.getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), false);
			getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosDefinirDisciplinasMatriculaPeriodo(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), usuarioVO, null, false, false);
			getFacadeFactory().getMatriculaPeriodoFacade().inicializarPlanoFinanceiroAlunoMatriculaPeriodo(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), renovarMatriculaControle.getMatriculaVO().getPlanoFinanceiroAluno(), true, usuarioVO);
			getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), usuarioVO);
			getFacadeFactory().getMatriculaFacade().calcularTotalDesconto(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), renovarMatriculaControle.getMatriculaVO().getPlanoFinanceiroAluno().obterOrdemAplicacaoDescontosPadraoAtual(), usuarioVO, configuracaoFinanceiroVO);
			getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(renovarMatriculaControle.getMatriculaVO(), usuarioVO);
			getFacadeFactory().getMatriculaFacade().incluir(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), renovarMatriculaControle.getProcessoCalendarioMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso(), configuracaoFinanceiroVO, getConfiguracaoGeralSistemaVO(), true, false, false, true,  usuarioVO);
			matriculaRSVO.setMatricula(renovarMatriculaControle.getMatriculaVO().getMatricula());
			matriculaRSVO.setCodigoMatriculaPeriodo(renovarMatriculaControle.getMatriculaPeriodoVO().getCodigo());
			matriculaRSVO.getPeriodoLetivoRSVO().setCodigo(renovarMatriculaControle.getMatriculaPeriodoVO().getPeridoLetivo().getCodigo());
			DocumentacaoMatriculaRelControle documentacaoMatriculaRelControle = new DocumentacaoMatriculaRelControle();
			documentacaoMatriculaRelControle.imprimirComprovanteMatriculaPDFMatriculaExterna(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaVO().getCurso().getNivelEducacional(), renovarMatriculaControle.getMatriculaPeriodoVO().getCodigo(), renovarMatriculaControle.getTipoLayoutComprovanteMatricula(), renovarMatriculaControle.getMatriculaVO().getUnidadeEnsino().getNome(), getConfiguracaoGeralSistemaVO(), configuracaoFinanceiroVO, usuarioVO);
			if(!getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao().endsWith("/")){
				getConfiguracaoGeralSistemaVO().setUrlAcessoExternoAplicacao(getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao()+"/");
			}
			matriculaRSVO.setLinkDownloadComprovante(getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao()+"relatorio/"+documentacaoMatriculaRelControle.getCaminhoRelatorio());
			matriculaRSVO.setLinkDownloadContrato(getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao()+"relatorio/"+ documentacaoMatriculaRelControle.gerarContratoPDFMatriculaExterna(renovarMatriculaControle.getMatriculaVO(),renovarMatriculaControle.getMatriculaPeriodoVO(), configuracaoGeralSistemaVO, configuracaoFinanceiroVO, usuarioVO, matriculaRSVO));
			matriculaRSVO.setAno(renovarMatriculaControle.getMatriculaPeriodoVO().getAno());
			matriculaRSVO.setSemestre(renovarMatriculaControle.getMatriculaPeriodoVO().getSemestre());
			matriculaRSVO.setAssinarDigitalmenteContrato(Uteis.isAtributoPreenchido(matriculaRSVO.getMensagemErroContrato()) ? false : renovarMatriculaControle.getMatriculaPeriodoVO().getContratoMatricula().getAssinarDigitalmenteTextoPadrao());
			matriculaRSVO.setMatriculaRealizadaComSucesso(true);
			return matriculaRSVO;
		} catch (Exception e) {
			e.printStackTrace();
			matriculaRSVO.setMatriculaRealizadaComSucesso(false);
			matriculaRSVO.setMensagem(e.getMessage());
			return matriculaRSVO;
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/realizarPagamentoCartaoCredito")
	public NegociacaoRecebimentoRSVO realizarPagamentoCartaoCredito(final NegociacaoRecebimentoRSVO negociacaoRecebimentoRSVO, @Context final SecurityContext security) {
		try {
			consultarConfiguracaoGeralSistemaUtilizar(negociacaoRecebimentoRSVO.getCodigoUnidadeEnsino());
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getConfiguracaoGeralSistemaVO().getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, negociacaoRecebimentoRSVO.getCodigoUnidadeEnsino(), usuarioVO);
			MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(negociacaoRecebimentoRSVO.getMatricula(), negociacaoRecebimentoRSVO.getCodigoUnidadeEnsino(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO);
			UsuarioVO usuarioMatriculaVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(matriculaVO.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
			NegociacaoRecebimentoVO negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
			negociacaoRecebimentoVO.setMatricula(negociacaoRecebimentoRSVO.getMatricula());
			negociacaoRecebimentoVO.setTipoPessoa(TipoPessoa.ALUNO.getValor());
			negociacaoRecebimentoVO.setPessoa(matriculaVO.getAluno());
			negociacaoRecebimentoVO.setValorTotalRecebimento(negociacaoRecebimentoRSVO.getValorTotalAPagar());
			negociacaoRecebimentoVO.setConfiguracaoRecebimentoCartaoOnlineVO(getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(0, matriculaVO.getCurso().getCodigo(), "", negociacaoRecebimentoRSVO.getCodigoUnidadeEnsino(),Uteis.NIVELMONTARDADOS_TODOS, usuarioMatriculaVO));
			getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().validarSeExisteConfiguracaoCartaoRecebimento(negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO());
			List<ContaReceberVO> contaReceberVOs = new ArrayList<>();
			contaReceberVOs.addAll(getFacadeFactory().getContaReceberFacade().consultaCompletaPorMatriculaPeriodoValindadoOrigemPorConfiguracaoRecebimentoCartaoOnline(negociacaoRecebimentoRSVO.getCodigoMatriculaPeriodo(), configuracaoFinanceiroVO, negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO(), usuarioVO));									
			for (ContaReceberVO obj : contaReceberVOs) {
				ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
				contaReceberNegociacaoRecebimentoVO.setContaReceber(obj);
				contaReceberNegociacaoRecebimentoVO.setValorTotal(obj.getCalcularValorFinal(configuracaoFinanceiroVO, usuarioVO));
				negociacaoRecebimentoVO.adicionarObjContaReceberNegociacaoRecebimentoVOs(contaReceberNegociacaoRecebimentoVO);
				negociacaoRecebimentoVO.setValorTotal(Uteis.arrendondarForcando2CadasDecimais(negociacaoRecebimentoVO.getValorTotal() + obj.getValorReceberCalculado()));
			}
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = null;
			for (FormaPagamentoMatriculaOnlineExternaRSVO obj : negociacaoRecebimentoRSVO.getFormaPagamentoMatriculaOnlineExternaRSVOs()) {
				formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
				formaPagamentoNegociacaoRecebimentoVO.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorTipo(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
				formaPagamentoNegociacaoRecebimentoVO.setQtdeParcelasCartaoCredito(negociacaoRecebimentoRSVO.getQuantidadeParcelasAPagar());
				formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO()
					.setTipoFinanciamentoEnum(negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO()
						.getTipoFinanciamentoPermitido(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento(),
								negociacaoRecebimentoVO.realizarCalculoMaiorDataVencimento(), usuarioVO, negociacaoRecebimentoVO.getListaTipoOrigemContaReceber()));
				formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNomeCartaoCredito(obj.getNomeNoCartao());
				formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroCartao(obj.getNumeroCartao());
				formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setMesValidade(obj.getMesValidade());
				formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setAnoValidade(obj.getAnoValidade());
				formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigoVerificacao(obj.getCodigoDeVerificacao());
				formaPagamentoNegociacaoRecebimentoVO.setValorRecebimento(obj.getValor());
				formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorParcela(obj.getValor());
				formaPagamentoNegociacaoRecebimentoVO.setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(obj.getCodigoConfiguracaoFinanceiroCartao(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
				formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoFinanceiroCartaoVO(formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO());
				negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().add(formaPagamentoNegociacaoRecebimentoVO);
			}
			getFacadeFactory().getNegociacaoRecebimentoFacade().realizarRecebimentoCartaoCreditoMatriculaRenovacaoOnline(negociacaoRecebimentoVO, negociacaoRecebimentoVO.getMatricula(), configuracaoFinanceiroVO, usuarioVO);
			//renovarMatriculaControle.setNegociacaoRecebimentoVO(negociacaoRecebimentoVO);
			imprimirComprovanteRecebimento(negociacaoRecebimentoRSVO, negociacaoRecebimentoVO, matriculaVO, usuarioMatriculaVO);
			negociacaoRecebimentoRSVO.setPagamentoConfirmado(true);
			return negociacaoRecebimentoRSVO;
		} catch (Exception e) {
			e.printStackTrace();
			negociacaoRecebimentoRSVO.setMensagem(e.getMessage());
			negociacaoRecebimentoRSVO.setPagamentoConfirmado(false);
			return negociacaoRecebimentoRSVO;
		}
	}
	
	public void imprimirComprovanteRecebimento(NegociacaoRecebimentoRSVO negociacaoRecebimentoRSVO, NegociacaoRecebimentoVO negociacaoRecebimentoVO, MatriculaVO matricula, UsuarioVO usuario) {
        	    try {
        		comprovanteRecebimentoRelControle = new ComprovanteRecebimentoRelControle();
        		/*comprovanteRecebimentoRelControle = (ComprovanteRecebimentoRelControle) context().getExternalContext().getSessionMap().get(ComprovanteRecebimentoRelControle.class.getSimpleName());
        		if (comprovanteRecebimentoRelControle == null) {
        			comprovanteRecebimentoRelControle = new ComprovanteRecebimentoRelControle();
        			context().getExternalContext().getSessionMap().put(ComprovanteRecebimentoRelControle.class.getSimpleName(), comprovanteRecebimentoRelControle);
        		}*/
        		if (!negociacaoRecebimentoVO.getCodigo().equals(0)) {
        			comprovanteRecebimentoRelControle.setNegociacaoRecebimentoVO((NegociacaoRecebimentoVO) negociacaoRecebimentoVO.clone());
        			comprovanteRecebimentoRelControle.imprimirPDFRecebimentoCartaoCredito(matricula, usuario);
    				if(!getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao().endsWith("/")){
    					getConfiguracaoGeralSistemaVO().setUrlAcessoExternoAplicacao(getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao()+"/");
    				}
    				negociacaoRecebimentoRSVO.setLinkDownloadComprovantePagamento(getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao()+"relatorio/"+comprovanteRecebimentoRelControle.getCaminhoRelatorio());
        		} else {
        			setMensagemID("msg_relatorio_sem_dados");
        		}
        	} catch (Exception e) {
        		setMensagemDetalhada("msg_erro", e.getMessage());
        	}finally{
        		comprovanteRecebimentoRelControle = null;
        	}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/consultarContaReceberAlunoNovaMatricula/{matricula}/{codigoMatriculaPeriodo}/{codigoUnidadeEnsino}")
	public NegociacaoRecebimentoRSVO consultarContaReceberAlunoNovaMatricula(@PathParam("matricula") String matricula, @PathParam("codigoMatriculaPeriodo") Integer codigoMatriculaPeriodo, @PathParam("codigoUnidadeEnsino") Integer codigoUnidadeEnsino) {
		NegociacaoRecebimentoRSVO negociacaoRecebimentoRSVO = new NegociacaoRecebimentoRSVO();
		try {
			List<ContaReceberVO> contaReceberVOs = new ArrayList<>();
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getConfiguracaoGeralSistemaVO().getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, codigoUnidadeEnsino, usuarioVO);
			MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, codigoUnidadeEnsino, Uteis.NIVELMONTARDADOS_COMBOBOX, configuracaoFinanceiroVO, usuarioVO);
			ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO = getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(0, matriculaVO.getCurso().getCodigo(), "", matriculaVO.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
//			getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().validarSeExisteConfiguracaoCartaoRecebimento(configuracaoRecebimentoCartaoOnlineVO);
			if (Uteis.isAtributoPreenchido(configuracaoRecebimentoCartaoOnlineVO)) {
				contaReceberVOs.addAll(getFacadeFactory().getContaReceberFacade().consultaCompletaPorMatriculaPeriodoValindadoOrigemPorConfiguracaoRecebimentoCartaoOnline(codigoMatriculaPeriodo, configuracaoFinanceiroVO, configuracaoRecebimentoCartaoOnlineVO, usuarioVO));
				Date dataBase = new Date();
				int count = 0;
				for (ContaReceberVO contaReceberVO : contaReceberVOs) {
					count++;
					contaReceberVO.setRealizandoRecebimento(true);
					negociacaoRecebimentoRSVO.setValorTotalAPagar(Uteis.arrendondarForcando2CadasDecimais(negociacaoRecebimentoRSVO.getValorTotalAPagar() + contaReceberVO.getCalcularValorFinal(dataBase, configuracaoFinanceiroVO, false, dataBase, usuarioVO)));
				}
				negociacaoRecebimentoRSVO.setQuantidadeParcelasAPagar(count);
				if (configuracaoRecebimentoCartaoOnlineVO.getHabilitarRecebimentoMatriculaOnline()) {
					negociacaoRecebimentoRSVO.getBandeiraRSVOs().addAll(getFacadeFactory().getNegociacaoRecebimentoFacade().consultarBandeirasDisponiveisPagamentoOnline(configuracaoRecebimentoCartaoOnlineVO, matriculaVO.getUnidadeEnsino(), negociacaoRecebimentoRSVO.getValorTotalAPagar()));
				}
				if(contaReceberVOs.size() == 1) {
					negociacaoRecebimentoRSVO.setContaReceber(contaReceberVOs.get(0));
				}
			}
			negociacaoRecebimentoRSVO.setCodigoMatriculaPeriodo(codigoMatriculaPeriodo);
			negociacaoRecebimentoRSVO.setCodigoUnidadeEnsino(codigoUnidadeEnsino);
			negociacaoRecebimentoRSVO.setMatricula(matricula);			
			MatriculaRSVO matriculaRSVO =  new MatriculaRSVO();
			matriculaRSVO.setCodigoMatriculaPeriodo(codigoMatriculaPeriodo);
			matriculaRSVO.setCodigoUnidadeEnsino(codigoUnidadeEnsino);
			matriculaRSVO.setMatricula(matricula);
			negociacaoRecebimentoRSVO.setMatriculaRSVO(matriculaRSVO);
		} catch (Exception e) {
			negociacaoRecebimentoRSVO.setMensagem(e.getMessage());
			negociacaoRecebimentoRSVO.setPagamentoConfirmado(false);
			e.printStackTrace();
		}
		return negociacaoRecebimentoRSVO;
	}
		
	@GET
	@Produces({ "application/json" })
	@Path("/atualizarDadosQuandoTurmaAlterado/{codigoUnidadeEnsino}/{codigoCurso}/{codigoTurno}/{codigoGradeCurricular}/{codigoBanner}/{codigoPessoa}/{codigoTurma}")
	public MatriculaRSVO atualizarDadosQuandoTurmaAlterado(@PathParam("codigoUnidadeEnsino") Integer codigoUnidadeEnsino, @PathParam("codigoCurso") Integer codigoCurso, @PathParam("codigoTurno") Integer codigoTurno, @PathParam("codigoGradeCurricular") Integer codigoGradeCurricular, @PathParam("codigoBanner") Integer codigoBanner, @PathParam("codigoTurma") Integer codigoTurma) {
		MatriculaRSVO matriculaRSVO = new MatriculaRSVO();
		try {
			renovarMatriculaControle = new RenovarMatriculaControle(true);
			renovarMatriculaControle.setMatriculaVO(new MatriculaVO());
			renovarMatriculaControle.setMatriculaPeriodoVO(new MatriculaPeriodoVO());
			renovarMatriculaControle.setRealizandoNovaMatriculaAluno(true);
			renovarMatriculaControle.setBanner(codigoBanner);
			renovarMatriculaControle.getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(codigoCurso, Uteis.NIVELMONTARDADOS_TODOS, false, null));
			matriculaRSVO.setCursoObject(getFacadeFactory().getCursoFacade().consultarPorChavePrimariaMatriculaExterna(codigoCurso));
			matriculaRSVO.getUnidadeEnsinoRSVOs().addAll(consultarUnidadeEnsinoPorCondigoBanner(codigoBanner));
			matriculaRSVO.getTurnoRSVOs().addAll(consultarTurnoPorCondigoBannerUnidadeEnsino(codigoBanner, codigoUnidadeEnsino));
			renovarMatriculaControle.getMatriculaVO().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(codigoTurno, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			matriculaRSVO.getProcessoMatriculaRSVOs().addAll(consultarProcessoMatriculaPorCondigoBanner(codigoBanner, codigoUnidadeEnsino));
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, codigoUnidadeEnsino, null);
			matriculaRSVO.getTurmaRSVOs().addAll(consultarTurmaPorPeriodoLetivoUnidadeEnsinoCursoTurno(codigoUnidadeEnsino, codigoCurso, codigoTurno, codigoGradeCurricular, configuracaoFinanceiroVO));
			renovarMatriculaControle.getMatriculaPeriodoVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(codigoTurma, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			getFacadeFactory().getTurmaFacade().processarDadosPermitinentesTurmaSelecionada(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), configuracaoFinanceiroVO, null);
			matriculaRSVO.getCondicaoPagamentoRSVOs().addAll(consultarCondicaoPagamentoPlanoFinanceiroCurso(configuracaoFinanceiroVO));
			matriculaRSVO.setCodigoUnidadeEnsino(codigoUnidadeEnsino);
			matriculaRSVO.setCodigoTurno(codigoTurno);
			matriculaRSVO.setCodigoTurma(codigoTurma);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matriculaRSVO;
	}
	
	@GET
	@Produces({ "application/json" })
	@Path("/atualizarDadosQuandoUnidadeEnsinoAlterado/{codigoCurso}/{codigoBanner}/{codigoUnidadeEnsino}")
	public MatriculaRSVO atualizarDadosQuandoUnidadeEnsinoAlterado(@PathParam("codigoCurso") Integer codigoCurso, @PathParam("codigoBanner") Integer codigoBanner, @PathParam("codigoUnidadeEnsino") Integer codigoUnidadeEnsino) {
		MatriculaRSVO matriculaRSVO = new MatriculaRSVO();
		try {
			renovarMatriculaControle = new RenovarMatriculaControle(true);
			renovarMatriculaControle.setMatriculaVO(new MatriculaVO());
			renovarMatriculaControle.setMatriculaPeriodoVO(new MatriculaPeriodoVO());
			renovarMatriculaControle.setRealizandoNovaMatriculaAluno(true);
			renovarMatriculaControle.setBanner(codigoBanner);
			renovarMatriculaControle.getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(codigoCurso, Uteis.NIVELMONTARDADOS_TODOS, false, null));
			matriculaRSVO.setCursoObject(getFacadeFactory().getCursoFacade().consultarPorChavePrimariaMatriculaExterna(codigoCurso));
			matriculaRSVO.getUnidadeEnsinoRSVOs().addAll(consultarUnidadeEnsinoPorCondigoBanner(codigoBanner));
			matriculaRSVO.getTurnoRSVOs().addAll(consultarTurnoPorCondigoBannerUnidadeEnsino(codigoBanner, codigoUnidadeEnsino));
			renovarMatriculaControle.getMatriculaVO().setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(!matriculaRSVO.getTurnoRSVOs().isEmpty() ? matriculaRSVO.getTurnoRSVOs().get(0).getCodigo() : 0, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			matriculaRSVO.getProcessoMatriculaRSVOs().addAll(consultarProcessoMatriculaPorCondigoBanner(codigoBanner, codigoUnidadeEnsino));
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, codigoUnidadeEnsino, null);
			matriculaRSVO.getTurmaRSVOs().addAll(consultarTurmaPorPeriodoLetivoUnidadeEnsinoCursoTurno(codigoUnidadeEnsino, matriculaRSVO.getCursoObject().getCodigo(), matriculaRSVO.getTurnoRSVOs().get(0).getCodigo(), matriculaRSVO.getCursoObject().getGradeDisciplinaObject().getCodigo(), configuracaoFinanceiroVO));
			renovarMatriculaControle.getMatriculaPeriodoVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(!matriculaRSVO.getTurmaRSVOs().isEmpty() ? matriculaRSVO.getTurmaRSVOs().get(0).getCodigo() : 0, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			getFacadeFactory().getTurmaFacade().processarDadosPermitinentesTurmaSelecionada(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), configuracaoFinanceiroVO, null);
			matriculaRSVO.getCondicaoPagamentoRSVOs().addAll(consultarCondicaoPagamentoPlanoFinanceiroCurso(configuracaoFinanceiroVO));
			matriculaRSVO.setCodigoUnidadeEnsino(codigoUnidadeEnsino);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matriculaRSVO;
	}
	
	public PessoaVO incluirPessoaMatricula(PessoaObject pessoaObject, PessoaVO pessoaVO, UsuarioVO usuario) throws Exception {
		pessoaVO = getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorCPFUnico(pessoaObject.getCpf(), false, false, false, usuario);
		if(pessoaVO.getCodigo().equals(0)) {
			pessoaVO.setNome(pessoaObject.getNome());
			pessoaVO.setEmail(pessoaObject.getEmail());
			pessoaVO.setTelefoneRes(pessoaObject.getTelefoneResidencial());
			pessoaVO.setCelular(pessoaObject.getCelular());
			pessoaVO.setCPF(pessoaObject.getCpf());
			pessoaVO.setRG(pessoaObject.getRg());
			pessoaVO.setCEP(pessoaObject.getCep());
			pessoaVO.setSetor(pessoaObject.getSetor());
			pessoaVO.setComplemento(pessoaObject.getComplemento());
			pessoaVO.getCidade().setCodigo(pessoaObject.getCidade().getCodigo());
			pessoaVO.getCidade().setEstado(pessoaObject.getCidade().getEstado());
			pessoaVO.setEndereco(pessoaObject.getEndereco());
			pessoaVO.setNumero(pessoaObject.getNumero());
			getFacadeFactory().getPessoaFacade().incluirPessoaProspectMatriculaCRM(pessoaVO, usuario, getConfiguracaoGeralSistemaVO(), false);
			getFacadeFactory().getPessoaFacade().realizarVinculoPessoaProspect(pessoaVO, usuario);
			return pessoaVO;
		} else {	
			if(Uteis.isAtributoPreenchido(pessoaObject.getEmail())){
				pessoaVO.setEmail(pessoaObject.getEmail());
			}
			if(Uteis.isAtributoPreenchido(pessoaObject.getTelefoneResidencial())){
				pessoaVO.setTelefoneRes(pessoaObject.getTelefoneResidencial());
			}
			if(Uteis.isAtributoPreenchido(pessoaObject.getCelular())){
				pessoaVO.setCelular(pessoaObject.getCelular());
			}
			if(Uteis.isAtributoPreenchido(pessoaObject.getRg())){
				pessoaVO.setRG(pessoaObject.getRg());
			}
			if(Uteis.isAtributoPreenchido(pessoaObject.getEndereco()) && pessoaVO.getEndereco().trim().isEmpty()){
				pessoaVO.setEndereco(pessoaObject.getEndereco());
				pessoaVO.setCEP(pessoaObject.getCep());
				pessoaVO.setNumero(pessoaObject.getNumero());
				pessoaVO.setComplemento(pessoaObject.getComplemento());
				pessoaVO.setSetor(pessoaObject.getSetor());
				pessoaVO.getCidade().setCodigo(pessoaObject.getCidade().getCodigo());
				pessoaVO.getCidade().setEstado(pessoaObject.getCidade().getEstado());
			}
			
			getFacadeFactory().getPessoaFacade().alterarPessoaProspectMatriculaCRM(pessoaVO, usuario, getConfiguracaoGeralSistemaVO(), false);
			getFacadeFactory().getPessoaFacade().realizarVinculoPessoaProspect(pessoaVO, usuario);
			return pessoaVO;
		}
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/realizarImpressaoBoletoBancario")
	public NegociacaoRecebimentoRSVO realizarImpressaoBoletoBancario(final NegociacaoRecebimentoRSVO negociacaoRecebimentoRSVO) {
		try {
			consultarConfiguracaoGeralSistemaUtilizar(negociacaoRecebimentoRSVO.getCodigoUnidadeEnsino());
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getConfiguracaoGeralSistemaVO().getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, negociacaoRecebimentoRSVO.getMatriculaRSVO().getCodigoUnidadeEnsino(), usuarioVO);
			SuperParametroRelVO superParametroRelVO = new SuperParametroRelVO();
			List<BoletoBancarioRelVO> boletoBancarioRelVOs = getFacadeFactory().getBoletoBancarioRelFacade().emitirRelatorioLista(false, null, negociacaoRecebimentoRSVO.getMatriculaRSVO().getMatricula(), negociacaoRecebimentoRSVO.getMatriculaRSVO().getAno(), negociacaoRecebimentoRSVO.getMatriculaRSVO().getSemestre(), "", 0, 0, null, null, 0, "aluno", 0, usuarioVO, "boletoAluno", 0, configuracaoFinanceiroVO, 0, false, null, null);
			UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(negociacaoRecebimentoRSVO.getMatriculaRSVO().getUnidadeEnsinoRSVO().getCodigo(), usuarioVO);
			if (ue.getExisteLogoRelatorio()) {
				String urlLogoUnidadeEnsinoRelatorio = ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogoRelatorio();
				String urlLogo = getConfiguracaoGeralSistemaVO().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
				superParametroRelVO.getParametros().put("logoPadraoRelatorio", urlLogo);
			}
			getFacadeFactory().getBoletoBancarioRelFacade().realizarImpressaoPDF(boletoBancarioRelVOs, superParametroRelVO, getVersaoSistema(), "boleto", usuarioVO);
			RenovarMatriculaControle renovarMatriculaControle = new RenovarMatriculaControle(true);
			renovarMatriculaControle.realizarImpressaoRelatorio(superParametroRelVO);
			if(!getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao().endsWith("/")){
				getConfiguracaoGeralSistemaVO().setUrlAcessoExternoAplicacao(getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao()+"/");
			}
			negociacaoRecebimentoRSVO.setLinkDownloadBoleto(getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao()+"relatorio/"+renovarMatriculaControle.getCaminhoRelatorio());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return negociacaoRecebimentoRSVO;
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/consultarEndereco")
	public PessoaObject consultarEndereco(final PessoaObject pessoaObject) {		
				if (pessoaObject == null || pessoaObject.getCep() == null || pessoaObject.getCep().equals("") || pessoaObject.getCep().length() != 8) {		         		         
		            return pessoaObject;
		        }
		        String cep = pessoaObject.getCep();
		        cep = cep.replace(".", "");
		        cep = cep.replace("-", "");
		        List<EnderecoVO> listaEndereco;
				try {
					listaEndereco = getFacadeFactory().getEnderecoFacade().consultarPorCep(cep, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, null);
		        if (!listaEndereco.isEmpty()) {
		        	pessoaObject.setEndereco("");		        	
		            EnderecoVO enderecoVO = listaEndereco.get(0);
		            pessoaObject.setEndereco(enderecoVO.getLogradouro());
		            pessoaObject.setSetor(enderecoVO.getBairrocodigo().getDescricao());
		            pessoaObject.getCidade().setCodigo(enderecoVO.getBairrocodigo().getCidade().getCodigo());
		    		pessoaObject.getCidade().setNome(enderecoVO.getBairrocodigo().getCidade().getNome());
		    		pessoaObject.getCidade().setEstado(enderecoVO.getBairrocodigo().getCidade().getEstado());		            		            
		        }
				} catch (Exception e) {
					listaEndereco =  null;
					e.printStackTrace();
				}
		
		return pessoaObject;
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/registrarIndeferimentoContratoPorAluno")
	public Response registrarIndeferimentoContratoPorAluno(final MatriculaRSVO matricula) {
		try {
			DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = new DocumentoAssinadoPessoaVO();
			documentoAssinadoPessoaVO = getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarDocumentosAssinadoPessoaPorMatricula(matricula.getMatricula());
			documentoAssinadoPessoaVO.setMotivoRejeicao(matricula.getMotivoRecusa());
			documentoAssinadoPessoaVO.setDataRejeicao(new Date());
			documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosRejeicao(documentoAssinadoPessoaVO);
			return Response.status(Status.OK).entity(documentoAssinadoPessoaVO).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.BAD_REQUEST.name());
			errorInfoRSVO.setMensagem(e.getMessage());
			try {
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(new RegistroExecucaoJobVO("registrarIndeferimentoContratoPorAluno", matricula.getMatricula().concat("-").concat(e.getMessage()) ,new Date()));
				return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
			} catch (Exception e1) {
				return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
			}
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/registrarAssinaturaContratoPorAluno")
	public Response registrarAssinaturaContratoPorAluno (final MatriculaRSVO matricula) {
		try {
			DocumentoAssinadoVO documentoAssinadoVO = new DocumentoAssinadoVO();											
			documentoAssinadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPorAlunoTipoOrigemCodigoOrigem(matricula.getMatricula(), 0, "", TipoOrigemDocumentoAssinadoEnum.CONTRATO, false, 0, getUsuarioLogado());
			documentoAssinadoVO.getListaDocumentoAssinadoPessoa().stream().filter(documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO)).collect(Collectors.toList()).forEach(documentoAssinadoPessoaAluno-> documentoAssinadoPessoaAluno.setAssinarDocumento(Boolean.TRUE));
			String arquivoTemp = getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getLocalUploadArquivoFixo() + File.separator + documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator + documentoAssinadoVO.getArquivo().getNome(), documentoAssinadoVO.getArquivo().getNome());
			File fileAssinar = new File(arquivoTemp);
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultaRapidaPorMatriculaAluno(matricula.getMatricula(), matricula.getCodigoUnidadeEnsino(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
			if (usuarioVO != null && !Uteis.isAtributoPreenchido(usuarioVO)) {
				throw new Exception("Não foi possível assinar o documento pois não existe uma Usuário cadastrado para o aluno " + matricula.getPessoaObject().getNome() + ", entre em contato com a instituição de ensino.");
			}
			getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarContrato(documentoAssinadoVO, fileAssinar, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), usuarioVO);
			return Response.status(Status.OK).entity(documentoAssinadoVO).build();
		} catch (Exception e) {
			ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
			errorInfoRSVO.setCodigo(Status.BAD_REQUEST.name());
			errorInfoRSVO.setMensagem("Ocorreu um erro na assinatura do seu contrato, para concluir este processo acesse a plataforma educacional com seu login e senha e tente novamente.");
			errorInfoRSVO.setMensagemErro(e.getMessage());
			try {
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(new RegistroExecucaoJobVO("registrarAssinaturaContratoPorAluno", matricula.getMatricula().concat("-").concat(e.getMessage()) ,new Date()));
				return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
			} catch (Exception e1) {
				return Response.status(Status.BAD_REQUEST).entity(errorInfoRSVO).build();
			}
		}
	}
	
	
}
