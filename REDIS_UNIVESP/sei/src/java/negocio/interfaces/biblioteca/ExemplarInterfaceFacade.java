package negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.ExemplarPainelGestorBibliotecaVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ExemplarInterfaceFacade {
	
	public List<ExemplarVO> consultar(String situacao, String codigoBarra, String catalogo, Integer biblioteca, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuario) throws Exception;

	public Boolean excluirExemplarCatalogoValidandoItemEmprestimo(ExemplarVO exemplar, UsuarioVO usuarioLogado) throws Exception;
	
    public ExemplarVO novo() throws Exception;

    public void incluir(ExemplarVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(final ExemplarVO obj, boolean periodico, UsuarioVO usuarioVO) throws Exception;

    public void excluir(ExemplarVO obj, UsuarioVO usuarioLogado) throws Exception;
    
    public List<ExemplarVO> consultaRapidaPorCodigoExemplar(Integer valorConsulta, UsuarioVO usuario) throws Exception;

    public void incluir(final ExemplarVO obj, boolean periodico, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    public ExemplarVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeBiblioteca(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public List consultarPorCatalogo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public List consultarPorSituacaoAtual(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino,  UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List<ExemplarVO> consultarPorCodigoCatalogoSituacaoAtual(Integer codigo, String situacaoAtual, boolean b,
            int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

    public List consultarPorTituloCatalogoDisponivel(String valorConsultarExemplar, String valor, boolean b, int nivelmontardadosTodos,  UsuarioVO usuario)
            throws Exception;

    public List consultarPorCodigoBarraDisponivel(String valorConsultarExemplar, String situacaoAtual, boolean verificarAcesso, int nivelmontardados,  Integer unidadeEnsino,   UsuarioVO usuario)
            throws Exception;

    public int consultarNrExemplaresCatalogoGravadosDisponiveis(ExemplarVO exemplar, String string) throws Exception;
    
    public int numeroExemplaresDisponiveisParaReserva(Integer codigoCatalogo) throws Exception;

    public void alterarSituacaoExemplaresParaDisponivel(ItemEmprestimoVO itemEmprestimoVO, UsuarioVO usuarioLogado) throws Exception;

    public void alterarEstadoExemplares(ExemplarVO exemplar, UsuarioVO usuarioLogado) throws Exception;

    public void alterarSituacaoExemplaresParaEmprestado(List<ItemEmprestimoVO> itemEmprestimoVOs, UsuarioVO usuarioLogado) throws Exception;

    public void executarAlteracaoSituacaoExemplares(ExemplarVO exemplar, String valor, UsuarioVO usuarioLogado) throws Exception;

    public void incluirListaExemplares(List<ExemplarVO> listaExemplaresGerados, UsuarioVO usuario) throws Exception;

    public void gerarExemplares(CatalogoVO catalogoVO, ExemplarVO exemplarVO, List<ExemplarVO> exemplarVOs, int numeroExemplaresAGerar, ConfiguracaoGeralSistemaVO conf) throws Exception;

    public void alterarListaExemplaresPorCodigoCatalogoSituacaoAtual(Integer catalogo, String situacaoAtual, List<ExemplarVO> objetos, UsuarioVO usuario)
            throws Exception;

    public List consultarPorCodigoCatalogoNaoInutilizados(Integer codigoCatalogo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception;

    public void validarDadosGeracaoExemplares(Integer numeroExemplaresASeremGerados, ExemplarVO exemplarVO, Boolean periodicos) throws Exception;

    public ExemplarVO consultarPorCodigoBarrasUnico(String codigoBarras, Integer biblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public ExemplarVO consultarPorCodigoBarrasUnicoCodigoCatalogo(String codigoBarras, Integer codigoCatalogo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void gerarExemplaresPeriodicos(ExemplarVO exemplarVO, List<ExemplarVO> exemplarVOs, int numeroExemplaresAGerar) throws Exception;    

    public List consultarPorCodigoAssinaturaPeriodico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeAssinaturaPeriodico(String assinaturaPeriodico, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ExemplarVO> consultaRapidaPorSituacaoAtual(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<ExemplarVO> consultaRapidaPorCodigoBarra(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<ExemplarVO> consultaRapidaPorTituloCatalogo(String valorConsulta, Integer unidadeEnsino, Integer biblioteca, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<ExemplarVO> consultaRapidaPorNomeBiblioteca(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<ExemplarVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<ExemplarVO> consultaRapidaPorTipoExemplar(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public Integer consultaTotalDeRegistroRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public Integer consultaTotalDeRegistroRapidaPorNomeBiblioteca(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public Integer consultaTotalDeRegistroRapidaPorTituloCatalogo(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public Integer consultaTotalDeRegistroRapidaPorCodigoBarra(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public Integer consultaTotalDeRegistroRapidaPorSituacaoAtual(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public Integer consultaTotalDeRegistroRapidaPorTipoExemplar(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public void carregarDados(ExemplarVO obj, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public void carregarDados(ExemplarVO obj, Integer unidadeEnsino, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ExemplarVO> consultaRapidaPorCodigoCatalogo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<ExemplarPainelGestorBibliotecaVO> consultarAcervoPeriodo(Date dataInicio, Date dataFim) throws Exception;

    public List consultarPorNomeBibliotecaETipoSaida(String valorConsulta, String tipoSaida, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarExemplaresDefasados(Integer codigoBiblioteca, String tipoDefasagem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    Integer consultarTotalRegistro(String situacao, String codigoBarra, String catalogo, Integer biblioteca, Integer unidadeEnsino) throws Exception;

    public List consultarPorCodigoECodigoBiblioteca(Integer valorConsulta, Integer codigoBiblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoBarraDisponivelECodigoBiblioteca(String valorConsulta, String disponivel, Integer codigoBiblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception;

    public List consultarPorSituacaoAtualECodigoBiblioteca(String valorConsulta, Integer codigoBiblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<ExemplarVO> consultaRapidaPorCodigoBiblioteca(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    Integer consultaTotalDeRegistroRapidaPorCodigoBiblioteca(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    List<ExemplarVO> consultaRapidaPorCodigoCatalogo(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    Integer consultaTotalDeRegistroRapidaPorCodigoCatalogo(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public void excluirExemplarCatalogos(CatalogoVO catalago, UsuarioVO usuarioLogado) throws Exception;

	Boolean realizarVerifacaoCatalogoPossuiExemplar(Integer codigoCatalogo) throws Exception;

	public List<ExemplarVO> consultaRapidaPorCodigoBarraCatalogo(String valorConsulta, Integer unidadeEnsino, Integer catalogo, Integer biblioteca, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public Boolean verificarExisteEmprestimoParaDeterminadoExemplar(Integer codigoExemplar) throws Exception;

	ExemplarVO consultarPorCodigoBarrasUnicoBiblioteca(String codigoBarras, Integer biblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public Boolean verificarExemplarPertenceBiblioteca(Integer codigoExemplar,Integer biblioteca) throws Exception;

	/**
	 * @author Rodrigo Wind - 02/03/2016
	 * @param codigoExemplar
	 * @return
	 * @throws Exception
	 */
	boolean realizaVerificacaoExemplarEstaEmprestado(Integer codigoExemplar) throws Exception;

	/**
	 * @author Rodrigo Wind - 02/03/2016
	 * @param exemplar
	 * @return
	 * @throws Exception
	 */
	List<String> consultarMensagemCadastrosReferenciamExemplar(Integer exemplar) throws Exception;
	
    public List<ExemplarVO> consultarPorCatalogoDisponivel(Integer catalogo, Integer biblioteca, String disponivel, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;			

	public void executarTransferenciaExemplarBiblioteca(ExemplarVO exemplarVO, UsuarioVO usuarioLogado) throws Exception;
	
	public int consultarNrExemplaresCatalogoGravadosDisponiveisParaEmprestimo(ExemplarVO exemplar, String string) throws Exception;

	List<ExemplarVO> consultarPorCatalogoBiblioteca(Integer codigoCatalogo, Integer biblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ExemplarVO> buscarExemplaresEdicao(Integer codigoExemplar , String TipoColunaAlterarExemplar , String valorConsultaAlteracao , Date valorConsultaAlteracaoData); 
	
	void marcarTodosListaExemplares(List<ExemplarVO> exemplarVO);

	void desmarcarTodosListaExemplares(List<ExemplarVO> exemplarVO);
	
	public void editarDadosExemplares(List<ExemplarVO> exemplarVOs, UsuarioVO usuario , String valorAlteracao , Boolean valorAlteracaoBoolean,Date valorAlteracaoData,String tipoAlterarColunaExemplar , Boolean assinaturaPeriodico) throws Exception;

	void realizarValidacaoDisponibilizarExemplarConsulta(ExemplarVO exemplarVO) throws Exception;
}
