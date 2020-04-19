package FileSystem;

import java.util.Scanner;

public class My_System {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Clasa_Serviciu program = new Clasa_Serviciu();
        while (true) {
            System.out.println("Alege o varianta\n" +
                    "1)Adauga un user\n" +
                    "2)Adauga un grup\n" +
                    "3)Muta userul in alt grup\n" +
                    "4)Creeaza un director\n" +
                    "5)Creeaza un fisier\n" +
                    "6)Arata toti userii\n" +
                    "7)Arata toate grupurile\n" +
                    "8)Arata fisierele\n" +
                    "9)Sterge userul\n" +
                    "10)Sterge grupul\n" +
                    "11)Sterge directorul\n" +
                    "0)Stop");
            int x = scanner.nextInt();
            String nume_user,nume_grup, nume_fisier, nume_director;
            switch (x){
                case 1:
                    System.out.println("Numele:");
                    nume_user = scanner.next();
                    program.addUser(nume_user);
                    break;
                case 2:
                    System.out.println("Numele:");
                    nume_user = scanner.next();
                    program.addGroup(nume_user);
                    break;
                case 3:
                    System.out.println("Userul si grupul:");
                    nume_user = scanner.next();
                    nume_grup = scanner.next();
                    program.moveUser(nume_user, nume_grup);
                    break;
                case 4:
                    System.out.println("Numele si locatia:");
                    nume_fisier = scanner.next();
                    nume_director = scanner.next();
                    program.createDir(nume_fisier, nume_director);
                    break;
                case 5:
                    System.out.println("Numele si locatia:");
                    nume_fisier = scanner.next();
                    nume_director = scanner.next();
                    program.createFile(nume_fisier, nume_director);
                    break;
                case 6:
                    program.showUsers();
                    break;
                case 7:
                    program.showGroups();
                    break;
                case 8:
                    program.showFiles();
                    break;
                case 9:
                    System.out.println("Numele:");
                    nume_user = scanner.next();
                    program.deleteUser(nume_user);
                    break;
                case 10:
                    System.out.println("Numele:");
                    nume_grup = scanner.next();
                    program.deleteGroup(nume_grup);
                    break;
                case 11:
                    System.out.println("Numele:");
                    nume_director = scanner.next();
                    program.deleteDir(nume_director);
                    break;
                case 0:
                    program.finalizare();
                    break;
                default:
                    System.out.println("Try again");
                    break;
            }
            if (x == 0)
                break;
        }
//        // asta e doar o demonstratie
//        program.creare_stergere();
    }
}
