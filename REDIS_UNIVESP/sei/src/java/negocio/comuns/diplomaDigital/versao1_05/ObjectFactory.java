//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the negocio.comuns.diplomaDigital.versao1_05 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DocumentoHistoricoEscolarSegundaViaNatoFisico_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "DocumentoHistoricoEscolarSegundaViaNatoFisico");
    private final static QName _SPKIData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SPKIData");
    private final static QName _DocumentoHistoricoEscolarParcial_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "DocumentoHistoricoEscolarParcial");
    private final static QName _KeyInfo_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfo");
    private final static QName _SignatureValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureValue");
    private final static QName _KeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyValue");
    private final static QName _Transforms_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Transforms");
    private final static QName _DigestMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DigestMethod");
    private final static QName _X509Data_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509Data");
    private final static QName _SignatureProperty_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperty");
    private final static QName _KeyName_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyName");
    private final static QName _RSAKeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "RSAKeyValue");
    private final static QName _Signature_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Signature");
    private final static QName _MgmtData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "MgmtData");
    private final static QName _SignatureMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureMethod");
    private final static QName _Object_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Object");
    private final static QName _SignatureProperties_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperties");
    private final static QName _DocumentoHistoricoEscolarFinal_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "DocumentoHistoricoEscolarFinal");
    private final static QName _Transform_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Transform");
    private final static QName _PGPData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPData");
    private final static QName _Reference_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Reference");
    private final static QName _RetrievalMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod");
    private final static QName _DSAKeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValue");
    private final static QName _DigestValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DigestValue");
    private final static QName _CanonicalizationMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethod");
    private final static QName _SignedInfo_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfo");
    private final static QName _Manifest_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Manifest");
    private final static QName _SignatureMethodTypeHMACOutputLength_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "HMACOutputLength");
    private final static QName _X509DataTypeX509IssuerSerial_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial");
    private final static QName _X509DataTypeX509CRL_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509CRL");
    private final static QName _X509DataTypeX509SubjectName_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName");
    private final static QName _X509DataTypeX509SKI_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509SKI");
    private final static QName _X509DataTypeX509Certificate_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509Certificate");
    private final static QName _TLivroRegistroNSFNumeroRegistro_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "NumeroRegistro");
    private final static QName _TLivroRegistroNSFResponsavelRegistro_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "ResponsavelRegistro");
    private final static QName _TLivroRegistroNSFDataColacaoGrau_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "DataColacaoGrau");
    private final static QName _TLivroRegistroNSFDataRegistroDiploma_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "DataRegistroDiploma");
    private final static QName _TLivroRegistroNSFProcessoDoDiploma_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "ProcessoDoDiploma");
    private final static QName _TLivroRegistroNSFLivroRegistro_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "LivroRegistro");
    private final static QName _TLivroRegistroNSFNumeroSequenciaDoDiploma_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "NumeroSequenciaDoDiploma");
    private final static QName _TLivroRegistroNSFNumeroFolhaDoDiploma_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "NumeroFolhaDoDiploma");
    private final static QName _TLivroRegistroNSFDataExpedicaoDiploma_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "DataExpedicaoDiploma");
    private final static QName _TransformTypeXPath_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "XPath");
    private final static QName _SPKIDataTypeSPKISexp_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SPKISexp");
    private final static QName _PGPDataTypePGPKeyID_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPKeyID");
    private final static QName _PGPDataTypePGPKeyPacket_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPKeyPacket");
    private final static QName _TEnadeHabilitado_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "Habilitado");
    private final static QName _TEnadeNaoHabilitado_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "NaoHabilitado");
    private final static QName _TEnadeIrregular_QNAME = new QName("http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", "Irregular");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: negocio.comuns.diplomaDigital.versao1_05
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TLivroRegistro }
     * 
     */
    public TLivroRegistro createTLivroRegistro() {
        return new TLivroRegistro();
    }

    /**
     * Create an instance of {@link THistoricoEscolarSegundaVia }
     * 
     */
    public THistoricoEscolarSegundaVia createTHistoricoEscolarSegundaVia() {
        return new THistoricoEscolarSegundaVia();
    }

    /**
     * Create an instance of {@link THistoricoEscolar }
     * 
     */
    public THistoricoEscolar createTHistoricoEscolar() {
        return new THistoricoEscolar();
    }

    /**
     * Create an instance of {@link TLivroRegistroNSF }
     * 
     */
    public TLivroRegistroNSF createTLivroRegistroNSF() {
        return new TLivroRegistroNSF();
    }

    /**
     * Create an instance of {@link TInfoAssinantes }
     * 
     */
    public TInfoAssinantes createTInfoAssinantes() {
        return new TInfoAssinantes();
    }

    /**
     * Create an instance of {@link TDadosIesRegistradora }
     * 
     */
    public TDadosIesRegistradora createTDadosIesRegistradora() {
        return new TDadosIesRegistradora();
    }

    /**
     * Create an instance of {@link TDadosIesEmissora }
     * 
     */
    public TDadosIesEmissora createTDadosIesEmissora() {
        return new TDadosIesEmissora();
    }

    /**
     * Create an instance of {@link TDocumentoHistoricoEscolarDigital }
     * 
     */
    public TDocumentoHistoricoEscolarDigital createTDocumentoHistoricoEscolarDigital() {
        return new TDocumentoHistoricoEscolarDigital();
    }

    /**
     * Create an instance of {@link TDocumentoHistoricoEscolarSegundaViaNatoFisico }
     * 
     */
    public TDocumentoHistoricoEscolarSegundaViaNatoFisico createTDocumentoHistoricoEscolarSegundaViaNatoFisico() {
        return new TDocumentoHistoricoEscolarSegundaViaNatoFisico();
    }

    /**
     * Create an instance of {@link TDadosRegistroNSF }
     * 
     */
    public TDadosRegistroNSF createTDadosRegistroNSF() {
        return new TDadosRegistroNSF();
    }

    /**
     * Create an instance of {@link TDadosRegistroPorDecisaoJudicial }
     * 
     */
    public TDadosRegistroPorDecisaoJudicial createTDadosRegistroPorDecisaoJudicial() {
        return new TDadosRegistroPorDecisaoJudicial();
    }

    /**
     * Create an instance of {@link TDocente }
     * 
     */
    public TDocente createTDocente() {
        return new TDocente();
    }

    /**
     * Create an instance of {@link TInfHistoricoEscolarSegundaViaNatoFisico }
     * 
     */
    public TInfHistoricoEscolarSegundaViaNatoFisico createTInfHistoricoEscolarSegundaViaNatoFisico() {
        return new TInfHistoricoEscolarSegundaViaNatoFisico();
    }

    /**
     * Create an instance of {@link TEnadeNaoHabilitado }
     * 
     */
    public TEnadeNaoHabilitado createTEnadeNaoHabilitado() {
        return new TEnadeNaoHabilitado();
    }

    /**
     * Create an instance of {@link TCargaHorariaComEtiqueta }
     * 
     */
    public TCargaHorariaComEtiqueta createTCargaHorariaComEtiqueta() {
        return new TCargaHorariaComEtiqueta();
    }

    /**
     * Create an instance of {@link THabilitacao }
     * 
     */
    public THabilitacao createTHabilitacao() {
        return new THabilitacao();
    }

    /**
     * Create an instance of {@link TElementosHistoricoSegundaViaNatoFisico }
     * 
     */
    public TElementosHistoricoSegundaViaNatoFisico createTElementosHistoricoSegundaViaNatoFisico() {
        return new TElementosHistoricoSegundaViaNatoFisico();
    }

    /**
     * Create an instance of {@link TEnade }
     * 
     */
    public TEnade createTEnade() {
        return new TEnade();
    }

    /**
     * Create an instance of {@link TSituacaoFormado }
     * 
     */
    public TSituacaoFormado createTSituacaoFormado() {
        return new TSituacaoFormado();
    }

    /**
     * Create an instance of {@link TDadosDiplomadoPorDecisaoJudicial }
     * 
     */
    public TDadosDiplomadoPorDecisaoJudicial createTDadosDiplomadoPorDecisaoJudicial() {
        return new TDadosDiplomadoPorDecisaoJudicial();
    }

    /**
     * Create an instance of {@link TDadosCursoPorDecisaoJudicial }
     * 
     */
    public TDadosCursoPorDecisaoJudicial createTDadosCursoPorDecisaoJudicial() {
        return new TDadosCursoPorDecisaoJudicial();
    }

    /**
     * Create an instance of {@link TEndereco }
     * 
     */
    public TEndereco createTEndereco() {
        return new TEndereco();
    }

    /**
     * Create an instance of {@link TDadosCurso }
     * 
     */
    public TDadosCurso createTDadosCurso() {
        return new TDadosCurso();
    }

    /**
     * Create an instance of {@link TDadosMinimoCurso }
     * 
     */
    public TDadosMinimoCurso createTDadosMinimoCurso() {
        return new TDadosMinimoCurso();
    }

    /**
     * Create an instance of {@link TEntradaHistoricoEstagio }
     * 
     */
    public TEntradaHistoricoEstagio createTEntradaHistoricoEstagio() {
        return new TEntradaHistoricoEstagio();
    }

    /**
     * Create an instance of {@link TDadosIesOriginalCursoPTA }
     * 
     */
    public TDadosIesOriginalCursoPTA createTDadosIesOriginalCursoPTA() {
        return new TDadosIesOriginalCursoPTA();
    }

    /**
     * Create an instance of {@link TFiliacao }
     * 
     */
    public TFiliacao createTFiliacao() {
        return new TFiliacao();
    }

    /**
     * Create an instance of {@link TEntradaHistoricoDisciplina }
     * 
     */
    public TEntradaHistoricoDisciplina createTEntradaHistoricoDisciplina() {
        return new TEntradaHistoricoDisciplina();
    }

    /**
     * Create an instance of {@link TEntradaHistoricoDisciplinaSegundaViaNatoFisica }
     * 
     */
    public TEntradaHistoricoDisciplinaSegundaViaNatoFisica createTEntradaHistoricoDisciplinaSegundaViaNatoFisica() {
        return new TEntradaHistoricoDisciplinaSegundaViaNatoFisica();
    }

    /**
     * Create an instance of {@link TInfDiploma }
     * 
     */
    public TInfDiploma createTInfDiploma() {
        return new TInfDiploma();
    }

    /**
     * Create an instance of {@link TDadosMinimoCursoNSF }
     * 
     */
    public TDadosMinimoCursoNSF createTDadosMinimoCursoNSF() {
        return new TDadosMinimoCursoNSF();
    }

    /**
     * Create an instance of {@link TDadosDiplomaNSF }
     * 
     */
    public TDadosDiplomaNSF createTDadosDiplomaNSF() {
        return new TDadosDiplomaNSF();
    }

    /**
     * Create an instance of {@link TAtoRegulatorio }
     * 
     */
    public TAtoRegulatorio createTAtoRegulatorio() {
        return new TAtoRegulatorio();
    }

    /**
     * Create an instance of {@link TDadosDiplomado }
     * 
     */
    public TDadosDiplomado createTDadosDiplomado() {
        return new TDadosDiplomado();
    }

    /**
     * Create an instance of {@link TNaturalidade }
     * 
     */
    public TNaturalidade createTNaturalidade() {
        return new TNaturalidade();
    }

    /**
     * Create an instance of {@link TDadosRegistro }
     * 
     */
    public TDadosRegistro createTDadosRegistro() {
        return new TDadosRegistro();
    }

    /**
     * Create an instance of {@link TEntradaHistoricoEstagioSegundaViaNatoFisica }
     * 
     */
    public TEntradaHistoricoEstagioSegundaViaNatoFisica createTEntradaHistoricoEstagioSegundaViaNatoFisica() {
        return new TEntradaHistoricoEstagioSegundaViaNatoFisica();
    }

    /**
     * Create an instance of {@link TTituloConferido }
     * 
     */
    public TTituloConferido createTTituloConferido() {
        return new TTituloConferido();
    }

    /**
     * Create an instance of {@link TConcedenteEstagio }
     * 
     */
    public TConcedenteEstagio createTConcedenteEstagio() {
        return new TConcedenteEstagio();
    }

    /**
     * Create an instance of {@link TInformacoesTramitacaoEMEC }
     * 
     */
    public TInformacoesTramitacaoEMEC createTInformacoesTramitacaoEMEC() {
        return new TInformacoesTramitacaoEMEC();
    }

    /**
     * Create an instance of {@link TAreasComNome }
     * 
     */
    public TAreasComNome createTAreasComNome() {
        return new TAreasComNome();
    }

    /**
     * Create an instance of {@link TSegurancaHistorico }
     * 
     */
    public TSegurancaHistorico createTSegurancaHistorico() {
        return new TSegurancaHistorico();
    }

    /**
     * Create an instance of {@link TSituacaoIntercambio }
     * 
     */
    public TSituacaoIntercambio createTSituacaoIntercambio() {
        return new TSituacaoIntercambio();
    }

    /**
     * Create an instance of {@link THoraRelogioComEtiqueta }
     * 
     */
    public THoraRelogioComEtiqueta createTHoraRelogioComEtiqueta() {
        return new THoraRelogioComEtiqueta();
    }

    /**
     * Create an instance of {@link TDadosDiploma }
     * 
     */
    public TDadosDiploma createTDadosDiploma() {
        return new TDadosDiploma();
    }

    /**
     * Create an instance of {@link TInformacoesProcessoJudicial }
     * 
     */
    public TInformacoesProcessoJudicial createTInformacoesProcessoJudicial() {
        return new TInformacoesProcessoJudicial();
    }

    /**
     * Create an instance of {@link TRg }
     * 
     */
    public TRg createTRg() {
        return new TRg();
    }

    /**
     * Create an instance of {@link TDadosDiplomaPorDecisaoJudicial }
     * 
     */
    public TDadosDiplomaPorDecisaoJudicial createTDadosDiplomaPorDecisaoJudicial() {
        return new TDadosDiplomaPorDecisaoJudicial();
    }

    /**
     * Create an instance of {@link TEntradaHistoricoAtividadeComplementar }
     * 
     */
    public TEntradaHistoricoAtividadeComplementar createTEntradaHistoricoAtividadeComplementar() {
        return new TEntradaHistoricoAtividadeComplementar();
    }

    /**
     * Create an instance of {@link TEntradaHistoricoAtividadeComplementarSegundaViaNatoFisica }
     * 
     */
    public TEntradaHistoricoAtividadeComplementarSegundaViaNatoFisica createTEntradaHistoricoAtividadeComplementarSegundaViaNatoFisica() {
        return new TEntradaHistoricoAtividadeComplementarSegundaViaNatoFisica();
    }

    /**
     * Create an instance of {@link TEntradaHistoricoSituacaoDiscentePeriodoLetivo }
     * 
     */
    public TEntradaHistoricoSituacaoDiscentePeriodoLetivo createTEntradaHistoricoSituacaoDiscentePeriodoLetivo() {
        return new TEntradaHistoricoSituacaoDiscentePeriodoLetivo();
    }

    /**
     * Create an instance of {@link TAreaComNome }
     * 
     */
    public TAreaComNome createTAreaComNome() {
        return new TAreaComNome();
    }

    /**
     * Create an instance of {@link TPessoa }
     * 
     */
    public TPessoa createTPessoa() {
        return new TPessoa();
    }

    /**
     * Create an instance of {@link TSeguranca }
     * 
     */
    public TSeguranca createTSeguranca() {
        return new TSeguranca();
    }

    /**
     * Create an instance of {@link TDeclaracoesAcercaProcesso }
     * 
     */
    public TDeclaracoesAcercaProcesso createTDeclaracoesAcercaProcesso() {
        return new TDeclaracoesAcercaProcesso();
    }

    /**
     * Create an instance of {@link TDadosCursoNSF }
     * 
     */
    public TDadosCursoNSF createTDadosCursoNSF() {
        return new TDadosCursoNSF();
    }

    /**
     * Create an instance of {@link TInfHistoricoEscolar }
     * 
     */
    public TInfHistoricoEscolar createTInfHistoricoEscolar() {
        return new TInfHistoricoEscolar();
    }

    /**
     * Create an instance of {@link TDocentes }
     * 
     */
    public TDocentes createTDocentes() {
        return new TDocentes();
    }

    /**
     * Create an instance of {@link TVazio }
     * 
     */
    public TVazio createTVazio() {
        return new TVazio();
    }

    /**
     * Create an instance of {@link TCargaHoraria }
     * 
     */
    public TCargaHoraria createTCargaHoraria() {
        return new TCargaHoraria();
    }

    /**
     * Create an instance of {@link TDisciplinaAprovada }
     * 
     */
    public TDisciplinaAprovada createTDisciplinaAprovada() {
        return new TDisciplinaAprovada();
    }

    /**
     * Create an instance of {@link TSituacaoAtualDiscente }
     * 
     */
    public TSituacaoAtualDiscente createTSituacaoAtualDiscente() {
        return new TSituacaoAtualDiscente();
    }

    /**
     * Create an instance of {@link TPolo }
     * 
     */
    public TPolo createTPolo() {
        return new TPolo();
    }

    /**
     * Create an instance of {@link TAtoRegulatorioComOuSemEMEC }
     * 
     */
    public TAtoRegulatorioComOuSemEMEC createTAtoRegulatorioComOuSemEMEC() {
        return new TAtoRegulatorioComOuSemEMEC();
    }

    /**
     * Create an instance of {@link TDiploma }
     * 
     */
    public TDiploma createTDiploma() {
        return new TDiploma();
    }

    /**
     * Create an instance of {@link TOutroDocumentoIdentificacao }
     * 
     */
    public TOutroDocumentoIdentificacao createTOutroDocumentoIdentificacao() {
        return new TOutroDocumentoIdentificacao();
    }

    /**
     * Create an instance of {@link TInformacoesEnade }
     * 
     */
    public TInformacoesEnade createTInformacoesEnade() {
        return new TInformacoesEnade();
    }

    /**
     * Create an instance of {@link TElementosHistorico }
     * 
     */
    public TElementosHistorico createTElementosHistorico() {
        return new TElementosHistorico();
    }

    /**
     * Create an instance of {@link PGPDataType }
     * 
     */
    public PGPDataType createPGPDataType() {
        return new PGPDataType();
    }

    /**
     * Create an instance of {@link KeyValueType }
     * 
     */
    public KeyValueType createKeyValueType() {
        return new KeyValueType();
    }

    /**
     * Create an instance of {@link DSAKeyValueType }
     * 
     */
    public DSAKeyValueType createDSAKeyValueType() {
        return new DSAKeyValueType();
    }

    /**
     * Create an instance of {@link ReferenceType }
     * 
     */
    public ReferenceType createReferenceType() {
        return new ReferenceType();
    }

    /**
     * Create an instance of {@link RetrievalMethodType }
     * 
     */
    public RetrievalMethodType createRetrievalMethodType() {
        return new RetrievalMethodType();
    }

    /**
     * Create an instance of {@link TransformsType }
     * 
     */
    public TransformsType createTransformsType() {
        return new TransformsType();
    }

    /**
     * Create an instance of {@link CanonicalizationMethodType }
     * 
     */
    public CanonicalizationMethodType createCanonicalizationMethodType() {
        return new CanonicalizationMethodType();
    }

    /**
     * Create an instance of {@link DigestMethodType }
     * 
     */
    public DigestMethodType createDigestMethodType() {
        return new DigestMethodType();
    }

    /**
     * Create an instance of {@link ManifestType }
     * 
     */
    public ManifestType createManifestType() {
        return new ManifestType();
    }

    /**
     * Create an instance of {@link SignaturePropertyType }
     * 
     */
    public SignaturePropertyType createSignaturePropertyType() {
        return new SignaturePropertyType();
    }

    /**
     * Create an instance of {@link X509DataType }
     * 
     */
    public X509DataType createX509DataType() {
        return new X509DataType();
    }

    /**
     * Create an instance of {@link SignedInfoType }
     * 
     */
    public SignedInfoType createSignedInfoType() {
        return new SignedInfoType();
    }

    /**
     * Create an instance of {@link RSAKeyValueType }
     * 
     */
    public RSAKeyValueType createRSAKeyValueType() {
        return new RSAKeyValueType();
    }

    /**
     * Create an instance of {@link SPKIDataType }
     * 
     */
    public SPKIDataType createSPKIDataType() {
        return new SPKIDataType();
    }

    /**
     * Create an instance of {@link SignatureValueType }
     * 
     */
    public SignatureValueType createSignatureValueType() {
        return new SignatureValueType();
    }

    /**
     * Create an instance of {@link KeyInfoType }
     * 
     */
    public KeyInfoType createKeyInfoType() {
        return new KeyInfoType();
    }

    /**
     * Create an instance of {@link SignatureType }
     * 
     */
    public SignatureType createSignatureType() {
        return new SignatureType();
    }

    /**
     * Create an instance of {@link SignaturePropertiesType }
     * 
     */
    public SignaturePropertiesType createSignaturePropertiesType() {
        return new SignaturePropertiesType();
    }

    /**
     * Create an instance of {@link SignatureMethodType }
     * 
     */
    public SignatureMethodType createSignatureMethodType() {
        return new SignatureMethodType();
    }

    /**
     * Create an instance of {@link ObjectType }
     * 
     */
    public ObjectType createObjectType() {
        return new ObjectType();
    }

    /**
     * Create an instance of {@link TransformType }
     * 
     */
    public TransformType createTransformType() {
        return new TransformType();
    }

    /**
     * Create an instance of {@link X509IssuerSerialType }
     * 
     */
    public X509IssuerSerialType createX509IssuerSerialType() {
        return new X509IssuerSerialType();
    }

    /**
     * Create an instance of {@link TLivroRegistro.ResponsavelRegistro }
     * 
     */
    public TLivroRegistro.ResponsavelRegistro createTLivroRegistroResponsavelRegistro() {
        return new TLivroRegistro.ResponsavelRegistro();
    }

    /**
     * Create an instance of {@link THistoricoEscolarSegundaVia.IngressoCurso }
     * 
     */
    public THistoricoEscolarSegundaVia.IngressoCurso createTHistoricoEscolarSegundaViaIngressoCurso() {
        return new THistoricoEscolarSegundaVia.IngressoCurso();
    }

    /**
     * Create an instance of {@link THistoricoEscolar.IngressoCurso }
     * 
     */
    public THistoricoEscolar.IngressoCurso createTHistoricoEscolarIngressoCurso() {
        return new THistoricoEscolar.IngressoCurso();
    }

    /**
     * Create an instance of {@link TLivroRegistroNSF.ResponsavelRegistro }
     * 
     */
    public TLivroRegistroNSF.ResponsavelRegistro createTLivroRegistroNSFResponsavelRegistro() {
        return new TLivroRegistroNSF.ResponsavelRegistro();
    }

    /**
     * Create an instance of {@link TInfoAssinantes.Assinante }
     * 
     */
    public TInfoAssinantes.Assinante createTInfoAssinantesAssinante() {
        return new TInfoAssinantes.Assinante();
    }

    /**
     * Create an instance of {@link TDadosIesRegistradora.Mantenedora }
     * 
     */
    public TDadosIesRegistradora.Mantenedora createTDadosIesRegistradoraMantenedora() {
        return new TDadosIesRegistradora.Mantenedora();
    }

    /**
     * Create an instance of {@link TDadosIesEmissora.Mantenedora }
     * 
     */
    public TDadosIesEmissora.Mantenedora createTDadosIesEmissoraMantenedora() {
        return new TDadosIesEmissora.Mantenedora();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TDocumentoHistoricoEscolarSegundaViaNatoFisico }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "DocumentoHistoricoEscolarSegundaViaNatoFisico")
    public JAXBElement<TDocumentoHistoricoEscolarSegundaViaNatoFisico> createDocumentoHistoricoEscolarSegundaViaNatoFisico(TDocumentoHistoricoEscolarSegundaViaNatoFisico value) {
        return new JAXBElement<TDocumentoHistoricoEscolarSegundaViaNatoFisico>(_DocumentoHistoricoEscolarSegundaViaNatoFisico_QNAME, TDocumentoHistoricoEscolarSegundaViaNatoFisico.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SPKIDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SPKIData")
    public JAXBElement<SPKIDataType> createSPKIData(SPKIDataType value) {
        return new JAXBElement<SPKIDataType>(_SPKIData_QNAME, SPKIDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TDocumentoHistoricoEscolarDigital }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "DocumentoHistoricoEscolarParcial")
    public JAXBElement<TDocumentoHistoricoEscolarDigital> createDocumentoHistoricoEscolarParcial(TDocumentoHistoricoEscolarDigital value) {
        return new JAXBElement<TDocumentoHistoricoEscolarDigital>(_DocumentoHistoricoEscolarParcial_QNAME, TDocumentoHistoricoEscolarDigital.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "KeyInfo")
    public JAXBElement<KeyInfoType> createKeyInfo(KeyInfoType value) {
        return new JAXBElement<KeyInfoType>(_KeyInfo_QNAME, KeyInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureValueType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureValue")
    public JAXBElement<SignatureValueType> createSignatureValue(SignatureValueType value) {
        return new JAXBElement<SignatureValueType>(_SignatureValue_QNAME, SignatureValueType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "KeyValue")
    public JAXBElement<KeyValueType> createKeyValue(KeyValueType value) {
        return new JAXBElement<KeyValueType>(_KeyValue_QNAME, KeyValueType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransformsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Transforms")
    public JAXBElement<TransformsType> createTransforms(TransformsType value) {
        return new JAXBElement<TransformsType>(_Transforms_QNAME, TransformsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DigestMethodType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "DigestMethod")
    public JAXBElement<DigestMethodType> createDigestMethod(DigestMethodType value) {
        return new JAXBElement<DigestMethodType>(_DigestMethod_QNAME, DigestMethodType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link X509DataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509Data")
    public JAXBElement<X509DataType> createX509Data(X509DataType value) {
        return new JAXBElement<X509DataType>(_X509Data_QNAME, X509DataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignaturePropertyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureProperty")
    public JAXBElement<SignaturePropertyType> createSignatureProperty(SignaturePropertyType value) {
        return new JAXBElement<SignaturePropertyType>(_SignatureProperty_QNAME, SignaturePropertyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "KeyName")
    public JAXBElement<String> createKeyName(String value) {
        return new JAXBElement<String>(_KeyName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RSAKeyValueType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "RSAKeyValue")
    public JAXBElement<RSAKeyValueType> createRSAKeyValue(RSAKeyValueType value) {
        return new JAXBElement<RSAKeyValueType>(_RSAKeyValue_QNAME, RSAKeyValueType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Signature")
    public JAXBElement<SignatureType> createSignature(SignatureType value) {
        return new JAXBElement<SignatureType>(_Signature_QNAME, SignatureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "MgmtData")
    public JAXBElement<String> createMgmtData(String value) {
        return new JAXBElement<String>(_MgmtData_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureMethodType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureMethod")
    public JAXBElement<SignatureMethodType> createSignatureMethod(SignatureMethodType value) {
        return new JAXBElement<SignatureMethodType>(_SignatureMethod_QNAME, SignatureMethodType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Object")
    public JAXBElement<ObjectType> createObject(ObjectType value) {
        return new JAXBElement<ObjectType>(_Object_QNAME, ObjectType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignaturePropertiesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureProperties")
    public JAXBElement<SignaturePropertiesType> createSignatureProperties(SignaturePropertiesType value) {
        return new JAXBElement<SignaturePropertiesType>(_SignatureProperties_QNAME, SignaturePropertiesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TDocumentoHistoricoEscolarDigital }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "DocumentoHistoricoEscolarFinal")
    public JAXBElement<TDocumentoHistoricoEscolarDigital> createDocumentoHistoricoEscolarFinal(TDocumentoHistoricoEscolarDigital value) {
        return new JAXBElement<TDocumentoHistoricoEscolarDigital>(_DocumentoHistoricoEscolarFinal_QNAME, TDocumentoHistoricoEscolarDigital.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransformType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Transform")
    public JAXBElement<TransformType> createTransform(TransformType value) {
        return new JAXBElement<TransformType>(_Transform_QNAME, TransformType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PGPDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "PGPData")
    public JAXBElement<PGPDataType> createPGPData(PGPDataType value) {
        return new JAXBElement<PGPDataType>(_PGPData_QNAME, PGPDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Reference")
    public JAXBElement<ReferenceType> createReference(ReferenceType value) {
        return new JAXBElement<ReferenceType>(_Reference_QNAME, ReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrievalMethodType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "RetrievalMethod")
    public JAXBElement<RetrievalMethodType> createRetrievalMethod(RetrievalMethodType value) {
        return new JAXBElement<RetrievalMethodType>(_RetrievalMethod_QNAME, RetrievalMethodType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DSAKeyValueType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "DSAKeyValue")
    public JAXBElement<DSAKeyValueType> createDSAKeyValue(DSAKeyValueType value) {
        return new JAXBElement<DSAKeyValueType>(_DSAKeyValue_QNAME, DSAKeyValueType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "DigestValue")
    public JAXBElement<byte[]> createDigestValue(byte[] value) {
        return new JAXBElement<byte[]>(_DigestValue_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CanonicalizationMethodType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "CanonicalizationMethod")
    public JAXBElement<CanonicalizationMethodType> createCanonicalizationMethod(CanonicalizationMethodType value) {
        return new JAXBElement<CanonicalizationMethodType>(_CanonicalizationMethod_QNAME, CanonicalizationMethodType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignedInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignedInfo")
    public JAXBElement<SignedInfoType> createSignedInfo(SignedInfoType value) {
        return new JAXBElement<SignedInfoType>(_SignedInfo_QNAME, SignedInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManifestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Manifest")
    public JAXBElement<ManifestType> createManifest(ManifestType value) {
        return new JAXBElement<ManifestType>(_Manifest_QNAME, ManifestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "HMACOutputLength", scope = SignatureMethodType.class)
    public JAXBElement<BigInteger> createSignatureMethodTypeHMACOutputLength(BigInteger value) {
        return new JAXBElement<BigInteger>(_SignatureMethodTypeHMACOutputLength_QNAME, BigInteger.class, SignatureMethodType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link X509IssuerSerialType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509IssuerSerial", scope = X509DataType.class)
    public JAXBElement<X509IssuerSerialType> createX509DataTypeX509IssuerSerial(X509IssuerSerialType value) {
        return new JAXBElement<X509IssuerSerialType>(_X509DataTypeX509IssuerSerial_QNAME, X509IssuerSerialType.class, X509DataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509CRL", scope = X509DataType.class)
    public JAXBElement<byte[]> createX509DataTypeX509CRL(byte[] value) {
        return new JAXBElement<byte[]>(_X509DataTypeX509CRL_QNAME, byte[].class, X509DataType.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509SubjectName", scope = X509DataType.class)
    public JAXBElement<String> createX509DataTypeX509SubjectName(String value) {
        return new JAXBElement<String>(_X509DataTypeX509SubjectName_QNAME, String.class, X509DataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509SKI", scope = X509DataType.class)
    public JAXBElement<byte[]> createX509DataTypeX509SKI(byte[] value) {
        return new JAXBElement<byte[]>(_X509DataTypeX509SKI_QNAME, byte[].class, X509DataType.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509Certificate", scope = X509DataType.class)
    public JAXBElement<byte[]> createX509DataTypeX509Certificate(byte[] value) {
        return new JAXBElement<byte[]>(_X509DataTypeX509Certificate_QNAME, byte[].class, X509DataType.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "NumeroRegistro", scope = TLivroRegistroNSF.class)
    public JAXBElement<String> createTLivroRegistroNSFNumeroRegistro(String value) {
        return new JAXBElement<String>(_TLivroRegistroNSFNumeroRegistro_QNAME, String.class, TLivroRegistroNSF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TLivroRegistroNSF.ResponsavelRegistro }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "ResponsavelRegistro", scope = TLivroRegistroNSF.class)
    public JAXBElement<TLivroRegistroNSF.ResponsavelRegistro> createTLivroRegistroNSFResponsavelRegistro(TLivroRegistroNSF.ResponsavelRegistro value) {
        return new JAXBElement<TLivroRegistroNSF.ResponsavelRegistro>(_TLivroRegistroNSFResponsavelRegistro_QNAME, TLivroRegistroNSF.ResponsavelRegistro.class, TLivroRegistroNSF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlSchemaType(name = "date")
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "DataColacaoGrau", scope = TLivroRegistroNSF.class)
    public JAXBElement<XMLGregorianCalendar> createTLivroRegistroNSFDataColacaoGrau(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_TLivroRegistroNSFDataColacaoGrau_QNAME, XMLGregorianCalendar.class, TLivroRegistroNSF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlSchemaType(name = "date")
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "DataRegistroDiploma", scope = TLivroRegistroNSF.class)
    public JAXBElement<XMLGregorianCalendar> createTLivroRegistroNSFDataRegistroDiploma(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_TLivroRegistroNSFDataRegistroDiploma_QNAME, XMLGregorianCalendar.class, TLivroRegistroNSF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "ProcessoDoDiploma", scope = TLivroRegistroNSF.class)
    public JAXBElement<String> createTLivroRegistroNSFProcessoDoDiploma(String value) {
        return new JAXBElement<String>(_TLivroRegistroNSFProcessoDoDiploma_QNAME, String.class, TLivroRegistroNSF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "LivroRegistro", scope = TLivroRegistroNSF.class)
    public JAXBElement<String> createTLivroRegistroNSFLivroRegistro(String value) {
        return new JAXBElement<String>(_TLivroRegistroNSFLivroRegistro_QNAME, String.class, TLivroRegistroNSF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "NumeroSequenciaDoDiploma", scope = TLivroRegistroNSF.class)
    public JAXBElement<String> createTLivroRegistroNSFNumeroSequenciaDoDiploma(String value) {
        return new JAXBElement<String>(_TLivroRegistroNSFNumeroSequenciaDoDiploma_QNAME, String.class, TLivroRegistroNSF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "NumeroFolhaDoDiploma", scope = TLivroRegistroNSF.class)
    public JAXBElement<String> createTLivroRegistroNSFNumeroFolhaDoDiploma(String value) {
        return new JAXBElement<String>(_TLivroRegistroNSFNumeroFolhaDoDiploma_QNAME, String.class, TLivroRegistroNSF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlSchemaType(name = "date")
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "DataExpedicaoDiploma", scope = TLivroRegistroNSF.class)
    public JAXBElement<XMLGregorianCalendar> createTLivroRegistroNSFDataExpedicaoDiploma(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_TLivroRegistroNSFDataExpedicaoDiploma_QNAME, XMLGregorianCalendar.class, TLivroRegistroNSF.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "XPath", scope = TransformType.class)
    public JAXBElement<String> createTransformTypeXPath(String value) {
        return new JAXBElement<String>(_TransformTypeXPath_QNAME, String.class, TransformType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SPKISexp", scope = SPKIDataType.class)
    public JAXBElement<byte[]> createSPKIDataTypeSPKISexp(byte[] value) {
        return new JAXBElement<byte[]>(_SPKIDataTypeSPKISexp_QNAME, byte[].class, SPKIDataType.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "PGPKeyID", scope = PGPDataType.class)
    public JAXBElement<byte[]> createPGPDataTypePGPKeyID(byte[] value) {
        return new JAXBElement<byte[]>(_PGPDataTypePGPKeyID_QNAME, byte[].class, PGPDataType.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "PGPKeyPacket", scope = PGPDataType.class)
    public JAXBElement<byte[]> createPGPDataTypePGPKeyPacket(byte[] value) {
        return new JAXBElement<byte[]>(_PGPDataTypePGPKeyPacket_QNAME, byte[].class, PGPDataType.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TInformacoesEnade }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "Habilitado", scope = TEnade.class)
    public JAXBElement<TInformacoesEnade> createTEnadeHabilitado(TInformacoesEnade value) {
        return new JAXBElement<TInformacoesEnade>(_TEnadeHabilitado_QNAME, TInformacoesEnade.class, TEnade.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEnadeNaoHabilitado }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "NaoHabilitado", scope = TEnade.class)
    public JAXBElement<TEnadeNaoHabilitado> createTEnadeNaoHabilitado(TEnadeNaoHabilitado value) {
        return new JAXBElement<TEnadeNaoHabilitado>(_TEnadeNaoHabilitado_QNAME, TEnadeNaoHabilitado.class, TEnade.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TInformacoesEnade }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", name = "Irregular", scope = TEnade.class)
    public JAXBElement<TInformacoesEnade> createTEnadeIrregular(TInformacoesEnade value) {
        return new JAXBElement<TInformacoesEnade>(_TEnadeIrregular_QNAME, TInformacoesEnade.class, TEnade.class, value);
    }

}
