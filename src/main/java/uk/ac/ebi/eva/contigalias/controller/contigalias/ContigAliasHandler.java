/*
 * Copyright 2020 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.ebi.eva.contigalias.controller.contigalias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import uk.ac.ebi.eva.contigalias.entities.AssemblyEntity;
import uk.ac.ebi.eva.contigalias.entities.ChromosomeEntity;
import uk.ac.ebi.eva.contigalias.service.AssemblyService;
import uk.ac.ebi.eva.contigalias.service.ChromosomeService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static uk.ac.ebi.eva.contigalias.controller.BaseHandler.convertToPage;
import static uk.ac.ebi.eva.contigalias.controller.BaseHandler.generatePagedModelFromPage;

@Service
public class ContigAliasHandler {

    private final AssemblyService assemblyService;

    private final ChromosomeService chromosomeService;

    private final PagedResourcesAssembler<AssemblyEntity> assemblyAssembler;

    private final PagedResourcesAssembler<ChromosomeEntity> chromosomeAssembler;

    @Autowired
    public ContigAliasHandler(AssemblyService assemblyService,
                              ChromosomeService chromosomeService,
                              PagedResourcesAssembler<AssemblyEntity> assemblyAssembler,
                              PagedResourcesAssembler<ChromosomeEntity> chromosomeAssembler) {
        this.assemblyService = assemblyService;
        this.chromosomeService = chromosomeService;
        this.assemblyAssembler = assemblyAssembler;
        this.chromosomeAssembler = chromosomeAssembler;
    }

    public PagedModel<EntityModel<AssemblyEntity>> getAssemblyByAccession(String accession) {
        Optional<AssemblyEntity> entity = assemblyService.getAssemblyByAccession(accession);
        entity.ifPresent(it -> it.setChromosomes(null));
        return generatePagedModelFromPage(convertToPage(entity), assemblyAssembler);
    }

    public PagedModel<EntityModel<AssemblyEntity>> getAssemblyByGenbank(String genbank) {
        Optional<AssemblyEntity> entity = assemblyService.getAssemblyByGenbank(genbank);
        entity.ifPresent(it -> it.setChromosomes(null));
        return generatePagedModelFromPage(convertToPage(entity), assemblyAssembler);
    }

    public PagedModel<EntityModel<AssemblyEntity>> getAssemblyByRefseq(String refseq) {
        Optional<AssemblyEntity> entity = assemblyService.getAssemblyByRefseq(refseq);
        entity.ifPresent(it -> it.setChromosomes(null));
        return generatePagedModelFromPage(convertToPage(entity), assemblyAssembler);

    }

    public PagedModel<EntityModel<AssemblyEntity>> getAssembliesByTaxid(long taxid, Pageable request) {
        Page<AssemblyEntity> page = assemblyService.getAssembliesByTaxid(taxid, request);
        return generatePagedModelFromPage(page, assemblyAssembler);
    }

    public PagedModel<EntityModel<AssemblyEntity>> getAssemblyByChromosomeGenbank(String genbank) {
        Optional<AssemblyEntity> assembly = chromosomeService.getAssemblyByChromosomeGenbank(genbank);
        return generatePagedModelFromPage(assembly, assemblyAssembler);
    }

    public PagedModel<EntityModel<AssemblyEntity>> getAssemblyByChromosomeRefseq(String refseq) {
        Optional<AssemblyEntity> assembly = chromosomeService.getAssemblyByChromosomeRefseq(refseq);
        return generatePagedModelFromPage(assembly, assemblyAssembler);
    }

    public PagedModel<EntityModel<ChromosomeEntity>> getChromosomeByGenbank(String genbank) {
        Optional<ChromosomeEntity> entity = chromosomeService.getChromosomeByGenbank(genbank);
        return generatePagedModelFromPage(convertToPage(entity), chromosomeAssembler);
    }

    public PagedModel<EntityModel<ChromosomeEntity>> getChromosomeByRefseq(String refseq) {
        Optional<ChromosomeEntity> entity = chromosomeService.getChromosomeByRefseq(refseq);
        return generatePagedModelFromPage(convertToPage(entity), chromosomeAssembler);
    }

    public PagedModel<EntityModel<ChromosomeEntity>> getChromosomesByAssemblyGenbank(String genbank, Pageable request) {
        Page<ChromosomeEntity> page = chromosomeService.getChromosomesByAssemblyGenbank(genbank, request);
        return generatePagedModelFromPage(page, chromosomeAssembler);
    }

    public PagedModel<EntityModel<ChromosomeEntity>> getChromosomesByAssemblyRefseq(String refseq, Pageable request) {
        Page<ChromosomeEntity> page = chromosomeService.getChromosomesByAssemblyRefseq(refseq, request);
        return generatePagedModelFromPage(page, chromosomeAssembler);
    }

    public PagedModel<EntityModel<ChromosomeEntity>> getChromosomesByAssemblyAccession(String accession, Pageable request) {
        Page<ChromosomeEntity> page = chromosomeService.getChromosomesByAssemblyAccession(accession, request);
        return generatePagedModelFromPage(page, chromosomeAssembler);
    }

    public PagedModel<EntityModel<ChromosomeEntity>> getChromosomesByChromosomeNameAndAssemblyTaxid(
            String name, long taxid, String nameType, Pageable request) {
        Page<ChromosomeEntity> page;
        if (nameType.equals(ContigAliasController.NAME_UCSC_TYPE)) {
            page = chromosomeService.getChromosomesByUcscNameAndAssemblyTaxid(name, taxid, request);
        } else {
            page = chromosomeService.getChromosomesByNameAndAssemblyTaxid(name, taxid, request);
        }
        return generatePagedModelFromPage(page, chromosomeAssembler);
    }

    public PagedModel<EntityModel<ChromosomeEntity>> getChromosomesByChromosomeNameAndAssemblyAccession(
            String name, String accession, String nameType, Pageable request) {
        Page<ChromosomeEntity> page = new PageImpl<>(Collections.emptyList());
        Optional<AssemblyEntity> assembly = assemblyService.getAssemblyByAccession(accession);
        if (assembly.isPresent()) {
            if (nameType.equals(ContigAliasController.NAME_UCSC_TYPE)) {
                page = chromosomeService.getChromosomesByUcscNameAndAssembly(name, assembly.get(), request);
            } else {
                page = chromosomeService.getChromosomesByNameAndAssembly(name, assembly.get(), request);
            }
        }
        return generatePagedModelFromPage(page, chromosomeAssembler);
    }

    public PagedModel<EntityModel<ChromosomeEntity>> getChromosomesByName(
            String name, String nameType, Pageable request) {
        Page<ChromosomeEntity> page;
        if (nameType.equals(ContigAliasController.NAME_UCSC_TYPE)) {
            page = chromosomeService.getChromosomesByUcscName(name, request);
        } else {
            page = chromosomeService.getChromosomesByName(name, request);
        }
        return generatePagedModelFromPage(page, chromosomeAssembler);
    }
}
