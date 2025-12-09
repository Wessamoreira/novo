package negocio.comuns.arquitetura.faturamento.nfe;

/**
 *
 * @author Euripedes
 */
public class DadosEnvio {

    public static final String GOIAS_CODIGOESTADO = "52";

    //HOMOLOGACAO GOIÁS
    public static final String GOIAS_HOMOLOGACAO_STATUS_SERVICO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2";
    public static final String GOIAS_HOMOLOGACAO_STATUS_SERVICO_URL = "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeStatusServico2";
    public static final String GOIAS_HOMOLOGACAO_STATUS_SERVICO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2/nfeStatusServicoNF2";

    public static final String GOIAS_HOMOLOGACAO_ENVIAR_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao";
    public static final String GOIAS_HOMOLOGACAO_ENVIAR_URL = "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeAutorizacao";
    public static final String GOIAS_HOMOLOGACAO_ENVIAR_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao/nfeAutorizacaoLote";

    public static final String GOIAS_HOMOLOGACAO_CONSULTA_LOTE_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao";
    public static final String GOIAS_HOMOLOGACAO_CONSULTA_LOTE_URL = "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeRetAutorizacao";
    public static final String GOIAS_HOMOLOGACAO_CONSULTA_LOTE_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao/nfeRetAutorizacaoLote";

    public static final String GOIAS_HOMOLOGACAO_CONSULTA_NOTA_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2";
    public static final String GOIAS_HOMOLOGACAO_CONSULTA_NOTA_URL = "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeConsulta2";
    public static final String GOIAS_HOMOLOGACAO_CONSULTA_NOTA_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2/nfeConsultaNF2";
    
    public static final String GOIAS_HOMOLOGACAO_CANCELAMENTO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento";
    public static final String GOIAS_HOMOLOGACAO_CANCELAMENTO_URL = "https://nfe.sefaz.go.gov.br/nfe/services/v2/RecepcaoEvento";
    public static final String GOIAS_HOMOLOGACAO_CANCELAMENTO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento/nfeRecepcaoEvento";

    public static final String GOIAS_HOMOLOGACAO_INUTILIZACAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2";
    public static final String GOIAS_HOMOLOGACAO_INUTILIZACAO_URL = "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeInutilizacao2";
    public static final String GOIAS_HOMOLOGACAO_INUTILIZACAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2/nfeInutilizacaoNF2";
    
    public static final String GOIAS_HOMOLOGACAO_CARTA_CORRECAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRecepcaoEvento";
    public static final String GOIAS_HOMOLOGACAO_CARTA_CORRECAO_URL = "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeRecepcaoEvento";
    public static final String GOIAS_HOMOLOGACAO_CARTA_CORRECAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRecepcaoEvento/nfeRecepcaoEventoNF";

    //PRODUÇÂO GOIÁS
    public static final String GOIAS_PRODUCAO_STATUS_SERVICO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2";
    public static final String GOIAS_PRODUCAO_STATUS_SERVICO_URL = "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeStatusServico2";
    public static final String GOIAS_PRODUCAO_STATUS_SERVICO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2/nfeStatusServicoNF2";

    public static final String GOIAS_PRODUCAO_ENVIAR_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao";
    public static final String GOIAS_PRODUCAO_ENVIAR_URL = "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeAutorizacao";
    public static final String GOIAS_PRODUCAO_ENVIAR_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao/nfeAutorizacaoLote";

    public static final String GOIAS_PRODUCAO_CONSULTA_LOTE_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao";
    public static final String GOIAS_PRODUCAO_CONSULTA_LOTE_URL = "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeRetAutorizacao";
    public static final String GOIAS_PRODUCAO_CONSULTA_LOTE_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao/nfeRetAutorizacaoLote";

    public static final String GOIAS_PRODUCAO_CONSULTA_NOTA_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2";
    public static final String GOIAS_PRODUCAO_CONSULTA_NOTA_URL = "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeConsulta2";
    public static final String GOIAS_PRODUCAO_CONSULTA_NOTA_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2/nfeConsultaNF2";

    public static final String GOIAS_PRODUCAO_CANCELAMENTO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento";
    public static final String GOIAS_PRODUCAO_CANCELAMENTO_URL = "https://nfe.sefaz.go.gov.br/nfe/services/v2/RecepcaoEvento";
    public static final String GOIAS_PRODUCAO_CANCELAMENTO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento/nfeRecepcaoEvento";

    public static final String GOIAS_PRODUCAO_INUTILIZACAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2";
    public static final String GOIAS_PRODUCAO_INUTILIZACAO_URL = "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeInutilizacao2";
    public static final String GOIAS_PRODUCAO_INUTILIZACAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2/nfeInutilizacaoNF2";
    
    public static final String GOIAS_PRODUCAO_CARTACORRECAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRecepcaoEvento";
    public static final String GOIAS_PRODUCAO_CARTACORRECAO_URL = "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeRecepcaoEvento";
    public static final String GOIAS_PRODUCAO_CARTACORRECAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRecepcaoEvento/nfeRecepcaoEventoNF";


