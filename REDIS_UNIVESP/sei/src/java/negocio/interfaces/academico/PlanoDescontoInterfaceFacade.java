package negocio.interfaces.academico;
import java.util.List;

import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface PlanoDescontoInterfaceFacade {
	

    public PlanoDescontoVO novo() throws Exception;
    public void incluir(PlanoDescontoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void alterar(PlanoDescontoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(PlanoDescontoVO obj, UsuarioVO usuarioVO) throws Exception;
    public PlanoDescontoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<PlanoDescontoVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, Integer limite, Integer offset) throws Exception;
    public List<PlanoDescontoVO> consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, Integer limite, Integer offset) throws Exception;
//    public List<PlanoDescontoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario, Integer limite, Integer offset) throws Exception;
    public List<PlanoDescontoVO> consultarPorAtivoOuInativo(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, Integer limite, Integer offset) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    /**
     * Método responsável por verificar se esta ativando ou inativando o objeto. Seta o campo ativo, dataAtivacao e responsavelAtivacao caso
     * esteja ativando e chama método que ativa no BD. Caso esteja inativando, seta os campos ativo, dataInativacao, responsavelInativacao
     * e chama método inativa no BD.
     * @param planoDescontoVO
     * @throws Exception
     */
    public String realizarAtivacaoInativacao(PlanoDescontoVO planoDescontoVO, UsuarioVO usuario) throws Exception;

    /**
     * Consulta por nome e ativa = true; Restorna um List Contendo vários objetos da classe <code>PlanoDescontoVO</code>
     * @param valorConsulta
     * @param controlarAcesso
     * @return List
     * @throws Exception
     */
    public List consultarPorNomeSomenteAtiva(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarPlanoDescontoNivelComboBox(UsuarioVO usuarioVO) throws Exception;

    public void alterarDadosDescritivos(final PlanoDescontoVO obj) throws Exception;

    public List consultarPorPlanoFinanceiroAluno(Integer planoFinanceiroAluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarPorContaReceber(Integer contaReceber, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarPlanoDescontoDisponivelCondicaoPagamentoPlanoFinanceiroCurso(Integer codCondicaoPagamento, UsuarioVO usuario) throws Exception;
	void realizarMarcacaoDiaUtil(PlanoDescontoVO planoDescontoVO);
	void realizarMarcacaoDiaFixo(PlanoDescontoVO planoDescontoVO);
	void realizarMarcacaoDescontoValidoAteDataVencimento(PlanoDescontoVO planoDescontoVO);
        
    public List consultarPlanoDescontoAtivoNivelComboBox(UsuarioVO usuarioVO) throws Exception;

	List<PlanoDescontoVO> consultarPorCategoriaDesconto(Integer categoriaDesconto, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	List<PlanoDescontoVO> consultarPlanoDescontoFiltrarRenovacaoTurmaNivelCombobox(Integer turma, Integer gradeCurricular, String ano, String semestre) throws Exception;
	Integer consultarTotalRegistroPorCodigo(Integer valorConsulta, Integer unidadeEnsino) throws Exception;
	Integer consultarTotalRegistroPorAtivoOuInativo(String valorConsulta, Integer unidadeEnsino) throws Exception;
	Integer consultarTotalRegistroPorNome(String valorConsulta, Integer unidadeEnsino) throws Exception;
	
	List<PlanoDescontoVO> consultarPlanoDescontoAtivoPorUnidadeEnsinoNivelComboBox(UnidadeEnsinoVO unidadeEnsinoVO, Boolean ativo, UsuarioVO usuarioVO) throws Exception;
	
	public List<PlanoDescontoVO> consultarPorNomeSomenteAtivaConsiderandoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}
