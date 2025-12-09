package negocio.interfaces.compras;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.SolicitacaoPgtoServicoAcademicoVO;

/**
 * Interface repons?vel por criar uma estrutura padr?o de comunida??o entre a camada de controle e camada de neg?cio (em
 * especial com a classe Fa?ade). Com a utiliza??o desta interface ? poss?vel substituir tecnologias de uma camada da
 * aplica??o com m?nimo de impacto nas demais. Al?m de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de neg?cio, por interm?dio de sua classe Fa?ade (respons?vel por persistir os dados das classes VO).
 */
public interface SolicitacaoPgtoServicoAcademicoInterfaceFacade {

	public SolicitacaoPgtoServicoAcademicoVO novo() throws Exception;

	public void incluir(SolicitacaoPgtoServicoAcademicoVO obj) throws Exception;

	public void alterar(SolicitacaoPgtoServicoAcademicoVO obj) throws Exception;

	public void excluir(SolicitacaoPgtoServicoAcademicoVO obj) throws Exception;

	public SolicitacaoPgtoServicoAcademicoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorDate(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorQuantidadeHoras(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorValorHora(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorValorTotal(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorDataAutorizacao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorParecerResponsavel(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorDescricaoPrevisaoCustos(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorTipoDestinatarioPagamento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorIdentificadorCentroDespesaCentroDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);
}