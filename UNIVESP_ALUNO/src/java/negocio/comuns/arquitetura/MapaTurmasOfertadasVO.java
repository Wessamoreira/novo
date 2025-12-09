package negocio.comuns.arquitetura;

import negocio.comuns.academico.TurmaVO;

import java.util.List;

public class MapaTurmasOfertadasVO {

    private String chave;
    private List<TurmaVO> listaTurma;


    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public List<TurmaVO> getListaTurma() {
        return listaTurma;
    }

    public void setListaTurma(List<TurmaVO> listaTurma) {
        this.listaTurma = listaTurma;
    }
}
