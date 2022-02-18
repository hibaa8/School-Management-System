import java.lang.reflect.Array;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Student {
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
    private String prefect;
    private int osis;
    private String username;
    private String password;
    private String email;
    private int grade;
    private String major;
    private HashMap<String, Double> english = new HashMap<String, Double>();
    private HashMap<String, Double> math = new HashMap<String, Double>();
    private HashMap<String, Double> computerScience = new HashMap<String, Double>();
    private HashMap<String, Double> science = new HashMap<String, Double>();
    private HashMap<Class, Double> reportCard = new HashMap<Class, Double>();
    private ArrayList<HashMap<String, Double>> possibleClasses = new ArrayList<>(
            Arrays.asList(english, math, computerScience, science));
    private HashMap<Class, Double> grades = new HashMap<Class, Double>();
    private ArrayList<Class> classes = new ArrayList<>();
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private ArrayList<String[]> inbox = new ArrayList<>();

    public Student(String username, String password) {
        this.username = username;
        this.password = password;
        english.put("english", 0.0);
        math.put("math", 0.0);
        computerScience.put("computer science", 0.0);
        science.put("science", 0.0);
    }

    public Student(String username, String password, String name, String prefect, int osis, String email, int grade,
            String major) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.prefect = prefect;
        this.osis = osis;
        this.email = email;
        this.grade = grade;
        this.major = major;
        english.put("english", 0.0);
        math.put("math", 0.0);
        computerScience.put("computer science", 0.0);
        science.put("science", 0.0);
    }

    public void addClass(Class class1) {
        classes.add(class1);
        grades.put(class1, 0.0);

    }

    public double getGrade(Class class1) {
        for (Class classInList : grades.keySet()) {
            if (classInList.getName().equals(class1.getName())) {
                return grades.get(classInList);
            }
        }
        return -1.0;
    }

    public void addGrade(Class class1, double grade) {
        double finalGrade;
        if (class1.getAssignments().size() == 0) {
            finalGrade = (100 + grade) / 2;
        } else {
            finalGrade = (grade + grades.get(class1) / 2);
        }
        grades.put(class1, finalGrade);
    }

    public void setGrade(Class class1, double grade) {
        for (Class classInList : grades.keySet()) {
            if (classInList.getName().equals(class1.getName())) {
                grades.replace(classInList, grade);
            }
        }
    }

    public String toString() {
        return "\nStudent Dashboard\n" + CYAN + "Name: " + name + "\nGrade: " + grade + "\nMajor: " + major
                + "\nOSIS: "
                + osis
                + "\nPrefect: "
                + prefect
                + "\nEmail: " +
                email + "\nUsername: " + username + RESET + "\n";
    }

    public void display() {
        System.out.println("\nClasses");
        for (Class class1 : this.getClasses()) {
            String output = GREEN + "Class: " + class1.getName() + " -- Teacher: " + RESET;
            if (class1.getTeacher() == null) {
                output += RED + " teacher not found " + RESET;
            } else {
                output += GREEN + class1.getTeacher().getName() + RESET;
            }
            output += GREEN + " -- Students: " + class1.getStudents().size() + " -- Assignments: "
                    + class1.getAssignments().size() + "\n" + RESET;
            System.out.println(output);
        }
        Scanner s = new Scanner(System.in);
        System.out
                .println("1-view class\n2-send email to teacher\n3-progress tracker\n4-view report card\n" +
                        "5-Check inbox\n6-log out\n\n" + YELLOW + "Choose an option: " + RESET);

        String option = s.nextLine();
        while (!(Arrays.asList("1", "2", "3", "4", "5", "6").contains(option))) {
            System.out.println(RED + "\nInvalid input. Try again");
            s = new Scanner(System.in);
            System.out.println(RED + "\nEnter a number from 1-7: " + RESET);
            option = s.nextLine();
        }
        int optionInt = Integer.valueOf(option);

        if (optionInt == 1) {
            viewClass();
        } else if (optionInt == 2) {
            sendEmail();
        } else if (optionInt == 3) {
            int gradYear = Year.now().getValue() + 12 - grade;
            boolean result = progressTracker();
            if (result) {
                System.out.println("You are on track to graduate in " + gradYear);
            } else if (!result) {
                System.out.println("You are not on track to graduate in " + gradYear);
            }
            display();
        } else if (optionInt == 4) {
            viewReportCard();
        } else if (optionInt == 5) {
            checkInbox();
        } else if (optionInt == 6) {
            logout();
        }

    }

    public void viewClass() {
        System.out.println("\nDate Assigned | Due | Weight | Details |\n");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println(i + "-" + classes.get(i).getName());
        }
        Scanner s1 = new Scanner(System.in);
        System.out.print(YELLOW + "\nChoose a class: " + RESET);
        int input = s1.nextInt();
        Class class1 = classes.get(input);
        if (classes.get(input).getName().equals("english")) {
            System.out.println("\nEnglish Class");
            for (String assignment : english.keySet()) {
                if (assignment.equals("english")) {
                    continue;
                } else {
                    String output = assignment + " -- " + getColor(english.get(assignment));
                    System.out.println(output);
                }
            }
        } else if (classes.get(input).getName().equals("science")) {
            System.out.println("\nScience Class");
            for (String assignment : science.keySet()) {
                if (assignment.equals("science")) {
                    continue;
                } else {
                    String output = assignment + " -- " + getColor(science.get(assignment));
                    System.out.println(output);
                }
            }
        } else if (classes.get(input).getName().equals("math")) {
            System.out.println("\nMath Class");
            for (String assignment : math.keySet()) {
                if (assignment.equals("math")) {
                    continue;
                } else {
                    String output = assignment + " -- " + getColor(math.get(assignment));
                    System.out.println(output);
                }
            }
        } else if (classes.get(input).getName().equals("computer science")) {
            System.out.println("\nComputer Science");
            for (String assignment : computerScience.keySet()) {
                if (assignment.equals("computer science")) {
                    continue;
                } else {
                    String output = assignment + " -- " + getColor(computerScience.get(assignment));
                    System.out.println(output);
                }
            }
        }
        System.out.println("Overall Grade: " + getColor(grades.get(class1)));
        display();
    }

    public void sendEmail() {
        System.out.println("\n");
        for (Teacher teacher : teachers) {
            System.out.println("Teacher: " + teacher.getName() + " Email: " + teacher.getEmail());
        }
        Scanner s = new Scanner(System.in);
        System.out.println("\nTo: ");
        String reciever = s.nextLine();
        boolean emailFound = false;
        for (Teacher teacher : teachers) {
            if (teacher.getEmail().equals(reciever)) {
                emailFound = true;
            }
        }
        while (!emailFound) {
            System.out.println(RED + "Email not found. Try again" + RESET);
            Scanner s1 = new Scanner(System.in);
            System.out.println("\nTo: ");
            reciever = s1.nextLine();
            emailFound = false;
            for (Teacher teacher : teachers) {
                if (teacher.getEmail().equals(reciever)) {
                    emailFound = true;
                }
            }
        }
        s = new Scanner(System.in);
        System.out.println("\nMessage: ");
        String message = s.nextLine();
        String emailMessage = "To: " + reciever + " -- From: " + email + " -- Message: " + message;
        for (Teacher teacher1 : teachers) {
            if (reciever.equalsIgnoreCase(teacher1.getEmail())) {
                String[] finalEmail = { email, emailMessage };
                teacher1.addMessage(finalEmail);
            }
            System.out.println(YELLOW + "Email sent!" + RESET);
            display();
        }
    }

    public ArrayList<HashMap<String, Double>> getPossibleClasses() {
        return possibleClasses;
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

    // used an array of primitive data and traversed it. Then I found the minimum
    // maximum values in the array
    public boolean progressTracker() {
        int failing = 0;

        System.out.println("Subject  |  Grade  |  Status  |");
        for (Class class1 : grades.keySet()) {
            String output = "";
            output += class1.getName() + "  " + grades.get(class1);
            if (grades.get(class1) >= 65) {
                output += GREEN + "  Passing" + RESET;

            } else {
                output += RED + "  Failing" + RESET;
            }
            System.out.println(output);
        }

        double[] onlyGrades = new double[grades.keySet().size()];
        int i = 0;
        for (Class class1 : grades.keySet()) {
            onlyGrades[i] = grades.get(class1);
            i++;
        }
        i = 0;
        double min = onlyGrades[0];
        double max = onlyGrades[0];
        while (i < onlyGrades.length) {
            if (onlyGrades[i] < 65) {
                failing++;
            }
            if (onlyGrades[i] < min) {
                min = onlyGrades[i];
            } else if (onlyGrades[i] > max) {
                max = onlyGrades[i];
            }
            i++;
        }
        System.out.println("Minimum grade: " + getColor(min));
        System.out.println("Maximum grade: " + getColor(max));

        if (failing > 0) {
            return false;
        } else {
            return true;
        }

    }

    private void viewReportCard() {
        System.out.println("\n");
        int classes = 0;
        double total = 0;
        if (reportCard.isEmpty()) {
            System.out.println(RED + "Report card not avaliable" + RESET);
        } else {
            System.out.println("Subject  |  Grade");
            for (Class class1 : reportCard.keySet()) {
                System.out.println(class1.getName() + "  " + getColor(reportCard.get(class1)));
                total += reportCard.get(class1);
                classes++;
            }
        }
        double average = total / classes;
        System.out.println("Overall average: " + getColor(average));
        display();
    }

    public void checkInbox() {
        System.out.println("\n");
        System.out.println("You have " + inbox.size() + " messages");
        for (String[] message : inbox) {
            System.out.println("----------------------------");
            System.out.println(PURPLE + message[1] + RESET);
            Scanner s = new Scanner(System.in);
            System.out.println("\nReply(y/n): ");
            if (s.nextLine().equals("y")) {
                String emailMessage = "\nTo: " + message[0] + " -- From: " + email;
                s = new Scanner(System.in);
                System.out.println("\nMessage: ");
                String message2 = s.nextLine();
                emailMessage += " -- Message: " + message2;
                // uses an initializer list to create an array
                String[] finalEmail = { email, emailMessage };
                for (Teacher teacher : teachers) {
                    if (teacher.getEmail().equals(message[0]))
                        teacher.addMessage(finalEmail);
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

    public void logout() {
        System.out.println("\n");
        Scanner s = new Scanner(System.in);
        System.out.println(YELLOW + "You have successfully logged out.\nEnter 1 to login: " + RESET);
        if (s.nextInt() == 1) {
            Main.start();
        }
    }

    public HashMap<Class, Double> getReportCard() {
        return reportCard;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMessage(String[] message) {
        inbox.add(message);
    }

    public String getName() {
        return name;
    }

    public void addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
    }

    public void setGradeLevel(int grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setPrefect(String prefect) {
        this.prefect = prefect;
    }

    public void setOsis(int osis) {
        this.osis = osis;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}