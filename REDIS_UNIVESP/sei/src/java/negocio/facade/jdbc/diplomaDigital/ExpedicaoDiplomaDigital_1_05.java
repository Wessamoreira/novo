package negocio.facade.jdbc.diplomaDigital;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.descriptor.XmlErrorHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.xml.sax.SAXParseException;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ConfiguracaoDiplomaDigitalVO;
import negocio.comuns.academico.ConfiguracaoLayoutHistoricoVO;
import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DeclaracaoAcercaProcessoJudicialVO;
import negocio.comuns.academico.DisciplinaPreRequisitoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.GestaoXmlGradeCurricularVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.GradeCurricularTipoAtividadeComplementarVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.MatriculaEnadeVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.academico.enumeradores.CampoPeriodoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.CoordenadorCursoDisciplinaAproveitadaEnum;
import negocio.comuns.academico.enumeradores.FormatoCargaHorariaXmlEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.academico.enumeradores.TipoTextoEnadeEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.diplomaDigital.versao1_05.*;
import negocio.comuns.diplomaDigital.versao1_05.TEquivalenciaUnidadesCurriculares.UnidadesCurricularesEquivalente;
import negocio.comuns.diplomaDigital.versao1_05.THistoricoEscolar.IngressoCurso;
import negocio.comuns.estagio.enumeradores.SituacaoEstagioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoDisciplina;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ExpedicaoDiplomaDigitalInterfaceFacade;
import relatorio.negocio.comuns.academico.HistoricoAlunoDisciplinaRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.aws.s3.ServidorArquivoOnlineS3RS;

@Repository
@Scope("singleton")
@Lazy
public class ExpedicaoDiplomaDigital_1_05 extends ControleAcesso implements ExpedicaoDiplomaDigitalInterfaceFacade {

	private static final long serialVersionUID = 1L;
	
