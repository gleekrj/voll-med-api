package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

@Component
public class ValidadorDuracaoConsulta implements ValidadorAgendamentoDeConsulta{

    public void validar(DadosAgendamentoConsulta dados){

        if(dados.data().getMinute() != 0){
            throw new ValidacaoException("A consulta deve ser agendada a cada hora inteira.");
        }
    }
}
