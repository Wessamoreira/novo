package negocio.interfaces.financeiro;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContratosReceitasVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ContratosReceitasInterfaceFacade {
	

    public ContratosReceitasVO novo() throws Exception;
    public void incluir(ContratosReceitasVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
    public void alterar(ContratosReceitasVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(ContratosReceitasVO obj, UsuarioVO usuario) throws Exception;
    public ContratosReceitasVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ContratosReceitasVO> consultarPorIdentificadorCentroReceitaCentroReceita(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ContratosReceitasVO> consultarPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ContratosReceitasVO> consultarPorDataTermino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ContratosReceitasVO> consultarPorDataInicio(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ContratosReceitasVO> consultarPorTipoContrato(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ContratosReceitasVO> consultarPorNomeSacado(String valorConsulta, String tipoSacado, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ContratosReceitasVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
	void alterarFinalizacaoContratoReceitasPorCodigoReceita(ContratosReceitasVO contratosReceitasVO, UsuarioVO usuarioVO) throws Exception;
	void alterarValorDasParcelasPendentes(ContratosReceitasVO contratosReceitasVO, Double novoValor, String motivoAlteracao,
			UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;
	
	List<ContratosReceitasVO> consultarPorMatriculaAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}