    //RIO DE JANEIRO
    public static final String RIO_DE_JANEIRO_CODIGOESTADO = "33";

    //HOMOLOGAÇÃO RIO DE JANEIRO
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_STATUS_SERVICO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_STATUS_SERVICO_URL = "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeStatusServico/nfeStatusServico2.asmx";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_STATUS_SERVICO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2/nfeStatusServicoNF2";

    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_ENVIAR_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_ENVIAR_URL = "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeRecepcao/NfeAutorizacao.asmx";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_ENVIAR_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao/nfeAutorizacaoLote";

    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_LOTE_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_LOTE_URL = "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeRetRecepcao/NfeRetAutorizacao.asmx";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_LOTE_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao/nfeRetAutorizacaoLote";

    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_NOTA_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_NOTA_URL = "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeConsulta/nfeConsulta2.asmx";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CONSULTA_NOTA_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2/nfeConsultaNF2";

    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CANCELAMENTO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CANCELAMENTO_URL = "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeCancelamento/nfeCancelamento2.asmx";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CANCELAMENTO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2/nfeCancelamentoNF2";

    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_INUTILIZACAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_INUTILIZACAO_URL = "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeInutilizacao/nfeInutilizacao2.asmx";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_INUTILIZACAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2/nfeInutilizacaoNF2";
    
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CARTA_CORRECAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRecepcaoEvento";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CARTA_CORRECAO_URL = "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeInutilizacao/nfeRecepcaoEvento.asmx";
    public static final String RIO_DE_JANEIRO_HOMOLOGACAO_CARTA_CORRECAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2/nfeRecepcaoEventoNF";
    
    //PRODUÇÃO RIO DE JANEIRO
    public static final String RIO_DE_JANEIRO_PRODUCAO_STATUS_SERVICO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2";
    public static final String RIO_DE_JANEIRO_PRODUCAO_STATUS_SERVICO_URL = "https://nfe.sefazvirtual.rs.gov.br/ws/nfeStatusServico/nfeStatusServico2.asmx";
    public static final String RIO_DE_JANEIRO_PRODUCAO_STATUS_SERVICO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2/nfeStatusServicoNF2";

    public static final String RIO_DE_JANEIRO_PRODUCAO_ENVIAR_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao";
    public static final String RIO_DE_JANEIRO_PRODUCAO_ENVIAR_URL = "https://nfe.sefazvirtual.rs.gov.br/ws/nferecepcao/NfeAutorizacao.asmx";
    public static final String RIO_DE_JANEIRO_PRODUCAO_ENVIAR_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao/nfeAutorizacaoLote";

    public static final String RIO_DE_JANEIRO_PRODUCAO_CONSULTA_LOTE_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao";
    public static final String RIO_DE_JANEIRO_PRODUCAO_CONSULTA_LOTE_URL = "https://nfe.sefazvirtual.rs.gov.br/ws/nfeRetRecepcao/NfeRetAutorizacao.asmx";
    public static final String RIO_DE_JANEIRO_PRODUCAO_CONSULTA_LOTE_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao/nfeRetAutorizacaoLote";

    public static final String RIO_DE_JANEIRO_PRODUCAO_CONSULTA_NOTA_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2";
    public static final String RIO_DE_JANEIRO_PRODUCAO_CONSULTA_NOTA_URL = "https://nfe.sefazvirtual.rs.gov.br/ws/nfeConsulta/nfeConsulta2.asmx";
    public static final String RIO_DE_JANEIRO_PRODUCAO_CONSULTA_NOTA_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2/nfeConsultaNF2";

    public static final String RIO_DE_JANEIRO_PRODUCAO_CANCELAMENTO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2";
    public static final String RIO_DE_JANEIRO_PRODUCAO_CANCELAMENTO_URL = "https://nfe.sefazvirtual.rs.gov.br/ws/nfeCancelamento/nfeCancelamento2.asmx";
    public static final String RIO_DE_JANEIRO_PRODUCAO_CANCELAMENTO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeCancelamento2/nfeCancelamentoNF2";

    public static final String RIO_DE_JANEIRO_PRODUCAO_INUTILIZACAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2";
    public static final String RIO_DE_JANEIRO_PRODUCAO_INUTILIZACAO_URL = "https://nfe.sefazvirtual.rs.gov.br/ws/nfeInutilizacao/nfeInutilizacao2.asmx";
    public static final String RIO_DE_JANEIRO_PRODUCAO_INUTILIZACAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2/nfeInutilizacaoNF2";

    public static final String RIO_DE_JANEIRO_PRODUCAO_CARTACORRECAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRecepcaoEvento";
    public static final String RIO_DE_JANEIRO_PRODUCAO_CARTACORRECAO_URL = "https://nfe.sefazvirtual.rs.gov.br/ws/nfeInutilizacao/nfeRecepcaoEvento.asmx";
    public static final String RIO_DE_JANEIRO_PRODUCAO_CARTACORRECAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2/nfeRecepcaoEventoNF";

