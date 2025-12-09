package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ItemPrestacaoContaCategoriaDespesaVO;
import negocio.comuns.financeiro.ItemPrestacaoContaOrigemContaReceberVO;
import negocio.comuns.financeiro.ItemPrestacaoContaPagarVO;
import negocio.comuns.financeiro.ItemPrestacaoContaTurmaVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.comuns.financeiro.enumerador.TipoPrestacaoContaEnum;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface PrestacaoContaInterfaceFacade {
    
    public void persistir(PrestacaoContaVO prestacaoContaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
    
    public void excluir(PrestacaoContaVO prestacaoContaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
    
    List<PrestacaoContaVO> consultar(String descricao, TipoPrestacaoContaEnum tipoPrestacaoContaEnum, TurmaVO turma, UnidadeEnsinoVO unidadeEnsinoVO, 
            Date dataCadastroInicio, Date dataCadastroTermino, Integer limite, Integer pagina, Boolean validarAcesso, UsuarioVO usuarioVO , Date dataCompetenciaInicio, Date dataCompetenciaTermino) throws Exception;
    
    Integer consultarTotalRegistro(String descricao, TipoPrestacaoContaEnum tipoPrestacaoContaEnum, TurmaVO turma, UnidadeEnsinoVO unidadeEnsinoVO, 
            Date dataCadastroInicio, Date dataCadastroTermino, Date dataCompetenciaInicio, Date dataCompetenciaTermino) throws Exception;
    
    PrestacaoContaVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
    
   
    void removerItemPrestacaoContaCategoriaDespesaVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO) ;
   
    void adicionarItemPrestacaoContaOrigemContaReceberVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO) ;
   
    void removerItemPrestacaoContaOrigemContaReceberVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaOrigemContaReceberVO itemPrestacaoContaOrigemContaReceberVO) ;
   
    void adicionarItemPrestacaoContaTurmaVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO) ;
   
    void removerItemPrestacaoContaTurmaVO(PrestacaoContaVO prestacaoContaVO, ItemPrestacaoContaTurmaVO itemPrestacaoContaTurmaVO) ;

    List<ItemPrestacaoContaTurmaVO> consultarPrestacaoContaTurmaDisponivelPrestacaoConta(String identificadorTurma, Date dataInicio, Date dataFim, PrestacaoContaVO prestacaoContaVO, UsuarioVO usuarioVO) throws Exception;

    int consultarTotalPrestacaoContaPorDataCompetencia(PrestacaoContaVO obj);

    int consultarPrestacaoContasPosteriorCompetencia(PrestacaoContaVO obj);

    List<PrestacaoContaVO> consultarDadosGeracaoPrestacaoContaPorTurma(String favorecido, String tipoFiltroData, Date dataInicio, Date dataTermino, String tipoOrigem, PrestacaoContaVO prestacaoContaVO) throws Exception;

    void persistirPrestacoesContas(List<PrestacaoContaVO> listaPrestacaoConta, PrestacaoContaVO prestacaoConta) throws Exception ;
    
    public PrestacaoContaVO consultarSaldoAnteriorPorDataCompetencia(PrestacaoContaVO obj) throws Exception;
    
    public Boolean permiteAlterarSaldoAnteriorTurma(UsuarioVO usuario);
    
    public Boolean permiteAlterarSaldoAnteriorUnidadeEnsino(UsuarioVO usuario);

	void adicionarVariasItensPrestacaoContaPagarVOs(List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs, PrestacaoContaVO prestacaoContaVO, UsuarioVO usuario) throws Exception;

	void preencherItemPrestacaoContaCategoriaDespesa(PrestacaoContaVO prestacaoConta, ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO, UsuarioVO usuario);

	void adicionarItemPrestacaoContaCategoriaDespesaVO(PrestacaoContaVO prestacaoContaVO, CategoriaDespesaVO categoriaDespesaVO, Double valor, boolean valorInformadoManual);

	public Boolean permiteIncluirSaldoReceberTurma(UsuarioVO usuario);

	public Boolean permiteIncluirSaldoReceberUnidadeEnsino(UsuarioVO usuario);
	
	public Boolean permitirAdicionarNovoItemPrestacaoContaCategoriaDespesaVO(PrestacaoContaVO prestacaoContaVO , UsuarioVO usuario);
}