	@Override
	public File criarXMLDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, String nonce, UsuarioVO usuarioVO, ConsistirException consistirException) throws Exception {
		TDiploma diploma = new TDiploma();
		TInfDiploma infDiploma = new TInfDiploma();
		infDiploma.setId("VDip" + nonce);
		infDiploma.setVersao("1.05");
		if (expedicaoDiplomaVO.getEmitidoPorDecisaoJudicial()) {
			criarDadosDiplomaDiplomaPorDecisaoJudicial(expedicaoDiplomaVO, usuarioVO, nonce, consistirException, infDiploma);
			preencherDadosRegistroPorDecisaoJudicial(expedicaoDiplomaVO, usuarioVO, nonce, infDiploma, consistirException);
		} else {
			infDiploma.setDadosDiploma(criarDadosDiploma(expedicaoDiplomaVO, usuarioVO, nonce, consistirException));
			preencherDadosRegistroDiploma(expedicaoDiplomaVO, usuarioVO, nonce, infDiploma, consistirException);
		}
		if (!consistirException.getListaMensagemErro().isEmpty() && !expedicaoDiplomaVO.getNovaGeracaoRepresentacaoVisualDiplomaDigital()) {
			throw consistirException;
		}
		diploma.setInfDiploma(infDiploma);
		return criarArquivoXML(TDiploma.class, diploma, "relatorio/Diploma_" + obterMatriculaTratada(expedicaoDiplomaVO.getMatricula().getMatricula()) + "_" + expedicaoDiplomaVO.getVia() + "_" + Uteis.removeCaractersEspeciais2(Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-").replaceAll(" ", "_"))  + ".xml");
	}

	@Override
	public File criarXMLDocumentacaoAcademicaRegistro(ExpedicaoDiplomaVO expedicaoDiplomaVO, String nonce, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		TDocumentacaoAcademicaRegistro documentacaoAcademicaRegistro = new TDocumentacaoAcademicaRegistro();
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO())) {
			expedicaoDiplomaVO.setCargoFuncionarioPrincipalVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		}
		if (expedicaoDiplomaVO.getEmitidoPorDecisaoJudicial()) {
			TRegistroPorDecisaoJudicialReq registroPorDecisaoJudicialReq = new TRegistroPorDecisaoJudicialReq();
			registroPorDecisaoJudicialReq.setId("ReqDip" + nonce);
			registroPorDecisaoJudicialReq.setVersao("1.05");
			criarDadosDocumentacaoAcademicaDiplomaPorDecisaoJudicial(expedicaoDiplomaVO, usuarioVO, nonce, ce, registroPorDecisaoJudicialReq);
			preencherDadosPrivadosDiplomadoDocumentacaoPorDecisaoJudicial(expedicaoDiplomaVO, registroPorDecisaoJudicialReq, config, usuarioVO, ce);
			registroPorDecisaoJudicialReq.setTermoResponsabilidadeEmissora(preencherTermoResponsabilidadeDocumentacao(expedicaoDiplomaVO));
			registroPorDecisaoJudicialReq.setDocumentacaoComprobatoria(preencherDocumentacaoComprobatoriaDocumentacaoDecisaoJudicial(expedicaoDiplomaVO, config, usuarioVO, ce));
			documentacaoAcademicaRegistro.setRegistroPorDecisaoJudicialReq(registroPorDecisaoJudicialReq);
		} else {
			if ("1".equals(expedicaoDiplomaVO.getVia())) {
				TRegistroReq registroReq = new TRegistroReq();
				registroReq.setId("ReqDip" + nonce);
				registroReq.setVersao("1.05");
				registroReq.setDadosDiploma(criarDadosDiploma(expedicaoDiplomaVO, usuarioVO, nonce, ce));
				preencherDadosPrivadosDiplomadoDocumentacao(expedicaoDiplomaVO, registroReq, config, usuarioVO, ce);
				registroReq.setDocumentacaoComprobatoria(preencherDocumentacaoComprobatoriaDocumentacao(expedicaoDiplomaVO, config, usuarioVO, ce));
				registroReq.setTermoResponsabilidadeEmissora(preencherTermoResponsabilidadeDocumentacao(expedicaoDiplomaVO));
				documentacaoAcademicaRegistro.setRegistroReq(registroReq);
			} else {
				TRegistroSegundaViaReq registroSegundaViaReq = new TRegistroSegundaViaReq();
				registroSegundaViaReq.setId("ReqDip" + nonce);
				registroSegundaViaReq.setVersao("1.05");
				registroSegundaViaReq.setDadosDiploma(criarDadosDiploma(expedicaoDiplomaVO, usuarioVO, nonce, ce));
				preencherDadosPrivadosDiplomadoSegundaViaDocumentacao(expedicaoDiplomaVO, registroSegundaViaReq, usuarioVO, ce);
				preencherDocumentacaoComprobatoriaSegundaViaDocumentacao(expedicaoDiplomaVO, registroSegundaViaReq, config, usuarioVO, ce);
				preencherTermoResponsabilidadeSegundaViaDocumentacao(expedicaoDiplomaVO, registroSegundaViaReq);
				documentacaoAcademicaRegistro.setRegistroSegundaViaReq(registroSegundaViaReq);
			}
		}
		if (!ce.getListaMensagemErro().isEmpty()) {
			throw ce;
		}
		return criarArquivoXML(TDocumentacaoAcademicaRegistro.class, documentacaoAcademicaRegistro, "relatorio/DocumentacaoDiploma_" + obterMatriculaTratada(expedicaoDiplomaVO.getMatricula().getMatricula()) + "_" + expedicaoDiplomaVO.getVia() + "_" + Uteis.removeCaractersEspeciais2(Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-").replaceAll(" ", "_"))  + ".xml");
	}

	@Override
	public File criarXMLHistoricoEscolar(ExpedicaoDiplomaVO expedicaoDiplomaVO, String nonce, UsuarioVO usuarioVO, ConsistirException consistirException, HistoricoAlunoRelVO histAlunoRelVO) throws Exception {
		Locale.setDefault(new Locale("pt", "BR"));
		TInfHistoricoEscolar infHistoricoEscolar = new TInfHistoricoEscolar();
		infHistoricoEscolar.setVersao("1.05");
		infHistoricoEscolar.setAmbiente(TAmb.PRODUÇÃO);
		infHistoricoEscolar.setAluno(preencherDadosDiplomadoGenerico(expedicaoDiplomaVO, consistirException, usuarioVO));
		preencherDadosMinimoCurso(expedicaoDiplomaVO, infHistoricoEscolar, consistirException);
		preencherDadosIesEmissoraHistoricoDigital(expedicaoDiplomaVO, infHistoricoEscolar, usuarioVO, consistirException);
		preencherHistoricoEscolarHistoricoEscolar(expedicaoDiplomaVO, usuarioVO, infHistoricoEscolar, histAlunoRelVO, consistirException);
		String hashCodigoValidacao = realizarGeracaoHashCodigoValidacaoHistoricoDigital(expedicaoDiplomaVO, usuarioVO);
		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
		TSegurancaHistorico seguranca = new TSegurancaHistorico();
		if (Uteis.isAtributoPreenchido(hashCodigoValidacao)) {
			seguranca.setCodigoValidacao(hashCodigoValidacao);
			expedicaoDiplomaVO.getDocumentoAssinadoVO().setCodigoValidacaoHistoricoDigital(hashCodigoValidacao);
		} else {
			seguranca.setCodigoValidacao(Constantes.EMPTY);
			expedicaoDiplomaVO.getDocumentoAssinadoVO().setCodigoValidacaoHistoricoDigital(Constantes.EMPTY);
		}
		infHistoricoEscolar.setSegurancaHistorico(seguranca);
		if (histAlunoRelVO.getCurriculoIntegralizado() || histAlunoRelVO.getMatriculaVO().getSituacao().equals("FO")) {
			TDocumentoHistoricoEscolarFinal documentoHistoricoEscolarDigital = new TDocumentoHistoricoEscolarFinal();
			documentoHistoricoEscolarDigital.setInfHistoricoEscolar(infHistoricoEscolar);
			return criarArquivoXML(TDocumentoHistoricoEscolarFinal.class, documentoHistoricoEscolarDigital, "relatorio/HistoricoEscolar_" + obterMatriculaTratada(expedicaoDiplomaVO.getMatricula().getMatricula()) + "_" + expedicaoDiplomaVO.getVia()  + "_" + Uteis.removeCaractersEspeciais2(Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-").replaceAll(" ", "_"))  + ".xml");
		} else {
			TDocumentoHistoricoEscolarParcial documentoHistoricoEscolarDigital = new TDocumentoHistoricoEscolarParcial();
			documentoHistoricoEscolarDigital.setInfHistoricoEscolar(infHistoricoEscolar);
			return criarArquivoXML(TDocumentoHistoricoEscolarParcial.class, documentoHistoricoEscolarDigital, "relatorio/HistoricoEscolar_" + obterMatriculaTratada(expedicaoDiplomaVO.getMatricula().getMatricula()) + "_" + expedicaoDiplomaVO.getVia()  + "_" + Uteis.removeCaractersEspeciais2(Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-").replaceAll(" ", "_"))  + ".xml");
		}
	}
	
	@Override
	public File criarXMLCurriculoEscolarDigital(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricularVO) throws Exception {
		TCurriculoEscolar curriculoEscolar = new TCurriculoEscolar();
		TInfCurriculoEscolar infCurriculoEscolar = new TInfCurriculoEscolar();
		infCurriculoEscolar.setVersao("1.05");
		infCurriculoEscolar.setAmbiente(TAmb.PRODUÇÃO);
		preencherDadosCurriculoEscolar(infCurriculoEscolar, gestaoXmlGradeCurricularVO);
		curriculoEscolar.setInfCurriculoEscolar(infCurriculoEscolar);
		if (!gestaoXmlGradeCurricularVO.getConsistirException().getListaMensagemErro().isEmpty()) {
			throw gestaoXmlGradeCurricularVO.getConsistirException();
		}
		gestaoXmlGradeCurricularVO.setCodigoValidacao(curriculoEscolar.getInfCurriculoEscolar().getSegurancaCurriculo().getCodigoValidacao());
		String nomeArquivo = gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO().getCodigo() + Constantes.UNDERSCORE + gestaoXmlGradeCurricularVO.getCursoVO().getCodigo() + Constantes.UNDERSCORE + gestaoXmlGradeCurricularVO.getGradeCurricularVO().getCodigo() + Constantes.UNDERSCORE + Uteis.removeCaractersEspeciais2(Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-").replaceAll(" ", "_"));
		return criarArquivoXML(TCurriculoEscolar.class, curriculoEscolar, "relatorio/CurriculoEscolarDigital_" + Uteis.removeCaractersEspeciais2(Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-").replaceAll(" ", "_")) + obterMatriculaTratada(nomeArquivo) + ".xml");
	}
	
	@Override
	public List<String> validarConformidadeXML(DocumentoAssinadoVO documentoAssinadoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		XmlErrorHandler xml = validadorXml(documentoAssinadoVO, configuracaoGeralSistemaVO);
		if (xml != null) {
			if (xml.getErrors().isEmpty()) {
				return new ArrayList<>(0);
			} else {
				List<String> list = new ArrayList<>(0);
				for (SAXParseException saxParseException : xml.getErrors()) {
					if (Uteis.isAtributoPreenchido(saxParseException.getMessage())) {
						list.add(saxParseException.getMessage().substring(saxParseException.getMessage().indexOf(":")+2, saxParseException.getMessage().length()));
					}
				}
				return list;
			}
		}
		return new ArrayList<>(0);
	}
	
	private XmlErrorHandler validadorXml(DocumentoAssinadoVO documentoAssinadoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (Uteis.isAtributoPreenchido(documentoAssinadoVO)) {
			String urlXml = Constantes.EMPTY;
			if (documentoAssinadoVO.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
				ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
				String nomeArquivoUsar = documentoAssinadoVO.getArquivo().getDescricao().contains(".") ? documentoAssinadoVO.getArquivo().getDescricao() : documentoAssinadoVO.getArquivo().getDescricao() + (documentoAssinadoVO.getArquivo().getNome().substring(documentoAssinadoVO.getArquivo().getNome().lastIndexOf("."), documentoAssinadoVO.getArquivo().getNome().length()));
				if (servidorArquivoOnlineS3RS.consultarSeObjetoExiste(documentoAssinadoVO.getArquivo().recuperarNomeArquivoServidorExterno(documentoAssinadoVO.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))) {
					File file = new File(getCaminhoPastaWeb() + "relatorio" + File.separator + documentoAssinadoVO.getArquivo().getNome());
					if (file.exists()) {
						file.delete();
					}
					getFacadeFactory().getArquivoHelper().realizarDownloadArquivoAmazon(documentoAssinadoVO.getArquivo(), UteisJSF.getCaminhoWeb() + File.separator + Constantes.relatorio + File.separator, configuracaoGeralSistemaVO);
					urlXml = ((getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + file.getName()).replace(File.separator, "/")).replace(File.separator, "/");
				} else {
					nomeArquivoUsar = documentoAssinadoVO.getArquivo().getDescricao();
					if (servidorArquivoOnlineS3RS.consultarSeObjetoExiste(documentoAssinadoVO.getArquivo().recuperarNomeArquivoServidorExterno(documentoAssinadoVO.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))) {
						File file = new File(getCaminhoPastaWeb() + "relatorio" + File.separator + documentoAssinadoVO.getArquivo().getNome());
						if (file.exists()) {
							file.delete();
						}
						getFacadeFactory().getArquivoHelper().realizarDownloadArquivoAmazon(documentoAssinadoVO.getArquivo(), UteisJSF.getCaminhoWeb() + File.separator + Constantes.relatorio + File.separator, configuracaoGeralSistemaVO);
						urlXml = ((getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + file.getName()).replace(File.separator, "/")).replace(File.separator, "/");
					} else {
						throw new Exception("Não foi encontrado no repositório da AMAZON o aquivo no caminho " + documentoAssinadoVO.getArquivo().recuperarNomeArquivoServidorExterno(documentoAssinadoVO.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar) + ".");
					}
				}
			} else {
				urlXml = (configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + "/" + documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + "/" + documentoAssinadoVO.getArquivo().getNome()).replace(File.separator, "/");
			}
			if (!Uteis.isAtributoPreenchido(urlXml)) {
				throw new Exception("Não foi possivel encontrar o xml para realizar a validação.");
			}
			XmlErrorHandler errorHandler = new XmlErrorHandler();
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Source[] source = getSources(documentoAssinadoVO);
			Schema schema = factory.newSchema(source);
			Validator validator = schema.newValidator();
			validator.setErrorHandler(errorHandler);
			try {
				validator.validate(new StreamSource(new File(urlXml)));
				return errorHandler;
			} catch (SAXParseException e) {
				return errorHandler;
			} finally {
				if (factory != null) {
					factory = null;
				}
				if (source != null) {
					source = null;
				}
				if (schema != null) {
					schema = null;
				}
				if (validator != null) {
					validator = null;
				}
			}
		}
		return new XmlErrorHandler();
	}
	
	private Source[] getSources(DocumentoAssinadoVO documentoAssinadoVO) throws Exception {
		Source[] arraySource = null;
		List<Source> sources = new ArrayList<>(0);
		if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL)) {
			String xsdDiplomaXml = getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "XSD" + File.separator + "1_05" + File.separator + "DiplomaDigital_v1.05.xsd";
			String xsdLeiauteXml = getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "XSD" + File.separator + "1_05" + File.separator + "leiauteDiplomaDigital_v1.05.xsd";
			Source streamDiploma = new StreamSource(new File(xsdDiplomaXml));
			Source streamLeiaute = new StreamSource(new File(xsdLeiauteXml));
			sources.add(streamDiploma);
			sources.add(streamLeiaute);
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL)) {
			String xsdDocumentacaoXml = getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "XSD" + File.separator + "1_05" + File.separator + "DocumentacaoAcademicaRegistroDiplomaDigital_v1.05.xsd";
			String xsdLeiauteXml = getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "XSD" + File.separator + "1_05" + File.separator + "leiauteDocumentacaoAcademicaRegistroDiplomaDigital_v1.05.xsd";
			Source streamDocumentacao = new StreamSource(new File(xsdDocumentacaoXml));
			Source streamLeiaute = new StreamSource(new File(xsdLeiauteXml));
			sources.add(streamDocumentacao);
			sources.add(streamLeiaute);
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL)) {
			String xsdHistoricoXml = getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "XSD" + File.separator + "1_05" + File.separator + "HistoricoEscolarDigital_v1.05.xsd";
			String xsdLeiauteXml = getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "XSD" + File.separator + "1_05" + File.separator + "leiauteHistoricoEscolar_v1.05.xsd";
			Source streamHistorico = new StreamSource(new File(xsdHistoricoXml));
			Source streamLeiaute = new StreamSource(new File(xsdLeiauteXml));
			sources.add(streamHistorico);
			sources.add(streamLeiaute);
		} else if (documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL)) {
			String xsdCurriculoXml = getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "XSD" + File.separator + "1_05" + File.separator + "CurriculoEscolarDigital_v1.05.xsd";
			String xsdLeiauteXml = getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "XSD" + File.separator + "1_05" + File.separator + "leiauteCurriculoEscolar_v1.05.xsd";
			Source streamCurriculo = new StreamSource(new File(xsdCurriculoXml));
			Source streamLeiaute = new StreamSource(new File(xsdLeiauteXml));
			sources.add(streamCurriculo);
			sources.add(streamLeiaute);
		}
		if (!Uteis.isAtributoPreenchido(sources)) {
			throw new Exception("Não foi possivel montar os xsds do " + documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().getDescricao());
		}
		Source streamTipoBasico = new StreamSource(new File(getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "XSD" + File.separator + "1_05" + File.separator + "tiposBasicos_v1.05.xsd"));
		Source streamCore = new StreamSource(new File(getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "XSD" + File.separator + "1_05" + File.separator + "xmldsig-core-schema_v1.1.xsd"));
		sources.add(streamTipoBasico);
		sources.add(streamCore);
		arraySource = new Source[sources.size()];
		for (int i = 0; i < arraySource.length; i++) {
			arraySource[i] = sources.get(i);
		}
		return arraySource;
	}

	@SuppressWarnings("rawtypes")
	private File criarArquivoXML(Class classe, Object objeto, String path) throws Exception {
		Marshaller marshaller = JAXBContext.newInstance(classe).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
		marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
		File arquivo = new File(UteisJSF.getCaminhoWeb() + path);
		OutputStream os = new FileOutputStream(arquivo);
		marshaller.marshal(objeto, os);
		return arquivo;
	}

	private String obterMatriculaTratada(String matriculaOriginal) {
		String matricula = Constantes.EMPTY;
		if (matriculaOriginal.contains("/") || matriculaOriginal.contains("\"")) {
			matricula = matriculaOriginal.replaceAll("/", "_");
			matricula = matricula.replaceAll("\"", "_");
		} else {
			matricula = matriculaOriginal;
		}
		if (matricula.contains(" ")) {
			matricula = matricula.replace(" ", "_");
		}
		return matricula;
	}
	
	private String getStringXml(String string) {
		if (Uteis.isAtributoPreenchido(string)) {
			return Uteis.formatarStringXML(string.trim());
		} else {
			return Constantes.EMPTY;
		}
	}
	
	private String getCnpjXml(String cnpj) {
		if (Uteis.isAtributoPreenchido(cnpj)) {
			return Uteis.removerMascara(getStringXml(cnpj));
		} else {
			return Constantes.EMPTY;
		}
	}
	
	private String getCpfXml(String cnpj) {
		if (Uteis.isAtributoPreenchido(cnpj)) {
			return Uteis.removerMascara(getStringXml(cnpj));
		} else {
			return Constantes.EMPTY;
		}
	}
	
	private enum TipoOrigemMontagemDados {
		CURSO("(Dados do Curso)"),
		IES_EMISSORA("(Dados da Unidade Emissora)"),
		MANTENEDORA("(Dados da Mantenedora)"),
		REGISTRADORA("(Dados da Registradora)"),
		MANTENEDORA_REGISTRADORA("(Dados da Mantenedora da Registradora)"),
		HISTORICO_ESCOLAR("(Dados do Histórico Escolar)"),
		ESTAGIO("(Dados do Estágio)");
		
		private final String mensagem;
		
		private TipoOrigemMontagemDados(final String mensagem) {
			this.mensagem = mensagem;
		}
		
		public String getMensagem(){
			return this.mensagem;
		}
	}

	private TDadosDiploma criarDadosDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, String nonce, ConsistirException consistirException) throws Exception {
		TDadosDiploma dadosDiploma = new TDadosDiploma();
		String id = "Dip" + nonce;
		expedicaoDiplomaVO.setIdDiplomaDigital(id);
		dadosDiploma.setId(id);
		preencherDadosDiplomado(expedicaoDiplomaVO, dadosDiploma, consistirException, usuarioVO);
		preencherDadosCurso(expedicaoDiplomaVO, dadosDiploma, consistirException);
		preencherDadosIesEmissora(expedicaoDiplomaVO, dadosDiploma, usuarioVO, consistirException);
		preencherDadosIesOriginalCursoPTADiploma(expedicaoDiplomaVO, dadosDiploma, consistirException, usuarioVO);
		dadosDiploma.setDataConclusao(getDataConvertidaXML(expedicaoDiplomaVO.getMatricula().getDataConclusaoCurso()));
		return dadosDiploma;
	}

	private void preencherDadosRegistroDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, String nonce, TInfDiploma infDiploma, ConsistirException consistirException) throws Exception {
		TDadosRegistro dadosRegistro = new TDadosRegistro();
		String id = "RDip" + nonce;
		expedicaoDiplomaVO.setIdDadosRegistrosDiplomaDigital(id);
		dadosRegistro.setId(id);
		dadosRegistro.setIdDocumentacaoAcademica("ReqDip" + nonce);
		preencherDadosIesRegistradora(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora(), expedicaoDiplomaVO.getMatricula().getCurso(), usuarioVO, dadosRegistro, consistirException);
		preencherDadosLivroRegistroDiploma(expedicaoDiplomaVO, usuarioVO, dadosRegistro, consistirException);
		TSeguranca seguranca = new TSeguranca();
		if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital()) || !expedicaoDiplomaVO.getNovaGeracaoRepresentacaoVisualDiplomaDigital()) {
			String hashCodigoValidacao = realizarGeracaoHashCodigoValidacao(expedicaoDiplomaVO, dadosRegistro.getLivroRegistro().getLivroRegistro(), dadosRegistro.getLivroRegistro().getNumeroFolhaDoDiploma(), dadosRegistro.getLivroRegistro().getNumeroSequenciaDoDiploma(), usuarioVO);
			if (Uteis.isAtributoPreenchido(hashCodigoValidacao)) {
				seguranca.setCodigoValidacao(hashCodigoValidacao);
				expedicaoDiplomaVO.setCodigoValidacaoDiplomaDigital(hashCodigoValidacao);
			} else {
				seguranca.setCodigoValidacao(Constantes.EMPTY);
				expedicaoDiplomaVO.setCodigoValidacaoDiplomaDigital(Constantes.EMPTY);
			}
		} else {
			seguranca.setCodigoValidacao(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital());
		}
		dadosRegistro.setSeguranca(seguranca);
		infDiploma.setDadosRegistro(dadosRegistro);
	}

	private void preencherDadosIesRegistradora(UnidadeEnsinoVO unidadeEmissora, CursoVO curso, UsuarioVO usuarioVO, TDadosRegistro dadosRegistro, ConsistirException consistirException) throws Exception {
		dadosRegistro.setIesRegistradora(preencherDadosIesRegistradora(unidadeEmissora, curso, usuarioVO, consistirException));
	}
	
	private void preencherDadosIesRegistradoraDecisaoJudicial(UnidadeEnsinoVO unidadeEmissora, CursoVO curso, UsuarioVO usuarioVO, TDadosRegistroPorDecisaoJudicial dadosRegistroPorDecisaoJudicial, ConsistirException consistirException) throws Exception {
		dadosRegistroPorDecisaoJudicial.setIesRegistradora(preencherDadosIesRegistradora(unidadeEmissora, curso, usuarioVO, consistirException));
	}
	
	private TDadosIesRegistradora preencherDadosIesRegistradora(UnidadeEnsinoVO unidadeEmissora, CursoVO curso, UsuarioVO usuarioVO, ConsistirException consistirException) throws Exception {
		try {
			TDadosIesRegistradora dadosIesRegistradora = new TDadosIesRegistradora();
			validarDadosIesRegistradora(unidadeEmissora, usuarioVO, consistirException);
			dadosIesRegistradora.setNome(getStringXml(unidadeEmissora.getUnidadeCertificadora()));
			dadosIesRegistradora.setCNPJ(getCnpjXml(unidadeEmissora.getCnpjUnidadeCertificadora()));
			dadosIesRegistradora.setCodigoMEC(Long.valueOf(unidadeEmissora.getCodigoIESUnidadeCertificadora()));
			dadosIesRegistradora.setCredenciamento(preencherDadosCredenciamentoIesRegistradora(unidadeEmissora, curso, consistirException));
			dadosIesRegistradora.setEndereco(preencherDadosEnderecoIesRegistradora(unidadeEmissora, consistirException));
			preencherDadosMantenedoraIesRegistradora(unidadeEmissora, dadosIesRegistradora, usuarioVO, consistirException);
			return dadosIesRegistradora;
		} catch (Exception e) {
			consistirException.getListaMensagemErro().add(e.getMessage());
			return null;
		}
	}

	public void validarDadosIesRegistradora(UnidadeEnsinoVO unidadeEmissora, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getUnidadeCertificadora()), "O NOME da UNIDADE REGISTRADORA deve ser informado (Dados da Unidade Registradora)", ce);
		String cnpj = Uteis.removerMascara(unidadeEmissora.getCnpjUnidadeCertificadora());
		Uteis.checkStateList(!Uteis.validaCNPJ(cnpj), "O CNPJ da UNIDADE REGISTRADORA deve ser válido (Dados da Unidade Registradora)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCodigoIESUnidadeCertificadora()), "O CÓDIGO MEC da REGISTRADORA deve ser informado (Dados da Unidade Registradora)", ce);
	}

	private TEndereco preencherDadosEnderecoIesRegistradora(UnidadeEnsinoVO unidadeEmissora, ConsistirException ce) throws Exception {
		TEndereco endereco = new TEndereco();
		if (unidadeEmissora.getUtilizarEnderecoUnidadeEnsinoRegistradora()) {
			endereco = getEnderecoIesEmissora(unidadeEmissora, TipoOrigemMontagemDados.REGISTRADORA, ce);
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getEnderecoRegistradora()), "O ENDEREÇO da IES REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.REGISTRADORA.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getBairroRegistradora()), "O SETOR/BAIRRO da IES REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.REGISTRADORA.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCepRegistradora()), "O CEP da IES REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.REGISTRADORA.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCidadeRegistradora().getCodigoIBGE()), "O CÓDIGO IBGE da CIDADE da IES REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.REGISTRADORA.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCidadeRegistradora().getNome()), "O NOME da CIDADE da IES REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.REGISTRADORA.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCidadeRegistradora().getEstado().getSigla()), "A SIGLA do ESTADO da CIDADE da IES REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.REGISTRADORA.getMensagem(), ce);
			endereco.setLogradouro(getStringXml(unidadeEmissora.getEnderecoRegistradora()));
			endereco.setBairro(getStringXml(unidadeEmissora.getBairroRegistradora()));
			endereco.setCodigoMunicipio(getStringXml(unidadeEmissora.getCidadeRegistradora().getCodigoIBGE()));
			endereco.setNomeMunicipio(getStringXml(unidadeEmissora.getCidadeRegistradora().getNome()));
			endereco.setCEP(Uteis.removerMascara(getStringXml(unidadeEmissora.getCepRegistradora())));
			endereco.setUF(getUfEndereco(unidadeEmissora.getCidadeRegistradora().getEstado().getSigla()));
			if (Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroRegistradora())) {
				endereco.setNumero(getStringXml(unidadeEmissora.getNumeroRegistradora()));
			}
			if (Uteis.isAtributoPreenchido(unidadeEmissora.getComplementoRegistradora())) {
				endereco.setComplemento(getStringXml(unidadeEmissora.getComplementoRegistradora()));
			}
		}
		return endereco;
	}

	private TAtoRegulatorioComOuSemEMEC preencherDadosCredenciamentoIesRegistradora(UnidadeEnsinoVO unidadeEmissora, CursoVO cursoVO, ConsistirException ce) throws Exception {
		TAtoRegulatorioComOuSemEMEC atoRegulatorio = new TAtoRegulatorioComOuSemEMEC();
		if (unidadeEmissora.getUtilizarCredenciamentoUnidadeEnsino()) {
			atoRegulatorio = preencherDadosCredenciamentoIesEmissora(unidadeEmissora, cursoVO, "do CREDENCIAMENTO @{MODALIDADE}", "(Dados da Registradora)", ce);
		} else {
			if (unidadeEmissora.getCredenciamentoRegistradoraEmTramitacao()) {
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroProcessoCredenciamentoRegistradora()), "O NÚMERO do PROCESSO do CREDENCIAMENTO da REGISTRADORA deve ser preenchido (Dados da Registradora)", ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoProcessoCredenciamentoRegistradora()), "O TIPO DE PROCESSO do CREDENCIAMENTO da REGISTRADORA deve ser preenchido (Dados da Registradora)", ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataCadastroCredenciamentoRegistradora()), "A DATA DE CADASTRO do CREDENCIAMENTO da REGISTRADORA deve ser preenchido (Dados da Registradora)", ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataProtocoloCredenciamentoRegistradora()), "A DATA do PROTOCOLO do CREDENCIAMENTO da REGISTRADORA deve ser preenchido (Dados da Registradora)", ce);
				atoRegulatorio.setInformacoesTramitacaoEMEC(new TInformacoesTramitacaoEMEC());
				atoRegulatorio.getInformacoesTramitacaoEMEC().setNumeroProcesso(Long.valueOf(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroProcessoCredenciamentoRegistradora()))));
				atoRegulatorio.getInformacoesTramitacaoEMEC().setTipoProcesso(getStringXml(unidadeEmissora.getTipoProcessoCredenciamentoRegistradora()));
				atoRegulatorio.getInformacoesTramitacaoEMEC().setDataCadastro(getDataConvertidaXML(unidadeEmissora.getDataCadastroCredenciamentoRegistradora()));
				atoRegulatorio.getInformacoesTramitacaoEMEC().setDataProtocolo(getDataConvertidaXML(unidadeEmissora.getDataProtocoloCredenciamentoRegistradora()));
			} else {
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoCredenciamentoRegistradora()), "O TIPO DE AUTORIZAÇÃO do CREDENCIAMENTO da REGISTRADORA deve ser preenchido (Dados da Registradora)", ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroCredenciamentoRegistradora()), "O NÚMERO do CREDENCIAMENTO da REGISTRADORA deve ser preenchido (Dados da Registradora)", ce);
				Uteis.checkStateList(!Uteis.getIsValorNumerico(unidadeEmissora.getNumeroCredenciamentoRegistradora()), "O NÚMERO do CREDENCIAMENTO da REGISTRADORA deve ser numérico (Dados da Registradora)", ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataCredenciamentoRegistradora()), "A DATA DE CREDENCIAMENTO da REGISTRADORA deve ser preenchido (Dados da Registradora)", ce);
				atoRegulatorio.setTipo(Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoCredenciamentoRegistradora()) ? unidadeEmissora.getTipoAutorizacaoCredenciamentoRegistradora().getTipoAtoDiploma() : null);
				atoRegulatorio.setNumero(Uteis.removeCaractersEspeciais2(unidadeEmissora.getNumeroCredenciamentoRegistradora()));
				atoRegulatorio.setData(getDataConvertidaXML(unidadeEmissora.getDataCredenciamentoRegistradora()));
				if (unidadeEmissora.getDataPublicacaoDORegistradora() != null) {
					atoRegulatorio.setDataPublicacao(getDataConvertidaXML(unidadeEmissora.getDataPublicacaoDORegistradora()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getVeiculoPublicacaoCredenciamentoRegistradora())) {
					atoRegulatorio.setVeiculoPublicacao(getStringXml(unidadeEmissora.getVeiculoPublicacaoCredenciamentoRegistradora()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getSecaoPublicacaoCredenciamentoRegistradora())) {
					atoRegulatorio.setSecaoPublicacao(Long.valueOf(unidadeEmissora.getSecaoPublicacaoCredenciamentoRegistradora()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getPaginaPublicacaoCredenciamentoRegistradora())) {
					atoRegulatorio.setPaginaPublicacao(Long.valueOf(unidadeEmissora.getPaginaPublicacaoCredenciamentoRegistradora()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroPublicacaoCredenciamentoRegistradora())) {
					atoRegulatorio.setNumeroDOU(Long.valueOf(unidadeEmissora.getNumeroPublicacaoCredenciamentoRegistradora()));
				}
			}
		}
		return atoRegulatorio;
	}

	private void preencherDadosMantenedoraIesRegistradora(UnidadeEnsinoVO unidadeEmissora, TDadosIesRegistradora dadosIesRegistradora, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		TDadosIesRegistradora.Mantenedora mantenedora = new TDadosIesRegistradora.Mantenedora();
		if (unidadeEmissora.getUtilizarMantenedoraUnidadeEnsino()) {
			mantenedora.setRazaoSocial(getStringXml(unidadeEmissora.getMantenedora()));
			mantenedora.setCNPJ(getCnpjXml(unidadeEmissora.getCnpjMantenedora()));
		} else {
			mantenedora.setRazaoSocial(getStringXml(unidadeEmissora.getMantenedoraRegistradora()));
			mantenedora.setCNPJ(getCnpjXml(unidadeEmissora.getCnpjMantenedoraRegistradora()));
		}
		preencherDadosEnderecoMantenedoraIesRegistradora(unidadeEmissora, mantenedora, ce);
		dadosIesRegistradora.setMantenedora(mantenedora);
	}

	private void preencherDadosEnderecoMantenedoraIesRegistradora(UnidadeEnsinoVO unidadeEnsinoVO, TDadosIesRegistradora.Mantenedora mantenedora, ConsistirException ce) throws Exception {
		TEndereco endereco = new TEndereco();
		if (unidadeEnsinoVO.getUtilizarMantenedoraUnidadeEnsino()) {
			endereco = preencherDadosEnderecoMantenedoraIesEmissora(unidadeEnsinoVO, TipoOrigemMontagemDados.MANTENEDORA_REGISTRADORA, ce);
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getEnderecoMantenedoraRegistradora()), "O ENDEREÇO da MANTENEDORA da REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.MANTENEDORA_REGISTRADORA.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getBairroMantenedoraRegistradora()), "O SETOR/BAIRRO da MANTENEDORA da REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.MANTENEDORA_REGISTRADORA.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCepMantenedoraRegistradora()), "O CEP da MANTENEDORA da REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.MANTENEDORA_REGISTRADORA.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCidadeMantenedoraRegistradora().getCodigoIBGE()), "O CÓDIGO IBGE da cidade da MANTENEDORA da REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.MANTENEDORA_REGISTRADORA.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCidadeMantenedoraRegistradora().getNome()), "O NOME da cidade da MANTENEDORA da REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.MANTENEDORA_REGISTRADORA.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCidadeMantenedoraRegistradora().getEstado().getSigla()), "A SIGLA do estado da cidade da MANTENEDORA da REGISTRADORA deve ser informado " + TipoOrigemMontagemDados.MANTENEDORA_REGISTRADORA.getMensagem(), ce);
			endereco.setLogradouro(getStringXml(unidadeEnsinoVO.getEnderecoMantenedoraRegistradora()));
			endereco.setBairro(getStringXml(unidadeEnsinoVO.getBairroMantenedoraRegistradora()));
			endereco.setCodigoMunicipio(getStringXml(unidadeEnsinoVO.getCidadeMantenedoraRegistradora().getCodigoIBGE()));
			endereco.setNomeMunicipio(getStringXml(unidadeEnsinoVO.getCidadeMantenedoraRegistradora().getNome()));
			endereco.setCEP(Uteis.removerMascara(getStringXml(unidadeEnsinoVO.getCepMantenedoraRegistradora())));
			endereco.setUF(getUfEndereco(unidadeEnsinoVO.getCidadeMantenedoraRegistradora().getEstado().getSigla()));
			if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getNumeroMantenedoraRegistradora())) {
				endereco.setNumero(getStringXml(unidadeEnsinoVO.getNumeroMantenedoraRegistradora()));
			}
			if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getComplementoMantenedoraRegistradora())) {
				endereco.setComplemento(getStringXml(unidadeEnsinoVO.getComplementoMantenedoraRegistradora()));
			}
		}
		mantenedora.setEndereco(endereco);
	}

	private void preencherDadosLivroRegistroDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, TDadosRegistro dadosRegistro, ConsistirException consistirException) throws Exception {
		validarDadosLivroRegistroDiploma(expedicaoDiplomaVO, usuarioVO, consistirException);
		TLivroRegistro livroRegistro = new TLivroRegistro();
		ControleLivroRegistroDiplomaVO livroRegistroDiplomaVO = new ControleLivroRegistroDiplomaVO();
		ControleLivroFolhaReciboVO livroFolhaReciboVO = new ControleLivroFolhaReciboVO();
		if (expedicaoDiplomaVO.getInformarCamposLivroRegistradora()) {
			livroRegistro.setLivroRegistro(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getLivroRegistradora().trim()) ? expedicaoDiplomaVO.getLivroRegistradora().trim() : "");
			livroRegistro.setNumeroFolhaDoDiploma(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFolhaReciboRegistradora().trim()) ? expedicaoDiplomaVO.getFolhaReciboRegistradora().trim() : null);
			if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFolhaReciboRegistradora())) {
				livroRegistro.setNumeroRegistro(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroRegistroDiploma().trim()) ? expedicaoDiplomaVO.getNumeroRegistroDiploma().trim() : "");
			} else {
				livroRegistro.setNumeroSequenciaDoDiploma(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroRegistroDiploma().trim()) ? expedicaoDiplomaVO.getNumeroRegistroDiploma().trim() : "");
			}
		} else {
			livroFolhaReciboVO = getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorMatriculaMatriculaPrimeiraEUltimaVia(expedicaoDiplomaVO.getMatricula().getMatricula(), expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), TipoLivroRegistroDiplomaEnum.DIPLOMA, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(livroFolhaReciboVO)) {
				livroRegistro.setLivroRegistro(livroFolhaReciboVO.getControleLivroRegistroDiploma().getNrLivro().toString().trim());
				livroRegistro.setNumeroFolhaDoDiploma(Uteis.isAtributoPreenchido(livroFolhaReciboVO.getFolhaReciboAtual().toString().trim()) ? livroFolhaReciboVO.getFolhaReciboAtual().toString().trim() : null);
				if (!Uteis.isAtributoPreenchido(livroFolhaReciboVO.getFolhaReciboAtual().toString().trim())) {
					livroRegistro.setNumeroRegistro(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroRegistroDiploma().trim()) ? expedicaoDiplomaVO.getNumeroRegistroDiploma().trim() : "");
				} else {
					livroRegistro.setNumeroSequenciaDoDiploma(livroFolhaReciboVO.getNumeroRegistro().toString().trim());
				}
			} else {
				livroRegistroDiplomaVO = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorCodigoCurso(expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), null, TipoLivroRegistroDiplomaEnum.DIPLOMA, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
				if (Uteis.isAtributoPreenchido(livroRegistroDiplomaVO)) {
					livroRegistro.setLivroRegistro(livroRegistroDiplomaVO.getNrLivro().toString().trim());
					livroRegistro.setNumeroFolhaDoDiploma(livroRegistroDiplomaVO.getNrFolhaRecibo().toString().trim());
					livroRegistro.setNumeroSequenciaDoDiploma(livroRegistroDiplomaVO.getNumeroRegistro().toString().trim());
				} else {
					livroRegistro.setLivroRegistro(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getLivroRegistradora().trim()) ? expedicaoDiplomaVO.getLivroRegistradora().trim() : "");
					livroRegistro.setNumeroFolhaDoDiploma(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFolhaReciboRegistradora().trim()) ? expedicaoDiplomaVO.getFolhaReciboRegistradora().trim() : "");
					livroRegistro.setNumeroSequenciaDoDiploma(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroRegistroDiploma().trim()) ? expedicaoDiplomaVO.getNumeroRegistroDiploma().trim() : "");
				}
			}
		}
		livroRegistro.setProcessoDoDiploma(Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroProcesso().trim().replace(" ", "")));
		livroRegistro.setDataColacaoGrau(getDataConvertidaXML(expedicaoDiplomaVO.getMatricula().getDataColacaoGrau()));
		livroRegistro.setDataExpedicaoDiploma(getDataConvertidaXML(expedicaoDiplomaVO.getDataExpedicao()));
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getDataRegistroDiploma())) {
			livroRegistro.setDataRegistroDiploma(getDataConvertidaXML(expedicaoDiplomaVO.getDataRegistroDiploma()));
		} else {
			livroRegistro.setDataRegistroDiploma(null);
		}
		TLivroRegistro.ResponsavelRegistro responsavelRegistro = new TLivroRegistro.ResponsavelRegistro();
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioQuartoVO().getCodigo())) {
			responsavelRegistro.setNome(expedicaoDiplomaVO.getFuncionarioQuartoVO().getPessoa().getNome().trim());
			responsavelRegistro.setCPF(Uteis.removerMascara(expedicaoDiplomaVO.getFuncionarioQuartoVO().getPessoa().getCPF().trim()));
			responsavelRegistro.setIDouNumeroMatricula(expedicaoDiplomaVO.getFuncionarioQuartoVO().getMatricula().trim());
		}
		livroRegistro.setResponsavelRegistro(responsavelRegistro);
		dadosRegistro.setLivroRegistro(livroRegistro);
	}

	public void validarDadosLivroRegistroDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getDataColacaoGrau()), "A data de colação grau da matrícula deve ser preenchido (Dados do Livro Registro Diploma)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getDataExpedicao()), "A data de expedição deve ser preenchido (Dados do Livro Registro Diploma)", ce);
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioQuartoVO())) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioQuartoVO().getPessoa().getNome()), "O nome do funcionário da registradora deve ser preenchido (Dados do Livro Registro Diploma)", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioQuartoVO().getPessoa().getCPF()), "O CPF do funcionário da registradora deve ser preenchido (Dados do Livro Registro Diploma)", ce);
			Uteis.checkStateList(!Uteis.validaCPF(Uteis.removerMascara(expedicaoDiplomaVO.getFuncionarioQuartoVO().getPessoa().getCPF())), ("O CPF do funcionário da registradora deve ser válido (Dados do Livro Registro Diploma)"), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioQuartoVO().getMatricula()), "A matrícula do funcionário da registradora deve ser preenchido (Dados do Livro Registro Diploma)", ce);
		}
	}

	private void criarDadosDiplomaDiplomaPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, String nonce, ConsistirException consistirException, TInfDiploma infDiploma) throws Exception {
		infDiploma.setDadosDiplomaPorDecisaoJudicial(criarDadosDiplomaPorDecisaoJudicial(expedicaoDiplomaVO, usuarioVO, nonce, consistirException));
	}

	private TDadosDiplomaPorDecisaoJudicial criarDadosDiplomaPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, String nonce, ConsistirException consistirException) throws Exception {
		TDadosDiplomaPorDecisaoJudicial dadosDiploma = new TDadosDiplomaPorDecisaoJudicial();
		String idDiploma = "Dip" + nonce;
		expedicaoDiplomaVO.setIdDiplomaDigital(idDiploma);
		dadosDiploma.setId(idDiploma);
		dadosDiploma.setDataConclusao(getDataConvertidaXML(expedicaoDiplomaVO.getMatricula().getDataConclusaoCurso()));
		preencherDadosDiplomadoPorDecisaoJudicial(expedicaoDiplomaVO, dadosDiploma, consistirException, usuarioVO);
		preencherDadosCursoPorDecisaoJudicial(expedicaoDiplomaVO, dadosDiploma, consistirException);
		preencherDadosIesOriginalCursoPTADiplomaPorDecisaoJudicial(expedicaoDiplomaVO, dadosDiploma, consistirException, usuarioVO);
		preencherDadosIesEmissoraPorDecisaoJudicial(expedicaoDiplomaVO, dadosDiploma, usuarioVO, consistirException);
		preencherDadosDeclaracoesEmissoraAcercaProcessoDecisaoJudicial(expedicaoDiplomaVO, dadosDiploma);
		return dadosDiploma;
	}

	private void preencherDadosDiplomadoPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiplomaPorDecisaoJudicial dadosDiploma, ConsistirException consistirException, UsuarioVO usuarioVO) throws Exception {
		dadosDiploma.setDiplomado(preencherDadosDiplomadoPorDecisaoJudicialGenerico(expedicaoDiplomaVO, consistirException, usuarioVO));
	}
	
	public void validarDadosDiplomadoDecisaoJudicial(PessoaVO aluno, ConsistirException ce, UsuarioVO usuario) throws Exception {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(aluno.getNomeBatismo()), "O nome registral do aluno deve ser preenchido (Dados do Diplomado)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(aluno.getCPF()), "O CPF do aluno deve ser preenchido (Dados do Diplomado)", ce);
		Uteis.checkStateList(Uteis.isAtributoPreenchido(aluno.getCPF()) && !Uteis.validaCPF(getCpfXml(aluno.getCPF())), "O CPF do aluno deve ser válido (Dados do Diplomado)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(aluno.getRG()), "O RG do aluno deve ser preenchido (Dados do Diplomado)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(aluno.getEstadoEmissaoRG()), "A UF da emissão do rg do aluno deve ser preenchido (Dados do Diplomado)", ce);
	}

	private TDadosDiplomadoPorDecisaoJudicial preencherDadosDiplomadoPorDecisaoJudicialGenerico(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException consistirException, UsuarioVO usuarioVO) throws Exception {
		validarDadosDiplomadoDecisaoJudicial(expedicaoDiplomaVO.getMatricula().getAluno(), consistirException, usuarioVO);
		TDadosDiplomadoPorDecisaoJudicial dadosDiplomado = new TDadosDiplomadoPorDecisaoJudicial();
		try {
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getAluno().getCodigo().toString())) {
				dadosDiplomado.setID(getStringXml(expedicaoDiplomaVO.getMatricula().getAluno().getCodigo().toString()));
			} else {
				dadosDiplomado.setIDIndisponivel(new TVazio());
			}
			dadosDiplomado.setNome(getStringXml(expedicaoDiplomaVO.getMatricula().getAluno().getNomeBatismo()));
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getAluno().getSexo())) {
				dadosDiplomado.setSexo(getSexoDiplomado(expedicaoDiplomaVO.getMatricula().getAluno().getSexo()));
			} else {
				dadosDiplomado.setSexoIndisponivel(new TVazio());
			}
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getAluno().getNacionalidade().getNacionalidade())) {
				dadosDiplomado.setNacionalidade(getStringXml(expedicaoDiplomaVO.getMatricula().getAluno().getNacionalidade().getNacionalidade()));
			} else {
				dadosDiplomado.setNacionalidadeIndisponivel(new TVazio());
			}
			dadosDiplomado.setCPF(getCpfXml(expedicaoDiplomaVO.getMatricula().getAluno().getCPF()));
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getAluno().getDataNasc())) {
				dadosDiplomado.setDataNascimento(getDataConvertidaXML(expedicaoDiplomaVO.getMatricula().getAluno().getDataNasc()));
			} else {
				dadosDiplomado.setDataNascimentoIndisponivel(new TVazio());
			}
			preencherDadosNomeSocialDadosDiplomadoPorDecisaoJudicial(expedicaoDiplomaVO, dadosDiplomado);
			preencherDadosNaturalidadeDiplomadoDecisaoJudicial(expedicaoDiplomaVO, dadosDiplomado, consistirException);
			preencherDadosRgDiplomadoDecisaoJudicial(expedicaoDiplomaVO, dadosDiplomado);
		} catch (Exception e) {
			Uteis.checkStateList(Boolean.TRUE, "Erro ao preencher Dados do Diplomado por Decisão Dudicial (" + e.getMessage() + ")", consistirException);
		}
		return dadosDiplomado;
	}
	
	private void preencherDadosNomeSocialDadosDiplomadoPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiplomadoPorDecisaoJudicial dadosDiplomado) {
		if (!Objects.equals(expedicaoDiplomaVO.getMatricula().getAluno().getNome().trim(), expedicaoDiplomaVO.getMatricula().getAluno().getNomeBatismo().trim())) {
			dadosDiplomado.setNomeSocial(getStringXml(expedicaoDiplomaVO.getMatricula().getAluno().getNome()));
		}
	}
	
	public void validarDadosCursoDecisaoJudicial(CursoVO cursoVO, UnidadeEnsinoVO unidadeEnsino, ConsistirException ce) throws Exception {
		if (!Uteis.isAtributoPreenchido(cursoVO.getNomeDocumentacao())) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getNome()), "O nome do curso deve ser preenchido (Dados do Curso)", ce);
		}
		if (cursoVO.getPossuiCodigoEMEC()) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getCodigoEMEC()), "O código emec do curso deve ser preenchido (Dados do Curso)", ce);
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getNumeroProcessoEMEC()), "O número processo emec do curso deve ser preenchido (Dados do Curso)", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getTipoProcessoEMEC()), "O tipo processo emec do curso deve ser preenchido (Dados do Curso)", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataCadastroEMEC()), "A data de cadastro emec do curso deve ser preenchido (Dados do Curso)", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataProtocoloEMEC()), "A data do protocolo emec do curso deve ser preenchido (Dados do Curso)", ce);
		}
		if (Uteis.isAtributoPreenchido(cursoVO.getHabilitacao())) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataHabilitacao()), "A data da habilitação do curso deve ser preenchido (Dados do Curso)", ce);
		}
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getModalidadeCurso()), "A modalidade do curso deve ser preenchido (Dados do Curso)", ce);
		if (!Uteis.isAtributoPreenchido(cursoVO.getTitulacaoFemininoApresentarDiploma()) || !Uteis.isAtributoPreenchido(cursoVO.getTitulacaoMasculinoApresentarDiploma())) {
			Uteis.checkStateList(Boolean.TRUE, "As titulações a ser exibida no diploma deve ser informada (Dados do Curso)", ce);
		}
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(getTitulo(cursoVO.getTitulo())), "O título do curso deve ser preenchido (Dados do Curso)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(getGrauConferido(cursoVO.getTitulo())), "O título preenchido no curso não é compatível com um grau conferido usado pelo mec (Curso) (Dados do Curso)", ce);
	}

	private void preencherDadosCursoPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiplomaPorDecisaoJudicial dadosDiploma, ConsistirException consistirException) throws Exception {
		validarDadosCursoDecisaoJudicial(expedicaoDiplomaVO.getMatricula().getCurso(), expedicaoDiplomaVO.getMatricula().getUnidadeEnsino(), consistirException);
		try {
			TDadosCursoPorDecisaoJudicial dadosCurso = new TDadosCursoPorDecisaoJudicial();
			dadosCurso.setNomeCurso(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getCurso().getNomeDocumentacao()) ? getStringXml(expedicaoDiplomaVO.getMatricula().getCurso().getNomeDocumentacao()) : getStringXml(expedicaoDiplomaVO.getMatricula().getCurso().getNome()));
			if (expedicaoDiplomaVO.getMatricula().getCurso().getPossuiCodigoEMEC()) {
				dadosCurso.setCodigoCursoEMEC(Long.valueOf(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()));
			} else {
				dadosCurso.setSemCodigoCursoEMEC(getCursoInformacoesTramitacaoEMEC(expedicaoDiplomaVO.getMatricula().getCurso()));
			}
			dadosCurso.setModalidade(expedicaoDiplomaVO.getMatricula().getCurso().getModalidadeCurso().equals(ModalidadeDisciplinaEnum.PRESENCIAL) ? TModalidadeCurso.PRESENCIAL.value() : TModalidadeCurso.EAD.value());
			dadosCurso.setTituloConferido(getTituloConferido(expedicaoDiplomaVO.getMatricula().getCurso().getTitulo()));
			dadosCurso.setGrauConferido(getGrauConferido(getStringXml(expedicaoDiplomaVO.getMatricula().getCurso().getTitulo())));
			preecherDadosHabilitacaoDecisaoJudicialCurso(expedicaoDiplomaVO, dadosCurso);
			preencherDadosEnderecoCursoDecisaoJudicial(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino(), dadosCurso, consistirException);
			preencherDadosAutorizacaoCursoDecisaoJudicial(expedicaoDiplomaVO.getMatricula().getCurso(), dadosCurso, consistirException);
			if (dadosCurso.getAutorizacao() != null && Uteis.isAtributoPreenchido(dadosCurso.getAutorizacao().getNumero())) {
				preencherDadosReconhecimentoCursoPorDecisaoJudicial(expedicaoDiplomaVO, dadosCurso, consistirException);
			}
			dadosDiploma.setDadosCurso(dadosCurso);
		} catch (Exception e) {
			Uteis.checkStateList(Boolean.TRUE, "Erro ao preencher Dados do Curso (" + e.getMessage() + ")", consistirException);
		}
	}

	private void preencherDadosIesEmissoraPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiplomaPorDecisaoJudicial dadosDiploma, UsuarioVO usuarioVO, ConsistirException consistirException) throws Exception {
		dadosDiploma.setIesEmissora(preencherDadosIesEmissora(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora(), expedicaoDiplomaVO.getMatricula().getCurso(), usuarioVO, consistirException));
	}

	private void preencherDadosDeclaracoesEmissoraAcercaProcessoDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiplomaPorDecisaoJudicial dadosDiploma) {
		if (!expedicaoDiplomaVO.getDeclaracaoAcercaProcessoJudicialVOs().isEmpty()) {
			TDeclaracoesAcercaProcesso declaracoesAcercaProcesso = new TDeclaracoesAcercaProcesso();
			declaracoesAcercaProcesso.getDeclaracao().addAll(expedicaoDiplomaVO.getDeclaracaoAcercaProcessoJudicialVOs().stream().filter(d -> Uteis.isAtributoPreenchido(d.getDeclaracao())).flatMap(declaracao -> criarDadosDeclaracaoAcercaProcesso(declaracao).stream()).collect(Collectors.toList()));
			dadosDiploma.setDeclaracoesEmissoraAcercaProcesso(declaracoesAcercaProcesso);
		}
	}

	private List<String> criarDadosDeclaracaoAcercaProcesso(DeclaracaoAcercaProcessoJudicialVO obj) {
		List<String> declaracoes = new ArrayList<String>();
		if (Uteis.isAtributoPreenchido(obj.getDeclaracao())) {
			declaracoes.add(getStringXml(obj.getDeclaracao().trim()));
		}
		return declaracoes;
	}

	private void preencherDadosRegistroPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, String nonce, TInfDiploma infDiploma, ConsistirException consistirException) throws Exception {
		TDadosRegistroPorDecisaoJudicial dadosRegistro = new TDadosRegistroPorDecisaoJudicial();
		String id = "RDip" + nonce;
		expedicaoDiplomaVO.setIdDadosRegistrosDiplomaDigital(id);
		dadosRegistro.setId(id);
		dadosRegistro.setIdDocumentacaoAcademica("ReqDip" + nonce);
		preencherDadosIesRegistradoraDecisaoJudicial(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora(), expedicaoDiplomaVO.getMatricula().getCurso(), usuarioVO, dadosRegistro, consistirException);
		preencherDadosLivroRegistroDiplomaPorDecisaoJudicial(expedicaoDiplomaVO, usuarioVO, dadosRegistro, consistirException);
		preencherDadosInformacoesProcessoJudicial(dadosRegistro, expedicaoDiplomaVO, consistirException);
		preencherDadosSeguranca(dadosRegistro, expedicaoDiplomaVO, usuarioVO);
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getInformacoesAdicionaisDecisaoJudicial())) {
			dadosRegistro.setInformacoesAdicionais(getStringXml(expedicaoDiplomaVO.getInformacoesAdicionaisDecisaoJudicial().trim()));
		}
		infDiploma.setDadosRegistroPorDecisaoJudicial(dadosRegistro);
	}

	private void preencherDadosLivroRegistroDiplomaPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, TDadosRegistroPorDecisaoJudicial dadosRegistro, ConsistirException consistirException) throws Exception {
		validarDadosLivroRegistroDiploma(expedicaoDiplomaVO, usuarioVO, consistirException);
		ObjectFactory factory = new ObjectFactory();
		TLivroRegistroNSF livroRegistro = new TLivroRegistroNSF();
		ControleLivroRegistroDiplomaVO livroRegistroDiplomaVO = new ControleLivroRegistroDiplomaVO();
		ControleLivroFolhaReciboVO livroFolhaReciboVO = new ControleLivroFolhaReciboVO();
		List<JAXBElement<?>> elements = new ArrayList<JAXBElement<?>>();
		if (expedicaoDiplomaVO.getInformarCamposLivroRegistradora()) {
			elements.add(factory.createTLivroRegistroNSFLivroRegistro(Uteis.isAtributoPreenchido(Uteis.formatarStringXML(expedicaoDiplomaVO.getLivroRegistradora())) ? Uteis.formatarStringXML(expedicaoDiplomaVO.getLivroRegistradora()) : ""));
			elements.add(factory.createTLivroRegistroNSFNumeroFolhaDoDiploma(Uteis.isAtributoPreenchido(Uteis.formatarStringXML(expedicaoDiplomaVO.getFolhaReciboRegistradora())) ? Uteis.formatarStringXML(expedicaoDiplomaVO.getFolhaReciboRegistradora()) : null));
			if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFolhaReciboRegistradora())) {
				elements.add(factory.createTLivroRegistroNSFNumeroRegistro(Uteis.isAtributoPreenchido(Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroRegistroDiploma())) ? Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroRegistroDiploma()) : ""));
			} else {
				elements.add(factory.createTLivroRegistroNSFNumeroSequenciaDoDiploma(Uteis.isAtributoPreenchido(Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroRegistroDiploma())) ? Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroRegistroDiploma()) : ""));
			}
		} else {
			livroFolhaReciboVO = getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorMatriculaMatriculaPrimeiraEUltimaVia(expedicaoDiplomaVO.getMatricula().getMatricula(), expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), TipoLivroRegistroDiplomaEnum.DIPLOMA, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(livroFolhaReciboVO)) {
				elements.add(factory.createTLivroRegistroNSFLivroRegistro(Uteis.formatarStringXML(livroFolhaReciboVO.getControleLivroRegistroDiploma().getNrLivro().toString())));
				elements.add(factory.createTLivroRegistroNSFNumeroFolhaDoDiploma(Uteis.isAtributoPreenchido(Uteis.formatarStringXML(livroFolhaReciboVO.getFolhaReciboAtual().toString())) ? Uteis.formatarStringXML(livroFolhaReciboVO.getFolhaReciboAtual().toString()) : null));
				if (!Uteis.isAtributoPreenchido(livroFolhaReciboVO.getFolhaReciboAtual().toString().trim())) {
					elements.add(factory.createTLivroRegistroNSFNumeroRegistro(Uteis.isAtributoPreenchido(Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroRegistroDiploma())) ? Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroRegistroDiploma()) : ""));
				} else {
					elements.add(factory.createTLivroRegistroNSFNumeroSequenciaDoDiploma(Uteis.formatarStringXML(livroFolhaReciboVO.getNumeroRegistro().toString())));
				}
			} else {
				livroRegistroDiplomaVO = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorCodigoCurso(expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), null, TipoLivroRegistroDiplomaEnum.DIPLOMA, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
				if (Uteis.isAtributoPreenchido(livroRegistroDiplomaVO)) {
					elements.add(factory.createTLivroRegistroNSFLivroRegistro(Uteis.formatarStringXML(livroRegistroDiplomaVO.getNrLivro().toString())));
					elements.add(factory.createTLivroRegistroNSFNumeroFolhaDoDiploma(Uteis.formatarStringXML(livroRegistroDiplomaVO.getNrFolhaRecibo().toString())));
					elements.add(factory.createTLivroRegistroNSFNumeroSequenciaDoDiploma(Uteis.formatarStringXML(livroRegistroDiplomaVO.getNumeroRegistro().toString())));
				} else {
					elements.add(factory.createTLivroRegistroNSFLivroRegistro(Uteis.isAtributoPreenchido(Uteis.formatarStringXML(expedicaoDiplomaVO.getLivroRegistradora())) ? Uteis.formatarStringXML(expedicaoDiplomaVO.getLivroRegistradora()) : ""));
					elements.add(factory.createTLivroRegistroNSFNumeroFolhaDoDiploma(Uteis.isAtributoPreenchido(Uteis.formatarStringXML(expedicaoDiplomaVO.getFolhaReciboRegistradora())) ? Uteis.formatarStringXML(expedicaoDiplomaVO.getFolhaReciboRegistradora()) : ""));
					elements.add(factory.createTLivroRegistroNSFNumeroSequenciaDoDiploma(Uteis.isAtributoPreenchido(Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroRegistroDiploma())) ? Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroRegistroDiploma()) : ""));
				}
			}
		}
		elements.add(factory.createTLivroRegistroNSFProcessoDoDiploma(Uteis.formatarStringXML(expedicaoDiplomaVO.getNumeroProcesso())));
		elements.add(factory.createTLivroRegistroNSFDataColacaoGrau(getDataConvertidaXML(expedicaoDiplomaVO.getMatricula().getDataColacaoGrau())));
		elements.add(factory.createTLivroRegistroNSFDataExpedicaoDiploma(getDataConvertidaXML(expedicaoDiplomaVO.getDataExpedicao())));
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getDataRegistroDiploma())) {
			elements.add(factory.createTLivroRegistroNSFDataRegistroDiploma(getDataConvertidaXML(expedicaoDiplomaVO.getDataRegistroDiploma())));
		} else {
			elements.add(factory.createTLivroRegistroNSFDataRegistroDiploma(null));
		}
		TLivroRegistroNSF.ResponsavelRegistro responsavelRegistro = new TLivroRegistroNSF.ResponsavelRegistro();
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFuncionarioQuartoVO().getCodigo())) {
			responsavelRegistro.setNome(Uteis.formatarStringXML(expedicaoDiplomaVO.getFuncionarioQuartoVO().getPessoa().getNome()));
			responsavelRegistro.setCPF(Uteis.removerMascara(Uteis.formatarStringXML(expedicaoDiplomaVO.getFuncionarioQuartoVO().getPessoa().getCPF())));
			responsavelRegistro.setIDouNumeroMatricula(Uteis.formatarStringXML(expedicaoDiplomaVO.getFuncionarioQuartoVO().getMatricula()));
		}
		elements.add(factory.createTLivroRegistroNSFResponsavelRegistro(responsavelRegistro));
		livroRegistro.getContent().addAll(elements);
		dadosRegistro.setLivroRegistro(livroRegistro);
	}

	private void preencherDadosInformacoesProcessoJudicial(TDadosRegistroPorDecisaoJudicial dadosRegistro, ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce) {
		dadosRegistro.setInformacoesProcessoJudicial(preencherDadosInformacoesProcessoJudicial(expedicaoDiplomaVO, ce));
	}

	private TInformacoesProcessoJudicial preencherDadosInformacoesProcessoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce) {
		validarDadosInformacoesProcessoJudicial(expedicaoDiplomaVO, ce);
		TInformacoesProcessoJudicial informacoesProcessoJudicial = new TInformacoesProcessoJudicial();
		informacoesProcessoJudicial.setNumeroProcessoJudicial(expedicaoDiplomaVO.getNumeroProcessoDecisaoJudicial());
		informacoesProcessoJudicial.setNomeJuiz(getStringXml(expedicaoDiplomaVO.getNomeJuizDecisaoJudicial()));
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getDecisaoJudicial())) {
			informacoesProcessoJudicial.setDecisao(getStringXml(expedicaoDiplomaVO.getDecisaoJudicial()));
		}
		return informacoesProcessoJudicial;
	}

	private void validarDadosInformacoesProcessoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce) {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroProcessoDecisaoJudicial()), "O NÚMERO PROCESSO da decisão judicial deve ser informado.", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNomeJuizDecisaoJudicial()), "O JUIZ da decisão judicial deve ser informado.", ce);
	}

	private void preencherDadosPrivadosDiplomadoDocumentacao(ExpedicaoDiplomaVO expedicaoDiplomaVO, TRegistroReq registroReq, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		TDadosPrivadosDiplomado dadosPrivadosDiplomado = new TDadosPrivadosDiplomado();
		dadosPrivadosDiplomado.setFiliacao(criarDadosPrivadosDiplomadoFiliacaoDocumentacao(expedicaoDiplomaVO));
		preencherDadosHistoricoEscolarDocumentacao(expedicaoDiplomaVO, usuarioVO, dadosPrivadosDiplomado, ce);
		registroReq.setDadosPrivadosDiplomado(dadosPrivadosDiplomado);
	}

	private TDocumentacaoComprobatoria preencherDocumentacaoComprobatoriaDocumentacao(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculaPorMatriculaAluno(expedicaoDiplomaVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, Boolean.TRUE, Boolean.FALSE, usuarioVO);
		documetacaoMatriculaVOs.addAll(getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarPorMatriculaParaDocumentacaoXmlDiploma(expedicaoDiplomaVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		TDocumentacaoComprobatoria documentacaoComprobatoria = new TDocumentacaoComprobatoria();
		if (Uteis.isAtributoPreenchido(documetacaoMatriculaVOs)) {
			documentacaoComprobatoria.getDocumento().addAll(documetacaoMatriculaVOs.stream().filter(documetacaoMatriculaVO -> Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVO()) || Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOAssinado()) || Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoGED())).flatMap(documetacaoMatriculaVO -> criarDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO, expedicaoDiplomaVO.getConfiguracaoDiplomaDigital(), expedicaoDiplomaVO.getEmitidoPorDecisaoJudicial(), config, ce).stream()).collect(Collectors.toList()));
		}
		Uteis.checkStateList(Objects.isNull(documentacaoComprobatoria) ||  !Uteis.isAtributoPreenchido(documentacaoComprobatoria.getDocumento()), "A DOCUMENTAÇÃO DE MATRÍCULA do aluno deve ser informado (Dados da Documentação Comprobatória)", ce);
		return documentacaoComprobatoria;
	}

	private TTermoResponsabilidade preencherTermoResponsabilidadeDocumentacao(ExpedicaoDiplomaVO expedicaoDiplomaVO) {
		TTermoResponsabilidade termoResponsabilidade = new TTermoResponsabilidade();
		termoResponsabilidade.setNome(getStringXml(expedicaoDiplomaVO.getFuncionarioPrimarioVO().getPessoa().getNome()));
		termoResponsabilidade.setCPF(Uteis.removerMascara(getStringXml(expedicaoDiplomaVO.getFuncionarioPrimarioVO().getPessoa().getCPF())));
		termoResponsabilidade.setCargo(getStringXml(expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO().getNome()));
		return termoResponsabilidade;
	}

	private void preencherDadosPrivadosDiplomadoSegundaViaDocumentacao(ExpedicaoDiplomaVO expedicaoDiplomaVO, TRegistroSegundaViaReq registroSegundaViaReq, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		TDadosPrivadosDiplomadoSegundaVia dadosPrivadosDiplomadoSegundaVia = new TDadosPrivadosDiplomadoSegundaVia();
		dadosPrivadosDiplomadoSegundaVia.setFiliacao(criarDadosPrivadosDiplomadoFiliacaoDocumentacao(expedicaoDiplomaVO));
		preencherHistoricoEscolarDocumentacaoSegundaVia(expedicaoDiplomaVO, usuarioVO, dadosPrivadosDiplomadoSegundaVia, ce);
		registroSegundaViaReq.setDadosPrivadosDiplomado(dadosPrivadosDiplomadoSegundaVia);
	}

	private void preencherDocumentacaoComprobatoriaSegundaViaDocumentacao(ExpedicaoDiplomaVO expedicaoDiplomaVO, TRegistroSegundaViaReq registroSegundaViaReq, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculaPorMatriculaAluno(expedicaoDiplomaVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, Boolean.FALSE, usuarioVO);
		documetacaoMatriculaVOs.addAll(getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarPorMatriculaParaDocumentacaoXmlDiploma(expedicaoDiplomaVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		TDocumentacaoComprobatoria documentacaoComprobatoria = new TDocumentacaoComprobatoria();
		if (Uteis.isAtributoPreenchido(documetacaoMatriculaVOs)) {
			documentacaoComprobatoria.getDocumento().addAll(documetacaoMatriculaVOs.stream().filter(documetacaoMatriculaVO -> Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVO()) || Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOAssinado()) || Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoGED())).flatMap(documetacaoMatriculaVO -> criarDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO, expedicaoDiplomaVO.getConfiguracaoDiplomaDigital(), expedicaoDiplomaVO.getEmitidoPorDecisaoJudicial(), config, ce).stream()).collect(Collectors.toList()));
		}
		Uteis.checkStateList(Objects.isNull(documentacaoComprobatoria) ||  !Uteis.isAtributoPreenchido(documentacaoComprobatoria.getDocumento()), "A DOCUMENTAÇÃO DE MATRÍCULA do aluno deve ser informado (Dados da Documentação Comprobatória)", ce);
		registroSegundaViaReq.setDocumentacaoComprobatoria(documentacaoComprobatoria);
	}

	private void preencherTermoResponsabilidadeSegundaViaDocumentacao(ExpedicaoDiplomaVO expedicaoDiplomaVO, TRegistroSegundaViaReq registroSegundaViaReq) {
		TTermoResponsabilidade termoResponsabilidade = new TTermoResponsabilidade();
		termoResponsabilidade.setNome(getStringXml(expedicaoDiplomaVO.getFuncionarioPrimarioVO().getPessoa().getNome()));
		termoResponsabilidade.setCPF(getCpfXml(expedicaoDiplomaVO.getFuncionarioPrimarioVO().getPessoa().getCPF()));
		termoResponsabilidade.setCargo(getStringXml(expedicaoDiplomaVO.getCargoFuncionarioPrincipalVO().getNome()));
		registroSegundaViaReq.setTermoResponsabilidadeEmissora(termoResponsabilidade);
	}

	private void criarDadosDocumentacaoAcademicaDiplomaPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, String nonce, ConsistirException consistirException, TRegistroPorDecisaoJudicialReq registroPorDecisaoJudicialReq) throws Exception {
		registroPorDecisaoJudicialReq.setDadosDiplomaPorDecisaoJudicial(criarDadosDiplomaPorDecisaoJudicial(expedicaoDiplomaVO, usuarioVO, nonce, consistirException));
	}

	private void preencherDadosPrivadosDiplomadoDocumentacaoPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, TRegistroPorDecisaoJudicialReq registroReq, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		TDadosPrivadosDiplomadoPorDecisaoJudicial dadosPrivadosDiplomado = new TDadosPrivadosDiplomadoPorDecisaoJudicial();
		dadosPrivadosDiplomado.setFiliacao(criarDadosPrivadosDiplomadoFiliacaoDocumentacao(expedicaoDiplomaVO));
		preencherHistoricoEscolarDocumentacaoSegundaVia(expedicaoDiplomaVO, usuarioVO, dadosPrivadosDiplomado, ce);;
		preencherDadosDocumentacaoAcademicaInformacoesProcessoJudicial(dadosPrivadosDiplomado, expedicaoDiplomaVO, ce);
		registroReq.setDadosPrivadosDiplomado(dadosPrivadosDiplomado);
	}

	private TDocumentacaoComprobatoriaPorDecisaoJudicial preencherDocumentacaoComprobatoriaDocumentacaoDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculaPorMatriculaAluno(expedicaoDiplomaVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, Boolean.TRUE, Boolean.FALSE, usuarioVO);
		documetacaoMatriculaVOs.addAll(getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarPorMatriculaParaDocumentacaoXmlDiploma(expedicaoDiplomaVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		TDocumentacaoComprobatoriaPorDecisaoJudicial documentacaoComprobatoria = new TDocumentacaoComprobatoriaPorDecisaoJudicial();
		documentacaoComprobatoria.getDocumentoOrDocumentoIndisponivel().addAll(documetacaoMatriculaVOs.stream().filter(documetacaoMatriculaVO -> Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVO()) || Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOAssinado()) || Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoGED())).flatMap(documetacaoMatriculaVO -> criarDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO, expedicaoDiplomaVO.getConfiguracaoDiplomaDigital(), true, config, ce).stream()).collect(Collectors.toList()));
		return Uteis.isAtributoPreenchido(documentacaoComprobatoria.getDocumentoOrDocumentoIndisponivel()) ? documentacaoComprobatoria : null;
	}

	private TDadosDiplomado preencherDadosDiplomadoGenerico(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException consistirException, UsuarioVO usuarioVO) throws Exception {
		TDadosDiplomado dadosDiplomado = new TDadosDiplomado();
		try {
			validarDadosDiplomado(expedicaoDiplomaVO.getMatricula().getAluno(), consistirException, usuarioVO);
			dadosDiplomado.setID(getStringXml(expedicaoDiplomaVO.getMatricula().getAluno().getCodigo().toString()));
			dadosDiplomado.setSexo(getSexoDiplomado(expedicaoDiplomaVO.getMatricula().getAluno().getSexo()));
			dadosDiplomado.setNacionalidade(getStringXml(expedicaoDiplomaVO.getMatricula().getAluno().getNacionalidade().getNacionalidade()));
			dadosDiplomado.setCPF(getCpfXml(expedicaoDiplomaVO.getMatricula().getAluno().getCPF()));
			dadosDiplomado.setDataNascimento(getDataConvertidaXML(expedicaoDiplomaVO.getMatricula().getAluno().getDataNasc()));
			dadosDiplomado.setNaturalidade(preencherDadosNaturalidadeDiplomado(expedicaoDiplomaVO.getMatricula().getAluno().getNaturalidade(), consistirException));
			preencherDadosNomeENomeSocialDiplomado(expedicaoDiplomaVO, dadosDiplomado);
			preencherDadosRgDiplomado(expedicaoDiplomaVO, dadosDiplomado);
		} catch (Exception e) {
			consistirException.getListaMensagemErro().add(e.getMessage());
		}
		return dadosDiplomado;
	}

	private TSexo getSexoDiplomado(String sexo) {
		if (Uteis.isAtributoPreenchido(sexo)) {
			return TSexo.valueOf(sexo);
		} else {
			return null;
		}
	}

	private void preencherDadosNomeENomeSocialDiplomado(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiplomado dadosDiplomado) {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getAluno().getNomeBatismo())) {
			dadosDiplomado.setNome(getStringXml(expedicaoDiplomaVO.getMatricula().getAluno().getNomeBatismo()));
			if (!Objects.equals(expedicaoDiplomaVO.getMatricula().getAluno().getNome().trim(), expedicaoDiplomaVO.getMatricula().getAluno().getNomeBatismo().trim())) {
				dadosDiplomado.setNomeSocial(getStringXml(expedicaoDiplomaVO.getMatricula().getAluno().getNome()));
			}
		} else {
			dadosDiplomado.setNome(getStringXml(expedicaoDiplomaVO.getMatricula().getAluno().getNome()));
		}
	}
	
	public void validarDadosMinimoCurso(CursoVO cursoVO, UnidadeEnsinoVO unidadeEnsino, ConsistirException ce) throws Exception {
		if (!Uteis.isAtributoPreenchido(cursoVO.getNomeDocumentacao().trim())) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getNome().trim()), "O NOME do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ce);
		}
		if (cursoVO.getPossuiCodigoEMEC()) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getCodigoEMEC()), "O CÓDIGO EMEC do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ce);
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getNumeroProcessoEMEC()), "O NÚMERO PROCESSO EMEC do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getTipoProcessoEMEC().trim()), "O TIPO PROCESSO EMEC do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataCadastroEMEC()), "A DATA de CADASTRO EMEC do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataProtocoloEMEC()), "A DATA do PROTOCOLO EMEC do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ce);
		}
		if (Uteis.isAtributoPreenchido(cursoVO.getHabilitacao())) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataHabilitacao()), "A DATA da HABILITAÇÃO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ce);
		}
	}

	private void preencherDadosMinimoCurso(ExpedicaoDiplomaVO expedicaoDiplomaVO, TInfHistoricoEscolar infHistoricoEscolar, ConsistirException consistirException) throws Exception {
		validarDadosMinimoCurso(expedicaoDiplomaVO.getMatricula().getCurso(), expedicaoDiplomaVO.getMatricula().getUnidadeEnsino(), consistirException);
		TDadosMinimoCurso dadosCurso = new TDadosMinimoCurso();
		dadosCurso.setNomeCurso(getNomeCurso(expedicaoDiplomaVO.getMatricula().getCurso()));
		if (expedicaoDiplomaVO.getMatricula().getCurso().getPossuiCodigoEMEC()) {
			dadosCurso.setCodigoCursoEMEC(Long.valueOf(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()));
		} else {
			dadosCurso.setSemCodigoCursoEMEC(getCursoInformacoesTramitacaoEMEC(expedicaoDiplomaVO.getMatricula().getCurso()));
		}
		preecherDadosHabilitacaoMinimoCurso(expedicaoDiplomaVO, dadosCurso);
		preencherDadosAutorizacaoMinimoCurso(expedicaoDiplomaVO, dadosCurso, consistirException);
		preencherDadosReconhecimentoMinimoCurso(expedicaoDiplomaVO, dadosCurso, consistirException);
		infHistoricoEscolar.setDadosCurso(dadosCurso);
	}

	private TInformacoesTramitacaoEMEC getCursoInformacoesTramitacaoEMEC(CursoVO cursoVO) throws Exception {
		TInformacoesTramitacaoEMEC informacoesTramitacaoEMEC = new TInformacoesTramitacaoEMEC();
		informacoesTramitacaoEMEC.setNumeroProcesso(Long.valueOf(cursoVO.getNumeroProcessoEMEC()));
		informacoesTramitacaoEMEC.setTipoProcesso(cursoVO.getTipoProcessoEMEC().trim());
		informacoesTramitacaoEMEC.setDataCadastro(getDataConvertidaXML(cursoVO.getDataCadastroEMEC()));
		informacoesTramitacaoEMEC.setDataProtocolo(getDataConvertidaXML(cursoVO.getDataProtocoloEMEC()));
		return informacoesTramitacaoEMEC;
	}

	private void preencherDadosAutorizacaoMinimoCurso(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosMinimoCurso tDadosMinimoCurso, ConsistirException consistirException) throws Exception {
		tDadosMinimoCurso.setAutorizacao(preencherDadosAutorizacaoCurso(expedicaoDiplomaVO.getMatricula().getCurso(), consistirException));
	}

	private TAtoRegulatorioComOuSemEMEC preencherDadosAutorizacaoCurso(CursoVO cursoVO, ConsistirException consistirException) throws Exception {
		TAtoRegulatorioComOuSemEMEC atoRegulatorio = new TAtoRegulatorioComOuSemEMEC();
		if (cursoVO.getAutorizacaoResolucaoEmTramitacao()) {
			atoRegulatorio.setInformacoesTramitacaoEMEC(new TInformacoesTramitacaoEMEC());
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getNumeroProcessoAutorizacaoResolucao()), "O NÚMERO do PROCESSO da AUTORIZAÇÃO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getTipoProcessoAutorizacaoResolucao()), "O TIPO DE PROCESSO da AUTORIZAÇÃO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataCadastroAutorizacaoResolucao()), "A DATA DE CADASTRO da AUTORIZAÇÃO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataProtocoloAutorizacaoResolucao()), "A DATA do PROTOCOLO da AUTORIZAÇÃO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), consistirException);
			atoRegulatorio.getInformacoesTramitacaoEMEC().setNumeroProcesso(Long.valueOf(Uteis.removeCaractersEspeciais2(getStringXml(cursoVO.getNumeroProcessoAutorizacaoResolucao()))));
			atoRegulatorio.getInformacoesTramitacaoEMEC().setTipoProcesso(getStringXml(cursoVO.getTipoProcessoAutorizacaoResolucao()));
			atoRegulatorio.getInformacoesTramitacaoEMEC().setDataCadastro(getDataConvertidaXML(cursoVO.getDataCadastroAutorizacaoResolucao()));
			atoRegulatorio.getInformacoesTramitacaoEMEC().setDataProtocolo(getDataConvertidaXML(cursoVO.getDataProtocoloAutorizacaoResolucao()));
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getTipoAutorizacaoCursoEnum()), "O TIPO DE AUTORIZAÇÃO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getNumeroAutorizacao()), "O NÚMERO da AUTORIZAÇÃO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataCredenciamento()), "A DATA do CREDENCIAMENTO da AUTORIZAÇÃO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), consistirException);
			atoRegulatorio.setTipo(Uteis.isAtributoPreenchido(cursoVO.getTipoAutorizacaoCursoEnum()) ? cursoVO.getTipoAutorizacaoCursoEnum().getTipoAtoDiploma() : null);
			atoRegulatorio.setNumero(Uteis.removeCaractersEspeciais2(getStringXml(cursoVO.getNumeroAutorizacao())));
			atoRegulatorio.setData(getDataConvertidaXML(cursoVO.getDataCredenciamento()));
			if (Uteis.isAtributoPreenchido(cursoVO.getDataPublicacaoDO())) {
				atoRegulatorio.setDataPublicacao(getDataConvertidaXML(cursoVO.getDataPublicacaoDO()));
			}
			if (Uteis.isAtributoPreenchido(cursoVO.getVeiculoPublicacao())) {
				atoRegulatorio.setVeiculoPublicacao(getStringXml(cursoVO.getVeiculoPublicacao()));
			}
			if (Uteis.isAtributoPreenchido(cursoVO.getSecaoPublicacao())) {
				atoRegulatorio.setSecaoPublicacao(Long.valueOf(cursoVO.getSecaoPublicacao()));
			}
			if (Uteis.isAtributoPreenchido(cursoVO.getPaginaPublicacao())) {
				atoRegulatorio.setPaginaPublicacao(Long.valueOf(cursoVO.getPaginaPublicacao()));
			}
			if (Uteis.isAtributoPreenchido(cursoVO.getNumeroDOU())) {
				atoRegulatorio.setNumeroDOU(Long.valueOf(cursoVO.getNumeroDOU()));
			}
		}
		return atoRegulatorio;
	}

	private void preencherDadosIesEmissoraHistoricoDigital(ExpedicaoDiplomaVO expedicaoDiplomaVO, TInfHistoricoEscolar infHistoricoEscolar, UsuarioVO usuarioVO, ConsistirException consistirException) throws Exception {
		infHistoricoEscolar.setIesEmissora(preencherDadosIesEmissora(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora(), expedicaoDiplomaVO.getMatricula().getCurso(), usuarioVO, consistirException));
	}

	private void preencherHistoricoEscolarHistoricoEscolar(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, TInfHistoricoEscolar infHistoricoEscolar, HistoricoAlunoRelVO histAlunoRelVO, ConsistirException ce) throws Exception {
		if (histAlunoRelVO == null) {
			FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
			filtroRelatorioAcademicoVO.realizarMarcarSituacoesConfiguracaoDiploma(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital());
			histAlunoRelVO = getFacadeFactory().getHistoricoAlunoRelFacade().criarObjeto(histAlunoRelVO, expedicaoDiplomaVO.getMatricula(), expedicaoDiplomaVO.getGradeCurricularVO(), filtroRelatorioAcademicoVO, 1, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, expedicaoDiplomaVO.getLayoutDiploma(), Boolean.FALSE, expedicaoDiplomaVO.getDataExpedicao(), Boolean.FALSE, Boolean.FALSE, Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital()) ? expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getHistoricoConsiderarForaGrade() : Boolean.TRUE, usuarioVO, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Constantes.EMPTY, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "PROFESSOR_APROVEITAMENTO_DISCIPLINA", null, null, Boolean.TRUE);
		}
		infHistoricoEscolar.setHistoricoEscolar(preencherDadosHistoricoEscolar(expedicaoDiplomaVO, histAlunoRelVO, usuarioVO, ce));
	}
	
	public String realizarGeracaoHashCodigoValidacaoHistoricoDigital(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			String hashCodigoValidacao = Constantes.EMPTY;
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getMatricula()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getAluno().getCPF().trim()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCNPJ().trim())) {
				hashCodigoValidacao = Uteis.removerMascara(expedicaoDiplomaVO.getMatricula().getMatricula() + expedicaoDiplomaVO.getMatricula().getAluno().getCPF().trim()) + Long.valueOf(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()) + Uteis.removerMascara(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCNPJ().trim() + UteisData.getDataAplicandoFormatacao(new Date(), "ddMMyyyyHHmm"));
				hashCodigoValidacao = DigestUtils.sha256Hex(hashCodigoValidacao).substring(0, 12);
				if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIES())) {
					hashCodigoValidacao = StringUtils.join(Arrays.asList(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIES(), ".", hashCodigoValidacao).toArray());
				}
			}
			return hashCodigoValidacao;
		} catch (Exception e) {
			throw e;
		}
	}

	private void preencherDadosDiplomado(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiploma dadosDiploma, ConsistirException consistirException, UsuarioVO usuarioVO) throws Exception {
		dadosDiploma.setDiplomado(preencherDadosDiplomadoGenerico(expedicaoDiplomaVO, consistirException, usuarioVO));
	}

	private void preencherDadosCurso(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiploma dadosDiploma, ConsistirException consistirException) throws Exception {
		validarDadosCurso(expedicaoDiplomaVO.getMatricula().getCurso(), expedicaoDiplomaVO.getMatricula().getUnidadeEnsino(), consistirException);
		try {
			TDadosCurso dadosCurso = new TDadosCurso();
			dadosCurso.setNomeCurso(getNomeCurso(expedicaoDiplomaVO.getMatricula().getCurso()));
			dadosCurso.setModalidade(getModalidadeCurso(expedicaoDiplomaVO.getMatricula().getCurso().getModalidadeCurso()));
			dadosCurso.setTituloConferido(getTituloConferido(expedicaoDiplomaVO.getMatricula().getCurso().getTitulo()));
			dadosCurso.setGrauConferido(getGrauConferido(getStringXml(expedicaoDiplomaVO.getMatricula().getCurso().getTitulo())));
			dadosCurso.setEnderecoCurso(getEnderecoIesEmissora(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino(), TipoOrigemMontagemDados.CURSO, consistirException));
			dadosCurso.setAutorizacao(preencherDadosAutorizacaoCurso(expedicaoDiplomaVO.getMatricula().getCurso(), consistirException));
			preencherDadosCodigoCursoEMEC(expedicaoDiplomaVO, dadosCurso);
			preencherDadosReconhecimentoCurso(expedicaoDiplomaVO, dadosCurso, consistirException);
			preecherDadosHabilitacaoCurso(expedicaoDiplomaVO, dadosCurso);
			dadosDiploma.setDadosCurso(dadosCurso);
		} catch (Exception e) {
			consistirException.getListaMensagemErro().add(e.getMessage());
		}
	}
	
	private String getNomeCurso(CursoVO curso) {
		if (Uteis.isAtributoPreenchido(curso.getNomeDocumentacao())) {
			return getStringXml(curso.getNomeDocumentacao());
		} else {
			return getStringXml(curso.getNome());
		}
	}
	
	private TModalidadeCurso getModalidadeCurso(ModalidadeDisciplinaEnum modalidadeDisciplina) {
		if (Objects.nonNull(modalidadeDisciplina)) {
			if (modalidadeDisciplina.equals(ModalidadeDisciplinaEnum.ON_LINE)) {
				return TModalidadeCurso.EAD;
			} else {
				return TModalidadeCurso.PRESENCIAL;
			}
		} else {
			return null;
		}
	}
	
	private void preencherDadosCodigoCursoEMEC(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosCurso dadosCurso) throws Exception {
		if (expedicaoDiplomaVO.getMatricula().getCurso().getPossuiCodigoEMEC()) {
			dadosCurso.setCodigoCursoEMEC(Long.valueOf(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()));
		} else {
			dadosCurso.setSemCodigoCursoEMEC(getCursoInformacoesTramitacaoEMEC(expedicaoDiplomaVO.getMatricula().getCurso()));
		}
	}
	
	private TTituloConferido getTituloConferido(String titulo) {
		TTituloConferido tituloConferido = new TTituloConferido();
		tituloConferido.setTitulo(getTitulo(getStringXml(titulo)));
		return tituloConferido;
	}

	private void preencherDadosIesEmissora(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiploma dadosDiploma, UsuarioVO usuarioVO, ConsistirException consistirException) throws Exception {
		dadosDiploma.setIesEmissora(preencherDadosIesEmissora(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora(), expedicaoDiplomaVO.getMatricula().getCurso(), usuarioVO, consistirException));
	}

	private void preencherDadosIesOriginalCursoPTADiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiploma dadosDiploma, ConsistirException consistirException, UsuarioVO usuarioVO) throws Exception {
		dadosDiploma.setDadosIesOriginalCursoPTA(preencherDadosIesOriginalCursoPTA(expedicaoDiplomaVO, consistirException, usuarioVO));
	}
	
	private void preencherDadosIesOriginalCursoPTADiplomaPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiplomaPorDecisaoJudicial dadosDiplomaPorDecisaoJudicial, ConsistirException consistirException, UsuarioVO usuarioVO) throws Exception {
		dadosDiplomaPorDecisaoJudicial.setDadosIesOriginalCursoPTA(preencherDadosIesOriginalCursoPTA(expedicaoDiplomaVO, consistirException, usuarioVO));
	}
	
	private TDadosIesOriginalCursoPTA preencherDadosIesOriginalCursoPTA(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException consistirException, UsuarioVO usuarioVO) throws Exception {
		if (expedicaoDiplomaVO.getEmitidoPorProcessoTransferenciaAssistida()) {
			TDadosIesOriginalCursoPTA dadosIesOriginalCursoPTA = new TDadosIesOriginalCursoPTA();
			try {
				validarDadosIesOriginalCursoPTA(expedicaoDiplomaVO, consistirException, usuarioVO);
				dadosIesOriginalCursoPTA.setNome(getStringXml(expedicaoDiplomaVO.getNomeIesPTA()));
				dadosIesOriginalCursoPTA.setCNPJ(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCnpjPTA()) ? getCnpjXml(expedicaoDiplomaVO.getCnpjPTA()) : null);
				preencherDadosCodigoEmecIesOriginalCursoPTA(expedicaoDiplomaVO, dadosIesOriginalCursoPTA);
				preencherDadosEnderecoIesOriginalCursoPTA(expedicaoDiplomaVO, dadosIesOriginalCursoPTA);
				preencherDadosDescredenciamentoIesOriginalCursoPTA(expedicaoDiplomaVO, dadosIesOriginalCursoPTA, consistirException);
				return dadosIesOriginalCursoPTA;
			} catch (Exception e) {
				consistirException.getListaMensagemErro().add(e.getMessage());
			}
		}
		return null;
	}
	
	private void preencherDadosCodigoEmecIesOriginalCursoPTA(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosIesOriginalCursoPTA dadosIesOriginalCursoPTA) {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigoMecPTA())) {
			dadosIesOriginalCursoPTA.setCodigoMEC(Long.valueOf(expedicaoDiplomaVO.getCodigoMecPTA()));
		} else {
			dadosIesOriginalCursoPTA.setCodigoMECIndisponivel(new TVazio());
		}
	}

	private XMLGregorianCalendar getDataConvertidaXML(Date date) throws Exception {
		if (!Uteis.isAtributoPreenchido(date)) {
			return null;
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		XMLGregorianCalendar calendarXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		calendarXml.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		return calendarXml;
	}

	public void validarDadosDiplomado(PessoaVO aluno, ConsistirException ce, UsuarioVO usuario) throws Exception {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(aluno.getSexo()), "O SEXO do ALUNO deve ser informado (Dados do Diplomado)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(aluno.getNacionalidade().getNacionalidade()), "A NACIONALIDADE do ALUNO deve ser informado (Dados do Diplomado)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(aluno.getNaturalidade()), "A NATURALIDADE do ALUNO deve ser informado (Dados do Diplomado)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(aluno.getCPF()), "O CPF do ALUNO deve ser informado (Dados do Diplomado)", ce);
		Uteis.checkStateList(!Uteis.validaCPF(getCpfXml(aluno.getCPF())), "O CPF do ALUNO deve ser válido (Dados do Diplomado)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(aluno.getRG()), "O RG do ALUNO deve ser informado (Dados do Diplomado)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(aluno.getEstadoEmissaoRG()), "A UF da EMISSÃO do RG do ALUNO deve ser informado (Dados do Diplomado)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(aluno.getDataNasc()), "A DATA DE NASCIMENTO do ALUNO deve ser informado (Dados do Diplomado)", ce);
	}
	
	private void preencherDadosNaturalidadeDiplomadoDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiplomadoPorDecisaoJudicial dadosDiplomadoDecisaoJudicial, ConsistirException ce) throws Exception {
		TNaturalidade naturalidade = preencherDadosNaturalidadeDiplomado(expedicaoDiplomaVO.getMatricula().getAluno().getNaturalidade(), ce);
		if (naturalidade != null && ((Uteis.isAtributoPreenchido(naturalidade.getCodigoMunicipio())) || (!Uteis.isAtributoPreenchido(naturalidade.getCodigoMunicipio()) && Uteis.isAtributoPreenchido(naturalidade.getNomeMunicipioEstrangeiro())))) {
			dadosDiplomadoDecisaoJudicial.setNaturalidade(naturalidade);
		} else {
			dadosDiplomadoDecisaoJudicial.setNaturalidadeIndisponivel(new TVazio());
		}
	}
	
	private TNaturalidade preencherDadosNaturalidadeDiplomado(CidadeVO cidadeVO, ConsistirException ce) throws Exception {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(cidadeVO.getNome().trim()), "O NOME da CIDADE da NATURALIDADE do ALUNO deve ser preenchido (Dados do Diplomado)", ce);
		TNaturalidade naturalidade = new TNaturalidade();
		if (Uteis.isAtributoPreenchido(cidadeVO.getEstado().getCodigoIBGE())) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cidadeVO.getCodigoIBGE().trim()), "O CÓDIGO IBGE da CIDADE da NATURALIDADE do ALUNO deve ser preenchido (Dados do Diplomado)", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cidadeVO.getEstado().getSigla().trim()), "A UF do ESTADO da NATURALIDADE do ALUNO deve ser preenchido (Dados do Diplomado)", ce);
			naturalidade.setCodigoMunicipio(getStringXml(cidadeVO.getCodigoIBGE()));
			naturalidade.setNomeMunicipio(getStringXml(cidadeVO.getNome()));
			naturalidade.setUF(Uteis.isAtributoPreenchido(cidadeVO.getEstado().getSigla()) ? TUf.valueOf(getStringXml(cidadeVO.getEstado().getSigla())) : null);
		} else {
			naturalidade.setCodigoMunicipio(null);
			naturalidade.setNomeMunicipio(null);
			naturalidade.setNomeMunicipioEstrangeiro(getStringXml(cidadeVO.getNome()));
		}
		return naturalidade;
	}
	
	private void preencherDadosRgDiplomado(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiplomado dadosDiplomado) {
		dadosDiplomado.setRG(preencherDadosRgDiplomado(expedicaoDiplomaVO.getMatricula().getAluno()));
	}
	
	private void preencherDadosRgDiplomadoDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosDiplomadoPorDecisaoJudicial dadosDiplomadoDecisaoJudicial) {
		dadosDiplomadoDecisaoJudicial.setRG(preencherDadosRgDiplomado(expedicaoDiplomaVO.getMatricula().getAluno()));
	}
	
	private TRg preencherDadosRgDiplomado(PessoaVO aluno) {
		TRg rg = new TRg();
		String numero = Uteis.removerMascara(getStringXml(aluno.getRG()));
		rg.setNumero(numero.contains(" ") ? numero.replaceAll(" ", Constantes.EMPTY) : numero);
		if (Uteis.isAtributoPreenchido(aluno.getOrgaoEmissor().trim())) {
			rg.setOrgaoExpedidor(getStringXml(aluno.getOrgaoEmissor()));
		}
		rg.setUF(Uteis.isAtributoPreenchido(getStringXml(aluno.getEstadoEmissaoRG())) ? TUf.valueOf(getStringXml(aluno.getEstadoEmissaoRG())) : null);
		return rg;
	}
	
	public void validarDadosCurso(CursoVO cursoVO, UnidadeEnsinoVO unidadeEnsino, ConsistirException ce) throws Exception {
		if (!Uteis.isAtributoPreenchido(cursoVO.getNomeDocumentacao())) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getNome()), "O NOME do CURSO deve ser informado (Dados do Curso)", ce);
		}
		if (cursoVO.getPossuiCodigoEMEC()) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getCodigoEMEC()), "O CÓDIGO EMEC do CURSO deve ser informado (Dados do Curso)", ce);
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getNumeroProcessoEMEC()), "O NÚMERO PROCESSO EMEC do CURSO deve ser informado (Dados do Curso)", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getTipoProcessoEMEC().trim()), "O TIPO PROCESSO EMEC do CURSO deve ser informado (Dados do Curso)", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataCadastroEMEC()), "A DATA DE CADASTRO EMEC do CURSO deve ser informado (Dados do Curso)", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataProtocoloEMEC()), "A DATA do PROTOCOLO EMEC do CURSO deve ser informado (Dados do Curso)", ce);
		}
		if (Uteis.isAtributoPreenchido(cursoVO.getHabilitacao())) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataHabilitacao()), "A DATA da HABILITAÇÃO do CURSO deve ser informado (Dados do Curso)", ce);
		}
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getModalidadeCurso()), "A MODALIDADE do CURSO deve ser informado (Dados do Curso)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getTitulacaoFemininoApresentarDiploma()) || !Uteis.isAtributoPreenchido(cursoVO.getTitulacaoMasculinoApresentarDiploma()), "AS TITULAÇÕES a ser exibida no diploma deve ser informada (Dados do Curso)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(getTitulo(cursoVO.getTitulo().trim())), "O TÍTULO do CURSO deve ser informado (Dados do Curso)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(getGrauConferido(cursoVO.getTitulo().trim())), "O título informado no curso não é compatível com um grau conferido usado pelo mec (Curso) (Dados do Curso)", ce);
	}

	private AutorizacaoCursoVO getAutorizacaoCursoVO(ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getAutorizacaoCurso().getCodigo())) {
			return getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(expedicaoDiplomaVO.getMatricula().getAutorizacaoCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		}
		return getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataMaisAntigo(expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), expedicaoDiplomaVO.getMatricula().getData(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	public TTitulo getTitulo(String titulo) {
		switch (titulo) {
		case "TE":
		case "TC":
			return TTitulo.TECNÓLOGO;
		case "BA":
			return TTitulo.BACHAREL;
		case "LI":
			return TTitulo.LICENCIADO;
		case "RM":
			return TTitulo.MÉDICO;
		default:
			return null;
		}
	}

	public TGrauConferido getGrauConferido(String titulo) {
		switch (titulo) {
		case "TE":
		case "TC":
			return TGrauConferido.TECNÓLOGO;
		case "BA":
			return TGrauConferido.BACHARELADO;
		case "LI":
			return TGrauConferido.LICENCIATURA;
		case "SE":
			return TGrauConferido.CURSO_SEQUENCIAL;
		default:
			return null;
		}
	}
	
	private void preencherDadosReconhecimentoMinimoCurso(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosMinimoCurso dadosMinimoCurso, ConsistirException ex) throws Exception {
		AutorizacaoCursoVO reconhecimentoCurso = getAutorizacaoCursoVO(expedicaoDiplomaVO);
		AutorizacaoCursoVO renovacaoReconhecimentoCurso = getRenovacaoReconhecimentoCursoVO(expedicaoDiplomaVO);
		if (Uteis.isAtributoPreenchido(reconhecimentoCurso)) {
			dadosMinimoCurso.setReconhecimento(preencherDadosReconhecimentoCurso(reconhecimentoCurso, ex));
			dadosMinimoCurso.setRenovacaoReconhecimento(preencherDadosRenovacaoReconhecimentoCurso(reconhecimentoCurso, renovacaoReconhecimentoCurso, ex));
		} else {
			Uteis.checkStateList(Boolean.TRUE, "O reconhecimento do curso deve ser informado (Dados do Curso)", ex);
		}
	}

	private TAtoRegulatorioComOuSemEMEC preencherDadosReconhecimentoCurso(AutorizacaoCursoVO reconhecimentoCurso, ConsistirException ex) throws Exception {
		TAtoRegulatorioComOuSemEMEC atoRegulatorio = new TAtoRegulatorioComOuSemEMEC();
		if (!Uteis.isAtributoPreenchido(reconhecimentoCurso)) {
			Uteis.checkStateList(Boolean.TRUE, "O Reconhecimento do Curso deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
		}
		if (reconhecimentoCurso.getEmTramitacao()) {
			TInformacoesTramitacaoEMEC informacoesTramitacaoEMEC = new TInformacoesTramitacaoEMEC();
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(reconhecimentoCurso.getTipoProcesso()), "O TIPO DE PROCESSO do RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(reconhecimentoCurso.getNumero()), "O NÚMERO do PROCESSO do RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
			Uteis.checkStateList(!Uteis.getIsValorNumerico(reconhecimentoCurso.getNumero()), "O NÚMERO do PROCESSO do RECONHECIMENTO do CURSO deve ser númerico " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(reconhecimentoCurso.getDataCadastro()), "A DATA DE CADASTRO do RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(reconhecimentoCurso.getDataProtocolo()), "A DATA do PROTOCOLO do RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
			informacoesTramitacaoEMEC.setTipoProcesso(getStringXml(reconhecimentoCurso.getTipoProcesso()));
			informacoesTramitacaoEMEC.setNumeroProcesso(Long.valueOf(Uteis.removeCaractersEspeciais2(getStringXml(reconhecimentoCurso.getNumero()))));
			informacoesTramitacaoEMEC.setDataCadastro(getDataConvertidaXML(reconhecimentoCurso.getDataCadastro()));
			informacoesTramitacaoEMEC.setDataProtocolo(getDataConvertidaXML(reconhecimentoCurso.getDataProtocolo()));
			atoRegulatorio.setInformacoesTramitacaoEMEC(informacoesTramitacaoEMEC);
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(reconhecimentoCurso.getTipoAutorizacaoCursoEnum()), "O TIPO DE RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(reconhecimentoCurso.getDataCredenciamento()), "A DATA do RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(reconhecimentoCurso.getNumero()), "O NÚMERO do RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
			Uteis.checkStateList(!Uteis.getIsValorNumerico(reconhecimentoCurso.getNumero()), "O NÚMERO do RECONHECIMENTO do CURSO deve ser númerico " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
			atoRegulatorio.setTipo(Uteis.isAtributoPreenchido(reconhecimentoCurso.getTipoAutorizacaoCursoEnum()) ? reconhecimentoCurso.getTipoAutorizacaoCursoEnum().getTipoAtoDiploma() : null);
			atoRegulatorio.setNumero(Uteis.removeCaractersEspeciais2(getStringXml(reconhecimentoCurso.getNumero())));
			atoRegulatorio.setData(getDataConvertidaXML(reconhecimentoCurso.getDataCredenciamento()));
			if (Uteis.isAtributoPreenchido(reconhecimentoCurso.getData())) {
				atoRegulatorio.setDataPublicacao(getDataConvertidaXML(reconhecimentoCurso.getData()));
			}
			if (Uteis.isAtributoPreenchido(reconhecimentoCurso.getVeiculoPublicacao())) {
				atoRegulatorio.setVeiculoPublicacao(getStringXml(reconhecimentoCurso.getVeiculoPublicacao()));
			}
			if (Uteis.isAtributoPreenchido(reconhecimentoCurso.getSecaoPublicacao())) {
				atoRegulatorio.setSecaoPublicacao(Long.valueOf(reconhecimentoCurso.getSecaoPublicacao()));
			}
			if (Uteis.isAtributoPreenchido(reconhecimentoCurso.getPaginaPublicacao())) {
				atoRegulatorio.setPaginaPublicacao(Long.valueOf(reconhecimentoCurso.getPaginaPublicacao()));
			}
			if (Uteis.isAtributoPreenchido(reconhecimentoCurso.getNumeroDOU())) {
				atoRegulatorio.setNumeroDOU(Long.valueOf(reconhecimentoCurso.getNumeroDOU()));
			}
		}
		return atoRegulatorio;
	}

	private TAtoRegulatorioComOuSemEMEC preencherDadosRenovacaoReconhecimentoCurso(AutorizacaoCursoVO reconhecimentoCurso, AutorizacaoCursoVO renovacaoReconhecimentoCurso, ConsistirException ex) throws Exception {
		if (!reconhecimentoCurso.getEmTramitacao() && Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso) && !(renovacaoReconhecimentoCurso.getCodigo().equals(reconhecimentoCurso.getCodigo()))) {
			TAtoRegulatorioComOuSemEMEC atoRegulatorio = new TAtoRegulatorioComOuSemEMEC();
			if (renovacaoReconhecimentoCurso.getEmTramitacao()) {
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getNumero()), "O NÚMERO do PROCESSO da RENOVAÇÃO RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getTipoProcesso()), "O TIPO do PROCESSO da RENOVAÇÃO RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getDataCadastro()), "A DATA DE CADASTRO da RENOVAÇÃO RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getDataProtocolo()), "A DATA do PROTOCOLO da RENOVAÇÃO RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
				TInformacoesTramitacaoEMEC informacoesTramitacaoEMEC = new TInformacoesTramitacaoEMEC();
				informacoesTramitacaoEMEC.setNumeroProcesso(Long.valueOf(Uteis.removeCaractersEspeciais2(getStringXml(renovacaoReconhecimentoCurso.getNumero()))));
				informacoesTramitacaoEMEC.setTipoProcesso(getStringXml(renovacaoReconhecimentoCurso.getTipoProcesso()));
				informacoesTramitacaoEMEC.setDataCadastro(getDataConvertidaXML(renovacaoReconhecimentoCurso.getDataCadastro()));
				informacoesTramitacaoEMEC.setDataProtocolo(getDataConvertidaXML(renovacaoReconhecimentoCurso.getDataProtocolo()));
				atoRegulatorio.setInformacoesTramitacaoEMEC(informacoesTramitacaoEMEC);
			} else {
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getTipoAutorizacaoCursoEnum()), "O TIPO DE RENOVAÇÃO da RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getDataCredenciamento()), "A DATA DE CREDENCIAMENTO da RENOVAÇÃO RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getNumero()), "O NUMERO da RENOVAÇÃO RECONHECIMENTO do CURSO deve ser informado " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
				Uteis.checkStateList(!Uteis.getIsValorNumerico(renovacaoReconhecimentoCurso.getNumero()), "O NUMERO da RENOVAÇÃO RECONHECIMENTO do CURSO deve ser apenas valor numérico " + TipoOrigemMontagemDados.CURSO.getMensagem(), ex);
				atoRegulatorio.setTipo(Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getTipoAutorizacaoCursoEnum()) ? renovacaoReconhecimentoCurso.getTipoAutorizacaoCursoEnum().getTipoAtoDiploma() : null);
				atoRegulatorio.setNumero(Uteis.removeCaractersEspeciais2(getStringXml(renovacaoReconhecimentoCurso.getNumero())));
				atoRegulatorio.setData(getDataConvertidaXML(renovacaoReconhecimentoCurso.getDataCredenciamento()));
				if (Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getData())) {
					atoRegulatorio.setDataPublicacao(getDataConvertidaXML(renovacaoReconhecimentoCurso.getData()));
				}
				if (Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getVeiculoPublicacao())) {
					atoRegulatorio.setVeiculoPublicacao(getStringXml(renovacaoReconhecimentoCurso.getVeiculoPublicacao()));
				}
				if (Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getSecaoPublicacao())) {
					atoRegulatorio.setSecaoPublicacao(Long.valueOf(renovacaoReconhecimentoCurso.getSecaoPublicacao()));
				}
				if (Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getPaginaPublicacao())) {
					atoRegulatorio.setPaginaPublicacao(Long.valueOf(renovacaoReconhecimentoCurso.getPaginaPublicacao()));
				}
				if (Uteis.isAtributoPreenchido(renovacaoReconhecimentoCurso.getNumeroDOU())) {
					atoRegulatorio.setNumeroDOU(Long.valueOf(renovacaoReconhecimentoCurso.getNumeroDOU()));
				}
			}
			return atoRegulatorio;
		}
		return null;
	}

	private AutorizacaoCursoVO getRenovacaoReconhecimentoCursoVO(ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getRenovacaoReconhecimentoVO().getCodigo())) {
			return getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(expedicaoDiplomaVO.getMatricula().getRenovacaoReconhecimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		}
		Date dataVigente = expedicaoDiplomaVO.getMatricula().getDataConclusaoCurso() != null ? expedicaoDiplomaVO.getMatricula().getDataConclusaoCurso() : expedicaoDiplomaVO.getMatricula().getData();
		return getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), dataVigente, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	private void preencherDadosEnderecoCursoDecisaoJudicial(UnidadeEnsinoVO unidadeEnsino, TDadosCursoPorDecisaoJudicial dadosCursoPorDecisaoJudicial, ConsistirException ce) throws Exception {
		TEndereco endereco = getEnderecoIesEmissora(unidadeEnsino, TipoOrigemMontagemDados.CURSO, ce);
		if (Uteis.isAtributoPreenchido(endereco.getCEP())) {
			dadosCursoPorDecisaoJudicial.setEnderecoCurso(endereco);
		} else {
			dadosCursoPorDecisaoJudicial.setEnderecoCursoIndisponivel(new TVazio());
		}
	}
	
	private void preencherDadosAutorizacaoCursoDecisaoJudicial(CursoVO curso, TDadosCursoPorDecisaoJudicial dadosCursoPorDecisaoJudicial, ConsistirException consistirException) throws Exception {
		if ((curso.getAutorizacaoResolucaoEmTramitacao() && Uteis.isAtributoPreenchido(curso.getNumeroProcessoAutorizacaoResolucao())) || (!curso.getAutorizacaoResolucaoEmTramitacao() && Uteis.isAtributoPreenchido(curso.getNumeroAutorizacao()))) {
			dadosCursoPorDecisaoJudicial.setAutorizacao(preencherDadosAutorizacaoCurso(curso, consistirException));
		}
	}
	
	private void preencherDadosReconhecimentoCurso(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosCurso dadosCurso, ConsistirException ex) throws Exception {
		AutorizacaoCursoVO reconhecimentoCurso = getAutorizacaoCursoVO(expedicaoDiplomaVO);
		AutorizacaoCursoVO renovacaoReconhecimentoCurso = getRenovacaoReconhecimentoCursoVO(expedicaoDiplomaVO);
		if (Uteis.isAtributoPreenchido(reconhecimentoCurso)) {
			dadosCurso.setReconhecimento(preencherDadosReconhecimentoCurso(reconhecimentoCurso, ex));
			dadosCurso.setRenovacaoReconhecimento(preencherDadosRenovacaoReconhecimentoCurso(reconhecimentoCurso, renovacaoReconhecimentoCurso, ex));
		} else {
			Uteis.checkStateList(Boolean.TRUE, "O reconhecimento do curso deve ser informado (Dados do Curso)", ex);
		}
	}
	
	private void preencherDadosReconhecimentoCursoPorDecisaoJudicial(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosCursoPorDecisaoJudicial dadosCursoPorDecisaoJudicial, ConsistirException ex) throws Exception {
		AutorizacaoCursoVO reconhecimentoCurso = getAutorizacaoCursoVO(expedicaoDiplomaVO);
		AutorizacaoCursoVO renovacaoReconhecimentoCurso = getRenovacaoReconhecimentoCursoVO(expedicaoDiplomaVO);
		if (Uteis.isAtributoPreenchido(reconhecimentoCurso)) {
			dadosCursoPorDecisaoJudicial.setReconhecimento(preencherDadosReconhecimentoCurso(reconhecimentoCurso, ex));
			dadosCursoPorDecisaoJudicial.setRenovacaoReconhecimento(preencherDadosRenovacaoReconhecimentoCurso(reconhecimentoCurso, renovacaoReconhecimentoCurso, ex));
		} else {
			Uteis.checkStateList(Boolean.TRUE, "O reconhecimento do curso deve ser informado (Dados do Curso)", ex);
		}
	}
	
	private TDadosIesEmissora preencherDadosIesEmissora(UnidadeEnsinoVO unidadeEmissora, CursoVO cursoVO, UsuarioVO usuarioVO, ConsistirException consistirException) throws Exception {
		validarDadosIESEmissora(unidadeEmissora, cursoVO, usuarioVO, consistirException);
		try {
			TDadosIesEmissora dadosIesEmissora = new TDadosIesEmissora();
			dadosIesEmissora.setNome(getNomeIes(unidadeEmissora));
			dadosIesEmissora.setCodigoMEC(Long.valueOf(unidadeEmissora.getCodigoIES()));
			dadosIesEmissora.setCNPJ(getCnpjXml(unidadeEmissora.getCNPJ()));
			dadosIesEmissora.setEndereco(getEnderecoIesEmissora(unidadeEmissora, TipoOrigemMontagemDados.IES_EMISSORA, consistirException));
			preencherDadosCredenciamentoIesEmissora(unidadeEmissora, cursoVO, dadosIesEmissora, consistirException);
			preencherDadosRecredenciamentoIesEmissora(unidadeEmissora, cursoVO, dadosIesEmissora, consistirException);
			preencherDadosRenovacaoRecredenciamentoIesEmissora(unidadeEmissora, cursoVO, dadosIesEmissora, consistirException);
			preencherDadosMantenedoraIesEmissora(unidadeEmissora, dadosIesEmissora, usuarioVO, consistirException);
			return dadosIesEmissora;
		} catch (Exception e) {
			consistirException.getListaMensagemErro().add(e.getMessage());
			return null;
		}
	}
	
	private String getNomeIes(UnidadeEnsinoVO unidadeEmissora) {
		if (Uteis.isAtributoPreenchido(unidadeEmissora.getNomeExpedicaoDiploma())) {
			return getStringXml(unidadeEmissora.getNomeExpedicaoDiploma());
		} else {
			return getStringXml(unidadeEmissora.getNome());
		}
	}
	
	public void validarDadosIESEmissora(UnidadeEnsinoVO unidadeEmissora, CursoVO cursoVO, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		if (!Uteis.isAtributoPreenchido(unidadeEmissora.getNomeExpedicaoDiploma())) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNome()), "O NOME da UNIDADE EMISSORA deve ser informado (Dados da Unidade Emissora)", ce);
		}
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCodigoIES()), "O CÓDIGO MEC da UNIDADE EMISSORA deve ser informado (Dados da Unidade Emissora)", ce);
		if (Uteis.isAtributoPreenchido(unidadeEmissora.getCNPJ())) {
			Uteis.checkStateList(!Uteis.validaCNPJ(getCnpjXml(unidadeEmissora.getCNPJ())), "O CNPJ da UNIDADE EMISSORA deve ser válido (Dados da Unidade Emissora)", ce);
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCNPJ()), "O CNPJ da UNIDADE EMISSORA deve ser informado (Dados da Unidade Emissora)", ce);
		}
	}
	
	private TEndereco getEnderecoIesEmissora(UnidadeEnsinoVO unidadeEmissora, TipoOrigemMontagemDados tipoOrigemMontagemDados, ConsistirException consistirException) throws Exception {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getEndereco()), "O ENDEREÇO da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getSetor()), "O SETOR/BAIRRO da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCEP()), "O CEP da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCidade().getCodigoIBGE()), "O CÓDIGO IBGE da CIDADE da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCidade().getNome()), "O NOME da CIDADE da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCidade().getEstado().getSigla()), "A SIGLA do ESTADO da CIDADE da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
		TEndereco endereco = new TEndereco();
		endereco.setLogradouro(getStringXml(unidadeEmissora.getEndereco()));
		endereco.setBairro(getStringXml(unidadeEmissora.getSetor()));
		endereco.setCodigoMunicipio(getStringXml(unidadeEmissora.getCidade().getCodigoIBGE()));
		endereco.setNomeMunicipio(getStringXml(unidadeEmissora.getCidade().getNome()));
		endereco.setCEP(Uteis.removerMascara(getStringXml(unidadeEmissora.getCEP())));
		endereco.setUF(getUfEndereco(unidadeEmissora.getCidade().getEstado().getSigla()));
		if (Uteis.isAtributoPreenchido(unidadeEmissora.getNumero())) {
			endereco.setNumero(getStringXml(unidadeEmissora.getNumero()));
		}
		if (Uteis.isAtributoPreenchido(unidadeEmissora.getComplemento())) {
			endereco.setComplemento(getStringXml(unidadeEmissora.getComplemento()));
		}
		return endereco;
	}
	
	private TUf getUfEndereco(String sigla) {
		if (Uteis.isAtributoPreenchido(sigla)) {
			return TUf.valueOf(sigla);
		} else {
			return null;
		}
	}

	private void preencherDadosCredenciamentoIesEmissora(UnidadeEnsinoVO unidadeEmissora, CursoVO cursoVO, TDadosIesEmissora dadosIesEmissora, ConsistirException ce) throws Exception {
		dadosIesEmissora.setCredenciamento(preencherDadosCredenciamentoIesEmissora(unidadeEmissora, cursoVO, "DO CREDENCIAMENTO @{MODALIDADE}", "(Dados da Unidade Emissora)", ce));
	}
	
	private TAtoRegulatorioComOuSemEMEC preencherDadosCredenciamentoIesEmissora(UnidadeEnsinoVO unidadeEmissora, CursoVO cursoVO, String mensagem, String mensagemValidacao, ConsistirException ce) throws Exception {
		TAtoRegulatorioComOuSemEMEC atoRegulatorio = new TAtoRegulatorioComOuSemEMEC();
		if (cursoVO.getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
			if (unidadeEmissora.getCredenciamentoEadEmTramitacao()) {
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroProcessoCredenciamentoEad()), "O NÚMERO do PROCESSO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoProcessoCredenciamentoEad()), "O TIPO DE PROCESSO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataCadastroCredenciamentoEad()), "A DATA DE CADASTRO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataProtocoloCredenciamentoEad()), "A DATA do PROTOCOLO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
				atoRegulatorio.setInformacoesTramitacaoEMEC(new TInformacoesTramitacaoEMEC());
				atoRegulatorio.getInformacoesTramitacaoEMEC().setNumeroProcesso(Long.valueOf(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroProcessoCredenciamentoEad()))));
				atoRegulatorio.getInformacoesTramitacaoEMEC().setTipoProcesso(getStringXml(unidadeEmissora.getTipoProcessoCredenciamentoEad()));
				atoRegulatorio.getInformacoesTramitacaoEMEC().setDataCadastro(getDataConvertidaXML(unidadeEmissora.getDataCadastroCredenciamentoEad()));
				atoRegulatorio.getInformacoesTramitacaoEMEC().setDataProtocolo(getDataConvertidaXML(unidadeEmissora.getDataProtocoloCredenciamentoEad()));
			} else {
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoEAD()), "O TIPO ATO DIPLOMA " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroCredenciamentoEAD()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.getIsValorNumerico(unidadeEmissora.getNumeroCredenciamentoEAD()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser numérico " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataCredenciamentoEAD()), "A DATA " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
				atoRegulatorio.setTipo(Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoEAD()) ? unidadeEmissora.getTipoAutorizacaoEAD().getTipoAtoDiploma() : null);
				atoRegulatorio.setNumero(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroCredenciamentoEAD())));
				atoRegulatorio.setData(getDataConvertidaXML(unidadeEmissora.getDataCredenciamentoEAD()));
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getDataPublicacaoDOEAD())) {
					atoRegulatorio.setDataPublicacao(getDataConvertidaXML(unidadeEmissora.getDataPublicacaoDOEAD()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getVeiculoPublicacaoCredenciamentoEAD())) {
					atoRegulatorio.setVeiculoPublicacao(getStringXml(unidadeEmissora.getVeiculoPublicacaoCredenciamentoEAD()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getSecaoPublicacaoCredenciamentoEAD())) {
					atoRegulatorio.setSecaoPublicacao(Long.valueOf(unidadeEmissora.getSecaoPublicacaoCredenciamentoEAD()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getPaginaPublicacaoCredenciamentoEAD())) {
					atoRegulatorio.setPaginaPublicacao(Long.valueOf(unidadeEmissora.getPaginaPublicacaoCredenciamentoEAD()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroDOUCredenciamentoEAD())) {
					atoRegulatorio.setNumeroDOU(Long.valueOf(unidadeEmissora.getNumeroDOUCredenciamentoEAD()));
				}
			}
		} else {
			if (unidadeEmissora.getCredenciamentoEmTramitacao()) {
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroProcessoCredenciamento()), "O NÚMERO do PROCESSO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoProcessoCredenciamento()), "O TIPO DE PROCESSO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataCadastroCredenciamento()), "A DATA DE CADASTRO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataProtocoloCredenciamento()), "A DATA do PROTOCOLO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
				atoRegulatorio.setInformacoesTramitacaoEMEC(new TInformacoesTramitacaoEMEC());
				atoRegulatorio.getInformacoesTramitacaoEMEC().setNumeroProcesso(Long.valueOf(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroProcessoCredenciamento()))));
				atoRegulatorio.getInformacoesTramitacaoEMEC().setTipoProcesso(getStringXml(unidadeEmissora.getTipoProcessoCredenciamento()));
				atoRegulatorio.getInformacoesTramitacaoEMEC().setDataCadastro(getDataConvertidaXML(unidadeEmissora.getDataCadastroCredenciamento()));
				atoRegulatorio.getInformacoesTramitacaoEMEC().setDataProtocolo(getDataConvertidaXML(unidadeEmissora.getDataProtocoloCredenciamento()));
			} else {
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoEnum()), "O TIPO ATO DIPLOMA " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroCredenciamento()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.getIsValorNumerico(unidadeEmissora.getNumeroCredenciamento()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser numérico " + mensagemValidacao, ce);
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataCredenciamento()), "A DATA " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
				atoRegulatorio.setTipo(Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoEnum()) ? unidadeEmissora.getTipoAutorizacaoEnum().getTipoAtoDiploma() : null);
				atoRegulatorio.setNumero(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroCredenciamento())));
				atoRegulatorio.setData(getDataConvertidaXML(unidadeEmissora.getDataCredenciamento()));
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getDataPublicacaoDO())) {
					atoRegulatorio.setDataPublicacao(getDataConvertidaXML(unidadeEmissora.getDataPublicacaoDO()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getVeiculoPublicacaoCredenciamento())) {
					atoRegulatorio.setVeiculoPublicacao(getStringXml(unidadeEmissora.getVeiculoPublicacaoCredenciamento()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getSecaoPublicacaoCredenciamento())) {
					atoRegulatorio.setSecaoPublicacao(Long.valueOf(unidadeEmissora.getSecaoPublicacaoCredenciamento()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getPaginaPublicacaoCredenciamento())) {
					atoRegulatorio.setPaginaPublicacao(Long.valueOf(unidadeEmissora.getPaginaPublicacaoCredenciamento()));
				}
				if (Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroDOUCredenciamento())) {
					atoRegulatorio.setNumeroDOU(Long.valueOf(unidadeEmissora.getNumeroDOUCredenciamento()));
				}
			}
		}
		return atoRegulatorio;
	}
	
	private Boolean recredenciamentoIesEmissoraExistente(UnidadeEnsinoVO unidadeCertificadora, CursoVO cursoVO) {
		if (cursoVO.getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) 
				&& ((unidadeCertificadora.getRecredenciamentoEmTramitacaoEad() && Uteis.isAtributoPreenchido(unidadeCertificadora.getNumeroProcessoRecredenciamentoEad())) 
						|| (!unidadeCertificadora.getRecredenciamentoEmTramitacaoEad() && Uteis.isAtributoPreenchido(unidadeCertificadora.getNumeroRecredenciamentoEAD())))) {
			return Boolean.TRUE;
		} else if (!cursoVO.getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) 
				&& ((unidadeCertificadora.getRecredenciamentoEmTramitacao() && Uteis.isAtributoPreenchido(unidadeCertificadora.getNumeroProcessoRecredenciamento())) 
						|| (!unidadeCertificadora.getRecredenciamentoEmTramitacao() && Uteis.isAtributoPreenchido(unidadeCertificadora.getNumeroRecredenciamento())))) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	private void preencherDadosRecredenciamentoIesEmissora(UnidadeEnsinoVO unidadeEmissora, CursoVO cursoVO, TDadosIesEmissora dadosIesEmissora, ConsistirException ce) throws Exception {
		dadosIesEmissora.setRecredenciamento(preencherDadosRecredenciamentoIesEmissora(unidadeEmissora, cursoVO, "DO RECREDENCIAMENTO @{MODALIDADE}", "(Dados da Unidade Emissora)", ce));
	}

	private TAtoRegulatorioComOuSemEMEC preencherDadosRecredenciamentoIesEmissora(UnidadeEnsinoVO unidadeEmissora, CursoVO cursoVO, String mensagem, String mensagemValidacao, ConsistirException ce) throws Exception {
		if (recredenciamentoIesEmissoraExistente(unidadeEmissora, cursoVO)) {
			TAtoRegulatorioComOuSemEMEC atoRegulatorio = new TAtoRegulatorioComOuSemEMEC();
			if (cursoVO.getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
				if (unidadeEmissora.getRecredenciamentoEmTramitacaoEad()) {
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroProcessoRecredenciamentoEad()), "O NÚMERO do PROCESSO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoProcessoRecredenciamentoEad()), "O TIPO DE PROCESSO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataCadastroRecredenciamentoEad()), "A DATA DE CADASTRO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataProtocoloRecredenciamentoEad()), "A DATA do PROTOCOLO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					atoRegulatorio.setInformacoesTramitacaoEMEC(new TInformacoesTramitacaoEMEC());
					atoRegulatorio.getInformacoesTramitacaoEMEC().setNumeroProcesso(Long.valueOf(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroProcessoRecredenciamentoEad()))));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setTipoProcesso(getStringXml(unidadeEmissora.getTipoProcessoRecredenciamentoEad()));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setDataCadastro(getDataConvertidaXML(unidadeEmissora.getDataCadastroRecredenciamentoEad()));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setDataProtocolo(getDataConvertidaXML(unidadeEmissora.getDataProtocoloRecredenciamentoEad()));
				} else {
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoRecredenciamentoEAD()), "O TIPO ATO DIPLOMA " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroRecredenciamentoEAD()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.getIsValorNumerico(unidadeEmissora.getNumeroRecredenciamentoEAD()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser numérico " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataRecredenciamentoEAD()), "A DATA " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					atoRegulatorio.setTipo(Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoRecredenciamentoEAD()) ? unidadeEmissora.getTipoAutorizacaoRecredenciamentoEAD().getTipoAtoDiploma() : null);
					atoRegulatorio.setNumero(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroRecredenciamentoEAD())));
					atoRegulatorio.setData(getDataConvertidaXML(unidadeEmissora.getDataRecredenciamentoEAD()));
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getDataPublicacaoRecredenciamentoEAD())) {
						atoRegulatorio.setDataPublicacao(getDataConvertidaXML(unidadeEmissora.getDataPublicacaoRecredenciamentoEAD()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getVeiculoPublicacaoRecredenciamentoEAD())) {
						atoRegulatorio.setVeiculoPublicacao(getStringXml(unidadeEmissora.getVeiculoPublicacaoRecredenciamentoEAD()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getSecaoPublicacaoRecredenciamentoEAD())) {
						atoRegulatorio.setSecaoPublicacao(Long.valueOf(unidadeEmissora.getSecaoPublicacaoRecredenciamentoEAD()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getPaginaPublicacaoRecredenciamentoEAD())) {
						atoRegulatorio.setPaginaPublicacao(Long.valueOf(unidadeEmissora.getPaginaPublicacaoRecredenciamentoEAD()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroDOURecredenciamentoEAD())) {
						atoRegulatorio.setNumeroDOU(Long.valueOf(unidadeEmissora.getNumeroDOURecredenciamentoEAD()));
					}
				}
			} else {
				if (unidadeEmissora.getRecredenciamentoEmTramitacao()) {
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroProcessoRecredenciamento()), "O NÚMERO do PROCESSO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoProcessoRecredenciamento()), "O TIPO DE PROCESSO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataCadastroRecredenciamento()), "A DATA DE CADASTRO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataProtocoloRecredenciamento()), "A DATA do PROTOCOLO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					atoRegulatorio.setInformacoesTramitacaoEMEC(new TInformacoesTramitacaoEMEC());
					atoRegulatorio.getInformacoesTramitacaoEMEC().setNumeroProcesso(Long.valueOf(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroProcessoRecredenciamento()))));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setTipoProcesso(getStringXml(unidadeEmissora.getTipoProcessoRecredenciamento()));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setDataCadastro(getDataConvertidaXML(unidadeEmissora.getDataCadastroRecredenciamento()));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setDataProtocolo(getDataConvertidaXML(unidadeEmissora.getDataProtocoloRecredenciamento()));
				} else {
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoRecredenciamento()), "O TIPO ATO DIPLOMA " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroRecredenciamento()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.getIsValorNumerico(unidadeEmissora.getNumeroRecredenciamento()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser numérico " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataRecredenciamento()), "A DATA " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					atoRegulatorio.setTipo(Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoRecredenciamento()) ? unidadeEmissora.getTipoAutorizacaoRecredenciamento().getTipoAtoDiploma() : null);
					atoRegulatorio.setNumero(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroRecredenciamento())));
					atoRegulatorio.setData(getDataConvertidaXML(unidadeEmissora.getDataRecredenciamento()));
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getDataPublicacaoRecredenciamento())) {
						atoRegulatorio.setDataPublicacao(getDataConvertidaXML(unidadeEmissora.getDataPublicacaoRecredenciamento()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getVeiculoPublicacaoRecredenciamento())) {
						atoRegulatorio.setVeiculoPublicacao(getStringXml(unidadeEmissora.getVeiculoPublicacaoRecredenciamento()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getSecaoPublicacaoRecredenciamento())) {
						atoRegulatorio.setSecaoPublicacao(Long.valueOf(unidadeEmissora.getSecaoPublicacaoRecredenciamento()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getPaginaPublicacaoRecredenciamento())) {
						atoRegulatorio.setPaginaPublicacao(Long.valueOf(unidadeEmissora.getPaginaPublicacaoRecredenciamento()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroDOURecredenciamento())) {
						atoRegulatorio.setNumeroDOU(Long.valueOf(unidadeEmissora.getNumeroDOURecredenciamento()));
					}
				}
			}
			return atoRegulatorio;
		}
		return null;
	}
	
	private Boolean renovacaoRecredenciamentoIesEmissoraExistente(UnidadeEnsinoVO unidadeCertificadora, CursoVO cursoVO) {
		if (cursoVO.getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) 
				&& ((unidadeCertificadora.getRenovacaoRecredenciamentoEmTramitacaoEad() && Uteis.isAtributoPreenchido(unidadeCertificadora.getNumeroProcessoRenovacaoRecredenciamentoEad())) 
						|| (!unidadeCertificadora.getRenovacaoRecredenciamentoEmTramitacaoEad() && Uteis.isAtributoPreenchido(unidadeCertificadora.getNumeroRenovacaoRecredenciamentoEAD())))) {
			return Boolean.TRUE;
		} else if (!cursoVO.getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE) 
				&& ((unidadeCertificadora.getRenovacaoRecredenciamentoEmTramitacao() && Uteis.isAtributoPreenchido(unidadeCertificadora.getNumeroProcessoRenovacaoRecredenciamento())) 
						|| (!unidadeCertificadora.getRenovacaoRecredenciamentoEmTramitacao() && Uteis.isAtributoPreenchido(unidadeCertificadora.getNumeroRenovacaoRecredenciamento())))) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	private void preencherDadosRenovacaoRecredenciamentoIesEmissora(UnidadeEnsinoVO unidadeEmissora, CursoVO cursoVO, TDadosIesEmissora dadosIesEmissora, ConsistirException ce) throws Exception {
		dadosIesEmissora.setRenovacaoDeRecredenciamento(preencherDadosRenovacaoRecredenciamentoIesEmissora(unidadeEmissora, cursoVO, "DA RENOVAÇÃO DE RECREDENCIAMENTO @{MODALIDADE}", "(Dados da Unidade Emissora)", ce));
	}

	private TAtoRegulatorioComOuSemEMEC preencherDadosRenovacaoRecredenciamentoIesEmissora(UnidadeEnsinoVO unidadeEmissora, CursoVO cursoVO, String mensagem, String mensagemValidacao, ConsistirException ce) throws Exception {
		if (renovacaoRecredenciamentoIesEmissoraExistente(unidadeEmissora, cursoVO)) {
			TAtoRegulatorioComOuSemEMEC atoRegulatorio = new TAtoRegulatorioComOuSemEMEC();
			if (cursoVO.getModalidadeCurso().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
				if (unidadeEmissora.getRenovacaoRecredenciamentoEmTramitacaoEad()) {
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoProcessoRenovacaoRecredenciamentoEad()), "O TIPO DE PROCESSO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroProcessoRenovacaoRecredenciamentoEad()), "O NÚMERO do PROCESSO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataCadastroRenovacaoRecredenciamentoEad()), "A DATA DE CADASTRO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataProtocoloRenovacaoRecredenciamentoEad()), "A DATA do PROTOCOLO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					atoRegulatorio.setInformacoesTramitacaoEMEC(new TInformacoesTramitacaoEMEC());
					atoRegulatorio.getInformacoesTramitacaoEMEC().setTipoProcesso(getStringXml(unidadeEmissora.getTipoProcessoRenovacaoRecredenciamentoEad()));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setNumeroProcesso(Long.valueOf(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroProcessoRenovacaoRecredenciamentoEad()))));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setDataCadastro(getDataConvertidaXML(unidadeEmissora.getDataCadastroRenovacaoRecredenciamentoEad()));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setDataProtocolo(getDataConvertidaXML(unidadeEmissora.getDataProtocoloRenovacaoRecredenciamentoEad()));
				} else {
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoRenovacaoRecredenciamentoEAD()), "O TIPO ATO DIPLOMA " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroRenovacaoRecredenciamentoEAD()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.getIsValorNumerico(unidadeEmissora.getNumeroRenovacaoRecredenciamentoEAD()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser numérico " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataRenovacaoRecredenciamentoEAD()), "A DATA " + mensagem.replace("@{MODALIDADE}", "de EAD") + " deve ser informado " + mensagemValidacao, ce);
					atoRegulatorio.setTipo(Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoRenovacaoRecredenciamentoEAD()) ? unidadeEmissora.getTipoAutorizacaoRenovacaoRecredenciamentoEAD().getTipoAtoDiploma() : null);
					atoRegulatorio.setNumero(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroRenovacaoRecredenciamentoEAD())));
					atoRegulatorio.setData(getDataConvertidaXML(unidadeEmissora.getDataRenovacaoRecredenciamentoEAD()));
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getDataPublicacaoRenovacaoRecredenciamentoEAD())) {
						atoRegulatorio.setDataPublicacao(getDataConvertidaXML(unidadeEmissora.getDataPublicacaoRenovacaoRecredenciamentoEAD()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getVeiculoPublicacaoRenovacaoRecredenciamentoEAD())) {
						atoRegulatorio.setVeiculoPublicacao(getStringXml(unidadeEmissora.getVeiculoPublicacaoRenovacaoRecredenciamentoEAD()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getSecaoPublicacaoRenovacaoRecredenciamentoEAD())) {
						atoRegulatorio.setSecaoPublicacao(Long.valueOf(unidadeEmissora.getSecaoPublicacaoRenovacaoRecredenciamentoEAD()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getPaginaPublicacaoRenovacaoRecredenciamentoEAD())) {
						atoRegulatorio.setPaginaPublicacao(Long.valueOf(unidadeEmissora.getPaginaPublicacaoRenovacaoRecredenciamentoEAD()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroDOURenovacaoRecredenciamentoEAD())) {
						atoRegulatorio.setNumeroDOU(Long.valueOf(unidadeEmissora.getNumeroDOURenovacaoRecredenciamentoEAD()));
					}
				}
			} else {
				if (unidadeEmissora.getRenovacaoRecredenciamentoEmTramitacao()) {
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoProcessoRenovacaoRecredenciamento()), "O TIPO DE PROCESSO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroProcessoRenovacaoRecredenciamento()), "O NÚMERO do PROCESSO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataCadastroRenovacaoRecredenciamento()), "A DATA DE CADASTRO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataProtocoloRenovacaoRecredenciamento()), "A DATA do PROTOCOLO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					atoRegulatorio.setInformacoesTramitacaoEMEC(new TInformacoesTramitacaoEMEC());
					atoRegulatorio.getInformacoesTramitacaoEMEC().setTipoProcesso(getStringXml(unidadeEmissora.getTipoProcessoRenovacaoRecredenciamento()));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setNumeroProcesso(Long.valueOf(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroProcessoRenovacaoRecredenciamento()))));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setDataCadastro(getDataConvertidaXML(unidadeEmissora.getDataCadastroRenovacaoRecredenciamento()));
					atoRegulatorio.getInformacoesTramitacaoEMEC().setDataProtocolo(getDataConvertidaXML(unidadeEmissora.getDataProtocoloRenovacaoRecredenciamento()));
				} else {
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoRenovacaoRecredenciamento()), "O TIPO ATO DIPLOMA " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroRenovacaoRecredenciamento()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.getIsValorNumerico(unidadeEmissora.getNumeroRenovacaoRecredenciamento()), "O NÚMERO " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser numérico " + mensagemValidacao, ce);
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getDataRenovacaoRecredenciamento()), "A DATA " + mensagem.replace("@{MODALIDADE}", "PRESENCIAL") + " deve ser informado " + mensagemValidacao, ce);
					atoRegulatorio.setTipo(Uteis.isAtributoPreenchido(unidadeEmissora.getTipoAutorizacaoRenovacaoRecredenciamento()) ? unidadeEmissora.getTipoAutorizacaoRenovacaoRecredenciamento().getTipoAtoDiploma() : null);
					atoRegulatorio.setNumero(Uteis.removeCaractersEspeciais2(getStringXml(unidadeEmissora.getNumeroRenovacaoRecredenciamento())));
					atoRegulatorio.setData(getDataConvertidaXML(unidadeEmissora.getDataRenovacaoRecredenciamento()));
					if (unidadeEmissora.getDataPublicacaoRenovacaoRecredenciamento() != null) {
						atoRegulatorio.setDataPublicacao(getDataConvertidaXML(unidadeEmissora.getDataPublicacaoRenovacaoRecredenciamento()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getVeiculoPublicacaoRenovacaoRecredenciamento())) {
						atoRegulatorio.setVeiculoPublicacao(getStringXml(unidadeEmissora.getVeiculoPublicacaoRenovacaoRecredenciamento()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getSecaoPublicacaoRenovacaoRecredenciamento())) {
						atoRegulatorio.setSecaoPublicacao(Long.valueOf(unidadeEmissora.getSecaoPublicacaoRenovacaoRecredenciamento()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getPaginaPublicacaoRenovacaoRecredenciamento())) {
						atoRegulatorio.setPaginaPublicacao(Long.valueOf(unidadeEmissora.getPaginaPublicacaoRenovacaoRecredenciamento()));
					}
					if (Uteis.isAtributoPreenchido(unidadeEmissora.getNumeroDOURenovacaoRecredenciamento())) {
						atoRegulatorio.setNumeroDOU(Long.valueOf(unidadeEmissora.getNumeroDOURenovacaoRecredenciamento()));
					}
				}
			}
			return atoRegulatorio;
		}
		return null;
	}

	private void preencherDadosMantenedoraIesEmissora(UnidadeEnsinoVO unidadeEmissora, TDadosIesEmissora dadosIesEmissora, UsuarioVO usuarioVO, ConsistirException consistirException) throws Exception {
		if (Uteis.isAtributoPreenchido(unidadeEmissora.getMantenedora())) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getMantenedora()), "A MANTENEDORA da IES EMISSORA deve ser informado " + TipoOrigemMontagemDados.IES_EMISSORA.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEmissora.getCnpjMantenedora()), "O CNPJ da MANTENEDORA da IES EMISSORA deve ser informado " + TipoOrigemMontagemDados.IES_EMISSORA.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.validaCNPJ(Uteis.removerMascara(unidadeEmissora.getCnpjMantenedora())), "O CNPJ da MANTENEDORA da IES EMISSORA deve ser válido " + TipoOrigemMontagemDados.IES_EMISSORA.getMensagem(), consistirException);
			TDadosIesEmissora.Mantenedora mantenedora = new TDadosIesEmissora.Mantenedora();
			mantenedora.setRazaoSocial(getStringXml(unidadeEmissora.getMantenedora()));
			mantenedora.setCNPJ(getCnpjXml(unidadeEmissora.getCnpjMantenedora()));
			preencherDadosEnderecoMantenedoraIesEmissora(unidadeEmissora, mantenedora, consistirException);
			dadosIesEmissora.setMantenedora(mantenedora);
		}
	}

	private void preencherDadosEnderecoMantenedoraIesEmissora(UnidadeEnsinoVO unidadeEmissora, TDadosIesEmissora.Mantenedora mantenedora, ConsistirException consistirException) throws Exception {
		mantenedora.setEndereco(preencherDadosEnderecoMantenedoraIesEmissora(unidadeEmissora, TipoOrigemMontagemDados.MANTENEDORA, consistirException));
	}
	
	private TEndereco preencherDadosEnderecoMantenedoraIesEmissora(UnidadeEnsinoVO unidadeEnsinoVO, TipoOrigemMontagemDados tipoOrigemMontagemDados, ConsistirException consistirException) throws Exception {
		TEndereco endereco = new TEndereco();
		if (unidadeEnsinoVO.getUtilizarEnderecoUnidadeEnsinoMantenedora()) {
			endereco = getEnderecoIesEmissora(unidadeEnsinoVO, tipoOrigemMontagemDados, consistirException);
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getEnderecoMantenedora()), "O ENDEREÇO da MANTENEDORA da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getBairroMantenedora()), "O SETOR/BAIRRO da MANTENEDORA da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCepMantenedora()), "O CEP da MANTENEDORA da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCidadeMantenedora().getCodigoIBGE()), "O CÓDIGO IBGE da CIDADE da MANTENEDORA da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCidadeMantenedora().getNome()), "O NOME da CIDADE da MANTENEDORA da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCidadeMantenedora().getEstado().getSigla()), "A SIGLA do ESTADO da CIDADE da MANTENEDORA da IES EMISSORA deve ser informado " + tipoOrigemMontagemDados.getMensagem(), consistirException);
			endereco.setLogradouro(getStringXml(unidadeEnsinoVO.getEnderecoMantenedora()));
			endereco.setBairro(getStringXml(unidadeEnsinoVO.getBairroMantenedora()));
			endereco.setCodigoMunicipio(getStringXml(unidadeEnsinoVO.getCidadeMantenedora().getCodigoIBGE()));
			endereco.setNomeMunicipio(getStringXml(unidadeEnsinoVO.getCidadeMantenedora().getNome()));
			endereco.setCEP(Uteis.removerMascara(getStringXml(unidadeEnsinoVO.getCepMantenedora())));
			endereco.setUF(getUfEndereco(unidadeEnsinoVO.getCidadeMantenedora().getEstado().getSigla()));
			if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getNumeroMantenedora())) {
				endereco.setNumero(getStringXml(unidadeEnsinoVO.getNumeroMantenedora()));
			}
			if (Uteis.isAtributoPreenchido(getStringXml(unidadeEnsinoVO.getComplementoMantenedora()))) {
				endereco.setComplemento(getStringXml(unidadeEnsinoVO.getComplementoMantenedora()));
			}
		}
		return endereco;
	}

	public void validarDadosCredenciamentoIes(TipoAutorizacaoCursoEnum tipo, String numero, Date data, String mensagem, ConsistirException ce) throws Exception {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(tipo), "O tipo ato diploma do credenciamento " + mensagem + " deve ser informado.", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(numero), "O número do credenciamento " + mensagem + " deve ser informado.", ce);
		Uteis.checkStateList(!Uteis.getIsValorNumerico(numero), "O número do credenciamento " + mensagem + " deve ser numérico.", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(data), "A data de credenciamento " + mensagem + " deve ser informado.", ce);
	}

	public void validarDadosRecredenciamentoIes(TipoAutorizacaoCursoEnum tipo, String numero, Date data, String mensagem, ConsistirException ce) throws Exception {
		if (Uteis.isAtributoPreenchido(numero)) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(tipo), "O tipo ato diploma do Recredenciamento " + mensagem + " deve ser informado.", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(numero), "O número do recredenciamento " + mensagem + " deve ser informado.", ce);
			Uteis.checkStateList(!Uteis.getIsValorNumerico(numero), "O número do recredenciamento " + mensagem + " deve ser numérico.", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(data), "A Data do Recredenciamento " + mensagem + " deve ser informado.", ce);
		}
	}

	public void validarDadosRenovacaoDeRecredenciamentoIes(TipoAutorizacaoCursoEnum tipo, String numero, Date data, String mensagem, ConsistirException ce) throws Exception {
		if (Uteis.isAtributoPreenchido(numero)) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(tipo), "O tipo ato diploma da renovação do recredenciamento " + mensagem + " deve ser informado.", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(numero), "O número da renovação do recredenciamento " + mensagem + " deve ser informado.", ce);
			Uteis.checkStateList(!Uteis.getIsValorNumerico(numero), "O número da renovação do recredenciamento " + mensagem + " deve ser numérico.", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(data), "A data da renovação do recredenciamento " + mensagem + " deve ser informado.", ce);
		}
	}

	private void validarDadosIesOriginalCursoPTA(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNomeIesPTA()), "O NOME da IES deve ser preenchido (Processo de Transferência Assistida)", ce);
		Uteis.checkStateList(Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCnpjPTA()) && !Uteis.validaCNPJ(getCnpjXml(expedicaoDiplomaVO.getCnpjPTA())), "O CNPJ da IES deve ser valido (Processo de Transferência Assistida)", ce);
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCidadePTA())) {
			expedicaoDiplomaVO.setCidadePTA(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(expedicaoDiplomaVO.getCidadePTA().getCodigo(), Boolean.FALSE, usuarioVO));
		}
	}

	private void preencherDadosEnderecoIesOriginalCursoPTA(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosIesOriginalCursoPTA dadosIesOriginalCursoPTA) {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCepPTA())) {
			TEndereco endereco = new TEndereco();
			endereco.setLogradouro(getStringXml(expedicaoDiplomaVO.getLogradouroPTA()));
			endereco.setBairro(getStringXml(expedicaoDiplomaVO.getBairroPTA()));
			endereco.setCEP(Uteis.removerMascara(getStringXml(expedicaoDiplomaVO.getCepPTA())));
			endereco.setCodigoMunicipio(getStringXml(expedicaoDiplomaVO.getCidadePTA().getCodigoIBGE()));
			endereco.setNomeMunicipio(getStringXml(expedicaoDiplomaVO.getCidadePTA().getNome()));
			endereco.setUF(getUfEndereco(expedicaoDiplomaVO.getCidadePTA().getEstado().getSigla()));
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroPTA())) {
				endereco.setNumero(getStringXml(expedicaoDiplomaVO.getNumeroPTA()));
			}
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getComplementoPTA())) {
				endereco.setComplemento(getStringXml(expedicaoDiplomaVO.getComplementoPTA()));
			}
			dadosIesOriginalCursoPTA.setEndereco(endereco);
		}
	}

	private void preencherDadosDescredenciamentoIesOriginalCursoPTA(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosIesOriginalCursoPTA dadosIesOriginalCursoPTA, ConsistirException ce) throws Exception {
		TAtoRegulatorio descredenciamento = new TAtoRegulatorio();
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getTipoDescredenciamentoPTA().getTipoAtoDiploma()), "O TIPO do DESCREDENCIAMENTO da IES deve ser preenchido (Processo de Transferência Assistida)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroDescredenciamentoPTA()), "O NÚMERO do DESCREDENCIAMENTO da IES deve ser preenchido (Processo de Transferência Assistida)", ce);
		Uteis.checkStateList(!Uteis.getIsValorNumerico(expedicaoDiplomaVO.getNumeroDescredenciamentoPTA()), "O NÚMERO do DESCREDENCIAMENTO da IES deve ser númerico (Processo de Transferência Assistida)", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getDataDescredenciamentoPTA()), "A DATA do DESCREDENCIAMENTO da IES deve ser preenchido (Processo de Transferência Assistida)", ce);
		descredenciamento.setTipo(expedicaoDiplomaVO.getTipoDescredenciamentoPTA().getTipoAtoDiploma());
		descredenciamento.setNumero(Uteis.removeCaractersEspeciais2(getStringXml(expedicaoDiplomaVO.getNumeroDescredenciamentoPTA())));
		descredenciamento.setData(getDataConvertidaXML(expedicaoDiplomaVO.getDataDescredenciamentoPTA()));
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getVeiculoPublicacaoDescredenciamentoPTA().trim())) {
			descredenciamento.setVeiculoPublicacao(Uteis.removeCaractersEspeciais2(getStringXml(expedicaoDiplomaVO.getVeiculoPublicacaoDescredenciamentoPTA().trim())));
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getDataPublicacaoDescredenciamentoPTA())) {
			descredenciamento.setDataPublicacao(getDataConvertidaXML(expedicaoDiplomaVO.getDataPublicacaoDescredenciamentoPTA()));
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getSecaoPublicacaoDescredenciamentoPTA())) {
			descredenciamento.setSecaoPublicacao(Long.valueOf(expedicaoDiplomaVO.getSecaoPublicacaoDescredenciamentoPTA()));
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getPaginaPublicacaoDescredenciamentoPTA())) {
			descredenciamento.setPaginaPublicacao(Long.valueOf(expedicaoDiplomaVO.getPaginaPublicacaoDescredenciamentoPTA()));
		}
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroDOUDescredenciamentoPTA())) {
			descredenciamento.setNumeroDOU(Long.valueOf(expedicaoDiplomaVO.getNumeroDOUDescredenciamentoPTA()));
		}
		dadosIesOriginalCursoPTA.setDescredenciamento(descredenciamento);
	}

	public String realizarGeracaoHashCodigoValidacao(ExpedicaoDiplomaVO expedicaoDiplomaVO, String livroRegistro, String numeroFolhaDoDiploma, String numeroSequenciaDoDiploma, UsuarioVO usuarioVO) throws Exception {
		try {
			String hashCodigoValidacao = "";
			ControleLivroRegistroDiplomaVO livroRegistroDiplomaVO = new ControleLivroRegistroDiplomaVO();
			ControleLivroFolhaReciboVO livroFolhaReciboVO = new ControleLivroFolhaReciboVO();
			if (!expedicaoDiplomaVO.getInformarCamposLivroRegistradora()) {
				livroFolhaReciboVO = getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorMatriculaMatriculaPrimeiraEUltimaVia(expedicaoDiplomaVO.getMatricula().getMatricula(), expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), TipoLivroRegistroDiplomaEnum.DIPLOMA, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
				if (Uteis.isAtributoPreenchido(livroFolhaReciboVO)) {
					livroRegistro = livroFolhaReciboVO.getControleLivroRegistroDiploma().getNrLivro().toString().trim();
					numeroFolhaDoDiploma = livroFolhaReciboVO.getFolhaReciboAtual().toString().trim();
					numeroSequenciaDoDiploma = livroFolhaReciboVO.getNumeroRegistro().toString().trim();
				} else {
					livroRegistroDiplomaVO = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorCodigoCurso(expedicaoDiplomaVO.getMatricula().getCurso().getCodigo(), null, TipoLivroRegistroDiplomaEnum.DIPLOMA, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
					if (Uteis.isAtributoPreenchido(livroRegistroDiplomaVO)) {
						livroRegistro = livroRegistroDiplomaVO.getNrLivro().toString().trim();
						numeroFolhaDoDiploma = livroRegistroDiplomaVO.getNrFolhaRecibo().toString().trim();
						numeroSequenciaDoDiploma = livroRegistroDiplomaVO.getNumeroRegistro().toString().trim();
					} else {
						livroRegistro = (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getLivroRegistradora().trim()) ? expedicaoDiplomaVO.getLivroRegistradora().trim() : "");
						numeroFolhaDoDiploma = (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFolhaReciboRegistradora().trim()) ? expedicaoDiplomaVO.getFolhaReciboRegistradora().trim() : "");
						numeroSequenciaDoDiploma = (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroRegistroDiploma().trim()) ? expedicaoDiplomaVO.getNumeroRegistroDiploma().trim() : "");
					}
				}
			}
			if (Uteis.isAtributoPreenchido(livroRegistro) && Uteis.isAtributoPreenchido(numeroFolhaDoDiploma) && Uteis.isAtributoPreenchido(numeroSequenciaDoDiploma)) {
				if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getAluno().getCPF().trim()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCNPJ().trim()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCnpjUnidadeCertificadora().trim()) && Uteis.isAtributoPreenchido(livroRegistro.toString().trim())
						&& Uteis.isAtributoPreenchido(numeroFolhaDoDiploma.toString().trim()) && Uteis.isAtributoPreenchido(numeroSequenciaDoDiploma.toString().trim())) {
					hashCodigoValidacao = Uteis.removerMascara(expedicaoDiplomaVO.getMatricula().getAluno().getCPF().trim()) + Long.valueOf(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()) + Uteis.removerMascara(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCNPJ().trim())
							+ Uteis.removerMascara(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCnpjUnidadeCertificadora().trim() + livroRegistro.toString().trim() + numeroFolhaDoDiploma.toString().trim() + numeroSequenciaDoDiploma.toString().trim());
					hashCodigoValidacao = DigestUtils.sha256Hex(hashCodigoValidacao);
					if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIES()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIESUnidadeCertificadora())) {
						hashCodigoValidacao = StringUtils.join(Arrays.asList(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIES(), ".", expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIESUnidadeCertificadora(), ".", hashCodigoValidacao).toArray());
					}
				}
			} else if (Uteis.isAtributoPreenchido(livroRegistro) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroRegistroDiploma()) && !Uteis.isAtributoPreenchido(numeroFolhaDoDiploma)) {
				if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getAluno().getCPF().trim()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCNPJ().trim()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCnpjUnidadeCertificadora().trim()) && Uteis.isAtributoPreenchido(livroRegistro.toString().trim())
						&& Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getNumeroRegistroDiploma().toString().trim())) {
					hashCodigoValidacao = Uteis.removerMascara(expedicaoDiplomaVO.getMatricula().getAluno().getCPF().trim()) + Long.valueOf(expedicaoDiplomaVO.getMatricula().getCurso().getCodigoEMEC()) + Uteis.removerMascara(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCNPJ().trim())
							+ Uteis.removerMascara(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCnpjUnidadeCertificadora().trim() + livroRegistro.toString().trim() + expedicaoDiplomaVO.getNumeroRegistroDiploma().toString().trim());
					hashCodigoValidacao = DigestUtils.sha256Hex(hashCodigoValidacao);
					if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIES()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIESUnidadeCertificadora())) {
						hashCodigoValidacao = StringUtils.join(Arrays.asList(expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIES(), ".", expedicaoDiplomaVO.getUnidadeEnsinoCertificadora().getCodigoIESUnidadeCertificadora(), ".", hashCodigoValidacao).toArray());
					}
				}
			}
			return hashCodigoValidacao;
		} catch (Exception e) {
			throw e;
		}
	}

	private void preencherDadosSeguranca(TDadosRegistroPorDecisaoJudicial dadosRegistro, ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO) throws Exception {
		TSeguranca seguranca = new TSeguranca();
		String livroRegistro = Constantes.EMPTY;
		String numeroFolhaDoDiploma = Constantes.EMPTY;
		String numeroSequenciaDoDiploma = Constantes.EMPTY;
		if (!dadosRegistro.getLivroRegistro().getContent().isEmpty()) {
			if (dadosRegistro.getLivroRegistro().getContent().stream().anyMatch(l -> l.getName().equals(new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "LivroRegistro")))) {
				JAXBElement<?> element = dadosRegistro.getLivroRegistro().getContent().stream().filter(l -> l.getName().equals(new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "LivroRegistro"))).collect(Collectors.toList()).get(0);
				if (element != null && element.getValue() != null && Uteis.isAtributoPreenchido(element.getValue().toString())) {
					livroRegistro = (String) element.getValue();
				}
			}
			if (dadosRegistro.getLivroRegistro().getContent().stream().anyMatch(l -> l.getName().equals(new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "NumeroFolhaDoDiploma")))) {
				JAXBElement<?> element = dadosRegistro.getLivroRegistro().getContent().stream().filter(l -> l.getName().equals(new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "NumeroFolhaDoDiploma"))).collect(Collectors.toList()).get(0);
				if (element != null && element.getValue() != null && Uteis.isAtributoPreenchido(element.getValue().toString())) {
					numeroFolhaDoDiploma = (String) element.getValue();
				}
			}
			if (dadosRegistro.getLivroRegistro().getContent().stream().anyMatch(l -> l.getName().equals(new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "NumeroSequenciaDoDiploma")))) {
				JAXBElement<?> element = dadosRegistro.getLivroRegistro().getContent().stream().filter(l -> l.getName().equals(new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "NumeroSequenciaDoDiploma"))).collect(Collectors.toList()).get(0);
				if (element != null && element.getValue() != null && Uteis.isAtributoPreenchido(element.getValue().toString())) {
					numeroSequenciaDoDiploma = (String) element.getValue();
				}
			}
		}
		if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital()) || !expedicaoDiplomaVO.getNovaGeracaoRepresentacaoVisualDiplomaDigital()) {
			String hashCodigoValidacao = realizarGeracaoHashCodigoValidacao(expedicaoDiplomaVO, livroRegistro, numeroFolhaDoDiploma, numeroSequenciaDoDiploma, usuarioVO);
			if (Uteis.isAtributoPreenchido(hashCodigoValidacao)) {
				seguranca.setCodigoValidacao(hashCodigoValidacao);
				expedicaoDiplomaVO.setCodigoValidacaoDiplomaDigital(hashCodigoValidacao);
			} else {
				seguranca.setCodigoValidacao(Constantes.EMPTY);
				expedicaoDiplomaVO.setCodigoValidacaoDiplomaDigital(Constantes.EMPTY);
			}
		} else {
			seguranca.setCodigoValidacao(expedicaoDiplomaVO.getCodigoValidacaoDiplomaDigital());
		}
		dadosRegistro.setSeguranca(seguranca);
	}

	private TFiliacao criarDadosPrivadosDiplomadoFiliacaoDocumentacao(ExpedicaoDiplomaVO expedicaoDiplomaVO) {
		TFiliacao filiacao = new TFiliacao();
		filiacao.getGenitor().addAll(expedicaoDiplomaVO.getMatricula().getAluno().getFiliacaoVOs().stream().filter(filiacaoVO -> !"RL".equals(filiacaoVO.getTipo())).map(obj -> {
			TPessoa pessoa = new TPessoa();
			pessoa.setNome(obj.getNome().trim());
			pessoa.setSexo("MA".equals(obj.getTipo().trim()) ? TSexo.F : TSexo.M);
			return pessoa;
		}).collect(Collectors.toList()));
		return filiacao;
	}

	private void preencherHistoricoEscolarDocumentacaoSegundaVia(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, TDadosPrivadosDiplomadoPorDecisaoJudicial dadosPrivadosDiplomadoDecisaoJudicial, ConsistirException ce) throws Exception {
		dadosPrivadosDiplomadoDecisaoJudicial.setHistoricoEscolar(getHistoricoEscolarDocumentacaoSegundaVia(expedicaoDiplomaVO, usuarioVO, ce));
	}
	
	private void preencherHistoricoEscolarDocumentacaoSegundaVia(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, TDadosPrivadosDiplomadoSegundaVia dadosPrivadosDiplomadoSegundaVia, ConsistirException ce) throws Exception {
		dadosPrivadosDiplomadoSegundaVia.setHistoricoEscolar(getHistoricoEscolarDocumentacaoSegundaVia(expedicaoDiplomaVO, usuarioVO, ce));
	}
	
	private THistoricoEscolarSegundaVia getHistoricoEscolarDocumentacaoSegundaVia(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		HistoricoAlunoRelVO histAlunoRelVO = getHistoricoAlunoRelVO(expedicaoDiplomaVO, usuarioVO);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs()), "Não foram encontradas DISCIPLINAS para a geração do xml", ce);
		if (Uteis.isAtributoPreenchido(histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs())) {
			THistoricoEscolarSegundaVia historicoEscolar = new THistoricoEscolarSegundaVia();
			historicoEscolar.setDataEmissaoHistorico(getDataConvertidaXML(new Date()));
			historicoEscolar.setHoraEmissaoHistorico(Uteis.getHoraAtualComSegundos());
			historicoEscolar.setSituacaoAtualDiscente(getSituacaoAlunoHistorico(histAlunoRelVO.getSituacaoFinal(), expedicaoDiplomaVO));
			historicoEscolar.setIngressoCurso(preencherDadosIngressoCursoHistoricoEscolarSegundaVia(expedicaoDiplomaVO, ce));
			preencherDadosCodigoCurriculoHistoricoEscolarSegundaVia(expedicaoDiplomaVO, historicoEscolar, ce);
			preencherHistoricoEscolarMatrizCurricularDocumentacaoSegundaVia(expedicaoDiplomaVO, histAlunoRelVO, usuarioVO, historicoEscolar, ce);
			preencherDadosAreasHistoricoEscolarSegundaVia(histAlunoRelVO, historicoEscolar);
			preencherEnadeHistoricoEscolarSegundaVia(historicoEscolar, histAlunoRelVO, expedicaoDiplomaVO, usuarioVO, ce);
			preencherDadosCargaHorariaCursoIntegralizadaHistoricoEscolarSegundaVia(historicoEscolar, expedicaoDiplomaVO, histAlunoRelVO, ce);
			return historicoEscolar;
		}
		return null;
	}

	private void preencherDadosDocumentacaoAcademicaInformacoesProcessoJudicial(TDadosPrivadosDiplomadoPorDecisaoJudicial dadosRegistro, ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce) {
		dadosRegistro.setInformacoesProcessoJudicial(preencherDadosInformacoesProcessoJudicial(expedicaoDiplomaVO, ce));
	}

	private void preencherDadosCodigoCurriculoHistoricoEscolarSegundaVia(ExpedicaoDiplomaVO expedicaoDiplomaVO, THistoricoEscolarSegundaVia historicoEscolarSegundaVia, ConsistirException ce) {
		historicoEscolarSegundaVia.setCodigoCurriculo(getCodigoCurriculoHistoricoEscolar(expedicaoDiplomaVO.getGradeCurricularVO(), Boolean.FALSE, ce));
	}
	
	private String getCodigoCurriculoHistoricoEscolar(GradeCurricularVO gradeCurricular, Boolean validarDados, ConsistirException ce) {
		Uteis.checkStateList(validarDados && !Uteis.isAtributoPreenchido(gradeCurricular), "A GRADE CURRICULAR deve ser informada (Dados do Histórico Escolar)", ce);
		if (Uteis.isAtributoPreenchido(gradeCurricular)) {
			return getStringXml(gradeCurricular.getCodigo().toString());
		} else {
			return null;
		}
	}

	private void preencherHistoricoEscolarMatrizCurricularDocumentacaoSegundaVia(ExpedicaoDiplomaVO expedicaoDiplomaVO, HistoricoAlunoRelVO histAlunoRelVO, UsuarioVO usuarioVO, THistoricoEscolarSegundaVia historicoEscolar, ConsistirException ce) throws Exception {
		try {
			historicoEscolar.setElementosHistorico(new TElementosHistoricoSegundaViaNatoFisico());
			List<RegistroAtividadeComplementarMatriculaVO> registroAtividadeComplementarMatriculaVOs = getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarPorMatriculaSituacao(expedicaoDiplomaVO.getMatricula().getMatricula(), SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO, Boolean.FALSE, usuarioVO);
			historicoEscolar.getElementosHistorico().getDisciplinaOrAtividadeComplementarOrEstagio().addAll(histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().stream().filter(d -> Uteis.isAtributoPreenchido(d.getCodigoDisciplina()) && !d.getDisciplinaEstagio()).map(disciplina -> criarHistoricoEscolarSegundaViaMatrizCurricularDisciplinaCursadaDocumentacao(disciplina, expedicaoDiplomaVO, ce)).collect(Collectors.toList()));
			if (!registroAtividadeComplementarMatriculaVOs.isEmpty()) {
				historicoEscolar.getElementosHistorico().getDisciplinaOrAtividadeComplementarOrEstagio().addAll(registroAtividadeComplementarMatriculaVOs.stream().map(atividadeComplementar -> criarHistoricoEscolarSegundaViaAtividadeComplementar(atividadeComplementar, expedicaoDiplomaVO, usuarioVO, ce)).collect(Collectors.toList()));
			}
			List<EstagioVO> estagioVOs = getFacadeFactory().getEstagioFacade().consultarPorMatriculaAluno(expedicaoDiplomaVO.getMatricula().getMatricula(), SituacaoEstagioEnum.DEFERIDO, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(estagioVOs)) {
				historicoEscolar.getElementosHistorico().getDisciplinaOrAtividadeComplementarOrEstagio().addAll(estagioVOs.stream().map(estagio -> criarHistoricoEscolarEstagioSegundaViaNatoFisica(estagio, usuarioVO, ce)).collect(Collectors.toList()));
			} else {
				if (Uteis.isAtributoPreenchido(histAlunoRelVO.getListaHistoricoAlunoDisciplinaEstagioRelVOs())) {
					historicoEscolar.getElementosHistorico().getDisciplinaOrAtividadeComplementarOrEstagio().addAll(criarHistoricoEscolarEstagioSegundaViaNatoFisicaPartirHistorico(histAlunoRelVO.getListaHistoricoAlunoDisciplinaEstagioRelVOs(), expedicaoDiplomaVO, ce));
				} else {
					historicoEscolar.getElementosHistorico().getDisciplinaOrAtividadeComplementarOrEstagio().addAll(criarHistoricoEscolarEstagioSegundaViaNatoFisicaPartirHistorico(histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs(), expedicaoDiplomaVO, ce));
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void preencherDadosAreasHistoricoEscolar(HistoricoAlunoRelVO histAlunoRelVO, THistoricoEscolar historicoEscolar) {
		historicoEscolar.setAreas(getAreasHistoricoEscolar(histAlunoRelVO.getMatriculaVO().getCurso()));
		if (Objects.nonNull(historicoEscolar.getAreas()) && Uteis.isAtributoPreenchido(historicoEscolar.getAreas().getArea())) {
			historicoEscolar.setNomeParaAreas("Area de Conhecimento");
		}
	}
	
	private void preencherDadosAreasHistoricoEscolarSegundaVia(HistoricoAlunoRelVO histAlunoRelVO, THistoricoEscolarSegundaVia historicoEscolarSegundaVia) {
		historicoEscolarSegundaVia.setAreas(getAreasHistoricoEscolar(histAlunoRelVO.getMatriculaVO().getCurso()));
		if (Objects.nonNull(historicoEscolarSegundaVia.getAreas()) && Uteis.isAtributoPreenchido(historicoEscolarSegundaVia.getAreas().getArea())) {
			historicoEscolarSegundaVia.setNomeParaAreas("Area de Conhecimento");
		}
	}
	
	private TAreasComNome getAreasHistoricoEscolar(CursoVO curso) {
		TAreaComNome areaComNome = new TAreaComNome();
		TAreasComNome areas = new TAreasComNome();
		if (Uteis.isAtributoPreenchido(curso.getAreaConhecimento())) {
			areaComNome.setCodigo(getStringXml(curso.getAreaConhecimento().getCodigo().toString().trim()));
			areaComNome.setNome(getStringXml(curso.getAreaConhecimento().getNome().trim()));
			areas.getArea().add(areaComNome);
			return areas;
		} else {
			return null;
		}
	}

	public void preencherEnadeHistoricoEscolarSegundaVia(THistoricoEscolarSegundaVia historicoEscolarSegundaVia, HistoricoAlunoRelVO histAlunoRelVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		historicoEscolarSegundaVia.setENADE(getEnadeHistoricoEscolar(histAlunoRelVO, expedicaoDiplomaVO, Boolean.FALSE, usuarioVO, ce));
	}
	
	public TEnade getEnadeHistoricoEscolar(HistoricoAlunoRelVO histAlunoRelVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, Boolean validarDados, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		List<MatriculaEnadeVO> matriculaEnadeVOs = getFacadeFactory().getMatriculaEnadeFacade().consultarMatriculaEnadePorMatricula(histAlunoRelVO.getMatriculaVO().getMatricula(), usuarioVO, Boolean.FALSE);
		if (Uteis.isAtributoPreenchido(matriculaEnadeVOs)) {
			TEnade tEnade = new TEnade();
			for (MatriculaEnadeVO matriculaEnadeVO : matriculaEnadeVOs) {
				criarEnadesHistoricoEscolar(matriculaEnadeVO, expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getApresentarTextoEnade(), tEnade, ce);
			}
			return tEnade;
		} else {
			if (validarDados) {
				ce.getListaMensagemErro().add("O ENADE da MATRÍCULA do ALUNO deve ser informado " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem());
			}
			return null;
		}
	}

	private void criarEnadesHistoricoEscolar(MatriculaEnadeVO matriculaEnadeVO, Boolean apresentarTextoEnade, TEnade tEnade, ConsistirException ce) {
		if (Uteis.isAtributoPreenchido(matriculaEnadeVO.getCondicaoEnade()) && (matriculaEnadeVO.getCondicaoEnade().equals(TEnumCondicaoEnade.INGRESSANTE) || matriculaEnadeVO.getCondicaoEnade().equals(TEnumCondicaoEnade.CONCLUINTE))) {
			if (matriculaEnadeVO.getTextoEnade().getTipoTextoEnade().equals(TipoTextoEnadeEnum.REALIZACAO) || matriculaEnadeVO.getTextoEnade().getTipoTextoEnade().equals(TipoTextoEnadeEnum.DISPENSA)) {
				preencherDadosEnadeHabilitado(matriculaEnadeVO, apresentarTextoEnade, tEnade, ce);
			} else if (matriculaEnadeVO.getTextoEnade().getTipoTextoEnade().equals(TipoTextoEnadeEnum.NAO_COMPARECIMENTO) || matriculaEnadeVO.getTextoEnade().getTipoTextoEnade().equals(TipoTextoEnadeEnum.NAO_PARTICIPANTE)) {
				preencherDadosEnadeIrregular(matriculaEnadeVO, apresentarTextoEnade, tEnade, ce);
			} else {
				preencherDadosEnadeNaoHabilitado(matriculaEnadeVO, tEnade, ce);
			}
		}
	}

	private void preencherDadosEnadeNaoHabilitado(MatriculaEnadeVO matriculaEnadeVO, TEnade tEnade, ConsistirException ce) {
		TEnadeNaoHabilitado tInformacoesEnade = new TEnadeNaoHabilitado();
		ObjectFactory factory = new ObjectFactory();
		String exceptionCondicaoEnade = "A CONDIÇÃO do ENADE deve ser informado, enade: " + matriculaEnadeVO.getEnadeVO().getTituloEnade().toUpperCase() + " " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem();
		String exceptionDataEnade = "A DATA da PROVA do ENADE deve ser informado, enade: " + matriculaEnadeVO.getEnadeVO().getTituloEnade().toUpperCase() + " " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem();
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(matriculaEnadeVO.getCondicaoEnade()), exceptionCondicaoEnade, ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(matriculaEnadeVO.getEnadeVO().getDataProva()), exceptionDataEnade, ce);
		tInformacoesEnade.setCondicao(Uteis.isAtributoPreenchido(matriculaEnadeVO.getCondicaoEnade()) ? matriculaEnadeVO.getCondicaoEnade() : null);
		tInformacoesEnade.setEdicao(Uteis.isAtributoPreenchido(matriculaEnadeVO.getEnadeVO().getDataProva()) ? UteisData.getAnoDataString(matriculaEnadeVO.getEnadeVO().getDataProva()) : null);
		tInformacoesEnade.setMotivo(getMotivoNaoHabilitado(matriculaEnadeVO.getTextoEnade().getTipoTextoEnade()));
		JAXBElement<TEnadeNaoHabilitado> element = factory.createTEnadeNaoHabilitado(tInformacoesEnade);
		tEnade.getHabilitadoOrNaoHabilitadoOrIrregular().add(element);
	}

	private TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico getMotivoNaoHabilitado(TipoTextoEnadeEnum tipoTextoEnade) {
		if (tipoTextoEnade.equals(TipoTextoEnadeEnum.ESTUDANTE_NAO_HABILITADO_AO_ENADE_EM_RAZAO_DA_NATUREZA_DO_PROJETO_PEDAGOGICO_DO_CURSO)) {
			return TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico.ESTUDANTE_NÃO_HABILITADO_AO_ENADE_EM_RAZÃO_DA_NATUREZA_DO_PROJETO_PEDAGÓGICO_DO_CURSO;
		} else if (tipoTextoEnade.equals(TipoTextoEnadeEnum.ESTUDANTE_NAO_HABILITADO_AO_ENADE_EM_RAZAO_DO_CALENDARIO_DO_CICLO_AVALIATIVO)) {
			return TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico.ESTUDANTE_NÃO_HABILITADO_AO_ENADE_EM_RAZÃO_DO_CALENDÁRIO_DO_CICLO_AVALIATIVO;
		} else {
			return TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico.CALENDARIO_TRIENAL;
		}
	}

	private void preencherDadosEnadeIrregular(MatriculaEnadeVO matriculaEnadeVO, Boolean apresentarTextoEnade, TEnade tEnade, ConsistirException ce) {
		TInformacoesEnade tInformacoesEnade = new TInformacoesEnade();
		ObjectFactory factory = new ObjectFactory();
		String exceptionCondicaoEnade = "A CONDIÇÃO do ENADE deve ser informado, enade: " + matriculaEnadeVO.getEnadeVO().getTituloEnade().toUpperCase() + " " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem();
		String exceptionDataEnade = "A DATA da PROVA do ENADE deve ser informado, enade: " + matriculaEnadeVO.getEnadeVO().getTituloEnade().toUpperCase() + " " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem();
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(matriculaEnadeVO.getCondicaoEnade()), exceptionCondicaoEnade, ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(matriculaEnadeVO.getEnadeVO().getDataProva()), exceptionDataEnade, ce);
		tInformacoesEnade.setCondicao(Uteis.isAtributoPreenchido(matriculaEnadeVO.getCondicaoEnade()) ? matriculaEnadeVO.getCondicaoEnade() : null);
		tInformacoesEnade.setEdicao(Uteis.isAtributoPreenchido(matriculaEnadeVO.getEnadeVO().getDataProva()) ? UteisData.getAnoDataString(matriculaEnadeVO.getEnadeVO().getDataProva()) : null);
		tInformacoesEnade.setMotivo(apresentarTextoEnade ? (Uteis.isAtributoPreenchido(matriculaEnadeVO.getTextoEnade().getTexto()) ? matriculaEnadeVO.getTextoEnade().getTexto() : null) : null);
		JAXBElement<TInformacoesEnade> element = factory.createTEnadeIrregular(tInformacoesEnade);
		tEnade.getHabilitadoOrNaoHabilitadoOrIrregular().add(element);
	}

	private void preencherDadosEnadeHabilitado(MatriculaEnadeVO matriculaEnadeVO, Boolean apresentarTextoEnade, TEnade tEnade, ConsistirException ce) {
		TInformacoesEnade tInformacoesEnade = new TInformacoesEnade();
		ObjectFactory factory = new ObjectFactory();
		String exceptionCondicaoEnade = "A CONDIÇÃO do ENADE deve ser informado, enade: " + matriculaEnadeVO.getEnadeVO().getTituloEnade().toUpperCase() + " " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem();
		String exceptionDataEnade = "A DATA da PROVA do ENADE deve ser informado, enade: " + matriculaEnadeVO.getEnadeVO().getTituloEnade().toUpperCase() + " " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem();
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(matriculaEnadeVO.getCondicaoEnade()), exceptionCondicaoEnade, ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(matriculaEnadeVO.getEnadeVO().getDataProva()), exceptionDataEnade, ce);
		tInformacoesEnade.setCondicao(Uteis.isAtributoPreenchido(matriculaEnadeVO.getCondicaoEnade()) ? matriculaEnadeVO.getCondicaoEnade() : null);
		tInformacoesEnade.setEdicao(Uteis.isAtributoPreenchido(matriculaEnadeVO.getEnadeVO().getDataProva()) ? UteisData.getAnoDataString(matriculaEnadeVO.getEnadeVO().getDataProva()) : null);
		tInformacoesEnade.setMotivo(apresentarTextoEnade ? (Uteis.isAtributoPreenchido(matriculaEnadeVO.getTextoEnade().getTexto()) ? matriculaEnadeVO.getTextoEnade().getTexto() : null) : null);
		JAXBElement<TInformacoesEnade> element = factory.createTEnadeHabilitado(tInformacoesEnade);
		tEnade.getHabilitadoOrNaoHabilitadoOrIrregular().add(element);
	}

	private TSituacaoAtualDiscente getSituacaoAlunoHistorico(String situacaoFinal, ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception {
		TSituacaoAtualDiscente tSituacaoAtualDiscente = new TSituacaoAtualDiscente();
		if (situacaoFinal.equals("Formado")) {
			TSituacaoFormado tSituacaoFormado = new TSituacaoFormado();
			tSituacaoFormado.setDataColacaoGrau(getDataConvertidaXML(expedicaoDiplomaVO.getMatricula().getDataColacaoGrau()));
			tSituacaoFormado.setDataConclusaoCurso(getDataConvertidaXML(expedicaoDiplomaVO.getMatricula().getDataConclusaoCurso()));
			tSituacaoFormado.setDataExpedicaoDiploma(getDataConvertidaXML(expedicaoDiplomaVO.getDataExpedicao()));
			tSituacaoAtualDiscente.setFormado(tSituacaoFormado);
		} else {
			switch (situacaoFinal) {
			case "Ativa":
				tSituacaoAtualDiscente.setMatriculadoEmDisciplina(new TVazio());
				break;

			case "Trancada":
				tSituacaoAtualDiscente.setTrancamento(new TVazio());
				break;

			case "Abandono de Curso":
				tSituacaoAtualDiscente.setAbandono(new TVazio());
				break;

			case "Jubilado":
				tSituacaoAtualDiscente.setJubilado(new TVazio());
				break;

			default:
				tSituacaoAtualDiscente.setOutraSituacao(situacaoFinal);
				break;
			}
		}
		return tSituacaoAtualDiscente;
	}

	public void preencherDadosCargaHorariaCursoIntegralizadaHistoricoEscolar(THistoricoEscolar historicoEscolar, ExpedicaoDiplomaVO expedicaoDiplomaVO, HistoricoAlunoRelVO histAlunoRelVO, ConsistirException ce) throws Exception {
		historicoEscolar.setCargaHorariaCursoIntegralizada(getCargaHorariaIntegralizada(expedicaoDiplomaVO, histAlunoRelVO, ce));
		historicoEscolar.setCargaHorariaCurso(getCargaHorariaCurso(expedicaoDiplomaVO, histAlunoRelVO, ce));
	}
	
	public void preencherDadosCargaHorariaCursoIntegralizadaHistoricoEscolarSegundaVia(THistoricoEscolarSegundaVia historicoEscolarSegundaVia, ExpedicaoDiplomaVO expedicaoDiplomaVO, HistoricoAlunoRelVO histAlunoRelVO, ConsistirException ce) throws Exception {
		historicoEscolarSegundaVia.setCargaHorariaCursoIntegralizada(getCargaHorariaIntegralizada(expedicaoDiplomaVO, histAlunoRelVO, ce));
		historicoEscolarSegundaVia.setCargaHorariaCurso(getCargaHorariaCurso(expedicaoDiplomaVO, histAlunoRelVO, ce));
	}
	
	private TCargaHoraria getCargaHorariaIntegralizada(ExpedicaoDiplomaVO expedicaoDiplomaVO, HistoricoAlunoRelVO histAlunoRelVO, ConsistirException ce) {
		TCargaHoraria cargaHorariaCursoIntegralizada = new TCargaHoraria();
		Integer cargaHorariaEstagio = 0;
		Integer cargaHorariaDisciplinas = 0;
		Boolean utilizarCargaHorariaEstagioModulo = histAlunoRelVO.getCargaHorariaUtilizar().equals("COMPONENTE_ESTAGIO");
		Predicate<HistoricoAlunoDisciplinaRelVO> disciplinasAprovadas = h -> h.getSituacaoHistoricoEnum() != null && h.getSituacaoHistoricoEnum().getHistoricoAprovado() && !h.getDisciplinaAtividadeComplementar();
		Predicate<HistoricoAlunoDisciplinaRelVO> disciplinasEstagiosAprovadas = h -> h.getSituacaoHistoricoEnum() != null && h.getSituacaoHistoricoEnum().getHistoricoAprovado() && h.getDisciplinaEstagio() && !h.getDisciplinaAtividadeComplementar();
		Predicate<HistoricoAlunoDisciplinaRelVO> disciplinasNormaisAprovadas = null;
		if (Uteis.isAtributoPreenchido(histAlunoRelVO.getListaHistoricoAlunoDisciplinaTccRelVOs())) {
			disciplinasNormaisAprovadas = h -> h.getSituacaoHistoricoEnum() != null && h.getSituacaoHistoricoEnum().getHistoricoAprovado() && h.getDisciplinaEstagio().equals(Boolean.FALSE) && h.getDisciplinaTcc().equals(Boolean.FALSE) && !h.getDisciplinaAtividadeComplementar();
		} else {
			disciplinasNormaisAprovadas = h -> h.getSituacaoHistoricoEnum() != null && h.getSituacaoHistoricoEnum().getHistoricoAprovado() && h.getDisciplinaEstagio().equals(Boolean.FALSE) && !h.getDisciplinaAtividadeComplementar();
		}
		if (utilizarCargaHorariaEstagioModulo) {
			cargaHorariaEstagio = histAlunoRelVO.getCargaHorariaAprovadaEstagio();
			cargaHorariaDisciplinas = histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().stream().filter(disciplinasNormaisAprovadas).map(c -> Integer.valueOf(c.getChDisciplina())).reduce(0, Integer::sum);
			if (Uteis.isAtributoPreenchido(histAlunoRelVO.getListaHistoricoAlunoDisciplinaTccRelVOs())) {
				cargaHorariaDisciplinas += histAlunoRelVO.getListaHistoricoAlunoDisciplinaTccRelVOs().stream().filter(disciplinasAprovadas).map(c -> Integer.valueOf(c.getChDisciplina())).reduce(0, Integer::sum);
			}
		} else {
			if (Uteis.isAtributoPreenchido(histAlunoRelVO.getListaHistoricoAlunoDisciplinaEstagioRelVOs())) {
				cargaHorariaEstagio = histAlunoRelVO.getListaHistoricoAlunoDisciplinaEstagioRelVOs().stream().filter(disciplinasEstagiosAprovadas).map(d -> Integer.valueOf(d.getChDisciplina())).reduce(0, Integer::sum);
			} else if (Uteis.isAtributoPreenchido(histAlunoRelVO.getListaHistoricoAlunoComponenteEstagioRelVOs())) {
				cargaHorariaEstagio = histAlunoRelVO.getListaHistoricoAlunoComponenteEstagioRelVOs().stream().filter(e -> e.getSituacao().equals("APROVADO")).map(e -> e.getCargaHorariaAprovada()).reduce(0, Integer::sum);
			} else {
				cargaHorariaEstagio = histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().stream().filter(disciplinasEstagiosAprovadas).map(d -> Integer.valueOf(d.getChDisciplina())).reduce(0, Integer::sum);
			}
			cargaHorariaDisciplinas = histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().stream().filter(disciplinasNormaisAprovadas).map(c -> Integer.valueOf(c.getChDisciplina())).reduce(0, Integer::sum);
			if (Uteis.isAtributoPreenchido(histAlunoRelVO.getListaHistoricoAlunoDisciplinaTccRelVOs())) {
				cargaHorariaDisciplinas += histAlunoRelVO.getListaHistoricoAlunoDisciplinaTccRelVOs().stream().filter(disciplinasAprovadas).map(c -> Integer.valueOf(c.getChDisciplina())).reduce(0, Integer::sum);
			}
		}
		Integer calculoCargaHoraria = 0;
		if ((Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital()) && expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getConsiderarCargaHorariaForaGrade())) {
			calculoCargaHoraria += cargaHorariaDisciplinas;
		} else {
			calculoCargaHoraria += (cargaHorariaDisciplinas - histAlunoRelVO.getCargaHorariaDisciplinaForaGrade());
		}
		Integer cargaHorariaCursada = (calculoCargaHoraria + (Uteis.isAtributoPreenchido(histAlunoRelVO.getCargaHorariaRealizadaAtividadeComplementar()) ? Integer.valueOf(histAlunoRelVO.getCargaHorariaRealizadaAtividadeComplementar()) : 0) + cargaHorariaEstagio);
		if (!Uteis.isAtributoPreenchido(cargaHorariaCursada)) {
			ce.getListaMensagemErro().add("Não foi possível realizar a montagem da carga horária de curso integralizada do aluno " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem());
			return null;
		} else {
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital())) {
				if (expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getFormatoCargaHorariaXML().equals(FormatoCargaHorariaXmlEnum.HORA_AULA)) {
					cargaHorariaCursoIntegralizada.setHoraAula(Long.valueOf(cargaHorariaCursada));
				} else {
					cargaHorariaCursoIntegralizada.setHoraRelogio(getHoraRelogio(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital(), cargaHorariaCursada.toString().trim()));
				}
			} else {
				cargaHorariaCursoIntegralizada.setHoraAula(Long.valueOf(cargaHorariaCursada));
			}
			return cargaHorariaCursoIntegralizada;
		}
	}
	
	public TCargaHoraria getCargaHorariaCurso(ExpedicaoDiplomaVO expedicaoDiplomaVO, HistoricoAlunoRelVO histAlunoRelVO, ConsistirException ce) throws Exception {
		if (!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getCargaHorariaTotal())) {
			ce.getListaMensagemErro().add("Não foi possível realizar a montagem da carga horária de curso do aluno " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem());
			return null;
		} else {
			TCargaHoraria cargaHorariaCurso = new TCargaHoraria();
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital())) {
				if (expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getFormatoCargaHorariaXML().equals(FormatoCargaHorariaXmlEnum.HORA_AULA)) {
					cargaHorariaCurso.setHoraAula(Long.valueOf(expedicaoDiplomaVO.getCargaHorariaTotal()));
				} else {
					cargaHorariaCurso.setHoraRelogio(getHoraRelogio(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital(), expedicaoDiplomaVO.getCargaHorariaTotal().toString().trim()));
				}
			} else {
				cargaHorariaCurso.setHoraAula(Long.valueOf(expedicaoDiplomaVO.getCargaHorariaTotal()));
			}
			return cargaHorariaCurso;
		}
	}
	
	public THistoricoEscolarSegundaVia.IngressoCurso preencherDadosIngressoCursoHistoricoEscolarSegundaVia(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce) throws Exception {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getDataInicioCurso()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getFormaIngresso())) {
			THistoricoEscolarSegundaVia.IngressoCurso ingressoCurso = new THistoricoEscolarSegundaVia.IngressoCurso();
			TFormaAcessoCurso formaAcesso = obterSituacaoIngressoCurso(expedicaoDiplomaVO.getMatricula());
			ingressoCurso.setData(getDataConvertidaXML(expedicaoDiplomaVO.getMatricula().getDataInicioCurso()));
			ingressoCurso.getFormaAcesso().add(formaAcesso);
			return ingressoCurso;
		} else {
			return null;
		}
	}

	public IngressoCurso preencherDadosIngressoCursoHistoricoEscolar(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce) throws Exception {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getDataInicioCurso()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getFormaIngresso())) {
			IngressoCurso ingressoCurso = new IngressoCurso();
			ingressoCurso.setData(getDataConvertidaXML(expedicaoDiplomaVO.getMatricula().getDataInicioCurso()));
			ingressoCurso.setFormaAcesso(obterSituacaoIngressoCurso(expedicaoDiplomaVO.getMatricula()));
			return ingressoCurso;
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getDataInicioCurso()), "A DATA de INÍCIO do CURSO da MATRÍCULA deve ser informado " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem(), ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getFormaIngresso()), "A FORMA de INGRESSO da MATRÍCULA deve ser informado " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem(), ce);
			return null;
		}
	}

	private TFormaAcessoCurso obterSituacaoIngressoCurso(MatriculaVO matriculaVO) throws Exception {
		switch (matriculaVO.getFormaIngresso()) {
		case "VE":
			return (TFormaAcessoCurso.VESTIBULAR);
		case "ET":
			return (TFormaAcessoCurso.SELEÇÃO_SIMPLIFICADA);
		case "TI":
			return (TFormaAcessoCurso.SELEÇÃO_SIMPLIFICADA);
		case "VR":
			return (TFormaAcessoCurso.SELEÇÃO_PARA_VAGAS_REMANESCENTES);
		case "VP":
			return (TFormaAcessoCurso.SELEÇÃO_PARA_VAGAS_DE_PROGRAMAS_ESPECIAIS);
		case "TE":
			return (TFormaAcessoCurso.SELEÇÃO_PARA_VAGAS_REMANESCENTES);
		case "TO":
			return (TFormaAcessoCurso.TRANSFERÊNCIA_EX_OFFICIO);
		case "PS":
			return (TFormaAcessoCurso.VESTIBULAR);
		case "PR":
			return (TFormaAcessoCurso.VESTIBULAR);
		case "AS":
			return (TFormaAcessoCurso.AVALIAÇÃO_SERIADA);
		case "EN":
			return (TFormaAcessoCurso.ENEM);
		case "PE":
			return (TFormaAcessoCurso.PEC_G);
		case "PD":
			return (TFormaAcessoCurso.SELEÇÃO_SIMPLIFICADA);
		case "RE":
			return (TFormaAcessoCurso.SELEÇÃO_SIMPLIFICADA);
		case "DJ":
			return (TFormaAcessoCurso.DECISÃO_JUDICIAL);
		case "OS":
			return (TFormaAcessoCurso.SELEÇÃO_SIMPLIFICADA);
		case "SS":
			return (TFormaAcessoCurso.SELEÇÃO_SIMPLIFICADA);
		case "FI":
			return (TFormaAcessoCurso.SELEÇÃO_SIMPLIFICADA);
		case "RC":
			return (TFormaAcessoCurso.SELEÇÃO_SIMPLIFICADA);
		default:
			return (TFormaAcessoCurso.SELEÇÃO_SIMPLIFICADA);
		}
	}

	private Object criarHistoricoEscolarSegundaViaMatrizCurricularDisciplinaCursadaDocumentacao(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce) {
		validarDadosHistoricoDisciplina(historicoAlunoDisciplinaRelVO, ce);
		TEntradaHistoricoDisciplinaSegundaViaNatoFisica tEntradaHistoricoDisciplina = new TEntradaHistoricoDisciplinaSegundaViaNatoFisica();
		tEntradaHistoricoDisciplina.setCodigoDisciplina(getStringXml(historicoAlunoDisciplinaRelVO.getCodigoDisciplina().toString()));
		tEntradaHistoricoDisciplina.setNomeDisciplina(getStringXml(historicoAlunoDisciplinaRelVO.getNomeDisciplina()));
		tEntradaHistoricoDisciplina.getCargaHoraria().addAll(preencherDadosCargaHorariaEntradaHistoricoDisciplina(historicoAlunoDisciplinaRelVO, expedicaoDiplomaVO));
		tEntradaHistoricoDisciplina.setPeriodoLetivo(preencherDadosPeriodoLetivoEntradaHistoricoDisciplina(historicoAlunoDisciplinaRelVO, expedicaoDiplomaVO, ce));
		preencherDadosDisciplinaAprovadaEntradaHistoricoDisciplinaSegundaVia(historicoAlunoDisciplinaRelVO, tEntradaHistoricoDisciplina);
		preencherDadosNotaNotaConceitoEntradaHistoricoDisciplinaSegundaVia(historicoAlunoDisciplinaRelVO, tEntradaHistoricoDisciplina, ce);
		preencherDadosDocenteEntradaHistoricoDisciplinaSegundaVia(historicoAlunoDisciplinaRelVO, expedicaoDiplomaVO, tEntradaHistoricoDisciplina, ce);
		return tEntradaHistoricoDisciplina;
	}

	public void validarDadosHistoricoDisciplina(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, ConsistirException ce) {
		String exception = "disciplina: " + historicoAlunoDisciplinaRelVO.getNomeDisciplina().toUpperCase() + (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getAnoSemstre()) ? ", ano/semestre: " + historicoAlunoDisciplinaRelVO.getAnoSemstre() : Constantes.EMPTY) + " " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem();
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getChDisciplina()), "A CARGA HORÁRIA da DISCIPLINA deve ser informado, " + exception, ce);
	}

	public List<TCargaHorariaComEtiqueta> preencherDadosCargaHorariaEntradaHistoricoDisciplina(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, ExpedicaoDiplomaVO expedicaoDiploma){
    	List<Integer> cargasHorarias = new ArrayList<Integer>(0);
    	List<TCargaHorariaComEtiqueta> cargaHorariaComEtiquetas = new ArrayList<>(0);
    	Boolean formatoCargaHorariaHoraAula = !Uteis.isAtributoPreenchido(expedicaoDiploma.getConfiguracaoDiplomaDigital()) ? Boolean.TRUE : Uteis.isAtributoPreenchido(expedicaoDiploma.getConfiguracaoDiplomaDigital()) && expedicaoDiploma.getConfiguracaoDiplomaDigital().getFormatoCargaHorariaXML().equals(FormatoCargaHorariaXmlEnum.HORA_AULA);
    	if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getChDisciplina())) {
    		TCargaHorariaComEtiqueta tCargaHorariaComEtiqueta = new TCargaHorariaComEtiqueta();
    		if (formatoCargaHorariaHoraAula) {
    			tCargaHorariaComEtiqueta.setHoraAula(Long.parseLong(getStringXml(historicoAlunoDisciplinaRelVO.getChDisciplina())));
    		} else {
    			tCargaHorariaComEtiqueta.setHoraRelogio(getHoraRelogio(expedicaoDiploma.getConfiguracaoDiplomaDigital(), historicoAlunoDisciplinaRelVO.getChDisciplina().trim()));
    		}
    		tCargaHorariaComEtiqueta.setEtiqueta("obrigatoria");
    		cargaHorariaComEtiquetas.add(tCargaHorariaComEtiqueta);
    	}
    	if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getCargaHorariaPratica())) {
    		TCargaHorariaComEtiqueta tCargaHorariaComEtiqueta = new TCargaHorariaComEtiqueta();
    		if (formatoCargaHorariaHoraAula) {
    			tCargaHorariaComEtiqueta.setHoraAula(Long.parseLong(getStringXml(historicoAlunoDisciplinaRelVO.getCargaHorariaPratica().toString())));
    		} else {
    			tCargaHorariaComEtiqueta.setHoraRelogio(getHoraRelogio(expedicaoDiploma.getConfiguracaoDiplomaDigital(), historicoAlunoDisciplinaRelVO.getCargaHorariaPratica().toString().trim()));
    		}
    		tCargaHorariaComEtiqueta.setEtiqueta("pratica");
    		cargasHorarias.add(historicoAlunoDisciplinaRelVO.getCargaHorariaPratica());
    		cargaHorariaComEtiquetas.add(tCargaHorariaComEtiqueta);
    	}
    	Integer teorica = ((Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getChDisciplina()) ? Integer.valueOf(historicoAlunoDisciplinaRelVO.getChDisciplina()) : 0) - (Uteis.isAtributoPreenchido(cargasHorarias) ? cargasHorarias.stream().reduce(0, Integer::sum) : 0));
    	if (Uteis.isAtributoPreenchido(teorica)) {
    		TCargaHorariaComEtiqueta tCargaHorariaComEtiqueta = new TCargaHorariaComEtiqueta();
    		if (formatoCargaHorariaHoraAula) {
    			tCargaHorariaComEtiqueta.setHoraAula(Long.parseLong(getStringXml(teorica.toString())));
    		} else {
    			tCargaHorariaComEtiqueta.setHoraRelogio(getHoraRelogio(expedicaoDiploma.getConfiguracaoDiplomaDigital(), teorica.toString().trim()));
    		}
    		tCargaHorariaComEtiqueta.setEtiqueta("teorica");
    		cargaHorariaComEtiquetas.add(tCargaHorariaComEtiqueta);
    	}
    	return cargaHorariaComEtiquetas;
    }

	private BigDecimal getHoraRelogio(ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigital, String cargaHoraria) {
		if (Uteis.isAtributoPreenchido(configuracaoDiplomaDigital)) {
			Integer hora = ((Integer.valueOf(cargaHoraria) * (configuracaoDiplomaDigital.getHoraRelogio() > 0 ? configuracaoDiplomaDigital.getHoraRelogio() : 60)) / 60);
			BigDecimal bd = new BigDecimal(hora);
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			return bd;
		} else {
			Integer hora = ((Integer.valueOf(cargaHoraria) * 60) / 60);
			BigDecimal bd = new BigDecimal(hora);
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			return bd;
		}
	}
	
