import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Teacher {
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
    private String subject;
    private int room;
    private String email;
    private String username;
    private String password;
    private ArrayList<Class> classes = new ArrayList<Class>();
    private ArrayList<Student> students = new ArrayList<>();
    private Admin supervisor;
    public ArrayList<String[]> inbox = new ArrayList<>();
    String lastActive;

    public Teacher(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Teacher(String name, String username, String password, String subject, int roomNum, String email,
            Admin supervisor) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.subject = subject;
        this.room = roomNum;
        this.email = email;
        this.supervisor = supervisor;
    }

    public String toString() {
        return "\nTeacher  Dashboard\n" + CYAN + "Name: " + name + "\nSubject: " + subject + "\nEmail: " + email
                + "\nRoom: " + room +
                "\nUsername: " + username + RESET;
    }

    public void display() {
        System.out.println("\nClasses");
        for (Class class1 : classes) {
            System.out.println(
                    CYAN + class1.getName() + "-- Students: " + class1.getStudents().size() + " -- Assignments: "
                            + class1.getAssignments().size() + "\n" + RESET);
        }
        Scanner s = new Scanner(System.in);
        System.out.println(
                "1-add assignment\n2-remove assignment\n3-grade asssignment\n4-finalize grades\n5-send email\n" +
                        "6-check inbox\n7-log out\n\n" + YELLOW + "Choose an option: " + RESET);

        String option = s.nextLine();
        while (!(Arrays.asList("1", "2", "3", "4", "5", "6", "7").contains(option))) {
            System.out.println(RED + "\nInvalid input. Try again");
            s = new Scanner(System.in);
            System.out.println(RED + "\nEnter a number from 1-7: " + RESET);
            option = s.nextLine();
        }
        int optionInt = Integer.valueOf(option);

        int class1 = -1;
        if (optionInt != 5 && optionInt != 6 && optionInt != 7) {
            s = new Scanner(System.in);
            System.out.println("\nCurrent classes: ");
            for (int i = 0; i < classes.size(); i++) {
                System.out.println(CYAN + "\n" + i + " - " + classes.get(i).getName());
            }
            System.out.println(YELLOW + "\nChoose a class: " + RESET);
            class1 = s.nextInt();
        }

        if (optionInt == 1) {
            addAssignment(classes.get(class1));
        } else if (optionInt == 2) {
            removeAssignment(classes.get(class1));
        } else if (optionInt == 3) {
            gradeAssignment(classes.get(class1));
        } else if (optionInt == 4) {
            finalizeGrades(classes.get(class1));
        } else if (optionInt == 5) {
            sendEmail();
        } else if (optionInt == 6) {
            checkInbox();
        } else if (optionInt == 7) {
            logOut();
        }

    }

    public String currentAssignments(Class class1) {
        String str = "\nCurrent Assignments: \n";

        ArrayList<String> assignments = class1.getAssignments();
        if (assignments.size() == 0) {
            str += RED + "\nNo current assignments" + RESET;
        } else {
            str += "\nDate Assigned | Due | Weight | Details |\n";
            for (int i = 0; i < assignments.size(); i++) {
                str += PURPLE + i + "- " + assignments.get(i) + "\n" + RESET;
            }
        }
        return str;
    }

    public void addAssignment(Class class1) {
        System.out.println(currentAssignments(class1));
        ArrayList<String> assignments = class1.getAssignments();
        String newAssignment = "";
        Scanner s = new Scanner(System.in);
        System.out.println(YELLOW + "\nDate assigned: " + RESET);
        String dateAssigned = s.nextLine();
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nDate due: " + RESET);
        String due = s.nextLine();
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nWeight: " + RESET);
        double weight = s.nextDouble();
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nDetails: " + RESET);
        String details = s.nextLine();
        newAssignment = dateAssigned + "  " + due + "  " + weight + "  " + details;
        assignments.add(newAssignment);
        class1.setAssignments(assignments);
        for (int i = 0; i < assignments.size(); i++) {
            System.out.println("\nDate Assigned | Due | Weight | Details |");
            System.out.println(i + "- " + assignments.get(i));
        }
        display();
    }

    public void removeAssignment(Class class1) {
        ArrayList<String> assignments = class1.getAssignments();
        if (assignments.size() > 0) {
            System.out.println(currentAssignments(class1));
            Scanner s = new Scanner(System.in);
            System.out.println(YELLOW + "Which assignment would you like to remove: " + RESET);
            String assignment = assignments.get(s.nextInt());
            class1.getAssignments().remove(assignment);
            assignments = class1.getAssignments();
            for (int i = 0; i < assignments.size(); i++) {
                System.out.println(i + "- " + assignments.get(i));
            }
        } else {
            System.out.println(RED + "\nThere are no assignments here to remove" + RESET);
        }
        display();
    }

    public String getColor(double grade) {
        if (grade < 65) {
            return RED + grade + RESET;
        } else if (grade > 65 && grade < 80) {
            return YELLOW + grade + RESET;
        } else {
            return GREEN + grade + RESET;
        }
    }

    public void gradeAssignment(Class class1) {
        System.out.println(currentAssignments(class1));
        ArrayList<String> assignments = class1.getAssignments();
        if (assignments.size() > 0) {
            Scanner s = new Scanner(System.in);
            System.out.println(YELLOW + "\nWhat assignment would you like to grade: " + RESET);
            String output = "";
            int assignmentIndex = s.nextInt();
            String assignment = class1.getAssignments().get(assignmentIndex);
            output += assignment;
            System.out.println(YELLOW + "\nWeight: " + RESET);
            double weight = s.nextDouble();
            for (Student student : students) {
                s = new Scanner(System.in);
                System.out.println(YELLOW + "\n" + student.getName() + ": " + RESET);
                double grade = weight * s.nextDouble();
                for (HashMap class2 : student.getPossibleClasses()) {
                    if (class1.getName().equals(class2.keySet().iterator().next())) {
                        class2.put(assignment, grade);
                    }
                }
                student.addGrade(class1, grade);
                output += "\n" + student.getName() + ": " + getColor(Math.round(grade * 100) / 100.0) + "\n";
            }
            System.out.println(output);
            class1.getAssignments().set(assignmentIndex, output);
        } else {
            System.out.println(RED + "\nThere are no assignments to grade" + RESET);
        }

        display();

    }

    private void finalizeGrades(Class class1) {
        double grade = 0;
        System.out.println(class1.getName());
        if (class1.getStudents().size() == 0) {
            System.out.println(RED + "\nNo students in this class" + RESET);
        }
        for (Student student : class1.getStudents()) {
            double grade1 = Math.round(student.getGrade(class1) * 100) / 100.0;
            System.out.println("\n" + student.getName() + ": " + getColor(grade1));
            Scanner s = new Scanner(System.in);
            System.out.println(YELLOW + "\nAP course(y/n): " + RESET);
            String input = s.nextLine().toLowerCase();
            if (input.equals("y")) {
                // Math.round(a * 100.0) / 100.0;
                grade1 *= 1.1;
            } else {
                grade1 = student.getGrade(class1);
            }
            System.out.println("\nCurrent grade: " + getColor(grade1));
            s = new Scanner(System.in);
            System.out.println(YELLOW + "\n1-round up\n2-round down" + RESET);
            int choice = s.nextInt();
            if (choice == 1) {
                grade1 = Math.ceil(grade1);
            } else if (choice == 2) {
                grade1 = Math.floor(grade1);
            }
            student.setGrade(class1, grade1);
            System.out.println("\n" + student.getName() + ": " + getColor(student.getGrade(class1)));
            student.getReportCard().put(class1, grade1);
        }
        display();
    }

    public boolean checkEmail(String emailAdd) {
        boolean emailFound = false;
        for (Student student : students) {
            if (student.getEmail().equalsIgnoreCase(emailAdd)) {
                emailFound = true;
            }
        }
        for (Admin admin : Manager.getAdmins()) {
            if (admin.getEmail().equalsIgnoreCase(emailAdd)) {
                emailFound = true;
            }
        }
        return emailFound;
    }

    public void sendEmail() {
        String emailMessage = "From: " + getEmail();
        System.out.println("\nStudent Emails: ");
        for (Student student : students) {
            System.out.println("Student: " + student.getEmail());
        }
        System.out.println("\nAdmin emails: ");
        for (Admin admin : Manager.getAdmins()) {
            System.out.println("Admin: " + admin.getEmail());
        }
        Scanner s = new Scanner(System.in);
        System.out.println(YELLOW + "\nTo: " + RESET);
        String emailAdd = s.nextLine();
        boolean emailFound = checkEmail(emailAdd);
        while (!emailFound) {
            System.out.println(RED + "\nEmail not found. Try again.");
            Scanner s1 = new Scanner(System.in);
            System.out.println(YELLOW + "\nTo: " + RESET);
            emailAdd = s1.nextLine();
            emailFound = checkEmail(emailAdd);
        }
        s = new Scanner(System.in);
        System.out.println(YELLOW + "\nMessage: " + RESET);
        String message = s.nextLine();
        emailMessage += " To: " + emailAdd + " Message: " + message;
        String[] finalEmail = { email, emailMessage };
        for (Student student : students) {
            if (student.getEmail().equalsIgnoreCase(emailAdd)) {
                student.addMessage(finalEmail);
            }
        }
        for (Admin admin : Manager.getAdmins()) {
            if (admin.getEmail().equalsIgnoreCase(emailAdd)) {
                admin.addMessage(finalEmail);
            }
        }
        System.out.println(YELLOW + "\nEmail sent!\n" + RESET);
        display();
    }

    public void checkInbox() {
        System.out.println("\nYou have " + inbox.size() + " messages");
        for (String[] message : inbox) {
            System.out.println("\n----------------------------");
            System.out.println(PURPLE + message[1] + RESET);
            Scanner s = new Scanner(System.in);
            System.out.println(YELLOW + "\nReply(y/n): ");
            if (s.nextLine().equals("y")) {
                String emailMessage = "To: " + message[0] + " -- From: " + email;
                s = new Scanner(System.in);
                System.out.println(YELLOW + "\nMessage: " + RESET);
                String message2 = s.nextLine();
                emailMessage += " -- Message: " + message2;
                String[] finalEmail = { email, emailMessage };
                for (Student student : students) {
                    if (student.getEmail().equals(message[0]))
                        student.addMessage(finalEmail);
                }
                for (Admin admin : Manager.getAdmins()) {
                    if (admin.getEmail().equals(message[0])) {
                        admin.addMessage(finalEmail);
                    }
                }
            }
        }
        display();
    }

    public void logOut() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        lastActive = dtf.format(LocalDateTime.now());
        Scanner s = new Scanner(System.in);
        System.out.println(YELLOW + "\nYou have successfully logged out.\nEnter 1 to login: " + RESET);
        if (s.nextInt() == 1) {
            Main.start();
        }

    }

    public String getLastActive() {
        return lastActive;
    }

    public void addMessage(String[] message) {
        inbox.add(message);
    }

    public String getSubject() {
        return subject;
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSupervisor(Admin supervisor) {
        this.supervisor = supervisor;
    }

    public Admin getSupervisor() {
        return supervisor;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addClass(Class class1) {
        classes.add(class1);
    }
}