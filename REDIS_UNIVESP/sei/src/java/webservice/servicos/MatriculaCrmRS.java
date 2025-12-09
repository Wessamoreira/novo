package webservice.servicos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import jobs.JobMatriculaCRM;
import negocio.comuns.academico.DisciplinaMatriculaCRMVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaCRMVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import webservice.arquitetura.InfoWSVO;
import webservice.servicos.excepetion.ErrorInfoRSVO;



@Path("/matriculaCRM")
public class MatriculaCrmRS extends SuperControleRelatorio {

	public static final long serialVersionUID = 9037131164208204886L;
	public ErrorInfoRSVO errorInfoRSVO;
	
	@GET
	@Path("/status")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response status() {
		try {
			errorInfoRSVO = new ErrorInfoRSVO(Status.OK, "WebService conectado com sucesso.");
		} catch (Exception e) {
			errorInfoRSVO = new ErrorInfoRSVO(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		} 
		return Response.status(errorInfoRSVO.getStatusCode()).entity(errorInfoRSVO).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/test")
	public Response test() throws Exception {
		try {
			/*IntegracaoMatriculaCRMVO mat  = new IntegracaoMatriculaCRMVO();
			mat.setCpf("015.628.221-60");
			IntegracaoDisciplinaMatriculaVO disc = new IntegracaoDisciplinaMatriculaVO();
			disc.setDisciplina(2393);
			disc.setNomeDisciplina("teste");
			mat.getDisciplinaMatriculaVOs().add(disc);
			disc = new IntegracaoDisciplinaMatriculaVO();
			disc.setDisciplina(2399);
			disc.setNomeDisciplina("teste 1");
			mat.getDisciplinaMatriculaVOs().add(disc);
			return Response.status(Status.OK).entity(mat).build();*/
			
			return Response.status(Status.OK).entity(" Web SER").build();
		} catch (Exception e) {
			errorInfoRSVO = new ErrorInfoRSVO(Status.UNAUTHORIZED, e.getMessage());
			return Response.status(errorInfoRSVO.getStatusCode()).entity(errorInfoRSVO).build();
		}
	}
	

	@GET
	@Path("/obterXMLMatriculaCRM")
	@Produces("application/xml")
	public IntegracaoMatriculaCRMVO getMatricula() {
		MatriculaCRMVO matCRM = null;
		try {
			matCRM = getFacadeFactory().getMatriculaCRMFacade().consultarAlunoASerMatriculadoPorCodigoPessoa(421453, Boolean.FALSE, null);
		} catch (Exception e) {
			
		}
		IntegracaoMatriculaCRMVO mat = new IntegracaoMatriculaCRMVO();
		mat.setCodigoUnidade(matCRM.getUnidadeEnsino().getCodigo());
		mat.setNomeUnidade(matCRM.getUnidadeEnsino().getNome());
		IntegracaoPessoaVO intPessoa = new IntegracaoPessoaVO();
		intPessoa.setNome(matCRM.getAluno().getNome());
		intPessoa.setEndereco(matCRM.getAluno().getEndereco());
		intPessoa.setSetor(matCRM.getAluno().getSetor());
		intPessoa.setNumero(matCRM.getAluno().getNumero());
		intPessoa.setCEP(matCRM.getAluno().getCEP());
		intPessoa.setComplemento(matCRM.getAluno().getComplemento());
		intPessoa.setSexo(matCRM.getAluno().getSexo());
		intPessoa.setEstadoCivil(matCRM.getAluno().getEstadoCivil());
		intPessoa.setTelefoneComer(matCRM.getAluno().getTelefoneComer());
		intPessoa.setTelefoneRes(matCRM.getAluno().getTelefoneRes());
		intPessoa.setTelefoneRecado(matCRM.getAluno().getTelefoneRecado());
		intPessoa.setCelular(matCRM.getAluno().getCelular());
		intPessoa.setEmail(matCRM.getAluno().getEmail());
		intPessoa.setEmail2(matCRM.getAluno().getEmail2());
		intPessoa.setPaginaPessoal(matCRM.getAluno().getPaginaPessoal());
		intPessoa.setDataNasc(matCRM.getAluno().getDataNasc().toString());
		intPessoa.setCPF(matCRM.getAluno().getCPF());
		intPessoa.setRG(matCRM.getAluno().getRG());
		intPessoa.setCertificadoMilitar(matCRM.getAluno().getCertificadoMilitar());
		intPessoa.setPispasep(matCRM.getAluno().getPispasep());
		intPessoa.setDataEmissaoRG(matCRM.getAluno().getDataEmissaoRG().toString());
		intPessoa.setEstadoEmissaoRG(matCRM.getAluno().getEstadoEmissaoRG());
		intPessoa.setOrgaoEmissor(matCRM.getAluno().getOrgaoEmissor());
		intPessoa.setTituloEleitoral(matCRM.getAluno().getTituloEleitoral());
		intPessoa.setNecessidadesEspeciais(matCRM.getAluno().getNecessidadesEspeciais());
		//intPessoa.setCodigoCidade(matCRM.getAluno().getCidade().getCodigo());
		intPessoa.setNomeCidade(matCRM.getAluno().getCidade().getNome());
		intPessoa.setCodigoIBGECidade(matCRM.getAluno().getCidade().getCodigoIBGE());
		intPessoa.setSiglaEstado(matCRM.getAluno().getCidade().getEstado().getSigla());
		intPessoa.setCodigoIBGENaturalidade(matCRM.getAluno().getNaturalidade().getCodigoIBGE());
		intPessoa.setNomeNaturalidade(matCRM.getAluno().getNaturalidade().getNome());
		
		Iterator i = matCRM.getAluno().getFormacaoAcademicaVOs().iterator();
		while (i.hasNext()) {
			FormacaoAcademicaVO f = (FormacaoAcademicaVO)i.next();
			IntegracaoFormacaoAcademicaVO intForm = new IntegracaoFormacaoAcademicaVO();
			intForm.setInstituicao(f.getInstituicao());
			intForm.setEscolaridade(f.getEscolaridade());
			intForm.setCurso(f.getCurso());
			intForm.setSituacao(f.getSituacao());
			intForm.setTipoInst(f.getTipoInst());
			intForm.setDataInicio(f.getDataInicio().toString());
			intForm.setDataFim(f.getDataFim().toString());
			intForm.setAnoDataFim(f.getAnoDataFim());
			intForm.setCodigoCidadeIBGE(f.getCidade().getCodigoIBGE());
			//intForm.setCodigoCidade(f.getCidade().getCodigo());
			intForm.setNomeCidade(f.getCidade().getNome());
			intForm.setDescricaoAreaConhecimento(f.getDescricaoAreaConhecimento());
			intPessoa.getFormacaoAcademicaVOs().add(intForm);
		}	
		Iterator l = matCRM.getAluno().getFiliacaoVOs().iterator();
		while (l.hasNext()) {
			FiliacaoVO f = (FiliacaoVO)l.next();
			IntegracaoFiliacaoVO intFil = new IntegracaoFiliacaoVO();
			intFil.setCelular(f.getPais().getCelular());
			intFil.setCEP(f.getPais().getCEP());
			//intFil.setCidade(f.getPais().getCidade().getCodigo());
			intFil.setCodigoCidadeIBGE(f.getPais().getCidade().getCodigoIBGE());
			intFil.setComplemento(f.getPais().getComplemento());
			intFil.setCPF(f.getPais().getCPF());
			if (f.getPais().getDataNasc() != null) {
				intFil.setDataNasc(f.getPais().getDataNasc().toString());
			}
			intFil.setEndereco(f.getPais().getEndereco());
			intFil.setNome(f.getPais().getNome());
			intFil.setRG(f.getPais().getRG());
			intFil.setSetor(f.getPais().getSetor());
			intFil.setTelComercial(f.getPais().getTelefoneComer());
			intFil.setTelRecado(f.getPais().getTelefoneRecado());
			intFil.setTelResidencial(f.getPais().getTelefoneRes());
			intFil.setCelular(f.getPais().getCelular());
			intFil.setTipo(f.getTipo());
			intPessoa.getFiliacaoVOs().add(intFil);
		}		
		List lista = new ArrayList();
		try {
			lista = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurma(matCRM.getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
		} catch (Exception e) {			
		}
		Iterator k = lista.iterator();
		while (k.hasNext()) {
			TurmaDisciplinaVO f = (TurmaDisciplinaVO)k.next();
			IntegracaoDisciplinaMatriculaVO intForm = new IntegracaoDisciplinaMatriculaVO();
			intForm.setDisciplina(f.getDisciplina().getCodigo());
			intForm.setNomeDisciplina(f.getDisciplina().getNome());
			mat.getDisciplinaMatriculaVOs().add(intForm);
		}		
		mat.setPessoa(intPessoa);
		mat.setCodigoTurma(matCRM.getTurma().getCodigo());
		mat.setNomeTurma(matCRM.getTurma().getIdentificadorTurma());
		mat.setCodigoConsultor(matCRM.getConsultor().getCodigo());
		mat.setNomeConsultor(matCRM.getConsultor().getPessoa().getNome());
		mat.setCodigoProcessoMatricula(matCRM.getProcessoMatricula().getCodigo());
		mat.setDescricaoProcessoMatricula(matCRM.getProcessoMatricula().getDescricao());		
		mat.setCodigoPlanoFinanceiroCurso(matCRM.getPlanoFinanceiroCurso().getCodigo());
		mat.setDescricaoPlanoFinanceiroCurso(matCRM.getPlanoFinanceiroCurso().getDescricao());
		mat.setCodigoCondicaoPagamentoPlanoFinanceiroCurso(matCRM.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo());
		mat.setDescricaoCondicaoPagamentoPlanoFinanceiroCurso(matCRM.getCondicaoPagamentoPlanoFinanceiroCurso().getDescricao());
		return mat;
	}

	@GET
	@Path("/obterXMLPessoaCRM")
	@Produces("application/xml")
	public IntegracaoPessoaVO getPessoa() {
		MatriculaCRMVO matCRM = null;
		try {
			matCRM = getFacadeFactory().getMatriculaCRMFacade().consultarAlunoASerMatriculadoPorCodigoPessoa(412504, Boolean.FALSE, null);
		} catch (Exception e) {
			
		}
		IntegracaoPessoaVO intPessoa = new IntegracaoPessoaVO();
		if (matCRM == null) {
			return new IntegracaoPessoaVO();
		}
		intPessoa.setNome(matCRM.getAluno().getNome());
		intPessoa.setEndereco(matCRM.getAluno().getEndereco());
		intPessoa.setSetor(matCRM.getAluno().getSetor());
		intPessoa.setNumero(matCRM.getAluno().getNumero());
		intPessoa.setCEP(matCRM.getAluno().getCEP());
		intPessoa.setComplemento(matCRM.getAluno().getComplemento());
		intPessoa.setSexo(matCRM.getAluno().getSexo());
		intPessoa.setEstadoCivil(matCRM.getAluno().getEstadoCivil());
		intPessoa.setTelefoneComer(matCRM.getAluno().getTelefoneComer());
		intPessoa.setTelefoneRes(matCRM.getAluno().getTelefoneRes());
		intPessoa.setTelefoneRecado(matCRM.getAluno().getTelefoneRecado());
		intPessoa.setCelular(matCRM.getAluno().getCelular());
		intPessoa.setEmail(matCRM.getAluno().getEmail());
		intPessoa.setEmail2(matCRM.getAluno().getEmail2());
		intPessoa.setPaginaPessoal(matCRM.getAluno().getPaginaPessoal());
		intPessoa.setDataNasc(matCRM.getAluno().getDataNasc().toString());
		intPessoa.setCPF(matCRM.getAluno().getCPF());
		intPessoa.setRG(matCRM.getAluno().getRG());
		intPessoa.setCertificadoMilitar(matCRM.getAluno().getCertificadoMilitar());
		intPessoa.setPispasep(matCRM.getAluno().getPispasep());
		intPessoa.setDataEmissaoRG(matCRM.getAluno().getDataEmissaoRG().toString());
		intPessoa.setEstadoEmissaoRG(matCRM.getAluno().getEstadoEmissaoRG());
		intPessoa.setOrgaoEmissor(matCRM.getAluno().getOrgaoEmissor());
		intPessoa.setTituloEleitoral(matCRM.getAluno().getTituloEleitoral());
		intPessoa.setNecessidadesEspeciais(matCRM.getAluno().getNecessidadesEspeciais());
		//intPessoa.setCodigoCidade(matCRM.getAluno().getCidade().getCodigo());
		intPessoa.setNomeCidade(matCRM.getAluno().getCidade().getNome());
		intPessoa.setCodigoIBGECidade(matCRM.getAluno().getCidade().getCodigoIBGE());
		intPessoa.setSiglaEstado(matCRM.getAluno().getCidade().getEstado().getSigla());
		intPessoa.setCodigoIBGENaturalidade(matCRM.getAluno().getNaturalidade().getCodigoIBGE());
		intPessoa.setNomeNaturalidade(matCRM.getAluno().getNaturalidade().getNome());
		
		Iterator i = matCRM.getAluno().getFormacaoAcademicaVOs().iterator();
		while (i.hasNext()) {
			FormacaoAcademicaVO f = (FormacaoAcademicaVO)i.next();
			IntegracaoFormacaoAcademicaVO intForm = new IntegracaoFormacaoAcademicaVO();
			intForm.setInstituicao(f.getInstituicao());
			intForm.setEscolaridade(f.getEscolaridade());
			intForm.setCurso(f.getCurso());
			intForm.setSituacao(f.getSituacao());
			intForm.setTipoInst(f.getTipoInst());
			intForm.setDataInicio(f.getDataInicio().toString());
			intForm.setDataFim(f.getDataFim().toString());
			intForm.setAnoDataFim(f.getAnoDataFim());
			//intForm.setCodigoCidade(f.getCidade().getCodigo());
			intForm.setNomeCidade(f.getCidade().getNome());
			intForm.setDescricaoAreaConhecimento(f.getDescricaoAreaConhecimento());
			intPessoa.getFormacaoAcademicaVOs().add(intForm);
		}	
		Iterator l = matCRM.getAluno().getFiliacaoVOs().iterator();
		while (l.hasNext()) {
			FiliacaoVO f = (FiliacaoVO)l.next();
			IntegracaoFiliacaoVO intFil = new IntegracaoFiliacaoVO();
			intFil.setCelular(f.getPais().getCelular());
			intFil.setCEP(f.getPais().getCEP());
			//intFil.setCidade(f.getPais().getCidade().getCodigo());
			intFil.setComplemento(f.getPais().getComplemento());
			intFil.setCPF(f.getPais().getCPF());
			if (f.getPais().getDataNasc() != null) {
				intFil.setDataNasc(f.getPais().getDataNasc().toString());
			}
			intFil.setEndereco(f.getPais().getEndereco());
			intFil.setNome(f.getPais().getNome());
			intFil.setRG(f.getPais().getRG());
			intFil.setSetor(f.getPais().getSetor());
			intFil.setTelComercial(f.getPais().getTelefoneComer());
			intFil.setTelRecado(f.getPais().getTelefoneRecado());
			intFil.setTelResidencial(f.getPais().getTelefoneRes());
			intFil.setCelular(f.getPais().getCelular());
			intFil.setTipo(f.getTipo());
			intPessoa.getFiliacaoVOs().add(intFil);
		}		
		return intPessoa;
	}
	
	@POST 
	@Path("/matricularAluno")
	@Consumes("application/xml")
	@Produces("application/xml")
	public IntegracaoMatriculaCRMVO getMensagem(final IntegracaoMatriculaCRMVO integracaoMatriculaCRMVO) {
		//System.out.println("Entrou aqui 1 ");
		StringBuilder mensagem = new StringBuilder("");
		MatriculaCRMVO matCRM = new MatriculaCRMVO();
		List<DisciplinaMatriculaCRMVO> lista = new ArrayList<DisciplinaMatriculaCRMVO>();
		try {
			//System.out.println("Entrou aqui 2 ");
			UsuarioVO usuResp = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(
					Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			
			ConfiguracaoFinanceiroVO conf = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, matCRM.getUnidadeEnsino().getCodigo(), usuResp);
			ConfiguracaoGeralSistemaVO confGeral = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuResp, matCRM.getUnidadeEnsino().getCodigo());
			usuResp.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPerfilParaFuncionarioAdministrador(matCRM.getUnidadeEnsino().getCodigo(), usuResp));
			
			List<UsuarioVO> listaUsuario = getFacadeFactory().getUsuarioFacade().consultarPorCodigoPessoa(confGeral.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuResp);
			if (!listaUsuario.isEmpty()) {
				usuResp = listaUsuario.get(0);
			}
			
			//System.out.println("Entrou aqui 3 ");
			/// inserir pessoa
			PessoaVO p = new PessoaVO();
			p = p.getPessoaVO(integracaoMatriculaCRMVO.getPessoa());
			PessoaVO.validarDados(p, true, false, false);
			// verifica se existe cidade com esse codigo IBGE ou nome
			CidadeVO cidade = getFacadeFactory().getCidadeFacade().consultarCidadePorCodigoIBGENomeCidade(p.getCidade().getCodigoIBGE(), p.getCidade().getNome(), usuResp);
			//System.out.println("Entrou aqui 4 " + cidade.getCodigo());
			if (cidade != null) {
				if(!cidade.getCodigoIBGE().equals(p.getCidade().getCodigoIBGE()) || !cidade.getNome().equals(p.getCidade().getNome())) {
					getFacadeFactory().getCidadeFacade().alterar(cidade, usuResp);
				}
				p.setCidade(cidade);
			} else {
				cidade = new CidadeVO();
				cidade.setNome(p.getCidade().getNome());
				cidade.setCodigoIBGE(p.getCidade().getCodigoIBGE());
				cidade.setEstado(getFacadeFactory().getEstadoFacade().consultarPorSigla(p.getCidade().getEstado().getSigla(), usuResp));
				getFacadeFactory().getCidadeFacade().incluir(cidade, usuResp);
				p.setCidade(cidade);
			}
			// verifica se existe cidade com esse codigo IBGE ou nome
			CidadeVO cidadeNat = getFacadeFactory().getCidadeFacade().consultarCidadePorCodigoIBGENomeCidade(p.getNaturalidade().getCodigoIBGE(), p.getNaturalidade().getNome(), usuResp);
			//System.out.println("Entrou aqui 5 " + cidadeNat.getCodigo());
			if (cidadeNat != null) {
				if(!cidadeNat.getCodigoIBGE().equals(p.getNaturalidade().getCodigoIBGE()) || !cidadeNat.getNome().equals(p.getNaturalidade().getNome())) {
					getFacadeFactory().getCidadeFacade().alterar(cidadeNat, usuResp);
				}
				p.setNaturalidade(cidadeNat);
			} else {
				cidadeNat = new CidadeVO();
				cidadeNat.setNome(p.getNaturalidade().getNome());
				cidadeNat.setCodigoIBGE(p.getNaturalidade().getCodigoIBGE());
				cidadeNat.setEstado(getFacadeFactory().getEstadoFacade().consultarPorSigla(p.getNaturalidade().getEstado().getSigla(), usuResp));
				getFacadeFactory().getCidadeFacade().incluir(cidadeNat, usuResp);
				p.setNaturalidade(cidadeNat);
			}
			// cidade formacao academica 
			// verifica se existe cidade com esse codigo IBGE ou nome
			if (!p.getFormacaoAcademicaVOs().isEmpty()) {
				Iterator i = p.getFormacaoAcademicaVOs().iterator();
				FormacaoAcademicaVO formacao = (FormacaoAcademicaVO)i.next();
				CidadeVO cidadeFor = getFacadeFactory().getCidadeFacade().consultarCidadePorCodigoIBGENomeCidade(formacao.getCidade().getCodigoIBGE(), "", usuResp);
				if (cidadeFor != null) {
					if(!cidadeFor.getCodigoIBGE().equals(formacao.getCidade().getCodigoIBGE()) || !cidadeFor.getNome().equals(formacao.getCidade().getNome())) {
						getFacadeFactory().getCidadeFacade().alterar(cidadeFor, usuResp);
					}
					formacao.setCidade(cidadeFor);
				} 			
			}
			List<PaizVO> listaPaiz = getFacadeFactory().getPaizFacade().consultarPorNome(p.getNacionalidade().getNome(), false, usuResp);
			if (!listaPaiz.isEmpty()) {
				PaizVO paiz = (PaizVO)listaPaiz.get(0);
				p.setNacionalidade(paiz);
			}
			PessoaVO p2 = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(p.getCPF(), null, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuResp);
			//System.out.println("Entrou aqui 6 " + p2.getCodigo());
			// se existir vincula pessoa a essa cidade
			// senao consulta cidade pelo nome e estado e vincula na cidade o codigo IBGE.
			if (p2.getCodigo().intValue() == 0) {
				getFacadeFactory().getPessoaFacade().incluirPessoaProspectMatriculaCRM(p, usuResp, confGeral, false);				
			} else {
				p.setCodigo(p2.getCodigo().intValue());
				//System.out.println("Entrou aqui 7 " + p2.getCodigo());
				getFacadeFactory().getPessoaFacade().alterarPessoaProspectMatriculaCRM(p, usuResp, confGeral, false);
				//System.out.println("Entrou aqui 8 " + p2.getCodigo());
				p = p2;
				p.setCidade(cidade);
				p.setNaturalidade(cidadeNat);
				//getFacadeFactory().getPessoaFacade().alterarPessoaProspectMatriculaCRM(p, usuResp, confGeral, false);
			}
			matCRM = matCRM.getMatriculaCRMVO(integracaoMatriculaCRMVO);
			matCRM.setAluno(p);			
			matCRM.setUsuario(usuResp);
			if (matCRM.getConsultor().getCodigo().intValue() == 0) {
				matCRM.setConsultor(confGeral.getFuncionarioRespAlteracaoDados());
			}
			if (matCRM.getProcessoMatricula().getCodigo().intValue() == 0) {
				Integer codProcesso = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorCodigoTurmaAtivoEntrePeriodo(matCRM.getTurma().getCodigo(), null, TipoAlunoCalendarioMatriculaEnum.CALOURO);
				if (codProcesso > 0) {
					matCRM.getProcessoMatricula().setCodigo(codProcesso);
				} else {
					throw new Exception("Não foi possível definir o processo de matricula, para conclusão do processo. Por favor verifica se existe um processo de matricula cadastrado que atenda as configurações de (turma, curso e periodo vigência) encaminhadas!");
				}
			}
			//System.out.println("Entrou aqui 9 " + matCRM.getAluno().getNome());
			/// inserir matriculaCRM
			getFacadeFactory().getMatriculaCRMFacade().incluir(matCRM, usuResp);
			/// processar matriculaCRM
			try {
				matCRM = getFacadeFactory().getMatriculaCRMFacade().consultarAlunoASerMatriculadoPorCodigoPessoa(matCRM.getAluno().getCodigo(), Boolean.FALSE, null);
				if (matCRM != null) {
					try {
						//System.out.println("Entrou aqui 10 " + matCRM.getAluno().getNome());
						matCRM.getUnidadeEnsino().getConfiguracoes();
						getFacadeFactory().getMatriculaCRMFacade().novaMatricula(matCRM, conf, usuResp);
						mensagem.append("\n=========== Matrícula Realizada com sucesso ============\n");
						mensagem.append("\n....NOME ALUNO => " + matCRM.getAluno().getNome());
						mensagem.append("\n....CODIGO ALUNO =>" + matCRM.getAluno().getCodigo());
						mensagem.append("\n....TURMA =>" + matCRM.getTurma().getIdentificadorTurma());
						mensagem.append("\n....UNIDADE ENSINO =>" + matCRM.getUnidadeEnsino().getNome());
						mensagem.append("\n....PROCESSO MATRICULA =>" + matCRM.getProcessoMatricula().getDescricao());
						mensagem.append("\n....PLANO FINANCEIRO CURSO =>" + matCRM.getPlanoFinanceiroCurso().getDescricao());
						mensagem.append("\n....CONDICAO PAGAMENTO PLANO FINANCEIRO CURSO =>"
								+ matCRM.getCondicaoPagamentoPlanoFinanceiroCurso().getDescricao());
						mensagem.append("\n===============================================\n");
					} catch (Exception e) {
						try {
							matCRM.setErro(Boolean.TRUE);
							matCRM.setMensagemErro(e.getMessage());
							//System.out.println(e.getMessage());
							getFacadeFactory().getMatriculaCRMFacade().alterar(matCRM);
							mensagem.append(e.getMessage()).append("\n");
							integracaoMatriculaCRMVO.setMensagemErro(e.getMessage());
							integracaoMatriculaCRMVO.setErro(Boolean.TRUE);
							integracaoMatriculaCRMVO.setMatricula("");							
							return integracaoMatriculaCRMVO;
							//matCRM = null;
						} catch (Exception x) {
							//mensagem.append(" MENSAGEM ERRO => ").append(x.getMessage()).append("\n");
						} finally {
							//removerObjetoMemoria(matCRM);
						}
					} finally {
					}
//					mensagem.append("\nProcessamento Finalizado!\n");
//					mensagem.append("=============================================================================\n");
				} else {
//					mensagem.append("\nProcessamento Finalizado!\n");
//					mensagem.append("Não há matrícula a ser realizada!\n");
//					mensagem.append("=============================================================================\n");
				}
			} catch (Exception e) {
				//System.out.println("Entrou aqui 11 " + e.getMessage());
				e.getMessage();
			}
			integracaoMatriculaCRMVO.setMensagemErro("");
			integracaoMatriculaCRMVO.setErro(Boolean.FALSE);
			if(matCRM != null) {
			integracaoMatriculaCRMVO.setMatricula(matCRM.getMatricula());
			integracaoMatriculaCRMVO.setHtmlContratoMatricula(matCRM.getHtmlContratoMatricula());
			integracaoMatriculaCRMVO.setDiretorioBoletoMatricula(matCRM.getDiretorioBoletoMatricula());
			integracaoMatriculaCRMVO.setDiretorioContratoPdf(matCRM.getDiretorioContratoPdf());
			removerObjetoMemoria(matCRM);
			}
			removerObjetoMemoria(conf);
			removerObjetoMemoria(usuResp);
			return integracaoMatriculaCRMVO;
		} catch (Exception ex) {
			//System.out.println("Entrou aqui 22 " + ex.getMessage());
			Logger.getLogger(JobMatriculaCRM.class.getName()).log(Level.SEVERE, null, ex);
			integracaoMatriculaCRMVO.setMensagemErro(ex.getMessage());
			integracaoMatriculaCRMVO.setErro(Boolean.TRUE);
			integracaoMatriculaCRMVO.setMatricula("");
			//System.out.println("Entrou aqui 23 " + ex.getMessage());
			return integracaoMatriculaCRMVO;
		} finally {
			System.out.println("FINALLY ");
		}
	}
	
	@POST 
	@Path("/incluirAluno")
	@Consumes("application/xml")
	@Produces("application/xml")
	public Response getMensagem(final IntegracaoPessoaVO integracaoPessoaVO) {
		//System.out.println("Entrou aqui 1 ");		
		//MatriculaCRMVO matCRM = new MatriculaCRMVO();
		//List<DisciplinaMatriculaCRMVO> lista = new ArrayList<DisciplinaMatriculaCRMVO>();
		try {
			UsuarioVO usuResp = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(
					Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			
			ConfiguracaoFinanceiroVO conf = getAplicacaoControle().getConfiguracaoFinanceiroVO(null);
			ConfiguracaoGeralSistemaVO confGeral = getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null);
			usuResp.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPerfilParaFuncionarioAdministrador(0, usuResp));
						
			
			PessoaVO p = new PessoaVO();
			p = p.getPessoaVO(integracaoPessoaVO);
			p.setAluno(Boolean.TRUE);
			PessoaVO.validarDados(p, true, false, false);
			// verifica se existe cidade com esse codigo IBGE ou nome
			CidadeVO cidade = getFacadeFactory().getCidadeFacade().consultarCidadePorCodigoIBGENomeCidade(p.getCidade().getCodigoIBGE(), p.getCidade().getNome(), usuResp);
			if (cidade != null) {
				if(!cidade.getCodigoIBGE().equals(p.getCidade().getCodigoIBGE()) || !cidade.getNome().equals(p.getCidade().getNome()) ) {
					getFacadeFactory().getCidadeFacade().alterar(cidade, usuResp);
				}
				p.setCidade(cidade);
			} else {
				cidade = new CidadeVO();
				cidade.setNome(p.getCidade().getNome());
				cidade.setCodigoIBGE(p.getCidade().getCodigoIBGE());
				cidade.setEstado(getFacadeFactory().getEstadoFacade().consultarPorSigla(p.getCidade().getEstado().getSigla(), usuResp));
				getFacadeFactory().getCidadeFacade().incluir(cidade, usuResp);
				p.setCidade(cidade);
			}
			// verifica se existe cidade com esse codigo IBGE ou nome
			CidadeVO cidadeNat = getFacadeFactory().getCidadeFacade().consultarCidadePorCodigoIBGENomeCidade(p.getNaturalidade().getCodigoIBGE(), p.getNaturalidade().getNome(), usuResp);
			//System.out.println("Entrou aqui 5 " + cidadeNat.getCodigo());
			if (cidadeNat != null) {
				if(!cidadeNat.getCodigoIBGE().equals(p.getNaturalidade().getCodigoIBGE()) || !cidadeNat.getNome().equals(p.getNaturalidade().getNome()) ) {
					getFacadeFactory().getCidadeFacade().alterar(cidadeNat, usuResp);
				}
				p.setNaturalidade(cidadeNat);
			} else {
				cidadeNat = new CidadeVO();
				cidadeNat.setNome(p.getNaturalidade().getNome());
				cidadeNat.setCodigoIBGE(p.getNaturalidade().getCodigoIBGE());
				cidadeNat.setEstado(getFacadeFactory().getEstadoFacade().consultarPorSigla(p.getNaturalidade().getEstado().getSigla(), usuResp));
				getFacadeFactory().getCidadeFacade().incluir(cidadeNat, usuResp);
				p.setNaturalidade(cidadeNat);
			}
			// cidade formacao academica 
			// verifica se existe cidade com esse codigo IBGE ou nome
			if (!p.getFormacaoAcademicaVOs().isEmpty()) {
				Iterator i = p.getFormacaoAcademicaVOs().iterator();
				while (i.hasNext()) {
					FormacaoAcademicaVO formacao = (FormacaoAcademicaVO)i.next();
					CidadeVO cidadeFor = getFacadeFactory().getCidadeFacade().consultarCidadePorCodigoIBGENomeCidade(formacao.getCidade().getCodigoIBGE(), "", usuResp);
					if (cidadeFor != null) {
						if(!cidadeFor.getCodigoIBGE().equals(formacao.getCidade().getCodigoIBGE()) || !cidadeFor.getNome().equals(formacao.getCidade().getNome()) ) {
							getFacadeFactory().getCidadeFacade().alterar(cidadeFor, usuResp);
						}
						formacao.setCidade(cidadeFor);
					}
					String descricaoArea = formacao.getDescricaoAreaConhecimento();
					if (!descricaoArea.isEmpty()) {
						try {
							Integer codigoArea = Integer.parseInt(descricaoArea);
							formacao.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(codigoArea, usuResp));
						} catch (Exception e) {
							
						}
					}
				}
			}
			List<PaizVO> listaPaiz = getFacadeFactory().getPaizFacade().consultarPorNome(p.getNacionalidade().getNome(), false, usuResp);
			if (!listaPaiz.isEmpty()) {
				PaizVO paiz = (PaizVO)listaPaiz.get(0);
				p.setNacionalidade(paiz);
			}
			PessoaVO p2 = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(p.getCPF(), null, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuResp);
			//System.out.println("Entrou aqui 6 " + p2.getCodigo());
			// se existir vincula pessoa a essa cidade
			// senao consulta cidade pelo nome e estado e vincula na cidade o codigo IBGE.
			if (p2.getCodigo().intValue() == 0) {
				getFacadeFactory().getPessoaFacade().incluirPessoaProspectMatriculaCRM(p, usuResp, confGeral, false);				
			} else {
				p.setCodigo(p2.getCodigo().intValue());
				p.setAluno(Boolean.TRUE);
				getFacadeFactory().getPessoaFacade().alterarPessoaProspectMatriculaCRM(p, usuResp, confGeral, false);
				p = p2;
				p.setCidade(cidade);
				p.setNaturalidade(cidadeNat);
			}						
			removerObjetoMemoria(conf);
			removerObjetoMemoria(usuResp);
			//return Response.status(Response.Status.OK).build();
			return Response.status(Response.Status.OK).entity(integracaoPessoaVO).build();
			//return integracaoPessoaVO;
		} catch (Exception ex) {
			Logger.getLogger(JobMatriculaCRM.class.getName()).log(Level.SEVERE, null, ex);
			//return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			integracaoPessoaVO.setMensagemErro(ex.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(integracaoPessoaVO).build();
			//return integracaoPessoaVO;
		} finally {
//			System.out.println("FINALLY ");
		}
	}
	