    // HOMOLOGACAO DISTRITO FEDERAL
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_STATUS_SERVICO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_STATUS_SERVICO_URL = "https://nfe-homologacao.svrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_STATUS_SERVICO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2/nfeStatusServicoNF2";
    
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_ENVIAR_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_ENVIAR_URL = "https://nfe-homologacao.svrs.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_ENVIAR_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao/nfeAutorizacaoLote";
    
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_LOTE_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_LOTE_URL = "https://nfe-homologacao.svrs.rs.gov.br/ws/NfeRetAutorizacao/NFeRetAutorizacao.asmx";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_LOTE_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao/nfeRetAutorizacaoLote";
    
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_NOTA_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_NOTA_URL = "https://nfe-homologacao.svrs.rs.gov.br/ws/NfeConsulta/NfeConsulta2.asmx";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CONSULTA_NOTA_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2/nfeConsultaNF2";
    
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CANCELAMENTO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CANCELAMENTO_URL = "https://nfe-homologacao.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CANCELAMENTO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento/nfeRecepcaoEvento";
    
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_INUTILIZACAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_INUTILIZACAO_URL = "https://nfe-homologacao.svrs.rs.gov.br/ws/nfeinutilizacao/nfeinutilizacao2.asmx";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_INUTILIZACAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2/nfeInutilizacaoNF2";
    
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CARTACORRECAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CARTACORRECAO_URL = "https://nfe-homologacao.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx";
    public static final String DISTRITO_FEDERAL_HOMOLOGACAO_CARTACORRECAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento/nfeRecepcaoEventoNF";
    
    // PRODUCAO DISTRITO FEDERAL
    public static final String DISTRITO_FEDERAL_PRODUCAO_STATUS_SERVICO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2";
    public static final String DISTRITO_FEDERAL_PRODUCAO_STATUS_SERVICO_URL = "https://nfe.svrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx";
    public static final String DISTRITO_FEDERAL_PRODUCAO_STATUS_SERVICO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico2/nfeStatusServicoNF2";
    
    public static final String DISTRITO_FEDERAL_PRODUCAO_ENVIAR_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao";
    public static final String DISTRITO_FEDERAL_PRODUCAO_ENVIAR_URL = "https://nfe.svrs.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx";
    public static final String DISTRITO_FEDERAL_PRODUCAO_ENVIAR_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeAutorizacao/nfeAutorizacaoLote";
    
    public static final String DISTRITO_FEDERAL_PRODUCAO_CONSULTA_LOTE_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao";
    public static final String DISTRITO_FEDERAL_PRODUCAO_CONSULTA_LOTE_URL = "https://nfe.svrs.rs.gov.br/ws/NfeRetAutorizacao/NFeRetAutorizacao.asmx";
    public static final String DISTRITO_FEDERAL_PRODUCAO_CONSULTA_LOTE_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetAutorizacao/nfeRetAutorizacaoLote";
    
    public static final String DISTRITO_FEDERAL_PRODUCAO_CONSULTA_NOTA_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2";
    public static final String DISTRITO_FEDERAL_PRODUCAO_CONSULTA_NOTA_URL = "https://nfe.svrs.rs.gov.br/ws/NfeConsulta/NfeConsulta2.asmx";
    public static final String DISTRITO_FEDERAL_PRODUCAO_CONSULTA_NOTA_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta2/nfeConsultaNF2";
    
    public static final String DISTRITO_FEDERAL_PRODUCAO_CANCELAMENTO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento";
    public static final String DISTRITO_FEDERAL_PRODUCAO_CANCELAMENTO_URL = "https://nfe.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx";
    public static final String DISTRITO_FEDERAL_PRODUCAO_CANCELAMENTO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento/nfeRecepcaoEvento";
    
    public static final String DISTRITO_FEDERAL_PRODUCAO_INUTILIZACAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2";
    public static final String DISTRITO_FEDERAL_PRODUCAO_INUTILIZACAO_URL = "https://nfe.svrs.rs.gov.br/ws/nfeinutilizacao/nfeinutilizacao2.asmx";
    public static final String DISTRITO_FEDERAL_PRODUCAO_INUTILIZACAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao2/nfeInutilizacaoNF2";
    
    public static final String DISTRITO_FEDERAL_PRODUCAO_CARTACORRECAO_XMLNS = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento";
    public static final String DISTRITO_FEDERAL_PRODUCAO_CARTACORRECAO_URL = "https://nfe.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx";
    public static final String DISTRITO_FEDERAL_PRODUCAO_CARTACORRECAO_SOAP_ACTION = "http://www.portalfiscal.inf.br/nfe/wsdl/RecepcaoEvento/nfeRecepcaoEventoNF";
    
}
