package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.FolderNotFoundException;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.repositories.FolderRepository;
import ru.hse.rekoder.repositories.ProblemRepository;
import ru.hse.rekoder.repositories.mongodb.seqGenerators.DatabaseIntSequenceService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private final ProblemRepository problemRepository;
    private final DatabaseIntSequenceService sequenceService;

    public FolderServiceImpl(FolderRepository folderRepository,
                             ProblemRepository problemRepository,
                             DatabaseIntSequenceService sequenceService) {
        this.folderRepository = folderRepository;
        this.problemRepository = problemRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    public Folder getFolder(int folderId) {
        return folderRepository.findById(folderId).orElseThrow();
    }

    @Override
    public Folder createNewFolder(int parentFolderId, Folder folder) {
        Folder parentFolder = folderRepository.findById(parentFolderId)
                .orElseThrow(() -> new FolderNotFoundException("Folder not found"));
        if (folderRepository.existsByParentFolderIdAndName(parentFolderId, folder.getName())) {
            throw new FolderNotFoundException("TODO you create two folders with equal names");
        }
        folder.setParentFolderId(parentFolderId);
        folder.setOwnerId(parentFolder.getOwnerId());
        folder.setId(sequenceService.generateSequence(Folder.SEQUENCE_NAME));
        folder = folderRepository.save(folder);
        return folder;
    }

    @Override
    public List<Folder> getSubFolders(int folderId) {
        return folderRepository.findAllByParentFolderId(folderId);
    }

    @Override
    public List<Problem> getProblemsFromFolder(int folderId) {
        //TODO
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("Folder not found"));
        List<Problem> problems = problemRepository.findAllById(folder.getProblemIds());
        Set<Integer> problemIds = problems.stream()
                .map(Problem::getId)
                .collect(Collectors.toSet());
        if (problemIds.size() != folder.getProblemIds().size()) {
            folderRepository.save(folder);
        }
        return problems;
    }
}
