package uk.ac.ebi.eva.contigalias.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.ebi.eva.contigalias.entities.SeqCol;
import uk.ac.ebi.eva.contigalias.repo.SeqColRepository;

import java.util.Optional;

@Service
public class SeqColService {

    @Autowired
    private SeqColRepository repository;

    public Optional<SeqCol> addSeqCol(SeqCol seqCol){
        SeqCol seqCol1 = repository.save(seqCol);
        return Optional.of(seqCol);
    }
}
