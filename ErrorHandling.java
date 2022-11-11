import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ErrorHandling {
    public static void main(String[] args) throws InterruptedException {

        int rightAnswersGiven = 0;
        int wrongAnswersGiven;
        int answerGiven;
        String resultOfTesting;
        Question[] questions = new Question[3];
        Answer[][] answers = new Answer[3][5];

        try {
            readQuestionsAndAnswersFromFile(questions, answers);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        for (int i=0; i<questions.length; i++) {

            questions[i].print();
            for (int j=0; j<answers[i].length; j++) {
                if (answers[i][j] != null) {
                    answers[i][j].print();
                }
            }
            System.out.println();

            answerGiven = getAnswerFromUser();
            if (answerGiven == questions[i].getRightAnswer()) {
                rightAnswersGiven++;
                System.out.println("Правильно");
            } else {
                System.out.println("Неправильно");
            }
            System.out.println();

        }

        wrongAnswersGiven = questions.length - rightAnswersGiven;
        resultOfTesting = "Результат: правильно " + rightAnswersGiven + ", неправильно " + wrongAnswersGiven + ".";

        System.out.println(resultOfTesting);

        try {
            writeResultToFile(resultOfTesting);
        } catch (IOException e) {
            TimeUnit.SECONDS.sleep(10);

            try {
                writeResultToFile(resultOfTesting);
            } catch (IOException e1) {
                System.out.println("Не удалось записать в файл!");
                System.out.println(e1.getMessage());
                return;
            }
        }

        System.out.println("Результаты тестирования успешно записаны в файл!");

    }

    private static int getAnswerFromUser() {
        Scanner scanner = new Scanner(System.in);
        int answerGiven = 0;
        boolean success = false;

        while (!success) {
            try {
                answerGiven = Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Нужно ввести число!");
                continue;
            }
            success = true;
        }

        return answerGiven;
    }
    private static void readQuestionsAndAnswersFromFile(Question[] questions, Answer[][] answers)
            throws IOException {

        int rightAnswer;
        int numberAnswer;
        String answerText;
        String questionText;
        int currentQuestionIndex = -1;
        Question currentQuestion = null;

        try (FileInputStream fis = new FileInputStream("C:\\Java\\Java Developer. Basic\\ДЗ 7\\Вопросы.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));) {

            String fileLine = br.readLine();
            while (fileLine != null) {

                if (!fileLine.isEmpty()) {
                    if (currentQuestion == null) {
                        questionText = fileLine.substring(2);
                        rightAnswer = Integer.parseInt(fileLine.substring(0, 1));
                        currentQuestion = new Question(questionText, rightAnswer);

                        currentQuestionIndex++;
                        questions[currentQuestionIndex] = currentQuestion;
                    } else {
                        numberAnswer = Integer.parseInt(fileLine.substring(0, 1));
                        answers[currentQuestionIndex][numberAnswer - 1] = new Answer(fileLine, numberAnswer);
                    }
                } else {
                    currentQuestion = null;
                }
                fileLine = br.readLine();

            }

        } catch (IOException e) {
            throw e;
        }

    }

    private static void writeResultToFile(String result) throws IOException {

        try (FileOutputStream fos = new FileOutputStream("D:\\Java\\Java Developer. Basic\\ДЗ 7\\" +
                "Результат тестирования.txt");
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));) {

            //bw.newLine();
            bw.write(result);

        } catch (IOException e) {
            throw e;
        }

    }

}

class Question {
    private String text;
    private int rightAnswer;

    Question(String text, int rightAnswer) {
        this.text = text;
        this.rightAnswer = rightAnswer;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void print() {
        System.out.println(text);
    }
}

class Answer {
    private String text;
    private int number;

    Answer(String text, int number) {
        this.text = text;
        this.number = number;
    }

    public void print() {
        System.out.println(text);
    }
}