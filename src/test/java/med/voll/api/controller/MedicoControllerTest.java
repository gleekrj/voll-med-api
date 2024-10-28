package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;

    @MockBean
    private MedicoRepository repository;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações estão inválidas")
    @WithMockUser
    void cadastrarMedicoCenario1() throws Exception {

        var response = mvc.perform(post("/medicos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver o código http 200 quando as informações estão válidas")
    @WithMockUser
    void cadastrarMedicoCenario2() throws Exception {

        var dadosMedico = new DadosCadastroMedico(
                "Medico",
                "medico@voll.med",
                "(21) 5555-99999",
                "123456",
                Especialidade.ORTOPEDIA,
                new DadosEndereco(
                        "R. X",
                        "Bairro",
                        "11234555",
                        "Cidade",
                        "MG",
                        null,
                        null
                )
        );

        var dadosDetalhamentoMedico = new DadosDetalhamentoMedico(
                null,
                "Medico",
                "medico@voll.med",
                "123456",
                "(21) 5555-99999",
                Especialidade.ORTOPEDIA,
                new Endereco(
                        "R. X",
                        "Bairro",
                        "11234555",
                        "Cidade",
                        "MG",
                        null,
                        null
                )
        );

        when(repository.save(any())).thenReturn(new Medico(dadosMedico));

        var response = mvc
                .perform(
                        post("/medicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dadosCadastroMedicoJson.
                                        write(dadosMedico).getJson()
                                )
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamentoMedico).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}