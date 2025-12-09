package negocio.comuns.utilitarias.faturamento.nfe;

/**
 *
 * @author Euripedes Doutor
 */
public class DadosNfe {
    public static final String VERSAO_LAYOUT = "3.10";
    public static final String NFE_XMLS = "http://www.portalfiscal.inf.br/nfe";
    //USADOS NA TAG indPag = Indicador da Forma de Pagamento
    public static final String PAGAMENTO_AVISTA = "0";
    public static final String PAGAMENTO_APRAZO = "1";
    public static final String PAGAMENTO_OUTROS = "2";
    //USADOS NA TAG tpNF = Tipo de Documento Fiscal
    public static final String NFE_ENTRADA = "0";
    public static final String NFE_SAIDA = "1";
    //USADOS NA TAG tpImp = Formato da Impressão do DANFE
    public static final String PAISAGEM = "2";
    public static final String RETRATO = "1";
    //USADOS NA TAG tpEmis = Forma de Emissão da NF-e
    public static final String NORMAL = "1";
    public static final String CONTINGENCIA = "2";
    //USADOS NA TAG finNFe = Finalidade da Emissão da NF-e
    public static final String FINALIDADE_EMISSAO_NFE_NORMAL = "1";
    public static final String FINALIDADE_EMISSAO_NFE_COMPLEMENTAR = "2";
    public static final String FINALIDADE_EMISSAO_NFE_AJUSTE = "3";
    //USADOS NA TAG procEmi = Processo de Emissão da NF-e
    public static final String EMISSAO_CONTRIBUITE = "0";
    public static final String EMISSAO_AVULSA = "1";
    public static final String EMISSAO_AVULSA_CONTRIBUINTE = "2";
    public static final String EMISSAO_CONTRIBUINTE_FISCO = "3";
    //USADOS NA TAG verProc = Versão do Aplicativo do Emissor
    public static final String VERSAO_APLICATIVO = "3.10";
    //USADOS NA TAG orig = Origem Mercadoria
    public static final String ORIGEM_NACIONAL = "0";
    public static final String ORIGEM_IMPORTADA_DIRETA = "1";
    public static final String ORIGEM_IMPORTADA_INTERNA = "2";
    //USADOS NA TAG modBCST = Modalidade de Determinação da BC do ICMS
    public static final String MODALIDADE_BC_ICMS_MARGEM_VALOR_AGREGADO = "0";
    public static final String MODALIDADE_BC_ICMS_PAUTA = "1";
    public static final String MODALIDADE_BC_ICMS_PRECO_TABELADO = "2";
    public static final String MODALIDADE_BC_ICMS_VALOR_OPERACAO = "3";
    //USADOS NA TAG modBCST = Modalidade de Determinação da BC do ICMS ST
    public static final String MODALIDADE_BC_ICMS_ST_PRECO_TABELADO = "0";
    public static final String MODALIDADE_BC_ICMS_ST_LISTA_NEGATIVA = "1";
    public static final String MODALIDADE_BC_ICMS_ST_LISTA_POSITIVA = "2";
    public static final String MODALIDADE_BC_ICMS_ST_LISTA_NEUTRA = "3";
    public static final String MODALIDADE_BC_ICMS_ST_MARGEM_VALOR_AGREGADO = "4";
    public static final String MODALIDADE_BC_ICMS_ST_PAUTA = "5";
    //USADOS NA TAG CST = Tributação do ICMS
    public static final String TRIBUTACAO_ICMS_INTEGRAL = "00";// Usada Para tag CST da TAG ICMS00
    public static final String TRIBUTACAO_ICMS_COM_SUB_TRUB = "10";// Usada Para tag CST da TAG ICMS00
    public static final String TRIBUTACAO_ICMS_COM_REDUCAO_BASE_CALCULO = "20";// Usada Para tag CST da TAG ICMS20
    public static final String TRIBUTACAO_ICMS_ISENTA_COM_SUB_TRIB = "30";// Usada Para tag CST da TAG ICMS30
    public static final String TRIBUTACAO_ICMS_ISENTA = "40";// Usada Para tag CST da TAG ICMS40
    public static final String TRIBUTACAO_ICMS_ISENTA_SIMPLES_NACIONAL = "102";// Usada Para tag CST da TAG ICMSNS102
    public static final String TRIBUTACAO_ICMS_COM_REDUCAO_POR_DEFERIMENTO = "51"; // Usada Para tag CST da TAG ICMS51
    public static final String TRIBUTACAO_ICMS_ISENTA_COM_SUB_TRIB_ANTERIOR = "60"; // Usada Para tag CST da TAG ICMS70
    public static final String TRIBUTACAO_ICMS_COM_REDUCAO_BASE_CALCULO_COM_SUB_TRIB = "70"; // Usada Para tag CST da TAG ICMS70
    public static final String TRIBUTACAO_ICMS_OUTROS = "90"; // Usada Para tag CST da TAG ICMS90
    //USADAS NA TAG CST = Tributação de IPI = Código da situação tributária do IPI
    public static final String TRIBUTACAO_IPI_ENTRADA_RECUPERACAO_CREDITO = "00";
    public static final String TRIBUTACAO_IPI_ENTRADA_ALIQUOTA_ZERO = "01";
    public static final String TRIBUTACAO_IPI_ENTRADA_ISENTA = "02";
    public static final String TRIBUTACAO_IPI_ENTRADA_NAO_TRIBUTADA = "03";
    public static final String TRIBUTACAO_IPI_ENTRADA_IMUNE = "04";
    public static final String TRIBUTACAO_IPI_ENTRADA_SUSPENSAO = "05";
    public static final String TRIBUTACAO_IPI_OUTRAS_ENTRADAS = "49";
    public static final String TRIBUTACAO_IPI_SAIDA_TRIBUTADA = "50";
    public static final String TRIBUTACAO_IPI_SAIDA_ALIQUOTA_ZERO = "51";
    public static final String TRIBUTACAO_IPI_SAIDA_ISENTA = "52";
    public static final String TRIBUTACAO_IPI_SAIDA_NAO_TRIBUTADA = "53";
    public static final String TRIBUTACAO_IPI_SAIDA_IMUNE = "54";
    public static final String TRIBUTACAO_IPI_SAIDA_SUSPENSAO = "55";
    public static final String TRIBUTACAO_IPI_OUTRAS_SAIDAS = "99";
    //USADAS NA TAG CST = Tributação de PIS = Código da situação tributária do PIS
    public static final String TRIBUTACAO_PIS_NORMAL = "01"; // Usada Para TAG CST da TAG PISAliq = quando é tributada pela aliquota
    public static final String TRIBUTACAO_PIS_DIFERENCIADA = "02";// Usada Para TAG CST da TAG PISAliq = quando é tributada pela aliquota
    public static final String TRIBUTACAO_PIS_UNIDADE_PRODUTO = "03";// Usada Para TAG CST da TAG PISQtde = quando é tributada pela quantidade
    public static final String TRIBUTACAO_PIS_MONOFASICA = "04";// Usada Para TAG CST da TAG PISNT = quando não é tributada
    public static final String TRIBUTACAO_PIS_ALIQUOTA_ZERO = "06";// Usada Para TAG CST da TAG PISNT = quando não é tributada
    public static final String TRIBUTACAO_PIS_ISENTA_CONTRIBUICAO = "07";// Usada Para TAG CST da TAG PISNT = quando não é tributada
    public static final String TRIBUTACAO_PIS_SEM_INCIDENTE_CONTRIBUICAO = "08";// Usada Para TAG CST da TAG PISNT = quando não é tributada
    public static final String TRIBUTACAO_PIS_COM_SUSPENSAO_CONTRIBUICAO = "09";// Usada Para TAG CST da TAG PISNT = quando não é tributada
    public static final String TRIBUTACAO_PIS_OUTRAS = "99";// Usada Para TAG CST da TAG PISOutros = quando não é tributada
    //USADAS NA TAG CST = Tributação de CONFINS = Código da situação tributária do CONFINS
    public static final String TRIBUTACAO_CONFINS_NORMAL = "01"; // Usada Para TAG CST da TAG CONFINSAliq = quando é tributada pela aliquota
    public static final String TRIBUTACAO_CONFINS_DIFERENCIADA = "02";// Usada Para TAG CST da TAG CONFINSAliq = quando é tributada pela aliquota
    public static final String TRIBUTACAO_CONFINS_UNIDADE_PRODUTO = "03";// Usada Para TAG CST da TAG CONFINSQtde = quando é tributada pela quantidade
    public static final String TRIBUTACAO_CONFINS_MONOFASICA = "04";// Usada Para TAG CST da TAG CONFINSNT = quando não é tributada
    public static final String TRIBUTACAO_CONFINS_ALIQUOTA_ZERO = "06";// Usada Para TAG CST da TAG CONFINSNT = quando não é tributada
    public static final String TRIBUTACAO_CONFINS_ISENTA_CONTRIBUICAO = "07";// Usada Para TAG CST da TAG CONFINSNT = quando não é tributada
    public static final String TRIBUTACAO_CONFINS_SEM_INCIDENTE_CONTRIBUICAO = "08";// Usada Para TAG CST da TAG CONFINSNT = quando não é tributada
    public static final String TRIBUTACAO_CONFINS_COM_SUSPENSAO_CONTRIBUICAO = "09";// Usada Para TAG CST da TAG CONFINSNT = quando não é tributada
    public static final String TRIBUTACAO_CONFINS_OUTRAS = "99";// Usada Para TAG CST da TAG CONFINSOutros = quando não é tributada
    //USADAS NA TAG modFrete = Modalidade do Frete em Transportadora
    public static final String FRETE_EMITENTE = "0";
    public static final String FRETE_DESTINATARIO = "1";
    //USADAS NA TAG indProc = Indicador da Origem do Processo
    public static final String PROCESSO_SEFAZ = "0";
    public static final String PROCESSO_JUSTICA_FEDERAL = "1";
    public static final String PROCESSO_JUSTICA_ESTADUAL = "2";
    public static final String PROCESSO_SECEX_RFB = "3";
    public static final String PROCESSO_OUTROS = "9";
    
    public static final String AMBIENTE = "2";
    
    //USADAS NA TAG idDest = Identificador de local de destino da operação
    public static final String OPERACAO_INTERNA = "1";
    public static final String OPERACAO_INTERESTADUAL = "2";
    public static final String OPERACAO_COM_EXTERIOR = "3";
    
    //USADAS NA TAG indPres = Indicador de presença do comprador  no estabelecimento comercial no momento da operação
    public static final String NAO_SE_APLICA = "0";
    public static final String OPERACAO_PRESENCIAL = "1";
    public static final String OPERACAO_NAO_PRESENCIAL_INTERNET = "2";
    public static final String OPERACAO_NAO_PRESENCIAL_TELEATENDIMENTO = "3";
    public static final String OPERACAO_ENTREGA_DOMICILIO_NFC = "4";
    public static final String OPERACAO_NAO_PRESENCIAL_OUTROS = "9";
}
