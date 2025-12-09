package negocio.interfaces.arquitetura;

import java.util.List;

import negocio.comuns.arquitetura.BloqueioFuncaoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface FuncaoBloqueadaInterfaceFacade {
	BloqueioFuncaoVO montarListaTurmasBloqueada(List<Integer> listaTurma, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	BloqueioFuncaoVO montarListaConfiguracaoNotaFiscalBloqueada(List<Integer> listaConfig, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	BloqueioFuncaoVO montarProcessamentoProvaPresencial(UsuarioVO usuario) throws Exception;
	BloqueioFuncaoVO montarListaUnidadeEnsinoGeracaoParcela(List<Integer> listaUnidadesEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	BloqueioFuncaoVO montarListaTurmaRealizandoRenovacao(int nivelMontarDados, UsuarioVO usuario) throws Exception;
	BloqueioFuncaoVO montarListaProfessorTurmas(List<Integer> listaProfessorTurma, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;
}
