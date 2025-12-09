package negocio.interfaces.financeiro;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CondicaoDescontoRenegociacaoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoVO;


public interface ItemCondicaoDescontoRenegociacaoInterfaceFacade {
	
	public void persistir(CondicaoDescontoRenegociacaoVO obj, List<ItemCondicaoDescontoRenegociacaoVO> lista,  boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
    public void validarDados(ItemCondicaoDescontoRenegociacaoVO obj);
    public ItemCondicaoDescontoRenegociacaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    List<ItemCondicaoDescontoRenegociacaoVO> consultarItemCondicaoDescontoRenegociacaoPorCondicaoDescontoRenegociacao(Integer condicaoDescontoRenegociacao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	ItemCondicaoDescontoRenegociacaoVO inicializarDadosCondicaoRenegociacaoNovo(UsuarioVO usuario) throws Exception;
	void adicionarItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs(CondicaoDescontoRenegociacaoVO cdr, ItemCondicaoDescontoRenegociacaoVO icdr, ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO icdrue);
	void removerItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs(ItemCondicaoDescontoRenegociacaoVO obj, ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO icdrue);
	void ativarItemCondicaoDescontoRenegociacaoVO(ItemCondicaoDescontoRenegociacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);
	void inativarItemCondicaoDescontoRenegociacaoVO(ItemCondicaoDescontoRenegociacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);
	ItemCondicaoDescontoRenegociacaoVO consultarItemCondicaoDescontoRenegociacaoDisponivel(Long nrDiasAtraso, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception;
	ItemCondicaoDescontoRenegociacaoVO consultarItemCondicaoDescontoRenegociacaoPorNegociacaoContaReceber(Integer negociacaoContaReceber, UsuarioVO usuarioLogado) throws Exception;
    
}