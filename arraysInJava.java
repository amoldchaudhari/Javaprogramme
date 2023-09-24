import java.util.Scanner;

public class arraysInJava {
  public static void main(String[] args){
     int studentId[] = new int[3]; 
    Scanner sc = new Scanner(System.in);
   for( int i = 0 ; i < studentId.length; i++) {
    System.out.println("can you enter your Student ID");
    studentId[i] = sc.nextInt();
    }

    for( int i = 0 ; i < studentId.length; i++) {
    System.out.println("Student ID " + studentId[i]);
    
     
   
    }





  }
}