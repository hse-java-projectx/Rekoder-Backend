package ru.hse.rekoder.services;

import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.*;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.repositories.FolderRepository;
import ru.hse.rekoder.repositories.ProblemRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private final ProblemRepository problemRepository;

    public FolderServiceImpl(FolderRepository folderRepository,
                             ProblemRepository problemRepository) {
        this.folderRepository = folderRepository;
        this.problemRepository = problemRepository;
    }

    @Override
    public Folder getFolder(int folderId) {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException(folderId));
    }

    @Override
    public Folder createNewFolder(int parentFolderId, Folder folder) {
        Folder parentFolder = folderRepository.findById(parentFolderId)
                .orElseThrow(() -> new FolderNotFoundException(parentFolderId));
        if (folderRepository.existsByParentFolderIdAndName(parentFolderId, folder.getName())) {
            throw new FolderConflictException(parentFolderId, folder.getName());
        }
        folder.setParentFolderId(parentFolderId);
        folder.setOwner(parentFolder.getOwner());
        folder = folderRepository.save(folder);
        return folder;
    }

    @Override
    public Folder updateFolder(Folder folder) {
        if (Objects.isNull(folder.getId())) {
            throw new FolderException("Folder must have an id");
        }
        if (Objects.isNull(folder.getParentFolderId())) {
            throw new FolderException("You cannot change the root folder");
        }
        if (folderRepository.existsByParentFolderIdAndNameAndIdIsNot(folder.getParentFolderId(),
                                                                     folder.getName(),
                                                                     folder.getId())) {
            throw new FolderConflictException(folder.getParentFolderId(), folder.getName());
        }
        return folderRepository.update(folder, folder.getId())
                .orElseThrow(() -> new FolderNotFoundException(folder.getId()));
    }

    @Override
    public List<Folder> getSubFolders(int folderId) {
        if (!folderRepository.existsById(folderId)) {
            throw new FolderNotFoundException(folderId);
        }
        return folderRepository.findAllByParentFolderId(folderId);
    }

    @Override
    public List<Problem> getProblemsFromFolder(int folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException(folderId));
        return problemRepository.findAllById(folder.getProblemIds());
    }

    @Override
    public void addProblemToFolder(int folderId, int problemId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException(folderId));
        if (!problemRepository.existsById(problemId)) {
            throw new ProblemNotFoundException(problemId);
        }
        List<Problem> problems = problemRepository.findAllById(folder.getProblemIds());
        Set<Integer> problemIds = problems.stream()
                .map(Problem::getId)
                .collect(Collectors.toSet());
        if (!problemIds.add(problemId)) {
            throw new ProblemException("The problem " + problemId + "is already in the folder " + folderId);
        }
        if (problemIds.size() != problems.size()) {
            folder.setProblemIds(problemIds);
            folderRepository.save(folder);
        }
    }

    @Override
    public void deleteProblemFromFolder(int folderId, int problemId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException(folderId));
        List<Problem> problems = problemRepository.findAllById(folder.getProblemIds());
        Set<Integer> problemIds = problems.stream()
                .map(Problem::getId)
                .collect(Collectors.toSet());
        if (!problemIds.remove(problemId)) {
            throw new ProblemException("The problem " + problemId + " does not exist or the folder " + folderId+ " does not contains it");
        }
        if (problems.size() != problemIds.size()) {
            folder.setProblemIds(problemIds);
            folderRepository.save(folder);
        }
    }

    @Override
    public void deleteFolder(int folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException(folderId));
        if (Objects.isNull(folder.getParentFolderId())) {
            throw new FolderException("The root folder cannot be deleted");
        }
        List<Integer> s = folderRepository.getSubTree(folderId);
        folderRepository.deleteByIdIn(s);
        folderRepository.deleteById(folderId);
    }
}
