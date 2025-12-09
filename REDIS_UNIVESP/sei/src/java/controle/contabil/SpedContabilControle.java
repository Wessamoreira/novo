package controle.contabil;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.contabil.SpedContabilVO;
import negocio.comuns.utilitarias.ControleConsulta; @Controller("SpedContabilControle")
@Scope("request")
@Lazy
public class SpedContabilControle extends SuperControle implements Serializable {

	private SpedContabilVO spedContabilVO;

	public SpedContabilControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {         removerObjetoMemoria(this);
		setSpedContabilVO(new SpedContabilVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	public String gerarArquivo() {
		try {
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	public void setSpedContabilVO(SpedContabilVO spedContabilVO) {
		this.spedContabilVO = spedContabilVO;
	}

	public SpedContabilVO getSpedContabilVO() {
		return spedContabilVO;
	}

}