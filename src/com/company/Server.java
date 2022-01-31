package com.company;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 * The Server class is a logic container of quiz flow
 */
public class Server {
    public static Queue<Question> queueOfQuestions = new LinkedList<>();
    static Vector<ClientHandler> ar = new Vector<>();
    static Vector<Thread> threads = new Vector<>();
    static int numberOfPlayers = 0;
    static int numberOfWrongAnswers = 0;
    static String questionText;
    static String correctAnswer;
    static boolean gameInProgress = false;
    static boolean sendQuestions = true;

    public static void setQuestions(List<Question> listOfQuestions, Queue<Question> queueOfQuestions) throws FileNotFoundException {
        String line, answer = null;
        StringBuilder text = new StringBuilder();
        String[] separated;
        File questions = new File("src/com/company/questions.txt");
        Scanner sc = new Scanner(questions);
        Question question = new Question();

        while (sc.hasNextLine()) {
            line = sc.nextLine();
            separated = line.split("=");

            if (separated.length == 2) {
                text.append(separated[0]).append('\n');
                answer = separated[1];
            } else if(line.equals("")) {
                question.setText(text.toString());
                question.setAnswer(answer);
                text = new StringBuilder();
                answer = null;
                listOfQuestions.add(question);
                question = new Question();
            } else {
                text.append(separated[0]).append('\n');
            }
        }
        randomizingQuestions(listOfQuestions, queueOfQuestions);
    }

    public static void randomizingQuestions(List<Question> listOfQuestions, Queue<Question> queueOfQuestions) {
        Collections.shuffle(listOfQuestions);

        for (int i = 0; i< 10; ++i) {
            queueOfQuestions.add(listOfQuestions.get(i));
        }
    }

    public static void printResults() {
        try {
            for (ClientHandler cli : Server.ar) {
                cli.dos.writeUTF("Results:");
                for (ClientHandler cli2 : Server.ar) {
                    String s = String.valueOf(cli2.getName() + ": " + cli2.getNumberOfPoints());
                    cli.dos.writeUTF(s);
                }
                cli.dos.writeUTF(getWinner()+" win");
            }
        } catch (IOException e) {
            System.err.println("missing user");
        }
    }

    public static String getWinner(){
        int x;
        int max=0;
        String winner="";
        for (ClientHandler cli : Server.ar) {
            x = cli.getNumberOfPoints();
            if (x > max) {
                winner = cli.getName();
                max = x;
            }
        }
       return winner;

    }


    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(1234);
        Socket s;
        List<Question> listOfQuestions = new ArrayList<>();
        setQuestions(listOfQuestions, queueOfQuestions);

        while (true) {
           try {
               s = ss.accept();
               numberOfPlayers++;
               System.err.println("New client request received : " + s);

               DataInputStream dis = new DataInputStream(s.getInputStream());
               DataOutputStream dos = new DataOutputStream(s.getOutputStream());

               if (numberOfPlayers <= 4 & !gameInProgress) {
                   System.err.println("Creating a new handler for player " + numberOfPlayers);
                   ClientHandler player = new ClientHandler(s, "Player " + numberOfPlayers, dis, dos);
                   System.err.println("Adding player " + numberOfPlayers + " to active client list");
                   ar.add(player);
                   Thread t = new Thread(player);
                   threads.add(t);
                   t.start();
                   dos.writeUTF("WELCOME!\nYour nick is Player "+ numberOfPlayers);

                   if (numberOfPlayers == 1) {
                       dos.writeUTF("You are admin");
                       dos.writeUTF("Type 'Start' to start the game.");
                   }

               } else if(gameInProgress) {
                   dos.writeUTF("Game in progress. Try again later.");
                   throw new Exception("gameInProgress");
               } else {
                   dos.writeUTF("No place in lobby. Try again later.");
                   throw new Exception("noPlaceInLobby");
               }
           } catch (Exception e) {
               if(e.equals("gameInProgress"))
                   System.out.println("Game in progress. Canceling connection ");
               if(e.equals("noPlaceInLobby"))
                   System.out.println("No place in lobby. Canceling connection ");
           }
        }
    }
}

