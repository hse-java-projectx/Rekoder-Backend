package ru.hse.rekoder.repositories.mongodb.seqGenerators;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.hse.rekoder.model.DocumentWithIncreasingIdSequence;

import java.util.Objects;

@Component
public class DocumentWithIncreasingIdSequenceListener extends AbstractMongoEventListener<DocumentWithIncreasingIdSequence> {
    private final DatabaseIntSequenceService sequenceService;

    public DocumentWithIncreasingIdSequenceListener(DatabaseIntSequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<DocumentWithIncreasingIdSequence> event) {
        DocumentWithIncreasingIdSequence document = event.getSource();
        if (Objects.isNull(document.getId())) {
            document.setId(sequenceService.generateSequence(event.getCollectionName() + "_sequence"));
        }
    }
}
