package negocio.interfaces.arquitetura;

import java.util.List;

import negocio.comuns.arquitetura.PesquisaPadraoAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface PesquisaPadraoAlunoInterfaceFacade {

	List<PesquisaPadraoAlunoVO> consultarAlunoPorNomeCpfEmailResponsavelAutoComplete(String valorConsulta, UsuarioVO usuario) throws Exception;

}