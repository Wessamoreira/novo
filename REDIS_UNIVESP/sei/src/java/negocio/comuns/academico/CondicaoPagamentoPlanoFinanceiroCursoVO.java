package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.TipoGeracaoMaterialDidaticoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.FormaIngresso;

/**
 * Reponsável por manter os dados da entidade PlanoFinanceiroCurso. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "condicaopagamentoplanofinanceirocurso")
public class CondicaoPagamentoPlanoFinanceiroCursoVO extends SuperVO {

	@XmlElement(name = "codigo")
    private Integer codigo;
    private Double valorMatricula;
    
    /**
     * Este TIPO DE CALCULO irá assumir o valor padrão do plano, contudo, o
     * usuário poderá alterar o modelo de uma determinada condição, gerando uma
     * situação diferenciada para ela. Ou seja, um plano financeiro poderá
     * ter um condição baseada em crédito e outra baseada em valor fixo, por exemplo.
     */
    private String tipoCalculoParcela;
    
    /**
     * Este tipo de uso será importante para distinguir uma condicação de pagamento
     * convencional (pagar matriculas normais) de uma matricula em regime especial
     * que é destinada para casos especícos de uma matrícula (nível graduação)
     * Matrícula em Regime Especial: Recurso que permite que um aluno possa renovar sua matrícula em regime especial 
     * (sem disciplinas regulares adicionadas a mesma,
     * pois o aluno já integralizou todas as disciplinas - portanto trata-se de uma renovação no último período - e 
     * falta somente cumprir alguma 
     * atividade extra sala de aula para se formar) para que o mesmo possa estar matrículado somente para cumprir 
     * atividades obrigatórias como: Atividade Complementar, Estágio Curricular, ENADE.
     * Ao renovar um aluno neste tipo de regime especial o sistema deverá buscar somente as condições de pagamento para 
     * Matrículas em Regime Especial.
     */
    private String tipoUsoCondicaoPagamento;
    
    /**
     * Campo que será apresentado somente para condições de pagamento destinadas
     * a matrículas em regime especial. Trata do valor a ser cobrado caso o aluno
     * estaja em matricula especical e esteja devendo Atividade Complementar.
     * Matrícula (ou renovação) regular é uma matrícula convencional no qual o aluno irá cursar 
     * disciplinas de um determinado período letivo. Matrícula Especial trata-se de uma renovação 
     * que não irá envolver disicplinas regulares. O aluno irá renovar somente para cumprir atividades o
     * brigatórias que o mesmo ainda tem pendências, como por exemplo: ENADE, Estágio ou Atividades Complementares.
     */
    private String tipoCobrancaAtividadeComplementar;
    
    /**
     * Campo utilizado somente para melhorar a usabilidade do usário. Ao informar
     * o nr de créditos o sistema já calcula e atualiza o campo valorPorENADE que
     * será utilizado no ato da matrícula.
     */
    private Integer nrCreditoPorAtividadeComplementar;
    
    private Double valorPorAtividadeComplementar;

    /**
     * Campo que será apresentado somente para condições de pagamento destinadas
     * a matrículas em regime especial. Trata do valor a ser cobrado caso o aluno
     * estaja em matricula especical e esteja devendo ENADE.
     * Matrícula (ou renovação) regular é uma matrícula convencional no qual o aluno irá cursar 
     * disciplinas de um determinado período letivo. Matrícula Especial trata-se de uma renovação 
     * que não irá envolver disicplinas regulares. O aluno irá renovar somente para cumprir atividades o
     * brigatórias que o mesmo ainda tem pendências, como por exemplo: ENADE, Estágio ou Atividades Complementares.
     */
    private String tipoCobrancaENADE;
    
    /**
     * Campo utilizado somente para melhorar a usabilidade do usário. Ao informar
     * o nr de créditos o sistema já calcula e atualiza o campo valorPorENADE que
     * será utilizado no ato da matrícula.
     */
    private Integer nrCreditoPorENADE;
    
    private Double valorPorENADE;

    /**
     * Campo que será apresentado somente para condições de pagamento destinadas
     * a matrículas em regime especial. Trata do valor a ser cobrado caso o aluno
     * estaja em matricula especical e esteja devendo ESTAGIO.
     * Matrícula (ou renovação) regular é uma matrícula convencional no qual o aluno irá cursar 
     * disciplinas de um determinado período letivo. Matrícula Especial trata-se de uma renovação 
     * que não irá envolver disicplinas regulares. O aluno irá renovar somente para cumprir atividades o
     * brigatórias que o mesmo ainda tem pendências, como por exemplo: ENADE, Estágio ou Atividades Complementares.
     */
    private String tipoCobrancaEstagio;
    /**
     * Campo utilizado somente para melhorar a usabilidade do usário. Ao informar
     * o nr de créditos o sistema já calcula e atualiza o campo valorPorEstagio que
     * será utilizado no ato da matrícula.
     */
    private Integer nrCreditoPorEstagio;
    
    private Double valorPorEstagio;

    
    /**
     * Determina se cobranca será por valor fixo, crédito ou carga horária. 
     * Para disciplinas incluidas regulares (que são disciplinas que não 
     * de dependência - ou seja, que o aluno nunca cursou)
     */
    private String tipoCobrancaInclusaoDisciplinaRegular;
    
    /**
     * Controle para indicar se a cobrança irá ocorrer somente quando as disciplinas
     * incluídas regulares ultrapassar o número máximo de créditos e/ou horas
     * permitidas para o período letivo (dado informado no próprio período letivo)
     * Cobrar por disciplina incluída regular (não dependência) somente quando ultrapassar limite máximo créditos / carga horária período
     */
    private Boolean cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo;
        
    /**
     * Determina se cobranca será por valor fixo, crédito ou carga horária. 
     * Para disciplinas incluidas de dependencia (que são disciplinas que o
     * aluno já cursou alguma vez, mas foi reprovado)
     */
    private String tipoCobrancaInclusaoDisciplinaDependencia;
    /**
     * Controle para indicar se a cobrança irá ocorrer somente quando as disciplinas
     * incluídas de dependencia ultrapassar o número máximo de créditos e/ou horas
     * permitidas para o período letivo (dado informado no próprio período letivo)
     */
    private Boolean cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet;
    /**
     * Valor por disciplina incluida de dependencia. Por armazenar o valor fixo,
     * valor por credito ou valor por hora, depedendo do tipo de cobranca.
     */
    private Double valorDisciplinaIncluidaDependencia;
    /**
     * Valor por disciplina incluida de dependencia na modalidade EAD. Por armazenar o valor fixo,
     * valor por credito ou valor por hora, depedendo do tipo de cobranca.
     */
    private Double valorDisciplinaIncluidaDependenciaEAD;
    
    /**
     * Caso o curso seja presencial e esta opção esteja marcada, significa que o 
     * aluno terá um desconto para cada disciplina regular (não incluída - da matriz do mesmo) que o mesmo optar 
     * em cursar na modalidade EAD. Para a instituição isto pode representar
     * uma economia financeiro, por isto, esta política pode ser interessante
     * para quem deseja estimular o EAD para um conjunto de disciplina regulares 
     * do curso.
     */
    private Boolean utilizarPoliticaDescontoDiscipRegularFeitasViaEAD;
    /**
     * Tipo de desconto por assumir as seguintes opções:
     * PE - percentual. Ou seja, será um desconto percentual sobre o valor da disciplina
     *     calculado pelo sistema. Caso a política da condição de pagamento seja de valor 
     *     fixo por mensalidade (ou fórmula de calculo), o SEI irá adotar automaticamente o valor por hora - dividindo 
     *     o valor fixo da mensalidade pela quantidade de horas das disciplinas regulares que 
     *     o aluno está estudando. Assim terá o valor da hora e por conseguinte o valor da disciplina,
     *     neste regime de valor fixo. Assim este percentual será aplicado sobre este valor. Para
     *     os demais, como cada disciplina tem seu valor calculado (Este desconto será aplicado 
     *     sobre o mesmo).
     * VF - Um valor fixo de desconto a ser dado para o aluno por disciplina estudada nesta modalidade
     * CR - Valor de desconto por crédtio financeiro (se a disciplina cursada via EAD tiver 04 créditos, será 04 vezes este valor
     * VH - Valor de desconto por hora.
     */
    private String tipoDescontoDisciplinaRegularEAD;
    /**
     * Campo onde será registrado o valor de desconto, podendo assumir diferentes significados, dependendo
     * do tipoDescontoDisciplinaRegularEAD
     */
    private Double valorDescontoDisciplinaRegularEAD;
            
    private Boolean naoControlarMatricula;
    @XmlElement(name = "valorParcela")
    private Double valorParcela;
    
    private Double valorPrimeiraParcela;
    private boolean usaValorPrimeiraParcela = false;
    private boolean vencimentoPrimeiraParcelaAntesMaterialDidatico = true;
    
    /**
     * Representa o valor a ser cobrado por disciplina regular incluída.
     * Entenda por disciplina regular, uma disciplina que o aluno nunca cursou
     * (foi reprovado) na mesma. Geralmente, trata-se de um aluno incluindo uma disciplina
     * do futuro ou um disciplina pendente do passado (mas que ele nunca cursou).
     * Apesar deste nome esta coluna pode assumir o valor por crédito e valor por hora.
     * Depedendo do tipo de cobrança para disciplian regular incluída.
     */
    private Double valorFixoDisciplinaIncluida;
    
    /**
     * Representa o valor a ser cobrado por disciplina regular incluída, na modalidade EAD.
     * Esta campo irá permitir a Instituição cobrar um valor diferenciado caso a disciplina
     * incluída seja na modalidade EAD.
     * Entenda por disciplina regular, uma disciplina que o aluno nunca cursou
     * (foi reprovado) na mesma. Geralmente, trata-se de um aluno incluindo uma disciplina
     * do futuro ou um disciplina pendente do passado (mas que ele nunca cursou).
     * Apesar deste nome esta coluna pode assumir o valor por crédito e valor por hora.
     * Depedendo do tipo de cobrança para disciplian regular incluída. 
     */
    private Double valorFixoDisciplinaIncluidaEAD;
    
    /**
     * NAO IMPLEMENTADO NA VERSAO 5.0 POIS CLIENTE NAO IRIA UTILIZA MAIS ESTA POLITICA
     * E NAO FAZ SENTIDO PARA OUTROS CLIENTES DO SEI - EDIGAR
     */
    private Boolean utilizarPoliticaCobrancaEspecificaParaOptativas;
    /**
     * Tipo de cobrança por ser por Valor Fixo, Valor Por Crédito, Valor por Hora da Disciplina
     */
    private String tipoCobrancaDisciplinaOptativa;
    /**
     * Esta opção deve ser marcada para indicar que o sistema irá adotar uma política
     * de cobrança especial para as disciplinas optativas, se e somente se, o número
     * de disciplinas optativas adicionadas ultrapassar o limite estipulado para o 
     * período letivo.
     */
    private Boolean cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet;
                    
    /**
     * Valor por disciplina optativa, que pode armazenar o valor fixo, valor por crédito
     * ou valor por hora.
     */
    private Double valorDisciplinaOptativa;
    /**
     * Valor por disciplina optativa, que pode armazenar o valor fixo, valor por crédito
     * ou valor por hora.
     */
    private Double valorDisciplinaOptativaEAD;
    
    /**
     * Determina se cobranca será por valor fixo, crédito ou carga horária. 
     * Para disciplinas excluidas regulares (que são disciplinas que não 
     * de dependência - ou seja, que o aluno nunca cursou)
     */
    private String tipoCobrancaExclusaoDisciplinaRegular;
    
    private Double valorDescontoDisciplinaExcluida;
    
    /**
     * Representa o valor a ser cobrado por disciplina regular excluída, na modalidade EAD.
     * Esta campo irá permitir a Instituição cobrar um valor diferenciado caso a disciplina
     * excluída seja na modalidade EAD.
     * Entenda por disciplina regular, uma disciplina que o aluno nunca cursou
     * (foi reprovado) na mesma. Geralmente, trata-se de um aluno excluindo uma disciplina
     * do futuro ou um disciplina pendente do passado (mas que ele nunca cursou).
     * Apesar deste nome esta coluna pode assumir o valor por crédito e valor por hora.
     * Depedendo do tipo de cobrança para disciplian regular excluída. 
     */
    private Double valorFixoDisciplinaExcluidaEAD;
    
    
    
    private Boolean gerarParcelasSeparadasParaDisciplinasIncluidas;
    private Boolean naoRegerarParcelaVencida;
    /**
     * Utilizada na condiçao de pagamento por crédito e fórmula de cálculo
     * indicando se o aluno deverá ou não pagar por disciplinas
     * incluídas em seu período letivo.
     */
    private Boolean gerarCobrancaPorDisciplinasIncluidas;
    /**
     * Utilizada na condiçao de pagamento por crédito e fórmula de cálculo
     * indicando se o sistema deverá gerar desconto por disciplinas excluídas.
     */
    private Boolean gerarDescontoPorDiscipliaExcluidas;
    /**
     * Variável relativa a <code>gerarDescontoPorDiscipliaExcluidas</code>. Ou seja,
     * só é apresentada quando a variável supracita for true. Ela indica que o desconto
     * em questão só deve ser aplica no último período. Outra condição relacionada,
     * refere-se ao número máximo de disciplinas que o aluno pode cursar, para que
     * o desconto seja válido.
     */
    private Boolean gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo;
    private Integer numeroMaximoDisciplinaCursarParaGerarDescontos;
    @XmlElement(name = "nrParcelasPeriodo")
    private Integer nrParcelasPeriodo;
    private Double valorMatriculaSistemaPorCredito;
    private Boolean utilizarValorMatriculaFixo;
    private Boolean ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas;
    private Boolean lancarValorRatiadoSobreValorBaseContaReceber;
    private Double valorMinimoParcelaSistemaPorCredito;
    private Double valorUnitarioCredito;
    private Boolean restituirDiferencaValorMatriculaSistemaPorCredito;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>DescontoProgressivo </code>.
     */
    private DescontoProgressivoVO descontoProgressivoPadrao;
    private DescontoProgressivoVO descontoProgressivoPrimeiraParcela;
    private DescontoProgressivoVO descontoProgressivoPadraoMatricula;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PlanoDesconto </code>.
     */
