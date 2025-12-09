package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade PlanoFinanceiroCurso. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PlanoDescontoDisponivelMatriculaVO extends SuperVO {

    private Integer codigo;
    private Integer condicaoPagamentoPlanoFinanceiroCurso;
    // Transient
    private Integer codigoTurma;
    private PlanoDescontoVO planoDesconto;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PlanoFinanceiroCurso</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public PlanoDescontoDisponivelMatriculaVO() {
        super();
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
    public static void validarDados(PlanoDescontoDisponivelMatriculaVO obj) throws ConsistirException {
        if (obj.getPlanoDesconto() == null || obj.getPlanoDesconto().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo PLANO DESCONTO (Plano Desconto Disponível Matrícula) deve ser informado.");
        }
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the condicaoPagamentoPlanoFinanceiroCurso
     */
    public Integer getCondicaoPagamentoPlanoFinanceiroCurso() {
        return condicaoPagamentoPlanoFinanceiroCurso;
    }

    /**
     * @param condicaoPagamentoPlanoFinanceiroCurso the condicaoPagamentoPlanoFinanceiroCurso to set
     */
    public void setCondicaoPagamentoPlanoFinanceiroCurso(Integer condicaoPagamentoPlanoFinanceiroCurso) {
        this.condicaoPagamentoPlanoFinanceiroCurso = condicaoPagamentoPlanoFinanceiroCurso;
    }

    /**
     * @return the planoDesconto
     */
    public PlanoDescontoVO getPlanoDesconto() {
        if (planoDesconto == null) {
            planoDesconto = new PlanoDescontoVO();
        }
        return planoDesconto;
    }

    /**
     * @param planoDesconto the planoDesconto to set
     */
    public void setPlanoDesconto(PlanoDescontoVO planoDesconto) {
        this.planoDesconto = planoDesconto;
    }

	public Integer getCodigoTurma() {
		if (codigoTurma == null) {
			codigoTurma = 0;
		}
		return codigoTurma;
	}

	public void setCodigoTurma(Integer codigoTurma) {
		this.codigoTurma = codigoTurma;
	}


}