//	private BigDecimal getHoraRelogio(ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigital, Double cargaHoraria) {
//		if (Uteis.isAtributoPreenchido(configuracaoDiplomaDigital)) {
//			Double hora = ((cargaHoraria * (configuracaoDiplomaDigital.getHoraRelogio() > 0 ? configuracaoDiplomaDigital.getHoraRelogio() : 60)) / 60);
//			BigDecimal bd = new BigDecimal(hora);
//			bd.setScale(2, BigDecimal.ROUND_HALF_UP);
//			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
//			return bd;
//		} else {
//			Double hora = ((cargaHoraria * 60) / 60);
//			BigDecimal bd = new BigDecimal(hora);
//			bd.setScale(2, BigDecimal.ROUND_HALF_UP);
//			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
//			return bd;
//		}
//	}

	public String preencherDadosPeriodoLetivoEntradaHistoricoDisciplina(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce) {
		String exception = "disciplina: " + historicoAlunoDisciplinaRelVO.getNomeDisciplina().toUpperCase() + (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getAnoSemstre()) ? ", ano/semestre: " + historicoAlunoDisciplinaRelVO.getAnoSemstre() : Constantes.EMPTY) + " (Dados do Histórico Escolar)";
		String periodoLetivo = Constantes.EMPTY;
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getUtilizarCampoPeriodoDisciplina())) {
			periodoLetivo = expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getUtilizarCampoPeriodoDisciplina().equals(CampoPeriodoDisciplinaEnum.NUMERO_PERIODO_LETIVO) ? historicoAlunoDisciplinaRelVO.getNomePeriodo() : (historicoAlunoDisciplinaRelVO.getAnoSemstre().contains("/") ? historicoAlunoDisciplinaRelVO.getAnoSemstre().replace("/", ".") : historicoAlunoDisciplinaRelVO.getAnoSemstre());
		} else {
			periodoLetivo = getStringXml(historicoAlunoDisciplinaRelVO.getNomePeriodo());
		}
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(periodoLetivo), "O PERÍODO LETIVO CURSADO deve ser informado, " + exception, ce);
		return periodoLetivo;
	}
	
	private void preencherDadosDisciplinaAprovadaEntradaHistoricoDisciplina(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, TEntradaHistoricoDisciplina entradaHistoricoDisciplina) {
		TDisciplinaAprovada disciplinaAprovada = getDisciplinaAprovada(historicoAlunoDisciplinaRelVO.getSituacaoHistorico());
		if (disciplinaAprovada != null) {
			entradaHistoricoDisciplina.setAprovado(disciplinaAprovada);
		} else {
			TVazio disciplinaReprovado = getDisciplinaReprovado(historicoAlunoDisciplinaRelVO.getSituacaoHistorico());
			if (disciplinaReprovado != null) {
				entradaHistoricoDisciplina.setReprovado(disciplinaReprovado);
			} else {
				entradaHistoricoDisciplina.setPendente(new TVazio());
			}
		}
	}
	
	private void preencherDadosDisciplinaAprovadaEntradaHistoricoDisciplinaSegundaVia(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, TEntradaHistoricoDisciplinaSegundaViaNatoFisica entradaHistoricoDisciplinaSegundaViaNatoFisica) {
		TDisciplinaAprovada disciplinaAprovada = getDisciplinaAprovada(historicoAlunoDisciplinaRelVO.getSituacaoHistorico());
		if (disciplinaAprovada != null) {
			entradaHistoricoDisciplinaSegundaViaNatoFisica.setAprovado(disciplinaAprovada);
		} else {
			TVazio disciplinaReprovado = getDisciplinaReprovado(historicoAlunoDisciplinaRelVO.getSituacaoHistorico());
			if (disciplinaReprovado != null) {
				entradaHistoricoDisciplinaSegundaViaNatoFisica.setReprovado(disciplinaReprovado);
			} else {
				entradaHistoricoDisciplinaSegundaViaNatoFisica.setPendente(new TVazio());
			}
		}
	}
	
	private TDisciplinaAprovada getDisciplinaAprovada(String situacao) {
		TDisciplinaAprovada disciplinaAprovada = new TDisciplinaAprovada();
		switch (situacao) {
		case "AP":
			disciplinaAprovada.setFormaIntegralizacao(TFormaIntegralizacao.CURSADO);
			break;
		case "AE":
			disciplinaAprovada.setFormaIntegralizacao(TFormaIntegralizacao.CURSADO);
			break;
		case "AA":
			disciplinaAprovada.setFormaIntegralizacao(TFormaIntegralizacao.APROVEITADO);
			break;
		case "AD":
			disciplinaAprovada.setFormaIntegralizacao(TFormaIntegralizacao.CURSADO);
			break;
		case "AB":
			disciplinaAprovada.setFormaIntegralizacao(TFormaIntegralizacao.CURSADO);
			break;
		case "IS":
			disciplinaAprovada.setFormaIntegralizacao(TFormaIntegralizacao.CURSADO);
			break;
		case "CC":
			disciplinaAprovada.setFormaIntegralizacao(TFormaIntegralizacao.VALIDADO);
			break;
		case "CH":
			disciplinaAprovada.setFormaIntegralizacao(TFormaIntegralizacao.APROVEITADO);
			break;
		default:
			return null;
		}
		return disciplinaAprovada;
	}
	
	private TVazio getDisciplinaReprovado(String situacao) {
		switch (situacao) {
		case "RE":
			return new TVazio();
		case "RF":
			return new TVazio();
		case "RP":
			return new TVazio();
		default:
			return null;
		}
	}
	
	private BigDecimal getNotaXml(BigDecimal nota) {
		if (Objects.nonNull(nota)) {
			if (Objects.nonNull(nota.scale()) && nota.scale() > 2) {
				nota = nota.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			return nota;
		}
		return null;
	}
	
	public void preencherDadosNotaNotaConceitoEntradaHistoricoDisciplina(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, TEntradaHistoricoDisciplina entradaHistoricoDisciplina, ConsistirException ce) {
		String exception = "disciplina: " + historicoAlunoDisciplinaRelVO.getNomeDisciplina().toUpperCase() + (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getAnoSemstre()) ? ", ano/semestre: " + historicoAlunoDisciplinaRelVO.getAnoSemstre() : Constantes.EMPTY) + " " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem();
		Boolean utilizaNotaConceito = historicoAlunoDisciplinaRelVO.getHistoricoVO().getUtilizaNotaFinalConceito() || Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getHistoricoVO().getMediaFinalConceito());
		if (Objects.nonNull(historicoAlunoDisciplinaRelVO.getMediaFinal()) && Uteis.getIsValorNumerico2(historicoAlunoDisciplinaRelVO.getMediaFinal()) && !utilizaNotaConceito) {
			BigDecimal nota = new BigDecimal(historicoAlunoDisciplinaRelVO.getMediaFinal().replaceAll(",", "."));
			String mensagemErroNota = "A NOTA da DISCIPLINA deve ser informado, " + exception;
			Uteis.checkStateList(Objects.isNull(nota), mensagemErroNota, ce);
			if (Objects.nonNull(nota)) {
				if (nota.compareTo(BigDecimal.TEN) > 0) {
					if (nota.compareTo(new BigDecimal(100)) > 0) {
						ce.getListaMensagemErro().add("Não foi possível realizar a montagem da nota da disciplina " + historicoAlunoDisciplinaRelVO.getNomeDisciplina().toUpperCase() + (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getAnoSemstre()) ? " ano/semestre " + historicoAlunoDisciplinaRelVO.getAnoSemstre() : ""));
					} else {
						entradaHistoricoDisciplina.setNotaAteCem(getNotaXml(nota));
					}
				} else {
					entradaHistoricoDisciplina.setNota(getNotaXml(nota));
				}
			}
		} else if (utilizaNotaConceito) {
			String mensagemErroNota = "A NOTA CONCEITO da DISCIPLINA deve ser informada, " + exception;
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getMediaFinal()), mensagemErroNota, ce);
			if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getMediaFinal())) {
				entradaHistoricoDisciplina.setConceitoEspecificoDoCurso(Uteis.formatarStringXML(historicoAlunoDisciplinaRelVO.getMediaFinal()));
			}
		} else if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getHistoricoVO()) && historicoAlunoDisciplinaRelVO.getHistoricoVO().getSituacao().equals(SituacaoHistorico.CONCESSAO_CREDITO.getValor())) {
			String mensagemErroNota = "A NOTA da DISCIPLINA deve ser informada, " + exception;
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getMediaFinal()), mensagemErroNota, ce);
			if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getMediaFinal())) {
				entradaHistoricoDisciplina.setConceitoEspecificoDoCurso(Uteis.formatarStringXML(historicoAlunoDisciplinaRelVO.getMediaFinal()));
			}
		} else {
			Uteis.checkStateList(Boolean.TRUE, "Não foi possível realizar a montagem da nota da disciplina " + historicoAlunoDisciplinaRelVO.getNomeDisciplina().toUpperCase() + (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getAnoSemstre()) ? " ano/semestre " + historicoAlunoDisciplinaRelVO.getAnoSemstre() : ""), ce);
		}
	}
	
	public void preencherDadosNotaNotaConceitoEntradaHistoricoDisciplinaSegundaVia(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, TEntradaHistoricoDisciplinaSegundaViaNatoFisica entradaHistoricoDisciplinaSegundaViaNatoFisica, ConsistirException ce) {
		String exception = "disciplina: " + historicoAlunoDisciplinaRelVO.getNomeDisciplina().toUpperCase() + (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getAnoSemstre()) ? ", ano/semestre: " + historicoAlunoDisciplinaRelVO.getAnoSemstre() : Constantes.EMPTY) + " " + TipoOrigemMontagemDados.HISTORICO_ESCOLAR.getMensagem();
		if (Objects.nonNull(historicoAlunoDisciplinaRelVO.getMediaFinal()) && Uteis.getIsValorNumerico2(historicoAlunoDisciplinaRelVO.getMediaFinal()) && !historicoAlunoDisciplinaRelVO.getHistoricoVO().getUtilizaNotaFinalConceito()) {
			BigDecimal nota = new BigDecimal(historicoAlunoDisciplinaRelVO.getMediaFinal().replaceAll(",", "."));
			String mensagemErroNota = "A NOTA da DISCIPLINA deve ser informado, " + exception;
			Uteis.checkStateList(Objects.isNull(nota), mensagemErroNota, ce);
			if (Objects.nonNull(nota)) {
				entradaHistoricoDisciplinaSegundaViaNatoFisica.setNota(nota);
			}
		} else if (historicoAlunoDisciplinaRelVO.getHistoricoVO().getUtilizaNotaFinalConceito()) {
			String mensagemErroNota = "A NOTA CONCEITO da DISCIPLINA deve ser informada, " + exception;
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getMediaFinal()), mensagemErroNota, ce);
			if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getMediaFinal())) {
				entradaHistoricoDisciplinaSegundaViaNatoFisica.setConceitoEspecificoDoCurso(Uteis.formatarStringXML(historicoAlunoDisciplinaRelVO.getMediaFinal()));
			}
		} else if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getHistoricoVO()) && historicoAlunoDisciplinaRelVO.getHistoricoVO().getSituacao().equals(SituacaoHistorico.CONCESSAO_CREDITO.getValor())) {
			String mensagemErroNota = "A NOTA CONCEITO da DISCIPLINA deve ser informada, " + exception;
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getMediaFinal()), mensagemErroNota, ce);
			if (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getMediaFinal())) {
				entradaHistoricoDisciplinaSegundaViaNatoFisica.setConceitoEspecificoDoCurso(Uteis.formatarStringXML(historicoAlunoDisciplinaRelVO.getMediaFinal()));
			}
		} else {
			Uteis.checkStateList(Boolean.TRUE, "Não foi possível realizar a montagem da nota da disciplina " + historicoAlunoDisciplinaRelVO.getNomeDisciplina().toUpperCase() + (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getAnoSemstre()) ? " ano/semestre " + historicoAlunoDisciplinaRelVO.getAnoSemstre() : ""), ce);
		}
	}
	
	public void preencherDadosDocenteEntradaHistoricoDisciplina(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, TEntradaHistoricoDisciplina entradaHistoricoDisciplina, ConsistirException ce) {
		entradaHistoricoDisciplina.setDocentes(preencherDadosDocenteEntradaHistoricoDisciplina(historicoAlunoDisciplinaRelVO, expedicaoDiplomaVO, Boolean.TRUE, TipoOrigemMontagemDados.HISTORICO_ESCOLAR, ce));
	}
	
	public void preencherDadosDocenteEntradaHistoricoDisciplinaSegundaVia(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, TEntradaHistoricoDisciplinaSegundaViaNatoFisica entradaHistoricoDisciplinaSegundaViaNatoFisica, ConsistirException ce) {
		entradaHistoricoDisciplinaSegundaViaNatoFisica.setDocentes(preencherDadosDocenteEntradaHistoricoDisciplina(historicoAlunoDisciplinaRelVO, expedicaoDiplomaVO, Boolean.FALSE, TipoOrigemMontagemDados.HISTORICO_ESCOLAR, ce));
		
	}
	
	public TDocentes preencherDadosDocenteEntradaHistoricoDisciplina(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, Boolean validarDados, TipoOrigemMontagemDados tipoOrigemMontagemDados, ConsistirException ce) {
		String exception = "disciplina: " + historicoAlunoDisciplinaRelVO.getNomeDisciplina().toUpperCase() + (Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getAnoSemstre()) ? ", ano/semestre: " + historicoAlunoDisciplinaRelVO.getAnoSemstre() : Constantes.EMPTY) + " " + tipoOrigemMontagemDados.getMensagem();
		TDocente docente = new TDocente();
		TDocentes docentes = new TDocentes();
		List<String> situacoesAproveitamento = Arrays.asList("AA", "CC", "CH", "IS", "AB");
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getCoordenadorCursoDisciplinasAproveitadas()) && (situacoesAproveitamento.stream().anyMatch(s -> s.trim().equals(historicoAlunoDisciplinaRelVO.getHistoricoVO().getSituacao())) || Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getHistoricoVO().getDisciplinasAproveitadas()))) {
			if (expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getCoordenadorCursoDisciplinasAproveitadas().equals(CoordenadorCursoDisciplinaAproveitadaEnum.APENAS_APROVEITAMENTO_SEM_PROFESSOR)) {
				if (!Uteis.isAtributoPreenchido(historicoAlunoDisciplinaRelVO.getProfessor())) {
					String nomeDocente = getStringXml(expedicaoDiplomaVO.getCursoCoordenadorVO().getFuncionario().getPessoa().getNome());
					TTitulacao titulacaoDocente = Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getFormacaoAcademicaVO()) ? getTitulacao(getStringXml(expedicaoDiplomaVO.getFormacaoAcademicaVO().getEscolaridade())) : null;
					if (validarDados) {
						if (!Uteis.isAtributoPreenchido(nomeDocente)) {
							String mensagem = "O COORDENADOR do CURSO deve ser informado, " + exception;
							Uteis.checkStateList(Boolean.TRUE, mensagem, ce);
						} else {
							String mensagem = "A TITULAÇÃO do DOCENTE deve ser informado, docente: " + nomeDocente.toUpperCase() + " " + tipoOrigemMontagemDados.getMensagem();
							Uteis.checkStateList(!Uteis.isAtributoPreenchido(titulacaoDocente), mensagem, ce);
						}
					}
					docente.setNome(nomeDocente);
					docente.setTitulacao(titulacaoDocente);
					if (Uteis.isAtributoPreenchido(docente.getNome()) && Uteis.isAtributoPreenchido(docente.getTitulacao())) {
						docentes.getDocente().add(docente);
					}
				} else {
					String nomeDocente = getStringXml(historicoAlunoDisciplinaRelVO.getProfessor());
					TTitulacao titulacaoDocente = getTitulacao(getStringXml(historicoAlunoDisciplinaRelVO.getSiglaTitulacaoProfessor()));
					if (validarDados) {
						if (!Uteis.isAtributoPreenchido(nomeDocente)) {
							String mensagem = "O DOCENTE da DISCIPLINA deve ser informado, " + exception;
							Uteis.checkStateList(Boolean.TRUE, mensagem, ce);
						} else {
							String mensagem = "A TITULAÇÃO do DOCENTE deve ser informado, docente: " + nomeDocente.toUpperCase() + " " + tipoOrigemMontagemDados.getMensagem();
							Uteis.checkStateList(!Uteis.isAtributoPreenchido(titulacaoDocente), mensagem, ce);
						}
					}
					docente.setNome(nomeDocente);
					docente.setTitulacao(titulacaoDocente);
					if (Uteis.isAtributoPreenchido(docente.getNome()) && Uteis.isAtributoPreenchido(docente.getTitulacao())) {
						docentes.getDocente().add(docente);
					}
				}
			} else {
				String nomeDocente = getStringXml(expedicaoDiplomaVO.getCursoCoordenadorVO().getFuncionario().getPessoa().getNome());
				TTitulacao titulacaoDocente = getTitulacao(getStringXml(expedicaoDiplomaVO.getFormacaoAcademicaVO().getEscolaridade()));
				if (validarDados) {
					if (!Uteis.isAtributoPreenchido(nomeDocente)) {
						String mensagem = "O COORDENADOR do CURSO deve ser informado, " + exception;
						Uteis.checkStateList(Boolean.TRUE, mensagem, ce);
					} else {
						String mensagem = "A TITULAÇÃO do DOCENTE deve ser informado, docente: " + nomeDocente.toUpperCase() + " " + tipoOrigemMontagemDados.getMensagem();
						Uteis.checkStateList(!Uteis.isAtributoPreenchido(titulacaoDocente), mensagem, ce);
					}
				}
				docente.setNome(nomeDocente);
				docente.setTitulacao(titulacaoDocente);
				if (Uteis.isAtributoPreenchido(docente.getNome()) && Uteis.isAtributoPreenchido(docente.getTitulacao())) {
					docentes.getDocente().add(docente);
				}
			}
		} else {
			String nomeDocente = getStringXml(historicoAlunoDisciplinaRelVO.getProfessor());
			TTitulacao titulacaoDocente = getTitulacao(getStringXml(historicoAlunoDisciplinaRelVO.getSiglaTitulacaoProfessor()));
			if (validarDados) {
				if (!Uteis.isAtributoPreenchido(nomeDocente)) {
					String mensagem = "O DOCENTE da DISCIPLINA deve ser informado, " + exception;
					Uteis.checkStateList(Boolean.TRUE, mensagem, ce);
				} else {
					String mensagem = "A TITULAÇÃO do DOCENTE deve ser informado, docente: " + nomeDocente.toUpperCase() + " (Dados do Histórico Escolar)";
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(titulacaoDocente), mensagem, ce);
				}
			}
			docente.setNome(nomeDocente);
			docente.setTitulacao(titulacaoDocente);
			if (Uteis.isAtributoPreenchido(docente.getNome()) && Uteis.isAtributoPreenchido(docente.getTitulacao())) {
				docentes.getDocente().add(docente);
			}
		}
		return docentes;
	}

	private TTitulacao getTitulacao(String titulacao) {
		switch (titulacao) {
		case "GR":
		case "GRA":
			return TTitulacao.GRADUAÇÃO;
		case "Esp":
			return TTitulacao.ESPECIALIZAÇÃO;
		case "Me":
			return TTitulacao.MESTRADO;
		case "DR":
		case "DRA":
			return TTitulacao.DOUTORADO;
		case "EP":
			return TTitulacao.ESPECIALIZAÇÃO;
		case "MS":
			return TTitulacao.MESTRADO;
		case "PO":
			return TTitulacao.ESPECIALIZAÇÃO;
		case "POS":
			return TTitulacao.ESPECIALIZAÇÃO;
		case "Pos":
			return TTitulacao.ESPECIALIZAÇÃO;
		case "PD":
			return TTitulacao.DOUTORADO;
		default:
			return null;
		}
	}

	private Object criarHistoricoEscolarSegundaViaAtividadeComplementar(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatricula, ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, ConsistirException ce) {
		TEntradaHistoricoAtividadeComplementarSegundaViaNatoFisica entradaHistoricoAtividadeComplementar = new TEntradaHistoricoAtividadeComplementarSegundaViaNatoFisica();
		try {
			validarDadosHistoricoAtividadeComplementar(registroAtividadeComplementarMatricula, ce);
			entradaHistoricoAtividadeComplementar.setCodigoAtividadeComplementar(getStringXml(registroAtividadeComplementarMatricula.getCodigo().toString()));
			entradaHistoricoAtividadeComplementar.setDataInicio(getDataConvertidaXML(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getData()));
			entradaHistoricoAtividadeComplementar.setDataFim(getDataConvertidaXML(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getData()));
			entradaHistoricoAtividadeComplementar.setTipoAtividadeComplementar(getStringXml(registroAtividadeComplementarMatricula.getTipoAtividadeComplementarVO().getNome()));
			entradaHistoricoAtividadeComplementar.getCargaHorariaEmHoraRelogio().add(preencherDadosCargaHorariaEmHoraRelogioEntradaHistoricoAtividadeComplementar(expedicaoDiplomaVO, registroAtividadeComplementarMatricula));
			if (Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getNomeEvento())) {
				entradaHistoricoAtividadeComplementar.setDescricao(getStringXml(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getNomeEvento()));
			}
			if (Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getDataDeferimentoIndeferimento())) {
				entradaHistoricoAtividadeComplementar.setDataRegistro(getDataConvertidaXML(registroAtividadeComplementarMatricula.getDataDeferimentoIndeferimento()));
			}
			entradaHistoricoAtividadeComplementar.setDocentesResponsaveisPelaValidacao(preencherDadosDocenteEntradaHistoricoAtividadeComplementar(registroAtividadeComplementarMatricula, expedicaoDiplomaVO, usuarioVO, ce));
		} catch (Exception e) {
			ce.getListaMensagemErro().add(e.getMessage());
		}
		return entradaHistoricoAtividadeComplementar;
	}

	public void validarDadosHistoricoAtividadeComplementar(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatricula, ConsistirException ce) {
		String exception = Constantes.EMPTY;
		if (Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getNomeEvento())) {
			exception = "evento: " + registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getNomeEvento().toUpperCase() + " (Dados da Atividade Complementar)";
		} else {
			exception = "tipo de atividade complementar: " + registroAtividadeComplementarMatricula.getTipoAtividadeComplementarVO().getNome().toUpperCase() + " (Dados da Atividade Complementar)";
		}
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getData()), "A DATA do REGISTRO de ATIVIDADE COMPLEMENTAR deve ser informado, " + exception, ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getCargaHorariaConsiderada()), "A CARGA HORARIA CONSIDERADA do REGISTRO de ATIVIDADE COMPLEMENTAR deve ser informado, " + exception, ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getTipoAtividadeComplementarVO().getNome()), "O NOME da ATIVIDADE COMPLEMENTAR deve ser informado, " + exception, ce);
	}

	private THoraRelogioComEtiqueta preencherDadosCargaHorariaEmHoraRelogioEntradaHistoricoAtividadeComplementar(ExpedicaoDiplomaVO expedicaoDiplomaVO, RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatricula) {
		THoraRelogioComEtiqueta tCargaHoraria = new THoraRelogioComEtiqueta();
		tCargaHoraria.setValue(getHoraRelogioAtividadeComplementar(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital(), registroAtividadeComplementarMatricula.getCargaHorariaConsiderada()));
		return tCargaHoraria;
	}

	public TDocentes preencherDadosDocenteEntradaHistoricoAtividadeComplementar(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatricula, ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		String exception = Constantes.EMPTY;
		if (Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getNomeEvento())) {
			exception = "evento: " + registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getNomeEvento().toUpperCase() + " (Dados da Atividade Complementar)";
		} else {
			exception = "tipo de atividade complementar: " + registroAtividadeComplementarMatricula.getTipoAtividadeComplementarVO().getNome().toUpperCase() + " (Dados da Atividade Complementar)";
		}
		TDocentes docentes = new TDocentes();
		TDocente docente = new TDocente();
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital()) && expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getUtilizarCoordenadorCursoAtividadeComplementar()) {
			String nomeDocente = getStringXml(expedicaoDiplomaVO.getCursoCoordenadorVO().getFuncionario().getPessoa().getNome());
			TTitulacao titulacaoDocente = getTitulacao(getStringXml(expedicaoDiplomaVO.getFormacaoAcademicaVO().getEscolaridade()));
			if (Uteis.isAtributoPreenchido(nomeDocente)) {
				String mensagemTitulacaoDocente = "A TITULAÇÃO DO DOCENTE deve ser informado, docente" + nomeDocente + " (Dados da Atividade Complementar)";
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(titulacaoDocente), mensagemTitulacaoDocente, ce);
			} else {
				String mensagemNomeDocente = "O COORDENADOR DO CURSO deve ser informado (Dados da Atividade Complementar)";
				Uteis.checkStateList(!Uteis.isAtributoPreenchido(nomeDocente), mensagemNomeDocente, ce);
			}
			docente.setNome(nomeDocente);
			docente.setTitulacao(titulacaoDocente);
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getResponsavelDeferimentoIndeferimento()), "O RESPONSÁVEL DEFERIMENTO do REGISTRO DE ATIVIDADE COMPLEMENTAR deve ser informado, " + exception, ce);
			if (Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getResponsavelDeferimentoIndeferimento())) {
				registroAtividadeComplementarMatricula.setResponsavelDeferimentoIndeferimento(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(registroAtividadeComplementarMatricula.getResponsavelDeferimentoIndeferimento().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
				FormacaoAcademicaVO formacaoAcademicaVO = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(registroAtividadeComplementarMatricula.getResponsavelDeferimentoIndeferimento().getPessoa().getCodigo(), usuarioVO);
				String nomeDocente = getStringXml(registroAtividadeComplementarMatricula.getResponsavelDeferimentoIndeferimento().getNome());
				TTitulacao titulacaoDocente = getTitulacao(getStringXml(formacaoAcademicaVO.getEscolaridade()));
				if (Uteis.isAtributoPreenchido(nomeDocente)) {
					String mensagemTitulacaoDocente = "A TITULAÇÃO DO DOCENTE deve ser informado, docente" + nomeDocente + " (Dados da Atividade Complementar)";
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(titulacaoDocente), mensagemTitulacaoDocente, ce);
				} else {
					String mensagemNomeDocente = Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getNomeEvento()) ? "O docente da atividade complementar (Responsável pelo deferimento/indeferimento) " + registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getNomeEvento() + " deve ser informado (Dados da Atividade Complementar)" : "O docente da atividade complementar (Responsável pelo deferimento/indeferimento) do tipo " + registroAtividadeComplementarMatricula.getTipoAtividadeComplementarVO().getNome() + " deve ser informado (Dados da Atividade Complementar)";
					Uteis.checkStateList(!Uteis.isAtributoPreenchido(nomeDocente), mensagemNomeDocente, ce);
				}
				docente.setNome(nomeDocente);
				docente.setTitulacao(titulacaoDocente);
			}
		}
		docentes.getDocente().add(docente);
		return docentes;
	}
	
	private Object criarHistoricoEscolarEstagioSegundaViaNatoFisica(EstagioVO estagioVO, UsuarioVO usuarioVO, ConsistirException ce) {
		TEntradaHistoricoEstagioSegundaViaNatoFisica tEntradaHistoricoEstagio = new TEntradaHistoricoEstagioSegundaViaNatoFisica();
		try {
			validarDadosHistoricoEstagio(estagioVO, ce);
			tEntradaHistoricoEstagio.setCodigoUnidadeCurricular(getStringXml(estagioVO.getCodigo().toString()));
			tEntradaHistoricoEstagio.setDataInicio(getDataConvertidaXML(estagioVO.getCreated()));
			tEntradaHistoricoEstagio.setDataFim(getDataConvertidaXML(estagioVO.getDataDeferimento()));
			preencherDadosCargaHorariaEmHorasRelogioEstagioSegundaViaNatoFisica(estagioVO, tEntradaHistoricoEstagio, ce);
			preencherDadosConcedenteEstagioSegundaViaNatoFisica(estagioVO, tEntradaHistoricoEstagio, ce);
			preencherDadosDocentesOrientadoresEstagioSegundaViaNatoFisica(estagioVO, tEntradaHistoricoEstagio, usuarioVO, ce);
		} catch (Exception e) {
			ce.getListaMensagemErro().add(e.getMessage());
		}
		return tEntradaHistoricoEstagio;
	}

	public void validarDadosHistoricoEstagio(EstagioVO estagioVO, ConsistirException ce) {
		String exception = "componente curricular: " + estagioVO.getGradeCurricularEstagioVO().getNome().toUpperCase() + " " + TipoOrigemMontagemDados.ESTAGIO.getMensagem();
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(estagioVO.getCreated()), "A DATA de CRIAÇÃO do ESTÁGIO deve ser informado, " + exception, ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(estagioVO.getDataDeferimento()), "A DATA do DEFERIMENTO do ESTÁGIO deve ser informado, " + exception, ce);
	}

	private void preencherDadosCargaHorariaEmHorasRelogioEstagio(EstagioVO estagioVO, TEntradaHistoricoEstagio tEntradaHistoricoEstagio, ConsistirException consistirException) {
		tEntradaHistoricoEstagio.getCargaHorariaEmHorasRelogio().add(getCargaHorariaEmHorasRelogioEstagio(estagioVO, consistirException));
	}
	
	private void preencherDadosCargaHorariaEmHorasRelogioEstagioSegundaViaNatoFisica(EstagioVO estagioVO, TEntradaHistoricoEstagioSegundaViaNatoFisica tEntradaHistoricoEstagioSegundaViaNatoFisica, ConsistirException consistirException) {
		tEntradaHistoricoEstagioSegundaViaNatoFisica.getCargaHorariaEmHorasRelogio().add(getCargaHorariaEmHorasRelogioEstagio(estagioVO, consistirException));
	}
	
	private THoraRelogioComEtiqueta getCargaHorariaEmHorasRelogioEstagio(EstagioVO estagioVO, ConsistirException consistirException) {
		String exception = "componente curricular: " + estagioVO.getGradeCurricularEstagioVO().getNome().toUpperCase() + " " + TipoOrigemMontagemDados.ESTAGIO.getMensagem();
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(estagioVO.getCargaHorariaDeferida()), "A CARGA HORARIA DEFERIDA do ESTÁGIO deve ser informado, " + exception, consistirException);
		THoraRelogioComEtiqueta tCargaHoraria = new THoraRelogioComEtiqueta();
		if (Uteis.isAtributoPreenchido(estagioVO.getCargaHorariaDeferida())) {
			tCargaHoraria.setValue(BigDecimal.valueOf(Uteis.arrendondarForcando2CadasDecimais(estagioVO.getCargaHorariaDeferida())));
		}
		return tCargaHoraria;
	}

	public void preencherDadosConcedenteEstagio(EstagioVO estagioVO, TEntradaHistoricoEstagio tEntradaHistoricoEstagio, ConsistirException ce) throws Exception {
		tEntradaHistoricoEstagio.setConcedente(getConcedenteEstagio(estagioVO, ce));
	}
	
	public void preencherDadosConcedenteEstagioSegundaViaNatoFisica(EstagioVO estagioVO, TEntradaHistoricoEstagioSegundaViaNatoFisica tEntradaHistoricoEstagioSegundaViaNatoFisica, ConsistirException ce) throws Exception {
		tEntradaHistoricoEstagioSegundaViaNatoFisica.setConcedente(getConcedenteEstagio(estagioVO, ce));
	}
	
	public TConcedenteEstagio getConcedenteEstagio(EstagioVO estagioVO, ConsistirException ce) throws Exception {
		if (Uteis.isAtributoPreenchido(estagioVO.getConcedenteVO()) && Uteis.isAtributoPreenchido(estagioVO.getConcedenteVO().getCnpj()) && Uteis.validaCNPJ(getCnpjXml(estagioVO.getConcedenteVO().getCnpj()))) {
			String exception = "componente curricular: " + estagioVO.getGradeCurricularEstagioVO().getNome().toUpperCase() + ", concedente: " + estagioVO.getConcedenteVO().getConcedente().toUpperCase() + " " + TipoOrigemMontagemDados.ESTAGIO.getMensagem();
			Uteis.checkStateList(!Uteis.validaCNPJ(getCnpjXml(estagioVO.getConcedenteVO().getCnpj())), "O CNPJ do CONCEDENTE deve ser valido, " + exception, ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(estagioVO.getConcedenteVO().getConcedente()), "O CONCEDENTE deve ser informado, " + exception, ce);
			TConcedenteEstagio concedenteEstagio = new TConcedenteEstagio();
			concedenteEstagio.setCNPJ(getCnpjXml(estagioVO.getConcedenteVO().getCnpj()));
			concedenteEstagio.setRazaoSocial(getStringXml(estagioVO.getConcedenteVO().getConcedente()));
			concedenteEstagio.setNomeFantasia(getStringXml(estagioVO.getConcedenteVO().getConcedente()));
			return concedenteEstagio;
		}
		return null;
	}

	public void preencherDadosDocentesOrientadoresEstagio(EstagioVO estagioVO, TEntradaHistoricoEstagio tEntradaHistoricoEstagio, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		tEntradaHistoricoEstagio.setDocentesOrientadores(getDocentesOrientadoresEstagio(estagioVO, usuarioVO, ce));
	}
	
	public void preencherDadosDocentesOrientadoresEstagioSegundaViaNatoFisica(EstagioVO estagioVO, TEntradaHistoricoEstagioSegundaViaNatoFisica tEntradaHistoricoEstagioSegundaViaNatoFisica, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		tEntradaHistoricoEstagioSegundaViaNatoFisica.setDocentesOrientadores(getDocentesOrientadoresEstagio(estagioVO, usuarioVO, ce));
	}
	
	public TDocentes getDocentesOrientadoresEstagio(EstagioVO estagioVO, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		String exception = "componente curricular: " + estagioVO.getGradeCurricularEstagioVO().getNome().toUpperCase() + " " + TipoOrigemMontagemDados.ESTAGIO.getMensagem();
		TDocente docente = new TDocente();
		TDocentes docentes = new TDocentes();
		if (Uteis.isAtributoPreenchido(estagioVO.getDocenteResponsavelEstagio())) {
			FormacaoAcademicaVO formacaoAcademicaVO = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(estagioVO.getDocenteResponsavelEstagio().getCodigo(), usuarioVO);
			TTitulacao titulacao = getTitulacao(formacaoAcademicaVO.getEscolaridade().trim());
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(titulacao), "A TITULAÇÃO do DOCENTE deve ser informado, docente: " + estagioVO.getDocenteResponsavelEstagio().getNome().toUpperCase() + " (Dados do Estágio)", ce);
			docente.setNome(getStringXml(estagioVO.getDocenteResponsavelEstagio().getNome()));
			docente.setTitulacao(titulacao);
			docentes.getDocente().add(docente);
		} else if (Uteis.isAtributoPreenchido(estagioVO.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio())) {
			PessoaVO orientador = estagioVO.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa();
			FormacaoAcademicaVO formacaoAcademicaVO = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(orientador.getCodigo(), usuarioVO);
			TTitulacao titulacao = getTitulacao(formacaoAcademicaVO.getEscolaridade().trim());
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(titulacao), "A TITULAÇÃO do DOCENTE deve ser informado, docente: " + orientador.getNome().toUpperCase() + " (Dados do Estágio)", ce);
			docente.setNome(getStringXml(orientador.getNome()));
			docente.setTitulacao(titulacao);
			docentes.getDocente().add(docente);
		} else {
			ce.getListaMensagemErro().add("O DOCENTE RESPONSÁVEL pelo ESTÁGIO deve ser informado, " + exception);
		}
		return docentes;
	}

	private void preencherDadosHistoricoEscolarDocumentacao(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, TDadosPrivadosDiplomado dadosPrivadosDiplomado, ConsistirException ce) throws Exception {
		HistoricoAlunoRelVO histAlunoRelVO = getHistoricoAlunoRelVO(expedicaoDiplomaVO, usuarioVO);
		dadosPrivadosDiplomado.setHistoricoEscolar(preencherDadosHistoricoEscolar(expedicaoDiplomaVO, histAlunoRelVO, usuarioVO, ce));
	}
	
	private THistoricoEscolar preencherDadosHistoricoEscolar(ExpedicaoDiplomaVO expedicaoDiplomaVO, HistoricoAlunoRelVO histAlunoRelVO, UsuarioVO usuarioVO, ConsistirException ce) throws Exception {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs()), "Não foi encontrados DISCIPLINAS dos HISTÓRICOS desse aluno (Dados do Histórico Escolar)", ce);
		if (Uteis.isAtributoPreenchido(histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs())) {
			THistoricoEscolar historicoEscolar = new THistoricoEscolar();
			historicoEscolar.setSituacaoAtualDiscente(getSituacaoAlunoHistorico(histAlunoRelVO.getSituacaoFinal(), expedicaoDiplomaVO));
			historicoEscolar.setDataEmissaoHistorico(getDataConvertidaXML(new Date()));
			historicoEscolar.setHoraEmissaoHistorico(Uteis.getHoraAtualComSegundos());
			historicoEscolar.setCodigoCurriculo(getCodigoCurriculoHistoricoEscolar(expedicaoDiplomaVO.getGradeCurricularVO(), Boolean.TRUE, ce));
			historicoEscolar.setIngressoCurso(preencherDadosIngressoCursoHistoricoEscolar(expedicaoDiplomaVO, ce));
			historicoEscolar.setENADE(getEnadeHistoricoEscolar(histAlunoRelVO, expedicaoDiplomaVO, Boolean.TRUE, usuarioVO, ce));
			preencherDadosCargaHorariaCursoIntegralizadaHistoricoEscolar(historicoEscolar, expedicaoDiplomaVO, histAlunoRelVO, ce);
			preencherDadosMatrizCurricularDocumentacaoHistoricoEscolar(expedicaoDiplomaVO, histAlunoRelVO, usuarioVO, historicoEscolar, ce);
			preencherDadosAreasHistoricoEscolar(histAlunoRelVO, historicoEscolar);
			return historicoEscolar;
		}
		return null;
	}

	private void preencherDadosMatrizCurricularDocumentacaoHistoricoEscolar(ExpedicaoDiplomaVO expedicaoDiplomaVO, HistoricoAlunoRelVO histAlunoRelVO, UsuarioVO usuarioVO, THistoricoEscolar historicoEscolar, ConsistirException ce) throws Exception {
		try {
			historicoEscolar.setElementosHistorico(new TElementosHistorico());
			List<RegistroAtividadeComplementarMatriculaVO> registroAtividadeComplementarMatriculaVOs = getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarPorMatriculaSituacao(expedicaoDiplomaVO.getMatricula().getMatricula(), SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO, Boolean.FALSE, usuarioVO);
			historicoEscolar.getElementosHistorico().getDisciplinaOrAtividadeComplementarOrEstagio().addAll(histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs().stream().filter(d -> Uteis.isAtributoPreenchido(d.getCodigoDisciplina()) && !d.getDisciplinaEstagio()).map(disciplina -> criarHistoricoEscolarMatrizCurricularDisciplinaCursadaDocumentacao(disciplina, expedicaoDiplomaVO, ce)).collect(Collectors.toList()));
			if (Uteis.isAtributoPreenchido(registroAtividadeComplementarMatriculaVOs)) {
				historicoEscolar.getElementosHistorico().getDisciplinaOrAtividadeComplementarOrEstagio().addAll(registroAtividadeComplementarMatriculaVOs.stream().map(atividadeComplementar -> criarHistoricoEscolarAtividadeComplementar(atividadeComplementar, expedicaoDiplomaVO, usuarioVO, ce)).collect(Collectors.toList()));
			}
			List<EstagioVO> estagioVOs = getFacadeFactory().getEstagioFacade().consultarPorMatriculaAluno(expedicaoDiplomaVO.getMatricula().getMatricula(), SituacaoEstagioEnum.DEFERIDO, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(estagioVOs)) {
				historicoEscolar.getElementosHistorico().getDisciplinaOrAtividadeComplementarOrEstagio().addAll(estagioVOs.stream().map(estagio -> criarHistoricoEscolarEstagio(estagio, usuarioVO, ce)).collect(Collectors.toList()));
			} else {
				if (Uteis.isAtributoPreenchido(histAlunoRelVO.getListaHistoricoAlunoDisciplinaEstagioRelVOs())) {
					historicoEscolar.getElementosHistorico().getDisciplinaOrAtividadeComplementarOrEstagio().addAll(criarHistoricoEscolarEstagioPartirHistorico(histAlunoRelVO.getListaHistoricoAlunoDisciplinaEstagioRelVOs(), expedicaoDiplomaVO, ce));
				} else {
					historicoEscolar.getElementosHistorico().getDisciplinaOrAtividadeComplementarOrEstagio().addAll(criarHistoricoEscolarEstagioPartirHistorico(histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs(), expedicaoDiplomaVO, ce));
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private Object criarHistoricoEscolarMatrizCurricularDisciplinaCursadaDocumentacao(HistoricoAlunoDisciplinaRelVO historicoAlunoDisciplinaRelVO, ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce) {
		validarDadosHistoricoDisciplina(historicoAlunoDisciplinaRelVO, ce);
		TEntradaHistoricoDisciplina tEntradaHistoricoDisciplina = new TEntradaHistoricoDisciplina();
		tEntradaHistoricoDisciplina.setCodigoDisciplina(getStringXml(historicoAlunoDisciplinaRelVO.getCodigoDisciplina().toString()));
		tEntradaHistoricoDisciplina.setNomeDisciplina(getStringXml(historicoAlunoDisciplinaRelVO.getNomeDisciplina()));
		tEntradaHistoricoDisciplina.getCargaHoraria().addAll(preencherDadosCargaHorariaEntradaHistoricoDisciplina(historicoAlunoDisciplinaRelVO, expedicaoDiplomaVO));
		tEntradaHistoricoDisciplina.setPeriodoLetivo(preencherDadosPeriodoLetivoEntradaHistoricoDisciplina(historicoAlunoDisciplinaRelVO, expedicaoDiplomaVO, ce));
		preencherDadosDisciplinaAprovadaEntradaHistoricoDisciplina(historicoAlunoDisciplinaRelVO, tEntradaHistoricoDisciplina);
		preencherDadosNotaNotaConceitoEntradaHistoricoDisciplina(historicoAlunoDisciplinaRelVO, tEntradaHistoricoDisciplina, ce);
		preencherDadosDocenteEntradaHistoricoDisciplina(historicoAlunoDisciplinaRelVO, expedicaoDiplomaVO, tEntradaHistoricoDisciplina, ce);
		return tEntradaHistoricoDisciplina;
	}

	private Object criarHistoricoEscolarAtividadeComplementar(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatricula, ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO, ConsistirException ce) {
		TEntradaHistoricoAtividadeComplementar entradaHistoricoAtividadeComplementar = new TEntradaHistoricoAtividadeComplementar();
		try {
			validarDadosHistoricoAtividadeComplementar(registroAtividadeComplementarMatricula, ce);
			entradaHistoricoAtividadeComplementar.setCodigoAtividadeComplementar(getStringXml(registroAtividadeComplementarMatricula.getCodigo().toString()));
			entradaHistoricoAtividadeComplementar.setDataInicio(getDataConvertidaXML(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getData()));
			entradaHistoricoAtividadeComplementar.setDataFim(getDataConvertidaXML(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getData()));
			entradaHistoricoAtividadeComplementar.setTipoAtividadeComplementar(getStringXml(registroAtividadeComplementarMatricula.getTipoAtividadeComplementarVO().getNome()));
			entradaHistoricoAtividadeComplementar.getCargaHorariaEmHoraRelogio().add(preencherDadosCargaHorariaEmHoraRelogioEntradaHistoricoAtividadeComplementar(expedicaoDiplomaVO, registroAtividadeComplementarMatricula));
			if (Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getNomeEvento())) {
				entradaHistoricoAtividadeComplementar.setDescricao(getStringXml(registroAtividadeComplementarMatricula.getRegistroAtividadeComplementar().getNomeEvento()));
			}
			if (Uteis.isAtributoPreenchido(registroAtividadeComplementarMatricula.getDataDeferimentoIndeferimento())) {
				entradaHistoricoAtividadeComplementar.setDataRegistro(getDataConvertidaXML(registroAtividadeComplementarMatricula.getDataDeferimentoIndeferimento()));
			}
			entradaHistoricoAtividadeComplementar.setDocentesResponsaveisPelaValidacao(preencherDadosDocenteEntradaHistoricoAtividadeComplementar(registroAtividadeComplementarMatricula, expedicaoDiplomaVO, usuarioVO, ce));
		} catch (Exception e) {
			ce.getListaMensagemErro().add(e.getMessage());
		}
		return entradaHistoricoAtividadeComplementar;
	}

	private Object criarHistoricoEscolarEstagio(EstagioVO estagioVO, UsuarioVO usuarioVO, ConsistirException ce) {
		TEntradaHistoricoEstagio tEntradaHistoricoEstagio = new TEntradaHistoricoEstagio();
		try {
			validarDadosHistoricoEstagio(estagioVO, ce);
			tEntradaHistoricoEstagio.setCodigoUnidadeCurricular(getStringXml(estagioVO.getCodigo().toString()));
			tEntradaHistoricoEstagio.setDataInicio(getDataConvertidaXML(estagioVO.getCreated()));
			tEntradaHistoricoEstagio.setDataFim(getDataConvertidaXML(estagioVO.getDataDeferimento()));
			preencherDadosCargaHorariaEmHorasRelogioEstagio(estagioVO, tEntradaHistoricoEstagio, ce);
			preencherDadosConcedenteEstagio(estagioVO, tEntradaHistoricoEstagio, ce);
			preencherDadosDocentesOrientadoresEstagio(estagioVO, tEntradaHistoricoEstagio, usuarioVO, ce);
		} catch (Exception e) {
			ce.getListaMensagemErro().add(e.getMessage());
		}
		return tEntradaHistoricoEstagio;
	}

	private List<Object> criarDocumentacaoComprobatoriaMatricula(DocumetacaoMatriculaVO documetacaoMatriculaVO, ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigitalVO, Boolean decisaoJudicial, ConfiguracaoGeralSistemaVO config, ConsistirException ce) {
    	ServidorArquivoOnlineS3RS servidorExternoAmazon = null;
    	if (config.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3.getValor())) {
    		servidorExternoAmazon = new ServidorArquivoOnlineS3RS(config.getUsuarioAutenticacao(), config.getSenhaAutenticacao(), config.getNomeRepositorio());
    	}
		if (decisaoJudicial) {
			return preencherDocumentacaoComprobatoriaPorDecisaoJudicial(documetacaoMatriculaVO, configuracaoDiplomaDigitalVO, config, servidorExternoAmazon, ce);
		} else {
			return preencherDocumentacaoComprobatoria(documetacaoMatriculaVO, configuracaoDiplomaDigitalVO, config, servidorExternoAmazon, ce);
		}
    }
	
	private File converterPDFDocumentacaoMatriculaParaPdfA(File fileOriginal, String tipoDocumento, ArquivoVO arquivo, ConfiguracaoGeralSistemaVO config, ConsistirException ce) {
		File arquivoCopia = null;
		try {
			arquivoCopia = new File(config.getLocalUploadArquivoFixo() + File.separator + arquivo.getPastaBaseArquivo() + File.separator + (Uteis.removeCaractersEspeciais2(Uteis.getDataComHoraCompleta(new Date()).replaceAll("/", "-").replaceAll(" ", "_")) + "_org.pdf"));
			getFacadeFactory().getArquivoHelper().copiar(fileOriginal, arquivoCopia);
			getFacadeFactory().getArquivoHelper().realizarConversaoPdfPdfA(arquivoCopia.getPath(), config, arquivo, Boolean.FALSE, Boolean.FALSE);
		} catch (Exception e) {
			ce.getListaMensagemErro().add("Erro ao CONVERTER PDF PARA PDF/A, (" + e.getMessage() + ") tipo documento: " + tipoDocumento.toUpperCase());
		}
		return arquivoCopia;
	}

	private byte[] preencherArquivoByteDocumentacaoComprobatoriaMatricula(ArquivoVO arquivo, String tipoDocumento, boolean validarArquivoIsPDFA, boolean ataColacaoGrau, ServidorArquivoOnlineS3RS servidorExternoAmazon, ConfiguracaoGeralSistemaVO config, ConsistirException ce) throws Exception {
		byte[] byteArquivo = null;
		if (arquivo.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
			File file = new File(getFacadeFactory().getArquivoFacade().realizarDownloadArquivoAmazon(arquivo, servidorExternoAmazon, config, false));
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(file), "O arquivo do tipo documento " + tipoDocumento.toUpperCase() + " não existe no servidor de arquivo on-line", ce);
			if (Uteis.isAtributoPreenchido(file)) {
				byteArquivo = (FileUtils.readFileToByteArray(file));
				if (file.exists()) {
					file.delete();
				}
				if (validarArquivoIsPDFA && !ataColacaoGrau) {
					String extension = FilenameUtils.getExtension(file.getPath());
					Uteis.checkState(!Uteis.isAtributoPreenchido(extension), "Não foi possível identificar qual a extensão do arquivo, extensão obrigatória é PDF, documento: " + tipoDocumento);
					Uteis.checkState(!(extension.contains("pdf") || extension.contains("PDF")), "O arquivo não é PDF/A. Formato obrigatório para documentos, documento: " + tipoDocumento);
					Uteis.checkState(Objects.nonNull(arquivo.getArquivoIsPdfa()) && !arquivo.getArquivoIsPdfa(), "O arquivo não é PDF/A. Formato obrigatório para documentos, documento: " + tipoDocumento);
					if (Objects.isNull(arquivo.getArquivoIsPdfa())) {
						getFacadeFactory().getArquivoHelper().validarArquivoIsPdfa(byteArquivo, tipoDocumento);
					}
				}
			}
		} else {
			File file = new File(config.getLocalUploadArquivoFixo() + File.separator + arquivo.getPastaBaseArquivo() + File.separator + arquivo.getNome());
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(file), "O arquivo do tipo documento " + tipoDocumento.toUpperCase() + " não existe no servidor de arquivo on-line", ce);
			if (Uteis.isAtributoPreenchido(file)) {
				byteArquivo = (FileUtils.readFileToByteArray(file));
				if (validarArquivoIsPDFA && !ataColacaoGrau) {
					String extension = FilenameUtils.getExtension(file.getPath());
					Uteis.checkState(!Uteis.isAtributoPreenchido(extension), "Não foi possível identificar qual a extensão do arquivo, extensão obrigatória é PDF, documento: " + tipoDocumento);
					Uteis.checkState(!(extension.contains("pdf") || extension.contains("PDF")), "O arquivo não é PDF/A. Formato obrigatório para documentos, documento: " + tipoDocumento);
					Uteis.checkState(Objects.nonNull(arquivo.getArquivoIsPdfa()) && !arquivo.getArquivoIsPdfa(), "O arquivo não é PDF/A. Formato obrigatório para documentos, documento: " + tipoDocumento);
					if (Objects.isNull(arquivo.getArquivoIsPdfa())) {
						getFacadeFactory().getArquivoHelper().validarArquivoIsPdfa(byteArquivo, tipoDocumento);
					}
				}
			}
		}
		return byteArquivo;
	}

	private TTipoDocumentacao preencherTipoDocumentacaoComprobatoriaMatricula(DocumetacaoMatriculaVO documetacaoMatriculaVO) {
		return TTipoDocumentacao.valueOf(documetacaoMatriculaVO.getTipoDeDocumentoVO().getTipoDocumentacaoEnum().name());
	}
	
	private List<Object> preencherDocumentacaoComprobatoria(DocumetacaoMatriculaVO documetacaoMatriculaVO, ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigitalVO, ConfiguracaoGeralSistemaVO config, ServidorArquivoOnlineS3RS servidorExternoAmazon, ConsistirException ce){
    	TDocumentacaoComprobatoria.Documento frente = new TDocumentacaoComprobatoria.Documento();
    	frente.setTipo(preencherTipoDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO));
    	String tipoDocumento = documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome();
    	if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOAssinado().getCodigo())) {
    		try {
    			frente.setValue(preencherArquivoByteDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO.getArquivoVOAssinado(), tipoDocumento, configuracaoDiplomaDigitalVO.getValidarArquivoComprobatoriaIsPDFA(), false, servidorExternoAmazon, config, ce));
    		} catch (Exception ignored) {
    			ce.getListaMensagemErro().add(ignored.getMessage());
    		}
    	} else if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoGED().getCodigo())) {
    		try {
    			frente.setValue(preencherArquivoByteDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO.getArquivoGED(), tipoDocumento, configuracaoDiplomaDigitalVO.getValidarArquivoComprobatoriaIsPDFA(), false, servidorExternoAmazon, config, ce));
    		} catch (Exception ignored) {
    			ce.getListaMensagemErro().add(ignored.getMessage());
    		}
    	} else {
    		try {
    			frente.setValue(preencherArquivoByteDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO.getArquivoVO(), tipoDocumento, configuracaoDiplomaDigitalVO.getValidarArquivoComprobatoriaIsPDFA(), false, servidorExternoAmazon, config, ce));
    			if (documetacaoMatriculaVO.getIsPossuiArquivoVerso()) {
    				TDocumentacaoComprobatoria.Documento verso = new TDocumentacaoComprobatoria.Documento();
    				verso.setTipo(frente.getTipo());
    				try {
    					verso.setValue(preencherArquivoByteDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO.getArquivoVOVerso(), tipoDocumento, configuracaoDiplomaDigitalVO.getValidarArquivoComprobatoriaIsPDFA(), false, servidorExternoAmazon, config, ce));
    				} catch (Exception ignored) {
    					ce.getListaMensagemErro().add(ignored.getMessage());
    				}
    				return Arrays.asList(frente, verso);
    			}
    		} catch (Exception ignored) {
    			ce.getListaMensagemErro().add(ignored.getMessage());
    		}
    	}
    	return Collections.singletonList(frente);
    }
    
	private List<Object> preencherDocumentacaoComprobatoriaPorDecisaoJudicial(DocumetacaoMatriculaVO documetacaoMatriculaVO, ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigitalVO, ConfiguracaoGeralSistemaVO config, ServidorArquivoOnlineS3RS servidorExternoAmazon, ConsistirException ce) {
    	TDocumentacaoComprobatoriaPorDecisaoJudicial.Documento frente = new TDocumentacaoComprobatoriaPorDecisaoJudicial.Documento();
    	frente.setTipo(preencherTipoDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO));
    	String tipoDocumento = documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome();
    	if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoVOAssinado().getCodigo())) {
    		try {
    			frente.setValue(preencherArquivoByteDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO.getArquivoVOAssinado(), tipoDocumento, configuracaoDiplomaDigitalVO.getValidarArquivoComprobatoriaIsPDFA(), false, servidorExternoAmazon, config, ce));
    		} catch (Exception ignored) {
    			ce.getListaMensagemErro().add(ignored.getMessage());
    		}
    	} else if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getArquivoGED().getCodigo())) {
    		try {
    			frente.setValue(preencherArquivoByteDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO.getArquivoGED(), tipoDocumento, configuracaoDiplomaDigitalVO.getValidarArquivoComprobatoriaIsPDFA(), false, servidorExternoAmazon, config, ce));
    		} catch (Exception ignored) {
    			ce.getListaMensagemErro().add(ignored.getMessage());
    		}
    	} else {
    		try {
    			frente.setValue(preencherArquivoByteDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO.getArquivoVO(), tipoDocumento, configuracaoDiplomaDigitalVO.getValidarArquivoComprobatoriaIsPDFA(), false, servidorExternoAmazon, config, ce));
    			if (documetacaoMatriculaVO.getIsPossuiArquivoVerso()) {
    				TDocumentacaoComprobatoriaPorDecisaoJudicial.Documento verso = new TDocumentacaoComprobatoriaPorDecisaoJudicial.Documento();
    				verso.setTipo(frente.getTipo());
    				try {
    					verso.setValue(preencherArquivoByteDocumentacaoComprobatoriaMatricula(documetacaoMatriculaVO.getArquivoVOVerso(), tipoDocumento, configuracaoDiplomaDigitalVO.getValidarArquivoComprobatoriaIsPDFA(), false, servidorExternoAmazon, config, ce));
    				} catch (Exception ignored) {
    					ce.getListaMensagemErro().add(ignored.getMessage());
    				}
    				return Arrays.asList(frente, verso);
    			}
    		} catch (Exception ignored) {
    			ce.getListaMensagemErro().add(ignored.getMessage());
    		}
    	}
    	return Collections.singletonList(frente);
    }

    private BigDecimal getHoraRelogioAtividadeComplementar(ConfiguracaoDiplomaDigitalVO configuracaoDiplomaDigital, Integer cargaHoraria) {
		if (Uteis.isAtributoPreenchido(configuracaoDiplomaDigital)) {
			double hora = ((cargaHoraria.doubleValue() * (configuracaoDiplomaDigital.getHoraRelogio() > 0 ? configuracaoDiplomaDigital.getHoraRelogio() : 60)) / 60);
			BigDecimal bd = new BigDecimal(hora);
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			return bd;
		} else {
			double hora = ((cargaHoraria.doubleValue() * 60) / 60);
			BigDecimal bd = new BigDecimal(hora);
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			return bd;
		}
	}
    
    private List<TEntradaHistoricoEstagio> criarHistoricoEscolarEstagioPartirHistorico(List<HistoricoAlunoDisciplinaRelVO> disciplinaRelVOs, ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce) throws ParseException, Exception {
		List<TEntradaHistoricoEstagio> tEntradaHistoricoEstagios = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(disciplinaRelVOs) && disciplinaRelVOs.stream().anyMatch(d -> Uteis.isAtributoPreenchido(d.getCodigoDisciplina()) && d.getDisciplinaEstagio())) {
			List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs = disciplinaRelVOs.stream().filter(d -> Uteis.isAtributoPreenchido(d.getCodigoDisciplina()) && d.getDisciplinaEstagio()).collect(Collectors.toList());
			for (HistoricoAlunoDisciplinaRelVO disciplinaRelVO : historicoAlunoDisciplinaRelVOs) {
				String exception = "disciplina: " + disciplinaRelVO.getNomeDisciplina().toUpperCase() + (Uteis.isAtributoPreenchido(disciplinaRelVO.getAnoSemstre()) ? ", ano/semestre: " + disciplinaRelVO.getAnoSemstre() : Constantes.EMPTY) + " " + TipoOrigemMontagemDados.ESTAGIO.getMensagem();
				TEntradaHistoricoEstagio tEntradaHistoricoEstagio = new TEntradaHistoricoEstagio();
				tEntradaHistoricoEstagio.setCodigoUnidadeCurricular(disciplinaRelVO.getCodigoDisciplina().toString());
				Date dataInicio = disciplinaRelVO.getHistoricoVO().getDataInicioAula();
    			Date dataFim = disciplinaRelVO.getHistoricoVO().getDataFimAula();
    			String exceptionDataInicioHistorico = "A DATA de INÍCIO do HISTÓRICO deve ser informada, " + exception;
    			String exceptionDataFimHistorico = "A DATA FINAL do HISTÓRICO deve ser informada, " + exception;
    			String exceptionCargaHorariaHistorico = "A CARGA HORÁRIA do HISTÓRICO deve ser informada, "  + exception;
    			Uteis.checkStateList(!Uteis.isAtributoPreenchido(dataInicio), exceptionDataInicioHistorico, ce);
    			Uteis.checkStateList(!Uteis.isAtributoPreenchido(dataFim), exceptionDataFimHistorico, ce);
    			Uteis.checkStateList(!Uteis.isAtributoPreenchido(disciplinaRelVO.getChDisciplina()), exceptionCargaHorariaHistorico, ce);
				if (Uteis.isAtributoPreenchido(dataInicio)) {
					tEntradaHistoricoEstagio.setDataInicio(getDataConvertidaXML(dataInicio));
				}
				if (Uteis.isAtributoPreenchido(dataFim)) {
					tEntradaHistoricoEstagio.setDataFim(getDataConvertidaXML(dataFim));
				}
				if (Uteis.isAtributoPreenchido(disciplinaRelVO.getChDisciplina())) {
					THoraRelogioComEtiqueta horaRelogioComEtiqueta = new THoraRelogioComEtiqueta();
					horaRelogioComEtiqueta.setValue(getHoraRelogio(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital(), disciplinaRelVO.getChDisciplina().trim()));
					tEntradaHistoricoEstagio.getCargaHorariaEmHorasRelogio().add(horaRelogioComEtiqueta);
				}
				tEntradaHistoricoEstagio.setDocentesOrientadores(preencherDadosDocenteEntradaHistoricoDisciplina(disciplinaRelVO, expedicaoDiplomaVO, Boolean.TRUE, TipoOrigemMontagemDados.ESTAGIO, ce));
				tEntradaHistoricoEstagios.add(tEntradaHistoricoEstagio);
			}
		}
		return tEntradaHistoricoEstagios;
	}
    
    private List<TEntradaHistoricoEstagioSegundaViaNatoFisica> criarHistoricoEscolarEstagioSegundaViaNatoFisicaPartirHistorico(List<HistoricoAlunoDisciplinaRelVO> disciplinaRelVOs, ExpedicaoDiplomaVO expedicaoDiplomaVO, ConsistirException ce) throws ParseException, Exception {
		List<TEntradaHistoricoEstagioSegundaViaNatoFisica> tEntradaHistoricoEstagios = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(disciplinaRelVOs) && disciplinaRelVOs.stream().anyMatch(d -> Uteis.isAtributoPreenchido(d.getCodigoDisciplina()) && d.getDisciplinaEstagio())) {
			List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs = disciplinaRelVOs.stream().filter(d -> Uteis.isAtributoPreenchido(d.getCodigoDisciplina()) && d.getDisciplinaEstagio()).collect(Collectors.toList());
			for (HistoricoAlunoDisciplinaRelVO disciplinaRelVO : historicoAlunoDisciplinaRelVOs) {
				String exception = "disciplina: " + disciplinaRelVO.getNomeDisciplina().toUpperCase() + (Uteis.isAtributoPreenchido(disciplinaRelVO.getAnoSemstre()) ? ", ano/semestre: " + disciplinaRelVO.getAnoSemstre() : Constantes.EMPTY) + " " + TipoOrigemMontagemDados.ESTAGIO.getMensagem();
				TEntradaHistoricoEstagioSegundaViaNatoFisica tEntradaHistoricoEstagio = new TEntradaHistoricoEstagioSegundaViaNatoFisica();
				tEntradaHistoricoEstagio.setCodigoUnidadeCurricular(disciplinaRelVO.getCodigoDisciplina().toString());
				Date dataInicio = disciplinaRelVO.getHistoricoVO().getDataInicioAula();
    			Date dataFim = disciplinaRelVO.getHistoricoVO().getDataFimAula();
    			String exceptionDataInicioHistorico = "A DATA de INÍCIO do HISTÓRICO deve ser informada, " + exception;
    			String exceptionDataFimHistorico = "A DATA FINAL do HISTÓRICO deve ser informada, " + exception;
    			String exceptionCargaHorariaHistorico = "A CARGA HORÁRIA do HISTÓRICO deve ser informada, "  + exception;
    			Uteis.checkStateList(!Uteis.isAtributoPreenchido(dataInicio), exceptionDataInicioHistorico, ce);
    			Uteis.checkStateList(!Uteis.isAtributoPreenchido(dataFim), exceptionDataFimHistorico, ce);
    			Uteis.checkStateList(!Uteis.isAtributoPreenchido(disciplinaRelVO.getChDisciplina()), exceptionCargaHorariaHistorico, ce);
				if (Uteis.isAtributoPreenchido(dataInicio)) {
					tEntradaHistoricoEstagio.setDataInicio(getDataConvertidaXML(dataInicio));
				}
				if (Uteis.isAtributoPreenchido(dataFim)) {
					tEntradaHistoricoEstagio.setDataFim(getDataConvertidaXML(dataFim));
				}
				if (Uteis.isAtributoPreenchido(disciplinaRelVO.getChDisciplina())) {
					THoraRelogioComEtiqueta horaRelogioComEtiqueta = new THoraRelogioComEtiqueta();
					horaRelogioComEtiqueta.setValue(getHoraRelogio(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital(), disciplinaRelVO.getChDisciplina().trim()));
					tEntradaHistoricoEstagio.getCargaHorariaEmHorasRelogio().add(horaRelogioComEtiqueta);
				}
				tEntradaHistoricoEstagio.setDocentesOrientadores(preencherDadosDocenteEntradaHistoricoDisciplina(disciplinaRelVO, expedicaoDiplomaVO, Boolean.TRUE, TipoOrigemMontagemDados.ESTAGIO, ce));
				tEntradaHistoricoEstagios.add(tEntradaHistoricoEstagio);
			}
		}
		return tEntradaHistoricoEstagios;
	}
    
    private void preecherDadosHabilitacaoCurso(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosCurso dadosCurso) throws Exception {
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getCurso().getHabilitacao())) {
			THabilitacao habilitacao = preencherDadosHabilitacaoCurso(expedicaoDiplomaVO.getMatricula().getCurso());
			dadosCurso.getHabilitacao().add(habilitacao);
		}
	}
    
    private void preecherDadosHabilitacaoDecisaoJudicialCurso(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosCursoPorDecisaoJudicial dadosCurso) throws Exception {
    	if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getCurso().getHabilitacao())) {
    		THabilitacao habilitacao = preencherDadosHabilitacaoCurso(expedicaoDiplomaVO.getMatricula().getCurso());
    		dadosCurso.getHabilitacao().add(habilitacao);
    	}
    }
    
    private void preecherDadosHabilitacaoMinimoCurso(ExpedicaoDiplomaVO expedicaoDiplomaVO, TDadosMinimoCurso dadosCurso) throws Exception {
    	if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getMatricula().getCurso().getHabilitacao())) {
    		THabilitacao habilitacao = preencherDadosHabilitacaoCurso(expedicaoDiplomaVO.getMatricula().getCurso());
    		dadosCurso.getHabilitacao().add(habilitacao);
    	}
    }
	
	private THabilitacao preencherDadosHabilitacaoCurso(CursoVO cursoVO) throws Exception {
		THabilitacao habilitacao = new THabilitacao();
		habilitacao.setNomeHabilitacao(getStringXml(cursoVO.getHabilitacao()));
		if (Uteis.isAtributoPreenchido(cursoVO.getDataHabilitacao())) {
			habilitacao.setDataHabilitacao(getDataConvertidaXML(cursoVO.getDataHabilitacao()));
		}
		return habilitacao;
	}
    
    private void validarDadosCurriculoEscolar(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricularVO) {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(gestaoXmlGradeCurricularVO.getGradeCurricularVO().getCodigo()), "O código da matriz curricular deve ser informado.", gestaoXmlGradeCurricularVO.getConsistirException());
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(gestaoXmlGradeCurricularVO.getGradeCurricularVO().getDataCadastro()), "A data de cadastro da matriz curricular deve ser informado.", gestaoXmlGradeCurricularVO.getConsistirException());
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(gestaoXmlGradeCurricularVO.getGradeCurricularVO().getCargaHoraria()), "A carga horaria da matriz curricular deve ser informado.", gestaoXmlGradeCurricularVO.getConsistirException());
	}
	
	private void preencherDadosCurriculoEscolar(TInfCurriculoEscolar infCurriculoEscolar, GestaoXmlGradeCurricularVO gestaoXmlGradeCurricularVO) throws Exception {
		validarDadosCurriculoEscolar(gestaoXmlGradeCurricularVO);
		infCurriculoEscolar.setCodigoCurriculo(gestaoXmlGradeCurricularVO.getGradeCurricularVO().getCodigo().toString());
		if (Uteis.isAtributoPreenchido(gestaoXmlGradeCurricularVO.getGradeCurricularVO().getDataCadastro())) {
			infCurriculoEscolar.setDataCurriculo(getDataConvertidaXML(gestaoXmlGradeCurricularVO.getGradeCurricularVO().getDataCadastro()));
		}
		infCurriculoEscolar.setMinutosRelogioDaHoraAula(gestaoXmlGradeCurricularVO.getGradeCurricularVO().getCargaHoraria().longValue());
		infCurriculoEscolar.setNomeParaAreas("Area de Conhecimento");
		infCurriculoEscolar.setDadosCurso(preencherDadosCursoMinimo(gestaoXmlGradeCurricularVO.getCursoVO(), gestaoXmlGradeCurricularVO.getConsistirException()));
		infCurriculoEscolar.setIesEmissora(preencherDadosIesEmissora(gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO(), gestaoXmlGradeCurricularVO.getCursoVO(), new UsuarioVO(), gestaoXmlGradeCurricularVO.getConsistirException()));
		infCurriculoEscolar.setInfAreas(preencherDadosAreas(gestaoXmlGradeCurricularVO.getCursoVO(), gestaoXmlGradeCurricularVO.getConsistirException()));
		infCurriculoEscolar.setInfEstruturaCurricular(preencherDadosEstruturaCurricular(gestaoXmlGradeCurricularVO.getGradeCurricularVO(), gestaoXmlGradeCurricularVO.getConsistirException()));
		infCurriculoEscolar.setInfEtiquetas(getInfEtiquetas(infCurriculoEscolar.getInfEstruturaCurricular().getUnidadeCurricular()));
		infCurriculoEscolar.setInfEstruturaAtividadesComplementares(preencherDadoEstruturaAtividadeComplementar(gestaoXmlGradeCurricularVO.getGradeCurricularVO(), gestaoXmlGradeCurricularVO.getConsistirException()));
		infCurriculoEscolar.setInfCriteriosIntegralizacao(preencherDadoCriteriosIntegralizacao(gestaoXmlGradeCurricularVO.getGradeCurricularVO(), gestaoXmlGradeCurricularVO.getConsistirException()));
		infCurriculoEscolar.setSegurancaCurriculo(preencherDadoSegurancaCurriculo(gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO(), gestaoXmlGradeCurricularVO.getGradeCurricularVO()));
	}
	
	private TInfEtiquetas getInfEtiquetas(List<TUnidadeCurricular> unidadeCurriculars) {
		TInfEtiquetas infEtiquetas = new TInfEtiquetas();
		if (Uteis.isAtributoPreenchido(unidadeCurriculars)) {
			Map<TipoEtiquetaEnum, List<TEtiqueta>> etiquetas = unidadeCurriculars.stream().map(u -> u.getEtiquetas().getEtiqueta()).flatMap(Collection::stream).collect(Collectors.groupingBy(u -> TipoEtiquetaEnum.getEnum(u.getCodigo())));
			if (!etiquetas.isEmpty()) {
				for (TipoEtiquetaEnum etiqueta : etiquetas.keySet()) {
					TDadoEtiqueta dadoEtiqueta = new TDadoEtiqueta();
					dadoEtiqueta.setCodigo(etiqueta.getValor());
					dadoEtiqueta.setNome(etiqueta.getNome());
					infEtiquetas.getEtiqueta().add(dadoEtiqueta);
				}
			}
		}
		return infEtiquetas;
	}
	
	private void validarDadosAreas(CursoVO cursoVO, ConsistirException ce) {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getAreaConhecimento()), "A area de conhecimento do curso deve ser informado (Dados do Curso).", ce);
	}
	
	private TInfAreas preencherDadosAreas(CursoVO cursoVO, ConsistirException ce) {
		validarDadosAreas(cursoVO, ce);
		TInfAreas infAreas = new TInfAreas();
		TDadoArea dadoArea = new TDadoArea();
		dadoArea.getCodigo().add(cursoVO.getAreaConhecimento().getCodigo().toString());
		dadoArea.getNome().add(getStringXml(cursoVO.getAreaConhecimento().getNome().trim()));
		infAreas.getArea().add(dadoArea);
		return infAreas;
	}
	
	private void validarDadosMinimoCurso(CursoVO cursoVO, ConsistirException ce) {
		if (!Uteis.isAtributoPreenchido(cursoVO.getNomeDocumentacao()) && !Uteis.isAtributoPreenchido(cursoVO.getNome())) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getCodigo()), "O nome do curso deve ser informado (Dados do Curso).", ce);
		}
		if (cursoVO.getPossuiCodigoEMEC()) {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getCodigoEMEC()), "O Código EMEC deve ser informado (Dados do curso).", ce);
		} else {
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getNumeroProcessoEMEC()), "O Número Processo EMEC deve ser informado (Dados do curso).", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getTipoProcessoEMEC().trim()), "O Tipo Processo EMEC deve ser informado (Dados do curso).", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataCadastroEMEC()), "A Data Cadastro EMEC deve ser informado (Dados do curso).", ce);
			Uteis.checkStateList(!Uteis.isAtributoPreenchido(cursoVO.getDataProtocoloEMEC()), "A Data Protocolo EMEC deve ser informado (Dados do curso).", ce);
		}
	}
	
	private TDadosMinimoCurso preencherDadosCursoMinimo(CursoVO cursoVO, ConsistirException ce) throws Exception {
		validarDadosMinimoCurso(cursoVO, ce);
		TDadosMinimoCurso dadosMinimoCurso = new TDadosMinimoCurso();
		dadosMinimoCurso.setNomeCurso(Uteis.isAtributoPreenchido(cursoVO.getNomeDocumentacao()) ? getStringXml(cursoVO.getNomeDocumentacao()) : getStringXml(cursoVO.getNome()));
		if (cursoVO.getPossuiCodigoEMEC()) {
			dadosMinimoCurso.setCodigoCursoEMEC(Long.valueOf(cursoVO.getCodigoEMEC()));
		} else {
			dadosMinimoCurso.setSemCodigoCursoEMEC(getCursoInformacoesTramitacaoEMEC(cursoVO));
		}
		dadosMinimoCurso.setAutorizacao(preencherDadosAutorizacaoCurso(cursoVO, ce));
		preencherDadosCurriculoEscolarDigitalCursoReconhecimento(cursoVO, dadosMinimoCurso, ce);
		return dadosMinimoCurso;
	}
	
	private void preencherDadosCurriculoEscolarDigitalCursoReconhecimento(CursoVO cursoVO, TDadosMinimoCurso dadosMinimoCurso, ConsistirException ex) throws Exception {
		AutorizacaoCursoVO reconhecimentoCurso = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataMaisAntigo(cursoVO.getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		AutorizacaoCursoVO renovacaoReconhecimentoCurso = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataMaisRecente(cursoVO.getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		if (Uteis.isAtributoPreenchido(reconhecimentoCurso)) {
			dadosMinimoCurso.setReconhecimento(preencherDadosReconhecimentoCurso(reconhecimentoCurso, ex));
			dadosMinimoCurso.setRenovacaoReconhecimento(preencherDadosRenovacaoReconhecimentoCurso(reconhecimentoCurso, renovacaoReconhecimentoCurso, ex));
		} else {
			Uteis.checkStateList(Boolean.TRUE, "O reconhecimento do curso deve ser informado.", ex);
		}
	}
	
	private TInfEstruturaCurricular preencherDadosEstruturaCurricular(GradeCurricularVO gradeCurricularVO, ConsistirException ce) {
		TInfEstruturaCurricular infEstruturaCurricular = new TInfEstruturaCurricular();
		if (Uteis.isAtributoPreenchido(gradeCurricularVO.getPeriodoLetivosVOs())) {
			gradeCurricularVO.getPeriodoLetivosVOs().stream().forEach(p -> preenherDadosUnidadeCurricular(p, infEstruturaCurricular.getUnidadeCurricular(), ce));
		} else {
			Uteis.checkStateList(Boolean.TRUE, "Os períodos letivos da matriz curricular deve ser informado (Dados da Matriz Curricular)", ce);
		}
		if (Uteis.isAtributoPreenchido(gradeCurricularVO.getListaGradeCurricularEstagioVO())) {
			gradeCurricularVO.getListaGradeCurricularEstagioVO().stream().forEach(e -> preenherDadosUnidadeCurricularEstagioObrigatorio(e, infEstruturaCurricular.getUnidadeCurricular(), ce));
		}
		return infEstruturaCurricular;
	}
	
	private void validarDadosUnidadeCurricular(GradeDisciplinaVO gradeDisciplina, String periodoLetivo, ConsistirException ce) {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(gradeDisciplina.getDisciplina()), "A disciplina da grade disciplina de código " + gradeDisciplina.getCodigo() + " no período " + periodoLetivo + " deve ser informado (Dados da Unidade Curricular).", ce);
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(gradeDisciplina.getCargaHoraria()), "A carga horaria da disciplina " + gradeDisciplina.getDisciplina().getNome() + " no período " + periodoLetivo + " deve ser informado (Dados da Unidade Curricular).", ce);
	}
	
	private String getEtiquetaTipoDisciplina(TipoDisciplina tipoDisciplina) {
		switch (tipoDisciplina) {
		case OBRIGATORIA:
			return TipoEtiquetaEnum.OBRIGATORIA.getValor();
		case OPTATIVA:
			return TipoEtiquetaEnum.OPTATIVA.getValor();
		case LABORATORIAL_OBRIGATORIA:
			return TipoEtiquetaEnum.LABORATORIAL_OBRIGATORIA.getValor();
		case LABORATORIAL_OPTATIVA:
			return TipoEtiquetaEnum.LABORATORIAL_OPTATIVA.getValor();
		}
		return null;
	}
	
	@SuppressWarnings("incomplete-switch")
	private String getEtiquetaModalidadeDisciplina(ModalidadeDisciplinaEnum modalidadeDisciplina) {
		switch (modalidadeDisciplina) {
		case ON_LINE:
			return TipoEtiquetaEnum.MODALIDADE_ONLINE.getValor();
		case PRESENCIAL:
			return TipoEtiquetaEnum.MODALIDADE_PRESENCIAL.getValor();
		}
		return null;
	}
	
	private TEtiquetas preencherDadoEtiquetaDisciplina(GradeDisciplinaVO gradeDisciplina) {
		TEtiquetas etiquetas = new TEtiquetas();
		if (Uteis.isAtributoPreenchido(gradeDisciplina.getTipoDisciplina())) {
			TEtiqueta etiqueta = new TEtiqueta();
			etiqueta.setCodigo(getEtiquetaTipoDisciplina(TipoDisciplina.getEnum(gradeDisciplina.getTipoDisciplina())));
			if (Uteis.isAtributoPreenchido(etiqueta.getCodigo())) {
				etiquetas.getEtiqueta().add(etiqueta);
			}
		}
		if (Uteis.isAtributoPreenchido(gradeDisciplina.getModalidadeDisciplina())) {
			TEtiqueta etiqueta = new TEtiqueta();
			etiqueta.setCodigo(getEtiquetaModalidadeDisciplina(gradeDisciplina.getModalidadeDisciplina()));
			if (Uteis.isAtributoPreenchido(etiqueta.getCodigo())) {
				etiquetas.getEtiqueta().add(etiqueta);
			}
		}
		if (Uteis.isAtributoPreenchido(gradeDisciplina.getCargaHorariaPratica())) {
			TEtiqueta etiqueta = new TEtiqueta();
			etiqueta.setCodigo(TipoEtiquetaEnum.PRATICA.getValor());
			etiqueta.setCargaHorariaEmHoraAula(gradeDisciplina.getCargaHorariaPratica().longValue());
			etiqueta.setCargaHorariaEmHoraRelogio(getHoraRelogio(null, gradeDisciplina.getCargaHorariaPratica().toString()));
			etiquetas.getEtiqueta().add(etiqueta);
		}
		if (Uteis.isAtributoPreenchido(gradeDisciplina.getCargaHorariaTeorica())) {
			TEtiqueta etiqueta = new TEtiqueta();
			etiqueta.setCodigo(TipoEtiquetaEnum.TEORICA.getValor());
			etiqueta.setCargaHorariaEmHoraAula(gradeDisciplina.getCargaHorariaTeorica().longValue());
			etiqueta.setCargaHorariaEmHoraRelogio(getHoraRelogio(null, gradeDisciplina.getCargaHorariaTeorica().toString()));
			etiquetas.getEtiqueta().add(etiqueta);
		}
		return Uteis.isAtributoPreenchido(etiquetas.getEtiqueta()) ? etiquetas : null;
	}
	
	private TEquivalenciaUnidadesCurriculares preencherDadoDsciplinaEquivalente(DisciplinaVO disciplina) {
		TEquivalenciaUnidadesCurriculares equivalenciaUnidadesCurriculares = new TEquivalenciaUnidadesCurriculares();
		if (Uteis.isAtributoPreenchido(disciplina.getDisciplinasEquivalentes())) {
			for (Integer disciplinaEquivalente : disciplina.getDisciplinasEquivalentes()) {
				UnidadesCurricularesEquivalente unidadesCurricularesEquivalente = new UnidadesCurricularesEquivalente();
				unidadesCurricularesEquivalente.getCodigoUnidadeEquivalente().add(disciplinaEquivalente.toString());
				equivalenciaUnidadesCurriculares.getUnidadesCurricularesEquivalente().add(unidadesCurricularesEquivalente);
			}
		}
		return Uteis.isAtributoPreenchido(equivalenciaUnidadesCurriculares.getUnidadesCurricularesEquivalente()) ? equivalenciaUnidadesCurriculares : null;
	}
	
	private TPreRequisitosUnidadesCurriculares preencherDadoPreRequisitoDisciplina(GradeDisciplinaVO gradeDisciplina) {
		TPreRequisitosUnidadesCurriculares preRequisitosUnidadesCurriculares = new TPreRequisitosUnidadesCurriculares();
		if (Uteis.isAtributoPreenchido(gradeDisciplina.getDisciplinaRequisitoVOs())) {
			for (DisciplinaPreRequisitoVO disciplinaPreRequisito : gradeDisciplina.getDisciplinaRequisitoVOs()) {
				preRequisitosUnidadesCurriculares.getCodigoDependencia().add(disciplinaPreRequisito.getDisciplina().getCodigo().toString());
			}
		}
		return Uteis.isAtributoPreenchido(preRequisitosUnidadesCurriculares.getCodigoDependencia()) ? preRequisitosUnidadesCurriculares : null;
	}
	
	private TAreas preencherDadoAreaConhecimentoDisciplina(DisciplinaVO disciplina) {
		return null;
//		TAreas areas = new TAreas();
//		if (Uteis.isAtributoPreenchido(disciplina.getDisciplinaAreaConhecimentoVOs())) {
//			for (DisciplinaAreaConhecimentoVO areaConhecimento : disciplina.getDisciplinaAreaConhecimentoVOs()) {
//				TCodigoArea codigoArea = new TCodigoArea();
//				codigoArea.setCodigo(areaConhecimento.getAreaConhecimentoVO().getCodigo().toString());
//				areas.getArea().add(codigoArea);
//			}
//		}
//		return Uteis.isAtributoPreenchido(areas.getArea()) ? areas : null;
	}
	
	private TTipoUnidadeCurricular getTipoUnidadeCurricularDisciplina(GradeDisciplinaVO gradeDisciplina) {
		if (gradeDisciplina.getDisciplinaEstagio()) {
			return TTipoUnidadeCurricular.ESTÁGIO;
		} else {
			return TTipoUnidadeCurricular.DISCIPLINA;
		}
	}
	
	private void preenherDadosUnidadeCurricular(PeriodoLetivoVO periodoLetivo, List<TUnidadeCurricular> unidadeCurriculars, ConsistirException ce) {
		if (Uteis.isAtributoPreenchido(periodoLetivo.getGradeDisciplinaVOs())) { 
			for (GradeDisciplinaVO gradeDisciplina : periodoLetivo.getGradeDisciplinaVOs()) {
				if (gradeDisciplina.getUtilizarEmissaoXmlDiploma()) {
					validarDadosUnidadeCurricular(gradeDisciplina, periodoLetivo.getDescricao(), ce);
					TUnidadeCurricular unidadeCurricular = new TUnidadeCurricular();
					unidadeCurricular.setTipo(getTipoUnidadeCurricularDisciplina(gradeDisciplina));
					unidadeCurricular.setCodigo(gradeDisciplina.getDisciplina().getCodigo().toString());
					unidadeCurricular.setNome(getStringXml(gradeDisciplina.getDisciplina().getNome().trim()));
					unidadeCurricular.setCargaHorariaEmHoraAula(gradeDisciplina.getCargaHoraria().longValue());
					unidadeCurricular.setCargaHorariaEmHoraRelogio(getHoraRelogio(null, gradeDisciplina.getCargaHoraria().toString()));
					unidadeCurricular.setFase(getStringXml(periodoLetivo.getDescricao().trim()));
					unidadeCurricular.setEquivalencias(preencherDadoDsciplinaEquivalente(gradeDisciplina.getDisciplina()));
					unidadeCurricular.setPreRequisitos(preencherDadoPreRequisitoDisciplina(gradeDisciplina));
					unidadeCurricular.setEtiquetas(preencherDadoEtiquetaDisciplina(gradeDisciplina));
//					unidadeCurricular.setAreas(preencherDadoAreaConhecimentoDisciplina(gradeDisciplina.getDisciplina()));
					unidadeCurriculars.add(unidadeCurricular);
				}
			}
		} else {
			Uteis.checkStateList(Boolean.TRUE, "As disciplinas do período letivo " + periodoLetivo.getDescricao() + " deve ser informado (Dados da Matriz Curricular).", ce);
		}
	}
	
	private TInfEstruturaAtividadesComplementares preencherDadoEstruturaAtividadeComplementar(GradeCurricularVO gradeCurricular, ConsistirException ce) {
		TInfEstruturaAtividadesComplementares infEstruturaAtividadesComplementares = new TInfEstruturaAtividadesComplementares();
		if (Uteis.isAtributoPreenchido(gradeCurricular.getListaGradeCurricularTipoAtividadeComplementarVOs())) {
			Map<String, TCategoriaAtividadeComplementar> map = new HashMap<>(0);
			for (GradeCurricularTipoAtividadeComplementarVO a : gradeCurricular.getListaGradeCurricularTipoAtividadeComplementarVOs()) {
				if (Uteis.isAtributoPreenchido(a.getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior())) {
					if (!(map.containsKey(a.getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().getCodigo().toString()))) {
						TCategoriaAtividadeComplementar categoriaAtividadeComplementar = new TCategoriaAtividadeComplementar();
						categoriaAtividadeComplementar.setCodigo(a.getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().getCodigo().toString());
						categoriaAtividadeComplementar.setNome(getStringXml(a.getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().getNome().trim()));
						map.put(a.getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().getCodigo().toString(), categoriaAtividadeComplementar);
					}
					TCategoriaAtividadeComplementar categoria = map.get(a.getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().getCodigo().toString());
					categoria.getAtividades().getAtividade().add(preencherDadoCategoriaAtividadeComplementar(a, ce));
				} else {
					if (!(map.containsKey("atividade-complementar"))) {
						TCategoriaAtividadeComplementar categoriaAtividadeComplementar = new TCategoriaAtividadeComplementar();
						categoriaAtividadeComplementar.setCodigo("atividade-complementar");
						categoriaAtividadeComplementar.setNome("Atividades Complementares");
						categoriaAtividadeComplementar.setAtividades(new TAtividadesComplementares());
						map.put("atividade-complementar", categoriaAtividadeComplementar);
					}
					TCategoriaAtividadeComplementar categoria = map.get("atividade-complementar");
					categoria.getAtividades().getAtividade().add(preencherDadoCategoriaAtividadeComplementar(a, ce));
				}
			}
			if (map != null && !map.values().isEmpty()) {
				infEstruturaAtividadesComplementares.getCategoria().addAll(map.values());
			}
		}
		return Uteis.isAtributoPreenchido(infEstruturaAtividadesComplementares.getCategoria()) ? infEstruturaAtividadesComplementares : null;
	}
	
	private TAtividadeComplementar preencherDadoCategoriaAtividadeComplementar(GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarVOs, ConsistirException ce) {
		Uteis.checkStateList(!Uteis.isAtributoPreenchido(gradeCurricularTipoAtividadeComplementarVOs.getCargaHoraria()), "A carga horária do tipo de atividade complementar " + gradeCurricularTipoAtividadeComplementarVOs.getTipoAtividadeComplementarVO().getNome() + " deve ser informado (Dados da Atividade Complementar).", ce);
		TAtividadeComplementar atividadeComplementar = new TAtividadeComplementar();
		atividadeComplementar.setCodigo(gradeCurricularTipoAtividadeComplementarVOs.getTipoAtividadeComplementarVO().getCodigo().toString());
		atividadeComplementar.setNome(getStringXml(gradeCurricularTipoAtividadeComplementarVOs.getTipoAtividadeComplementarVO().getNome().trim()));
		if (Uteis.isAtributoPreenchido(gradeCurricularTipoAtividadeComplementarVOs.getCargaHoraria())) {
			atividadeComplementar.setLimiteCargaHorariaEmHoraRelogio(getHoraRelogio(null, gradeCurricularTipoAtividadeComplementarVOs.getCargaHoraria().toString()));
		}
		return atividadeComplementar;
	}
	
	private TInfCriteriosIntegralizacao preencherDadoCriteriosIntegralizacao(GradeCurricularVO gradeCurricular, ConsistirException ce) throws Exception {
		TInfCriteriosIntegralizacao infCriteriosIntegralizacao = new TInfCriteriosIntegralizacao();
		try {
			montarCriterioIntegralizacaoCriterio1(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao(), gradeCurricular);
			montarCriterioIntegralizacaoCriterio2(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao(), gradeCurricular);
			montarCriterioIntegralizacaoCriterio3(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao(), gradeCurricular);
			montarCriterioIntegralizacaoCriterio4(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao(), gradeCurricular);
			montarCriterioIntegralizacaoCriterio5(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao(), gradeCurricular);
			montarCriterioIntegralizacaoCriterio6(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao(), gradeCurricular);
			montarCriterioIntegralizacaoCriterio7(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao(), gradeCurricular);
			montarCriterioIntegralizacaoCriterio8(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao(), gradeCurricular);
			montarCriterioIntegralizacaoCriterio9(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao(), gradeCurricular);
			montarCriterioIntegralizacaoCriterio10(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao(), gradeCurricular);
			montarCriterioIntegralizacaoCriterio11(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao(), gradeCurricular);
			montarCriterioIntegralizacaoCriterio12(infCriteriosIntegralizacao.getCriterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao());
		} catch (Exception e) {
			Uteis.checkStateList(Boolean.TRUE, e.getMessage(), ce);
		}
		return infCriteriosIntegralizacao;
	}
	
	private void montarCriterioIntegralizacaoCriterio1(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao, GradeCurricularVO gradeCurricular) {
		BigDecimal cargaHorariaDisciplinaObrigatoria = new BigDecimal(gradeCurricular.getChDisciplinaObrigatoria());
		TCriterioIntegralizacaoRotulos C1 = montarCriterioIntegralizacaoRotulos("C1", TTipoUnidadeCurricular.DISCIPLINA, Arrays.asList(TipoEtiquetaEnum.OBRIGATORIA.getValor()), cargaHorariaDisciplinaObrigatoria, cargaHorariaDisciplinaObrigatoria, cargaHorariaDisciplinaObrigatoria);
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C1);
	}
	
	private void montarCriterioIntegralizacaoCriterio2(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao, GradeCurricularVO gradeCurricular) {
		BigDecimal cargaHorariaDisciplinaPratica = new BigDecimal(gradeCurricular.getChDisciplinaObrigatoriaPratica());
		TCriterioIntegralizacaoRotulos C2 = montarCriterioIntegralizacaoRotulos("C2", TTipoUnidadeCurricular.DISCIPLINA, Arrays.asList(TipoEtiquetaEnum.OBRIGATORIA.getValor(), TipoEtiquetaEnum.PRATICA.getValor()), cargaHorariaDisciplinaPratica, cargaHorariaDisciplinaPratica, cargaHorariaDisciplinaPratica);
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C2);
	}
	
	private void montarCriterioIntegralizacaoCriterio3(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao, GradeCurricularVO gradeCurricular) {
		BigDecimal cargaHorariaDisciplinaTeorica = new BigDecimal(gradeCurricular.getChDisciplinaObrigatoriaTeorica());
		TCriterioIntegralizacaoRotulos C3 = montarCriterioIntegralizacaoRotulos("C3", TTipoUnidadeCurricular.DISCIPLINA, Arrays.asList(TipoEtiquetaEnum.OBRIGATORIA.getValor(), TipoEtiquetaEnum.TEORICA.getValor()), cargaHorariaDisciplinaTeorica, cargaHorariaDisciplinaTeorica, cargaHorariaDisciplinaTeorica);
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C3);
	}
	
	private void montarCriterioIntegralizacaoCriterio4(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao, GradeCurricularVO gradeCurricular) {
		Integer cargaHoraria = gradeCurricular.getTotalCargaHorariaAtividadeComplementar();
		TCriterioIntegralizacaoRotulos C4 = montarCriterioIntegralizacaoRotulos("C4", TTipoUnidadeCurricular.ATIVIDADE_COMPLEMENTAR, new ArrayList<>(0), new BigDecimal(cargaHoraria), new BigDecimal(cargaHoraria), new BigDecimal(cargaHoraria));
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C4);
	}
	private void montarCriterioIntegralizacaoCriterio5(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao, GradeCurricularVO gradeCurricular) {
		BigDecimal cargaHorariaEstagio = new BigDecimal(gradeCurricular.getChDisciplinaEstagio());
		TCriterioIntegralizacaoRotulos C5 = montarCriterioIntegralizacaoRotulos("C5", TTipoUnidadeCurricular.ESTÁGIO, new ArrayList<>(0), cargaHorariaEstagio, cargaHorariaEstagio, cargaHorariaEstagio);
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C5);
	}
	
	private void montarCriterioIntegralizacaoCriterio6(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao, GradeCurricularVO gradeCurricular) {
		BigDecimal cargaHorariaTotalDisciplinaOptativa = new BigDecimal(gradeCurricular.getChDisciplinaOptativa());
		BigDecimal cargaHorariaDisciplinaOptativa = new BigDecimal(gradeCurricular.getTotalCargaHorariaOptativaExigida());
		TCriterioIntegralizacaoRotulos C6 = montarCriterioIntegralizacaoRotulos("C6", TTipoUnidadeCurricular.DISCIPLINA, Arrays.asList(TipoEtiquetaEnum.OPTATIVA.getValor()), cargaHorariaDisciplinaOptativa, cargaHorariaTotalDisciplinaOptativa, cargaHorariaDisciplinaOptativa);
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C6);
	}
	
	private void montarCriterioIntegralizacaoCriterio7(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao, GradeCurricularVO gradeCurricular) {
		BigDecimal cargaHorariaTotalDisciplinaOptativaPratica = new BigDecimal(gradeCurricular.getChDisciplinaOptativaPratica());
		TCriterioIntegralizacaoRotulos C7 = montarCriterioIntegralizacaoRotulos("C7", TTipoUnidadeCurricular.DISCIPLINA, Arrays.asList(TipoEtiquetaEnum.OPTATIVA.getValor(), TipoEtiquetaEnum.PRATICA.getValor()), BigDecimal.ZERO, cargaHorariaTotalDisciplinaOptativaPratica, BigDecimal.ZERO);
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C7);
	}
	
	private void montarCriterioIntegralizacaoCriterio8(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao, GradeCurricularVO gradeCurricular) {
		BigDecimal cargaHorariaTotalDisciplinaOptativaTeorica = new BigDecimal(gradeCurricular.getChDisciplinaOptativaTeorica());
		TCriterioIntegralizacaoRotulos C8 = montarCriterioIntegralizacaoRotulos("C8", TTipoUnidadeCurricular.DISCIPLINA, Arrays.asList(TipoEtiquetaEnum.OPTATIVA.getValor(), TipoEtiquetaEnum.TEORICA.getValor()), BigDecimal.ZERO, cargaHorariaTotalDisciplinaOptativaTeorica, BigDecimal.ZERO);
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C8);
	}
	
	private void montarCriterioIntegralizacaoCriterio9(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao, GradeCurricularVO gradeCurricular) {
		BigDecimal totalCargaHoraria = new BigDecimal(gradeCurricular.getChDisciplinaTotal());
		TCriterioIntegralizacaoRotulos C9 = montarCriterioIntegralizacaoRotulos("C9", TTipoUnidadeCurricular.DISCIPLINA, new ArrayList<>(0), BigDecimal.ZERO, totalCargaHoraria, BigDecimal.ZERO);
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C9);
	}
	
	private void montarCriterioIntegralizacaoCriterio10(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao, GradeCurricularVO gradeCurricular) {
		BigDecimal totalCargaHoraria = new BigDecimal(gradeCurricular.getChDisciplinaTeoricaTotal());
		TCriterioIntegralizacaoRotulos C10 = montarCriterioIntegralizacaoRotulos("C10", TTipoUnidadeCurricular.DISCIPLINA, Arrays.asList(TipoEtiquetaEnum.TEORICA.getValor()), BigDecimal.ZERO, totalCargaHoraria, BigDecimal.ZERO);
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C10);
	}
	
	private void montarCriterioIntegralizacaoCriterio11(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao, GradeCurricularVO gradeCurricular) {
		BigDecimal totalCargaHoraria = new BigDecimal(gradeCurricular.getChDisciplinaPraticaTotal());
		TCriterioIntegralizacaoRotulos C11 = montarCriterioIntegralizacaoRotulos("C11", TTipoUnidadeCurricular.DISCIPLINA, Arrays.asList(TipoEtiquetaEnum.PRATICA.getValor()), BigDecimal.ZERO, totalCargaHoraria, BigDecimal.ZERO);
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C11);
	}
	
	private void montarCriterioIntegralizacaoCriterio12(List<Object> criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao) {
		TCriterioIntegralizacaoExpressao C12 = montarCriterioIntegralizacaoExpressao("C12", criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao);
		criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao.add(C12);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private TCriterioIntegralizacaoExpressao montarCriterioIntegralizacaoExpressao(String codigo, List criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao) {
		TCriterioIntegralizacaoExpressao criterioIntegralizacaoExpressao = new TCriterioIntegralizacaoExpressao();
		if (Uteis.isAtributoPreenchido(criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao)) {
			List<TCriterioIntegralizacaoRotulos> listaCriterioIntegralizacaoRotulos = ((List<TCriterioIntegralizacaoRotulos>) criterioIntegralizacaoRotulosOrCriterioIntegralizacaoExpressao).stream().filter(c -> c.getCodigo().equals("C1") || c.getCodigo().equals("C6") || c.getCodigo().equals("C7") || c.getCodigo().equals("C8")).collect(Collectors.toList());
			if (Uteis.isAtributoPreenchido(listaCriterioIntegralizacaoRotulos)) {
				criterioIntegralizacaoExpressao.setCodigo(codigo);
				TExpressao expressao = new TExpressao();
				TCodigos codigos = new TCodigos();
				codigos.getCodigo().addAll(Arrays.asList("C1", "C4", "C5", "C6"));
				expressao.setSoma(codigos);
				criterioIntegralizacaoExpressao.setExpressao(expressao);
				Optional<BigDecimal> cargaHorariaMaxima = listaCriterioIntegralizacaoRotulos.stream().map(c -> c.getCargasHorariasCriterio().getCargaHorariaMaxima()).reduce(BigDecimal::add);
				Optional<BigDecimal> cargaHorariaMinima = listaCriterioIntegralizacaoRotulos.stream().map(c -> c.getCargasHorariasCriterio().getCargaHorariaMinima()).reduce(BigDecimal::add);
				Optional<BigDecimal> cargaHorariaParaTotal = listaCriterioIntegralizacaoRotulos.stream().map(c -> c.getCargasHorariasCriterio().getCargaHorariaParaTotal()).reduce(BigDecimal::add);
				TCriterioLimitesCargas criterioLimitesCargas = new TCriterioLimitesCargas();
				criterioLimitesCargas.setCargaHorariaMaxima(cargaHorariaMaxima.isPresent() ? cargaHorariaMaxima.get() : BigDecimal.ZERO);
				criterioLimitesCargas.setCargaHorariaMinima(cargaHorariaMinima.isPresent() ? cargaHorariaMinima.get() : BigDecimal.ZERO);
				criterioLimitesCargas.setCargaHorariaParaTotal(cargaHorariaParaTotal.isPresent() ? cargaHorariaParaTotal.get() : BigDecimal.ZERO);
				criterioIntegralizacaoExpressao.setCargasHorariasCriterio(criterioLimitesCargas);
			}
		}
		return criterioIntegralizacaoExpressao;
	}
	
	private TCriterioIntegralizacaoRotulos montarCriterioIntegralizacaoRotulos(String codigo, TTipoUnidadeCurricular tipoUnidadeCurricular, List<String> etiquetas, BigDecimal cargaHorariaMinima, BigDecimal cargaHorariaMaxima, BigDecimal cargaHorariaParaTotal) {
		TCriterioIntegralizacaoRotulos criterioIntegralizacaoRotulos = new TCriterioIntegralizacaoRotulos();
		criterioIntegralizacaoRotulos.setCodigo(codigo);
		if (tipoUnidadeCurricular != null) {
			criterioIntegralizacaoRotulos.setUnidadeCurricular(tipoUnidadeCurricular);
		}
		if (Uteis.isAtributoPreenchido(etiquetas)) {
			criterioIntegralizacaoRotulos.getEtiqueta().addAll(etiquetas);
		}
		TCriterioLimitesCargas criterioLimitesCargas = new TCriterioLimitesCargas();
		criterioLimitesCargas.setCargaHorariaMinima(cargaHorariaMinima);
		criterioLimitesCargas.setCargaHorariaMaxima(cargaHorariaMaxima);
		criterioLimitesCargas.setCargaHorariaParaTotal(cargaHorariaParaTotal);
		criterioIntegralizacaoRotulos.setCargasHorariasCriterio(criterioLimitesCargas);
		return criterioIntegralizacaoRotulos;
	}
	
	private TSegurancaCurriculo preencherDadoSegurancaCurriculo(UnidadeEnsinoVO unidadeEnsino, GradeCurricularVO gradeCurricular) {
		TSegurancaCurriculo segurancaCurriculo = new TSegurancaCurriculo();
		if (Uteis.isAtributoPreenchido(gradeCurricular.getDataCadastro()) && Uteis.isAtributoPreenchido(unidadeEnsino.getCodigoIES())) {
			String hashCodigoValidacao = Constantes.EMPTY;
			String dataCurriculo = Uteis.getData(gradeCurricular.getDataCadastro(), "DDMMYYYY");
			hashCodigoValidacao = gradeCurricular.getCodigo() + Long.valueOf(unidadeEnsino.getCodigoIES()) + Uteis.removerMascara(unidadeEnsino.getCNPJ().trim()) + dataCurriculo;
			hashCodigoValidacao = DigestUtils.sha256Hex(hashCodigoValidacao);
			hashCodigoValidacao = StringUtils.join(Arrays.asList(unidadeEnsino.getCodigoIES(), ".", hashCodigoValidacao).toArray());
			segurancaCurriculo.setCodigoValidacao(hashCodigoValidacao);
		}
		return segurancaCurriculo;
	}
	
	private void preenherDadosUnidadeCurricularEstagioObrigatorio(GradeCurricularEstagioVO gradeCurricularEstagioVO, List<TUnidadeCurricular> unidadeCurriculars, ConsistirException ce) {
		if (Uteis.isAtributoPreenchido(gradeCurricularEstagioVO)) {
			TUnidadeCurricular unidadeCurricular = new TUnidadeCurricular();
			unidadeCurricular.setTipo(TTipoUnidadeCurricular.ESTÁGIO);
			unidadeCurricular.setCodigo(gradeCurricularEstagioVO.getCodigo().toString());
			unidadeCurricular.setNome(getStringXml(gradeCurricularEstagioVO.getNome().trim()));
			unidadeCurricular.setCargaHorariaEmHoraAula(gradeCurricularEstagioVO.getCargaHorarioObrigatorio().longValue());
			unidadeCurricular.setCargaHorariaEmHoraRelogio(getHoraRelogio(null, gradeCurricularEstagioVO.getCargaHorarioObrigatorio().toString()));
			unidadeCurriculars.add(unidadeCurricular);
		}
	}
	
	private HistoricoAlunoRelVO getHistoricoAlunoRelVO(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO) throws Exception {
		FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		filtroRelatorioAcademicoVO.realizarMarcarSituacoesConfiguracaoDiploma(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital());
		String tipoLayoutHistorico = expedicaoDiplomaVO.getLayoutDiploma();
		Boolean apresentarApenasUltimoHistoricoDisciplina = Boolean.FALSE;
		Boolean considerarCargaHorariaCursadaIgualCargaHorariaPrevista = Boolean.FALSE;
		ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO = null;
		if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital())) {
			if (!expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().isNivelMontarDadosTodos()) {
				expedicaoDiplomaVO.setConfiguracaoDiplomaDigital(getAplicacaoControle().getConfiguracaoDiplomaDigitalVO(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getCodigo(), usuarioVO));
			}
			if (expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacionalGraduacao() && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getTipoLayoutHistoricoGraduacao())) {
				String tipoLayoutHistoricoGraduacao = expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getTipoLayoutHistoricoGraduacao();
				if (expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().isTipoLayoutGraduacaoPersonalizado()) {
					tipoLayoutHistorico = Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getConfiguracaoLayoutHistoricoGraduacao()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getConfiguracaoLayoutHistoricoGraduacao().getChave()) ? expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getConfiguracaoLayoutHistoricoGraduacao().getChave() : Constantes.EMPTY;
					configuracaoLayoutHistoricoVO = expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getConfiguracaoLayoutHistoricoGraduacao();
				} else {
					tipoLayoutHistorico = tipoLayoutHistoricoGraduacao;
				}
			} else if (expedicaoDiplomaVO.getMatricula().getCurso().getNivelEducacionalGraduacaoTecnologica() && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getTipoLayoutHistoricoGraduacaoTecnologica())) {
				String tipoLayoutHistoricoGraduacaoTecnologica = expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getTipoLayoutHistoricoGraduacaoTecnologica();
				if (expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().isTipoLayoutGraduacaoTecnologicaPersonalizado()) {
					tipoLayoutHistorico = Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getConfiguracaoLayoutHistoricoGraduacaoTecnologica()) && Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getConfiguracaoLayoutHistoricoGraduacaoTecnologica().getChave()) ? expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getConfiguracaoLayoutHistoricoGraduacaoTecnologica().getChave() : Constantes.EMPTY;
					configuracaoLayoutHistoricoVO = expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getConfiguracaoLayoutHistoricoGraduacaoTecnologica();
				} else {
					tipoLayoutHistorico = tipoLayoutHistoricoGraduacaoTecnologica;
				}
			}
			apresentarApenasUltimoHistoricoDisciplina = expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getApresentarApenasUltimoHistoricoDisciplina();
			considerarCargaHorariaCursadaIgualCargaHorariaPrevista = expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getConsiderarCargaHorariaCursadaIgualCargaHorariaPrevista();
		}
		HistoricoAlunoRelVO histAlunoRelVO = new HistoricoAlunoRelVO();
		histAlunoRelVO = getFacadeFactory().getHistoricoAlunoRelFacade().criarObjeto(
				histAlunoRelVO,
				expedicaoDiplomaVO.getMatricula(),
				expedicaoDiplomaVO.getGradeCurricularVO(),
				filtroRelatorioAcademicoVO,
				1, Constantes.EMPTY,
				Constantes.EMPTY,
				Constantes.EMPTY,
				tipoLayoutHistorico,
				Boolean.FALSE,
				expedicaoDiplomaVO.getDataExpedicao(),
				Boolean.FALSE,
				Boolean.FALSE,
				Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getConfiguracaoDiplomaDigital()) ? expedicaoDiplomaVO.getConfiguracaoDiplomaDigital().getHistoricoConsiderarForaGrade() : Boolean.TRUE,
				usuarioVO,
				Boolean.FALSE,
				Boolean.FALSE,
				Boolean.FALSE,
				Constantes.EMPTY,
				apresentarApenasUltimoHistoricoDisciplina,
				considerarCargaHorariaCursadaIgualCargaHorariaPrevista,
				Boolean.FALSE,
				"PROFESSOR_APROVEITAMENTO_DISCIPLINA",
				null,
				configuracaoLayoutHistoricoVO,
				Boolean.TRUE);
		return histAlunoRelVO;
	}
}