//	@POST 
//	@Path("/atualizarAluno")
//	@Consumes("application/xml")
//	@Produces("application/xml")
//	public IntegracaoPessoaVO getMensagem(final IntegracaoPessoaVO pessoa) {
//		
//		StringBuilder mensagem = new StringBuilder("");
//		MatriculaCRMVO matCRM = new MatriculaCRMVO();
//		List<DisciplinaMatriculaCRMVO> lista = new ArrayList<DisciplinaMatriculaCRMVO>();
//		try {
//			UsuarioVO usuResp = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(
//					Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
//			ConfiguracaoFinanceiroVO conf = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(
//					Uteis.NIVELMONTARDADOS_DADOSBASICOS, matCRM.getUnidadeEnsino().getCodigo(), usuResp);
//			ConfiguracaoGeralSistemaVO confGeral = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(
//					Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuResp, matCRM.getUnidadeEnsino().getCodigo());
//			usuResp.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPerfilParaFuncionarioAdministrador(matCRM.getUnidadeEnsino().getCodigo(), usuResp, confGeral));
//			
//			List<UsuarioVO> listaUsuario = getFacadeFactory().getUsuarioFacade().consultarPorCodigoPessoa(confGeral.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuResp);
//			if (!listaUsuario.isEmpty()) {
//				usuResp = listaUsuario.get(0);
//			}
//			
//			/// inserir pessoa
//			PessoaVO p = new PessoaVO();
//			p = p.getPessoaVO(pessoa);
//			PessoaVO.validarDados(p, true, false);
//			// verifica se existe cidade com esse codigo IBGE ou nome
//			CidadeVO cidade = getFacadeFactory().getCidadeFacade().consultarCidadePorCodigoIBGENomeCidade(p.getCidade().getCodigoIBGE(), p.getCidade().getNome(), usuResp);
//			if (cidade != null) {
//				p.setCidade(cidade);
//				getFacadeFactory().getCidadeFacade().alterar(cidade, usuResp);
//			} else {
//				cidade = new CidadeVO();
//				cidade.setNome(p.getCidade().getNome());
//				cidade.setCodigoIBGE(p.getCidade().getCodigoIBGE());
//				cidade.setEstado(getFacadeFactory().getEstadoFacade().consultarPorSigla(p.getCidade().getEstado().getSigla(), usuResp));
//				getFacadeFactory().getCidadeFacade().incluir(cidade, usuResp);
//				p.setCidade(cidade);
//			}			
//			List<PaizVO> listaPaiz = getFacadeFactory().getPaizFacade().consultarPorNome(p.getNacionalidade().getNome(), false, usuResp);
//			if (!listaPaiz.isEmpty()) {
//				PaizVO paiz = (PaizVO)listaPaiz.get(0);
//				p.setNacionalidade(paiz);
//			}			
//			PessoaVO p2 = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(p.getCPF(), null, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuResp);
//			if (p2.getCodigo().intValue() == 0) {
//				getFacadeFactory().getPessoaFacade().incluirPessoaProspectMatriculaCRM(p, usuResp, confGeral, false);				
//			} else {
//				p.setCodigo(p2.getCodigo());
//				p.setFiliacaoVOs(null);
//				getFacadeFactory().getPessoaFacade().alterarPessoaProspectMatriculaCRM(p, usuResp, confGeral, false);
////				p2.setNome(p.getNome());
////				p2.setEndereco(p.getEndereco());
////				p2.setSetor(p.getSetor());
////				p2.setNumero(p.getNumero());
////				p2.setCEP(p.getCEP());
////				p2.setComplemento(p.getComplemento());
////				p2.setSexo(p.getSexo());
////				p2.setEstadoCivil(p.getEstadoCivil());
////				p2.setTelefoneComer(p.getTelefoneComer());
////				p2.setTelefoneRes(p.getTelefoneRes());
////				p2.setTelefoneRecado(p.getTelefoneRecado());
////				p2.setCelular(p.getCelular());
////				p2.setEmail(p.getEmail());
////				p2.setEmail2(p.getEmail2());
////				p2.setPaginaPessoal(p.getPaginaPessoal());
////				p2.setDataEmissaoRG(p.getDataEmissaoRG());
////				p2.setCPF(p.getCPF());
////				p2.setRG(p.getRG());
////				p2.setCertificadoMilitar(p.getCertificadoMilitar());
////				p2.setPispasep(p.getPispasep());
////				p2.setDataNasc(p.getDataNasc());
////				p2.setEstadoEmissaoRG(p.getEstadoEmissaoRG());
////				p2.setOrgaoEmissor(p.getOrgaoEmissor());
////				p2.setTituloEleitoral(p.getTituloEleitoral());
////				p2.setNecessidadesEspeciais(p.getNecessidadesEspeciais());
////				p2.getCidade().setCodigo(p.getCidade().getCodigo());
////				p2.getNaturalidade().setCodigo(p.getNaturalidade().getCodigo());
//////
//////				private List<IntegracaoFormacaoAcademicaVO> formacaoAcademicaVOs;
//////				private List<IntegracaoFiliacaoVO> filiacaoVOs;
////
////				getFacadeFactory().getPessoaFacade().alterarPessoaProspectMatriculaCRM(p2, usuResp, confGeral, false);
////				
//			}
//			removerObjetoMemoria(matCRM);
//			removerObjetoMemoria(conf);
//			removerObjetoMemoria(usuResp);
//			return pessoa;
//		} catch (Exception ex) {
//			return pessoa;
//		} finally {
//			
//		}
//	}
	
	@POST
	@Path("/incluirPessoa")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response incluirPessoa(@Context final HttpServletRequest request, final IntegracaoPessoaVO integracaoPessoaVO) {
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if (infoWSVO.getStatus() == Status.OK.getStatusCode()) {
				getFacadeFactory().getMatriculaCRMFacade().preencherNovaIntegracaoPessoaWS(integracaoPessoaVO);
				errorInfoRSVO = new ErrorInfoRSVO(Status.OK, "Integração Realizada Com Sucesso");
			}
		} catch (Exception e) {
			errorInfoRSVO = new ErrorInfoRSVO(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return Response.status(errorInfoRSVO.getStatusCode()).entity(errorInfoRSVO).build();
	}
	
	@POST
	@Path("/matricularPessoa")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response matricularPessoa(@Context final HttpServletRequest request, final IntegracaoMatriculaCRMVO integracaoMatriculaCRMVO) {
		StringBuilder sbRetorno = new StringBuilder();
		try {
			InfoWSVO infoWSVO = validarDadosAutenticacaoTokenWebService(request);
			if(infoWSVO.getStatus() == Status.OK.getStatusCode()) {
				//booleano usado somente para tratar erro retornado do webservice no ConsistirException;
				boolean validarAlunoMatriculadoWebServiceMatriculaCrm = true ;
				MatriculaCRMVO matCrmVO = getFacadeFactory().getMatriculaCRMFacade().preencherNovaMatriculaAlunoWS(integracaoMatriculaCRMVO,validarAlunoMatriculadoWebServiceMatriculaCrm);
				errorInfoRSVO = new ErrorInfoRSVO(Status.OK, "Integração Realizada Com Sucesso");
				sbRetorno.append("matricula:").append(matCrmVO.getMatricula());
				sbRetorno.append(",urlBoleto:").append(matCrmVO.getUrlBoletoMatricula());
				sbRetorno.append(",matriculaPeriodo:").append(matCrmVO.getMatriculaPeriodo());
				errorInfoRSVO.setCampo(sbRetorno.toString());
			}
			
		} catch (ConsistirException e) {
			MatriculaVO matriculaExistente = (MatriculaVO) e.getObjetoOrigem();
			errorInfoRSVO = new ErrorInfoRSVO(Status.INTERNAL_SERVER_ERROR, "Matricula Existente");
			sbRetorno.append("matricula:").append(matriculaExistente.getMatricula());			
			sbRetorno.append(",mensagem:").append(e.getToStringMensagemErro());
			errorInfoRSVO.setCampo(sbRetorno.toString());			
		 
		} catch (Exception e) {
			errorInfoRSVO = new ErrorInfoRSVO(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		} 
		return Response.status(errorInfoRSVO.getStatusCode()).entity(errorInfoRSVO).build();
	}
	
	
}