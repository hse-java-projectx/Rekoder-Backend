package ru.hse.rekoder.services;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ru.hse.rekoder.exceptions.*;
import ru.hse.rekoder.model.Folder;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.repositories.FolderRepository;
import ru.hse.rekoder.repositories.ProblemRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private final ProblemRepository problemRepository;
    private final TaskExecutor taskExecutor;

    public FolderServiceImpl(FolderRepository folderRepository,
                             ProblemRepository problemRepository,
                             TaskExecutor taskExecutor) {
        this.folderRepository = folderRepository;
        this.problemRepository = problemRepository;
        this.taskExecutor = taskExecutor;
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
        folder.setId(null);
        folder = folderRepository.save(folder);

        Optional<Integer> optionalFolderDepth = getLengthOfPathToRootFrom(folder);
        if (getLengthOfPathToRootFrom(folder).filter(depth -> depth <= Folder.MAX_DEPTH).isEmpty()) {
            deleteFolder(folder.getId());
            if (optionalFolderDepth.isEmpty()) {
                throw new FolderNotFoundException(parentFolderId);
            } else {
                throw new FolderException("The maximum folder depth is 25, but here " + optionalFolderDepth.get());
            }
        }

        return folder;
    }

    @Override
    public Folder createRootFolder(Folder folder) {
        folder.setId(null);
        folder.setParentFolderId(null);
        return folderRepository.save(folder);
    }

    @Override
    public Folder updateFolder(Folder folder) {
        if (Objects.isNull(folder.getId())) {
            throw new FolderException("The folder must have an id");
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
    public List<Folder> getPathToRootFrom(int folderId) {
        List<Folder> pathToRoot = new ArrayList<>();
        Folder currentFolder = getFolder(folderId);
        pathToRoot.add(currentFolder);
        while (Objects.nonNull(currentFolder.getParentFolderId())) {
            final int parentFolderId = currentFolder.getParentFolderId();
            currentFolder = getFolder(parentFolderId);
            pathToRoot.add(currentFolder);
        }
        Collections.reverse(pathToRoot);
        return pathToRoot;
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
        Folder folder = getFolder(folderId);
        return problemRepository.findAllById(folder.getProblemIds());
    }

    @Override
    public boolean addProblemToFolder(int folderId, int problemId) {
        if (!problemRepository.existsById(problemId)) {
            throw new ProblemNotFoundException(problemId);
        }
        Folder oldFolder = folderRepository.addProblemToFolderById(folderId, problemId)
                .orElseThrow(() -> new FolderNotFoundException(folderId));
        boolean result = !oldFolder.getProblemIds().contains(problemId);

        taskExecutor.execute(() -> {
            List<Integer> existingProblemsFromFolder = problemRepository.findAllById(oldFolder.getProblemIds())
                    .stream()
                    .map(Problem::getId)
                    .collect(Collectors.toList());
            existingProblemsFromFolder.forEach(oldFolder.getProblemIds()::remove);
            Folder latestVersionOfFolder = oldFolder;
            for (int idOfNonExistingProblem : oldFolder.getProblemIds()) {
                if (latestVersionOfFolder.getProblemIds().contains(idOfNonExistingProblem)) {
                    Optional<Folder> optLatestVersionOfFolder =
                            folderRepository.deleteProblemFromFolderById(folderId, idOfNonExistingProblem);
                    if (optLatestVersionOfFolder.isPresent()) {
                        latestVersionOfFolder = optLatestVersionOfFolder.get();
                    } else {
                        return;
                    }
                }
            }
        });

        return result;
    }

    @Override
    public boolean deleteProblemFromFolder(int folderId, int problemId) {
        Folder oldFolder = folderRepository.deleteProblemFromFolderById(folderId, problemId)
                .orElseThrow(() -> new FolderNotFoundException(folderId));
        return oldFolder.getProblemIds().contains(problemId);
    }

    @Override
    public void deleteFolder(int folderId) {
        if (!folderRepository.existsById(folderId)) {
            throw new FolderNotFoundException(folderId);
        }
        List<Integer> folderIdsToDelete = List.of(folderId);
        while (!folderIdsToDelete.isEmpty()) {
            folderRepository.deleteByIdIn(folderIdsToDelete);
            folderIdsToDelete = folderRepository.findAllByParentFolderIdIn(folderIdsToDelete)
                    .stream()
                    .map(Folder::getId)
                    .collect(Collectors.toList());
        }
    }

    private Optional<Integer> getLengthOfPathToRootFrom(Folder folder) {
        int pathLength = 1;
        Folder currentFolder = folder;
        while (Objects.nonNull(currentFolder.getParentFolderId())) {
            pathLength += 1;
            currentFolder = folderRepository.findById(currentFolder.getParentFolderId())
                    .orElse(null);
            if (Objects.isNull(currentFolder)) {
                return Optional.empty();
            }
        }
        return Optional.of(pathLength);
    }
}
