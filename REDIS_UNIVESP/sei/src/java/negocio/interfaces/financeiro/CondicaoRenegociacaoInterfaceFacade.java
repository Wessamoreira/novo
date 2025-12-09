package negocio.interfaces.financeiro;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoFuncionarioVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoVO;
import negocio.comuns.financeiro.ItemCondicaoRenegociacaoVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface CondicaoRenegociacaoInterfaceFacade {
	

    public void persistir(CondicaoRenegociacaoVO obj, UsuarioVO usuarioLogado) throws Exception;
    public void excluir(CondicaoRenegociacaoVO obj) throws Exception;
    public List<CondicaoRenegociacaoVO> consultar(String descricao, Integer unidadeEnsino, Integer turno, Integer curso, Integer turma, 
            Integer contaCorrente,Integer perfilEconomico, String status, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void validarDados(CondicaoRenegociacaoVO obj) throws ConsistirException;
    public CondicaoRenegociacaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List<CondicaoRenegociacaoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario, int nivelMontarDados) throws Exception;
    public List<CondicaoRenegociacaoVO> consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario, int nivelMontarDados ) throws Exception;
    public List<CondicaoRenegociacaoVO> consultarPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario, int nivelMontarDados ) throws Exception;
    public List<CondicaoRenegociacaoVO> consultarPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario, int nivelMontarDados ) throws Exception;
    public List<CondicaoRenegociacaoVO> consultarPorCodigoContaCorrente(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario, int nivelMontarDados ) throws Exception;
    public List<CondicaoRenegociacaoVO> consultarPorCodigoDescontoProgressivo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario, int nivelMontarDados ) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public CondicaoRenegociacaoVO inicializarDadosCondicaoRenegociacaoNovo(UsuarioVO usuario) throws Exception;
    public void inicializarDadosCondicaoRenegociacaoEditar(CondicaoRenegociacaoVO obj, UsuarioVO usuario) throws Exception;
    public void adicionarObjItemCondicaoRenegociacaoVOs(CondicaoRenegociacaoVO objCondicaoRenegociacaoVO, ItemCondicaoRenegociacaoVO obj) throws Exception;
    public void excluirObjItemCondicaoRenegociacaoVOs(CondicaoRenegociacaoVO objCondicaoRenegociacaoVO, ItemCondicaoRenegociacaoVO obj) throws Exception;
    public ItemCondicaoRenegociacaoVO consultarObjItemCondicaoRenegociacaoVO(CondicaoRenegociacaoVO objCondicaoRenegociacaoVO, Integer codigo) throws Exception;
	public List<SelectItem> montarListSelectItemUnidadeEnsino(UsuarioVO usuario) throws Exception;
	public List<SelectItem> montarListSelectItemDescontoProgressivo(UsuarioVO usuario) throws Exception;
	public List<SelectItem> montarListSelectItemContaCorrente(Integer codigoUnidadeEnsino, Boolean obrigatorio, UsuarioVO usuario) throws Exception;
	public List<SelectItem> montarListSelectItemTurno(List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoUnidadeEnsinoVOs, UsuarioVO usuario) throws Exception;
	public List<CursoVO> consultarCurso(String campoConsultarCurso, String valorConsultarCurso, Integer codigoUnidadeEnsino, Integer codigoTurno, UsuarioVO usuario) throws Exception;
	public List<TurmaVO> consultarTurma(String campoConsultarCurso, String valorConsultarCurso, Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurno, UsuarioVO usuario) throws Exception;
    void realizarAtivacaoItemCondicaoRenegociacao(CondicaoRenegociacaoVO condicaoRenegociacaoVO, ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO, UsuarioVO usuarioLogado) throws Exception;
    void realizarInativacaoItemCondicaoRenegociacao(CondicaoRenegociacaoVO condicaoRenegociacaoVO, ItemCondicaoRenegociacaoVO itemCondicaoRenegociacaoVO, UsuarioVO usuarioLogado) throws Exception;
    void realizarAtivacaoCondicaoRenegociacao(CondicaoRenegociacaoVO condicaoRenegociacaoVO, UsuarioVO usuarioLogado) throws Exception;
    void realizarInativacaoCondicaoRenegociacao(CondicaoRenegociacaoVO condicaoRenegociacaoVO, UsuarioVO usuarioLogado) throws Exception;
    void realizarClonagem(CondicaoRenegociacaoVO condicaoRenegociacaoVO);
    
    void inicializarDadosUnidadeEnsinoSelecionada(CondicaoRenegociacaoVO condicaoRenegociacaoVO, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, UsuarioVO usuarioVO);
	List<SelectItem> montarListSelectItemTurno(Integer codigoUnidadeEnsino, UsuarioVO usuario) throws Exception;
	List<CursoVO> consultarCursoPorUnidadeEnsinoCondicaoRenegociacao(String campoConsultarCurso, String valorConsultarCurso, List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoUnidadeEnsinoVOs, Integer codigoTurno, UsuarioVO usuario) throws Exception;
	List<TurmaVO> consultarTurmaPorUnidadeEnsinoCondicaoRenegociacao(String campoConsultarCurso, String valorConsultarCurso, List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoUnidadeEnsinoVOs, Integer codigoCurso, Integer codigoTurno, UsuarioVO usuario) throws Exception;
	void adicionarCondicaoRenegociacaoFuncionarVOs(List<CondicaoRenegociacaoFuncionarioVO> listaCondicaoFuncionarioVOs, CondicaoRenegociacaoFuncionarioVO condicaoRenegociacaoFuncionarioAdicionarVO, UsuarioVO usuarioVO);
	void removerCondicaoRenegociacaoFuncionarVOs(List<CondicaoRenegociacaoFuncionarioVO> listaCondicaoFuncionarioVOs, CondicaoRenegociacaoFuncionarioVO condicaoRenegociacaoFuncionarioAdicionarVO, UsuarioVO usuarioVO);
	void inicializarDadosUnidadeEnsinoSelecionadaEdicao(CondicaoRenegociacaoVO condicaoRenegociacaoVO, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, UsuarioVO usuarioVO);
	CondicaoRenegociacaoVO consultarCondicaoRenegociacaoPorUnidadeEnsino(Integer unidadeEnsino, boolean visaoAdministrativa, UsuarioVO usuarioLogado) throws Exception;
	CondicaoRenegociacaoVO consultarCondicaoRenegociacaoDisponivelAluno(Integer perfilEconomino, Integer turma, Integer curso, Integer turno, int unidadeEnsino, double valor, Long nrDiasAtraso, Boolean visaoAdministrativa, UsuarioVO usuarioLogado) throws Exception;;
}