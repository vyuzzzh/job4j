package ru.job4j.filter;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class School {
    List<Student> collect(List<Student> students, Predicate<Student> predict) {
        return students.stream().filter(predict).collect(Collectors.toList());
    }

    Map<String, Student> change(List<Student> students) {
        return students.stream().collect(
                Collectors.toMap(
                        e -> e.getSurname() + e.getScore(),
                        e -> e,
                        ((student, student2) -> student)
                )
        );
    }
}