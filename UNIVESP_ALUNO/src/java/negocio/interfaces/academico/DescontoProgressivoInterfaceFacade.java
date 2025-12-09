package negocio.interfaces.academico;
import java.util.List;

//import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface DescontoProgressivoInterfaceFacade {
	

//    public DescontoProgressivoVO novo() throws Exception;
//    public void incluir(DescontoProgressivoVO obj, UsuarioVO usuario) throws Exception;
//    public void alterar(DescontoProgressivoVO obj, UsuarioVO usuario) throws Exception;
//    public void excluir(DescontoProgressivoVO obj, UsuarioVO usuario) throws Exception;
//    public DescontoProgressivoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
//    public void realizarAtivacaoDescontoProgressivo(DescontoProgressivoVO descontoProgressivoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception;
//    public void realizarInativacaoDescontoProgressivo(DescontoProgressivoVO descontoProgressivoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception;
    public List consultarDescontoProgressivoComboBox(UsuarioVO usuarioVO) throws Exception;
    public List consultarDescontoProgressivoPorSituacao(UsuarioVO usuarioVO, Boolean ativo) throws Exception;
//    public List<DescontoProgressivoVO> consultarPorNomeAtivos(String nome, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
//    public List<DescontoProgressivoVO> consultarDescontoProgressivoAtivosComboBox(UsuarioVO usuarioVO) throws Exception;
//	List<DescontoProgressivoVO> consultarPorPlanoFinannceiroAluno(Integer planoFinanceiroAluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	Boolean consultarDescontoProgressivoUtilizado(Integer codigo) throws Exception;
//	public List<DescontoProgressivoVO> consultarPorNomeSituacao(String nome, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
//	public List<DescontoProgressivoVO> consultarPorCodigoSituacao(Integer codigo, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
//	void carregarDados(DescontoProgressivoVO descontoProgressivoVO, UsuarioVO usuarioVO) throws Exception;
}