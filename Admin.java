import java.io.Console;
import java.security.DrbgParameters.Reseed;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Admin {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";

    private String name;
    private String email;
    private String username;
    private String password;
    private ArrayList<Class> classes = new ArrayList<>();
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private ArrayList<String[]> inbox = new ArrayList<>();
    private Manager manager;

    public Admin(String username, String password, Manager manager) {
        this.username = username;
        this.password = password;
        this.manager = manager;
    }

    public Admin(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void display() {
        // System.out.println("\nAdmin Dashboard");
        // System.out.print(toString());
        System.out.println("\nClasses");
        for (Class class1 : manager.getClasses()) {
            String output = class1.getName() + " -- Teacher: ";
            if (class1.getTeacher() != null) {
                output += class1.getTeacher().getName();
            }
            output += " -- Students: " + class1.getStudents().size();
            System.out.println(CYAN + output + RESET);

        }
        Scanner s = new Scanner(System.in);
        System.out.println("\n1-add teacher\n2-remove teacher\n3-add Student\n4-remove Student\n5-send email\n" +
                "6-check inbox\n7-log out\n\n" + YELLOW + "Choose an option: " + RESET);

        String option = s.nextLine();
        while (!(Arrays.asList("1", "2", "3", "4", "5", "6", "7").contains(option))) {
            System.out.println(RED + "\nInvalid input. Try again");
            s = new Scanner(System.in);
            System.out.println(RED + "\nEnter a number from 1-7: " + RESET);
            option = s.nextLine();
        }
        int optionInt = Integer.valueOf(option);

        if (optionInt == 1) {
            addTeacher();
        } else if (optionInt == 2) {
            removeTeacher();
        } else if (optionInt == 3) {
            addStudent();
        } else if (optionInt == 4) {
            removeStudent();
        } else if (optionInt == 5) {
            sendEmail();
        } else if (optionInt == 6) {
            checkInbox();
        } else if (optionInt == 7) {
            logout();
        }
    }

    public String displayTeachers() {
        String output = "\nCurrent teachers: \n";
        ArrayList<Teacher> teachers = manager.getTeachers();
        for (int i = 0; i < teachers.size(); i++) {
            output += GREEN + i + " -- Name: " + teachers.get(i).getName() + " -- Subject: "
                    + teachers.get(i).getSubject()
                    + " -- Classes: " +
                    teachers.get(i).getClasses().size() + " -- Last Active: " + teachers.get(i).getLastActive() + "\n"
                    + RESET;
        }
        return output;
    }

    public void addMessage(String[] message) {
        inbox.add(message);
    }

    public void addTeacher() {
        System.out.println(displayTeachers());
        System.out.println("\nCreate teacher account");
        Scanner s = new Scanner(System.in);
        System.out.println(YELLOW + "\nUsername: " + RESET);
        String username = s.nextLine();
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nPassword: " + RESET);
        String password = s.nextLine();
        Teacher teacher = new Teacher(username, password);
        manager.addTeacher(teacher);
        System.out.println(RED + "\nTeacher added" + RESET);
        System.out.println(displayStudents());
        display();
    }

    // includes an array of object references
    public void removeTeacher() {
        System.out.println(displayTeachers());
        Teacher[] teachers = new Teacher[manager.getTeachers().size()];
        teachers = manager.getTeachers().toArray(teachers);
        Scanner s = new Scanner(System.in);
        System.out.println(YELLOW + "\nWhich teacher would you like to remove: " + RESET);
        Teacher removeTeacher = teachers[s.nextInt()];
        for (Class class1 : removeTeacher.getClasses()) {
            class1.removeTeacher();
        }
        manager.getTeachers().remove(removeTeacher);
        System.out.println(RED + "\nTeacher removed" + RESET);
        System.out.println(displayTeachers());
        display();
    }

    public String displayStudents() {
        String output = "\nCurrent students: \n";
        ArrayList<Student> students = manager.getStudents();
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            output += GREEN + i + " -- Name: " + students.get(i).getName() + " -- Major: " + student.getMajor()
                    + " -- Classes: " + RESET;
            for (Class class1 : classes) {
                output += class1.getName() + ", ";
            }
            output += "\n";
            // output += "On track: " + student.progressTracker() + "\n";
        }
        return output;
    }

    public void addStudent() {
        System.out.println(displayStudents());
        System.out.println("Create student account");
        Scanner s = new Scanner(System.in);
        System.out.println(YELLOW + "\nUsername: " + RESET);
        String username = s.nextLine();
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nPassword: " + RESET);
        String password = s.nextLine();
        Student student = new Student(username, password);
        manager.addNewStudent(student);
        System.out.println(displayStudents());
        System.out.println(RED + "\nStudent added" + RESET);
        display();
    }

    // alters static variable in a different class
    public void removeStudent() {
        System.out.println(displayStudents());
        ArrayList<Student> students = manager.getStudents();
        Scanner s = new Scanner(System.in);
        if (manager.getStudents().size() > 0) {
            System.out.println(YELLOW + "\nWhich student would you like to remove: " + RESET);
            Student student = students.get(s.nextInt());
            manager.getStudents().remove(student);
            for (Class class1 : student.getClasses()) {
                class1.removeStudent(student);
            }
            System.out.println(RED + "\nStudent removed" + RESET);
            System.out.println(displayStudents());
        } else {
            System.out.println(RED + "\nThere are no students to remove" + RESET);
        }
        display();
    }

    public void sendEmail() {
        System.out.println("\nStudent emails: ");
        for (Student student : manager.getStudents()) {
            System.out.println(GREEN + student.getEmail() + RESET);
        }
        System.out.println("\nTeacher emails: ");
        for (Teacher teacher : manager.getTeachers()) {
            System.out.println(GREEN + teacher.getEmail() + RESET);
        }
        String emailMessage = "From: " + this.email;
        Scanner s = new Scanner(System.in);
        System.out.println("\n1-email ALL students\n2-email ALL teachers\n3-email one student\n4-email one teacher\n");
        System.out.println(YELLOW + "\nChoose a receiver: " + RESET);
        int reciever = s.nextInt();
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nMessage: " + RESET);
        String message = s.nextLine();
        if (reciever == 1) {
            emailMessage += " -- To: ALL students -- Message: " + message;
            String[] finalEmail = { email, emailMessage };
            for (Student student : manager.getStudents()) {
                student.addMessage(finalEmail);
                System.out.println(YELLOW + "\nEmail sent!" + RESET);
            }
        } else if (reciever == 2) {
            emailMessage += " -- To: ALL teachers -- Message: " + message;
            String[] finalEmail = { email, emailMessage };
            for (Teacher teacher : manager.getTeachers()) {
                teacher.addMessage(finalEmail);
                System.out.println(YELLOW + "\nEmail sent!" + RESET);
            }
        } else if (reciever == 3) {
            s = new Scanner(System.in);
            System.out.println(YELLOW + "\nTo: " + RESET);
            String studentEmail = s.nextLine();
            for (Student student : manager.getStudents()) {
                if (student.getEmail().equals(studentEmail)) {
                    emailMessage += "To: " + student.getEmail() + " -- Message: " + message;
                    String[] finalEmail = { email, emailMessage };
                    student.addMessage(finalEmail);
                    System.out.println(YELLOW + "\nEmail sent!" + RESET);
                }
            }
        } else if (reciever == 4) {
            s = new Scanner(System.in);
            System.out.println(YELLOW + "\nTo: " + RESET);
            String teacherEmail = s.nextLine();
            for (Teacher teacher : manager.getTeachers()) {
                if (teacher.getEmail().equals(teacherEmail)) {
                    emailMessage += "To: " + teacher.getEmail() + " -- Message: " + message;
                    String[] finalEmail = { email, emailMessage };
                    teacher.addMessage(finalEmail);
                    System.out.println(YELLOW + "\nEmail sent!" + RESET);
                }
            }
        }
        display();
    }

    // an initializer list used to create an array
    public void checkInbox() {
        System.out.println("\nYou have " + inbox.size() + " messages");
        for (String[] message : inbox) {
            System.out.println("\n----------------------------");
            System.out.println(PURPLE + message[1] + RESET);
            Scanner s = new Scanner(System.in);
            System.out.println(YELLOW + "\nReply(y/n): " + RESET);
            if (s.nextLine().equals("y")) {
                String emailMessage = "To: " + message[0] + " -- From: " + email;
                s = new Scanner(System.in);
                System.out.println(YELLOW + "\nMessage: " + RESET);
                String message2 = s.nextLine();
                emailMessage += " -- Message: " + message2;
                String[] finalEmail = { email, emailMessage };
                for (Teacher teacher : manager.getTeachers()) {
                    if (teacher.getEmail().equals(message[0]))
                        teacher.addMessage(finalEmail);
                }
                for (Student student : manager.getStudents()) {
                    if (student.getEmail().equals(message[0])) {
                        student.addMessage(finalEmail);
                    }
                }
            }
        }
        display();
    }

    public void logout() {
        Scanner s = new Scanner(System.in);
        System.out.println(YELLOW + "\nYou have successfully logged out.\nEnter 1 to login: " + RESET);
        if (s.nextInt() == 1) {
            Main.start();
        }
    }

    public String getEmail() {
        return email;
    }

    public void getManager(Manager manager) {
        this.manager = manager;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addClass(Class class1) {
        classes.add(class1);
    }

    public String toString() {
        return "\n" + CYAN + "Admin Dashboard\nName: " + name + "\nEmail: " + email + "\nUsername: " + username
                + RESET;
    }

}