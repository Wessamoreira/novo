package sms;

import java.util.List;

import com.human.gateway.client.ClienteHuman;
import com.human.gateway.client.MensagemIndividual;
import com.human.gateway.client.Retorno;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

public class HumanSms {

	public static String enviarMensagem(ConfiguracaoGeralSistemaVO conf, String celular, String texto) throws Exception {
		if (Uteis.validarEnvioEmail(conf.getIpServidor()) && !celular.equals("") && celular != null && !texto.equals("") && texto != null) {
			// Cria uma instancia do cliente de conexao
			ClienteHuman clienteHuman = new ClienteHuman();

			// Configura usuário e senha
			clienteHuman.setAccount(conf.getUsernameSMS());
			clienteHuman.setCode(conf.getSenhaSMS());

			// Cria uma mensagem individual
			MensagemIndividual mensagem = new MensagemIndividual();

			// Número do celular de destino
			mensagem.setTo(celular);

			// Conteudo do SMS.
			// Quantidade máxima de 141 caracteres.
			// Mensagem é enviada sem acentuação.
			mensagem.setMsg(texto);

			// Obtem o retorno da integração (codigo:descrição)
			List<Retorno> retornos = clienteHuman.send(mensagem);
			String resumo = "";
			for (Retorno retorno : retornos) {
				resumo += retorno.getReturnCode() + ":" + retorno.getReturnDescription() + ";";
			}
			if (resumo.length() > 128) {
				resumo = resumo.substring(0, 125) + "...";
			}
			return resumo;
		}
		return "";
	}
}
