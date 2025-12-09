/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobs;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.NotificacaoRegistroAulaNotaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioGrupoDestinatariosVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.facade.jdbc.academico.NotificacaoRegistroAulaNota;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
import relatorio.negocio.jdbc.academico.DiarioRel;

/**
 *
 * @author PEDRO
 */
@Service
@Lazy
public class JobNotificacaoRegistroAulaNota extends SuperFacadeJDBC implements Serializable {

	public void realizarNotificacaoRegistroAulaNota() {
		try {
			ConfiguracoesVO conf = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO(), null);
			ConfiguracaoGeralSistemaVO confEmail = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO(), null);
			List lista = new NotificacaoRegistroAulaNota().consultaRegistroANotificar();
			Iterator i = lista.iterator();
			while (i.hasNext()) {
				NotificacaoRegistroAulaNotaVO notif = (NotificacaoRegistroAulaNotaVO) i.next();

				//getFacadeFactory().getRegistroAulaFacade().validarDadosRegistroAulaNotaTurma(notif.getTurma(), notif.getProfessor(), notif.getDisciplina());
				this.enviarEmail(getFacadeFactory().getRegistroAulaFacade().executarMontagemDadosRegistroAulaENota(notif.getUnidadeEnsino(), notif.getTurmaVO(), notif.getDisciplinaVO(), "", "", notif.getProfessorVO().getPessoa(), notif.getUsuario(), new ConfiguracaoFinanceiroVO(), true, null, confEmail.getPermitirProfessorRealizarLancamentoAlunosPreMatriculados()), notif.getTurmaVO(), notif.getDisciplina(), notif.getUsuario(), confEmail, notif.getUnidadeEnsino());
			}
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			// System.out.println("Erro JobNotificacaoRegistroAulaNota...");
		}
	}

	private void enviarEmail(List<RegistroAulaVO> listaAulas, TurmaVO turma, Integer disciplina, UsuarioVO usuario, ConfiguracaoGeralSistemaVO confEmail, Integer unidadeEnsino) throws Exception {
		ComunicacaoInternaVO comunicacaoInternaVO = null;
		try {
			List listaProfessores = new ArrayList(0);
			comunicacaoInternaVO = new ComunicacaoInternaVO();
			String tipoLayout = "DiarioModRetratoRel";
			ProfessorTitularDisciplinaTurmaVO p = new ProfessorTitularDisciplinaTurmaVO();
			p = getFacadeFactory().getDiarioRelFacade().consultarProfessorTitularTurma(turma, disciplina, "", "", true, usuario);
			listaProfessores.add(p);
			List listaObjetos = new ArrayList(0);
			try {
				listaObjetos = getFacadeFactory().getDiarioRelFacade().consultarRegistroAula(listaProfessores, disciplina, turma, "", "", usuario, null, false, false, "", tipoLayout, "", "", "", "", 0, false, false, false, confEmail.getPermitirProfessorRealizarLancamentoAlunosPreMatriculados(), "", new Date(), new Date(), new ArrayList<String>(0));
			} catch (Exception e) {
			}
			String nomeRelatorio = "";
			String idTurma = "";
			if (!listaAulas.isEmpty()) {
				nomeRelatorio += "-";
				RegistroAulaVO reg = (RegistroAulaVO) listaAulas.get(0);
				nomeRelatorio += Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(reg.getDisciplina().getNome());
				nomeRelatorio += "-";
				nomeRelatorio += Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(reg.getTurma().getIdentificadorTurma());
				nomeRelatorio += "-";
			}
			File file = UteisJSF.realizarGeracaoArquivoPDF(usuario, new Long(0), listaObjetos, "Diário", DiarioRel.getIdEntidade() + nomeRelatorio, DiarioRel.getDesignIReportRelatorio(tipoLayout), DiarioRel.getCaminhoBaseRelatorio(), null);
			ArquivoVO arquivo = new ArquivoVO();
			arquivo.setResponsavelUpload(usuario);
			upLoad(file, arquivo, confEmail, confEmail.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVO_TMP.getValue(), usuario);
			String corpoMensagem = gerarMensagemRegistroAula2("Aula Registrada pela tela de Aula/Nota\nConteúdo postado automaticamente.", usuario);
			comunicacaoInternaVO.getResponsavel().setNome("SEI - SISTEMA EDUCACIONAL INTEGRADO");
			comunicacaoInternaVO.setAssunto("Registro de Aula - " + idTurma);
			comunicacaoInternaVO.setMensagem(comunicacaoInternaVO.getMensagemComLayout(corpoMensagem));
			getFacadeFactory().getComunicacaoInternaFacade().criarFileCorpoMensagemEmail(comunicacaoInternaVO);
			adicionarUsuarioLogadoGrupoEmail(turma, usuario, unidadeEnsino);
			adicionarCoordenadoresCursoGrupoEmail(turma, usuario, unidadeEnsino);
			List<ArquivoVO> listaAnexos = new ArrayList<ArquivoVO>();
			if (file != null) {
				listaAnexos.add(arquivo);
				// getFacadeFactory().getEmailFacade().realizarGravacaoEmail(turma,
				// comunicacaoInternaVO, listaAnexos, confEmail, true, usuario);
			}
			comunicacaoInternaVO.setListaArquivosAnexo(listaAnexos);
			comunicacaoInternaVO.setDigitarMensagem(Boolean.TRUE);
			comunicacaoInternaVO.setResponsavel(confEmail.getResponsavelPadraoComunicadoInterno());
			comunicacaoInternaVO.setTipoDestinatario("PR");

			carregarDestinatarioComunicadoInterno(comunicacaoInternaVO, turma);
			comunicacaoInternaVO.setEnviarEmail(Boolean.TRUE);
			// comunicacaoInternaVO.setData(new Date());
			// comunicacaoInternaVO.setFuncionario(usuario.get);
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, usuario, confEmail,null);
			// if (file != null) {
			// List<File> listaAnexos = new ArrayList<File>();
			// listaAnexos.add(file);
			// getFacadeFactory().getEmailFacade().realizarGravacaoEmail(turma,
			// comunicacaoInternaVO, listaAnexos, confEmail, true, usuario);
			// listaAnexos = null;
			// }
			listaAnexos = null;
			p = null;
			listaProfessores = null;
			tipoLayout = null;
			listaObjetos = null;

		} catch (Exception e) {
			// System.out.print("Erro Envio Email REGISTRO AULA NOTA CONTROLE => "
			// + e.getMessage());
			// throw e;
		}
	}

	public void upLoad(File file, ArquivoVO arquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhaArquivo, UsuarioVO usuarioVO) throws Exception {
		File dir = null;
		File fileArquivo = null;
		String nomeArquivoSemAcento = "";

		try {
			dir = file;
			nomeArquivoSemAcento = criarNomeArquivo(file.getName().substring(0, file.getName().lastIndexOf(".")), usuarioVO);
			arquivoVO.setNome(nomeArquivoSemAcento + "." + file.getName().substring(file.getName().lastIndexOf(".") + 1));

			fileArquivo = new File(caminhaArquivo + File.separator + arquivoVO.getNome());
			FileUtils.copyFile(file, fileArquivo);
			arquivoVO.setPastaBaseArquivo(caminhaArquivo);
			arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVO_TMP);

		} catch (Exception e) {
			throw e;
		} finally {
			dir = null;
			fileArquivo = null;
			// upload = null;
			nomeArquivoSemAcento = null;
		}
	}

	public String criarNomeArquivo(String nomeArquivo, UsuarioVO usuarioVO) {
		return usuarioVO.getCodigo() + "_" + new Date().getTime();
	}

	public String gerarMensagemRegistroAula2(String conteudoAula, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("\r <br />Este é um email automático enviado pelo SEI, informando que: \r <br />O Usuário \"");
		sb.append(usuario.getNome());
		sb.append("\" \r <br />Registrou aula no dia:");
		sb.append(Uteis.getData(new Date(), "dd/MM/yyyy"));
		sb.append(" <br /> Com o Conteúdo: \"");
		sb.append(conteudoAula);
		sb.append("\" <br />O PDF contendo o Diário está em anexo. <br />\r <br />");
		return sb.toString();
	}

	public void carregarDestinatarioComunicadoInterno(ComunicacaoInternaVO comunicacaoInterna, TurmaVO turmaVO) throws Exception {
		Iterator i = turmaVO.getGrupoDestinatarios().getListaFuncionariosGrupoDestinatariosVOs().iterator();
		while (i.hasNext()) {
			FuncionarioGrupoDestinatariosVO func = (FuncionarioGrupoDestinatariosVO) i.next();
			ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
			destinatario.setCiJaLida(Boolean.FALSE);
			destinatario.setDestinatario(func.getFuncionario().getPessoa());
			destinatario.setEmail(func.getFuncionario().getPessoa().getEmail());
			destinatario.setNome(func.getFuncionario().getPessoa().getNome());
			destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
			comunicacaoInterna.adicionarObjComunicadoInternoDestinatarioVOs(destinatario);
		}
	}

	public void adicionarUsuarioLogadoGrupoEmail(TurmaVO turmaVO, UsuarioVO usuario, Integer unidadeEnsino) throws Exception {
		try {
			FuncionarioVO funcionarioVO = (getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuario.getPessoa().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			Boolean jaPossuiFuncionario = false;
			for (FuncionarioGrupoDestinatariosVO obj : turmaVO.getGrupoDestinatarios().getListaFuncionariosGrupoDestinatariosVOs()) {
				if (obj.getFuncionario().getCodigo().equals(funcionarioVO.getCodigo())) {
					jaPossuiFuncionario = true;
				}
			}
			if (!jaPossuiFuncionario) {
				FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO = new FuncionarioGrupoDestinatariosVO();
				funcionarioGrupoDestinatariosVO.setFuncionario(funcionarioVO);
				funcionarioGrupoDestinatariosVO.setGrupoDestinatarios(turmaVO.getGrupoDestinatarios().getCodigo());
				turmaVO.getGrupoDestinatarios().getListaFuncionariosGrupoDestinatariosVOs().add(funcionarioGrupoDestinatariosVO);
			}
		} catch (Exception e) {

		}
	}

	public void adicionarCoordenadoresCursoGrupoEmail(TurmaVO turmaVO, UsuarioVO usuario, Integer unidadeEnsino) throws Exception {
		turmaVO.getCurso().setCursoCoordenadorVOs(getFacadeFactory().getCursoCoordenadorFacade().consultarPorCodigoCurso(turmaVO.getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		for (CursoCoordenadorVO obj : turmaVO.getCurso().getCursoCoordenadorVOs()) {
			if (obj.getTurma().getCodigo() == 0) {
				FuncionarioVO funcionarioVO = (getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getFuncionario().getPessoa().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
				FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO = new FuncionarioGrupoDestinatariosVO();
				funcionarioGrupoDestinatariosVO.setFuncionario(funcionarioVO);
				funcionarioGrupoDestinatariosVO.setGrupoDestinatarios(turmaVO.getGrupoDestinatarios().getCodigo());
				turmaVO.getGrupoDestinatarios().getListaFuncionariosGrupoDestinatariosVOs().add(funcionarioGrupoDestinatariosVO);
			} else {
				if (obj.getTurma().getCodigo().equals(turmaVO.getCodigo())) {
					FuncionarioVO funcionarioVO = (getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getFuncionario().getPessoa().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
					FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO = new FuncionarioGrupoDestinatariosVO();
					funcionarioGrupoDestinatariosVO.setFuncionario(funcionarioVO);
					funcionarioGrupoDestinatariosVO.setGrupoDestinatarios(turmaVO.getGrupoDestinatarios().getCodigo());
					turmaVO.getGrupoDestinatarios().getListaFuncionariosGrupoDestinatariosVOs().add(funcionarioGrupoDestinatariosVO);
				}
			}
		}
	}
}
