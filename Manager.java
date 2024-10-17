import java.beans.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class Manager {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    private static ArrayList<Student> students = new ArrayList<>();
    private static ArrayList<Teacher> teachers = new ArrayList<>();
    private ArrayList<Class> classes = new ArrayList<>();
    private static ArrayList<Admin> admins = new ArrayList<Admin>();


    public void addClasses(Student student) {
        String output = "\nCurrent classes avaliable: ";
        for (int j = 0; j < classes.size(); j++) {
            String name = classes.get(j).getName();
            if (!(output.contains(name))) {
                output += "\n" + classes.get(j).getName();
            }
        }
        System.out.println(output);
        System.out.println(
                "\nYou can enter 1-4 of the classes listed above. Once you are done adding classes, enter -1 to continue to the student dashboard");
        for (int i = 0; i < 4; i++) {
            if (student.getClasses().size() < 4) {
                Scanner s = new Scanner(System.in);
                System.out.println(YELLOW + "\nAdd class: " + RESET);
                String input = s.nextLine().toLowerCase();
                boolean classFound = false;
                if (input.equals("-1")) {
                    classFound = true;
                }
                for (Class classInList : classes) {
                    if (classInList.getName().equalsIgnoreCase(input)) {
                        classFound = true;
                    }
                }
                while (!classFound) {
                    System.out.println(RED + "\nInvalid class name. " + RESET);
                    s = new Scanner(System.in);
                    System.out.println("\nType out the name of one of the classes listed above: ");
                    input = s.nextLine();
                    for (Class classInList : classes) {
                        if (classInList.getName().equalsIgnoreCase(input)) {
                            classFound = true;
                            break;
                        }
                    }
                }
                if (input.equals("-1")) {
                    break;
                } else {
                    Class newClass = getClass(input);
                    newClass.addStudent(student);
                    student.addClass(newClass);
                    Teacher teacher = newClass.getTeacher();
                    student.addTeacher(teacher);
                    teacher.addStudent(student);
                }
            }
        }
    }

    public void addNewStudent(Student student) {
        students.add(student);
        Scanner s = new Scanner(System.in);
        System.out.println(YELLOW + "\nName: " + RESET);
        student.setName(s.nextLine().toLowerCase());
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nGrade: " + RESET);
        int grade = -1;
        if (s.hasNextInt()) {
            grade = s.nextInt();
        } else {
            while (!(s.hasNextInt())) {
                System.out.println(RED + "\nEnter a number from 1-12 " + RESET);
                s = new Scanner(System.in);
                System.out.println(YELLOW + "\nGrade: " + RESET);
            }
        }
        student.setGradeLevel(grade);
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nPrefect: " + RESET);
        student.setPrefect(s.nextLine());
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nOSIS: " + RESET);
        int osis = -1;

        if (s.hasNextInt()) {
            osis = s.nextInt();
        } else {
            while (!(s.hasNextInt())) {
                System.out.println(RED + "\nEnter your integer OSIS number" + RESET);
                s = new Scanner(System.in);
                System.out.println(YELLOW + "\nOSIS: " + RESET);
            }
        }

        student.setOsis(osis);
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nEmail: " + RESET);
        student.setEmail(s.nextLine());
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nMajor: " + RESET);
        student.setMajor(s.nextLine());
        addClasses(student);
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        Scanner s = new Scanner(System.in);
        System.out.println(YELLOW + "\nName: " + RESET);
        teacher.setName(s.nextLine());
        teacher.setRoom((teachers.get(teachers.size() - 1).getRoom()) + 1);
        System.out.println(YELLOW + "\nEmail: " + RESET);
        teacher.setEmail(s.nextLine());
        teacher.setSupervisor(admins.get(new Random().nextInt(admins.size() - 1)));
        s = new Scanner(System.in);
        System.out.print(YELLOW + "\nClass: " + RESET);
        String addClass = s.nextLine().toLowerCase();
        teacher.setSubject(addClass);
        int i = 0;
        int counter = 0;
        while (i < classes.size()) {
            if (classes.get(i).getName().equalsIgnoreCase(addClass)) {
                counter++;
            }
            i++;
        }
        if (counter > 1) {
            for (Class class1 : classes) {
                if (class1.getName().equals(addClass)) {
                    teacher.addClass(class1);
                    class1.setTeacher(teacher);
                }
            }
        } else {
            Class newClass = new Class(addClass);
            newClass.setTeacher(teacher);
            teacher.addClass(newClass);
            classes.add(newClass);
        }
    }
    
    public Admin addAdmin(String username, String password, Manager manager) {
        Admin admin = new Admin(username, password, manager);
        admins.add(admin);
        Scanner s = new Scanner(System.in);
        System.out.println("\nName: " + RESET);
        admin.setName(s.nextLine());
        System.out.println("\nEmail: ");
        admin.setEmail(s.nextLine());
        for (Teacher teacher : teachers) {
            if (teacher.getSupervisor().equals(null)) {
                admin.addTeacher(teacher);
                teacher.setSupervisor(admin);
            }
        }
        for (int i = 0; i < 2; i++) {
            admin.addClass(classes.get(new Random().nextInt(classes.size())));
        }
        admin.getManager(this);
        return admin;
    }

    // a return statement inside a loop
    public Class getClass(String name) {
        for (Class classInList : classes) {
            if (classInList.getName().equalsIgnoreCase(name)) {
                return classInList;
            }
        }
        return null;
    }

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public static ArrayList<Admin> getAdmins() {
        return admins;
    }

    public void setTeachers(ArrayList teachers) {
        this.teachers = teachers;
    }

    public void setStudents(ArrayList students) {
        this.students = students;
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public void setAdmins(ArrayList admins) {
        this.admins = admins;
    }

    public void setClasses(ArrayList classes) {
        this.classes = classes;
    }
}
