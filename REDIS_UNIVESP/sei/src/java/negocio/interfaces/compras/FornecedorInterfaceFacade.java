package negocio.interfaces.compras;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FornecedorVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface FornecedorInterfaceFacade {
	

    public FornecedorVO novo() throws Exception;
    public void incluir(FornecedorVO obj,UsuarioVO usuario) throws Exception;
    public void alterar(FornecedorVO obj,UsuarioVO usuario) throws Exception;
    public void excluir(FornecedorVO obj, UsuarioVO usuarioVO) throws Exception;
    public FornecedorVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<FornecedorVO> consultarPorCodigo(Integer valorConsulta, String situacao,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<FornecedorVO> consultarPorNome(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<FornecedorVO> consultarPorRazaoSocial(String valorConsulta, String situacao,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<FornecedorVO> consultarPorNomeCidade(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<FornecedorVO> consultarPorCNPJ(String valorConsulta, String situacao,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<FornecedorVO> consultarPorInscEstadual(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<FornecedorVO> consultarPorRG(String valorConsulta, String situacao,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<FornecedorVO> consultarPorCPF(String valorConsulta, String situacao,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public FornecedorVO consultarPorCPFUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public FornecedorVO consultarPorCNPJUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<FornecedorVO> consultarPorCategoriaProduto(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;
    public void setIdEntidade(String aIdEntidade);
	void alterarSituacao(FornecedorVO obj, UsuarioVO usuario) throws Exception;
	public FornecedorVO consultarPorCnpjOuCpf(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}