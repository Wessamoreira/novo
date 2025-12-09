package negocio.interfaces.financeiro;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.BancoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface BancoInterfaceFacade {
	

    
    public void incluir(BancoVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(BancoVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(BancoVO obj, UsuarioVO usuario) throws Exception;
    public BancoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<BancoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<BancoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<BancoVO> consultarPorNrBanco(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public BancoVO consultarPorCodigoContaReceber(Integer codigoContaReceber, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)	throws Exception;
    public List<BancoVO> consultarPorBancoNivelComboBox(boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public BancoVO consultarPorCodigoAgencia(Integer agencia, UsuarioVO usuarioVO) throws Exception;
    BancoVO consultarPorNumeroContaCorrentePorDigitoContaCorrente(String numero, String digito, UsuarioVO usuarioVO) throws Exception;
}