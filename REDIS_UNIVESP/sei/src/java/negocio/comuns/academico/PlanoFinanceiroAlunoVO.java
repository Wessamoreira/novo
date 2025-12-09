package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.OrdemDescontoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;

/**
 * Reponsável por manter os dados da entidade PlanoFinanceiroAluno. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PlanoFinanceiroAlunoVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private Double percDescontoMatricula;
    private Double percDescontoParcela;
    private Double valorDescontoMatricula;
    private Double valorDescontoParcela;
    private String tipoDescontoMatricula;
    private String tipoDescontoParcela;
    private String justificativa;
    private String documentoComprobatorio;
    /**
     * Attribute responsável por manter os objetos da classe
     * <code>ItemPlanoFinanceiroAluno</code>.
     */
    private List itemPlanoFinanceiroAlunoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Matricula </code>.
     */
    private String matricula;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>DescontoProgressivo </code>.
     */
    private DescontoProgressivoVO descontoProgressivo;
    private DescontoProgressivoVO descontoProgressivoPrimeiraParcela;
    private DescontoProgressivoVO descontoProgressivoMatricula;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private UsuarioVO responsavel;
    private Integer ordemDescontoAluno;
    private Boolean ordemDescontoAlunoValorCheio;
    private Integer ordemPlanoDesconto;
    private Boolean ordemPlanoDescontoValorCheio;
    private Integer ordemConvenio;
    private Boolean ordemConvenioValorCheio;
    private Integer ordemDescontoProgressivo;
    private Boolean ordemDescontoProgressivoValorCheio;
    private PlanoFinanceiroCursoVO planoFinanceiroCursoVO;
    private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO;
    private Integer matriculaPeriodo;
    private Boolean descontoValidoAteDataParcela;
    /**
     * Atributo utilizado no relatório ListagemDescontosAlunos
     */
    private Double valorTotalDescontoMatriculaPorPlanoFinanceiroAlunoCalculado;
    private Double valorTotalDescontoParcelaPorPlanoFinanceiroAlunoCalculado;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PlanoFinanceiroAluno</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public PlanoFinanceiroAlunoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PlanoFinanceiroAlunoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PlanoFinanceiroAlunoVO obj) throws ConsistirException {
        if (obj.getCodigo().intValue() == 0) {
            if (!obj.getPercDescontoMatricula().equals(0.0)) {
                if (obj.getJustificativa().equals("")) {
                    throw new ConsistirException("O campo JUSTIFICATIVA (Plano Financeiro Aluno) deve ser informado.");
                }
            }
            if (!obj.getPercDescontoParcela().equals(0.0)) {
                if (obj.getJustificativa().equals("")) {
                    throw new ConsistirException("O campo JUSTIFICATIVA (Plano Financeiro Aluno) deve ser informado.");
                }
            }
            // if (obj.getDescontoProgressivo().getCodigo().intValue() != 0) {
            // if (obj.getJustificativa().equals("")) {
            // throw new
            // ConsistirException("O campo JUSTIFICATIVA (Plano Financeiro Aluno) deve ser informado.");
            // }
            // return true;
            // }
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
        setPercDescontoMatricula(0.0);
        setPercDescontoParcela(0.0);
        setJustificativa("");
        setDocumentoComprobatorio("");
        setMatricula("");

        setOrdemDescontoAluno(0);
        setOrdemPlanoDesconto(1);
        setOrdemConvenio(2);
        setOrdemDescontoProgressivo(3);

        setOrdemDescontoAlunoValorCheio(Boolean.TRUE);
        setOrdemPlanoDescontoValorCheio(Boolean.TRUE);
        setOrdemConvenioValorCheio(Boolean.TRUE);
        setOrdemDescontoProgressivoValorCheio(Boolean.TRUE);

        setValorDescontoMatricula(0.0);
        setValorDescontoParcela(0.0);
        setTipoDescontoMatricula("PO");
        setTipoDescontoParcela("PO");
    }

    
    

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ItemPlanoFinanceiroAlunoVO</code> no List
     * <code>itemPlanoFinanceiroAlunoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ItemPlanoFinanceiroAluno</code> -
     * getPlanoDesconto().getCodigo() - como identificador (key) do objeto no
     * List.
     *
     * @param planoDesconto
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjItemPlanoFinanceiroAlunoVOs(Integer planoDesconto) throws Exception {
        int index = 0;
        Iterator i = getItemPlanoFinanceiroAlunoVOs().iterator();
        while (i.hasNext()) {
            ItemPlanoFinanceiroAlunoVO objExistente = (ItemPlanoFinanceiroAlunoVO) i.next();
            if (objExistente.getPlanoDesconto().getCodigo().equals(planoDesconto)) {
                getItemPlanoFinanceiroAlunoVOs().remove(index);
                return;
            }
            index++;
        }
    }
    
    public void excluirObjItemPlanoFinanceiroAlunoConvenioVOs(Integer convenioRemover) throws Exception {
        int index = 0;
        Iterator i = getItemPlanoFinanceiroAlunoVOs().iterator();
        while (i.hasNext()) {
            ItemPlanoFinanceiroAlunoVO objExistente = (ItemPlanoFinanceiroAlunoVO) i.next();
            if (objExistente.getConvenio().getCodigo().equals(convenioRemover)) {
                getItemPlanoFinanceiroAlunoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ItemPlanoFinanceiroAlunoVO</code> no List
     * <code>itemPlanoFinanceiroAlunoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ItemPlanoFinanceiroAluno</code> -
     * getPlanoDesconto().getCodigo() - como identificador (key) do objeto no
     * List.
     *
     * @param planoDesconto
     *            Parâmetro para localizar o objeto do List.
     */
    public ItemPlanoFinanceiroAlunoVO consultarObjItemPlanoFinanceiroAlunoVO(Integer planoDesconto) throws Exception {
        Iterator i = getItemPlanoFinanceiroAlunoVOs().iterator();
        while (i.hasNext()) {
            ItemPlanoFinanceiroAlunoVO objExistente = (ItemPlanoFinanceiroAlunoVO) i.next();
            if (objExistente.getPlanoDesconto().getCodigo().equals(planoDesconto)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PlanoFinanceiroAluno</code>).
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PlanoFinanceiroAluno</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }

    /**
     * Retorna o objeto da classe <code>DescontoProgressivo</code> relacionado
     * com (<code>PlanoFinanceiroAluno</code>).
     */
    public DescontoProgressivoVO getDescontoProgressivo() {
        if (descontoProgressivo == null) {
            descontoProgressivo = new DescontoProgressivoVO();
        }
        return (descontoProgressivo);
    }

    /**
     * Define o objeto da classe <code>DescontoProgressivo</code> relacionado
     * com (<code>PlanoFinanceiroAluno</code>).
     */
    public void setDescontoProgressivo(DescontoProgressivoVO obj) {
        this.descontoProgressivo = obj;
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com (
     * <code>PlanoFinanceiroAluno</code>).
     */
    public String getMatricula() {
        return (matricula);
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com (
     * <code>PlanoFinanceiroAluno</code>).
     */
    public void setMatricula(String obj) {
        this.matricula = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ItemPlanoFinanceiroAluno</code>.
     */
    public List<ItemPlanoFinanceiroAlunoVO> getItemPlanoFinanceiroAlunoVOs() {
        if (itemPlanoFinanceiroAlunoVOs == null) {
            itemPlanoFinanceiroAlunoVOs = new ArrayList();
        }
        return (itemPlanoFinanceiroAlunoVOs);
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ItemPlanoFinanceiroAluno</code>.
     */
    public List<PlanoDescontoVO> getPlanoDescontoInstitucionalVOs() {
        List<PlanoDescontoVO> listaRetornar = new ArrayList();
        for (ItemPlanoFinanceiroAlunoVO item : getItemPlanoFinanceiroAlunoVOs()) {
            if (item.getTipoPlanoFinanceiroDescontoInstitucional()) {
                listaRetornar.add(item.getPlanoDesconto());
            }
        }
        Ordenacao.ordenarLista(listaRetornar, "ordenacao");
        return (listaRetornar);
    }

    public List<ConvenioVO> getPlanoFinanceiroConvenioVOs() {
        List<ConvenioVO> listaRetornar = new ArrayList();
        for (ItemPlanoFinanceiroAlunoVO item : getItemPlanoFinanceiroAlunoVOs()) {
            if (item.getTipoPlanoFinanceiroConvenio()) {
                listaRetornar.add(item.getConvenio());
            }
        }
        Ordenacao.ordenarLista(listaRetornar, "ordenacao");
        return (listaRetornar);
    }

    public List getItemPlanoFinanceiroAlunoVOs(String tipo) {
        List<ItemPlanoFinanceiroAlunoVO> listaRetornar = new ArrayList();
        Iterator i = getItemPlanoFinanceiroAlunoVOs().iterator();
        while (i.hasNext()) {
            ItemPlanoFinanceiroAlunoVO item = (ItemPlanoFinanceiroAlunoVO) i.next();
            if (item.getTipoItemPlanoFinanceiro().equals(tipo)) {
                listaRetornar.add(item);
            }
        }
        Ordenacao.ordenarLista(listaRetornar, "ordenacao");
        return listaRetornar;
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ItemPlanoFinanceiroAluno</code>.
     */
    public void setItemPlanoFinanceiroAlunoVOs(List itemPlanoFinanceiroAlunoVOs) {
        this.itemPlanoFinanceiroAlunoVOs = itemPlanoFinanceiroAlunoVOs;
    }

    public String getDocumentoComprobatorio() {
        return (documentoComprobatorio);
    }

    public void setDocumentoComprobatorio(String documentoComprobatorio) {
        this.documentoComprobatorio = documentoComprobatorio;
    }

    public String getJustificativa() {
        return (justificativa);
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Double getPercDescontoParcela() {
        return (percDescontoParcela);
    }

    public void setPercDescontoParcela(Double percDescontoParcela) {
        this.percDescontoParcela = percDescontoParcela;
    }

    public Double getPercDescontoMatricula() {
        return (percDescontoMatricula);
    }

    public void setPercDescontoMatricula(Double percDescontoMatricula) {
        this.percDescontoMatricula = percDescontoMatricula;
    }

    public Date getData() {
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
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
     * @return the ordemDescontoAluno
     */
    public Integer getOrdemDescontoAluno() {
        return ordemDescontoAluno;
    }

    /**
     * @param ordemDescontoAluno
     *            the ordemDescontoAluno to set
     */
    public void setOrdemDescontoAluno(Integer ordemDescontoAluno) {
        this.ordemDescontoAluno = ordemDescontoAluno;
    }

    /**
     * @return the ordemPlanoDesconto
     */
    public Integer getOrdemPlanoDesconto() {
        return ordemPlanoDesconto;
    }

    /**
     * @param ordemPlanoDesconto
     *            the ordemPlanoDesconto to set
     */
    public void setOrdemPlanoDesconto(Integer ordemPlanoDesconto) {
        this.ordemPlanoDesconto = ordemPlanoDesconto;
    }

    /**
     * @return the ordemConvenio
     */
    public Integer getOrdemConvenio() {
        return ordemConvenio;
    }

    /**
     * @param ordemConvenio
     *            the ordemConvenio to set
     */
    public void setOrdemConvenio(Integer ordemConvenio) {
        this.ordemConvenio = ordemConvenio;
    }

    /**
     * @return the valorDescontoMatricula
     */
    public Double getValorDescontoMatricula() {
        return valorDescontoMatricula;
    }

    /**
     * @param valorDescontoMatricula
     *            the valorDescontoMatricula to set
     */
    public void setValorDescontoMatricula(Double valorDescontoMatricula) {
        this.valorDescontoMatricula = valorDescontoMatricula;
    }

    /**
     * @return the valorDescontoParcela
     */
    public Double getValorDescontoParcela() {
        return valorDescontoParcela;
    }

    /**
     * @param valorDescontoParcela
     *            the valorDescontoParcela to set
     */
    public void setValorDescontoParcela(Double valorDescontoParcela) {
        this.valorDescontoParcela = valorDescontoParcela;
    }

    /**
     * @return the tipoDescontoMatricula
     */
    public String getTipoDescontoMatricula() {
        if (tipoDescontoMatricula == null) {
            tipoDescontoMatricula = "";
        }
        return tipoDescontoMatricula;
    }
    public String getTipoDescontoMatricula_Apresentar() {
		return TipoDescontoAluno.getSimbolo(getTipoDescontoMatricula());					
    }
    
    public String getTipoDescontoParcela_Apresentar() {
		return TipoDescontoAluno.getSimbolo(getTipoDescontoParcela());					
    }

    /**
     * @param tipoDescontoMatricula
     *            the tipoDescontoMatricula to set
     */
    public void setTipoDescontoMatricula(String tipoDescontoMatricula) {
        this.tipoDescontoMatricula = tipoDescontoMatricula;
    }

    /**
     * @return the tipoDescontoParcela
     */
    public String getTipoDescontoParcela() {
        if (tipoDescontoParcela == null) {
            tipoDescontoParcela = "";
        }
        return tipoDescontoParcela;
    }

    /**
     * @param tipoDescontoParcela
     *            the tipoDescontoParcela to set
     */
    public void setTipoDescontoParcela(String tipoDescontoParcela) {
        this.tipoDescontoParcela = tipoDescontoParcela;
    }

    public PlanoFinanceiroCursoVO getPlanoFinanceiroCursoVO() {
        if (planoFinanceiroCursoVO == null) {
            planoFinanceiroCursoVO = new PlanoFinanceiroCursoVO();
        }
        return planoFinanceiroCursoVO;
    }

    public void setPlanoFinanceiroCursoVO(PlanoFinanceiroCursoVO planoFinanceiroCursoVO) {
        this.planoFinanceiroCursoVO = planoFinanceiroCursoVO;
    }

    public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoVO() {
        if (condicaoPagamentoPlanoFinanceiroCursoVO == null) {
            condicaoPagamentoPlanoFinanceiroCursoVO = new CondicaoPagamentoPlanoFinanceiroCursoVO();
        }
        return condicaoPagamentoPlanoFinanceiroCursoVO;
    }

    public void setCondicaoPagamentoPlanoFinanceiroCursoVO(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO) {
        this.condicaoPagamentoPlanoFinanceiroCursoVO = condicaoPagamentoPlanoFinanceiroCursoVO;
    }

    public Integer obterPosicaoOrdemListaDesconto(List<OrdemDescontoVO> listaOrdemDesconto, String tipoDesconto) {
        for (OrdemDescontoVO ordemDesconto : listaOrdemDesconto) {
            if (ordemDesconto.getDescricaoDesconto().equals(tipoDesconto)) {
                return ordemDesconto.getPosicaoAtual();
            }
        }
        return 0;
    }

    public void aplicarOrdemDescontoDefinidaUsuarioPlanoFinanceiroAluno(List<OrdemDescontoVO> listaOrdemDesconto) {
        this.setOrdemConvenio(obterPosicaoOrdemListaDesconto(listaOrdemDesconto, "Convênio"));
        this.setOrdemDescontoAluno(obterPosicaoOrdemListaDesconto(listaOrdemDesconto, "Desconto Aluno"));
        this.setOrdemPlanoDesconto(obterPosicaoOrdemListaDesconto(listaOrdemDesconto, "Plano Desconto"));
        this.setOrdemDescontoProgressivo(obterPosicaoOrdemListaDesconto(listaOrdemDesconto, "Desc.Progressivo"));
    }

    public void alterarOrdemAplicacaoDescontosSubindoItem(List<OrdemDescontoVO> listaOrdemDesconto, OrdemDescontoVO itemSubir) {
        if (itemSubir.getPosicaoAtual().equals(0)) {
            // nao tem mais como subir
        }
        if (itemSubir.getPosicaoAtual().equals(1)) {
            // entao ele vai para a posicao 0, e o que está na posicao 0, vai
            // para 1)
            OrdemDescontoVO itemAcima = listaOrdemDesconto.get(0);
            itemAcima.setPosicaoAtual(1);
            itemSubir.setPosicaoAtual(0);
        }
        if (itemSubir.getPosicaoAtual().equals(2)) {
            // entao ele vai para a posicao 1, e o que está na posicao 2, vai
            // para 1)
            OrdemDescontoVO itemAcima = listaOrdemDesconto.get(1);
            itemAcima.setPosicaoAtual(2);
            itemSubir.setPosicaoAtual(1);
        }
        if (itemSubir.getPosicaoAtual().equals(3)) {
            // entao ele vai para a posicao 2, e o que está na posicao 2, vai
            // para 3)
            OrdemDescontoVO itemAcima = listaOrdemDesconto.get(2);
            itemAcima.setPosicaoAtual(3);
            itemSubir.setPosicaoAtual(2);
        }
        Ordenacao.ordenarLista(listaOrdemDesconto, "posicaoAtual");
        aplicarOrdemDescontoDefinidaUsuarioPlanoFinanceiroAluno(listaOrdemDesconto);
        // this.setOrdemConvenio(obterPosicaoOrdemListaDesconto(listaOrdemDesconto,
        // "Convênio"));
        // this.setOrdemDescontoAluno(obterPosicaoOrdemListaDesconto(listaOrdemDesconto,
        // "Desconto Aluno"));
        // this.setOrdemPlanoDesconto(obterPosicaoOrdemListaDesconto(listaOrdemDesconto,
        // "Plano Desconto"));
    }

    public OrdemDescontoVO obterOrdemDescontoProgressivoVO() {
        OrdemDescontoVO ordem = new OrdemDescontoVO();
        ordem.setDescricaoDesconto("Desc.Progressivo");
        ordem.setValorCheio(this.ordemDescontoProgressivoValorCheio);
        ordem.setPosicaoAtual(this.ordemDescontoProgressivo);
        return ordem;
    }

    public OrdemDescontoVO obterOrdemDescontoVOConvenio() {
        OrdemDescontoVO ordem = new OrdemDescontoVO();
        ordem.setDescricaoDesconto("Convênio");
        ordem.setValorCheio(this.ordemConvenioValorCheio);
        ordem.setPosicaoAtual(this.ordemConvenio);
        return ordem;
    }

    public OrdemDescontoVO obterOrdemDescontoVODescontoAluno() {
        OrdemDescontoVO ordem = new OrdemDescontoVO();
        ordem.setDescricaoDesconto("Desconto Aluno");
        ordem.setValorCheio(this.ordemDescontoAlunoValorCheio);
        ordem.setPosicaoAtual(this.ordemDescontoAluno);
        return ordem;
    }

    public OrdemDescontoVO obterOrdemDescontoVOPlanoDesconto() {
        OrdemDescontoVO ordem = new OrdemDescontoVO();
        ordem.setDescricaoDesconto("Plano Desconto");
        ordem.setValorCheio(this.ordemPlanoDescontoValorCheio);
        ordem.setPosicaoAtual(this.ordemPlanoDesconto);
        return ordem;
    }

    public List<OrdemDescontoVO> obterOrdemAplicacaoDescontosPadraoAtual() {
        if ((this.ordemDescontoAluno == null) || (this.ordemDescontoAluno >= 4)) {
            this.ordemDescontoAluno = 0;
        }
        if ((this.ordemPlanoDesconto == null) || (this.ordemPlanoDesconto >= 4)) {
            this.ordemPlanoDesconto = 1;
        }
        if ((this.ordemConvenio == null) || (this.ordemConvenio >= 4)) {
            this.ordemConvenio = 2;
        }
        if ((this.ordemDescontoProgressivo == null) || (this.ordemDescontoProgressivo > 4)) {
            this.ordemDescontoProgressivo = 3;
        }
        List<OrdemDescontoVO> obj = new ArrayList<OrdemDescontoVO>(0);
        obj.add(this.obterOrdemDescontoVODescontoAluno());
        obj.add(this.obterOrdemDescontoVOPlanoDesconto());
        obj.add(this.obterOrdemDescontoVOConvenio());
        obj.add(this.obterOrdemDescontoProgressivoVO());
        Ordenacao.ordenarLista(obj, "posicaoAtual");
        return obj;

    }

    public List<OrdemDescontoVO> inicializarOrdemAplicacaoDescontosPadrao() {
        this.ordemDescontoAluno = 0;
        this.ordemDescontoAlunoValorCheio = Boolean.TRUE;
        this.ordemPlanoDesconto = 1;
        this.ordemPlanoDescontoValorCheio = Boolean.TRUE;
        this.ordemConvenio = 2;
        this.ordemConvenioValorCheio = Boolean.TRUE;

        List<OrdemDescontoVO> obj = new ArrayList<OrdemDescontoVO>(0);
        obj.add(this.obterOrdemDescontoVODescontoAluno());
        obj.add(this.obterOrdemDescontoVOPlanoDesconto());
        obj.add(this.obterOrdemDescontoVOConvenio());
        Ordenacao.ordenarLista(obj, "posicaoAtual");
        return obj;
    }

    public void alterarOrdemAplicacaoDescontosDescentoItem(List<OrdemDescontoVO> listaOrdemDesconto, OrdemDescontoVO itemDescer) {
        if (itemDescer.getPosicaoAtual().equals(3)) {
        }
        if (itemDescer.getPosicaoAtual().equals(2)) {
            // entao ele vai para a posicao 1, e o que está na posicao 1, vai
            // para 2)
            OrdemDescontoVO itemAbaixo = listaOrdemDesconto.get(3);
            itemAbaixo.setPosicaoAtual(2);
            itemDescer.setPosicaoAtual(3);
        }
        if (itemDescer.getPosicaoAtual().equals(1)) {
            // entao ele vai para a posicao 0, e o que está na posicao 0, vai
            // para 1)
            OrdemDescontoVO itemAbaixo = listaOrdemDesconto.get(2);
            itemAbaixo.setPosicaoAtual(1);
            itemDescer.setPosicaoAtual(2);
        }
        if (itemDescer.getPosicaoAtual().equals(0)) {
            // entao ele vai para a posicao 1, e o que está na posicao 2, vai
            // para 1)
            OrdemDescontoVO itemAbaixo = listaOrdemDesconto.get(1);
            itemAbaixo.setPosicaoAtual(0);
            itemDescer.setPosicaoAtual(1);
        }
        Ordenacao.ordenarLista(listaOrdemDesconto, "posicaoAtual");
        aplicarOrdemDescontoDefinidaUsuarioPlanoFinanceiroAluno(listaOrdemDesconto);
    }

    /**
     * @return the ordemDescontoAlunoValorCheio
     */
    public Boolean getOrdemDescontoAlunoValorCheio() {
        if (ordemDescontoAlunoValorCheio == null) {
            ordemDescontoAlunoValorCheio = true;
        }
        return ordemDescontoAlunoValorCheio;
    }

    /**
     * @param ordemDescontoAlunoValorCheio
     *            the ordemDescontoAlunoValorCheio to set
     */
    public void setOrdemDescontoAlunoValorCheio(Boolean ordemDescontoAlunoValorCheio) {
        this.ordemDescontoAlunoValorCheio = ordemDescontoAlunoValorCheio;
    }

    /**
     * @return the ordemPlanoDescontoValorCheio
     */
    public Boolean getOrdemPlanoDescontoValorCheio() {
        if (ordemPlanoDescontoValorCheio == null) {
            ordemPlanoDescontoValorCheio = true;
        }
        return ordemPlanoDescontoValorCheio;
    }

    /**
     * @param ordemPlanoDescontoValorCheio
     *            the ordemPlanoDescontoValorCheio to set
     */
    public void setOrdemPlanoDescontoValorCheio(Boolean ordemPlanoDescontoValorCheio) {
        this.ordemPlanoDescontoValorCheio = ordemPlanoDescontoValorCheio;
    }

    /**
     * @return the ordemConvenioValorCheio
     */
    public Boolean getOrdemConvenioValorCheio() {
        if (ordemConvenioValorCheio == null) {
            ordemConvenioValorCheio = true;
        }
        return ordemConvenioValorCheio;
    }

    /**
     * @param ordemConvenioValorCheio
     *            the ordemConvenioValorCheio to set
     */
    public void setOrdemConvenioValorCheio(Boolean ordemConvenioValorCheio) {
        this.ordemConvenioValorCheio = ordemConvenioValorCheio;
    }

    public void atualizarSituacaoValorCheioOrdemDesconto(OrdemDescontoVO ordemClicou) {
        if (ordemClicou.isDescontoAluno()) {
            this.setOrdemDescontoAlunoValorCheio(ordemClicou.getValorCheio());
        }
        if (ordemClicou.isPlanoDesconto()) {
            this.setOrdemPlanoDescontoValorCheio(ordemClicou.getValorCheio());
        }
        if (ordemClicou.isConvenio()) {
            this.setOrdemConvenioValorCheio(ordemClicou.getValorCheio());
        }
        if (ordemClicou.isDescontoProgressivo()) {
            this.setOrdemDescontoProgressivoValorCheio(ordemClicou.getValorCheio());
        }
    }

    /**
     * @return the ordemDescontoProgressivo
     */
    public Integer getOrdemDescontoProgressivo() {
        return ordemDescontoProgressivo;
    }

    /**
     * @param ordemDescontoProgressivo the ordemDescontoProgressivo to set
     */
    public void setOrdemDescontoProgressivo(Integer ordemDescontoProgressivo) {
        this.ordemDescontoProgressivo = ordemDescontoProgressivo;
    }

    /**
     * @return the ordemDescontoProgressivoValorCheio
     */
    public Boolean getOrdemDescontoProgressivoValorCheio() {
        return ordemDescontoProgressivoValorCheio;
    }

    /**
     * @param ordemDescontoProgressivoValorCheio the ordemDescontoProgressivoValorCheio to set
     */
    public void setOrdemDescontoProgressivoValorCheio(Boolean ordemDescontoProgressivoValorCheio) {
        this.ordemDescontoProgressivoValorCheio = ordemDescontoProgressivoValorCheio;
    }

    public void setMatriculaPeriodo(Integer matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    public Integer getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = 0;
        }
        return matriculaPeriodo;
    }

    public Double getValorTotalDescontoMatriculaPorPlanoFinanceiroAlunoCalculado() {
        if (valorTotalDescontoMatriculaPorPlanoFinanceiroAlunoCalculado == null) {
            valorTotalDescontoMatriculaPorPlanoFinanceiroAlunoCalculado = 0.0;
        }
        return valorTotalDescontoMatriculaPorPlanoFinanceiroAlunoCalculado;
    }

    public void setValorTotalDescontoMatriculaPorPlanoFinanceiroAlunoCalculado(Double valorTotalDescontoMatriculaPorPlanoFinanceiroAlunoCalculado) {
        this.valorTotalDescontoMatriculaPorPlanoFinanceiroAlunoCalculado = valorTotalDescontoMatriculaPorPlanoFinanceiroAlunoCalculado;
    }

    public Double getValorTotalDescontoParcelaPorPlanoFinanceiroAlunoCalculado() {
        if (valorTotalDescontoParcelaPorPlanoFinanceiroAlunoCalculado == null) {
            valorTotalDescontoParcelaPorPlanoFinanceiroAlunoCalculado = 0.0;
        }
        return valorTotalDescontoParcelaPorPlanoFinanceiroAlunoCalculado;
    }

    public void setValorTotalDescontoParcelaPorPlanoFinanceiroAlunoCalculado(Double valorTotalDescontoParcelaPorPlanoFinanceiroAlunoCalculado) {
        this.valorTotalDescontoParcelaPorPlanoFinanceiroAlunoCalculado = valorTotalDescontoParcelaPorPlanoFinanceiroAlunoCalculado;
    }

    public void setDescontoProgressivoMatricula(DescontoProgressivoVO descontoProgressivoMatricula) {
        this.descontoProgressivoMatricula = descontoProgressivoMatricula;
    }

    public DescontoProgressivoVO getDescontoProgressivoMatricula() {
        if (descontoProgressivoMatricula == null) {
            descontoProgressivoMatricula = new DescontoProgressivoVO();
        }
        return descontoProgressivoMatricula;
    }

    /**
     * @return the descontoProgressivoPrimeiraParcela
     */
    public DescontoProgressivoVO getDescontoProgressivoPrimeiraParcela() {
        if (descontoProgressivoPrimeiraParcela == null) {
            descontoProgressivoPrimeiraParcela = new DescontoProgressivoVO();
        }
        return descontoProgressivoPrimeiraParcela;
    }

    /**
     * @param descontoProgressivoPrimeiraParcela the descontoProgressivoPrimeiraParcela to set
     */
    public void setDescontoProgressivoPrimeiraParcela(DescontoProgressivoVO descontoProgressivoPrimeiraParcela) {
        this.descontoProgressivoPrimeiraParcela = descontoProgressivoPrimeiraParcela;
    }

    public Boolean getDescontoValidoAteDataParcela() {
        if (descontoValidoAteDataParcela == null) {
            descontoValidoAteDataParcela = Boolean.FALSE;
        }
        return descontoValidoAteDataParcela;
    }

    public void setDescontoValidoAteDataParcela(Boolean descontoValidoAteDataParcela) {
        this.descontoValidoAteDataParcela = descontoValidoAteDataParcela;
    }
    
    public void verificarDadosConvenio(ItemPlanoFinanceiroAlunoVO obj) throws Exception {
        ItemPlanoFinanceiroAlunoVO.validarDados(obj);
        int index = 0;
        Iterator i = getItemPlanoFinanceiroAlunoVOs().iterator();
        while (i.hasNext()) {
            ItemPlanoFinanceiroAlunoVO objExistente = (ItemPlanoFinanceiroAlunoVO) i.next();   
                
            	if (objExistente.getConvenio().getParceiro().equals(obj.getConvenio().getParceiro())) {
                	
            		if(objExistente.getConvenio().getBolsaCusteadaParceiroMatricula() != 0 && obj.getConvenio().getBolsaCusteadaParceiroMatricula() != 0 ) {            		
            		throw new Exception("Não e Possivel inserir convênios que tenha desconto de matricula vinculados ao mesmo cadastro de parceiro.");
            		}
            		if(objExistente.getConvenio().getBolsaCusteadaParceiroParcela() != 0 && obj.getConvenio().getBolsaCusteadaParceiroParcela() != 0 ) {            		
            			throw new Exception("Não e Possivel inserir convênios que tenha desconto de parcela vinculados ao mesmo cadastro de parceiro.");
                		
                		}
				}
            	
            
            index++;
        }        
    }
}
