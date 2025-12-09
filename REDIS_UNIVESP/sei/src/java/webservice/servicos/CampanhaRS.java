package webservice.servicos;

import java.io.Serializable;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.CampanhaRSLogVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoCompromissoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.servicos.objetos.CampanhaRSVO;

@Service
@Path("/campanhaRS")
public class CampanhaRS extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;

	public CampanhaRS() throws Exception {
		super();
	}
	
	public void validarDados(CampanhaRSVO campanhaRSVO, TipoCompromissoEnum tipoCompromisso) throws Exception {
		if (campanhaRSVO.getCodigoUnidadeEnsino() == null || campanhaRSVO.getCodigoUnidadeEnsino().equals(0)) {
			throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
		}
		if (campanhaRSVO.getPessoaObject().getNome() == null || campanhaRSVO.getPessoaObject().getNome().equals("")) {
			throw new Exception("O campo NOME deve ser informado.");
		}
		if (campanhaRSVO.getPessoaObject().getEmail() == null || campanhaRSVO.getPessoaObject().getEmail().equals("")) {
			throw new Exception("O campo EMAIL deve ser informado.");
		}
		if (tipoCompromisso.equals(TipoCompromissoEnum.TIRE_SUAS_DUVIDAS)) {
			if (campanhaRSVO.getDuvida() == null || campanhaRSVO.getDuvida().equals("")) {
				throw new Exception("O campo DÚVIDA deve ser informado.");
			}
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gerarAgendaLigacaoReceptivaTireSuasDuvidasCampanhaCRM")
	public CampanhaRSVO gerarAgendaLigacaoReceptivaTireSuasDuvidasCampanhaCRM(final CampanhaRSVO campanhaRSVO, @Context final SecurityContext security) throws Exception {
		ProspectsVO prospectsVO = new ProspectsVO();
		CampanhaVO campanhaVO = new CampanhaVO();
		UsuarioVO usuarioVO = new UsuarioVO();
		CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO = new CompromissoAgendaPessoaHorarioVO();
		Boolean ocorreuErro = false;
		try {
			validarDados(campanhaRSVO, TipoCompromissoEnum.TIRE_SUAS_DUVIDAS);
			consultarConfiguracaoGeralSistemaUtilizar(campanhaRSVO.getCodigoUnidadeEnsino());
			usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getConfiguracaoGeralSistemaVO().getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);

			campanhaVO = getFacadeFactory().getCampanhaFacade().consultarCampanhaPorTipoCampanhaUnidadeEnsino(campanhaRSVO.getCodigoUnidadeEnsino(), "AT", TipoCampanhaEnum.LIGACAO_RECEPTIVA, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if (campanhaVO == null) {
				throw new Exception("Não foi Encontrada Campanha do Tipo Ligação Receptiva!");
			}
			inicializarDadosProspect(prospectsVO, campanhaRSVO.getPessoaObject(), campanhaRSVO.getCodigoUnidadeEnsino(), usuarioVO);
			inicializarDadosPessoaProspect(prospectsVO, campanhaRSVO.getPessoaObject());
			if (!campanhaRSVO.getCodigoUnidadeEnsino().equals(0)) {
				campanhaVO.getUnidadeEnsino().setCodigo(campanhaRSVO.getCodigoUnidadeEnsino());
				getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(campanhaVO.getUnidadeEnsino(), NivelMontarDados.BASICO, usuarioVO);
			}
			if (!campanhaRSVO.getCodigoCurso().equals(0)) {
				CursoInteresseVO cursoInteresseVO = new CursoInteresseVO();
				cursoInteresseVO.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campanhaRSVO.getCodigoCurso(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO));
				prospectsVO.getCursoInteresseVOs().add(cursoInteresseVO);
			}
			prospectsVO = getFacadeFactory().getProspectsFacade().realizarCriacaoProspectLigacaoReceptivaRS(prospectsVO, usuarioVO);
			compromissoAgendaPessoaHorarioVO = getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarCriacaoCompromissoCampanhaLigacaoReceptivaRS(prospectsVO, campanhaVO, TipoCompromissoEnum.TIRE_SUAS_DUVIDAS, campanhaRSVO.getCodigoUnidadeEnsino(), campanhaRSVO.getCodigoCurso(), campanhaRSVO.getCodigoTurno(), campanhaRSVO.getDuvida(), usuarioVO);

			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_RECEPTIVA_TIRE_SUA_DUVIDA, false, campanhaRSVO.getCodigoUnidadeEnsino(), usuarioVO);
			realizarEnvioEmailProspect(prospectsVO.getPessoa(), campanhaVO.getUnidadeEnsino(), campanhaVO.getCurso().getNome(), "", campanhaRSVO.getDuvida(), TipoCompromissoEnum.TIRE_SUAS_DUVIDAS, mensagemTemplate, usuarioVO, configuracaoGeralSistemaVO);
			campanhaRSVO.setMensagem("Dúvida enviada com Sucesso!");
			return campanhaRSVO;
		} catch (Exception e) {
			ocorreuErro = true;
			System.out.println("===> Campanha Tire Suas Duvidas LOG: ERRO: " + e.getMessage());
			e.printStackTrace();
			campanhaRSVO.setMensagem(e.getMessage());
			return campanhaRSVO;
		} finally {
			persistirLog(campanhaRSVO, campanhaVO, prospectsVO, compromissoAgendaPessoaHorarioVO, TipoCompromissoEnum.TIRE_SUAS_DUVIDAS, ocorreuErro, usuarioVO);
			System.out.println("===> Campanha Tire Suas Duvidas LOG: SUCESSO: ");
			inicializarDadosCampanhaRS(campanhaRSVO);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gerarAgendaLigacaoReceptivaQueroSerAlunoCampanhaCRM")
	public CampanhaRSVO gerarAgendaLigacaoReceptivaQueroSerAlunoCampanhaCRM(final CampanhaRSVO campanhaRSVO, @Context final SecurityContext security) throws Exception {
		ProspectsVO prospectsVO = new ProspectsVO();
		CampanhaVO campanhaVO = new CampanhaVO();
		UsuarioVO usuarioVO = new UsuarioVO();
		CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO = new CompromissoAgendaPessoaHorarioVO();
		Boolean ocorreuErro = false;
		boolean entrarMetodo = true;
		try {
			if (entrarMetodo) {
				validarDados(campanhaRSVO, TipoCompromissoEnum.QUERO_SER_ALUNO);
				consultarConfiguracaoGeralSistemaUtilizar(campanhaRSVO.getCodigoUnidadeEnsino());
				usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getConfiguracaoGeralSistemaVO().getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
				campanhaVO = getFacadeFactory().getCampanhaFacade().consultarCampanhaPorTipoCampanhaUnidadeEnsino(campanhaRSVO.getCodigoUnidadeEnsino(), "AT", TipoCampanhaEnum.LIGACAO_RECEPTIVA, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				if (campanhaVO == null) {
					throw new Exception("Não foi Encontrada Campanha do Tipo Ligação Receptiva!");
				}
				inicializarDadosProspect(prospectsVO, campanhaRSVO.getPessoaObject(), campanhaRSVO.getCodigoUnidadeEnsino(), usuarioVO);
				inicializarDadosPessoaProspect(prospectsVO, campanhaRSVO.getPessoaObject());
				if (!campanhaRSVO.getCodigoUnidadeEnsino().equals(0)) {
					campanhaVO.getUnidadeEnsino().setCodigo(campanhaRSVO.getCodigoUnidadeEnsino());
					getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(campanhaVO.getUnidadeEnsino(), NivelMontarDados.BASICO, usuarioVO);
				}
				if (!campanhaRSVO.getCodigoCurso().equals(0)) {
					CursoInteresseVO cursoInteresseVO = new CursoInteresseVO();
					cursoInteresseVO.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campanhaRSVO.getCodigoCurso(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO));
					prospectsVO.getCursoInteresseVOs().add(cursoInteresseVO);
				}
				prospectsVO = getFacadeFactory().getProspectsFacade().realizarCriacaoProspectLigacaoReceptivaRS(prospectsVO, usuarioVO);
				compromissoAgendaPessoaHorarioVO = getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarCriacaoCompromissoCampanhaLigacaoReceptivaRS(prospectsVO, campanhaVO, TipoCompromissoEnum.QUERO_SER_ALUNO, campanhaRSVO.getCodigoUnidadeEnsino(), campanhaRSVO.getCodigoCurso(), campanhaRSVO.getCodigoTurno(), campanhaRSVO.getDuvida(), usuarioVO);

				PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_RECEPTIVA_QUERO_SER_ALUNO, false, campanhaVO.getUnidadeEnsino().getCodigo(), usuarioVO);
				realizarEnvioEmailProspect(prospectsVO.getPessoa(), campanhaVO.getUnidadeEnsino(), campanhaVO.getCurso().getNome(), "", campanhaRSVO.getDuvida(), TipoCompromissoEnum.TIRE_SUAS_DUVIDAS, mensagemTemplate, usuarioVO, configuracaoGeralSistemaVO);
				campanhaRSVO.setMensagem("Mensagem enviada com Sucesso!");
			}
			return campanhaRSVO;
		} catch (Exception e) {
			ocorreuErro = true;
			System.out.println("===> Campanha Tire Suas Duvidas LOG: ERRO: " + e.getMessage());
			e.printStackTrace();
			campanhaRSVO.setMensagem(e.getMessage());
			return campanhaRSVO;
		} finally {
			persistirLog(campanhaRSVO, campanhaVO, prospectsVO, compromissoAgendaPessoaHorarioVO, TipoCompromissoEnum.TIRE_SUAS_DUVIDAS, ocorreuErro, usuarioVO);
			System.out.println("===> Campanha Tire Suas Duvidas LOG: SUCESSO: ");
			inicializarDadosCampanhaRS(campanhaRSVO);
		}
	}

	public void realizarEnvioEmailProspect(PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, String nomeCurso, String nomeTurno, String assuntoDuvida, TipoCompromissoEnum tipoCompromisso, PersonalizacaoMensagemAutomaticaVO msg, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (msg == null) {
			return;
		}

		ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
		if (usuarioVO.getPessoa() == null || usuarioVO.getPessoa().getCodigo().equals(0)) {
			comunicacaoInternaVO.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());
		} else {
			comunicacaoInternaVO.setResponsavel(usuarioVO.getPessoa());
		}
		comunicacaoInternaVO.setEnviarEmail(true);
		comunicacaoInternaVO.setTipoMarketing(false);
		comunicacaoInternaVO.setTipoLeituraObrigatoria(false);
		comunicacaoInternaVO.setDigitarMensagem(true);
		comunicacaoInternaVO.setRemoverCaixaSaida(false);
		comunicacaoInternaVO.setAluno(pessoaVO);
		comunicacaoInternaVO.setUnidadeEnsino(unidadeEnsinoVO);
		String mensagem = msg.getMensagem();
		mensagem = mensagem.replaceAll("NOME_UNIDADE_ENSINO", unidadeEnsinoVO.getNome());
		mensagem = mensagem.replaceAll("NOME_CURSO", pessoaVO.getNome());
		mensagem = mensagem.replaceAll("NOME_TURNO", pessoaVO.getNome());
		mensagem = mensagem.replaceAll("NOME_PESSOA", pessoaVO.getNome());
		if (tipoCompromisso.equals(TipoCompromissoEnum.TIRE_SUAS_DUVIDAS)) {
			mensagem = mensagem.replaceAll("ASSUNTO_DUVIDA", assuntoDuvida);
		}
		comunicacaoInternaVO.setAssunto(msg.getAssunto());
		comunicacaoInternaVO.setMensagem(mensagem);
		ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
		comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
		comunicadoInternoDestinatarioVO.setDataLeitura(null);
		comunicadoInternoDestinatarioVO.setCiJaRespondida(false);
		comunicadoInternoDestinatarioVO.setCiJaLida(false);
		comunicadoInternoDestinatarioVO.setRemoverCaixaEntrada(false);
		comunicadoInternoDestinatarioVO.setMensagemMarketingLida(false);
		comunicadoInternoDestinatarioVO.setDestinatario(pessoaVO);
		comunicadoInternoDestinatarioVO.setEmail(pessoaVO.getEmail());
		comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
		if (!pessoaVO.getEmail().equals("")) {
			getFacadeFactory().getEmailFacade().realizarGravacaoEmail(comunicacaoInternaVO, null, true, usuarioVO, null);
		}
	}

	public void inicializarDadosCampanhaRS(CampanhaRSVO campanhaRSVO) {
		campanhaRSVO.setCodigoUnidadeEnsino(0);
		campanhaRSVO.getPessoaObject().setNome("");
		campanhaRSVO.getPessoaObject().setEmail("");
		campanhaRSVO.getPessoaObject().setTelefoneResidencial("");
		campanhaRSVO.getPessoaObject().setCelular("");
		campanhaRSVO.setDuvida("");
	}

	public String obterMensagemFormatadaMensagemReceptivaTireSuasDuvidas(String nomePessoa, String nomeUnidadeEnsino, String nomeCurso, String nomeTurno, String assuntoDuvida, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), nomePessoa);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), nomeUnidadeEnsino);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), nomeCurso);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_TURNO.name(), nomeTurno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ASSUNTO_DUVIDA.name(), assuntoDuvida);
		return mensagemTexto;
	}

	public void inicializarDadosProspect(ProspectsVO prospectsVO, PessoaObject pessoaVO, Integer codigoUnidadeEnsino, UsuarioVO usuarioVO) {
		prospectsVO.setNome(pessoaVO.getNome());
		prospectsVO.setEmailPrincipal(pessoaVO.getEmail());
		prospectsVO.setTelefoneResidencial(pessoaVO.getTelefoneResidencial());
		prospectsVO.setCelular(pessoaVO.getCelular());
		prospectsVO.getUnidadeEnsino().setCodigo(codigoUnidadeEnsino);
		prospectsVO.getResponsavelCadastro().setCodigo(usuarioVO.getCodigo());
	}

	public void inicializarDadosPessoaProspect(ProspectsVO prospectsVO, PessoaObject pessoaVO) {
		prospectsVO.getPessoa().setNome(pessoaVO.getNome());
		prospectsVO.getPessoa().setEmail(pessoaVO.getEmail());
		prospectsVO.getPessoa().setTelefoneRes(pessoaVO.getTelefoneResidencial());
		prospectsVO.getPessoa().setCelular(pessoaVO.getCelular());
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

	public void consultarConfiguracaoGeralSistemaUtilizar(Integer codigoUnidadeEnsino) {
		try {
			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(codigoUnidadeEnsino, Uteis.NIVELMONTARDADOS_TODOS, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void persistirLog(CampanhaRSVO campanhaRSVO, CampanhaVO campanhaVO, ProspectsVO prospectsVO, CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO, TipoCompromissoEnum tipoCompromisso, Boolean ocorreuErro, UsuarioVO usuarioVO) throws Exception {
		CampanhaRSLogVO campanhaRSLogVO = new CampanhaRSLogVO();
		campanhaRSLogVO.setCampanha(campanhaVO.getCodigo());
		campanhaRSLogVO.setCodigoUnidadeEnsino(campanhaRSVO.getCodigoUnidadeEnsino());
		campanhaRSLogVO.setCodigoCurso(campanhaRSVO.getCodigoCurso());
		campanhaRSLogVO.setCodigoTurno(campanhaRSVO.getCodigoTurno());
		campanhaRSLogVO.setNome(campanhaRSVO.getPessoaObject().getNome());
		campanhaRSLogVO.setEmail(campanhaRSVO.getPessoaObject().getEmail());
		campanhaRSLogVO.setTelefoneResidencial(campanhaRSVO.getPessoaObject().getTelefoneResidencial());
		campanhaRSLogVO.setCelular(campanhaRSVO.getPessoaObject().getCelular());
		campanhaRSLogVO.setProspectsVO(prospectsVO);
		campanhaRSLogVO.setTipoCompromisso(tipoCompromisso);
		campanhaRSLogVO.setCompromissoAgendaPessoaHorarioVO(compromissoAgendaPessoaHorarioVO);
		campanhaRSLogVO.setColaboradorVO(compromissoAgendaPessoaHorarioVO.getAgendaPessoaHorario().getAgendaPessoa().getPessoa());
		campanhaRSLogVO.setDuvida(campanhaRSVO.getDuvida());
		campanhaRSLogVO.setMensagem(campanhaRSVO.getMensagem());
		campanhaRSLogVO.setOcorreuErro(ocorreuErro);
		campanhaRSLogVO.setUsuarioOperacoesExternasVO(usuarioVO);
		campanhaRSLogVO.setConfiguracaoGeralSistemaVO(getConfiguracaoGeralSistemaVO());
		getFacadeFactory().getCampanhaRSLogFacade().incluir(campanhaRSLogVO, usuarioVO);
	}

}
