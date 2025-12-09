package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;

/**
 * Reponsável por manter os dados da entidade Convenio. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ConvenioVO extends SuperVO implements Comparable<ConvenioVO> {
	
	private String tipoFinanciamentoEstudantil;
    private Integer codigo;
    private String descricao;
    private Date dataAssinatura;
    private String cobertura;
    private String preRequisitos;
    private Date dataInicioVigencia;
    private Date dataFinalVigencia;
    private Double descontoMatricula;
    private String tipoDescontoMatricula;
    private Boolean abaterDescontoNoValorMatricula;
    private Double descontoParcela;
    private String tipoDescontoParcela;
    private Boolean abaterDescontoNoValorParcela;
    private Double bolsaCusteadaParceiroMatricula;
    private String tipoBolsaCusteadaParceiroMatricula;
    private Double bolsaCusteadaParceiroParcela;
    private String tipoBolsaCusteadaParceiroParcela;
    private FormaPagamentoVO formaRecebimentoParceiro;
    private Integer diaBaseRecebimentoParceiro;
    private Date dataRequisicao;
    private Date dataAutorizacao;
    private Date dataFinalizacao;
    private String situacao;
    private Boolean validoParaTodoCurso;
    private Boolean validoParaTodaUnidadeEnsino;
    private Boolean validoParaTodoTurno;
    private Boolean periodoIndeterminado;
    private Boolean ativo;
    private Date dataAtivacao;
    private UsuarioVO responsavelAtivacao;
    private Date dataInativacao;
    private UsuarioVO responsavelInativacao;
    private Boolean convenioInadimplenteBloqueaMatricula;
    private Boolean convenioTipoPadrao;
    
    /**
     * Ao marcar esta opcao o sistema irá gerar as parcelas
     * vencidas do convenio, já com a data de vencimento atualizada para o mês
     * corrente. O sistema irá automaticamente,
     * alinhar a data de vencimento da parcela do convenio, com
     * o primeiro mês válido (que ainda não tenha passado a data).
     * Isto é importante por questões contábeis. Pois, na medida 
     * que uma IE fecha sua contabilidade por competência, então
     * o sistema não poderá gerar uma parcela do convênio para competências
     * já fechadas (meses passados). 
     */
    private Boolean gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual;
    
    /**
     * Ao marcar esta opção o sistema irá gerar uma conta a pagar, 
     * para restituir o aluno do valor que já foi pago por ele, 
     * relativo ao convênio, mas que a IE irá receber novamente por
     * meio da conveniada. Ou seja, esta opção deverá ser marcada
     * para que o sistema gere uma conta a pagar para o aluno
     * (gerando assim um crédito para ele junto à instituição) para
     * restituí-lo do valor pago por ele, uma vez, que este valor
     * será recebido pela instituição da conveniada. Esta conta a pagar
     * terá como vencimento a mesma data do vencimento da primeira
     * parcela do convênio (não vencida).
     */
    private Boolean gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio;
    
    private CategoriaDespesaVO categoriaDespesaRestituicaoConvenio;
    
    private CentroReceitaVO centroReceitaMatricula;
    
    private CentroReceitaVO centroReceitaMensalidade;
    
    /**
     * Atributo utilizado no relatório ListagemDescontosAlunos
     */
    private Double valorTotalDescontoMatriculaPorConvenioCalculado;
    private Double valorTotalDescontoParcelaPorConvenioCalculado;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ConvenioUnidadeEnsino</code>.
     */
    private List convenioUnidadeEnsinoVOs;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ConvenioTurno</code>.
     */
    private List convenioTurnoVOs;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ConvenioCurso</code>.
     */
    private List convenioCursoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Parceiro </code>.
     */
    private ParceiroVO parceiro;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private FuncionarioVO requisitante;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO responsavelAutorizacao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO responsavelFinalizacao;
    private Boolean possuiDescontoAntecipacao;
    private DescontoProgressivoVO descontoProgressivoParceiro;
    private DescontoProgressivoVO descontoProgressivoAluno;
    /**
     * Indica se o convênio sempre será calculado sobre o valor líquido da conta
     * a receber (o contrário da opção calcular sobre valor cheio).
     */
    private Boolean calculadoEmCimaValorLiquido;
    private Boolean aplicarDescontoProgressivoMatricula;
    private Boolean aplicarDescontoProgressivoMatriculaParceiro;
    
    
    /**
     * Esta variável só poderá ser marcada, quando o usuário indicar que o convênio não é 
     * calculado sobre o valor cheio (atributo acima). Neste caso, temos que determinar
     * se o Convênio será sempre calculado sobre a Base de Cálculo Inicial do Convênio, 
     * ou se o mesmo será aplicado sobre esta Base de Cálculo Inicial, 
     * já dezudido outros convênios (um aluno pode ter mais de um convênio), que já tenham sido calculados
     * conforme a ordem de prioridade de aplicação (ver atributo abaixo - ordemPrioridadeParaCalculo).
     * Exemplo (A): aplicarSobreValorCheio (true) aplicarSobreValorBaseDeduzidoValorOutrosConvenios (false)
     *              Ordem de aplicação dos descontos: progressivo -> convenio
     *                Valor da Parcela: R$500,00
     *                Valor Desc. Progressivo (Ordem Aplicação 1): R$ 50,00 (10%)
     *                Valor Convênio A (Ordem Prioridade Calculo 1): R$ 100,00 (20% sobre R$ 500,00)
     *                Valor Convênio B (Ordem Prioridade Calculo 2): R$ 100,00 (20% sobre R$ 500,00)
     *                     * neste caso nem a ordem dos descontos (convenio, progressivo, institucional) nem
     *                        a ordem de prioridade de cálculo do convênio em si fazem diferença para
     *                        o cálculo do valor de cada convênio, pois ambos são sobre o valor cheio da 
     *                        conta a receber
     *         
     * Exemplo (B): aplicarSobreValorCheio (false) aplicarSobreValorBaseDeduzidoValorOutrosConvenios (false)
     *              Ordem de aplicação dos descontos: progressivo -> convenio
     *                Valor da Parcela: R$500,00
     *                Valor Desc. Progressivo (Ordem Aplicação 1): R$ 50,00 (10% sobre R$ 500,00)
     *                Valor Convênio A (Ordem Prioridade Calculo 1): R$ 90,00 (20% sobre R$ 450,00)
     *                Valor Convênio B (Ordem Prioridade Calculo 2): R$ 90,00 (20% sobre R$ 450,00)
     *                     * neste caso a ordem dos descontos (convenio, progressivo, institucional) influência
     *                        no cálculo pois o desconto progressivo deve ser aplicado primeiro. Já ordem
     *                        de prioridade de cálculo do convênio não impacta no cálculo, pois 
     *                        como a opção aplicarSobreValorBaseDeduzidoValorOutrosConvenios está falso
     *                        para ambos os planos, os dois serão aplicados sobre o valor base inicial de 
     *                        convênio.
     * 
     * Exemplo (C): aplicarSobreValorCheio (false) aplicarSobreValorBaseDeduzidoValorOutrosConvênios (true) 
     *              Ordem de aplicação dos descontos: progressivo -> Convênio
     *                Valor da Parcela: R$500,00
     *                Valor Desc. Progressivo (Ordem Aplicação 1): R$ 50,00 (10% sobre R$ 500,00)
     *                Valor Convênio A (Ordem Prioridade Calculo 1): R$ 90,00 (20% sobre R$ 450,00)
     *                Valor Convênio B (Ordem Prioridade Calculo 2): R$ 72,00 (20% sobre R$ 360,00)
     *                     * neste caso a ordem dos descontos (convenio, progressivo, institucional) influência
     *                        no cálculo pois o desconto progressivo deve ser aplicado primeiro. O mesmo ocorre com a 
     *                        ordem de prioridade de cálculo dos convênios (ou seja, impacta no cálculo), pois 
     *                        como a opção aplicarSobreValorBaseDeduzidoValorOutrosConvenios está (true)
     *                        para ambos os convênios, o convênio de prioridade 1 será aplicado primeiro e o 
     *                        convênio de prioridade 2 será aplicado posteriormente, com o valor do primeiro 
     *                        sendo abatido da base inicial de cálculo.
     * 
     */
    private Boolean aplicarSobreValorBaseDeduzidoValorOutrosConvenios; 
    
    /**
     * Ordem de Prioridade de Cálculo define uma ordem na aplicação de um conjunto
     * de convênios. Ou seja, caso seja adicionado mais de um convênio
     * para um aluno, o SEI utilizará esta variável para determinar qual convênio deverá
     * ser aplicado primeiramente e qual deverá ser processado depois.
     */
    private Integer ordemPrioridadeParaCalculo;
    
    private Boolean removerDescontoRenovacao;
    
    public static final long serialVersionUID = 1L;

    public ConvenioVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ConvenioVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ConvenioVO obj) throws ConsistirException {
    	 if (!obj.isValidarDados().booleanValue()) {
             return;
         }
         if (obj.getDescricao().equals("")) {
             throw new ConsistirException("O campo DESCRIÇÃO (Convênio) deve ser informado.");
         }

         if ((obj.getParceiro() == null) || (obj.getParceiro().getCodigo().intValue() == 0)) {
             throw new ConsistirException("O campo PARCEIRO (Convênio) deve ser informado.");
         }

         if (obj.getCobertura() == null || obj.getCobertura().equals("0")) {
             throw new ConsistirException("O campo COBERTURA (Convênio) deve ser informado.");
         }
//         if ((obj.getRequisitante() == null) || (obj.getRequisitante().getCodigo().intValue() == 0)) {
//             throw new ConsistirException("O campo REQUISITANTE (Convênio) deve ser informado.");
//         }
//         if (obj.getPreRequisitos().equals("")) {
//             throw new ConsistirException("O campo PRÉ-REQUISITOS (Convênio) deve ser informado.");
//         }
         if (obj.getBolsaCusteadaParceiroParcela() == 0.0
                 && (obj.getFormaRecebimentoParceiro().getCodigo().intValue() == 0 || obj.getFormaRecebimentoParceiro().getCodigo() == null)) {
             throw new ConsistirException("O campo BOLSA CUSTEADA PARCEIRO PARA PARCELA (Convênio) deve ser informado.");
         }
         if (obj.getBolsaCusteadaParceiroParcela() != 0.0 && obj.getFormaRecebimentoParceiro().getCodigo() == 0) {
             throw new ConsistirException("O campo FORMA RECEBIMENTO PARCEIRO (Convênio) deve ser informado.");
         }
         if (obj.getBolsaCusteadaParceiroMatricula() != 0.0 && obj.getFormaRecebimentoParceiro().getCodigo() == 0) {
             throw new ConsistirException("O campo FORMA RECEBIMENTO PARCEIRO (Convênio) deve ser informado.");
         }
         if (obj.getBolsaCusteadaParceiroMatricula() == 0.0
                 && (obj.getFormaRecebimentoParceiro().getCodigo() == 0 || obj.getFormaRecebimentoParceiro().getCodigo() == null)) {
             throw new ConsistirException("O campo BOLSA CUSTEADA PARCEIRO PARA MATRÍCULA (Convênio) deve ser informado.");
         }




         if (obj.getDataAssinatura() == null) {
             throw new ConsistirException("O campo DATA ASSINATURA (Convênio) deve ser informado.");
         }

         if (!obj.getTipoDescontoParcela().equals(obj.getTipoBolsaCusteadaParceiroParcela())) {
             throw new ConsistirException("O campo TIPO DESCONTO em DESCONTO PARCELA deve ser igual ao campo TIPO BOLSA em BOLSA CUSTEADA PARCEIRO PARA PARCELA");
         }
         if (!obj.getTipoDescontoMatricula().equals(obj.getTipoBolsaCusteadaParceiroMatricula())) {
             throw new ConsistirException("O campo TIPO DESCONTO em DESCONTO MATRíCULA deve ser igual ao campo TIPO BOLSA em BOLSA CUSTEADA PARCEIRO PARA MATRíCULA");
         }
         if (obj.getDescontoParcela() != 0.0 && obj.getTipoDescontoParcela().equals("")) {
             throw new ConsistirException("O campo TIPO DESCONTO PARCELA deve ser informado.");
         }
         if (obj.getDescontoMatricula() != 0.0 && obj.getTipoDescontoMatricula().equals("")) {
             throw new ConsistirException("O campo TIPO DESCONTO  MATRÍCULA deve ser informado.");
         }
         if ((obj.getDataInicioVigencia() != null && obj.getDataFinalVigencia() != null)
                 && (obj.getDataInicioVigencia().after(obj.getDataFinalVigencia()))) {
             throw new ConsistirException("O campo DATA FINAL VIGÊNCIA (Convênio) deve ser posterior da DATA INÍCIO VIGÊNCIA.");
         }

         if (obj.getBolsaCusteadaParceiroMatricula() != 0.0 && obj.getTipoBolsaCusteadaParceiroMatricula().equals("")) {
             throw new ConsistirException("O campo TIPO BOLSA  MATRÍCULA (Convênio) deve ser informado.");
         }
         if (obj.getBolsaCusteadaParceiroParcela() != 0.0 && obj.getTipoBolsaCusteadaParceiroParcela().equals("")) {
             throw new ConsistirException("O campo TIPO BOLSA PARCELA (Convênio) deve ser informado.");
         }
         if (obj.getTipoDescontoParcela().equals("PE") && obj.getDescontoParcela() > 100.0) {
             throw new ConsistirException("O campo DESCONTO PARCELA tem que ser menor ou igual 100 quando o campo TIPO DESCONTO for percentual");
         }
         if (obj.getTipoDescontoMatricula().equals("PE") && obj.getDescontoMatricula() > 100.0) {
             throw new ConsistirException("O campo DESCONTO MATRÍCULA tem que ser menor ou igual 100 quando o campo TIPO DESCONTO for percentual");
         }
         if ((obj.getTipoDescontoParcela().equals("VA") && obj.getTipoBolsaCusteadaParceiroParcela().equals("VA"))
                 && (obj.getBolsaCusteadaParceiroParcela() > obj.getDescontoParcela())) {
             throw new ConsistirException("O campo BOLSA CUSTEADA PARCEIRO PARA PARCELA tem que ser menor ou igual ao valor DESCONTO PARCELA");
         }
         if ((obj.getTipoDescontoMatricula().equals("VA") && obj.getTipoBolsaCusteadaParceiroMatricula().equals("VA"))
                 && (obj.getBolsaCusteadaParceiroMatricula() > obj.getDescontoMatricula())) {
             throw new ConsistirException("O campo BOLSA CUSTEADA PARCEIRO PARA MATRÍCULA tem que ser menor ou igual ao valor DESCONTO MATRÍCULA");
         }
         if ((obj.getTipoDescontoParcela().equals("PE") && obj.getTipoBolsaCusteadaParceiroParcela().equals("PE"))
                 && (obj.getBolsaCusteadaParceiroParcela() > obj.getDescontoParcela())) {
             throw new ConsistirException("O campo BOLSA CUSTEADA PARCEIRO PARA PARCELA tem que ser menor ou igual ao valor DESCONTO PARCELA");
         }
         if ((obj.getTipoDescontoMatricula().equals("PE") && obj.getTipoBolsaCusteadaParceiroMatricula().equals("PE"))
                 && (obj.getBolsaCusteadaParceiroMatricula() > obj.getDescontoMatricula())) {
             throw new ConsistirException("O campo BOLSA CUSTEADA PARCEIRO PARA MATRÍCULA tem que ser menor ou igual ao valor DESCONTO MATRÍCULA");
         }
         if ((obj.getBolsaCusteadaParceiroMatricula() != 0.0 || obj.getBolsaCusteadaParceiroParcela() != 0.0)
                 && obj.getDiaBaseRecebimentoParceiro() == 0) {
             throw new ConsistirException("O campo DIA BASE RECEBIMENTO PARCEIRO (Convênio) deve ser informado.");
         }

         if (obj.getSituacao().equals("")) {
             throw new ConsistirException("O campo SITUAÇÃO (Convênio) deve ser informado.");
         }
         if (obj.getValidoParaTodaUnidadeEnsino()) {
             obj.setConvenioUnidadeEnsinoVOs(new ArrayList());
         } else if (obj.convenioUnidadeEnsinoVOs == null || obj.convenioUnidadeEnsinoVOs.isEmpty()) {
             throw new ConsistirException("O campo CONVENIO UNIDADE DE ENSINO deve ter pelo menos uma Unidade de Ensino informado");
         }
         if (obj.getValidoParaTodoCurso()) {
             obj.setConvenioCursoVOs(new ArrayList());
         } else if (obj.convenioCursoVOs == null || obj.convenioCursoVOs.isEmpty()) {
             throw new ConsistirException("O campo COVENIO CURSO deve ter pelo menos um Curso informado");
         }
         if (obj.getValidoParaTodoTurno()) {
             obj.setConvenioTurnoVOs(new ArrayList());
         } else if (obj.convenioTurnoVOs == null || obj.convenioTurnoVOs.isEmpty()) {
             throw new ConsistirException("O campo CONVENIO TURNO deve ter pelo menos um Turno informado");
         }
         if(obj.getTipoFinanciamentoEstudantil().isEmpty()){
         	throw new ConsistirException("O campo TIPO FINANCIAMENTO deve ser informado");
         }    
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ConvenioCursoVO</code> ao List <code>convenioCursoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ConvenioCurso</code> - getCurso().getCodigo() - como identificador
     * (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ConvenioCursoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjConvenioCursoVOs(ConvenioCursoVO obj) throws Exception {
        ConvenioCursoVO.validarDados(obj);
        int index = 0;
        Iterator i = getConvenioCursoVOs().iterator();
        while (i.hasNext()) {
            ConvenioCursoVO objExistente = (ConvenioCursoVO) i.next();
            if (objExistente.getCurso().getCodigo().equals(obj.getCurso().getCodigo())) {
                getConvenioCursoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getConvenioCursoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ConvenioCursoVO</code> no List <code>convenioCursoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ConvenioCurso</code> - getCurso().getCodigo() - como identificador
     * (key) do objeto no List.
     *
     * @param curso
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjConvenioCursoVOs(Integer curso) throws Exception {
        int index = 0;
        Iterator i = getConvenioCursoVOs().iterator();
        while (i.hasNext()) {
            ConvenioCursoVO objExistente = (ConvenioCursoVO) i.next();
            if (objExistente.getCurso().getCodigo().equals(curso)) {
                getConvenioCursoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ConvenioCursoVO</code> no List <code>convenioCursoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ConvenioCurso</code> - getCurso().getCodigo() - como identificador
     * (key) do objeto no List.
     *
     * @param curso
     *            Parâmetro para localizar o objeto do List.
     */
    public ConvenioCursoVO consultarObjConvenioCursoVO(Integer curso) throws Exception {
        Iterator i = getConvenioCursoVOs().iterator();
        while (i.hasNext()) {
            ConvenioCursoVO objExistente = (ConvenioCursoVO) i.next();
            if (objExistente.getCurso().getCodigo().equals(curso)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ConvenioTurnoVO</code> ao List <code>convenioTurnoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ConvenioTurno</code> - getTurno().getCodigo() - como identificador
     * (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ConvenioTurnoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjConvenioTurnoVOs(ConvenioTurnoVO obj) throws Exception {
        ConvenioTurnoVO.validarDados(obj);
        int index = 0;
        Iterator i = getConvenioTurnoVOs().iterator();
        while (i.hasNext()) {
            ConvenioTurnoVO objExistente = (ConvenioTurnoVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(obj.getTurno().getCodigo())) {
                getConvenioTurnoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getConvenioTurnoVOs().add(obj);
        // adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ConvenioTurnoVO</code> no List <code>convenioTurnoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ConvenioTurno</code> - getTurno().getCodigo() - como identificador
     * (key) do objeto no List.
     *
     * @param turno
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjConvenioTurnoVOs(Integer turno) throws Exception {
        int index = 0;
        Iterator i = getConvenioTurnoVOs().iterator();
        while (i.hasNext()) {
            ConvenioTurnoVO objExistente = (ConvenioTurnoVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(turno)) {
                getConvenioTurnoVOs().remove(index);
                return;
            }
            index++;
        }
        // excluirObjSubordinadoOC
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ConvenioTurnoVO</code> no List <code>convenioTurnoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ConvenioTurno</code> - getTurno().getCodigo() - como identificador
     * (key) do objeto no List.
     *
     * @param turno
     *            Parâmetro para localizar o objeto do List.
     */
    public ConvenioTurnoVO consultarObjConvenioTurnoVO(Integer turno) throws Exception {
        Iterator i = getConvenioTurnoVOs().iterator();
        while (i.hasNext()) {
            ConvenioTurnoVO objExistente = (ConvenioTurnoVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(turno)) {
                return objExistente;
            }
        }
        return null;
        // consultarObjSubordinadoOC
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ConvenioUnidadeEnsinoVO</code> ao List
     * <code>convenioUnidadeEnsinoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ConvenioUnidadeEnsino</code> -
     * getUnidadeEnsino().getCodigo() - como identificador (key) do objeto no
     * List.
     *
     * @param obj
     *            Objeto da classe <code>ConvenioUnidadeEnsinoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjConvenioUnidadeEnsinoVOs(ConvenioUnidadeEnsinoVO obj) throws Exception {
        ConvenioUnidadeEnsinoVO.validarDados(obj);
        int index = 0;
        Iterator i = getConvenioUnidadeEnsinoVOs().iterator();
        while (i.hasNext()) {
            ConvenioUnidadeEnsinoVO objExistente = (ConvenioUnidadeEnsinoVO) i.next();
            if (objExistente.getUnidadeEnsino().getCodigo().equals(obj.getUnidadeEnsino().getCodigo())) {
                getConvenioUnidadeEnsinoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getConvenioUnidadeEnsinoVOs().add(obj);
        // adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ConvenioUnidadeEnsinoVO</code> no List
     * <code>convenioUnidadeEnsinoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ConvenioUnidadeEnsino</code> -
     * getUnidadeEnsino().getCodigo() - como identificador (key) do objeto no
     * List.
     *
     * @param unidadeEnsino
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjConvenioUnidadeEnsinoVOs(Integer unidadeEnsino) throws Exception {
        int index = 0;
        Iterator i = getConvenioUnidadeEnsinoVOs().iterator();
        while (i.hasNext()) {
            ConvenioUnidadeEnsinoVO objExistente = (ConvenioUnidadeEnsinoVO) i.next();
            if (objExistente.getUnidadeEnsino().getCodigo().equals(unidadeEnsino)) {
                getConvenioUnidadeEnsinoVOs().remove(index);
                return;
            }
            index++;
        }
        // excluirObjSubordinadoOC
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ConvenioUnidadeEnsinoVO</code> no List
     * <code>convenioUnidadeEnsinoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ConvenioUnidadeEnsino</code> -
     * getUnidadeEnsino().getCodigo() - como identificador (key) do objeto no
     * List.
     *
     * @param unidadeEnsino
     *            Parâmetro para localizar o objeto do List.
     */
    public ConvenioUnidadeEnsinoVO consultarObjConvenioUnidadeEnsinoVO(Integer unidadeEnsino) throws Exception {
        Iterator i = getConvenioUnidadeEnsinoVOs().iterator();
        while (i.hasNext()) {
            ConvenioUnidadeEnsinoVO objExistente = (ConvenioUnidadeEnsinoVO) i.next();
            if (objExistente.getUnidadeEnsino().getCodigo().equals(unidadeEnsino)) {
                return objExistente;
            }
        }
        return null;
        // consultarObjSubordinadoOC
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Convenio</code>).
     */
    public PessoaVO getResponsavelFinalizacao() {
        if (responsavelFinalizacao == null) {
            responsavelFinalizacao = new PessoaVO();
        }
        return (responsavelFinalizacao);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Convenio</code>).
     */
    public void setResponsavelFinalizacao(PessoaVO obj) {
        this.responsavelFinalizacao = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Convenio</code>).
     */
    public PessoaVO getResponsavelAutorizacao() {
        if (responsavelAutorizacao == null) {
            responsavelAutorizacao = new PessoaVO();
        }
        return (responsavelAutorizacao);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Convenio</code>).
     */
    public void setResponsavelAutorizacao(PessoaVO obj) {
        this.responsavelAutorizacao = obj;
    }

    public FuncionarioVO getRequisitante() {
        if (requisitante == null) {
            requisitante = new FuncionarioVO();
        }
        return requisitante;
    }

    public void setRequisitante(FuncionarioVO requisitante) {
        this.requisitante = requisitante;
    }

    /**
     * Retorna o objeto da classe <code>Parceiro</code> relacionado com (
     * <code>Convenio</code>).
     */
    public ParceiroVO getParceiro() {
        if (parceiro == null) {
            parceiro = new ParceiroVO();
        }
        return (parceiro);
    }

    /**
     * Define o objeto da classe <code>Parceiro</code> relacionado com (
     * <code>Convenio</code>).
     */
    public void setParceiro(ParceiroVO obj) {
        this.parceiro = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ConvenioCurso</code>.
     */
    public List getConvenioCursoVOs() {
        if (convenioCursoVOs == null) {
            convenioCursoVOs = new ArrayList(0);
        }
        return (convenioCursoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ConvenioCurso</code>.
     */
    public void setConvenioCursoVOs(List convenioCursoVOs) {
        this.convenioCursoVOs = convenioCursoVOs;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ConvenioTurno</code>.
     */
    public List getConvenioTurnoVOs() {
        if (convenioTurnoVOs == null) {
            convenioTurnoVOs = new ArrayList(0);
        }
        return (convenioTurnoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ConvenioTurno</code>.
     */
    public void setConvenioTurnoVOs(List convenioTurnoVOs) {
        this.convenioTurnoVOs = convenioTurnoVOs;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ConvenioUnidadeEnsino</code>.
     */
    public List getConvenioUnidadeEnsinoVOs() {
        if (convenioUnidadeEnsinoVOs == null) {
            convenioUnidadeEnsinoVOs = new ArrayList(0);
        }
        return (convenioUnidadeEnsinoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ConvenioUnidadeEnsino</code>.
     */
    public void setConvenioUnidadeEnsinoVOs(List convenioUnidadeEnsinoVOs) {
        this.convenioUnidadeEnsinoVOs = convenioUnidadeEnsinoVOs;
    }

    public Boolean getValidoParaTodoTurno() {
        if (validoParaTodoTurno == null) {
            validoParaTodoTurno = Boolean.TRUE;
        }
        return (validoParaTodoTurno);
    }

    public Boolean isValidoParaTodoTurno() {
        if (validoParaTodoTurno == null) {
            validoParaTodoTurno = Boolean.TRUE;
        }
        return (validoParaTodoTurno);
    }

    public void setValidoParaTodoTurno(Boolean validoParaTodoTurno) {
        this.validoParaTodoTurno = validoParaTodoTurno;
    }

    public Boolean getValidoParaTodaUnidadeEnsino() {
        if (validoParaTodaUnidadeEnsino == null) {
            validoParaTodaUnidadeEnsino = Boolean.TRUE;
        }
        return (validoParaTodaUnidadeEnsino);
    }

    public Boolean isValidoParaTodaUnidadeEnsino() {
        if (validoParaTodaUnidadeEnsino == null) {
            validoParaTodaUnidadeEnsino = Boolean.TRUE;
        }
        return (validoParaTodaUnidadeEnsino);
    }

    public void setValidoParaTodaUnidadeEnsino(Boolean validoParaTodaUnidadeEnsino) {
        this.validoParaTodaUnidadeEnsino = validoParaTodaUnidadeEnsino;
    }

    public Boolean getValidoParaTodoCurso() {
        if (validoParaTodoCurso == null) {
            validoParaTodoCurso = Boolean.TRUE;
        }
        return (validoParaTodoCurso);
    }

    public Boolean isValidoParaTodoCurso() {
        if (validoParaTodoCurso == null) {
            validoParaTodoCurso = Boolean.TRUE;
        }
        return (validoParaTodoCurso);
    }

    public void setValidoParaTodoCurso(Boolean validoParaTodoCurso) {
        this.validoParaTodoCurso = validoParaTodoCurso;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "AA";
        }
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        if (situacao.equals("FI")) {
            return "Finalizado";
        }
        if (situacao.equals("IN")) {
            return "Inativo";
        }
        if (situacao.equals("AT")) {
            return "Ativo";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDataFinalizacao() {
        // Checar se é realmente null
        return (dataFinalizacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinalizacao_Apresentar() {
        return (Uteis.getData(dataFinalizacao));
    }

    public void setDataFinalizacao(Date dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public Date getDataAutorizacao() {
        // Checar se é realmente null
        return (dataAutorizacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataAutorizacao_Apresentar() {
        return (Uteis.getData(dataAutorizacao));
    }

    public void setDataAutorizacao(Date dataAutorizacao) {
        this.dataAutorizacao = dataAutorizacao;
    }

    public Date getDataRequisicao() {
        if (dataRequisicao == null) {
            dataRequisicao = new Date();
        }
        return (dataRequisicao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataRequisicao_Apresentar() {
        return (Uteis.getData(dataRequisicao));
    }

    public void setDataRequisicao(Date dataRequisicao) {
        this.dataRequisicao = dataRequisicao;
    }

    public Integer getDiaBaseRecebimentoParceiro() {
        if (diaBaseRecebimentoParceiro == null) {
            diaBaseRecebimentoParceiro = 0;
        }
        return (diaBaseRecebimentoParceiro);
    }

    public void setDiaBaseRecebimentoParceiro(Integer diaBaseRecebimentoParceiro) {
        this.diaBaseRecebimentoParceiro = diaBaseRecebimentoParceiro;
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getFormaRecebimentoParceiro_Apresentar() {
        return getFormaRecebimentoParceiro().getTipo_Apresentar();
    }

    public void setFormaRecebimentoParceiro(FormaPagamentoVO formaRecebimentoParceiro) {
        this.formaRecebimentoParceiro = formaRecebimentoParceiro;
    }

    public String getTipoBolsaCusteadaParceiroParcela() {
        if (tipoBolsaCusteadaParceiroParcela == null) {
            tipoBolsaCusteadaParceiroParcela = "VA";
        }
        return (tipoBolsaCusteadaParceiroParcela);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoBolsaCusteadaParceiroParcela_Apresentar() {
        if (tipoBolsaCusteadaParceiroParcela.equals("PE")) {
            return "Percentual";
        }
        if (tipoBolsaCusteadaParceiroParcela.equals("VA")) {
            return "Valor";
        }
        return (tipoBolsaCusteadaParceiroParcela);
    }

    public void setTipoBolsaCusteadaParceiroParcela(String tipoBolsaCusteadaParceiroParcela) {
        this.tipoBolsaCusteadaParceiroParcela = tipoBolsaCusteadaParceiroParcela;
    }

    public Double getBolsaCusteadaParceiroParcela() {
        if (bolsaCusteadaParceiroParcela == null) {
            bolsaCusteadaParceiroParcela = 0.0;
        }
        return (bolsaCusteadaParceiroParcela);
    }

    public void setBolsaCusteadaParceiroParcela(Double bolsaCusteadaParceiroParcela) {
        this.bolsaCusteadaParceiroParcela = bolsaCusteadaParceiroParcela;
    }

    public String getTipoBolsaCusteadaParceiroMatricula() {
        if (tipoBolsaCusteadaParceiroMatricula == null) {
            tipoBolsaCusteadaParceiroMatricula = "VA";
        }
        return (tipoBolsaCusteadaParceiroMatricula);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoBolsaCusteadaParceiroMatricula_Apresentar() {
        if (tipoBolsaCusteadaParceiroMatricula.equals("PE")) {
            return "Percentual";
        }
        if (tipoBolsaCusteadaParceiroMatricula.equals("VA")) {
            return "Valor";
        }
        return (tipoBolsaCusteadaParceiroMatricula);
    }

    public void setTipoBolsaCusteadaParceiroMatricula(String tipoBolsaCusteadaParceiroMatricula) {
        this.tipoBolsaCusteadaParceiroMatricula = tipoBolsaCusteadaParceiroMatricula;
    }

    public Double getBolsaCusteadaParceiroMatricula() {
        if (bolsaCusteadaParceiroMatricula == null) {
            bolsaCusteadaParceiroMatricula = 0.0;
        }
        return (bolsaCusteadaParceiroMatricula);
    }

    public void setBolsaCusteadaParceiroMatricula(Double bolsaCusteadaParceiroMatricula) {
        this.bolsaCusteadaParceiroMatricula = bolsaCusteadaParceiroMatricula;
    }

    public String getTipoDescontoParcela() {
        if (tipoDescontoParcela == null) {
            tipoDescontoParcela = "VA";
        }
        return (tipoDescontoParcela);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoDescontoParcela_Apresentar() {
        if (tipoDescontoParcela.equals("PE")) {
            return "Percentual";
        }
        if (tipoDescontoParcela.equals("VA")) {
            return "Valor";
        }
        return (tipoDescontoParcela);
    }

    public void setTipoDescontoParcela(String tipoDescontoParcela) {
        this.tipoDescontoParcela = tipoDescontoParcela;
    }

    public Double getDescontoParcela() {
        if (descontoParcela == null) {
            descontoParcela = 0.0;
        }
        return (descontoParcela);
    }

    public void setDescontoParcela(Double descontoParcela) {
        this.descontoParcela = descontoParcela;
    }

    public String getTipoDescontoMatricula() {
        if (tipoDescontoMatricula == null) {
            tipoDescontoMatricula = "VA";
        }
        return (tipoDescontoMatricula);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoDescontoMatricula_Apresentar() {
        if (tipoDescontoMatricula.equals("PE")) {
            return "Percentual";
        }
        if (tipoDescontoMatricula.equals("VA")) {
            return "Valor";
        }
        return (tipoDescontoMatricula);
    }

    public void setTipoDescontoMatricula(String tipoDescontoMatricula) {
        this.tipoDescontoMatricula = tipoDescontoMatricula;
    }

    public Double getDescontoMatricula() {
        if (descontoMatricula == null) {
            descontoMatricula = 0.0;
        }
        return (descontoMatricula);
    }

    public void setDescontoMatricula(Double descontoMatricula) {
        this.descontoMatricula = descontoMatricula;
    }

    public Date getDataFinalVigencia() {
        if (dataFinalVigencia == null) {
            dataFinalVigencia = new Date();
        }
        return (dataFinalVigencia);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinalVigencia_Apresentar() {
        return (Uteis.getData(dataFinalVigencia));
    }

    public void setDataFinalVigencia(Date dataFinalVigencia) {
        this.dataFinalVigencia = dataFinalVigencia;
    }

    public Date getDataInicioVigencia() {
        if (dataInicioVigencia == null) {
            dataInicioVigencia = new Date();
        }
        return (dataInicioVigencia);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicioVigencia_Apresentar() {
        return (Uteis.getData(dataInicioVigencia));
    }

    public void setDataInicioVigencia(Date dataInicioVigencia) {
        this.dataInicioVigencia = dataInicioVigencia;
    }

    public String getPreRequisitos() {
        if (preRequisitos == null) {
            preRequisitos = "";
        }
        return (preRequisitos);
    }

    public void setPreRequisitos(String preRequisitos) {
        this.preRequisitos = preRequisitos;
    }

    public String getCobertura() {
        if (cobertura == null) {
            cobertura = "";
        }
        return (cobertura);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getCobertura_Apresentar() {
        if (cobertura.equals("CD")) {
            return "Colaborador e Dependentes";
        }
        if (cobertura.equals("CO")) {
            return "Colaborador";
        }
        return (cobertura);
    }

    public void setCobertura(String cobertura) {
        this.cobertura = cobertura;
    }

    public Date getDataAssinatura() {
        if (dataAssinatura == null) {
            dataAssinatura = new Date();
        }
        return (dataAssinatura);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataAssinatura_Apresentar() {
        return (Uteis.getData(dataAssinatura));
    }

    public void setDataAssinatura(Date dataAssinatura) {
        this.dataAssinatura = dataAssinatura;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the periodoIndeterminado
     */
    public Boolean getPeriodoIndeterminado() {
        if (periodoIndeterminado == null) {
            periodoIndeterminado = Boolean.FALSE;
        }
        return periodoIndeterminado;
    }

    /**
     * @param periodoIndeterminado
     *            the periodoIndeterminado to set
     */
    public void setPeriodoIndeterminado(Boolean periodoIndeterminado) {
        this.periodoIndeterminado = periodoIndeterminado;
    }

    /**
     * @return the formaRecebimentoParceiro
     */
    public FormaPagamentoVO getFormaRecebimentoParceiro() {
        if (formaRecebimentoParceiro == null) {
            formaRecebimentoParceiro = new FormaPagamentoVO();
        }
        return formaRecebimentoParceiro;
    }

    public Double getValorTotalDescontoMatriculaPorConvenioCalculado() {
        if (valorTotalDescontoMatriculaPorConvenioCalculado == null) {
            valorTotalDescontoMatriculaPorConvenioCalculado = 0.0;
        }
        return valorTotalDescontoMatriculaPorConvenioCalculado;
    }

    public void setValorTotalDescontoMatriculaPorConvenioCalculado(Double valorTotalDescontoMatriculaPorConvenioCalculado) {
        this.valorTotalDescontoMatriculaPorConvenioCalculado = valorTotalDescontoMatriculaPorConvenioCalculado;
    }

    public Double getValorTotalDescontoParcelaPorConvenioCalculado() {
        if (valorTotalDescontoParcelaPorConvenioCalculado == null) {
            valorTotalDescontoParcelaPorConvenioCalculado = 0.0;
        }
        return valorTotalDescontoParcelaPorConvenioCalculado;
    }

    public void setValorTotalDescontoParcelaPorConvenioCalculado(Double valorTotalDescontoParcelaPorConvenioCalculado) {
        this.valorTotalDescontoParcelaPorConvenioCalculado = valorTotalDescontoParcelaPorConvenioCalculado;
    }

    public Boolean getAtivo() {
        if (ativo == null) {
            ativo = Boolean.FALSE;
        }
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getAtivo_Apresentar() {
        if (getAtivo()) {
            return UteisJSF.internacionalizar("prt_Convenio_ativoSim");
        } else {
            return UteisJSF.internacionalizar("prt_Convenio_ativoNao");
        }
    }

    public Date getDataAtivacao() {
        if (dataAtivacao == null) {
            dataAtivacao = new Date();
        }
        return dataAtivacao;
    }

    public void setDataAtivacao(Date dataAtivacao) {
        this.dataAtivacao = dataAtivacao;
    }

    public UsuarioVO getResponsavelAtivacao() {
        if (responsavelAtivacao == null) {
            responsavelAtivacao = new UsuarioVO();
        }
        return responsavelAtivacao;
    }

    public void setResponsavelAtivacao(UsuarioVO responsavelAtivacao) {
        this.responsavelAtivacao = responsavelAtivacao;
    }

    public Date getDataInativacao() {
        if (dataInativacao == null) {
            dataInativacao = new Date();
        }
        return dataInativacao;
    }

    public void setDataInativacao(Date dataInativacao) {
        this.dataInativacao = dataInativacao;
    }

    public UsuarioVO getResponsavelInativacao() {
        if (responsavelInativacao == null) {
            responsavelInativacao = new UsuarioVO();
        }
        return responsavelInativacao;
    }

    public void setResponsavelInativacao(UsuarioVO responsavelInativacao) {
        this.responsavelInativacao = responsavelInativacao;
    }

    public void setPossuiDescontoAntecipacao(Boolean possuiDescontoAntecipacao) {
        this.possuiDescontoAntecipacao = possuiDescontoAntecipacao;
    }

    public Boolean getPossuiDescontoAntecipacao() {
        if (possuiDescontoAntecipacao == null) {
            possuiDescontoAntecipacao = Boolean.FALSE;
        }
        return possuiDescontoAntecipacao;
    }

    public void setDescontoProgressivoParceiro(DescontoProgressivoVO descontoProgressivoParceiro) {
        this.descontoProgressivoParceiro = descontoProgressivoParceiro;
    }

    public DescontoProgressivoVO getDescontoProgressivoParceiro() {
        if (descontoProgressivoParceiro == null) {
            descontoProgressivoParceiro = new DescontoProgressivoVO();
        }
        return descontoProgressivoParceiro;
    }

    public void setDescontoProgressivoAluno(DescontoProgressivoVO descontoProgressivoAluno) {
        this.descontoProgressivoAluno = descontoProgressivoAluno;
    }

    public DescontoProgressivoVO getDescontoProgressivoAluno() {
        if (descontoProgressivoAluno == null) {
            descontoProgressivoAluno = new DescontoProgressivoVO();
        }
        return descontoProgressivoAluno;
    }

    public Boolean getCalculadoEmCimaValorLiquido() {
        if (calculadoEmCimaValorLiquido == null) {
            calculadoEmCimaValorLiquido = Boolean.FALSE;
        }
        return calculadoEmCimaValorLiquido;
    }

    public void setCalculadoEmCimaValorLiquido(Boolean calculadoEmCimaValorLiquido) {
        this.calculadoEmCimaValorLiquido = calculadoEmCimaValorLiquido;
    }

    public Boolean getAplicarDescontoProgressivoMatricula() {
        if (aplicarDescontoProgressivoMatricula == null) {
            aplicarDescontoProgressivoMatricula = false;
        }
        return aplicarDescontoProgressivoMatricula;
    }

    public void setAplicarDescontoProgressivoMatricula(Boolean aplicarDescontoProgressivoMatricula) {
        this.aplicarDescontoProgressivoMatricula = aplicarDescontoProgressivoMatricula;
    }

    public Boolean getAplicarDescontoProgressivoMatriculaParceiro() {
        if (aplicarDescontoProgressivoMatriculaParceiro == null) {
            aplicarDescontoProgressivoMatriculaParceiro = false;
        }
        return aplicarDescontoProgressivoMatriculaParceiro;
    }

    public void setAplicarDescontoProgressivoMatriculaParceiro(Boolean aplicarDescontoProgressivoMatriculaParceiro) {
        this.aplicarDescontoProgressivoMatriculaParceiro = aplicarDescontoProgressivoMatriculaParceiro;
    }

    /**
     * @return the abaterDescontoNoValorMatricula
     */
    public Boolean getAbaterDescontoNoValorMatricula() {
        if (abaterDescontoNoValorMatricula == null) {
            abaterDescontoNoValorMatricula = Boolean.FALSE;
        }
        return abaterDescontoNoValorMatricula;
    }

    /**
     * @param abaterDescontoNoValorMatricula the abaterDescontoNoValorMatricula to set
     */
    public void setAbaterDescontoNoValorMatricula(Boolean abaterDescontoNoValorMatricula) {
        this.abaterDescontoNoValorMatricula = abaterDescontoNoValorMatricula;
    }

    /**
     * @return the abaterDescontoNoValorParcela
     */
    public Boolean getAbaterDescontoNoValorParcela() {
        if (abaterDescontoNoValorParcela == null) {
            abaterDescontoNoValorParcela = Boolean.FALSE;
        }
        return abaterDescontoNoValorParcela;
    }

    /**
     * @param abaterDescontoNoValorParcela the abaterDescontoNoValorParcela to set
     */
    public void setAbaterDescontoNoValorParcela(Boolean abaterDescontoNoValorParcela) {
        this.abaterDescontoNoValorParcela = abaterDescontoNoValorParcela;
    }
    
    /**
     * @return the aplicarSobreValorBaseDeduzidoValorOutrosConvenios
     */
    public Boolean getAplicarSobreValorBaseDeduzidoValorOutrosConvenios() {
        if (aplicarSobreValorBaseDeduzidoValorOutrosConvenios == null) {
            aplicarSobreValorBaseDeduzidoValorOutrosConvenios = Boolean.FALSE;
        }
        return aplicarSobreValorBaseDeduzidoValorOutrosConvenios;
    }

    /**
     * @param aplicarSobreValorBaseDeduzidoValorOutrosConvenios the aplicarSobreValorBaseDeduzidoValorOutrosConvenios to set
     */
    public void setAplicarSobreValorBaseDeduzidoValorOutrosConvenios(Boolean aplicarSobreValorBaseDeduzidoValorOutrosConvenios) {
        this.aplicarSobreValorBaseDeduzidoValorOutrosConvenios = aplicarSobreValorBaseDeduzidoValorOutrosConvenios;
    }

    /**
     * @return the ordemPrioridadeParaCalculo
     */
    public Integer getOrdemPrioridadeParaCalculo() {
        if (ordemPrioridadeParaCalculo == null) {
            ordemPrioridadeParaCalculo = 0;
        }
        return ordemPrioridadeParaCalculo;
    }

    /**
     * @param ordemPrioridadeParaCalculo the ordemPrioridadeParaCalculo to set
     */
    public void setOrdemPrioridadeParaCalculo(Integer ordemPrioridadeParaCalculo) {
        this.ordemPrioridadeParaCalculo = ordemPrioridadeParaCalculo;
    }
    
    public Boolean getAplicarSobreValorCheio() {
	return !getCalculadoEmCimaValorLiquido();
    }
    
    public int compareTo(ConvenioVO o) {
        Integer ordemPrioridadeAplicacaoThis = this.getOrdemPrioridadeParaCalculo();
        Integer ordemPrioridadeObjetoComparacao = o.getOrdemPrioridadeParaCalculo();
        if (ordemPrioridadeAplicacaoThis.intValue() == ordemPrioridadeObjetoComparacao.intValue()) {
        	return this.getCodigo().compareTo(o.getCodigo());
        }
        return ordemPrioridadeAplicacaoThis.compareTo(ordemPrioridadeObjetoComparacao);
    }

	public Boolean getConvenioInadimplenteBloqueaMatricula() {
		if (convenioInadimplenteBloqueaMatricula == null) {
			convenioInadimplenteBloqueaMatricula = Boolean.TRUE;
		}
		return convenioInadimplenteBloqueaMatricula;
	}

	public void setConvenioInadimplenteBloqueaMatricula(Boolean convenioInadimplenteBloqueaMatricula) {
		this.convenioInadimplenteBloqueaMatricula = convenioInadimplenteBloqueaMatricula;
	}

    /**
     * @return the naoGerarParcelasConvenioVencidas
     */
    public Boolean getGerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual() {
        if (gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual == null) {
            gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual = Boolean.FALSE;
        }
        return gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual;
    }
    
    /**
     * @param gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual the gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual to set
     */
    public void setGerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual(Boolean gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual) {
        this.gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual = gerarParcelasConvenioVencidasComDataAtaulizadaParaMesAtual;
    }

   
    /**
     * @return the gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio
     */
    public Boolean getGerarRestituicaoAlunoValorJaPagoRelativoAoConvenio() {
        if (gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio == null) {
            gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio = Boolean.FALSE;
        }
        return gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio;
    }

    /**
     * @param gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio the gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio to set
     */
    public void setGerarRestituicaoAlunoValorJaPagoRelativoAoConvenio(Boolean gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio) {
        this.gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio = gerarRestituicaoAlunoValorJaPagoRelativoAoConvenio;
    }

    /**
     * @return the categoriaDespesaRestituicaoConvenio
     */
    public CategoriaDespesaVO getCategoriaDespesaRestituicaoConvenio() {
        if (categoriaDespesaRestituicaoConvenio == null) {
            categoriaDespesaRestituicaoConvenio = new CategoriaDespesaVO();
        }
        return categoriaDespesaRestituicaoConvenio;
    }

    /**
     * @param categoriaDespesaRestituicaoConvenio the categoriaDespesaRestituicaoConvenio to set
     */
    public void setCategoriaDespesaRestituicaoConvenio(CategoriaDespesaVO categoriaDespesaRestituicaoConvenio) {
        this.categoriaDespesaRestituicaoConvenio = categoriaDespesaRestituicaoConvenio;
    }

    /**
     * @return the centroReceitaMatricula
     */
    public CentroReceitaVO getCentroReceitaMatricula() {
        if (centroReceitaMatricula == null) {
            centroReceitaMatricula = new CentroReceitaVO();
        }
        return centroReceitaMatricula;
    }

    /**
     * @param centroReceitaMatricula the centroReceitaMatricula to set
     */
    public void setCentroReceitaMatricula(CentroReceitaVO centroReceitaMatricula) {
        this.centroReceitaMatricula = centroReceitaMatricula;
    }

    /**
     * @return the centroReceitaMensalidade
     */
    public CentroReceitaVO getCentroReceitaMensalidade() {
        if (centroReceitaMensalidade == null) {
            centroReceitaMensalidade = new CentroReceitaVO();
        }
        return centroReceitaMensalidade;
    }

    /**
     * @param centroReceitaMensalidade the centroReceitaMensalidade to set
     */
    public void setCentroReceitaMensalidade(CentroReceitaVO centroReceitaMensalidade) {
        this.centroReceitaMensalidade = centroReceitaMensalidade;
    }

	public String getTipoFinanciamentoEstudantil() {
		if (tipoFinanciamentoEstudantil == null) {
			tipoFinanciamentoEstudantil = "";
		}
		return tipoFinanciamentoEstudantil;
	}

	public void setTipoFinanciamentoEstudantil(String tipoFinanciamentoEstudantil) {
		this.tipoFinanciamentoEstudantil = tipoFinanciamentoEstudantil;
	}

	public Boolean getRemoverDescontoRenovacao() {
		if (removerDescontoRenovacao == null) {
			removerDescontoRenovacao = false;
		}
		return removerDescontoRenovacao;
	}

	public void setRemoverDescontoRenovacao(Boolean removerDescontoRenovacao) {
		this.removerDescontoRenovacao = removerDescontoRenovacao;
	}
	
	public Boolean getConvenioTipoPadrao() {
		if(convenioTipoPadrao == null){
			convenioTipoPadrao = false;
		}
		return convenioTipoPadrao;
	}

	public void setConvenioTipoPadrao(Boolean convenioTipoPadrao) {
		this.convenioTipoPadrao = convenioTipoPadrao;
	}
	
	public String tipoDescParcelaApresentar;
	public String getTipoDescParcelaApresentar(){
		if(tipoDescParcelaApresentar == null){
			if(getTipoDescontoParcela().equals("PE")){
				tipoDescParcelaApresentar=  UteisNfe.formatarStringDouble(getDescontoParcela().toString(), 2)+"%";
			}else{
				tipoDescParcelaApresentar = "R$ "+UteisNfe.formatarStringDouble(getDescontoParcela().toString(), 2);
			}
		}
		return tipoDescParcelaApresentar;
	}
	
	public String tipoDescMatriculaApresentar;
	public String getTipoDescMatriculaApresentar(){
		if(tipoDescMatriculaApresentar == null){
			if(getTipoDescontoMatricula().equals("PE")){
				tipoDescMatriculaApresentar=  UteisNfe.formatarStringDouble(getDescontoMatricula().toString(), 2)+"%";
			}else{
				tipoDescMatriculaApresentar = "R$ "+UteisNfe.formatarStringDouble(getDescontoMatricula().toString(), 2);
			}
		}
		return tipoDescMatriculaApresentar;
	}
	
	public String valorBaseAplicarDesconto;
	public String getValorBaseAplicarDesconto(){
		if(valorBaseAplicarDesconto == null){
			if(getAplicarSobreValorCheio() && getAplicarSobreValorBaseDeduzidoValorOutrosConvenios()){
				valorBaseAplicarDesconto=  "Aplicar Sobre Valor Cheio Deduzido Outros Descontos";
			}else if(getCalculadoEmCimaValorLiquido() && getAplicarSobreValorBaseDeduzidoValorOutrosConvenios()){
				valorBaseAplicarDesconto=  "Valor Líquido Deduzido Outros Descontos"; 
			}else if(getAplicarSobreValorBaseDeduzidoValorOutrosConvenios()){
				valorBaseAplicarDesconto=  "Aplicar Valor Deduzido Outros Descontos"; 
			}else if(getAplicarSobreValorCheio()){
				valorBaseAplicarDesconto = "Aplicar Valor Cheio";
			}else if(getCalculadoEmCimaValorLiquido()){
				valorBaseAplicarDesconto = "Aplicar Sobre o Valor Líquido";
			}			
		}
		return valorBaseAplicarDesconto;
	}
	
	public String getOrdenacao(){
		return getOrdemPrioridadeParaCalculo()+"_"+getCodigo();
	}
}