//    private PlanoDescontoVO planoDescontoPadrao;
    private List<CondicaoPagamentoPlanoDescontoVO> condicaoPagamentoPlanoDescontoVOs;
    @XmlElement(name = "descricao")
    private String descricao;
    private String nomeFormula;
    private String formulaCalculoValorFinal;
    private String formulaUsoVariavel1;
    private String formulaCalculoVariavel1;
    private Boolean utilizarVariavel1;
    private Double variavel1;
    private String formulaUsoVariavel2;
    private String formulaCalculoVariavel2;
    private Boolean utilizarVariavel2;
    private Double variavel2;
    private String formulaUsoVariavel3;
    private String formulaCalculoVariavel3;
    private Boolean utilizarVariavel3;
    private Double variavel3;
    private String tituloVariavel1;
    private String tituloVariavel2;
    private String tituloVariavel3;
    private Integer planoFinanceiroCurso;
    private String descricaoDuracao;
    private TextoPadraoVO textoPadraoContratoMatricula;
    //Atributos para controle de ativação e inativação da condicaoPagamento
    private String situacao;
    private Date dataAtivacao;
    private UsuarioVO responsavelAtivacao;
    private Date dataInativacao;
    private UsuarioVO responsavelInativacao;
    private Boolean apresentarCondicaoVisaoAluno;
    private Boolean definirPlanoDescontoApresentarMatricula;
    private List<PlanoDescontoDisponivelMatriculaVO> planoDescontoDisponivelMatriculaVOs;
    private List<CondicaoPlanoFinanceiroCursoTurmaVO> condicaoPlanoFinanceiroCursoTurmaVOs;
    private CentroReceitaVO centroReceita;
    private Boolean aplicarCalculoComBaseDescontosCalculados;
    public static final long serialVersionUID = 1L;
    
    /**
     * Ao marcar esta opção significa que esta condicação de pagamento será apresentada
     * somente para alunos que estejam concluindo o curso (ou seja, estejam próximos
     * de integralizar o curso.
     */
    private Boolean apresentarSomenteParaAlunosIntegralizandoCurso;
    /**
     * Caso esta opção esteja marcada então o SEI irá considerar que o aluno está integralizando 
     * somente se o aluno já tiver cursado o último período do curso ao menos uma vez. Ou seja
     * ele precisa estar renovando no último período pela 2o vez (ou mais).
     */
    private Boolean considerarIntegralizandoEstiverCursandoUltimoPer2Vez;
    /**
     * Existem três tipos de controles possiveis para verificar se o aluno está integralizando
     * o curso. Descritos abaixo:
     * a) Por % Crédito Pendentes no Último Período: Condicação válida para o aluno
     *     caso o mesmo esteja devendo no máximo este percentual de créditos no último período.
     * a) Por % CH Pendentes no Último Período: Condicação válida para o aluno
     *     caso o mesmo esteja devendo no máximo este percentual de créditos no último período.
     * b) Por Carga Horária Pendente: Condicação válida para o aluno somente se o 
     *     mesmo estiver devendo no máximo a quantidade de horas informadas.
     * c) Por Nr. Créditos Pendentes: Condicação válida para o aluno somente se o 
     *     mesmo estiver devendo no máximo a quantidade de créditos informados.
     */
    private String tipoControleAlunoIntegralizandoCurso;
    private Double valorBaseDefinirAlunoIntegralizandoCurso;
    
    
    private Boolean apresentarSomenteParaDeterminadaFormaIngresso;
    private Boolean apresentarSomenteParaIngressanteNoSemestreAno;
    private Boolean formaIngressoEntrevista;
    private Boolean formaIngressoPortadorDiploma;
    private Boolean formaIngressoTransferenciaInterna;
    private Boolean formaIngressoProcessoSeletivo;
    private Boolean formaIngressoTransferenciaExterna;
    private Boolean formaIngressoReingresso;
    private Boolean formaIngressoProuni;
    private Boolean formaIngressoEnem;
    private Boolean formaIngressoOutroTipoSelecao;
    private String anoIngressante;
    private String semestreIngressante;
    /*
     * CodTurma criado apenas para preenchimento a nivel de sessão, não deve ser pesistido.
     */
    private Integer codTurma;
    
    private Boolean apresentarMatriculaOnlineExterna;
    private Boolean apresentarMatriculaOnlineProfessor;
    private Boolean apresentarMatriculaOnlineCoordenador;
    
    /**
     * Com esta opção marcada ao repassar pela matrícula alterando os descontos do aluno o sistema irá considerar como calculo de
     * rateio o valor da conta a receber abatendo todos os possíveis descontos que o aluno poderia receber e compara com o valor das novas contas 
     * a serem geradas com os valores de todos os descontos também calculados.
     */
    private Boolean considerarValorRateioBaseadoValorBaseComDescontosAplicados;
    /**
     * Com esta opção caso exista valores a serem abatidos na conta a receber do aluno o mesmo será aplicado como desconto do recebimento e não será usado para abatimento no valor base da conta a receber.
     */
    private Boolean abaterValorRateiroComoDescontoRateio;

    private Boolean gerarParcelasExtrasSeparadoMensalidadeAReceber;
    /**
     * Com esta opção marcada não será considerado no rateio e no calculo de parcela extra a conta a receber que já venceu, recebida ou não.
     *      */
    private Boolean considerarValorRateioExtraParcelaVencida; 

	
    /**
     * Definine e cataloga uma categoria
     */
    @XmlElement(name = "categoria")
    private String categoria;
    
    /**
     * Desabilita a opcao para o campo NrParcelasPeriodo
     */
    private Boolean naoControlarValorParcela;
    
    
    /**
     * Campos usado para definir a opção de material didático     *  
     */
    
    
    private boolean gerarParcelaMaterialDidatico= false;
    private boolean usarUnidadeEnsinoEspecifica = true;    
    private boolean controlaDiaBaseVencimentoParcelaMaterialDidatico=true;
    private boolean aplicarDescontosParcelasNoMaterialDidatico=true;
    private boolean aplicarDescontoMaterialDidaticoDescontoAluno=true;
    private boolean aplicarDescontoMaterialDidaticoDescontoInstitucional=true;
    private boolean aplicarDescontoMaterialDidaticoDescontoProgressivo=true;
    private boolean aplicarDescontoMaterialDidaticoDescontoConvenio=true;
    private boolean aplicarDescontosDesconsiderandosVencimento=true;
    private UnidadeEnsinoVO unidadeEnsinoFinanceira;
    private Long quantidadeParcelasMaterialDidatico;
    private Long diaBaseVencimentoParcelaOutraUnidade;
    private Double valorPorParcelaMaterialDidatico;
    private TipoGeracaoMaterialDidaticoEnum tipoGeracaoMaterialDidatico;
    private Boolean apresentarCondPagtoCRM;
    private Boolean regerarFinanceiro;
    private boolean considerarParcelasMaterialDidaticoReajustePreco = true;
     
    
    /**
     * Construtor padrão da classe <code>PlanoFinanceiroCurso</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public CondicaoPagamentoPlanoFinanceiroCursoVO() {
        super();
        inicializarDados();
    }

    public void excluirObjItemPlanoDesconto(CondicaoPagamentoPlanoDescontoVO planoDesconto) throws Exception {
        int index = 0;
        Iterator i = getCondicaoPagamentoPlanoDescontoVOs().iterator();
        while (i.hasNext()) {
        	CondicaoPagamentoPlanoDescontoVO objExistente = (CondicaoPagamentoPlanoDescontoVO) i.next();
            if (objExistente.getCodigo().equals(planoDesconto.getCodigo())) {
            	getCondicaoPagamentoPlanoDescontoVOs().remove(index);
                return;
            }
            index++;
        }
    }
    
    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PlanoFinanceiroCursoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(CondicaoPagamentoPlanoFinanceiroCursoVO obj) throws ConsistirException {
        if (obj.getTipoCalculoParcela().equals("")) {
            throw new ConsistirException("O campo TIPO DE CÁLCULO PARCELAS (Condição Plano Financeiro Curso) deve ser informado.");
        }
        if (obj.getNrParcelasPeriodo().intValue() == 0 && !obj.getNaoControlarValorParcela()) {
            throw new ConsistirException("O campo NÚMERO PARCELAS PERÍODO (Plano Financeiro Curso) deve ser informado.");
        }        
        Uteis.checkState(obj.isUsaValorPrimeiraParcela() && !Uteis.isAtributoPreenchido(obj.getValorPrimeiraParcela()), "O Valor 1ª Mensalidade (Entrada) deve ser informado. ");
        if(!obj.isUsaValorPrimeiraParcela() && Uteis.isAtributoPreenchido(obj.getValorPrimeiraParcela())) {
        	obj.setValorPrimeiraParcela(0.0);	
        }
        

        if(!obj.isGerarParcelaMaterialDidatico()){
        	obj.setUsarUnidadeEnsinoEspecifica(false);    
        	obj.setControlaDiaBaseVencimentoParcelaMaterialDidatico(false);
        	obj.setAplicarDescontosParcelasNoMaterialDidatico(false);
        	obj.setAplicarDescontoMaterialDidaticoDescontoAluno(false);
        	obj.setAplicarDescontoMaterialDidaticoDescontoInstitucional(false);
        	obj.setAplicarDescontoMaterialDidaticoDescontoProgressivo(false);
        	obj.setAplicarDescontoMaterialDidaticoDescontoConvenio(false);
        	obj.setConsiderarParcelasMaterialDidaticoReajustePreco(false);
        	obj.setAplicarDescontosDesconsiderandosVencimento(false);
        	obj.setUnidadeEnsinoFinanceira(null);
        	obj.setQuantidadeParcelasMaterialDidatico(0L);
        	obj.setDiaBaseVencimentoParcelaOutraUnidade(0L);
        	obj.setValorPorParcelaMaterialDidatico(0.0);
        	obj.setTipoGeracaoMaterialDidatico(null);
        } else  if(obj.isGerarParcelaMaterialDidatico()){
        	if(!obj.isUsarUnidadeEnsinoEspecifica()){
        		obj.setUnidadeEnsinoFinanceira(null);	
        	}
        	if(!obj.isControlaDiaBaseVencimentoParcelaMaterialDidatico()){
        		obj.setDiaBaseVencimentoParcelaOutraUnidade(0L);	
        	}
        	if(!obj.isAplicarDescontosParcelasNoMaterialDidatico()){
            	obj.setAplicarDescontoMaterialDidaticoDescontoAluno(false);
            	obj.setAplicarDescontoMaterialDidaticoDescontoInstitucional(false);
            	obj.setAplicarDescontoMaterialDidaticoDescontoProgressivo(false);
            	obj.setAplicarDescontoMaterialDidaticoDescontoConvenio(false);
            	obj.setAplicarDescontosDesconsiderandosVencimento(false);		
        	}	
        }       
        if(!obj.getRatiarValorDiferencaInclusaoExclusaoParaTodasParcelas()) {
        	obj.setLancarValorRatiadoSobreValorBaseContaReceber(false);
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setValorMatricula(0.0);
        setValorParcela(0.0);
        setValorPrimeiraParcela(0.0);
        setNrParcelasPeriodo(0);
        setValorMatriculaSistemaPorCredito(0.0);
        setValorMinimoParcelaSistemaPorCredito(0.0);
        setValorUnitarioCredito(0.0);
        setUtilizarVariavel1(Boolean.TRUE);
        setUtilizarVariavel2(Boolean.TRUE);
        setTituloVariavel2("CHPL");
        setUtilizarVariavel3(Boolean.TRUE);
        setTituloVariavel3("CHD");
        setDefinirPlanoDescontoApresentarMatricula(Boolean.FALSE);
    }

//    /**
//     * Retorna o objeto da classe <code>PlanoDesconto</code> relacionado com (
//     * <code>PlanoFinanceiroCurso</code>).
//     */
//    public PlanoDescontoVO getPlanoDescontoPadrao() {
//        if (planoDescontoPadrao == null) {
//            planoDescontoPadrao = new PlanoDescontoVO();
//        }
//        return (planoDescontoPadrao);
//    }
//
//    /**
//     * Define o objeto da classe <code>PlanoDesconto</code> relacionado com (
//     * <code>PlanoFinanceiroCurso</code>).
//     */
//    public void setPlanoDescontoPadrao(PlanoDescontoVO obj) {
//        this.planoDescontoPadrao = obj;
//    }

    /**
     * Retorna o objeto da classe <code>DescontoProgressivo</code> relacionado
     * com (<code>PlanoFinanceiroCurso</code>).
     */
    public DescontoProgressivoVO getDescontoProgressivoPadrao() {
        if (descontoProgressivoPadrao == null) {
            descontoProgressivoPadrao = new DescontoProgressivoVO();
        }
        return (descontoProgressivoPadrao);
    }

    /**
     * Define o objeto da classe <code>DescontoProgressivo</code> relacionado
     * com (<code>PlanoFinanceiroCurso</code>).
     */
    public void setDescontoProgressivoPadrao(DescontoProgressivoVO obj) {
        this.descontoProgressivoPadrao = obj;
    }

    /**
     * Retorna o objeto da classe <code>DescontoProgressivo</code> relacionado
     * com (<code>PlanoFinanceiroCurso</code>).
     */
    public DescontoProgressivoVO getDescontoProgressivoPadraoMatricula() {
        if (descontoProgressivoPadraoMatricula == null) {
            descontoProgressivoPadraoMatricula = new DescontoProgressivoVO();
        }
        return (descontoProgressivoPadraoMatricula);
    }

    /**
     * Define o objeto da classe <code>DescontoProgressivo</code> relacionado
     * com (<code>PlanoFinanceiroCurso</code>).
     */
    public void setDescontoProgressivoPadraoMatricula(DescontoProgressivoVO obj) {
        this.descontoProgressivoPadraoMatricula = obj;
    }
    
    public Double getValorTotalParcelas() {
    	if(isUsaValorPrimeiraParcela()) {
    		return Uteis.arrendondarForcando2CadasDecimais((getValorParcela() * getNrParcelasPeriodo()) + getValorPrimeiraParcela());
    	}    	
        return Uteis.arrendondarForcando2CadasDecimais((getValorParcela() * getNrParcelasPeriodo()));
    }

    public Integer getNrParcelasPeriodo() {
        return (nrParcelasPeriodo);
    }

    public void setNrParcelasPeriodo(Integer nrParcelasPeriodo) {
        this.nrParcelasPeriodo = nrParcelasPeriodo;
    }

    public Double getValorParcela() {
        return (valorParcela);
    }

    public void setValorParcela(Double valorParcela) {
        this.valorParcela = Uteis.arrendondarForcando2CadasDecimais(valorParcela);
    }
    

    public Double getValorPrimeiraParcela() {
		return valorPrimeiraParcela;
	}

	public void setValorPrimeiraParcela(Double valorPrimeiraParcela) {
		this.valorPrimeiraParcela = valorPrimeiraParcela;
	}

	public boolean isUsaValorPrimeiraParcela() {
		return usaValorPrimeiraParcela;
	}

	public void setUsaValorPrimeiraParcela(boolean usaValorPrimeiraParcela) {
		this.usaValorPrimeiraParcela = usaValorPrimeiraParcela;
	}

	public boolean isVencimentoPrimeiraParcelaAntesMaterialDidatico() {
		return vencimentoPrimeiraParcelaAntesMaterialDidatico;
	}

	public void setVencimentoPrimeiraParcelaAntesMaterialDidatico(boolean vencimentoPrimeiraParcelaAntesMaterialDidatico) {
		this.vencimentoPrimeiraParcelaAntesMaterialDidatico = vencimentoPrimeiraParcelaAntesMaterialDidatico;
	}

	public Double getValorMatricula() {
        return (valorMatricula);
    }

    public void setValorMatricula(Double valorMatricula) {
        this.valorMatricula = Uteis.arrendondarForcando2CadasDecimais(valorMatricula);
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

    public Double getValorMatriculaSistemaPorCredito() {
        return valorMatriculaSistemaPorCredito;
    }

    public void setValorMatriculaSistemaPorCredito(Double valorMatriculaSistemaPorCredito) {
        this.valorMatriculaSistemaPorCredito = Uteis.arrendondarForcando2CadasDecimais(valorMatriculaSistemaPorCredito);
    }

    public Double getValorMinimoParcelaSistemaPorCredito() {
        return valorMinimoParcelaSistemaPorCredito;
    }

    public void setValorMinimoParcelaSistemaPorCredito(Double valorMinimoParcelaSistemaPorCredito) {
        this.valorMinimoParcelaSistemaPorCredito = Uteis.arrendondarForcando2CadasDecimais(valorMinimoParcelaSistemaPorCredito);
    }

    public Double getValorUnitarioCredito() {
        return valorUnitarioCredito;
    }

    public void setValorUnitarioCredito(Double valorUnitarioCredito) {
        this.valorUnitarioCredito = valorUnitarioCredito;
    }

    public Boolean getRestituirDiferencaValorMatriculaSistemaPorCredito() {
        if (restituirDiferencaValorMatriculaSistemaPorCredito == null) {
            restituirDiferencaValorMatriculaSistemaPorCredito = Boolean.FALSE;
        }
        return restituirDiferencaValorMatriculaSistemaPorCredito;
    }

    public void setRestituirDiferencaValorMatriculaSistemaPorCredito(Boolean restituirDiferencaValorMatriculaSistemaPorCredito) {
        this.restituirDiferencaValorMatriculaSistemaPorCredito = restituirDiferencaValorMatriculaSistemaPorCredito;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    /**
     * @param descricao
     *            the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the nomeFormula
     */
    public String getNomeFormula() {
        if (nomeFormula == null) {
            nomeFormula = "";
        }
        return nomeFormula;
    }

    /**
     * @param nomeFormula
     *            the nomeFormula to set
     */
    public void setNomeFormula(String nomeFormula) {
        this.nomeFormula = nomeFormula;
    }

    /**
     * @return the formulaCalculoValorFinal
     */
    public String getFormulaCalculoValorFinal() {
        if (formulaCalculoValorFinal == null) {
            formulaCalculoValorFinal = "";
        }
        return formulaCalculoValorFinal;
    }

    /**
     * @param formulaCalculoValorFinal
     *            the formulaCalculoValorFinal to set
     */
    public void setFormulaCalculoValorFinal(String formulaCalculoValorFinal) {
        this.formulaCalculoValorFinal = formulaCalculoValorFinal;
    }

    /**
     * @return the formulaUsoVariavel1
     */
    public String getFormulaUsoVariavel1() {
        if (formulaUsoVariavel1 == null) {
            formulaUsoVariavel1 = "";
        }
        return formulaUsoVariavel1;
    }

    /**
     * @param formulaUsoVariavel1
     *            the formulaUsoVariavel1 to set
     */
    public void setFormulaUsoVariavel1(String formulaUsoVariavel1) {
        this.formulaUsoVariavel1 = formulaUsoVariavel1;
    }

    /**
     * @return the formulaCalculoVariavel1
     */
    public String getFormulaCalculoVariavel1() {
        if (formulaCalculoVariavel1 == null) {
            formulaCalculoVariavel1 = "";
        }
        return formulaCalculoVariavel1;
    }

    /**
     * @param formulaCalculoVariavel1
     *            the formulaCalculoVariavel1 to set
     */
    public void setFormulaCalculoVariavel1(String formulaCalculoVariavel1) {
        this.formulaCalculoVariavel1 = formulaCalculoVariavel1;
    }

    /**
     * @return the utilizarVariavel1
     */
    public Boolean getUtilizarVariavel1() {
        if (utilizarVariavel1 == null) {
            utilizarVariavel1 = Boolean.FALSE;
        }
        return utilizarVariavel1;
    }

    /**
     * @param utilizarVariavel1
     *            the utilizarVariavel1 to set
     */
    public void setUtilizarVariavel1(Boolean utilizarVariavel1) {
        this.utilizarVariavel1 = utilizarVariavel1;
    }

    /**
     * @return the variavel1
     */
    public Double getVariavel1() {
        if (variavel1 == null) {
            variavel1 = new Double(0.0);
        }
        return variavel1;
    }

    /**
     * @param variavel1
     *            the variavel1 to set
     */
    public void setVariavel1(Double variavel1) {
        this.variavel1 = Uteis.arrendondarForcando2CadasDecimais(variavel1);
    }

    /**
     * @return the formulaUsoVariavel2
     */
    public String getFormulaUsoVariavel2() {
        if (formulaUsoVariavel2 == null) {
            formulaUsoVariavel2 = "";
        }
        return formulaUsoVariavel2;
    }

    /**
     * @param formulaUsoVariavel2
     *            the formulaUsoVariavel2 to set
     */
    public void setFormulaUsoVariavel2(String formulaUsoVariavel2) {
        this.formulaUsoVariavel2 = formulaUsoVariavel2;
    }

    /**
     * @return the formulaCalculoVariavel2
     */
    public String getFormulaCalculoVariavel2() {
        if (formulaCalculoVariavel2 == null) {
            formulaCalculoVariavel2 = "";
        }
        return formulaCalculoVariavel2;
    }

    /**
     * @param formulaCalculoVariavel2
     *            the formulaCalculoVariavel2 to set
     */
    public void setFormulaCalculoVariavel2(String formulaCalculoVariavel2) {
        this.formulaCalculoVariavel2 = formulaCalculoVariavel2;
    }

    /**
     * @return the utilizarVariavel2
     */
    public Boolean getUtilizarVariavel2() {
        if (utilizarVariavel2 == null) {
            utilizarVariavel2 = Boolean.FALSE;
        }
        return utilizarVariavel2;
    }

    /**
     * @param utilizarVariavel2
     *            the utilizarVariavel2 to set
     */
    public void setUtilizarVariavel2(Boolean utilizarVariavel2) {
        this.utilizarVariavel2 = utilizarVariavel2;
    }

    /**
     * @return the variavel2
     */
    public Double getVariavel2() {
        if (variavel2 == null) {
            variavel2 = new Double(0.0);
        }
        return variavel2;
    }

    /**
     * @param variavel2
     *            the variavel2 to set
     */
    public void setVariavel2(Double variavel2) {
        this.variavel2 = Uteis.arrendondarForcando2CadasDecimais(variavel2);
    }

    /**
     * @return the formulaUsoVariavel3
     */
    public String getFormulaUsoVariavel3() {
        if (formulaUsoVariavel3 == null) {
            formulaUsoVariavel3 = "";
        }
        return formulaUsoVariavel3;
    }

    /**
     * @param formulaUsoVariavel3
     *            the formulaUsoVariavel3 to set
     */
    public void setFormulaUsoVariavel3(String formulaUsoVariavel3) {
        this.formulaUsoVariavel3 = formulaUsoVariavel3;
    }

    /**
     * @return the formulaCalculoVariavel3
     */
    public String getFormulaCalculoVariavel3() {
        if (formulaCalculoVariavel3 == null) {
            formulaCalculoVariavel3 = "";
        }
        return formulaCalculoVariavel3;
    }

    /**
     * @param formulaCalculoVariavel3
     *            the formulaCalculoVariavel3 to set
     */
    public void setFormulaCalculoVariavel3(String formulaCalculoVariavel3) {
        this.formulaCalculoVariavel3 = formulaCalculoVariavel3;
    }

    /**
     * @return the utilizarVariavel3
     */
    public Boolean getUtilizarVariavel3() {
        if (utilizarVariavel3 == null) {
            utilizarVariavel3 = Boolean.FALSE;
        }
        return utilizarVariavel3;
    }

    /**
     * @param utilizarVariavel3
     *            the utilizarVariavel3 to set
     */
    public void setUtilizarVariavel3(Boolean utilizarVariavel3) {
        this.utilizarVariavel3 = utilizarVariavel3;
    }

    /**
     * @return the variavel3
     */
    public Double getVariavel3() {
        if (variavel3 == null) {
            variavel3 = new Double(0.0);
        }
        return variavel3;
    }

    /**
     * @param variavel3
     *            the variavel3 to set
     */
    public void setVariavel3(Double variavel3) {
        this.variavel3 = Uteis.arrendondarForcando2CadasDecimais(variavel3);
    }

    /**
     * @return the tituloVariavel1
     */
    public String getTituloVariavel1() {
        // tituloVariavel1 = "Valor Mensalidade";
        if (tituloVariavel1 == null) {
            tituloVariavel1 = "VM";
        }
        return tituloVariavel1;
    }

    /**
     * @param tituloVariavel1
     *            the tituloVariavel1 to set
     */
    public void setTituloVariavel1(String tituloVariavel1) {
        this.tituloVariavel1 = tituloVariavel1;
    }

    /**
     * @return the tituloVariavel2
     */
    public String getTituloVariavel2() {
        if (tituloVariavel2 == null) {
            tituloVariavel2 = "";
        }
        return tituloVariavel2;
    }

    /**
     * @param tituloVariavel2
     *            the tituloVariavel2 to set
     */
    public void setTituloVariavel2(String tituloVariavel2) {
        this.tituloVariavel2 = tituloVariavel2;
    }

    /**
     * @return the tituloVariavel3
     */
    public String getTituloVariavel3() {
        if (tituloVariavel3 == null) {
            tituloVariavel3 = "";
        }
        return tituloVariavel3;
    }

    /**
     * @param tituloVariavel3
     *            the tituloVariavel3 to set
     */
    public void setTituloVariavel3(String tituloVariavel3) {
        this.tituloVariavel3 = tituloVariavel3;
    }

    /**
     * @return the planoFinanceiroCursoVO
     */
    public Integer getPlanoFinanceiroCurso() {
        if (planoFinanceiroCurso == null) {
            planoFinanceiroCurso = 0;
        }
        return planoFinanceiroCurso;
    }

    /**
     * @param planoFinanceiroCursoVO
     *            the planoFinanceiroCursoVO to set
     */
    public void setPlanoFinanceiroCurso(Integer planoFinanceiroCurso) {
        this.planoFinanceiroCurso = planoFinanceiroCurso;
    }

    /**
     * @return the utilizarValorMatriculaFixo
     */
    public Boolean getUtilizarValorMatriculaFixo() {
        if (utilizarValorMatriculaFixo == null) {
            utilizarValorMatriculaFixo = Boolean.TRUE;
        }
        return utilizarValorMatriculaFixo;
    }

    /**
     * @param utilizarValorMatriculaFixo
     *            the utilizarValorMatriculaFixo to set
     */
    public void setUtilizarValorMatriculaFixo(Boolean utilizarValorMatriculaFixo) {
        this.utilizarValorMatriculaFixo = utilizarValorMatriculaFixo;
    }

    public double obterResultadoFormula(String formula) {
        int posicao = 0;
        String formulaEsquerda = "";
        String formulaDireita = "";
        if (formula.indexOf('+') != -1) {
            posicao = formula.indexOf('+');
            formulaEsquerda = formula.substring(0, posicao);
            formulaDireita = formula.substring(posicao + 1);
            return obterResultadoFormula(formulaEsquerda) + obterResultadoFormula(formulaDireita);
        } else if (formula.indexOf('-') != -1) {
            posicao = formula.indexOf('-');
            formulaEsquerda = formula.substring(0, posicao);
            formulaDireita = formula.substring(posicao + 1);
            return obterResultadoFormula(formulaEsquerda) - obterResultadoFormula(formulaDireita);
        } else if (formula.indexOf('*') != -1) {
            posicao = formula.indexOf('*');
            formulaEsquerda = formula.substring(0, posicao);
            formulaDireita = formula.substring(posicao + 1);
            return obterResultadoFormula(formulaEsquerda) * obterResultadoFormula(formulaDireita);
        } else if (formula.indexOf('/') != -1) {
            posicao = formula.indexOf('/');
            formulaEsquerda = formula.substring(0, posicao);
            formulaDireita = formula.substring(posicao + 1);
            return obterResultadoFormula(formulaEsquerda) / obterResultadoFormula(formulaDireita);
        } else {
            return Double.parseDouble(formula);
        }
    }

    public String alterarVariaveisFormulaPorValor(String formula, Double valorVariavel1, Double valorVariavel2, Double valorVariavel3) throws Exception {
        int cont = 0;
        while (cont != -1) {
            if ((!getTituloVariavel1().equals("")) && (formula.indexOf(getTituloVariavel1()) != -1)
                    && (this.utilizarVariavel1)) {
                if (valorVariavel1 == null || valorVariavel1 < 0) {
                    throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloVariavel1()
                            + " deve ser informado.");
                }
                formula = formula.replaceFirst(getTituloVariavel1(), valorVariavel1.toString());
                cont = formula.indexOf(getTituloVariavel1());
            }
            if ((!getTituloVariavel2().equals("")) && (formula.indexOf(getTituloVariavel2()) != -1)
                    && (this.utilizarVariavel2)) {
                if (valorVariavel2 == null || valorVariavel2 < 0) {
                    throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloVariavel2()
                            + " deve ser informado.");
                }
                formula = formula.replaceFirst(getTituloVariavel2(), valorVariavel2.toString());
                cont = formula.indexOf(getTituloVariavel2());
            }
            if ((!getTituloVariavel3().equals("")) && (formula.indexOf(getTituloVariavel3()) != -1)
                    && (this.utilizarVariavel3)) {
                if (valorVariavel3 == null || valorVariavel3 < 0) {
                    throw new Exception("Não foi possível realizar o cálculo. O campo " + getTituloVariavel3()
                            + " deve ser informado.");
                }
                formula = formula.replaceFirst(getTituloVariavel3(), valorVariavel3.toString());
                cont = formula.indexOf(getTituloVariavel3());
            }
        }
        return formula;
    }

    public double obterResultadoFormulaCompleta(String formulaCalculoMediaFinal) throws Exception {
        String formula = formulaCalculoMediaFinal.trim();
        int cont = 0;
        double valorFim = 0.0;
        try {
			while (cont != 1) {
			    if ((formula.indexOf('(') == -1) && (formula.indexOf(')') == -1)) {
			        valorFim = obterResultadoFormula(formula);
			        cont = 1;
			    } else {
			        int ini = formula.lastIndexOf('(');
			        int fim = formula.indexOf(')', ini);
			        String novaFormula = formula.substring(ini + 1, fim);
			        valorFim = obterResultadoFormula(novaFormula);
			        String parteFormulaSubstituir = formula.substring(ini, fim + 1);
			        formula = formula.replace(parteFormulaSubstituir, String.valueOf(valorFim));
			        if ((formula.indexOf('(') == -1) && (formula.indexOf(')') == -1) && (formula.indexOf('+') == -1)
			                && (formula.indexOf('-') == -1) && (formula.indexOf('/') == -1) && (formula.indexOf('*') == -1)) {
			            cont = 1;
			        }
			    }
			}
		} catch (StringIndexOutOfBoundsException e) {
			throw new Exception(UteisJSF.internacionalizar("msg_CondicaoPagamentoPlanoFinaiceiroCurso_erroFormulaCalculo"));
		}
        return valorFim;
    }

    public Double realizarCalculoFormulaSubstituindoVariaveisPorValores(Double valorVariavel1, Double valorVariavel2, Double valorVariavel3) throws Exception {
        String formulaCalculo = alterarVariaveisFormulaPorValor(this.getFormulaCalculoValorFinal(), valorVariavel1, valorVariavel2, valorVariavel3);
        Double valorCalculado = obterResultadoFormulaCompleta(formulaCalculo);
        return valorCalculado;
    }

    /**
     * @return the valorFixoDisciplinaIncluida
     */
    public Double getValorFixoDisciplinaIncluida() {
        if (valorFixoDisciplinaIncluida == null) {
            valorFixoDisciplinaIncluida = 0.0;
        }
        return valorFixoDisciplinaIncluida;
    }

    /**
     * @param valorFixoDisciplinaIncluida
     *            the valorFixoDisciplinaIncluida to set
     */
    public void setValorFixoDisciplinaIncluida(Double valorFixoDisciplinaIncluida) {
        this.valorFixoDisciplinaIncluida = valorFixoDisciplinaIncluida;
    }

    /**
     * @return the valorDescontoDisciplinaExcluida
     */
    public Double getValorDescontoDisciplinaExcluida() {
        if (valorDescontoDisciplinaExcluida == null) {
            valorDescontoDisciplinaExcluida = 0.0;
        }
        return valorDescontoDisciplinaExcluida;
    }

    /**
     * @param valorDescontoDisciplinaExcluida
     *            the valorDescontoDisciplinaExcluida to set
     */
    public void setValorDescontoDisciplinaExcluida(Double valorDescontoDisciplinaExcluida) {
        this.valorDescontoDisciplinaExcluida = valorDescontoDisciplinaExcluida;
    }

    /**
     * @return the gerarParcelasSeparadasParaDisciplinasIncluidas
     */
    public Boolean getGerarParcelasSeparadasParaDisciplinasIncluidas() {
        if (gerarParcelasSeparadasParaDisciplinasIncluidas == null) {
            gerarParcelasSeparadasParaDisciplinasIncluidas = Boolean.FALSE;
        }
        return gerarParcelasSeparadasParaDisciplinasIncluidas;
    }

    /**
     * @param gerarParcelasSeparadasParaDisciplinasIncluidas
     *            the gerarParcelasSeparadasParaDisciplinasIncluidas to set
     */
    public void setGerarParcelasSeparadasParaDisciplinasIncluidas(Boolean gerarParcelasSeparadasParaDisciplinasIncluidas) {
        this.gerarParcelasSeparadasParaDisciplinasIncluidas = gerarParcelasSeparadasParaDisciplinasIncluidas;
    }

    /**
     * @return the gerarCobrancaPorDisciplinasIncluidas
     */
    public Boolean getGerarCobrancaPorDisciplinasIncluidas() {
        if (gerarCobrancaPorDisciplinasIncluidas == null) {
            gerarCobrancaPorDisciplinasIncluidas = Boolean.FALSE;
        }
        return gerarCobrancaPorDisciplinasIncluidas;
    }

    /**
     * @param gerarCobrancaPorDisciplinasIncluidas the gerarCobrancaPorDisciplinasIncluidas to set
     */
    public void setGerarCobrancaPorDisciplinasIncluidas(Boolean gerarCobrancaPorDisciplinasIncluidas) {
        this.gerarCobrancaPorDisciplinasIncluidas = gerarCobrancaPorDisciplinasIncluidas;
    }

    /**
     * @return the gerarDescontoPorDiscipliaExcluidas
     */
    public Boolean getGerarDescontoPorDiscipliaExcluidas() {
        if (gerarDescontoPorDiscipliaExcluidas == null) {
            gerarDescontoPorDiscipliaExcluidas = Boolean.FALSE;
        }
        return gerarDescontoPorDiscipliaExcluidas;
    }

    /**
     * @param gerarDescontoPorDiscipliaExcluidas the gerarDescontoPorDiscipliaExcluidas to set
     */
    public void setGerarDescontoPorDiscipliaExcluidas(Boolean gerarDescontoPorDiscipliaExcluidas) {
        this.gerarDescontoPorDiscipliaExcluidas = gerarDescontoPorDiscipliaExcluidas;
    }

    /**
     * @return the gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo
     */
    public Boolean getGerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo() {
        if (gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo == null) {
            gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo = Boolean.FALSE;
        }
        return gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo;
    }

    /**
     * @param gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo the gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo to set
     */
    public void setGerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo(Boolean gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo) {
        this.gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo = gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo;
    }

    /**
     * @return the numeroMaximoDisciplinaCursarParaGerarDescontos
     */
    public Integer getNumeroMaximoDisciplinaCursarParaGerarDescontos() {
        if (numeroMaximoDisciplinaCursarParaGerarDescontos == null) {
            numeroMaximoDisciplinaCursarParaGerarDescontos = 0;
        }
        return numeroMaximoDisciplinaCursarParaGerarDescontos;
    }

    /**
     * @param numeroMaximoDisciplinaCursarParaGerarDescontos the numeroMaximoDisciplinaCursarParaGerarDescontos to set
     */
    public void setNumeroMaximoDisciplinaCursarParaGerarDescontos(Integer numeroMaximoDisciplinaCursarParaGerarDescontos) {
        this.numeroMaximoDisciplinaCursarParaGerarDescontos = numeroMaximoDisciplinaCursarParaGerarDescontos;
    }

    /**
     * @return the ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas
     */
    public Boolean getRatiarValorDiferencaInclusaoExclusaoParaTodasParcelas() {
        if (ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas == null) {
            ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas = Boolean.FALSE;
        }
        return ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas;
    }

    /**
     * @param ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas the ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas to set
     */
    public void setRatiarValorDiferencaInclusaoExclusaoParaTodasParcelas(Boolean ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas) {
        this.ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas = ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas;
    }

    /**
     * @return the descricaoDuracao
     */
    public String getDescricaoDuracao() {
        if (descricaoDuracao == null) {
            descricaoDuracao = "";
        }
        return descricaoDuracao;
    }

    /**
     * @param descricaoDuracao the descricaoDuracao to set
     */
    public void setDescricaoDuracao(String descricaoDuracao) {
        this.descricaoDuracao = descricaoDuracao;
    }

    /**
     * @return the textoPadraoContratoMatricula
     */
    public TextoPadraoVO getTextoPadraoContratoMatricula() {
        if (textoPadraoContratoMatricula == null) {
            textoPadraoContratoMatricula = new TextoPadraoVO();
        }
        return textoPadraoContratoMatricula;
    }

    /**
     * @param textoPadraoContratoMatricula the textoPadraoContratoMatricula to set
     */
    public void setTextoPadraoContratoMatricula(TextoPadraoVO textoPadraoContratoMatricula) {
        this.textoPadraoContratoMatricula = textoPadraoContratoMatricula;
    }

    public CondicaoPagamentoPlanoFinanceiroCursoVO clonar(CondicaoPagamentoPlanoFinanceiroCursoVO cond) {
        CondicaoPagamentoPlanoFinanceiroCursoVO condFinal = new CondicaoPagamentoPlanoFinanceiroCursoVO();
        condFinal.getDescontoProgressivoPadrao().setCodigo(cond.getDescontoProgressivoPadrao().getCodigo());
        condFinal.getDescontoProgressivoPadraoMatricula().setCodigo(cond.getDescontoProgressivoPadraoMatricula().getCodigo());
        condFinal.setDescricao("");
        condFinal.setDescricaoDuracao(cond.getDescricaoDuracao());
        condFinal.setFormulaCalculoValorFinal(cond.getFormulaCalculoValorFinal());
        condFinal.setFormulaCalculoVariavel1(cond.getFormulaCalculoVariavel1());
        condFinal.setFormulaCalculoVariavel2(cond.getFormulaCalculoVariavel2());
        condFinal.setFormulaCalculoVariavel3(cond.getFormulaCalculoVariavel3());
        condFinal.setFormulaUsoVariavel1(cond.getFormulaUsoVariavel1());
        condFinal.setFormulaUsoVariavel2(cond.getFormulaUsoVariavel2());
        condFinal.setFormulaUsoVariavel3(cond.getFormulaUsoVariavel3());

        condFinal.setGerarCobrancaPorDisciplinasIncluidas(cond.getGerarCobrancaPorDisciplinasIncluidas());
        condFinal.setGerarDescontoPorDiscipliaExcluidas(cond.getGerarDescontoPorDiscipliaExcluidas());
        condFinal.setGerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo(cond.getGerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo());
        condFinal.setGerarParcelasSeparadasParaDisciplinasIncluidas(cond.getGerarParcelasSeparadasParaDisciplinasIncluidas());
        condFinal.setNomeFormula(cond.getNomeFormula());
        condFinal.setNrParcelasPeriodo(cond.getNrParcelasPeriodo());
        condFinal.setNumeroMaximoDisciplinaCursarParaGerarDescontos(cond.getNumeroMaximoDisciplinaCursarParaGerarDescontos());
//        condFinal.getPlanoDescontoPadrao().setCodigo(cond.getPlanoDescontoPadrao().getCodigo());
        condFinal.setPlanoFinanceiroCurso(cond.getPlanoFinanceiroCurso());
        condFinal.setRatiarValorDiferencaInclusaoExclusaoParaTodasParcelas(cond.getRatiarValorDiferencaInclusaoExclusaoParaTodasParcelas());
        condFinal.setRestituirDiferencaValorMatriculaSistemaPorCredito(cond.getRestituirDiferencaValorMatriculaSistemaPorCredito());
        condFinal.getTextoPadraoContratoMatricula().setCodigo(cond.getTextoPadraoContratoMatricula().getCodigo());
        condFinal.setTituloVariavel1(cond.getTituloVariavel1());
        condFinal.setTituloVariavel2(cond.getTituloVariavel2());
        condFinal.setTituloVariavel3(cond.getTituloVariavel3());
        condFinal.setUtilizarValorMatriculaFixo(cond.getUtilizarValorMatriculaFixo());
        condFinal.setUtilizarVariavel1(cond.getUtilizarVariavel1());
        condFinal.setUtilizarVariavel2(cond.getUtilizarVariavel2());
        condFinal.setUtilizarVariavel3(cond.getUtilizarVariavel3());
        condFinal.setValorDescontoDisciplinaExcluida(cond.getValorDescontoDisciplinaExcluida());
        condFinal.setValorFixoDisciplinaIncluida(cond.getValorFixoDisciplinaIncluida());
        condFinal.setGerarDescontoPorDiscipliaExcluidas(cond.getGerarDescontoPorDiscipliaExcluidas());
        condFinal.setValorMatricula(cond.getValorMatricula());
        condFinal.setValorMatriculaSistemaPorCredito(cond.getValorMatriculaSistemaPorCredito());
        condFinal.setValorMinimoParcelaSistemaPorCredito(cond.getValorMinimoParcelaSistemaPorCredito());
        condFinal.setValorParcela(cond.getValorParcela());
        condFinal.setValorPrimeiraParcela(cond.getValorPrimeiraParcela());
        condFinal.setValorUnitarioCredito(cond.getValorUnitarioCredito());
        condFinal.setVariavel1(cond.getVariavel1());
        condFinal.setVariavel2(cond.getVariavel2());
        condFinal.setVariavel3(cond.getVariavel3());
        for(CondicaoPlanoFinanceiroCursoTurmaVO obj:cond.getCondicaoPlanoFinanceiroCursoTurmaVOs()){
            CondicaoPlanoFinanceiroCursoTurmaVO objClone = new CondicaoPlanoFinanceiroCursoTurmaVO();
            objClone.getDisciplina().setCodigo(obj.getDisciplina().getCodigo());
            objClone.getDisciplina().setNome(obj.getDisciplina().getNome());
//            objClone.getDisciplina().setNrCreditos(obj.getDisciplina().getNrCreditos());
//            objClone.getDisciplina().setCargaHoraria(obj.getDisciplina().getCargaHoraria());
            objClone.setValor(obj.getValor());
            objClone.setNovoObj(true);
            condFinal.getCondicaoPlanoFinanceiroCursoTurmaVOs().add(objClone);
        }
        condFinal.setSituacao("EL");
        condFinal.setNovoObj(true);
        //aba Material Didatico
        condFinal.setGerarParcelaMaterialDidatico(cond.isGerarParcelaMaterialDidatico());
        condFinal.setUsarUnidadeEnsinoEspecifica(cond.isUsarUnidadeEnsinoEspecifica());
        condFinal.setControlaDiaBaseVencimentoParcelaMaterialDidatico(cond.isControlaDiaBaseVencimentoParcelaMaterialDidatico());
        condFinal.setAplicarDescontosParcelasNoMaterialDidatico(cond.isAplicarDescontosParcelasNoMaterialDidatico());
        condFinal.setAplicarDescontoMaterialDidaticoDescontoAluno(cond.isAplicarDescontoMaterialDidaticoDescontoAluno());
        condFinal.setAplicarDescontoMaterialDidaticoDescontoInstitucional(cond.isAplicarDescontoMaterialDidaticoDescontoInstitucional());
        condFinal.setAplicarDescontoMaterialDidaticoDescontoProgressivo(cond.isAplicarDescontoMaterialDidaticoDescontoProgressivo());
        condFinal.setAplicarDescontoMaterialDidaticoDescontoConvenio(cond.isAplicarDescontoMaterialDidaticoDescontoConvenio());
        condFinal.setConsiderarParcelasMaterialDidaticoReajustePreco(cond.isConsiderarParcelasMaterialDidaticoReajustePreco());
        condFinal.setAplicarDescontosDesconsiderandosVencimento(cond.isAplicarDescontosDesconsiderandosVencimento());
        condFinal.getUnidadeEnsinoFinanceira().setCodigo(cond.getUnidadeEnsinoFinanceira().getCodigo());
        condFinal.setQuantidadeParcelasMaterialDidatico(cond.getQuantidadeParcelasMaterialDidatico());
        condFinal.setDiaBaseVencimentoParcelaOutraUnidade(cond.getDiaBaseVencimentoParcelaOutraUnidade());
        condFinal.setValorPorParcelaMaterialDidatico(cond.getValorPorParcelaMaterialDidatico());
        condFinal.setTipoGeracaoMaterialDidatico(cond.getTipoGeracaoMaterialDidatico());
        //Aba Outras configuracoes
        condFinal.setApresentarCondicaoVisaoAluno(cond.getApresentarCondicaoVisaoAluno());
        condFinal.setCentroReceita(cond.getCentroReceita());
        condFinal.setApresentarSomenteParaDeterminadaFormaIngresso(cond.getApresentarSomenteParaDeterminadaFormaIngresso());
        condFinal.setApresentarSomenteParaIngressanteNoSemestreAno(cond.getApresentarSomenteParaIngressanteNoSemestreAno());
        condFinal.setFormaIngressoEnem(cond.getFormaIngressoEnem());
        condFinal.setFormaIngressoEntrevista(cond.getFormaIngressoEntrevista());
        condFinal.setFormaIngressoOutroTipoSelecao(cond.getFormaIngressoOutroTipoSelecao());
        condFinal.setFormaIngressoPortadorDiploma(cond.getFormaIngressoPortadorDiploma());
        condFinal.setFormaIngressoProcessoSeletivo(cond.getFormaIngressoProcessoSeletivo());
        condFinal.setFormaIngressoProuni(cond.getFormaIngressoProuni());
        condFinal.setFormaIngressoReingresso(cond.getFormaIngressoReingresso());
        condFinal.setFormaIngressoTransferenciaExterna(cond.getFormaIngressoTransferenciaExterna());
        condFinal.setFormaIngressoTransferenciaInterna(cond.getFormaIngressoTransferenciaInterna());
        condFinal.setSemestreIngressante(cond.getSemestreIngressante());
        condFinal.setAnoIngressante(cond.getAnoIngressante());
        condFinal.setApresentarSomenteParaAlunosIntegralizandoCurso(cond.getApresentarSomenteParaAlunosIntegralizandoCurso());
        condFinal.setConsiderarIntegralizandoEstiverCursandoUltimoPer2Vez(cond.getConsiderarIntegralizandoEstiverCursandoUltimoPer2Vez());
        condFinal.setTipoControleAlunoIntegralizandoCurso(cond.getTipoControleAlunoIntegralizandoCurso());
        condFinal.setValorBaseDefinirAlunoIntegralizandoCurso(cond.getValorBaseDefinirAlunoIntegralizandoCurso());
        condFinal.setPermiteRecebimentoOnlineCartaoCredito(cond.getPermiteRecebimentoOnlineCartaoCredito());
        condFinal.setApresentarMatriculaOnlineExterna(cond.getApresentarMatriculaOnlineExterna());
        condFinal.setApresentarMatriculaOnlineProfessor(cond.getApresentarMatriculaOnlineProfessor());
        condFinal.setApresentarMatriculaOnlineCoordenador(cond.getApresentarMatriculaOnlineCoordenador());
        return condFinal;
    }

    /**
     * @return the dataAtivacao
     */
    public Date getDataAtivacao() {
        if (dataAtivacao == null) {
            dataAtivacao = new Date();
        }
        return dataAtivacao;
    }

    /**
     * @param dataAtivacao the dataAtivacao to set
     */
    public void setDataAtivacao(Date dataAtivacao) {
        this.dataAtivacao = dataAtivacao;
    }

    /**
     * @return the responsavelAtivacao
     */
    public UsuarioVO getResponsavelAtivacao() {
        if (responsavelAtivacao == null) {
            responsavelAtivacao = new UsuarioVO();
        }
        return responsavelAtivacao;
    }

    /**
     * @param responsavelAtivacao the responsavelAtivacao to set
     */
    public void setResponsavelAtivacao(UsuarioVO responsavelAtivacao) {
        this.responsavelAtivacao = responsavelAtivacao;
    }

    /**
     * @return the dataInativacao
     */
    public Date getDataInativacao() {
        return dataInativacao;
    }

    /**
     * @param dataInativacao the dataInativacao to set
     */
    public void setDataInativacao(Date dataInativacao) {
        this.dataInativacao = dataInativacao;
    }

    /**
     * @return the responsavelInativacao
     */
    public UsuarioVO getResponsavelInativacao() {
        if (responsavelInativacao == null) {
            responsavelInativacao = new UsuarioVO();
        }
        return responsavelInativacao;
    }

    /**
     * @param responsavelInativacao the responsavelInativacao to set
     */
    public void setResponsavelInativacao(UsuarioVO responsavelInativacao) {
        this.responsavelInativacao = responsavelInativacao;
    }

    /**
     * @return the situacao
     */
    public String getSituacao() {
        if (situacao == null) {
            situacao = "EL";
        }
        return situacao;
    }

    public String getSituacao_Apresentar() {
        if (situacao == null) {
            return "Em Elaboração";
        }
        if (situacao.equals("EL")) {
            return "Em Elaboração";
        }
        if (situacao.equals("AT")) {
            return "Ativo";
        }
        if (situacao.equals("IN")) {
            return "Inativo";
        }
        return "";
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    
    public Boolean getIsAtivo() {
        if (getSituacao().equals("AT")) {
            return true;
        }
        return false;
    }

    public Boolean getIsAtivar() {
        if (getSituacao().equals("EL") || getSituacao().equals("IN")) {
            return true;
        }
        return false;
    }

    public Boolean getIsInativar() {
        if (getSituacao().equals("AT")) {
            return true;
        }
        return false;
    }

    /**
     * @return the naoControlarMatricula
     */
    public Boolean getNaoControlarMatricula() {
        if (naoControlarMatricula == null) {
            naoControlarMatricula = Boolean.FALSE;
        }
        return naoControlarMatricula;
    }

    /**
     * @param naoControlarMatricula the naoControlarMatricula to set
     */
    public void setNaoControlarMatricula(Boolean naoControlarMatricula) {
        this.naoControlarMatricula = naoControlarMatricula;
    }

    public Boolean getApresentarCondicaoVisaoAluno() {
        if (apresentarCondicaoVisaoAluno == null) {
            apresentarCondicaoVisaoAluno = Boolean.FALSE;
        }
        return apresentarCondicaoVisaoAluno;
    }

    public void setApresentarCondicaoVisaoAluno(Boolean apresentarCondicaoVisaoAluno) {
        this.apresentarCondicaoVisaoAluno = apresentarCondicaoVisaoAluno;
    }

    /**
     * @return the definirPlanoDescontoApresentarMatricula
     */
    public Boolean getDefinirPlanoDescontoApresentarMatricula() {
        return definirPlanoDescontoApresentarMatricula;
    }

    /**
     * @param definirPlanoDescontoApresentarMatricula the definirPlanoDescontoApresentarMatricula to set
     */
    public void setDefinirPlanoDescontoApresentarMatricula(Boolean definirPlanoDescontoApresentarMatricula) {
        this.definirPlanoDescontoApresentarMatricula = definirPlanoDescontoApresentarMatricula;
    }

    /**
     * @return the planoDescontoDisponivelMatriculaVOs
     */
    public List<PlanoDescontoDisponivelMatriculaVO> getPlanoDescontoDisponivelMatriculaVOs() {
        if (planoDescontoDisponivelMatriculaVOs == null) {
            planoDescontoDisponivelMatriculaVOs = new ArrayList<PlanoDescontoDisponivelMatriculaVO>(0);
        }
        return planoDescontoDisponivelMatriculaVOs;
    }

    /**
     * @param planoDescontoDisponivelMatriculaVOs the planoDescontoDisponivelMatriculaVOs to set
     */
    public void setPlanoDescontoDisponivelMatriculaVOs(List<PlanoDescontoDisponivelMatriculaVO> planoDescontoDisponivelMatriculaVOs) {
        this.planoDescontoDisponivelMatriculaVOs = planoDescontoDisponivelMatriculaVOs;
    }

    public void adicionarPlanoDescontoDisponivelMatriculaVOs(PlanoDescontoDisponivelMatriculaVO planoDescontoDisponivelMatriculaVO) throws ConsistirException {
        int index = 0;
        Iterator i = getPlanoDescontoDisponivelMatriculaVOs().iterator();
        while (i.hasNext()) {
            PlanoDescontoDisponivelMatriculaVO objExistente = (PlanoDescontoDisponivelMatriculaVO) i.next();
            if (objExistente.getPlanoDesconto().getCodigo().intValue() == planoDescontoDisponivelMatriculaVO.getPlanoDesconto().getCodigo().intValue()) {
                getPlanoDescontoDisponivelMatriculaVOs().set(index, planoDescontoDisponivelMatriculaVO);
                return;
            }
            index++;
        }
        getPlanoDescontoDisponivelMatriculaVOs().add(planoDescontoDisponivelMatriculaVO);
    }

    public void excluirObjPlanoDescontoDisponivelMatriculaVOs(Integer planoDesconto) throws Exception {
        int index = 0;
        Iterator i = getPlanoDescontoDisponivelMatriculaVOs().iterator();
        while (i.hasNext()) {
            PlanoDescontoDisponivelMatriculaVO objExistente = (PlanoDescontoDisponivelMatriculaVO) i.next();
            if (objExistente.getPlanoDesconto().getCodigo().intValue() == planoDesconto) {
                getPlanoDescontoDisponivelMatriculaVOs().remove(index);
                return;
            }
            index++;
        }
    }
    
    

    
    public List<CondicaoPlanoFinanceiroCursoTurmaVO> getCondicaoPlanoFinanceiroCursoTurmaVOs() {
        if(condicaoPlanoFinanceiroCursoTurmaVOs==null){
            condicaoPlanoFinanceiroCursoTurmaVOs = new ArrayList<CondicaoPlanoFinanceiroCursoTurmaVO>(0);
        }
        return condicaoPlanoFinanceiroCursoTurmaVOs;
    }

    
    public void setCondicaoPlanoFinanceiroCursoTurmaVOs(List<CondicaoPlanoFinanceiroCursoTurmaVO> condicaoPlanoFinanceiroCursoTurmaVOs) {
        this.condicaoPlanoFinanceiroCursoTurmaVOs = condicaoPlanoFinanceiroCursoTurmaVOs;
    }

	public Boolean getNaoRegerarParcelaVencida() {
		if(naoRegerarParcelaVencida == null){
			naoRegerarParcelaVencida = false;
		}
		return naoRegerarParcelaVencida;
	}

	public void setNaoRegerarParcelaVencida(Boolean naoRegerarParcelaVencida) {
		this.naoRegerarParcelaVencida = naoRegerarParcelaVencida;
	}

	public CentroReceitaVO getCentroReceita() {
		if(centroReceita == null){
			centroReceita = new CentroReceitaVO();
		}
		return centroReceita;
	}

	public void setCentroReceita(CentroReceitaVO centroReceita) {
		this.centroReceita = centroReceita;
	}

	public Boolean getAplicarCalculoComBaseDescontosCalculados() {
		if (aplicarCalculoComBaseDescontosCalculados == null) {
			aplicarCalculoComBaseDescontosCalculados = Boolean.TRUE;
		}
		return aplicarCalculoComBaseDescontosCalculados;
	}

	public void setAplicarCalculoComBaseDescontosCalculados(Boolean aplicarCalculoComBaseDescontosCalculados) {
		this.aplicarCalculoComBaseDescontosCalculados = aplicarCalculoComBaseDescontosCalculados;
	}

	public DescontoProgressivoVO getDescontoProgressivoPrimeiraParcela() {
		if (descontoProgressivoPrimeiraParcela == null) {
			descontoProgressivoPrimeiraParcela = new DescontoProgressivoVO();
		}
		return descontoProgressivoPrimeiraParcela;
	}

	public void setDescontoProgressivoPrimeiraParcela(DescontoProgressivoVO descontoProgressivoPrimeiraParcela) {
		this.descontoProgressivoPrimeiraParcela = descontoProgressivoPrimeiraParcela;
	}

	public List<CondicaoPagamentoPlanoDescontoVO> getCondicaoPagamentoPlanoDescontoVOs() {
		if (condicaoPagamentoPlanoDescontoVOs == null) {
			condicaoPagamentoPlanoDescontoVOs = new ArrayList<CondicaoPagamentoPlanoDescontoVO>(0);
		}
		return condicaoPagamentoPlanoDescontoVOs;
	}

	public void setCondicaoPagamentoPlanoDescontoVOs(List<CondicaoPagamentoPlanoDescontoVO> condicaoPagamentoPlanoDescontoVOs) {
		this.condicaoPagamentoPlanoDescontoVOs = condicaoPagamentoPlanoDescontoVOs;
	}

    /**
     * @return the tipoUsoCondicaoPagamento
     */
    public String getTipoUsoCondicaoPagamento() {
        if (tipoUsoCondicaoPagamento == null) {
            tipoUsoCondicaoPagamento = "MATRICULA_REGULAR";
        }
        return tipoUsoCondicaoPagamento;
    }
    
    public Boolean getTipoUsoIsMatriculaEspecial() {
        if (this.getTipoUsoCondicaoPagamento().equals("MATRICULA_ESPECIAL")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    public Boolean getTipoUsoIsMatriculaRegular() {
        if (this.getTipoUsoCondicaoPagamento().equals("MATRICULA_REGULAR")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    public String getTipoUsoCondicaoPagamento_Apresentar() {
        if (getTipoUsoCondicaoPagamento().equals("MATRICULA_REGULAR")) {
            return "Matrícula - Renovação Regular";
        }
        if (getTipoUsoCondicaoPagamento().equals("MATRICULA_ESPECIAL")) {
            return "Matrícula Especial - Somente para concluir atividades obrigatórias";
        }
        return "";
    }

    /**
     * @param tipoUsoCondicaoPagamento the tipoUsoCondicaoPagamento to set
     */
    public void setTipoUsoCondicaoPagamento(String tipoUsoCondicaoPagamento) {
        this.tipoUsoCondicaoPagamento = tipoUsoCondicaoPagamento;
    }

    /**
     * @return the tipoCalculoParcela
     */
    public String getTipoCalculoParcela() {
        if (tipoCalculoParcela == null) {
            tipoCalculoParcela = "VF";
        }
        return tipoCalculoParcela;
    }
    
    public String getTipoCalculoParcela_Apresentar() {
        if (getTipoCalculoParcela().equals("VF")) {
            return "Valor Fixo";
        }
        if (getTipoCalculoParcela().equals("VC")) {
            return "Valor por Total Créditos";
        }
        if (getTipoCalculoParcela().equals("FC")) {
            return "Valor por Forma Cálculo";
        }
        if (getTipoCalculoParcela().equals("VD")) {
            return "Valor por Disciplina";
        }
        return (getTipoCalculoParcela());
    }

    /**
     * @param tipoCalculoParcela the tipoCalculoParcela to set
     */
    public void setTipoCalculoParcela(String tipoCalculoParcela) {
        this.tipoCalculoParcela = tipoCalculoParcela;
    }
 
    public Boolean utilizaValorMatriculaMensalidadeBaseadoNrCreditos() {
        if (getTipoCalculoParcela().equals("VC")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean utilizaValorMatriculaMensalidadeBaseadoFormaCalculo() {
        if (getTipoCalculoParcela().equals("FC")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean utilizaValorMatriculaMensalidadeFixo() {
        if (getTipoCalculoParcela().equals("VF")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }    
    
    public Boolean getIsValorPorDisciplina(){
        return getTipoCalculoParcela().equals("VD");
    }

    /**
     * @return the tipoCobrancaInclusaoDisciplinaRegular
     */
    public String getTipoCobrancaInclusaoDisciplinaRegular() {
        if (tipoCobrancaInclusaoDisciplinaRegular == null) {
            tipoCobrancaInclusaoDisciplinaRegular = "VF";
        }
        return tipoCobrancaInclusaoDisciplinaRegular;
    }
    
    public String getTipoCobrancaInclusaoDisciplinaRegular_Apresentar() {
        if (getTipoCobrancaInclusaoDisciplinaRegular().equals("VF")) {
            return "Valor Fixo";
        }
        if (getTipoCobrancaInclusaoDisciplinaRegular().equals("VC")) {
            return "Valor por Crédito";
        }
        if (getTipoCobrancaInclusaoDisciplinaRegular().equals("VH")) {
            return "Valor por Hora Disciplina";
        }
        return "";
    }

    /**
     * @param tipoCobrancaInclusaoDisciplinaRegular the tipoCobrancaInclusaoDisciplinaRegular to set
     */
    public void setTipoCobrancaInclusaoDisciplinaRegular(String tipoCobrancaInclusaoDisciplinaRegular) {
        this.tipoCobrancaInclusaoDisciplinaRegular = tipoCobrancaInclusaoDisciplinaRegular;
    }

    /**
     * @return the cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo
     */
    public Boolean getCobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo() {
        if (cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo == null) {
            cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo = Boolean.FALSE;
        }
        return cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo;
    }

    /**
     * @param cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo the cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo to set
     */
    public void setCobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo(Boolean cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo) {
        this.cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo = cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo;
    }

    /**
     * @return the tipoCobrancaInclusaoDisciplinaDependencia
     */
    public String getTipoCobrancaInclusaoDisciplinaDependencia() {
        if (tipoCobrancaInclusaoDisciplinaDependencia == null) {
            tipoCobrancaInclusaoDisciplinaDependencia = "VF";
        }
        return tipoCobrancaInclusaoDisciplinaDependencia;
    }
    
    public String getTipoCobrancaInclusaoDisciplinaDependencia_Apresentar() {
        if (getTipoCobrancaInclusaoDisciplinaDependencia().equals("VF")) {
            return "Valor Fixo";
        }
        if (getTipoCobrancaInclusaoDisciplinaDependencia().equals("VC")) {
            return "Valor por Crédito";
        }
        if (getTipoCobrancaInclusaoDisciplinaDependencia().equals("VH")) {
            return "Valor por Hora Disciplina";
        }
        return "";
    }

    /**
     * @param tipoCobrancaInclusaoDisciplinaDependencia the tipoCobrancaInclusaoDisciplinaDependencia to set
     */
    public void setTipoCobrancaInclusaoDisciplinaDependencia(String tipoCobrancaInclusaoDisciplinaDependencia) {
        this.tipoCobrancaInclusaoDisciplinaDependencia = tipoCobrancaInclusaoDisciplinaDependencia;
    }

    /**
     * @return the cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet
     */
    public Boolean getCobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet() {
        if (cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet == null) {
            cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet = Boolean.FALSE;
        }
        return cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet;
    }

    /**
     * @param cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet the cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet to set
     */
    public void setCobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet(Boolean cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet) {
        this.cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet = cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet;
    }

    /**
     * @return the tipoCobrancaAtividadeComplementar
     */
    public String getTipoCobrancaAtividadeComplementar() {
        if (tipoCobrancaAtividadeComplementar == null) {
            tipoCobrancaAtividadeComplementar = "VF";
        }
        return tipoCobrancaAtividadeComplementar;
    }

    /**
     * @param tipoCobrancaAtividadeComplementar the tipoCobrancaAtividadeComplementar to set
     */
    public void setTipoCobrancaAtividadeComplementar(String tipoCobrancaAtividadeComplementar) {
        this.tipoCobrancaAtividadeComplementar = tipoCobrancaAtividadeComplementar;
    }

    /**
     * @return the nrCreditoPorAtividadeComplementar
     */
    public Integer getNrCreditoPorAtividadeComplementar() {
        if (nrCreditoPorAtividadeComplementar == null) {
            nrCreditoPorAtividadeComplementar = 0;
        }
        return nrCreditoPorAtividadeComplementar;
    }

    /**
     * @param nrCreditoPorAtividadeComplementar the nrCreditoPorAtividadeComplementar to set
     */
    public void setNrCreditoPorAtividadeComplementar(Integer nrCreditoPorAtividadeComplementar) {
        this.nrCreditoPorAtividadeComplementar = nrCreditoPorAtividadeComplementar;
    }

    /**
     * @return the valorPorAtividadeComplementar
     */
    public Double getValorPorAtividadeComplementar() {
        if (valorPorAtividadeComplementar == null) {
            valorPorAtividadeComplementar = 0.0;
        }
        return valorPorAtividadeComplementar;
    }

    /**
     * @param valorPorAtividadeComplementar the valorPorAtividadeComplementar to set
     */
    public void setValorPorAtividadeComplementar(Double valorPorAtividadeComplementar) {
        this.valorPorAtividadeComplementar = valorPorAtividadeComplementar;
    }

    /**
     * @return the tipoCobrancaENADE
     */
    public String getTipoCobrancaENADE() {
        if (tipoCobrancaENADE == null) {
            tipoCobrancaENADE = "VF";
        }
        return tipoCobrancaENADE;
    }

    /**
     * @param tipoCobrancaENADE the tipoCobrancaENADE to set
     */
    public void setTipoCobrancaENADE(String tipoCobrancaENADE) {
        this.tipoCobrancaENADE = tipoCobrancaENADE;
    }

    /**
     * @return the nrCreditoPorENADE
     */
    public Integer getNrCreditoPorENADE() {
        if (nrCreditoPorENADE == null) {
            nrCreditoPorENADE = 0;
        }
        return nrCreditoPorENADE;
    }

    /**
     * @param nrCreditoPorENADE the nrCreditoPorENADE to set
     */
    public void setNrCreditoPorENADE(Integer nrCreditoPorENADE) {
        this.nrCreditoPorENADE = nrCreditoPorENADE;
    }

    /**
     * @return the valorPorENADE
     */
    public Double getValorPorENADE() {
        if (valorPorENADE == null) {
            valorPorENADE = 0.0;
        }
        return valorPorENADE;
    }

    /**
     * @param valorPorENADE the valorPorENADE to set
     */
    public void setValorPorENADE(Double valorPorENADE) {
        this.valorPorENADE = valorPorENADE;
    }

    /**
     * @return the tipoCobrancaEstagio
     */
    public String getTipoCobrancaEstagio() {
        if (tipoCobrancaEstagio == null) {
            tipoCobrancaEstagio = "VF";
        }
        return tipoCobrancaEstagio;
    }

    /**
     * @param tipoCobrancaEstagio the tipoCobrancaEstagio to set
     */
    public void setTipoCobrancaEstagio(String tipoCobrancaEstagio) {
        this.tipoCobrancaEstagio = tipoCobrancaEstagio;
    }

    /**
     * @return the nrCreditoPorEstagio
     */
    public Integer getNrCreditoPorEstagio() {
        if (nrCreditoPorEstagio == null) {
            nrCreditoPorEstagio = 0;
        }
        return nrCreditoPorEstagio;
    }

    /**
     * @param nrCreditoPorEstagio the nrCreditoPorEstagio to set
     */
    public void setNrCreditoPorEstagio(Integer nrCreditoPorEstagio) {
        this.nrCreditoPorEstagio = nrCreditoPorEstagio;
    }

    /**
     * @return the valorPorEstagio
     */
    public Double getValorPorEstagio() {
        if (valorPorEstagio == null) {
            valorPorEstagio = 0.0;
        }
        return valorPorEstagio;
    }

    /**
     * @param valorPorEstagio the valorPorEstagio to set
     */
    public void setValorPorEstagio(Double valorPorEstagio) {
        this.valorPorEstagio = valorPorEstagio;
    }
    
    public Boolean getIsTipoCobrancaAtividadeComplementarPorCredito() {
        if (this.getTipoCobrancaAtividadeComplementar().equals("VC")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    public Boolean getIsTipoCobrancaENADEPorCredito() {
        if (this.getTipoCobrancaENADE().equals("VC")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getIsTipoCobrancaEstagioPorCredito() {
        if (this.getTipoCobrancaEstagio().equals("VC")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    public void atualizarValorCobrarPorAtividadeComplementar() {
        if (this.getTipoCobrancaAtividadeComplementar().equals("VC")) {
            this.setValorPorAtividadeComplementar(this.getValorUnitarioCredito() * this.getNrCreditoPorAtividadeComplementar());
        }
    }
    
    public void atualizarValorCobrarPorENADE() {
        if (this.getTipoCobrancaENADE().equals("VC")) {
            this.setValorPorENADE(this.getValorUnitarioCredito() * this.getNrCreditoPorENADE());
        }
    }

    public void atualizarValorCobrarPorEstagio() {
        if (this.getTipoCobrancaEstagio().equals("VC")) {
            this.setValorPorEstagio(this.getValorUnitarioCredito() * this.getNrCreditoPorEstagio());
        }
    }    
    
    public Boolean getUtilizaTipoCobrancaPorCreditoParaMatriculaEspecial() {
        if ((this.getIsTipoCobrancaAtividadeComplementarPorCredito()) ||
            (this.getIsTipoCobrancaEstagioPorCredito()) ||
            (this.getIsTipoCobrancaENADEPorCredito())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @return the valorDisciplinaIncluidaDependencia
     */
    public Double getValorDisciplinaIncluidaDependencia() {
        if (valorDisciplinaIncluidaDependencia == null) {
            valorDisciplinaIncluidaDependencia = 0.0;
        }
        return valorDisciplinaIncluidaDependencia;
    }

    /**
     * @param valorDisciplinaIncluidaDependencia the valorDisciplinaIncluidaDependencia to set
     */
    public void setValorDisciplinaIncluidaDependencia(Double valorDisciplinaIncluidaDependencia) {
        this.valorDisciplinaIncluidaDependencia = valorDisciplinaIncluidaDependencia;
    }

    /**
     * @return the valorDisciplinaIncluidaDependenciaEAD
     */
    public Double getValorDisciplinaIncluidaDependenciaEAD() {
        if (valorDisciplinaIncluidaDependenciaEAD == null) {
            valorDisciplinaIncluidaDependenciaEAD = 0.0;
        }
        return valorDisciplinaIncluidaDependenciaEAD;
    }

    /**
     * @param valorDisciplinaIncluidaDependenciaEAD the valorDisciplinaIncluidaDependenciaEAD to set
     */
    public void setValorDisciplinaIncluidaDependenciaEAD(Double valorDisciplinaIncluidaDependenciaEAD) {
        this.valorDisciplinaIncluidaDependenciaEAD = valorDisciplinaIncluidaDependenciaEAD;
    }

    /**
     * @return the utilizarPoliticaDescontoDiscipRegularFeitasViaEAD
     */
    public Boolean getUtilizarPoliticaDescontoDiscipRegularFeitasViaEAD() {
        if (utilizarPoliticaDescontoDiscipRegularFeitasViaEAD == null) {
            utilizarPoliticaDescontoDiscipRegularFeitasViaEAD = Boolean.FALSE;
        }
        return utilizarPoliticaDescontoDiscipRegularFeitasViaEAD;
    }

    /**
     * @param utilizarPoliticaDescontoDiscipRegularFeitasViaEAD the utilizarPoliticaDescontoDiscipRegularFeitasViaEAD to set
     */
    public void setUtilizarPoliticaDescontoDiscipRegularFeitasViaEAD(Boolean utilizarPoliticaDescontoDiscipRegularFeitasViaEAD) {
        this.utilizarPoliticaDescontoDiscipRegularFeitasViaEAD = utilizarPoliticaDescontoDiscipRegularFeitasViaEAD;
    }

    /**
     * @return the tipoDescontoDisciplinaRegularEAD
     */
    public String getTipoDescontoDisciplinaRegularEAD() {
        if (tipoDescontoDisciplinaRegularEAD == null) {
            tipoDescontoDisciplinaRegularEAD = "VF";
        }
        return tipoDescontoDisciplinaRegularEAD;
    }
    
    public String getTipoDescontoDisciplinaRegularEAD_Apresentar() {
        if (getTipoDescontoDisciplinaRegularEAD().equals("PE")) {
            return "% sobre Valor Disciplina";
        }
        if (getTipoDescontoDisciplinaRegularEAD().equals("VF")) {
            return "Valor Fixo por Disciplina";
        }
        if (getTipoDescontoDisciplinaRegularEAD().equals("VC")) {
            return "Valor por Crédito da Disciplina";
        }
        if (getTipoDescontoDisciplinaRegularEAD().equals("VH")) {
            return "Valor por Hora da Disciplina";
        }
        return "";
    }

    /**
     * @param tipoDescontoDisciplinaRegularEAD the tipoDescontoDisciplinaRegularEAD to set
     */
    public void setTipoDescontoDisciplinaRegularEAD(String tipoDescontoDisciplinaRegularEAD) {
        this.tipoDescontoDisciplinaRegularEAD = tipoDescontoDisciplinaRegularEAD;
    }

    /**
     * @return the valorDescontoDisciplinaRegularEAD
     */
    public Double getValorDescontoDisciplinaRegularEAD() {
        if (valorDescontoDisciplinaRegularEAD == null) {
            valorDescontoDisciplinaRegularEAD = 0.0;
        }
        return valorDescontoDisciplinaRegularEAD;
    }

    /**
     * @param valorDescontoDisciplinaRegularEAD the valorDescontoDisciplinaRegularEAD to set
     */
    public void setValorDescontoDisciplinaRegularEAD(Double valorDescontoDisciplinaRegularEAD) {
        this.valorDescontoDisciplinaRegularEAD = valorDescontoDisciplinaRegularEAD;
    }

    /**
     * @return the valorFixoDisciplinaIncluidaEAD
     */
    public Double getValorFixoDisciplinaIncluidaEAD() {
        if (valorFixoDisciplinaIncluidaEAD == null) {
            valorFixoDisciplinaIncluidaEAD = 0.0;
        }
        return valorFixoDisciplinaIncluidaEAD;
    }

    /**
     * @param valorFixoDisciplinaIncluidaEAD the valorFixoDisciplinaIncluidaEAD to set
     */
    public void setValorFixoDisciplinaIncluidaEAD(Double valorFixoDisciplinaIncluidaEAD) {
        this.valorFixoDisciplinaIncluidaEAD = valorFixoDisciplinaIncluidaEAD;
    }

    /**
     * @return the utilizarPoliticaCobrancaEspecificaParaOptativas
     */
    public Boolean getUtilizarPoliticaCobrancaEspecificaParaOptativas() {
        if (utilizarPoliticaCobrancaEspecificaParaOptativas == null) {
            utilizarPoliticaCobrancaEspecificaParaOptativas = Boolean.FALSE;
        }
        return utilizarPoliticaCobrancaEspecificaParaOptativas;
    }

    /**
     * @param utilizarPoliticaCobrancaEspecificaParaOptativas the utilizarPoliticaCobrancaEspecificaParaOptativas to set
     */
    public void setUtilizarPoliticaCobrancaEspecificaParaOptativas(Boolean utilizarPoliticaCobrancaEspecificaParaOptativas) {
        this.utilizarPoliticaCobrancaEspecificaParaOptativas = utilizarPoliticaCobrancaEspecificaParaOptativas;
    }

    /**
     * @return the tipoCobrancaDisciplinaOptativa
     */
    public String getTipoCobrancaDisciplinaOptativa() {
        if (tipoCobrancaDisciplinaOptativa == null) {
            tipoCobrancaDisciplinaOptativa = "VF";
        }
        return tipoCobrancaDisciplinaOptativa;
    }
    
    public String getTipoCobrancaDisciplinaOptativa_Apresentar() {
        if (getTipoCobrancaDisciplinaOptativa().equals("VF")) {
            return "Valor Fixo";
        }
        if (getTipoCobrancaDisciplinaOptativa().equals("VC")) {
            return "Valor por Crédito";
        }
        if (getTipoCobrancaDisciplinaOptativa().equals("VH")) {
            return "Valor por Hora Disciplina";
        }
        return "";
    }

    /**
     * @param tipoCobrancaDisciplinaOptativa the tipoCobrancaDisciplinaOptativa to set
     */
    public void setTipoCobrancaDisciplinaOptativa(String tipoCobrancaDisciplinaOptativa) {
        this.tipoCobrancaDisciplinaOptativa = tipoCobrancaDisciplinaOptativa;
    }

    /**
     * @return the cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet
     */
    public Boolean getCobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet() {
        if (cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet == null) {
            cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet = Boolean.FALSE;
        }
        return cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet;
    }

    /**
     * @param cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet the cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet to set
     */
    public void setCobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet(Boolean cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet) {
        this.cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet = cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet;
    }

    /**
     * @return the valorDisciplinaOptativa
     */
    public Double getValorDisciplinaOptativa() {
        if (valorDisciplinaOptativa == null) {
            valorDisciplinaOptativa = 0.0;
        }
        return valorDisciplinaOptativa;
    }

    /**
     * @param valorDisciplinaOptativa the valorDisciplinaOptativa to set
     */
    public void setValorDisciplinaOptativa(Double valorDisciplinaOptativa) {
        this.valorDisciplinaOptativa = valorDisciplinaOptativa;
    }

    /**
     * @return the valorDisciplinaOptativaEAD
     */
    public Double getValorDisciplinaOptativaEAD() {
        if (valorDisciplinaOptativaEAD == null) {
            valorDisciplinaOptativaEAD = 0.0;
        }
        return valorDisciplinaOptativaEAD;
    }

    /**
     * @param valorDisciplinaOptativaEAD the valorDisciplinaOptativaEAD to set
     */
    public void setValorDisciplinaOptativaEAD(Double valorDisciplinaOptativaEAD) {
        this.valorDisciplinaOptativaEAD = valorDisciplinaOptativaEAD;
    }

	public String getTipoCobrancaExclusaoDisciplinaRegular() {
		if (tipoCobrancaExclusaoDisciplinaRegular == null) {
			tipoCobrancaExclusaoDisciplinaRegular = "VF";
		}
		return tipoCobrancaExclusaoDisciplinaRegular;
	}
	
    public String getTipoCobrancaExclusaoDisciplinaRegular_Apresentar() {
        if (getTipoCobrancaExclusaoDisciplinaRegular().equals("VF")) {
            return "Valor Fixo";
        }
        if (getTipoCobrancaExclusaoDisciplinaRegular().equals("VC")) {
            return "Valor por Crédito";
        }
        if (getTipoCobrancaExclusaoDisciplinaRegular().equals("VH")) {
            return "Valor por Hora Disciplina";
        }
        return "";
    }

	public void setTipoCobrancaExclusaoDisciplinaRegular(String tipoCobrancaExclusaoDisciplinaRegular) {
		this.tipoCobrancaExclusaoDisciplinaRegular = tipoCobrancaExclusaoDisciplinaRegular;
	}

	public Double getValorFixoDisciplinaExcluidaEAD() {
		if (valorFixoDisciplinaExcluidaEAD == null) {
			valorFixoDisciplinaExcluidaEAD = 0.0;
		}
		return valorFixoDisciplinaExcluidaEAD;
	}

	public void setValorFixoDisciplinaExcluidaEAD(Double valorFixoDisciplinaExcluidaEAD) {
		this.valorFixoDisciplinaExcluidaEAD = valorFixoDisciplinaExcluidaEAD;
	}

	public Boolean getApresentarSomenteParaDeterminadaFormaIngresso() {
		if (apresentarSomenteParaDeterminadaFormaIngresso == null) {
			apresentarSomenteParaDeterminadaFormaIngresso = false;
		}
		return apresentarSomenteParaDeterminadaFormaIngresso;
	}

	public void setApresentarSomenteParaDeterminadaFormaIngresso(Boolean apresentarSomenteParaDeterminadaFormaIngresso) {
		this.apresentarSomenteParaDeterminadaFormaIngresso = apresentarSomenteParaDeterminadaFormaIngresso;
	}

	public Boolean getApresentarSomenteParaIngressanteNoSemestreAno() {
		if (apresentarSomenteParaIngressanteNoSemestreAno == null) {
			apresentarSomenteParaIngressanteNoSemestreAno = false;
		}
		return apresentarSomenteParaIngressanteNoSemestreAno;
	}

	public void setApresentarSomenteParaIngressanteNoSemestreAno(Boolean apresentarSomenteParaIngressanteNoSemestreAno) {
		this.apresentarSomenteParaIngressanteNoSemestreAno = apresentarSomenteParaIngressanteNoSemestreAno;
	}

	public Boolean getFormaIngressoEntrevista() {
		if (formaIngressoEntrevista == null) {
			formaIngressoEntrevista = false;
		}
		return formaIngressoEntrevista;
	}

	public void setFormaIngressoEntrevista(Boolean formaIngressoEntrevista) {
		this.formaIngressoEntrevista = formaIngressoEntrevista;
	}

	public Boolean getFormaIngressoPortadorDiploma() {
		if (formaIngressoPortadorDiploma == null) {
			formaIngressoPortadorDiploma = false;
		}
		return formaIngressoPortadorDiploma;
	}

	public void setFormaIngressoPortadorDiploma(Boolean formaIngressoPortadorDiploma) {
		this.formaIngressoPortadorDiploma = formaIngressoPortadorDiploma;
	}

	public Boolean getFormaIngressoTransferenciaInterna() {
		if (formaIngressoTransferenciaInterna == null) {
			formaIngressoTransferenciaInterna = false;
		}
		return formaIngressoTransferenciaInterna;
	}

	public void setFormaIngressoTransferenciaInterna(Boolean formaIngressoTransferenciaInterna) {
		this.formaIngressoTransferenciaInterna = formaIngressoTransferenciaInterna;
	}

	public Boolean getFormaIngressoProcessoSeletivo() {
		if (formaIngressoProcessoSeletivo == null) {
			formaIngressoProcessoSeletivo = false;
		}
		return formaIngressoProcessoSeletivo;
	}

	public void setFormaIngressoProcessoSeletivo(Boolean formaIngressoProcessoSeletivo) {
		this.formaIngressoProcessoSeletivo = formaIngressoProcessoSeletivo;
	}

	public Boolean getFormaIngressoTransferenciaExterna() {
		if (formaIngressoTransferenciaExterna == null) {
			formaIngressoTransferenciaExterna = false;
		}
		return formaIngressoTransferenciaExterna;
	}

	public void setFormaIngressoTransferenciaExterna(Boolean formaIngressoTransferenciaExterna) {
		this.formaIngressoTransferenciaExterna = formaIngressoTransferenciaExterna;
	}

	public Boolean getFormaIngressoReingresso() {
		if (formaIngressoReingresso == null) {
			formaIngressoReingresso = false;
		}
		return formaIngressoReingresso;
	}

	public void setFormaIngressoReingresso(Boolean formaIngressoReingresso) {
		this.formaIngressoReingresso = formaIngressoReingresso;
	}

	public Boolean getFormaIngressoProuni() {
		if (formaIngressoProuni == null) {
			formaIngressoProuni = false;
		}
		return formaIngressoProuni;
	}

	public void setFormaIngressoProuni(Boolean formaIngressoProuni) {
		this.formaIngressoProuni = formaIngressoProuni;
	}

	public Boolean getFormaIngressoEnem() {
		if (formaIngressoEnem == null) {
			formaIngressoEnem = false;
		}
		return formaIngressoEnem;
	}

	public void setFormaIngressoEnem(Boolean formaIngressoEnem) {
		this.formaIngressoEnem = formaIngressoEnem;
	}

	public Boolean getFormaIngressoOutroTipoSelecao() {
		if (formaIngressoOutroTipoSelecao == null) {
			formaIngressoOutroTipoSelecao = false;
		}
		return formaIngressoOutroTipoSelecao;
	}

	public void setFormaIngressoOutroTipoSelecao(Boolean formaIngressoOutroTipoSelecao) {
		this.formaIngressoOutroTipoSelecao = formaIngressoOutroTipoSelecao;
	}

	public String getAnoIngressante() {
		if (anoIngressante == null) {
			anoIngressante = "";
		}
		return anoIngressante;
	}

	public void setAnoIngressante(String anoIngressante) {
		this.anoIngressante = anoIngressante;
	}

	public String getSemestreIngressante() {
		if (semestreIngressante == null) {
			semestreIngressante = "";
		}
		return semestreIngressante;
	}

	public void setSemestreIngressante(String semestreIngressante) {
		this.semestreIngressante = semestreIngressante;
	}
        
        public Boolean getAplicavelParaDeterminadaSemestreAnoIngresso(
                String semestreIngresso,
                String anoIngresso) {
            if (semestreIngresso == null) {
                semestreIngresso = "";
            } else {
                semestreIngresso = semestreIngresso.trim();
            }
            if (anoIngresso == null) {
                anoIngresso = "";
            } else {
                anoIngresso = anoIngresso.trim();
            }
            String anoIngressoCondicaoFiltroCondicao = this.getAnoIngressante().trim();
            String semestreIngressoCondicaoFiltroCondicao = this.getSemestreIngressante().trim();
            
            if ((!this.getApresentarSomenteParaIngressanteNoSemestreAno()) ||
                ((semestreIngresso.equals("")) && (anoIngresso.equals("")))) {
                return true;
            }
            if (!semestreIngressoCondicaoFiltroCondicao.equals("")) {
                if (semestreIngressoCondicaoFiltroCondicao.equals(semestreIngresso)) {
                    // se o semestr é igual ainda temos que verificar se o ano foi informado
                    // como filtro e validado abaixo.
                    if (!anoIngressoCondicaoFiltroCondicao.equals("")) {
                        if (anoIngressoCondicaoFiltroCondicao.equals(anoIngresso)) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
            if (!anoIngressoCondicaoFiltroCondicao.equals("")) {
                if (anoIngressoCondicaoFiltroCondicao.equals(anoIngresso)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }
        
        public Boolean getAplicavelParaDeterminadaFormaIngresso(String formaIngresso) {
            if ((!this.getApresentarSomenteParaDeterminadaFormaIngresso()) ||
                (formaIngresso == null) ||
                (formaIngresso.equals(""))) {
                return true;
            }
            if ((getFormaIngressoProcessoSeletivo()) && 
                (formaIngresso.equals(FormaIngresso.PROCESSO_SELETIVO.getValor()))) {
                return true;
            }
            if ((getFormaIngressoEntrevista()) && 
                (formaIngresso.equals(FormaIngresso.ENTREVISTA.getValor()))) {
                return true;
            }
            if ((getFormaIngressoPortadorDiploma()) && 
                (formaIngresso.equals(FormaIngresso.PORTADOR_DE_DIPLOMA.getValor()))) {
                return true;
            }
            if ((getFormaIngressoTransferenciaInterna()) && 
                (formaIngresso.equals(FormaIngresso.TRANSFERENCIA_INTERNA.getValor()))) {
                return true;
            }
            if ((getFormaIngressoTransferenciaExterna()) && 
                (formaIngresso.equals(FormaIngresso.TRANSFERENCIA_EXTERNA.getValor()))) {
                return true;
            }
            if ((getFormaIngressoReingresso()) && 
                (formaIngresso.equals(FormaIngresso.REINGRESSO.getValor()))) {
                return true;
            }
            if ((getFormaIngressoProuni()) && 
                (formaIngresso.equals(FormaIngresso.PROUNI.getValor()))) {
                return true;
            }
            if ((getFormaIngressoEnem()) && 
                (formaIngresso.equals(FormaIngresso.ENEM.getValor()))) {
                return true;
            }
            if ((getFormaIngressoOutroTipoSelecao()) && 
                (formaIngresso.equals(FormaIngresso.OUTROS_TIPOS_SELECAO.getValor()))) {
                return true;
            }
            return false;
        }
        
        public Boolean getAplicavelParaVisaoUsuario(Boolean logadoVisaoAlunoOuPais) {
            if (logadoVisaoAlunoOuPais) {
                if (!this.getApresentarCondicaoVisaoAluno())  {
                    return false;
                }
            }
            return true; 
        }
        
        public Boolean getAplicavelParaTipoMatricula(Boolean matriculaEspecial) {
            if (matriculaEspecial) {
                if (this.getTipoUsoIsMatriculaEspecial()) {
                    return true;
                }
            } else {
                if ((getTipoCalculoParcela().equals("VF") && this.getTipoUsoIsMatriculaRegular()) || !getTipoCalculoParcela().equals("VF")) {
                    return true;
                }
            }
            return false;
        }

    /**
     * @return the apresentarSomenteParaAlunosIntegralizandoCurso
     */
    public Boolean getApresentarSomenteParaAlunosIntegralizandoCurso() {
        if (apresentarSomenteParaAlunosIntegralizandoCurso == null) {
            apresentarSomenteParaAlunosIntegralizandoCurso = Boolean.FALSE;
        }
        return apresentarSomenteParaAlunosIntegralizandoCurso;
    }

    /**
     * @param apresentarSomenteParaAlunosIntegralizandoCurso the apresentarSomenteParaAlunosIntegralizandoCurso to set
     */
    public void setApresentarSomenteParaAlunosIntegralizandoCurso(Boolean apresentarSomenteParaAlunosIntegralizandoCurso) {
        this.apresentarSomenteParaAlunosIntegralizandoCurso = apresentarSomenteParaAlunosIntegralizandoCurso;
    }

    public Boolean getTipoControleAlunoIntegralizandoPercentual() {
        if (getTipoControleAlunoIntegralizandoCurso().equals("PC")) {
            return Boolean.TRUE;
        }
        if (getTipoControleAlunoIntegralizandoCurso().equals("PH")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    public String getTipoControleAlunoIntegralizandoCurso_Apresentar() {
        if (getTipoControleAlunoIntegralizandoCurso().equals("PC")) {
            return "% Crédito Pendente Último Período";
        }
        if (getTipoControleAlunoIntegralizandoCurso().equals("PH")) {
            return "% CH Pendente Último Período";
        }
        if (getTipoControleAlunoIntegralizandoCurso().equals("CH")) {
            return "CH Pendente";
        }
        if (getTipoControleAlunoIntegralizandoCurso().equals("CR")) {
            return "Nr. Créditos Pendentes";
        }
        return "";
    }

    /**
     * @return the tipoControleAlunoIntegralizandoCurso
     */
    public String getTipoControleAlunoIntegralizandoCurso() {
        if (tipoControleAlunoIntegralizandoCurso == null) {
            tipoControleAlunoIntegralizandoCurso = "PC";
        }
        return tipoControleAlunoIntegralizandoCurso;
    }

    /**
     * @param tipoControleAlunoIntegralizandoCurso the tipoControleAlunoIntegralizandoCurso to set
     */
    public void setTipoControleAlunoIntegralizandoCurso(String tipoControleAlunoIntegralizandoCurso) {
        this.tipoControleAlunoIntegralizandoCurso = tipoControleAlunoIntegralizandoCurso;
    }

    /**
     * @return the valorBaseDefinirAlunoIntegralizandoCurso
     */
    public Double getValorBaseDefinirAlunoIntegralizandoCurso() {
        if (valorBaseDefinirAlunoIntegralizandoCurso == null) {
            valorBaseDefinirAlunoIntegralizandoCurso = 0.0;
        }
        return valorBaseDefinirAlunoIntegralizandoCurso;
    }

    /**
     * @param valorBaseDefinirAlunoIntegralizandoCurso the valorBaseDefinirAlunoIntegralizandoCurso to set
     */
    public void setValorBaseDefinirAlunoIntegralizandoCurso(Double valorBaseDefinirAlunoIntegralizandoCurso) {
        this.valorBaseDefinirAlunoIntegralizandoCurso = valorBaseDefinirAlunoIntegralizandoCurso;
    }

    /**
     * @return the considerarIntegralizandoEstiverCursandoUltimoPer2Vez
     */
    public Boolean getConsiderarIntegralizandoEstiverCursandoUltimoPer2Vez() {
        if (considerarIntegralizandoEstiverCursandoUltimoPer2Vez == null) {
            considerarIntegralizandoEstiverCursandoUltimoPer2Vez = Boolean.FALSE;
        }
        return considerarIntegralizandoEstiverCursandoUltimoPer2Vez;
    }

    /**
     * @param considerarIntegralizandoEstiverCursandoUltimoPer2Vez the considerarIntegralizandoEstiverCursandoUltimoPer2Vez to set
     */
    public void setConsiderarIntegralizandoEstiverCursandoUltimoPer2Vez(Boolean considerarIntegralizandoEstiverCursandoUltimoPer2Vez) {
        this.considerarIntegralizandoEstiverCursandoUltimoPer2Vez = considerarIntegralizandoEstiverCursandoUltimoPer2Vez;
    }
   
    /**
     * @author Victor Hugo de Paula Costa
     */
    private Boolean permiteRecebimentoOnlineCartaoCredito;

	public Boolean getPermiteRecebimentoOnlineCartaoCredito() {
		if(permiteRecebimentoOnlineCartaoCredito == null) {
			permiteRecebimentoOnlineCartaoCredito = true;
		}
		return permiteRecebimentoOnlineCartaoCredito;
	}

	public void setPermiteRecebimentoOnlineCartaoCredito(Boolean permiteRecebimentoOnlineCartaoCredito) {
		this.permiteRecebimentoOnlineCartaoCredito = permiteRecebimentoOnlineCartaoCredito;
	}
	
	public Integer getCodTurma() {
		if (codTurma == null) {
			codTurma = 0;
		}
		return codTurma;
	}

	public void setCodTurma(Integer codTurma) {
		this.codTurma = codTurma;
	}
	
	public Boolean getApresentarMatriculaOnlineExterna() {
		if (apresentarMatriculaOnlineExterna == null) {
			apresentarMatriculaOnlineExterna = false;
		}
		return apresentarMatriculaOnlineExterna;
	}

	public void setApresentarMatriculaOnlineExterna(Boolean apresentarMatriculaOnlineExterna) {
		this.apresentarMatriculaOnlineExterna = apresentarMatriculaOnlineExterna;
	}

	public Boolean getApresentarMatriculaOnlineProfessor() {
		if (apresentarMatriculaOnlineProfessor == null) {
			apresentarMatriculaOnlineProfessor = false;
		}
		return apresentarMatriculaOnlineProfessor;
	}

	public void setApresentarMatriculaOnlineProfessor(Boolean apresentarMatriculaOnlineProfessor) {
		this.apresentarMatriculaOnlineProfessor = apresentarMatriculaOnlineProfessor;
	}

	public Boolean getApresentarMatriculaOnlineCoordenador() {
		if (apresentarMatriculaOnlineCoordenador == null) {
			apresentarMatriculaOnlineCoordenador = false;
		}
		return apresentarMatriculaOnlineCoordenador;
	}

	public void setApresentarMatriculaOnlineCoordenador(Boolean apresentarMatriculaOnlineCoordenador) {
		this.apresentarMatriculaOnlineCoordenador = apresentarMatriculaOnlineCoordenador;
	}
	
	public Boolean getAplicavelParaMatriculaOnlineExterna(MatriculaVO matriculaVO) {
		if (matriculaVO.getMatriculaOnlineExterna() && getApresentarMatriculaOnlineExterna()) {
			return true;
		}
		return false;
	}
	
	public Boolean getAplicavelParaMatriculaOnlineProfessor(Boolean logadoVisaoProfessor) {
		if (logadoVisaoProfessor && getApresentarMatriculaOnlineProfessor()) {
			return true;
		}
		return false;
	}
	
	public Boolean getAplicavelParaMatriculaOnlineCoordenador(Boolean logadoVisaoCoordenador) {
		return logadoVisaoCoordenador&& getApresentarMatriculaOnlineCoordenador();
	}

	public Boolean getConsiderarValorRateioBaseadoValorBaseComDescontosAplicados() {
		if(considerarValorRateioBaseadoValorBaseComDescontosAplicados == null){
			considerarValorRateioBaseadoValorBaseComDescontosAplicados = false;
		}
		return considerarValorRateioBaseadoValorBaseComDescontosAplicados;
	}

	public void setConsiderarValorRateioBaseadoValorBaseComDescontosAplicados(
			Boolean considerarValorRateioBaseadoValorBaseComDescontosAplicados) {
		this.considerarValorRateioBaseadoValorBaseComDescontosAplicados = considerarValorRateioBaseadoValorBaseComDescontosAplicados;
	}

	public Boolean getAbaterValorRateiroComoDescontoRateio() {
		if(abaterValorRateiroComoDescontoRateio == null){
			abaterValorRateiroComoDescontoRateio = false;
		}
		return abaterValorRateiroComoDescontoRateio;
	}

	public void setAbaterValorRateiroComoDescontoRateio(Boolean abaterValorRateiroComoDescontoRateio) {
		this.abaterValorRateiroComoDescontoRateio = abaterValorRateiroComoDescontoRateio;
	}

	public Boolean getGerarParcelasExtrasSeparadoMensalidadeAReceber() {
		if(gerarParcelasExtrasSeparadoMensalidadeAReceber == null){
			gerarParcelasExtrasSeparadoMensalidadeAReceber = false;
		}
		return gerarParcelasExtrasSeparadoMensalidadeAReceber;
	}

	public void setGerarParcelasExtrasSeparadoMensalidadeAReceber(Boolean gerarParcelasExtrasSeparadoMensalidadeAReceber) {
		this.gerarParcelasExtrasSeparadoMensalidadeAReceber = gerarParcelasExtrasSeparadoMensalidadeAReceber;
	}
	
	public Boolean getConsiderarValorRateioExtraParcelaVencida() {
		if(considerarValorRateioExtraParcelaVencida == null){
			considerarValorRateioExtraParcelaVencida = true;
		}
		return considerarValorRateioExtraParcelaVencida;
	}

	public void setConsiderarValorRateioExtraParcelaVencida(Boolean considerarValorRateioExtraParcelaVencida) {
		this.considerarValorRateioExtraParcelaVencida = considerarValorRateioExtraParcelaVencida;
	}

	public String getCategoria() {
		if(categoria == null){
			categoria = "";
		}
		return categoria;
	}
    private Boolean apresentarSomenteParaRenovacoesNoSemestreAno;
    private String anoRenovacao;
    private String semestreRenovacao;

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public Boolean getApresentarSomenteParaRenovacoesNoSemestreAno() {
		if (apresentarSomenteParaRenovacoesNoSemestreAno == null) {
			apresentarSomenteParaRenovacoesNoSemestreAno = Boolean.FALSE;
		}
		return apresentarSomenteParaRenovacoesNoSemestreAno;
	}

	public boolean isGerarParcelaMaterialDidatico() {
		return gerarParcelaMaterialDidatico;
	}
	public void setApresentarSomenteParaRenovacoesNoSemestreAno(Boolean apresentarSomenteParaRenovacoesNoSemestreAno) {
		this.apresentarSomenteParaRenovacoesNoSemestreAno = apresentarSomenteParaRenovacoesNoSemestreAno;
	}

	public void setGerarParcelaMaterialDidatico(boolean gerarParcelaMaterialDidatico) {
		this.gerarParcelaMaterialDidatico = gerarParcelaMaterialDidatico;
	}

	public boolean isUsarUnidadeEnsinoEspecifica() {		
		return usarUnidadeEnsinoEspecifica;
	}

	public void setUsarUnidadeEnsinoEspecifica(boolean usarUnidadeEnsinoEspecifica) {
		this.usarUnidadeEnsinoEspecifica = usarUnidadeEnsinoEspecifica;
	}

	public boolean isControlaDiaBaseVencimentoParcelaMaterialDidatico() {
		return controlaDiaBaseVencimentoParcelaMaterialDidatico;
	}

	public void setControlaDiaBaseVencimentoParcelaMaterialDidatico(boolean controlaDiaBaseVencimentoParcelaMaterialDidatico) {
		this.controlaDiaBaseVencimentoParcelaMaterialDidatico = controlaDiaBaseVencimentoParcelaMaterialDidatico;
	}

	public boolean isAplicarDescontosParcelasNoMaterialDidatico() {		
		return aplicarDescontosParcelasNoMaterialDidatico;
	}

	public void setAplicarDescontosParcelasNoMaterialDidatico(boolean aplicarDescontosParcelasNoMaterialDidatico) {
		this.aplicarDescontosParcelasNoMaterialDidatico = aplicarDescontosParcelasNoMaterialDidatico;
	}

	public boolean isAplicarDescontoMaterialDidaticoDescontoAluno() {		
		return aplicarDescontoMaterialDidaticoDescontoAluno;
	}

	public void setAplicarDescontoMaterialDidaticoDescontoAluno(boolean aplicarDescontoMaterialDidaticoDescontoAluno) {
		this.aplicarDescontoMaterialDidaticoDescontoAluno = aplicarDescontoMaterialDidaticoDescontoAluno;
	}

	public boolean isAplicarDescontoMaterialDidaticoDescontoInstitucional() {		
		return aplicarDescontoMaterialDidaticoDescontoInstitucional;
	}

	public void setAplicarDescontoMaterialDidaticoDescontoInstitucional(boolean aplicarDescontoMaterialDidaticoDescontoInstitucional) {
		this.aplicarDescontoMaterialDidaticoDescontoInstitucional = aplicarDescontoMaterialDidaticoDescontoInstitucional;
	}

	public boolean isAplicarDescontoMaterialDidaticoDescontoProgressivo() {		
		return aplicarDescontoMaterialDidaticoDescontoProgressivo;
	}

	public void setAplicarDescontoMaterialDidaticoDescontoProgressivo(boolean aplicarDescontoMaterialDidaticoDescontoProgressivo) {
		this.aplicarDescontoMaterialDidaticoDescontoProgressivo = aplicarDescontoMaterialDidaticoDescontoProgressivo;
	}

	public boolean isAplicarDescontoMaterialDidaticoDescontoConvenio() {		
		return aplicarDescontoMaterialDidaticoDescontoConvenio;
	}
	
	public boolean isConsiderarParcelasMaterialDidaticoReajustePreco() {		
		return considerarParcelasMaterialDidaticoReajustePreco;
	}

	public void setAplicarDescontoMaterialDidaticoDescontoConvenio(boolean aplicarDescontoMaterialDidaticoDescontoConvenio) {
		this.aplicarDescontoMaterialDidaticoDescontoConvenio = aplicarDescontoMaterialDidaticoDescontoConvenio;
	}

	public boolean isAplicarDescontosDesconsiderandosVencimento() {		
		return aplicarDescontosDesconsiderandosVencimento;
	}

	public void setAplicarDescontosDesconsiderandosVencimento(boolean aplicarDescontosDesconsiderandosVencimento) {
		this.aplicarDescontosDesconsiderandosVencimento = aplicarDescontosDesconsiderandosVencimento;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoFinanceira() {
		if (unidadeEnsinoFinanceira == null) {
			unidadeEnsinoFinanceira = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoFinanceira;
	}

	public void setUnidadeEnsinoFinanceira(UnidadeEnsinoVO unidadeEnsinoFinanceira) {
		this.unidadeEnsinoFinanceira = unidadeEnsinoFinanceira;
	}

	public Long getQuantidadeParcelasMaterialDidatico() {
		if (quantidadeParcelasMaterialDidatico == null) {
			quantidadeParcelasMaterialDidatico = 0L;
		}
		return quantidadeParcelasMaterialDidatico;
	}

	public void setQuantidadeParcelasMaterialDidatico(Long quantidadeParcelasMaterialDidatico) {
		this.quantidadeParcelasMaterialDidatico = quantidadeParcelasMaterialDidatico;
	}

	public Long getDiaBaseVencimentoParcelaOutraUnidade() {
		if (diaBaseVencimentoParcelaOutraUnidade == null) {
			diaBaseVencimentoParcelaOutraUnidade = 0L;
		}
		return diaBaseVencimentoParcelaOutraUnidade;
	}

	public void setDiaBaseVencimentoParcelaOutraUnidade(Long diaBaseVencimentoParcelaOutraUnidade) {
		this.diaBaseVencimentoParcelaOutraUnidade = diaBaseVencimentoParcelaOutraUnidade;
	}

	public Double getValorPorParcelaMaterialDidatico() {
		if (valorPorParcelaMaterialDidatico == null) {
			valorPorParcelaMaterialDidatico = 0.0;
		}
		return valorPorParcelaMaterialDidatico;
	}

	public void setValorPorParcelaMaterialDidatico(Double valorPorParcelaMaterialDidatico) {
		this.valorPorParcelaMaterialDidatico = valorPorParcelaMaterialDidatico;
	}

	public TipoGeracaoMaterialDidaticoEnum getTipoGeracaoMaterialDidatico() {
		if (tipoGeracaoMaterialDidatico == null) {
			tipoGeracaoMaterialDidatico = TipoGeracaoMaterialDidaticoEnum.NENHUM;
		}
		return tipoGeracaoMaterialDidatico;
	}

	public void setTipoGeracaoMaterialDidatico(TipoGeracaoMaterialDidaticoEnum tipoGeracaoMaterialDidatico) {
		this.tipoGeracaoMaterialDidatico = tipoGeracaoMaterialDidatico;
	}

	public Boolean getNaoControlarValorParcela() {
		if(naoControlarValorParcela == null){
			naoControlarValorParcela = false;
		}
		return naoControlarValorParcela;
	}

	public void setNaoControlarValorParcela(Boolean naoControlarValorParcela) {
		this.naoControlarValorParcela = naoControlarValorParcela;
	}

	public Boolean getApresentarCondPagtoCRM() {
		if (apresentarCondPagtoCRM == null) {
			apresentarCondPagtoCRM = Boolean.TRUE;
		}
		return apresentarCondPagtoCRM;
	}

	public void setApresentarCondPagtoCRM(Boolean apresentarCondPagtoCRM) {
		this.apresentarCondPagtoCRM = apresentarCondPagtoCRM;
	}
	
	public Boolean getRegerarFinanceiro() {
		if (regerarFinanceiro == null) {
			regerarFinanceiro = true;
		}
		return regerarFinanceiro;
	}

	public void setRegerarFinanceiro(Boolean regerarFinanceiro) {
		this.regerarFinanceiro = regerarFinanceiro;
	}
	
	public void setConsiderarParcelasMaterialDidaticoReajustePreco(
			boolean considerarParcelasMaterialDidaticoReajustePreco) {
		this.considerarParcelasMaterialDidaticoReajustePreco = considerarParcelasMaterialDidaticoReajustePreco;
	}

	public CondicaoPagamentoPlanoFinanceiroCursoVO getClone() throws CloneNotSupportedException {
		CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO = (CondicaoPagamentoPlanoFinanceiroCursoVO) this.clone();
		condicaoPagamentoPlanoFinanceiroCursoVO.setCondicaoPagamentoPlanoDescontoVOs(new ArrayList<CondicaoPagamentoPlanoDescontoVO>(0));
		getCondicaoPagamentoPlanoDescontoVOs().forEach(c -> {
			CondicaoPagamentoPlanoDescontoVO condicaoClonada = new CondicaoPagamentoPlanoDescontoVO();
			condicaoClonada.getPlanoDescontoVO().setCodigo(c.getPlanoDescontoVO().getCodigo());
			condicaoClonada.getPlanoDescontoVO().setNome(c.getPlanoDescontoVO().getNome());
			condicaoClonada.setNovoObj(Boolean.TRUE);
			condicaoPagamentoPlanoFinanceiroCursoVO.getCondicaoPagamentoPlanoDescontoVOs().add(condicaoClonada);
		});
		condicaoPagamentoPlanoFinanceiroCursoVO.setCondicaoPlanoFinanceiroCursoTurmaVOs(new ArrayList<CondicaoPlanoFinanceiroCursoTurmaVO>(0));
		getCondicaoPlanoFinanceiroCursoTurmaVOs().forEach(c -> {
			CondicaoPlanoFinanceiroCursoTurmaVO condicaoClonada = new CondicaoPlanoFinanceiroCursoTurmaVO();
			condicaoClonada.getDisciplina().setCodigo(c.getDisciplina().getCodigo());
			condicaoClonada.getDisciplina().setNome(c.getDisciplina().getNome());
			condicaoClonada.setValor(c.getValor());
			condicaoClonada.setNovoObj(Boolean.TRUE);
			condicaoPagamentoPlanoFinanceiroCursoVO.getCondicaoPlanoFinanceiroCursoTurmaVOs().add(condicaoClonada);
		});
		condicaoPagamentoPlanoFinanceiroCursoVO.setPlanoDescontoDisponivelMatriculaVOs(new ArrayList<PlanoDescontoDisponivelMatriculaVO>(0));
		if (condicaoPagamentoPlanoFinanceiroCursoVO.getDefinirPlanoDescontoApresentarMatricula()) {
			getPlanoDescontoDisponivelMatriculaVOs().forEach(p -> {
				PlanoDescontoDisponivelMatriculaVO planoClonado = new PlanoDescontoDisponivelMatriculaVO();
				planoClonado.getPlanoDesconto().setCodigo(p.getPlanoDesconto().getCodigo());
				planoClonado.getPlanoDesconto().setNome(p.getPlanoDesconto().getNome());
				planoClonado.setCondicaoPagamentoPlanoFinanceiroCurso(p.getCondicaoPagamentoPlanoFinanceiroCurso());
				planoClonado.setNovoObj(Boolean.TRUE);
				condicaoPagamentoPlanoFinanceiroCursoVO.getPlanoDescontoDisponivelMatriculaVOs().add(planoClonado);
			});
		}
		condicaoPagamentoPlanoFinanceiroCursoVO.getDescontoProgressivoPadrao().setCodigo(getDescontoProgressivoPadrao().getCodigo());
		condicaoPagamentoPlanoFinanceiroCursoVO.getTextoPadraoContratoMatricula().setCodigo(getTextoPadraoContratoMatricula().getCodigo());
		condicaoPagamentoPlanoFinanceiroCursoVO.getDescontoProgressivoPadraoMatricula().setCodigo(getDescontoProgressivoPadraoMatricula().getCodigo());
		condicaoPagamentoPlanoFinanceiroCursoVO.getDescontoProgressivoPrimeiraParcela().setCodigo(getDescontoProgressivoPrimeiraParcela().getCodigo());
		condicaoPagamentoPlanoFinanceiroCursoVO.getCentroReceita().setCodigo(getCentroReceita().getCodigo());
		condicaoPagamentoPlanoFinanceiroCursoVO.setCodigo(0);
		condicaoPagamentoPlanoFinanceiroCursoVO.setSituacao("EL");
		condicaoPagamentoPlanoFinanceiroCursoVO.setDescricao("");
		condicaoPagamentoPlanoFinanceiroCursoVO.setDescricaoDuracao("");
		condicaoPagamentoPlanoFinanceiroCursoVO.setNovoObj(Boolean.TRUE);
		return condicaoPagamentoPlanoFinanceiroCursoVO;
	}
	public String getAnoRenovacao() {
		if (anoRenovacao == null) {
			anoRenovacao = "";
		}
		return anoRenovacao;
	}

	public void setAnoRenovacao(String anoRenovacao) {
		this.anoRenovacao = anoRenovacao;
	}

	public String getSemestreRenovacao() {
		if (semestreRenovacao == null) { 
			semestreRenovacao = "";
		}
		return semestreRenovacao;
	}

	public void setSemestreRenovacao(String semestreRenovacao) {
		this.semestreRenovacao = semestreRenovacao;
	}

    public Boolean getAplicavelParaDeterminadaSemestreAnoRenovacao(
    		Boolean alunoVeterando,
            String semestreRenovacao,
            String anoRenovacao) {
    	if ((this.getApresentarSomenteParaRenovacoesNoSemestreAno()) &&
    	    (!alunoVeterando)) {
    		// se esta marcado é por que esta condicacao deverá ser apresentada somente
    		// para alunos que sao veterandos (ou seja, que esta renovando). Neste, caso,
    		// como o aluno é calouro o plano nao vale para ele.
    		return false;
    	}
    	
        if (semestreRenovacao == null) {
        	semestreRenovacao = "";
        } else {
        	semestreRenovacao = semestreRenovacao.trim();
        }
        if (anoRenovacao == null) {
        	anoRenovacao = "";
        } else {
        	anoRenovacao = anoRenovacao.trim();
        }
        String anoRenovacaoCondicaoFiltroCondicao = this.getAnoRenovacao().trim();
        String semestreRenovacaoCondicaoFiltroCondicao = this.getSemestreRenovacao().trim();
        
        if ((!this.getApresentarSomenteParaRenovacoesNoSemestreAno()) ||
            ((semestreRenovacao.equals("")) && (anoRenovacao.equals("")))) {
            return true;
        }
        if (!semestreRenovacaoCondicaoFiltroCondicao.equals("")) {
            if (semestreRenovacaoCondicaoFiltroCondicao.equals(semestreRenovacao)) {
                // se o semestr é igual ainda temos que verificar se o ano foi informado
                // como filtro e validado abaixo.
                if (!anoRenovacaoCondicaoFiltroCondicao.equals("")) {
                    if (anoRenovacaoCondicaoFiltroCondicao.equals(anoRenovacao)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
        if (!anoRenovacaoCondicaoFiltroCondicao.equals("")) {
            if (anoRenovacaoCondicaoFiltroCondicao.equals(anoRenovacao)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

	public Boolean getLancarValorRatiadoSobreValorBaseContaReceber() {
		if(lancarValorRatiadoSobreValorBaseContaReceber == null) {
			lancarValorRatiadoSobreValorBaseContaReceber =  false;
		}
		return lancarValorRatiadoSobreValorBaseContaReceber;
	}

	public void setLancarValorRatiadoSobreValorBaseContaReceber(Boolean lancarValorRatiadoSobreValorBaseContaReceber) {
		this.lancarValorRatiadoSobreValorBaseContaReceber = lancarValorRatiadoSobreValorBaseContaReceber;
	}	
    
    

}