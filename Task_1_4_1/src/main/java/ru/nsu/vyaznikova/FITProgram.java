package ru.nsu.vyaznikova;

import java.util.Arrays;

public class FITProgram {

    public static SemesterRecord firstSemester() {
        SemesterRecord semester = new SemesterRecord();
        semester.addGrade("Введение в алгебру и анализ", 0, GradeType.EXAM);
        semester.addGrade("Введение в дискретную математику и математическую логику", 0, GradeType.EXAM);
        semester.addGrade("История России", 0, GradeType.EXAM);
        semester.addGrade("Декларативное программирование", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Императивное программирование", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("ОРГ", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Цифровые платформы", 0, GradeType.CREDIT);
        semester.addGrade("Иностранный язык", 0, GradeType.CREDIT);
        semester.addGrade("Физическая культура и спорт", 0, GradeType.CREDIT);
        return semester;
    }

    public static SemesterRecord secondSemester() {
        SemesterRecord semester = new SemesterRecord();
        semester.addGrade("Введение в алгебру и анализ", 0, GradeType.EXAM);
        semester.addGrade("Введение в дискретную математику и математическую логику", 0, GradeType.EXAM);
        semester.addGrade("Императивное программирование", 0, GradeType.EXAM);
        semester.addGrade("Цифровые платформы", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Декларативное программирование", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Иностранный язык", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("История России", 0, GradeType.CREDIT);
        semester.addGrade("Физическая культура и спорт", 0, GradeType.CREDIT);
        return semester;
    }

    public static SemesterRecord thirdSemester() {
        SemesterRecord semester = new SemesterRecord();
        semester.addGrade("Дифференциальные уравнения и теория функций комплексного переменного", 0, GradeType.EXAM);
        semester.addGrade("Введение в искусственный интеллект", 0, GradeType.EXAM);
        semester.addGrade("Операционные системы", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Объектно-ориентированное программирование", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Иностранный язык", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Теория вероятностей и математическая статистика", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("ПАК", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Модели вычислений", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Физическая культура и спорт", 0, GradeType.CREDIT);
        return semester;
    }

    public static SemesterRecord fourthSemester() {
        SemesterRecord semester = new SemesterRecord();
        semester.addGrade("Операционные системы", 0, GradeType.EXAM);
        semester.addGrade("Объектно-ориентированное программирование", 0, GradeType.EXAM);
        semester.addGrade("Теория вероятностей и математическая статистика", 0, GradeType.EXAM);
        semester.addGrade("Модели вычислений", 0, GradeType.EXAM);
        semester.addGrade("Теория параллелизма", 0, GradeType.EXAM);
        semester.addGrade("Деловой английский язык", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("ПАК", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Введение в компьютерные сети", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Введение в аналоговую электронику и технику измерений", 0, GradeType.DIFFERENTIATED_CREDIT);
        semester.addGrade("Встроенные цифровые системы управления", 0, GradeType.DIFFERENTIATED_CREDIT);
        return semester;
    }
}
