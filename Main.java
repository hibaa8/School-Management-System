import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.io.Console;

/**
 * This is a student management system that can be used to manage grades and
 * assignments while providing
 * a souce of communication between students, teachers, and administrators.
 * Teachers can use this platform to
 * make assignments, grade them, make report cards, write emails to students and
 * admins, and track student progress.
 * Students can view their grades, assignments, class information, and report
 * cards and use it to write emails to their teachers.
 * Admins can use this platform to add or remove teachers and students and write
 * emails to them.
 * 
 * @author Hiba Altaf
 * 
 */

public class Main {
    public static Manager manager = new Manager();
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static void main(String[] args) {
        Admin knottingham = new Admin("knottingham", "123", "knottingham", "k@gmail.com");
        Admin hiba = new Admin("hiba", "123", "hiba", "h@gmail.com");
        knottingham.setName("knottingham");
        hiba.setName("Hiba");
        knottingham.getManager(manager);
        hiba.getManager(manager);
        Class math = new Class("math");
        Class computerScience = new Class("computer science");
        Class computerScience2 = new Class("computer science");
        Class computerScience3 = new Class("computer science");
        Class science = new Class("science");
        Class science2 = new Class("science");
        Class english = new Class("english");
        Teacher amy = new Teacher("amy", "amy", "123", "math", 1, "amy@gmail.com", hiba);
        amy.addClass(math);
        math.setTeacher(amy);

        Teacher stephanie = new Teacher("stephanie", "stephanie", "123", "computer science", 2, "stephanie@gmail.com",
                hiba);
        stephanie.addClass(computerScience);
        stephanie.addClass(computerScience2);
        stephanie.addClass(computerScience3);
        computerScience.setTeacher(stephanie);
        computerScience2.setTeacher(stephanie);
        computerScience3.setTeacher(stephanie);

        Teacher robert = new Teacher("robert", "robert", "123", "science", 3, "robert@gmail.com", knottingham);

        robert.addClass(science);
        robert.addClass(science2);
        science.setTeacher(robert);
        science2.setTeacher(robert);

        Teacher ali = new Teacher("ali", "ali", "123", "english", 4, "ali@gmail.com", knottingham);

        ali.addClass(english);
        english.setTeacher(ali);
        manager.setClasses(new ArrayList<>(
                Arrays.asList(math, science, science2, computerScience, computerScience2, computerScience3, english)));

        System.out.println();
        manager.setTeachers(new ArrayList<>(Arrays.asList(amy, stephanie, robert, ali)));
        manager.setAdmins(new ArrayList<>(Arrays.asList(hiba, knottingham)));
        hiba.addTeacher(amy);
        hiba.addTeacher(stephanie);
        knottingham.addTeacher(robert);
        knottingham.addTeacher(ali);

        Student elizabeth = new Student("elizabeth", "123", "elizabeth", "j43", 189, "elizabeth@gmail.com", 9,
                "software");

        elizabeth.addClass(computerScience);
        elizabeth.addClass(math);
        computerScience.addStudent(elizabeth);
        math.addStudent(elizabeth);
        elizabeth.addTeacher(stephanie);
        stephanie.addStudent(elizabeth);
        elizabeth.addTeacher(amy);
        amy.addStudent(elizabeth);

        Student tommy = new Student("tommy", "123", "tommy", "k94", 743, "tommy@gmail.com", 10, "mechatronics");

        tommy.addClass(science);
        tommy.addClass(english);
        science.addStudent(tommy);
        english.addStudent(tommy);
        tommy.addTeacher(robert);
        robert.addStudent(tommy);
        tommy.addTeacher(ali);
        ali.addStudent(tommy);

        manager.setStudents(new ArrayList<>(Arrays.asList(elizabeth, tommy)));
        start();
    }

    public static void start() {
        System.out.println(YELLOW + "Student Management System\n" + RESET);
        System.out.println("1-student\n2-teacher\n3-admin");
        Scanner s = new Scanner(System.in);
        int user = 0;
        boolean userFound = false;
        if (s.hasNextInt()) {
            int n = s.nextInt();
            if (Arrays.asList(1, 2, 3).contains(n)) {
                user = n;
                userFound = true;
            }
        }
        while (!userFound) {
            System.out.println(RED + "\nIncorrect input. Please try again" + RESET);
            s = new Scanner(System.in);
            System.out.println("\nPut 1, 2, or 3: ");
            user = 0;
            if (s.hasNextInt()) {
                int n = s.nextInt();
                if (Arrays.asList(1, 2, 3).contains(n)) {
                    user = n;
                    userFound = true;
                    break;
                }
            }
        }

        System.out.println("\n1-create account\n2-login");
        boolean inputFound = false;
        int input = 0;
        if (s.hasNextInt()) {
            int n = s.nextInt();
            if (Arrays.asList(1, 2).contains(n)) {
                input = n;
                inputFound = true;
            }
        }

        while (!inputFound) {
            System.out.println(RED + "\nInvalid input. Please try again" + RESET);
            s = new Scanner(System.in);
            System.out.println("Enter 1 or 2: ");
            if (s.hasNextInt()) {
                int n = s.nextInt();
                if (Arrays.asList(1, 2).contains(n)) {
                    input = n;
                    inputFound = true;
                    break;
                }
            }
        }

        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nUsername: " + RESET);
        String username = s.nextLine();
        String password = new String(System.console().readPassword(YELLOW + "\nPassword: \n" + RESET));
        if (input == 1) {
            createAccount(username, password, user);
        } else if (input == 2) {
            login(username, password, user);
        }
    }

