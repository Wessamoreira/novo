package negocio.interfaces.estagio;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.estagio.GrupoPessoaItemVO;
import negocio.comuns.estagio.GrupoPessoaVO;

public interface GrupoPessoaItemInterfaceFacade {

	void persistir(List<GrupoPessoaItemVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);
	
	public void consultar(DataModelo dataModelo, GrupoPessoaItemVO obj) throws Exception;
	
	void inativarGrupoPessoaItemVO(GrupoPessoaItemVO obj, UsuarioVO usuario) throws Exception;

	void atualizarCampoStatus(GrupoPessoaItemVO obj, StatusAtivoInativoEnum statusAtivoInativo, UsuarioVO usuario);

	List<GrupoPessoaItemVO> consultarPorGrupoPessoaVO(GrupoPessoaVO obj, StatusAtivoInativoEnum statusAtivoInativoEnum, int nivelMontarDados, UsuarioVO usuario);
	
	List<GrupoPessoaItemVO> consultarPorGrupoPessoaItemVOExistenteEstagio(Integer pessoa, int nivelMontarDados, UsuarioVO usuario); 

	GrupoPessoaItemVO buscarGrupoPessoaItemDistribuicaoQuantitativoPorEstagio(TipoEstagioEnum tipoEstagioEnum, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	GrupoPessoaItemVO buscarGrupoPessoaItemDistribuicaoQuantitativoPorGrupoPessoaItemInativo(GrupoPessoaItemVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Integer consultarQuantidadeEstagioObrigatorioPorGrupoPessoaItemVO(GrupoPessoaItemVO obj, UsuarioVO usuario) throws Exception;

	Integer consultarQuantidadeEstagioNaoObrigatorioPorGrupoPessoaItemVO(GrupoPessoaItemVO obj, UsuarioVO usuario) throws Exception;

	GrupoPessoaItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario);

	List<PessoaVO> consultarPorPessoaGrupoPessoaItemVOExistenteEstagio(Integer pessoa, int nivelMontarDados,
			UsuarioVO usuario);

	

}
