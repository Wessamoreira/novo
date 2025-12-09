package negocio.interfaces.financeiro;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.DevolucaoChequeVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface DevolucaoChequeInterfaceFacade {

    public DevolucaoChequeVO novo() throws Exception;
    public void incluir(DevolucaoChequeVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
    public void alterar(DevolucaoChequeVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(DevolucaoChequeVO obj, UsuarioVO usuario) throws Exception;
    public DevolucaoChequeVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<DevolucaoChequeVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<DevolucaoChequeVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<DevolucaoChequeVO> consultarPorNomeUsuario(String valorConsulta,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<DevolucaoChequeVO> consultarPorNomePessoa(String valorConsulta,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<DevolucaoChequeVO> consultarPorNumeroCheque(String valorConsulta,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public DevolucaoChequeVO consultarPorCodigoCheque(Integer codigoCheque, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception;
	/** 
	 * @author Wellington Rodrigues - 08/06/2015 
	 * @param devolucaoChequeVO
	 * @param usuario
	 * @throws Exception 
	 */
	void criarPendenciaChequeDevolvido(DevolucaoChequeVO devolucaoChequeVO, UsuarioVO usuario) throws Exception;
	/** 
	 * @author Wellington Rodrigues - 12/06/2015 
	 * @param obj
	 * @param usuario
	 * @throws Exception 
	 */
	void movimentacaoCaixa(DevolucaoChequeVO obj, UsuarioVO usuario) throws Exception;
}