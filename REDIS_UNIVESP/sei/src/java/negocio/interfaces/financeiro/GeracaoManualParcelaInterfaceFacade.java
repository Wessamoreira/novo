package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import controle.arquitetura.AplicacaoControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.GeracaoManualParcelaVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoGeracaoManualParcelaEnum;

public interface GeracaoManualParcelaInterfaceFacade {

	public void realizarConsultarContaReceberGerar(GeracaoManualParcelaVO geracaoManualParcelaVO, UsuarioVO usuarioVO) throws Exception;
	
	public void persistir(GeracaoManualParcelaVO geracaoManualParcelaVO, UsuarioVO usuarioVO) throws Exception;
	
	public List<GeracaoManualParcelaVO> consultar(Integer unidadeEnsino, String curso, String turma, Date dataInicio, Date dataTermino, SituacaoProcessamentoGeracaoManualParcelaEnum situacao, Boolean controlarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception;
	
	public Integer consultarTotalRegistro(Integer unidadeEnsino, String curso, String turma, Date dataInicio, Date dataTermino, SituacaoProcessamentoGeracaoManualParcelaEnum situacao) throws Exception;
	
	
	
	public void validarDados(GeracaoManualParcelaVO geracaoManualParcelaVO) throws Exception;
	
	public GeracaoManualParcelaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception;

	void realizarGerarParcelas(GeracaoManualParcelaVO geracaoManualParcelaVO, AplicacaoControle aplicacaoControle) throws Exception;

	void realizarReinicializacaoGerarManualParcelas(AplicacaoControle aplicacaoControle) throws Exception;
	
}
