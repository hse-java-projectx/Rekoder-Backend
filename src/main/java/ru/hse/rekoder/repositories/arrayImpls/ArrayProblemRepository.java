package ru.hse.rekoder.repositories.arrayImpls;

import org.springframework.stereotype.Component;
import ru.hse.rekoder.model.Problem;
import ru.hse.rekoder.repositories.ProblemRepository;

import java.util.ArrayList;
import java.util.Optional;

public class ArrayProblemRepository implements ProblemRepository {
    private final ArrayList<Problem> problems = new ArrayList<>();


    @Override
    public Optional<Problem> findById(Integer id) {
        if (id < 0 || id >= problems.size()) {
            return Optional.empty();
        }
        return Optional.of(problems.get(id));
    }

    @Override
    public Problem save(Problem problem) {
        /*if (problem.getId() == null || problem.getId() < 0 || problem.getId() >= problems.size()) {
            problem.setId(problems.size());
            problems.add(problem);
            return problem;
        }
        problems.set(problem.getId(), problem);
        return problem;*/
        return null;
    }

    @Override
    public long count() {
        return problems.size();
    }
}
