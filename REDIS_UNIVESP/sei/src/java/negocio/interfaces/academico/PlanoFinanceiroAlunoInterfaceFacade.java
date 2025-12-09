package negocio.interfaces.academico;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface PlanoFinanceiroAlunoInterfaceFacade {
	

    public PlanoFinanceiroAlunoVO novo() throws Exception;
    public void incluir(PlanoFinanceiroAlunoVO obj) throws Exception;
    public void alterar(PlanoFinanceiroAlunoVO obj) throws Exception;
    public void excluir(PlanoFinanceiroAlunoVO obj) throws Exception;
    public PlanoFinanceiroAlunoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorMatriculaMatricula(String valorConsulta,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomePessoa(String valorConsulta,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public List<PlanoFinanceiroAlunoVO> consultarPorCondicaoPagamentoPlanoFinanceiroCurso(Integer intValue, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;
    public PlanoFinanceiroAlunoVO consultarPorMatriculaMatriculaUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void persistir(PlanoFinanceiroAlunoVO obj) throws Exception;
    // TODO ((SEI CA37.1)) Adicionado para consultar o plano financeiro do aluno com base na matrícula periodo.
    public PlanoFinanceiroAlunoVO consultarPorMatriculaPeriodo(Integer codigoMatriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
 // TODO ((SEI CA37.1)) Adicionado para levar em conta o código da matricula periodo, sempre irá ser criado um novo
	// registro de plano financeiro aluno se a matrícula periodo for diferente.
    public void persistirLevandoEmContaMatriculaPeriodo(PlanoFinanceiroAlunoVO obj, Integer codigoMatriculaPeriodo) throws Exception;
    public void alterarPlanoFinanceiroAlunoDescontoProgressivoCondicaoPagamentoPlanoCursoPorMatriculaPeriodo(Integer descontoProgressivo, Integer descontoProgressivoMatricula, Integer planoFinanceiroCurso, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer matriculaPeriodo);
    public void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

    public void adicionarObjItemPlanoFinanceiroAlunoVOs(PlanoFinanceiroAlunoVO obj, ItemPlanoFinanceiroAlunoVO ipfa) throws Exception;
    public void realizarAlteracaoPlanoFinanceiroAlunoConformeMapaPendenciaControleCobranca(Integer matriculaPeriodo, Integer planoDesconto, Integer descontoProgressivo, UsuarioVO usuarioVO) throws Exception ;
    public void excluirComBaseNaMatriculaPeriodo(Integer codMatriculaPeriodo, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;
    public void excluirObjItemPlanoFinanceiroPlanoDescontoAlunoVOs(List itemPlanoFinanceiroAlunoVOs) throws Exception;
    /**
	 * @author Carlos Eugênio - 17/11/2016
	 * @param itemPlanoFinanceiroAlunoVOs
	 * @throws Exception
	 */
	void excluirObjItemPlanoFinanceiroAlunoConfiguradoRemoverRenovacao(List<ItemPlanoFinanceiroAlunoVO> itemPlanoFinanceiroAlunoVOs) throws Exception;
	PlanoFinanceiroAlunoVO consultarPorMatriculaPeriodoFichaAluno(Integer matriculaPeriodo, UsuarioVO usuarioVO) throws Exception;
}