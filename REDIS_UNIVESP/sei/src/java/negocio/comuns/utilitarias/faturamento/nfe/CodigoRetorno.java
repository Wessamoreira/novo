/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.faturamento.nfe;

/**
 *
 * @author Euripedes
 */
public class CodigoRetorno {

    public static final String CODIGO_100 = "Autorizado o uso da NF-e";
    public static final String CODIGO_101 = "Cancelamento de NF-e homologado";
    public static final String CODIGO_102 = "Inutilização de número homologado";
    public static final String CODIGO_128 = "Lote de Evento Processado";
    public static final String CODIGO_135 = "Evento registrado e vinculado a NF-e";
    public static final String CODIGO_103 = "Lote recebido com sucesso";
    public static final String CODIGO_104 = "Lote processado";
    public static final String CODIGO_105 = "Lote em processamento";
    public static final String CODIGO_106 = "Lote não localizado";
    public static final String CODIGO_107 = "Serviço em Operação";
    public static final String CODIGO_108 = "Serviço Paralisado Momentaneamente (curto prazo)";
    public static final String CODIGO_109 = "Serviço Paralisado sem Previsão";
    public static final String CODIGO_110 = "Uso Denegado";
    public static final String CODIGO_111 = "Consulta cadastro com uma ocorrência";
    public static final String CODIGO_112 = "Consulta cadastro com mais de uma ocorrência";
    public static final String CODIGO_201 = "Rejeição: O numero máximo de numeração de NF-e a inutilizar ultrapassou o limite";
    public static final String CODIGO_202 = "Rejeição: Falha no reconhecimento da autoria ou integridade do arquivo digital";
    public static final String CODIGO_203 = "Rejeição: Emissor não habilitado para emissão da NF-e";
    public static final String CODIGO_204 = "Rejeição: Duplicidade de NF-e";
    public static final String CODIGO_205 = "Rejeição: NF-e está denegada na base de dados da SEFAZ";
    public static final String CODIGO_206 = "Rejeição: NF-e já está inutilizada na Base de dados da SEFAZ";
    public static final String CODIGO_207 = "Rejeição: CNPJ do emitente inválido";
    public static final String CODIGO_208 = "Rejeição: CNPJ do destinatário inválido";
    public static final String CODIGO_209 = "Rejeição: IE do emitente inválida";
    public static final String CODIGO_210 = "Rejeição: IE do destinatário inválida";
    public static final String CODIGO_211 = "Rejeição: IE do substituto inválida";
    public static final String CODIGO_212 = "Rejeição: Data de emissão NF-e posterior a data de recebimento";
    public static final String CODIGO_213 = "Rejeição: CNPJ-Base do Emitente difere do CNPJ-Base do Certificado Digital";
    public static final String CODIGO_214 = "Rejeição: Tamanho da mensagem excedeu o limite estabelecido";
    public static final String CODIGO_215 = "Rejeição: Falha no schema XML";
    public static final String CODIGO_216 = "Rejeição: Chave de Acesso difere da cadastrada";
    public static final String CODIGO_217 = "Rejeição: NF-e não consta na base de dados da SEFAZ";
    public static final String CODIGO_218 = "Rejeição: NF-e já esta cancelada na base de dados da SEFAZ";
    public static final String CODIGO_219 = "Rejeição: Circulação da NF-e verificada";
    public static final String CODIGO_220 = "Rejeição: NF-e autorizada há mais de 1 dia (24 horas)";
    public static final String CODIGO_221 = "Rejeição: Confirmado o recebimento da NF-e pelo destinatário";
    public static final String CODIGO_222 = "Rejeição: Protocolo de Autorização de Uso difere do cadastrado";
    public static final String CODIGO_223 = "Rejeição: CNPJ do transmissor do lote difere do CNPJ do transmissor da consulta";
    public static final String CODIGO_224 = "Rejeição: A faixa inicial é maior que a faixa final";
    public static final String CODIGO_225 = "Rejeição: Falha no Schema XML do lote de NFe";
    public static final String CODIGO_226 = "Rejeição: Código da UF do Emitente diverge da UF autorizadora";
    public static final String CODIGO_227 = "Rejeição: Erro na Chave de Acesso - Campo Id ? falta a literal NFe";
    public static final String CODIGO_228 = "Rejeição: Data de Emissão muito atrasada";
    public static final String CODIGO_229 = "Rejeição: IE do emitente não informada";
    public static final String CODIGO_230 = "Rejeição: IE do emitente não cadastrada";
    public static final String CODIGO_231 = "Rejeição: IE do emitente não vinculada ao CNPJ";
    public static final String CODIGO_232 = "Rejeição: IE do destinatário não informada";
    public static final String CODIGO_233 = "Rejeição: IE do destinatário não cadastrada";
    public static final String CODIGO_234 = "Rejeição: IE do destinatário não vinculada ao CNPJ";
    public static final String CODIGO_235 = "Rejeição: Inscrição SUFRAMA inválida";
    public static final String CODIGO_236 = "Rejeição: Chave de Acesso com dígito verificador inválido";
    public static final String CODIGO_237 = "Rejeição: CPF do destinatário inválido";
    public static final String CODIGO_238 = "Rejeição: Cabeçalho - Versão do arquivo XML superior a Versão vigente";
    public static final String CODIGO_239 = "Rejeição: Cabeçalho - Versão do arquivo XML não suportada";
    public static final String CODIGO_240 = "Rejeição: Cancelamento/Inutilização - Irregularidade Fiscal do Emitente";
    public static final String CODIGO_241 = "Rejeição: Um número da faixa já foi utilizado";
    public static final String CODIGO_242 = "Rejeição: Cabeçalho - Falha no Schema XML";
    public static final String CODIGO_243 = "Rejeição: XML Mal Formado";
    public static final String CODIGO_244 = "Rejeição: CNPJ do Certificado Digital difere do CNPJ da Matriz e do CNPJ do Emitente";
    public static final String CODIGO_245 = "Rejeição: CNPJ Emitente não cadastrado";
    public static final String CODIGO_246 = "Rejeição: CNPJ Destinatário não cadastrado";
    public static final String CODIGO_247 = "Rejeição: Sigla da UF do Emitente diverge da UF autorizadora";
    public static final String CODIGO_248 = "Rejeição: UF do Recibo diverge da UF autorizadora";
    public static final String CODIGO_249 = "Rejeição: UF da Chave de Acesso diverge da UF autorizadora";
    public static final String CODIGO_250 = "Rejeição: UF diverge da UF autorizadora";
    public static final String CODIGO_251 = "Rejeição: UF/Município destinatário não pertence a SUFRAMA";
    public static final String CODIGO_252 = "Rejeição: Ambiente informado diverge do Ambiente de recebimento";
    public static final String CODIGO_253 = "Rejeição: Digito Verificador da chave de acesso composta inválida";
    public static final String CODIGO_254 = "Rejeição: NF-e complementar não possui NF referenciada";
    public static final String CODIGO_255 = "Rejeição: NF-e complementar possui mais de uma NF referenciada";
    public static final String CODIGO_256 = "Rejeição: Uma NF-e da faixa já está inutilizada na Base de dados da SEFAZ";
    public static final String CODIGO_257 = "Rejeição: Solicitante não habilitado para emissão da NF-e";
    public static final String CODIGO_258 = "Rejeição: CNPJ da consulta inválido";
    public static final String CODIGO_259 = "Rejeição: CNPJ da consulta não cadastrado como contribuinte na UF";
    public static final String CODIGO_260 = "Rejeição: IE da consulta inválida";
    public static final String CODIGO_261 = "Rejeição: IE da consulta não cadastrada como contribuinte na UF";
    public static final String CODIGO_262 = "Rejeição: UF não fornece consulta por CPF";
    public static final String CODIGO_263 = "Rejeição: CPF da consulta inválido";
    public static final String CODIGO_264 = "Rejeição: CPF da consulta não cadastrado como contribuinte na UF";
    public static final String CODIGO_265 = "Rejeição: Sigla da UF da consulta difere da UF do Web Service";
    public static final String CODIGO_266 = "Rejeição: Série utilizada não permitida no Web Service";
    public static final String CODIGO_267 = "Rejeição: NF Complementar referencia uma NF-e inexistente";
    public static final String CODIGO_268 = "Rejeição: NF Complementar referencia uma outra NF-e Complementar";
    public static final String CODIGO_269 = "Rejeição: CNPJ Emitente da NF Complementar difere do CNPJ da NF Referenciada";
    public static final String CODIGO_270 = "Rejeição: Código Município do Fato Gerador: dígito inválido";
    public static final String CODIGO_271 = "Rejeição: Código Município do Fato Gerador: difere da UF do emitente";
    public static final String CODIGO_272 = "Rejeição: Código Município do Emitente: dígito inválido";
    public static final String CODIGO_273 = "Rejeição: Código Município do Emitente: difere da UF do emitente";
    public static final String CODIGO_274 = "Rejeição: Código Município do Destinatário: dígito inválido";
    public static final String CODIGO_275 = "Rejeição: Código Município do Destinatário: difere da UF do Destinatário";
    public static final String CODIGO_276 = "Rejeição: Código Município do Local de Retirada: dígito inválido";
    public static final String CODIGO_277 = "Rejeição: Código Município do Local de Retirada: difere da UF do Local de Retirada";
    public static final String CODIGO_278 = "Rejeição: Código Município do Local de Entrega: dígito inválido";
    public static final String CODIGO_279 = "Rejeição: Código Município do Local de Entrega: difere da UF do Local de Entrega";
    public static final String CODIGO_280 = "Rejeição: Certificado Transmissor inválido";
    public static final String CODIGO_281 = "Rejeição: Certificado Transmissor Data Validade";
    public static final String CODIGO_282 = "Rejeição: Certificado Transmissor sem CNPJ";
    public static final String CODIGO_283 = "Rejeição: Certificado Transmissor - erro Cadeia de Certificação";
    public static final String CODIGO_284 = "Rejeição: Certificado Transmissor revogado";
    public static final String CODIGO_285 = "Rejeição: Certificado Transmissor difere ICP-Brasil";
    public static final String CODIGO_286 = "Rejeição: Certificado Transmissor erro no acesso a LCR";
    public static final String CODIGO_287 = "Rejeição: Código Município do FG - ISSQN: dígito inválido";
    public static final String CODIGO_288 = "Rejeição: Código Município do FG - Transporte: dígito inválido";
    public static final String CODIGO_289 = "Rejeição: Código da UF informada diverge da UF solicitada";
    public static final String CODIGO_290 = "Rejeição: Certificado Assinatura inválido";
    public static final String CODIGO_291 = "Rejeição: Certificado Assinatura Data Validade";
    public static final String CODIGO_292 = "Rejeição: Certificado Assinatura sem CNPJ";
    public static final String CODIGO_293 = "Rejeição: Certificado Assinatura - erro Cadeia de Certificação";
    public static final String CODIGO_294 = "Rejeição: Certificado Assinatura revogado";
    public static final String CODIGO_295 = "Rejeição: Certificado Assinatura difere ICP-Brasil";
    public static final String CODIGO_296 = "Rejeição: Certificado Assinatura erro no acesso a LCR";
    public static final String CODIGO_297 = "Rejeição: Assinatura difere do calculado";
    public static final String CODIGO_298 = "Rejeição: Assinatura difere do padrão do Projeto";
    public static final String CODIGO_299 = "Rejeição: XML da área de cabeçalho com codificação diferente de UTF-8";
    public static final String CODIGO_401 = "Rejeição: CPF do remetente inválido";
    public static final String CODIGO_402 = "Rejeição: XML da área de dados com codificação diferente de UTF-8";
    public static final String CODIGO_403 = "Rejeição: O grupo de informações da NF-e avulsa é de uso exclusivo do Fisco";
    public static final String CODIGO_404 = "Rejeição: Uso de prefixo de namespace não permitido";
    public static final String CODIGO_405 = "Rejeição: Código do país do emitente: dígito inválido";
    public static final String CODIGO_406 = "Rejeição: Código do país do destinatário: dígito inválido";
    public static final String CODIGO_407 = "Rejeição: O CPF só pode ser informado no campo emitente para a NF-e avulsa";
    public static final String CODIGO_409 = "Rejeição: Campo cUF inexistente no elemento nfeCabecMsg do SOAP Header";
    public static final String CODIGO_410 = "Rejeição: UF informada no campo cUF não é atendida pelo Web Service";
    public static final String CODIGO_411 = "Rejeição: Campo versaoDados inexistente no elemento nfeCabecMsg do SOAP Header";
    public static final String CODIGO_420 = "Rejeição: Cancelamento para NF-e já cancelada";
    public static final String CODIGO_450 = "Rejeição: Modelo da NF-e diferente de 55";
    public static final String CODIGO_451 = "Rejeição: Processo de emissão informado inválido";
    public static final String CODIGO_452 = "Rejeição: Tipo Autorizador do Recibo diverge do Órgão Autorizador";
    public static final String CODIGO_453 = "Rejeição: Ano de inutilização não pode ser superior ao Ano atual";
    public static final String CODIGO_454 = "Rejeição: Ano de inutilização não pode ser inferior a 2006";
    public static final String CODIGO_478 = "Rejeição: Local da entrega não informado para faturamento direto de veículos novos";
    public static final String CODIGO_502 = "Rejeição: Erro na Chave de Acesso - Campo Id não corresponde à concatenação dos campos correspondentes";
    public static final String CODIGO_501 = "Rejeição: Prazo de cancelamento superior ao previsto na Legislação";
    public static final String CODIGO_503 = "Rejeição: Série utilizada fora da faixa permitida no SCAN (900-999)";
    public static final String CODIGO_504 = "Rejeição: Data de Entrada/Saída posterior ao permitido";
    public static final String CODIGO_505 = "Rejeição: Data de Entrada/Saída anterior ao permitido";
    public static final String CODIGO_506 = "Rejeição: Data de Saída menor que a Data de Emissão";
    public static final String CODIGO_507 = "Rejeição: O CNPJ do destinatário/remetente não deve ser informado em operação com o exterior";
    public static final String CODIGO_508 = "Rejeição: O CNPJ com conteúdo nulo só é válido em operação com exterior";
    public static final String CODIGO_509 = "Rejeição: Informado código de município diferente de ?9999999? para operação com o exterior";
    public static final String CODIGO_510 = "Rejeição: Operação com Exterior e Código País destinatário é 1058 (Brasil) ou não informado";
    public static final String CODIGO_511 = "Rejeição: Não é de Operação com Exterior e Código País destinatário difere de 1058 (Brasil)";
    public static final String CODIGO_512 = "Rejeição: CNPJ do Local de Retirada inválido";
    public static final String CODIGO_513 = "Rejeição: Código Município do Local de Retirada deve ser 9999999 para UF retirada = EX";
    public static final String CODIGO_514 = "Rejeição: CNPJ do Local de Entrega inválido";
    public static final String CODIGO_515 = "Rejeição: Código Município do Local de Entrega deve ser 9999999 para UF entrega = EX";
    public static final String CODIGO_516 = "Rejeição: Falha no schema XML ? inexiste a tag raiz esperada para a mensagem";
    public static final String CODIGO_517 = "Rejeição: Falha no schema XML ? inexiste atributo versao na tag raiz da mensagem";
    public static final String CODIGO_518 = "Rejeição: CFOP de entrada para NF-e de saída";
    public static final String CODIGO_519 = "Rejeição: CFOP de saída para NF-e de entrada";
    public static final String CODIGO_520 = "Rejeição: CFOP de Operação com Exterior e UF destinatário difere de EX";
    public static final String CODIGO_521 = "Rejeição: CFOP não é de Operação com Exterior e UF destinatário é EX";
    public static final String CODIGO_522 = "Rejeição: CFOP de Operação Estadual e UF emitente difere UF destinatário.";
    public static final String CODIGO_523 = "Rejeição: CFOP não é de Operação Estadual e UF emitente igual a UF destinatário.";
    public static final String CODIGO_524 = "Rejeição: CFOP de Operação com Exterior e não informado NCM";
    public static final String CODIGO_525 = "Rejeição: CFOP de Importação e não informado dados da DI";
    public static final String CODIGO_526 = "Rejeição: CFOP de Exportação e não informado Local de Embarque";
    public static final String CODIGO_527 = "Rejeição: Operação de Exportação com informação de ICMS incompatível";
    public static final String CODIGO_528 = "Rejeição: Valor do ICMS difere do produto BC e Alíquota";
    public static final String CODIGO_529 = "Rejeição: NCM de informação obrigatória para produto tributado pelo IPI";
    public static final String CODIGO_530 = "Rejeição: Operação com tributação de ISSQN sem informar a Inscrição Municipal";
    public static final String CODIGO_531 = "Rejeição: Total da BC ICMS difere do somatório dos itens";
    public static final String CODIGO_532 = "Rejeição: Total do ICMS difere do somatório dos itens";
    public static final String CODIGO_533 = "Rejeição: Total da BC ICMS-ST difere do somatório dos itens";
    public static final String CODIGO_534 = "Rejeição: Total do ICMS-ST difere do somatório dos itens";
    public static final String CODIGO_535 = "Rejeição: Total do Frete difere do somatório dos itens";
    public static final String CODIGO_536 = "Rejeição: Total do Seguro difere do somatório dos itens";
    public static final String CODIGO_537 = "Rejeição: Total do Desconto difere do somatório dos itens";
    public static final String CODIGO_538 = "Rejeição: Total do IPI difere do somatório dos itens";
    public static final String CODIGO_539 = "Rejeição: Duplicidade de NF-e, com diferença na Chave de Acesso[99999999999999999999999999999999999999999]";
    public static final String CODIGO_540 = "Rejeição: CPF do Local de Retirada inválido";
    public static final String CODIGO_541 = "Rejeição: CPF do Local de Entrega inválido";
    public static final String CODIGO_542 = "Rejeição: CNPJ do Transportador inválido";
    public static final String CODIGO_543 = "Rejeição: CPF do Transportador inválido";
    public static final String CODIGO_544 = "Rejeição: Inscrição Estadual do Transportador inválida";
    public static final String CODIGO_545 = "Rejeição: Falha no schema XML ? versão informada na versaoDados do SOAPHeader diverge da versão da mensagem";
    public static final String CODIGO_546 = "Rejeição: Erro na Chave de Acesso ? Campo Id ? falta a literal NFe";
    public static final String CODIGO_547 = "Rejeição: Dígito Verificador da Chave de Acesso da NF-e Referenciada inválido";
    public static final String CODIGO_548 = "Rejeição: CNPJ da NF referenciada inválido.";
    public static final String CODIGO_549 = "Rejeição: CNPJ da NF referenciada de produtor inválido.";
    public static final String CODIGO_550 = "Rejeição: CPF da NF referenciada de produtor inválido.";
    public static final String CODIGO_551 = "Rejeição: Inscrição Estadual da NF referenciada de produtor inválido.";
    public static final String CODIGO_552 = "Rejeição: Dígito Verificador da Chave de Acesso do CT-e Referenciado inválido";
    public static final String CODIGO_553 = "Rejeição: Tipo autorizador do recibo diverge do Órgão Autorizador.";
    public static final String CODIGO_554 = "Rejeição: Série difere da faixa 0-899";
    public static final String CODIGO_555 = "Rejeição: Tipo autorizador do protocolo diverge do Órgão Autorizador.";
    public static final String CODIGO_556 = "Rejeição: Justificativa de entrada em contingência não deve ser informada para tipo de emissão normal.";
    public static final String CODIGO_557 = "Rejeição: A Justificativa de entrada em contingência deve ser informada.";
    public static final String CODIGO_558 = "Rejeição: Data de entrada em contingência posterior a data de emissão.";
    public static final String CODIGO_559 = "Rejeição: UF do Transportador não informada";
    public static final String CODIGO_560 = "Rejeição: CNPJ base do emitente difere do CNPJ base da primeira NF-e do lote recebido";
    public static final String CODIGO_561 = "Rejeição: Mês de Emissão informado na Chave de Acesso difere do Mês de Emissão da NFe";
    public static final String CODIGO_562 = "Rejeição: Código Numérico informado na Chave de Acesso difere do Código Numérico da NF-e";
    public static final String CODIGO_563 = "Rejeição: Já existe pedido de Inutilização com a mesma faixa de inutilização";
    public static final String CODIGO_564 = "Rejeição: Total do Produto / Serviço difere do somatório dos itens";
    public static final String CODIGO_565 = "Rejeição: Falha no schema XML ? inexiste a tag raiz esperada para o lote de NF-e";
    public static final String CODIGO_567 = "Rejeição: Falha no schema XML ? versão informada na versaoDados do SOAPHeader diverge da versão do lote de NF-e";
    public static final String CODIGO_568 = "Rejeição: Falha no schema XML ? inexiste atributo versao na tag raiz do lote de NF-e";
    public static final String CODIGO_573 = "Rejeição: Duplicaidade de Envento ( ex. Cancelar um nota ja cancelada na Sefaz )";
    public static final String CODIGO_574 = "Rejeição: O autor do evento diverge do emissor da NF-e";
    public static final String CODIGO_579 = "Rejeição: A data do evento não pode ser menor que a data de autorização para NF-e não emitida em contingência";
    public static final String CODIGO_578 = "Rejeição: A data do evento não pode ser maior que a data do processamento (Horário de verão)";
    public static final String CODIGO_577 = "Rejeição: A data do evento não pode ser menor que a data de emissão da NF-e(Aguarda no Minimo 5 min após emissão)";
    public static final String CODIGO_594 = "Rejeição: O número de sequencia do evento informado é maior que o permitido";
    public static final String CODIGO_610 = "Rejeição: Total da NF difere do somatório dos Valores compõe o valor total da NF";
    public static final String CODIGO_605 = "Rejeição: Total da NF difere do somatório dos itens sujeitos ao ISSQN e/ou Diferença no Calculo do ISSQN com o valor Total da NF";
    public static final String CODIGO_629 = "Rejeição: Valor do Produto diferente do produto Valor Unitário";
    public static final String CODIGO_685 = "Rejeição: Total do Valor Aproximado dos Tributos difere do somatório dos itens";
    public static final String CODIGO_703 = "Rejeição: Data-Hora de Emissão posterior ao horário de recebimento";
    public static final String CODIGO_805 = "Rejeição: A SEFAZ do destinatário não permite Contribuinte Isento de Inscrição Estadual";
    public static final String CODIGO_301 = "Uso Denegado: Irregularidade fiscal do emitente";
    public static final String CODIGO_999 = "Rejeição: Erro não catalogado";
    //Códigos Criados Internamente
    public static final String CODIGO_000 = "Erro Interno";
    public static final String CODIGO_001 = "Aguardando Envio";

