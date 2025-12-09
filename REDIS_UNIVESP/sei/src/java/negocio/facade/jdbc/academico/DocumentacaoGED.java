package negocio.facade.jdbc.academico;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.AssinarDocumentoEntregueVO;
import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TipoDocumentoGEDVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoLocalizadoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.enumeradores.TipoExigenciaDocumentoEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DocumentacaoGEDInterfaceFacade;

@Service
@Scope
@Lazy
public class DocumentacaoGED extends ControleAcesso implements DocumentacaoGEDInterfaceFacade {

	private static final long serialVersionUID = -2619397970379649529L;

	protected static String idEntidade;

	public DocumentacaoGED() throws Exception {
		super();
		setIdEntidade("MapaDocumentacaoDigitalizadoGED");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(DocumentacaoGEDVO obj, Boolean validarAcesso, List<TipoDocumentoGEDVO> listaAnteriorTipoDocumentoGED, UsuarioVO usuario) throws Exception {
		realizarAtualizacaoSituacaoDocumentacaoGed(obj);
		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuario);
		} else {
			alterar(obj, validarAcesso, usuario);
		}
		persistirTipoDocumentoGED(obj, listaAnteriorTipoDocumentoGED, usuario);
		obj.setPessoaNaoEncontrada(!Uteis.isAtributoPreenchido(obj.getPessoa()));
		obj.setCategoriaGedNaoEncontrada(!Uteis.isAtributoPreenchido(obj.getCategoriaGED()));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirComUploadArquivo(DocumentacaoGEDVO obj, List<TipoDocumentoGEDVO> listaAnteriorTipoDocumentoGED, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean validarAcesso, UsuarioVO usuario) throws Exception {
		PastaBaseArquivoEnum pastaBaseArquivoEnum = obj.getArquivo().getPastaBaseArquivoEnum();
		Integer codigoArquivo = obj.getArquivo().getCodigo();
		String pastaBase = obj.getArquivo().getPastaBaseArquivo();
		String nome = obj.getArquivo().getNome();
		try {
		if (!Uteis.isAtributoPreenchido(obj.getArquivo().getNome())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_arquivo"));
		}
		ArquivoVO arquivoVO = null;
		if (Uteis.isAtributoPreenchido(obj.getArquivo().getPastaBaseArquivoEnum()) && obj.getArquivo().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP)) {
			obj.getArquivo().setOrigem(OrigemArquivo.DOCUMENTO_GED.getValor());
			obj.getArquivo().setCodOrigem(obj.getCodigo());
			obj.getArquivo().setValidarDados(false);
			obj.getArquivo().setResponsavelUpload(usuario);
			obj.getArquivo().setDataUpload(new Date());
			obj.setUsuario(usuario);
			if (Uteis.isAtributoPreenchido(obj.getArquivo().getCodigo())) {
				arquivoVO = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivo().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
				obj.getArquivo().setNovoObj(false);
			} else {
				obj.getArquivo().setNovoObj(true);
			}
			
			if(obj.getUploadNovoArquivo()) {
				assinarDocumentoGED(obj, usuario);	
			}
			getFacadeFactory().getArquivoFacade().persistir(obj.getArquivo(), validarAcesso, usuario, configuracaoGeralSistemaVO);
			
			if(Uteis.isAtributoPreenchido(obj.getArquivo().getListaFuncionarioAssinarDigitalmenteVOs())) {
				realizarAssinaturaPeloFuncionario(obj.getArquivo(), obj.getMatricula(), configuracaoGeralSistemaVO, usuario);
			}
		}
		
		persistir(obj, validarAcesso, listaAnteriorTipoDocumentoGED, usuario);
		if (arquivoVO != null && !arquivoVO.obterUrlParaDownload(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), PastaBaseArquivoEnum.DIGITALIZACAO_GED).equals(obj.getArquivo().obterUrlParaDownload(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), PastaBaseArquivoEnum.DIGITALIZACAO_GED))) {
			File fileAntigo = new File(arquivoVO.obterUrlParaDownload(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo(), PastaBaseArquivoEnum.DIGITALIZACAO_GED));
			if (fileAntigo.exists()) {
				fileAntigo.delete();
			}
		}
		obj.getArquivo().setPastaBaseArquivoWeb(null);
		}catch (Exception e) {
			obj.getArquivo().setPastaBaseArquivoEnum(pastaBaseArquivoEnum);
			obj.getArquivo().setCodigo(codigoArquivo);
			obj.getArquivo().setPastaBaseArquivo(pastaBase);
			obj.getArquivo().setNome(nome);
			throw e;
		}
	}
	
	private void realizarAssinaturaPeloFuncionario(ArquivoVO arquivoVO, MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		DocumentoAssinadoVO documentoAssinado = new DocumentoAssinadoVO();
		documentoAssinado.setDataRegistro(new Date());
		documentoAssinado.setUsuario(usuario);
		documentoAssinado.setMatricula(matriculaVO);
		documentoAssinado.setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_GED);
		UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		documentoAssinado.setUnidadeEnsinoVO(unidadeEnsinoVO);
		documentoAssinado.setArquivo(arquivoVO);
		
		for (FuncionarioVO funcionarioVO : arquivoVO.getListaFuncionarioAssinarDigitalmenteVOs()) {
			documentoAssinado.getListaDocumentoAssinadoPessoa().add(new DocumentoAssinadoPessoaVO(new Date(), funcionarioVO.getPessoa(), TipoPessoa.FUNCIONARIO, SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, documentoAssinado, 1, "", "", null, funcionarioVO.getPessoa().getTipoAssinaturaDocumentoEnum()));
		}
		getFacadeFactory().getDocumentoAssinadoFacade().incluir(documentoAssinado, false, usuario, configuracaoGeralSistemaVO);
		
	}
	
	@Override
	public void assinarDocumentoGED(DocumentacaoGEDVO obj, UsuarioVO usuario) throws Exception {
		if(obj != null && obj.getMatricula() != null && !obj.getMatricula().getMatricula().trim().isEmpty()) {
			if(!Uteis.isAtributoPreenchido(obj.getMatricula().getUnidadeEnsino().getCodigo())) {
				obj.getMatricula().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorMatricula(obj.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			}
				ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(obj.getMatricula().getUnidadeEnsino().getCodigo(), usuario);
				if(Uteis.isAtributoPreenchido(configGEDVO) && configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getAssinarDocumento()) {
					ConfiguracaoGeralSistemaVO config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(obj.getMatricula().getUnidadeEnsino().getCodigo(), usuario);
					File fileAssinar = new File(config.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo() + File.separator +  obj.getArquivo().getNome());
					obj.getArquivo().setValidarDados(Boolean.FALSE);
					getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaDocumentacaoAluno(obj.getMatricula().getUnidadeEnsino(),obj.getArquivo(),configGEDVO, fileAssinar, obj.getIdDocumentacao(), config, usuario);		
				}
			}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistirTipoDocumentoGED(DocumentacaoGEDVO obj, List<TipoDocumentoGEDVO> listaAnteriorTipoDocumentoGED, UsuarioVO usuario) throws Exception {
		for (TipoDocumentoGEDVO tipoDocumentoGEDVO : listaAnteriorTipoDocumentoGED) {
			if (Uteis.isAtributoPreenchido(tipoDocumentoGEDVO) && !contains(obj.getListaTipoDocumentoGED(), tipoDocumentoGEDVO)) {
				getFacadeFactory().getTipoDocumentoGEDInterfaceFacade().excluir(obj, tipoDocumentoGEDVO, usuario);
				getFacadeFactory().getDocumetacaoMatriculaFacade().removerVinculoDocumentacaoGed(obj, tipoDocumentoGEDVO.getDocumetacaoMatricula(), usuario);
			}
		}

		boolean existeErro = false;
		StringBuilder mensagemErro = new StringBuilder();
		for (TipoDocumentoGEDVO tipoDocumentoGEDVO : obj.getListaTipoDocumentoGED()) {
			tipoDocumentoGEDVO.setDocumentacaoGED(obj);
			if (Uteis.isAtributoPreenchido(tipoDocumentoGEDVO.getDocumetacaoMatricula())) {
				tipoDocumentoGEDVO.getDocumetacaoMatricula().setArquivoGED(obj.getArquivo());
				tipoDocumentoGEDVO.getDocumetacaoMatricula().setEntregue(true);
				tipoDocumentoGEDVO.getDocumetacaoMatricula().setDataEntrega(new Date());
				tipoDocumentoGEDVO.getDocumetacaoMatricula().setSituacao("OK");
			}
			if (!Uteis.isAtributoPreenchido(tipoDocumentoGEDVO.getTipoDocumento())) {
				existeErro = true;
				mensagemErro.append(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_tipoDocumento").concat("<br />"));
			}
			getFacadeFactory().getTipoDocumentoGEDInterfaceFacade().persistir(tipoDocumentoGEDVO, Boolean.FALSE, usuario);
		}
		if (!Uteis.isAtributoPreenchido(obj.getPessoa())) {
			mensagemErro.append(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_pessoa").concat("<br />"));
		}
		if (!Uteis.isAtributoPreenchido(obj.getCategoriaGED())) {
			mensagemErro.append(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_categoriaGED").concat("<br />"));
		}
		if (existeErro) {
			obj.setSituacao("Erro");
			obj.setMensagem(mensagemErro.toString());
		} else {
			obj.setSituacao("Sucesso");
			obj.setMensagem("");
		}
	}

	public boolean contains(List<TipoDocumentoGEDVO> lista, TipoDocumentoGEDVO tipoDocumentoGED) {
		for (TipoDocumentoGEDVO tipoDocumentoGEDVO : lista) {
			if (tipoDocumentoGED.getCodigo().equals(tipoDocumentoGEDVO.getCodigo())) {
				return true;
			}
		}
		return false;
	}
	
	public static synchronized void processarArquivosDocumentacaoGED(Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (AplicacaoControle.registrarProcessamentoDigitalizacaoGED()) {
			try {
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoGED();
				if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo()) && usuarioVO == null) {
					usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
				}
				Date hoje = new Date();
				Date dataUltimoProcessamento = consultarDataUltimoProcessamento();
 				File[] filesOrigem = getFacadeFactory().getArquivoHelper().getTodosArquivosDiretorioPorExtensao(configuracaoGeralSistemaVO.getLocalUploadArquivoGED(), ".TXT", dataUltimoProcessamento != null ? Uteis.obterDataAntiga(dataUltimoProcessamento, 60) : null);
				String caminhoBaseFinal = PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue() + File.separator + UteisData.getAnoData(hoje) + File.separator + UteisData.getMesData(hoje);
				if (filesOrigem != null) {
					if (configuracaoGeralSistemaVO.getLocalUploadArquivoGED() == null || configuracaoGeralSistemaVO.getLocalUploadArquivoGED().isEmpty()) {
						throw new Exception("Local de Upload de Arquivos do GED não está configurado.");
					}
					for (File file : filesOrigem) {
						if (file.isFile() && !file.getName().contains("ERRO_PROCESSAMENTO")) {
							executarProcessamentoLoteDocumentacaoGED(file, caminhoBaseFinal, configuracaoGeralSistemaVO, hoje, validarAcesso, usuarioVO);
						}
					}
					filesOrigem = null;
				}
			} catch (Exception e) {
				AplicacaoControle.registrarTerminoProcessamentoDigitalizacaoGED();
				e.printStackTrace();
				throw e;
			} finally {
				AplicacaoControle.registrarTerminoProcessamentoDigitalizacaoGED();
				AplicacaoControle.setDataExecucaoJobGED(new Date());
			}
		}
	}
	
	
	private static synchronized void executarProcessamentoLoteDocumentacaoGED(File lote, String caminhoBaseFinal, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Date hoje, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		try {
			// Recupera as linhas do(s) arquivo(s) txt.
			List<String> linhasArquivo = getFacadeFactory().getArquivoHelper().lerArquivoTexto(lote);
			List<String> identificadorTipoDocumentoCabecalho = new ArrayList<String>(0);
			List<String> linhasComErro = new ArrayList<String>(0);
			int x = 1;
			int posicaoMatricula = 0;
			MatriculaVO matriculaVO = new MatriculaVO();
			forLinhasAquivos:
			for (String linhaArquivo : linhasArquivo) {
				if (!linhaArquivo.trim().isEmpty()) {
					
					if (x == 1) {
						identificadorTipoDocumentoCabecalho.addAll(realizarLeituraCabecalhoArquivoDocumentacaoGED(linhaArquivo));
						if (validarDadosEstruturaArquivoLayout1(identificadorTipoDocumentoCabecalho)) {
							posicaoMatricula = 3;
							x++;
							linhasComErro.add(linhaArquivo);
							continue forLinhasAquivos;
						}
						if(validarDadosEstruturaArquivoLayout2(identificadorTipoDocumentoCabecalho)) {
							posicaoMatricula = 4;
							x++;
							linhasComErro.add(linhaArquivo);
							continue forLinhasAquivos;
						}
						throw new Exception(montarMensagemErroEstruturaArquivo(identificadorTipoDocumentoCabecalho, lote.getName()));
					} else {
						try {
						String[] dadosSeparadosArquivo = linhaArquivo.split(";");
						matriculaVO = new MatriculaVO();
						if (dadosSeparadosArquivo.length > 3) {
							matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(dadosSeparadosArquivo[posicaoMatricula].replaceAll("\"", ""));
							if(matriculaVO == null) {
								matriculaVO = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorRegistroAcademico(dadosSeparadosArquivo[posicaoMatricula].replaceAll("\"", ""), 0, 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, usuarioVO);
							}
						}
						if (matriculaVO != null && usuarioVO == null) {
							MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoAtivaPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
							if (Uteis.isAtributoPreenchido(matriculaPeriodoVO.getResponsavelRenovacaoMatricula().getCodigo())) {
								usuarioVO = matriculaPeriodoVO.getResponsavelRenovacaoMatricula();
							}
						}
						DocumentacaoGEDVO documentacaoGEDVO = (getFacadeFactory().getDocumentacaoGEDInterfaceFacade().realizarOperacoesDocumentacaoGED(validarAcesso, configuracaoGeralSistemaVO, linhaArquivo, identificadorTipoDocumentoCabecalho, usuarioVO, caminhoBaseFinal, matriculaVO, hoje, lote));
						if (documentacaoGEDVO.getSituacao().equals("Erro")) {
							linhasComErro.add(linhaArquivo);
						}
						}catch (Exception e) {
							linhasComErro.add(linhaArquivo);
						}
					}
					x++;
					
				}else {
					break;
				}
			}
			//excluirArquivosDigitalizadoGED(documentacaoGEDVOs, configuracaoGeralSistemaVO);
			ArquivoHelper.copiarArquivoDaPastaConfiguracaoGedParaPastaDigitalizacaoGed(configuracaoGeralSistemaVO.getLocalUploadArquivoGED(), caminhoBaseFinal, lote.getName(), lote.getName(), configuracaoGeralSistemaVO);
			lote.delete();
			if(linhasComErro.size() > 1) {
				x = 0;
				FileWriter fr =  new FileWriter(configuracaoGeralSistemaVO.getLocalUploadArquivoGED()+File.separator+"ERRO_PROCESSAMENTO"+lote.getName());
				for(String erro : linhasComErro) {
					if(x>0) {
						fr.append("\n");
					}
					fr.append(erro);
					x++;
				}
				fr.flush();
				fr.close();
				
			}
			
			linhasArquivo = null;
			identificadorTipoDocumentoCabecalho = null;
		} catch (Exception e) {
//			for (DocumentacaoGEDVO documentacaoGEDVO : documentacaoGEDVOs) {
//				if (!documentacaoGEDVO.getArquivo().getNome().isEmpty()) {
//					File file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+documentacaoGEDVO.getArquivo().getPastaBaseArquivo()+File.separator+documentacaoGEDVO.getArquivo().getNome());
//					if(file.exists()) {
//						file.delete();
//					}	
//				}
//			}			
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.GED, e);			
			e.printStackTrace();
			throw e;
		}
	}
	
	private void excluirArquivosDigitalizadoGED(List<DocumentacaoGEDVO> documentacaoGEDVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		for (DocumentacaoGEDVO documentacaoGEDVO : documentacaoGEDVOs) {
			if (!documentacaoGEDVO.getArquivo().getCodigo().equals(0)) {
				File file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoGED() + File.separator + documentacaoGEDVO.getArquivo().getDescricao());
				if (file.isFile() && file.exists()) {
					file.delete();
					file = null;
				}
			}
		}
	}

	public static boolean validarDadosEstruturaArquivoLayout1(List<String> identificadorTipoDocumentoCabecalho) {
		if (!identificadorTipoDocumentoCabecalho.isEmpty()) {
			if (identificadorTipoDocumentoCabecalho.size() > 3) {
				if (identificadorTipoDocumentoCabecalho.get(0).equals("DocumentFileName") 
						&& identificadorTipoDocumentoCabecalho.get(1).equals("PageCount") 
						&& identificadorTipoDocumentoCabecalho.get(2).equals("TIPO DE DOCUMENTO") 
						&& (identificadorTipoDocumentoCabecalho.get(3).equals("MATRÍCULA") || identificadorTipoDocumentoCabecalho.get(3).equals("MATRICULA") )) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean validarDadosEstruturaArquivoLayout2(List<String> identificadorTipoDocumentoCabecalho) {
		if (!identificadorTipoDocumentoCabecalho.isEmpty()) {
			if (identificadorTipoDocumentoCabecalho.size() > 3) {
				if (identificadorTipoDocumentoCabecalho.get(0).equals("DocumentFileName") 
						&& identificadorTipoDocumentoCabecalho.get(1).equals("PageCount") 
						&& identificadorTipoDocumentoCabecalho.get(2).equals("TIPO DE DOCUMENTO") 
						&& identificadorTipoDocumentoCabecalho.get(3).equals("NOME DO ALUNO") 
						&& identificadorTipoDocumentoCabecalho.get(4).equals("MATRÍCULA")) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static String montarMensagemErroEstruturaArquivo(List<String> identificadorTipoDocumentoCabecalho, String nomeArquivo) {
		StringBuilder mensagemErro = new StringBuilder("");
		mensagemErro.append("Estrutura do arquivo(" + nomeArquivo + ") inválido. <br/>");
		mensagemErro.append("O arquivo deve conter a seguinte estrutura: <br/>");
		mensagemErro.append("Layout 1 - 'DocumentFileName', 'PageCount', 'TIPO DE DOCUMENTO', 'MATRÍCULA': <br/>");
		mensagemErro.append("Layout 2 - 'DocumentFileName', 'PageCount', 'TIPO DE DOCUMENTO', 'NOME DO ALUNO', 'MATRÍCULA': <br/>");

		if (!identificadorTipoDocumentoCabecalho.isEmpty()) {
			if (identificadorTipoDocumentoCabecalho.size() > 3) {
				mensagemErro.append("Estrutura do arquivo: ");
				mensagemErro.append("").append(identificadorTipoDocumentoCabecalho.get(0)).append(", ").append(identificadorTipoDocumentoCabecalho.get(1))
				.append(", ").append(identificadorTipoDocumentoCabecalho.get(2)).append(", ").append(identificadorTipoDocumentoCabecalho.get(3)).append(".");
			}
		}
		return mensagemErro.toString();
	}

	private static List<String> realizarLeituraCabecalhoArquivoDocumentacaoGED(String dadoArquivo) {
		String[] cabecalhoArquivoDocumentacaoGED = dadoArquivo.replaceAll("\"", "").split(";");
		List<String> listaDadosCabecalho = new ArrayList<>(0);

		for (String dados : cabecalhoArquivoDocumentacaoGED) {
			listaDadosCabecalho.add(dados);
		}

		return listaDadosCabecalho;
	}

	/**
	 * Realiza o processo de leitura dos arquivo de texto e processa as informações
	 * para incluir os documentos GED recuperados de acordo com os tipos informados
	 * nos arquivos.
	 * 
	 * @param validarAcesso
	 * @param configuracaoGeralSistemaVO
	 * @param linhaArquivo
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentacaoGEDVO realizarOperacoesDocumentacaoGED(Boolean validarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
			String linhaArquivo, List<String> identificadorTipoDocumentoCabecalho, UsuarioVO usuario, String caminhoBaseFinal,
			MatriculaVO matriculaVO, Date hoje, File lote) throws Exception {
		String[] dadosSeparadosArquivo = linhaArquivo.split(";");
		ArquivoVO arquivoVO = new ArquivoVO();
		DocumentacaoGEDVO obj =  null;
		try {		
		obj = montarDadosDocumentacaoGED(matriculaVO, dadosSeparadosArquivo);
		String nomeArquivo = dadosSeparadosArquivo[0].substring(dadosSeparadosArquivo[0].lastIndexOf('\\') + 1, dadosSeparadosArquivo[0].length()).replaceAll("\"", "");
		String caminhoArquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoGED().endsWith(File.separator) ? configuracaoGeralSistemaVO.getLocalUploadArquivoGED() + nomeArquivo : configuracaoGeralSistemaVO.getLocalUploadArquivoGED() + File.separator + nomeArquivo;
		if ("/".equals(File.separator) && caminhoArquivo.contains("\\")) {
			caminhoArquivo = caminhoArquivo.replace("\\", File.separator);
		}
		if ("\\".equals(File.separator) && caminhoArquivo.contains("/")) {
			caminhoArquivo = caminhoArquivo.replace("/", File.separator);
		}
		File file = new File(caminhoArquivo);
		boolean corrompido = false;
		if (file.exists() && !file.isDirectory()) {
			arquivoVO = montarDadosArquivo(configuracaoGeralSistemaVO, dadosSeparadosArquivo, caminhoBaseFinal, hoje, usuario);
			arquivoVO.setResponsavelUpload(usuario);
			obj.setArquivo(arquivoVO);
			if(obj.getSituacao().equals("Sucesso")) {
				if (file.canRead()) {
					assinarDocumentoGED(obj, usuario);
				} else {
					corrompido = true;
					obj.setMensagem(obj.getMensagem().concat("Arquivo " + file.getName() + " corrompido!<br />"));
					obj.setSituacao("Erro");
				}
			}
			if(!Uteis.isAtributoPreenchido(arquivoVO)) {
				getFacadeFactory().getArquivoFacade().incluir(arquivoVO, usuario, configuracaoGeralSistemaVO);
			}
		} else {
			obj.setMensagem(obj.getMensagem().concat("Arquivo " + caminhoArquivo + " não foi localizado.<br />"));
			obj.setSituacao("Erro");
		}
		obj.setUsuario(usuario);
		obj.setLote(lote.getName());
		incluir(obj, validarAcesso, usuario);
		if (!arquivoVO.getCodigo().equals(0)) {
			arquivoVO.setCodOrigem(obj.getCodigo());
			getFacadeFactory().getArquivoFacade().alterarCodigoOrigemArquivo(arquivoVO, usuario);
		}
		if (!corrompido) {
			incluirDadosTipoDocumentoGED(validarAcesso, configuracaoGeralSistemaVO, identificadorTipoDocumentoCabecalho, usuario, dadosSeparadosArquivo, matriculaVO, obj, arquivoVO);
			if(!obj.getSituacao().equals("Erro")) {				
				file.delete();
			}
		}		
		file = null;
		return obj;
		}catch(Exception e) {
			if (!arquivoVO.getNome().isEmpty()) {
				File file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+arquivoVO.getPastaBaseArquivo()+File.separator+arquivoVO.getNome());
				if(file.exists()) {
					file.delete();
				}	
			}			
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluirDadosTipoDocumentoGED(Boolean validarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, List<String> identificadorTipoDocumentoCabecalho, UsuarioVO usuario, String[] dadosSeparadosArquivo, MatriculaVO matriculaVO, DocumentacaoGEDVO obj, ArquivoVO arquivoVO) throws Exception {

		for (int contador = 4; contador < dadosSeparadosArquivo.length; contador++) {
			if (identificadorTipoDocumentoCabecalho.size() > 3) {

				if (dadosSeparadosArquivo[contador].replaceAll("\"", "").equalsIgnoreCase("SIM") || dadosSeparadosArquivo[contador].replaceAll("\"", "").equalsIgnoreCase("S")) {
					String identificadorTipoDocumento = "";
					if (identificadorTipoDocumentoCabecalho.size() >= contador) {
						identificadorTipoDocumento = identificadorTipoDocumentoCabecalho.get(contador);
					}
					TipoDocumentoVO tipoDocumento = null;
					List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs = new ArrayList<DocumetacaoMatriculaVO>(0);
					if (matriculaVO != null) {
						if (Uteis.isAtributoPreenchido(identificadorTipoDocumento) && Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
							tipoDocumento = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorCategoriaGedIdentificadorGED(obj.getCategoriaGED().getIdentificador(), identificadorTipoDocumento.replaceAll("\"", ""), matriculaVO.getMatricula());
						}
						if(Uteis.isAtributoPreenchido(tipoDocumento) && tipoDocumento.getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO) &&  Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
							documetacaoMatriculaVOs.addAll(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorTipoDeDocumentoAluno(tipoDocumento.getCodigo(), matriculaVO.getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, null));
						}else if (Uteis.isAtributoPreenchido(tipoDocumento) && Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
							DocumetacaoMatriculaVO doc = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorTipoDeDocumentoMatricula(tipoDocumento.getCodigo(), matriculaVO.getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, false, null);
							if(Uteis.isAtributoPreenchido(doc)) {
								documetacaoMatriculaVOs.add(doc);
							}
						}
					} else {
						tipoDocumento = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorCategoriaGedIdentificadorGED(obj.getCategoriaGED().getIdentificador(), identificadorTipoDocumento.replaceAll("\"", ""), null);
					}

					if (!documetacaoMatriculaVOs.isEmpty()) {
						for(DocumetacaoMatriculaVO documetacaoMatriculaVO: documetacaoMatriculaVOs) {
							boolean alterar = false;
							if(!Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOAssinado()) || !documetacaoMatriculaVO.getEntregue()) {								
								documetacaoMatriculaVO.setArquivoGED(arquivoVO);
								if(!Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOAssinado())) {
									documetacaoMatriculaVO.setArquivoVOAssinado(arquivoVO);	
								}
								alterar = true;
							}
							if(!documetacaoMatriculaVO.getEntregue()) {
								documetacaoMatriculaVO.setEntregue(Boolean.TRUE);
								documetacaoMatriculaVO.setDataEntrega(new Date());
								documetacaoMatriculaVO.setSituacao("OK");
								alterar = true;
							}
							if(alterar && !documetacaoMatriculaVO.getMatricula().equals(matriculaVO.getMatricula())) {													
								getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentacaoGed(documetacaoMatriculaVO, usuario);
							}
						}
					}
					DocumetacaoMatriculaVO documetacaoMatriculaVO =  null;
					if (!documetacaoMatriculaVOs.isEmpty() && documetacaoMatriculaVOs.stream().anyMatch(d -> d.getMatricula().equals(matriculaVO.getMatricula()))) {
						documetacaoMatriculaVO = documetacaoMatriculaVOs.stream().filter(d -> d.getMatricula().equals(matriculaVO.getMatricula())).findFirst().get(); 
					}
					TipoDocumentoGEDVO tipoDocumentoGED = montarDadosTipoDocumentoGED(obj, tipoDocumento, identificadorTipoDocumentoCabecalho.get(contador), documetacaoMatriculaVO);
					if (!obj.getMensagem().contains("já foi processado anteriormente para a matrícula")) {
						getFacadeFactory().getTipoDocumentoGEDInterfaceFacade().persistir(tipoDocumentoGED, validarAcesso, usuario);
					}

				}
			}
		}
	}

	private DocumentacaoGEDVO montarDadosDocumentacaoGED(MatriculaVO matricula, String[] dadosSeparadosArquivo) throws Exception {
		DocumentacaoGEDVO obj = new DocumentacaoGEDVO();
		obj.setSituacao("Sucesso");
		CategoriaGEDVO categoriaGED = getFacadeFactory().getCategoriaGEDInterfaceFacade().consultarPorIdentificador(dadosSeparadosArquivo[2].replaceAll(" ", "").trim().replaceAll("\"", ""));
		if (categoriaGED == null) {
			obj.setSituacao("Erro");
			obj.setMensagem(obj.getMensagem().concat("Categoria GED " + dadosSeparadosArquivo[2].trim().replaceAll("\"", "").replaceAll(" ", "").toUpperCase() + " não cadastrado.<br />"));
		}
		obj.setCategoriaGED(categoriaGED);

		if (matricula != null) {
			if (matricula.getAluno() == null || matricula.getAluno().getCodigo() == 0) {
				obj.setMensagem(obj.getMensagem().concat(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_pessoa").concat("<br />")));
			} else {
				obj.setMatricula(matricula);
				obj.setPessoa(matricula.getAluno());
			}

		} else {
			obj.setSituacao("Erro");
			obj.setMensagem(obj.getMensagem().concat(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_matricula")).concat("<br />"));
		}

		return obj;
	}

	public TipoDocumentoGEDVO montarDadosTipoDocumentoGED(DocumentacaoGEDVO obj, TipoDocumentoVO tipoDocumento, String identificadorGed, DocumetacaoMatriculaVO documetacaoMatricula) throws Exception {
		TipoDocumentoGEDVO tipoDocumentoGED = new TipoDocumentoGEDVO();
		if (tipoDocumento == null) {
			obj.setSituacao("Erro");
			obj.setMensagem(obj.getMensagem().concat(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_tipoDocumento").replace("{0}", identificadorGed)).concat("<br />"));
			getFacadeFactory().getDocumentacaoGEDInterfaceFacade().alterarSituacao(obj, false, null);

			tipoDocumentoGED.setSituacaoDocumentoLocalizadoEnum(SituacaoDocumentoLocalizadoEnum.NAO_LOCALIZADO);
		} else {
			tipoDocumentoGED.setSituacaoDocumentoLocalizadoEnum(SituacaoDocumentoLocalizadoEnum.LOCALIZADO);
		}

		tipoDocumentoGED.setIdentificadorGed(identificadorGed.replaceAll("\"", ""));
		tipoDocumentoGED.setTipoDocumento(tipoDocumento);
		tipoDocumentoGED.setDocumentacaoGED(obj);
		tipoDocumentoGED.setDocumetacaoMatricula(documetacaoMatricula);
		return tipoDocumentoGED;
	}

	private ArquivoVO montarDadosArquivo(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String[] dadosSeparadosArquivo, String caminhoBaseFinal, Date hoje, UsuarioVO usuario) throws Exception {
		ArquivoVO arquivoVO = new ArquivoVO();
		arquivoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue() + File.separator + UteisData.getAnoData(hoje) + File.separator + UteisData.getMesData(hoje));
		String nomeArquivo = dadosSeparadosArquivo[0].substring(dadosSeparadosArquivo[0].lastIndexOf('\\') + 1, dadosSeparadosArquivo[0].length()).replaceAll("\"", "");

		@SuppressWarnings("static-access")
		String nomeArquivoSemAcento = getFacadeFactory().getArquivoHelper().criarNomeArquivo(nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")), PastaBaseArquivoEnum.DIGITALIZACAO_GED, usuario);
		String nomeArquivoSemAcentoComExtensao = nomeArquivoSemAcento + nomeArquivo.substring(nomeArquivo.length() - 4);

		ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixaComOutroNome(configuracaoGeralSistemaVO.getLocalUploadArquivoGED(), caminhoBaseFinal, nomeArquivo, nomeArquivoSemAcentoComExtensao, configuracaoGeralSistemaVO, true);
		arquivoVO.setNome(nomeArquivoSemAcentoComExtensao);
		arquivoVO.setDescricao(nomeArquivo);
		arquivoVO.setDescricaoAntesAlteracao(nomeArquivo);
		arquivoVO.setDataUpload(new Date());
		arquivoVO.setDataDisponibilizacao(new Date());
		if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas())) {
			arquivoVO.setResponsavelUpload(getFacadeFactory().getUsuarioFacade().consultarPorPessoa(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DIGITALIZACAO_GED);
		arquivoVO.setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(configuracaoGeralSistemaVO.getServidorArquivoOnline()));
		arquivoVO.setOrigem(OrigemArquivo.DOCUMENTO_GED.getValor());
		return arquivoVO;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(DocumentacaoGEDVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception {
		try {
			DocumentacaoGED.incluir(getIdEntidade(), validarAcesso, usuario);
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO DocumentacaoGED( categoriaGED, arquivo, matricula, pessoa, situacao, mensagem, usuario, data, lote) ");
			sql.append("VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo");

			if (usuario != null) {
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			}

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					if (!obj.getCategoriaGED().getCodigo().equals(0)) {
						sqlInserir.setInt(1, obj.getCategoriaGED().getCodigo());
					} else {
						sqlInserir.setNull(1, 0);
					}

					if (!obj.getArquivo().getCodigo().equals(0)) {
						sqlInserir.setInt(2, obj.getArquivo().getCodigo());
					} else {
						sqlInserir.setNull(2, 0);
					}

					if (obj.getMatricula().getMatricula() != null && !obj.getMatricula().getMatricula().trim().equals("")) {
						sqlInserir.setString(3, obj.getMatricula().getMatricula());
					} else {
						sqlInserir.setNull(3, 0);
					}

					if (!obj.getPessoa().getCodigo().equals(0)) {
						sqlInserir.setInt(4, obj.getPessoa().getCodigo());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setString(5, obj.getSituacao());
					sqlInserir.setString(6, obj.getMensagem());

					if (!obj.getUsuario().getCodigo().equals(0)) {
						sqlInserir.setInt(7, obj.getUsuario().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setTimestamp(8, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlInserir.setString(9, obj.getLote());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterar(DocumentacaoGEDVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception {
		try {
			DocumentacaoGED.incluir(getIdEntidade(), validarAcesso, usuario);
			validarDados(obj);
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE DocumentacaoGED set categoriaged=?, arquivo=?, matricula=?, pessoa=?, situacao = ?, mensagem = ?, usuario = ? WHERE codigo = ? ");

			if (usuario != null) {
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			}
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					if (!obj.getCategoriaGED().getCodigo().equals(0)) {
						sqlAlterar.setInt(1, obj.getCategoriaGED().getCodigo());
					} else {
						sqlAlterar.setNull(1, 0);
					}

					if (!obj.getArquivo().getCodigo().equals(0)) {
						sqlAlterar.setInt(2, obj.getArquivo().getCodigo());
					} else {
						sqlAlterar.setNull(2, 0);
					}

					if (obj.getMatricula().getMatricula() != null && !obj.getMatricula().getMatricula().equals("")) {
						sqlAlterar.setString(3, obj.getMatricula().getMatricula());
					} else {
						sqlAlterar.setNull(3, 0);
					}

					if (!obj.getPessoa().getCodigo().equals(0)) {
						sqlAlterar.setInt(4, obj.getPessoa().getCodigo());
					} else {
						sqlAlterar.setNull(4, 0);
					}

					sqlAlterar.setString(5, obj.getSituacao());
					sqlAlterar.setString(6, obj.getMensagem());

					if (!obj.getUsuario().getCodigo().equals(0)) {
						sqlAlterar.setInt(7, obj.getUsuario().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setInt(8, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarSituacao(DocumentacaoGEDVO obj, Boolean validarAcesso, UsuarioVO usuario) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE DocumentacaoGED set situacao = ?, mensagem = ? WHERE codigo = ? ");

			if (usuario != null) {
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			}
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(1, obj.getSituacao());
					sqlAlterar.setString(2, obj.getMensagem());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<DocumentacaoGEDVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, String valorConsultaSituacao, DataModelo dataModelo) throws Exception {
		List<DocumentacaoGEDVO> listaDocumentacaoGED = new ArrayList<>(0);
		List<Object> parametros = new ArrayList<>();
		StringBuilder sql = new StringBuilder(getSqlBasico());
		sql.append(" WHERE 1 = 1 ");
		montarFiltroConsulta(campoConsulta, valorConsulta, valorConsultaSituacao, parametros, sql);
		sql.append(" ORDER BY codigo DESC ");
		sql.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray());
		while (tabelaResultado.next()) {
			listaDocumentacaoGED.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return listaDocumentacaoGED;
	}

	private void montarFiltroConsulta(String campoConsulta, String valorConsulta, String valorConsultaSituacao, List<Object> parametros, StringBuilder sql) {
		if (!valorConsulta.isEmpty()) {
			switch (campoConsulta) {
			case "nome":
				sql.append(" AND sem_acentos(pessoa.nome) ilike sem_acentos(?) ");
				parametros.add(valorConsulta + PERCENT);
				break;
			case "registroAcademico":
				sql.append(" AND Pessoa.registroAcademico ilike ? ");
				parametros.add(valorConsulta + PERCENT);
				break;
			case "matricula":
				sql.append(" AND matricula ilike ? ");
				parametros.add(valorConsulta + PERCENT);
				break;
			case "categoriaged":
				sql.append(" AND CategoriaGED.descricao ilike ? ");
				parametros.add(valorConsulta + PERCENT);
				break;
			default:
				break;
			}
		}
		if (!valorConsultaSituacao.equals("todos")) {
			sql.append(" AND DocumentacaoGED.situacao ilike (sem_acentos(?)) ");
			parametros.add(valorConsultaSituacao);
		}
	}

	@Override
	public List<DocumentacaoGEDVO> consultarPorMatricula(String matricula, int todos, UsuarioVO usuarioLogado) throws Exception {
		List<DocumentacaoGEDVO> listaDocumentacaoGED = new ArrayList<>(0);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE matricula = '").append(matricula).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			listaDocumentacaoGED.add(montarDados(tabelaResultado, todos));
		}
		return listaDocumentacaoGED;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DocumentacaoGED.*, CategoriaGED.descricao, Arquivo.nome, Arquivo.descricao as arquivo_descricao, Arquivo.pastaBaseArquivo, Arquivo.servidorArquivoOnline, Pessoa.nome as nomePessoa, Pessoa.registroAcademico as registroAcademico , Usuario.codigo as codigoUsuario, Usuario.nome as nomeUsuario FROM DocumentacaoGED");
		sql.append(" LEFT JOIN CategoriaGED ON CategoriaGED.codigo = DocumentacaoGED.CategoriaGED");
		sql.append(" LEFT JOIN Arquivo ON Arquivo.codigo = DocumentacaoGED.Arquivo");
		sql.append(" LEFT JOIN Pessoa ON Pessoa.codigo = DocumentacaoGED.pessoa");
		sql.append(" LEFT JOIN Usuario ON Usuario.codigo = DocumentacaoGED.usuario");

		return sql.toString();
	}

	@SuppressWarnings("unchecked")
	private DocumentacaoGEDVO montarDados(SqlRowSet tabelaResultado, int nivelmontardadosDadosbasicos) throws Exception {
		DocumentacaoGEDVO obj = new DocumentacaoGEDVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setNovoObj(false);
		obj.setSituacao(tabelaResultado.getString("situacao"));
		obj.setData(tabelaResultado.getDate("data"));
		obj.setMensagem(tabelaResultado.getString("mensagem"));
		obj.getCategoriaGED().setCodigo(tabelaResultado.getInt("categoriaged"));
		obj.getCategoriaGED().setDescricao(tabelaResultado.getString("descricao"));
		obj.getArquivo().setCodigo(tabelaResultado.getInt("arquivo"));
		obj.getArquivo().setNome(tabelaResultado.getString("nome"));
		obj.getArquivo().setPastaBaseArquivo(tabelaResultado.getString("pastaBaseArquivo"));
		obj.getArquivo().setDescricao(tabelaResultado.getString("arquivo_descricao"));
		obj.getArquivo().setDescricaoAntesAlteracao(tabelaResultado.getString("arquivo_descricao"));
		obj.getArquivo().setServidorArquivoOnline(tabelaResultado.getString("servidorArquivoOnline") == null ? null : ServidorArquivoOnlineEnum.valueOf(tabelaResultado.getString("servidorArquivoOnline")));
		obj.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
		obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa"));
		obj.getPessoa().setNome(tabelaResultado.getString("nomePessoa"));
		obj.getPessoa().setRegistroAcademico(tabelaResultado.getString("registroAcademico"));
		obj.getUsuario().setCodigo(tabelaResultado.getInt("codigoUsuario"));
		obj.getUsuario().setNome(tabelaResultado.getString("nomeUsuario"));
		obj.setPessoaNaoEncontrada(!Uteis.isAtributoPreenchido(obj.getPessoa()));
		obj.setCategoriaGedNaoEncontrada(!Uteis.isAtributoPreenchido(obj.getCategoriaGED()));
		obj.setLote(tabelaResultado.getString("lote"));
		if (Uteis.isAtributoPreenchido(obj.getArquivo().getCodigo())) {
			obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DIGITALIZACAO_GED);
		}
		if (nivelmontardadosDadosbasicos == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setListaTipoDocumentoGED(getFacadeFactory().getTipoDocumentoGEDInterfaceFacade().consultarPorDocumentacaoGED(false, null, obj.getCodigo()));
		}
		return obj;
	}

	public void validarDados(DocumentacaoGEDVO obj) throws ConsistirException {
		if (obj.getMatricula() == null || obj.getMatricula().getMatricula().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_DocumentacaoGED_matricula"));
		}

		if (obj.getCategoriaGED().getCodigo() == null || obj.getCategoriaGED().getCodigo() == 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_DocumentacaoGED_categoriaGED"));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(DocumentacaoGEDVO obj, Boolean validarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		DocumentacaoGED.excluir(getIdEntidade(), validarAcesso, usuario);
		verificarVinculoDocumentoAssinado(obj, usuario);
		getFacadeFactory().getTipoDocumentoGEDInterfaceFacade().excluirPorDocumentacaoGED(obj, usuario);
		try {
			String sql = "DELETE FROM documentacaoged WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			if (Uteis.isAtributoPreenchido(obj.getArquivo())) {
				getFacadeFactory().getDocumetacaoMatriculaFacade().removerVinculoDocumentacaoGed(obj, usuario);
				getFacadeFactory().getArquivoFacade().excluir(obj.getArquivo(), usuario, configuracaoGeralSistemaVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void verificarVinculoDocumentoAssinado(DocumentacaoGEDVO obj, UsuarioVO usuario) throws Exception {
		DocumentoAssinadoVO documentoAssinadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPorArquivo(obj.getArquivo().getCodigo(), usuario);
		if(Uteis.isAtributoPreenchido(documentoAssinadoVO.getCodigo())){
			removerVinculoDocumentoAssinado(documentoAssinadoVO, usuario);
		}
		
	}

	private void removerVinculoDocumentoAssinado(DocumentoAssinadoVO documentoAssinadoVO, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getDocumentoAssinadoPessoaFacade().excluirPorCodigoDocumentoAssinado(documentoAssinadoVO.getCodigo(), false, usuario);
		getFacadeFactory().getDocumentoAssinadoFacade().excluirDocumentoAssinadoVinculadoDocumentacaoGED(documentoAssinadoVO.getCodigo(), usuario);
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		DocumentacaoGED.idEntidade = idEntidade;
	}

	@Override
	public int consultarTotal(String campoConsulta, String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, String valorConsultaSituacao) throws Exception {
		StringBuilder sql = new StringBuilder();
		List<Object> parametros = new ArrayList<>();
		sql.append(" SELECT COUNT(DocumentacaoGED.codigo) as qtde FROM documentacaoged");
		sql.append(" LEFT JOIN CategoriaGED ON CategoriaGED.codigo = DocumentacaoGED.CategoriaGED");
		sql.append(" LEFT JOIN Arquivo ON Arquivo.codigo = DocumentacaoGED.Arquivo");
		sql.append(" LEFT JOIN Pessoa ON Pessoa.codigo = DocumentacaoGED.pessoa");
		sql.append(" LEFT JOIN Usuario ON Usuario.codigo = DocumentacaoGED.usuario");
		sql.append(" WHERE 1 = 1 ");
		montarFiltroConsulta(campoConsulta, valorConsulta, valorConsultaSituacao, parametros, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	@Override
	public void adicionarTipoDocumento(DocumentacaoGEDVO documentacaoGEDVO, TipoDocumentoVO tipoDocumentoVO, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(documentacaoGEDVO.getCategoriaGED().getCodigo())) {
			throw new Exception("Selecione uma categoria GED para adicionar.");
		}

		if (!Uteis.isAtributoPreenchido(tipoDocumentoVO)) {
			throw new Exception("Selecione um tipo de documento para adicionar.");
		}
		for (TipoDocumentoGEDVO tipoDocumentoGEDVO : documentacaoGEDVO.getListaTipoDocumentoGED()) {
			if (tipoDocumentoGEDVO.getTipoDocumento().getCodigo().equals(tipoDocumentoVO.getCodigo())) {
				throw new Exception("TIPO DE DOCUMENTO já foi adicionado.");
			}
		}
		DocumetacaoMatriculaVO documetacaoMatriculaVO = null;
		if (Uteis.isAtributoPreenchido(tipoDocumentoVO) && Uteis.isAtributoPreenchido(documentacaoGEDVO.getMatricula().getMatricula())) {
			documetacaoMatriculaVO = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorTipoDeDocumentoMatricula(tipoDocumentoVO.getCodigo(), documentacaoGEDVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, false, null);
		}
		TipoDocumentoGEDVO tipoDocumentoGEDVO = montarDadosTipoDocumentoGED(documentacaoGEDVO, tipoDocumentoVO, tipoDocumentoVO.getIdentificadorGED(), documetacaoMatriculaVO);
		documentacaoGEDVO.getListaTipoDocumentoGED().add(tipoDocumentoGEDVO);
	}

	@Override
	public void realizarLocalizacaoDocumentacaoMatriculaVincularDocumentoGED(DocumentacaoGEDVO documentacaoGEDVO) throws Exception {
		if (Uteis.isAtributoPreenchido(documentacaoGEDVO.getMatricula().getMatricula())) {
			if (!Uteis.isAtributoPreenchido(documentacaoGEDVO.getCategoriaGED().getIdentificador()) && Uteis.isAtributoPreenchido(documentacaoGEDVO.getCategoriaGED().getCodigo())) {
				documentacaoGEDVO.setCategoriaGED(getFacadeFactory().getCategoriaGEDInterfaceFacade().consultarPorChavePrimaria(documentacaoGEDVO.getCategoriaGED().getCodigo()));
			}
			for (TipoDocumentoGEDVO item : documentacaoGEDVO.getListaTipoDocumentoGED()) {
				if (!Uteis.isAtributoPreenchido(item.getTipoDocumento()) && Uteis.isAtributoPreenchido(item.getIdentificadorGed()) && Uteis.isAtributoPreenchido(documentacaoGEDVO.getMatricula().getMatricula()) && Uteis.isAtributoPreenchido(documentacaoGEDVO.getCategoriaGED().getIdentificador())) {
					item.setTipoDocumento(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorCategoriaGedIdentificadorGED(documentacaoGEDVO.getCategoriaGED().getIdentificador(), item.getIdentificadorGed().replaceAll("\"", ""), documentacaoGEDVO.getMatricula().getMatricula()));
				}

				if (Uteis.isAtributoPreenchido(item.getTipoDocumento()) && !Uteis.isAtributoPreenchido(item.getDocumetacaoMatricula())) {
					try {
						item.setDocumetacaoMatricula(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorTipoDeDocumentoMatricula(item.getTipoDocumento().getCodigo(), documentacaoGEDVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, false, null));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} else {
			documentacaoGEDVO.getListaTipoDocumentoGED().forEach(item -> {
				item.setDocumetacaoMatricula(null);
			});
		}
		realizarAtualizacaoSituacaoDocumentacaoGed(documentacaoGEDVO);
	}

	public void realizarAtualizacaoSituacaoDocumentacaoGed(DocumentacaoGEDVO documentacaoGEDVO) throws Exception {
		boolean existeErro = false;
		StringBuilder mensagemErro = new StringBuilder();
		for (TipoDocumentoGEDVO tipoDocumentoGEDVO : documentacaoGEDVO.getListaTipoDocumentoGED()) {
			if (!Uteis.isAtributoPreenchido(tipoDocumentoGEDVO.getTipoDocumento())) {
				existeErro = true;
				mensagemErro.append(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_tipoDocumento").concat("<br />"));
			}
		}
		if (!Uteis.isAtributoPreenchido(documentacaoGEDVO.getArquivo())) {
			existeErro = true;
			mensagemErro.append(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_arquivo"));
		}
		if (!Uteis.isAtributoPreenchido(documentacaoGEDVO.getPessoa())) {
			existeErro = true;
			mensagemErro.append(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_pessoa").concat("<br />"));
		}
		if (!Uteis.isAtributoPreenchido(documentacaoGEDVO.getCategoriaGED())) {
			existeErro = true;
			mensagemErro.append(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_categoriaGED").concat("<br />"));
		}
		if (existeErro) {
			documentacaoGEDVO.setSituacao("Erro");
			documentacaoGEDVO.setMensagem(mensagemErro.toString());
		} else {
			documentacaoGEDVO.setSituacao("Sucesso");
			documentacaoGEDVO.setMensagem("");
		}
	}

	@Override
	public DocumentacaoGEDVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlBasico());
		sql.append(" where  DocumentacaoGED.codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados);
		}
		throw new Exception("Dados não encontrados (Documentação GED)");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDocumentacaoGEDPorMatricula(MatriculaVO matriculaVO, boolean validarAcesso, UsuarioVO usuarioVO, 
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (matriculaVO != null && !matriculaVO.getMatricula().isEmpty()) {
			List<DocumentacaoGEDVO> documentacaoGEDVOs = consultarPorMatricula(matriculaVO.getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			for (DocumentacaoGEDVO documentacaoGEDVO : documentacaoGEDVOs) {
				excluir(documentacaoGEDVO, validarAcesso, usuarioVO, configuracaoGeralSistemaVO);
			}
		}
	}
	
	@Override
	public List<DocumentacaoGEDVO> consultarDocumentoGEDEntregue(AssinarDocumentoEntregueVO assinarDocumentoEntregueVO,  Integer nivelMontarDados, UsuarioVO usuario) throws Exception{		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select DocumentacaoGED.*, CategoriaGED.descricao, Arquivo.nome, Arquivo.descricao as arquivo_descricao,  ");
		sqlStr.append(" Arquivo.pastaBaseArquivo, Arquivo.servidorArquivoOnline, Pessoa.nome as nomePessoa, Usuario.codigo as codigoUsuario, Usuario.nome as nomeUsuario FROM DocumentacaoGED  ");
		sqlStr.append(" inner JOIN CategoriaGED ON CategoriaGED.codigo = DocumentacaoGED.CategoriaGED ");
		sqlStr.append(" inner join matricula on matricula.matricula = documentacaoged.matricula  ");
		sqlStr.append(" inner join arquivo on arquivo.codigo = documentacaoged.arquivo  ");
		sqlStr.append(" inner JOIN Pessoa ON Pessoa.codigo = DocumentacaoGED.pessoa ");
		sqlStr.append(" inner JOIN Usuario ON Usuario.codigo = DocumentacaoGED.usuario ");
		sqlStr.append(" where documentacaoged.situacao = 'Sucesso' and arquivo.arquivoAssinadoFuncionario = false and arquivo.arquivoAssinadoUnidadeEnsino = false and arquivo.arquivoAssinadoUnidadeCertificadora = false ");
		
		sqlStr.append(" AND documentacaoged.data >=  '").append(Uteis.getDataBD0000(assinarDocumentoEntregueVO.getDataInicioEntrega())).append("' ");
		sqlStr.append(" AND documentacaoged.data <= '").append(Uteis.getDataBD2359(assinarDocumentoEntregueVO.getDataFimEntrega())).append("' ");
		
		if(assinarDocumentoEntregueVO.getConsiderarDocumentosProcessadorComErro()) {
			sqlStr.append(" and (documentacaoged.processadocomerro = true or documentacaoged.processadocomerro is null)");
		}else {
			sqlStr.append(" AND documentacaoged.processadocomerro is null  ");
		}
		
		if(Uteis.isAtributoPreenchido(assinarDocumentoEntregueVO.getCodigoUnidadeEnsino())) {
			sqlStr.append(" AND matricula.unidadeensino =  ").append(assinarDocumentoEntregueVO.getCodigoUnidadeEnsino()).append(" ");
		}
		sqlStr.append(" AND arquivo.origem = 'DC' ");
		sqlStr.append(" AND arquivo.servidorarquivoonline = '").append(assinarDocumentoEntregueVO.getServidorArquivo()).append("' ");
		if(assinarDocumentoEntregueVO.getQtdProcessarLote() != null && assinarDocumentoEntregueVO.getQtdProcessarLote() > 0) {
			sqlStr.append(" limit   ").append(assinarDocumentoEntregueVO.getQtdProcessarLote());	
		} else {
			sqlStr.append(" limit   100 ");
		}
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<DocumentacaoGEDVO> listaArquivoVOs = new ArrayList<DocumentacaoGEDVO>(0);
		while (dadosSQL.next()) {		
			listaArquivoVOs.add(montarDados(dadosSQL));
		}
		return listaArquivoVOs;
	}
	
	private DocumentacaoGEDVO montarDados(SqlRowSet tabelaResultado) throws Exception {
		DocumentacaoGEDVO obj = new DocumentacaoGEDVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setNovoObj(false);
		obj.setSituacao(tabelaResultado.getString("situacao"));
		obj.setData(tabelaResultado.getDate("data"));
		obj.setMensagem(tabelaResultado.getString("mensagem"));
		obj.getCategoriaGED().setCodigo(tabelaResultado.getInt("categoriaged"));
		obj.getCategoriaGED().setDescricao(tabelaResultado.getString("descricao"));
		obj.getArquivo().setCodigo(tabelaResultado.getInt("arquivo"));
		obj.getArquivo().setNome(tabelaResultado.getString("nome"));
		obj.getArquivo().setPastaBaseArquivo(tabelaResultado.getString("pastaBaseArquivo"));
		obj.getArquivo().setDescricao(tabelaResultado.getString("arquivo_descricao"));
		obj.getArquivo().setDescricaoAntesAlteracao(tabelaResultado.getString("arquivo_descricao"));
		obj.getArquivo().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(tabelaResultado.getString("servidorArquivoOnline")));
		obj.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
		obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa"));
		obj.getPessoa().setNome(tabelaResultado.getString("nomePessoa"));
		obj.getUsuario().setCodigo(tabelaResultado.getInt("codigoUsuario"));
		obj.getUsuario().setNome(tabelaResultado.getString("nomeUsuario"));
		obj.setPessoaNaoEncontrada(!Uteis.isAtributoPreenchido(obj.getPessoa()));
		obj.setCategoriaGedNaoEncontrada(!Uteis.isAtributoPreenchido(obj.getCategoriaGED()));
		obj.setLote(tabelaResultado.getString("lote"));
		if (Uteis.isAtributoPreenchido(obj.getArquivo().getCodigo())) {
			obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DIGITALIZACAO_GED);
		}
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAssinaturaDocumentoGEDJOB(DocumentacaoGEDVO documentacaoGEDVO,UnidadeEnsinoVO unidadeEnsino,ConfiguracaoGEDVO configuracaoGEDVO, File fileAssinar, ConfiguracaoGeralSistemaVO configuracaoGeralSistema,UsuarioVO usuario) throws Exception {
		documentacaoGEDVO.getArquivo().setValidarDados(Boolean.FALSE);
		getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaDocumentacaoAluno(unidadeEnsino,documentacaoGEDVO.getArquivo(),configuracaoGEDVO, fileAssinar, documentacaoGEDVO.getIdDocumentacao(), configuracaoGeralSistema, usuario);
		atualizarStatusDocumentacaoGED(documentacaoGEDVO, "Processado com sucesso", Boolean.FALSE, usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarStatusDocumentacaoGED(DocumentacaoGEDVO documentacaoGEDVO, String descricao, Boolean statusProcessamento,  final UsuarioVO usuarioVO)  {
		alterar(documentacaoGEDVO, "documentacaoged",
				new AtributoPersistencia().add("descricaoprocessamento", descricao)
				.add("processadocomerro", statusProcessamento)
				.add("dataprocessamento", new Date()),
				new AtributoPersistencia().add("codigo", documentacaoGEDVO.getCodigo()), usuarioVO);
	}
	
	@Override
	public List<DocumentacaoGEDVO> consultarDocumentoGEDPorStatusProcessamento(Boolean status, int limite, int pagina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("  select tipodocumentoged.identificadorged, documentacaoged.codigo, documentacaoged.matricula, documentacaoged.descricaoprocessamento, documentacaoged.dataprocessamento from documentacaoged inner join tipodocumentoged on tipodocumentoged.documentacaoged = documentacaoged.codigo where documentacaoged.processadocomerro = ? order by  documentacaoged.dataprocessamento desc ");
		if (limite != 0) {
			sqlStr.append(" limit ");
			sqlStr.append(limite);
			sqlStr.append(" offset ");
			sqlStr.append(pagina);
		}
		
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(),  new Object[] { status });
		List<DocumentacaoGEDVO> listaArquivoVOs = new ArrayList<DocumentacaoGEDVO>(0);
		while (dadosSQL.next()) {		
			listaArquivoVOs.add(montarDadosPorStatus(dadosSQL));
		}
		return listaArquivoVOs;
	}
	
	private DocumentacaoGEDVO montarDadosPorStatus(SqlRowSet tabelaResultado) throws Exception {
		DocumentacaoGEDVO obj = new DocumentacaoGEDVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
		obj.setDescricaoprocessamento(tabelaResultado.getString("descricaoprocessamento"));
		obj.setIdentificadorged(tabelaResultado.getString("identificadorged"));
		obj.setDataProcessamento(tabelaResultado.getTimestamp("dataprocessamento"));
		obj.setNovoObj(false);
		return obj;
	}
	
	public Integer consultarTotalDocumentosProcessados(Boolean status, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select count (distinct documentacaoged.codigo) as total from documentacaoged where documentacaoged.processadocomerro =  ?");
		Integer totalRegistro = 0;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { status });
		if (rs.next()) {
			totalRegistro = rs.getInt("total");
		}
		return totalRegistro;
	}
	
	
	private static Date consultarDataUltimoProcessamento() {
		StringBuilder sqlStr = new StringBuilder("select documentacaoged.data from documentacaoged order by codigo desc limit 1 ");
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()) {
			return rs.getDate("data");
		}
		return null;
	}
	
	@Override
	public List<DocumetacaoMatriculaVO> consultarPorMatriculaParaDocumentacaoXmlDiploma(String matricula, int todos, UsuarioVO usuarioLogado) throws Exception {
		List<DocumetacaoMatriculaVO> listaDocumentacaoGED = new ArrayList<>(0);
		StringBuilder sql = new StringBuilder(" SELECT DocumentacaoGED.*, tipoDocumento.codigo as tipoDocumento, CategoriaGED.descricao, Arquivo.nome, Arquivo.descricao as arquivo_descricao, Arquivo.pastaBaseArquivo, Arquivo.servidorArquivoOnline, Pessoa.nome as nomePessoa, Usuario.codigo as codigoUsuario, Usuario.nome as nomeUsuario ");
		sql.append(" FROM DocumentacaoGED");
		sql.append(" inner JOIN TipoDocumentoGED ON TipoDocumentoGED.DocumentacaoGED = DocumentacaoGED.codigo");
		sql.append(" inner JOIN TipoDocumento ON TipoDocumento.codigo = TipoDocumentoGED.TipoDocumento and TipoDocumento.enviarDocumentoXml ");
		sql.append(" LEFT JOIN CategoriaGED ON CategoriaGED.codigo = DocumentacaoGED.CategoriaGED");
		sql.append(" LEFT JOIN Arquivo ON Arquivo.codigo = DocumentacaoGED.Arquivo");
		sql.append(" LEFT JOIN Pessoa ON Pessoa.codigo = DocumentacaoGED.pessoa");
		sql.append(" LEFT JOIN Usuario ON Usuario.codigo = DocumentacaoGED.usuario");
		sql.append(" WHERE matricula = '").append(matricula).append("' ");
		sql.append(" and not exists (select documetacaoMatricula.codigo from documetacaoMatricula where documetacaoMatricula.matricula = '").append(matricula).append("' ");
		sql.append(" and documetacaoMatricula.tipodedocumento = TipoDocumento.codigo ");
		sql.append(" and (documetacaoMatricula.arquivo is not null or documetacaoMatricula.arquivoAssinado is not null or documetacaoMatricula.arquivoGed is not null)) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			DocumetacaoMatriculaVO obj = new DocumetacaoMatriculaVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.setNovoObj(false);
			obj.setSituacao("OK");
			obj.setDataEntrega(tabelaResultado.getDate("data"));
			obj.setDataAprovacaoDocDep(tabelaResultado.getDate("data"));
			obj.setArquivoAprovadoPeloDep(true);
			obj.getArquivoGED().setCodigo(tabelaResultado.getInt("arquivo"));
			obj.getArquivoGED().setNome(tabelaResultado.getString("nome"));
			obj.getArquivoGED().setPastaBaseArquivo(tabelaResultado.getString("pastaBaseArquivo"));
			obj.getArquivoGED().setDescricao(tabelaResultado.getString("arquivo_descricao"));
			obj.getArquivoGED().setDescricaoAntesAlteracao(tabelaResultado.getString("arquivo_descricao"));
			obj.getArquivoGED().setServidorArquivoOnline(tabelaResultado.getString("servidorArquivoOnline") == null ? null : ServidorArquivoOnlineEnum.valueOf(tabelaResultado.getString("servidorArquivoOnline")));
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaVO().getAluno().setCodigo(tabelaResultado.getInt("pessoa"));
			obj.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("nomePessoa"));
			obj.getUsuario().setCodigo(tabelaResultado.getInt("codigoUsuario"));
			obj.getUsuario().setNome(tabelaResultado.getString("nomeUsuario"));
			obj.setEntregue(true);			
			if (Uteis.isAtributoPreenchido(obj.getArquivoGED().getCodigo())) {
				obj.getArquivoGED().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DIGITALIZACAO_GED);
			
			}
			obj.setTipoDeDocumentoVO(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("tipoDocumento"), todos, usuarioLogado));
			listaDocumentacaoGED.add(obj);
		}
		return listaDocumentacaoGED;
	}
}