package relatorio.negocio.jdbc.biblioteca;

import relatorio.negocio.interfaces.biblioteca.TicketRelInterface;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;


import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.biblioteca.TicketRelVO;

import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class TicketRel extends SuperRelatorio implements TicketRelInterface {

    public TicketRelVO montarDadosTicketEmprestimo(EmprestimoVO emprestimoVO, List<ItemEmprestimoVO> listaItensEmprestados, List<ItemEmprestimoVO> listaItensRenovacao, List<ItemEmprestimoVO> listaItensDevolucao, String textoPadraoEmprestimo, String textoPadraoDevolucao,  String textoPadraoRenovacao, String matriculaAluno, String matriculaFuncionario) throws Exception {
        TicketRelVO ticketEmprestimoRelVO = new TicketRelVO();
        ticketEmprestimoRelVO.setTextoPadrao(textoPadraoEmprestimo);
        ticketEmprestimoRelVO.setTextoPadraoDevolucao(textoPadraoDevolucao);
        ticketEmprestimoRelVO.setTextoPadraoRenovacao(textoPadraoRenovacao);
        ticketEmprestimoRelVO.setNomeBiblioteca(emprestimoVO.getBiblioteca().getNome());
        ticketEmprestimoRelVO.setTipoPessoa(emprestimoVO.getTipoPessoa());
        if (emprestimoVO.getTipoPessoa().equals("AL")) {
            ticketEmprestimoRelVO.setNumeroMatricula(matriculaAluno);
        } else {
            ticketEmprestimoRelVO.setNumeroMatricula(matriculaFuncionario);
        }
        ticketEmprestimoRelVO.setNomePessoa(emprestimoVO.getPessoa().getNome());
        ticketEmprestimoRelVO.setNomeCurso("");//verificar q curso será setado
        ticketEmprestimoRelVO.setItemEmprestimoVOs(listaItensEmprestados);
        ticketEmprestimoRelVO.setItemEmprestimoVOsDevolucao(listaItensDevolucao);
        ticketEmprestimoRelVO.setItemEmprestimoVOsRenovacao(listaItensRenovacao);
        return ticketEmprestimoRelVO;
    }

    public List<TicketRelVO> criarObjeto(EmprestimoVO emprestimoVO, List<ItemEmprestimoVO> listaItensEmprestimo, String textoPadraoEmprestimo, String textoPadraoDevolucao, String textoPadraoRenovacao, String matriculaAluno, String matriculaFuncionario) throws Exception {
        List<TicketRelVO> listaEmprestimo = new ArrayList<TicketRelVO>(0);
        List<ItemEmprestimoVO> listaItensEmprestimoParaDevolucao = new ArrayList<ItemEmprestimoVO>();
        List<ItemEmprestimoVO> listaItensEmprestimoNovo = new ArrayList<ItemEmprestimoVO>();
        List<ItemEmprestimoVO> listaItensEmprestimoRenovacao = new ArrayList<ItemEmprestimoVO>();
        for (ItemEmprestimoVO ie : listaItensEmprestimo) {        	
			if (ie.getDevolverEmprestimo()) {
                listaItensEmprestimoParaDevolucao.add(ie);
            } else if (ie.getEmprestar()) {
                listaItensEmprestimoNovo.add(ie);
            } else if (ie.getRenovarEmprestimo()) {
                listaItensEmprestimoRenovacao.add(ie);
            }
        }
        if(listaItensEmprestimoParaDevolucao.isEmpty() && listaItensEmprestimoRenovacao.isEmpty() && listaItensEmprestimoNovo.isEmpty()){
        	listaItensEmprestimoNovo.addAll(listaItensEmprestimo);
        }
        listaEmprestimo.add(montarDadosTicketEmprestimo(emprestimoVO, listaItensEmprestimoNovo, listaItensEmprestimoRenovacao, listaItensEmprestimoParaDevolucao, textoPadraoEmprestimo, textoPadraoDevolucao, textoPadraoRenovacao, matriculaAluno, matriculaFuncionario));
        return listaEmprestimo;
    }

    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator);
    }

    @Override
    public String designerTicketEmprestimo() {
        return caminhoBaseRelatorio() + "TicketEmprestimo.jrxml";
    }
}
