package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.TaxaVO;
import negocio.comuns.financeiro.TaxaValorVO;


public interface TaxaInterfaceFacade {

	public List<TaxaVO> consultarDescricaoTaxa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public TaxaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluirTaxa(TaxaVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterar(TaxaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;

	public void incluir(TaxaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;

	List<TaxaVO> consultarTaxaPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Double consultarValorTaxaAtual(Integer taxa) throws Exception;

	public void adicionarObjTaxaValorVOs(TaxaVO taxaVO, TaxaValorVO taxaValorVO) throws Exception;

	public void ativarTaxa(Integer codTaxa, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;

	public void inativarTaxa(Integer codTaxa, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;



}