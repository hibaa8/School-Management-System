import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Class {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    private String name;
    private ArrayList<Student> students = new ArrayList<>();
    private Teacher teacher;
    private ArrayList<String> assignments = new ArrayList<>();
    // has an array of primitive types
    private double[] grades = new double[students.size()];

    public Class(String name) {
        this.name = name;
    }

    public void addAssignment(String newAssignment) {
        Scanner s = new Scanner(System.in);
        System.out.println("Description: ");
        String description = s.nextLine();
        s = new Scanner(System.in);
        System.out.println("Weight: ");
        double weight = s.nextDouble();
        for (Student student : students) {
            s = new Scanner(System.in);
            System.out.println(student.getName() + ": ");
            double grade = weight * s.nextDouble();
            for (HashMap class1 : student.getPossibleClasses()) {
                if (name.equals(class1.keySet().iterator().next())) {
                    class1.put(description, grade);
                }
            }
            grades[students.indexOf(student)] = grade;
        }
    }

    public void removeStudent(Student student) {
        int i = 0;
        while (i < students.size()) {
            if (students.get(i).getName().equals(student.getName())) {
                students.remove(students.get(i));
                break;
            }
            i++;
        }
    }

    public String getName() {
        return name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void setAssignments(ArrayList<String> assignments) {
        this.assignments = assignments;
    }

    public void removeTeacher() {
        teacher = null;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<String> getAssignments() {
        return assignments;
    }
}
