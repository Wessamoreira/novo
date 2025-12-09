package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;

public class ListagemDescontosAlunoUnidadeEnsinoVO {

	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<ListagemDescontosAlunosRelVO> listagemDescontosAlunosRelVOs;
	private List<ListagemDescontosAlunosPorTipoDescontoRelVO> listagemTotalDescontosRelVOs;
		
	private List<String> matriculaComDesconto;	
	
	public List<String> getMatriculaComDesconto() {
		if(matriculaComDesconto == null){
			matriculaComDesconto = new ArrayList<String>();
		}
		return matriculaComDesconto;
	}

	public void setMatriculaComDesconto(List<String> matriculaComDesconto) {
		this.matriculaComDesconto = matriculaComDesconto;
	}

	public Integer getQtdeMatriculaComDesconto() {
		return getMatriculaComDesconto().size();
	}

	public List<ListagemDescontosAlunosRelVO> getListagemDescontosAlunosRelVOs() {
		if(listagemDescontosAlunosRelVOs == null){
			listagemDescontosAlunosRelVOs = new ArrayList<ListagemDescontosAlunosRelVO>(0);
		}
		return listagemDescontosAlunosRelVOs;
	}

	public void setListagemDescontosAlunosRelVOs(List<ListagemDescontosAlunosRelVO> listagemDescontosAlunosRelVOs) {
		this.listagemDescontosAlunosRelVOs = listagemDescontosAlunosRelVOs;
	}

	public List<ListagemDescontosAlunosPorTipoDescontoRelVO> getListagemTotalDescontosRelVOs() {
		if(listagemTotalDescontosRelVOs == null){
			listagemTotalDescontosRelVOs = new ArrayList<ListagemDescontosAlunosPorTipoDescontoRelVO>(0);
		}
		return listagemTotalDescontosRelVOs;
	}

	public void setListagemTotalDescontosRelVOs(
			List<ListagemDescontosAlunosPorTipoDescontoRelVO> listagemTotalDescontosRelVOs) {
		this.listagemTotalDescontosRelVOs = listagemTotalDescontosRelVOs;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null){
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	
	
	
}
