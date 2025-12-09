package negocio.facade.jdbc.processosel;

import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.PeriodoChamadaProcSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoVO;

public interface PeriodoChamadaProcSeletivoInterfaceFacade {

	void persistir(List<PeriodoChamadaProcSeletivoVO> lista, UsuarioVO usuarioVO);

	void excluir(PeriodoChamadaProcSeletivoVO obj, UsuarioVO usuarioLogado) throws Exception;

	PeriodoChamadaProcSeletivoVO consultarPorCodigoProcessoSeletivoNumeroChamada(Integer procSeletivo,Integer nrChamada,int nivelMontarDados, UsuarioVO usuario) throws Exception;

	PeriodoChamadaProcSeletivoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario)throws Exception;

	void adicionarPeriodoChamadaProcSeletivoVO(PeriodoChamadaProcSeletivoVO periodoChamadaProcSeletivoVO,ProcSeletivoVO procSeletivoVO) throws Exception;

	void removerPeriodoChamadaProcSeletivoVO(PeriodoChamadaProcSeletivoVO periodoChamadaProcSeletivoVO,	ProcSeletivoVO procSeletivoVO) throws Exception;


	void incluirPeriodoChamadaProcSeletivo(Integer procSeletivo, List<PeriodoChamadaProcSeletivoVO> objetos,UsuarioVO usuarioVO) throws Exception;

	void alterarPeriodoChamadaProcSeletivo(Integer procSeletivo, List<PeriodoChamadaProcSeletivoVO> objetos, UsuarioVO usuarioVO) throws Exception;

	List<SelectItem> getConsultaComboNrChamadaProcSeletivo();

}