    public static void createAccount(String username, String password, int user) {
        if (user == 1) {
            System.console().printf("Password entered was: %s%n", password);
            Student student = new Student(username, password);
            manager.addNewStudent(student);
            student.display();
        } else if (user == 2) {
            Teacher teacher = new Teacher(username, password);
            manager.addTeacher(teacher);
            teacher.display();
        } else if (user == 3) {
            Admin admin = manager.addAdmin(username, password, manager);
            admin.display();
        }
    }

    public static void login(String username, String password, int user) {
        if (user == 1) {
            boolean studentFound = false;
            int counter = 3;
            while (!studentFound && counter > 0) {
                for (Student student : manager.getStudents()) {
                    if (student.getUsername().equalsIgnoreCase(username)
                            && student.getPassword().equalsIgnoreCase(password)) {
                        studentFound = true;
                        System.out.println(YELLOW + "\nLogin successful. Welcome, " + student.getName() + RESET);
                        System.out.println(student);
                        student.display();
                        break;
                    }
                }

                if (!studentFound && counter > 0) {
                    counter--;
                    System.out.println(RED + "\nIncorrect username or password. Please try again. " + counter
                            + " tries remaining." + RESET);
                    System.console().printf("Password entered was: %s%n", password);
                    if (counter == 0) {
                        System.out.println(YELLOW + "\nGet out of here intruder! No trespassing!" + RESET);
                        break;
                    }
                    Scanner s = new Scanner(System.in);
                    System.out.println(YELLOW + "\nUsername: " + RESET);
                    username = s.nextLine();
                    password = new String(System.console().readPassword(YELLOW + "\nPassword: \n" + RESET));
                }
            }

        } else if (user == 2) {
            boolean teacherFound = false;
            int counter = 3;
            while (!teacherFound && counter > 0) {
                for (Teacher teacher : manager.getTeachers()) {
                    if (teacher.getUsername().equalsIgnoreCase(username)
                            && teacher.getPassword().equalsIgnoreCase(password)) {
                        teacherFound = true;
                        System.out.println(YELLOW + "\nLogin successful. Welcome, " + teacher.getName() + RESET);
                        System.out.println(teacher);
                        teacher.display();
                        break;
                    }
                }

                if (!teacherFound && counter > 0) {
                    counter--;
                    System.out.println(RED + "\nIncorrect username or password. Please try again. " + counter
                            + " tries remaining." + RESET);
                    System.console().printf("Password entered was: %s%n", password);
                    if (counter == 0) {
                        System.out.println(YELLOW + "\nGet out of here intruder! No trespassing!" + RESET);
                        break;
                    }
                    Scanner s = new Scanner(System.in);
                    System.out.println(YELLOW + "\nUsername: " + RESET);
                    username = s.nextLine();
                    password = new String(System.console().readPassword(YELLOW + "\nPassword: \n" + RESET));
                }
            }
        } else if (user == 3) {
            boolean adminFound = false;
            int counter = 3;
            while (!adminFound && counter > 0) {
                for (Admin admin : manager.getAdmins()) {
                    if (admin.getUsername().equalsIgnoreCase(username)
                            && admin.getPassword().equalsIgnoreCase(password)) {
                        adminFound = true;
                        System.out.println(YELLOW + "\nLogin successful. Welcome, " + admin.getName() + RESET);
                        System.out.println(admin);
                        admin.display();
                        break;
                    }
                }

                if (!adminFound && counter > 0) {
                    counter--;
                    System.out.println(RED + "\nIncorrect username or password. Please try again. " + counter
                            + " tries remaining." + RESET);
                    System.console().printf("Password entered was: %s%n", password);
                    if (counter == 0) {
                        System.out.println(YELLOW + "\nGet out of here intruder! No trespassing!" + RESET);
                        break;
                    }
                    Scanner s = new Scanner(System.in);
                    System.out.println(YELLOW + "\nUsername: " + RESET);
                    username = s.nextLine();
                    password = new String(System.console().readPassword(YELLOW + "\nPassword: \n" + RESET));
                }
            }
        } else {
            while (!(Arrays.asList(1, 2, 3).contains(user))) {
                System.out.println(RED + "\nInvalid input. Enter 1, 2, or 3: \n" + RESET);
                Scanner s = new Scanner(System.in);
                user = s.nextInt();
            }
        }
    }
}
