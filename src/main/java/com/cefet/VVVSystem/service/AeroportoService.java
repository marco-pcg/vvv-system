package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Aeroporto;
import com.cefet.VVVSystem.domain.entity.Cidade;
import com.cefet.VVVSystem.domain.repository.AeroportoRepository;
import com.cefet.VVVSystem.domain.repository.CidadeRepository;
import com.cefet.VVVSystem.dto.AeroportoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AeroportoService {

    private final AeroportoRepository aeroportoRepository;
    private final CidadeRepository cidadeRepository;

    public AeroportoService(AeroportoRepository aeroportoRepository, CidadeRepository cidadeRepository) {
        this.aeroportoRepository = aeroportoRepository;
        this.cidadeRepository = cidadeRepository;
    }

    @Transactional(readOnly = true)
    public List<AeroportoDTO> findAll() {
        return aeroportoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AeroportoDTO findById(Long id) {
        Aeroporto aeroporto = aeroportoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aeroporto não encontrado com ID: " + id));
        return toDTO(aeroporto);
    }

    @Transactional
    public AeroportoDTO create(AeroportoDTO dto) {
        if (aeroportoRepository.findByCodigo(dto.codigo().toUpperCase()).isPresent()) {
            throw new RuntimeException("Já existe um aeroporto cadastrado com este código.");
        }

        Cidade cidade = cidadeRepository.findById(dto.cidadeId())
                .orElseThrow(() -> new RuntimeException("Cidade vinculada não encontrada."));

        Aeroporto aeroporto = new Aeroporto();
        aeroporto.setCodigo(dto.codigo().toUpperCase());
        aeroporto.setNome(dto.nome());
        aeroporto.setCidade(cidade);

        Aeroporto saved = aeroportoRepository.save(aeroporto);
        return toDTO(saved);
    }

    @Transactional
    public AeroportoDTO update(Long id, AeroportoDTO dto) {
        Aeroporto aeroporto = aeroportoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aeroporto não encontrado com ID: " + id));

        aeroportoRepository.findByCodigo(dto.codigo().toUpperCase())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Já existe outro aeroporto cadastrado com este código.");
                });

        Cidade cidade = cidadeRepository.findById(dto.cidadeId())
                .orElseThrow(() -> new RuntimeException("Cidade vinculada não encontrada."));

        aeroporto.setCodigo(dto.codigo().toUpperCase());
        aeroporto.setNome(dto.nome());
        aeroporto.setCidade(cidade);

        Aeroporto updated = aeroportoRepository.save(aeroporto);
        return toDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!aeroportoRepository.existsById(id)) {
            throw new RuntimeException("Aeroporto não encontrado com ID: " + id);
        }
        aeroportoRepository.deleteById(id);
    }

    private AeroportoDTO toDTO(Aeroporto aeroporto) {
        return new AeroportoDTO(
                aeroporto.getId(),
                aeroporto.getCodigo(),
                aeroporto.getNome(),
                aeroporto.getCidade().getId()
        );
    }
}