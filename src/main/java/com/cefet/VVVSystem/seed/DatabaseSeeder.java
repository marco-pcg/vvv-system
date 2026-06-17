package com.cefet.VVVSystem.seed;

import com.cefet.VVVSystem.domain.entity.*;
import com.cefet.VVVSystem.domain.enums.*;
import com.cefet.VVVSystem.domain.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Component
@Profile("!test") // Não executa durante os testes
public class DatabaseSeeder implements CommandLineRunner {

    private final CidadeRepository cidadeRepository;
    private final TransportadoraRepository transportadoraRepository;
    private final ModalRepository modalRepository;
    private final ViagemRepository viagemRepository;
    private final ClienteRepository clienteRepository;
    private final PassageiroRepository passageiroRepository;

    public DatabaseSeeder(CidadeRepository cidadeRepository,
                          TransportadoraRepository transportadoraRepository,
                          ModalRepository modalRepository,
                          ViagemRepository viagemRepository,
                          ClienteRepository clienteRepository,
                          PassageiroRepository passageiroRepository) {
        this.cidadeRepository = cidadeRepository;
        this.transportadoraRepository = transportadoraRepository;
        this.modalRepository = modalRepository;
        this.viagemRepository = viagemRepository;
        this.clienteRepository = clienteRepository;
        this.passageiroRepository = passageiroRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (cidadeRepository.count() > 0) {
            System.out.println("Banco de dados já populado. Pulando Seeder...");
            return;
        }

        System.out.println("Executando Data Seeder para ambiente de desenvolvimento...");

        // 1. Cidades
        Cidade rj = new Cidade();
        rj.setNome("Rio de Janeiro");
        rj.setUf(UF.RJ);
        rj = cidadeRepository.save(rj);

        Cidade sp = new Cidade();
        sp.setNome("São Paulo");
        sp.setUf(UF.SP);
        sp = cidadeRepository.save(sp);

        // 2. Transportadora
        Transportadora transportadora = new Transportadora();
        transportadora.setNome("Viação Teste Rápido");
        transportadora.setCnpj("12345678000199");
        transportadora = transportadoraRepository.save(transportadora);

        // 3. Modal
        Modal onibus = new Modal();
        onibus.setCodigo("BUS-001");
        onibus.setTipo(TipoModal.ONIBUS);
        onibus.setCapacidade(40);
        onibus.setStatusOperacional(StatusOperacional.OPERACIONAL);
        onibus.setTransportadora(transportadora);
        onibus = modalRepository.save(onibus);

        // 4. Viagem
        Viagem viagem = new Viagem();
        viagem.setCidadeOrigem(rj);
        viagem.setCidadeDestino(sp);
        viagem.setPartida(LocalDateTime.now().plusDays(5));
        viagem.setChegada(LocalDateTime.now().plusDays(5).plusHours(6));
        viagem.setPreco(new BigDecimal("150.00"));
        viagem.setStatus(StatusViagem.AGENDADA);
        viagem.setModais(Set.of(onibus));
        viagem = viagemRepository.save(viagem);

        // 5. Cliente e Passageiro (Party Pattern)
        Pessoa pessoaCliente = new Pessoa();
        pessoaCliente.setNome("João Silva (Cliente)");
        pessoaCliente.setCpf("11122233344");
        pessoaCliente.setEmail("joao.cliente@teste.com");
        pessoaCliente.setTelefone("21999999999");
        pessoaCliente.setDataNascimento(LocalDate.of(1990, 5, 20));

        Cliente cliente = new Cliente();
        cliente.setPessoa(pessoaCliente);
        cliente = clienteRepository.save(cliente);

        Pessoa pessoaPassageiro = new Pessoa();
        pessoaPassageiro.setNome("Maria Silva (Passageira)");
        pessoaPassageiro.setCpf("55566677788");
        pessoaPassageiro.setEmail("maria.passageira@teste.com");
        pessoaPassageiro.setDataNascimento(LocalDate.of(1995, 8, 15));

        Passageiro passageiro = new Passageiro();
        passageiro.setPessoa(pessoaPassageiro);
        passageiro.setPossuiAcompanhante(false);
        passageiro = passageiroRepository.save(passageiro);

        System.out.println("=================================================");
        System.out.println(" DADOS GERADOS PARA TESTE NO SWAGGER (VendaOnline):");
        System.out.println(" ID Viagem: " + viagem.getId());
        System.out.println(" ID Cliente: " + cliente.getId());
        System.out.println(" ID Passageiro: " + passageiro.getId());
        System.out.println("=================================================");
    }
}