    public static String MensagemRetorno(String codigo) {
        if (codigo.equals("000")) {
            return codigo + "-" + CODIGO_000;
        } else if (codigo.equals("001")) {
            return codigo + "-" + CODIGO_001;
        } else if (codigo.equals("100")) {
            return codigo + "-" + CODIGO_100;
        } else if (codigo.equals("101")) {
            return codigo + "-" + CODIGO_101;
        } else if (codigo.equals("102")) {
            return codigo + "-" + CODIGO_102;
        } else if (codigo.equals("103")) {
            return codigo + "-" + CODIGO_103;
        } else if (codigo.equals("104")) {
            return codigo + "-" + CODIGO_104;
        } else if (codigo.equals("105")) {
            return codigo + "-" + CODIGO_105;
        } else if (codigo.equals("106")) {
            return codigo + "-" + CODIGO_106;
        } else if (codigo.equals("107")) {
            return codigo + "-" + CODIGO_107;
        } else if (codigo.equals("108")) {
            return codigo + "-" + CODIGO_108;
        } else if (codigo.equals("109")) {
            return codigo + "-" + CODIGO_109;
        } else if (codigo.equals("110")) {
            return codigo + "-" + CODIGO_110;
        } else if (codigo.equals("111")) {
            return codigo + "-" + CODIGO_111;
        } else if (codigo.equals("112")) {
            return codigo + "-" + CODIGO_112;
        } else if (codigo.equals("135")) {
            return codigo + "-" + CODIGO_135;
        } else if (codigo.equals("201")) {
            return codigo + "-" + CODIGO_201;
        } else if (codigo.equals("202")) {
            return codigo + "-" + CODIGO_202;
        } else if (codigo.equals("203")) {
            return codigo + "-" + CODIGO_203;
        } else if (codigo.equals("204")) {
            return codigo + "-" + CODIGO_204;
        } else if (codigo.equals("205")) {
            return codigo + "-" + CODIGO_205;
        } else if (codigo.equals("206")) {
            return codigo + "-" + CODIGO_206;
        } else if (codigo.equals("207")) {
            return codigo + "-" + CODIGO_207;
        } else if (codigo.equals("208")) {
            return codigo + "-" + CODIGO_208;
        } else if (codigo.equals("209")) {
            return codigo + "-" + CODIGO_209;
        } else if (codigo.equals("210")) {
            return codigo + "-" + CODIGO_210;
        } else if (codigo.equals("211")) {
            return codigo + "-" + CODIGO_211;
        } else if (codigo.equals("212")) {
            return codigo + "-" + CODIGO_212;
        } else if (codigo.equals("213")) {
            return codigo + "-" + CODIGO_213;
        } else if (codigo.equals("214")) {
            return codigo + "-" + CODIGO_214;
        } else if (codigo.equals("215")) {
            return codigo + "-" + CODIGO_215;
        } else if (codigo.equals("216")) {
            return codigo + "-" + CODIGO_216;
        } else if (codigo.equals("217")) {
            return codigo + "-" + CODIGO_217;
        } else if (codigo.equals("218")) {
            return codigo + "-" + CODIGO_218;
        } else if (codigo.equals("219")) {
            return codigo + "-" + CODIGO_219;
        } else if (codigo.equals("220")) {
            return codigo + "-" + CODIGO_220;
        } else if (codigo.equals("221")) {
            return codigo + "-" + CODIGO_221;
        } else if (codigo.equals("222")) {
            return codigo + "-" + CODIGO_222;
        } else if (codigo.equals("223")) {
            return codigo + "-" + CODIGO_223;
        } else if (codigo.equals("224")) {
            return codigo + "-" + CODIGO_224;
        } else if (codigo.equals("225")) {
            return codigo + "-" + CODIGO_225;
        } else if (codigo.equals("226")) {
            return codigo + "-" + CODIGO_226;
        } else if (codigo.equals("227")) {
            return codigo + "-" + CODIGO_227;
        } else if (codigo.equals("228")) {
            return codigo + "-" + CODIGO_228;
        } else if (codigo.equals("229")) {
            return codigo + "-" + CODIGO_229;
        } else if (codigo.equals("230")) {
            return codigo + "-" + CODIGO_230;
        } else if (codigo.equals("231")) {
            return codigo + "-" + CODIGO_231;
        } else if (codigo.equals("232")) {
            return codigo + "-" + CODIGO_232;
        } else if (codigo.equals("233")) {
            return codigo + "-" + CODIGO_233;
        } else if (codigo.equals("234")) {
            return codigo + "-" + CODIGO_234;
        } else if (codigo.equals("235")) {
            return codigo + "-" + CODIGO_235;
        } else if (codigo.equals("236")) {
            return codigo + "-" + CODIGO_236;
        } else if (codigo.equals("237")) {
            return codigo + "-" + CODIGO_237;
        } else if (codigo.equals("238")) {
            return codigo + "-" + CODIGO_238;
        } else if (codigo.equals("239")) {
            return codigo + "-" + CODIGO_239;
        } else if (codigo.equals("240")) {
            return codigo + "-" + CODIGO_240;
        } else if (codigo.equals("241")) {
            return codigo + "-" + CODIGO_241;
        } else if (codigo.equals("242")) {
            return codigo + "-" + CODIGO_242;
        } else if (codigo.equals("243")) {
            return codigo + "-" + CODIGO_243;
        } else if (codigo.equals("244")) {
            return codigo + "-" + CODIGO_244;
        } else if (codigo.equals("245")) {
            return codigo + "-" + CODIGO_245;
        } else if (codigo.equals("246")) {
            return codigo + "-" + CODIGO_246;
        } else if (codigo.equals("247")) {
            return codigo + "-" + CODIGO_247;
        } else if (codigo.equals("248")) {
            return codigo + "-" + CODIGO_248;
        } else if (codigo.equals("249")) {
            return codigo + "-" + CODIGO_249;
        } else if (codigo.equals("250")) {
            return codigo + "-" + CODIGO_250;
        } else if (codigo.equals("251")) {
            return codigo + "-" + CODIGO_251;
        } else if (codigo.equals("252")) {
            return codigo + "-" + CODIGO_252;
        } else if (codigo.equals("253")) {
            return codigo + "-" + CODIGO_253;
        } else if (codigo.equals("254")) {
            return codigo + "-" + CODIGO_254;
        } else if (codigo.equals("255")) {
            return codigo + "-" + CODIGO_255;
        } else if (codigo.equals("256")) {
            return codigo + "-" + CODIGO_256;
        } else if (codigo.equals("257")) {
            return codigo + "-" + CODIGO_257;
        } else if (codigo.equals("258")) {
            return codigo + "-" + CODIGO_258;
        } else if (codigo.equals("259")) {
            return codigo + "-" + CODIGO_259;
        } else if (codigo.equals("260")) {
            return codigo + "-" + CODIGO_260;
        } else if (codigo.equals("261")) {
            return codigo + "-" + CODIGO_261;
        } else if (codigo.equals("262")) {
            return codigo + "-" + CODIGO_262;
        } else if (codigo.equals("263")) {
            return codigo + "-" + CODIGO_263;
        } else if (codigo.equals("264")) {
            return codigo + "-" + CODIGO_264;
        } else if (codigo.equals("265")) {
            return codigo + "-" + CODIGO_265;
        } else if (codigo.equals("266")) {
            return codigo + "-" + CODIGO_266;
        } else if (codigo.equals("267")) {
            return codigo + "-" + CODIGO_267;
        } else if (codigo.equals("268")) {
            return codigo + "-" + CODIGO_268;
        } else if (codigo.equals("269")) {
            return codigo + "-" + CODIGO_269;
        } else if (codigo.equals("270")) {
            return codigo + "-" + CODIGO_270;
        } else if (codigo.equals("271")) {
            return codigo + "-" + CODIGO_271;
        } else if (codigo.equals("272")) {
            return codigo + "-" + CODIGO_272;
        } else if (codigo.equals("273")) {
            return codigo + "-" + CODIGO_273;
        } else if (codigo.equals("274")) {
            return codigo + "-" + CODIGO_274;
        } else if (codigo.equals("275")) {
            return codigo + "-" + CODIGO_275;
        } else if (codigo.equals("276")) {
            return codigo + "-" + CODIGO_276;
        } else if (codigo.equals("277")) {
            return codigo + "-" + CODIGO_277;
        } else if (codigo.equals("278")) {
            return codigo + "-" + CODIGO_278;
        } else if (codigo.equals("279")) {
            return codigo + "-" + CODIGO_279;
        } else if (codigo.equals("280")) {
            return codigo + "-" + CODIGO_280;
        } else if (codigo.equals("281")) {
            return codigo + "-" + CODIGO_281;
        } else if (codigo.equals("282")) {
            return codigo + "-" + CODIGO_282;
        } else if (codigo.equals("283")) {
            return codigo + "-" + CODIGO_283;
        } else if (codigo.equals("284")) {
            return codigo + "-" + CODIGO_284;
        } else if (codigo.equals("285")) {
            return codigo + "-" + CODIGO_285;
        } else if (codigo.equals("286")) {
            return codigo + "-" + CODIGO_286;
        } else if (codigo.equals("287")) {
            return codigo + "-" + CODIGO_287;
        } else if (codigo.equals("288")) {
            return codigo + "-" + CODIGO_288;
        } else if (codigo.equals("289")) {
            return codigo + "-" + CODIGO_289;
        } else if (codigo.equals("290")) {
            return codigo + "-" + CODIGO_290;
        } else if (codigo.equals("291")) {
            return codigo + "-" + CODIGO_291;
        } else if (codigo.equals("292")) {
            return codigo + "-" + CODIGO_292;
        } else if (codigo.equals("293")) {
            return codigo + "-" + CODIGO_293;
        } else if (codigo.equals("294")) {
            return codigo + "-" + CODIGO_294;
        } else if (codigo.equals("295")) {
            return codigo + "-" + CODIGO_295;
        } else if (codigo.equals("296")) {
            return codigo + "-" + CODIGO_296;
        } else if (codigo.equals("297")) {
            return codigo + "-" + CODIGO_297;
        } else if (codigo.equals("298")) {
            return codigo + "-" + CODIGO_298;
        } else if (codigo.equals("299")) {
            return codigo + "-" + CODIGO_299;
        } else if (codigo.equals("401")) {
            return codigo + "-" + CODIGO_401;
        } else if (codigo.equals("402")) {
            return codigo + "-" + CODIGO_402;
        } else if (codigo.equals("403")) {
            return codigo + "-" + CODIGO_403;
        } else if (codigo.equals("404")) {
            return codigo + "-" + CODIGO_404;
        } else if (codigo.equals("405")) {
            return codigo + "-" + CODIGO_405;
        } else if (codigo.equals("406")) {
            return codigo + "-" + CODIGO_406;
        } else if (codigo.equals("407")) {
            return codigo + "-" + CODIGO_407;
        } else if (codigo.equals("409")) {
            return codigo + "-" + CODIGO_409;
        } else if (codigo.equals("410")) {
            return codigo + "-" + CODIGO_410;
        } else if (codigo.equals("411")) {
            return codigo + "-" + CODIGO_411;
        } else if (codigo.equals("420")) {
            return codigo + "-" + CODIGO_420;
        } else if (codigo.equals("450")) {
            return codigo + "-" + CODIGO_450;
        } else if (codigo.equals("451")) {
            return codigo + "-" + CODIGO_451;
        } else if (codigo.equals("452")) {
            return codigo + "-" + CODIGO_452;
        } else if (codigo.equals("453")) {
            return codigo + "-" + CODIGO_453;
        } else if (codigo.equals("454")) {
            return codigo + "-" + CODIGO_454;
        } else if (codigo.equals("478")) {
            return codigo + "-" + CODIGO_478;
        } else if (codigo.equals("501")) {
            return codigo + "-" + CODIGO_501;
        } else if (codigo.equals("502")) {
            return codigo + "-" + CODIGO_502;
        } else if (codigo.equals("503")) {
            return codigo + "-" + CODIGO_503;
        } else if (codigo.equals("504")) {
            return codigo + "-" + CODIGO_504;
        } else if (codigo.equals("505")) {
            return codigo + "-" + CODIGO_505;
        } else if (codigo.equals("506")) {
            return codigo + "-" + CODIGO_506;
        } else if (codigo.equals("507")) {
            return codigo + "-" + CODIGO_507;
        } else if (codigo.equals("508")) {
            return codigo + "-" + CODIGO_508;
        } else if (codigo.equals("509")) {
            return codigo + "-" + CODIGO_509;
        } else if (codigo.equals("510")) {
            return codigo + "-" + CODIGO_510;
        } else if (codigo.equals("511")) {
            return codigo + "-" + CODIGO_511;
        } else if (codigo.equals("512")) {
            return codigo + "-" + CODIGO_512;
        } else if (codigo.equals("513")) {
            return codigo + "-" + CODIGO_513;
        } else if (codigo.equals("514")) {
            return codigo + "-" + CODIGO_514;
        } else if (codigo.equals("515")) {
            return codigo + "-" + CODIGO_515;
        } else if (codigo.equals("516")) {
            return codigo + "-" + CODIGO_516;
        } else if (codigo.equals("517")) {
            return codigo + "-" + CODIGO_517;
        } else if (codigo.equals("518")) {
            return codigo + "-" + CODIGO_518;
        } else if (codigo.equals("519")) {
            return codigo + "-" + CODIGO_519;
        } else if (codigo.equals("520")) {
            return codigo + "-" + CODIGO_520;
        } else if (codigo.equals("521")) {
            return codigo + "-" + CODIGO_521;
        } else if (codigo.equals("522")) {
            return codigo + "-" + CODIGO_522;
        } else if (codigo.equals("523")) {
            return codigo + "-" + CODIGO_523;
        } else if (codigo.equals("524")) {
            return codigo + "-" + CODIGO_524;
        } else if (codigo.equals("525")) {
            return codigo + "-" + CODIGO_525;
        } else if (codigo.equals("526")) {
            return codigo + "-" + CODIGO_526;
        } else if (codigo.equals("527")) {
            return codigo + "-" + CODIGO_527;
        } else if (codigo.equals("528")) {
            return codigo + "-" + CODIGO_528;
        } else if (codigo.equals("529")) {
            return codigo + "-" + CODIGO_529;
        } else if (codigo.equals("530")) {
            return codigo + "-" + CODIGO_530;
        } else if (codigo.equals("531")) {
            return codigo + "-" + CODIGO_531;
        } else if (codigo.equals("532")) {
            return codigo + "-" + CODIGO_532;
        } else if (codigo.equals("533")) {
            return codigo + "-" + CODIGO_533;
        } else if (codigo.equals("534")) {
            return codigo + "-" + CODIGO_534;
        } else if (codigo.equals("535")) {
            return codigo + "-" + CODIGO_535;
        } else if (codigo.equals("536")) {
            return codigo + "-" + CODIGO_536;
        } else if (codigo.equals("537")) {
            return codigo + "-" + CODIGO_537;
        } else if (codigo.equals("538")) {
            return codigo + "-" + CODIGO_538;
        } else if (codigo.equals("539")) {
            return codigo + "-" + CODIGO_539;
        } else if (codigo.equals("540")) {
            return codigo + "-" + CODIGO_540;
        } else if (codigo.equals("541")) {
            return codigo + "-" + CODIGO_541;
        } else if (codigo.equals("542")) {
            return codigo + "-" + CODIGO_542;
        } else if (codigo.equals("543")) {
            return codigo + "-" + CODIGO_543;
        } else if (codigo.equals("544")) {
            return codigo + "-" + CODIGO_544;
        } else if (codigo.equals("545")) {
            return codigo + "-" + CODIGO_545;
        } else if (codigo.equals("546")) {
            return codigo + "-" + CODIGO_546;
        } else if (codigo.equals("547")) {
            return codigo + "-" + CODIGO_547;
        } else if (codigo.equals("548")) {
            return codigo + "-" + CODIGO_548;
        } else if (codigo.equals("549")) {
            return codigo + "-" + CODIGO_549;
        } else if (codigo.equals("550")) {
            return codigo + "-" + CODIGO_550;
        } else if (codigo.equals("551")) {
            return codigo + "-" + CODIGO_551;
        } else if (codigo.equals("552")) {
            return codigo + "-" + CODIGO_552;
        } else if (codigo.equals("553")) {
            return codigo + "-" + CODIGO_553;
        } else if (codigo.equals("554")) {
            return codigo + "-" + CODIGO_554;
        } else if (codigo.equals("555")) {
            return codigo + "-" + CODIGO_555;
        } else if (codigo.equals("556")) {
            return codigo + "-" + CODIGO_556;
        } else if (codigo.equals("557")) {
            return codigo + "-" + CODIGO_557;
        } else if (codigo.equals("558")) {
            return codigo + "-" + CODIGO_558;
        } else if (codigo.equals("559")) {
            return codigo + "-" + CODIGO_559;
        } else if (codigo.equals("560")) {
            return codigo + "-" + CODIGO_560;
        } else if (codigo.equals("561")) {
            return codigo + "-" + CODIGO_561;
        } else if (codigo.equals("562")) {
            return codigo + "-" + CODIGO_562;
        } else if (codigo.equals("563")) {
            return codigo + "-" + CODIGO_563;
        } else if (codigo.equals("564")) {
            return codigo + "-" + CODIGO_564;
        } else if (codigo.equals("565")) {
            return codigo + "-" + CODIGO_565;
        } else if (codigo.equals("567")) {
            return codigo + "-" + CODIGO_567;
        } else if (codigo.equals("568")) {
            return codigo + "-" + CODIGO_568;
        } else if (codigo.equals("573")) {
            return codigo + "-" + CODIGO_573;
        } else if (codigo.equals("579")) {
            return codigo + "-" + CODIGO_579;
        } else if (codigo.equals("301")) {
            return codigo + "-" + CODIGO_301;
        } else if (codigo.equals("594")) {
            return codigo + "-" + CODIGO_594;
        } else if (codigo.equals("610")) {
            return codigo + "-" + CODIGO_610;
        } else if (codigo.equals("629")) {
            return codigo + "-" + CODIGO_629;
        } else if (codigo.equals("574")) {
            return codigo + "-" + CODIGO_574;
        } else if (codigo.equals("577")) {
            return codigo + "-" + CODIGO_577;
        } else if (codigo.equals("578")) {
            return codigo + "-" + CODIGO_578;
        } else if (codigo.equals("605")) {
            return codigo + "-" + CODIGO_605;
        } else if (codigo.equals("685")) {
            return codigo + "-" + CODIGO_685;
        } else if (codigo.equals("703")) {
            return codigo + "-" + CODIGO_703;
        } else if (codigo.equals("805")) {
            return codigo + "-" + CODIGO_805;
        } 
        else if (codigo.equals("999")) {
            return codigo + "-" + CODIGO_999;
        }
        return codigo + "-" + "Código não reconhecido.";
    }

    public static String IconeRetorno(String codigo) {
        if (codigo.equals("001") ||
                codigo.equals("105")) {
            return "iconeAmarelo.png";
        } else if (codigo.equals("100") ||
                codigo.equals("101") ||
                codigo.equals("102") ||
                codigo.equals("103") ||
                codigo.equals("104") ||
                codigo.equals("107") ||
                codigo.equals("111") ||
                codigo.equals("112")) {
            return "iconeVerde.png";
        } else if (codigo.equals("106") ||
                codigo.equals("108") ||
                codigo.equals("109") ||
                codigo.equals("110") ||
                codigo.equals("301") ||
                codigo.equals("999") ||
                codigo.equals("000") ||
                ComecaCom("2", codigo) ||
                ComecaCom("4", codigo) ||
                ComecaCom("5", codigo)) {
            return "iconeVermelho.png";
        }
        return "";
    }

    public static Boolean ComecaCom(String inicio, String texto) {
        if (texto.substring(0, 1).equals(inicio)) {
            return true;
        }
        return false;
    }
}
