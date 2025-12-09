package negocio.facade.jdbc.administrativo;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.poi.hslf.dev.SlideAndNotesAtomListing;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import java.io.InputStream;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import controle.arquitetura.DataModelo;
import jobs.JobEnvioPushAplicativo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaArquivoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.TextoPadraoBancoCurriculumVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.ProvisaoCustoVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.administrativo.ComunicacaoInternaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ComunicacaoInternaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ComunicacaoInternaVO</code>. Encapsula toda a interação com o
 * banco de dados.
 * 
 * @see ComunicacaoInternaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ComunicacaoInterna extends ControleAcesso implements ComunicacaoInternaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ComunicacaoInterna() throws Exception {
		super();
		setIdEntidade("ComunicacaoInterna");

	}

	public void validarUsuarioConsultarMeusAmigos(UsuarioVO usuario) throws Exception {
		setIdEntidade("MeusAmigos");
		ComunicacaoInterna.consultar(getIdEntidade(), false, usuario);
		setIdEntidade("ComunicacaoInterna");
	}

	public void validarUsuarioConsultarMeusProfessores(UsuarioVO usuario) throws Exception {
		setIdEntidade("MeusProfessores");
		ComunicacaoInterna.consultar(getIdEntidade(), false, usuario);
		setIdEntidade("ComunicacaoInterna");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ComunicacaoInternaVO</code>.
	 */
	public ComunicacaoInternaVO novo() throws Exception {
		ComunicacaoInterna.incluir(getIdEntidade());
		ComunicacaoInternaVO obj = new ComunicacaoInternaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void enviarEmailComunicacaoInterna(ComunicacaoInternaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO config, PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO, ProgressBarVO progressBarVO, boolean enviarEmailMultiplosDestinatarios) throws Exception {
		if(Uteis.isAtributoPreenchido(personalizacaoMensagemAutomaticaVO)) {
			obj.setEnviarEmail(personalizacaoMensagemAutomaticaVO.getEnviarEmail());
			obj.setEnviarEmailInstitucional(personalizacaoMensagemAutomaticaVO.getEnviarEmailInstitucional());
		}
		if (obj.getEnviarEmail() || obj.getEnviarEmailInstitucional()) {
			List<File> listaAnexos = new ArrayList<File>();
			try {
				criarFileCorpoMensagemEmail(obj);
				File file = null;
				if (obj.getListaArquivosAnexo().size() > 1) {
					Iterator<ArquivoVO> i = obj.getListaArquivosAnexo().iterator();
					while (i.hasNext()) {
						ArquivoVO arq = (ArquivoVO) i.next();
						listaAnexos.add(getFacadeFactory().getArquivoHelper().buscarArquivoDiretorioFixo(arq, config));
					}
				} else {
					file= getFacadeFactory().getArquivoHelper().buscarArquivoDiretorioFixo(obj.getArquivoAnexo(), config);
				}
				if (file == null && !listaAnexos.isEmpty()) {
					if (enviarEmailMultiplosDestinatarios 
							&& obj.getComunicadoInternoDestinatarioVOs().size() > 1) {
						getFacadeFactory().getEmailFacade().realizarGravacaoEmailMultiplosDestinatarios(obj, listaAnexos, true, usuario, progressBarVO, config);
					} else {
						getFacadeFactory().getEmailFacade().realizarGravacaoEmail(obj, listaAnexos, true, usuario, progressBarVO);
					}
				} else if (file.getName().equals("")) {
					if (enviarEmailMultiplosDestinatarios 
							&& obj.getComunicadoInternoDestinatarioVOs().size() > 1) {
						getFacadeFactory().getEmailFacade().realizarGravacaoEmailMultiplosDestinatarios(obj, null, true, usuario, progressBarVO, config);
					} else {
						getFacadeFactory().getEmailFacade().realizarGravacaoEmail(obj, null, true, usuario, progressBarVO);
					}
				} else {
					listaAnexos.add(file);
					if (enviarEmailMultiplosDestinatarios 
							&& obj.getComunicadoInternoDestinatarioVOs().size() > 1) {
						getFacadeFactory().getEmailFacade().realizarGravacaoEmailMultiplosDestinatarios(obj, listaAnexos, true, usuario, progressBarVO, config);
					} else {
						getFacadeFactory().getEmailFacade().realizarGravacaoEmail(obj, listaAnexos, true, usuario, progressBarVO);
					}
				}
				listaAnexos = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		if (obj.getEnviarSMS()) {
			try {
				getFacadeFactory().getSmsFacade().realizarGravacaoSMS(obj, usuario);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ComunicacaoInternaVO obj, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ProgressBarVO progressBarVO, boolean enviarEmailMultiplosDestinatarios) throws Exception {
		try {
			ComunicacaoInterna.incluir(getIdEntidade(), controlarAcesso, usuario);
			ComunicacaoInternaVO.validarDados(obj);
//			validarUnicidadeMensagemTipoLeituraObrigatoriaTipoMarketing(obj.getTipoLeituraObrigatoria(), obj.getTipoMarketing(), obj.getTipoDestinatario());
			boolean inserirLista = false;
			if (!obj.getListaArquivosAnexo().isEmpty()) {
				inserirLista = true;
				obj.setArquivoAnexo(obj.getListaArquivosAnexo().get(0));
			}
			if (obj.getArquivoAnexo().getPastaBaseArquivoEnum() != null) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoAnexo(), false, usuario, configuracaoGeralSistemaVO);
			}
			obj.setMensagem(this.trocarTagAssinatura(obj, configuracaoGeralSistemaVO, usuario));
			if(progressBarVO != null) {
				progressBarVO.setStatus("Incluindo Comunicado Interno");	
			}			
			final String sql = " INSERT INTO ComunicacaoInterna( data, responsavel, tipoComunicadoInterno, tipoDestinatario, funcionario, cargo, departamento, " + " unidadeEnsino, turma, areaConhecimento, aluno, professor, assunto, mensagem, comunicadoInternoOrigem, dataExibicaoInicial, " +	" dataExibicaoFinal, prioridade, enviarEmail,  tipoMarketing, tipoLeituraObrigatoria, digitarMensagem, " + 
			" removerCaixaSaida, arquivoAnexo, tipoRemetente, disciplina, " + " codigoTipoOrigemComunicacaoInterna, tipoOrigemComunicacaoInternaEnum, coordenador, mensagemSMS, enviarSMS, enviaremailinstitucional " + " ) VALUES ( ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setString(3, obj.getTipoComunicadoInterno());
					sqlInserir.setString(4, obj.getTipoDestinatario());
					if (obj.getFuncionario().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getFuncionario().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getCargo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getCargo().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (obj.getDepartamento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getDepartamento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(8, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(8, 0);
					}
					if (obj.getTurma().getCodigo().intValue() != 0) {
						sqlInserir.setInt(9, obj.getTurma().getCodigo().intValue());
					} else {
						sqlInserir.setNull(9, 0);
					}
					if (obj.getAreaConhecimento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(10, obj.getAreaConhecimento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(10, 0);
					}
					if (obj.getAluno().getCodigo().intValue() != 0) {
						sqlInserir.setInt(11, obj.getAluno().getCodigo().intValue());
					} else {
						sqlInserir.setNull(11, 0);
					}
					if (obj.getProfessor().getCodigo().intValue() != 0) {
						sqlInserir.setInt(12, obj.getProfessor().getCodigo().intValue());
					} else {
						sqlInserir.setNull(12, 0);
					}
					sqlInserir.setString(13, obj.getAssunto());
					sqlInserir.setString(14, obj.getMensagem());
					if (obj.getComunicadoInternoOrigem().getCodigo().intValue() != 0) {
						sqlInserir.setInt(15, obj.getComunicadoInternoOrigem().getCodigo().intValue());
					} else {
						sqlInserir.setNull(15, 0);
					}
					sqlInserir.setDate(16, Uteis.getDataJDBC(obj.getDataExibicaoInicial()));
					sqlInserir.setDate(17, Uteis.getDataJDBC(obj.getDataExibicaoFinal()));
					sqlInserir.setString(18, obj.getPrioridade());
					sqlInserir.setBoolean(19, obj.isEnviarEmail().booleanValue());

					sqlInserir.setBoolean(20, obj.getTipoMarketing());
					sqlInserir.setBoolean(21, obj.getTipoLeituraObrigatoria());
					sqlInserir.setBoolean(22, obj.getDigitarMensagem());
					sqlInserir.setBoolean(23, obj.getRemoverCaixaSaida());
					if (obj.getArquivoAnexo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(24, obj.getArquivoAnexo().getCodigo().intValue());
					} else {
						sqlInserir.setNull(24, 0);
					}
					sqlInserir.setString(25, obj.getTipoRemetente());
					if (obj.getDisciplina().getCodigo().intValue() != 0) {
						sqlInserir.setInt(26, obj.getDisciplina().getCodigo().intValue());
					} else {
						sqlInserir.setNull(26, 0);
					}
					sqlInserir.setInt(27, obj.getCodigoTipoOrigemComunicacaoInterna());
					sqlInserir.setString(28, obj.getTipoOrigemComunicacaoInternaEnum().name());
					if (obj.getCoordenador().getCodigo().intValue() != 0) {
						sqlInserir.setInt(29, obj.getCoordenador().getCodigo().intValue());
					} else {
						sqlInserir.setNull(29, 0);
					}
					sqlInserir.setString(30, obj.getMensagemSMS());
					sqlInserir.setBoolean(31, obj.getEnviarSMS().booleanValue());
					sqlInserir.setBoolean(32, obj.getEnviarEmailInstitucional());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(false);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			if (inserirLista) {
				getFacadeFactory().getComunicacaoInternaArquivoFacade().incluirLista(obj, usuario, configuracaoGeralSistemaVO);
			}

			if(progressBarVO != null) {
				progressBarVO.setStatus("Realizando contagem de destinatários, por favor aguarde...");
			}
			getFacadeFactory().getComunicadoInternoDestinatarioFacade().incluirComunicadoInternoDestinatarios(obj, obj.getComunicadoInternoDestinatarioVOs(), usuario);
			if(progressBarVO != null) {
				progressBarVO.setStatus("Incluindo destinatários...");
			}
			enviarEmailComunicacaoInterna(obj, usuario, configuracaoGeralSistemaVO, obj.getPersonalizacaoMensagemAutomaticaVO(), progressBarVO, enviarEmailMultiplosDestinatarios);
			if (usuario != null && usuario.getVisaoLogar().equals("professor") && configuracaoGeralSistemaVO.getMonitorarMensagensProfessor()) {
				monitorarMensagensProfessor(obj, usuario, configuracaoGeralSistemaVO);
			}
			
			if ((usuario != null && (usuario.getVisaoLogar().equals("professor") || usuario.getVisaoLogar().equals("coordenador")) && !usuario.getPessoa().getCodigo().equals(obj.getProfessor().getCodigo()))) {
				adicionarCopiaDestinatarioRemetente2(obj, usuario, configuracaoGeralSistemaVO);
			}
			if (Uteis.validarEnvioEmail((configuracaoGeralSistemaVO.getIpServidor()))) {
				JobEnvioPushAplicativo jobEnvioPushAplicativo = new JobEnvioPushAplicativo(obj, usuario);
				Thread thread = new Thread(jobEnvioPushAplicativo);
				thread.start();			
			}
			if(progressBarVO != null) {
				progressBarVO.setStatus("Finalizando...");
			}
			obj.getListaConsultaDestinatario().setTotalRegistrosEncontrados(obj.getComunicadoInternoDestinatarioVOs().size());
			List<ComunicadoInternoDestinatarioVO> comunicadoInternoDestinatarioVOs = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
			int x= 0;
			for(ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO: obj.getComunicadoInternoDestinatarioVOs()) {
				if(x >= 10) {
					break;
				}
				comunicadoInternoDestinatarioVOs.add(comunicadoInternoDestinatarioVO);
				x++;
			}
			obj.getListaConsultaDestinatario().setListaConsulta(comunicadoInternoDestinatarioVOs);
			Uteis.liberarListaMemoria(obj.getComunicadoInternoDestinatarioVOs());
		} catch (Exception e) {
			throw e;
		} 
	}

	public String trocarTagAssinatura(ComunicacaoInternaVO obj, ConfiguracaoGeralSistemaVO conf, UsuarioVO usuario) throws Exception {
		try {
			if (obj.getResponsavel().getCodigo().intValue() != 0) {
				if (obj.getMensagem().contains("TAG_ASSINATURA")) {
					FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getResponsavel().getCodigo(), 0, Boolean.FALSE, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);				
					String urlImagem = getUrlAssinaturaApresentar(func, conf);
					obj.setMensagem(obj.getMensagem().replaceAll("TAG_ASSINATURA", "<img src=\"" + urlImagem + "\" width=\"140\" height=\"50\" />"));
					// <img
					// src="http://localhost:8090/assinatura/20943_1376058236534.png"
					// alt="" width="137" height="47" />
				}
			}
			return obj.getMensagem();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public String substituirTag(String mensagem, PessoaVO pessoaVO) throws Exception {
	try {
		if(Uteis.isAtributoPreenchido(pessoaVO)) {
		if (mensagem.contains("NOME_DESTINATARIO")) {
			mensagem = mensagem.replaceAll("NOME_DESTINATARIO", pessoaVO.getNome());
		}
		if (mensagem.contains("MATRICULA_ALUNO")) {
			String consultarUltimaMatricula =  getFacadeFactory().getMatriculaFacade().consultarUltimaMatriculaAtivaPorCodigoPessoa(pessoaVO.getCodigo());
			mensagem = mensagem.replaceAll("MATRICULA_ALUNO", consultarUltimaMatricula);
		}
		if (mensagem.contains("EMAIL_INSTITUCIONAL")) {
			PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(pessoaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			mensagem = mensagem.replaceAll("EMAIL_INSTITUCIONAL", pessoaEmailInstitucionalVO.getEmail());
		}
		}
		return mensagem;
	} catch (Exception e) {
		throw e;
	}
}
	

	public String getUrlAssinaturaApresentar(FuncionarioVO obj, ConfiguracaoGeralSistemaVO conf) {
		try {
			if(this.context() != null){
			if (obj.getArquivoAssinaturaVO().getCodigo().intValue() == 0) {
				return getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(obj.getArquivoAssinaturaVO(), PastaBaseArquivoEnum.ASSINATURA_TMP.getValue(), conf, getCaminhoPastaWeb(), "", false);
			} else {
				return getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(obj.getArquivoAssinaturaVO(), PastaBaseArquivoEnum.ASSINATURA.getValue(), conf, getCaminhoPastaWeb(), "", false);
			}
			}
			return "";
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void adicionarCopiaDestinatarioRemetente2(ComunicacaoInternaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			if (!usuario.getPessoa().getEmail().equals("")) {
				int qtdeMaximaComunicadoInternoDestinatario = 0;
				if (!configuracaoGeralSistemaVO.getEmailConfirmacaoEnvioComunicado().equals("")) {
					qtdeMaximaComunicadoInternoDestinatario = 2;
				} else {
					qtdeMaximaComunicadoInternoDestinatario = 1;
				}
				ComunicacaoInternaVO comunicacaoInternaVO = (ComunicacaoInternaVO) obj.clone();
				comunicacaoInternaVO.setTipoDestinatario("PR");
				comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().clear();
				for (int num = 0; num < qtdeMaximaComunicadoInternoDestinatario; num++) {
					ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
					comunicadoInternoDestinatarioVO.setTipoComunicadoInterno(comunicacaoInternaVO.getTipoComunicadoInterno());
					comunicadoInternoDestinatarioVO.setComunicadoInterno(comunicacaoInternaVO.getCodigo());
					if (num == 0) {
						comunicadoInternoDestinatarioVO.setDestinatario(usuario.getPessoa());
					} else {
						comunicadoInternoDestinatarioVO.getDestinatario().setEmail(configuracaoGeralSistemaVO.getEmailConfirmacaoEnvioComunicado());
						comunicadoInternoDestinatarioVO.getDestinatario().setNome("Email Confirmação Envio Comunicado");
					}
					comunicacaoInternaVO.adicionarObjComunicadoInternoDestinatarioVOs(comunicadoInternoDestinatarioVO);
				}
				if(Uteis.isAtributoPreenchido(obj.getPersonalizacaoMensagemAutomaticaVO())) {
					comunicacaoInternaVO.setEnviarEmail(obj.getPersonalizacaoMensagemAutomaticaVO().getEnviarEmail());
					comunicacaoInternaVO.setEnviarEmailInstitucional(obj.getPersonalizacaoMensagemAutomaticaVO().getEnviarEmailInstitucional());
				}
				// criarFileCorpoMensagemEmail(obj);
				
				File file = getFacadeFactory().getArquivoHelper().buscarArquivoDiretorioFixo(obj.getArquivoAnexo(), configuracaoGeralSistemaVO);
				if (file.getName().equals("")) {
					// Thread jobEmail = new Thread(new
					// JobEmailComunicacaoInterna(configuracaoGeralSistemaVO,
					// obj, obj.getAssunto(), usuario, null, mensagemSistema,
					// true, obj.getListaFileCorpoMensagem()), "JobEmail");
					// jobEmail.start();
					getFacadeFactory().getEmailFacade().realizarGravacaoEmail(obj, null, true, usuario, null);

				} else {
					// Thread jobEmail = new Thread(new
					// JobEmailComunicacaoInterna(configuracaoGeralSistemaVO,
					// obj, obj.getAssunto(), usuario, file, mensagemSistema,
					// true, obj.getListaFileCorpoMensagem()), "JobEmail");
					// jobEmail.start();
					List<File> listaAnexos = new ArrayList<File>();
					listaAnexos.add(file);
					getFacadeFactory().getEmailFacade().realizarGravacaoEmail(obj, listaAnexos, true, usuario, null);
				}
			}
		} catch (Exception e) {
			////System.out.print("Erro comunicacaoInterna...");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void monitorarMensagensProfessor(ComunicacaoInternaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			if (!configuracaoGeralSistemaVO.getEmailRemetente().equals("")) {
				String listaDestinatarios = "Mensagem enviada para os seguintes destinatários: <br>";
				ComunicacaoInternaVO comunicacaoInternaVO = (ComunicacaoInternaVO) obj.clone();
				for (ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario : obj.getComunicadoInternoDestinatarioVOs()) {
					if (listaDestinatarios.length() > 54) {
						listaDestinatarios += "<br>";
					}
					listaDestinatarios += "Nome: " + comunicadoInternoDestinatario.getDestinatario().getNome() + "  /  Email: " + comunicadoInternoDestinatario.getDestinatario().getEmail();
				}
				listaDestinatarios += "<br><br>";
				if(Uteis.isAtributoPreenchido(obj.getPersonalizacaoMensagemAutomaticaVO())) {
					comunicacaoInternaVO.setEnviarEmail(obj.getPersonalizacaoMensagemAutomaticaVO().getEnviarEmail());
					comunicacaoInternaVO.setEnviarEmailInstitucional(obj.getPersonalizacaoMensagemAutomaticaVO().getEnviarEmailInstitucional());
				}
				comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(new ArrayList<ComunicadoInternoDestinatarioVO>());
				ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
				comunicadoInternoDestinatarioVO.setTipoComunicadoInterno(comunicacaoInternaVO.getTipoComunicadoInterno());
				comunicadoInternoDestinatarioVO.setComunicadoInterno(obj.getCodigo());
				comunicadoInternoDestinatarioVO.getDestinatario().setEmail(configuracaoGeralSistemaVO.getEmailRemetente());
				comunicadoInternoDestinatarioVO.getDestinatario().setNome("Sistema");
				comunicacaoInternaVO.adicionarObjComunicadoInternoDestinatarioVOs(comunicadoInternoDestinatarioVO);

				StringBuffer mensagemSemHtml = new StringBuffer(comunicacaoInternaVO.getMensagem());
				String inicioMensagem = mensagemSemHtml.substring(0, 726);
				mensagemSemHtml.delete(0, 726);
				String remover = "<tr style=\"height: 43.5pt;\">";
				int pos = mensagemSemHtml.indexOf(remover);
				String fimMensagem = mensagemSemHtml.substring(pos - 13, mensagemSemHtml.length());
				mensagemSemHtml.delete(pos - 13, mensagemSemHtml.length());
				String mensagemSistema = inicioMensagem + listaDestinatarios + mensagemSemHtml + fimMensagem;

				// criarFileCorpoMensagemEmail(obj);
				File file = getFacadeFactory().getArquivoHelper().buscarArquivoDiretorioFixo(obj.getArquivoAnexo(), configuracaoGeralSistemaVO);				
				if (file.getName().equals("")) {
					// Thread jobEmail = new Thread(new
					// JobEmailComunicacaoInterna(configuracaoGeralSistemaVO,
					// obj, obj.getAssunto(), usuario, null, mensagemSistema,
					// true, obj.getListaFileCorpoMensagem()), "JobEmail");
					// jobEmail.start();
					getFacadeFactory().getEmailFacade().realizarGravacaoEmail(obj, null, true, usuario, null);

				} else {
					// Thread jobEmail = new Thread(new
					// JobEmailComunicacaoInterna(configuracaoGeralSistemaVO,
					// obj, obj.getAssunto(), usuario, file, mensagemSistema,
					// true, obj.getListaFileCorpoMensagem()), "JobEmail");
					// jobEmail.start();
					List<File> listaAnexos = new ArrayList<File>();
					listaAnexos.add(file);
					getFacadeFactory().getEmailFacade().realizarGravacaoEmail(obj, listaAnexos, true, usuario, null);
				}
			}
		} catch (Exception e) {
			////System.out.print("Erro comunicacaoInterna...");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void adicionarCopiaDestinatarioRemetente(ComunicacaoInternaVO comunicacaoInternaVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			if (!usuario.getPessoa().getEmail().equals("")) {
				int qtdeMaximaComunicadoInternoDestinatario = 0;
				if (!configuracaoGeralSistemaVO.getEmailConfirmacaoEnvioComunicado().equals("")) {
					qtdeMaximaComunicadoInternoDestinatario = 2;
				} else {
					qtdeMaximaComunicadoInternoDestinatario = 1;
				}
				for (int num = 0; num < qtdeMaximaComunicadoInternoDestinatario; num++) {
					ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
					comunicadoInternoDestinatarioVO.setTipoComunicadoInterno(comunicacaoInternaVO.getTipoComunicadoInterno());
					comunicadoInternoDestinatarioVO.setComunicadoInterno(comunicacaoInternaVO.getCodigo());
					if (num == 0) {
						comunicadoInternoDestinatarioVO.setDestinatario(usuario.getPessoa());
					} else {
						comunicadoInternoDestinatarioVO.getDestinatario().setEmail(configuracaoGeralSistemaVO.getEmailConfirmacaoEnvioComunicado());
						comunicadoInternoDestinatarioVO.getDestinatario().setNome("Email Confirmação Envio Comunicado");
					}
					comunicacaoInternaVO.adicionarObjComunicadoInternoDestinatarioVOs(comunicadoInternoDestinatarioVO);
				}
			}
		} catch (Exception e) {
			////System.out.print("Erro comunicacaoInterna...");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarUnicidadeMensagemTipoLeituraObrigatoriaTipoMarketing(Boolean tipoLeituraObrigatoria, Boolean tipoMarketing, String tipoDestinatario) throws Exception {
		if (tipoLeituraObrigatoria) {
			Boolean jaExisteMensagemLeituraObrigatoria = consultarMensagemTipoLeituraObrigatoria(tipoDestinatario);
			if (jaExisteMensagemLeituraObrigatoria) {
				throw new Exception("Já existe uma mensagem do tipo LEITURA OBRIGATÓRIA com a data de exibição posterior a data atual. " + "Para que você possa criar uma nova mensagem, deve-se cancelar a apresentação do Comunicado.");
			}
		}
		if (tipoMarketing) {
			Boolean jaExisteMensagemMarketing = consultarMensagemTipoMarketing(tipoDestinatario);
			if (jaExisteMensagemMarketing) {
				throw new Exception("Já existe uma mensagem do tipo MARKETING com a data de exibição posterior a data atual. " + "Para que você possa criar uma nova mensagem, deve-se cancelar a apresentação do Comunicado.");
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataExibicaoComunicadoInterno(final Integer codigo, final Date dataExibicaoFinal, final Integer responsavelCancelamento, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE ComunicacaoInterna set dataExibicaoFinal=?, responsavelCancelamento=?, dataCancelamento=?  WHERE ((codigo = ?)) "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(dataExibicaoFinal));
					sqlAlterar.setInt(2, responsavelCancelamento);
					sqlAlterar.setDate(3, Uteis.getDataJDBC(new Date()));
					sqlAlterar.setInt(4, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean consultarMensagemTipoLeituraObrigatoria(String tipoDestinatario) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM comunicacaointerna ");
		sqlStr.append(" WHERE dataExibicaoFinal >= '");
		sqlStr.append(Uteis.getDataJDBC(new Date()));
		sqlStr.append("' AND tipoLeituraObrigatoria = true ");
		sqlStr.append(" AND tipoDestinatario = '");
		sqlStr.append(tipoDestinatario);
		sqlStr.append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean consultarMensagemTipoMarketing(String tipoDestinatario) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM comunicacaointerna ");
		sqlStr.append(" WHERE dataExibicaoFinal >= '");
		sqlStr.append(Uteis.getDataJDBC(new Date()));
		sqlStr.append("' AND tipoMarketing = true ");
		sqlStr.append(" AND tipoDestinatario = '");
		sqlStr.append(tipoDestinatario);
		sqlStr.append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void responder(final ComunicacaoInternaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			ComunicacaoInterna.incluir(getIdEntidade(), true, usuario);
			ComunicacaoInternaVO.validarDados(obj);
			boolean inserirLista = false;
			if (!obj.getListaArquivosAnexo().isEmpty()) {
				if (obj.getListaArquivosAnexo().size() > 1) {
					inserirLista = true;
				} else {
					obj.setArquivoAnexo(obj.getListaArquivosAnexo().get(0));
				}
			}
			
			if (obj.getArquivoAnexo().getPastaBaseArquivoEnum() != null) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoAnexo(), false, usuario, configuracaoGeralSistemaVO);
			}
			
			final String sql = "INSERT INTO ComunicacaoInterna( data, responsavel, tipoComunicadoInterno, tipoDestinatario, funcionario, cargo, departamento, " + "unidadeEnsino, turma, areaConhecimento, aluno, professor, assunto, mensagem, comunicadoInternoOrigem, dataExibicaoInicial, " + "dataExibicaoFinal, prioridade, enviarEmail,  tipoMarketing, tipoLeituraObrigatoria, digitarMensagem, " + "removerCaixaSaida, arquivoAnexo, tipoRemetente, disciplina, " + "codigoTipoOrigemComunicacaoInterna, tipoOrigemComunicacaoInternaEnum, mensagemSMS, enviarSMS " + " ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setString(3, obj.getTipoComunicadoInterno());
					sqlInserir.setString(4, obj.getTipoDestinatario());
					if (obj.getFuncionario().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getFuncionario().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getCargo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getCargo().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (obj.getDepartamento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getDepartamento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(8, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(8, 0);
					}
					if (obj.getTurma().getCodigo().intValue() != 0) {
						sqlInserir.setInt(9, obj.getTurma().getCodigo().intValue());
					} else {
						sqlInserir.setNull(9, 0);
					}
					if (obj.getAreaConhecimento().getCodigo().intValue() != 0) {
						sqlInserir.setInt(10, obj.getAreaConhecimento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(10, 0);
					}
					if (obj.getAluno().getCodigo().intValue() != 0) {
						sqlInserir.setInt(11, obj.getAluno().getCodigo().intValue());
					} else {
						sqlInserir.setNull(11, 0);
					}
					if (obj.getProfessor().getCodigo().intValue() != 0) {
						sqlInserir.setInt(12, obj.getProfessor().getCodigo().intValue());
					} else {
						sqlInserir.setNull(12, 0);
					}
					sqlInserir.setString(13, obj.getAssunto());
					sqlInserir.setString(14, obj.getMensagem());
					if (obj.getComunicadoInternoOrigem().getCodigo().intValue() != 0) {
						sqlInserir.setInt(15, obj.getComunicadoInternoOrigem().getCodigo().intValue());
					} else {
						sqlInserir.setNull(15, 0);
					}
					sqlInserir.setDate(16, Uteis.getDataJDBC(obj.getDataExibicaoInicial()));
					sqlInserir.setDate(17, Uteis.getDataJDBC(obj.getDataExibicaoFinal()));
					sqlInserir.setString(18, obj.getPrioridade());
					sqlInserir.setBoolean(19, obj.isEnviarEmail().booleanValue());
					sqlInserir.setBoolean(20, obj.getTipoMarketing());
					sqlInserir.setBoolean(21, obj.getTipoLeituraObrigatoria());
					sqlInserir.setBoolean(22, obj.getDigitarMensagem());
					sqlInserir.setBoolean(23, obj.getRemoverCaixaSaida());
					if (obj.getArquivoAnexo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(24, obj.getArquivoAnexo().getCodigo().intValue());
					} else {
						sqlInserir.setNull(24, 0);
					}
					sqlInserir.setString(25, obj.getTipoRemetente());
					if (obj.getDisciplina().getCodigo().intValue() != 0) {
						sqlInserir.setInt(26, obj.getDisciplina().getCodigo().intValue());
					} else {
						sqlInserir.setNull(26, 0);
					}
					sqlInserir.setInt(27, obj.getCodigoTipoOrigemComunicacaoInterna());
					sqlInserir.setString(28, obj.getTipoOrigemComunicacaoInternaEnum().name());
					sqlInserir.setString(29, obj.getMensagemSMS());
					sqlInserir.setBoolean(30, obj.getEnviarSMS().booleanValue());

					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(false);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			if (inserirLista) {
				new ComunicacaoInternaArquivo().incluirLista(obj, usuario, configuracaoGeralSistemaVO);
			}
			getFacadeFactory().getComunicadoInternoDestinatarioFacade().incluirComunicadoInternoDestinatarios(obj, obj.getComunicadoInternoDestinatarioVOs(), usuario);
			getFacadeFactory().getComunicacaoInternaFacade().registrarRespostaComunicadoInterno(obj, obj.getResponsavel());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ComunicacaoInternaVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ComunicacaoInternaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ComunicacaoInternaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			ComunicacaoInternaVO.validarDados(obj);
			ComunicacaoInterna.alterar(getIdEntidade(), true, usuario);
			obj.setMensagem(this.trocarTagAssinatura(obj, configuracaoGeralSistemaVO, usuario));
			final String sql = "UPDATE ComunicacaoInterna set data=?, responsavel=?, tipoComunicadoInterno=?, tipoDestinatario=?, funcionario=?, cargo=?, " + "departamento=?, unidadeEnsino=?, turma=?, areaConhecimento=?, aluno=?, professor=?, assunto=?, mensagem=?, comunicadoInternoOrigem=?, " + "dataExibicaoInicial=?, dataExibicaoFinal=?, prioridade=?, enviarEmail=?,  tipoMarketing=?, tipoLeituraObrigatoria=?, " + "digitarMensagem=?, removerCaixaSaida=?, arquivoAnexo=?, tipoRemetente=?, disciplina = ?, " + "codigoTipoOrigemComunicacaoInterna=?, tipoOrigemComunicacaoInternaEnum =?, mensagemSMS=?, enviarSMS=?, enviaremailinstitucional=? " + " WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setString(3, obj.getTipoComunicadoInterno());
					sqlAlterar.setString(4, obj.getTipoDestinatario());
					if (obj.getFuncionario().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getFuncionario().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (obj.getCargo().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getCargo().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getDepartamento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getDepartamento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(8, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(8, 0);
					}
					if (obj.getTurma().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(9, obj.getTurma().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(9, 0);
					}
					if (obj.getAreaConhecimento().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(10, obj.getAreaConhecimento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					if (obj.getAluno().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(11, obj.getAluno().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					if (obj.getProfessor().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(12, obj.getProfessor().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(12, 0);
					}
					sqlAlterar.setString(13, obj.getAssunto());
					sqlAlterar.setString(14, obj.getMensagem());
					if (obj.getComunicadoInternoOrigem().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(15, obj.getComunicadoInternoOrigem().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(15, 0);
					}
					sqlAlterar.setDate(16, Uteis.getDataJDBC(obj.getDataExibicaoInicial()));
					sqlAlterar.setDate(17, Uteis.getDataJDBC(obj.getDataExibicaoFinal()));
					sqlAlterar.setString(18, obj.getPrioridade());
					sqlAlterar.setBoolean(19, obj.isEnviarEmail().booleanValue());
					sqlAlterar.setBoolean(20, obj.getTipoMarketing());
					sqlAlterar.setBoolean(21, obj.getTipoLeituraObrigatoria());
					sqlAlterar.setBoolean(22, obj.getDigitarMensagem());
					sqlAlterar.setBoolean(23, obj.getRemoverCaixaSaida());
					if (obj.getArquivoAnexo().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(24, obj.getArquivoAnexo().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(24, 0);
					}
					sqlAlterar.setString(25, obj.getTipoRemetente());
					if (obj.getDisciplina().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(26, obj.getDisciplina().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(26, 0);
					}
					sqlAlterar.setInt(27, obj.getCodigoTipoOrigemComunicacaoInterna());
					sqlAlterar.setString(28, obj.getTipoOrigemComunicacaoInternaEnum().name());
					sqlAlterar.setString(29, obj.getMensagemSMS());
					sqlAlterar.setBoolean(30, obj.getEnviarSMS().booleanValue());
					sqlAlterar.setBoolean(31, obj.getEnviarEmailInstitucional());
					sqlAlterar.setInt(32, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			if (usuario.getVisaoLogar().equals("professor") || usuario.getVisaoLogar().equals("coordenador")) {
				// adicionarCopiaDestinatarioRemetente(obj, usuario,
				// configuracaoGeralSistemaVO);
			}
			getFacadeFactory().getComunicadoInternoDestinatarioFacade().alterarComunicadoInternoDestinatarios(obj, obj.getComunicadoInternoDestinatarioVOs(), usuario);
			if (usuario.getVisaoLogar().equals("professor") && configuracaoGeralSistemaVO.getMonitorarMensagensProfessor()) {
				monitorarMensagensProfessor(obj, usuario, configuracaoGeralSistemaVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void registrarLeituraComunicadoInterno(ComunicacaoInternaVO obj, PessoaVO pessoa, UsuarioVO usuarioLogado) throws Exception {
		try {
			getFacadeFactory().getComunicadoInternoDestinatarioFacade().alterarMarcarComoLida(pessoa.getCodigo(), obj.getCodigo(), true, usuarioLogado);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void registrarRespostaComunicadoInterno(ComunicacaoInternaVO obj, PessoaVO pessoa) throws Exception {
		if (obj.getComunicadoInternoOrigem().getCodigo() != null && obj.getComunicadoInternoOrigem().getCodigo() > 0) {
			getFacadeFactory().getComunicadoInternoDestinatarioFacade().alterarComunicadoDestinatarioMensagemComoLidaERespondida(obj.getComunicadoInternoOrigem().getCodigo(), obj.getResponsavel().getCodigo());
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ComunicacaoInternaVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ComunicacaoInternaVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ComunicacaoInternaVO obj,UsuarioVO usuarioVO) throws Exception {
		try {
			ComunicacaoInterna.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM ComunicacaoInterna WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getComunicadoInternoDestinatarioFacade().excluirComunicadoInternoDestinatarios(obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarRemoverCaixaSaida(final Integer comunicado, final Boolean remover, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE ComunicacaoInterna set removerCaixaSaida=? WHERE (codigo = ? )"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setBoolean(1, remover.booleanValue());
					sqlAlterar.setInt(2, comunicado.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	// public static void consultarArquivo(ComunicacaoInternaVO obj) throws
	// Exception {
	// String caminho = new SuperControle().obterCaminhoWebFotos() +
	// File.separator + obj.getNomeArquivo();
	// File file1 = new File(caminho);
	// file1.delete();
	// obj.setArquivo(caminho);
	// obj.setArquivo(obj.getArquivo().replace("\\", "/"));
	// String sql = "SELECT lo_export(arquivo, '" + obj.getArquivo() + "') " +
	// "FROM ComunicacaoInterna " +
	// "WHERE codigo = " + obj.getCodigo().intValue();
	// Statement stm = con.createStatement();
	// stm.executeQuery(sql);
	// }
	/** Metodo que retorna codigo da Pessoa Destinatário */
	public List<ComunicadoInternoDestinatarioVO> consultaComunicadoInternoDestinatario(ComunicacaoInternaVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		ComunicacaoInternaVO.validarDadosParaConsultaComunicadoInternoDestinatario(obj);
		StringBuilder sqlStr = new StringBuilder("SELECT PE.codigo, PE.nome, PE.email FROM Pessoa PE ");
		if (obj.getTipoDestinatario().equals("AL")) {
			if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
				sqlStr.append(" INNER JOIN Matricula MA ON MA.aluno = PE.codigo and (MA.unidadeEnsino) = ").append(obj.getUnidadeEnsino().getCodigo().intValue());
			}
			sqlStr.append(" WHERE PE.aluno = 'TRUE'");

			if (obj.getAluno().getCodigo() != 0) {
				sqlStr.append(" AND PE.codigo = ").append(obj.getAluno().getCodigo().intValue());
			}
		} else if (obj.getTipoDestinatario().equals("PR")) {
			if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
				sqlStr.append(", Funcionario FU, funcionariocargo fc, UnidadeEnsino UE WHERE PE.professor = 'true' and FU.pessoa = PE.codigo and fu.codigo = fc.funcionario and Fc.unidadeEnsino = UE.codigo" + " and (UE.codigo) = ").append(obj.getUnidadeEnsino().getCodigo().intValue());
			} else {
				sqlStr.append(" WHERE PE.professor = 'true' ");
			}
			if (obj.getProfessor().getCodigo() != 0) {
				sqlStr.append(" AND PE.codigo = ").append(obj.getProfessor().getCodigo().intValue());
			}
		} else if (obj.getTipoDestinatario().equals("FU")) {
			if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
				sqlStr.append(", Funcionario FU, funcionariocargo fc, UnidadeEnsino UE WHERE PE.funcionario = 'true' and FU.pessoa = PE.codigo and fu.codigo = fc.funcionario and Fc.unidadeEnsino = UE.codigo" + " and (UE.codigo) = ").append(obj.getUnidadeEnsino().getCodigo().intValue());
			} else {
				sqlStr.append(", Funcionario FU WHERE PE.funcionario = 'true' and FU.pessoa = PE.codigo and fu.codigo = fc.funcionario ");
			}
			if (obj.getFuncionario().getCodigo() != 0) {
				sqlStr.append(" AND FU.codigo = ").append(obj.getFuncionario().getCodigo().intValue());
			}
		} else if (obj.getTipoDestinatario().equals("CA")) {

			if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
				sqlStr.append(", Funcionario FU, funcionariocargo fc, UnidadeEnsino UE " + "WHERE PE.funcionario = 'true' and FU.pessoa = PE.codigo and fc.funcionario = fu.codigo and fc.unidadeEnsino = UE.codigo and (UE.codigo) = ").append(obj.getUnidadeEnsino().getCodigo().intValue());
			} else {
				sqlStr.append(", Funcionario FU, funcionariocargo fc WHERE PE.funcionario = 'true' and FU.pessoa = PE.codigo and fc.funcionario = fu.codigo ");
			}
			if (obj.getCargo().getCodigo() != 0) {
				sqlStr.append(" AND fc.cargo = ").append(obj.getCargo().getCodigo().intValue());
			}
		} else if (obj.getTipoDestinatario().equals("DE")) {
			if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
				sqlStr.append(", Funcionario FU, funcionariocargo fc, UnidadeEnsino UE, Cargo " + "WHERE PE.funcionario = 'true' and FU.pessoa = PE.codigo and fc.funcionario = fu.codigo and Fc.unidadeEnsino = UE.codigo and cargo.codigo = fc.cargo  and (UE.codigo) = ").append(obj.getUnidadeEnsino().getCodigo().intValue());
			} else {
				sqlStr.append(", Funcionario FU, funcionariocargo fc, Cargo WHERE PE.funcionario = 'true' and FU.pessoa = PE.codigo and fc.funcionario = fu.codigo and cargo.codigo = fc.cargo ");
			}

			if (obj.getDepartamento().getCodigo() != 0) {
				sqlStr.append(" AND cargo.departamento = ").append(obj.getDepartamento().getCodigo().intValue());
			}

		} else if (obj.getTipoDestinatario().equals("AR")) {
			sqlStr.append(", Matricula MA, Curso CU WHERE MA.aluno = PE.codigo and MA.curso = CU.codigo and CU.areaConhecimento = ").append(obj.getAreaConhecimento().getCodigo().intValue()).append(" and PE.aluno = 'true' ");
			if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
				sqlStr.append(" AND MA.unidadeEnsino = ").append(obj.getUnidadeEnsino().getCodigo().intValue());
			}
		} else if (obj.getTipoDestinatario().equals("TU")) {
			sqlStr.append(", Matricula MA, MatriculaPeriodo MP  WHERE MA.aluno = PE.codigo and MP.matricula = MA.matricula and MP.turma = ").append(obj.getTurma().getCodigo().intValue()).append(" and PE.aluno = 'true' ");
		}
		sqlStr.append(" group by PE.codigo, PE.nome, PE.email ORDER BY PE.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ComunicadoInternoDestinatarioVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			ComunicadoInternoDestinatarioVO CID = new ComunicadoInternoDestinatarioVO();
			CID.setComunicadoInterno(obj.getCodigo());
			PessoaVO pVO = new PessoaVO();
			pVO.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			pVO.setNome(tabelaResultado.getString("nome"));
			pVO.setEmail(tabelaResultado.getString("email"));
			CID.setDestinatario(pVO);
			CID.setTipoComunicadoInterno(obj.getTipoComunicadoInterno());
			lista.add(CID);
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<ComunicadoInternoDestinatarioVO> consultarListaDeFuncionariosDestinatarios(List lista, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		ProvisaoCustoVO pc;
		sql.append("SELECT PE.codigo, PE.nome, PE.email ");
		sql.append("FROM Pessoa as PE ");
		sql.append("inner join Funcionario as FU on pe.codigo = fu.pessoa ");
		sql.append("inner join funcionariocargo as fc on fc.codigo = fu.codigo ");
		sql.append("inner join UnidadeEnsino as UE on fc.unidadeensino = ue.codigo WHERE (UE.codigo)");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" = ").append(unidadeEnsino.intValue()).append(" ");
		} else {
			sql.append(" is not null ");
		}
		String andOr = " and ";
		for (MapaLancamentoFuturoVO obj : (List<MapaLancamentoFuturoVO>) lista) {
			pc = getFacadeFactory().getProvisaoCustoFacade().consultarPorMapaLancamentoFuturo(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
			if (pc.getDataPrestacaoConta().before(new Date())) {
				sql.append(andOr).append(" fu.codigo = ").append(pc.getRequisitante().getCodigo()).append(" ");
				andOr = " or ";
			}
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		return montarDadosDestinatario(resultado);

	}

	private List<ComunicadoInternoDestinatarioVO> montarDadosDestinatario(SqlRowSet rs) throws SQLException {
		List<ComunicadoInternoDestinatarioVO> lista = new ArrayList<>();
		while (rs.next()) {
			ComunicadoInternoDestinatarioVO CID = new ComunicadoInternoDestinatarioVO();
			PessoaVO pVO = new PessoaVO();
			pVO.setCodigo(new Integer(rs.getInt("codigo")));
			pVO.setNome(rs.getString("nome"));
			pVO.setEmail(rs.getString("email"));
			CID.setDestinatario(pVO);
			CID.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
			lista.add(CID);
		}
		return lista;
	}

	/**
	 * Este método tem a função de criar um comunicado interno onde no corpo da
	 * mensagem será utilizado o atrinbuto frase, os destinários serão montados
	 * conforme os outros parâmentros onde se passar como true o sistema irá
	 * enviar o comunicado para os mesmos
	 * 
	 * @param frase
	 *            - Texto a ser enviado
	 * @param todos
	 *            - Responsavel em buscar a lista de destinatarios sendo eles:
	 *            (Diretores, Coordenadores, Funcionarios, Professores, Alunos);
	 * @param diretores
	 *            - Filtro para buscar os destinatarios do tipo Diretor
	 * @param coordenadores
	 *            - Filtro para buscar os destinatarios do tipo Coordenador
	 * @param funcionarios
	 *            - Filtro para buscar os destinatarios do tipo Funcionario
	 * @param professores
	 *            - Filtro para buscar os destinatarios do tipo Professores
	 * @param alunos
	 *            - Filtro para buscar os destinatarios do tipo Alunos
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCompartilhamentoFraseInspiracao(String frase, String autor, Boolean todos, Boolean cargo, Integer codigoCargo, Boolean departamento, Integer codigoDepartamento, Boolean funcionarios, Boolean professores, Boolean alunos, Boolean enviarEmail, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (frase.trim().isEmpty()) {
			throw new ConsistirException("Deve ser cadastrado uma frase para ser compartilhada!");
		}
		ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
		try {

			comunicacaoInternaVO.setResponsavel(usuario.getPessoa());
			comunicacaoInternaVO.setAssunto("Frase de Inspiração");
			comunicacaoInternaVO.setEnviarEmail(enviarEmail);
			comunicacaoInternaVO.setMensagem(frase + "\n" + "Autor: " + autor);
			comunicacaoInternaVO.setDigitarMensagem(true);
			comunicacaoInternaVO.setUnidadeEnsino(usuario.getUnidadeEnsinoLogado());
			if (alunos || todos) {
				comunicacaoInternaVO.setTipoDestinatario("AL");
				comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(consultaComunicadoInternoDestinatario(comunicacaoInternaVO, false, usuario));
				if (!comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().isEmpty()) {
					incluir(comunicacaoInternaVO, false, usuario, configuracaoGeralSistemaVO,null);
				}
			}
			if (cargo || todos) {
				comunicacaoInternaVO.setTipoDestinatario("CA");
				comunicacaoInternaVO.getCargo().setCodigo(codigoCargo);
				comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(consultaComunicadoInternoDestinatario(comunicacaoInternaVO, false, usuario));
				if (!comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().isEmpty()) {
					incluir(comunicacaoInternaVO, false, usuario, configuracaoGeralSistemaVO,null);
				}
			}
			if (departamento || todos) {
				comunicacaoInternaVO.setTipoDestinatario("DE");
				comunicacaoInternaVO.getDepartamento().setCodigo(codigoDepartamento);
				comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(consultaComunicadoInternoDestinatario(comunicacaoInternaVO, false, usuario));
				if (!comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().isEmpty()) {
					incluir(comunicacaoInternaVO, false, usuario, configuracaoGeralSistemaVO,null);
				}
			}
			if (funcionarios || todos) {
				comunicacaoInternaVO.setTipoDestinatario("FU");
				comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(consultaComunicadoInternoDestinatario(comunicacaoInternaVO, false, usuario));
				if (!comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().isEmpty()) {
					incluir(comunicacaoInternaVO, false, usuario, configuracaoGeralSistemaVO,null);
				}
			}
			if (professores || todos) {
				comunicacaoInternaVO.setTipoDestinatario("PR");
				comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(consultaComunicadoInternoDestinatario(comunicacaoInternaVO, false, usuario));
				if (!comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().isEmpty()) {
					incluir(comunicacaoInternaVO, false, usuario, configuracaoGeralSistemaVO,null);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			comunicacaoInternaVO = null;
		}
	}

	public List<ComunicacaoInternaVO> consultarParaMuralComunicacaoInterna(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ComunicacaoInterna CI WHERE CI.tipoComunicadoInterno = 'MU' ORDER BY CI.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ComunicacaoInternaVO> consultarPorTipoComunicadoInternoMuralDestinatario(String valorConsulta, Integer destinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ComunicacaoInterna AS CI " + "WHERE (upper( CI.tipoComunicadoInterno ) = '" + valorConsulta.toUpperCase() + "') " + "AND (to_char( CI.dataExibicaoInicial, 'YYYY-MM-DD') <= to_char( now(), 'YYYY-MM-DD') OR CI.dataExibicaoInicial is null) " + "AND (to_char( CI.dataExibicaoFinal, 'YYYY-MM-DD') >= to_char( now(), 'YYYY-MM-DD') OR CI.dataExibicaoFinal is null) " + "ORDER BY CI.prioridade";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ComunicacaoInternaVO> consultarPorTipoComunicadoInternoNaoLidaRespondidaDestinatario(String valorConsulta, Integer destinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String respondida = "";
		if (valorConsulta.toUpperCase().equals("RE")) {
			respondida = "OR (NOT CID.ciJarespondida)";
		}
		String sqlStr = "SELECT * FROM ComunicacaoInterna AS CI " + "INNER JOIN ComunicadoInternoDestinatario CID ON CID.ComunicadoInterno = CI.codigo " + "AND CID.destinatario = " + destinatario.toString() + " AND ((NOT CID.ciJalida) " + respondida + ")" + "WHERE upper( CI.tipoComunicadoInterno ) = ('" + valorConsulta.toUpperCase() + "') " + "AND (to_char( CI.dataExibicaoInicial, 'YYYY-MM-DD') <= to_char( now(), 'YYYY-MM-DD') OR CI.dataExibicaoInicial is null) " + "AND (to_char( CI.dataExibicaoFinal, 'YYYY-MM-DD') >= to_char( now(), 'YYYY-MM-DD') OR CI.dataExibicaoFinal is null) " + "ORDER BY CI.prioridade, CI.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ComunicacaoInterna</code>
	 * através do valor do atributo <code>String tipoDestinatario</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ComunicacaoInternaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ComunicacaoInternaVO> consultarPorTipoDestinatario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ComunicacaoInterna WHERE upper( tipoDestinatario ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoDestinatario";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ComunicacaoInternaVO> consultarPorTipoOrigemECodigoTipoOrigemEPessoa(TipoOrigemComunicacaoInternaEnum tipoOrigemComunicacaoInternaEnum, Integer codigoTipoOrigem, PessoaVO pessoaVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM ComunicacaoInterna");
		sql.append(" inner join comunicadointernodestinatario on comunicadointernodestinatario.comunicadointerno = comunicacaointerna.codigo " );
		sql.append(" WHERE codigotipoorigemcomunicacaointerna = ? AND tipoorigemcomunicacaointernaenum = ? AND comunicadointernodestinatario.destinatario = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoTipoOrigem, tipoOrigemComunicacaoInternaEnum.name(), pessoaVO.getCodigo());
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ComunicacaoInterna</code>
	 * através do valor do atributo <code>String tipoComunicadoInterno</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ComunicacaoInternaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ComunicacaoInternaVO> consultarPorTipoComunicadoInterno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ComunicacaoInterna WHERE upper( tipoComunicadoInterno ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoComunicadoInterno";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ComunicacaoInterna</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Pessoa</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ComunicacaoInternaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ComunicacaoInternaVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ComunicacaoInterna.* FROM ComunicacaoInterna, Pessoa WHERE ComunicacaoInterna.responsavel = Pessoa.codigo and upper( Pessoa.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ComunicacaoInterna</code>
	 * através do valor do atributo <code>Date data</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ComunicacaoInternaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ComunicacaoInternaVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ComunicacaoInterna WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ComunicacaoInterna</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ComunicacaoInternaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ComunicacaoInternaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ComunicacaoInterna WHERE codigo = " + valorConsulta.intValue() + " Order by data desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ComunicacaoInternaVO</code> resultantes da consulta.
	 */
	public static List<ComunicacaoInternaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ComunicacaoInternaVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ComunicacaoInternaVO</code>.
	 * 
	 * @return O objeto da classe <code>ComunicacaoInternaVO</code> com os dados
	 *         devidamente montados.
	 */
	public static ComunicacaoInternaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ComunicacaoInternaVO obj = new ComunicacaoInternaVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.getResponsavel().setCodigo((dadosSQL.getInt("responsavel")));
		obj.setTipoComunicadoInterno(dadosSQL.getString("tipoComunicadoInterno"));
		obj.setTipoDestinatario(dadosSQL.getString("tipoDestinatario"));
		obj.setTipoRemetente(dadosSQL.getString("tipoRemetente"));
		obj.getFuncionario().setCodigo((dadosSQL.getInt("funcionario")));
		obj.getCargo().setCodigo((dadosSQL.getInt("cargo")));
		obj.getDepartamento().setCodigo((dadosSQL.getInt("departamento")));
		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino")));
		obj.getTurma().setCodigo((dadosSQL.getInt("turma")));
		obj.getAreaConhecimento().setCodigo((dadosSQL.getInt("areaConhecimento")));
		obj.getAluno().setCodigo((dadosSQL.getInt("aluno")));
		obj.getProfessor().setCodigo((dadosSQL.getInt("professor")));
		obj.getDisciplina().setCodigo((dadosSQL.getInt("disciplina")));
		obj.setAssunto(dadosSQL.getString("assunto"));
		obj.setMensagem(dadosSQL.getString("mensagem"));
		obj.setMensagemSMS(dadosSQL.getString("mensagemSMS"));
		obj.setEnviarSMS(dadosSQL.getBoolean("enviarSMS"));
		obj.getComunicadoInternoOrigem().setCodigo(new Integer(dadosSQL.getInt("comunicadoInternoOrigem")));
		obj.setDataExibicaoInicial(dadosSQL.getDate("dataExibicaoInicial"));
		obj.setDataExibicaoFinal(dadosSQL.getDate("dataExibicaoFinal"));
		obj.setPrioridade(dadosSQL.getString("prioridade"));
		obj.setEnviarEmail(dadosSQL.getBoolean("enviarEmail"));
		obj.setTipoMarketing(dadosSQL.getBoolean("tipoMarketing"));
		obj.setTipoLeituraObrigatoria(dadosSQL.getBoolean("tipoLeituraObrigatoria"));
		obj.setDigitarMensagem(dadosSQL.getBoolean("digitarMensagem"));
		obj.getArquivoAnexo().setCodigo(dadosSQL.getInt("arquivoAnexo"));
		obj.setCodigoTipoOrigemComunicacaoInterna(dadosSQL.getInt("codigoTipoOrigemComunicacaoInterna"));
		obj.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.valueOf(dadosSQL.getString("tipoOrigemComunicacaoInternaEnum")));
		montarDadosArquivo(obj, nivelMontarDados, usuario);
		try {
			if ((dadosSQL.getBoolean("cijalida"))) {
				obj.setImagemEnvelope("./resources/imagens/envelopeAberto1.png");
			} else {
				obj.setImagemEnvelope("./resources/imagens/envelopeFechado1.png");
			}

		} catch (Exception e) {
			obj.setImagemEnvelope("./resources/imagens/envelopeFechado1.png");
		}

		obj.setNovoObj(false);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setComunicadoInternoDestinatarioVOs(ComunicadoInternoDestinatario.consultarComunicadoInternoDestinatarios(obj.getCodigo(), nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}

		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosFuncionario(obj, nivelMontarDados, usuario);
		montarDadosCargo(obj, nivelMontarDados, usuario);
		montarDadosDepartamento(obj, nivelMontarDados, usuario);
		montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
		montarDadosAluno(obj, nivelMontarDados, usuario);
		montarDadosProfessor(obj, nivelMontarDados, usuario);
		montarDadosComunicadoInternoOrigem(obj, nivelMontarDados, usuario);
		return obj;
	}

	public static void montarDadosArquivo(ComunicacaoInternaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getArquivoAnexo().getCodigo().intValue() == 0) {
			obj.setArquivoAnexo(new ArquivoVO());
			return;
		}
		obj.setArquivoAnexo(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoAnexo().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ComunicacaoInternaVO</code> relacionado ao objeto
	 * <code>ComunicacaoInternaVO</code>. Faz uso da chave primária da classe
	 * <code>ComunicacaoInternaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosComunicadoInternoOrigem(ComunicacaoInternaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getComunicadoInternoOrigem().getCodigo().intValue() == 0) {
			obj.setComunicadoInternoOrigem(new ComunicacaoInternaVO());
			return;
		}
		obj.setComunicadoInternoOrigem(getFacadeFactory().getComunicacaoInternaFacade().consultarPorChavePrimaria(obj.getComunicadoInternoOrigem().getCodigo(), true, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto
	 * <code>ComunicacaoInternaVO</code>. Faz uso da chave primária da classe
	 * <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosProfessor(ComunicacaoInternaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProfessor().getCodigo().intValue() == 0) {
			obj.setProfessor(new PessoaVO());
			return;
		}
		obj.setProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setProfessorNome(obj.getProfessor().getNome());
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto
	 * <code>ComunicacaoInternaVO</code>. Faz uso da chave primária da classe
	 * <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosAluno(ComunicacaoInternaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getAluno().getCodigo().intValue() == 0) {
			obj.setAluno(new PessoaVO());
			return;
		}
		obj.setAluno(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setAlunoNome(obj.getAluno().getNome());
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>UnidadeEnsinoVO</code> relacionado ao objeto
	 * <code>ComunicacaoInternaVO</code>. Faz uso da chave primária da classe
	 * <code>UnidadeEnsinoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosUnidadeEnsino(ComunicacaoInternaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosTurma(ComunicacaoInternaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurma().getCodigo().intValue() == 0) {
			obj.setTurma(new TurmaVO());
			return;
		}
		obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados, usuario));
		obj.setTurmaNome(obj.getTurma().getIdentificadorTurma());
	}

	public static void montarDadosAreaConhecimento(ComunicacaoInternaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
			obj.setAreaConhecimento(new AreaConhecimentoVO());
			return;
		}
		obj.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(obj.getAreaConhecimento().getCodigo(), usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>DepartamentoVO</code> relacionado ao objeto
	 * <code>ComunicacaoInternaVO</code>. Faz uso da chave primária da classe
	 * <code>DepartamentoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosDepartamento(ComunicacaoInternaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDepartamento().getCodigo().intValue() == 0) {
			obj.setDepartamento(new DepartamentoVO());
			return;
		}
		obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamento().getCodigo(), true, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>CargoVO</code> relacionado ao objeto
	 * <code>ComunicacaoInternaVO</code>. Faz uso da chave primária da classe
	 * <code>CargoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCargo(ComunicacaoInternaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCargo().getCodigo().intValue() == 0) {
			return;
		}
		obj.setCargo(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(obj.getCargo().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>FuncionarioVO</code> relacionado ao objeto
	 * <code>ComunicacaoInternaVO</code>. Faz uso da chave primária da classe
	 * <code>FuncionarioVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosFuncionario(ComunicacaoInternaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionario().getCodigo().intValue() == 0) {
			obj.setFuncionario(new FuncionarioVO());
			return;
		}
		obj.setFuncionario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), obj.getFuncionario().getUnidadeEnsino().getCodigo(), true, nivelMontarDados, usuario));
		obj.setFuncionarioNome(obj.getFuncionario().getPessoa().getNome());
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto
	 * <code>ComunicacaoInternaVO</code>. Faz uso da chave primária da classe
	 * <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(ComunicacaoInternaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new PessoaVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavel().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ComunicacaoInternaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ComunicacaoInternaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM ComunicacaoInterna WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ComunicacaoInterna ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ComunicacaoInterna.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ComunicacaoInterna.idEntidade = idEntidade;
	}

	/** Metodo para consultar comunicados enviados */
	public List<ComunicacaoInternaVO> consultarCodigoResponsavel(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ComunicacaoInterna WHERE responsavel = " + valorConsulta.intValue() + " and ((data >= '" + Uteis.getDataJDBC(dataIni) + "') and (data <= '" + Uteis.getDataJDBC(dataFim) + "')) ORDER BY codigo";
		if (!tipoSaidaConsulta.equals("")) {
			sqlStr = "SELECT * FROM ComunicacaoInterna WHERE responsavel = " + valorConsulta.intValue() + " and ((data >= '" + Uteis.getDataJDBC(dataIni) + "') and (data <= '" + Uteis.getDataJDBC(dataFim) + "')) and tipoComunicadoInterno = '" + tipoSaidaConsulta + "' ORDER BY codigo";
		}
		if (dataIni == null && dataFim == null) {
			sqlStr = "SELECT * FROM ComunicacaoInterna WHERE responsavel = " + valorConsulta.intValue() + " ORDER BY codigo";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ComunicacaoInternaVO> consultarPorSituacaoEntrada(Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT cid.cijalida,  ci.codigo, ci.data, ci.tipoComunicadoInterno, ci.tipoDestinatario, " + " ci.assunto, ci.mensagem, ci.comunicadoInternoOrigem, ci.prioridade, " + "ci.enviarEmail, ci.responsavel, " + " ci.dataExibicaoInicial, ci.dataExibicaoFinal, ci.funcionario, ci.aluno, ci.professor, " + " ci.unidadeEnsino, ci.turma, ci.areaConhecimento, " + " ci.departamento, ci.cargo, " + " ci.codigoTipoOrigemComunicacaoInterna, ci.tipoOrigemComunicacaoInternaEnum  " + " FROM ComunicacaoInterna ci, ComunicadoInternoDestinatario cid " + " WHERE  cid.comunicadointerno = ci.codigo and ((ci.data >= '" + Uteis.getDataJDBC(dataIni) + "') and (ci.data <= '" + Uteis.getDataJDBC(dataFim) + "')) " + " AND cid.destinatario = " + codigo.intValue();
		if (lida != null && respondida != null) {
			sqlStr = sqlStr + " and cid.ciJaLida = " + lida.booleanValue() + " or ((cid.ciJaRespondida = " + respondida.booleanValue() + ") and (ci.tipoComunicadoInterno = 'RE')) ";
		} else if (lida != null) {
			sqlStr = sqlStr + " and cid.ciJaLida = " + lida.booleanValue();
		} else if (respondida != null) {
			sqlStr = sqlStr + " and cid.ciJaRespondida = " + respondida.booleanValue() + " and ci.tipoComunicadoInterno = 'RE' ";
		}
		sqlStr = sqlStr + " Order by ci.data desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ComunicacaoInternaVO> consultarPorEntradaLimite(Integer codigo, String tipoDestinatario, Integer limite, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT cid.cijalida, ci.codigo, ci.data, ci.tipoComunicadoInterno, ci.tipoDestinatario, " + " ci.assunto, ci.mensagem, ci.comunicadoInternoOrigem, ci.prioridade, ci.enviarEmail, ci.responsavel, " + " ci.dataExibicaoInicial, ci.dataExibicaoFinal, ci.funcionario, ci.aluno, ci.professor, " + " ci.unidadeEnsino, ci.turma, ci.areaConhecimento, ci.departamento, ci.cargo, " + " ci.codigoTipoOrigemComunicacaoInterna, ci.tipoOrigemComunicacaoInternaEnum  " + " FROM ComunicacaoInterna ci, ComunicadoInternoDestinatario cid " + " WHERE  cid.comunicadointerno = ci.codigo  and cid.destinatario = " + codigo.intValue() + " and cid.removerCaixaEntrada = 'false'" + " Order by ci.data desc" + " limit " + limite.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>UnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 * 
	 */
	private StringBuffer getSQLPadraoConsultaBasica(Boolean filtrarCaixaSaida) {
		StringBuffer str = new StringBuffer();
		str.append("SELECT DISTINCT count(ComunicacaoInterna.codigo) over() as qtde_total_registros, ComunicacaoInterna.codigo, ComunicacaoInterna.data, ComunicacaoInterna.tipoDestinatario, ComunicacaoInterna.tipoRemetente, ComunicacaoInterna.tipoComunicadoInterno, ComunicacaoInterna.assunto,  ");
		str.append("ComunicacaoInterna.mensagem, ComunicacaoInterna.mensagemSMS, ComunicacaoInterna.enviarSMS, ComunicacaoInterna.tipoMarketing, ComunicacaoInterna.tipoLeituraObrigatoria, ComunicacaoInterna.arquivoAnexo, ");
		str.append("arquivoAnexo.pastaBaseArquivo AS anexoPastaBase, ComunicacaoInterna.removerCaixaSaida, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"responsavel.nome\", pessoa.email AS \"responsavel.email\", ");
		str.append("pessoa.arquivoImagem AS arquivoImagem, arquivoImagem.pastaBaseArquivo AS imagemPastaBase,  ");
		str.append("ComunicacaoInterna.tipoOrigemComunicacaoInternaEnum AS tipoOrigemComunicacaoInternaEnum, ComunicacaoInterna.codigoTipoOrigemComunicacaoInterna AS codigoTipoOrigemComunicacaoInterna,  ");
		str.append("arquivoImagem.nome AS nomeImagem, arquivoAnexo.nome AS nomeAnexo, arquivoAnexo.descricao AS descricaoAnexo, comunicacaointerna.enviaremail, comunicacaointerna.enviaremailinstitucional, ");
		if(Uteis.isAtributoPreenchido(filtrarCaixaSaida) && filtrarCaixaSaida){
			str.append(" true as ciJaLida ");	
		}else {
			str.append(" ComunicadoInternoDestinatario.ciJaLida as ciJaLida "); 
		}
		str.append(" FROM ComunicacaoInterna ");
		str.append("LEFT JOIN pessoa on pessoa.codigo = ComunicacaoInterna.responsavel ");

		str.append("LEFT JOIN arquivo arquivoImagem on pessoa.arquivoImagem = arquivoImagem.codigo ");
		str.append("LEFT JOIN arquivo arquivoAnexo on ComunicacaoInterna.arquivoAnexo = arquivoAnexo.codigo ");

		str.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicacaoInterna.codigo = ComunicadoInternoDestinatario.comunicadointerno ");
		return str;
	}

	private StringBuffer getSQLPadraoConsultaBasicaMontandoSubordinada() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT ComunicacaoInterna.codigo, ComunicacaoInterna.digitarMensagem, ComunicacaoInterna.data, ComunicacaoInterna.tipoDestinatario, ComunicacaoInterna.tipoRemetente, ComunicacaoInterna.aluno, ComunicacaoInterna.professor, ");
		str.append("ComunicacaoInterna.tipoComunicadoInterno, ComunicacaoInterna.assunto, ComunicacaoInterna.mensagem, ComunicacaoInterna.mensagemSMS, ComunicacaoInterna.enviarSMS, ComunicacaoInterna.tipoMarketing, ComunicacaoInterna.removerCaixaSaida, ");
		str.append("ComunicacaoInterna.tipoOrigemComunicacaoInternaEnum AS tipoOrigemComunicacaoInternaEnum, ComunicacaoInterna.codigoTipoOrigemComunicacaoInterna AS codigoTipoOrigemComunicacaoInterna,  ");
		str.append("ComunicacaoInterna.tipoLeituraObrigatoria,  pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"responsavel.nome\", ");
		str.append(" peComunicadoDestinatario.codigo AS \"pessoaDestinatario.codigo\", peComunicadoDestinatario.nome AS \"pessoaDestinatario.nome\", ComunicadoInternoDestinatario.codigo AS \"destinatario.codigo\", ");
		str.append(" ComunicadoInternoDestinatario.nome AS \"destinatario.nome\",  ComunicadoInternoDestinatario.email AS \"destinatario.email\",");
		str.append(" pessoa.arquivoImagem AS arquivoImagem, arquivoImagem.pastaBaseArquivo AS imagemPastaBase, ComunicacaoInterna.arquivoAnexo, arquivoAnexo.pastaBaseArquivo AS anexoPastaBase, ");
		str.append("arquivoImagem.nome AS nomeImagem, arquivoAnexo.nome AS nomeAnexo FROM ComunicacaoInterna ");
		str.append(" LEFT JOIN pessoa on pessoa.codigo = ComunicacaoInterna.responsavel ");

		str.append("LEFT JOIN arquivo arquivoImagem on pessoa.arquivoImagem = arquivoImagem.codigo ");
		str.append("LEFT JOIN arquivo arquivoAnexo on ComunicacaoInterna.arquivoAnexo = arquivoAnexo.codigo ");

		str.append(" LEFT JOIN ComunicadoInternoDestinatario on ComunicacaoInterna.codigo = ComunicadoInternoDestinatario.comunicadointerno ");
		str.append(" LEFT JOIN pessoa peComunicadoDestinatario on peComunicadoDestinatario.codigo = ComunicadoInternoDestinatario.destinatario ");
		return str;
	}

	/**
	 * Método responsavel por fazer uma seleção completa da Entidade Comunicação
	 * Interna e mais algumas outras entidades que possuem relacionamento com a
	 * mesma. É uma consulta que busca completa e padrão.
	 * 
	 * @return List Contendo vários objetos da classe
	 * @author Carlos
	 */
	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT ci.codigo, ci.digitarMensagem, ci.mensagemSMS, ci.enviarSMS, ci.data, ci.tipoDestinatario, ci.tipoRemetente, ci.tipoComunicadoInterno, ci.prioridade, ci.dataExibicaoInicial, ci.dataExibicaoFinal, ci.mensagem, ci.assunto, ci.tipoMarketing, ci.removerCaixaSaida, ci.tipoLeituraObrigatoria, ");
		str.append("ci.tipoOrigemComunicacaoInternaEnum AS tipoOrigemComunicacaoInternaEnum, ci.codigoTipoOrigemComunicacaoInterna AS codigoTipoOrigemComunicacaoInterna, ci.enviaremail, ci.enviaremailinstitucional,  ");
		// Dados da Unidade Ensino
		str.append("UnidadeEnsino.codigo AS \"UnidadeEnsino.codigo\", UnidadeEnsino.nome AS \"UnidadeEnsino.nome\", ");
		// Dados do Responsavel Logado
		str.append("pessoa.codigo AS \"responsavel.codigo\", pessoa.nome AS \"responsavel.nome\",  pessoa.email AS \"responsavel.email\", ");
		// Dados do Aluno
		str.append(" peAluno.codigo AS \"aluno.codigo\", peAluno.nome AS \"aluno.nome\",  ");
		// Dados do Cargo
		str.append(" cargo.codigo AS \"cargo.codigo\", cargo.nome AS \"cargo.nome\",  ");
		// Dados do Departamento
		str.append(" departamento.codigo AS \"departamento.codigo\", departamento.nome AS \"departamento.nome\", ");
		// Dados do Funcionario
		str.append(" peFuncionario.codigo AS \"funcionario.codigo\", peFuncionario.nome AS \"funcionario.nome\", ");
		// Dados do Funcionario
		str.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		// Dados do Professor
		str.append(" peProfessor.codigo AS \"professor.codigo\", peProfessor.nome AS \"professor.nome\", ");
		// Dados do Coordenador
		str.append(" peCoordenador.codigo AS \"coordenador.codigo\", peCoordenador.nome AS \"coordenador.nome\", ");
		// Dados da Turma
		str.append(" turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", ");
		// Dados da Area de Conhecimento
		str.append(" ac.codigo AS \"areaConhecimento.codigo\", ac.nome AS \"areaConhecimento.nome\", ");
		// Dados do Comunicado Interno Destinatário
		//str.append(" peComunicadoDestinatario.codigo AS \"pessoaDestinatario.codigo\", peComunicadoDestinatario.nome AS \"pessoaDestinatario.nome\", peComunicadoDestinatario.email AS \"pessoaDestinatario.email\",");
		//str.append(" cid.tipoComunicadoInterno AS \"destinatario.tipoComunicadoInterno\", cid.ciJaLida AS \"destinatario.ciJaLida\", cid.ciJaRespondida AS \"destinatario.ciJaRespondida\", ");
		//str.append(" cid.dataLeitura AS \"destinatario.dataLeitura\", cid.codigo AS \"destinatario.codigo\", cid.comunicadoInterno AS \"destinatario.comunicadoInterno\", ");
		//str.append(" cid.nome AS \"destinatario.nome\", cid.email AS \"destinatario.email\", ");
		str.append(" pessoa.arquivoImagem AS arquivoImagem, arquivoImagem.pastaBaseArquivo AS imagemPastaBase, ci.arquivoAnexo, arquivoAnexo.pastaBaseArquivo AS anexoPastaBase, ");
		str.append("arquivoImagem.nome AS nomeImagem, arquivoAnexo.nome AS nomeAnexo ");

		str.append(" FROM ComunicacaoInterna ci ");
		str.append(" LEFT JOIN pessoa on pessoa.codigo = ci.responsavel ");
		str.append(" LEFT JOIN pessoa peAluno on peAluno.codigo = ci.aluno ");
		str.append(" LEFT JOIN UnidadeEnsino on UnidadeEnsino.codigo = ci.UnidadeEnsino ");
		str.append(" LEFT JOIN cargo on cargo.codigo = ci.cargo ");
		str.append(" LEFT JOIN departamento on departamento.codigo = ci.departamento ");
		str.append(" LEFT JOIN funcionario on funcionario.codigo = ci.funcionario ");
		str.append(" LEFT JOIN pessoa peFuncionario on pefuncionario.codigo = funcionario.pessoa ");
		str.append(" LEFT JOIN pessoa peCoordenador on peCoordenador.codigo = ci.coordenador ");
		str.append(" LEFT JOIN pessoa peProfessor on peProfessor.codigo = ci.professor ");
		str.append(" LEFT JOIN turma on turma.codigo = ci.turma ");
		str.append(" LEFT JOIN areaConhecimento ac on ac.codigo = ci.areaConhecimento ");
		//str.append(" LEFT JOIN comunicadoInternoDestinatario cid on ci.codigo = cid.comunicadoInterno ");
		//str.append(" LEFT JOIN pessoa peComunicadoDestinatario on peComunicadoDestinatario.codigo = cid.destinatario ");
		str.append(" LEFT JOIN disciplina on disciplina.codigo = ci.disciplina ");
		str.append("LEFT JOIN arquivo arquivoImagem on pessoa.arquivoImagem = arquivoImagem.codigo ");
		str.append("LEFT JOIN arquivo arquivoAnexo on ci.arquivoAnexo = arquivoAnexo.codigo ");

		return str;
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public void consultaRapidaPorCodigoResponsavel(DataModelo controleConsulta, Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer pagina, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		
		StringBuffer sqlStr = new StringBuffer();		
		if(tipoSaidaConsulta.equals("")) {
			sqlStr.append(getSQLPadraoConsultaBasica(true));
		}else {
			sqlStr.append(getSQLPadraoConsultaBasica(false));
		}
		sqlStr.append("WHERE responsavel = ");
		sqlStr.append(valorConsulta.intValue());
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" and ((data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		if (!tipoSaidaConsulta.equals("")) {
			sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = '");
			sqlStr.append(tipoSaidaConsulta);
			sqlStr.append("'");
		}
		sqlStr.append(" and ComunicacaoInterna.removerCaixaSaida = 'false' ");
		
		List<String> filtros =  new ArrayList<String>(0);
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and sem_acentos(ComunicacaoInterna.assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorAssunto+PERCENT);
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorRemetente+PERCENT);
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') = ? ");
			filtros.add(filtroPorData);
		}
		sqlStr.append("  ORDER BY comunicacaoInterna.data desc");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
	}

	@Override
	public void consultaRapidaPorCodigoResponsavelFuncionarioOuAdministrador(DataModelo controleConsulta, Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer pagina, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		
		StringBuffer sqlStr = new StringBuffer();
		if(tipoSaidaConsulta.equals("")) {
			sqlStr = getSQLPadraoConsultaBasica(true);
		}else {
			sqlStr = getSQLPadraoConsultaBasica(false);
		}
		
		sqlStr.append(" and ComunicacaoInterna.responsavel = ComunicadoInternoDestinatario.destinatario ");
		sqlStr.append(" WHERE responsavel = ");
		sqlStr.append(valorConsulta.intValue());
		sqlStr.append(" AND (ComunicacaoInterna.tipoRemetente not in ('PR', 'AL')) ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" and ((data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		if (!tipoSaidaConsulta.equals("")) {
			sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = '");
			sqlStr.append(tipoSaidaConsulta);
			sqlStr.append("'");
		}
		sqlStr.append(" and ComunicacaoInterna.removerCaixaSaida = 'false'  ");
		
		List<String> filtros =  new ArrayList<String>(0);
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and sem_acentos(ComunicacaoInterna.assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorAssunto+PERCENT);
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorRemetente+PERCENT);
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') = ? ");
			filtros.add(filtroPorData);
		}
		sqlStr.append(" ORDER BY  data desc ");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
//		return montarDadosConsultaRapida(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}
	
	@Override
	public void consultaRapidaPorCodigoResponsavelVisaoProfessor(DataModelo controleConsulta,  Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer pagina, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		if(tipoSaidaConsulta.equals("")) {
			sqlStr = getSQLPadraoConsultaBasica(true);
		}else {
			sqlStr = getSQLPadraoConsultaBasica(false);
		}
		sqlStr.append("WHERE responsavel = ");
		sqlStr.append(valorConsulta.intValue());
		sqlStr.append(" AND (ComunicacaoInterna.tipoRemetente = 'PR' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoRemetente = 'TP') ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" and ((data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		if (!tipoSaidaConsulta.equals("")) {
			sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = '");
			sqlStr.append(tipoSaidaConsulta);
			sqlStr.append("'");
		}
		sqlStr.append(" and ComunicacaoInterna.removerCaixaSaida = 'false'  ");
		List<String> filtros =  new ArrayList<String>(0);
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and sem_acentos(ComunicacaoInterna.assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorAssunto+PERCENT);
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorRemetente+PERCENT);
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') = ? ");
			filtros.add(filtroPorData);
		}
		sqlStr.append(" ORDER BY data desc ");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
//		return montarDadosConsultaRapida(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public void consultaRapidaPorCodigoResponsavelVisaoAluno(DataModelo controleConsulta, Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer pagina, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		
		StringBuffer sqlStr = new StringBuffer();
		if(tipoSaidaConsulta.equals("")) {
			sqlStr = getSQLPadraoConsultaBasica(true);
		}else {
			sqlStr = getSQLPadraoConsultaBasica(false);
		}		sqlStr.append("WHERE responsavel = ");
		
		sqlStr.append(valorConsulta.intValue());
		sqlStr.append(" AND (ComunicacaoInterna.tipoRemetente = 'AL' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoRemetente = 'TA' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoRemetente = 'TU') ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" and ((data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		if (!tipoSaidaConsulta.equals("")) {
			sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = '");
			sqlStr.append(tipoSaidaConsulta);
			sqlStr.append("'");
		}
		sqlStr.append(" and ComunicacaoInterna.removerCaixaSaida = 'false'  ");
		List<String> filtros =  new ArrayList<String>(0);
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and sem_acentos(ComunicacaoInterna.assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorAssunto+PERCENT);
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorRemetente+PERCENT);
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') = ? ");
			filtros.add(filtroPorData);
		}
		sqlStr.append(" ORDER BY data desc ");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
//		return montarDadosConsultaRapida(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public void consultaRapidaPorCodigoResponsavelVisaoCoordenador(DataModelo controleConsulta, Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer pagina, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		
		StringBuffer sqlStr = new StringBuffer();
		if(tipoSaidaConsulta.equals("")) {
			sqlStr = getSQLPadraoConsultaBasica(true);
		}else {
			sqlStr = getSQLPadraoConsultaBasica(false);
		}
		sqlStr.append("WHERE responsavel = ");
		sqlStr.append(valorConsulta.intValue());
		sqlStr.append("AND ComunicacaoInterna.tipoRemetente = 'CO'  ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" and ((data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		if (!tipoSaidaConsulta.equals("")) {
			sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = '");
			sqlStr.append(tipoSaidaConsulta);
			sqlStr.append("'");
		}
		sqlStr.append(" and ComunicacaoInterna.removerCaixaSaida = 'false'  ");
		List<String> filtros =  new ArrayList<String>(0);
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and sem_acentos(ComunicacaoInterna.assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorAssunto+PERCENT);
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorRemetente+PERCENT);
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') = ? ");
			filtros.add(filtroPorData);
		}
		sqlStr.append(" ORDER BY data desc ");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
//		return montarDadosConsultaRapida(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}
	
	@Override
	public void consultaRapidaPorSituacaoEntradaVisaoFuncionarioOuAdministrador(DataModelo controleConsulta, Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer pagina, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false);
		// sqlStr.append("WHERE responsavel = ");
		// sqlStr.append(codigo.intValue());
		sqlStr.append(" WHERE 1=1");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" AND (data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 ') ");
		}
		sqlStr.append("AND ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append("AND (ComunicacaoInterna.tipoDestinatario = 'FU' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'DE') ");
		if (lida != null && respondida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
			// sqlStr.append(" or ((ComunicadoInternoDestinatario.ciJaRespondida = ");
			// sqlStr.append(respondida.booleanValue());
			// sqlStr.append(") and (ComunicacaoInterna.tipoComunicadoInterno = 'RE'))");
		} else if (lida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
			// } else if (respondida != null) {
			// sqlStr.append(" and ComunicadoInternoDestinatario.ciJaRespondida = ");
			// sqlStr.append(respondida.booleanValue());
			// sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = 'RE' ");
		}
		List<String> filtros =  new ArrayList<String>(0);
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and sem_acentos(ComunicacaoInterna.assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorAssunto+PERCENT);
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorRemetente+PERCENT);
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') = ? ");
			filtros.add(filtroPorData);
		}
		sqlStr.append(" ORDER BY ComunicacaoInterna.data desc");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
//		return montarDadosConsultaRapida(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}
	
	public List<ComunicacaoInternaVO> consultaRapidaPorEntradaLimiteMarketingLeituraObrigatoria(Integer codigo, Integer limite, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaMontandoSubordinada();
		sqlStr.append("WHERE ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = false and ComunicacaoInterna.dataExibicaoInicial::date <= '").append(Uteis.getDataJDBC(new Date())).append("' and  ComunicacaoInterna.dataExibicaoFinal::date >= '").append(Uteis.getDataJDBC(new Date())).append("' ");
		sqlStr.append(" and (ComunicacaoInterna.tipoMarketing = 'TRUE' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoLeituraObrigatoria = 'TRUE' OR ComunicacaoInterna.tipocomunicadointerno = 'MU') ");
		sqlStr.append("ORDER BY ComunicacaoInterna.data limit ");
		sqlStr.append(limite.intValue());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapidaMontandoSubordinada(tabelaResultado);
	}
	
	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação.
	 * 
	 * @author Carlos
	 */
	@Override
	public void consultaRapidaPorEntradaLimite(DataModelo controleConsulta, Integer codigo, String tipoDestinatario, Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData, Boolean somenteNaoLidas) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false);
		sqlStr.append("WHERE ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append(" and ComunicadoInternoDestinatario.removerCaixaEntrada = 'false'  ");
		if(somenteNaoLidas != null && somenteNaoLidas) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = false  ");		
		}
		List<String> filtros =  new ArrayList<String>(0);
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and sem_acentos(ComunicacaoInterna.assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorAssunto+PERCENT);
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorRemetente+PERCENT);
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') = ? ");
			filtros.add(filtroPorData);
		}
		sqlStr.append(" ORDER BY ComunicacaoInterna.data desc limit ");
		sqlStr.append(limite.intValue());
		if (pagina != null && pagina >= 0) {
			sqlStr.append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
	}

	public void consultaRapidaPorEntradaLimiteVisaoCoordenador(DataModelo controleConsulta, Integer codigo, String tipoDestinatario, Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false);
		sqlStr.append("WHERE ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append(" and ComunicadoInternoDestinatario.removerCaixaEntrada = 'false'  ");
		//sqlStr.append(" and ComunicacaoInterna.tipoDestinatario = 'CO' ");
		
		List<String> filtros =  new ArrayList<String>(0);
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and sem_acentos(ComunicacaoInterna.assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorAssunto+PERCENT);
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorRemetente+PERCENT);
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') = ? ");
			filtros.add(filtroPorData);
		}
		
		sqlStr.append(" ORDER BY ComunicacaoInterna.data desc ");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
	}

	public void consultaRapidaPorEntradaLimiteVisaoProfessor(DataModelo controleConsulta,  Integer codigo, String tipoDestinatario, Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false);
		sqlStr.append("WHERE ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append(" and ComunicadoInternoDestinatario.removerCaixaEntrada = 'false' ");
		
		List<String> filtros =  new ArrayList<String>(0);
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and sem_acentos(ComunicacaoInterna.assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorAssunto+PERCENT);
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorRemetente+PERCENT);
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') = ? ");
			filtros.add(filtroPorData);
		}
		
		//sqlStr.append(" and (ComunicacaoInterna.tipoDestinatario = 'PR' ");
		//sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TP' OR ComunicacaoInterna.tipoDestinatario = 'TC') ");
		sqlStr.append(" ORDER BY ComunicacaoInterna.data desc ");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
	}

	public void consultaRapidaPorEntradaLimiteVisaoAluno(DataModelo controleConsulta, Integer codigo, String tipoDestinatario, Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData, boolean somenteNaoLidas) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false);
		sqlStr.append("WHERE (ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		// sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TC' ) ");
		sqlStr.append(") and ComunicadoInternoDestinatario.removerCaixaEntrada = 'false'  ");
		if(somenteNaoLidas) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = false  ");		
		}
		List<String> filtros =  new ArrayList<String>(0);
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and sem_acentos(ComunicacaoInterna.assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorAssunto+PERCENT);
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorRemetente+PERCENT);
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') = ? ");
			filtros.add(filtroPorData);
		}
		
		sqlStr.append(" and (ComunicacaoInterna.tipoDestinatario = 'AL' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TA' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'RL' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TC' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'AA' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TU' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TD' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'IP' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'ALAS') ");
		sqlStr.append(" ORDER BY ComunicacaoInterna.data desc ");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
	}

	public List<ComunicacaoInternaVO> montarDadosConsultaRapidaMontandoSubordinada(SqlRowSet tabelaResultado) throws Exception {
		List<ComunicacaoInternaVO> vetResultado = new ArrayList<ComunicacaoInternaVO>(0);
		while (tabelaResultado.next()) {
			ComunicacaoInternaVO obj = new ComunicacaoInternaVO();
			montarDadosBasicoMontadoSubordinada(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<ComunicacaoInternaVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<ComunicacaoInternaVO> vetResultado = new ArrayList<ComunicacaoInternaVO>(0);
		while (tabelaResultado.next()) {
			ComunicacaoInternaVO obj = new ComunicacaoInternaVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void carregarDados(ComunicacaoInternaVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((ComunicacaoInternaVO) obj, NivelMontarDados.TODOS, usuario);
	}

	public void carregarDados(ComunicacaoInternaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((ComunicacaoInternaVO) obj, resultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((ComunicacaoInternaVO) obj, resultado, usuario);
		}
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codComunicacaoInterna, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false);
		sqlStr.append(" WHERE (ComunicacaoInterna.codigo= ").append(codComunicacaoInterna.intValue()).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codComunicacaoInterna, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (ci.codigo= ").append(codComunicacaoInterna.intValue()).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Comunicado Interno).");
		}
		return tabelaResultado;
	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta
	 * rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar
	 * os atributos básicos do objeto e alguns atributos relacionados de
	 * relevância para o contexto da aplicação.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasico(ComunicacaoInternaVO obj, SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		// Dados da ComunicacaoInterna
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setTipoDestinatario(dadosSQL.getString("tipoDestinatario"));
		obj.setTipoRemetente(dadosSQL.getString("tipoRemetente"));
		obj.setTipoComunicadoInterno(dadosSQL.getString("tipoComunicadoInterno"));
		obj.setAssunto(dadosSQL.getString("assunto"));
		obj.setMensagem(dadosSQL.getString("mensagem"));
		obj.setMensagemSMS(dadosSQL.getString("mensagemSMS"));
		obj.setEnviarSMS(dadosSQL.getBoolean("enviarSMS"));
		obj.setTipoMarketing(dadosSQL.getBoolean("tipoMarketing"));
		obj.setTipoLeituraObrigatoria(dadosSQL.getBoolean("tipoLeituraObrigatoria"));
		obj.setRemoverCaixaSaida(dadosSQL.getBoolean("removerCaixaSaida"));
		obj.setMensagemLidaVisaoAlunoCoordenador(dadosSQL.getBoolean("ciJaLida"));
		obj.setCodigoTipoOrigemComunicacaoInterna(dadosSQL.getInt("codigoTipoOrigemComunicacaoInterna"));
		obj.setEnviarEmail(dadosSQL.getBoolean("enviaremail"));
		obj.setEnviarEmailInstitucional(dadosSQL.getBoolean("enviaremailinstitucional"));
		if (dadosSQL.getString("tipoOrigemComunicacaoInternaEnum") == null) {
			obj.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.valueOf("NENHUM"));
		} else {
			obj.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.valueOf(dadosSQL.getString("tipoOrigemComunicacaoInternaEnum")));
		}

		obj.getArquivoAnexo().setCodigo(dadosSQL.getInt("arquivoAnexo"));
		obj.getArquivoAnexo().setPastaBaseArquivo(dadosSQL.getString("anexoPastaBase"));
		obj.getArquivoAnexo().setNome(dadosSQL.getString("nomeAnexo"));
		obj.getArquivoAnexo().setDescricao(dadosSQL.getString("descricaoAnexo"));
		obj.getArquivoAnexo().setDescricaoAntesAlteracao(dadosSQL.getString("descricaoAnexo"));

		obj.getResponsavel().getArquivoImagem().setCodigo(dadosSQL.getInt("arquivoImagem"));
		obj.getResponsavel().getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("imagemPastaBase"));
		obj.getResponsavel().getArquivoImagem().setNome(dadosSQL.getString("nomeImagem"));

		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Pessoa
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("pessoa.codigo")));
		obj.getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));
		obj.getResponsavel().setEmail(dadosSQL.getString("responsavel.email"));
		obj.getResponsavel().setNivelMontarDados(NivelMontarDados.BASICO);
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return;
		}
		montarDadosListaArquivosAnexo(obj, Uteis.NIVELMONTARDADOS_TODOS);
		if (obj.getListaArquivosAnexo().isEmpty() && obj.getArquivoAnexo().getCodigo() != 0) {
			obj.getListaArquivosAnexo().add(obj.getArquivoAnexo());
		}
	}

	public void montarDadosListaArquivosAnexo(ComunicacaoInternaVO obj, int nivelMontarDados) throws Exception {
		ComunicacaoInternaArquivo comIntArq = new ComunicacaoInternaArquivo();
		List<ComunicacaoInternaArquivoVO> listaComIntArq = comIntArq.consultarPorCodigoComunicacaoInterna(obj.getCodigo(), false, nivelMontarDados, null);
		obj.getListaArquivosAnexo().clear();
		for (ComunicacaoInternaArquivoVO comIntArqVO : listaComIntArq) {
			comIntArqVO.getArquivoAnexo().setCaminhoImagemAnexo(renderizarAnexo(comIntArqVO.getArquivoAnexo(), comIntArqVO.getArquivoAnexo().getPastaBaseArquivo(), obj.getConfiguracaoGeralSistemaVO(), "", "", false));
			obj.getListaArquivosAnexo().add(comIntArqVO.getArquivoAnexo());
		}
	}
	
	public String renderizarAnexo(ArquivoVO arquivoVO, String pastaBase, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoPastaWeb, String imagemDefault, Boolean upLoadImagem) throws Exception {
		String caminhoArquivo;
		String nomeArquivo = arquivoVO.getNome();
		File arquivoImagem = null;
		if (!arquivoVO.getNome().equals("")) {
			if (configuracaoGeralSistemaVO == null) {
				return "/" + pastaBase + "/" + arquivoVO.getNome();
			}
			if (configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo().endsWith("/")) {
				caminhoArquivo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + pastaBase + "/";
			} else {
				caminhoArquivo = configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + pastaBase + "/";
			}
			if (!upLoadImagem) {
				arquivoImagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBase + File.separator + nomeArquivo);
				if (arquivoImagem.exists()) {
					return caminhoArquivo + "/" + nomeArquivo;
				}
			}
		}
		return "";
	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta
	 * rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar
	 * os atributos básicos do objeto e alguns atributos relacionados de
	 * relevância para o contexto da aplicação.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasicoMinimo(ComunicacaoInternaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da ComunicacaoInterna
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setAssunto(dadosSQL.getString("assunto"));
	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta
	 * rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar
	 * os atributos básicos do objeto e alguns atributos relacionados de
	 * relevância para o contexto da aplicação.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasicoMontadoSubordinada(ComunicacaoInternaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da ComunicacaoInterna
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setTipoDestinatario(dadosSQL.getString("tipoDestinatario"));
		obj.setTipoRemetente(dadosSQL.getString("tipoRemetente"));
		obj.setTipoComunicadoInterno(dadosSQL.getString("tipoComunicadoInterno"));
		obj.setAssunto(dadosSQL.getString("assunto"));
		obj.setMensagem(dadosSQL.getString("mensagem"));
		obj.setMensagemSMS(dadosSQL.getString("mensagemSMS"));
		obj.setEnviarSMS(dadosSQL.getBoolean("enviarSMS"));
		obj.setTipoMarketing(dadosSQL.getBoolean("tipoMarketing"));
		obj.setTipoLeituraObrigatoria(dadosSQL.getBoolean("tipoLeituraObrigatoria"));
		obj.setDigitarMensagem(dadosSQL.getBoolean("digitarMensagem"));
		obj.setRemoverCaixaSaida(dadosSQL.getBoolean("removerCaixaSaida"));
		obj.setCodigoTipoOrigemComunicacaoInterna(dadosSQL.getInt("codigoTipoOrigemComunicacaoInterna"));
		obj.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.valueOf(dadosSQL.getString("tipoOrigemComunicacaoInternaEnum")));

		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Pessoa
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("pessoa.codigo")));
		obj.getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));
		obj.getResponsavel().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Aluno
		obj.getAluno().setCodigo(new Integer(dadosSQL.getInt("aluno")));
		// Dados do Professor
		obj.getProfessor().setCodigo(new Integer(dadosSQL.getInt("professor")));

		obj.getArquivoAnexo().setCodigo(dadosSQL.getInt("arquivoAnexo"));
		obj.getArquivoAnexo().setPastaBaseArquivo(dadosSQL.getString("anexoPastaBase"));
		obj.getArquivoAnexo().setNome(dadosSQL.getString("nomeAnexo"));

		obj.getResponsavel().getArquivoImagem().setCodigo(dadosSQL.getInt("arquivoImagem"));
		obj.getResponsavel().getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("imagemPastaBase"));
		obj.getResponsavel().getArquivoImagem().setNome(dadosSQL.getString("nomeImagem"));
		montarDadosListaArquivosAnexo(obj, Uteis.NIVELMONTARDADOS_TODOS);

		int posicaoCursor = dadosSQL.getRow();
		ComunicadoInternoDestinatarioVO comunicadoDestinatarioVO = null;
		do {
			comunicadoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
			comunicadoDestinatarioVO.setCodigo(new Integer(dadosSQL.getInt("destinatario.codigo")));
			// Dados do Destinatario(Pessoa)
			comunicadoDestinatarioVO.getDestinatario().setCodigo(new Integer(dadosSQL.getInt("pessoaDestinatario.codigo")));
			comunicadoDestinatarioVO.getDestinatario().setNome(dadosSQL.getString("pessoaDestinatario.nome"));
			if (comunicadoDestinatarioVO.getCodigo() != 0) {
				obj.getComunicadoInternoDestinatarioVOs().add(comunicadoDestinatarioVO);
			}
		} while (dadosSQL.next());
		dadosSQL.absolute(posicaoCursor);
	}

	/**
	 * Consulta que espera um ResultSet com todos os campos e dados de objetos
	 * relacionados, Para reconstituir o objeto por completo, de uma determinada
	 * entidade.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosCompleto(ComunicacaoInternaVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		// Dados ComunicacaoInterna
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setTipoDestinatario(dadosSQL.getString("tipoDestinatario"));
		obj.setTipoRemetente(dadosSQL.getString("tipoRemetente"));
		obj.setTipoComunicadoInterno(dadosSQL.getString("tipoComunicadoInterno"));
		obj.setPrioridade(dadosSQL.getString("prioridade"));
		obj.setDataExibicaoInicial(dadosSQL.getDate("dataExibicaoInicial"));
		obj.setDataExibicaoFinal(dadosSQL.getDate("dataExibicaoFinal"));
		obj.setAssunto(dadosSQL.getString("assunto"));
		obj.setMensagem(dadosSQL.getString("mensagem"));
		obj.setMensagemSMS(dadosSQL.getString("mensagemSMS"));
		obj.setEnviarSMS(dadosSQL.getBoolean("enviarSMS"));
		obj.setTipoMarketing(dadosSQL.getBoolean("tipoMarketing"));
		obj.setTipoLeituraObrigatoria(dadosSQL.getBoolean("tipoLeituraObrigatoria"));
		obj.setDigitarMensagem(dadosSQL.getBoolean("digitarMensagem"));
		obj.setRemoverCaixaSaida(dadosSQL.getBoolean("removerCaixaSaida"));
		obj.setCodigoTipoOrigemComunicacaoInterna(dadosSQL.getInt("codigoTipoOrigemComunicacaoInterna"));
		obj.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.valueOf(dadosSQL.getString("tipoOrigemComunicacaoInternaEnum")));
		obj.setEnviarEmail(dadosSQL.getBoolean("enviaremail"));
		obj.setEnviarEmailInstitucional(dadosSQL.getBoolean("enviaremailinstitucional"));
		// obj.setArquivo((byte[]) dadosSQL.getObject("arquivo"));
		obj.setNivelMontarDados(NivelMontarDados.TODOS);
		// Dados da Unidade Ensino
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("UnidadeEnsino.codigo")));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("UnidadeEnsino.nome"));
		obj.getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Responsavel Logado
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel.codigo")));
		obj.getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));
		obj.getResponsavel().setEmail(dadosSQL.getString("responsavel.email"));
		obj.getResponsavel().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Aluno
		obj.getAluno().setCodigo(new Integer(dadosSQL.getInt("aluno.codigo")));
		obj.getAluno().setNome(dadosSQL.getString("aluno.nome"));
		obj.getAluno().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Cargo
		obj.getCargo().setCodigo(new Integer(dadosSQL.getInt("cargo.codigo")));
		obj.getCargo().setNome(dadosSQL.getString("cargo.nome"));
		obj.getCargo().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Departamento
		obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("departamento.codigo")));
		obj.getDepartamento().setNome(dadosSQL.getString("departamento.nome"));
		obj.getDepartamento().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Funcionario
		obj.getFuncionario().getPessoa().setCodigo(new Integer(dadosSQL.getInt("funcionario.codigo")));
		obj.getFuncionario().getPessoa().setNome(dadosSQL.getString("funcionario.nome"));
		obj.getFuncionario().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Professor
		obj.getProfessor().setCodigo(new Integer(dadosSQL.getInt("professor.codigo")));
		obj.getProfessor().setNome(dadosSQL.getString("professor.nome"));
		obj.getProfessor().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Coordenador
		obj.getCoordenador().setCodigo(new Integer(dadosSQL.getInt("coordenador.codigo")));
		obj.getCoordenador().setNome(dadosSQL.getString("coordenador.nome"));
		obj.getCoordenador().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Disciplina
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina.codigo")));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
		obj.getDisciplina().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Turma
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma.codigo")));
		obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
		obj.getTurma().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Area do conhecimento
		obj.getAreaConhecimento().setCodigo(new Integer(dadosSQL.getInt("areaConhecimento.codigo")));
		obj.getAreaConhecimento().setNome(dadosSQL.getString("areaConhecimento.nome"));
		obj.getAreaConhecimento().setNivelMontarDados(NivelMontarDados.BASICO);

		obj.getArquivoAnexo().setCodigo(dadosSQL.getInt("arquivoAnexo"));
		obj.getArquivoAnexo().setPastaBaseArquivo(dadosSQL.getString("anexoPastaBase"));
		obj.getArquivoAnexo().setNome(dadosSQL.getString("nomeAnexo"));

		obj.getResponsavel().getArquivoImagem().setCodigo(dadosSQL.getInt("arquivoImagem"));
		obj.getResponsavel().getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("imagemPastaBase"));
		obj.getResponsavel().getArquivoImagem().setNome(dadosSQL.getString("nomeImagem"));
		
		obj.getListaConsultaDestinatario().setListaConsulta(getFacadeFactory().getComunicadoInternoDestinatarioFacade().consultarPorCodigoComunicacaoInterna(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario, obj.getListaConsultaDestinatario().getLimitePorPagina(), obj.getListaConsultaDestinatario().getOffset()));		
		obj.getListaConsultaDestinatario().setTotalRegistrosEncontrados(getFacadeFactory().getComunicadoInternoDestinatarioFacade().consultarTotalRegistroPorCodigoComunicacaoInterna(obj.getCodigo()));
		montarDadosListaArquivosAnexo(obj, Uteis.NIVELMONTARDADOS_TODOS);
		if (obj.getListaArquivosAnexo().isEmpty() && obj.getArquivoAnexo().getCodigo() != 0) {
			obj.getListaArquivosAnexo().add(obj.getArquivoAnexo());
		}
//		ComunicadoInternoDestinatarioVO comunicadoDestinatarioVO = null;
//
//		do {
//			comunicadoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
//			// Dados da Comunicacao Interna Destinatario
//			comunicadoDestinatarioVO.setCodigo(new Integer(dadosSQL.getInt("destinatario.codigo")));
//			comunicadoDestinatarioVO.setTipoComunicadoInterno(dadosSQL.getString("destinatario.tipoComunicadoInterno"));
//			comunicadoDestinatarioVO.setComunicadoInterno(dadosSQL.getInt("destinatario.comunicadoInterno"));
//			comunicadoDestinatarioVO.setCiJaLida(dadosSQL.getBoolean("destinatario.ciJaLida"));
//			comunicadoDestinatarioVO.setCiJaRespondida(dadosSQL.getBoolean("destinatario.ciJaRespondida"));
//			comunicadoDestinatarioVO.setDataLeitura(dadosSQL.getDate("destinatario.dataLeitura"));
//			// Dados do Destinatario(Pessoa)
//			comunicadoDestinatarioVO.getDestinatario().setCodigo(new Integer(dadosSQL.getInt("pessoaDestinatario.codigo")));
//			if(Uteis.isAtributoPreenchido(comunicadoDestinatarioVO.getDestinatario().getCodigo())){
//				comunicadoDestinatarioVO.getDestinatario().setNome(dadosSQL.getString("pessoaDestinatario.nome"));
//				comunicadoDestinatarioVO.getDestinatario().setEmail(dadosSQL.getString("pessoaDestinatario.email"));	
//			}else{
//				comunicadoDestinatarioVO.getDestinatario().setNome(dadosSQL.getString("destinatario.nome"));
//				comunicadoDestinatarioVO.getDestinatario().setEmail(dadosSQL.getString("destinatario.email"));
//			}
//			if (comunicadoDestinatarioVO.getCodigo() != 0) {
//				obj.getComunicadoInternoDestinatarioVOs().add(comunicadoDestinatarioVO);
//			}
//		} while (dadosSQL.next());

	}

	public ComunicacaoInternaVO inicializarDadosRespotaComunicado(ComunicacaoInternaVO obj, UsuarioVO usuario) throws Exception {
		String destinatario = obj.getTipoRemetente();
		String remetente = obj.getTipoDestinatario();

		if (usuario.getIsApresentarVisaoAluno() || usuario.getIsApresentarVisaoPais()) {
			remetente = "AL";
		} else if (usuario.getIsApresentarVisaoProfessor()) {
			remetente = "PR";
		} else if (usuario.getIsApresentarVisaoAdministrativa()) {
			remetente = "FU";
		} else {
			remetente = "CO";
		}

		if (destinatario.equals("TU")) {
			PessoaVO responsavelComunicadoAnterior = getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(obj.getResponsavel().getCodigo(), null, false, false, usuario);
			if (responsavelComunicadoAnterior.getAluno()) {
				destinatario = "AL";
			} else if (responsavelComunicadoAnterior.getCoordenador()) {
				destinatario = "CO";
			} else if (responsavelComunicadoAnterior.getProfessor()) {
				destinatario = "PR";
			} else {
				destinatario = "FU";
			}
		}

		ComunicacaoInternaVO comunicaoResposta = new ComunicacaoInternaVO();
		comunicaoResposta.setTipoComunicadoInterno(obj.getTipoComunicadoInterno());
		comunicaoResposta.setUnidadeEnsino(obj.getUnidadeEnsino());
		comunicaoResposta.setComunicadoInternoOrigem(obj);
		comunicaoResposta.setNovoObj(true);
		comunicaoResposta.setCodigo(0);
		comunicaoResposta.setAssunto("RES: " + obj.getAssunto());
		String resposta = obj.getMensagem();
		if (!obj.getTipoMarketing()) {			
			String tagLinha = "<p class=\"MsoNormal\" style=\"width: 100%;\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>";
			String tagLinhaAssunto = "<p class=\"MsoNormal\" style=\"width: 100%;\"><span style=\"font-family: Arial; color: black; font-size: 8pt;\"><strong>Assunto: " + obj.getAssunto() + "</strong></span></p>";
			String tagLinhaDe = "<p class=\"MsoNormal\" style=\"width: 100%;\"><span style=\"font-family: Arial; color: black; font-size: 8pt;\"><strong>De: " + obj.getResponsavel().getNome() + "</strong></span></p>";
			String tagLinhaData = "<p class=\"MsoNormal\" style=\"width: 100%;\"><span style=\"font-family: Arial; color: black; font-size: 8pt;\"><strong>Data: " + obj.getData_Apresentar() + "</strong></span></p>";
			String tagLinhaTracejado = "<p class=\"MsoNormal\" style=\"width: 100%;height:2px;border-bottom:1px dashed #999999;\"></p>";
			if (resposta.contains("<td style=\"width: 469.5pt; background: #FFFFFF; padding: 0cm;\" width=\"626\">")) {
				resposta = resposta.replaceFirst("<td style=\"width: 469.5pt; background: #FFFFFF; padding: 0cm;\" width=\"626\">", "<td style=\"width: 469.5pt; background: #FFFFFF; padding: 0cm;\" width=\"626\">" + tagLinha + tagLinha + tagLinha + tagLinha + tagLinhaTracejado + tagLinhaAssunto + tagLinhaDe + tagLinhaData + tagLinha);
			} else if (resposta.contains("<tr style=\"mso-yfti-irow:2\">")) {
				resposta = resposta.replaceFirst("<tr style=\"mso-yfti-irow:2\">", "<tr style=\"mso-yfti-irow:2\"><td style=\"width: 469.5pt; background: #FFFFFF; padding: 0cm;\" width=\"626\">"+ tagLinha + tagLinha + tagLinha + tagLinha + tagLinhaTracejado + tagLinhaAssunto + tagLinhaDe + tagLinhaData + tagLinha + "</td></tr><tr>");			
			} else if (resposta.contains("<p class=\"MsoNormal\" style=\"width: 100%;\">")) {
				resposta = resposta.replaceFirst("<p class=\"MsoNormal\" style=\"width: 100%;\">", tagLinha + tagLinha + tagLinha + tagLinha + tagLinhaTracejado + tagLinhaAssunto + tagLinhaDe + tagLinhaData + tagLinha + "<p class=\"MsoNormal\" style=\"width: 100%;\">");			
			} else if (resposta.contains("<body>")) {
				resposta = resposta.replaceFirst("<body>", "<body>" + tagLinha + tagLinha + tagLinha + tagLinha + tagLinhaTracejado + tagLinhaAssunto + tagLinhaDe + tagLinhaData + tagLinha);
			} else {
				resposta = obj.getMensagemComLayout("");
				resposta = resposta.replace("<td style=\"width: 469.5pt; background: #FFFFFF; padding: 0cm;\" width=\"626\">", "<td style=\"width: 469.5pt; background: #FFFFFF; padding: 0cm;\" width=\"626\">" + tagLinha + tagLinha + tagLinha + tagLinha + tagLinhaTracejado + tagLinhaAssunto + tagLinhaDe + tagLinhaData + tagLinha);
			}
		} else {
			resposta = obj.getMensagemComLayout("");
		}
		comunicaoResposta.setMensagem(resposta);
		comunicaoResposta.setData(new Date());
		comunicaoResposta.setDigitarMensagem(true);
		comunicaoResposta.setTipoDestinatario(destinatario);
		comunicaoResposta.setTipoRemetente(remetente);
		comunicaoResposta.setComunicadoInternoDestinatarioVOs(new ArrayList<ComunicadoInternoDestinatarioVO>());
		comunicaoResposta.getComunicadoInternoDestinatarioVOs().add(inicializarDadosRespotaComunicadoInternoDestinatarioVO(obj.getResponsavel(), usuario));
		// esses atributos deve fica depois da chamada desse metodo pois para
		// inicializar dados preciso do campos antes da alteracao
		comunicaoResposta.setResponsavel(usuario.getPessoa());
		return comunicaoResposta;
	}

	public ComunicadoInternoDestinatarioVO inicializarDadosRespotaComunicadoInternoDestinatarioVO(PessoaVO remetenteComunicadoAnterior, UsuarioVO usuario) throws Exception {
		ComunicadoInternoDestinatarioVO comunicado = new ComunicadoInternoDestinatarioVO();
		comunicado.setDestinatario(remetenteComunicadoAnterior);
		comunicado.setCiJaRespondida(false);
		comunicado.setComunicadoInterno(0);
		comunicado.setDataLeitura(new Date());
		comunicado.setNovoObj(false);
		comunicado.setTipoComunicadoInterno("LE");
		return comunicado;
	}

	public void validarTipoMarketing(ComunicacaoInternaVO obj) {
		if (obj.getTipoMarketing()) {
			obj.setTipoLeituraObrigatoria(false);
		}
	}

	public void validarTipoLeituraObrigatoria(ComunicacaoInternaVO obj) {
		if (obj.getTipoLeituraObrigatoria()) {
			obj.setTipoMarketing(false);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarComunicacaoInternaTipoMarketingTipoLeituraObrigatoria(final ComunicacaoInternaVO obj) throws Exception {
		try {
			final String sql = "UPDATE ComunicacaoInterna set tipoMarketing=?, tipoLeituraObrigatoria=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setBoolean(1, obj.getTipoMarketing());
					sqlAlterar.setBoolean(2, obj.getTipoLeituraObrigatoria());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsavel por alterar o campo TipoMarketing de Comunicação
	 * Interna apos a visualização do Marketing.
	 * 
	 * @param comunicacaoInternaVO
	 * @throws Exception
	 * @Autor Carlos
	 */
	public void alterarTipoMarketingAposVisualizacao(ComunicacaoInternaVO comunicacaoInternaVO) throws Exception {
		comunicacaoInternaVO.setTipoMarketing(false);
		alterarComunicacaoInternaTipoMarketingTipoLeituraObrigatoria(comunicacaoInternaVO);
	}

	/**
	 * Método responsavel por alterar o campo TipoLeituraObrigatoria apos a
	 * leitura do mesmo e por invocar o metodo que altera o campo ciJaLida para
	 * 'TRUE' em ComunicadoInternoDestinatario.
	 * 
	 * @param comunicacaoInternaVO
	 * @param usuarioLogado
	 * @throws Exception
	 * @Autor Carlos
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTipoLeituraObrigtoriaAPosLeitura(ComunicacaoInternaVO comunicacaoInternaVO, UsuarioVO usuarioLogado) throws Exception {
		getFacadeFactory().getComunicadoInternoDestinatarioFacade().alterarComunicadoInternoDestinatarioLeituraObrigatoria(comunicacaoInternaVO.getCodigo().intValue(), usuarioLogado.getPessoa().getCodigo().intValue());
	}

	public List<ComunicacaoInternaVO> consultaRapidaComunicacaoInternaNaoLidas(Integer codUsuarioLogado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct ComunicacaoInterna.codigo, ComunicacaoInterna.data, ComunicacaoInterna.assunto from ComunicacaoInterna ");
		sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicadoInternoDestinatario.comunicadoInterno = ComunicacaoInterna.codigo ");
		sqlStr.append("WHERE ComunicadoInternoDestinatario.ciJaLida = 'false' and funcionario.pessoa = ");
		sqlStr.append(codUsuarioLogado.intValue());
		// sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TC' ");
		sqlStr.append(" ORDER BY ComunicacaoInterna.data");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapidaMinimo(tabelaResultado);
	}

	public List<ComunicacaoInternaVO> consultaRapidaComunicacaoInterna(Integer codUsuarioLogado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct ComunicacaoInterna.codigo, ComunicacaoInterna.data, ComunicacaoInterna.assunto from ComunicacaoInterna ");
		sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicadoInternoDestinatario.comunicadoInterno = ComunicacaoInterna.codigo ");
		sqlStr.append("WHERE ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codUsuarioLogado.intValue());
		sqlStr.append(" and (tipodestinatario in ('FU', 'DE', 'CA', 'AR')) ");
		sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = false ");
		sqlStr.append(" ORDER BY ComunicacaoInterna.data desc");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapidaMinimo(tabelaResultado);
	}

	public Integer consultaRapidaComunicacaoInterna(Integer codUsuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count(ComunicacaoInterna.codigo) as qtd from ComunicacaoInterna ");
		sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicadoInternoDestinatario.comunicadoInterno = ComunicacaoInterna.codigo ");
		sqlStr.append("WHERE ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codUsuarioLogado.intValue());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtd"));
		} else {
			return new Integer(0);
		}
	}

	public Integer consultaRapidaComunicacaoInternaNaoLidas(Integer codUsuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count( DISTINCT ComunicacaoInterna.codigo) as qtd from ComunicacaoInterna ");
		sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicadoInternoDestinatario.comunicadoInterno = ComunicacaoInterna.codigo ");
		sqlStr.append("WHERE ComunicadoInternoDestinatario.ciJaLida = 'false' and ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codUsuarioLogado.intValue());
		// sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TC' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtd"));
		} else {
			return new Integer(0);
		}
	}

	public Integer consultaRapidaComunicacaoInternaNaoLidasVisaoProfessor(Integer codUsuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count( DISTINCT ComunicacaoInterna.codigo) as qtd from ComunicacaoInterna ");
		sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicadoInternoDestinatario.comunicadoInterno = ComunicacaoInterna.codigo ");
		sqlStr.append("WHERE ComunicadoInternoDestinatario.ciJaLida = 'false' and (ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codUsuarioLogado.intValue());
		// sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TC') ");
		sqlStr.append(")" );
		//sqlStr.append(" AND (ComunicacaoInterna.tipoDestinatario in ('PR', 'TP', 'TC')) ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtd"));
		} else {
			return new Integer(0);
		}
	}

	public Integer consultaRapidaComunicacaoInternaNaoLidasVisaoCoordenador(Integer codUsuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count(DISTINCT ComunicacaoInterna.codigo) as qtd from ComunicacaoInterna ");
		sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicadoInternoDestinatario.comunicadoInterno = ComunicacaoInterna.codigo ");
		sqlStr.append("WHERE ComunicadoInternoDestinatario.ciJaLida = 'false' and (ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codUsuarioLogado.intValue());
		// sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TC') ");
		sqlStr.append(") ");
		//sqlStr.append(" AND ComunicacaoInterna.tipoDestinatario in ('CO', 'TC') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtd"));
		} else {
			return new Integer(0);
		}
	}

	public Integer consultaRapidaComunicacaoInternaNaoLidasVisaoAluno(Integer codUsuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count(DISTINCT ComunicacaoInterna.codigo) as qtd from ComunicacaoInterna ");
		sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicadoInternoDestinatario.comunicadoInterno = ComunicacaoInterna.codigo ");
		sqlStr.append("WHERE ComunicadoInternoDestinatario.ciJaLida = 'false' and (ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codUsuarioLogado.intValue());
		// sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TC') ");
		sqlStr.append(") AND (ComunicacaoInterna.tipoDestinatario in ( 'AL', 'TA', 'TU', 'TC', 'AA', 'TD', 'ALAS')) ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtd"));
		} else {
			return new Integer(0);
		}
	}

	@Override
	public Integer consultaRapidaComunicacaoInternaNaoLidasVisaoPais(Integer codUsuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count(DISTINCT ComunicacaoInterna.codigo) as qtd from ComunicacaoInterna ");
		sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicadoInternoDestinatario.comunicadoInterno = ComunicacaoInterna.codigo ");
		sqlStr.append("WHERE ComunicadoInternoDestinatario.ciJaLida = 'false' and (ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codUsuarioLogado.intValue());
		// sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TC') ");
		sqlStr.append(") AND (ComunicacaoInterna.tipoDestinatario in ('RL', 'TA', 'TU', 'TC', 'AA', 'TD', 'ALAS', 'AL')) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtd"));
		} else {
			return new Integer(0);
		}
	}

	public Integer consultaRapidaComunicacaoInternaNaoLidasVisaoFuncionario(Integer codUsuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count(DISTINCT ComunicacaoInterna.codigo) as qtd from ComunicacaoInterna ");
		sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicadoInternoDestinatario.comunicadoInterno = ComunicacaoInterna.codigo ");
		sqlStr.append("WHERE ComunicadoInternoDestinatario.ciJaLida = 'false' and ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codUsuarioLogado.intValue()); 
		sqlStr.append(" ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtd"));
		} else {
			return new Integer(0);
		}
	}

	public Integer consultaRapidaComunicacaoInternaLidasNaoLidas(Integer codUsuarioLogado, Boolean situacao, Date dataIni, Date dataFim) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count( DISTINCT ComunicacaoInterna.codigo) as qtd from ComunicacaoInterna ");
		// sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicadoInternoDestinatario.comunicadoInterno = ComunicacaoInterna.codigo ");
		sqlStr.append(" WHERE ").append(realizarGeracaoWherePeriodo(dataIni, dataFim, "data", false));		
		sqlStr.append("AND ComunicadoInternoDestinatario.ciJaLida = ").append(situacao.booleanValue()).append(" and ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codUsuarioLogado.intValue());
		sqlStr.append(" AND ComunicadoInternoDestinatario.removercaixaentrada IS FALSE ");
		// sqlStr.append(") ");
		// sqlStr.append(") OR ComunicacaoInterna.tipoDestinatario = 'TC' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtd"));
		} else {
			return new Integer(0);
		}
	}

	public Integer consultaRapidaComunicacaoInternaLidasNaoLidasFuncionarioOuAdministrador(Integer codUsuarioLogado, Boolean situacao, Date dataIni, Date dataFim) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT count( DISTINCT ComunicacaoInterna.codigo) as qtd from ComunicacaoInterna ");
		// sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicadoInternoDestinatario.comunicadoInterno = ComunicacaoInterna.codigo ");
		sqlStr.append(" WHERE ").append(realizarGeracaoWherePeriodo(dataIni, dataFim, "data", false));		
		sqlStr.append("AND ComunicadoInternoDestinatario.ciJaLida = ").append(situacao.booleanValue()).append(" and ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codUsuarioLogado.intValue());
		// sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TC' ");
		sqlStr.append(" AND (ComunicacaoInterna.tipoDestinatario in ('FU', 'DE', 'TC')) ");
		sqlStr.append(" AND ComunicadoInternoDestinatario.removercaixaentrada IS FALSE ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtd"));
		} else {
			return new Integer(0);
		}
	}

	public List<ComunicacaoInternaVO> consultaRapidaComunicacaoInternaNaoLidasMenu(Integer codUsuarioLogado) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false);
		// sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = comunicacaoInterna.funcionario ");
		sqlStr.append("WHERE ComunicadoInternoDestinatario.ciJaLida = 'false' and ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codUsuarioLogado.intValue());
		// sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TC' ");
		sqlStr.append(" order by ComunicacaoInterna.data desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS );
	}

	public List<ComunicacaoInternaVO> montarDadosConsultaRapidaMinimo(SqlRowSet tabelaResultado) throws Exception {
		List<ComunicacaoInternaVO> vetResultado = new ArrayList<ComunicacaoInternaVO>(0);
		while (tabelaResultado.next()) {
			ComunicacaoInternaVO obj = new ComunicacaoInternaVO();
			montarDadosBasicoMinimo(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void criarFileCorpoMensagemEmail(ComunicacaoInternaVO obj) throws Exception {
		try {
			obj.getListaFileCorpoMensagem().add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email" + File.separator + obj.getImgCima() + ".jpg"));
			obj.getListaFileCorpoMensagem().add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email" + File.separator + obj.getImgBaixo() + ".jpg"));
			// obj.getListaFileCorpoMensagem().add(new
			// File(UteisJSF.obterCaminhoWebImagem() + File.separator + "email"
			// + File.separator + obj.getImgCima() + ".jpg"));
			// obj.getListaFileCorpoMensagem().add(new
			// File(UteisJSF.obterCaminhoWebImagem() + File.separator + "email"
			// + File.separator + obj.getImgBaixo() + ".jpg"));
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarNotificacaoMensagemPreDefinidaTextoPadrao(TextoPadraoBancoCurriculumVO textoPadrao, PessoaVO pessoa, ParceiroVO parceiro, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {

		try {
			ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
			comunicacaoInternaVO.setResponsavel(usuario.getPessoa());
			comunicacaoInternaVO.setAssunto(textoPadrao.getNome());
			comunicacaoInternaVO.setEnviarEmail(true);
			// comunicacaoInternaVO.setMensagem(comunicacaoInternaVO.getMensagemComLayoutTextoPadrao(textoPadrao.getTexto()));
			comunicacaoInternaVO.setMensagem(textoPadrao.getTexto());
			comunicacaoInternaVO.setDigitarMensagem(true);
			ComunicadoInternoDestinatarioVO cid = new ComunicadoInternoDestinatarioVO();
			if (pessoa != null && !pessoa.getCodigo().equals(0)) {
				cid.setDestinatario(pessoa);
			} else if (parceiro != null && !parceiro.getCodigo().equals(0)) {
				cid.setDestinatarioParceiro(parceiro);
			}
			comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(cid);
			comunicacaoInternaVO.setTipoDestinatario("AL");
			// incluir(comunicacaoInternaVO, false, usuario,
			// configuracaoGeralSistemaVO);
			enviarEmailComunicacaoInterna(comunicacaoInternaVO, usuario, configuracaoGeralSistemaVO, comunicacaoInternaVO.getPersonalizacaoMensagemAutomaticaVO(), null, false);

		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarNotificacaoMatriculaSerasa(NegociacaoRecebimentoVO negociacaoRecebimentoVO, PessoaVO func, String textoPadrao, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {

		try {
			ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
			comunicacaoInternaVO.setMensagem("");
			comunicacaoInternaVO.setResponsavel(usuario.getPessoa());
			comunicacaoInternaVO.setAssunto("Notificação Pagamento Parcela de Matrícula Negativada");
			comunicacaoInternaVO.setEnviarEmail(true);
			comunicacaoInternaVO.setMensagem(comunicacaoInternaVO.getMensagemComLayoutTextoPadrao(textoPadrao));
			comunicacaoInternaVO.setDigitarMensagem(true);
			ComunicadoInternoDestinatarioVO cid = new ComunicadoInternoDestinatarioVO();
			PessoaVO respFin = func.getResponsavelFinanceiroAluno();
			if (respFin.getCodigo().intValue() > 0) {
				cid.setDestinatario(respFin);
				cid.setCiJaLida(Boolean.FALSE);
				cid.setEmail(respFin.getEmail());
				cid.setNome(respFin.getNome());
			} else {
				cid.setDestinatario(func);
				cid.setCiJaLida(Boolean.FALSE);
				cid.setEmail(func.getEmail());
				cid.setNome(func.getNome());
			}
			cid.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
			comunicacaoInternaVO.adicionarObjComunicadoInternoDestinatarioVOs(cid);
			// comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().add(cid);
			comunicacaoInternaVO.setTipoDestinatario("FU");
			comunicacaoInternaVO.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());
			comunicacaoInternaVO.getResponsavel().setNome("SEI - SISTEMA EDUCACIONAL INTEGRADO");
			incluir(comunicacaoInternaVO, false, usuario, configuracaoGeralSistemaVO, null);
			// enviarEmailComunicacaoInterna(comunicacaoInternaVO, usuario,
			// configuracaoGeralSistemaVO);
			// getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar,
			// false, new UsuarioVO(), config);

		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarNotificacaoSuspensaoMatricula(ComunicacaoInternaVO comunicacaoInternaVO, PessoaVO aluno, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {

		try {
			configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO(), null);
			comunicacaoInternaVO.setEnviarEmail(true);
			comunicacaoInternaVO.setDigitarMensagem(true);
			ComunicadoInternoDestinatarioVO cid = new ComunicadoInternoDestinatarioVO();
			getFacadeFactory().getPessoaFacade().carregarDados(aluno, NivelMontarDados.BASICO, usuario);
			cid.setDestinatario(aluno);
			cid.setCiJaLida(Boolean.FALSE);
			cid.setEmail(aluno.getEmail());
			cid.setNome(aluno.getNome());
			cid.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
			comunicacaoInternaVO.adicionarObjComunicadoInternoDestinatarioVOs(cid);
			comunicacaoInternaVO.setTipoDestinatario("AL");
			comunicacaoInternaVO.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());
			comunicacaoInternaVO.getResponsavel().setNome("SEI - SISTEMA EDUCACIONAL INTEGRADO");
			incluir(comunicacaoInternaVO, false, new UsuarioVO(), configuracaoGeralSistemaVO, null);

		} catch (Exception e) {
			throw e;
		} finally {
		}
	}
	
	public String removerCabecalhoERodapeComunicadoInterno(String body) throws Exception {
		String html = body;

		if(html.contains("<img id=\"_x0000_i1025\"")){
			String parte1, parte2;
			parte1 = html.substring(html.indexOf("<img id=\"_x0000_i1025\""), html.length());
			parte2 = parte1.substring(0, parte1.indexOf(">") + 1);		
			html = html.replaceAll(parte2, "");	
		}
		if(html.contains("<img id=\"_x0000_i1028\"")){
			String parte1, parte2;
			parte1 = html.substring(html.indexOf("<img id=\"_x0000_i1028\""), html.length());
			parte2 = parte1.substring(0, parte1.indexOf(">") + 1);		
			html = html.replaceAll(parte2, "");	
		}
		return html;
	}

	@Override
	public void preencherTodosListaTurma(List<TurmaVO> listaTurmaVOs) {
		for (TurmaVO turmaVO : listaTurmaVOs) {
			turmaVO.setTurmaSelecionada(Boolean.TRUE);
		}
	}

	@Override
	public void desmarcarTodosListaTurma(List<TurmaVO> listaTurmaVOs) {
		for (TurmaVO turmaVO : listaTurmaVOs) {
			turmaVO.setTurmaSelecionada(Boolean.FALSE);
		}
	}

	public Boolean realizarVerificacaoNivelEducacionalCursoTurmaSelecionada(List<TurmaVO> listaTurmaVOs, UsuarioVO usuarioVO) {
		for (TurmaVO turmaVO : listaTurmaVOs) {
			String nivelEducacional = "";
			if (turmaVO.getTurmaSelecionada()) {
				if (turmaVO.getTurmaAgrupada()) {
					nivelEducacional = getFacadeFactory().getCursoFacade().consultarNivelEducacionalPorTurmaAgrupadaUnico(turmaVO.getCodigo(), usuarioVO);
					if (nivelEducacional.equals("PO") || nivelEducacional.equals("EX")) {
						return Boolean.FALSE;
					} else {
						return Boolean.TRUE;
					}
				} else {
					if (turmaVO.getCurso().getNivelEducacional().equals("PO") || turmaVO.getCurso().getNivelEducacional().equals("EX")) {
						return Boolean.FALSE;
					} else {
						return Boolean.TRUE;
					}
				}
			}
		}
		return Boolean.FALSE;
	}
	
	@Override
	public ComunicacaoInternaVO realizarMontagemNotificacaoAlteracaoDataProva(InscricaoVO inscricaoVO, ConfiguracaoGeralSistemaVO configuracaoSistema, UsuarioVO usuario, String corpoMensagemSMS, String corpoMensagem, Boolean enviarSmsInformativoAlteracaoDataProva,String assuntoEmail) throws Exception {
		ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
		List<ComunicadoInternoDestinatarioVO> listaComunicadoInternoDestinatarioVO = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
			ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
			comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
			if (!inscricaoVO.getCandidato().getEmail().equals("")) {
				comunicadoInternoDestinatarioVO.setEmail(inscricaoVO.getCandidato().getEmail());
				comunicadoInternoDestinatarioVO.setNome(inscricaoVO.getCandidato().getNome());
				comunicadoInternoDestinatarioVO.getDestinatario().setEmail(inscricaoVO.getCandidato().getEmail());
			} else if (!inscricaoVO.getCandidato().getEmail2().equals("")) {
				comunicadoInternoDestinatarioVO.setEmail(inscricaoVO.getCandidato().getEmail2());
				comunicadoInternoDestinatarioVO.setNome(inscricaoVO.getCandidato().getNome());
				comunicadoInternoDestinatarioVO.getDestinatario().setEmail(inscricaoVO.getCandidato().getEmail2());
			}
			comunicadoInternoDestinatarioVO.getDestinatario().setCelular(inscricaoVO.getCandidato().getCelular());
			comunicadoInternoDestinatarioVO.getDestinatario().setNome(inscricaoVO.getCandidato().getNome());
			listaComunicadoInternoDestinatarioVO.add(comunicadoInternoDestinatarioVO);
			comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(listaComunicadoInternoDestinatarioVO);
			comunicacaoInternaVO.setTipoRemetente("FU");
			comunicacaoInternaVO.setTipoDestinatario("AL");
			comunicacaoInternaVO.setTipoComunicadoInterno("LE");
			comunicacaoInternaVO.setResponsavel(usuario.getPessoa());
			PessoaVO responsavel = configuracaoSistema.getResponsavelPadraoComunicadoInterno();
			comunicacaoInternaVO.setResponsavel(responsavel);
			comunicacaoInternaVO.setMensagem(obterMensagemFormatadaMensagemNotificacaoAlteracaoDataProva(inscricaoVO, corpoMensagem));
			comunicacaoInternaVO.setEnviarEmail(Boolean.TRUE);
			comunicacaoInternaVO.setEnviarSMS(enviarSmsInformativoAlteracaoDataProva);
			corpoMensagemSMS = obterMensagemFormatadaMensagemNotificacaoAlteracaoDataProva(inscricaoVO, corpoMensagemSMS);
			String msgSMS = corpoMensagemSMS;
			if (msgSMS.length() > 150) {
				msgSMS = msgSMS.substring(0, 150);
			}
			comunicacaoInternaVO.setMensagemSMS(msgSMS);
			comunicacaoInternaVO.setAssunto(assuntoEmail);
		return comunicacaoInternaVO;
	}
	
	@Override
	public void executarNotificacaoAlteracaoDataProvaCandidado(ComunicacaoInternaVO comunicacaoInternaVO,ConfiguracaoGeralSistemaVO configuracaoSistema, UsuarioVO usuario) throws Exception{
		try {
			incluir(comunicacaoInternaVO, false, usuario, configuracaoSistema,null);
		} catch (Exception e) {
			throw e;
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoAlteracaoDataProva(InscricaoVO inscricaoVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_INSCRICAO.name(), Integer.toString(inscricaoVO.getCodigo()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CANDIDATO.name(), Uteis.getNomeResumidoPessoa(inscricaoVO.getCandidato().getNome()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_PROVA.name(), inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva_Apresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.PROCESSO_SELETIVO.name(), inscricaoVO.getProcSeletivo().getDescricao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), inscricaoVO.getCursoOpcao1().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MOTIVO.name(), inscricaoVO.getItemProcessoSeletivoDataProva().getMotivoAlteracaoDataProva());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), inscricaoVO.getUnidadeEnsino().getNome());
		return mensagemTexto;
	}
	
	@Override
	public void realizarTrocarLogoEmailPorUnidadeEnsino(ComunicacaoInternaVO comunicacaoInternaVO, ConfiguracaoGeralSistemaVO configuracaoSistema, UsuarioVO usuario) {
		String parte1, parte2;
		String texto = comunicacaoInternaVO.getMensagem();
		String dominio = null;
		if(Uteis.isAtributoPreenchido(configuracaoSistema.getUrlAcessoExternoAplicacao())) {
			dominio = (configuracaoSistema.getUrlAcessoExternoAplicacao().endsWith("/") ? configuracaoSistema.getUrlAcessoExternoAplicacao() : configuracaoSistema.getUrlAcessoExternoAplicacao()+"/")+"resources/imagens/email/";
		}else {
			FacesContext context = FacesContext.getCurrentInstance();
			String paginaAtual = context == null ? "" : context.getViewRoot().getViewId();
			if (paginaAtual.contains("visaoProfessor") 
		
      			|| paginaAtual.contains("visaoAluno") 
      			|| paginaAtual.contains("visaoCoordenador") 
      			|| paginaAtual.contains("visaoCandidato") 
      			|| paginaAtual.contains("visaoParceiro") 
      			|| paginaAtual.contains("visaoPreInscricao")) {
      		dominio = "../resources/imagens/email/";
      	}else if (paginaAtual.contains("/relatorio/")) {
      		dominio = configuracaoSistema.getUrlAcessoExternoAplicacao();          	
      	} else {
      		dominio = "../../resources/imagens/email/";        		
      	}
		}
      	String seiCima = dominio+"cima_sei.jpg";
      	String seiBaixo = dominio+"baixo_sei.jpg";
      	UnidadeEnsinoVO unidadeEnsinoVO = null;
      	if(Uteis.isAtributoPreenchido(comunicacaoInternaVO.getUnidadeEnsino())) {
      		try {
				unidadeEnsinoVO =  getAplicacaoControle().getUnidadeEnsinoVO(comunicacaoInternaVO.getUnidadeEnsino().getCodigo(), usuario);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      	}
		if (texto.contains("id=\"_x0000_i1025\"")) {
			parte1 = texto.substring(texto.indexOf("id=\"_x0000_i1025\""), texto.length());
			parte1 = parte1.substring(parte1.indexOf("src=\"")+5, parte1.length());
			parte2 = parte1.substring(0, parte1.indexOf("\""));
			
			if (Uteis.isAtributoPreenchido(unidadeEnsinoVO) && Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCaminhoBaseLogoEmailCima()) && Uteis.isAtributoPreenchido(unidadeEnsinoVO.getNomeArquivoLogoEmailCima())) {
				seiCima = configuracaoSistema.getUrlExternoDownloadArquivo() + "/" + unidadeEnsinoVO.getCaminhoBaseLogoEmailCima().replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogoEmailCima();
				try {
					URL url = new URL(seiCima);
					URLConnection conn = url.openConnection();
					conn.getContent();			
				}catch (Exception e) {
					seiCima = dominio+"cima_sei.jpg";
				}
			}			
			if(!parte2.equalsIgnoreCase(seiCima)) {
				texto = texto.replaceAll(parte2, seiCima);
			}
		}
		
		if (texto.contains("id=\"_x0000_i1028\"")) {			
			parte1 = texto.substring(texto.indexOf("id=\"_x0000_i1028\""), texto.length());
			parte1 = parte1.substring(parte1.indexOf("src=\"")+5, parte1.length());
			parte2 = parte1.substring(0, parte1.indexOf("\""));
			if (Uteis.isAtributoPreenchido(unidadeEnsinoVO) && Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCaminhoBaseLogoEmailBaixo())&& Uteis.isAtributoPreenchido(unidadeEnsinoVO.getNomeArquivoLogoEmailBaixo())) {
				seiBaixo = configuracaoSistema.getUrlExternoDownloadArquivo() + "/" + unidadeEnsinoVO.getCaminhoBaseLogoEmailBaixo().replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogoEmailBaixo();
				try {
					URL url = new URL(seiBaixo);
					URLConnection conn = url.openConnection();
					conn.getContent();					
				}catch (Exception e) {
					seiBaixo = dominio+"baixo_sei.jpg";
				}
			} 
			if(!parte2.equalsIgnoreCase(seiBaixo)) {
				texto = texto.replaceAll(parte2, seiBaixo );
			}
			
			
		}
		comunicacaoInternaVO.setMensagem(texto);
	}
	
	@Override
	public String redimensionarTextoHTMLAplicativo(ComunicacaoInternaVO comunicacaoInternaVO, String caminhoWeb, String texto) throws Exception {	
		getFacadeFactory().getComunicacaoInternaFacade().realizarTrocarLogoEmailPorUnidadeEnsino(comunicacaoInternaVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null), null);
		texto = UteisNfe.removerQuebraLinhaFinalTag(comunicacaoInternaVO.getMensagem());
		texto = UteisNfe.removerEspacoDuplo(texto);
		if (texto.contains("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\">")) {
			texto = texto.replaceAll("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\">", "<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt;width: 88%\">");
		}
		if (texto.contains("<td style=\"height: 59.25pt; padding: 0cm;\"><p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt;width: 88%\">")) {
			texto = texto.replace("<td style=\"height: 59.25pt; padding: 0cm;\"><p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt;width: 88%\">", "<td style=\"height: 59.25pt; padding: 0cm;\"><p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt;\">");
		}
		if (texto.contains("<td style=\"height: 43.5pt; padding: 0cm;\"><p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt;width: 88%\">")) {
			texto = texto.replace("<td style=\"height: 43.5pt; padding: 0cm;\"><p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt;width: 88%\">", "<td style=\"height: 59.25pt; padding: 0cm;\"><p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt;\">");
		}
		return texto;
	}
	
	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação com limite e offset
	 * 
	 * @author Victor Hugo
	 */
	@Override
	public void consultaRapidaPorSituacaoEntradaLimitOffset(DataModelo controleConsulta, Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, Integer limit, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false);
		// sqlStr.append("WHERE responsavel = ");
		// sqlStr.append(codigo.intValue());
		sqlStr.append(" WHERE 1=1 ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" AND (data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and ((data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		sqlStr.append("AND ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append("");
		if (lida != null && respondida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
			// sqlStr.append(" or ((ComunicadoInternoDestinatario.ciJaRespondida = ");
			// sqlStr.append(respondida.booleanValue());
			// sqlStr.append(") and (ComunicacaoInterna.tipoComunicadoInterno = 'RE'))");
		} else if (lida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
			// } else if (respondida != null) {
			// sqlStr.append(" and ComunicadoInternoDestinatario.ciJaRespondida = ");
			// sqlStr.append(respondida.booleanValue());
			// sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = 'RE' ");
		}
		sqlStr.append(" ORDER BY ComunicacaoInterna.data desc");
		if (limit != null && offset != null) {
			sqlStr.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
	}
	
	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação com limite e offset
	 * 
	 * @author Victor Hugo
	 */
	@Override
	public Integer consultaQuantidadeRapidaPorSituacaoEntrada(Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		// sqlStr.append("WHERE responsavel = ");
		// sqlStr.append(codigo.intValue());
		sqlStr.append(" WHERE 1=1 ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" AND (data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and ((data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		sqlStr.append("AND ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append("");
		if (lida != null && respondida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
			// sqlStr.append(" or ((ComunicadoInternoDestinatario.ciJaRespondida = ");
			// sqlStr.append(respondida.booleanValue());
			// sqlStr.append(") and (ComunicacaoInterna.tipoComunicadoInterno = 'RE'))");
		} else if (lida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
			// } else if (respondida != null) {
			// sqlStr.append(" and ComunicadoInternoDestinatario.ciJaRespondida = ");
			// sqlStr.append(respondida.booleanValue());
			// sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = 'RE' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}
	
	private StringBuffer getSQLPadraoConsultaBasicaQuantidade() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT count(DISTINCT ComunicacaoInterna.codigo) FROM ComunicacaoInterna ");
		str.append("LEFT JOIN pessoa on pessoa.codigo = ComunicacaoInterna.responsavel ");
		str.append("LEFT JOIN arquivo arquivoImagem on pessoa.arquivoImagem = arquivoImagem.codigo ");
		str.append("LEFT JOIN arquivo arquivoAnexo on ComunicacaoInterna.arquivoAnexo = arquivoAnexo.codigo ");
		str.append("LEFT JOIN ComunicadoInternoDestinatario on ComunicacaoInterna.codigo = ComunicadoInternoDestinatario.comunicadointerno ");
		return str;
	}
	
	@Override
	public List<ComunicacaoInternaVO> consultarComunicacaoInternaPorAlunoFichaAluno(PessoaVO aluno, String mesAno, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, Integer limite, Integer offset) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct comunicacaointerna.codigo, comunicacaointerna.data, comunicacaointerna.assunto, ");
		sb.append(" responsavel.codigo AS \"responsavel.codigo\",  responsavel.nome AS \"responsavel.nome\", responsavel.cpf AS \"responsavel.cpf\", ");
		sb.append(" arquivo.codigo AS codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo, comunicacaointerna.mensagem, comunicacaointerna.tipoMarketing ");
		sb.append(" from comunicacaointerna ");
		sb.append(" inner join comunicadointernodestinatario on comunicadointernodestinatario.comunicadoInterno = comunicacaointerna.codigo ");
		sb.append(" inner join pessoa responsavel on responsavel.codigo = comunicacaointerna.responsavel ");
		sb.append(" left join arquivo on arquivo.codigo = responsavel.arquivoImagem ");
		sb.append(" where comunicadointernodestinatario.destinatario = ").append(aluno.getCodigo());
		if (mesAno != null && !mesAno.equals("")) {
			sb.append(" and extract(month from comunicacaointerna.data) = ").append(getMesData(mesAno));
			sb.append(" and extract(year from comunicacaointerna.data) = ").append(getAnoData(mesAno));
		}
		sb.append(" order by comunicacaointerna.data desc");
		sb.append(" limit ").append(limite);
		sb.append(" offset ").append(offset);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ComunicacaoInternaVO> listaComunicacaoInternaVOs = new ArrayList<ComunicacaoInternaVO>(0);
		while (tabelaResultado.next()) {
			ComunicacaoInternaVO obj = new ComunicacaoInternaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setData(tabelaResultado.getDate("data"));
			obj.setTipoMarketing(tabelaResultado.getBoolean("tipoMarketing"));
			obj.setAssunto(tabelaResultado.getString("assunto"));
			obj.setMensagem(tabelaResultado.getString("mensagem"));
			obj.getResponsavel().setCodigo(tabelaResultado.getInt("responsavel.codigo"));
			obj.getResponsavel().setNome(tabelaResultado.getString("responsavel.nome"));
			obj.getResponsavel().setCPF(tabelaResultado.getString("responsavel.cpf"));
			obj.getResponsavel().getArquivoImagem().setCodigo(tabelaResultado.getInt("codArquivo"));
			obj.getResponsavel().getArquivoImagem().setPastaBaseArquivo(tabelaResultado.getString("pastaBaseArquivo"));
			obj.getResponsavel().getArquivoImagem().setNome(tabelaResultado.getString("nomeArquivo"));
			obj.getResponsavel().setUrlFotoAluno(getFacadeFactory().getPessoaFacade().inicializarDadosFotoUsuario(obj.getResponsavel(), configuracaoGeralSistemaVO, usuarioVO));
			montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			montarDadosListaArquivosAnexo(obj, Uteis.NIVELMONTARDADOS_TODOS);
			if(obj.getTipoMarketing() && Uteis.isAtributoPreenchido(obj.getArquivoAnexo())) {
				obj.getListaArquivosAnexo().add(obj.getArquivoAnexo());
			}
			
			obj.setMensagem(getFacadeFactory().getComunicacaoInternaFacade().substituirTag(obj.getMensagem(), aluno ));
			listaComunicacaoInternaVOs.add(obj);
		}
		return listaComunicacaoInternaVOs;
	}
	
	@Override
	public Integer consultarTotalRegistroComunicacaoInternaPorAlunoFichaAluno(Integer aluno, String mesAno, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select  count(distinct comunicacaointerna.codigo) as qtde ");
		sb.append(" from comunicacaointerna ");
		sb.append(" inner join comunicadointernodestinatario on comunicadointernodestinatario.comunicadoInterno = comunicacaointerna.codigo ");
		sb.append(" where comunicadointernodestinatario.destinatario = ").append(aluno);
		if (mesAno != null && !mesAno.equals("")) {
			sb.append(" and extract(month from comunicacaointerna.data) = ").append(getMesData(mesAno));
			sb.append(" and extract(year from comunicacaointerna.data) = ").append(getAnoData(mesAno));
		}		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());		
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}
	
	
	public Integer getMesData(String mesAno) {
		if (mesAno != null && !mesAno.equals("")) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			return Uteis.getMesConcatenadoReferencia(mes);
		}
		return 0;
	}
	
	public Integer getAnoData(String mesAno) {
		if (mesAno != null && !mesAno.equals("")) {
			String ano = mesAno.substring(mesAno.indexOf("/") + 1);
			return Integer.parseInt(ano);
		}
		return 0;
	}

	@Override
	public List<SelectItem> consultarMesAnoComunicacaoInternaPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select mes ||'/'|| ano AS mesAno from (");
		sb.append(" select distinct case ");
		sb.append(" when extract(month from comunicacaointerna.data) = 1 then 'JAN' ");
		sb.append(" when extract(month from comunicacaointerna.data) = 2 then 'FEV' ");
		sb.append(" when extract(month from comunicacaointerna.data) = 3 then 'MAR' ");
		sb.append(" when extract(month from comunicacaointerna.data) = 4 then 'ABR' ");
		sb.append(" when extract(month from comunicacaointerna.data) = 5 then 'MAI' ");
		sb.append(" when extract(month from comunicacaointerna.data) = 6 then 'JUN' ");
		sb.append(" when extract(month from comunicacaointerna.data) = 7 then 'JUL' ");
		sb.append(" when extract(month from comunicacaointerna.data) = 8 then 'AGO' ");
		sb.append(" when extract(month from comunicacaointerna.data) = 9 then 'SET' ");
		sb.append(" when extract(month from comunicacaointerna.data) = 10 then 'OUT' ");
		sb.append(" when extract(month from comunicacaointerna.data) = 11 then 'NOV' ");
		sb.append(" when extract(month from comunicacaointerna.data) = 12 then 'DEZ' ");
		sb.append(" end AS mes, extract(year from comunicacaointerna.data) AS ano, comunicacaointerna.data ");
		sb.append(" from comunicacaointerna ");
		sb.append(" inner join comunicadointernodestinatario on comunicadointernodestinatario.comunicadoInterno = comunicacaointerna.codigo  ");
		sb.append(" where comunicadointernodestinatario.destinatario = ").append(aluno);
		sb.append(" order by comunicacaointerna.data desc) as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		HashMap<String, String> mapMesAnoVOs = new HashMap<String, String>(0);
		List<SelectItem> listaSelectItemMesAnoItemEmprestimoVOs = new ArrayList<SelectItem>(0);
		listaSelectItemMesAnoItemEmprestimoVOs.add(new SelectItem("", ""));
		while (tabelaResultado.next()) {
			if (!mapMesAnoVOs.containsKey(tabelaResultado.getString("mesAno"))) {
				listaSelectItemMesAnoItemEmprestimoVOs.add(new SelectItem(tabelaResultado.getString("mesAno"), tabelaResultado.getString("mesAno")));
				mapMesAnoVOs.put(tabelaResultado.getString("mesAno"), tabelaResultado.getString("mesAno"));
			}
		}
		return listaSelectItemMesAnoItemEmprestimoVOs;
	}
	public Integer consultaRapidaQuantidadeEntradaVisaoAluno(Integer codigo, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData){
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		sqlStr.append("WHERE (ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append(") and ComunicadoInternoDestinatario.removerCaixaEntrada = 'false' ");
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and lower(ComunicacaoInterna.assunto) like lower('%").append(filtroPorAssunto).append("%') ");
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" and lower(pessoa.nome) like lower('%").append(filtroPorRemetente).append("%') ");
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') ilike ('").append(filtroPorData).append("%') ");
		}
		
		sqlStr.append(" and (ComunicacaoInterna.tipoDestinatario = 'AL' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TA' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'RL' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TC' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'AA' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TU' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TD' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'ALAS') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("count"));
		} else {
			return new Integer(0);
		}
	}
	
	public Integer consultaRapidaQuantidadeEntradaVisaoProfessor(Integer codigo, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		sqlStr.append("WHERE ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append(" and ComunicadoInternoDestinatario.removerCaixaEntrada = 'false'  ");
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and lower(ComunicacaoInterna.assunto) like lower('%").append(filtroPorAssunto).append("%') ");
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" and lower(pessoa.nome) like lower('%").append(filtroPorRemetente).append("%') ");
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') ilike ('").append(filtroPorData).append("%') ");
		}
		//sqlStr.append(" and (ComunicacaoInterna.tipoDestinatario = 'PR' ");
		//sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TP' OR ComunicacaoInterna.tipoDestinatario = 'TC') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("count"));
		} else {
			return new Integer(0);
		}
	}
	
	public Integer consultaRapidaQuantidadeEntradaVisaoCoordenador(Integer codigo, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		sqlStr.append("WHERE ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append(" and ComunicadoInternoDestinatario.removerCaixaEntrada = 'false'  ");
		//sqlStr.append(" and ComunicacaoInterna.tipoDestinatario = 'CO' ");
		
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and lower(ComunicacaoInterna.assunto) like lower('%").append(filtroPorAssunto).append("%') ");
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" and lower(pessoa.nome) like lower('%").append(filtroPorRemetente).append("%') ");
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') ilike ('").append(filtroPorData).append("%') ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("count"));
		} else {
			return new Integer(0);
		}
	}
	
	public Integer consultaRapidaQuantidadePorCodigoResponsavelFuncionarioOuAdministrador(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData){
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		sqlStr.append(" and ComunicacaoInterna.responsavel = ComunicadoInternoDestinatario.destinatario ");
		sqlStr.append(" WHERE responsavel = ");
		sqlStr.append(valorConsulta.intValue());
		sqlStr.append(" AND (ComunicacaoInterna.tipoRemetente not in ('PR', 'AL')) ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" and ((data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		if (!tipoSaidaConsulta.equals("")) {
			sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = '");
			sqlStr.append(tipoSaidaConsulta);
			sqlStr.append("'");
		}
		sqlStr.append(" and ComunicacaoInterna.removerCaixaSaida = 'false'  ");
		
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and lower(ComunicacaoInterna.assunto) like lower('%").append(filtroPorAssunto).append("%') ");
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" and lower(pessoa.nome) like lower('%").append(filtroPorRemetente).append("%') ");
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') ilike ('").append(filtroPorData).append("%') ");
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("count"));
		} else {
			return new Integer(0);
		}
	}
	
	public Integer consultaRapidaQuantidadePorCodigoResponsavel(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		sqlStr.append("WHERE responsavel = ");
		sqlStr.append(valorConsulta.intValue());
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" and ((data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		if (!tipoSaidaConsulta.equals("")) {
			sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = '");
			sqlStr.append(tipoSaidaConsulta);
			sqlStr.append("'");
		}
		sqlStr.append(" and ComunicacaoInterna.removerCaixaSaida = 'false'  ");
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and lower(ComunicacaoInterna.assunto) like lower('%").append(filtroPorAssunto).append("%') ");
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" and lower(pessoa.nome) like lower('%").append(filtroPorRemetente).append("%') ");
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') ilike ('").append(filtroPorData).append("%') ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("count"));
		} else {
			return new Integer(0);
		}
	}
	
	public Integer consultaRapidaQuantidadePorCodigoResponsavelVisaoProfessor(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		sqlStr.append("WHERE responsavel = ");
		sqlStr.append(valorConsulta.intValue());
		sqlStr.append(" AND (ComunicacaoInterna.tipoRemetente = 'PR' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoRemetente = 'TP') ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" and ((data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		if (!tipoSaidaConsulta.equals("")) {
			sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = '");
			sqlStr.append(tipoSaidaConsulta);
			sqlStr.append("'");
		}
		sqlStr.append(" and ComunicacaoInterna.removerCaixaSaida = 'false' ");
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and lower(ComunicacaoInterna.assunto) like lower('%").append(filtroPorAssunto).append("%') ");
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" and lower(pessoa.nome) like lower('%").append(filtroPorRemetente).append("%') ");
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') ilike ('").append(filtroPorData).append("%') ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("count"));
		} else {
			return new Integer(0);
		}
	}
	
	public Integer consultaRapidaQuantidadePorCodigoResponsavelVisaoAluno(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		sqlStr.append("WHERE responsavel = ");
		sqlStr.append(valorConsulta.intValue());
		sqlStr.append(" AND (ComunicacaoInterna.tipoRemetente = 'AL' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoRemetente = 'TA' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoRemetente = 'TU') ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" and ((data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		if (!tipoSaidaConsulta.equals("")) {
			sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = '");
			sqlStr.append(tipoSaidaConsulta);
			sqlStr.append("'");
		}
		sqlStr.append(" and ComunicacaoInterna.removerCaixaSaida = 'false'  ");
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and lower(ComunicacaoInterna.assunto) like lower('%").append(filtroPorAssunto).append("%') ");
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" and lower(pessoa.nome) like lower('%").append(filtroPorRemetente).append("%') ");
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') ilike ('").append(filtroPorData).append("%') ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("count"));
		} else {
			return new Integer(0);
		}
	}
	
	public Integer consultaRapidaQuantidadePorCodigoResponsavelVisaoCoordenador(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		sqlStr.append("WHERE responsavel = ");
		sqlStr.append(valorConsulta.intValue());
		sqlStr.append("AND ComunicacaoInterna.tipoRemetente = 'CO'  ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" and ((data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		if (!tipoSaidaConsulta.equals("")) {
			sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = '");
			sqlStr.append(tipoSaidaConsulta);
			sqlStr.append("'");
		}
		sqlStr.append(" and ComunicacaoInterna.removerCaixaSaida = 'false'  ");
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and lower(ComunicacaoInterna.assunto) like lower('%").append(filtroPorAssunto).append("%') ");
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" and lower(pessoa.nome) like lower('%").append(filtroPorRemetente).append("%') ");
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') ilike ('").append(filtroPorData).append("%') ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("count"));
		} else {
			return new Integer(0);
		}
	}
	
	public Integer consultaRapidaQuantidadeSituacaoEntradaVisaoFuncionarioOuAdministrador(Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		sqlStr.append(" WHERE 1=1");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" AND (data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and (data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 ') ");
		}
		sqlStr.append("AND ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append(" AND (ComunicacaoInterna.tipoDestinatario = 'FU' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'DE') ");
		if (lida != null && respondida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
		} else if (lida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
		}
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and lower(ComunicacaoInterna.assunto) like lower('%").append(filtroPorAssunto).append("%') ");
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" and lower(pessoa.nome) like lower('%").append(filtroPorRemetente).append("%') ");
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') ilike ('").append(filtroPorData).append("%') ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("count"));
		} else {
			return new Integer(0);
		}
	}
	
	public Integer consultaRapidaQuantidadeSituacaoEntradaLimitOffset(Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		sqlStr.append(" WHERE 1=1 ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" AND (data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and ((data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		sqlStr.append("AND ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append("");
		if (lida != null && respondida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
		} else if (lida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
		}
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and lower(ComunicacaoInterna.assunto) like lower('%").append(filtroPorAssunto).append("%') ");
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" and lower(pessoa.nome) like lower('%").append(filtroPorRemetente).append("%') ");
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') ilike ('").append(filtroPorData).append("%') ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("count"));
		} else {
			return new Integer(0);
		}
	}
	
	public Integer consultaRapidaQuantidadeEntradaLimite(Integer codigo, String tipoDestinatario, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		sqlStr.append(" WHERE ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append(" and ComunicadoInternoDestinatario.removerCaixaEntrada = 'false'  ");
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and lower(ComunicacaoInterna.assunto) like lower('%").append(filtroPorAssunto).append("%') ");
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" and lower(pessoa.nome) like lower('%").append(filtroPorRemetente).append("%') ");
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') ilike ('").append(filtroPorData).append("%') ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("count"));
		} else {
			return new Integer(0);
		}
	}
	
	public void consultaRapidaPorSituacaoEntradaLimitOffset(DataModelo controleConsulta, Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, Integer limit, Integer offset, UsuarioVO usuario, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false);
		sqlStr.append(" WHERE 1=1 ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" AND (data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and ((data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		sqlStr.append("AND ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append(" AND ComunicadoInternoDestinatario.removercaixaentrada IS FALSE ");
		if (lida != null && respondida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
		} else if (lida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
		}
		
		List<String> filtros =  new ArrayList<String>(0);
		if (!filtroPorAssunto.equals("")) {
			sqlStr.append(" and sem_acentos(ComunicacaoInterna.assunto) ilike sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorAssunto+PERCENT);
		}
		if (!filtroPorRemetente.equals("")) {
			sqlStr.append(" AND sem_acentos(pessoa.nome) ILIKE sem_acentos(?) ");
			filtros.add(PERCENT+filtroPorRemetente+PERCENT);
		}
		if(!filtroPorData.equals("")) {
			sqlStr.append(" and to_char(data , 'dd/MM/yyyy') = ? ");
			filtros.add(filtroPorData);
		}
		
		sqlStr.append(" ORDER BY ComunicacaoInterna.data desc");
		if (limit != null && offset != null) {
			sqlStr.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}
	}	
	
	

	@Override
	public Integer consultaQuantidadeRapidaPorSituacaoEntradaProfessor(Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaQuantidade();
		// sqlStr.append("WHERE responsavel = ");
		// sqlStr.append(codigo.intValue());
		sqlStr.append(" WHERE 1=1 ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" AND (data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and ((data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		sqlStr.append("AND ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append("");
		if (lida != null && respondida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
			// sqlStr.append(" or ((ComunicadoInternoDestinatario.ciJaRespondida = ");
			// sqlStr.append(respondida.booleanValue());
			// sqlStr.append(") and (ComunicacaoInterna.tipoComunicadoInterno = 'RE'))");
		} else if (lida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
			// } else if (respondida != null) {
			// sqlStr.append(" and ComunicadoInternoDestinatario.ciJaRespondida = ");
			// sqlStr.append(respondida.booleanValue());
			// sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = 'RE' ");
		}
		sqlStr.append(" and (ComunicacaoInterna.tipoDestinatario = 'PR' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TP' OR ComunicacaoInterna.tipoDestinatario = 'TC') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}
	
	
	@Override
	public void consultaRapidaPorEntradaLimiteOffsetVisaoProfessor(DataModelo controleConsulta, Integer codigo, String tipoDestinatario, Integer limite, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer offset) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false);
		sqlStr.append("WHERE ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append(" and ComunicadoInternoDestinatario.removerCaixaEntrada = 'false'  ");
		sqlStr.append(" and (ComunicacaoInterna.tipoDestinatario = 'PR' ");
		sqlStr.append(" OR ComunicacaoInterna.tipoDestinatario = 'TP' OR ComunicacaoInterna.tipoDestinatario = 'TC') ");
		sqlStr.append(" ORDER BY ComunicacaoInterna.data desc limit ");
		sqlStr.append(limite.intValue());
		sqlStr.append(" offset ").append(offset.intValue());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaRapida(tabelaResultado, nivelMontarDados));
		}else {
			controleConsulta.setListaConsulta(new ArrayList(0));
			controleConsulta.setTotalRegistrosEncontrados(0);
		}		
	}
	
	public List<ComunicacaoInternaVO> consultarPorTipoOrigemComunicacaoInterna(Integer codigoTipoOrigemComunicacaoInterna, TipoOrigemComunicacaoInternaEnum tipoOrigemComunicacaoInternaEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT * FROM ComunicacaoInterna ");	
		sqlStr.append(" WHERE tipoorigemcomunicacaointernaenum = '").append(tipoOrigemComunicacaoInternaEnum).append("'");
		sqlStr.append(" AND codigotipoorigemcomunicacaointerna = ").append(codigoTipoOrigemComunicacaoInterna);
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return (montarDadosConsultaRapidaMinimo(tabelaResultado));
	}
	
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
	public void excluirPorTipoOrigemComunicacaoInterna(Integer codigoTipoOrigemComunicacaoInterna, TipoOrigemComunicacaoInternaEnum tipoOrigemComunicacaoInternaEnum, UsuarioVO usuario) {		
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("DELETE FROM ComunicacaoInterna ");	
		sqlStr.append(" WHERE tipoorigemcomunicacaointernaenum = '").append(tipoOrigemComunicacaoInternaEnum).append("'");
		sqlStr.append(" AND codigotipoorigemcomunicacaointerna = ").append(codigoTipoOrigemComunicacaoInterna);
		getConexao().getJdbcTemplate().update(sqlStr.toString());
		
	}
    
    @Override
    public String consultarBannerComunicadoInternoMarketingDisponivelUsuario(UsuarioVO usuario) throws Exception {
		
		StringBuffer sqlStr = new StringBuffer("select ComunicacaoInterna.codigo, ComunicacaoInterna.assunto, ComunicacaoInterna.data, arquivoAnexo.nome, arquivoAnexo.pastaBaseArquivo from ComunicacaoInterna ");
		sqlStr.append(" inner JOIN arquivo arquivoAnexo on ComunicacaoInterna.arquivoAnexo = arquivoAnexo.codigo ");
		sqlStr.append(" WHERE ComunicacaoInterna.dataExibicaoInicial <= '").append(Uteis.getDataJDBC(new Date())).append("' and  ComunicacaoInterna.dataExibicaoFinal >= '").append(Uteis.getDataJDBC(new Date())).append("' ");
		sqlStr.append(" and ComunicacaoInterna.tipoMarketing ");
		sqlStr.append(" and exists (select destinatario from ComunicadoInternoDestinatario where ComunicadoInternoDestinatario.destinatario = ? and ComunicacaoInterna.codigo = ComunicadoInternoDestinatario.comunicadointerno ) ");
		sqlStr.append( "ORDER BY ComunicacaoInterna.data ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), usuario.getPessoa().getCodigo());
		StringBuilder html = new StringBuilder("");
		while(tabelaResultado.next()) {
		String url = getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuario).getUrlExternoDownloadArquivo() + "/"
				+ tabelaResultado.getString("pastaBaseArquivo").replaceAll("\\\\", "/").trim() + "/"
				+ tabelaResultado.getString("nome").replaceAll("\\\\", "/").trim();
//			html.append("<div class=\"col-md-12 pn text-center\">");		
			html.append(" 	<img u=\"image\" height=\"270px\" id=\"bannerMky").append(tabelaResultado.getInt("codigo")).append("\" src=\"").append(url).append("\"  title=\"").append(tabelaResultado.getString("assunto")).append("\"  >");		
//			html.append("</div>");
		}
		
		return html.toString();
	}
    

    /**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação com limite e offset
	 * 
	 * @author Victor Hugo
	 */
	@Override
	public List<ComunicacaoInternaVO> consultaRapidaPorSituacaoEntradaLimitOffset(Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, Integer limit, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false);
		// sqlStr.append("WHERE responsavel = ");
		// sqlStr.append(codigo.intValue());
		sqlStr.append(" WHERE 1=1 ");
		if (dataIni != null && dataFim != null) {
			sqlStr.append(" AND (data >= '");
			sqlStr.append(Uteis.getDataJDBC(dataIni));
			sqlStr.append("') and ((data <= '");
			sqlStr.append(Uteis.getDataJDBC(dataFim));
			sqlStr.append(" 23:59:29 '))");
		}
		sqlStr.append("AND ComunicadoInternoDestinatario.destinatario = ");
		sqlStr.append(codigo.intValue());
		sqlStr.append("");
		if (lida != null && respondida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
			// sqlStr.append(" or ((ComunicadoInternoDestinatario.ciJaRespondida = ");
			// sqlStr.append(respondida.booleanValue());
			// sqlStr.append(") and (ComunicacaoInterna.tipoComunicadoInterno = 'RE'))");
		} else if (lida != null) {
			sqlStr.append(" and ComunicadoInternoDestinatario.ciJaLida = ");
			sqlStr.append(lida.booleanValue());
			// } else if (respondida != null) {
			// sqlStr.append(" and ComunicadoInternoDestinatario.ciJaRespondida = ");
			// sqlStr.append(respondida.booleanValue());
			// sqlStr.append(" and ComunicacaoInterna.tipoComunicadoInterno = 'RE' ");
		}
		sqlStr.append(" ORDER BY ComunicacaoInterna.data desc");
		if (limit != null && offset != null) {
			sqlStr.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}
	
	public void preencherDestinatariosPlanilhaExcel(FileUploadEvent uploadEvent, ArquivoVO arquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ComunicacaoInternaVO comunicacaoInterna, UsuarioVO usuario) throws Exception {
		ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
		ArrayList<MatriculaVO> listaMatriculaDestinatarioVOs = realizarProcessamentoPlanilhaDestinatario(uploadEvent, arquivoVO, configuracaoGeralSistemaVO, comunicacaoInterna, usuario);
		consultarAdicionarDestinatarioPlanilha(listaMatriculaDestinatarioVOs, comunicacaoInterna, comunicadoInternoDestinatarioVO, usuario);
	}
	
	public void consultarAdicionarDestinatarioPlanilha(ArrayList<MatriculaVO> listaMatriculaDestinatarioVOs, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, UsuarioVO usuario) {
		ArrayList<PessoaVO> listaDestinatarios = new ArrayList<>();
		try {
			for(MatriculaVO matriculaVO : listaMatriculaDestinatarioVOs){
				PessoaVO destinatario = new PessoaVO();
				destinatario = consultarDestinatarioPorMatriculaEmail(matriculaVO, destinatario, usuario);
				listaDestinatarios.add(destinatario);
			}
			getFacadeFactory().getPessoaFacade().adicionarPessoaListaDestinatario(listaDestinatarios, comunicacaoInterna, comunicadoInternoDestinatario);
		} catch (Exception e) {
			comunicacaoInterna.getErrosProcessamentoPlanilha().add(e.getMessage());
		}
	}
	
	public PessoaVO consultarDestinatarioPorMatriculaEmail(MatriculaVO matriculaVO, PessoaVO destinatario, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
			destinatario = getFacadeFactory().getPessoaFacade().consultarPorRegistroAcademico(matriculaVO.getMatricula(), false, usuario);
			if (!Uteis.isAtributoPreenchido(destinatario)) {
				destinatario = getFacadeFactory().getPessoaFacade().consultarPorMatricula(matriculaVO.getMatricula().trim(), Uteis.NIVELMONTARDADOS_COMBOBOX);
			} 
		}
		else if (!Uteis.isAtributoPreenchido(destinatario.getCodigo()) && Uteis.isAtributoPreenchido(matriculaVO.getAluno().getEmail())) {
			destinatario = getFacadeFactory().getPessoaFacade().consultarPorEmail(matriculaVO.getAluno().getEmail().trim(), "", Boolean.FALSE, Boolean.FALSE, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			
//			Caso não ache o destinatário mas informou nome e email é possível adicional email manualmente no comunicado
			if((!Uteis.isAtributoPreenchido(destinatario) && Uteis.isAtributoPreenchido(matriculaVO.getAluno().getNome()))) {
				PessoaVO destinatarioManual = new PessoaVO();
				destinatarioManual.setNome(matriculaVO.getAluno().getNome());
				destinatarioManual.setEmail(matriculaVO.getAluno().getEmail());
				return destinatarioManual;
			}
		}
		return destinatario;
	}
	
	public void montarErroProcessamentoPlanilha(MatriculaVO matriculaVO, ComunicacaoInternaVO comunicacaoInterna) {
		StringBuilder erroTratado = new StringBuilder("Não foi possível localizar destinatário para ");
		if(Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
			erroTratado.append("a matrícula ").append(matriculaVO.getMatricula());
		} else if(Uteis.isAtributoPreenchido(matriculaVO.getAluno().getEmail())) {
			erroTratado.append("o email ").append(matriculaVO.getAluno().getEmail());
		}
		comunicacaoInterna.getErrosProcessamentoPlanilha().add(erroTratado.toString());
	}

    public ArrayList<MatriculaVO> realizarProcessamentoPlanilhaDestinatario(FileUploadEvent uploadEvent, ArquivoVO arquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ComunicacaoInternaVO comunicacaoInterna, UsuarioVO usuario) throws Exception {
        ArrayList<MatriculaVO> listaMatriculaDestinatarioVOs = new ArrayList<>();
        InputStream stream = new FileInputStream(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVO_TMP.getValue() + File.separator + arquivoVO.getNome()));
        String extensao = uploadEvent.getUploadedFile().getName().substring(uploadEvent.getUploadedFile().getName().lastIndexOf(".") + 1);
        XSSFSheet mySheetXlsx = null;
		HSSFSheet mySheetXls = null;
//        Sheet mySheet = null;
        int linha = 0;
        int rowMax = 0;
        if (extensao.equalsIgnoreCase("xlsx")) {
        	XSSFWorkbook workbook = new XSSFWorkbook(stream);
			mySheetXlsx = workbook.getSheetAt(0);
			rowMax = mySheetXlsx.getLastRowNum();
			workbook.close();
			workbook = null;
        } else {
        	HSSFWorkbook workbook = new HSSFWorkbook(stream);
			mySheetXls = workbook.getSheetAt(0);
			rowMax = mySheetXls.getLastRowNum();
//			workbook.close();
			workbook = null;
        }
//        mySheet = workbook.getSheetAt(0);

//        for (int linha = 0; linha <= mySheet.getLastRowNum(); linha++) {
        Row row = null;
        while (linha <= rowMax) {
            if (extensao.equals("xlsx")) {
				row = mySheetXlsx.getRow(linha);
			} else {
				row = mySheetXls.getRow(linha);
			}
            
            if (row == null) {
                continue;
            }

            if (linha == 0) {
                validarDadosCabecalhoExcelTabela(row);
                linha++;
                continue;
            }

            MatriculaVO matriculaDestinatarioVO = new MatriculaVO();
            int coluna = 0;

            try {
                matriculaDestinatarioVO.getAluno().setNome(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
                matriculaDestinatarioVO.setMatricula(getValorCelula(++coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
                matriculaDestinatarioVO.getAluno().setEmail(getValorCelula(++coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
                linha++;
                if (!Uteis.isAtributoPreenchido(matriculaDestinatarioVO.getMatricula()) && !Uteis.isAtributoPreenchido(matriculaDestinatarioVO.getAluno().getEmail())) {
                    comunicacaoInterna.getErrosProcessamentoPlanilha().add("Erro na linha " + (linha+1) + ": o campo RA e EMAIL estão vazios. ");
                    continue;
                }
                if(!Uteis.isAtributoPreenchido(matriculaDestinatarioVO.getMatricula()) && !Uteis.getValidaEmail(matriculaDestinatarioVO.getAluno().getEmail())) {
                	comunicacaoInterna.getErrosProcessamentoPlanilha().add("Erro na linha " + (linha+1) + ": o email informado é informado. ");
                	continue;
                }

                listaMatriculaDestinatarioVOs.add(matriculaDestinatarioVO);
            } catch (Exception e) {
                throw new Exception("Erro na linha " + (linha + 1) + " e coluna " + CellReference.convertNumToColString(coluna) + " o valor '" + row.getCell(coluna).getStringCellValue() + "' não condiz com o valor esperado.");
            }
        }
        stream.close();
        stream = null;
//        workbook.close();
        return listaMatriculaDestinatarioVOs;
    }

	public Cell getValorCelula(int numeroCelula, Row row, Boolean isString) {
		Cell cell = row.getCell(numeroCelula);
		if (cell != null && isString) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		return cell;
	}

	private void validarDadosCabecalhoExcelTabela(Row row) throws Exception {
	    String[] esperado = {"nome", "ra", "email"};
	    for (int index = 0; index < esperado.length; index++) {
	        Cell cell = row.getCell(index);
	        if (cell == null || !cell.getStringCellValue().toLowerCase().equals(esperado[index])) {
	            throw new Exception("A coluna '" + CellReference.convertNumToColString(index) + "' deve ser referente ao campo " + esperado[index].toUpperCase() + ", favor informe um título com a descrição \"" + esperado[index] + "\".");
	        }
	    }
	}
	
	public void incluir(final ComunicacaoInternaVO obj, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ProgressBarVO progressBarVO) throws Exception {
		incluir(obj, controlarAcesso, usuario, configuracaoGeralSistemaVO, progressBarVO, false);
	}
	
	public void realizarValidacaoTamanhoAnexosEmail(UploadedFile uploadedFile, List<ArquivoVO> arquivoVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (uploadedFile != null
				&& Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)
				&& Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getTamanhoLimiteAnexoEmail())
				&& (arquivoVOs.stream().mapToLong(ArquivoVO::getTamanhoArquivo).sum() + uploadedFile.getSize()) > (configuracaoGeralSistemaVO.getTamanhoLimiteAnexoEmail().longValue() * 1024 * 1024)) {
			throw new Exception("O tamanho do anexo excedeu o limite definido na Configuração Geral do Sistema.");
		}
	}
}
